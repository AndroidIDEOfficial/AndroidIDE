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

package com.itsaky.androidide.services

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.itsaky.androidide.services.GradleBuildService.GradleServiceBinder
import com.itsaky.androidide.utils.ILogger

/**
 * [ServiceConnection] for [GradleBuildService].
 *
 * @author Akash Yadav
 */
class GradleBuildServiceConnnection : ServiceConnection {
  
  internal var onConnected: (GradleBuildService) -> Unit = {}
  private val log = ILogger.newInstance("GradleBuildServiceConnnection")
  
  override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    val buildService = (service as GradleServiceBinder).service
    onConnected(buildService)
  }
  
  override fun onServiceDisconnected(name: ComponentName?) {
    log.info("Disconnected from Gradle build service")
  }
}