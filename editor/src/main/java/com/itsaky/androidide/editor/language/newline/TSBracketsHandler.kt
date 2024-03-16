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

package com.itsaky.androidide.editor.language.newline

import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandleResult
import io.github.rosemoe.sora.lang.styling.Styles
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.TextUtils

/**
 * Newline handler for tree-sitter languages.
 *
 * @author Akash Yadav
 */
abstract class TSBracketsHandler(private val language: TreeSitterLanguage) : BaseNewlineHandler() {

  override fun handleNewline(
    text: Content,
    position: CharPosition,
    style: Styles?,
    tabSize: Int
  ): NewlineHandleResult {
    val count = TextUtils.countLeadingSpaceCount(text.getLine(position.line), tabSize)
    var txt: String
    val sb =
      StringBuilder("\n")
        .append(TextUtils.createIndent(count + tabSize, tabSize, language.useTab()))
        .append("\n")
        .append(
          TextUtils.createIndent(count, tabSize, language.useTab()).also { txt = it }
        )
    return NewlineHandleResult(sb, txt.length + 1)
  }
}
