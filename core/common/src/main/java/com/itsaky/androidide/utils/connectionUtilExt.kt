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

package com.itsaky.androidide.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService

/**
 * Information about the device's internet connection.
 *
 * @property isConnected Indicates whether the device is able to reach the internet.
 *   See [NetworkCapabilities.NET_CAPABILITY_INTERNET].
 * @property isCellularTransport Indicates whether the device is using a cellular transport.
 *   See [NetworkCapabilities.TRANSPORT_CELLULAR].
 * @property isMeteredConnection Indicates whether the device is using a cellular transport.
 *   See [NetworkCapabilities.NET_CAPABILITY_NOT_METERED] and [NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED].
 * @property isBackgroundDataRestricted Indicates whether the background data is restricted. If the
 *   application is whitelisted from background data restriction, this will be `false`.
 *   See [NetworkCapabilities.TRANSPORT_CELLULAR].
 * @author Akash Yadav
 */
data class ConnectionInfo(
  val isConnected: Boolean,
  val isCellularTransport: Boolean,
  val isMeteredConnection: Boolean,
  val isBackgroundDataRestricted: Boolean
) {

  companion object {

    @JvmStatic
    val UNKNOWN = ConnectionInfo(
      isConnected = false,
      isCellularTransport = false,
      isMeteredConnection = false,
      isBackgroundDataRestricted = false
    )
  }
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
@JvmOverloads
fun getConnectionInfo(context: Context, networkCapabilities: NetworkCapabilities? = null): ConnectionInfo {
  val connectivityManager = context.getSystemService<ConnectivityManager>()
    ?: return ConnectionInfo.UNKNOWN
  val activeNetCapabilities = networkCapabilities ?: connectivityManager.activeNetwork?.let(
    connectivityManager::getNetworkCapabilities) ?: return ConnectionInfo.UNKNOWN

  val isConnected = activeNetCapabilities.hasCapability(
    NetworkCapabilities.NET_CAPABILITY_VALIDATED)
  val isCellularTransport = activeNetCapabilities.hasTransport(
    NetworkCapabilities.TRANSPORT_CELLULAR)
  val isMeteredConnection = connectivityManager.isActiveNetworkMetered

  // RESTRICT_BACKGROUND_STATUS_DISABLED -> background data usage is not restricted
  // RESTRICT_BACKGROUND_STATUS_WHITELISTED -> background data usage might be restricted, but the application is whitelisted
  val isBackgroundDataRestricted = !connectivityManager.restrictBackgroundStatus.let {
    it == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED
        || it == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED
  }

  return ConnectionInfo(
    isConnected = isConnected,
    isCellularTransport = isCellularTransport,
    isMeteredConnection = isMeteredConnection,
    isBackgroundDataRestricted = isBackgroundDataRestricted
  )
}