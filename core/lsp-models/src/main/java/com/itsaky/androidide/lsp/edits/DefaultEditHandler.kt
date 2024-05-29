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

package com.itsaky.androidide.lsp.edits

import android.os.Looper
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.lsp.models.Command
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.InsertTextFormat.SNIPPET
import com.itsaky.androidide.lsp.util.RewriteHelper
import io.github.rosemoe.sora.lang.completion.snippet.parser.CodeSnippetParser
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor
import org.slf4j.LoggerFactory

/**
 * Default edit handler for completion items.
 *
 * @author Akash Yadav
 */
open class DefaultEditHandler : IEditHandler {

  companion object {

    private val log = LoggerFactory.getLogger(DefaultEditHandler::class.java)
  }

  override fun performEdits(
    item: CompletionItem,
    editor: CodeEditor,
    text: Content,
    line: Int,
    column: Int,
    index: Int
  ) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      ThreadUtils.runOnUiThread { performEditsInternal(item, editor, text, line, column, index) }
      return
    }

    performEditsInternal(item, editor, text, line, column, index)
  }

  protected open fun performEditsInternal(
    item: CompletionItem,
    editor: CodeEditor,
    text: Content,
    line: Int,
    column: Int,
    index: Int
  ) {
    if (item.insertTextFormat == SNIPPET) {
      insertSnippet(item, editor, text, line, column, index)
      return
    }

    val start = getIdentifierStart(text.getLine(line), column)
    text.delete(line, start, line, column)
    editor.commitText(item.insertText)

    text.beginBatchEdit()
    if (item.additionalEditHandler != null) {
      item.additionalEditHandler!!.performEdits(item, editor, text, line, column, index)
    } else if (item.additionalTextEdits != null && item.additionalTextEdits!!.isNotEmpty()) {
      RewriteHelper.performEdits(item.additionalTextEdits!!, editor)
    }
    text.beginBatchEdit()

    executeCommand(editor, item.command)
  }

  protected open fun insertSnippet(
    item: CompletionItem,
    editor: CodeEditor,
    text: Content,
    line: Int,
    column: Int,
    index: Int
  ) {
    val snippetDescription = item.snippetDescription!!
    val snippet = CodeSnippetParser.parse(item.insertText)
    val prefixLength = snippetDescription.selectedLength
    val selectedText = text.subSequence(index - prefixLength, index).toString()
    var actionIndex = index
    if (snippetDescription.deleteSelected) {
      text.delete(index - prefixLength, index)
      actionIndex -= prefixLength
    }
    editor.snippetController.startSnippet(actionIndex, snippet, selectedText)

    if (snippetDescription.allowCommandExecution) {
      executeCommand(editor, item.command)
    }
  }

  protected open fun executeCommand(editor: CodeEditor, command: Command?) {
    if (command == null) {
      return
    }

    try {
      val klass = editor::class.java
      val method = klass.getMethod("executeCommand", Command::class.java)
      method.isAccessible = true
      method.invoke(editor, command)
    } catch (th: Throwable) {
      log.error("Unable to invoke 'executeCommand(Command) method in IDEEditor.", th)
    }
  }

  protected open fun getIdentifierStart(text: CharSequence, end: Int): Int {
    var start = end
    while (start > 0) {
      if (isPartialPart(text[start - 1])) {
        start--
        continue
      }
      break
    }
    return start
  }

  protected open fun isPartialPart(c: Char) = Character.isJavaIdentifierPart(c)
}
