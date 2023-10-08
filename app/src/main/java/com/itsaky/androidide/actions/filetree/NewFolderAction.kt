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

import android.content.Context
import android.view.LayoutInflater
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.adapters.viewholders.FileTreeViewHolder
import com.itsaky.androidide.preferences.databinding.LayoutDialogTextInputBinding
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashSuccess
import com.unnamed.b.atv.model.TreeNode
import java.io.File

/**
 * File tree action to create a new folder.
 *
 * @author Akash Yadav
 */
class NewFolderAction(context: Context, override val order: Int) :
  BaseDirNodeAction(
    context = context,
    labelRes = R.string.new_folder,
    iconRes = R.drawable.ic_new_folder
  ) {

  override val id: String = "ide.editor.fileTree.newFolder"

  override suspend fun execAction(data: ActionData) {
    val context = data.requireActivity()
    val currentDir = data.requireFile()
    val lastHeld = data.getTreeNode()
    val binding = LayoutDialogTextInputBinding.inflate(LayoutInflater.from(context))
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    binding.name.editText!!.setHint(R.string.folder_name)
    builder.setTitle(R.string.new_folder)
    builder.setMessage(R.string.msg_can_contain_slashes)
    builder.setView(binding.root)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.text_create) { dialogInterface, _ ->
      dialogInterface.dismiss()
      val name: String = binding.name.editText!!.text.toString().trim()
      if (name.length !in 1..40 || name.startsWith("/")) {
        flashError(R.string.msg_invalid_name)
        return@setPositiveButton
      }

      val newDir = File(currentDir, name)
      if (newDir.exists()) {
        flashError(R.string.msg_folder_exists)
        return@setPositiveButton
      }

      if (!newDir.mkdirs()) {
        flashError(R.string.msg_folder_creation_failed)
        return@setPositiveButton
      }

      flashSuccess(R.string.msg_folder_created)
      if (lastHeld != null) {
        val node = TreeNode(newDir)
        node.viewHolder = FileTreeViewHolder(context)
        lastHeld.addChild(node)
        requestExpandNode(lastHeld)
      } else {
        requestFileListing()
      }
    }
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.create().show()
  }
}
