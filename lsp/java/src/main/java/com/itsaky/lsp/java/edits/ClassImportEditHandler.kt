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

package com.itsaky.lsp.java.edits

import com.itsaky.lsp.java.compiler.JavaCompilerService
import com.itsaky.lsp.java.utils.EditHelper
import com.itsaky.lsp.util.RewriteHelper
import com.itsaky.lsp.models.CompletionItem
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path

/**
 * Imports the required class for a ClassCompletionItem.
 *
 * @param imports The current file imports.
 * @param file The file in which this edit will be performed.
 * @author Akash Yadav
 */
class ClassImportEditHandler(val imports: Set<String>, file: Path) : IJavaEditHandler(file) {

  override fun performEdits(
    compiler: JavaCompilerService,
    editor: CodeEditor,
    completionItem: CompletionItem
  ) {
    val className = completionItem.data!!.className
    val edits = EditHelper.addImportIfNeeded(compiler, file, imports, className)
    RewriteHelper.performEdits(edits, editor)
  }
}
