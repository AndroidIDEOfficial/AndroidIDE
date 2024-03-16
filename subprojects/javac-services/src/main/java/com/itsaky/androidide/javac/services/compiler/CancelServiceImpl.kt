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

package com.itsaky.androidide.javac.services.compiler

import com.itsaky.androidide.javac.services.CancelService
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Cancel service implementation for the java compiler.
 * @author Akash Yadav
 */
class CancelServiceImpl : CancelService() {
  val cancelled = AtomicBoolean(false)

  /**
   * Sets the cancellation flag.
   *
   * @return `true` if compilation process was running and it was set to be cancelled, `false`
   * otherwise.
   */
  fun cancel(): Boolean {
    ILogger.ROOT.info("...requesting compilation cancellation")
    return !cancelled.getAndSet(true)
  }

  override fun isCanceled(): Boolean = cancelled.get()
}
