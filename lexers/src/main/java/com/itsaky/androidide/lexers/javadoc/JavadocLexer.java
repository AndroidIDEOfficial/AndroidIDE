// Generated from JavadocLexer.g4 by ANTLR 4.9.3
package com.itsaky.androidide.lexers.javadoc;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class JavadocLexer extends Lexer {
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
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  public static final String _serializedATN =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\16x\b\1\4\2\t\2\4"
          + "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
          + "\13\4\f\t\f\4\r\t\r\3\2\6\2\35\n\2\r\2\16\2\36\3\3\3\3\5\3#\n\3\3\3\3"
          + "\3\3\3\6\3(\n\3\r\3\16\3)\5\3,\n\3\3\3\3\3\3\3\3\3\5\3\62\n\3\3\3\3\3"
          + "\3\3\6\3\67\n\3\r\3\16\38\5\3;\n\3\3\3\3\3\5\3?\n\3\3\3\3\3\3\3\6\3D\n"
          + "\3\r\3\16\3E\5\3H\n\3\5\3J\n\3\3\4\6\4M\n\4\r\4\16\4N\3\5\6\5R\n\5\r\5"
          + "\16\5S\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\3\t\7\ta\n\t\f\t\16\td"
          + "\13\t\3\n\5\ng\n\n\3\n\7\nj\n\n\f\n\16\nm\13\n\3\n\3\n\3\n\3\13\3\13\3"
          + "\13\3\f\3\f\3\r\3\r\2\2\16\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"
          + "\f\27\r\31\16\3\2\5\4\2C\\c|\4\2\13\13\"\"\n\2\13\f\17\17\"\",,\61\61"
          + "B\\c}\177\177\2\u0088\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2"
          + "\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25"
          + "\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\34\3\2\2\2\5I\3\2\2\2\7L\3\2\2\2"
          + "\tQ\3\2\2\2\13U\3\2\2\2\rW\3\2\2\2\17Y\3\2\2\2\21[\3\2\2\2\23f\3\2\2\2"
          + "\25q\3\2\2\2\27t\3\2\2\2\31v\3\2\2\2\33\35\t\2\2\2\34\33\3\2\2\2\35\36"
          + "\3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37\4\3\2\2\2 +\7\f\2\2!#\5\7\4\2"
          + "\"!\3\2\2\2\"#\3\2\2\2#\'\3\2\2\2$%\5\r\7\2%&\6\3\2\2&(\3\2\2\2\'$\3\2"
          + "\2\2()\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*,\3\2\2\2+\"\3\2\2\2+,\3\2\2\2,J\3"
          + "\2\2\2-.\7\17\2\2./\7\f\2\2/:\3\2\2\2\60\62\5\7\4\2\61\60\3\2\2\2\61\62"
          + "\3\2\2\2\62\66\3\2\2\2\63\64\5\r\7\2\64\65\6\3\3\2\65\67\3\2\2\2\66\63"
          + "\3\2\2\2\678\3\2\2\28\66\3\2\2\289\3\2\2\29;\3\2\2\2:\61\3\2\2\2:;\3\2"
          + "\2\2;J\3\2\2\2<G\7\17\2\2=?\5\7\4\2>=\3\2\2\2>?\3\2\2\2?C\3\2\2\2@A\5"
          + "\r\7\2AB\6\3\4\2BD\3\2\2\2C@\3\2\2\2DE\3\2\2\2EC\3\2\2\2EF\3\2\2\2FH\3"
          + "\2\2\2G>\3\2\2\2GH\3\2\2\2HJ\3\2\2\2I \3\2\2\2I-\3\2\2\2I<\3\2\2\2J\6"
          + "\3\2\2\2KM\t\3\2\2LK\3\2\2\2MN\3\2\2\2NL\3\2\2\2NO\3\2\2\2O\b\3\2\2\2"
          + "PR\n\4\2\2QP\3\2\2\2RS\3\2\2\2SQ\3\2\2\2ST\3\2\2\2T\n\3\2\2\2UV\7B\2\2"
          + "V\f\3\2\2\2WX\7,\2\2X\16\3\2\2\2YZ\7\61\2\2Z\20\3\2\2\2[\\\7\61\2\2\\"
          + "]\7,\2\2]^\7,\2\2^b\3\2\2\2_a\5\r\7\2`_\3\2\2\2ad\3\2\2\2b`\3\2\2\2bc"
          + "\3\2\2\2c\22\3\2\2\2db\3\2\2\2eg\5\7\4\2fe\3\2\2\2fg\3\2\2\2gk\3\2\2\2"
          + "hj\5\r\7\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2ln\3\2\2\2mk\3\2\2\2"
          + "no\7,\2\2op\7\61\2\2p\24\3\2\2\2qr\7}\2\2rs\7B\2\2s\26\3\2\2\2tu\7}\2"
          + "\2u\30\3\2\2\2vw\7\177\2\2w\32\3\2\2\2\23\2\36\")+\618:>EGINSbfk\2";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  private static final String[] _LITERAL_NAMES = makeLiteralNames();
  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
  public static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};
  public static String[] modeNames = {"DEFAULT_MODE"};

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

  public JavadocLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  private static String[] makeRuleNames() {
    return new String[] {
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
    return "JavadocLexer.g4";
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  @Override
  public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
    switch (ruleIndex) {
      case 1:
        return NEWLINE_sempred((RuleContext) _localctx, predIndex);
    }
    return true;
  }

  private boolean NEWLINE_sempred(RuleContext _localctx, int predIndex) {
    switch (predIndex) {
      case 0:
        return _input.LA(1) != '/';
      case 1:
        return _input.LA(1) != '/';
      case 2:
        return _input.LA(1) != '/';
    }
    return true;
  }

  @Override
  public String[] getChannelNames() {
    return channelNames;
  }

  @Override
  public String[] getModeNames() {
    return modeNames;
  }

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }
}
