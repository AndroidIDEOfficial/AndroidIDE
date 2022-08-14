// Generated from XMLLexer.g4 by ANTLR 4.9.3
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
  public static final int COLON = 1,
      NOT = 2,
      DASH = 3,
      COMMENT_START = 4,
      COMMENT_END = 5,
      COMMENT = 6,
      CDATA = 7,
      DTD = 8,
      EntityRef = 9,
      CharRef = 10,
      SEA_WS = 11,
      OPEN = 12,
      OPEN_SLASH = 13,
      XMLDeclOpen = 14,
      TEXT = 15,
      CommentText = 16,
      CommentModeEnd = 17,
      CLOSE = 18,
      SPECIAL_CLOSE = 19,
      SLASH_CLOSE = 20,
      SLASH = 21,
      EQUALS = 22,
      STRING = 23,
      Name = 24,
      S = 25,
      PI = 26;
  public static final int COMMENT_MODE = 1, TAG_MODE = 2, PROC_INSTR = 3;
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  public static final String _serializedATN =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\34\u0114\b\1\b\1"
          + "\b\1\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t"
          + "\t\4\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21"
          + "\t\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30"
          + "\t\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37"
          + "\t\37\4 \t \4!\t!\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5"
          + "\3\6\3\6\3\6\3\6\3\7\3\7\7\7Z\n\7\f\7\16\7]\13\7\3\7\3\7\3\b\3\b\3\b\3"
          + "\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\7\bl\n\b\f\b\16\bo\13\b\3\b\3\b\3\b\3\b"
          + "\3\t\3\t\3\t\3\t\7\ty\n\t\f\t\16\t|\13\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3"
          + "\n\3\13\3\13\3\13\3\13\6\13\u008a\n\13\r\13\16\13\u008b\3\13\3\13\3\13"
          + "\3\13\3\13\3\13\3\13\6\13\u0095\n\13\r\13\16\13\u0096\3\13\3\13\5\13\u009b"
          + "\n\13\3\f\3\f\5\f\u009f\n\f\3\f\6\f\u00a2\n\f\r\f\16\f\u00a3\3\r\3\r\3"
          + "\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"
          + "\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\6\21\u00c2\n\21"
          + "\r\21\16\21\u00c3\3\22\6\22\u00c7\n\22\r\22\16\22\u00c8\3\23\3\23\3\23"
          + "\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26"
          + "\3\26\3\27\3\27\3\30\3\30\3\31\3\31\7\31\u00e3\n\31\f\31\16\31\u00e6\13"
          + "\31\3\31\3\31\3\31\7\31\u00eb\n\31\f\31\16\31\u00ee\13\31\3\31\5\31\u00f1"
          + "\n\31\3\32\3\32\7\32\u00f5\n\32\f\32\16\32\u00f8\13\32\3\33\3\33\3\33"
          + "\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\36\3\36\3\36\5\36\u0107\n\36\3\37"
          + "\5\37\u010a\n\37\3 \3 \3 \3 \3 \3!\3!\3!\3!\5[mz\2\"\6\3\b\4\n\5\f\6\16"
          + "\7\20\b\22\t\24\n\26\13\30\f\32\r\34\16\36\17 \20\"\2$\21&\22(\23*\24"
          + ",\25.\26\60\27\62\30\64\31\66\328\33:\2<\2>\2@\2B\34D\2\6\2\3\4\5\r\4"
          + "\2\13\13\"\"\4\2((>>\5\2((//>>\4\2$$>>\4\2))>>\5\2\13\f\17\17\"\"\5\2"
          + "\62;CHch\3\2\62;\4\2\60\60aa\5\2\u00b9\u00b9\u0302\u0371\u2041\u2042\n"
          + "\2<<C\\c|\u2072\u2191\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2\uffff"
          + "\2\u011f\2\6\3\2\2\2\2\b\3\2\2\2\2\n\3\2\2\2\2\f\3\2\2\2\2\16\3\2\2\2"
          + "\2\20\3\2\2\2\2\22\3\2\2\2\2\24\3\2\2\2\2\26\3\2\2\2\2\30\3\2\2\2\2\32"
          + "\3\2\2\2\2\34\3\2\2\2\2\36\3\2\2\2\2 \3\2\2\2\2\"\3\2\2\2\2$\3\2\2\2\3"
          + "&\3\2\2\2\3(\3\2\2\2\4*\3\2\2\2\4,\3\2\2\2\4.\3\2\2\2\4\60\3\2\2\2\4\62"
          + "\3\2\2\2\4\64\3\2\2\2\4\66\3\2\2\2\48\3\2\2\2\5B\3\2\2\2\5D\3\2\2\2\6"
          + "F\3\2\2\2\bH\3\2\2\2\nJ\3\2\2\2\fL\3\2\2\2\16S\3\2\2\2\20W\3\2\2\2\22"
          + "`\3\2\2\2\24t\3\2\2\2\26\u0081\3\2\2\2\30\u009a\3\2\2\2\32\u00a1\3\2\2"
          + "\2\34\u00a5\3\2\2\2\36\u00a9\3\2\2\2 \u00ae\3\2\2\2\"\u00b8\3\2\2\2$\u00c1"
          + "\3\2\2\2&\u00c6\3\2\2\2(\u00ca\3\2\2\2*\u00ce\3\2\2\2,\u00d2\3\2\2\2."
          + "\u00d7\3\2\2\2\60\u00dc\3\2\2\2\62\u00de\3\2\2\2\64\u00f0\3\2\2\2\66\u00f2"
          + "\3\2\2\28\u00f9\3\2\2\2:\u00fd\3\2\2\2<\u00ff\3\2\2\2>\u0106\3\2\2\2@"
          + "\u0109\3\2\2\2B\u010b\3\2\2\2D\u0110\3\2\2\2FG\7<\2\2G\7\3\2\2\2HI\7#"
          + "\2\2I\t\3\2\2\2JK\7/\2\2K\13\3\2\2\2LM\7>\2\2MN\5\b\3\2NO\5\n\4\2OP\5"
          + "\n\4\2PQ\3\2\2\2QR\b\5\2\2R\r\3\2\2\2ST\5\n\4\2TU\5\n\4\2UV\7@\2\2V\17"
          + "\3\2\2\2W[\5\f\5\2XZ\13\2\2\2YX\3\2\2\2Z]\3\2\2\2[\\\3\2\2\2[Y\3\2\2\2"
          + "\\^\3\2\2\2][\3\2\2\2^_\5\16\6\2_\21\3\2\2\2`a\7>\2\2ab\7#\2\2bc\7]\2"
          + "\2cd\7E\2\2de\7F\2\2ef\7C\2\2fg\7V\2\2gh\7C\2\2hi\7]\2\2im\3\2\2\2jl\13"
          + "\2\2\2kj\3\2\2\2lo\3\2\2\2mn\3\2\2\2mk\3\2\2\2np\3\2\2\2om\3\2\2\2pq\7"
          + "_\2\2qr\7_\2\2rs\7@\2\2s\23\3\2\2\2tu\7>\2\2uv\7#\2\2vz\3\2\2\2wy\13\2"
          + "\2\2xw\3\2\2\2y|\3\2\2\2z{\3\2\2\2zx\3\2\2\2{}\3\2\2\2|z\3\2\2\2}~\7@"
          + "\2\2~\177\3\2\2\2\177\u0080\b\t\3\2\u0080\25\3\2\2\2\u0081\u0082\7(\2"
          + "\2\u0082\u0083\5\66\32\2\u0083\u0084\7=\2\2\u0084\27\3\2\2\2\u0085\u0086"
          + "\7(\2\2\u0086\u0087\7%\2\2\u0087\u0089\3\2\2\2\u0088\u008a\5<\35\2\u0089"
          + "\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u0089\3\2\2\2\u008b\u008c\3\2"
          + "\2\2\u008c\u008d\3\2\2\2\u008d\u008e\7=\2\2\u008e\u009b\3\2\2\2\u008f"
          + "\u0090\7(\2\2\u0090\u0091\7%\2\2\u0091\u0092\7z\2\2\u0092\u0094\3\2\2"
          + "\2\u0093\u0095\5:\34\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0094"
          + "\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\7=\2\2\u0099"
          + "\u009b\3\2\2\2\u009a\u0085\3\2\2\2\u009a\u008f\3\2\2\2\u009b\31\3\2\2"
          + "\2\u009c\u00a2\t\2\2\2\u009d\u009f\7\17\2\2\u009e\u009d\3\2\2\2\u009e"
          + "\u009f\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\u00a2\7\f\2\2\u00a1\u009c\3\2"
          + "\2\2\u00a1\u009e\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3"
          + "\u00a4\3\2\2\2\u00a4\33\3\2\2\2\u00a5\u00a6\7>\2\2\u00a6\u00a7\3\2\2\2"
          + "\u00a7\u00a8\b\r\4\2\u00a8\35\3\2\2\2\u00a9\u00aa\7>\2\2\u00aa\u00ab\7"
          + "\61\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\b\16\4\2\u00ad\37\3\2\2\2\u00ae"
          + "\u00af\7>\2\2\u00af\u00b0\7A\2\2\u00b0\u00b1\7z\2\2\u00b1\u00b2\7o\2\2"
          + "\u00b2\u00b3\7n\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b5\58\33\2\u00b5\u00b6"
          + "\3\2\2\2\u00b6\u00b7\b\17\4\2\u00b7!\3\2\2\2\u00b8\u00b9\7>\2\2\u00b9"
          + "\u00ba\7A\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\5\66\32\2\u00bc\u00bd\3"
          + "\2\2\2\u00bd\u00be\b\20\5\2\u00be\u00bf\b\20\6\2\u00bf#\3\2\2\2\u00c0"
          + "\u00c2\n\3\2\2\u00c1\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c1\3\2"
          + "\2\2\u00c3\u00c4\3\2\2\2\u00c4%\3\2\2\2\u00c5\u00c7\n\4\2\2\u00c6\u00c5"
          + "\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9"
          + "\'\3\2\2\2\u00ca\u00cb\5\16\6\2\u00cb\u00cc\3\2\2\2\u00cc\u00cd\b\23\7"
          + "\2\u00cd)\3\2\2\2\u00ce\u00cf\7@\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1\b"
          + "\24\7\2\u00d1+\3\2\2\2\u00d2\u00d3\7A\2\2\u00d3\u00d4\7@\2\2\u00d4\u00d5"
          + "\3\2\2\2\u00d5\u00d6\b\25\7\2\u00d6-\3\2\2\2\u00d7\u00d8\7\61\2\2\u00d8"
          + "\u00d9\7@\2\2\u00d9\u00da\3\2\2\2\u00da\u00db\b\26\7\2\u00db/\3\2\2\2"
          + "\u00dc\u00dd\7\61\2\2\u00dd\61\3\2\2\2\u00de\u00df\7?\2\2\u00df\63\3\2"
          + "\2\2\u00e0\u00e4\7$\2\2\u00e1\u00e3\n\5\2\2\u00e2\u00e1\3\2\2\2\u00e3"
          + "\u00e6\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7\3\2"
          + "\2\2\u00e6\u00e4\3\2\2\2\u00e7\u00f1\7$\2\2\u00e8\u00ec\7)\2\2\u00e9\u00eb"
          + "\n\6\2\2\u00ea\u00e9\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec"
          + "\u00ed\3\2\2\2\u00ed\u00ef\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f1\7)"
          + "\2\2\u00f0\u00e0\3\2\2\2\u00f0\u00e8\3\2\2\2\u00f1\65\3\2\2\2\u00f2\u00f6"
          + "\5@\37\2\u00f3\u00f5\5>\36\2\u00f4\u00f3\3\2\2\2\u00f5\u00f8\3\2\2\2\u00f6"
          + "\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\67\3\2\2\2\u00f8\u00f6\3\2\2"
          + "\2\u00f9\u00fa\t\7\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\b\33\3\2\u00fc"
          + "9\3\2\2\2\u00fd\u00fe\t\b\2\2\u00fe;\3\2\2\2\u00ff\u0100\t\t\2\2\u0100"
          + "=\3\2\2\2\u0101\u0107\5@\37\2\u0102\u0107\5\n\4\2\u0103\u0107\t\n\2\2"
          + "\u0104\u0107\5<\35\2\u0105\u0107\t\13\2\2\u0106\u0101\3\2\2\2\u0106\u0102"
          + "\3\2\2\2\u0106\u0103\3\2\2\2\u0106\u0104\3\2\2\2\u0106\u0105\3\2\2\2\u0107"
          + "?\3\2\2\2\u0108\u010a\t\f\2\2\u0109\u0108\3\2\2\2\u010aA\3\2\2\2\u010b"
          + "\u010c\7A\2\2\u010c\u010d\7@\2\2\u010d\u010e\3\2\2\2\u010e\u010f\b \7"
          + "\2\u010fC\3\2\2\2\u0110\u0111\13\2\2\2\u0111\u0112\3\2\2\2\u0112\u0113"
          + "\b!\5\2\u0113E\3\2\2\2\27\2\3\4\5[mz\u008b\u0096\u009a\u009e\u00a1\u00a3"
          + "\u00c3\u00c8\u00e4\u00ec\u00f0\u00f6\u0106\u0109\b\7\3\2\b\2\2\7\4\2\5"
          + "\2\2\7\5\2\6\2\2";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  private static final String[] _LITERAL_NAMES = makeLiteralNames();
  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
  public static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};
  public static String[] modeNames = {"DEFAULT_MODE", "COMMENT_MODE", "TAG_MODE", "PROC_INSTR"};

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

  public XMLLexer(CharStream input) {
    super(input);
    _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  private static String[] makeRuleNames() {
    return new String[] {
      "COLON",
      "NOT",
      "DASH",
      "COMMENT_START",
      "COMMENT_END",
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
      "CommentText",
      "CommentModeEnd",
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

  private static String[] makeLiteralNames() {
    return new String[] {
      null, "':'", "'!'", "'-'", null, null, null, null, null, null, null, null, "'<'", "'</'",
      null, null, null, null, "'>'", null, "'/>'", "'/'", "'='"
    };
  }

  private static String[] makeSymbolicNames() {
    return new String[] {
      null,
      "COLON",
      "NOT",
      "DASH",
      "COMMENT_START",
      "COMMENT_END",
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
      "CommentText",
      "CommentModeEnd",
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
    return "XMLLexer.g4";
  }

  @Override
  public ATN getATN() {
    return _ATN;
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
