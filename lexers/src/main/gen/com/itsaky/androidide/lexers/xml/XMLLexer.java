// Generated from
// /home/itsaky/Projects/Android/AndroidIDE/lexers/src/main/java/com/itsaky/androidide/lexers/xml/XMLLexer.g4 by ANTLR 4.10.1
package com.itsaky.androidide.lexers.xml;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

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
      CLOSE = 16,
      SPECIAL_CLOSE = 17,
      SLASH_CLOSE = 18,
      SLASH = 19,
      EQUALS = 20,
      STRING = 21,
      Name = 22,
      S = 23,
      PI = 24;
  public static final int INSIDE = 1, PROC_INSTR = 2;
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;
  public static final String _serializedATN =
      "\u0004\u0000\u0018\u0101\u0006\uffff\uffff\u0006\uffff\uffff\u0006\uffff"
          + "\uffff\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007"
          + "\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007"
          + "\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b"
          + "\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007"
          + "\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002"
          + "\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012\u0002"
          + "\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015\u0002"
          + "\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018\u0002"
          + "\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b\u0002"
          + "\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0001\u0000\u0001\u0000\u0001"
          + "\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"
          + "\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"
          + "\u0004\u0001\u0005\u0001\u0005\u0005\u0005Q\b\u0005\n\u0005\f\u0005T\t"
          + "\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001"
          + "\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"
          + "\u0006\u0001\u0006\u0005\u0006c\b\u0006\n\u0006\f\u0006f\t\u0006\u0001"
          + "\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"
          + "\u0007\u0001\u0007\u0005\u0007p\b\u0007\n\u0007\f\u0007s\t\u0007\u0001"
          + "\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001"
          + "\b\u0001\t\u0001\t\u0001\t\u0001\t\u0004\t\u0081\b\t\u000b\t\f\t\u0082"
          + "\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0004\t\u008c"
          + "\b\t\u000b\t\f\t\u008d\u0001\t\u0001\t\u0003\t\u0092\b\t\u0001\n\u0001"
          + "\n\u0003\n\u0096\b\n\u0001\n\u0004\n\u0099\b\n\u000b\n\f\n\u009a\u0001"
          + "\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001"
          + "\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"
          + "\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"
          + "\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000f\u00b9"
          + "\b\u000f\u000b\u000f\f\u000f\u00ba\u0001\u0010\u0001\u0010\u0001\u0010"
          + "\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011"
          + "\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0013"
          + "\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0005\u0015"
          + "\u00d1\b\u0015\n\u0015\f\u0015\u00d4\t\u0015\u0001\u0015\u0001\u0015\u0001"
          + "\u0015\u0005\u0015\u00d9\b\u0015\n\u0015\f\u0015\u00dc\t\u0015\u0001\u0015"
          + "\u0003\u0015\u00df\b\u0015\u0001\u0016\u0001\u0016\u0005\u0016\u00e3\b"
          + "\u0016\n\u0016\f\u0016\u00e6\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017"
          + "\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u001a"
          + "\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u00f4\b\u001a\u0001\u001b"
          + "\u0003\u001b\u00f7\b\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c"
          + "\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003Rdq"
          + "\u0000\u001e\u0003\u0001\u0005\u0002\u0007\u0003\t\u0004\u000b\u0005\r"
          + "\u0006\u000f\u0007\u0011\b\u0013\t\u0015\n\u0017\u000b\u0019\f\u001b\r"
          + "\u001d\u000e\u001f\u0000!\u000f#\u0010%\u0011\'\u0012)\u0013+\u0014-\u0015"
          + "/\u00161\u00173\u00005\u00007\u00009\u0000;\u0018=\u0000\u0003\u0000\u0001"
          + "\u0002\n\u0002\u0000\t\t  \u0002\u0000&&<<\u0002\u0000\"\"<<\u0002\u0000"
          + "\'\'<<\u0003\u0000\t\n\r\r  \u0003\u000009AFaf\u0001\u000009\u0002\u0000"
          + "-.__\u0003\u0000\u00b7\u00b7\u0300\u036f\u203f\u2040\b\u0000::AZaz\u2070"
          + "\u218f\u2c00\u2fef\u3001\u8000\ud7ff\u8000\uf900\u8000\ufdcf\u8000\ufdf0"
          + "\u8000\ufffd\u010b\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001"
          + "\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000"
          + "\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000"
          + "\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000"
          + "\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000"
          + "\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000"
          + "\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000"
          + "\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000"
          + "\u0001#\u0001\u0000\u0000\u0000\u0001%\u0001\u0000\u0000\u0000\u0001\'"
          + "\u0001\u0000\u0000\u0000\u0001)\u0001\u0000\u0000\u0000\u0001+\u0001\u0000"
          + "\u0000\u0000\u0001-\u0001\u0000\u0000\u0000\u0001/\u0001\u0000\u0000\u0000"
          + "\u00011\u0001\u0000\u0000\u0000\u0002;\u0001\u0000\u0000\u0000\u0002="
          + "\u0001\u0000\u0000\u0000\u0003?\u0001\u0000\u0000\u0000\u0005A\u0001\u0000"
          + "\u0000\u0000\u0007C\u0001\u0000\u0000\u0000\tE\u0001\u0000\u0000\u0000"
          + "\u000bJ\u0001\u0000\u0000\u0000\rN\u0001\u0000\u0000\u0000\u000fW\u0001"
          + "\u0000\u0000\u0000\u0011k\u0001\u0000\u0000\u0000\u0013x\u0001\u0000\u0000"
          + "\u0000\u0015\u0091\u0001\u0000\u0000\u0000\u0017\u0098\u0001\u0000\u0000"
          + "\u0000\u0019\u009c\u0001\u0000\u0000\u0000\u001b\u00a0\u0001\u0000\u0000"
          + "\u0000\u001d\u00a5\u0001\u0000\u0000\u0000\u001f\u00af\u0001\u0000\u0000"
          + "\u0000!\u00b8\u0001\u0000\u0000\u0000#\u00bc\u0001\u0000\u0000\u0000%"
          + "\u00c0\u0001\u0000\u0000\u0000\'\u00c5\u0001\u0000\u0000\u0000)\u00ca"
          + "\u0001\u0000\u0000\u0000+\u00cc\u0001\u0000\u0000\u0000-\u00de\u0001\u0000"
          + "\u0000\u0000/\u00e0\u0001\u0000\u0000\u00001\u00e7\u0001\u0000\u0000\u0000"
          + "3\u00eb\u0001\u0000\u0000\u00005\u00ed\u0001\u0000\u0000\u00007\u00f3"
          + "\u0001\u0000\u0000\u00009\u00f6\u0001\u0000\u0000\u0000;\u00f8\u0001\u0000"
          + "\u0000\u0000=\u00fd\u0001\u0000\u0000\u0000?@\u0005:\u0000\u0000@\u0004"
          + "\u0001\u0000\u0000\u0000AB\u0005!\u0000\u0000B\u0006\u0001\u0000\u0000"
          + "\u0000CD\u0005-\u0000\u0000D\b\u0001\u0000\u0000\u0000EF\u0005<\u0000"
          + "\u0000FG\u0003\u0005\u0001\u0000GH\u0003\u0007\u0002\u0000HI\u0003\u0007"
          + "\u0002\u0000I\n\u0001\u0000\u0000\u0000JK\u0003\u0007\u0002\u0000KL\u0003"
          + "\u0007\u0002\u0000LM\u0005>\u0000\u0000M\f\u0001\u0000\u0000\u0000NR\u0003"
          + "\t\u0003\u0000OQ\t\u0000\u0000\u0000PO\u0001\u0000\u0000\u0000QT\u0001"
          + "\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000RP\u0001\u0000\u0000\u0000"
          + "SU\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000\u0000UV\u0003\u000b\u0004"
          + "\u0000V\u000e\u0001\u0000\u0000\u0000WX\u0005<\u0000\u0000XY\u0005!\u0000"
          + "\u0000YZ\u0005[\u0000\u0000Z[\u0005C\u0000\u0000[\\\u0005D\u0000\u0000"
          + "\\]\u0005A\u0000\u0000]^\u0005T\u0000\u0000^_\u0005A\u0000\u0000_`\u0005"
          + "[\u0000\u0000`d\u0001\u0000\u0000\u0000ac\t\u0000\u0000\u0000ba\u0001"
          + "\u0000\u0000\u0000cf\u0001\u0000\u0000\u0000de\u0001\u0000\u0000\u0000"
          + "db\u0001\u0000\u0000\u0000eg\u0001\u0000\u0000\u0000fd\u0001\u0000\u0000"
          + "\u0000gh\u0005]\u0000\u0000hi\u0005]\u0000\u0000ij\u0005>\u0000\u0000"
          + "j\u0010\u0001\u0000\u0000\u0000kl\u0005<\u0000\u0000lm\u0005!\u0000\u0000"
          + "mq\u0001\u0000\u0000\u0000np\t\u0000\u0000\u0000on\u0001\u0000\u0000\u0000"
          + "ps\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000"
          + "\u0000rt\u0001\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000tu\u0005>\u0000"
          + "\u0000uv\u0001\u0000\u0000\u0000vw\u0006\u0007\u0000\u0000w\u0012\u0001"
          + "\u0000\u0000\u0000xy\u0005&\u0000\u0000yz\u0003/\u0016\u0000z{\u0005;"
          + "\u0000\u0000{\u0014\u0001\u0000\u0000\u0000|}\u0005&\u0000\u0000}~\u0005"
          + "#\u0000\u0000~\u0080\u0001\u0000\u0000\u0000\u007f\u0081\u00035\u0019"
          + "\u0000\u0080\u007f\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000"
          + "\u0000\u0082\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000"
          + "\u0000\u0083\u0084\u0001\u0000\u0000\u0000\u0084\u0085\u0005;\u0000\u0000"
          + "\u0085\u0092\u0001\u0000\u0000\u0000\u0086\u0087\u0005&\u0000\u0000\u0087"
          + "\u0088\u0005#\u0000\u0000\u0088\u0089\u0005x\u0000\u0000\u0089\u008b\u0001"
          + "\u0000\u0000\u0000\u008a\u008c\u00033\u0018\u0000\u008b\u008a\u0001\u0000"
          + "\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d\u008b\u0001\u0000"
          + "\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008f\u0001\u0000"
          + "\u0000\u0000\u008f\u0090\u0005;\u0000\u0000\u0090\u0092\u0001\u0000\u0000"
          + "\u0000\u0091|\u0001\u0000\u0000\u0000\u0091\u0086\u0001\u0000\u0000\u0000"
          + "\u0092\u0016\u0001\u0000\u0000\u0000\u0093\u0099\u0007\u0000\u0000\u0000"
          + "\u0094\u0096\u0005\r\u0000\u0000\u0095\u0094\u0001\u0000\u0000\u0000\u0095"
          + "\u0096\u0001\u0000\u0000\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u0097"
          + "\u0099\u0005\n\u0000\u0000\u0098\u0093\u0001\u0000\u0000\u0000\u0098\u0095"
          + "\u0001\u0000\u0000\u0000\u0099\u009a\u0001\u0000\u0000\u0000\u009a\u0098"
          + "\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009b\u0018"
          + "\u0001\u0000\u0000\u0000\u009c\u009d\u0005<\u0000\u0000\u009d\u009e\u0001"
          + "\u0000\u0000\u0000\u009e\u009f\u0006\u000b\u0001\u0000\u009f\u001a\u0001"
          + "\u0000\u0000\u0000\u00a0\u00a1\u0005<\u0000\u0000\u00a1\u00a2\u0005/\u0000"
          + "\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000\u00a3\u00a4\u0006\f\u0001\u0000"
          + "\u00a4\u001c\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005<\u0000\u0000\u00a6"
          + "\u00a7\u0005?\u0000\u0000\u00a7\u00a8\u0005x\u0000\u0000\u00a8\u00a9\u0005"
          + "m\u0000\u0000\u00a9\u00aa\u0005l\u0000\u0000\u00aa\u00ab\u0001\u0000\u0000"
          + "\u0000\u00ab\u00ac\u00031\u0017\u0000\u00ac\u00ad\u0001\u0000\u0000\u0000"
          + "\u00ad\u00ae\u0006\r\u0001\u0000\u00ae\u001e\u0001\u0000\u0000\u0000\u00af"
          + "\u00b0\u0005<\u0000\u0000\u00b0\u00b1\u0005?\u0000\u0000\u00b1\u00b2\u0001"
          + "\u0000\u0000\u0000\u00b2\u00b3\u0003/\u0016\u0000\u00b3\u00b4\u0001\u0000"
          + "\u0000\u0000\u00b4\u00b5\u0006\u000e\u0002\u0000\u00b5\u00b6\u0006\u000e"
          + "\u0003\u0000\u00b6 \u0001\u0000\u0000\u0000\u00b7\u00b9\b\u0001\u0000"
          + "\u0000\u00b8\u00b7\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000"
          + "\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001\u0000\u0000"
          + "\u0000\u00bb\"\u0001\u0000\u0000\u0000\u00bc\u00bd\u0005>\u0000\u0000"
          + "\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bf\u0006\u0010\u0004\u0000"
          + "\u00bf$\u0001\u0000\u0000\u0000\u00c0\u00c1\u0005?\u0000\u0000\u00c1\u00c2"
          + "\u0005>\u0000\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u00c4\u0006"
          + "\u0011\u0004\u0000\u00c4&\u0001\u0000\u0000\u0000\u00c5\u00c6\u0005/\u0000"
          + "\u0000\u00c6\u00c7\u0005>\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000\u0000"
          + "\u00c8\u00c9\u0006\u0012\u0004\u0000\u00c9(\u0001\u0000\u0000\u0000\u00ca"
          + "\u00cb\u0005/\u0000\u0000\u00cb*\u0001\u0000\u0000\u0000\u00cc\u00cd\u0005"
          + "=\u0000\u0000\u00cd,\u0001\u0000\u0000\u0000\u00ce\u00d2\u0005\"\u0000"
          + "\u0000\u00cf\u00d1\b\u0002\u0000\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000"
          + "\u00d1\u00d4\u0001\u0000\u0000\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000"
          + "\u00d2\u00d3\u0001\u0000\u0000\u0000\u00d3\u00d5\u0001\u0000\u0000\u0000"
          + "\u00d4\u00d2\u0001\u0000\u0000\u0000\u00d5\u00df\u0005\"\u0000\u0000\u00d6"
          + "\u00da\u0005\'\u0000\u0000\u00d7\u00d9\b\u0003\u0000\u0000\u00d8\u00d7"
          + "\u0001\u0000\u0000\u0000\u00d9\u00dc\u0001\u0000\u0000\u0000\u00da\u00d8"
          + "\u0001\u0000\u0000\u0000\u00da\u00db\u0001\u0000\u0000\u0000\u00db\u00dd"
          + "\u0001\u0000\u0000\u0000\u00dc\u00da\u0001\u0000\u0000\u0000\u00dd\u00df"
          + "\u0005\'\u0000\u0000\u00de\u00ce\u0001\u0000\u0000\u0000\u00de\u00d6\u0001"
          + "\u0000\u0000\u0000\u00df.\u0001\u0000\u0000\u0000\u00e0\u00e4\u00039\u001b"
          + "\u0000\u00e1\u00e3\u00037\u001a\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000"
          + "\u00e3\u00e6\u0001\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000"
          + "\u00e4\u00e5\u0001\u0000\u0000\u0000\u00e50\u0001\u0000\u0000\u0000\u00e6"
          + "\u00e4\u0001\u0000\u0000\u0000\u00e7\u00e8\u0007\u0004\u0000\u0000\u00e8"
          + "\u00e9\u0001\u0000\u0000\u0000\u00e9\u00ea\u0006\u0017\u0000\u0000\u00ea"
          + "2\u0001\u0000\u0000\u0000\u00eb\u00ec\u0007\u0005\u0000\u0000\u00ec4\u0001"
          + "\u0000\u0000\u0000\u00ed\u00ee\u0007\u0006\u0000\u0000\u00ee6\u0001\u0000"
          + "\u0000\u0000\u00ef\u00f4\u00039\u001b\u0000\u00f0\u00f4\u0007\u0007\u0000"
          + "\u0000\u00f1\u00f4\u00035\u0019\u0000\u00f2\u00f4\u0007\b\u0000\u0000"
          + "\u00f3\u00ef\u0001\u0000\u0000\u0000\u00f3\u00f0\u0001\u0000\u0000\u0000"
          + "\u00f3\u00f1\u0001\u0000\u0000\u0000\u00f3\u00f2\u0001\u0000\u0000\u0000"
          + "\u00f48\u0001\u0000\u0000\u0000\u00f5\u00f7\u0007\t\u0000\u0000\u00f6"
          + "\u00f5\u0001\u0000\u0000\u0000\u00f7:\u0001\u0000\u0000\u0000\u00f8\u00f9"
          + "\u0005?\u0000\u0000\u00f9\u00fa\u0005>\u0000\u0000\u00fa\u00fb\u0001\u0000"
          + "\u0000\u0000\u00fb\u00fc\u0006\u001c\u0004\u0000\u00fc<\u0001\u0000\u0000"
          + "\u0000\u00fd\u00fe\t\u0000\u0000\u0000\u00fe\u00ff\u0001\u0000\u0000\u0000"
          + "\u00ff\u0100\u0006\u001d\u0002\u0000\u0100>\u0001\u0000\u0000\u0000\u0013"
          + "\u0000\u0001\u0002Rdq\u0082\u008d\u0091\u0095\u0098\u009a\u00ba\u00d2"
          + "\u00da\u00de\u00e4\u00f3\u00f6\u0005\u0006\u0000\u0000\u0005\u0001\u0000"
          + "\u0003\u0000\u0000\u0005\u0002\u0000\u0004\u0000\u0000";
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  private static final String[] _LITERAL_NAMES = makeLiteralNames();
  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
  public static String[] channelNames = {"DEFAULT_TOKEN_CHANNEL", "HIDDEN"};
  public static String[] modeNames = {"DEFAULT_MODE", "INSIDE", "PROC_INSTR"};

  static {
    RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION);
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
      null, null, "'>'", null, "'/>'", "'/'", "'='"
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
  @Deprecated
  public String[] getTokenNames() {
    return tokenNames;
  }

  @Override
  public Vocabulary getVocabulary() {
    return VOCABULARY;
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
}
