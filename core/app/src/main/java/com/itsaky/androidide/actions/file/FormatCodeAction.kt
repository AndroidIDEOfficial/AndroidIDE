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
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.EditorRelatedAction
import com.itsaky.androidide.resources.R

/**
 * Action that formats the code in the editor.
 *
 * @author Akash Yadav
 */
class FormatCodeAction(context: Context, override val order: Int) : EditorRelatedAction() {
  override val id: String = "ide.editor.code.text.format"
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS

  init {
    label = context.getString(R.string.title_format_code)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_format_code)
  }

  override suspend fun execAction(data: ActionData): Any {
    val editor = data.getEditor()!!
    val cursor = editor.text.cursor

    if (cursor.isSelected) {
      editor.formatCodeAsync(cursor.left(), cursor.right())
    } else {
      editor.formatCodeAsync()
    }
    return true
  }
}
