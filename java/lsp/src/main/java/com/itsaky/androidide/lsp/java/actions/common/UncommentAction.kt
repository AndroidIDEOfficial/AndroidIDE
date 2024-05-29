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
package com.itsaky.androidide.lsp.java.actions.common

import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireEditor
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.resources.R

/** @author Akash Yadav */
class UncommentAction : BaseJavaCodeAction() {
  override val id: String = "ide.editor.lsp.java.uncommentLine"
  override var label: String = ""

  override val titleTextRes: Int = R.string.action_uncomment_line
  
  override var requiresUIThread: Boolean = true
  
  override suspend fun execAction(data: ActionData): Boolean {
    val editor = data.requireEditor()
    val text = editor.text
    val cursor = editor.cursor
    
    text.beginBatchEdit()
    for (line in cursor.leftLine..cursor.rightLine) {
      val l = text.getLineString(line)
      if (l.trim().startsWith("//")) {
        val i = l.indexOf("//")
        text.delete(line, i, line, i + 2)
      }
    }
    text.endBatchEdit()

    return true
  }
  
  override fun dismissOnAction() = false
}
