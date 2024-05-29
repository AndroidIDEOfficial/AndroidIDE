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
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class ApkInstallationSessionCallback(private var activity: BaseEditorActivity?) :
  SingleSessionCallback() {

  private var sessionId = -1

  companion object {
    private val log = LoggerFactory.getLogger(ApkInstallationSessionCallback::class.java)
  }

  override fun onCreated(sessionId: Int) {
    this.sessionId = sessionId
    log.debug("Created package installation session: {}", sessionId)
    activity?._binding?.content?.apply {
      bottomSheet.setActionText(activity!!.getString(string.msg_installing_apk))
      bottomSheet.setActionProgress(0)
      bottomSheet.showChild(EditorBottomSheet.CHILD_ACTION)
    }
  }

  override fun onProgressChanged(sessionId: Int, progress: Float) {
    activity?._binding?.content?.bottomSheet?.setActionProgress((progress * 100f).toInt())
  }

  override fun onFinished(sessionId: Int, success: Boolean) {
    activity?._binding?.content?.apply {
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
    if (this.sessionId != -1) {
      this.activity?.packageManager?.packageInstaller?.let { packageInstaller ->
        packageInstaller.mySessions.find { session -> session.sessionId == this.sessionId }
          ?.also { info ->
            try {
              packageInstaller.abandonSession(info.sessionId)
            } catch (ex: Exception) {
              log.error("Failed to abandon session {} : {}", info.sessionId, ex.cause?.message ?: ex.message)
            }
          }
      }
    }
    this.activity = null
    this.sessionId = -1
  }
}
