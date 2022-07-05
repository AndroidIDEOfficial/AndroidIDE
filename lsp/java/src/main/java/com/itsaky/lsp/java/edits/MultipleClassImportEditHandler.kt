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

import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.java.compiler.JavaCompilerService
import com.itsaky.lsp.java.utils.EditHelper
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.TextEdit
import com.itsaky.lsp.util.RewriteHelper
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path

/**
 * Imports multiple classes at once.
 *
 * @param classes The fully qualified classnames to import.
 * @param imported The current imports of the given file.
 * @author Akash Yadav
 */
class MultipleClassImportEditHandler(
  private val classes: Set<String>,
  private val imported: Set<String>,
  file: Path
) : IJavaEditHandler(file) {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun performEdits(
    compiler: JavaCompilerService,
    editor: CodeEditor,
    completionItem: CompletionItem
  ) {
    val edits = mutableListOf<TextEdit>()
    for (className in classes) {
      try {
        edits.addAll(EditHelper.addImportIfNeeded(compiler, file, imported, className))
      } catch (err: Throwable) {
        log.error("Unable to compute edits to perform import for class:", className)
      }
    }
    RewriteHelper.performEdits(edits, editor)
  }
}
