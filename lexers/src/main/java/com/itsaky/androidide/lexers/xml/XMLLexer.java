// Generated from XMLLexer.g4 by ANTLR 4.9.3
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
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COLON=1, NOT=2, DASH=3, COMMENT=4, CDATA=5, DTD=6, EntityRef=7, CharRef=8, 
		SEA_WS=9, OPEN=10, OPEN_SLASH=11, XMLDeclOpen=12, TEXT=13, CLOSE=14, SPECIAL_CLOSE=15, 
		SLASH_CLOSE=16, SLASH=17, EQUALS=18, STRING=19, Name=20, S=21, PI=22;
	public static final int
		INSIDE=1, PROC_INSTR=2;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "INSIDE", "PROC_INSTR"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"COLON", "NOT", "DASH", "COMMENT", "CDATA", "DTD", "EntityRef", "CharRef", 
			"SEA_WS", "OPEN", "OPEN_SLASH", "XMLDeclOpen", "SPECIAL_OPEN", "TEXT", 
			"CLOSE", "SPECIAL_CLOSE", "SLASH_CLOSE", "SLASH", "EQUALS", "STRING", 
			"Name", "S", "HEXDIGIT", "DIGIT", "NameChar", "NameStartChar", "PI", 
			"IGNORE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", "'!'", "'-'", null, null, null, null, null, null, "'<'", 
			"'</'", null, null, "'>'", null, "'/>'", "'/'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COLON", "NOT", "DASH", "COMMENT", "CDATA", "DTD", "EntityRef", 
			"CharRef", "SEA_WS", "OPEN", "OPEN_SLASH", "XMLDeclOpen", "TEXT", "CLOSE", 
			"SPECIAL_CLOSE", "SLASH_CLOSE", "SLASH", "EQUALS", "STRING", "Name", 
			"S", "PI"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
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
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "XMLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\u00fc\b\1\b\1"+
		"\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4"+
		"\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t"+
		"\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t"+
		"\30\4\31\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\3\3"+
		"\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\7\5J\n\5\f\5\16\5M\13\5\3\5\3\5\3\5\3"+
		"\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6^\n\6\f\6\16\6a\13\6"+
		"\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\7\7k\n\7\f\7\16\7n\13\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\6\t|\n\t\r\t\16\t}\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\6\t\u0087\n\t\r\t\16\t\u0088\3\t\3\t\5\t\u008d\n\t\3\n\3"+
		"\n\5\n\u0091\n\n\3\n\6\n\u0094\n\n\r\n\16\n\u0095\3\13\3\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\17\6\17\u00b4\n\17\r\17\16\17\u00b5\3"+
		"\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3"+
		"\23\3\23\3\24\3\24\3\25\3\25\7\25\u00cc\n\25\f\25\16\25\u00cf\13\25\3"+
		"\25\3\25\3\25\7\25\u00d4\n\25\f\25\16\25\u00d7\13\25\3\25\5\25\u00da\n"+
		"\25\3\26\3\26\7\26\u00de\n\26\f\26\16\26\u00e1\13\26\3\27\3\27\3\27\3"+
		"\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\5\32\u00ef\n\32\3\33\5\33"+
		"\u00f2\n\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\5K_l\2\36\5\3"+
		"\7\4\t\5\13\6\r\7\17\b\21\t\23\n\25\13\27\f\31\r\33\16\35\2\37\17!\20"+
		"#\21%\22\'\23)\24+\25-\26/\27\61\2\63\2\65\2\67\29\30;\2\5\2\3\4\f\4\2"+
		"\13\13\"\"\4\2((>>\4\2$$>>\4\2))>>\5\2\13\f\17\17\"\"\5\2\62;CHch\3\2"+
		"\62;\4\2/\60aa\5\2\u00b9\u00b9\u0302\u0371\u2041\u2042\n\2<<C\\c|\u2072"+
		"\u2191\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2\uffff\2\u0106\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\3!\3\2\2\2\3#\3\2\2\2\3%\3\2\2\2\3\'\3"+
		"\2\2\2\3)\3\2\2\2\3+\3\2\2\2\3-\3\2\2\2\3/\3\2\2\2\49\3\2\2\2\4;\3\2\2"+
		"\2\5=\3\2\2\2\7?\3\2\2\2\tA\3\2\2\2\13C\3\2\2\2\rR\3\2\2\2\17f\3\2\2\2"+
		"\21s\3\2\2\2\23\u008c\3\2\2\2\25\u0093\3\2\2\2\27\u0097\3\2\2\2\31\u009b"+
		"\3\2\2\2\33\u00a0\3\2\2\2\35\u00aa\3\2\2\2\37\u00b3\3\2\2\2!\u00b7\3\2"+
		"\2\2#\u00bb\3\2\2\2%\u00c0\3\2\2\2\'\u00c5\3\2\2\2)\u00c7\3\2\2\2+\u00d9"+
		"\3\2\2\2-\u00db\3\2\2\2/\u00e2\3\2\2\2\61\u00e6\3\2\2\2\63\u00e8\3\2\2"+
		"\2\65\u00ee\3\2\2\2\67\u00f1\3\2\2\29\u00f3\3\2\2\2;\u00f8\3\2\2\2=>\7"+
		"<\2\2>\6\3\2\2\2?@\7#\2\2@\b\3\2\2\2AB\7/\2\2B\n\3\2\2\2CD\7>\2\2DE\7"+
		"#\2\2EF\7/\2\2FG\7/\2\2GK\3\2\2\2HJ\13\2\2\2IH\3\2\2\2JM\3\2\2\2KL\3\2"+
		"\2\2KI\3\2\2\2LN\3\2\2\2MK\3\2\2\2NO\7/\2\2OP\7/\2\2PQ\7@\2\2Q\f\3\2\2"+
		"\2RS\7>\2\2ST\7#\2\2TU\7]\2\2UV\7E\2\2VW\7F\2\2WX\7C\2\2XY\7V\2\2YZ\7"+
		"C\2\2Z[\7]\2\2[_\3\2\2\2\\^\13\2\2\2]\\\3\2\2\2^a\3\2\2\2_`\3\2\2\2_]"+
		"\3\2\2\2`b\3\2\2\2a_\3\2\2\2bc\7_\2\2cd\7_\2\2de\7@\2\2e\16\3\2\2\2fg"+
		"\7>\2\2gh\7#\2\2hl\3\2\2\2ik\13\2\2\2ji\3\2\2\2kn\3\2\2\2lm\3\2\2\2lj"+
		"\3\2\2\2mo\3\2\2\2nl\3\2\2\2op\7@\2\2pq\3\2\2\2qr\b\7\2\2r\20\3\2\2\2"+
		"st\7(\2\2tu\5-\26\2uv\7=\2\2v\22\3\2\2\2wx\7(\2\2xy\7%\2\2y{\3\2\2\2z"+
		"|\5\63\31\2{z\3\2\2\2|}\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\177\3\2\2\2\177\u0080"+
		"\7=\2\2\u0080\u008d\3\2\2\2\u0081\u0082\7(\2\2\u0082\u0083\7%\2\2\u0083"+
		"\u0084\7z\2\2\u0084\u0086\3\2\2\2\u0085\u0087\5\61\30\2\u0086\u0085\3"+
		"\2\2\2\u0087\u0088\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089"+
		"\u008a\3\2\2\2\u008a\u008b\7=\2\2\u008b\u008d\3\2\2\2\u008cw\3\2\2\2\u008c"+
		"\u0081\3\2\2\2\u008d\24\3\2\2\2\u008e\u0094\t\2\2\2\u008f\u0091\7\17\2"+
		"\2\u0090\u008f\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0094"+
		"\7\f\2\2\u0093\u008e\3\2\2\2\u0093\u0090\3\2\2\2\u0094\u0095\3\2\2\2\u0095"+
		"\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\26\3\2\2\2\u0097\u0098\7>\2\2"+
		"\u0098\u0099\3\2\2\2\u0099\u009a\b\13\3\2\u009a\30\3\2\2\2\u009b\u009c"+
		"\7>\2\2\u009c\u009d\7\61\2\2\u009d\u009e\3\2\2\2\u009e\u009f\b\f\3\2\u009f"+
		"\32\3\2\2\2\u00a0\u00a1\7>\2\2\u00a1\u00a2\7A\2\2\u00a2\u00a3\7z\2\2\u00a3"+
		"\u00a4\7o\2\2\u00a4\u00a5\7n\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\5/\27"+
		"\2\u00a7\u00a8\3\2\2\2\u00a8\u00a9\b\r\3\2\u00a9\34\3\2\2\2\u00aa\u00ab"+
		"\7>\2\2\u00ab\u00ac\7A\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\5-\26\2\u00ae"+
		"\u00af\3\2\2\2\u00af\u00b0\b\16\4\2\u00b0\u00b1\b\16\5\2\u00b1\36\3\2"+
		"\2\2\u00b2\u00b4\n\3\2\2\u00b3\u00b2\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5"+
		"\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6 \3\2\2\2\u00b7\u00b8\7@\2\2\u00b8"+
		"\u00b9\3\2\2\2\u00b9\u00ba\b\20\6\2\u00ba\"\3\2\2\2\u00bb\u00bc\7A\2\2"+
		"\u00bc\u00bd\7@\2\2\u00bd\u00be\3\2\2\2\u00be\u00bf\b\21\6\2\u00bf$\3"+
		"\2\2\2\u00c0\u00c1\7\61\2\2\u00c1\u00c2\7@\2\2\u00c2\u00c3\3\2\2\2\u00c3"+
		"\u00c4\b\22\6\2\u00c4&\3\2\2\2\u00c5\u00c6\7\61\2\2\u00c6(\3\2\2\2\u00c7"+
		"\u00c8\7?\2\2\u00c8*\3\2\2\2\u00c9\u00cd\7$\2\2\u00ca\u00cc\n\4\2\2\u00cb"+
		"\u00ca\3\2\2\2\u00cc\u00cf\3\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2"+
		"\2\2\u00ce\u00d0\3\2\2\2\u00cf\u00cd\3\2\2\2\u00d0\u00da\7$\2\2\u00d1"+
		"\u00d5\7)\2\2\u00d2\u00d4\n\5\2\2\u00d3\u00d2\3\2\2\2\u00d4\u00d7\3\2"+
		"\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d8\3\2\2\2\u00d7"+
		"\u00d5\3\2\2\2\u00d8\u00da\7)\2\2\u00d9\u00c9\3\2\2\2\u00d9\u00d1\3\2"+
		"\2\2\u00da,\3\2\2\2\u00db\u00df\5\67\33\2\u00dc\u00de\5\65\32\2\u00dd"+
		"\u00dc\3\2\2\2\u00de\u00e1\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2"+
		"\2\2\u00e0.\3\2\2\2\u00e1\u00df\3\2\2\2\u00e2\u00e3\t\6\2\2\u00e3\u00e4"+
		"\3\2\2\2\u00e4\u00e5\b\27\2\2\u00e5\60\3\2\2\2\u00e6\u00e7\t\7\2\2\u00e7"+
		"\62\3\2\2\2\u00e8\u00e9\t\b\2\2\u00e9\64\3\2\2\2\u00ea\u00ef\5\67\33\2"+
		"\u00eb\u00ef\t\t\2\2\u00ec\u00ef\5\63\31\2\u00ed\u00ef\t\n\2\2\u00ee\u00ea"+
		"\3\2\2\2\u00ee\u00eb\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ee\u00ed\3\2\2\2\u00ef"+
		"\66\3\2\2\2\u00f0\u00f2\t\13\2\2\u00f1\u00f0\3\2\2\2\u00f28\3\2\2\2\u00f3"+
		"\u00f4\7A\2\2\u00f4\u00f5\7@\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\b\34"+
		"\6\2\u00f7:\3\2\2\2\u00f8\u00f9\13\2\2\2\u00f9\u00fa\3\2\2\2\u00fa\u00fb"+
		"\b\35\4\2\u00fb<\3\2\2\2\25\2\3\4K_l}\u0088\u008c\u0090\u0093\u0095\u00b5"+
		"\u00cd\u00d5\u00d9\u00df\u00ee\u00f1\7\b\2\2\7\3\2\5\2\2\7\4\2\6\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}