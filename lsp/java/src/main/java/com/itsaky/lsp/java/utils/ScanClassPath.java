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

package com.itsaky.lsp.java.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.reflect.ClassPath;
import com.itsaky.androidide.utils.ILogger;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class ScanClassPath {

  private static final ILogger LOG = ILogger.newInstance("ScanClassPath");

  public Set<String> jdkTopLevelClasses() {
    LOG.info("Searching for top-level classes in the JDK");

    Set<String> classes = new TreeSet<>();
    try {
      final FileSystem fs = getJRTFileSystem();
      if (fs == null) {
        LOG.error("Unable to create an instance of JRTFileSystem");
        return classes;
      }

      final Path moduleRoot = fs.getPath("/modules/java.base/");
      try (Stream<Path> stream = Files.walk(moduleRoot)) {
        final Iterator<Path> it = stream.iterator();
        while (it.hasNext()) {
          final Path classFile = it.next();
          final String relative = moduleRoot.relativize(classFile).toString();
          if (relative.endsWith(".class") && !relative.contains("$")) {
            final String trim = relative.substring(0, relative.length() - ".class".length());
            final String qualifiedName = trim.replace(File.separatorChar, '.');
            classes.add(qualifiedName);
          }
        }
      } catch (IOException e) {
        LOG.error("An error occurred while indexing module 'java.base'", e);
      }
    } catch (Throwable e) {
      LOG.error("Unable to create an instance of JRTFileSystem", e);
    }

    LOG.info(String.format(Locale.ROOT, "Found %d classes in the java platform", classes.size()));
    return classes;
  }

  @Nullable
  @Contract(" -> new")
  public FileSystem getJRTFileSystem() throws Exception {
    final URI uri = URI.create("jrt:/");
    try {
      // Tests run on the JVM, so we cannot directly access JRTFileSystemProvider
      // But this method will work
      return FileSystems.getFileSystem(uri);
    } catch (Throwable th) {

      // When on ART, JRT file system is not available
      // So we create an instance of the FS manually
      final Class<?> klass = Class.forName("jdk.internal.jrtfs.JrtFileSystemProvider");
      final Method newFs = klass.getMethod("newFileSystem", URI.class, Map.class);
      newFs.setAccessible(true);
      return (FileSystem) newFs.invoke(klass.newInstance(), uri, Collections.emptyMap());
    }
  }

  public Set<String> classPathTopLevelClasses(Set<Path> classPath) {
    LOG.info(
        String.format(
            Locale.getDefault(),
            "Searching for top-level classes in %d classpath locations",
            classPath.size()));

    URL[] urls = classPath.stream().map(this::toUrl).toArray(URL[]::new);
    ClassLoader classLoader = new URLClassLoader(urls, null);

    ClassPath scanner;
    try {
      scanner = ClassPath.from(classLoader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Set<String> classes = new HashSet<>();
    for (ClassPath.ClassInfo c : scanner.getTopLevelClasses()) {
      classes.add(c.getName());
    }

    LOG.info(String.format(Locale.ROOT, "Found %d classes in classpath", classes.size()));

    return classes;
  }

  private URL toUrl(@NonNull Path p) {
    try {
      return p.toUri().toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
