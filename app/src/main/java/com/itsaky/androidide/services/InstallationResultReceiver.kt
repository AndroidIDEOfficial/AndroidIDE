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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.itsaky.androidide.events.InstallationResultEvent
import org.greenrobot.eventbus.EventBus

/**
 * Receives status events for APK installation process.
 *
 * @author Akash Yadav
 */
class InstallationResultReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    EventBus.getDefault().post(InstallationResultEvent(intent))
  }
}
