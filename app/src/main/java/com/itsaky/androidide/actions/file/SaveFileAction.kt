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
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorRelatedAction
import com.itsaky.androidide.models.SaveResult
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashSuccess

/** @author Akash Yadav */
class SaveFileAction(context: Context) : EditorRelatedAction() {

  private val log = ILogger.newInstance("SaveFileAction")

  override val id: String = "editor_saveFile"

  init {
    label = context.getString(R.string.save)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_save)
  }

  override fun prepare(data: ActionData) {

    val context =
      data.getActivity()
        ?: run {
          visible = false
          enabled = false
          return
        }

    visible = context.editorViewModel.getOpenedFiles().isNotEmpty()
    enabled = context.areFilesModified()
  }

  override fun execAction(data: ActionData): ResultWrapper {
    val context = data.getActivity() ?: return ResultWrapper()

    return try {
      // Cannot use context.saveAll() because this.execAction is called on non-UI thread
      // and saveAll call will result in UI actions
      ResultWrapper(context.saveAllResult())
    } catch (error: Throwable) {
      log.error("Failed to save file", error)
      ResultWrapper()
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result is ResultWrapper && result.result != null) {
      val context = data.requireActivity()

      // show save notification before calling 'notifySyncNeeded' so that the file save notification
      // does not overlap the sync notification
      flashSuccess(R.string.all_saved)

      val saveResult = result.result
      if (saveResult.xmlSaved) {
        ProjectManager.generateSources()
      }

      if (saveResult.gradleSaved) {
        context.editorViewModel.isSyncNeeded = true
      }

      context.invalidateOptionsMenu()
    } else {
      log.error("Failed to save file")
      flashError(R.string.save_failed)
    }
  }

  inner class ResultWrapper(val result: SaveResult? = null)
}
