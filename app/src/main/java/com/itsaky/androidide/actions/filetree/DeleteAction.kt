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

package com.itsaky.androidide.actions.filetree

import android.app.ProgressDialog
import android.content.Context
import com.blankj.utilcode.util.FileUtils
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.eventbus.events.file.FileDeletionEvent
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.FlashType
import com.itsaky.androidide.utils.flashMessage
import org.greenrobot.eventbus.EventBus
import java.io.File

/**
 * File tree action to delete files.
 *
 * @author Akash Yadav
 */
class DeleteAction(context: Context, override val order: Int) :
  BaseFileTreeAction(context, labelRes = R.string.delete_file, iconRes = R.drawable.ic_delete) {

  override val id: String = "ide.editor.fileTree.delete"

  override suspend fun execAction(data: ActionData) {
    val context = data.requireActivity()
    val file = data.requireFile()
    val lastHeld = data.getTreeNode()
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    builder
      .setNegativeButton(R.string.no, null)
      .setPositiveButton(R.string.yes) { dialogInterface, _ ->
        dialogInterface.dismiss()
        @Suppress("DEPRECATION")
        val progressDialog =
          ProgressDialog.show(context, null, context.getString(R.string.please_wait), true, false)
        executeAsync({ FileUtils.delete(file) }) {
          progressDialog.dismiss()

          val deleted = it ?: false

          flashMessage(
            if (deleted) R.string.deleted else R.string.delete_failed,
            if (deleted) FlashType.SUCCESS else FlashType.ERROR
          )

          if (!deleted) {
            return@executeAsync
          }

          notifyFileDeleted(file, context)

          if (lastHeld != null) {
            val parent = lastHeld.parent
            parent.deleteChild(lastHeld)
            requestExpandNode(parent)
          } else {
            requestFileListing()
          }

          val frag = context.getEditorForFile(file)
          if (frag != null) {
            context.closeFile(context.findIndexOfEditorByFile(frag.file))
          }
        }
      }
      .setTitle(R.string.title_confirm_delete)
      .setMessage(
        context.getString(
          R.string.msg_confirm_delete,
          String.format("%s [%s]", file.name, file.absolutePath)
        )
      )
      .setCancelable(false)
      .create()
      .show()
  }

  private fun notifyFileDeleted(file: File, context: Context) {
    val deletionEvent = FileDeletionEvent(file)

    // Notify FileManager first
    FileManager.onFileDeleted(deletionEvent)

    EventBus.getDefault().post(deletionEvent.putData(context))
  }
}
