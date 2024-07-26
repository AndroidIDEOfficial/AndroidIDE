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

import com.itsaky.androidide.levelhash.LevelBucket
import com.itsaky.androidide.levelhash.LevelSlot

/**
 * @author Akash Yadav
 */
internal class InMemoryLevelBucket<K : Any, V : Any?> internal constructor(
  private val size: Int,
) : LevelBucket<K, V> {

  private val slots = Array(size) {
    InMemoryLevelSlot.newInstance<K, V>()
  }

  override fun getSlot(index: Int): LevelSlot<K, V> {
    checkIndex(index)
    return slots[index]
  }

  private fun checkIndex(index: Int) {
    require(index in 0..<size) { "Index must be in [0, $size)" }
  }
}