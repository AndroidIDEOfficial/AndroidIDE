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

package com.itsaky.androidide.tooling.api.messages

import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.ILogger

/**
 * Parameters for sending a log message from the tooling API to the IDE.
 *
 * @author Akash Yadav
 */
data class LogMessageParams(val level: Char, val tag: String, val message: String)

/**
 * Creates a new [LogLine] instance from this [LogMessageParams].
 */
fun LogMessageParams.toLogLine(): LogLine {
  return LogLine.obtain(ILogger.Level.forChar(level), tag, message)
}
