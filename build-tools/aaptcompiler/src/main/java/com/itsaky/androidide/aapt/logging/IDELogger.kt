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

package com.itsaky.androidide.aapt.logging

import com.android.utils.StdLogger
import com.android.utils.StdLogger.Level.VERBOSE

object IDELogger : StdLogger(VERBOSE) {

  var enabled = false

  override fun error(t: Throwable?, errorFormat: String?, vararg args: Any?) {
    if (!enabled) {
      return
    }
    super.error(t, errorFormat, *args)
  }

  override fun warning(warningFormat: String?, vararg args: Any?) {
    if (!enabled) {
      return
    }

    super.warning(warningFormat, *args)
  }

  override fun quiet(msgFormat: String?, vararg args: Any?) {
    if (!enabled) {
      return
    }

    super.quiet(msgFormat, *args)
  }

  override fun lifecycle(msgFormat: String?, vararg args: Any?) {
    if (!enabled) {
      return
    }

    super.lifecycle(msgFormat, *args)
  }

  override fun info(msgFormat: String?, vararg args: Any?) {
    if (!enabled) {
      return
    }

    super.info(msgFormat, *args)
  }

  override fun verbose(msgFormat: String?, vararg args: Any?) {
    if (!enabled) {
      return
    }

    super.verbose(msgFormat, *args)
  }
}
