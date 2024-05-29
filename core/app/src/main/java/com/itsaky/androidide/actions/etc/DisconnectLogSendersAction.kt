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

package com.itsaky.androidide.actions.etc

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.services.log.lookupLogService

/**
 * An action to disconnect all connected LogSenders at once.
 *
 * @author Akash Yadav
 */
class DisconnectLogSendersAction(context: Context, override val order: Int) : EditorActivityAction() {

  override val id: String = "ide.editor.service.logreceiver.disconnectSenders"

  init {
    label = context.getString(R.string.title_disconnect_log_senders)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_logs_disconnect)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    data.getActivity() ?: run {
      markInvisible()
      return
    }

    val receiverService = lookupLogService()
    if (receiverService == null) {
      markInvisible()
      return
    }
  }

  override suspend fun execAction(data: ActionData): Any {
    val receiverService = lookupLogService()
    receiverService?.disconnectAll()

    markInvisible()
    data.getActivity()?.invalidateOptionsMenu()
    return true
  }
}