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

import com.itsaky.androidide.views.editor.CodeEditorView
import java.io.File

/** @author Akash Yadav */
abstract class EditorRelatedAction : EditorActivityAction() {
    override fun prepare(data: ActionData) {
        val editor = getEditor(data)
        val file = data.get(File::class.java)

        visible = editor != null && file != null
        enabled = visible
    }

    fun getEditor(data: ActionData): CodeEditorView? = data.get(CodeEditorView::class.java)
}
