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

package com.itsaky.androidide.lsp.xml.edits

import com.itsaky.androidide.lsp.edits.IEditHandler
import com.itsaky.androidide.lsp.models.CompletionItem
import io.github.rosemoe.sora.widget.CodeEditor

/**
 * Handles edits for attribute values with qualified binary names. The default implementation in
 * [CompletionItem] cannot handle these type of edits.
 *
 * @author Akash Yadav
 */
class QualifiedValueEditHandler : IEditHandler {

  override fun performEdits(editor: CodeEditor, completionItem: CompletionItem) {
    val cursor = editor.cursor
    val text = editor.text
    val line = cursor.leftLine
    val column = cursor.leftColumn
    val start = getIdentifierStart(text.getLine(line), column)

    text.delete(line, start, line, column)
    
    // Do not use CompletionItem.insertText as it is always '.'
    editor.commitText(
      completionItem.label
    )
  }
  private fun getIdentifierStart(text: CharSequence, end: Int): Int {
    var start = end
    while (start > 0) {
      val c = text[start - 1]
      if (Character.isJavaIdentifierPart(c) || c == '.') {
        start--
        continue
      }
      break
    }
    return start
  }
}
