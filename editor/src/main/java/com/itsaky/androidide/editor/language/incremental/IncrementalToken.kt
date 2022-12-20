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

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.TokenSource

/**
 * Token used for incremental lexing tasks. Allows us to store other information to tokens.
 *
 * @author Akash Yadav
 */
class IncrementalToken(val token: Token) : Token {

  @JvmField var type = token.type
  @JvmField var startIndex = token.startIndex
  @JvmField var incomplete = false

  override fun getText(): String = token.text
  override fun getType() = type
  override fun getLine() = token.line
  override fun getCharPositionInLine() = token.charPositionInLine
  override fun getChannel() = token.channel
  override fun getTokenIndex() = token.tokenIndex
  override fun getStartIndex() = startIndex
  override fun getStopIndex() = token.stopIndex
  override fun getTokenSource(): TokenSource = token.tokenSource
  override fun getInputStream(): CharStream = token.inputStream
  override fun equals(other: Any?) = token == other
  override fun hashCode() = token.hashCode()
  override fun toString() = token.toString()
}
