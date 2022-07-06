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

package com.itsaky.androidide.utils;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides class names from the bootstrap classpath (i.e. android.jar).
 *
 * @author Akash Yadav
 */
public class BootstrapClassesProvider {

  private static Set<String> sCachedClasses;
  private static final ILogger LOG = ILogger.newInstance("BootstrapClassesProvider");

  public static Set<String> bootstrapClasses() {
    if (sCachedClasses != null && !sCachedClasses.isEmpty()) {
      return sCachedClasses;
    }

    final var start = System.currentTimeMillis();
    sCachedClasses = new HashSet<>();

    try {
      final var fs = getJRTFileSystem();
      if (fs == null) {
        LOG.error("Unable to create JRT FileSystem instance");
        return sCachedClasses;
      }

      final var javaBase = fs.getPath("/modules/java.base/");
      try (final var stream = Files.walk(javaBase)) {
        final var it = stream.iterator();
        while (it.hasNext()) {
          final var classFile = it.next();
          final String relative = javaBase.relativize(classFile).toString();
          if (relative.endsWith(".class") && !relative.contains("$")) {
            final String trim = relative.substring(0, relative.length() - ".class".length());
            final String qualifiedName = trim.replace(File.separatorChar, '.');
            sCachedClasses.add(qualifiedName);
          }
        }
        LOG.info(
            "Found",
            sCachedClasses.size(),
            "classes in bootstrap classpath in",
            System.currentTimeMillis() - start,
            "ms");
      } catch (IOException io) {
        LOG.error("An error occurred while listing classes from bootstrap classpath", io);
      }
    } catch (Throwable err) {
      LOG.error("Unable to find classes from bootstrap classpath", err);
    }

    return sCachedClasses;
  }

  @Nullable
  @Contract(" -> new")
  private static FileSystem getJRTFileSystem() throws Exception {
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
}
