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

import com.itsaky.lsp.java.FileStore;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Cache maps a file + an arbitrary key to a value. When the file is modified, the mapping expires.
 */
public class Cache<K, V> {
  private static class Key<K> {
    final Path file;
    final K key;

    Key(Path file, K key) {
      this.file = file;
      this.key = key;
    }

    @Override
    public boolean equals(Object other) {
      if (other.getClass() != Cache.Key.class) return false;
      Key that = (Cache.Key) other;
      return Objects.equals(this.key, that.key) && Objects.equals(this.file, that.file);
    }

    @Override
    public int hashCode() {
      return Objects.hash(file, key);
    }
  }

  private class Value {
    final V value;
    final Instant created = Instant.now();

    Value(V value) {
      this.value = value;
    }
  }

  private final Map<Key, Value> map = new HashMap<>();

  public boolean has(Path file, K k) {
    return !needs(file, k);
  }

  public boolean needs(Path file, K k) {
    // If key is not in map, it needs to be loaded
    Key key = new Key<K>(file, k);
    if (!map.containsKey(key)) return true;

    // If key was loaded before file was last modified, it needs to be reloaded
    Value value = map.get(key);
    Instant modified = FileStore.modified(file);
    // TODO remove all keys associated with file when file changes
    return value.created.isBefore(modified);
  }

  public void load(Path file, K k, V v) {
    // TODO limit total size of cache
    Key key = new Key<K>(file, k);
    Value value = new Value(v);
    map.put(key, value);
  }

  public V get(Path file, K k) {
    Key key = new Key<K>(file, k);
    if (!map.containsKey(key)) {
      throw new IllegalArgumentException(k + " is not in map " + map);
    }
    return map.get(key).value;
  }
}
