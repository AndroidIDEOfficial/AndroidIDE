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
import com.itsaky.androidide.lsp.util.RewriteHelper
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.CodeEditor

/**
 * Default edit handler for completion items.
 *
 * @author Akash Yadav
 */
class DefaultEditHandler : IEditHandler {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun performEdits(
    item: CompletionItem,
    editor: CodeEditor,
    text: Content,
    line: Int,
    column: Int
  ) {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      ThreadUtils.runOnUiThread { performEdits(item, editor, text, line, column) }
      return
    }

    val start = getIdentifierStart(text.getLine(line), column)
    val shift = item.insertText.contains("$0")

    text.delete(line, start, line, column)

    if (text.contains("\n")) {
      val lines = item.insertText.split("\\\n")
      var i = 0
      lines.forEach {
        var commit = it
        if (i != 0) {
          commit = "\n" + commit
        }
        editor.commitText(commit)
        i++
      }
    } else {
      editor.commitText(text)
    }

    if (shift) {
      val l = editor.cursor.leftLine
      val t = editor.text.getLineString(l)
      val c = t.lastIndexOf("$0")

      if (c != -1) {
        editor.setSelection(l, c)
        editor.text.delete(l, c, l, c + 2)
      }
    }

    text.beginBatchEdit()
    if (item.additionalEditHandler != null) {
      item.additionalEditHandler!!.performEdits(item, editor, text, line, column)
    } else if (item.additionalTextEdits != null && item.additionalTextEdits!!.isNotEmpty()) {
      RewriteHelper.performEdits(item.additionalTextEdits!!, editor)
    }

    text.beginBatchEdit()
    executeCommand(editor, item.command)
  }

  private fun executeCommand(editor: CodeEditor, command: Command?) {
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

  private fun getIdentifierStart(text: CharSequence, end: Int): Int {
    var start = end
    while (start > 0) {
      if (Character.isJavaIdentifierPart(text[start - 1])) {
        start--
        continue
      }
      break
    }
    return start
  }
}
