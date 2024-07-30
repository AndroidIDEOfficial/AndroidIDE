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

import com.itsaky.androidide.levelhash.DataExternalizer
import com.itsaky.androidide.levelhash.HashT
import com.itsaky.androidide.levelhash.LevelHashFn
import com.itsaky.androidide.levelhash.LevelSlot
import java.io.File
import kotlin.math.pow

/**
 * An file-based persistent implementation of [AbstractLevelHash].
 *
 * @author Akash Yadav
 */
internal class PersistentLevelHash<K : Any, V : Any?>(
  levelSize: Int,
  bucketSize: Int,
  uniqueKeys: Boolean,
  autoExpand: Boolean,
  levelHashFn: LevelHashFn<K>,
  seeds: Pair<HashT, HashT>,
  keyExternalizer: DataExternalizer<K>,
  valueExternalizer: DataExternalizer<V>,
  indexFile: File,
) : AbstractLevelHash<K, V>(
  levelSize = levelSize,
  bucketSize = bucketSize,
  uniqueKeys = uniqueKeys,
  autoExpand = autoExpand,
  levelHashFn = levelHashFn,
  seeds = seeds
) {

  private val io =
    PersistentLevelHashIO(indexFile, keyExternalizer, valueExternalizer)

  override fun getSlot(levelIdx: Int, bucketIdx: Int, slotIdx: Int
  ): LevelSlot<K, V> {
    return object : LevelSlot<K, V> {
      override val key: K
        get() = checkNotNull(io.readKey(levelIdx, bucketIdx, slotIdx))

      override val value: V?
        get() = io.readValue(levelIdx, bucketIdx, slotIdx)

      override fun isOccupied(): Boolean {
        return io.isOccupied(levelIdx, bucketIdx, slotIdx)
      }

      override fun reset(key: K?, value: V?) {
        io.writeEntry(levelIdx, bucketIdx, slotIdx, key, value)
      }
    }
  }

  override fun clear() {
    this.io.clear()
  }

  override fun close() {
    io.close()
  }

  private var interimBucketCount = -1
  override fun prepareExpansion(bucketCount: Int) {
    check(this.interimBucketCount == -1)
    this.interimBucketCount = bucketCount
  }

  override fun tryMoveToInterim(
    slot: LevelSlot<K, V>,
    bucketIdx: Int,
    slotIdx: Int
  ): Boolean {
    TODO()
  }

  override fun onExpand(newLevelSize: Int, interimItemCount: Int) {
    this.levelSize = newLevelSize
    this.topLevelBucketCount = 2.0.pow(newLevelSize).toInt()
  }
}