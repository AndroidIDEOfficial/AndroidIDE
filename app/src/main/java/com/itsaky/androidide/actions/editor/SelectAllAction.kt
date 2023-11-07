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

package com.itsaky.androidide.actions.editor

import android.content.Context
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.BaseEditorAction

/** @author Akash Yadav */
class SelectAllAction(context: Context, override val order: Int) : BaseEditorAction() {

  init {
    label = context.getString(android.R.string.selectAll)

    val arr = context.obtainStyledAttributes(intArrayOf(android.R.attr.actionModeSelectAllDrawable))
    icon = arr.getDrawable(0)?.let { tintDrawable(context, it) }
    arr.recycle()
  }

  override val id: String = "ide.editor.code.text.selectAll"

  override suspend fun execAction(data: ActionData): Boolean {
    val editor = getEditor(data) ?: return false
    editor.selectAll()
    return true
  }

  override fun dismissOnAction() = false
}
