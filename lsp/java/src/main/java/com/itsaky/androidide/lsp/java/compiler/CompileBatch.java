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

import com.itsaky.androidide.javac.services.compiler.ReusableCompiler;
import com.itsaky.androidide.javac.services.partial.DiagnosticListenerImpl;
import com.itsaky.androidide.lsp.java.parser.Parser;
import com.itsaky.androidide.lsp.java.visitors.MethodRangeScanner;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.projects.util.SourceClassTrie;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.StopWatch;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTaskImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.lang.model.element.Name;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public class CompileBatch implements AutoCloseable {

  private static final ILogger LOG = ILogger.newInstance("CompileBatch");
  private static final Path FILE_NOT_FOUND = Paths.get("");
  final JavaCompilerService parent;
  final ReusableCompiler.Borrow borrow;
  final JavacTaskImpl task;
  final List<CompilationUnitTree> roots;
  DiagnosticListenerImpl diagnosticListener;
  /** Indicates the task that requested the compilation is finished with it. */
  boolean closed;

  final Map<String, List<Pair<Range, TreePath>>> methodPositions = new HashMap<>();

  CompileBatch(JavaCompilerService parent, Collection<? extends JavaFileObject> files) {
    this.parent = parent;
    this.borrow = batchTask(parent, files);
    this.task = borrow.task;
    this.roots = new ArrayList<>();

    final StopWatch watch = new StopWatch("Create CompileBatch");

    final Iterable<? extends CompilationUnitTree> trees = borrow.task.parse();
    watch.lap("CompilationUnitTree(s) parsed");

    for (CompilationUnitTree t : trees) {
      roots.add(t);
      updatePositions(t, false);
    }

    // The results of borrow.task.analyze() are unreliable when errors are present
    // You can get at `Element` values using `Trees`
    borrow.task.analyze();
    watch.log();
  }

  void updatePositions(CompilationUnitTree tree, boolean allowDuplicate) {
    final StopWatch positionWatch = new StopWatch("Scan method positions");
    final List<Pair<Range, TreePath>> positions = new ArrayList<>();
    new MethodRangeScanner(this.task).scan(tree, positions);
    final String path = new File(tree.getSourceFile().toUri()).getAbsolutePath();
    final List<Pair<Range, TreePath>> old = this.methodPositions.put(path, positions);
    if (old != null && !allowDuplicate) {
      throw new IllegalStateException(
          "Duplicate CompilationUnitTree for file:" + tree.getSourceFile().toUri());
    }

    positionWatch.log();
  }

  private ReusableCompiler.Borrow batchTask(
      @NonNull JavaCompilerService parent, @NonNull Collection<? extends JavaFileObject> sources) {

    parent.diagnostics.clear();
    final Iterable<String> options = options(parent.classPath);

    diagnosticListener =
        new DiagnosticListenerWrapper(parent.diagnostics::add, sources.iterator().next());

    return parent.compiler.getTask(
        parent.fileManager, diagnosticListener, options, Collections.emptyList(), sources);
  }

  @NonNull
  private static List<String> options(Set<Path> classPath) {
    List<String> list = new ArrayList<>();

    Collections.addAll(list, "-classpath", joinPath(classPath));
    Collections.addAll(list, "-source", "11", "-target", "11");
    Collections.addAll(list, "--system", Environment.COMPILER_MODULE.getAbsolutePath());
    Collections.addAll(list, "-proc:none");
    Collections.addAll(list, "-g");

    Collections.addAll(
        list,
        "-Xlint:cast",
        "-Xlint:deprecation",
        "-Xlint:empty",
        "-Xlint:fallthrough",
        "-Xlint:finally",
        "-Xlint:path",
        "-Xlint:unchecked",
        "-Xlint:varargs",
        "-Xlint:static");

    return list;
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
    // Check for "class not found errors" that refer to package private classes
    Set<Path> addFiles = new HashSet<>();
    for (Diagnostic<? extends JavaFileObject> err : parent.diagnostics) {
      if (!err.getCode().equals("compiler.err.cant.resolve.location")) {
        continue;
      }
      if (!isValidFileRange(err)) {
        continue;
      }
      String className = errorText(err);
      String packageName = packageName(err);
      Path location = findPackagePrivateClass(packageName, className);
      if (location != FILE_NOT_FOUND) {
        addFiles.add(location);
      }
    }
    return addFiles;
  }

  private String errorText(javax.tools.Diagnostic<? extends javax.tools.JavaFileObject> err) {
    Path file = Paths.get(err.getSource().toUri());
    CharSequence contents = ProjectManager.INSTANCE.getDocumentContents(file);
    int begin = (int) err.getStartPosition();
    int end = (int) err.getEndPosition();
    return substring(contents, begin, end);
  }

  private String substring(CharSequence source, int start, int end) {
    final StringBuilder sb = new StringBuilder();
    if (source == null) {
      return sb.toString();
    }

    if (start > end || start < 0 || end >= source.length()) {
      throw new IndexOutOfBoundsException(
          String.format(Locale.ROOT, "length=%d, start=%d, end=%d", source.length(), start, end));
    }

    if (source instanceof String) {
      return ((String) source).substring(start, end);
    }

    for (int i = start; i < end; i++) {
      sb.append(source.charAt(i));
    }

    return sb.toString();
  }

  private String packageName(javax.tools.Diagnostic<? extends javax.tools.JavaFileObject> err) {
    Path file = Paths.get(err.getSource().toUri());
    ModuleProject module = ProjectManager.INSTANCE.findModuleForFile(file);
    if (module == null) {
      return "";
    }

    SourceClassTrie.SourceNode node = module.compileJavaSourceClasses.findSource(file);
    if (node == null) {
      return "";
    }

    return node.getPackageName();
  }

  private Path findPackagePrivateClass(String packageName, String className) {
    final List<SourceClassTrie.SourceNode> classes =
        parent.module != null
            ? parent.module.listClassesFromSourceDirs(packageName)
            : Collections.emptyList();
    if (classes.isEmpty()) {
      return FILE_NOT_FOUND;
    }

    for (SourceClassTrie.SourceNode node : classes) {
      final Path file = node.getFile();
      Parser parse = Parser.parseFile(file);
      for (Name candidate : parse.packagePrivateClasses()) {
        if (candidate.contentEquals(className)) {
          return file;
        }
      }
    }
    return FILE_NOT_FOUND;
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
