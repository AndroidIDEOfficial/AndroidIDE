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

import com.itsaky.androidide.logsender.socket.SenderInfoCommand
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.ILogger
import java.net.Socket
import java.net.SocketException

/**
 * Handles a single log sender.
 *
 * @author Akash Yadav
 */
class LogSenderHandler(
  private val sender: SenderInfoCommand,
  private val socket: Socket,
  internal var consumer: ((LogLine) -> Unit)? = null,
  internal var onClose: (String) -> Unit = {}
) : Thread("LogSenderHandler"), AutoCloseable {

  private val log = ILogger.newInstance("LogSenderHandler")

  override fun run() {
    try {
      socket.getInputStream().bufferedReader().use { reader ->
        while (!socket.isClosed) {
          try {
            LogLine.forLogString(reader.readLine())?.let { line -> consumer?.invoke(line) }
          } catch (interrupt: InterruptedException) {
            currentThread().interrupt()
          }
        }
      }
    } catch (err: SocketException) {
      log.error("An error occurred while reading from socket", err)
    } finally {
      close()
    }
  }

  override fun close() {
    try {
      if (!socket.isClosed) {
        log.debug("Closing log sender handler...")
        socket.close()
      }
    } catch (err: Throwable) {
      log.error("Failed to close socket", err)
    } finally {
      onClose(this.sender.senderId)
    }
  }
}
