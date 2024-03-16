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
import com.itsaky.androidide.tasks.cancelIfActive
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.lang.Thread.currentThread
import java.net.ServerSocket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * Handles clients that connect to the log receiver.
 *
 * @author Akash Yadav
 */
class MultiLogSenderHandler(consumer: ((LogLine) -> Unit)? = null) : AutoCloseable {

  private val clients = ConcurrentHashMap<String, LogSenderHandler>()
  private val port = AtomicInteger(-1)
  private var isAlive = AtomicBoolean(false)

  private var logHandlerScope = CoroutineScope(
    Dispatchers.IO + CoroutineName("MultiLogSenderHandler"))

  internal var consumer: ((LogLine) -> Unit)? = consumer
    set(value) {
      field = value
      clients.forEach { (_, client) -> client.consumer = value }
    }

  companion object {
    private val log = LoggerFactory.getLogger(MultiLogSenderHandler::class.java)
  }

  fun getPort(): Int {
    return port.get()
  }

  private suspend fun startAsync() = withContext(Dispatchers.IO) {
    val job = coroutineContext[Job]
    val server = try {
      ServerSocket(0)
    } catch (err: Exception) {
      log.error("Failed to start log receiver socket", err)
      return@withContext
    }

    try {
      port.set(server.localPort)
      log.info("Starting log receiver server socket at port {}", getPort())

      while (job?.isCancelled != true && isAlive.get()) {
        val clientSocket = server.accept()

        val senderInfoLine = clientSocket.getInputStream().bufferedReader().readLine()
        val command = SocketCommandParser.parse(senderInfoLine)
        if (command == null || command !is SenderInfoCommand) {
          log.error(
            "Cannot accept log sender client. A sender must send the /sender command first.")
          clientSocket.use {}
          continue
        }

        val handler = LogSenderHandler(command, clientSocket, consumer, ::removeClient)

        log.info("A log sender has been connected")

        clients[command.senderId] = handler

        handler.startAsync()
      }
    } catch (interrupt: InterruptedException) {
      log.warn("MultiLogSenderHandler thread has been interrupted")
      currentThread().interrupt()
    } catch (err: Throwable) {
      log.error("An error occurred while accept log client connections", err)
    } finally {
      this@MultiLogSenderHandler.close()
      server.close()
    }
  }

  internal fun removeClient(senderId: String) {
    clients.remove(senderId)?.closeAndLogError()
  }

  private fun removeAllClients() {
    this.clients.forEach { (_, handler) ->
      handler.closeAndLogError()
    }

    this.clients.clear()
  }

  fun start() {
    isAlive.set(true)
    logHandlerScope.launch {
      startAsync()
    }
  }

  fun isAlive() = isAlive.get()

  override fun close() {
    this.isAlive.set(false)
    this.removeAllClients()
    this.consumer = null
    this.logHandlerScope.cancelIfActive()
  }

  private fun LogSenderHandler.closeAndLogError() {
    try {
      close()
    } catch (e: Exception) {
      log.warn("Failed to close", e)
    }
  }
}
