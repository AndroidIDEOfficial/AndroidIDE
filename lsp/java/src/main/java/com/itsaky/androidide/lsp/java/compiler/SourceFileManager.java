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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.CloseUtils;
import com.itsaky.androidide.javac.services.fs.AndroidFsProviderImpl;
import com.itsaky.androidide.projects.api.AndroidModule;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.projects.util.StringSearch;
import com.itsaky.androidide.utils.ClassTrie;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.SourceClassTrie;
import com.sun.tools.javac.api.JavacTool;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.util.Context;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@SuppressWarnings("Since15")
public class SourceFileManager extends ForwardingJavaFileManager<JavacFileManager> {

  public static final String ANDROIDIDE_CACHE_LOCATION = "ANDROIDIDE_CACHE_LOCATION";
  public static final SourceFileManager NO_MODULE;
  private static final ILogger LOG;
  private static final Map<ModuleProject, SourceFileManager> cachedFileManagers =
      new ConcurrentHashMap<>();

  static {
    // Initialize LOG first to prevent NPE
    LOG = ILogger.newInstance("SourceFileManager");
    NO_MODULE = new SourceFileManager(null);
  }

  private final ModuleProject module;

  private SourceFileManager(final ModuleProject module) {
    super(createDelegateFileManager());
    this.module = module;

    AndroidFsProviderImpl.INSTANCE.init();

    if (module == null) {
      setFallbackPlatformClasspath();
      return;
    }

    setLocationLogError(StandardLocation.SOURCE_PATH, module.getCompileSourceDirectories());

    final Set<File> classpaths = configureClasspaths(module);
    setLocationLogError(StandardLocation.CLASS_PATH, classpaths);

    listLocations(EnumSet.of(StandardLocation.CLASS_PATH, StandardLocation.PLATFORM_CLASS_PATH));
  }

  @NonNull
  private Set<File> configureClasspaths(final ModuleProject module) {
    if (module == null) {
      setFallbackPlatformClasspath();
      return emptySet();
    }

    final Set<File> classpaths = module.getCompileClasspaths();
    if (!(module instanceof AndroidModule)) {
      setFallbackPlatformClasspath();
      return emptySet();
    }
  
    final AndroidModule androidModule = (AndroidModule) module;
    classpaths.addAll(androidModule.getBootClassPaths());
    
    setLocationLogError(StandardLocation.PLATFORM_CLASS_PATH, androidModule.getBootClassPaths());
    return classpaths;
  }

  private static JavacFileManager createDelegateFileManager() {
    return JavacTool.create()
        .getStandardFileManager(LOG::debug, Locale.getDefault(), StandardCharsets.UTF_8);
  }

  protected void setFallbackPlatformClasspath() {
    // TODO Module system must be enabled for Java modules instead of setting Platform Classpath
    setLocationLogError(
        StandardLocation.PLATFORM_CLASS_PATH, Collections.singleton(Environment.ANDROID_JAR));
  }

  public void setLocationLogError(Location location, Iterable<File> searchPath) {
    try {
      fileManager.setLocation(location, searchPath);
    } catch (IOException e) {
      LOG.error("Unable to set location", e);
    }
  }

  private void listLocations(final EnumSet<StandardLocation> locations) {
    for (StandardLocation location : locations) {
      try {
        list(
            location,
            ANDROIDIDE_CACHE_LOCATION,
            EnumSet.of(
                JavaFileObject.Kind.CLASS,
                JavaFileObject.Kind.SOURCE,
                JavaFileObject.Kind.HTML,
                JavaFileObject.Kind.OTHER),
            true);
      } catch (IOException e) {
        // Ignored
        LOG.debug("Failed to list location:", location, e);
      }
    }
  }

  @Override
  public Iterable<JavaFileObject> list(
      Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse)
      throws IOException {
    if (location == StandardLocation.SOURCE_PATH) {
      if (this.module == null) {
        return emptyList();
      }

      final ClassTrie.Node node = module.compileJavaSourceClasses.findNode(packageName);
      if (node == null || node.isClass()) {
        return emptyList();
      }

      final Stream<ClassTrie.Node> nodes;
      if (recurse) {
        nodes = node.allClassNodes().stream();
      } else {
        nodes = node.getChildren().values().stream();
      }

      final Stream<JavaFileObject> stream =
          nodes
              .filter(it -> it instanceof SourceClassTrie.SourceNode)
              .map(it -> asJavaFileObject((SourceClassTrie.SourceNode) it));

      //noinspection NullableProblems
      return stream::iterator;
    }

    return super.list(location, packageName, kinds, recurse);
  }

  @Override
  public String inferBinaryName(Location location, JavaFileObject file) {
    if (location == StandardLocation.SOURCE_PATH) {
      SourceFileObject source = (SourceFileObject) file;
      String packageName = packageNameOrEmpty(source.path);
      String className = removeExtension(source.path.getFileName().toString());
      if (!packageName.isEmpty()) className = packageName + "." + className;
      return className;
    } else {
      return super.inferBinaryName(location, file);
    }
  }

  @Override
  public boolean hasLocation(Location location) {
    return location == StandardLocation.SOURCE_PATH || super.hasLocation(location);
  }

  @Override
  public JavaFileObject getJavaFileForInput(
      Location location, String className, JavaFileObject.Kind kind) throws IOException {
    if (location == StandardLocation.SOURCE_PATH) {
      String packageName = StringSearch.mostName(className);
      String simpleClassName = StringSearch.lastName(className);
      List<SourceClassTrie.SourceNode> classes =
          module != null ? module.listClassesFromSourceDirs(packageName) : emptyList();
      for (SourceClassTrie.SourceNode node : classes) {
        final Path path = node.getFile();
        if (path.getFileName().toString().equals(simpleClassName + kind.extension)) {
          return new SourceFileObject(path);
        }
      }
      // Fall through to disk in case we have .jar or .zip files on the source path
    }
    return super.getJavaFileForInput(location, className, kind);
  }

  @Override
  public FileObject getFileForInput(Location location, String packageName, String relativeName)
      throws IOException {
    if (location == StandardLocation.SOURCE_PATH) {
      return null;
    }
    return super.getFileForInput(location, packageName, relativeName);
  }

  @Override
  public boolean contains(Location location, FileObject file) throws IOException {
    if (location == StandardLocation.SOURCE_PATH) {
      SourceFileObject source = (SourceFileObject) file;
      return module.compileJavaSourceClasses.findSource(source.path) != null;
    } else {
      return super.contains(location, file);
    }
  }

  private String packageNameOrEmpty(Path file) {
    return this.module != null ? module.packageNameOrEmpty(file) : "";
  }

  private String removeExtension(String fileName) {
    int lastDot = fileName.lastIndexOf(".");
    return (lastDot == -1 ? fileName : fileName.substring(0, lastDot));
  }

  private JavaFileObject asJavaFileObject(SourceClassTrie.SourceNode node) {
    // TODO erase method bodies of files that are not open
    return new SourceFileObject(node.getFile());
  }

  public static SourceFileManager forModule(@NonNull ModuleProject project) {
    Objects.requireNonNull(project);
    return cachedFileManagers.computeIfAbsent(project, SourceFileManager::createForModule);
  }

  private static SourceFileManager createForModule(@NonNull ModuleProject project) {
    LOG.info("Creating source file manager instance for module:", project);
    return new SourceFileManager(project);
  }

  public static void clearCache() {
    for (final SourceFileManager fileManager : cachedFileManagers.values()) {
      CloseUtils.closeIO(fileManager);
    }

    cachedFileManagers.clear();
  }

  public void setContext(final Context context) {
    fileManager.setContext(context);
  }
}
