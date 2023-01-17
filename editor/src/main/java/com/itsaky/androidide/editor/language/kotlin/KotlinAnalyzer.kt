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

package com.itsaky.androidide.editor.language.kotlin

import com.itsaky.androidide.editor.language.incremental.BaseIncrementalAnalyzeManager
import com.itsaky.androidide.editor.language.incremental.IncrementalToken
import com.itsaky.androidide.editor.language.incremental.LineState
import com.itsaky.androidide.lexers.kotlin.KotlinLexer
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.ANNOTATION
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString
import io.github.rosemoe.sora.lang.analysis.IncrementalAnalyzeManager.LineTokenizeResult
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle

/**
 * Incremental syntax analyzer for Kotlin.
 *
 * @author Akash Yadav
 */
class KotlinAnalyzer : BaseIncrementalAnalyzeManager(KotlinLexer::class.java) {

  override fun getCodeBlockTokens() = intArrayOf(KotlinLexer.LCURL, KotlinLexer.RCURL)

  override fun getMultilineTokenStartEndTypes(): Array<IntArray> {
    val start = intArrayOf(KotlinLexer.DIV, KotlinLexer.MULT)
    val end = intArrayOf(KotlinLexer.MULT, KotlinLexer.DIV)
    return arrayOf(start, end)
  }

  override fun generateSpans(
    tokens: LineTokenizeResult<LineState, IncrementalToken>,
  ): MutableList<Span> {
    val spans = mutableListOf<Span>()
    var first = true
    for (token in tokens.tokens) {
      val type = token.type
      val offset = token.startIndex

      when (type) {
        KotlinLexer.WS ->
          if (first) {
            spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
            first = false
          }
        KotlinLexer.AT_WS -> spans.add(Span.obtain(offset, makeStyle(ANNOTATION)))
        KotlinLexer.ANNOTATION,
        KotlinLexer.ABSTRACT,
        KotlinLexer.BY,
        KotlinLexer.CATCH,
        KotlinLexer.COMPANION,
        KotlinLexer.CONSTRUCTOR,
        KotlinLexer.CROSSINLINE,
        KotlinLexer.DATA,
        KotlinLexer.DYNAMIC,
        KotlinLexer.ENUM,
        KotlinLexer.EXTERNAL,
        KotlinLexer.FINAL,
        KotlinLexer.FINALLY,
        KotlinLexer.IMPORT,
        KotlinLexer.INFIX,
        KotlinLexer.INIT,
        KotlinLexer.INLINE,
        KotlinLexer.INNER,
        KotlinLexer.INTERNAL,
        KotlinLexer.LATEINIT,
        KotlinLexer.NOINLINE,
        KotlinLexer.OPEN,
        KotlinLexer.OPERATOR,
        KotlinLexer.OUT,
        KotlinLexer.OVERRIDE,
        KotlinLexer.PRIVATE,
        KotlinLexer.PROTECTED,
        KotlinLexer.PUBLIC,
        KotlinLexer.REIFIED,
        KotlinLexer.SEALED,
        KotlinLexer.TAILREC,
        KotlinLexer.VARARG,
        KotlinLexer.WHERE,
        KotlinLexer.EXPECT,
        KotlinLexer.ACTUAL,
        KotlinLexer.CONST,
        KotlinLexer.SUSPEND,
        KotlinLexer.RETURN_AT,
        KotlinLexer.CONTINUE_AT,
        KotlinLexer.BREAK_AT,
        KotlinLexer.THIS_AT,
        KotlinLexer.SUPER_AT,
        KotlinLexer.PACKAGE,
        KotlinLexer.CLASS,
        KotlinLexer.INTERFACE,
        KotlinLexer.FUN,
        KotlinLexer.OBJECT,
        KotlinLexer.VAL,
        KotlinLexer.VAR,
        KotlinLexer.TYPE_ALIAS,
        KotlinLexer.THIS,
        KotlinLexer.SUPER,
        KotlinLexer.TYPEOF,
        KotlinLexer.IF,
        KotlinLexer.ELSE,
        KotlinLexer.WHEN,
        KotlinLexer.TRY,
        KotlinLexer.FOR,
        KotlinLexer.DO,
        KotlinLexer.WHILE,
        KotlinLexer.THROW,
        KotlinLexer.RETURN,
        KotlinLexer.CONTINUE,
        KotlinLexer.BREAK,
        KotlinLexer.AS,
        KotlinLexer.IS,
        KotlinLexer.IN,
        KotlinLexer.NOT_IS,
        KotlinLexer.NOT_IN -> spans.add(Span.obtain(offset, forKeyword()))
        KotlinLexer.RealLiteral,
        KotlinLexer.FloatLiteral,
        KotlinLexer.DoubleLiteral,
        KotlinLexer.IntegerLiteral,
        KotlinLexer.HexLiteral,
        KotlinLexer.BinLiteral,
        KotlinLexer.LongLiteral,
        KotlinLexer.BooleanLiteral,
        KotlinLexer.NullLiteral,
        KotlinLexer.CharacterLiteral -> spans.add(Span.obtain(offset, makeStyle(LITERAL)))
        KotlinLexer.QUOTE_OPEN,
        KotlinLexer.TRIPLE_QUOTE_OPEN,
        KotlinLexer.QUOTE_CLOSE,
        KotlinLexer.LineStrRef,
        KotlinLexer.LineStrText,
        KotlinLexer.LineStrEscapedChar,
        KotlinLexer.LineStrExprStart,
        KotlinLexer.TRIPLE_QUOTE_CLOSE,
        KotlinLexer.MultiLineStringQuote,
        KotlinLexer.MultiLineStrRef,
        KotlinLexer.MultiLineStrText,
        KotlinLexer.MultiLineStrExprStart -> spans.add(Span.obtain(offset, forString()))
        KotlinLexer.RESERVED,
        KotlinLexer.DOT,
        KotlinLexer.COMMA,
        KotlinLexer.LPAREN,
        KotlinLexer.RPAREN,
        KotlinLexer.LSQUARE,
        KotlinLexer.RSQUARE,
        KotlinLexer.LCURL,
        KotlinLexer.RCURL,
        KotlinLexer.MULT,
        KotlinLexer.MOD,
        KotlinLexer.DIV,
        KotlinLexer.ADD,
        KotlinLexer.SUB,
        KotlinLexer.INCR,
        KotlinLexer.DECR,
        KotlinLexer.CONJ,
        KotlinLexer.DISJ,
        KotlinLexer.EXCL_WS,
        KotlinLexer.EXCL_NO_WS,
        KotlinLexer.COLON,
        KotlinLexer.SEMICOLON,
        KotlinLexer.ASSIGNMENT,
        KotlinLexer.ADD_ASSIGNMENT,
        KotlinLexer.SUB_ASSIGNMENT,
        KotlinLexer.MULT_ASSIGNMENT,
        KotlinLexer.DIV_ASSIGNMENT,
        KotlinLexer.MOD_ASSIGNMENT,
        KotlinLexer.ARROW,
        KotlinLexer.DOUBLE_ARROW,
        KotlinLexer.RANGE,
        KotlinLexer.COLONCOLON,
        KotlinLexer.DOUBLE_SEMICOLON,
        KotlinLexer.QUEST_WS,
        KotlinLexer.QUEST_NO_WS,
        KotlinLexer.LANGLE,
        KotlinLexer.RANGLE,
        KotlinLexer.LE,
        KotlinLexer.GE,
        KotlinLexer.EXCL_EQ,
        KotlinLexer.EXCL_EQEQ,
        KotlinLexer.`AS_SAFE`,
        KotlinLexer.EQEQ,
        KotlinLexer.EQEQEQ,
        KotlinLexer.SINGLE_QUOTE -> spans.add(Span.obtain(offset, makeStyle(OPERATOR)))
        KotlinLexer.LineComment -> handleLineCommentSpan(token, spans, offset)
        KotlinLexer.ShebangLine,
        KotlinLexer.DelimitedComment,
        KotlinLexer.Inside_Comment -> spans.add(Span.obtain(offset, forComment()))
        else -> spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)))
      }
    }
    return spans
  }

  override fun handleIncompleteToken(token: IncrementalToken) {
    token.type = KotlinLexer.DelimitedComment
  }
}
