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

import com.itsaky.androidide.progress.ICancelChecker.Default
import java.util.WeakHashMap
import java.util.concurrent.CancellationException

/**
 * @author Akash Yadav
 */
class ProgressManager private constructor() {

  private val threads = WeakHashMap<Thread, ICancelChecker>()

  companion object {

    val instance by lazy {
      ProgressManager()
    }

    @JvmStatic
    fun abortIfCancelled() {
      instance.abortIfCancelled()
    }
  }

  fun cancel(thread: Thread) {
    var checker = threads[thread]
    if (checker == null) {
      checker = Default()
    }
    checker.cancel()
    threads[thread] = checker
  }

  @JvmName("internalAbortIfCancelled")
  private fun abortIfCancelled() {
    val thisThread = Thread.currentThread()
    val checker = threads[thisThread]
    if (checker != null && checker.isCancelled()) {
      threads.remove(thisThread)
      throw CancellationException()
    }
  }
}