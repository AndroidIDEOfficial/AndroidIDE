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

package com.itsaky.androidide.actions.file

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorRelatedAction
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.utils.ILogger
import com.itsaky.toaster.Toaster
import com.itsaky.toaster.toast

/** @author Akash Yadav */
class SaveFileAction() : EditorRelatedAction() {

  private val log = ILogger.newInstance("SaveFileAction")

  override val id: String = "editor_saveFile"

  constructor(context: Context) : this() {
    label = context.getString(R.string.save)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_save)
  }

  override fun prepare(data: ActionData) {

    val context =
      getActivity(data)
        ?: run {
          visible = false
          enabled = false
          return
        }

    visible = context.viewModel.getOpenedFiles().isNotEmpty()
    enabled = context.areFilesModified()
  }

  override fun execAction(data: ActionData): Boolean {
    val context = getActivity(data) ?: return false

    return try {
      // Cannot use context.saveAll() because this.execAction is called on non-UI thread
      // and saveAll call will result in UI actions
      val result = context.saveAllResult()

      if (result.xmlSaved) {
        ProjectManager.generateSources()
      }

      if (result.gradleSaved) {
        context.notifySyncNeeded()
      }
      true
    } catch (error: Throwable) {
      log.error("Failed to save file", error)
      false
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result is Boolean && result) {
      toast(R.string.all_saved, Toaster.Type.SUCCESS)
    } else {
      log.error("Failed to save file")
      TODO("Create message in strings.xml")
    }
  }
}
