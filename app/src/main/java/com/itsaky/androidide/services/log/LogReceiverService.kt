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
import com.itsaky.androidide.logsender.LogSender
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.models.LogLine

/**
 * Service for handling log lines that are sent by the client applications.
 *
 * @author Akash Yadav
 */
class LogReceiverService : Service() {

  private val binder = LogReceiverImpl()

  companion object {
    @JvmStatic internal val LOOKUP_KEY = Lookup.Key<LogReceiverService>()
  }

  override fun onCreate() {
    super.onCreate()
    Lookup.DEFAULT.update(LOOKUP_KEY, this)
  }

  override fun onBind(intent: Intent?): IBinder? {
    if (intent?.action != LogSender.SERVICE_ACTION) {
      return null
    }
    return binder
  }

  override fun onDestroy() {
    super.onDestroy()
    Lookup.DEFAULT.unregister(LOOKUP_KEY)
  }

  fun setConsumer(consumer: ((LogLine) -> Unit)?) {
    binder.consumer = consumer
  }
}

fun lookupLogService(): LogReceiverService? {
  return Lookup.DEFAULT.lookup(LogReceiverService.LOOKUP_KEY)
}
