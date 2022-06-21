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
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.get;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.withoutCompletion;

import com.itsaky.androidide.lexers.cpp.CPP14Lexer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;

import java.io.IOException;
import java.lang.Override;
import java.lang.Void;

import io.github.rosemoe.sora.lang.analysis.SimpleAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Styles;

public class CppAnalyzer extends SimpleAnalyzeManager<Void> {

  private static final ILogger LOG = ILogger.newInstance("CppAnalyzer");

  @Override
  protected Styles analyze(StringBuilder text, Delegate<Void> delegate) {
    final var styles = new Styles();
    final var colors = new MappedSpans.Builder();
    final CodePointCharStream stream;
    try {
      stream = CharStreams.fromReader(new CharSequenceReader(text));
    } catch (IOException e) {
      LOG.error("Unable to read text to analyze", e);
      return styles;
    }

    final var lexer = new CPP14Lexer(stream);
    // final var parser = new CPP14Parser(new CommonTokenStream(lexer));
    Token token;
    int line, column, lastLine = 1, type;
    boolean isFirst = true;

    while (!delegate.isCancelled()) {
      token = lexer.nextToken();
      if (token == null) {
        break;
      }

      line = token.getLine() - 1;
      column = token.getCharPositionInLine();
      type = token.getType();

      if (type == CPP14Lexer.EOF) {
        lastLine = line;
        break;
      }

      switch (type) {
        case Whitespace:
          if (isFirst) {
            colors.addNormalIfNull();
          }
          break;
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
          colors.addIfNeeded(line, column, forKeyword());
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
          colors.addIfNeeded(line, column, get(TYPE_NAME));
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
          colors.addIfNeeded(line, column, get(OPERATOR));
          break;
        case IntegerLiteral:
        case DecimalLiteral:
        case OctalLiteral:
        case HexadecimalLiteral:
        case BinaryLiteral:
        case Integersuffix:
        case CharacterLiteral:
        case FloatingLiteral:
        case StringLiteral:
        case BooleanLiteral:
        case Nullptr:
        case True_:
        case False_:
          colors.addIfNeeded(line, column, get(LITERAL));
          break;
        case BlockComment:
          colors.addIfNeeded(line, column, forComment());
          break;
        case LineComment:
          var commentType = SchemeAndroidIDE.COMMENT;

          // highlight special line comments
          var commentText = token.getText();
          if (commentText.length() > 2) {
            commentText = commentText.substring(2);
            commentText = commentText.trim();

            if ("todo".equalsIgnoreCase(commentText.substring(0, 4))) {
              commentType = SchemeAndroidIDE.TODO_COMMENT;
            } else if ("fixme".equalsIgnoreCase(commentText.substring(0, 5))) {
              commentType = SchemeAndroidIDE.FIXME_COMMENT;
            }
          }

          colors.addIfNeeded(line, column, withoutCompletion(commentType));
          break;
        case MultiLineMacro:
        case Directive:
          colors.addIfNeeded(line, column, forString());
          break;

        default:
          colors.addIfNeeded(line, column, get(TEXT_NORMAL));
          break;
      }

      isFirst = false;
    }

    colors.determine(lastLine);

    styles.spans = colors.build();

    return styles;
  }
}
