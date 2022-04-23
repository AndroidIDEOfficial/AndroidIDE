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

package com.itsaky.androidide.actions.editor.find

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.databinding.LayoutEditorFindReplaceBinding
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.ILogger

/**
 * Shows dialog to replace one or all the matched text in editor searcher.
 * @author Akash Yadav
 */
class ReplaceAction() : SearchActionModeAction() {

    override val id: String = "editor.search.replace"
    private val log = ILogger.newInstance(javaClass.simpleName)

    constructor(context: Context) : this() {
        label = context.getString(R.string.replace)
        icon = ContextCompat.getDrawable(context, R.drawable.ic_search_replace)
    }

    override fun execAction(data: ActionData): Any {
        val context = data[Context::class.java]!!
        val editor = getEditor(data)!!

        if (!editor.searcher.hasQuery()) {
            return false
        }

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

            editor.searcher.replaceAllAsync(input.text.toString()).whenComplete { _, error ->
                if (error != null) {
                    log.error("Unable to replace all matched text", error)
                } else {
                    editor.post { data[ActionMode::class.java]?.finish() }
                }
            }
        }
        builder.show()

        return true
    }
}
