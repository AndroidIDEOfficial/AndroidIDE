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

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.itsaky.androidide.builder.model.IJavaCompilerSettings;
import com.itsaky.androidide.config.JavacConfigProvider;
import com.itsaky.androidide.javac.services.compiler.ReusableBorrow;
import com.itsaky.androidide.javac.services.partial.DiagnosticListenerImpl;
import com.itsaky.androidide.lsp.java.visitors.MethodRangeScanner;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.projects.util.StringSearch;
import com.itsaky.androidide.utils.ClassTrie;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.SourceClassTrie;
import com.itsaky.androidide.utils.StopWatch;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.ClientCodeWrapper;
import com.sun.tools.javac.api.JavacTaskImpl;
import com.sun.tools.javac.code.Kinds;
import com.sun.tools.javac.util.JCDiagnostic;

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
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompileBatch implements AutoCloseable {

  public static final String DEFAULT_COMPILER_SOURCE_AND_TARGET_VERSION = "11";
  private static final ILogger LOG = ILogger.newInstance("CompileBatch");
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
      CompilationTaskProcessor taskProcessor) {
    this.parent = parent;
    this.borrow = batchTask(parent, files);
    this.task = borrow.task;
    this.roots = new ArrayList<>();

    Objects.requireNonNull(taskProcessor, "A task processor is required");

    try {
      taskProcessor.process(borrow.task, this::processCompilationUnit);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  private void processCompilationUnit(final CompilationUnitTree root) {
    roots.add(root);
    updatePositions(root, false);
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

  @SuppressWarnings("Since15")
  @NonNull
  private List<String> options() {
    List<String> options = new ArrayList<>();

    // TODO Boot classpath must be different in java projects
    //  Also, java modules must be enabled in java projects
    System.setProperty(
        JavacConfigProvider.PROP_ANDROIDIDE_JAVA_HOME, Environment.JAVA_HOME.getAbsolutePath());
    JavacConfigProvider.setLatestSourceVersion(SourceVersion.RELEASE_8);
    JavacConfigProvider.setLatestSupportedSourceVersion(SourceVersion.RELEASE_11);

    JavacConfigProvider.disableModules();

    LOG.debug(
        JavacConfigProvider.getJavaHome(),
        JavacConfigProvider.isModulesEnabled(),
        SourceVersion.latest(),
        SourceVersion.latestSupported());

    setupCompileOptions(parent.module, options);
    Collections.addAll(options, "-proc:none", "-g");

    Collections.addAll(
        options,
        "-XDcompilePolicy=byfile",
        "-XD-Xprefer=source",
        "-XDide",
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

  /**
   * Combine source path or class path entries using the system separator, for example ':' in unix
   */
  private static String joinPath(@NonNull Collection<Path> classOrSourcePath) {
    return classOrSourcePath.stream()
        .map(Path::toString)
        .collect(Collectors.joining(File.pathSeparator));
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

  private String packageName(javax.tools.Diagnostic<? extends javax.tools.JavaFileObject> err) {
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

  private boolean isValidFileRange(javax.tools.Diagnostic<? extends JavaFileObject> d) {
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
