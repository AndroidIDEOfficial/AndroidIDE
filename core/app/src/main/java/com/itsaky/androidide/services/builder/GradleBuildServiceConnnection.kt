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

package com.itsaky.androidide.services.builder

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import org.slf4j.LoggerFactory

/**
 * [ServiceConnection] for [GradleBuildService].
 *
 * @author Akash Yadav
 */
class GradleBuildServiceConnnection : ServiceConnection {

  internal var onConnected: ((GradleBuildService) -> Unit)? = null

  companion object {

    private val log = LoggerFactory.getLogger(GradleBuildServiceConnnection::class.java)
  }

  override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
    val serviceBinder = service as GradleServiceBinder
    onConnected?.invoke(serviceBinder.service!!)
  }

  override fun onServiceDisconnected(name: ComponentName?) {
    onConnected = null
    log.info("Disconnected from Gradle build service")
  }
}