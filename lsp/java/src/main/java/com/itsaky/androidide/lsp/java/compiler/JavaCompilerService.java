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
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent;
import com.itsaky.androidide.javac.services.compiler.ReusableCompiler;
import com.itsaky.androidide.javac.services.partial.CompilationInfo;
import com.itsaky.androidide.javac.services.partial.PartialReparser;
import com.itsaky.androidide.javac.services.partial.PartialReparserImpl;
import com.itsaky.androidide.lsp.java.models.CompilationRequest;
import com.itsaky.androidide.lsp.java.models.PartialReparseRequest;
import com.itsaky.androidide.lsp.java.parser.ParseTask;
import com.itsaky.androidide.lsp.java.parser.Parser;
import com.itsaky.androidide.lsp.java.utils.Extractors;
import com.itsaky.androidide.lsp.java.visitors.FindTypeDeclarations;
import com.itsaky.androidide.models.Range;
import com.itsaky.androidide.projects.FileManager;
import com.itsaky.androidide.projects.ProjectManager;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.projects.util.StringSearch;
import com.itsaky.androidide.utils.BootClasspathProvider;
import com.itsaky.androidide.utils.Cache;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.SourceClassTrie;
import com.itsaky.androidide.utils.StopWatch;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class JavaCompilerService implements CompilerProvider {

  public static JavaCompilerService NO_MODULE_COMPILER = new JavaCompilerService(null);

  private static final Cache<String, Boolean> cacheContainsWord = new Cache<>();
  private static final Cache<Void, List<String>> cacheContainsType = new Cache<>();
  private static final ILogger LOG = ILogger.newInstance("JavaCompilerService");

  protected final Set<Path> classPath;
  protected final Set<String> classPathClasses;
  protected final Set<String> bootClasspathClasses =
      BootClasspathProvider.getTopLevelClasses(
          Collections.singleton(Environment.ANDROID_JAR.getAbsolutePath()));
  protected final List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();
  protected final Map<JavaFileObject, Long> cachedModified = new HashMap<>();
  protected final Cache<Void, List<String>> cacheFileImports = new Cache<>();
  protected final SynchronizedTask synchronizedTask = new SynchronizedTask();
  protected final SourceFileManager fileManager;
  protected final ModuleProject module;

  public ReusableCompiler compiler = new ReusableCompiler();

  private CompileBatch cachedCompile;
  private int changeDelta = 0;

  // The module project must not be null
  // It is marked as nullable just for some special cases like tests
  public JavaCompilerService(@Nullable ModuleProject module) {
    this.module = module;
    this.fileManager = new SourceFileManager(module);
    if (module == null) {
      this.classPath = Collections.unmodifiableSet(Collections.emptySet());
      this.classPathClasses = Collections.emptySet();
    } else {
      this.classPath =
          Collections.unmodifiableSet(
              module.getCompileClasspaths().stream().map(File::toPath).collect(Collectors.toSet()));
      this.classPathClasses = module.compileClasspathClasses.allClassNames();
    }
  }

  public ModuleProject getModule() {
    return module;
  }

  private List<String> readImports(Path file) {
    if (cacheFileImports.needs(file, null)) {
      loadImports(file);
    }
    return cacheFileImports.get(file, null);
  }

  private String packageNameOrEmpty(Path file) {
    return module != null ? module.packageNameOrEmpty(file) : "";
  }

  private void loadImports(Path file) {
    List<String> list = new ArrayList<>();
    Pattern importClass = Pattern.compile("^import +([\\w.]+\\.\\w+);");
    Pattern importStar = Pattern.compile("^import +([\\w.]+\\.\\*);");
    try (BufferedReader lines = FileManager.INSTANCE.getReader(file)) {
      for (String line = lines.readLine(); line != null; line = lines.readLine()) {
        // If we reach a class declaration, stop looking for imports
        // TODO This could be a little more specific
        if (line.contains("class")) {
          break;
        }
        // import foo.bar.Doh;
        Matcher matchesClass = importClass.matcher(line);
        if (matchesClass.matches()) {
          list.add(matchesClass.group(1));
        }
        // import foo.bar.*
        Matcher matchesStar = importStar.matcher(line);
        if (matchesStar.matches()) {
          list.add(matchesStar.group(1));
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    cacheFileImports.load(file, null, list);
  }

  @Override
  public TreeSet<String> publicTopLevelTypes() {
    TreeSet<String> all = new TreeSet<>();
    List<SourceClassTrie.SourceNode> sourceClasses =
        module != null ? module.compileJavaSourceClasses.allSources() : Collections.emptyList();
    for (SourceClassTrie.SourceNode node : sourceClasses) {
      all.add(node.getQualifiedName());
    }
    all.addAll(classPathClasses);
    all.addAll(bootClasspathClasses);
    return all;
  }

  @Override
  public TreeSet<String> packagePrivateTopLevelTypes(String packageName) {
    return new TreeSet<>();
  }

  @Override
  public Optional<JavaFileObject> findAnywhere(String className) {
    Path fromSource = findTypeDeclaration(className);
    if (fromSource != NOT_FOUND) {
      return Optional.of(new SourceFileObject(fromSource));
    }
    return Optional.empty();
  }

  @Override
  public Path findTypeDeclaration(String className) {
    Path fastFind = findPublicTypeDeclaration(className);
    if (fastFind != NOT_FOUND) {
      return fastFind;
    }
    // In principle, the slow path can be skipped in many cases.
    // If we're spending a lot of time in findTypeDeclaration, this would be a good
    // optimization.
    String packageName = Extractors.packageName(className);
    String simpleName = Extractors.simpleName(className);
    List<SourceClassTrie.SourceNode> classes =
        module != null ? module.listClassesFromSourceDirs(packageName) : Collections.emptyList();
    for (SourceClassTrie.SourceNode node : classes) {
      final Path path = node.getFile();
      if (containsWord(path, simpleName) && containsType(path, className)) {
        return path;
      }
    }
    return NOT_FOUND;
  }

  @Override
  public Path[] findTypeReferences(String className) {
    String packageName = Extractors.packageName(className);
    String simpleName = Extractors.simpleName(className);
    List<Path> candidates = new ArrayList<>();
    List<SourceClassTrie.SourceNode> sourceNodes =
        module != null ? module.compileJavaSourceClasses.allSources() : Collections.emptyList();
    for (SourceClassTrie.SourceNode node : sourceNodes) {
      final Path path = node.getFile();
      if (containsWord(path, packageName)
          && containsImport(path, className)
          && containsWord(path, simpleName)) {
        candidates.add(path);
      }
    }

    return candidates.toArray(new Path[0]);
  }

  @Override
  public Path[] findMemberReferences(String className, String memberName) {
    List<Path> candidates = new ArrayList<>();
    List<SourceClassTrie.SourceNode> sourceNodes =
        module != null ? module.compileJavaSourceClasses.allSources() : Collections.emptyList();
    for (SourceClassTrie.SourceNode node : sourceNodes) {
      final Path path = node.getFile();
      if (containsWord(path, memberName)) {
        candidates.add(path);
      }
    }
    return candidates.toArray(new Path[0]);
  }

  @Override
  public ParseTask parse(Path file) {
    Parser parser = Parser.parseFile(file);
    return new ParseTask(parser.task, parser.root);
  }

  @Override
  public ParseTask parse(JavaFileObject file) {
    Parser parser = Parser.parseJavaFileObject(file);
    return new ParseTask(parser.task, parser.root);
  }

  @Override
  public SynchronizedTask compile(final CompilationRequest request) {
    return compileBatch(request);
  }

  public synchronized void close() {
    if (cachedCompile != null) {
      cachedCompile.close();
      cachedCompile.borrow.close();
    }
  }

  public void destroy() {
    synchronizedTask.post(
        () -> {
          close();
          cachedCompile = null;
          cachedModified.clear();
          compiler = new ReusableCompiler();
        });
  }

  public SynchronizedTask getSynchronizedTask() {
    return synchronizedTask;
  }

  public void onDocumentChange(@NonNull DocumentChangeEvent event) {
    this.changeDelta += event.getChangeDelta();
  }

  private boolean containsWord(Path file, String word) {
    if (cacheContainsWord.needs(file, word)) {
      cacheContainsWord.load(file, word, StringSearch.containsWord(file, word));
    }
    return cacheContainsWord.get(file, word);
  }

  private boolean containsImport(Path file, String className) {
    String packageName = Extractors.packageName(className);
    if (packageNameOrEmpty(file).equals(packageName)) {
      return true;
    }
    String star = packageName + ".*";
    for (String i : readImports(file)) {
      if (i.equals(className) || i.equals(star)) {
        return true;
      }
    }
    return false;
  }

  private SynchronizedTask compileBatch(CompilationRequest request) {
    synchronizedTask.post(
        () -> {
          if (needsCompilation(request.sources)) {
            reparseOrRecompile(request);
          } else {
            LOG.info("...using cached compile");
          }
          synchronizedTask.setTask(new CompileTask(cachedCompile, diagnostics));
        });

    return synchronizedTask;
  }

  private boolean needsCompilation(Collection<? extends JavaFileObject> sources) {
    if (cachedModified.size() != sources.size()) {
      return true;
    }
    for (JavaFileObject f : sources) {
      if (!cachedModified.containsKey(f)) {
        return true;
      }

      final Long modified = cachedModified.get(f);
      if (modified != null && f.getLastModified() != modified) {
        return true;
      }
    }
    return false;
  }

  private synchronized void reparseOrRecompile(CompilationRequest request) {
    if (needsRecompilation(request)) {
      LOG.warn("Cannot reparse. Recompilation is required");
      recompile(request);
    } else {
      LOG.debug("Trying to perform a reparse...");
      tryReparse(request);
    }
  }

  private boolean needsRecompilation(final CompilationRequest request) {
    return this.cachedCompile == null
        || this.cachedCompile.closed
        || request.partialRequest == null
        || request.partialRequest.cursor < 0
        || request.sources.size() != 1; // Cannot perform a reparse if there are multiple files
  }

  private void tryReparse(@NonNull final CompilationRequest request) {

    // Satisfy lint
    final PartialReparseRequest partialRequest = request.partialRequest;
    Objects.requireNonNull(partialRequest);

    final StopWatch watch = new StopWatch("Method reparse");
    final File file = new File(request.sources.iterator().next().toUri());
    final String path = file.getAbsolutePath();
    final List<Pair<Range, TreePath>> positions = this.cachedCompile.methodPositions.get(path);
    if (positions == null) {
      LOG.warn("Cannot perform reparse. No method positions found.");
      recompile(request);
      return;
    }

    final Pair<Range, TreePath> currentMethod =
        binarySearchCurrentMethod(positions, partialRequest.cursor);
    if (currentMethod == null) {
      LOG.warn("Cannot perform reparse. Unable to find current method");
      recompile(request);
      return;
    } else {
      watch.lapFromLast("Found method at cursor position");
    }

    final MethodTree methodTree = (MethodTree) currentMethod.second.getLeaf();
    LOG.debug("Trying to reparse method:", methodTree.getName());

    final CompilationInfo info =
        new CompilationInfo(
            cachedCompile.task, cachedCompile.diagnosticListener, cachedCompile.roots.get(0));
    watch.setLastLap(System.currentTimeMillis());
    final SourcePositions sourcePositions = Trees.instance(cachedCompile.task).getSourcePositions();
    final int start = (int) sourcePositions.getStartPosition(info.cu, methodTree.getBody());
    final int end =
        (int) sourcePositions.getEndPosition(info.cu, methodTree.getBody()) + this.changeDelta;
    watch.lapFromLast("Found start and end positions of current method");
    final PartialReparser reparser = new PartialReparserImpl();

    if (end < 0 || end > partialRequest.contents.length()) {
      LOG.warn(
          "Cannot reparse. Invalid change delta. end:",
          end,
          "changeDelta:",
          this.changeDelta,
          "content.length:",
          partialRequest.contents.length());
      recompile(request);
      return;
    }

    final String newBody = partialRequest.contents.substring(start, end);
    final boolean reparsed =
        reparser.reparseMethod(info, currentMethod.second, newBody, partialRequest.contents);
    if (!reparsed) {
      LOG.error("Failed to reparse");
      recompile(request);
      return;
    }

    watch.log();
    LOG.info("Successfully reparsed method", methodTree.getName());
    updateModificationCache(request);
    cachedCompile.updatePositions(info.cu, true);
    this.changeDelta = 0;
  }

  @Nullable
  private Pair<Range, TreePath> binarySearchCurrentMethod(
      @NonNull final List<Pair<Range, TreePath>> positions, final long cursor) {
    int left = 0;
    int right = positions.size() - 1;
    while (left <= right) {
      int mid = (left + right) / 2;
      final Pair<Range, TreePath> method = positions.get(mid);
      final Range range = method.first;
      final int startIndex = range.getStart().requireIndex();
      final int endIndex = range.getEnd().requireIndex();

      if (cursor < startIndex) {
        right = mid - 1;
      } else if (cursor > endIndex) {
        left = mid + 1;
      } else {
        return method;
      }
    }
    return null;
  }

  @Nullable
  private Pair<Range, TreePath> binarySearchMethodForRange(
      final List<Pair<Range, TreePath>> methods, final Range range) {
    int left = 0;
    int right = methods.size() - 1;
    while (left <= right) {
      final int mid = (left + right) / 2;
      final Pair<Range, TreePath> method = methods.get(mid);
      final int compareResult = method.first.containsForBinarySearch(range.getStart());
      if (compareResult == 0) {
        return method;
      }

      if (compareResult < 0) {
        right = mid - 1;
      } else {
        left = mid + 1;
      }
    }
    return null;
  }

  private synchronized void recompile(CompilationRequest request) {
    close();
    cachedCompile = performCompilation(request.sources);
    updateModificationCache(request);
  }

  private void updateModificationCache(final CompilationRequest request) {
    cachedModified.clear();
    for (JavaFileObject f : request.sources) {
      cachedModified.put(f, f.getLastModified());
    }
  }

  private CompileBatch performCompilation(Collection<? extends JavaFileObject> sources) {
    if (sources.isEmpty()) {
      throw new RuntimeException("empty sources");
    }

    CompileBatch firstAttempt = new CompileBatch(this, sources);
    Set<Path> addFiles = firstAttempt.needsAdditionalSources();

    if (addFiles.isEmpty()) {
      return firstAttempt;
    }

    // If the compiler needs additional source files that contain package-private files
    LOG.info("...need to recompile with " + addFiles);
    firstAttempt.close();
    firstAttempt.borrow.close();

    List<JavaFileObject> moreSources = new ArrayList<>(sources);
    for (Path add : addFiles) {
      moreSources.add(new SourceFileObject(add));
    }

    return new CompileBatch(this, moreSources);
  }

  private boolean containsType(Path file, String className) {
    if (cacheContainsType.needs(file, null)) {
      CompilationUnitTree root = parse(file).root;
      List<String> types = new ArrayList<>();
      new FindTypeDeclarations().scan(root, types);
      cacheContainsType.load(file, null, types);
    }
    return cacheContainsType.get(file, null).contains(className);
  }

  private Path findPublicTypeDeclaration(String className) {
    JavaFileObject source;
    try {
      source =
          fileManager.getJavaFileForInput(
              StandardLocation.SOURCE_PATH, className, JavaFileObject.Kind.SOURCE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (source == null) {
      return NOT_FOUND;
    }
    if (!source.toUri().getScheme().equals("file")) {
      return NOT_FOUND;
    }
    Path file = Paths.get(source.toUri());
    if (!containsType(file, className)) {
      return NOT_FOUND;
    }
    return file;
  }
}
