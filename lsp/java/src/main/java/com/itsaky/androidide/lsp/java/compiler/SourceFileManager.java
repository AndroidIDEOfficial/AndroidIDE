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

import com.blankj.utilcode.util.CloseUtils;
import com.google.common.collect.Iterables;
import com.itsaky.androidide.projects.api.AndroidModule;
import com.itsaky.androidide.projects.api.ModuleProject;
import com.itsaky.androidide.projects.util.StringSearch;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.SourceClassTrie;
import com.sun.tools.javac.api.JavacTool;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

@SuppressWarnings("Since15")
public class SourceFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

  public static final SourceFileManager NO_MODULE = new SourceFileManager(null);
  private static final ILogger LOG = ILogger.newInstance("SourceFileManager");
  private static final Map<ModuleProject, SourceFileManager> cachedFileManagers = new HashMap<>();
  private final ModuleProject module;

  private SourceFileManager(final ModuleProject module) {
    super(createDelegateFileManager());
    this.module = module;

    if (module != null) {
      setLocation(StandardLocation.CLASS_PATH, module.getCompileClasspaths());
      setLocation(StandardLocation.SOURCE_PATH, module.getCompileSourceDirectories());

      if (module instanceof AndroidModule) {
        final AndroidModule android = ((AndroidModule) module);
        setLocation(StandardLocation.PLATFORM_CLASS_PATH, android.getBootClassPaths());
      } else {
        setFallbackPlatformClasspath();
      }
    } else {
      setFallbackPlatformClasspath();
    }
  }

  protected void setFallbackPlatformClasspath() {
    // TODO Module system must be enabled for Java modules instead of setting Platform Classpath
    setLocation(
        StandardLocation.PLATFORM_CLASS_PATH, Collections.singleton(Environment.ANDROID_JAR));
  }

  private static StandardJavaFileManager createDelegateFileManager() {
    JavaCompiler compiler = JavacTool.create();
    return compiler.getStandardFileManager(
        SourceFileManager::logError, null, Charset.defaultCharset());
  }

  private static void logError(Diagnostic<?> error) {
    LOG.warn("SourceFileManager DiagnosticErr:", error.getMessage(null));
  }

  void setLocation(Location location, Iterable<? extends File> files) {
    try {
      fileManager.setLocation(location, files);
    } catch (IOException e) {
      LOG.error("Failed to update location", location, "with files", Iterables.toString(files), e);
    }
  }

  public static void clearCache() {
    for (final SourceFileManager fileManager : cachedFileManagers.values()) {
      CloseUtils.closeIO(fileManager);
    }

    cachedFileManagers.clear();
  }

  public static SourceFileManager forModule(@NonNull ModuleProject project) {
    Objects.requireNonNull(project);
    return cachedFileManagers.computeIfAbsent(project, SourceFileManager::createForModule);
  }

  private static SourceFileManager createForModule(@NonNull ModuleProject project) {
    LOG.info("Creating source file manager instance for module:", project);
    return new SourceFileManager(project);
  }
  
  @Override
  public Iterable<JavaFileObject> list(
      Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse)
      throws IOException {
    if (location == StandardLocation.SOURCE_PATH) {
      if (this.module == null) {
        return Collections.emptyList();
      }
      Stream<JavaFileObject> stream =
          module.listClassesFromSourceDirs(packageName).stream().map(this::asJavaFileObject);
      return stream::iterator;
    } else {
      return super.list(location, packageName, kinds, recurse);
    }
  }

  private JavaFileObject asJavaFileObject(SourceClassTrie.SourceNode node) {
    // TODO erase method bodies of files that are not open
    return new SourceFileObject(node.getFile());
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

  private String packageNameOrEmpty(Path file) {
    return this.module != null ? module.packageNameOrEmpty(file) : "";
  }

  private String removeExtension(String fileName) {
    int lastDot = fileName.lastIndexOf(".");
    return (lastDot == -1 ? fileName : fileName.substring(0, lastDot));
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
          module != null ? module.listClassesFromSourceDirs(packageName) : Collections.emptyList();
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

  void setLocationFromPaths(Location location, Collection<? extends Path> searchpath)
      throws IOException {
    fileManager.setLocationFromPaths(location, searchpath);
  }
}
