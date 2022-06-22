package com.itsaky.androidide.language.cpp;

import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Class;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Double;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Enum;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Float;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Long;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Override;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Short;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.Void;
import static com.itsaky.androidide.lexers.cpp.CPP14Lexer.*;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TYPE_NAME;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString;
import static io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle;

import com.itsaky.androidide.language.incremental.BaseIncrementalAnalyzeManager;
import com.itsaky.androidide.language.incremental.IncrementalToken;
import com.itsaky.androidide.language.incremental.LineState;
import com.itsaky.androidide.lexers.cpp.CPP14Lexer;
import com.itsaky.androidide.utils.ILogger;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rosemoe.sora.lang.analysis.AsyncIncrementalAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.text.Content;

public class CppAnalyzer extends BaseIncrementalAnalyzeManager {

  private static final ILogger LOG = ILogger.newInstance("CppAnalyzer");

  public CppAnalyzer() {
    super(CPP14Lexer.class);
  }

  @Override
  public List<CodeBlock> computeBlocks(
      final Content text,
      final AsyncIncrementalAnalyzeManager<LineState, IncrementalToken>.CodeBlockAnalyzeDelegate
          delegate) {
    return Collections.emptyList();
  }

  @Override
  protected int[] getBraceTypes() {
    return new int[] {LeftBrace, RightBrace};
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
        case Alignas:
        case Alignof:
        case Asm:
        case Auto:
        case Break:
        case Class:
        case Const:
        case Catch:
        case Case:
        case Constexpr:
        case Const_cast:
        case Continue:
        case Decltype:
        case Default:
        case Delete:
        case Do:
        case Dynamic_cast:
        case Else:
        case Enum:
        case Explicit:
        case Export:
        case Extern:
        case Final:
        case For:
        case Friend:
        case Goto:
        case If:
        case Inline:
        case Mutable:
        case Namespace:
        case New:
        case Noexcept:
        case Operator:
        case Override:
        case Private:
        case Protected:
        case Public:
        case Register:
        case Reinterpret_cast:
        case Return:
        case Sizeof:
        case Signed:
        case Static:
        case Static_cast:
        case Static_assert:
        case Struct:
        case Switch:
        case Template:
        case This:
        case Throw:
        case Thread_local:
        case Try:
        case Typedef:
        case Typeid_:
        case Typename_:
        case Unsigned:
        case Union:
        case Using:
        case Virtual:
        case Volatile:
        case While:
        case Void:
          spans.add(Span.obtain(offset, forKeyword()));
          break;
        case Bool:
        case Char:
        case Char16:
        case Char32:
        case Double:
        case Float:
        case Int:
        case Long:
        case Short:
        case Wchar:
          spans.add(Span.obtain(offset, makeStyle(TYPE_NAME)));
          break;
        case LeftParen:
        case LeftBracket:
        case LeftBrace:
        case Dot:
        case RightParen:
        case RightBracket:
        case RightBrace:
        case Semi:
        case Comma:
        case Plus:
        case Minus:
        case Star:
        case Div:
        case Mod:
        case Caret:
        case And:
        case Or:
        case Tilde:
        case Not:
        case Assign:
        case Less:
        case Greater:
        case PlusAssign:
        case MinusAssign:
        case StarAssign:
        case DivAssign:
        case ModAssign:
        case XorAssign:
        case AndAssign:
        case OrAssign:
        case RightShiftAssign:
        case LeftShiftAssign:
        case Equal:
        case NotEqual:
        case LessEqual:
        case GreaterEqual:
        case AndAnd:
        case OrOr:
        case PlusPlus:
        case MinusMinus:
        case Arrow:
        case ArrowStar:
        case Question:
        case Colon:
        case Doublecolon:
        case DotStar:
        case Ellipsis:
          spans.add(Span.obtain(offset, makeStyle(OPERATOR)));
          break;
        case IntegerLiteral:
        case DecimalLiteral:
        case OctalLiteral:
        case HexadecimalLiteral:
        case BinaryLiteral:
        case Integersuffix:
        case CharacterLiteral:
        case FloatingLiteral:
        case BooleanLiteral:
        case Nullptr:
        case True_:
        case False_:
          spans.add(Span.obtain(offset, makeStyle(LITERAL)));
          break;
        case StringLiteral:
        case MultiLineMacro:
        case Directive:
          spans.add(Span.obtain(offset, forString()));
          break;
        case BlockComment:
          spans.add(Span.obtain(offset, forComment()));
          break;
        case LineComment:
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
  protected int[][] getMultilineTokenStartEndTypes() {
    final var start = new int[] {Div, Star};
    final var end = new int[] {Star, Div};
    return new int[][] {start, end};
  }

  @Override
  protected void handleIncompleteToken(final IncrementalToken token) {
    token.type = BlockComment;
  }
}
