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

package com.itsaky.lsp.java.compiler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.FileStore;
import com.itsaky.lsp.java.models.CompilationRequest;
import com.itsaky.lsp.java.parser.ParseTask;
import com.itsaky.lsp.java.parser.Parser;
import com.itsaky.lsp.java.partial.CompilationInfo;
import com.itsaky.lsp.java.partial.PartialReparser;
import com.itsaky.lsp.java.partial.PartialReparserImpl;
import com.itsaky.lsp.java.utils.Cache;
import com.itsaky.lsp.java.utils.Extractors;
import com.itsaky.lsp.java.utils.ScanClassPath;
import com.itsaky.lsp.java.utils.StringSearch;
import com.itsaky.lsp.java.visitors.FindTypeDeclarations;
import com.itsaky.lsp.models.Range;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class JavaCompilerService implements CompilerProvider {

  private static final Cache<String, Boolean> cacheContainsWord = new Cache<>();
  private static final Cache<Void, List<String>> cacheContainsType = new Cache<>();
  private static final ILogger LOG = ILogger.newInstance("JavaCompilerService");
  protected final ScanClassPath classPathScanner = new ScanClassPath();
  protected final Set<Path> classPath, docPath;
  protected final Set<String> jdkClasses = classPathScanner.jdkTopLevelClasses(), classPathClasses;
  protected final SynchronizedTask synchronizedTask = new SynchronizedTask();
  protected final List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();
  protected final Map<JavaFileObject, Long> cachedModified = new HashMap<>();
  protected final SourceFileManager fileManager;
  private final Cache<Void, List<String>> cacheFileImports = new Cache<>();
  protected ReusableCompiler compiler = new ReusableCompiler();
  private CompileBatch cachedCompile;

  public JavaCompilerService(Set<Path> classPath, Set<Path> docPath) {
    this.classPath = Collections.unmodifiableSet(classPath);
    this.docPath = Collections.unmodifiableSet(docPath);
    this.classPathClasses = classPathScanner.classPathTopLevelClasses(classPath);
    this.fileManager = new SourceFileManager();
  }

  @Override
  public Set<String> imports() {
    HashSet<String> all = new HashSet<>();
    for (Path f : FileStore.all()) {
      all.addAll(readImports(f));
    }
    return all;
  }

  private List<String> readImports(Path file) {
    if (cacheFileImports.needs(file, null)) {
      loadImports(file);
    }
    return cacheFileImports.get(file, null);
  }

  private void loadImports(Path file) {
    List<String> list = new ArrayList<>();
    Pattern importClass = Pattern.compile("^import +([\\w\\.]+\\.\\w+);");
    Pattern importStar = Pattern.compile("^import +([\\w\\.]+\\.\\*);");
    try (BufferedReader lines = FileStore.lines(file)) {
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
  public List<String> publicTopLevelTypes() {
    List<String> all = new ArrayList<>();
    for (Path file : FileStore.all()) {
      String fileName = file.getFileName().toString();
      if (!fileName.endsWith(".java")) {
        continue;
      }
      String className = fileName.substring(0, fileName.length() - ".java".length());
      String packageName = FileStore.packageName(file);
      if (!packageName.isEmpty()) {
        className = packageName + "." + className;
      }
      all.add(className);
    }
    all.addAll(classPathClasses);
    all.addAll(jdkClasses);
    return all;
  }

  @Override
  public List<String> packagePrivateTopLevelTypes(String packageName) {
    return Collections.emptyList();
  }

  @Override
  public Iterable<Path> search(String query) {
    Predicate<Path> test = f -> StringSearch.containsWordMatching(f, query);
    return () -> FileStore.all().stream().filter(test).iterator();
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
    for (Path f : FileStore.list(packageName)) {
      if (containsWord(f, simpleName) && containsType(f, className)) {
        return f;
      }
    }
    return NOT_FOUND;
  }

  @Override
  public Path[] findTypeReferences(String className) {
    String packageName = Extractors.packageName(className);
    String simpleName = Extractors.simpleName(className);
    List<Path> candidates = new ArrayList<>();
    for (Path f : FileStore.all()) {
      if (containsWord(f, packageName)
          && containsImport(f, className)
          && containsWord(f, simpleName)) {
        candidates.add(f);
      }
    }

    return candidates.toArray(new Path[0]);
  }

  private boolean containsWord(Path file, String word) {
    if (cacheContainsWord.needs(file, word)) {
      cacheContainsWord.load(file, word, StringSearch.containsWord(file, word));
    }
    return cacheContainsWord.get(file, word);
  }

  private boolean containsImport(Path file, String className) {
    String packageName = Extractors.packageName(className);
    if (FileStore.packageName(file).equals(packageName)) {
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

  @Override
  public Path[] findMemberReferences(String className, String memberName) {
    List<Path> candidates = new ArrayList<>();
    for (Path f : FileStore.all()) {
      if (containsWord(f, memberName)) {
        candidates.add(f);
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

      if (f.getLastModified() != cachedModified.get(f)) {
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

  private void tryReparse(final CompilationRequest request) {

    // Satisfy lint
    Objects.requireNonNull(request.partialRequest);

    final File file = new File(request.sources.iterator().next().toUri());
    final String path = file.getAbsolutePath();
    final List<Pair<Range, MethodTree>> positions = this.cachedCompile.methodPositions.get(path);
    if (positions == null) {
      LOG.warn("Cannot perform reparse. No method positions found.");
      recompile(request);
      return;
    }

    final Pair<Range, MethodTree> currentMethod =
        binarySearchCurrentMethod(positions, request.partialRequest.cursor);
    if (currentMethod == null) {
      LOG.warn("Cannot perform reparse. Unable to find current method");
      recompile(request);
      return;
    }

    LOG.debug("Trying to reparse method:", currentMethod.second.getName());

    final CompilationInfo info =
        new CompilationInfo(
            cachedCompile.task, cachedCompile.diagnosticListener, cachedCompile.roots.get(0));
    final PartialReparser reparser = new PartialReparserImpl();
    final boolean reparsed =
        reparser.reparseMethod(info, currentMethod.second, request.partialRequest.contents);
    if (!reparsed) {
      LOG.error("Failed to reparse");
      recompile(request);
      return;
    }

    LOG.info("Successfully reparsed method", currentMethod.second.getName());
  }

  @Nullable
  private Pair<Range, MethodTree> binarySearchCurrentMethod(
      @NonNull final List<Pair<Range, MethodTree>> positions, final long cursor) {
    int left = 0;
    int right = positions.size() - 1;
    while (left <= right) {
      int mid = (left + right) / 2;
      final Pair<Range, MethodTree> method = positions.get(mid);
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

  private synchronized void recompile(CompilationRequest request) {
    close();
    cachedCompile = performCompilation(request.sources);
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

  public synchronized void close() {
    if (cachedCompile != null && !cachedCompile.closed) {
      cachedCompile.close();
      if (!cachedCompile.borrow.closed) {
        cachedCompile.borrow.close();
      }
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

  public SynchronizedTask getSynchronizedTask() {
    return synchronizedTask;
  }
}
