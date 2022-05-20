package com.itsaky.lsp.java;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.CharSequenceInputStream;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.lsp.java.utils.StringSearch;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.util.PathUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import javax.lang.model.element.TypeElement;

public class FileStore {

  private static final Set<Path> workspaceRoots = new HashSet<>();
  private static final Map<Path, VersionedContent> activeDocuments = new HashMap<>();

  /** javaSources[file] is the javaSources time of a .java source file. */
  // TODO organize by package name for speed of list(...)
  private static final TreeMap<Path, Info> javaSources = new TreeMap<>();

  private static final ILogger LOG = ILogger.newInstance("FileStore");

  public static void shutdown() {
    workspaceRoots.clear();
    activeDocuments.clear();
    javaSources.clear();
  }

  public static void configurationChanged(Set<Path> newWorkspaceRoots) {
    javaSources.clear();
    workspaceRoots.clear();
    setWorkspaceRoots(newWorkspaceRoots);
  }

  public static void setWorkspaceRoots(Set<Path> newRoots) {
    newRoots.removeIf(it -> !Files.exists(it) || !Files.isReadable(it));
    for (Path root : workspaceRoots) {
      if (!newRoots.contains(root)) {
        workspaceRoots.removeIf(f -> f.startsWith(root));
      }
    }
    for (Path root : newRoots) {
      if (!workspaceRoots.contains(root)) {
        addFiles(root);
      }
    }
    workspaceRoots.clear();
    workspaceRoots.addAll(newRoots);
  }

  private static void addFiles(Path root) {
    if (!Files.isReadable(root) || !Files.isWritable(root)) {
      return;
    }

    try {
      Files.walkFileTree(root, new FindJavaSources());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Collection<Path> all() {
    return javaSources.keySet();
  }

  public static boolean contains(Path file) {
    return isJavaFile(file) && javaSources.containsKey(file);
  }

  public static boolean isJavaFile(@NonNull Path file) {
    String name = file.getFileName().toString();
    // We hide module-info.java from javac, because when javac sees module-info.java
    // it goes into "module mode" and starts looking for classes on the module class path.
    // This becomes evident when javac starts recompiling *way too much* on each task,
    // because it doesn't realize there are already up-to-date .class files.
    // The better solution would be for java-language server to detect the presence of
    // module-info.java,
    // and go into its own "module mode" where it infers a module source path and a module class
    // path.
    return name.endsWith(".java") && !Files.isDirectory(file) && !name.equals("module-info.java");
  }

  public static Instant modified(Path file) {
    // If file is open, use last in-memory modification time
    if (activeDocuments.containsKey(file)) {
      return Objects.requireNonNull(activeDocuments.get(file)).modified;
    }
    // If we've never checked before, look up modified time on disk
    if (!javaSources.containsKey(file)) {
      readInfoFromDisk(file);
    }
    // Look up modified time from cache
    return Objects.requireNonNull(javaSources.get(file)).modified;
  }

  private static void readInfoFromDisk(Path file) {
    try {
      Instant time = Files.getLastModifiedTime(file).toInstant();
      String packageName = StringSearch.packageName(file);
      javaSources.put(file, new Info(time, packageName));
    } catch (NoSuchFileException e) {
      LOG.warn(e);
      javaSources.remove(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String suggestedPackageName(Path file) {
    // Look in each parent directory of file
    for (Path dir = file.getParent(); dir != null; dir = dir.getParent()) {
      // Try to find a sibling with a package declaration
      for (Path sibling : javaSourcesIn(dir)) {
        if (PathUtils.isSameFile(sibling, file)) {
          continue;
        }
        String packageName = packageName(sibling);
        if (TextUtils.isEmpty(packageName.trim())) {
          continue;
        }
        Path relativePath = dir.relativize(file.getParent());
        String relativePackage = relativePath.toString().replace(File.separatorChar, '.');
        if (!relativePackage.isEmpty()) {
          packageName = packageName + "." + relativePackage;
        }
        return packageName;
      }
    }
    return "";
  }

  private static List<Path> javaSourcesIn(Path dir) {
    Map<Path, Info> tail = javaSources.tailMap(dir, false);
    List<Path> list = new ArrayList<>();
    for (Path file : tail.keySet()) {
      if (!file.startsWith(dir)) {
        break;
      }
      list.add(file);
    }
    return list;
  }

  public static String packageName(Path file) {
    // If we've never checked before, look up package name on disk
    if (!javaSources.containsKey(file)) {
      readInfoFromDisk(file);
    }
    // Look up package name from cache
    return Objects.requireNonNull(javaSources.get(file)).packageName;
  }

  public static void externalCreate(Path file) {
    readInfoFromDisk(file);
  }

  public static void externalChange(Path file) {
    readInfoFromDisk(file);
  }

  public static void externalDelete(Path file) {
    javaSources.remove(file);
  }

  public static void open(DocumentOpenEvent params) {
    Path document = params.getOpenedFile();
    if (!isJavaFile(document)) {
      LOG.error("Opened file is not a java file. Ignoring...");
      return;
    }

    activeDocuments.put(document, new VersionedContent(params.getText(), params.getVersion()));
  }

  public static void change(DocumentChangeEvent params) {
    final Path document = params.getChangedFile();
    if (!isJavaFile(document)) {
      LOG.error("Changed file is not a java file. Ignoring...");
      return;
    }

    // We do not support range changes
    CharSequence newText = params.getNewText();
    activeDocuments.put(document, new VersionedContent(newText, params.getVersion()));
  }

  public static void close(DocumentCloseEvent params) {
    final Path document = params.getClosedFile();
    if (!isJavaFile(document)) {
      LOG.error("Closed file is not a java file. Ignoring...");
      return;
    }
    activeDocuments.remove(document);
  }

  public static Set<Path> activeDocuments() {
    return activeDocuments.keySet();
  }

  public static CharSequence contents(Path file) {
    if (!isJavaFile(file)) {
      throw new RuntimeException(file + " is not a java file");
    }

    if (activeDocuments.containsKey(file)) {
      return Objects.requireNonNull(activeDocuments.get(file)).content;
    }

    try (final BufferedReader reader = Files.newBufferedReader(file)) {
      final StringBuilder builder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
        builder.append("\n");
      }

      return builder.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static InputStream inputStream(Path file) {
    if (activeDocuments.containsKey(file)) {
      CharSequence content = Objects.requireNonNull(activeDocuments.get(file)).content;
      return new CharSequenceInputStream(content, "UTF-8");
    }

    try {
      return Files.newInputStream(file);
    } catch (NoSuchFileException e) {
      LOG.warn(e);
      byte[] bs = {};
      return new ByteArrayInputStream(bs);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static BufferedReader lines(Path file) {
    return bufferedReader(file);
  }

  public static BufferedReader bufferedReader(Path file) {
    if (activeDocuments.containsKey(file)) {
      CharSequence string = Objects.requireNonNull(activeDocuments.get(file)).content;
      return new BufferedReader(new CharSequenceReader(string));
    }

    try {
      return Files.newBufferedReader(file);
    } catch (NoSuchFileException e) {
      LOG.warn(e);
      return new BufferedReader(new StringReader(""));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Convert from line/column (1-based) to offset (0-based) */
  public static int offset(String contents, int line, int column) {
    line--;
    column--;
    int cursor = 0;
    while (line > 0) {
      if (contents.charAt(cursor) == '\n') {
        line--;
      }
      cursor++;
    }
    return cursor + column;
  }

  public static Optional<Path> findDeclaringFile(@NonNull TypeElement el) {
    String qualifiedName = el.getQualifiedName().toString();
    String packageName = StringSearch.mostName(qualifiedName);
    String className = StringSearch.lastName(qualifiedName);
    // Fast path: look for text `class Foo` in file Foo.java
    for (Path f : list(packageName)) {
      if (f.getFileName().toString().equals(className) && StringSearch.containsType(f, el)) {
        return Optional.of(f);
      }
    }
    // Slow path: look for text `class Foo` in any file in package
    for (Path f : list(packageName)) {
      if (StringSearch.containsType(f, el)) {
        return Optional.of(f);
      }
    }
    return Optional.empty();
  }

  public static List<Path> list(String packageName) {
    List<Path> list = new ArrayList<>();
    for (Path file : javaSources.keySet()) {
      if (Objects.requireNonNull(javaSources.get(file)).packageName.equals(packageName)) {
        list.add(file);
      }
    }
    return list;
  }

  public static class FindJavaSources extends SimpleFileVisitor<Path> {
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes _attrs) {
      if (isJavaFile(file)) {
        readInfoFromDisk(file);
      }
      return FileVisitResult.CONTINUE;
    }
  }

  private static class Info {
    final Instant modified;
    final String packageName;

    Info(Instant modified, String packageName) {
      this.modified = modified;
      this.packageName = packageName;
    }
  }

  static class VersionedContent {
    final CharSequence content;
    final int version;
    final Instant modified = Instant.now();

    VersionedContent(CharSequence content, int version) {
      Objects.requireNonNull(content, "content is null");
      this.content = content;
      this.version = version;
    }
  }
}
