package com.itsaky.lsp.java;

import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Cache maps a file + an arbitrary key to a value. When the file is modified, the mapping expires. */
class Cache<K, V> {
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

    boolean has(Path file, K k) {
        return !needs(file, k);
    }

    boolean needs(Path file, K k) {
        // If key is not in map, it needs to be loaded
        Key key = new Key<K>(file, k);
        if (!map.containsKey(key)) return true;

        // If key was loaded before file was last modified, it needs to be reloaded
        Value value = map.get(key);
        Instant modified = FileStore.modified(file);
        // TODO remove all keys associated with file when file changes
        return value.created.isBefore(modified);
    }

    void load(Path file, K k, V v) {
        // TODO limit total size of cache
        Key key = new Key<K>(file, k);
        Value value = new Value(v);
        map.put(key, value);
    }

    V get(Path file, K k) {
        Key key = new Key<K>(file, k);
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException(k + " is not in map " + map);
        }
        return map.get(key).value;
    }
}
