// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.javadoc;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class JavadocParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NAME=1, NEWLINE=2, SPACE=3, TEXT_CONTENT=4, AT=5, STAR=6, SLASH=7, JAVADOC_START=8, 
		JAVADOC_END=9, INLINE_TAG_START=10, BRACE_OPEN=11, BRACE_CLOSE=12;
	public static final int
		RULE_documentation = 0, RULE_documentationContent = 1, RULE_skipWhitespace = 2, 
		RULE_description = 3, RULE_descriptionLine = 4, RULE_descriptionLineStart = 5, 
		RULE_descriptionLineNoSpaceNoAt = 6, RULE_descriptionLineElement = 7, 
		RULE_descriptionLineText = 8, RULE_descriptionNewline = 9, RULE_tagSection = 10, 
		RULE_blockTag = 11, RULE_blockTagName = 12, RULE_blockTagContent = 13, 
		RULE_blockTagText = 14, RULE_blockTagTextElement = 15, RULE_inlineTag = 16, 
		RULE_inlineTagName = 17, RULE_inlineTagContent = 18, RULE_braceExpression = 19, 
		RULE_braceContent = 20, RULE_braceText = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"documentation", "documentationContent", "skipWhitespace", "description", 
			"descriptionLine", "descriptionLineStart", "descriptionLineNoSpaceNoAt", 
			"descriptionLineElement", "descriptionLineText", "descriptionNewline", 
			"tagSection", "blockTag", "blockTagName", "blockTagContent", "blockTagText", 
			"blockTagTextElement", "inlineTag", "inlineTagName", "inlineTagContent", 
			"braceExpression", "braceContent", "braceText"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'@'", "'*'", "'/'", null, null, "'{@'", 
			"'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NAME", "NEWLINE", "SPACE", "TEXT_CONTENT", "AT", "STAR", "SLASH", 
			"JAVADOC_START", "JAVADOC_END", "INLINE_TAG_START", "BRACE_OPEN", "BRACE_CLOSE"
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

	public JavadocParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DocumentationContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(JavadocParser.EOF, 0); }
		public TerminalNode JAVADOC_START() { return getToken(JavadocParser.JAVADOC_START, 0); }
		public DocumentationContentContext documentationContent() {
			return getRuleContext(DocumentationContentContext.class,0);
		}
		public TerminalNode JAVADOC_END() { return getToken(JavadocParser.JAVADOC_END, 0); }
		public List<SkipWhitespaceContext> skipWhitespace() {
			return getRuleContexts(SkipWhitespaceContext.class);
		}
		public SkipWhitespaceContext skipWhitespace(int i) {
			return getRuleContext(SkipWhitespaceContext.class,i);
		}
		public DocumentationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_documentation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDocumentation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDocumentation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDocumentation(this);
			else return visitor.visitChildren(this);
		}
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
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(46);
						skipWhitespace();
						}
						} 
					}
					setState(51);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
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
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(56);
						skipWhitespace();
						}
						} 
					}
					setState(61);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
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
	public static class DocumentationContentContext extends ParserRuleContext {
		public DescriptionContext description() {
			return getRuleContext(DescriptionContext.class,0);
		}
		public List<SkipWhitespaceContext> skipWhitespace() {
			return getRuleContexts(SkipWhitespaceContext.class);
		}
		public SkipWhitespaceContext skipWhitespace(int i) {
			return getRuleContext(SkipWhitespaceContext.class,i);
		}
		public TagSectionContext tagSection() {
			return getRuleContext(TagSectionContext.class,0);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(JavadocParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(JavadocParser.NEWLINE, i);
		}
		public DocumentationContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_documentationContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDocumentationContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDocumentationContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDocumentationContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DocumentationContentContext documentationContent() throws RecognitionException {
		DocumentationContentContext _localctx = new DocumentationContentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_documentationContent);
		int _la;
		try {
			int _alt;
			setState(95);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(67);
				description();
				setState(71);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==NEWLINE || _la==SPACE) {
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
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(74);
						skipWhitespace();
						}
						} 
					}
					setState(79);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
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
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(90);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(87);
						skipWhitespace();
						}
						} 
					}
					setState(92);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
				}
				setState(93);
				tagSection();
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
	public static class SkipWhitespaceContext extends ParserRuleContext {
		public TerminalNode SPACE() { return getToken(JavadocParser.SPACE, 0); }
		public TerminalNode NEWLINE() { return getToken(JavadocParser.NEWLINE, 0); }
		public SkipWhitespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_skipWhitespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterSkipWhitespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitSkipWhitespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitSkipWhitespace(this);
			else return visitor.visitChildren(this);
		}
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
			if ( !(_la==NEWLINE || _la==SPACE) ) {
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
	public static class DescriptionContext extends ParserRuleContext {
		public List<DescriptionLineContext> descriptionLine() {
			return getRuleContexts(DescriptionLineContext.class);
		}
		public DescriptionLineContext descriptionLine(int i) {
			return getRuleContext(DescriptionLineContext.class,i);
		}
		public List<DescriptionNewlineContext> descriptionNewline() {
			return getRuleContexts(DescriptionNewlineContext.class);
		}
		public DescriptionNewlineContext descriptionNewline(int i) {
			return getRuleContext(DescriptionNewlineContext.class,i);
		}
		public DescriptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_description; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescription(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescription(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescription(this);
			else return visitor.visitChildren(this);
		}
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
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
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
					} while ( _la==NEWLINE );
					setState(105);
					descriptionLine();
					}
					} 
				}
				setState(111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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
	public static class DescriptionLineContext extends ParserRuleContext {
		public DescriptionLineStartContext descriptionLineStart() {
			return getRuleContext(DescriptionLineStartContext.class,0);
		}
		public List<DescriptionLineElementContext> descriptionLineElement() {
			return getRuleContexts(DescriptionLineElementContext.class);
		}
		public DescriptionLineElementContext descriptionLineElement(int i) {
			return getRuleContext(DescriptionLineElementContext.class,i);
		}
		public InlineTagContext inlineTag() {
			return getRuleContext(InlineTagContext.class,0);
		}
		public DescriptionLineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionLine; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescriptionLine(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescriptionLine(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescriptionLine(this);
			else return visitor.visitChildren(this);
		}
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
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(113);
						descriptionLineElement();
						}
						} 
					}
					setState(118);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
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
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(120);
						descriptionLineElement();
						}
						} 
					}
					setState(125);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class DescriptionLineStartContext extends ParserRuleContext {
		public List<TerminalNode> SPACE() { return getTokens(JavadocParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(JavadocParser.SPACE, i);
		}
		public List<DescriptionLineNoSpaceNoAtContext> descriptionLineNoSpaceNoAt() {
			return getRuleContexts(DescriptionLineNoSpaceNoAtContext.class);
		}
		public DescriptionLineNoSpaceNoAtContext descriptionLineNoSpaceNoAt(int i) {
			return getRuleContext(DescriptionLineNoSpaceNoAtContext.class,i);
		}
		public List<TerminalNode> AT() { return getTokens(JavadocParser.AT); }
		public TerminalNode AT(int i) {
			return getToken(JavadocParser.AT, i);
		}
		public DescriptionLineStartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionLineStart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescriptionLineStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescriptionLineStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescriptionLineStart(this);
			else return visitor.visitChildren(this);
		}
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
			if (_la==SPACE) {
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
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(141);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
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
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
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
	public static class DescriptionLineNoSpaceNoAtContext extends ParserRuleContext {
		public TerminalNode TEXT_CONTENT() { return getToken(JavadocParser.TEXT_CONTENT, 0); }
		public TerminalNode NAME() { return getToken(JavadocParser.NAME, 0); }
		public TerminalNode STAR() { return getToken(JavadocParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(JavadocParser.SLASH, 0); }
		public TerminalNode BRACE_OPEN() { return getToken(JavadocParser.BRACE_OPEN, 0); }
		public TerminalNode BRACE_CLOSE() { return getToken(JavadocParser.BRACE_CLOSE, 0); }
		public DescriptionLineNoSpaceNoAtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionLineNoSpaceNoAt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescriptionLineNoSpaceNoAt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescriptionLineNoSpaceNoAt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescriptionLineNoSpaceNoAt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DescriptionLineNoSpaceNoAtContext descriptionLineNoSpaceNoAt() throws RecognitionException {
		DescriptionLineNoSpaceNoAtContext _localctx = new DescriptionLineNoSpaceNoAtContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_descriptionLineNoSpaceNoAt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 6354L) != 0) ) {
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
	public static class DescriptionLineElementContext extends ParserRuleContext {
		public InlineTagContext inlineTag() {
			return getRuleContext(InlineTagContext.class,0);
		}
		public DescriptionLineTextContext descriptionLineText() {
			return getRuleContext(DescriptionLineTextContext.class,0);
		}
		public DescriptionLineElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionLineElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescriptionLineElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescriptionLineElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescriptionLineElement(this);
			else return visitor.visitChildren(this);
		}
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
	public static class DescriptionLineTextContext extends ParserRuleContext {
		public List<DescriptionLineNoSpaceNoAtContext> descriptionLineNoSpaceNoAt() {
			return getRuleContexts(DescriptionLineNoSpaceNoAtContext.class);
		}
		public DescriptionLineNoSpaceNoAtContext descriptionLineNoSpaceNoAt(int i) {
			return getRuleContext(DescriptionLineNoSpaceNoAtContext.class,i);
		}
		public List<TerminalNode> SPACE() { return getTokens(JavadocParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(JavadocParser.SPACE, i);
		}
		public List<TerminalNode> AT() { return getTokens(JavadocParser.AT); }
		public TerminalNode AT(int i) {
			return getToken(JavadocParser.AT, i);
		}
		public DescriptionLineTextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionLineText; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescriptionLineText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescriptionLineText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescriptionLineText(this);
			else return visitor.visitChildren(this);
		}
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
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	public static class DescriptionNewlineContext extends ParserRuleContext {
		public TerminalNode NEWLINE() { return getToken(JavadocParser.NEWLINE, 0); }
		public DescriptionNewlineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_descriptionNewline; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterDescriptionNewline(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitDescriptionNewline(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitDescriptionNewline(this);
			else return visitor.visitChildren(this);
		}
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
	public static class TagSectionContext extends ParserRuleContext {
		public List<BlockTagContext> blockTag() {
			return getRuleContexts(BlockTagContext.class);
		}
		public BlockTagContext blockTag(int i) {
			return getRuleContext(BlockTagContext.class,i);
		}
		public TagSectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tagSection; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterTagSection(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitTagSection(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitTagSection(this);
			else return visitor.visitChildren(this);
		}
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
			} while ( _la==SPACE || _la==AT );
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
	public static class BlockTagContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(JavadocParser.AT, 0); }
		public BlockTagNameContext blockTagName() {
			return getRuleContext(BlockTagNameContext.class,0);
		}
		public List<TerminalNode> SPACE() { return getTokens(JavadocParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(JavadocParser.SPACE, i);
		}
		public List<BlockTagContentContext> blockTagContent() {
			return getRuleContexts(BlockTagContentContext.class);
		}
		public BlockTagContentContext blockTagContent(int i) {
			return getRuleContext(BlockTagContentContext.class,i);
		}
		public BlockTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBlockTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBlockTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBlockTag(this);
			else return visitor.visitChildren(this);
		}
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
			if (_la==SPACE) {
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
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(169);
				match(SPACE);
				}
				break;
			}
			setState(175);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(172);
					blockTagContent();
					}
					} 
				}
				setState(177);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,23,_ctx);
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
	public static class BlockTagNameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(JavadocParser.NAME, 0); }
		public BlockTagNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockTagName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBlockTagName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBlockTagName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBlockTagName(this);
			else return visitor.visitChildren(this);
		}
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
	public static class BlockTagContentContext extends ParserRuleContext {
		public BlockTagTextContext blockTagText() {
			return getRuleContext(BlockTagTextContext.class,0);
		}
		public InlineTagContext inlineTag() {
			return getRuleContext(InlineTagContext.class,0);
		}
		public TerminalNode NEWLINE() { return getToken(JavadocParser.NEWLINE, 0); }
		public BlockTagContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockTagContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBlockTagContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBlockTagContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBlockTagContent(this);
			else return visitor.visitChildren(this);
		}
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
	public static class BlockTagTextContext extends ParserRuleContext {
		public List<BlockTagTextElementContext> blockTagTextElement() {
			return getRuleContexts(BlockTagTextElementContext.class);
		}
		public BlockTagTextElementContext blockTagTextElement(int i) {
			return getRuleContext(BlockTagTextElementContext.class,i);
		}
		public BlockTagTextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockTagText; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBlockTagText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBlockTagText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBlockTagText(this);
			else return visitor.visitChildren(this);
		}
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
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	public static class BlockTagTextElementContext extends ParserRuleContext {
		public TerminalNode TEXT_CONTENT() { return getToken(JavadocParser.TEXT_CONTENT, 0); }
		public TerminalNode NAME() { return getToken(JavadocParser.NAME, 0); }
		public TerminalNode SPACE() { return getToken(JavadocParser.SPACE, 0); }
		public TerminalNode STAR() { return getToken(JavadocParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(JavadocParser.SLASH, 0); }
		public TerminalNode BRACE_OPEN() { return getToken(JavadocParser.BRACE_OPEN, 0); }
		public TerminalNode BRACE_CLOSE() { return getToken(JavadocParser.BRACE_CLOSE, 0); }
		public BlockTagTextElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockTagTextElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBlockTagTextElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBlockTagTextElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBlockTagTextElement(this);
			else return visitor.visitChildren(this);
		}
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
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 6362L) != 0) ) {
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
	public static class InlineTagContext extends ParserRuleContext {
		public TerminalNode INLINE_TAG_START() { return getToken(JavadocParser.INLINE_TAG_START, 0); }
		public InlineTagNameContext inlineTagName() {
			return getRuleContext(InlineTagNameContext.class,0);
		}
		public TerminalNode BRACE_CLOSE() { return getToken(JavadocParser.BRACE_CLOSE, 0); }
		public List<TerminalNode> SPACE() { return getTokens(JavadocParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(JavadocParser.SPACE, i);
		}
		public InlineTagContentContext inlineTagContent() {
			return getRuleContext(InlineTagContentContext.class,0);
		}
		public InlineTagContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineTag; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterInlineTag(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitInlineTag(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitInlineTag(this);
			else return visitor.visitChildren(this);
		}
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
			_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(194);
					match(SPACE);
					}
					} 
				}
				setState(199);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,26,_ctx);
			}
			setState(201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 2270L) != 0) {
				{
				setState(200);
				inlineTagContent();
				}
			}

			setState(203);
			match(BRACE_CLOSE);
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
	public static class InlineTagNameContext extends ParserRuleContext {
		public TerminalNode NAME() { return getToken(JavadocParser.NAME, 0); }
		public InlineTagNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineTagName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterInlineTagName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitInlineTagName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitInlineTagName(this);
			else return visitor.visitChildren(this);
		}
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
	public static class InlineTagContentContext extends ParserRuleContext {
		public List<BraceContentContext> braceContent() {
			return getRuleContexts(BraceContentContext.class);
		}
		public BraceContentContext braceContent(int i) {
			return getRuleContext(BraceContentContext.class,i);
		}
		public InlineTagContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inlineTagContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterInlineTagContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitInlineTagContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitInlineTagContent(this);
			else return visitor.visitChildren(this);
		}
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
			} while ( ((_la) & ~0x3f) == 0 && ((1L << _la) & 2270L) != 0 );
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
	public static class BraceExpressionContext extends ParserRuleContext {
		public TerminalNode BRACE_OPEN() { return getToken(JavadocParser.BRACE_OPEN, 0); }
		public TerminalNode BRACE_CLOSE() { return getToken(JavadocParser.BRACE_CLOSE, 0); }
		public List<BraceContentContext> braceContent() {
			return getRuleContexts(BraceContentContext.class);
		}
		public BraceContentContext braceContent(int i) {
			return getRuleContext(BraceContentContext.class,i);
		}
		public BraceExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_braceExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBraceExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBraceExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBraceExpression(this);
			else return visitor.visitChildren(this);
		}
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
			while (((_la) & ~0x3f) == 0 && ((1L << _la) & 2270L) != 0) {
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
	public static class BraceContentContext extends ParserRuleContext {
		public BraceExpressionContext braceExpression() {
			return getRuleContext(BraceExpressionContext.class,0);
		}
		public List<BraceTextContext> braceText() {
			return getRuleContexts(BraceTextContext.class);
		}
		public BraceTextContext braceText(int i) {
			return getRuleContext(BraceTextContext.class,i);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(JavadocParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(JavadocParser.NEWLINE, i);
		}
		public BraceContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_braceContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBraceContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBraceContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBraceContent(this);
			else return visitor.visitChildren(this);
		}
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
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(226);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
						while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
							if ( _alt==1 ) {
								{
								{
								setState(223);
								match(NEWLINE);
								}
								} 
							}
							setState(228);
							_errHandler.sync(this);
							_alt = getInterpreter().adaptivePredict(_input,30,_ctx);
						}
						setState(229);
						braceText();
						}
						} 
					}
					setState(234);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class BraceTextContext extends ParserRuleContext {
		public TerminalNode TEXT_CONTENT() { return getToken(JavadocParser.TEXT_CONTENT, 0); }
		public TerminalNode NAME() { return getToken(JavadocParser.NAME, 0); }
		public TerminalNode SPACE() { return getToken(JavadocParser.SPACE, 0); }
		public TerminalNode STAR() { return getToken(JavadocParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(JavadocParser.SLASH, 0); }
		public TerminalNode NEWLINE() { return getToken(JavadocParser.NEWLINE, 0); }
		public BraceTextContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_braceText; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).enterBraceText(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavadocParserListener ) ((JavadocParserListener)listener).exitBraceText(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavadocParserVisitor ) return ((JavadocParserVisitor<? extends T>)visitor).visitBraceText(this);
			else return visitor.visitChildren(this);
		}
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
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 222L) != 0) ) {
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
		"\u0004\u0001\f\u00f0\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u00000\b\u0000\n\u0000\f\u0000"+
		"3\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0005\u0000:\b\u0000\n\u0000\f\u0000=\t\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0003\u0000B\b\u0000\u0001\u0001\u0001\u0001\u0005\u0001"+
		"F\b\u0001\n\u0001\f\u0001I\t\u0001\u0001\u0001\u0005\u0001L\b\u0001\n"+
		"\u0001\f\u0001O\t\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0004\u0001"+
		"T\b\u0001\u000b\u0001\f\u0001U\u0001\u0001\u0005\u0001Y\b\u0001\n\u0001"+
		"\f\u0001\\\t\u0001\u0001\u0001\u0001\u0001\u0003\u0001`\b\u0001\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0004\u0003f\b\u0003\u000b"+
		"\u0003\f\u0003g\u0001\u0003\u0001\u0003\u0005\u0003l\b\u0003\n\u0003\f"+
		"\u0003o\t\u0003\u0001\u0004\u0001\u0004\u0005\u0004s\b\u0004\n\u0004\f"+
		"\u0004v\t\u0004\u0001\u0004\u0001\u0004\u0005\u0004z\b\u0004\n\u0004\f"+
		"\u0004}\t\u0004\u0003\u0004\u007f\b\u0004\u0001\u0005\u0003\u0005\u0082"+
		"\b\u0005\u0001\u0005\u0004\u0005\u0085\b\u0005\u000b\u0005\f\u0005\u0086"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005\u008c\b\u0005\n\u0005"+
		"\f\u0005\u008f\t\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007"+
		"\u0003\u0007\u0095\b\u0007\u0001\b\u0001\b\u0001\b\u0004\b\u009a\b\b\u000b"+
		"\b\f\b\u009b\u0001\t\u0001\t\u0001\n\u0004\n\u00a1\b\n\u000b\n\f\n\u00a2"+
		"\u0001\u000b\u0003\u000b\u00a6\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000b\u00ab\b\u000b\u0001\u000b\u0005\u000b\u00ae\b\u000b\n\u000b"+
		"\f\u000b\u00b1\t\u000b\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0003\r"+
		"\u00b8\b\r\u0001\u000e\u0004\u000e\u00bb\b\u000e\u000b\u000e\f\u000e\u00bc"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0005\u0010"+
		"\u00c4\b\u0010\n\u0010\f\u0010\u00c7\t\u0010\u0001\u0010\u0003\u0010\u00ca"+
		"\b\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0004"+
		"\u0012\u00d1\b\u0012\u000b\u0012\f\u0012\u00d2\u0001\u0013\u0001\u0013"+
		"\u0005\u0013\u00d7\b\u0013\n\u0013\f\u0013\u00da\t\u0013\u0001\u0013\u0001"+
		"\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u00e1\b\u0014\n"+
		"\u0014\f\u0014\u00e4\t\u0014\u0001\u0014\u0005\u0014\u00e7\b\u0014\n\u0014"+
		"\f\u0014\u00ea\t\u0014\u0003\u0014\u00ec\b\u0014\u0001\u0015\u0001\u0015"+
		"\u0001\u0015\u0000\u0000\u0016\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*\u0000\u0004\u0001\u0000"+
		"\u0002\u0003\u0004\u0000\u0001\u0001\u0004\u0004\u0006\u0007\u000b\f\u0004"+
		"\u0000\u0001\u0001\u0003\u0004\u0006\u0007\u000b\f\u0002\u0000\u0001\u0004"+
		"\u0006\u0007\u00ff\u0000A\u0001\u0000\u0000\u0000\u0002_\u0001\u0000\u0000"+
		"\u0000\u0004a\u0001\u0000\u0000\u0000\u0006c\u0001\u0000\u0000\u0000\b"+
		"~\u0001\u0000\u0000\u0000\n\u0081\u0001\u0000\u0000\u0000\f\u0090\u0001"+
		"\u0000\u0000\u0000\u000e\u0094\u0001\u0000\u0000\u0000\u0010\u0099\u0001"+
		"\u0000\u0000\u0000\u0012\u009d\u0001\u0000\u0000\u0000\u0014\u00a0\u0001"+
		"\u0000\u0000\u0000\u0016\u00a5\u0001\u0000\u0000\u0000\u0018\u00b2\u0001"+
		"\u0000\u0000\u0000\u001a\u00b7\u0001\u0000\u0000\u0000\u001c\u00ba\u0001"+
		"\u0000\u0000\u0000\u001e\u00be\u0001\u0000\u0000\u0000 \u00c0\u0001\u0000"+
		"\u0000\u0000\"\u00cd\u0001\u0000\u0000\u0000$\u00d0\u0001\u0000\u0000"+
		"\u0000&\u00d4\u0001\u0000\u0000\u0000(\u00eb\u0001\u0000\u0000\u0000*"+
		"\u00ed\u0001\u0000\u0000\u0000,B\u0005\u0000\u0000\u0001-1\u0005\b\u0000"+
		"\u0000.0\u0003\u0004\u0002\u0000/.\u0001\u0000\u0000\u000003\u0001\u0000"+
		"\u0000\u00001/\u0001\u0000\u0000\u000012\u0001\u0000\u0000\u000024\u0001"+
		"\u0000\u0000\u000031\u0001\u0000\u0000\u000045\u0003\u0002\u0001\u0000"+
		"56\u0005\t\u0000\u000067\u0005\u0000\u0000\u00017B\u0001\u0000\u0000\u0000"+
		"8:\u0003\u0004\u0002\u000098\u0001\u0000\u0000\u0000:=\u0001\u0000\u0000"+
		"\u0000;9\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000<>\u0001\u0000"+
		"\u0000\u0000=;\u0001\u0000\u0000\u0000>?\u0003\u0002\u0001\u0000?@\u0005"+
		"\u0000\u0000\u0001@B\u0001\u0000\u0000\u0000A,\u0001\u0000\u0000\u0000"+
		"A-\u0001\u0000\u0000\u0000A;\u0001\u0000\u0000\u0000B\u0001\u0001\u0000"+
		"\u0000\u0000CG\u0003\u0006\u0003\u0000DF\u0003\u0004\u0002\u0000ED\u0001"+
		"\u0000\u0000\u0000FI\u0001\u0000\u0000\u0000GE\u0001\u0000\u0000\u0000"+
		"GH\u0001\u0000\u0000\u0000H`\u0001\u0000\u0000\u0000IG\u0001\u0000\u0000"+
		"\u0000JL\u0003\u0004\u0002\u0000KJ\u0001\u0000\u0000\u0000LO\u0001\u0000"+
		"\u0000\u0000MK\u0001\u0000\u0000\u0000MN\u0001\u0000\u0000\u0000NP\u0001"+
		"\u0000\u0000\u0000OM\u0001\u0000\u0000\u0000P`\u0003\u0014\n\u0000QS\u0003"+
		"\u0006\u0003\u0000RT\u0005\u0002\u0000\u0000SR\u0001\u0000\u0000\u0000"+
		"TU\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000"+
		"\u0000VZ\u0001\u0000\u0000\u0000WY\u0003\u0004\u0002\u0000XW\u0001\u0000"+
		"\u0000\u0000Y\\\u0001\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001"+
		"\u0000\u0000\u0000[]\u0001\u0000\u0000\u0000\\Z\u0001\u0000\u0000\u0000"+
		"]^\u0003\u0014\n\u0000^`\u0001\u0000\u0000\u0000_C\u0001\u0000\u0000\u0000"+
		"_M\u0001\u0000\u0000\u0000_Q\u0001\u0000\u0000\u0000`\u0003\u0001\u0000"+
		"\u0000\u0000ab\u0007\u0000\u0000\u0000b\u0005\u0001\u0000\u0000\u0000"+
		"cm\u0003\b\u0004\u0000df\u0003\u0012\t\u0000ed\u0001\u0000\u0000\u0000"+
		"fg\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000"+
		"\u0000hi\u0001\u0000\u0000\u0000ij\u0003\b\u0004\u0000jl\u0001\u0000\u0000"+
		"\u0000ke\u0001\u0000\u0000\u0000lo\u0001\u0000\u0000\u0000mk\u0001\u0000"+
		"\u0000\u0000mn\u0001\u0000\u0000\u0000n\u0007\u0001\u0000\u0000\u0000"+
		"om\u0001\u0000\u0000\u0000pt\u0003\n\u0005\u0000qs\u0003\u000e\u0007\u0000"+
		"rq\u0001\u0000\u0000\u0000sv\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000"+
		"\u0000tu\u0001\u0000\u0000\u0000u\u007f\u0001\u0000\u0000\u0000vt\u0001"+
		"\u0000\u0000\u0000w{\u0003 \u0010\u0000xz\u0003\u000e\u0007\u0000yx\u0001"+
		"\u0000\u0000\u0000z}\u0001\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000"+
		"{|\u0001\u0000\u0000\u0000|\u007f\u0001\u0000\u0000\u0000}{\u0001\u0000"+
		"\u0000\u0000~p\u0001\u0000\u0000\u0000~w\u0001\u0000\u0000\u0000\u007f"+
		"\t\u0001\u0000\u0000\u0000\u0080\u0082\u0005\u0003\u0000\u0000\u0081\u0080"+
		"\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0084"+
		"\u0001\u0000\u0000\u0000\u0083\u0085\u0003\f\u0006\u0000\u0084\u0083\u0001"+
		"\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0084\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u008d\u0001"+
		"\u0000\u0000\u0000\u0088\u008c\u0003\f\u0006\u0000\u0089\u008c\u0005\u0003"+
		"\u0000\u0000\u008a\u008c\u0005\u0005\u0000\u0000\u008b\u0088\u0001\u0000"+
		"\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008b\u008a\u0001\u0000"+
		"\u0000\u0000\u008c\u008f\u0001\u0000\u0000\u0000\u008d\u008b\u0001\u0000"+
		"\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u000b\u0001\u0000"+
		"\u0000\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u0090\u0091\u0007\u0001"+
		"\u0000\u0000\u0091\r\u0001\u0000\u0000\u0000\u0092\u0095\u0003 \u0010"+
		"\u0000\u0093\u0095\u0003\u0010\b\u0000\u0094\u0092\u0001\u0000\u0000\u0000"+
		"\u0094\u0093\u0001\u0000\u0000\u0000\u0095\u000f\u0001\u0000\u0000\u0000"+
		"\u0096\u009a\u0003\f\u0006\u0000\u0097\u009a\u0005\u0003\u0000\u0000\u0098"+
		"\u009a\u0005\u0005\u0000\u0000\u0099\u0096\u0001\u0000\u0000\u0000\u0099"+
		"\u0097\u0001\u0000\u0000\u0000\u0099\u0098\u0001\u0000\u0000\u0000\u009a"+
		"\u009b\u0001\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009b"+
		"\u009c\u0001\u0000\u0000\u0000\u009c\u0011\u0001\u0000\u0000\u0000\u009d"+
		"\u009e\u0005\u0002\u0000\u0000\u009e\u0013\u0001\u0000\u0000\u0000\u009f"+
		"\u00a1\u0003\u0016\u000b\u0000\u00a0\u009f\u0001\u0000\u0000\u0000\u00a1"+
		"\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a2"+
		"\u00a3\u0001\u0000\u0000\u0000\u00a3\u0015\u0001\u0000\u0000\u0000\u00a4"+
		"\u00a6\u0005\u0003\u0000\u0000\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a5"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a8\u0005\u0005\u0000\u0000\u00a8\u00aa\u0003\u0018\f\u0000\u00a9\u00ab"+
		"\u0005\u0003\u0000\u0000\u00aa\u00a9\u0001\u0000\u0000\u0000\u00aa\u00ab"+
		"\u0001\u0000\u0000\u0000\u00ab\u00af\u0001\u0000\u0000\u0000\u00ac\u00ae"+
		"\u0003\u001a\r\u0000\u00ad\u00ac\u0001\u0000\u0000\u0000\u00ae\u00b1\u0001"+
		"\u0000\u0000\u0000\u00af\u00ad\u0001\u0000\u0000\u0000\u00af\u00b0\u0001"+
		"\u0000\u0000\u0000\u00b0\u0017\u0001\u0000\u0000\u0000\u00b1\u00af\u0001"+
		"\u0000\u0000\u0000\u00b2\u00b3\u0005\u0001\u0000\u0000\u00b3\u0019\u0001"+
		"\u0000\u0000\u0000\u00b4\u00b8\u0003\u001c\u000e\u0000\u00b5\u00b8\u0003"+
		" \u0010\u0000\u00b6\u00b8\u0005\u0002\u0000\u0000\u00b7\u00b4\u0001\u0000"+
		"\u0000\u0000\u00b7\u00b5\u0001\u0000\u0000\u0000\u00b7\u00b6\u0001\u0000"+
		"\u0000\u0000\u00b8\u001b\u0001\u0000\u0000\u0000\u00b9\u00bb\u0003\u001e"+
		"\u000f\u0000\u00ba\u00b9\u0001\u0000\u0000\u0000\u00bb\u00bc\u0001\u0000"+
		"\u0000\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000"+
		"\u0000\u0000\u00bd\u001d\u0001\u0000\u0000\u0000\u00be\u00bf\u0007\u0002"+
		"\u0000\u0000\u00bf\u001f\u0001\u0000\u0000\u0000\u00c0\u00c1\u0005\n\u0000"+
		"\u0000\u00c1\u00c5\u0003\"\u0011\u0000\u00c2\u00c4\u0005\u0003\u0000\u0000"+
		"\u00c3\u00c2\u0001\u0000\u0000\u0000\u00c4\u00c7\u0001\u0000\u0000\u0000"+
		"\u00c5\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000\u0000"+
		"\u00c6\u00c9\u0001\u0000\u0000\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000"+
		"\u00c8\u00ca\u0003$\u0012\u0000\u00c9\u00c8\u0001\u0000\u0000\u0000\u00c9"+
		"\u00ca\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000\u0000\u0000\u00cb"+
		"\u00cc\u0005\f\u0000\u0000\u00cc!\u0001\u0000\u0000\u0000\u00cd\u00ce"+
		"\u0005\u0001\u0000\u0000\u00ce#\u0001\u0000\u0000\u0000\u00cf\u00d1\u0003"+
		"(\u0014\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000"+
		"\u0000\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000"+
		"\u0000\u0000\u00d3%\u0001\u0000\u0000\u0000\u00d4\u00d8\u0005\u000b\u0000"+
		"\u0000\u00d5\u00d7\u0003(\u0014\u0000\u00d6\u00d5\u0001\u0000\u0000\u0000"+
		"\u00d7\u00da\u0001\u0000\u0000\u0000\u00d8\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u00db\u0001\u0000\u0000\u0000"+
		"\u00da\u00d8\u0001\u0000\u0000\u0000\u00db\u00dc\u0005\f\u0000\u0000\u00dc"+
		"\'\u0001\u0000\u0000\u0000\u00dd\u00ec\u0003&\u0013\u0000\u00de\u00e8"+
		"\u0003*\u0015\u0000\u00df\u00e1\u0005\u0002\u0000\u0000\u00e0\u00df\u0001"+
		"\u0000\u0000\u0000\u00e1\u00e4\u0001\u0000\u0000\u0000\u00e2\u00e0\u0001"+
		"\u0000\u0000\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e5\u0001"+
		"\u0000\u0000\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e5\u00e7\u0003"+
		"*\u0015\u0000\u00e6\u00e2\u0001\u0000\u0000\u0000\u00e7\u00ea\u0001\u0000"+
		"\u0000\u0000\u00e8\u00e6\u0001\u0000\u0000\u0000\u00e8\u00e9\u0001\u0000"+
		"\u0000\u0000\u00e9\u00ec\u0001\u0000\u0000\u0000\u00ea\u00e8\u0001\u0000"+
		"\u0000\u0000\u00eb\u00dd\u0001\u0000\u0000\u0000\u00eb\u00de\u0001\u0000"+
		"\u0000\u0000\u00ec)\u0001\u0000\u0000\u0000\u00ed\u00ee\u0007\u0003\u0000"+
		"\u0000\u00ee+\u0001\u0000\u0000\u0000!1;AGMUZ_gmt{~\u0081\u0086\u008b"+
		"\u008d\u0094\u0099\u009b\u00a2\u00a5\u00aa\u00af\u00b7\u00bc\u00c5\u00c9"+
		"\u00d2\u00d8\u00e2\u00e8\u00eb";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}