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

package com.itsaky.androidide.logsender.utils

/**
 * Reads application logs with `logcat`.
 *
 * @author Akash Yadav
 */
class LogReader(command: Array<String> = defaultCmd(), private val consume: (String) -> Unit) : Thread("AndroidIDE-LogReader") {

  private val process = buildProcess(command)

  override fun run() {
    info("Starting to read logs...")
    try {
      val process = process.start()
      process.inputStream.bufferedReader().forEachLine(consume)
    } catch (err: Throwable) {
      error("Failed to start logcat process", err)
    }
  }

  private fun buildProcess(command: Array<String>): ProcessBuilder {
    return ProcessBuilder(*command).apply {
      this.redirectErrorStream(true)
    }
  }
}

fun defaultCmd(): Array<String> {
  return arrayOf("logcat", "-v", "threadtime")
}
