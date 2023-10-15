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
import java.util.concurrent.locks.ReentrantLock

/**
 * @author Akash Yadav
 */
internal class DefaultSynchronized(
  // disabled by default
  private val isEnabled: Boolean = false
) : TSSynchronized {

  private val lock = ReentrantLock()

  override fun lock(timeout: Long, unit: TimeUnit): Boolean {
    return if (isEnabled) {
      require(timeout > 0) { "Timeout must be greater than 0" }
      lock.tryLock() || lock.tryLock(timeout, unit)
    } else {
      false
    }
  }

  override fun unlock() {
    if (isEnabled) {
      lock.unlock()
    }
  }

  override fun <T> withLock(timeout: Long, unit: TimeUnit, action: () -> T): T {
    if (!isEnabled) {
      return action()
    }

    check(lock(timeout, unit)) { "Failed to acquire lock" }
    try {
      return action()
    } finally {
      unlock()
    }
  }
}