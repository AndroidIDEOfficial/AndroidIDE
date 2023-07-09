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

import com.itsaky.androidide.logsender.ILogReceiver
import com.itsaky.androidide.logsender.ILogSender
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Handles IPC connections from other proceses.
 *
 * @author Akash Yadav
 */
class LogReceiverImpl(consumer: ((LogLine) -> Unit)? = null) : ILogReceiver.Stub(), AutoCloseable {

  private val log = ILogger.newInstance("LogReceiverImpl")
  private val senderHandler = MultiLogSenderHandler()
  private val senders = ConcurrentHashMap<Int, ILogSender>()
  private val consumerLock = ReentrantLock(true)

  internal var consumer: ((LogLine) -> Unit)? = consumer
    set(value) {
      field = value
      senderHandler.consumer = value?.let { synchronizeConsumer(value) }
    }

  private fun synchronizeConsumer(consumer: (LogLine) -> Unit): (LogLine) -> Unit {
    return { line -> consumerLock.withLock { consumer(line) } }
  }

  fun acceptSenders() {
    if (senderHandler.isAlive) {
      return
    }

    log.info("Starting log sender handler..")
    senderHandler.start()
  }

  override fun connect(sender: ILogSender?) {
    val port = senderHandler.getPort()
    if (port == -1) {
      log.error("A log sender is trying to connect, but log receiver is not started")
      return
    }

    sender?.let {
      if (senders.containsKey(it.pid)) {
        log.warn("Rejecting duplicate connection request from client '${it.pid}'")
        return
      }

      log.info("Connecting to client ${it.pid}")

      this.senders[it.pid] = it

      it.startReader(port)

      log.info("Total clients connected: ${senders.size}")
    }
  }

  override fun close() {
    // TODO : Send close request to clients
    senderHandler.close()
    consumer = null
    senders.clear()
  }
}
