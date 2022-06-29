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

import com.itsaky.lsp.api.ILanguageServerRegistry
import com.itsaky.lsp.edits.IEditHandler
import com.itsaky.lsp.java.JavaLanguageServer
import com.itsaky.lsp.java.compiler.JavaCompilerService
import com.itsaky.lsp.models.CompletionItem
import io.github.rosemoe.sora.widget.CodeEditor
import java.nio.file.Path

/**
 * Handles edits for Java completion items.
 *
 * @author Akash Yadav
 */
abstract class IJavaEditHandler(protected val file: Path) : IEditHandler {

  override fun performEdits(editor: CodeEditor, completionItem: CompletionItem) {
    val languageServer =
      ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
    if (languageServer != null && languageServer is JavaLanguageServer) {
      performEdits(languageServer.compiler, editor, completionItem)
    }
  }

  /**
   * Java edit handlers which require instance of the compiler should override this method instead
   * of [performEdits].
   *
   * @param compiler The compiler service instance.
   * @param editor The editor to perform edits on.
   * @param completionItem The completion item which contains required data.
   */
  abstract fun performEdits(
    compiler: JavaCompilerService,
    editor: CodeEditor,
    completionItem: CompletionItem
  )
}
