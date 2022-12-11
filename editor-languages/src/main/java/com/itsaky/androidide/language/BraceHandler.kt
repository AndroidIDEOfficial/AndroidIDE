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

package com.itsaky.androidide.language

import io.github.rosemoe.sora.lang.smartEnter.NewlineHandleResult
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler
import io.github.rosemoe.sora.lang.styling.Styles
import io.github.rosemoe.sora.lang.styling.StylesUtils
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.TextUtils
import kotlin.math.max
import kotlin.math.min

internal class BraceHandler(val getIndentAdvance : (String?) -> Int, val useTab: () -> Boolean) : NewlineHandler {
  override fun matchesRequirement(text: Content, position: CharPosition, style: Styles?): Boolean {
    val line = text.getLine(position.line)
    return !StylesUtils.checkNoCompletion(style, position) &&
      (getNonEmptyTextBefore(line, position.column, 1) == "{") &&
      (getNonEmptyTextAfter(line, position.column, 1) == "}")
  }

  override fun handleNewline(
    text: Content,
    position: CharPosition,
    style: Styles?,
    tabSize: Int
  ): NewlineHandleResult {
    val line = text.getLine(position.line)
    val index = position.column
    val beforeText = line.subSequence(0, index).toString()
    val afterText = line.subSequence(index, line.length).toString()
    return handleNewline(beforeText, afterText, tabSize)
  }

  private fun handleNewline(beforeText: String?, afterText: String?, tabSize: Int): NewlineHandleResult {
    val count = TextUtils.countLeadingSpaceCount(beforeText!!, tabSize)
    val advanceBefore: Int = getIndentAdvance(beforeText)
    val advanceAfter: Int = getIndentAdvance(afterText)
    var text: String
    val sb =
      StringBuilder("\n")
        .append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()))
        .append('\n')
        .append(TextUtils.createIndent(count + advanceAfter, tabSize, useTab()).also { text = it })
    val shiftLeft = text.length + 1
    return NewlineHandleResult(sb, shiftLeft)
  }

  @Suppress("SameParameterValue")
  private fun getNonEmptyTextBefore(text: CharSequence, index: Int, length: Int): String {
    var idx = index
    while (idx > 0 && Character.isWhitespace(text[idx - 1])) {
      idx--
    }
    return text.subSequence(max(0, idx - length), idx).toString()
  }

  @Suppress("SameParameterValue")
  private fun getNonEmptyTextAfter(text: CharSequence, index: Int, length: Int): String {
    var idx = index
    while (idx < text.length && Character.isWhitespace(text[idx])) {
      idx++
    }
    return text.subSequence(idx, min(idx + length, text.length)).toString()
  }
}
