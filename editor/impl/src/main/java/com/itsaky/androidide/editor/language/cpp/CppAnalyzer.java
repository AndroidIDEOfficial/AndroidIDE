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

package com.itsaky.androidide.editor.language.cpp;

import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TYPE_NAME;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString;
import static io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle;

import com.itsaky.androidide.editor.language.incremental.BaseIncrementalAnalyzeManager;
import com.itsaky.androidide.editor.language.incremental.IncrementalToken;
import com.itsaky.androidide.editor.language.incremental.LineState;
import com.itsaky.androidide.lexers.cpp.CPP14Lexer;
import io.github.rosemoe.sora.lang.styling.Span;
import java.util.ArrayList;
import java.util.List;

public class CppAnalyzer extends BaseIncrementalAnalyzeManager {

  public CppAnalyzer() {
    super(CPP14Lexer.class);
  }

  @Override
  protected int[][] getMultilineTokenStartEndTypes() {
    final var start = new int[]{CPP14Lexer.Div, CPP14Lexer.Star};
    final var end = new int[]{CPP14Lexer.Star, CPP14Lexer.Div};
    return new int[][]{start, end};
  }

  @Override
  protected List<Span> generateSpans(final LineTokenizeResult<LineState, IncrementalToken> tokens) {
    final var spans = new ArrayList<Span>();
    spans.add(Span.obtain(0, makeStyle(TEXT_NORMAL)));
    for (int i = 0; i < tokens.tokens.size(); i++) {
      final var token = tokens.tokens.get(i);
      final var type = token.getType();
      final var offset = token.getStartIndex();

      switch (type) {
        case CPP14Lexer.Alignas:
        case CPP14Lexer.Alignof:
        case CPP14Lexer.Asm:
        case CPP14Lexer.Auto:
        case CPP14Lexer.Break:
        case CPP14Lexer.Class:
        case CPP14Lexer.Const:
        case CPP14Lexer.Catch:
        case CPP14Lexer.Case:
        case CPP14Lexer.Constexpr:
        case CPP14Lexer.Const_cast:
        case CPP14Lexer.Continue:
        case CPP14Lexer.Decltype:
        case CPP14Lexer.Default:
        case CPP14Lexer.Delete:
        case CPP14Lexer.Do:
        case CPP14Lexer.Dynamic_cast:
        case CPP14Lexer.Else:
        case CPP14Lexer.Enum:
        case CPP14Lexer.Explicit:
        case CPP14Lexer.Export:
        case CPP14Lexer.Extern:
        case CPP14Lexer.Final:
        case CPP14Lexer.For:
        case CPP14Lexer.Friend:
        case CPP14Lexer.Goto:
        case CPP14Lexer.If:
        case CPP14Lexer.Inline:
        case CPP14Lexer.Mutable:
        case CPP14Lexer.Namespace:
        case CPP14Lexer.New:
        case CPP14Lexer.Noexcept:
        case CPP14Lexer.Operator:
        case CPP14Lexer.Override:
        case CPP14Lexer.Private:
        case CPP14Lexer.Protected:
        case CPP14Lexer.Public:
        case CPP14Lexer.Register:
        case CPP14Lexer.Reinterpret_cast:
        case CPP14Lexer.Return:
        case CPP14Lexer.Sizeof:
        case CPP14Lexer.Signed:
        case CPP14Lexer.Static:
        case CPP14Lexer.Static_cast:
        case CPP14Lexer.Static_assert:
        case CPP14Lexer.Struct:
        case CPP14Lexer.Switch:
        case CPP14Lexer.Template:
        case CPP14Lexer.This:
        case CPP14Lexer.Throw:
        case CPP14Lexer.Thread_local:
        case CPP14Lexer.Try:
        case CPP14Lexer.Typedef:
        case CPP14Lexer.Typeid_:
        case CPP14Lexer.Typename_:
        case CPP14Lexer.Unsigned:
        case CPP14Lexer.Union:
        case CPP14Lexer.Using:
        case CPP14Lexer.Virtual:
        case CPP14Lexer.Volatile:
        case CPP14Lexer.While:
        case CPP14Lexer.Void:
          spans.add(Span.obtain(offset, forKeyword()));
          break;
        case CPP14Lexer.Bool:
        case CPP14Lexer.Char:
        case CPP14Lexer.Char16:
        case CPP14Lexer.Char32:
        case CPP14Lexer.Double:
        case CPP14Lexer.Float:
        case CPP14Lexer.Int:
        case CPP14Lexer.Long:
        case CPP14Lexer.Short:
        case CPP14Lexer.Wchar:
          spans.add(Span.obtain(offset, makeStyle(TYPE_NAME)));
          break;
        case CPP14Lexer.LeftParen:
        case CPP14Lexer.LeftBracket:
        case CPP14Lexer.LeftBrace:
        case CPP14Lexer.Dot:
        case CPP14Lexer.RightParen:
        case CPP14Lexer.RightBracket:
        case CPP14Lexer.RightBrace:
        case CPP14Lexer.Semi:
        case CPP14Lexer.Comma:
        case CPP14Lexer.Plus:
        case CPP14Lexer.Minus:
        case CPP14Lexer.Star:
        case CPP14Lexer.Div:
        case CPP14Lexer.Mod:
        case CPP14Lexer.Caret:
        case CPP14Lexer.And:
        case CPP14Lexer.Or:
        case CPP14Lexer.Tilde:
        case CPP14Lexer.Not:
        case CPP14Lexer.Assign:
        case CPP14Lexer.Less:
        case CPP14Lexer.Greater:
        case CPP14Lexer.PlusAssign:
        case CPP14Lexer.MinusAssign:
        case CPP14Lexer.StarAssign:
        case CPP14Lexer.DivAssign:
        case CPP14Lexer.ModAssign:
        case CPP14Lexer.XorAssign:
        case CPP14Lexer.AndAssign:
        case CPP14Lexer.OrAssign:
        case CPP14Lexer.RightShiftAssign:
        case CPP14Lexer.LeftShiftAssign:
        case CPP14Lexer.Equal:
        case CPP14Lexer.NotEqual:
        case CPP14Lexer.LessEqual:
        case CPP14Lexer.GreaterEqual:
        case CPP14Lexer.AndAnd:
        case CPP14Lexer.OrOr:
        case CPP14Lexer.PlusPlus:
        case CPP14Lexer.MinusMinus:
        case CPP14Lexer.Arrow:
        case CPP14Lexer.ArrowStar:
        case CPP14Lexer.Question:
        case CPP14Lexer.Colon:
        case CPP14Lexer.Doublecolon:
        case CPP14Lexer.DotStar:
        case CPP14Lexer.Ellipsis:
          spans.add(Span.obtain(offset, makeStyle(OPERATOR)));
          break;
        case CPP14Lexer.IntegerLiteral:
        case CPP14Lexer.DecimalLiteral:
        case CPP14Lexer.OctalLiteral:
        case CPP14Lexer.HexadecimalLiteral:
        case CPP14Lexer.BinaryLiteral:
        case CPP14Lexer.Integersuffix:
        case CPP14Lexer.CharacterLiteral:
        case CPP14Lexer.FloatingLiteral:
        case CPP14Lexer.BooleanLiteral:
        case CPP14Lexer.Nullptr:
        case CPP14Lexer.True_:
        case CPP14Lexer.False_:
          spans.add(Span.obtain(offset, makeStyle(LITERAL)));
          break;
        case CPP14Lexer.StringLiteral:
        case CPP14Lexer.MultiLineMacro:
        case CPP14Lexer.Directive:
          spans.add(Span.obtain(offset, forString()));
          break;
        case CPP14Lexer.BlockComment:
          spans.add(Span.obtain(offset, forComment()));
          break;
        case CPP14Lexer.LineComment:
          handleLineCommentSpan(token, spans, offset);
          break;
        default:
          spans.add(Span.obtain(offset, makeStyle(TEXT_NORMAL)));
          break;
      }
    }
    return spans;
  }

  @Override
  protected void handleIncompleteToken(final IncrementalToken token) {
    token.type = CPP14Lexer.BlockComment;
  }

  @Override
  protected int[] getCodeBlockTokens() {
    return new int[]{CPP14Lexer.LeftBrace, CPP14Lexer.RightBrace};
  }
}
