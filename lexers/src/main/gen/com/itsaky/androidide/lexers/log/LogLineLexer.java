// Generated from /home/itsaky/Projects/Android/AndroidIDE/lexers/src/main/java/com/itsaky/androidide/lexers/log/LogLineLexer.g4 by ANTLR 4.10.1
package com.itsaky.androidide.lexers.log;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LogLineLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Tag=1, Priority=2, Message=3, Date=4, Time=5, ProcessAndThread=6, Digit=7, 
		Digits=8, CapsLetter=9, Identifier=10, Identifiers=11, WhiteSpace=12, 
		NewLine=13, COLON=14, DASH=15, DOT=16, SLASH=17, Unknown=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"Date", "Time", "ProcessAndThread", "Digit", "Digits", "CapsLetter", 
			"Letter", "Identifier", "Identifiers", "WhiteSpace", "NewLine", "COLON", 
			"DASH", "DOT", "SLASH", "Unknown"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "'\\n'", "':'", "'-'", "'.'", "'/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Tag", "Priority", "Message", "Date", "Time", "ProcessAndThread", 
			"Digit", "Digits", "CapsLetter", "Identifier", "Identifiers", "WhiteSpace", 
			"NewLine", "COLON", "DASH", "DOT", "SLASH", "Unknown"
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


	    static boolean tagSeen = false;
	    static boolean prioritySeen = false;


	public LogLineLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LogLineLexer.g4"; }

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

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 8:
			Identifiers_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void Identifiers_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			  if (tagSeen) {
			                                 setType(Priority);
			                                 prioritySeen = true;
			                               } else if (prioritySeen) {
			                                 setType(Message)
			                                 prioritySeen = true;
			                               } else {
			                                 setType(Tag);
			                                 tagSeen = true;
			                               }
			                             
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000\u0012g\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0001\u0003\u0001\u0003\u0001\u0004\u0004\u0004<\b\u0004\u000b"+
		"\u0004\f\u0004=\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0003\u0006F\b\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0004\bK\b\b\u000b\b\f\bL\u0001\b\u0001\b\u0001\t\u0004\tR\b\t\u000b"+
		"\t\f\tS\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0005\u000fa\b\u000f\n"+
		"\u000f\f\u000fd\t\u000f\u0001\u000f\u0001\u000f\u0001b\u0000\u0010\u0001"+
		"\u0004\u0003\u0005\u0005\u0006\u0007\u0007\t\b\u000b\t\r\u0000\u000f\n"+
		"\u0011\u000b\u0013\f\u0015\r\u0017\u000e\u0019\u000f\u001b\u0010\u001d"+
		"\u0011\u001f\u0012\u0001\u0000\u0006\u0004\u0000$$AZ__az\u0002\u0000\u0000"+
		"\u007f\u8000\ud800\u8000\udbff\u0001\u0000\u8000\ud800\u8000\udbff\u0001"+
		"\u0000\u8000\udc00\u8000\udfff\u0005\u0000..09AZ__az\u0002\u0000\t\t "+
		" k\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000"+
		"\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000"+
		"\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000"+
		"\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000"+
		"\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000"+
		"\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000"+
		"\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000"+
		"\u001f\u0001\u0000\u0000\u0000\u0001!\u0001\u0000\u0000\u0000\u0003\'"+
		"\u0001\u0000\u0000\u0000\u00054\u0001\u0000\u0000\u0000\u00078\u0001\u0000"+
		"\u0000\u0000\t;\u0001\u0000\u0000\u0000\u000b?\u0001\u0000\u0000\u0000"+
		"\rE\u0001\u0000\u0000\u0000\u000fG\u0001\u0000\u0000\u0000\u0011J\u0001"+
		"\u0000\u0000\u0000\u0013Q\u0001\u0000\u0000\u0000\u0015U\u0001\u0000\u0000"+
		"\u0000\u0017W\u0001\u0000\u0000\u0000\u0019Y\u0001\u0000\u0000\u0000\u001b"+
		"[\u0001\u0000\u0000\u0000\u001d]\u0001\u0000\u0000\u0000\u001fb\u0001"+
		"\u0000\u0000\u0000!\"\u0003\u0007\u0003\u0000\"#\u0003\u0007\u0003\u0000"+
		"#$\u0003\u0019\f\u0000$%\u0003\u0007\u0003\u0000%&\u0003\u0007\u0003\u0000"+
		"&\u0002\u0001\u0000\u0000\u0000\'(\u0003\u0007\u0003\u0000()\u0003\u0007"+
		"\u0003\u0000)*\u0003\u0017\u000b\u0000*+\u0003\u0007\u0003\u0000+,\u0003"+
		"\u0007\u0003\u0000,-\u0003\u0017\u000b\u0000-.\u0003\u0007\u0003\u0000"+
		"./\u0003\u0007\u0003\u0000/0\u0003\u001b\r\u000001\u0003\u0007\u0003\u0000"+
		"12\u0003\u0007\u0003\u000023\u0003\u0007\u0003\u00003\u0004\u0001\u0000"+
		"\u0000\u000045\u0003\t\u0004\u000056\u0003\u001d\u000e\u000067\u0003\t"+
		"\u0004\u00007\u0006\u0001\u0000\u0000\u000089\u000209\u00009\b\u0001\u0000"+
		"\u0000\u0000:<\u0003\u0007\u0003\u0000;:\u0001\u0000\u0000\u0000<=\u0001"+
		"\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000"+
		">\n\u0001\u0000\u0000\u0000?@\u0002AZ\u0000@\f\u0001\u0000\u0000\u0000"+
		"AF\u0007\u0000\u0000\u0000BF\b\u0001\u0000\u0000CD\u0007\u0002\u0000\u0000"+
		"DF\u0007\u0003\u0000\u0000EA\u0001\u0000\u0000\u0000EB\u0001\u0000\u0000"+
		"\u0000EC\u0001\u0000\u0000\u0000F\u000e\u0001\u0000\u0000\u0000GH\u0007"+
		"\u0004\u0000\u0000H\u0010\u0001\u0000\u0000\u0000IK\u0003\u000f\u0007"+
		"\u0000JI\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000LJ\u0001\u0000"+
		"\u0000\u0000LM\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000NO\u0006"+
		"\b\u0000\u0000O\u0012\u0001\u0000\u0000\u0000PR\u0007\u0005\u0000\u0000"+
		"QP\u0001\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000"+
		"\u0000ST\u0001\u0000\u0000\u0000T\u0014\u0001\u0000\u0000\u0000UV\u0005"+
		"\n\u0000\u0000V\u0016\u0001\u0000\u0000\u0000WX\u0005:\u0000\u0000X\u0018"+
		"\u0001\u0000\u0000\u0000YZ\u0005-\u0000\u0000Z\u001a\u0001\u0000\u0000"+
		"\u0000[\\\u0005.\u0000\u0000\\\u001c\u0001\u0000\u0000\u0000]^\u0005/"+
		"\u0000\u0000^\u001e\u0001\u0000\u0000\u0000_a\t\u0000\u0000\u0000`_\u0001"+
		"\u0000\u0000\u0000ad\u0001\u0000\u0000\u0000bc\u0001\u0000\u0000\u0000"+
		"b`\u0001\u0000\u0000\u0000ce\u0001\u0000\u0000\u0000db\u0001\u0000\u0000"+
		"\u0000ef\u0003\u0015\n\u0000f \u0001\u0000\u0000\u0000\u0006\u0000=EL"+
		"Sb\u0001\u0001\b\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}