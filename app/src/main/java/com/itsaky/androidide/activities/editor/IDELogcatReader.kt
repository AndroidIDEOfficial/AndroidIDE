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
package com.itsaky.androidide.activities.editor

import android.os.Process
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Reads the logs from AndroidIDE and saves it to a file in the projects directory.
 *
 * @author Akash Yadav
 */
class IDELogcatReader : Thread("IDELogcatReader") {

  private val log = ILogger.newInstance("IDELogcatReader")

  override fun run() {
    val date = Date()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS")
    val outputFile = File(Environment.ANDROIDIDE_HOME,
      "logs/AndroidIDE-LOG-${dateFormat.format(date)}.txt")
    log.debug("Creating output file: $outputFile")
    outputFile.parentFile!!.mkdirs()
    outputFile.createNewFile()
    outputFile.outputStream().buffered().use {
      try {
        val process = ProcessBuilder("logcat", "--pid=${Process.myPid()}", "-v",
          "threadtime").let { builder ->
          builder.redirectErrorStream(true)
          builder.start()
        }

        process.inputStream.bufferedReader().forEachLine { line ->
          it.write("$line\n".toByteArray())
          it.flush()
        }

        log.info("Process ended with exit code :", process.waitFor())
      } catch (err: Throwable) {
        log.error("Failed to read logs", err)
      }
    }
  }
}