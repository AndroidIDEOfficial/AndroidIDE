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
 * A slot in a [LevelBucket].
 *
 * @author Akash Yadav
 */
interface LevelSlot<K, V> {

  /**
   * The key for this slot.
   */
  val key: K

  /**
   * The value for this slot.
   */
  val value: V?

  /**
   * Whether the slot is occupied.
   */
  fun isOccupied(): Boolean

  /**
   * Reset the value of this entry.
   *
   * @param key The new key.
   * @param value The new value.
   */
  fun reset(
    key: K? = null,
    value: V? = null,
  )
}