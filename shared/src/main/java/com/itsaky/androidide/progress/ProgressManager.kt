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

import com.itsaky.androidide.progress.ICancelChecker.Companion.Default
import java.util.WeakHashMap

/**
 * @author Akash Yadav
 */
class ProgressManager private constructor(){

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
    val checker = threads.remove(Thread.currentThread())
    if (checker != null && checker.isCancelled()) {
      throw ProcessCancelledException()
    }
  }
}