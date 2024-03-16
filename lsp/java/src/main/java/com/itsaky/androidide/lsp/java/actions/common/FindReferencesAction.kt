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
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.editor.api.ILspEditor
import com.itsaky.androidide.lsp.java.actions.BaseJavaCodeAction
import com.itsaky.androidide.resources.R
import io.github.rosemoe.sora.widget.CodeEditor
import java.io.File

/**
 * Action that allows the user to find references to a variable, field, method or class.
 *
 * @author Akash Yadav
 */
class FindReferencesAction : BaseJavaCodeAction() {

  override val titleTextRes: Int = R.string.action_find_references
  override val id: String = "ide.editor.lsp.java.findReferences"
  override var label: String = ""
  override var requiresUIThread: Boolean = true

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible || !data.hasRequiredData(CodeEditor::class.java, File::class.java)) {
      markInvisible()
      return
    }
  }

  override suspend fun execAction(data: ActionData): Any {
    val editor = data[CodeEditor::class.java]!!
    return (editor as? ILspEditor)?.findReferences() ?: false
  }
}
