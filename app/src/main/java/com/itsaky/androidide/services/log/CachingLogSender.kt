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

import com.itsaky.androidide.logsender.ILogSender

/**
 * An implementation of [ILogSender] which caches the ID, PID and package name from the provided sender.
 *
 * @author Akash Yadav
 */
class CachingLogSender(
  private val sender: ILogSender,
  internal val port: Int,
  internal var isStarted: Boolean
) : ILogSender by sender {

  private var cachedPid: Int = -1
  private var cachedId: String? = null
  private var cachedPckName: String? = null

  override fun getPid(): Int {
    if (this.cachedPid != -1) {
      return this.cachedPid
    }
    return sender.pid.also { this.cachedPid = it }
  }

  override fun getPackageName(): String {
    return this.cachedPckName ?: sender.packageName.also { this.cachedPckName = it }
  }

  override fun getId(): String {
    return this.cachedId ?: sender.id.also { this.cachedId = it }
  }
}