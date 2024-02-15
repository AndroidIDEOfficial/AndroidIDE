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

package com.itsaky.androidide.editor.language.groovy

import com.itsaky.androidide.editor.language.incremental.BaseIncrementalAnalyzeManager
import com.itsaky.androidide.editor.language.incremental.IncrementalToken
import com.itsaky.androidide.editor.language.incremental.LineState
import com.itsaky.androidide.lexers.groovy.GroovyLexer
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.SpanFactory
import io.github.rosemoe.sora.lang.styling.TextStyle

/** @author Akash Yadav */
class GroovyAnalyzer : BaseIncrementalAnalyzeManager(GroovyLexer::class.java) {

  override fun getCodeBlockTokens() = intArrayOf(GroovyLexer.LBRACE, GroovyLexer.RBRACE)

  override fun generateSpans(
    tokens: LineTokenizeResult<LineState, IncrementalToken>,
  ): List<Span> {
    val spans = mutableListOf<Span>()
    var previous = GroovyLexer.WS
    var first = true
    for (token in tokens.tokens) {
      val type = token.type
      val offset = token.startIndex
      when (type) {
        GroovyLexer.WS -> {
          if (first) {
            spans.add(SpanFactory.obtain(offset, TextStyle.makeStyle(SchemeAndroidIDE.TEXT_NORMAL)))
            first = false
          }
        }

        GroovyLexer.ABSTRACT,
        GroovyLexer.ASSERT,
        GroovyLexer.BREAK,
        GroovyLexer.CASE,
        GroovyLexer.CATCH,
        GroovyLexer.CLASS,
        GroovyLexer.CONST,
        GroovyLexer.CONTINUE,
        GroovyLexer.DEFAULT,
        GroovyLexer.DO,
        GroovyLexer.ELSE,
        GroovyLexer.EXTENDS,
        GroovyLexer.FINAL,
        GroovyLexer.FINALLY,
        GroovyLexer.FOR,
        GroovyLexer.IF,
        GroovyLexer.GOTO,
        GroovyLexer.IMPLEMENTS,
        GroovyLexer.IMPORT,
        GroovyLexer.INSTANCEOF,
        GroovyLexer.INTERFACE,
        GroovyLexer.NATIVE,
        GroovyLexer.NEW,
        GroovyLexer.PACKAGE,
        GroovyLexer.PRIVATE,
        GroovyLexer.PROTECTED,
        GroovyLexer.PUBLIC,
        GroovyLexer.RETURN,
        GroovyLexer.STATIC,
        GroovyLexer.STRICTFP,
        GroovyLexer.SUPER,
        GroovyLexer.SWITCH,
        GroovyLexer.SYNCHRONIZED,
        GroovyLexer.THIS,
        GroovyLexer.THROW,
        GroovyLexer.THROWS,
        GroovyLexer.TRANSIENT,
        GroovyLexer.TRY,
        GroovyLexer.VOID,
        GroovyLexer.VOLATILE,
        GroovyLexer.WHILE -> spans.add(SpanFactory.obtain(offset, SchemeAndroidIDE.forKeyword()))

        GroovyLexer.DECIMAL_LITERAL,
        GroovyLexer.HEX_LITERAL,
        GroovyLexer.OCT_LITERAL,
        GroovyLexer.BINARY_LITERAL,
        GroovyLexer.FLOAT_LITERAL,
        GroovyLexer.HEX_FLOAT_LITERAL,
        GroovyLexer.BOOL_LITERAL,
        GroovyLexer.CHAR_LITERAL,
        GroovyLexer.NULL_LITERAL,
        ->
          spans.add(SpanFactory.obtain(offset, TextStyle.makeStyle(SchemeAndroidIDE.LITERAL)))

        GroovyLexer.SINGLE_QUOTE_STRING,
        GroovyLexer.STRING_LITERAL -> spans.add(
          SpanFactory.obtain(offset, SchemeAndroidIDE.forString()))

        GroovyLexer.LPAREN,
        GroovyLexer.RPAREN,
        GroovyLexer.LBRACK,
        GroovyLexer.RBRACK,
        GroovyLexer.SEMI,
        GroovyLexer.COMMA,
        GroovyLexer.ASSIGN,
        GroovyLexer.GT,
        GroovyLexer.LT,
        GroovyLexer.BANG,
        GroovyLexer.TILDE,
        GroovyLexer.QUESTION,
        GroovyLexer.COLON,
        GroovyLexer.EQUAL,
        GroovyLexer.GE,
        GroovyLexer.LE,
        GroovyLexer.NOTEQUAL,
        GroovyLexer.AND,
        GroovyLexer.OR,
        GroovyLexer.INC,
        GroovyLexer.DEC,
        GroovyLexer.ADD,
        GroovyLexer.SUB,
        GroovyLexer.MUL,
        GroovyLexer.DIV,
        GroovyLexer.BITAND,
        GroovyLexer.BITOR,
        GroovyLexer.CARET,
        GroovyLexer.MOD,
        GroovyLexer.ADD_ASSIGN,
        GroovyLexer.SUB_ASSIGN,
        GroovyLexer.MUL_ASSIGN,
        GroovyLexer.DIV_ASSIGN,
        GroovyLexer.AND_ASSIGN,
        GroovyLexer.OR_ASSIGN,
        GroovyLexer.XOR_ASSIGN,
        GroovyLexer.MOD_ASSIGN,
        GroovyLexer.LSHIFT_ASSIGN,
        GroovyLexer.RSHIFT_ASSIGN,
        GroovyLexer.URSHIFT_ASSIGN,
        GroovyLexer.ARROW,
        GroovyLexer.COLONCOLON,
        GroovyLexer.ELLIPSIS,
        GroovyLexer.LBRACE,
        GroovyLexer.RBRACE,
        GroovyLexer.DOT,
        ->
          spans.add(SpanFactory.obtain(offset, TextStyle.makeStyle(SchemeAndroidIDE.OPERATOR)))

        GroovyLexer.BOOLEAN,
        GroovyLexer.BYTE,
        GroovyLexer.CHAR,
        GroovyLexer.DOUBLE,
        GroovyLexer.ENUM,
        GroovyLexer.FLOAT,
        GroovyLexer.INT,
        GroovyLexer.LONG,
        GroovyLexer.SHORT,
        ->
          spans.add(SpanFactory.obtain(offset, TextStyle.makeStyle(SchemeAndroidIDE.TYPE_NAME)))

        GroovyLexer.COMMENT -> spans.add(SpanFactory.obtain(offset, SchemeAndroidIDE.forComment()))
        GroovyLexer.LINE_COMMENT -> handleLineCommentSpan(token, spans, offset)
        GroovyLexer.AT ->
          spans.add(SpanFactory.obtain(offset, TextStyle.makeStyle(SchemeAndroidIDE.ANNOTATION)))

        GroovyLexer.IDENTIFIER -> {
          var colorId = SchemeAndroidIDE.TEXT_NORMAL
          if (previous == GroovyLexer.AT) {
            colorId = SchemeAndroidIDE.ANNOTATION
          }
          spans.add(SpanFactory.obtain(offset, TextStyle.makeStyle(colorId)))
        }

        else -> spans.add(
          SpanFactory.obtain(offset, TextStyle.makeStyle(SchemeAndroidIDE.TEXT_NORMAL)))
      }
      previous = type
    }

    return spans
  }

  override fun getMultilineTokenStartEndTypes(): Array<IntArray> {
    val start = intArrayOf(GroovyLexer.DIV, GroovyLexer.MUL)
    val end = intArrayOf(GroovyLexer.MUL, GroovyLexer.DIV)
    return arrayOf(start, end)
  }

  override fun handleIncompleteToken(token: IncrementalToken) {
    token.type = GroovyLexer.COMMENT
  }
}
