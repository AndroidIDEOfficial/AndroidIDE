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

package com.itsaky.androidide.editor.language.incremental

import org.antlr.v4.runtime.Lexer

/**
 * Tokenization state of a line.
 *
 * @param state The state of the line.
 * @param hasBraces `true` if the line has braces. `false` otherwise.
 * @param lexerMode The mode of the lexer. This MUST be preserved in the lexer.
 *
 * @author Akash Yadav
 */
data class LineState(
  @JvmField var state: Int = NORMAL,
  @JvmField var hasBraces: Boolean = false,
  @JvmField var lexerMode: Int = Lexer.DEFAULT_MODE
) {
  companion object {
    const val NORMAL = 0
    const val INCOMPLETE = 1
  }
}
