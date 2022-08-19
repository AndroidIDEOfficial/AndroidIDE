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

import com.android.utils.ILogger

/**
 * Implementation of [ILogger] which redirects all logs to AndroidIDE's logger implementation i.e.
 * [com.itsaky.androidide.utils.ILogger].
 *
 * @author Akash Yadav
 */
object IDELogger : ILogger {

  private val log = com.itsaky.androidide.utils.ILogger.newInstance("AAPTCompilerLogger")

  override fun error(t: Throwable?, msgFormat: String?, vararg args: Any?) {
    if (msgFormat != null) {
      log.error(String.format(msgFormat, args))
    }
    if (t != null) {
      log.error(t)
    }
  }

  override fun warning(msgFormat: String?, vararg args: Any?) {
    if (msgFormat != null) {
      log.warn(String.format(msgFormat, args))
    }
  }

  override fun info(msgFormat: String?, vararg args: Any?) {
    if (msgFormat != null) {
      log.info(String.format(msgFormat, args))
    }
  }

  override fun verbose(msgFormat: String?, vararg args: Any?) {
    if (msgFormat != null) {
      log.verbose(String.format(msgFormat, args))
    }
  }
}
