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

package com.itsaky.androidide.editor.language.utils

import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference

/**
 * Utility functions for completion providers.
 *
 * @author Akash Yadav
 */
object CompletionHelper {

  /**
   * Searches backward on the line, with the given checker to check chars.
   * Returns the longest text that matches the requirement.
   *
   * This is a variant of [CompletionHelper.computePrefix][io.github.rosemoe.sora.lang.completion.CompletionHelper.computePrefix]
   * which inlines the predicate for better performance.
   */
  inline fun computePrefix(
    ref: ContentReference, pos: CharPosition,
    checker: (Char) -> Boolean
  ): String {
    var begin = pos.column
    val line = ref.getLine(pos.line)
    while (begin > 0) {
      if (!checker(line[begin - 1])) {
        break
      }
      begin--
    }
    return line.substring(begin, pos.column)
  }
}