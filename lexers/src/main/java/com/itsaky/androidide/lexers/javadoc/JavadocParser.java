// Generated from JavadocParser.g4 by ANTLR 4.9.3
package com.itsaky.androidide.lexers.javadoc;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JavadocParser extends Parser {
  public static final int NAME = 1,
      NEWLINE = 2,
      SPACE = 3,
      TEXT_CONTENT = 4,
      AT = 5,
      STAR = 6,
      SLASH = 7,
      JAVADOC_START = 8,
      JAVADOC_END = 9,
      INLINE_TAG_START = 10,
      BRACE_OPEN = 11,
      BRACE_CLOSE = 12;
  public static final int RULE_documentation = 0,
      RULE_documentationContent = 1,
      RULE_skipWhitespace = 2,
      RULE_description = 3,
      RULE_descriptionLine = 4,
      RULE_descriptionLineStart = 5,
      RULE_descriptionLineNoSpaceNoAt = 6,
      RULE_descriptionLineElement = 7,
      RULE_descriptionLineText = 8,
      RULE_descriptionNewline = 9,
      RULE_tagSection = 10,
      RULE_blockTag = 11,
      RULE_blockTagName = 12,
      RULE_blockTagContent = 13,
      RULE_blockTagText = 14,
      RULE_blockTagTextElement = 15,
      RULE_inlineTag = 16,
      RULE_inlineTagName = 17,
      RULE_inlineTagContent = 18,
      RULE_braceExpression = 19,
      RULE_braceContent = 20,
      RULE_braceText = 21;
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  public static final String _serializedATN =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\16\u00f2\4\2\t\2"
          + "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
          + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
          + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\2\7\2\62"
          + "\n\2\f\2\16\2\65\13\2\3\2\3\2\3\2\3\2\3\2\7\2<\n\2\f\2\16\2?\13\2\3\2"
          + "\3\2\3\2\5\2D\n\2\3\3\3\3\7\3H\n\3\f\3\16\3K\13\3\3\3\7\3N\n\3\f\3\16"
          + "\3Q\13\3\3\3\3\3\3\3\6\3V\n\3\r\3\16\3W\3\3\7\3[\n\3\f\3\16\3^\13\3\3"
          + "\3\3\3\5\3b\n\3\3\4\3\4\3\5\3\5\6\5h\n\5\r\5\16\5i\3\5\3\5\7\5n\n\5\f"
          + "\5\16\5q\13\5\3\6\3\6\7\6u\n\6\f\6\16\6x\13\6\3\6\3\6\7\6|\n\6\f\6\16"
          + "\6\177\13\6\5\6\u0081\n\6\3\7\5\7\u0084\n\7\3\7\6\7\u0087\n\7\r\7\16\7"
          + "\u0088\3\7\3\7\3\7\7\7\u008e\n\7\f\7\16\7\u0091\13\7\3\b\3\b\3\t\3\t\5"
          + "\t\u0097\n\t\3\n\3\n\3\n\6\n\u009c\n\n\r\n\16\n\u009d\3\13\3\13\3\f\6"
          + "\f\u00a3\n\f\r\f\16\f\u00a4\3\r\5\r\u00a8\n\r\3\r\3\r\3\r\5\r\u00ad\n"
          + "\r\3\r\7\r\u00b0\n\r\f\r\16\r\u00b3\13\r\3\16\3\16\3\17\3\17\3\17\5\17"
          + "\u00ba\n\17\3\20\6\20\u00bd\n\20\r\20\16\20\u00be\3\21\3\21\3\22\3\22"
          + "\3\22\7\22\u00c6\n\22\f\22\16\22\u00c9\13\22\3\22\5\22\u00cc\n\22\3\22"
          + "\3\22\3\23\3\23\3\24\6\24\u00d3\n\24\r\24\16\24\u00d4\3\25\3\25\7\25\u00d9"
          + "\n\25\f\25\16\25\u00dc\13\25\3\25\3\25\3\26\3\26\3\26\7\26\u00e3\n\26"
          + "\f\26\16\26\u00e6\13\26\3\26\7\26\u00e9\n\26\f\26\16\26\u00ec\13\26\5"
          + "\26\u00ee\n\26\3\27\3\27\3\27\2\2\30\2\4\6\b\n\f\16\20\22\24\26\30\32"
          + "\34\36 \"$&(*,\2\6\3\2\4\5\6\2\3\3\6\6\b\t\r\16\6\2\3\3\5\6\b\t\r\16\4"
          + "\2\3\6\b\t\2\u0101\2C\3\2\2\2\4a\3\2\2\2\6c\3\2\2\2\be\3\2\2\2\n\u0080"
          + "\3\2\2\2\f\u0083\3\2\2\2\16\u0092\3\2\2\2\20\u0096\3\2\2\2\22\u009b\3"
          + "\2\2\2\24\u009f\3\2\2\2\26\u00a2\3\2\2\2\30\u00a7\3\2\2\2\32\u00b4\3\2"
          + "\2\2\34\u00b9\3\2\2\2\36\u00bc\3\2\2\2 \u00c0\3\2\2\2\"\u00c2\3\2\2\2"
          + "$\u00cf\3\2\2\2&\u00d2\3\2\2\2(\u00d6\3\2\2\2*\u00ed\3\2\2\2,\u00ef\3"
          + "\2\2\2.D\7\2\2\3/\63\7\n\2\2\60\62\5\6\4\2\61\60\3\2\2\2\62\65\3\2\2\2"
          + "\63\61\3\2\2\2\63\64\3\2\2\2\64\66\3\2\2\2\65\63\3\2\2\2\66\67\5\4\3\2"
          + "\678\7\13\2\289\7\2\2\39D\3\2\2\2:<\5\6\4\2;:\3\2\2\2<?\3\2\2\2=;\3\2"
          + "\2\2=>\3\2\2\2>@\3\2\2\2?=\3\2\2\2@A\5\4\3\2AB\7\2\2\3BD\3\2\2\2C.\3\2"
          + "\2\2C/\3\2\2\2C=\3\2\2\2D\3\3\2\2\2EI\5\b\5\2FH\5\6\4\2GF\3\2\2\2HK\3"
          + "\2\2\2IG\3\2\2\2IJ\3\2\2\2Jb\3\2\2\2KI\3\2\2\2LN\5\6\4\2ML\3\2\2\2NQ\3"
          + "\2\2\2OM\3\2\2\2OP\3\2\2\2PR\3\2\2\2QO\3\2\2\2Rb\5\26\f\2SU\5\b\5\2TV"
          + "\7\4\2\2UT\3\2\2\2VW\3\2\2\2WU\3\2\2\2WX\3\2\2\2X\\\3\2\2\2Y[\5\6\4\2"
          + "ZY\3\2\2\2[^\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^\\\3\2\2\2_`\5\26"
          + "\f\2`b\3\2\2\2aE\3\2\2\2aO\3\2\2\2aS\3\2\2\2b\5\3\2\2\2cd\t\2\2\2d\7\3"
          + "\2\2\2eo\5\n\6\2fh\5\24\13\2gf\3\2\2\2hi\3\2\2\2ig\3\2\2\2ij\3\2\2\2j"
          + "k\3\2\2\2kl\5\n\6\2ln\3\2\2\2mg\3\2\2\2nq\3\2\2\2om\3\2\2\2op\3\2\2\2"
          + "p\t\3\2\2\2qo\3\2\2\2rv\5\f\7\2su\5\20\t\2ts\3\2\2\2ux\3\2\2\2vt\3\2\2"
          + "\2vw\3\2\2\2w\u0081\3\2\2\2xv\3\2\2\2y}\5\"\22\2z|\5\20\t\2{z\3\2\2\2"
          + "|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\u0080r\3"
          + "\2\2\2\u0080y\3\2\2\2\u0081\13\3\2\2\2\u0082\u0084\7\5\2\2\u0083\u0082"
          + "\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\3\2\2\2\u0085\u0087\5\16\b\2"
          + "\u0086\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089"
          + "\3\2\2\2\u0089\u008f\3\2\2\2\u008a\u008e\5\16\b\2\u008b\u008e\7\5\2\2"
          + "\u008c\u008e\7\7\2\2\u008d\u008a\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008c"
          + "\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090"
          + "\r\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0093\t\3\2\2\u0093\17\3\2\2\2\u0094"
          + "\u0097\5\"\22\2\u0095\u0097\5\22\n\2\u0096\u0094\3\2\2\2\u0096\u0095\3"
          + "\2\2\2\u0097\21\3\2\2\2\u0098\u009c\5\16\b\2\u0099\u009c\7\5\2\2\u009a"
          + "\u009c\7\7\2\2\u009b\u0098\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009a\3\2"
          + "\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e"
          + "\23\3\2\2\2\u009f\u00a0\7\4\2\2\u00a0\25\3\2\2\2\u00a1\u00a3\5\30\r\2"
          + "\u00a2\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a5"
          + "\3\2\2\2\u00a5\27\3\2\2\2\u00a6\u00a8\7\5\2\2\u00a7\u00a6\3\2\2\2\u00a7"
          + "\u00a8\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00aa\7\7\2\2\u00aa\u00ac\5\32"
          + "\16\2\u00ab\u00ad\7\5\2\2\u00ac\u00ab\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad"
          + "\u00b1\3\2\2\2\u00ae\u00b0\5\34\17\2\u00af\u00ae\3\2\2\2\u00b0\u00b3\3"
          + "\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\31\3\2\2\2\u00b3"
          + "\u00b1\3\2\2\2\u00b4\u00b5\7\3\2\2\u00b5\33\3\2\2\2\u00b6\u00ba\5\36\20"
          + "\2\u00b7\u00ba\5\"\22\2\u00b8\u00ba\7\4\2\2\u00b9\u00b6\3\2\2\2\u00b9"
          + "\u00b7\3\2\2\2\u00b9\u00b8\3\2\2\2\u00ba\35\3\2\2\2\u00bb\u00bd\5 \21"
          + "\2\u00bc\u00bb\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00bc\3\2\2\2\u00be\u00bf"
          + "\3\2\2\2\u00bf\37\3\2\2\2\u00c0\u00c1\t\4\2\2\u00c1!\3\2\2\2\u00c2\u00c3"
          + "\7\f\2\2\u00c3\u00c7\5$\23\2\u00c4\u00c6\7\5\2\2\u00c5\u00c4\3\2\2\2\u00c6"
          + "\u00c9\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00cb\3\2"
          + "\2\2\u00c9\u00c7\3\2\2\2\u00ca\u00cc\5&\24\2\u00cb\u00ca\3\2\2\2\u00cb"
          + "\u00cc\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00ce\7\16\2\2\u00ce#\3\2\2\2"
          + "\u00cf\u00d0\7\3\2\2\u00d0%\3\2\2\2\u00d1\u00d3\5*\26\2\u00d2\u00d1\3"
          + "\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5"
          + "\'\3\2\2\2\u00d6\u00da\7\r\2\2\u00d7\u00d9\5*\26\2\u00d8\u00d7\3\2\2\2"
          + "\u00d9\u00dc\3\2\2\2\u00da\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00db\u00dd"
          + "\3\2\2\2\u00dc\u00da\3\2\2\2\u00dd\u00de\7\16\2\2\u00de)\3\2\2\2\u00df"
          + "\u00ee\5(\25\2\u00e0\u00ea\5,\27\2\u00e1\u00e3\7\4\2\2\u00e2\u00e1\3\2"
          + "\2\2\u00e3\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5"
          + "\u00e7\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00e9\5,\27\2\u00e8\u00e4\3\2"
          + "\2\2\u00e9\u00ec\3\2\2\2\u00ea\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb"
          + "\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ed\u00df\3\2\2\2\u00ed\u00e0\3\2"
          + "\2\2\u00ee+\3\2\2\2\u00ef\u00f0\t\5\2\2\u00f0-\3\2\2\2#\63=CIOW\\aiov"
          + "}\u0080\u0083\u0088\u008d\u008f\u0096\u009b\u009d\u00a4\u00a7\u00ac\u00b1"
          + "\u00b9\u00be\u00c7\u00cb\u00d4\u00da\u00e4\u00ea\u00ed";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  private static final String[] _LITERAL_NAMES = makeLiteralNames();
  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  static {
    RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION);
  }

  static {
    tokenNames = new String[_SYMBOLIC_NAMES.length];
    for (int i = 0; i < tokenNames.length; i++) {
      tokenNames[i] = VOCABULARY.getLiteralName(i);
      if (tokenNames[i] == null) {
        tokenNames[i] = VOCABULARY.getSymbolicName(i);
      }

      if (tokenNames[i] == null) {
        tokenNames[i] = "<INVALID>";
      }
    }
  }

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }

  public JavadocParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  private static String[] makeRuleNames() {
    return new String[] {
      "documentation",
      "documentationContent",
      "skipWhitespace",
      "description",
      "descriptionLine",
      "descriptionLineStart",
      "descriptionLineNoSpaceNoAt",
      "descriptionLineElement",
      "descriptionLineText",
      "descriptionNewline",
      "tagSection",
      "blockTag",
      "blockTagName",
      "blockTagContent",
      "blockTagText",
      "blockTagTextElement",
      "inlineTag",
      "inlineTagName",
      "inlineTagContent",
      "braceExpression",
      "braceContent",
      "braceText"
    };
  }

  private static String[] makeLiteralNames() {
    return new String[] {
      null, null, null, null, null, "'@'", "'*'", "'/'", null, null, "'{@'", "'{'", "'}'"
    };
  }

  private static String[] makeSymbolicNames() {
    return new String[] {
      null,
      "NAME",
      "NEWLINE",
      "SPACE",
      "TEXT_CONTENT",
      "AT",
      "STAR",
      "SLASH",
      "JAVADOC_START",
      "JAVADOC_END",
      "INLINE_TAG_START",
      "BRACE_OPEN",
      "BRACE_CLOSE"
    };
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
  }

  @Override
  public String getGrammarFileName() {
    return "JavadocParser.g4";
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public final DocumentationContext documentation() throws RecognitionException {
    DocumentationContext _localctx = new DocumentationContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_documentation);
    try {
      int _alt;
      setState(65);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case EOF:
          enterOuterAlt(_localctx, 1);
          {
            setState(44);
            match(EOF);
          }
          break;
        case JAVADOC_START:
          enterOuterAlt(_localctx, 2);
          {
            setState(45);
            match(JAVADOC_START);
            setState(49);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(46);
                    skipWhitespace();
                  }
                }
              }
              setState(51);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 0, _ctx);
            }
            setState(52);
            documentationContent();
            setState(53);
            match(JAVADOC_END);
            setState(54);
            match(EOF);
          }
          break;
        case NAME:
        case NEWLINE:
        case SPACE:
        case TEXT_CONTENT:
        case AT:
        case STAR:
        case SLASH:
        case INLINE_TAG_START:
        case BRACE_OPEN:
        case BRACE_CLOSE:
          enterOuterAlt(_localctx, 3);
          {
            setState(59);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(56);
                    skipWhitespace();
                  }
                }
              }
              setState(61);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
            }
            setState(62);
            documentationContent();
            setState(63);
            match(EOF);
          }
          break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DocumentationContentContext documentationContent() throws RecognitionException {
    DocumentationContentContext _localctx = new DocumentationContentContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_documentationContent);
    int _la;
    try {
      int _alt;
      setState(95);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(67);
            description();
            setState(71);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NEWLINE || _la == SPACE) {
              {
                {
                  setState(68);
                  skipWhitespace();
                }
              }
              setState(73);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(77);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(74);
                    skipWhitespace();
                  }
                }
              }
              setState(79);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 4, _ctx);
            }
            setState(80);
            tagSection();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(81);
            description();
            setState(83);
            _errHandler.sync(this);
            _alt = 1;
            do {
              switch (_alt) {
                case 1:
                  {
                    {
                      setState(82);
                      match(NEWLINE);
                    }
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
              setState(85);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
            } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
            setState(90);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(87);
                    skipWhitespace();
                  }
                }
              }
              setState(92);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
            }
            setState(93);
            tagSection();
          }
          break;
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final SkipWhitespaceContext skipWhitespace() throws RecognitionException {
    SkipWhitespaceContext _localctx = new SkipWhitespaceContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_skipWhitespace);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(97);
        _la = _input.LA(1);
        if (!(_la == NEWLINE || _la == SPACE)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionContext description() throws RecognitionException {
    DescriptionContext _localctx = new DescriptionContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_description);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(99);
        descriptionLine();
        setState(109);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(101);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                  {
                    {
                      setState(100);
                      descriptionNewline();
                    }
                  }
                  setState(103);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                } while (_la == NEWLINE);
                setState(105);
                descriptionLine();
              }
            }
          }
          setState(111);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionLineContext descriptionLine() throws RecognitionException {
    DescriptionLineContext _localctx = new DescriptionLineContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_descriptionLine);
    try {
      int _alt;
      setState(126);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case NAME:
        case SPACE:
        case TEXT_CONTENT:
        case STAR:
        case SLASH:
        case BRACE_OPEN:
        case BRACE_CLOSE:
          enterOuterAlt(_localctx, 1);
          {
            setState(112);
            descriptionLineStart();
            setState(116);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(113);
                    descriptionLineElement();
                  }
                }
              }
              setState(118);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
            }
          }
          break;
        case INLINE_TAG_START:
          enterOuterAlt(_localctx, 2);
          {
            setState(119);
            inlineTag();
            setState(123);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(120);
                    descriptionLineElement();
                  }
                }
              }
              setState(125);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
            }
          }
          break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionLineStartContext descriptionLineStart() throws RecognitionException {
    DescriptionLineStartContext _localctx = new DescriptionLineStartContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_descriptionLineStart);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(129);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == SPACE) {
          {
            setState(128);
            match(SPACE);
          }
        }

        setState(132);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(131);
                  descriptionLineNoSpaceNoAt();
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(134);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 14, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
        setState(141);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              setState(139);
              _errHandler.sync(this);
              switch (_input.LA(1)) {
                case NAME:
                case TEXT_CONTENT:
                case STAR:
                case SLASH:
                case BRACE_OPEN:
                case BRACE_CLOSE:
                  {
                    setState(136);
                    descriptionLineNoSpaceNoAt();
                  }
                  break;
                case SPACE:
                  {
                    setState(137);
                    match(SPACE);
                  }
                  break;
                case AT:
                  {
                    setState(138);
                    match(AT);
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
            }
          }
          setState(143);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionLineNoSpaceNoAtContext descriptionLineNoSpaceNoAt()
      throws RecognitionException {
    DescriptionLineNoSpaceNoAtContext _localctx =
        new DescriptionLineNoSpaceNoAtContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_descriptionLineNoSpaceNoAt);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(144);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << NAME)
                        | (1L << TEXT_CONTENT)
                        | (1L << STAR)
                        | (1L << SLASH)
                        | (1L << BRACE_OPEN)
                        | (1L << BRACE_CLOSE)))
                != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionLineElementContext descriptionLineElement() throws RecognitionException {
    DescriptionLineElementContext _localctx = new DescriptionLineElementContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_descriptionLineElement);
    try {
      setState(148);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case INLINE_TAG_START:
          enterOuterAlt(_localctx, 1);
          {
            setState(146);
            inlineTag();
          }
          break;
        case NAME:
        case SPACE:
        case TEXT_CONTENT:
        case AT:
        case STAR:
        case SLASH:
        case BRACE_OPEN:
        case BRACE_CLOSE:
          enterOuterAlt(_localctx, 2);
          {
            setState(147);
            descriptionLineText();
          }
          break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionLineTextContext descriptionLineText() throws RecognitionException {
    DescriptionLineTextContext _localctx = new DescriptionLineTextContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_descriptionLineText);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(153);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                setState(153);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                  case NAME:
                  case TEXT_CONTENT:
                  case STAR:
                  case SLASH:
                  case BRACE_OPEN:
                  case BRACE_CLOSE:
                    {
                      setState(150);
                      descriptionLineNoSpaceNoAt();
                    }
                    break;
                  case SPACE:
                    {
                      setState(151);
                      match(SPACE);
                    }
                    break;
                  case AT:
                    {
                      setState(152);
                      match(AT);
                    }
                    break;
                  default:
                    throw new NoViableAltException(this);
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(155);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DescriptionNewlineContext descriptionNewline() throws RecognitionException {
    DescriptionNewlineContext _localctx = new DescriptionNewlineContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_descriptionNewline);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(157);
        match(NEWLINE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final TagSectionContext tagSection() throws RecognitionException {
    TagSectionContext _localctx = new TagSectionContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_tagSection);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(160);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(159);
              blockTag();
            }
          }
          setState(162);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while (_la == SPACE || _la == AT);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BlockTagContext blockTag() throws RecognitionException {
    BlockTagContext _localctx = new BlockTagContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_blockTag);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(165);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == SPACE) {
          {
            setState(164);
            match(SPACE);
          }
        }

        setState(167);
        match(AT);
        setState(168);
        blockTagName();
        setState(170);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
          case 1:
            {
              setState(169);
              match(SPACE);
            }
            break;
        }
        setState(175);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(172);
                blockTagContent();
              }
            }
          }
          setState(177);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 23, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BlockTagNameContext blockTagName() throws RecognitionException {
    BlockTagNameContext _localctx = new BlockTagNameContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_blockTagName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(178);
        match(NAME);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BlockTagContentContext blockTagContent() throws RecognitionException {
    BlockTagContentContext _localctx = new BlockTagContentContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_blockTagContent);
    try {
      setState(183);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case NAME:
        case SPACE:
        case TEXT_CONTENT:
        case STAR:
        case SLASH:
        case BRACE_OPEN:
        case BRACE_CLOSE:
          enterOuterAlt(_localctx, 1);
          {
            setState(180);
            blockTagText();
          }
          break;
        case INLINE_TAG_START:
          enterOuterAlt(_localctx, 2);
          {
            setState(181);
            inlineTag();
          }
          break;
        case NEWLINE:
          enterOuterAlt(_localctx, 3);
          {
            setState(182);
            match(NEWLINE);
          }
          break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BlockTagTextContext blockTagText() throws RecognitionException {
    BlockTagTextContext _localctx = new BlockTagTextContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_blockTagText);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(186);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(185);
                  blockTagTextElement();
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(188);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 25, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BlockTagTextElementContext blockTagTextElement() throws RecognitionException {
    BlockTagTextElementContext _localctx = new BlockTagTextElementContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_blockTagTextElement);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(190);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << NAME)
                        | (1L << SPACE)
                        | (1L << TEXT_CONTENT)
                        | (1L << STAR)
                        | (1L << SLASH)
                        | (1L << BRACE_OPEN)
                        | (1L << BRACE_CLOSE)))
                != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final InlineTagContext inlineTag() throws RecognitionException {
    InlineTagContext _localctx = new InlineTagContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_inlineTag);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(192);
        match(INLINE_TAG_START);
        setState(193);
        inlineTagName();
        setState(197);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 26, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(194);
                match(SPACE);
              }
            }
          }
          setState(199);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 26, _ctx);
        }
        setState(201);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << NAME)
                        | (1L << NEWLINE)
                        | (1L << SPACE)
                        | (1L << TEXT_CONTENT)
                        | (1L << STAR)
                        | (1L << SLASH)
                        | (1L << BRACE_OPEN)))
                != 0)) {
          {
            setState(200);
            inlineTagContent();
          }
        }

        setState(203);
        match(BRACE_CLOSE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final InlineTagNameContext inlineTagName() throws RecognitionException {
    InlineTagNameContext _localctx = new InlineTagNameContext(_ctx, getState());
    enterRule(_localctx, 34, RULE_inlineTagName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(205);
        match(NAME);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final InlineTagContentContext inlineTagContent() throws RecognitionException {
    InlineTagContentContext _localctx = new InlineTagContentContext(_ctx, getState());
    enterRule(_localctx, 36, RULE_inlineTagContent);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(208);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(207);
              braceContent();
            }
          }
          setState(210);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while ((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << NAME)
                        | (1L << NEWLINE)
                        | (1L << SPACE)
                        | (1L << TEXT_CONTENT)
                        | (1L << STAR)
                        | (1L << SLASH)
                        | (1L << BRACE_OPEN)))
                != 0));
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BraceExpressionContext braceExpression() throws RecognitionException {
    BraceExpressionContext _localctx = new BraceExpressionContext(_ctx, getState());
    enterRule(_localctx, 38, RULE_braceExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(212);
        match(BRACE_OPEN);
        setState(216);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << NAME)
                        | (1L << NEWLINE)
                        | (1L << SPACE)
                        | (1L << TEXT_CONTENT)
                        | (1L << STAR)
                        | (1L << SLASH)
                        | (1L << BRACE_OPEN)))
                != 0)) {
          {
            {
              setState(213);
              braceContent();
            }
          }
          setState(218);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(219);
        match(BRACE_CLOSE);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BraceContentContext braceContent() throws RecognitionException {
    BraceContentContext _localctx = new BraceContentContext(_ctx, getState());
    enterRule(_localctx, 40, RULE_braceContent);
    try {
      int _alt;
      setState(235);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case BRACE_OPEN:
          enterOuterAlt(_localctx, 1);
          {
            setState(221);
            braceExpression();
          }
          break;
        case NAME:
        case NEWLINE:
        case SPACE:
        case TEXT_CONTENT:
        case STAR:
        case SLASH:
          enterOuterAlt(_localctx, 2);
          {
            setState(222);
            braceText();
            setState(232);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 31, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(226);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 30, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                      if (_alt == 1) {
                        {
                          {
                            setState(223);
                            match(NEWLINE);
                          }
                        }
                      }
                      setState(228);
                      _errHandler.sync(this);
                      _alt = getInterpreter().adaptivePredict(_input, 30, _ctx);
                    }
                    setState(229);
                    braceText();
                  }
                }
              }
              setState(234);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 31, _ctx);
            }
          }
          break;
        default:
          throw new NoViableAltException(this);
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BraceTextContext braceText() throws RecognitionException {
    BraceTextContext _localctx = new BraceTextContext(_ctx, getState());
    enterRule(_localctx, 42, RULE_braceText);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(237);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << NAME)
                        | (1L << NEWLINE)
                        | (1L << SPACE)
                        | (1L << TEXT_CONTENT)
                        | (1L << STAR)
                        | (1L << SLASH)))
                != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public static class DocumentationContext extends ParserRuleContext {
    public DocumentationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode EOF() {
      return getToken(JavadocParser.EOF, 0);
    }

    public TerminalNode JAVADOC_START() {
      return getToken(JavadocParser.JAVADOC_START, 0);
    }

    public DocumentationContentContext documentationContent() {
      return getRuleContext(DocumentationContentContext.class, 0);
    }

    public TerminalNode JAVADOC_END() {
      return getToken(JavadocParser.JAVADOC_END, 0);
    }

    public List<SkipWhitespaceContext> skipWhitespace() {
      return getRuleContexts(SkipWhitespaceContext.class);
    }

    public SkipWhitespaceContext skipWhitespace(int i) {
      return getRuleContext(SkipWhitespaceContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_documentation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDocumentation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDocumentation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDocumentation(this);
    }
  }

  public static class DocumentationContentContext extends ParserRuleContext {
    public DocumentationContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DescriptionContext description() {
      return getRuleContext(DescriptionContext.class, 0);
    }

    public List<SkipWhitespaceContext> skipWhitespace() {
      return getRuleContexts(SkipWhitespaceContext.class);
    }

    public SkipWhitespaceContext skipWhitespace(int i) {
      return getRuleContext(SkipWhitespaceContext.class, i);
    }

    public TagSectionContext tagSection() {
      return getRuleContext(TagSectionContext.class, 0);
    }

    public List<TerminalNode> NEWLINE() {
      return getTokens(JavadocParser.NEWLINE);
    }

    public TerminalNode NEWLINE(int i) {
      return getToken(JavadocParser.NEWLINE, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_documentationContent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDocumentationContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDocumentationContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDocumentationContent(this);
    }
  }

  public static class SkipWhitespaceContext extends ParserRuleContext {
    public SkipWhitespaceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode SPACE() {
      return getToken(JavadocParser.SPACE, 0);
    }

    public TerminalNode NEWLINE() {
      return getToken(JavadocParser.NEWLINE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_skipWhitespace;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitSkipWhitespace(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterSkipWhitespace(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitSkipWhitespace(this);
    }
  }

  public static class DescriptionContext extends ParserRuleContext {
    public DescriptionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<DescriptionLineContext> descriptionLine() {
      return getRuleContexts(DescriptionLineContext.class);
    }

    public DescriptionLineContext descriptionLine(int i) {
      return getRuleContext(DescriptionLineContext.class, i);
    }

    public List<DescriptionNewlineContext> descriptionNewline() {
      return getRuleContexts(DescriptionNewlineContext.class);
    }

    public DescriptionNewlineContext descriptionNewline(int i) {
      return getRuleContext(DescriptionNewlineContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_description;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescription(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescription(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescription(this);
    }
  }

  public static class DescriptionLineContext extends ParserRuleContext {
    public DescriptionLineContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DescriptionLineStartContext descriptionLineStart() {
      return getRuleContext(DescriptionLineStartContext.class, 0);
    }

    public List<DescriptionLineElementContext> descriptionLineElement() {
      return getRuleContexts(DescriptionLineElementContext.class);
    }

    public DescriptionLineElementContext descriptionLineElement(int i) {
      return getRuleContext(DescriptionLineElementContext.class, i);
    }

    public InlineTagContext inlineTag() {
      return getRuleContext(InlineTagContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_descriptionLine;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescriptionLine(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescriptionLine(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescriptionLine(this);
    }
  }

  public static class DescriptionLineStartContext extends ParserRuleContext {
    public DescriptionLineStartContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TerminalNode> SPACE() {
      return getTokens(JavadocParser.SPACE);
    }

    public TerminalNode SPACE(int i) {
      return getToken(JavadocParser.SPACE, i);
    }

    public List<DescriptionLineNoSpaceNoAtContext> descriptionLineNoSpaceNoAt() {
      return getRuleContexts(DescriptionLineNoSpaceNoAtContext.class);
    }

    public DescriptionLineNoSpaceNoAtContext descriptionLineNoSpaceNoAt(int i) {
      return getRuleContext(DescriptionLineNoSpaceNoAtContext.class, i);
    }

    public List<TerminalNode> AT() {
      return getTokens(JavadocParser.AT);
    }

    public TerminalNode AT(int i) {
      return getToken(JavadocParser.AT, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_descriptionLineStart;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescriptionLineStart(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescriptionLineStart(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescriptionLineStart(this);
    }
  }

  public static class DescriptionLineNoSpaceNoAtContext extends ParserRuleContext {
    public DescriptionLineNoSpaceNoAtContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TEXT_CONTENT() {
      return getToken(JavadocParser.TEXT_CONTENT, 0);
    }

    public TerminalNode NAME() {
      return getToken(JavadocParser.NAME, 0);
    }

    public TerminalNode STAR() {
      return getToken(JavadocParser.STAR, 0);
    }

    public TerminalNode SLASH() {
      return getToken(JavadocParser.SLASH, 0);
    }

    public TerminalNode BRACE_OPEN() {
      return getToken(JavadocParser.BRACE_OPEN, 0);
    }

    public TerminalNode BRACE_CLOSE() {
      return getToken(JavadocParser.BRACE_CLOSE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_descriptionLineNoSpaceNoAt;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescriptionLineNoSpaceNoAt(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescriptionLineNoSpaceNoAt(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescriptionLineNoSpaceNoAt(this);
    }
  }

  public static class DescriptionLineElementContext extends ParserRuleContext {
    public DescriptionLineElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public InlineTagContext inlineTag() {
      return getRuleContext(InlineTagContext.class, 0);
    }

    public DescriptionLineTextContext descriptionLineText() {
      return getRuleContext(DescriptionLineTextContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_descriptionLineElement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescriptionLineElement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescriptionLineElement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescriptionLineElement(this);
    }
  }

  public static class DescriptionLineTextContext extends ParserRuleContext {
    public DescriptionLineTextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<DescriptionLineNoSpaceNoAtContext> descriptionLineNoSpaceNoAt() {
      return getRuleContexts(DescriptionLineNoSpaceNoAtContext.class);
    }

    public DescriptionLineNoSpaceNoAtContext descriptionLineNoSpaceNoAt(int i) {
      return getRuleContext(DescriptionLineNoSpaceNoAtContext.class, i);
    }

    public List<TerminalNode> SPACE() {
      return getTokens(JavadocParser.SPACE);
    }

    public TerminalNode SPACE(int i) {
      return getToken(JavadocParser.SPACE, i);
    }

    public List<TerminalNode> AT() {
      return getTokens(JavadocParser.AT);
    }

    public TerminalNode AT(int i) {
      return getToken(JavadocParser.AT, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_descriptionLineText;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescriptionLineText(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescriptionLineText(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescriptionLineText(this);
    }
  }

  public static class DescriptionNewlineContext extends ParserRuleContext {
    public DescriptionNewlineContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode NEWLINE() {
      return getToken(JavadocParser.NEWLINE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_descriptionNewline;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitDescriptionNewline(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterDescriptionNewline(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitDescriptionNewline(this);
    }
  }

  public static class TagSectionContext extends ParserRuleContext {
    public TagSectionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<BlockTagContext> blockTag() {
      return getRuleContexts(BlockTagContext.class);
    }

    public BlockTagContext blockTag(int i) {
      return getRuleContext(BlockTagContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_tagSection;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitTagSection(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterTagSection(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitTagSection(this);
    }
  }

  public static class BlockTagContext extends ParserRuleContext {
    public BlockTagContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode AT() {
      return getToken(JavadocParser.AT, 0);
    }

    public BlockTagNameContext blockTagName() {
      return getRuleContext(BlockTagNameContext.class, 0);
    }

    public List<TerminalNode> SPACE() {
      return getTokens(JavadocParser.SPACE);
    }

    public TerminalNode SPACE(int i) {
      return getToken(JavadocParser.SPACE, i);
    }

    public List<BlockTagContentContext> blockTagContent() {
      return getRuleContexts(BlockTagContentContext.class);
    }

    public BlockTagContentContext blockTagContent(int i) {
      return getRuleContext(BlockTagContentContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockTag;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBlockTag(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBlockTag(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBlockTag(this);
    }
  }

  public static class BlockTagNameContext extends ParserRuleContext {
    public BlockTagNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode NAME() {
      return getToken(JavadocParser.NAME, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockTagName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBlockTagName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBlockTagName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBlockTagName(this);
    }
  }

  public static class BlockTagContentContext extends ParserRuleContext {
    public BlockTagContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BlockTagTextContext blockTagText() {
      return getRuleContext(BlockTagTextContext.class, 0);
    }

    public InlineTagContext inlineTag() {
      return getRuleContext(InlineTagContext.class, 0);
    }

    public TerminalNode NEWLINE() {
      return getToken(JavadocParser.NEWLINE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockTagContent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBlockTagContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBlockTagContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBlockTagContent(this);
    }
  }

  public static class BlockTagTextContext extends ParserRuleContext {
    public BlockTagTextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<BlockTagTextElementContext> blockTagTextElement() {
      return getRuleContexts(BlockTagTextElementContext.class);
    }

    public BlockTagTextElementContext blockTagTextElement(int i) {
      return getRuleContext(BlockTagTextElementContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockTagText;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBlockTagText(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBlockTagText(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBlockTagText(this);
    }
  }

  public static class BlockTagTextElementContext extends ParserRuleContext {
    public BlockTagTextElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TEXT_CONTENT() {
      return getToken(JavadocParser.TEXT_CONTENT, 0);
    }

    public TerminalNode NAME() {
      return getToken(JavadocParser.NAME, 0);
    }

    public TerminalNode SPACE() {
      return getToken(JavadocParser.SPACE, 0);
    }

    public TerminalNode STAR() {
      return getToken(JavadocParser.STAR, 0);
    }

    public TerminalNode SLASH() {
      return getToken(JavadocParser.SLASH, 0);
    }

    public TerminalNode BRACE_OPEN() {
      return getToken(JavadocParser.BRACE_OPEN, 0);
    }

    public TerminalNode BRACE_CLOSE() {
      return getToken(JavadocParser.BRACE_CLOSE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockTagTextElement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBlockTagTextElement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBlockTagTextElement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBlockTagTextElement(this);
    }
  }

  public static class InlineTagContext extends ParserRuleContext {
    public InlineTagContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode INLINE_TAG_START() {
      return getToken(JavadocParser.INLINE_TAG_START, 0);
    }

    public InlineTagNameContext inlineTagName() {
      return getRuleContext(InlineTagNameContext.class, 0);
    }

    public TerminalNode BRACE_CLOSE() {
      return getToken(JavadocParser.BRACE_CLOSE, 0);
    }

    public List<TerminalNode> SPACE() {
      return getTokens(JavadocParser.SPACE);
    }

    public TerminalNode SPACE(int i) {
      return getToken(JavadocParser.SPACE, i);
    }

    public InlineTagContentContext inlineTagContent() {
      return getRuleContext(InlineTagContentContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inlineTag;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitInlineTag(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterInlineTag(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitInlineTag(this);
    }
  }

  public static class InlineTagNameContext extends ParserRuleContext {
    public InlineTagNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode NAME() {
      return getToken(JavadocParser.NAME, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inlineTagName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitInlineTagName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterInlineTagName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitInlineTagName(this);
    }
  }

  public static class InlineTagContentContext extends ParserRuleContext {
    public InlineTagContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<BraceContentContext> braceContent() {
      return getRuleContexts(BraceContentContext.class);
    }

    public BraceContentContext braceContent(int i) {
      return getRuleContext(BraceContentContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inlineTagContent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitInlineTagContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterInlineTagContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitInlineTagContent(this);
    }
  }

  public static class BraceExpressionContext extends ParserRuleContext {
    public BraceExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode BRACE_OPEN() {
      return getToken(JavadocParser.BRACE_OPEN, 0);
    }

    public TerminalNode BRACE_CLOSE() {
      return getToken(JavadocParser.BRACE_CLOSE, 0);
    }

    public List<BraceContentContext> braceContent() {
      return getRuleContexts(BraceContentContext.class);
    }

    public BraceContentContext braceContent(int i) {
      return getRuleContext(BraceContentContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_braceExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBraceExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBraceExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBraceExpression(this);
    }
  }

  public static class BraceContentContext extends ParserRuleContext {
    public BraceContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BraceExpressionContext braceExpression() {
      return getRuleContext(BraceExpressionContext.class, 0);
    }

    public List<BraceTextContext> braceText() {
      return getRuleContexts(BraceTextContext.class);
    }

    public BraceTextContext braceText(int i) {
      return getRuleContext(BraceTextContext.class, i);
    }

    public List<TerminalNode> NEWLINE() {
      return getTokens(JavadocParser.NEWLINE);
    }

    public TerminalNode NEWLINE(int i) {
      return getToken(JavadocParser.NEWLINE, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_braceContent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBraceContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBraceContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBraceContent(this);
    }
  }

  public static class BraceTextContext extends ParserRuleContext {
    public BraceTextContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TEXT_CONTENT() {
      return getToken(JavadocParser.TEXT_CONTENT, 0);
    }

    public TerminalNode NAME() {
      return getToken(JavadocParser.NAME, 0);
    }

    public TerminalNode SPACE() {
      return getToken(JavadocParser.SPACE, 0);
    }

    public TerminalNode STAR() {
      return getToken(JavadocParser.STAR, 0);
    }

    public TerminalNode SLASH() {
      return getToken(JavadocParser.SLASH, 0);
    }

    public TerminalNode NEWLINE() {
      return getToken(JavadocParser.NEWLINE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_braceText;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof JavadocParserVisitor)
        return ((JavadocParserVisitor<? extends T>) visitor).visitBraceText(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).enterBraceText(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof JavadocParserListener)
        ((JavadocParserListener) listener).exitBraceText(this);
    }
  }
}
