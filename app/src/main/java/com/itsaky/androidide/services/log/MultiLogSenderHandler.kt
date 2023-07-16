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
import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Handles clients that connect to the log receiver.
 *
 * @author Akash Yadav
 */
class MultiLogSenderHandler(consumer: ((LogLine) -> Unit)? = null) :
  Thread("MultiLogSenderHandler"), AutoCloseable {

  private val log = ILogger.newInstance("MultiLogSenderHandler")
  private val lock = ReentrantLock(true)
  private val clients = mutableListOf<LogSenderHandler>()
  private val port = AtomicInteger(-1)
  private var keepAlive = AtomicBoolean(false)

  internal var consumer: ((LogLine) -> Unit)? = consumer
    set(value) {
      field = value
      lock.withLock {
        clients.forEach { it?.consumer = value }
      }
    }

  fun getPort(): Int {
    return port.get()
  }

  override fun run() {
    try {
      ServerSocket(0).use {
        port.set(it.localPort)
        log.info("Starting log receiver server socket at port ${getPort()}")

        while (keepAlive.get()) {
          val handler = LogSenderHandler(it.accept(), consumer, ::removeClient)

          log.info("A log sender has been connected")

          clients.add(handler)
          handler.start()
        }
      }
    } catch (err: Throwable) {
      if (err !is InterruptedException) {
        log.error("Failed to start log receiver socket", err)
      }
    } finally {
      this.close()
    }
  }

  private fun removeClient(handler: LogSenderHandler) {
    lock.withLock {
      clients.remove(handler)
    }
  }

  override fun start() {
    keepAlive.set(true)
    super.start()
  }

  override fun close() {
    this.keepAlive.set(false)
    lock.withLock {
      clients.forEach { it?.closeAndLogError() }
    }
  }

  private fun AutoCloseable.closeAndLogError() {
    try {
      close()
    } catch (e: Exception) {
      log.warn("Failed to close", e)
    }
  }
}
