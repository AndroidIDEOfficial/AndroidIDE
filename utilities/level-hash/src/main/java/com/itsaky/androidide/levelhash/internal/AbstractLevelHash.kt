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

import androidx.annotation.VisibleForTesting
import com.itsaky.androidide.levelhash.HashT
import com.itsaky.androidide.levelhash.LevelHash
import com.itsaky.androidide.levelhash.LevelHash.DuplicateKeyException
import com.itsaky.androidide.levelhash.LevelHash.EntryNotFoundException
import com.itsaky.androidide.levelhash.LevelHash.Level
import com.itsaky.androidide.levelhash.LevelHash.OverflowException
import com.itsaky.androidide.levelhash.LevelHash.ResizeFailure
import com.itsaky.androidide.levelhash.LevelHashFn
import com.itsaky.androidide.levelhash.LevelSlot
import org.slf4j.LoggerFactory
import kotlin.math.pow

/**
 * @author Akash Yadav
 */
internal abstract class AbstractLevelHash<K : Any, V : Any?> internal constructor(
  levelSize: Int,
  final override val bucketSize: Int,
  protected val uniqueKeys: Boolean,
  protected val autoExpand: Boolean,
  protected val levelHashFn: LevelHashFn<K>,
  protected val seeds: Pair<HashT, HashT>,
) : LevelHash<K, V> {

  override var levelSize: Int = levelSize
    set(value) {
      require(value <= 30) { "Level size must be <= 30" }
      field = value
    }

  override var topLevelBucketCount = 2.0.pow(levelSize).toInt()

  /**
   * The number of occupied slots in each level.
   */
  @VisibleForTesting
  internal val levelItemCounts = IntArray(LEVEL_COUNT)

  /**
   * The number of times the level hash has been expanded.
   */
  protected var expandCount: Int = 0

  /**
   * The state indicating whether the level hash is being resized or not.
   * 0 - Not resizing
   * 1 - Expanding
   * 2 - Shrinking
   */
  protected var resizeState = RESIZE_STATE_NOT_RESIZING

  protected val logger = LoggerFactory.getLogger(javaClass)

  companion object {

    /**
     * The number of levels in level hash.
     */
    const val LEVEL_COUNT = 2

    const val RESIZE_STATE_NOT_RESIZING = 0
    const val RESIZE_STATE_EXPANDING = 1
    const val RESIZE_STATE_SHRINKING = 2
  }

  /**
   * Get the slot for the given slot position.
   *
   * @param levelIdx The index of the level.
   * @param bucketIdx The index of the bucket in the level.
   * @param slotIdx The index of the slot in the bucket.
   * @return The slot for the given slot position.
   */
  abstract fun getSlot(levelIdx: Int, bucketIdx: Int, slotIdx: Int
  ): LevelSlot<K, V>

  /**
   * Get the slot for the given slot position.
   */
  fun getSlot(level: Level, bucketIdx: Int, slotIdx: Int
  ): LevelSlot<K, V> {
    return getSlot(level.index, bucketIdx, slotIdx)
  }

  override fun loadFactor(): Float {
    return levelItemCounts.sum() / totalSlotCount.toFloat()
  }

  override fun get(key: K): V? {
    return findSlot(key)?.value
  }

  override fun set(key: K, value: V): V? {
    val slot = findSlot(key)
    if (slot != null) {
      val oldValue = slot.value
      slot.reset(key, value)
      return oldValue
    }

    throw EntryNotFoundException(key)
  }

  override fun insert(key: K, value: V): Boolean {

    if (loadFactor() >= 0.92 && autoExpand) {
      check(expand()) {
        "Failed to expand the level hash"
      }
    }

    if (loadFactor() == 1f) {
      throw OverflowException(totalSlotCount, levelItemCounts.sum())
    }

    val insertFn =
      fun(levelIdx: Int, bucketIdx: Int, slotIdx: Int): Boolean {
        val slot = getSlot(levelIdx, bucketIdx, slotIdx)
        if (!slot.isOccupied()) {
          slot.reset(key, value)
          levelItemCounts[levelIdx]++
          return true
        } else if (uniqueKeys && getSlot(levelIdx, bucketIdx,
            slotIdx).key == key
        ) {
          throw DuplicateKeyException(key)
        }
        return false
      }

    val fhash = fhash(key)
    val shash = shash(key)

    // Check if there are any empty slots available in any of the levels
    // If there are, insert the key-value pair and return true
    for (i in 0..<LEVEL_COUNT) {
      val fidx = buckIdxLvl(fhash, i)
      val sidx = buckIdxLvl(shash, i)
      for (j in 0..<bucketSize) {
        if (insertFn(i, fidx, j) || insertFn(i, sidx, j)) {
          return true
        }
      }
    }

    for (i in 0..<LEVEL_COUNT) {
      val fidx = buckIdxLvl(fhash, i)
      val sidx = buckIdxLvl(shash, i)
      if (tryMovement(i, fidx, key, value)) {
        return true
      }
      if (tryMovement(i, sidx, key, value)) {
        return true
      }
    }

    if (expandCount > 0) {
      val fidx = buckIdxLvl(fhash, Level.TOP.index)
      val sidx = buckIdxLvl(shash, Level.TOP.index)
      var emptyLocation = b2tMovement(fidx)
      if (emptyLocation != -1) {
        val emptySlot = getSlot(Level.TOP, fidx, emptyLocation)
        emptySlot.reset(key, value)
        levelItemCounts[Level.TOP.index]++
        return true
      }

      emptyLocation = b2tMovement(sidx)
      if (emptyLocation != -1) {
        val emptySlot = getSlot(Level.TOP, sidx, emptyLocation)
        emptySlot.reset(key, value)
        levelItemCounts[Level.TOP.index]++
        return true
      }
    }

    return false
  }

  override fun remove(key: K): V? {
    val slot = findSlot(key)
    if (slot != null) {
      val oldValue = slot.value
      slot.reset(null, null)
      return oldValue
    }
    return null
  }

  override fun findSlot(key: K): LevelSlot<K, V>? {
    val fhash = fhash(key)
    val shash = shash(key)

    var init = 0
    var lim = LEVEL_COUNT
    var step = 1

    if (levelItemCounts[0] < levelItemCounts[1]) {
      // if there are more occupied slots in the bottom level
      // than in the top level, then scan the bottom level first
      init = LEVEL_COUNT - 1
      lim = -1
      step = -1
    }

    var i = init
    while (i != lim) {
      val fidx = buckIdxLvl(fhash, i)
      val sidx = buckIdxLvl(shash, i)
      for (j in 0..<bucketSize) {
        var slot = getSlot(i, fidx, j)
        if (slot.isOccupied() && slot.key == key) {
          return slot
        }

        slot = getSlot(i, sidx, j)
        if (slot.isOccupied() && slot.key == key) {
          return slot
        }
      }

      i += step
    }

    return null
  }

  override fun expand(): Boolean {
    check(this.resizeState == RESIZE_STATE_NOT_RESIZING) {
      "Level hash is already being resized. Maybe on a different thread?"
    }

    try {
      this.resizeState = RESIZE_STATE_EXPANDING
      return doExpand(this.levelSize + 1)
    } finally {
      this.resizeState = RESIZE_STATE_NOT_RESIZING
    }
  }

  private fun doExpand(levelSize: Int): Boolean {
    val newTopLevelCapacity = 2.0.pow(levelSize).toInt()
    var newLevelItemCount = 0

    prepareExpansion(newTopLevelCapacity)

    for (oldBuckIdx in 0..<(this.topLevelBucketCount shr 1)) {
      for (oldSlotIdx in 0..<this.bucketSize) {
        val oldSlot = getSlot(Level.BOTTOM, oldBuckIdx, oldSlotIdx)
        if (!oldSlot.isOccupied()) {
          continue
        }

        val fhash = fhash(oldSlot.key)
        val shash = shash(oldSlot.key)
        val fidx = buckIdxCap(fhash, newTopLevelCapacity)
        val sidx = buckIdxCap(shash, newTopLevelCapacity)

        var insertSuccess = false
        for (newSlotIdx in 0..<this.bucketSize) {
          if (moveForExpansion(oldSlot, fidx, newSlotIdx) || moveForExpansion(
              oldSlot, sidx, newSlotIdx)
          ) {
            insertSuccess = true
            newLevelItemCount++
            break
          }
        }

        if (!insertSuccess) {
          throw ResizeFailure("Failed to move slot to the interim level")
        }

        oldSlot.reset(null, null)
      }
    }

    this.levelSize = levelSize
    this.topLevelBucketCount = 2.0.pow(levelSize).toInt()

    onExpand(levelSize, newLevelItemCount)

    this.levelItemCounts[1] = this.levelItemCounts[0]
    this.levelItemCounts[0] = newLevelItemCount
    this.expandCount++

    return true
  }

  /**
   * Prepare the interim level for a expansion, ensuring that it can hold at least
   * [bucketCount] buckets.
   *
   * @param bucketCount The bucket count of the interim level.
   */
  protected open fun prepareExpansion(bucketCount: Int) {}

  /**
   * Called to notify that the expand operation was successful.
   *
   * @param newLevelSize The new level size of the level hash.
   * @param interimItemCount The number of slot that are occupied in the
   * interim level.
   */
  protected open fun onExpand(newLevelSize: Int, interimItemCount: Int) {}

  /**
   * Move the given slot to the interim level at the given slot in the given bucket.
   * The caller does not check whether the slot is occupied or not. If the given
   * slot position is already occupied, this method must return `false`.
   *
   * @param slot The existing slot to move.
   * @param bucketIdx The index of the bucket in the interim level where the slot
   * should be moved.
   * @param slotIdx The index of the slot bucket of the interim level where the
   * slot should be moved.
   * @return Whether the slot was moved to the interim level.
   */
  protected abstract fun moveForExpansion(slot: LevelSlot<K, V>,
                                  bucketIdx: Int, slotIdx: Int
  ): Boolean

  /**
   * Try to move an item from the current bucket to its same-level alternative bucket.
   *
   * @param levelIndex The index of the level.
   * @param bucketIndex The index of the bucket.
   * @param key The key to insert.
   * @param value The value to associate with key.
   */
  private fun tryMovement(
    levelIndex: Int,
    bucketIndex: Int,
    key: K,
    value: V,
  ): Boolean {
    for (i in 0..<bucketSize) {
      val thisSlot = getSlot(levelIndex, bucketIndex, i)
      val thisKey = thisSlot.key
      val thisValue = thisSlot.value

      val fhash = fhash(thisKey)
      val shash = shash(thisKey)
      val fidx = buckIdxLvl(fhash, levelIndex)
      val sidx = buckIdxLvl(shash, levelIndex)

      val jidx = if (fidx == bucketIndex) sidx else fidx

      for (j in 0..<bucketSize) {
        val thatSlot = getSlot(levelIndex, jidx, j)
        if (!thatSlot.isOccupied()) {
          thatSlot.reset(thisKey, thisValue)
          thisSlot.reset(key, value)
          levelItemCounts[levelIndex]++
          return true
        }
      }
    }

    return false
  }

  /**
   * Try to move an item from a bottom-level to its top-level alternative buckets.
   *
   * @param bucketIndex The index of the bucket in the bottom level.
   * @return The index of the slot in the bucket at [bucketIndex] which was moved to the top-level.
   */
  private fun b2tMovement(bucketIndex: Int): Int {

    val b2tMover =
      fun(bottomSlot: LevelSlot<K, V>, topSlot: LevelSlot<K, V>): Boolean {
        if (!topSlot.isOccupied()) {
          topSlot.reset(bottomSlot.key, bottomSlot.value)
          bottomSlot.reset(null, null)
          levelItemCounts[Level.TOP.index]++
          levelItemCounts[Level.BOTTOM.index]--
          return true
        }

        return false
      }

    for (i in 0..<bucketSize) {
      val bottomSlot = getSlot(Level.BOTTOM, bucketIndex, i)
      val fhash = fhash(bottomSlot.key)
      val shash = shash(bottomSlot.key)
      val fidx = buckIdxLvl(fhash, Level.TOP.index)
      val sidx = buckIdxLvl(shash, Level.TOP.index)

      for (j in 0..<bucketSize) {
        var topSlot = getSlot(Level.TOP, fidx, j)
        if (b2tMover(bottomSlot, topSlot)) {
          return i
        }

        topSlot = getSlot(Level.TOP, sidx, j)
        if (b2tMover(bottomSlot, topSlot)) {
          return i
        }
      }
    }
    return -1
  }

  private fun fhash(key: K): HashT {
    return levelHashFn(key, seeds.first)
  }

  private fun shash(key: K): HashT {
    return levelHashFn(key, seeds.second)
  }

  private fun buckIdxLvl(keyHash: HashT, levelIndex: Int): Int {
    require(levelIndex == 0 || levelIndex == 1)
    var capacity = topLevelBucketCount
    if (levelIndex == Level.BOTTOM.index) {
      // divide by 2
      capacity = capacity shr 1
    }

    return buckIdxCap(keyHash, capacity)
  }

  private fun buckIdxCap(keyHash: HashT, capacity: Int): Int {
    // since capacity is a power of two and key hash is unsigned
    // keyHash % capacity can be simplified with simple bit shift operation
    return (keyHash and (capacity - 1).toULong()).toInt()
  }
}