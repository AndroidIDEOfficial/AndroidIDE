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

package com.itsaky.androidide.levelhash

/**
 * Level hash is a write-optimized and high-performance hashing index scheme with cost-efficient
 * resizing and low-overhead consistency guarantee for persistent memory.
 *
 * @param K The key type.
 * @param V The value type.
 */
interface LevelHash<K : Any, V : Any?> {

  /**
   * The size of the level size. This is `log2(addrCapacity)`.
   */
  val levelSize: Int

  /**
   * The number of buckets in the top level. The number of buckets in the bottom
   * level is exactly half of this.
   */
  val topLevelBucketCount: Int

  /**
   * The size of each bucket, denoting number of slots in a bucket.
   */
  val bucketSize: Int

  /**
   * The number of buckets in the level hash (including both, top and bottom levels).
   */
  val totalBucketCount: Int
    get() = topLevelBucketCount + (topLevelBucketCount shr 1)

  /**
   * The number of slots in the level hash (including both, top and bottom levels).
   */
  val totalSlotCount: Int
    get() = totalBucketCount * bucketSize

  /**
   * Get the value associated with the given key.
   *
   * @param key The key for the value.
   * @return The value associated with the given key, or `null` if not found.
   */
  operator fun get(key: K): V?

  /**
   * Update the value associated with the given key.
   *
   * @param key The key.
   * @param value The value.
   * @return The old value associated with the given key, or `null` if not found
   * (or if the value was already `null`).
   */
  operator fun set(key: K, value: V): V?

  /**
   * Insert the given key-value pair into the level has.
   *
   * @param key The key.
   * @param value The value.
   * @return Whether the key-value pair was inserted successfully.
   */
  fun insert(key: K, value: V): Boolean

  /**
   * Remove the entry with the given key, if any.
   *
   * @param key The key.
   * @return The (nullable) value associated with the given key, or `null` if
   * not found.
   */
  fun remove(key: K): V?

  /**
   * Clear the level hash, removing all entries.
   */
  fun clear()

  /**
   * Find the slot with the given key.
   *
   * @param key The key to find.
   * @return The slot if found, or `null`.
   */
  fun findSlot(key: K): LevelSlot<K, V>?

  /**
   * Expand the level hash by the given ADDITIONAL level size.
   *
   * @param addtionalLevelSize The additional level size of the level hash. If this is
   * less than the current level size, an exception is thrown. If this is equal
   * to the current level size, this function has no effect.
   *
   * @return Whether the level hash was resized.
   * @throws IllegalArgumentException If the level size if less than the current
   * level size.
   */
  fun expand(addtionalLevelSize: Int): Boolean

  /**
   * The two levels in a [LevelHash].
   */
  enum class Level(internal val index: Int) {

    TOP(0),
    BOTTOM(1)
  }

  companion object {

    /**
     * The default level size.
     */
    const val BUCKET_SIZE_DEFAULT = 4

    /**
     * Build an in-memory level hash.
     */
    fun <K : Any, V : Any?> inMemoryHashBuilder(): InMemoryHashBuilder<K, V> =
      InMemoryHashBuilder()

    /**
     * Build a persistent level hash.
     */
    fun <K : Any, V : Any?> persistentHashBuilder(): PersistentHashBuilder<K, V> =
      PersistentHashBuilder()
  }

  open class LevelHashException(msg: String) : RuntimeException(msg)

  /**
   * A [RuntimeException] thrown when *insert* operation fails in a [LevelHash].
   */
  open class InsertionError(msg: String) : LevelHashException(msg)

  /**
   * An [InsertionError] which is thrown when trying to insert a key in a [LevelHash] and an entry
   * with the same key already exists.
   */
  class DuplicateKeyException(key: Any) :
    InsertionError("Key '$key' already exists")

  /**
   * An [InsertionError] which is thrown when the level hash is full and unable
   * to insert new values.
   */
  class OverflowException : InsertionError("Level hash has reached is capacity")

  /**
   * Thrown when the level hash cannot be resized.
   */
  class ResizeFailure(msg: String) : InsertionError(msg)

  /**
   * Thrown when *update* operation fails to find the slot in a [LevelHash].
   */
  class EntryNotFoundException(key: Any) :
    LevelHashException("Entry not found for key '$key'")
}