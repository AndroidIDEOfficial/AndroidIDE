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

package com.itsaky.androidide.tooling.impl.util

import ch.qos.logback.core.status.Status
import ch.qos.logback.core.status.StatusListener
import ch.qos.logback.core.util.StatusPrinter
import com.itsaky.androidide.tooling.api.messages.LogMessageParams
import com.itsaky.androidide.tooling.impl.Main

/**
 * @author Akash Yadav
 */
class LogbackStatusListener : StatusListener {

  companion object {

    private fun levelChar(level: Int): Char {
      return when (level) {
        Status.ERROR -> 'E'
        Status.WARN -> 'W'
        Status.INFO -> 'I'
        else -> 'D'
      }
    }
  }

  override fun addStatusEvent(status: Status?) {
    status ?: return
    val sb = StringBuilder(256)
    val client = Main.client
    StatusPrinter.buildStr(sb, "", status)

    client?.logMessage(
      LogMessageParams(
        levelChar(status.level),
        status.origin.javaClass.simpleName,
        sb.toString()
      )
    )
  }
}