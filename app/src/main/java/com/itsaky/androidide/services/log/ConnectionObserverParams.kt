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

import android.content.Intent
import android.os.Bundle

/**
 * Arguments passed to a connection observer from [LogReceiverImpl].
 *
 * @property clientId The unique identifier for the client that was connected or disconnected.
 * @property totalConnections The total number of connections after this client was connected/disconnected.
 * @author Akash Yadav
 */
data class ConnectionObserverParams(val clientId: String, val totalConnections: Int) {

  companion object {

    const val KEY_CLIENT_ID = "com.itsaky.androidide.logreceiver.connectedClientId"
    const val KEY_CONNECTION_COUNT = "com.itsaky.androidide.logreceiver.connectionCount"

    @JvmStatic
    internal fun from(intent: Intent?): ConnectionObserverParams? {
      val clientId = intent?.getStringExtra(KEY_CLIENT_ID) ?: return null
      val connectionCount = intent.getIntExtra(KEY_CONNECTION_COUNT, -1)
      if (connectionCount == -1) {
        return null
      }

      return ConnectionObserverParams(clientId, connectionCount)
    }
  }

  internal fun bundle(): Bundle {
    return Bundle().apply {
      putString(KEY_CLIENT_ID, clientId)
      putInt(KEY_CONNECTION_COUNT, totalConnections)
    }
  }
}
