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

package com.itsaky.androidide.utils

import org.slf4j.LoggerFactory
import java.io.PrintStream

/**
 * A stop watch helps to log duration between the time when the instance of the stopwatch instance
 * was created and the time when [StopWatch.log] or [StopWatch.lap] method is called.
 *
 * @param label The label for the log message.
 * @author Akash Yadav
 */
class StopWatch
@JvmOverloads
constructor(
  val label: String,
  val start: Long = System.currentTimeMillis(),
  var lastLap: Long = start
) {

  companion object {

    private val log = LoggerFactory.getLogger(StopWatch::class.java)
  }

  fun log() {
    log.debug("{} completed in {}ms", label, System.currentTimeMillis() - start)
  }

  fun lap(message: String) {
    log.debug("{} in {}ms", message, System.currentTimeMillis() - start)
    lastLap = System.currentTimeMillis()
  }

  fun lapFromLast(message: String) {
    log.debug("{} in {}ms", message, System.currentTimeMillis() - lastLap)
    lastLap = System.currentTimeMillis()
  }

  fun writeTo(stream: PrintStream) {
    stream.println("$label completed in ${System.currentTimeMillis() - start}ms")
  }
}

/**
 * Run the given action with a stopwatch to log the time the action took to execute.
 *
 * @see StopWatch
 */
inline fun <R> withStopWatch(
  label: String,
  start: Long = System.currentTimeMillis(),
  lastLap: Long = start,
  action: (StopWatch) -> R
): R {
  return StopWatch(label, start, lastLap).run {
    try {
      action(this)
    } finally {
      log()
    }
  }
}