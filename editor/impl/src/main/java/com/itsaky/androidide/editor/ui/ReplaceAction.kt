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

package com.itsaky.androidide.editor.ui

import android.view.LayoutInflater
import com.itsaky.androidide.editor.databinding.LayoutEditorFindReplaceBinding
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.DialogUtils
import org.slf4j.LoggerFactory

/**
 * Handles the replace action while searching in file.
 *
 * @author Akash Yadav
 */
object ReplaceAction {

  private val log = LoggerFactory.getLogger(ReplaceAction::class.java)

  @JvmStatic
  fun doReplace(editor: IDEEditor) {
    val context = editor.context
    val binding = LayoutEditorFindReplaceBinding.inflate(LayoutInflater.from(context))
    val builder = DialogUtils.newMaterialDialogBuilder(context)
    builder.setTitle(R.string.replace)
    builder.setView(binding.root)
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.setPositiveButton(R.string.replace) { dialog, _ ->
      dialog.dismiss()
      val input = binding.replacementInput.editText
      if (input == null) {
        log.error("Unable to perform replace action. Input field is null")
        return@setPositiveButton
      }

      editor.searcher.replaceThis(input.text.toString())
    }
    builder.setNeutralButton(R.string.replaceAll) { dialog, _ ->
      dialog.dismiss()
      val input = binding.replacementInput.editText
      if (input == null) {
        log.error("Unable to perform replace action. Input field is null")
        return@setNeutralButton
      }

      editor.searcher.replaceAll(input.text.toString())
    }
    builder.show()
  }
}
