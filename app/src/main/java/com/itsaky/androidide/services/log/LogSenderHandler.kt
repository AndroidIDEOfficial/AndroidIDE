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

package com.itsaky.androidide.services.log

import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.ILogger
import java.net.Socket
import java.net.SocketException

/**
 * Handles a single log sender.
 *
 * @author Akash Yadav
 */
class LogSenderHandler(private val socket: Socket,
                       internal var consumer: ((LogLine) -> Unit)? = null
) : Thread("LogSenderHandler"), AutoCloseable {

  private val log = ILogger.newInstance("LogSenderHandler")

  override fun run() {
    socket.getInputStream().bufferedReader().use { reader ->
      try {
        while (!socket.isClosed) {
          LogLine.forLogString(reader.readLine())?.let { line -> consumer?.invoke(line) }
        }
      } catch (err: SocketException) {
        // ignored
      } finally {
        close()
      }
    }
  }

  override fun close() {
    try {
      if (!socket.isClosed) {
        socket.close()
      }
    } catch (err: Throwable) {
      log.error("Failed to close socket", err)
    }
  }
}
