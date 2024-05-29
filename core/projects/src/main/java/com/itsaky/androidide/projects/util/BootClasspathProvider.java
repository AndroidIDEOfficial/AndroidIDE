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

package com.itsaky.androidide.projects.util;

import com.itsaky.androidide.projects.classpath.JarFsClasspathReader;
import com.itsaky.androidide.utils.ClassTrie;
import com.itsaky.androidide.utils.StopWatch;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides class names from the boot classpath (i.e. android.jar).
 *
 * @author Akash Yadav
 */
public class BootClasspathProvider {

  private static final Map<String, ClassTrie> bootClasspathClasses = new ConcurrentHashMap<>();
  private static final Logger LOG = LoggerFactory.getLogger(BootClasspathProvider.class);

  /**
   * Updates the boot classpath cache. If a classpath is already indexed, it is skipped.
   *
   * @param classpaths The full file paths of JAR files to index.
   * @return <code>true</code> if the the classpath cache was updated. <code>false</code> otherwise.
   * <p>This method <code>true</code> if and only if any new classpath classes are added into
   * the cache.
   */
  public static synchronized boolean update(Collection<String> classpaths) {
    final var watch = new StopWatch("Indexing " + classpaths.size() + " bootclasspaths");
    var count = 0;
    for (final var classpath : classpaths) {
      if (bootClasspathClasses.containsKey(classpath)) {
        LOG.info("Skipping indexing for boot classpath as it is already indexed: {}", classpath);
        continue;
      }

      LOG.debug("Indexing boot classpath: {}", classpath);
      final var classes =
          new JarFsClasspathReader().listClasses(Collections.singleton(new File(classpath)));
      final var trie = new ClassTrie();
      for (final var info : classes) {
        if (!info.isTopLevel()) {
          continue;
        }

        trie.append(info.getName());
      }

      bootClasspathClasses.put(classpath, trie);
      count += classes.size();
    }

    watch.log();
    return count > 0;
  }

  /**
   * Drops entries for all the given classpaths from the cache.
   *
   * @param classpaths The classpaths to drop entry for.
   */
  public static synchronized void dropAll(Collection<String> classpaths) {
    classpaths.forEach(BootClasspathProvider::drop);
  }

  /**
   * Drops the entry for the given classpath from the cache.
   *
   * @param classpath The classpath to drop entry for.
   */
  public static synchronized void drop(String classpath) {
    bootClasspathClasses.remove(classpath);
  }

  /**
   * Returns all cached <strong>top-level</strong> classes from all the given classpath locations.
   *
   * @param classpaths The classpaths to get class list from.
   * @return The cached <strong>top-level</strong> classes from the given classpaths.
   */
  public static synchronized Set<String> getTopLevelClasses(Collection<String> classpaths) {
    final var result = new TreeSet<String>();
    if (classpaths == null || classpaths.isEmpty()) {
      return result;
    }

    for (final String classpath : classpaths) {
      final var trie = bootClasspathClasses.get(classpath);
      if (trie == null) {
        continue;
      }

      result.addAll(trie.allClassNames());
    }

    return result;
  }

  /**
   * Returns all the {@link ClassTrie} entries.
   *
   * @return All {@link ClassTrie} entries.
   */
  public static Collection<ClassTrie> getAllEntries() {
    return bootClasspathClasses.values();
  }
}
