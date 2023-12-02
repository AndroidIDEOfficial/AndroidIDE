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

package com.itsaky.androidide.progress

import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Check whether a process is cancelled.
 *
 * @author Akash Yadav
 */
interface ICancelChecker {

  /**
   * Cancel this process.
   */
  fun cancel()

  /**
   * Check whether this process has been cancelled or not.
   *
   * @return Whether the process has been cancelled.
   */
  fun isCancelled(): Boolean

  /**
   * Throw [CancellationException] if this process has been cancelled.
   */
  @Throws(CancellationException::class)
  fun abortIfCancelled()

  open class Default(cancelled: Boolean = false) : ICancelChecker {

    private val cancelled = AtomicBoolean(cancelled)

    override fun cancel() {
      cancelled.set(true)
    }

    override fun isCancelled(): Boolean {
      return cancelled.get()
    }

    override fun abortIfCancelled() {
      if (isCancelled()) {
        throw CancellationException()
      }
    }
  }

  companion object {

    /**
     * A no-op cancel checker. The task is never cancelled.
     */
    @JvmField
    val NOOP = Default(false)

    /**
     * An already cancelled cancel checker.
     */
    @JvmField
    val CANCELLED = Default(true)
  }
}
