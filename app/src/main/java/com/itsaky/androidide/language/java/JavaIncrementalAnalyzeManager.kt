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
package com.itsaky.androidide.language.java

import com.itsaky.androidide.language.incremental.BaseIncrementalAnalyzeManager
import com.itsaky.androidide.language.incremental.IncrementalToken
import com.itsaky.androidide.language.incremental.LineState
import com.itsaky.androidide.lexers.java.JavaLexer
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.ANNOTATION
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.COMMENT
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.FIXME_COMMENT
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TODO_COMMENT
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TYPE_NAME
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.withoutCompletion
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.models.DiagnosticItem
import com.itsaky.lsp.models.DiagnosticSeverity.WARNING
import com.itsaky.lsp.models.Position
import com.itsaky.lsp.models.Range
import io.github.rosemoe.sora.lang.styling.CodeBlock
import io.github.rosemoe.sora.lang.styling.MappedSpans
import io.github.rosemoe.sora.lang.styling.Span
import io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle
import io.github.rosemoe.sora.text.Content
import org.antlr.v4.runtime.CharStreams

/** @author Akash Yadav */
class JavaIncrementalAnalyzeManager :
  BaseIncrementalAnalyzeManager(JavaLexer(CharStreams.fromString(""))) {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun computeBlocks(text: Content, delegate: CodeBlockAnalyzeDelegate): List<CodeBlock> {
    return emptyList()
  }

  override fun generateSpans(
    tokens: AsyncLineTokenizeResult<LineState, IncrementalToken>,
  ): MappedSpans.Builder {
    val line = tokens.line
    val spans = MappedSpans.Builder(1)
    var previous = JavaLexer.WS
    var first = true
    for (token in tokens.tokens) {
      val type = token.type
      val offset = token.startIndex
      val tokenLength = token.text.length
      when (type) {
        JavaLexer.WS -> {
          if (first) {
            spans.addIfNeeded(line, offset, makeStyle(TEXT_NORMAL))
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
        JavaLexer.VAR, -> spans.addIfNeeded(line, offset, forKeyword())
        JavaLexer.DECIMAL_LITERAL,
        JavaLexer.HEX_LITERAL,
        JavaLexer.OCT_LITERAL,
        JavaLexer.BINARY_LITERAL,
        JavaLexer.FLOAT_LITERAL,
        JavaLexer.HEX_FLOAT_LITERAL,
        JavaLexer.BOOL_LITERAL,
        JavaLexer.CHAR_LITERAL,
        JavaLexer.NULL_LITERAL, -> spans.addIfNeeded(line, offset, makeStyle(LITERAL))
        JavaLexer.STRING_LITERAL -> spans.addIfNeeded(line, offset, forString())
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
        JavaLexer.DOT, -> spans.addIfNeeded(line, offset, makeStyle(OPERATOR))
        JavaLexer.BOOLEAN,
        JavaLexer.BYTE,
        JavaLexer.CHAR,
        JavaLexer.DOUBLE,
        JavaLexer.ENUM,
        JavaLexer.FLOAT,
        JavaLexer.INT,
        JavaLexer.LONG,
        JavaLexer.SHORT, -> spans.addIfNeeded(line, offset, makeStyle(TYPE_NAME))
        JavaLexer.BLOCK_COMMENT -> spans.addIfNeeded(line, offset, forComment())
        JavaLexer.LINE_COMMENT -> {
          var commentType = COMMENT

          // highlight special line comments
          var commentText = token.text
          if (commentText.length > 2) {
            commentText = commentText.substring(2)
            commentText = commentText.trim { it <= ' ' }
            var mark = true
            if ("todo".equals(commentText.substring(0, 4), ignoreCase = true)) {
              commentType = TODO_COMMENT
            } else if ("fixme".equals(commentText.substring(0, 5), ignoreCase = true)) {
              commentType = FIXME_COMMENT
            } else {
              mark = false
            }
            if (mark) {
              if (diagnostics == null) {
                diagnostics = ArrayList<DiagnosticItem>()
              }
              val diagnostic = DiagnosticItem()
              diagnostic.severity = WARNING
              diagnostic.message = commentText
              diagnostic.code = "special.comment"
              diagnostic.range = Range(Position(0, offset), Position(0, offset + tokenLength))
              diagnostic.source = commentText
              ideDiagnostics.add(diagnostic)
            }
          }
          spans.addIfNeeded(line, offset, withoutCompletion(commentType))
        }
        JavaLexer.AT -> spans.addIfNeeded(line, offset, makeStyle(ANNOTATION))
        JavaLexer.IDENTIFIER -> {
          var colorId = TEXT_NORMAL
          if (previous == JavaLexer.AT) {
            colorId = ANNOTATION
          }
          spans.addIfNeeded(line, offset, makeStyle(colorId))
        }
        else -> spans.addIfNeeded(line, offset, makeStyle(TEXT_NORMAL))
      }
      previous = type
    }
    
    return spans;
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
