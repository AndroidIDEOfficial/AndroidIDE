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

import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler
import io.github.rosemoe.sora.lang.styling.Styles
import io.github.rosemoe.sora.lang.styling.StylesUtils
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.Content
import kotlin.math.max
import kotlin.math.min

/**
 * Base class for new line handler implementations.
 *
 * @author Akash Yadav
 */
abstract class BaseNewlineHandler : NewlineHandler {
  
  protected val openingBrackets = mutableListOf<String>()
  protected val closingBrackets = mutableListOf<String>()
  
  override fun matchesRequirement(text: Content, position: CharPosition, style: Styles?): Boolean {
    val line = text.getLine(position.line)
    return !StylesUtils.checkNoCompletion(style, position) &&
      (getNonEmptyTextBefore(line, position.column, 1) in openingBrackets) &&
      (getNonEmptyTextAfter(line, position.column, 1) in closingBrackets)
  }
  
  @Suppress("SameParameterValue")
  protected open fun getNonEmptyTextBefore(text: CharSequence, index: Int, length: Int): String {
    var idx = index
    while (idx > 0 && Character.isWhitespace(text[idx - 1])) {
      idx--
    }
    return text.subSequence(max(0, idx - length), idx).toString()
  }
  
  @Suppress("SameParameterValue")
  protected open fun getNonEmptyTextAfter(text: CharSequence, index: Int, length: Int): String {
    var idx = index
    while (idx < text.length && Character.isWhitespace(text[idx])) {
      idx++
    }
    return text.subSequence(idx, min(idx + length, text.length)).toString()
  }
}