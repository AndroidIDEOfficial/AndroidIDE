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

import com.itsaky.androidide.R.string
import com.itsaky.androidide.activities.editor.BaseEditorActivity
import com.itsaky.androidide.ui.EditorBottomSheet

/** @author Akash Yadav */
class ApkInstallationSessionCallback(private var activity: BaseEditorActivity?) :
  SingleSessionCallback() {

  private val log = ILogger.newInstance("InstallationSessionCallback")

  override fun onCreated(sessionId: Int) {
    log.debug("on session created:", sessionId)
    activity?.binding?.apply {
      bottomSheet.setActionText(activity!!.getString(string.msg_installing_apk))
      bottomSheet.setActionProgress(0)
      bottomSheet.showChild(EditorBottomSheet.CHILD_ACTION)
    }
  }

  override fun onProgressChanged(sessionId: Int, progress: Float) {
    activity?.binding?.bottomSheet?.setActionProgress((progress * 100f).toInt())
  }

  override fun onFinished(sessionId: Int, success: Boolean) {
    activity?.binding?.apply {
      bottomSheet.showChild(EditorBottomSheet.CHILD_HEADER)
      bottomSheet.setActionProgress(0)
      if (!success) {
        activity?.flashError(string.title_installation_failed)
      }
      
      activity?.let {
        it.installationCallback?.destroy()
        it.installationCallback = null
      }
    }
  }

  fun destroy() {
    this.activity = null
  }
}
