/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.syntax.highlighters;

import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import com.itsaky.androidide.lexers.java.JavaLexer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class JavaHighlighter implements Highlighter {

  @Override
  public SpannableStringBuilder highlight(SchemeAndroidIDE scheme, String code, String match) throws Exception {
    final JavaLexer lexer = new JavaLexer(CharStreams.fromReader(new StringReader(code)));
    final SpannableStringBuilder sb = new SpannableStringBuilder();
    Token token;
    while ((token = lexer.nextToken()) != null) {
      if (token.getType() == JavaLexer.EOF) break;
      switch (token.getType()) {
        case JavaLexer.WS:
          sb.append(token.getText());
          break;
        case JavaLexer.ABSTRACT:
        case JavaLexer.ASSERT:
        case JavaLexer.BREAK:
        case JavaLexer.CASE:
        case JavaLexer.CATCH:
        case JavaLexer.CLASS:
        case JavaLexer.CONST:
        case JavaLexer.CONTINUE:
        case JavaLexer.DEFAULT:
        case JavaLexer.DO:
        case JavaLexer.ELSE:
        case JavaLexer.EXTENDS:
        case JavaLexer.FINAL:
        case JavaLexer.FINALLY:
        case JavaLexer.FOR:
        case JavaLexer.IF:
        case JavaLexer.GOTO:
        case JavaLexer.IMPLEMENTS:
        case JavaLexer.IMPORT:
        case JavaLexer.INSTANCEOF:
        case JavaLexer.INTERFACE:
        case JavaLexer.NATIVE:
        case JavaLexer.NEW:
        case JavaLexer.PACKAGE:
        case JavaLexer.PRIVATE:
        case JavaLexer.PROTECTED:
        case JavaLexer.PUBLIC:
        case JavaLexer.RETURN:
        case JavaLexer.STATIC:
        case JavaLexer.STRICTFP:
        case JavaLexer.SUPER:
        case JavaLexer.SWITCH:
        case JavaLexer.SYNCHRONIZED:
        case JavaLexer.THIS:
        case JavaLexer.THROW:
        case JavaLexer.THROWS:
        case JavaLexer.TRANSIENT:
        case JavaLexer.TRY:
        case JavaLexer.VOID:
        case JavaLexer.VOLATILE:
        case JavaLexer.WHILE:
          sb.append(
              token.getText(),
              new ForegroundColorSpan(scheme.getColor(EditorColorScheme.KEYWORD)),
              SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
          break;
        case JavaLexer.DECIMAL_LITERAL:
        case JavaLexer.HEX_LITERAL:
        case JavaLexer.OCT_LITERAL:
        case JavaLexer.BINARY_LITERAL:
        case JavaLexer.FLOAT_LITERAL:
        case JavaLexer.HEX_FLOAT_LITERAL:
        case JavaLexer.BOOL_LITERAL:
        case JavaLexer.CHAR_LITERAL:
        case JavaLexer.NULL_LITERAL:
        case JavaLexer.STRING_LITERAL:
          sb.append(
              token.getText(),
              new ForegroundColorSpan(scheme.getColor(EditorColorScheme.LITERAL)),
              SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
          break;
        case JavaLexer.LPAREN:
        case JavaLexer.RPAREN:
        case JavaLexer.LBRACK:
        case JavaLexer.RBRACK:
        case JavaLexer.SEMI:
        case JavaLexer.COMMA:
        case JavaLexer.ASSIGN:
        case JavaLexer.GT:
        case JavaLexer.LT:
        case JavaLexer.BANG:
        case JavaLexer.TILDE:
        case JavaLexer.QUESTION:
        case JavaLexer.COLON:
        case JavaLexer.EQUAL:
        case JavaLexer.GE:
        case JavaLexer.LE:
        case JavaLexer.NOTEQUAL:
        case JavaLexer.AND:
        case JavaLexer.OR:
        case JavaLexer.INC:
        case JavaLexer.DEC:
        case JavaLexer.ADD:
        case JavaLexer.SUB:
        case JavaLexer.MUL:
        case JavaLexer.DIV:
        case JavaLexer.BITAND:
        case JavaLexer.BITOR:
        case JavaLexer.CARET:
        case JavaLexer.MOD:
        case JavaLexer.ADD_ASSIGN:
        case JavaLexer.SUB_ASSIGN:
        case JavaLexer.MUL_ASSIGN:
        case JavaLexer.DIV_ASSIGN:
        case JavaLexer.AND_ASSIGN:
        case JavaLexer.OR_ASSIGN:
        case JavaLexer.XOR_ASSIGN:
        case JavaLexer.MOD_ASSIGN:
        case JavaLexer.LSHIFT_ASSIGN:
        case JavaLexer.RSHIFT_ASSIGN:
        case JavaLexer.URSHIFT_ASSIGN:
        case JavaLexer.ARROW:
        case JavaLexer.COLONCOLON:
        case JavaLexer.ELLIPSIS:
        case JavaLexer.DOT:
        case JavaLexer.LBRACE:
        case JavaLexer.RBRACE:
        case JavaLexer.AT:
          sb.append(
              token.getText(),
              new ForegroundColorSpan(scheme.getColor(EditorColorScheme.OPERATOR)),
              SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
          break;
        case JavaLexer.BOOLEAN:
        case JavaLexer.BYTE:
        case JavaLexer.CHAR:
        case JavaLexer.DOUBLE:
        case JavaLexer.ENUM:
        case JavaLexer.FLOAT:
        case JavaLexer.INT:
        case JavaLexer.LONG:
        case JavaLexer.SHORT:
          sb.append(
              token.getText(),
              new ForegroundColorSpan(scheme.getColor(SchemeAndroidIDE.TYPE_NAME)),
              SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
          break;
        case JavaLexer.BLOCK_COMMENT:
        case JavaLexer.LINE_COMMENT:
          sb.append(
              token.getText(),
              new ForegroundColorSpan(scheme.getColor(EditorColorScheme.COMMENT)),
              SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
          break;
        default:
          sb.append(
              token.getText(),
              new ForegroundColorSpan(scheme.getColor(EditorColorScheme.TEXT_NORMAL)),
              SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
          break;
      }
    }
    final BackgroundColorSpan bg = new BackgroundColorSpan(0xffffff00);
    final ForegroundColorSpan fg = new ForegroundColorSpan(0xff000000);

    Pattern pattern = Pattern.compile(Pattern.quote(match));
    Matcher matcher = pattern.matcher(code);
    while (matcher.find()) {
      sb.setSpan(
          bg, matcher.start(), matcher.end(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
      sb.setSpan(
          fg, matcher.start(), matcher.end(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    return sb;
  }
}
