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

package com.itsaky.androidide.lsp.java.utils

import com.itsaky.androidide.javac.services.CancelAbort
import java.util.concurrent.CancellationException

/** @author Akash Yadav */
class CancelChecker {

  companion object {

    @JvmStatic
    fun isCancelled(err: Throwable?): Boolean {
      if (err == null) {
        return false
      }

      return err is CancellationException ||
          err is CancelAbort ||
          isCancelled(err.cause)
    }
  }
}
