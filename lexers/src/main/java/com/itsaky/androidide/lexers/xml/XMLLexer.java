/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.androidide.lexers.xml;

// Generated from XMLLexer.g4 by ANTLR 4.9.2
import java.io.StringReader;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import java.io.IOException;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class XMLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
	new PredictionContextCache();
	public static final int
	COMMENT=1, CDATA=2, DTD=3, EntityRef=4, CharRef=5, SEA_WS=6, OPEN=7, OPEN_SLASH=8, 
	XMLDeclOpen=9, TEXT=10, CLOSE=11, SPECIAL_CLOSE=12, SLASH_CLOSE=13, SLASH=14, 
	EQUALS=15, STRING=16, Name=17, S=18, PI=19;
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
			"COMMENT", "CDATA", "DTD", "EntityRef", "CharRef", "SEA_WS", "OPEN", 
			"CLOSE_OPEN", "XMLDeclOpen", "SPECIAL_OPEN", "TEXT", "CLOSE", "SPECIAL_CLOSE", 
			"SLASH_CLOSE", "SLASH", "EQUALS", "STRING", "Name", "S", "HEXDIGIT", 
			"DIGIT", "NameChar", "NameStartChar", "PI", "IGNORE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, "'<'", "'</'", null, null, 
			"'>'", null, "'/>'", "'/'", "'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COMMENT", "CDATA", "DTD", "EntityRef", "CharRef", "SEA_WS", "OPEN", 
			"CLOSE_OPEN", "XMLDeclOpen", "TEXT", "CLOSE", "SPECIAL_CLOSE", "SLASH_CLOSE", 
			"SLASH", "EQUALS", "STRING", "Name", "S", "PI"
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
    
    public XMLLexer (String input) throws IOException {
        this (CharStreams.fromReader(new StringReader(input)));
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
	"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\25\u00f0\b\1\b\1"+
	"\b\1\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4"+
	"\n\t\n\4\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t"+
	"\21\4\22\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t"+
	"\30\4\31\t\31\4\32\t\32\3\2\3\2\3\2\3\2\3\2\3\2\7\2>\n\2\f\2\16\2A\13"+
	"\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3R\n"+
	"\3\f\3\16\3U\13\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\7\4_\n\4\f\4\16\4b\13"+
	"\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\6\6p\n\6\r\6\16\6q"+
	"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\6\6{\n\6\r\6\16\6|\3\6\3\6\5\6\u0081\n\6"+
	"\3\7\3\7\5\7\u0085\n\7\3\7\6\7\u0088\n\7\r\7\16\7\u0089\3\b\3\b\3\b\3"+
	"\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3"+
	"\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\6\f\u00a8\n\f\r\f\16\f\u00a9\3\r"+
	"\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\20\3"+
	"\20\3\21\3\21\3\22\3\22\7\22\u00c0\n\22\f\22\16\22\u00c3\13\22\3\22\3"+
	"\22\3\22\7\22\u00c8\n\22\f\22\16\22\u00cb\13\22\3\22\5\22\u00ce\n\22\3"+
	"\23\3\23\7\23\u00d2\n\23\f\23\16\23\u00d5\13\23\3\24\3\24\3\24\3\24\3"+
	"\25\3\25\3\26\3\26\3\27\3\27\3\27\3\27\5\27\u00e3\n\27\3\30\5\30\u00e6"+
	"\n\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\5?S`\2\33\5\3\7\4\t"+
	"\5\13\6\r\7\17\b\21\t\23\n\25\13\27\2\31\f\33\r\35\16\37\17!\20#\21%\22"+
	"\'\23)\24+\2-\2/\2\61\2\63\25\65\2\5\2\3\4\f\4\2\13\13\"\"\4\2((>>\4\2"+
	"$$>>\4\2))>>\5\2\13\f\17\17\"\"\5\2\62;CHch\3\2\62;\4\2/\60aa\5\2\u00b9"+
	"\u00b9\u0302\u0371\u2041\u2042\n\2<<C\\c|\u2072\u2191\u2c02\u2ff1\u3003"+
	"\ud801\uf902\ufdd1\ufdf2\uffff\2\u00fa\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
	"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
	"\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\3\33\3\2\2\2\3\35\3\2\2\2\3\37\3"+
	"\2\2\2\3!\3\2\2\2\3#\3\2\2\2\3%\3\2\2\2\3\'\3\2\2\2\3)\3\2\2\2\4\63\3"+
	"\2\2\2\4\65\3\2\2\2\5\67\3\2\2\2\7F\3\2\2\2\tZ\3\2\2\2\13g\3\2\2\2\r\u0080"+
	"\3\2\2\2\17\u0087\3\2\2\2\21\u008b\3\2\2\2\23\u008f\3\2\2\2\25\u0094\3"+
	"\2\2\2\27\u009e\3\2\2\2\31\u00a7\3\2\2\2\33\u00ab\3\2\2\2\35\u00af\3\2"+
	"\2\2\37\u00b4\3\2\2\2!\u00b9\3\2\2\2#\u00bb\3\2\2\2%\u00cd\3\2\2\2\'\u00cf"+
	"\3\2\2\2)\u00d6\3\2\2\2+\u00da\3\2\2\2-\u00dc\3\2\2\2/\u00e2\3\2\2\2\61"+
	"\u00e5\3\2\2\2\63\u00e7\3\2\2\2\65\u00ec\3\2\2\2\678\7>\2\289\7#\2\29"+
	":\7/\2\2:;\7/\2\2;?\3\2\2\2<>\13\2\2\2=<\3\2\2\2>A\3\2\2\2?@\3\2\2\2?"+
	"=\3\2\2\2@B\3\2\2\2A?\3\2\2\2BC\7/\2\2CD\7/\2\2DE\7@\2\2E\6\3\2\2\2FG"+
	"\7>\2\2GH\7#\2\2HI\7]\2\2IJ\7E\2\2JK\7F\2\2KL\7C\2\2LM\7V\2\2MN\7C\2\2"+
	"NO\7]\2\2OS\3\2\2\2PR\13\2\2\2QP\3\2\2\2RU\3\2\2\2ST\3\2\2\2SQ\3\2\2\2"+
	"TV\3\2\2\2US\3\2\2\2VW\7_\2\2WX\7_\2\2XY\7@\2\2Y\b\3\2\2\2Z[\7>\2\2[\\"+
	"\7#\2\2\\`\3\2\2\2]_\13\2\2\2^]\3\2\2\2_b\3\2\2\2`a\3\2\2\2`^\3\2\2\2"+
	"ac\3\2\2\2b`\3\2\2\2cd\7@\2\2de\3\2\2\2ef\b\4\2\2f\n\3\2\2\2gh\7(\2\2"+
	"hi\5\'\23\2ij\7=\2\2j\f\3\2\2\2kl\7(\2\2lm\7%\2\2mo\3\2\2\2np\5-\26\2"+
	"on\3\2\2\2pq\3\2\2\2qo\3\2\2\2qr\3\2\2\2rs\3\2\2\2st\7=\2\2t\u0081\3\2"+
	"\2\2uv\7(\2\2vw\7%\2\2wx\7z\2\2xz\3\2\2\2y{\5+\25\2zy\3\2\2\2{|\3\2\2"+
	"\2|z\3\2\2\2|}\3\2\2\2}~\3\2\2\2~\177\7=\2\2\177\u0081\3\2\2\2\u0080k"+
	"\3\2\2\2\u0080u\3\2\2\2\u0081\16\3\2\2\2\u0082\u0088\t\2\2\2\u0083\u0085"+
	"\7\17\2\2\u0084\u0083\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0086\3\2\2\2"+
	"\u0086\u0088\7\f\2\2\u0087\u0082\3\2\2\2\u0087\u0084\3\2\2\2\u0088\u0089"+
	"\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2\2\2\u008a\20\3\2\2\2\u008b"+
	"\u008c\7>\2\2\u008c\u008d\3\2\2\2\u008d\u008e\b\b\3\2\u008e\22\3\2\2\2"+
	"\u008f\u0090\7>\2\2\u0090\u0091\7\61\2\2\u0091\u0092\3\2\2\2\u0092\u0093"+
	"\b\t\3\2\u0093\24\3\2\2\2\u0094\u0095\7>\2\2\u0095\u0096\7A\2\2\u0096"+
	"\u0097\7z\2\2\u0097\u0098\7o\2\2\u0098\u0099\7n\2\2\u0099\u009a\3\2\2"+
	"\2\u009a\u009b\5)\24\2\u009b\u009c\3\2\2\2\u009c\u009d\b\n\3\2\u009d\26"+
	"\3\2\2\2\u009e\u009f\7>\2\2\u009f\u00a0\7A\2\2\u00a0\u00a1\3\2\2\2\u00a1"+
	"\u00a2\5\'\23\2\u00a2\u00a3\3\2\2\2\u00a3\u00a4\b\13\4\2\u00a4\u00a5\b"+
	"\13\5\2\u00a5\30\3\2\2\2\u00a6\u00a8\n\3\2\2\u00a7\u00a6\3\2\2\2\u00a8"+
	"\u00a9\3\2\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\32\3\2\2"+
	"\2\u00ab\u00ac\7@\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ae\b\r\6\2\u00ae\34"+
	"\3\2\2\2\u00af\u00b0\7A\2\2\u00b0\u00b1\7@\2\2\u00b1\u00b2\3\2\2\2\u00b2"+
	"\u00b3\b\16\6\2\u00b3\36\3\2\2\2\u00b4\u00b5\7\61\2\2\u00b5\u00b6\7@\2"+
	"\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\b\17\6\2\u00b8 \3\2\2\2\u00b9\u00ba"+
	"\7\61\2\2\u00ba\"\3\2\2\2\u00bb\u00bc\7?\2\2\u00bc$\3\2\2\2\u00bd\u00c1"+
	"\7$\2\2\u00be\u00c0\n\4\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1"+
	"\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c4\3\2\2\2\u00c3\u00c1\3\2"+
	"\2\2\u00c4\u00ce\7$\2\2\u00c5\u00c9\7)\2\2\u00c6\u00c8\n\5\2\2\u00c7\u00c6"+
	"\3\2\2\2\u00c8\u00cb\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca"+
	"\u00cc\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cc\u00ce\7)\2\2\u00cd\u00bd\3\2"+
	"\2\2\u00cd\u00c5\3\2\2\2\u00ce&\3\2\2\2\u00cf\u00d3\5\61\30\2\u00d0\u00d2"+
	"\5/\27\2\u00d1\u00d0\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3"+
	"\u00d4\3\2\2\2\u00d4(\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d6\u00d7\t\6\2\2"+
	"\u00d7\u00d8\3\2\2\2\u00d8\u00d9\b\24\2\2\u00d9*\3\2\2\2\u00da\u00db\t"+
	"\7\2\2\u00db,\3\2\2\2\u00dc\u00dd\t\b\2\2\u00dd.\3\2\2\2\u00de\u00e3\5"+
	"\61\30\2\u00df\u00e3\t\t\2\2\u00e0\u00e3\5-\26\2\u00e1\u00e3\t\n\2\2\u00e2"+
	"\u00de\3\2\2\2\u00e2\u00df\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e1\3\2"+
	"\2\2\u00e3\60\3\2\2\2\u00e4\u00e6\t\13\2\2\u00e5\u00e4\3\2\2\2\u00e6\62"+
	"\3\2\2\2\u00e7\u00e8\7A\2\2\u00e8\u00e9\7@\2\2\u00e9\u00ea\3\2\2\2\u00ea"+
	"\u00eb\b\31\6\2\u00eb\64\3\2\2\2\u00ec\u00ed\13\2\2\2\u00ed\u00ee\3\2"+
	"\2\2\u00ee\u00ef\b\32\4\2\u00ef\66\3\2\2\2\25\2\3\4?S`q|\u0080\u0084\u0087"+
	"\u0089\u00a9\u00c1\u00c9\u00cd\u00d3\u00e2\u00e5\7\b\2\2\7\3\2\5\2\2\7"+
	"\4\2\6\2\2";
	public static final ATN _ATN =
	new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
