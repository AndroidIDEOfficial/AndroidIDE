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
package com.itsaky.androidide.editor.language.java

import com.itsaky.androidide.editor.language.incremental.BaseIncrementalAnalyzeManager
import com.itsaky.androidide.editor.language.incremental.IncrementalToken
import com.itsaky.androidide.editor.language.incremental.LineState
import com.itsaky.androidide.lexers.java.JavaLexer
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.ANNOTATION
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TYPE_NAME
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle

/** @author Akash Yadav */
class JavaAnalyzer : BaseIncrementalAnalyzeManager(JavaLexer::class.java) {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun getCodeBlockTokens(): IntArray = intArrayOf(JavaLexer.LBRACE, JavaLexer.RBRACE)

  override fun generateSpans(
    tokens: LineTokenizeResult<LineState, IncrementalToken>,
  ): List<Span> {
    val spans = mutableListOf<Span>()
    var previous = JavaLexer.WS
    var first = true
    for (token in tokens.tokens) {
      val type = token.type
      val offset = token.startIndex
      when (type) {
        JavaLexer.WS -> {
          if (first) {
            spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
            first = false
          }
        }
        JavaLexer.ABSTRACT,
        JavaLexer.ASSERT,
        JavaLexer.BREAK,
        JavaLexer.CASE,
        JavaLexer.CATCH,
        JavaLexer.CLASS,
        JavaLexer.CONST,
        JavaLexer.CONTINUE,
        JavaLexer.DEFAULT,
        JavaLexer.DO,
        JavaLexer.ELSE,
        JavaLexer.EXTENDS,
        JavaLexer.FINAL,
        JavaLexer.FINALLY,
        JavaLexer.FOR,
        JavaLexer.IF,
        JavaLexer.GOTO,
        JavaLexer.IMPLEMENTS,
        JavaLexer.IMPORT,
        JavaLexer.INSTANCEOF,
        JavaLexer.INTERFACE,
        JavaLexer.NATIVE,
        JavaLexer.NEW,
        JavaLexer.PACKAGE,
        JavaLexer.PRIVATE,
        JavaLexer.PROTECTED,
        JavaLexer.PUBLIC,
        JavaLexer.RETURN,
        JavaLexer.STATIC,
        JavaLexer.STRICTFP,
        JavaLexer.SUPER,
        JavaLexer.SWITCH,
        JavaLexer.SYNCHRONIZED,
        JavaLexer.THIS,
        JavaLexer.THROW,
        JavaLexer.THROWS,
        JavaLexer.TRANSIENT,
        JavaLexer.TRY,
        JavaLexer.VOID,
        JavaLexer.VOLATILE,
        JavaLexer.WHILE,
        JavaLexer.VAR, -> spans.add(Span.obtain(offset, forKeyword()))
        JavaLexer.DECIMAL_LITERAL,
        JavaLexer.HEX_LITERAL,
        JavaLexer.OCT_LITERAL,
        JavaLexer.BINARY_LITERAL,
        JavaLexer.FLOAT_LITERAL,
        JavaLexer.HEX_FLOAT_LITERAL,
        JavaLexer.BOOL_LITERAL,
        JavaLexer.CHAR_LITERAL,
        JavaLexer.NULL_LITERAL, -> spans.add(Span.obtain(offset, makeStyle(LITERAL)))
        JavaLexer.STRING_LITERAL -> spans.add(Span.obtain(offset, forString()))
        JavaLexer.LPAREN,
        JavaLexer.RPAREN,
        JavaLexer.LBRACK,
        JavaLexer.RBRACK,
        JavaLexer.SEMI,
        JavaLexer.COMMA,
        JavaLexer.ASSIGN,
        JavaLexer.GT,
        JavaLexer.LT,
        JavaLexer.BANG,
        JavaLexer.TILDE,
        JavaLexer.QUESTION,
        JavaLexer.COLON,
        JavaLexer.EQUAL,
        JavaLexer.GE,
        JavaLexer.LE,
        JavaLexer.NOTEQUAL,
        JavaLexer.AND,
        JavaLexer.OR,
        JavaLexer.INC,
        JavaLexer.DEC,
        JavaLexer.ADD,
        JavaLexer.SUB,
        JavaLexer.MUL,
        JavaLexer.DIV,
        JavaLexer.BITAND,
        JavaLexer.BITOR,
        JavaLexer.CARET,
        JavaLexer.MOD,
        JavaLexer.ADD_ASSIGN,
        JavaLexer.SUB_ASSIGN,
        JavaLexer.MUL_ASSIGN,
        JavaLexer.DIV_ASSIGN,
        JavaLexer.AND_ASSIGN,
        JavaLexer.OR_ASSIGN,
        JavaLexer.XOR_ASSIGN,
        JavaLexer.MOD_ASSIGN,
        JavaLexer.LSHIFT_ASSIGN,
        JavaLexer.RSHIFT_ASSIGN,
        JavaLexer.URSHIFT_ASSIGN,
        JavaLexer.ARROW,
        JavaLexer.COLONCOLON,
        JavaLexer.ELLIPSIS,
        JavaLexer.LBRACE,
        JavaLexer.RBRACE,
        JavaLexer.DOT, -> spans.add(Span.obtain(offset, makeStyle(OPERATOR)))
        JavaLexer.BOOLEAN,
        JavaLexer.BYTE,
        JavaLexer.CHAR,
        JavaLexer.DOUBLE,
        JavaLexer.ENUM,
        JavaLexer.FLOAT,
        JavaLexer.INT,
        JavaLexer.LONG,
        JavaLexer.SHORT, -> spans.add(Span.obtain(offset, makeStyle(TYPE_NAME)))
        JavaLexer.BLOCK_COMMENT -> spans.add(Span.obtain(offset, forComment()))
        JavaLexer.LINE_COMMENT -> handleLineCommentSpan(token, spans, offset)
        JavaLexer.AT -> spans.add(Span.obtain(offset, makeStyle(ANNOTATION)))
        JavaLexer.IDENTIFIER -> {
          var colorId = TEXT_NORMAL
          if (previous == JavaLexer.AT) {
            colorId = ANNOTATION
          }
          spans.add(Span.obtain(offset, makeStyle(colorId)))
        }
        else -> spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
      }
      previous = type
    }

    return spans
  }

  override fun getMultilineTokenStartEndTypes(): Array<IntArray> {
    val start = intArrayOf(JavaLexer.DIV, JavaLexer.MUL)
    val end = intArrayOf(JavaLexer.MUL, JavaLexer.DIV)
    return arrayOf(start, end)
  }

  override fun handleIncompleteToken(token: IncrementalToken) {
    token.type = JavaLexer.BLOCK_COMMENT
  }
}
