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

/**
 * Check whether a process is cancelled.
 *
 * @author Akash Yadav
 */
interface ICancelChecker {
  fun cancel()
  fun isCancelled(): Boolean

  companion object {

    internal class Default(private var cancelled: Boolean = false) : ICancelChecker {

      override fun cancel() {
        cancelled = true
      }

      override fun isCancelled(): Boolean {
        return cancelled
      }
    }
  }
}
