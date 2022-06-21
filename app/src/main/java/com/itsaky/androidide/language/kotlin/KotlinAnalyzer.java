package com.itsaky.androidide.language.kotlin;

import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.ANNOTATION;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.LITERAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.OPERATOR;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.TEXT_NORMAL;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forComment;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forKeyword;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.forString;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.get;
import static com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE.withoutCompletion;

import com.itsaky.androidide.lexers.kotlin.KotlinLexer;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.CharSequenceReader;
import com.itsaky.androidide.utils.ILogger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.Token;

import java.io.IOException;

import io.github.rosemoe.sora.lang.analysis.SimpleAnalyzeManager;
import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Styles;

public class KotlinAnalyzer extends SimpleAnalyzeManager<Void> {

  private static final ILogger LOG = ILogger.newInstance("KotlinAnalyzer");

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

    final var lexer = new KotlinLexer(stream);
    // final var parser = new KotlinParser(new CommonTokenStream(lexer));
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

      if (type == KotlinLexer.EOF) {
        lastLine = line;
        break;
      }

      switch (type) {
        case KotlinLexer.WS:
          if (isFirst) {
            colors.addNormalIfNull();
          }
          break;
          // annotations
        case KotlinLexer.AT_NO_WS:
        case KotlinLexer.AT_POST_WS:
        case KotlinLexer.AT_PRE_WS:
        case KotlinLexer.AT_BOTH_WS:
          colors.addIfNeeded(line, column, get(ANNOTATION));
          break;
          // lexicalModifiers
        case KotlinLexer.ANNOTATION:
        case KotlinLexer.ABSTRACT:
        case KotlinLexer.BY:
        case KotlinLexer.CATCH:
        case KotlinLexer.COMPANION:
        case KotlinLexer.CONSTRUCTOR:
        case KotlinLexer.CROSSINLINE:
        case KotlinLexer.DATA:
        case KotlinLexer.DYNAMIC:
        case KotlinLexer.ENUM:
        case KotlinLexer.EXTERNAL:
        case KotlinLexer.FINAL:
        case KotlinLexer.FINALLY:
        case KotlinLexer.IMPORT:
        case KotlinLexer.INFIX:
        case KotlinLexer.INIT:
        case KotlinLexer.INLINE:
        case KotlinLexer.INNER:
        case KotlinLexer.INTERNAL:
        case KotlinLexer.LATEINIT:
        case KotlinLexer.NOINLINE:
        case KotlinLexer.OPEN:
        case KotlinLexer.OPERATOR:
        case KotlinLexer.OUT:
        case KotlinLexer.OVERRIDE:
        case KotlinLexer.PRIVATE:
        case KotlinLexer.PROTECTED:
        case KotlinLexer.PUBLIC:
        case KotlinLexer.REIFIED:
        case KotlinLexer.SEALED:
        case KotlinLexer.TAILREC:
        case KotlinLexer.VARARG:
        case KotlinLexer.WHERE:
        case KotlinLexer.GET:
        case KotlinLexer.SET:
        case KotlinLexer.FIELD:
        case KotlinLexer.PROPERTY:
        case KotlinLexer.RECEIVER:
        case KotlinLexer.PARAM:
        case KotlinLexer.SETPARAM:
        case KotlinLexer.DELEGATE:
        case KotlinLexer.FILE:
        case KotlinLexer.EXPECT:
        case KotlinLexer.ACTUAL:
        case KotlinLexer.VALUE:
        case KotlinLexer.CONST:
        case KotlinLexer.SUSPEND:
          // SECTION: keywords
        case KotlinLexer.RETURN_AT:
        case KotlinLexer.CONTINUE_AT:
        case KotlinLexer.BREAK_AT:
        case KotlinLexer.THIS_AT:
        case KotlinLexer.SUPER_AT:
        case KotlinLexer.PACKAGE:
        case KotlinLexer.CLASS:
        case KotlinLexer.INTERFACE:
        case KotlinLexer.FUN:
        case KotlinLexer.OBJECT:
        case KotlinLexer.VAL:
        case KotlinLexer.VAR:
        case KotlinLexer.TYPE_ALIAS:
        case KotlinLexer.THIS:
        case KotlinLexer.SUPER:
        case KotlinLexer.TYPEOF:
        case KotlinLexer.IF:
        case KotlinLexer.ELSE:
        case KotlinLexer.WHEN:
        case KotlinLexer.TRY:
        case KotlinLexer.FOR:
        case KotlinLexer.DO:
        case KotlinLexer.WHILE:
        case KotlinLexer.THROW:
        case KotlinLexer.RETURN:
        case KotlinLexer.CONTINUE:
        case KotlinLexer.BREAK:
        case KotlinLexer.AS:
        case KotlinLexer.IS:
        case KotlinLexer.IN:
        case KotlinLexer.NOT_IS:
        case KotlinLexer.NOT_IN:
          colors.addIfNeeded(line, column, forKeyword());
          break;
          // SECTION: literals
        case KotlinLexer.RealLiteral:
        case KotlinLexer.FloatLiteral:
        case KotlinLexer.DoubleLiteral:
        case KotlinLexer.IntegerLiteral:
        case KotlinLexer.HexLiteral:
        case KotlinLexer.BinLiteral:
        case KotlinLexer.UnsignedLiteral:
        case KotlinLexer.LongLiteral:
        case KotlinLexer.BooleanLiteral:
        case KotlinLexer.NullLiteral:
        case KotlinLexer.CharacterLiteral:
          colors.addIfNeeded(line, column, get(LITERAL));
          break;
          // strings
        case KotlinLexer.QUOTE_OPEN:
        case KotlinLexer.TRIPLE_QUOTE_OPEN:
        case KotlinLexer.QUOTE_CLOSE:
        case KotlinLexer.LineStrRef:
        case KotlinLexer.LineStrText:
        case KotlinLexer.LineStrEscapedChar:
        case KotlinLexer.LineStrExprStart:
        case KotlinLexer.TRIPLE_QUOTE_CLOSE:
        case KotlinLexer.MultiLineStringQuote:
        case KotlinLexer.MultiLineStrRef:
        case KotlinLexer.MultiLineStrText:
        case KotlinLexer.MultiLineStrExprStart:
          colors.addIfNeeded(line, column, forString());
          break;
          // SECTION: separatorsAndOperations
        case KotlinLexer.RESERVED:
        case KotlinLexer.DOT:
        case KotlinLexer.COMMA:
        case KotlinLexer.LPAREN:
        case KotlinLexer.RPAREN:
        case KotlinLexer.LSQUARE:
        case KotlinLexer.RSQUARE:
        case KotlinLexer.LCURL:
        case KotlinLexer.RCURL:
        case KotlinLexer.MULT:
        case KotlinLexer.MOD:
        case KotlinLexer.DIV:
        case KotlinLexer.ADD:
        case KotlinLexer.SUB:
        case KotlinLexer.INCR:
        case KotlinLexer.DECR:
        case KotlinLexer.CONJ:
        case KotlinLexer.DISJ:
        case KotlinLexer.EXCL_WS:
        case KotlinLexer.EXCL_NO_WS:
        case KotlinLexer.COLON:
        case KotlinLexer.SEMICOLON:
        case KotlinLexer.ASSIGNMENT:
        case KotlinLexer.ADD_ASSIGNMENT:
        case KotlinLexer.SUB_ASSIGNMENT:
        case KotlinLexer.MULT_ASSIGNMENT:
        case KotlinLexer.DIV_ASSIGNMENT:
        case KotlinLexer.MOD_ASSIGNMENT:
        case KotlinLexer.ARROW:
        case KotlinLexer.DOUBLE_ARROW:
        case KotlinLexer.RANGE:
        case KotlinLexer.COLONCOLON:
        case KotlinLexer.DOUBLE_SEMICOLON:
        case KotlinLexer.QUEST_WS:
        case KotlinLexer.QUEST_NO_WS:
        case KotlinLexer.LANGLE:
        case KotlinLexer.RANGLE:
        case KotlinLexer.LE:
        case KotlinLexer.GE:
        case KotlinLexer.EXCL_EQ:
        case KotlinLexer.EXCL_EQEQ:
        case KotlinLexer.AS_SAFE:
        case KotlinLexer.EQEQ:
        case KotlinLexer.EQEQEQ:
        case KotlinLexer.SINGLE_QUOTE:
          colors.addIfNeeded(line, column, get(OPERATOR));
          break;
          // comments
        case KotlinLexer.LineComment:
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
        case KotlinLexer.ShebangLine:
        case KotlinLexer.DelimitedComment:
        case KotlinLexer.Inside_Comment:
          colors.addIfNeeded(line, column, forComment());
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
