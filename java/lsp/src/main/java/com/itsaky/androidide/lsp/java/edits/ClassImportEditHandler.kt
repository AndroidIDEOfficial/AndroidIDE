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

package com.itsaky.androidide.lsp.java.edits

import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.java.utils.EditHelper
import com.itsaky.androidide.lsp.models.ClassCompletionData
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.util.RewriteHelper
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path

/**
 * Imports the required class for a ClassCompletionItem.
 *
 * @param imports The current file imports.
 * @param file The file in which this edit will be performed.
 * @author Akash Yadav
 */
class ClassImportEditHandler(val imports: Set<String>, file: Path) : AdvancedJavaEditHandler(file) {

  override fun performEdits(
    compiler: JavaCompilerService,
    editor: CodeEditor,
    completionItem: CompletionItem
  ) {
    val data = completionItem.data as? ClassCompletionData ?: return
    val className = data.className
    val edits = EditHelper.addImportIfNeeded(compiler, file, imports, className)

    if (edits.isNotEmpty()) {
      RewriteHelper.performEdits(edits, editor)
    }
  }
}
