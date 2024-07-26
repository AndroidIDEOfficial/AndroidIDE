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

import com.itsaky.androidide.levelhash.LevelHash.Companion.BUCKET_SIZE_DEFAULT
import com.itsaky.androidide.levelhash.internal.InMemoryLevelHash
import com.itsaky.androidide.levelhash.internal.PersistentLevelHash
import com.sun.jna.Platform
import java.io.File

/**
 * Builder used to build instances of [LevelHash].
 */
abstract class AbstractLevelHashBuilder<K : Any, V : Any?> {

  protected var levelSize = -1
  protected var bucketSize = BUCKET_SIZE_DEFAULT
  protected var hashFn: LevelHashFn<K>? = null
  protected var uniqueKeys = false
  protected var autoExpand = true
  protected var seedGenerator: SeedGeneratorFn = ::levelHashDefaultSeeds

  /**
   * Set the level size.
   *
   * @param levelSize The level size.
   */
  fun levelSize(levelSize: Int) = apply {
    this.levelSize = levelSize
  }

  /**
   * Set the bucket size.
   *
   * @param bucketSize The number of slots in a bucket.
   */
  fun bucketSize(bucketSize: Int) = apply {
    this.bucketSize = bucketSize
  }

  /**
   * Set the hash function.
   *
   * @param hashFn The hash function.
   */
  fun hashFn(hashFn: LevelHashFn<K>) = apply {
    this.hashFn = hashFn
  }

  /**
   * Set the unique keys flag.
   *
   * @param uniqueKeys Whether the keys in the level hash must be unique.
   */
  fun uniqueKeys(uniqueKeys: Boolean) = apply {
    this.uniqueKeys = uniqueKeys
  }

  /**
   * Set the auto expand flag.
   *
   * @param autoExpand Whether the level hash should auto expand.
   */
  fun autoExpand(autoExpand: Boolean) = apply {
    this.autoExpand = autoExpand
  }

  /**
   * Set the seed generator.
   *
   * @param seedGenerator The seed generator.
   */
  fun seedGenerator(seedGenerator: SeedGeneratorFn) = apply {
    this.seedGenerator = seedGenerator
  }

  /**
   * Build the level hash instance.
   */
  abstract fun build(): LevelHash<K, V>

  protected fun baseChecks() {
    require(this.levelSize > 0) { "Level size must be > 0" }
    require(this.bucketSize in 1..32) { "Slot size must be in range [1, 33)" }
    requireNotNull(this.hashFn) { "Hash function must be set" }
  }
}

class InMemoryHashBuilder<K : Any, V : Any?> :
  AbstractLevelHashBuilder<K, V>() {

  override fun build(): LevelHash<K, V> {
    baseChecks()
    return InMemoryLevelHash(
      levelSize = levelSize,
      bucketSize = bucketSize,
      levelHashFn = hashFn!!,
      seeds = seedGenerator(),
      uniqueKeys = uniqueKeys,
      autoExpand = autoExpand
    )
  }
}

class PersistentHashBuilder<K : Any, V : Any?> :
  AbstractLevelHashBuilder<K, V>() {

  private var keyExternalizer: DataExternalizer<K>? = null
  private var valueExternalizer: DataExternalizer<V>? = null
  private var indexFile: File? = null

  init {
    // Persistent level hash is only supported on Linux
    check(Platform.isLinux())
  }

  /**
   * Set the index file where the index will be stored.
   *
   * @param indexFile The index directory.
   */
  fun indexFile(indexFile: File) = apply {
    this.indexFile = indexFile
  }

  /**
   * Set the key exernalizer.
   *
   * @param externalizer The [DataExternalizer] for keys.
   */
  fun keyExternalizer(externalizer: DataExternalizer<K>) = apply {
    this.keyExternalizer = externalizer
  }

  /**
   * Set the value externalizer.
   *
   * @param externalizer The [DataExternalizer] for values.
   */
  fun valueExternalizer(externalizer: DataExternalizer<V>) = apply {
    this.valueExternalizer = externalizer
  }

  override fun build(): LevelHash<K, V> {
    baseChecks()
    val keyExternalizer =
      requireNotNull(this.keyExternalizer) { "Key externalizer must be set" }
    val valueExternalizer = requireNotNull(
      this.valueExternalizer) { "Value externalizer must be set" }

    val seeds = seedGenerator()

    return PersistentLevelHash(
      levelSize = levelSize,
      bucketSize = bucketSize,
      uniqueKeys = uniqueKeys,
      autoExpand = autoExpand,
      levelHashFn = hashFn!!,
      seeds = seeds,
      keyExternalizer = keyExternalizer,
      valueExternalizer = valueExternalizer,
      indexFile = indexFile!!
    )
  }
}