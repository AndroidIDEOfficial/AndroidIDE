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

package com.itsaky.androidide.lsp.util

import androidx.annotation.UiThread
import com.itsaky.androidide.lsp.models.TextEdit
import io.github.rosemoe.sora.widget.CodeEditor

/** @author Akash Yadav */
class RewriteHelper {
  companion object {
    @UiThread
    @JvmStatic
    fun performEdits(edits: List<TextEdit>, editor: CodeEditor) {
      if (edits.isEmpty()) {
        return
      }

      edits.forEach {
        val s = it.range.start
        val e = it.range.end
        if (s == e) {
          editor.text.insert(s.line, s.column, it.newText)
        } else {
          editor.text.replace(s.line, s.column, e.line, e.column, it.newText)
        }
      }
    }
  }
}
