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

import com.itsaky.androidide.utils.ILogger
import java.io.PrintStream

/**
 * A stop watch helps to log duration between the time when the instance of the stopwatch instance
 * was created and the time when [StopWatch.log] or [StopWatch.lap] method is called.
 *
 * @param label The label for the log message.
 * @author Akash Yadav
 */
class StopWatch(
  val label: String,
  val start: Long = System.currentTimeMillis(),
  var lastLap: Long = start
) {
  constructor(label: String) : this(label, System.currentTimeMillis())

  private val log = ILogger.newInstance(javaClass.simpleName)

  fun log() {
    log.debug(getLogMessage())
  }

  private fun getLogMessage() = "$label completed in ${System.currentTimeMillis() - start}ms"

  fun writeTo(stream: PrintStream) {
    stream.println(getLogMessage())
  }

  fun lap(message: String) {
    log.debug("$message in ${System.currentTimeMillis() - start}ms")
    lastLap = System.currentTimeMillis()
  }

  fun lapFromLast(message: String) {
    log.debug("$message in ${System.currentTimeMillis() - lastLap}ms")
    lastLap = System.currentTimeMillis()
  }
}
