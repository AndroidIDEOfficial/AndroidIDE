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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.net.Socket
import java.net.SocketException

/**
 * Handles a single log sender.
 *
 * @author Akash Yadav
 */
class LogSenderHandler(private val sender: SenderInfoCommand, private val socket: Socket,
  internal var consumer: ((LogLine) -> Unit)? = null,
  private var onClose: ((String) -> Unit)? = null) : AutoCloseable {

  private var manuallyClosed = false

  companion object {

    private val log = LoggerFactory.getLogger(LogSenderHandler::class.java)
  }

  suspend fun startAsync() = withContext(Dispatchers.IO) {
    try {
      socket.getInputStream().bufferedReader().use { reader ->
        while (!socket.isClosed) {
          try {
            LogLine.forLogString(reader.readLine())?.let { line -> consumer?.invoke(line) }
          } catch (cancellation: CancellationException) {
            break
          }
        }
      }
    } catch (err: SocketException) {
      if (!manuallyClosed) {
        log.error("An error occurred while reading from socket", err)
      }
    } finally {
      close()
    }
  }

  override fun close() {
    try {
      manuallyClosed = true

      if (!socket.isClosed) {
        log.debug("Closing log sender handler...")
        socket.close()
      }
    } catch (err: Throwable) {
      log.error("Failed to close socket", err)
    } finally {
      onClose?.invoke(this.sender.senderId)
      this.consumer = null
      this.onClose = null
    }
  }
}
