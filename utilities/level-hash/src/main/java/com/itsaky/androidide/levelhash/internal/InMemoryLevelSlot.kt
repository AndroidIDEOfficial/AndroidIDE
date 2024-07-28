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

import com.itsaky.androidide.levelhash.LevelSlot
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.withLock

/**
 * @author Akash Yadav
 */
internal class InMemoryLevelSlot<K, V> private constructor(
  private var _key: K? = null,
  private var _value: V? = null,
) : LevelSlot<K, V> {

  private val lock = ReentrantReadWriteLock()

  override val key: K
    get() = lock.readLock().withLock { checkNotNull(_key) }

  override val value: V?
    get() = lock.readLock().withLock { _value }

  companion object {

    /**
     * Create a new slot.
     */
    fun <K, V> newInstance(): InMemoryLevelSlot<K, V> {
      return InMemoryLevelSlot()
    }
  }

  /**
   * Whether the slot is occupied.
   */
  override fun isOccupied(): Boolean = lock.readLock().withLock { _key != null }

  /**
   * Reset the value of this entry.
   */
  override fun reset(key: K?, value: V?) {
    lock.writeLock().withLock {
      _key = key
      _value = value
    }
  }
}