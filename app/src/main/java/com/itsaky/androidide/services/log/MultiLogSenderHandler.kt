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
import com.itsaky.androidide.logsender.socket.SocketCommandParser
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.ILogger
import java.net.ServerSocket
import java.util.Collections
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Handles clients that connect to the log receiver.
 *
 * @author Akash Yadav
 */
class MultiLogSenderHandler(consumer: ((LogLine) -> Unit)? = null) :
  Thread("MultiLogSenderHandler"), AutoCloseable {

  private val log = ILogger.newInstance("MultiLogSenderHandler")
  private val clients = Collections.synchronizedMap(HashMap<String, LogSenderHandler>())
  private val port = AtomicInteger(-1)
  private var keepAlive = AtomicBoolean(false)

  internal var consumer: ((LogLine) -> Unit)? = consumer
    set(value) {
      field = value
      clients.forEach { (_, client) -> client.consumer = value }
    }

  fun getPort(): Int {
    return port.get()
  }

  override fun run() {
    val server = try {
      ServerSocket(0)
    } catch (err: Exception) {
      log.error("Failed to start log receiver socket", err)
      return
    }

    try {
      port.set(server.localPort)
      log.info("Starting log receiver server socket at port ${getPort()}")

      while (!currentThread().isInterrupted && keepAlive.get()) {
        val clientSocket = server.accept()

        val senderInfoLine = clientSocket.getInputStream().bufferedReader().readLine()
        val command = SocketCommandParser.parse(senderInfoLine)
        if (command == null || command !is SenderInfoCommand) {
          log.error("Cannot accept log sender client. A sender must send the /sender command first.")
          clientSocket.use {}
          continue
        }

        val handler = LogSenderHandler(command, clientSocket, consumer, ::removeClient)

        log.info("A log sender has been connected")

        clients[command.senderId] = handler

        handler.start()
      }
    } catch (interrupt: InterruptedException) {
      log.warn("MultiLogSenderHandler thread has been interrupted")
      currentThread().interrupt()
    } catch (err: Throwable) {
      log.error("An error occurred while accept log client connections", err)
    } finally {
      this.close()
      server.close()
    }
  }

  internal fun removeClient(senderId: String) {
    clients.remove(senderId)?.closeAndLogError()
  }

  override fun start() {
    keepAlive.set(true)
    super.start()
  }

  override fun close() {
    this.keepAlive.set(false)
    clients.forEach { (_, client) -> client?.closeAndLogError() }
  }

  private fun LogSenderHandler.closeAndLogError() {
    try {
      close()
    } catch (e: Exception) {
      log.warn("Failed to close", e)
    }
  }
}
