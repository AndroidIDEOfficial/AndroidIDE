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

package com.itsaky.androidide.levelhash.internal

import com.google.common.base.Preconditions
import com.itsaky.androidide.levelhash.HashT
import com.itsaky.androidide.levelhash.LevelHash.Level
import com.itsaky.androidide.levelhash.LevelHashFn
import com.itsaky.androidide.levelhash.LevelSlot
import kotlin.math.pow

/**
 * An in-memory implementation of [AbstractLevelHash].
 *
 * @author Akash Yadav
 */
internal class InMemoryLevelHash<K : Any, V : Any?>(
  levelSize: Int,
  bucketSize: Int,
  uniqueKeys: Boolean,
  autoExpand: Boolean,
  levelHashFn: LevelHashFn<K>,
  seeds: Pair<HashT, HashT>,
) : AbstractLevelHash<K, V>(levelSize, bucketSize, uniqueKeys, autoExpand,
  levelHashFn, seeds) {

  private val levels = Array(LEVEL_COUNT) { levelIndex ->
    allocateLevel(levelSize, levelIndex, bucketSize)
  }

  private fun allocateLevel(levelSize: Int, levelIndex: Int, bucketSize: Int
  ) = List(2.0.pow(levelSize - levelIndex).toInt()) {
    InMemoryLevelBucket<K, V>(size = bucketSize)
  }

  private var interimLevel: List<InMemoryLevelBucket<K, V>>? = null

  /**
   * Get the bucket at the given position.
   *
   * @param level The level of the bucket to retrieve.
   * @param bucketIndex The index of the bucket in [level].
   */
  fun getBucket(level: Level, bucketIndex: Long): InMemoryLevelBucket<K, V> {
    return getBucket(level.index, bucketIndex)
  }

  /**
   * Get the bucket at the given position.
   *
   * @param levelIndex The index of the level of the bucket to retrieve.
   * @param bucketIndex The index of the bucket.
   */
  fun getBucket(levelIndex: Int, bucketIndex: Long): InMemoryLevelBucket<K, V> {
    Preconditions.checkElementIndex(levelIndex, LEVEL_COUNT)
    Preconditions.checkElementIndex(bucketIndex.toInt(),
      levels[levelIndex].size)
    return levels[levelIndex][bucketIndex.toInt()]
  }

  override fun clear() {
    // reallocate levels, and let the GC handle the rest
    for (i in 0 ..< LEVEL_COUNT) {
      levels[i] = allocateLevel(levelSize, i, bucketSize)
    }

    this.interimLevel = null
  }

  override fun getSlot(levelIdx: Int, bucketIdx: Int, slotIdx: Int
  ): LevelSlot<K, V> {
    return levels[levelIdx][bucketIdx].getSlot(slotIdx)
  }

  override fun prepareExpansion(bucketCount: Int) {
    check(interimLevel == null) {
      "Interim level must have been null. Is the level hash being resized on multiple threads?"
    }

    this.interimLevel = List(bucketCount) { InMemoryLevelBucket(bucketSize) }
  }

  override fun tryMoveToInterim(slot: LevelSlot<K, V>, bucketIdx: Int,
                                slotIdx: Int
  ): Boolean {
    val interimLevel = checkNotNull(interimLevel)
    slot as InMemoryLevelSlot<K, V>

    val interimSlot = interimLevel[bucketIdx].getSlot(slotIdx)
    if (interimSlot.isOccupied()) {
      return false
    }

    interimSlot.reset(slot.key, slot.value)
    slot.reset(null, null)

    return true
  }

  override fun onExpand(newLevelSize: Int, interimItemCount: Int) {
    this.levelSize = newLevelSize
    this.topLevelBucketCount = 2.0.pow(newLevelSize).toInt()

    // after expand, interim level becomes the new top level
    // and the current top level becomes the new bottom level
    // the current bottom level is released
    this.levels[Level.BOTTOM.index] = levels[Level.TOP.index]
    this.levels[Level.TOP.index] = checkNotNull(this.interimLevel)
    this.interimLevel = null
  }
}