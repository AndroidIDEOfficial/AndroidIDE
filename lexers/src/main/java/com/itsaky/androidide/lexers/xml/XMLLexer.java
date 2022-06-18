// Generated from
// D:/Projects/AndroidStudioProjects/AndroidIDE/lexers/src/main/java/com/itsaky/androidide/lexers/xml\XMLLexer.g4 by ANTLR 4.9.2
package com.itsaky.androidide.lexers.xml;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XMLLexer extends Lexer {
  static {
    RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
  }

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  public static final int COLON = 1,
      COMMENT = 2,
      CDATA = 3,
      DTD = 4,
      EntityRef = 5,
      CharRef = 6,
      SEA_WS = 7,
      OPEN = 8,
      OPEN_SLASH = 9,
      XMLDeclOpen = 10,
      TEXT = 11,
      CLOSE = 12,
      SPECIAL_CLOSE = 13,
      SLASH_CLOSE = 14,
      SLASH = 15,
      EQUALS = 16,
      STRING = 17,
      Name = 18,
      S = 19,
      PI = 20;
  public static final int INSIDE = 1, PROC_INSTR = 2;
  public static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};

  public static String[] modeNames = {"DEFAULT_MODE", "INSIDE", "PROC_INSTR"};

  private static String[] makeRuleNames() {
    return new String[] {
      "COLON",
      "COMMENT",
      "CDATA",
      "DTD",
      "EntityRef",
      "CharRef",
      "SEA_WS",
      "OPEN",
      "OPEN_SLASH",
      "XMLDeclOpen",
      "SPECIAL_OPEN",
      "TEXT",
      "CLOSE",
      "SPECIAL_CLOSE",
      "SLASH_CLOSE",
      "SLASH",
      "EQUALS",
      "STRING",
      "Name",
      "S",
      "HEXDIGIT",
      "DIGIT",
      "NameChar",
      "NameStartChar",
      "PI",
      "IGNORE"
    };
  }

  public static final String[] ruleNames = makeRuleNames();

  private static String[] makeLiteralNames() {
    return new String[] {
      null, "':'", null, null, null, null, null, null, "'<'", "'</'", null, null, "'>'", null,
      "'/>'", "'/'", "'='"
    };
  }

  private static final String[] _LITERAL_NAMES = makeLiteralNames();

  private static String[] makeSymbolicNames() {
    return new String[] {
      null,
      "COLON",
      "COMMENT",
      "CDATA",
      "DTD",
      "EntityRef",
      "CharRef",
      "SEA_WS",
      "OPEN",
      "OPEN_SLASH",
      "XMLDeclOpen",
      "TEXT",
      "CLOSE",
      "SPECIAL_CLOSE",
      "SLASH_CLOSE",
      "SLASH",
      "EQUALS",
      "STRING",
      "Name",
      "S",
      "PI"
    };
  }

  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

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

  @Override
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override
  public Vocabulary getVocabulary() {
    return VOCABULARY;
  }

  public XMLLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  @Override
  public String getGrammarFileName() {
    return "XMLLexer.g4";
  }

  @Override
  public String[] getRuleNames() {
    return ruleNames;
  }

  @Override
  public String getSerializedATN() {
    return _serializedATN;
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
  public ATN getATN() {
    return _ATN;
  }

  public static final String _serializedATN =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\26\u00f4\b\1\b\1"
          + "\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4"
          + "\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t"
          + "\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t"
          + "\30\4\31\t\31\4\32\t\32\4\33\t\33\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\7\3"
          + "B\n\3\f\3\16\3E\13\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"
          + "\4\3\4\3\4\7\4V\n\4\f\4\16\4Y\13\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\7\5"
          + "c\n\5\f\5\16\5f\13\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\6"
          + "\7t\n\7\r\7\16\7u\3\7\3\7\3\7\3\7\3\7\3\7\3\7\6\7\177\n\7\r\7\16\7\u0080"
          + "\3\7\3\7\5\7\u0085\n\7\3\b\3\b\5\b\u0089\n\b\3\b\6\b\u008c\n\b\r\b\16"
          + "\b\u008d\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13"
          + "\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\6\r\u00ac"
          + "\n\r\r\r\16\r\u00ad\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20"
          + "\3\20\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\7\23\u00c4\n\23\f\23"
          + "\16\23\u00c7\13\23\3\23\3\23\3\23\7\23\u00cc\n\23\f\23\16\23\u00cf\13"
          + "\23\3\23\5\23\u00d2\n\23\3\24\3\24\7\24\u00d6\n\24\f\24\16\24\u00d9\13"
          + "\24\3\25\3\25\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30\3\30\5\30\u00e7"
          + "\n\30\3\31\5\31\u00ea\n\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33"
          + "\5CWd\2\34\5\3\7\4\t\5\13\6\r\7\17\b\21\t\23\n\25\13\27\f\31\2\33\r\35"
          + "\16\37\17!\20#\21%\22\'\23)\24+\25-\2/\2\61\2\63\2\65\26\67\2\5\2\3\4"
          + "\f\4\2\13\13\"\"\4\2((>>\4\2$$>>\4\2))>>\5\2\13\f\17\17\"\"\5\2\62;CH"
          + "ch\3\2\62;\4\2/\60aa\5\2\u00b9\u00b9\u0302\u0371\u2041\u2042\n\2<<C\\"
          + "c|\u2072\u2191\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2\uffff\2\u00fe"
          + "\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"
          + "\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"
          + "\2\33\3\2\2\2\3\35\3\2\2\2\3\37\3\2\2\2\3!\3\2\2\2\3#\3\2\2\2\3%\3\2\2"
          + "\2\3\'\3\2\2\2\3)\3\2\2\2\3+\3\2\2\2\4\65\3\2\2\2\4\67\3\2\2\2\59\3\2"
          + "\2\2\7;\3\2\2\2\tJ\3\2\2\2\13^\3\2\2\2\rk\3\2\2\2\17\u0084\3\2\2\2\21"
          + "\u008b\3\2\2\2\23\u008f\3\2\2\2\25\u0093\3\2\2\2\27\u0098\3\2\2\2\31\u00a2"
          + "\3\2\2\2\33\u00ab\3\2\2\2\35\u00af\3\2\2\2\37\u00b3\3\2\2\2!\u00b8\3\2"
          + "\2\2#\u00bd\3\2\2\2%\u00bf\3\2\2\2\'\u00d1\3\2\2\2)\u00d3\3\2\2\2+\u00da"
          + "\3\2\2\2-\u00de\3\2\2\2/\u00e0\3\2\2\2\61\u00e6\3\2\2\2\63\u00e9\3\2\2"
          + "\2\65\u00eb\3\2\2\2\67\u00f0\3\2\2\29:\7<\2\2:\6\3\2\2\2;<\7>\2\2<=\7"
          + "#\2\2=>\7/\2\2>?\7/\2\2?C\3\2\2\2@B\13\2\2\2A@\3\2\2\2BE\3\2\2\2CD\3\2"
          + "\2\2CA\3\2\2\2DF\3\2\2\2EC\3\2\2\2FG\7/\2\2GH\7/\2\2HI\7@\2\2I\b\3\2\2"
          + "\2JK\7>\2\2KL\7#\2\2LM\7]\2\2MN\7E\2\2NO\7F\2\2OP\7C\2\2PQ\7V\2\2QR\7"
          + "C\2\2RS\7]\2\2SW\3\2\2\2TV\13\2\2\2UT\3\2\2\2VY\3\2\2\2WX\3\2\2\2WU\3"
          + "\2\2\2XZ\3\2\2\2YW\3\2\2\2Z[\7_\2\2[\\\7_\2\2\\]\7@\2\2]\n\3\2\2\2^_\7"
          + ">\2\2_`\7#\2\2`d\3\2\2\2ac\13\2\2\2ba\3\2\2\2cf\3\2\2\2de\3\2\2\2db\3"
          + "\2\2\2eg\3\2\2\2fd\3\2\2\2gh\7@\2\2hi\3\2\2\2ij\b\5\2\2j\f\3\2\2\2kl\7"
          + "(\2\2lm\5)\24\2mn\7=\2\2n\16\3\2\2\2op\7(\2\2pq\7%\2\2qs\3\2\2\2rt\5/"
          + "\27\2sr\3\2\2\2tu\3\2\2\2us\3\2\2\2uv\3\2\2\2vw\3\2\2\2wx\7=\2\2x\u0085"
          + "\3\2\2\2yz\7(\2\2z{\7%\2\2{|\7z\2\2|~\3\2\2\2}\177\5-\26\2~}\3\2\2\2\177"
          + "\u0080\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0082\3\2\2\2"
          + "\u0082\u0083\7=\2\2\u0083\u0085\3\2\2\2\u0084o\3\2\2\2\u0084y\3\2\2\2"
          + "\u0085\20\3\2\2\2\u0086\u008c\t\2\2\2\u0087\u0089\7\17\2\2\u0088\u0087"
          + "\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008c\7\f\2\2\u008b"
          + "\u0086\3\2\2\2\u008b\u0088\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008b\3\2"
          + "\2\2\u008d\u008e\3\2\2\2\u008e\22\3\2\2\2\u008f\u0090\7>\2\2\u0090\u0091"
          + "\3\2\2\2\u0091\u0092\b\t\3\2\u0092\24\3\2\2\2\u0093\u0094\7>\2\2\u0094"
          + "\u0095\7\61\2\2\u0095\u0096\3\2\2\2\u0096\u0097\b\n\3\2\u0097\26\3\2\2"
          + "\2\u0098\u0099\7>\2\2\u0099\u009a\7A\2\2\u009a\u009b\7z\2\2\u009b\u009c"
          + "\7o\2\2\u009c\u009d\7n\2\2\u009d\u009e\3\2\2\2\u009e\u009f\5+\25\2\u009f"
          + "\u00a0\3\2\2\2\u00a0\u00a1\b\13\3\2\u00a1\30\3\2\2\2\u00a2\u00a3\7>\2"
          + "\2\u00a3\u00a4\7A\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a6\5)\24\2\u00a6\u00a7"
          + "\3\2\2\2\u00a7\u00a8\b\f\4\2\u00a8\u00a9\b\f\5\2\u00a9\32\3\2\2\2\u00aa"
          + "\u00ac\n\3\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ab\3\2"
          + "\2\2\u00ad\u00ae\3\2\2\2\u00ae\34\3\2\2\2\u00af\u00b0\7@\2\2\u00b0\u00b1"
          + "\3\2\2\2\u00b1\u00b2\b\16\6\2\u00b2\36\3\2\2\2\u00b3\u00b4\7A\2\2\u00b4"
          + "\u00b5\7@\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b7\b\17\6\2\u00b7 \3\2\2\2"
          + "\u00b8\u00b9\7\61\2\2\u00b9\u00ba\7@\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc"
          + "\b\20\6\2\u00bc\"\3\2\2\2\u00bd\u00be\7\61\2\2\u00be$\3\2\2\2\u00bf\u00c0"
          + "\7?\2\2\u00c0&\3\2\2\2\u00c1\u00c5\7$\2\2\u00c2\u00c4\n\4\2\2\u00c3\u00c2"
          + "\3\2\2\2\u00c4\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6"
          + "\u00c8\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00d2\7$\2\2\u00c9\u00cd\7)\2"
          + "\2\u00ca\u00cc\n\5\2\2\u00cb\u00ca\3\2\2\2\u00cc\u00cf\3\2\2\2\u00cd\u00cb"
          + "\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00d0\3\2\2\2\u00cf\u00cd\3\2\2\2\u00d0"
          + "\u00d2\7)\2\2\u00d1\u00c1\3\2\2\2\u00d1\u00c9\3\2\2\2\u00d2(\3\2\2\2\u00d3"
          + "\u00d7\5\63\31\2\u00d4\u00d6\5\61\30\2\u00d5\u00d4\3\2\2\2\u00d6\u00d9"
          + "\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8*\3\2\2\2\u00d9"
          + "\u00d7\3\2\2\2\u00da\u00db\t\6\2\2\u00db\u00dc\3\2\2\2\u00dc\u00dd\b\25"
          + "\2\2\u00dd,\3\2\2\2\u00de\u00df\t\7\2\2\u00df.\3\2\2\2\u00e0\u00e1\t\b"
          + "\2\2\u00e1\60\3\2\2\2\u00e2\u00e7\5\63\31\2\u00e3\u00e7\t\t\2\2\u00e4"
          + "\u00e7\5/\27\2\u00e5\u00e7\t\n\2\2\u00e6\u00e2\3\2\2\2\u00e6\u00e3\3\2"
          + "\2\2\u00e6\u00e4\3\2\2\2\u00e6\u00e5\3\2\2\2\u00e7\62\3\2\2\2\u00e8\u00ea"
          + "\t\13\2\2\u00e9\u00e8\3\2\2\2\u00ea\64\3\2\2\2\u00eb\u00ec\7A\2\2\u00ec"
          + "\u00ed\7@\2\2\u00ed\u00ee\3\2\2\2\u00ee\u00ef\b\32\6\2\u00ef\66\3\2\2"
          + "\2\u00f0\u00f1\13\2\2\2\u00f1\u00f2\3\2\2\2\u00f2\u00f3\b\33\4\2\u00f3"
          + "8\3\2\2\2\25\2\3\4CWdu\u0080\u0084\u0088\u008b\u008d\u00ad\u00c5\u00cd"
          + "\u00d1\u00d7\u00e6\u00e9\7\b\2\2\7\3\2\5\2\2\7\4\2\6\2\2";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

  static {
    _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
    for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
      _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
    }
  }
}
