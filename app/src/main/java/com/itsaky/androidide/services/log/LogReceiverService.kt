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

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.itsaky.androidide.logsender.LogSender
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.preferences.logsenderEnabled
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Service for handling log lines that are sent by the client applications.
 *
 * @author Akash Yadav
 */
class LogReceiverService : Service() {

  private val binder = LogReceiverImpl()
  private val started = AtomicBoolean(false)
  private val isBoundToConsumer = AtomicBoolean(false)

  private val scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
  private val log = ILogger.newInstance("LogReceiverService")

  companion object {

    internal const val ACTION_CONNECT_LOG_CONSUMER = "com.itsaky.androidide.logrecevier.CONNECT_LOG_CONSUMER"
    internal const val ACTION_CONNECTION_UPDATE = "com.itsaky.androidide.logreceiver.CONNECTION_UPDATE"

    private const val LOG_CONSUMER_WAIT_DURATION = 10 // seconds

    @JvmStatic
    internal val LOOKUP_KEY = Lookup.Key<LogReceiverService>()
  }

  override fun onCreate() {
    super.onCreate()
    Lookup.getDefault().update(LOOKUP_KEY, this)
    log.info("LogReceiverService has been created")
  }

  override fun onBind(intent: Intent?): IBinder? {
    log.debug("Received bind request: $intent")

    if (!logsenderEnabled) {
      log.debug("Rejecting bind request. LogReceiver is disabled.")
      return null
    }

    // TODO: Handle the case where a log consumer unbinds from the service while senders
    //   are connected to the service. In this case, we should disconnect from the senders
    //   and stop this service.
    if (intent?.action == ACTION_CONNECT_LOG_CONSUMER) {
      // a log consumer has been bound to the service

      if (isBoundToConsumer.get()) {
        log.warn("LogReceiverService is limited to one consumer only.")
        return null
      }

      // start accepting senders
      log.info("Log consumer has been bound")
      return startBinderAndGet().also { binder ->
        binder.startReaders()
        isBoundToConsumer.set(true)
      }
    }
    
    if (intent?.action != LogSender.SERVICE_ACTION) {
      log.debug("Rejecting bind request: action=${intent?.action}")
      return null
    }

    log.debug("Accepting bind request...")
    return startBinderAndGet().also {
      if (!isBoundToConsumer.get()) {
        // if there are no consumers bound to this service
        // listen for consumers to bind to the service for next LOG_CONSUMER_WAIT_DURATION
        // if the consumer still does not connect, disconnect from all senders and stop the service
        listenForConsumer()
      }
    }
  }

  private fun startBinderAndGet(): LogReceiverImpl {
    return binder.also {
      if (!started.getAndSet(true)) {
        binder.acceptSenders()
        binder.connectionObserver = this::onConnectionUpdated
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    log.debug("LogReceiverService is being destroyed...")
    binder.close()
    try {
      scheduledExecutor.shutdownNow()
    } catch (e: Exception) {
      // ignored
    }
    Lookup.getDefault().unregister(LOOKUP_KEY)
  }

  fun setConsumer(consumer: ((LogLine) -> Unit)?) {
    binder.consumer = consumer
  }

  private fun onConnectionUpdated(params: ConnectionObserverParams) {
    val intent = Intent(ACTION_CONNECTION_UPDATE)
    intent.putExtras(params.bundle())
    LocalBroadcastManager.getInstance(this).sendBroadcastSync(intent)
  }

  private fun listenForConsumer() {
    log.debug("Waiting for log consumer...")
    scheduledExecutor.schedule({
      if (!isBoundToConsumer.get()) {
        // ask senders to disconnect
        log.debug("No log consumer has been bound to the log receiver service")
        binder.disconnectAll()
      }
    }, LOG_CONSUMER_WAIT_DURATION.toLong(), TimeUnit.SECONDS)
  }
}

fun lookupLogService(): LogReceiverService? {
  return Lookup.getDefault().lookup(LogReceiverService.LOOKUP_KEY)
}
