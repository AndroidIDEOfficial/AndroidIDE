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
import com.itsaky.androidide.projects.internal.ProjectManagerImpl
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashSuccess
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class SaveFileAction(context: Context, override val order: Int) : EditorRelatedAction() {

  override val id: String = "ide.editor.files.saveAll"
  override var requiresUIThread: Boolean = false

  companion object {
    private val log = LoggerFactory.getLogger(SaveFileAction::class.java)
  }

  init {
    label = context.getString(R.string.save)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_save)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    val context = data.getActivity() ?: run {
      visible = false
      enabled = false
      return
    }

    visible = context.editorViewModel.getOpenedFiles().isNotEmpty()
    enabled = context.areFilesModified() && !context.areFilesSaving()
  }

  override suspend fun execAction(data: ActionData): ResultWrapper {
    val context = data.getActivity() ?: return ResultWrapper()

    if (context.areFilesSaving()) {
      return ResultWrapper(isAlreadySaving = true)
    }

    return try {
      // Cannot use context.saveAll() because this.execAction is called on non-UI thread
      // and saveAll call will result in UI actions
      ResultWrapper(result = context.saveAllResult())
    } catch (error: Throwable) {
      log.error("Failed to save file", error)
      ResultWrapper()
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result is ResultWrapper && result.result != null) {
      val context = data.requireActivity()

      if (result.isAlreadySaving) {
        context.flashError(R.string.msg_files_being_saved)
        return
      }

      // show save notification before calling 'notifySyncNeeded' so that the file save notification
      // does not overlap the sync notification
      context.flashSuccess(R.string.all_saved)

      val saveResult = result.result
      if (saveResult.xmlSaved) {
        ProjectManagerImpl.getInstance().generateSources()
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

  inner class ResultWrapper(val isAlreadySaving: Boolean = false, val result: SaveResult? = null)
}
