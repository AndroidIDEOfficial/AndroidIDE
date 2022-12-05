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

package com.itsaky.androidide.actions

import com.itsaky.androidide.ui.editor.CodeEditorView
import com.itsaky.androidide.ui.editor.IDEEditor

/** @author Akash Yadav */
abstract class EditorRelatedAction : EditorActivityAction(), EditorActionItem {

  override var requiresUIThread: Boolean = true

  override fun prepare(data: ActionData) {
    val editor =
      getEditor(data)
        ?: run {
          visible = false
          enabled = false
          return
        }

    val file = editor.file

    visible = file != null
    enabled = visible
  }

  fun getEditor(data: ActionData): IDEEditor? = data.get(IDEEditor::class.java)

  fun getEditorView(data: ActionData): CodeEditorView? = data[CodeEditorView::class.java]
}
