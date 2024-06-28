/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.lsp.java.compiler;

import static com.itsaky.androidide.javac.config.JavacConfigProvider.PROP_ANDROIDIDE_JAVA_HOME;
import static com.itsaky.androidide.javac.config.JavacConfigProvider.disableModules;
import static com.itsaky.androidide.javac.config.JavacConfigProvider.enableModules;
import static com.itsaky.androidide.javac.config.JavacConfigProvider.setLatestSourceVersion;
import static com.itsaky.androidide.javac.config.JavacConfigProvider.setLatestSupportedSourceVersion;
import static com.itsaky.androidide.utils.Environment.JAVA_HOME;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import com.itsaky.androidide.builder.model.IJavaCompilerSettings;
import com.itsaky.androidide.javac.services.compiler.ReusableBorrow;
import com.itsaky.androidide.javac.services.partial.DiagnosticListenerImpl;
import com.itsaky.androidide.lsp.java.models.CompilationRequest;
import com.itsaky.androidide.lsp.java.visitors.MethodRangeScanner;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.ModuleProject;
import com.itsaky.androidide.projects.util.StringSearch;
import com.itsaky.androidide.tooling.api.ProjectType;
import com.itsaky.androidide.utils.ClassTrie;
import com.itsaky.androidide.utils.SourceClassTrie;
import com.itsaky.androidide.utils.StopWatch;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import jdkx.lang.model.SourceVersion;
import jdkx.tools.Diagnostic;
import jdkx.tools.JavaFileObject;
import openjdk.source.tree.CompilationUnitTree;
import openjdk.source.util.TreePath;
import openjdk.tools.javac.api.ClientCodeWrapper;
import openjdk.tools.javac.api.JavacTaskImpl;
import openjdk.tools.javac.code.Kinds;
import openjdk.tools.javac.util.JCDiagnostic;

public class CompileBatch implements AutoCloseable {

  public static final String DEFAULT_COMPILER_SOURCE_AND_TARGET_VERSION = "11";
  protected final JavaCompilerService parent;
  protected final ReusableBorrow borrow;
  protected final JavacTaskImpl task;
  protected final List<CompilationUnitTree> roots;
  protected final Map<String, List<Pair<Range, TreePath>>> methodPositions = new HashMap<>();
  protected DiagnosticListenerImpl diagnosticListener;
  /** Indicates the task that requested the compilation is finished with it. */
  boolean closed;

  CompileBatch(
    JavaCompilerService parent,
    Collection<? extends JavaFileObject> files,
    CompilationRequest compilationRequest) {
    this.parent = parent;
    this.borrow = batchTask(parent, files);
    this.task = borrow.task;
    this.roots = new ArrayList<>();
  
    final var context = task.getContext();
    final var config = JavaCompilerConfig.instance(context);
    config.setFiles(files);
    
    if (compilationRequest.configureContext != null) {
      compilationRequest.configureContext.accept(context);
    }
  
    Objects.requireNonNull(compilationRequest, "A task processor is required");

    try {
      compilationRequest.compilationTaskProcessor.process(borrow.task, this::processCompilationUnit);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    
    config.setFiles(null);
  }

  private void processCompilationUnit(final CompilationUnitTree root) {
    roots.add(root);
//    updatePositions(root, false);
  }

  void updatePositions(CompilationUnitTree tree, boolean allowDuplicate) {
    final StopWatch watch = new StopWatch("Scan method positions");
    final List<Pair<Range, TreePath>> positions = new ArrayList<>();
    new MethodRangeScanner(this.task).scan(tree, positions);
    final String path = new File(tree.getSourceFile().toUri()).getAbsolutePath();
    final List<Pair<Range, TreePath>> old = this.methodPositions.put(path, positions);
    if (old != null && !allowDuplicate) {
      throw new IllegalStateException(
          "Duplicate CompilationUnitTree for file:" + tree.getSourceFile().toUri());
    }

    watch.log();
  }

  private ReusableBorrow batchTask(
      @NonNull JavaCompilerService parent, @NonNull Collection<? extends JavaFileObject> sources) {

    parent.diagnostics.clear();
    final Iterable<String> options = options();

    diagnosticListener =
        new DiagnosticListenerWrapper(parent.diagnostics::add, sources.iterator().next());

    final ReusableBorrow borrow =
        parent.compiler.getTask(
            parent.fileManager, diagnosticListener, options, Collections.emptyList(), sources);

    if (parent.fileManager != null) {
      parent.fileManager.setContext(borrow.task.getContext());
    }

    return borrow;
  }

  @NonNull
  private List<String> options() {
    List<String> options = new ArrayList<>();

    // This won't be used if the current module is Android module project
    System.setProperty(PROP_ANDROIDIDE_JAVA_HOME, JAVA_HOME.getAbsolutePath());
    if (this.parent.module != null && this.parent.module.getType() == ProjectType.Android) {
      setLatestSourceVersion(SourceVersion.RELEASE_8);
      setLatestSupportedSourceVersion(SourceVersion.RELEASE_11);
      disableModules();
    } else {
      setLatestSourceVersion(SourceVersion.RELEASE_11);
      setLatestSupportedSourceVersion(SourceVersion.RELEASE_11);
      enableModules();
    }

    setupCompileOptions(parent.module, options);
    Collections.addAll(options, "-proc:none", "-g");

    Collections.addAll(
        options,
        "-XDcompilePolicy=byfile",
        "-XD-Xprefer=source",
        "-XDide",
        "-XDkeepCommentsOverride=ignore",
        "-XDsuppressAbortOnBadClassFile",
        "-XDshould-stop.at=GENERATE",
        "-XDdiags.formatterOptions=-source",
        "-XDdiags.layout=%L%m|%L%m|%L%m",
        "-XDbreakDocCommentParsingOnError=false",
        "-Xlint:cast",
        "-Xlint:deprecation",
        "-Xlint:empty",
        "-Xlint:fallthrough",
        "-Xlint:finally",
        "-Xlint:path",
        "-Xlint:unchecked",
        "-Xlint:varargs",
        "-Xlint:static");

    return options;
  }

  protected void setupCompileOptions(final ModuleProject module, final List<String> options) {
    if (module == null) {
      Collections.addAll(
          options,
          "-source",
          DEFAULT_COMPILER_SOURCE_AND_TARGET_VERSION,
          "-target",
          DEFAULT_COMPILER_SOURCE_AND_TARGET_VERSION);
      return;
    }

    final IJavaCompilerSettings compilerSettings = module.getCompilerSettings();
    options.add("-source");
    options.add(compilerSettings.getJavaSourceVersion());
    options.add("-target");
    options.add(compilerSettings.getJavaBytecodeVersion());
  }

  @Override
  public void close() {
    closed = true;
  }

  /**
   * If the compilation failed because javac didn't find some package-private files in source files
   * with different names, list those source files.
   */
  Set<Path> needsAdditionalSources() {

    if (parent.getModule() == null) {
      return Collections.emptySet();
    }

    // Check for "class not found errors" that refer to package private classes
    Set<Path> addFiles = new HashSet<>();
    for (Diagnostic<? extends JavaFileObject> err : parent.diagnostics) {
      if (!err.getCode().equals("compiler.err.cant.resolve.location")) {
        continue;
      }
      if (!isValidFileRange(err)) {
        continue;
      }
      String packageName = packageName(err);
      ClassTrie.Node node = parent.getModule().compileJavaSourceClasses.findNode(packageName);
      if (node != null && node.isClass() && node instanceof SourceClassTrie.SourceNode) {
        addFiles.add(((SourceClassTrie.SourceNode) node).getFile());
      }
    }
    return addFiles;
  }

  private String packageName(Diagnostic<? extends JavaFileObject> err) {
    if (err instanceof ClientCodeWrapper.DiagnosticSourceUnwrapper) {
      JCDiagnostic diagnostic = ((ClientCodeWrapper.DiagnosticSourceUnwrapper) err).d;
      JCDiagnostic.DiagnosticPosition pos = diagnostic.getDiagnosticPosition();
      Object[] args = diagnostic.getArgs();
      Kinds.KindName kind = (Kinds.KindName) args[0];
      if (kind == Kinds.KindName.CLASS) {
        if (pos.toString().contains(".")) {
          return pos.toString().substring(0, pos.toString().lastIndexOf('.'));
        }
      }
    }
    Path file = Paths.get(err.getSource().toUri());
    return StringSearch.packageName(file);
  }

  private boolean isValidFileRange(Diagnostic<? extends JavaFileObject> d) {
    return d.getSource().toUri().getScheme().equals("file")
        && d.getStartPosition() >= 0
        && d.getEndPosition() >= 0;
  }

  public static class DiagnosticListenerWrapper extends DiagnosticListenerImpl {

    private final Consumer<Diagnostic<? extends JavaFileObject>> consumer;

    public DiagnosticListenerWrapper(
        final Consumer<Diagnostic<? extends JavaFileObject>> consumer, JavaFileObject jfo) {
      super(jfo);
      this.consumer = consumer;
    }

    @Override
    public void report(final Diagnostic<? extends JavaFileObject> diagnostic) {
      consumer.accept(diagnostic);
      super.report(diagnostic);
    }
  }
}
