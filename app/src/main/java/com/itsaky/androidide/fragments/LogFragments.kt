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

package com.itsaky.androidide.fragments

import com.itsaky.androidide.utils.ILogger

/**
 * Fragment to show IDE logs.
 * @author Akash Yadav
 */
class IDELogFragment : LogViewFragment() {
  private var logListener: ILogger.LogListener? =
    ILogger.LogListener { priority, tag, message ->
      if (message.contains("\n")) {
        val split = message.split("\n").toTypedArray()
        for (line in split) {
          logLine(priority, tag, line)
        }
      } else {
        logLine(priority, tag, message)
      }
    }

  init {
    ILogger.addLogListener(this.logListener)
  }

  override fun isSimpleFormattingEnabled() = true
  override fun getFilename() = "ide_logs"

  override fun onDestroy() {
    super.onDestroy()
    ILogger.removeLogListener(logListener)
    logListener = null
  }
}

/**
 * Fragment to show application logs.
 * @author Akash Yadav
 */
class AppLogFragment : LogViewFragment() {
  override fun isSimpleFormattingEnabled() = false
  override fun getFilename() = "app_logs"
}
