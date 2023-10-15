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

package com.itsaky.androidide.treesitter.api

import java.util.concurrent.TimeUnit

/**
 * Synchronization for tree sitter objects.
 *
 * @author Akash Yadav
 */
interface TSSynchronized {

  /**
   * Acquires the lock. Throws an [IllegalStateException] if the lock can not be acquired within
   * the given [timeout].
   *
   * @param timeout The timeout duration.
   * @param unit The time unit for the timeout duration.
   * @return Whether the lock was held or not.
   */
  fun lock(timeout: Long = 3, unit: TimeUnit = TimeUnit.SECONDS): Boolean

  /**
   * Releases the lock.
   */
  fun unlock()

  /**
   * Execute the given [action] with the lock. The lock will be held before executing the action
   * and finally released. Throws an [IllegalStateException] if the lock can not be acquired within
   *    * the given [timeout].
   *
   * @param timeout The timeout duration.
   * @param unit The time unit for the timeout duration.
   * @param action The action to perform.
   */
  fun <T : Any?> withLock(timeout: Long = 3, unit: TimeUnit = TimeUnit.SECONDS, action: () -> T): T
}