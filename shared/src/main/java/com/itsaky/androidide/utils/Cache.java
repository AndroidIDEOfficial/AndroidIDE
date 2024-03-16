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

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache maps a file + an arbitrary key to a value. When the file is modified, the mapping expires.
 */
public class Cache<K, V> {

  private static final Logger LOG = LoggerFactory.getLogger(Cache.class);
  // Cannot access FileManager from this module
  private static Object FileManager_INSTANCE;
  private static Method FileManager_getLastModified;
  private final Map<Key<K>, Value> map = new HashMap<>();

  public boolean has(Path file, K k) {
    return !needs(file, k);
  }

  public boolean needs(Path file, K k) {
    // If key is not in map, it needs to be loaded
    Key<K> key = new Key<>(file, k);
    if (!map.containsKey(key)) {
      return true;
    }

    // If key was loaded before file was last modified, it needs to be reloaded
    Value value = map.get(key);
    if (value == null) {
      return true;
    }

    Instant modified = getLastModified(file);
    // TODO remove all keys associated with file when file changes
    return value.created.isBefore(modified);
  }

  private static Instant getLastModified(Path file) {
    initProjectManagerInstance();

    try {
      return (Instant) FileManager_getLastModified.invoke(FileManager_INSTANCE, file);
    } catch (Throwable err) {
      LOG.error("Cannot get last modified from ProjectManager", err);
      return Instant.now();
    }
  }

  private static void initProjectManagerInstance() {
    if (FileManager_INSTANCE == null) {
      try {
        final var klass = Class.forName("com.itsaky.androidide.projects.FileManager");
        final var field = klass.getDeclaredField("INSTANCE");
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }

        FileManager_INSTANCE = field.get(null);

        FileManager_getLastModified = klass.getDeclaredMethod("getLastModified", Path.class);
        if (!FileManager_getLastModified.isAccessible()) {
          FileManager_getLastModified.setAccessible(true);
        }
      } catch (Throwable err) {
        LOG.error("Cannot reflect ProjectManager INSTANCE", err);
        throw new RuntimeException(err);
      }
    }
  }

  public void load(Path file, K k, V v) {
    // TODO limit total size of cache
    Key<K> key = new Key<>(file, k);
    Value value = new Value(v);
    map.put(key, value);
  }

  public V get(Path file, K k) {
    final Key<K> key = new Key<>(file, k);
    final var val = map.get(key);
    if (val == null) {
      throw new IllegalArgumentException(k + " is not in map " + map);
    }

    return val.value;
  }

  private static class Key<K> {
    final Path file;
    final K key;

    Key(Path file, K key) {
      this.file = file;
      this.key = key;
    }

    @Override
    public int hashCode() {
      return Objects.hash(file, key);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object other) {
      if (other.getClass() != Cache.Key.class) return false;
      Key that = (Cache.Key) other;
      return Objects.equals(this.key, that.key) && Objects.equals(this.file, that.file);
    }
  }

  private class Value {
    final V value;
    final Instant created = Instant.now();

    Value(V value) {
      this.value = value;
    }
  }
}
