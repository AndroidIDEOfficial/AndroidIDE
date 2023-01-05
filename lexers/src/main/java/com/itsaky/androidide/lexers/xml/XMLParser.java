// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.xml;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class XMLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		COLON=1, NOT=2, DASH=3, COMMENT_START=4, COMMENT_END=5, COMMENT=6, CDATA=7, 
		DTD=8, EntityRef=9, CharRef=10, SEA_WS=11, OPEN=12, OPEN_SLASH=13, XMLDeclOpen=14, 
		TEXT=15, CommentText=16, CommentModeEnd=17, CLOSE=18, SPECIAL_CLOSE=19, 
		SLASH_CLOSE=20, SLASH=21, EQUALS=22, STRING=23, Name=24, S=25, PI=26;
	public static final int
		RULE_document = 0, RULE_prolog = 1, RULE_content = 2, RULE_element = 3, 
		RULE_nonEmptyClosingElement = 4, RULE_emptyClosingElement = 5, RULE_reference = 6, 
		RULE_attribute = 7, RULE_prefix = 8, RULE_chardata = 9, RULE_misc = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"document", "prolog", "content", "element", "nonEmptyClosingElement", 
			"emptyClosingElement", "reference", "attribute", "prefix", "chardata", 
			"misc"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "':'", "'!'", "'-'", null, null, null, null, null, null, null, 
			null, "'<'", "'</'", null, null, null, null, "'>'", null, "'/>'", "'/'", 
			"'='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "COLON", "NOT", "DASH", "COMMENT_START", "COMMENT_END", "COMMENT", 
			"CDATA", "DTD", "EntityRef", "CharRef", "SEA_WS", "OPEN", "OPEN_SLASH", 
			"XMLDeclOpen", "TEXT", "CommentText", "CommentModeEnd", "CLOSE", "SPECIAL_CLOSE", 
			"SLASH_CLOSE", "SLASH", "EQUALS", "STRING", "Name", "S", "PI"
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

	@Override
	public String getGrammarFileName() { return "java-escape"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public XMLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DocumentContext extends ParserRuleContext {
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public PrologContext prolog() {
			return getRuleContext(PrologContext.class,0);
		}
		public List<MiscContext> misc() {
			return getRuleContexts(MiscContext.class);
		}
		public MiscContext misc(int i) {
			return getRuleContext(MiscContext.class,i);
		}
		public DocumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_document; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterDocument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitDocument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitDocument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentContext document() throws RecognitionException {
		DocumentContext _localctx = new DocumentContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_document);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==XMLDeclOpen) {
				{
				setState(22);
				prolog();
				}
			}

			setState(28);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 67110976L) != 0) {
				{
				{
				setState(25);
				misc();
				}
				}
				setState(30);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(31);
			element();
			setState(35);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 67110976L) != 0) {
				{
				{
				setState(32);
				misc();
				}
				}
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrologContext extends ParserRuleContext {
		public TerminalNode XMLDeclOpen() { return getToken(XMLParser.XMLDeclOpen, 0); }
		public TerminalNode SPECIAL_CLOSE() { return getToken(XMLParser.SPECIAL_CLOSE, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public PrologContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prolog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterProlog(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitProlog(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitProlog(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrologContext prolog() throws RecognitionException {
		PrologContext _localctx = new PrologContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_prolog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			match(XMLDeclOpen);
			setState(42);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Name) {
				{
				{
				setState(39);
				attribute();
				}
				}
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(45);
			match(SPECIAL_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContentContext extends ParserRuleContext {
		public List<ChardataContext> chardata() {
			return getRuleContexts(ChardataContext.class);
		}
		public ChardataContext chardata(int i) {
			return getRuleContext(ChardataContext.class,i);
		}
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public List<ReferenceContext> reference() {
			return getRuleContexts(ReferenceContext.class);
		}
		public ReferenceContext reference(int i) {
			return getRuleContext(ReferenceContext.class,i);
		}
		public List<TerminalNode> CDATA() { return getTokens(XMLParser.CDATA); }
		public TerminalNode CDATA(int i) {
			return getToken(XMLParser.CDATA, i);
		}
		public List<TerminalNode> PI() { return getTokens(XMLParser.PI); }
		public TerminalNode PI(int i) {
			return getToken(XMLParser.PI, i);
		}
		public List<TerminalNode> COMMENT() { return getTokens(XMLParser.COMMENT); }
		public TerminalNode COMMENT(int i) {
			return getToken(XMLParser.COMMENT, i);
		}
		public ContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_content; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContentContext content() throws RecognitionException {
		ContentContext _localctx = new ContentContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_content);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SEA_WS || _la==TEXT) {
				{
				setState(47);
				chardata();
				}
			}

			setState(62);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 67114688L) != 0) {
				{
				{
				setState(55);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case OPEN:
					{
					setState(50);
					element();
					}
					break;
				case EntityRef:
				case CharRef:
					{
					setState(51);
					reference();
					}
					break;
				case CDATA:
					{
					setState(52);
					match(CDATA);
					}
					break;
				case PI:
					{
					setState(53);
					match(PI);
					}
					break;
				case COMMENT:
					{
					setState(54);
					match(COMMENT);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(58);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SEA_WS || _la==TEXT) {
					{
					setState(57);
					chardata();
					}
				}

				}
				}
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElementContext extends ParserRuleContext {
		public NonEmptyClosingElementContext nonEmptyClosingElement() {
			return getRuleContext(NonEmptyClosingElementContext.class,0);
		}
		public EmptyClosingElementContext emptyClosingElement() {
			return getRuleContext(EmptyClosingElementContext.class,0);
		}
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_element);
		try {
			setState(67);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(65);
				nonEmptyClosingElement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(66);
				emptyClosingElement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonEmptyClosingElementContext extends ParserRuleContext {
		public TerminalNode OPEN() { return getToken(XMLParser.OPEN, 0); }
		public List<TerminalNode> Name() { return getTokens(XMLParser.Name); }
		public TerminalNode Name(int i) {
			return getToken(XMLParser.Name, i);
		}
		public List<TerminalNode> CLOSE() { return getTokens(XMLParser.CLOSE); }
		public TerminalNode CLOSE(int i) {
			return getToken(XMLParser.CLOSE, i);
		}
		public ContentContext content() {
			return getRuleContext(ContentContext.class,0);
		}
		public TerminalNode OPEN_SLASH() { return getToken(XMLParser.OPEN_SLASH, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public NonEmptyClosingElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonEmptyClosingElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterNonEmptyClosingElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitNonEmptyClosingElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitNonEmptyClosingElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonEmptyClosingElementContext nonEmptyClosingElement() throws RecognitionException {
		NonEmptyClosingElementContext _localctx = new NonEmptyClosingElementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_nonEmptyClosingElement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			match(OPEN);
			setState(70);
			match(Name);
			setState(74);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Name) {
				{
				{
				setState(71);
				attribute();
				}
				}
				setState(76);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(77);
			match(CLOSE);
			setState(78);
			content();
			setState(79);
			match(OPEN_SLASH);
			setState(80);
			match(Name);
			setState(81);
			match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptyClosingElementContext extends ParserRuleContext {
		public TerminalNode OPEN() { return getToken(XMLParser.OPEN, 0); }
		public TerminalNode Name() { return getToken(XMLParser.Name, 0); }
		public TerminalNode SLASH_CLOSE() { return getToken(XMLParser.SLASH_CLOSE, 0); }
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public EmptyClosingElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyClosingElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterEmptyClosingElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitEmptyClosingElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitEmptyClosingElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyClosingElementContext emptyClosingElement() throws RecognitionException {
		EmptyClosingElementContext _localctx = new EmptyClosingElementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_emptyClosingElement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			match(OPEN);
			setState(84);
			match(Name);
			setState(88);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Name) {
				{
				{
				setState(85);
				attribute();
				}
				}
				setState(90);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(91);
			match(SLASH_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReferenceContext extends ParserRuleContext {
		public TerminalNode EntityRef() { return getToken(XMLParser.EntityRef, 0); }
		public TerminalNode CharRef() { return getToken(XMLParser.CharRef, 0); }
		public ReferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterReference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitReference(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitReference(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReferenceContext reference() throws RecognitionException {
		ReferenceContext _localctx = new ReferenceContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_reference);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !(_la==EntityRef || _la==CharRef) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeContext extends ParserRuleContext {
		public TerminalNode Name() { return getToken(XMLParser.Name, 0); }
		public TerminalNode EQUALS() { return getToken(XMLParser.EQUALS, 0); }
		public TerminalNode STRING() { return getToken(XMLParser.STRING, 0); }
		public PrefixContext prefix() {
			return getRuleContext(PrefixContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(95);
				prefix();
				}
				break;
			}
			setState(98);
			match(Name);
			setState(99);
			match(EQUALS);
			setState(100);
			match(STRING);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrefixContext extends ParserRuleContext {
		public TerminalNode Name() { return getToken(XMLParser.Name, 0); }
		public TerminalNode COLON() { return getToken(XMLParser.COLON, 0); }
		public PrefixContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefix; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterPrefix(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitPrefix(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitPrefix(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrefixContext prefix() throws RecognitionException {
		PrefixContext _localctx = new PrefixContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_prefix);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(Name);
			setState(103);
			match(COLON);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ChardataContext extends ParserRuleContext {
		public TerminalNode TEXT() { return getToken(XMLParser.TEXT, 0); }
		public TerminalNode SEA_WS() { return getToken(XMLParser.SEA_WS, 0); }
		public ChardataContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_chardata; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterChardata(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitChardata(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitChardata(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ChardataContext chardata() throws RecognitionException {
		ChardataContext _localctx = new ChardataContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_chardata);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(105);
			_la = _input.LA(1);
			if ( !(_la==SEA_WS || _la==TEXT) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MiscContext extends ParserRuleContext {
		public TerminalNode COMMENT() { return getToken(XMLParser.COMMENT, 0); }
		public TerminalNode PI() { return getToken(XMLParser.PI, 0); }
		public TerminalNode SEA_WS() { return getToken(XMLParser.SEA_WS, 0); }
		public MiscContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_misc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).enterMisc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof XMLParserListener ) ((XMLParserListener)listener).exitMisc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof XMLParserVisitor ) return ((XMLParserVisitor<? extends T>)visitor).visitMisc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MiscContext misc() throws RecognitionException {
		MiscContext _localctx = new MiscContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_misc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 67110976L) != 0) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001an\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000\u0003\u0000\u0018"+
		"\b\u0000\u0001\u0000\u0005\u0000\u001b\b\u0000\n\u0000\f\u0000\u001e\t"+
		"\u0000\u0001\u0000\u0001\u0000\u0005\u0000\"\b\u0000\n\u0000\f\u0000%"+
		"\t\u0000\u0001\u0001\u0001\u0001\u0005\u0001)\b\u0001\n\u0001\f\u0001"+
		",\t\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0003\u00021\b\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u00028\b"+
		"\u0002\u0001\u0002\u0003\u0002;\b\u0002\u0005\u0002=\b\u0002\n\u0002\f"+
		"\u0002@\t\u0002\u0001\u0003\u0001\u0003\u0003\u0003D\b\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0005\u0004I\b\u0004\n\u0004\f\u0004L\t\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005W\b\u0005\n\u0005\f\u0005"+
		"Z\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0003\u0007a\b\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0000"+
		"\u0000\u000b\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0000"+
		"\u0003\u0001\u0000\t\n\u0002\u0000\u000b\u000b\u000f\u000f\u0003\u0000"+
		"\u0006\u0006\u000b\u000b\u001a\u001aq\u0000\u0017\u0001\u0000\u0000\u0000"+
		"\u0002&\u0001\u0000\u0000\u0000\u00040\u0001\u0000\u0000\u0000\u0006C"+
		"\u0001\u0000\u0000\u0000\bE\u0001\u0000\u0000\u0000\nS\u0001\u0000\u0000"+
		"\u0000\f]\u0001\u0000\u0000\u0000\u000e`\u0001\u0000\u0000\u0000\u0010"+
		"f\u0001\u0000\u0000\u0000\u0012i\u0001\u0000\u0000\u0000\u0014k\u0001"+
		"\u0000\u0000\u0000\u0016\u0018\u0003\u0002\u0001\u0000\u0017\u0016\u0001"+
		"\u0000\u0000\u0000\u0017\u0018\u0001\u0000\u0000\u0000\u0018\u001c\u0001"+
		"\u0000\u0000\u0000\u0019\u001b\u0003\u0014\n\u0000\u001a\u0019\u0001\u0000"+
		"\u0000\u0000\u001b\u001e\u0001\u0000\u0000\u0000\u001c\u001a\u0001\u0000"+
		"\u0000\u0000\u001c\u001d\u0001\u0000\u0000\u0000\u001d\u001f\u0001\u0000"+
		"\u0000\u0000\u001e\u001c\u0001\u0000\u0000\u0000\u001f#\u0003\u0006\u0003"+
		"\u0000 \"\u0003\u0014\n\u0000! \u0001\u0000\u0000\u0000\"%\u0001\u0000"+
		"\u0000\u0000#!\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000\u0000$\u0001"+
		"\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000&*\u0005\u000e\u0000"+
		"\u0000\')\u0003\u000e\u0007\u0000(\'\u0001\u0000\u0000\u0000),\u0001\u0000"+
		"\u0000\u0000*(\u0001\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+-\u0001"+
		"\u0000\u0000\u0000,*\u0001\u0000\u0000\u0000-.\u0005\u0013\u0000\u0000"+
		".\u0003\u0001\u0000\u0000\u0000/1\u0003\u0012\t\u00000/\u0001\u0000\u0000"+
		"\u000001\u0001\u0000\u0000\u00001>\u0001\u0000\u0000\u000028\u0003\u0006"+
		"\u0003\u000038\u0003\f\u0006\u000048\u0005\u0007\u0000\u000058\u0005\u001a"+
		"\u0000\u000068\u0005\u0006\u0000\u000072\u0001\u0000\u0000\u000073\u0001"+
		"\u0000\u0000\u000074\u0001\u0000\u0000\u000075\u0001\u0000\u0000\u0000"+
		"76\u0001\u0000\u0000\u00008:\u0001\u0000\u0000\u00009;\u0003\u0012\t\u0000"+
		":9\u0001\u0000\u0000\u0000:;\u0001\u0000\u0000\u0000;=\u0001\u0000\u0000"+
		"\u0000<7\u0001\u0000\u0000\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000"+
		"\u0000\u0000>?\u0001\u0000\u0000\u0000?\u0005\u0001\u0000\u0000\u0000"+
		"@>\u0001\u0000\u0000\u0000AD\u0003\b\u0004\u0000BD\u0003\n\u0005\u0000"+
		"CA\u0001\u0000\u0000\u0000CB\u0001\u0000\u0000\u0000D\u0007\u0001\u0000"+
		"\u0000\u0000EF\u0005\f\u0000\u0000FJ\u0005\u0018\u0000\u0000GI\u0003\u000e"+
		"\u0007\u0000HG\u0001\u0000\u0000\u0000IL\u0001\u0000\u0000\u0000JH\u0001"+
		"\u0000\u0000\u0000JK\u0001\u0000\u0000\u0000KM\u0001\u0000\u0000\u0000"+
		"LJ\u0001\u0000\u0000\u0000MN\u0005\u0012\u0000\u0000NO\u0003\u0004\u0002"+
		"\u0000OP\u0005\r\u0000\u0000PQ\u0005\u0018\u0000\u0000QR\u0005\u0012\u0000"+
		"\u0000R\t\u0001\u0000\u0000\u0000ST\u0005\f\u0000\u0000TX\u0005\u0018"+
		"\u0000\u0000UW\u0003\u000e\u0007\u0000VU\u0001\u0000\u0000\u0000WZ\u0001"+
		"\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000"+
		"Y[\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000[\\\u0005\u0014\u0000"+
		"\u0000\\\u000b\u0001\u0000\u0000\u0000]^\u0007\u0000\u0000\u0000^\r\u0001"+
		"\u0000\u0000\u0000_a\u0003\u0010\b\u0000`_\u0001\u0000\u0000\u0000`a\u0001"+
		"\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000bc\u0005\u0018\u0000\u0000"+
		"cd\u0005\u0016\u0000\u0000de\u0005\u0017\u0000\u0000e\u000f\u0001\u0000"+
		"\u0000\u0000fg\u0005\u0018\u0000\u0000gh\u0005\u0001\u0000\u0000h\u0011"+
		"\u0001\u0000\u0000\u0000ij\u0007\u0001\u0000\u0000j\u0013\u0001\u0000"+
		"\u0000\u0000kl\u0007\u0002\u0000\u0000l\u0015\u0001\u0000\u0000\u0000"+
		"\f\u0017\u001c#*07:>CJX`";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}