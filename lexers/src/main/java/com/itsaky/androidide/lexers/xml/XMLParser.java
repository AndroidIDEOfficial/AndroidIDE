// Generated from XMLParser.g4 by ANTLR 4.9.3
package com.itsaky.androidide.lexers.xml;

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
public class XMLParser extends Parser {
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
  public static final int RULE_document = 0,
      RULE_prolog = 1,
      RULE_content = 2,
      RULE_element = 3,
      RULE_nonEmptyClosingElement = 4,
      RULE_emptyClosingElement = 5,
      RULE_reference = 6,
      RULE_attribute = 7,
      RULE_prefix = 8,
      RULE_chardata = 9,
      RULE_misc = 10;
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  public static final String _serializedATN =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\34p\4\2\t\2\4\3\t"
          + "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"
          + "\f\t\f\3\2\5\2\32\n\2\3\2\7\2\35\n\2\f\2\16\2 \13\2\3\2\3\2\7\2$\n\2\f"
          + "\2\16\2\'\13\2\3\3\3\3\7\3+\n\3\f\3\16\3.\13\3\3\3\3\3\3\4\5\4\63\n\4"
          + "\3\4\3\4\3\4\3\4\3\4\5\4:\n\4\3\4\5\4=\n\4\7\4?\n\4\f\4\16\4B\13\4\3\5"
          + "\3\5\5\5F\n\5\3\6\3\6\3\6\7\6K\n\6\f\6\16\6N\13\6\3\6\3\6\3\6\3\6\3\6"
          + "\3\6\3\7\3\7\3\7\7\7Y\n\7\f\7\16\7\\\13\7\3\7\3\7\3\b\3\b\3\t\5\tc\n\t"
          + "\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\f\2\2\r\2\4\6\b\n\f\16"
          + "\20\22\24\26\2\5\3\2\13\f\4\2\r\r\21\21\5\2\b\b\r\r\34\34\2s\2\31\3\2"
          + "\2\2\4(\3\2\2\2\6\62\3\2\2\2\bE\3\2\2\2\nG\3\2\2\2\fU\3\2\2\2\16_\3\2"
          + "\2\2\20b\3\2\2\2\22h\3\2\2\2\24k\3\2\2\2\26m\3\2\2\2\30\32\5\4\3\2\31"
          + "\30\3\2\2\2\31\32\3\2\2\2\32\36\3\2\2\2\33\35\5\26\f\2\34\33\3\2\2\2\35"
          + " \3\2\2\2\36\34\3\2\2\2\36\37\3\2\2\2\37!\3\2\2\2 \36\3\2\2\2!%\5\b\5"
          + "\2\"$\5\26\f\2#\"\3\2\2\2$\'\3\2\2\2%#\3\2\2\2%&\3\2\2\2&\3\3\2\2\2\'"
          + "%\3\2\2\2(,\7\20\2\2)+\5\20\t\2*)\3\2\2\2+.\3\2\2\2,*\3\2\2\2,-\3\2\2"
          + "\2-/\3\2\2\2.,\3\2\2\2/\60\7\25\2\2\60\5\3\2\2\2\61\63\5\24\13\2\62\61"
          + "\3\2\2\2\62\63\3\2\2\2\63@\3\2\2\2\64:\5\b\5\2\65:\5\16\b\2\66:\7\t\2"
          + "\2\67:\7\34\2\28:\7\b\2\29\64\3\2\2\29\65\3\2\2\29\66\3\2\2\29\67\3\2"
          + "\2\298\3\2\2\2:<\3\2\2\2;=\5\24\13\2<;\3\2\2\2<=\3\2\2\2=?\3\2\2\2>9\3"
          + "\2\2\2?B\3\2\2\2@>\3\2\2\2@A\3\2\2\2A\7\3\2\2\2B@\3\2\2\2CF\5\n\6\2DF"
          + "\5\f\7\2EC\3\2\2\2ED\3\2\2\2F\t\3\2\2\2GH\7\16\2\2HL\7\32\2\2IK\5\20\t"
          + "\2JI\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2MO\3\2\2\2NL\3\2\2\2OP\7\24"
          + "\2\2PQ\5\6\4\2QR\7\17\2\2RS\7\32\2\2ST\7\24\2\2T\13\3\2\2\2UV\7\16\2\2"
          + "VZ\7\32\2\2WY\5\20\t\2XW\3\2\2\2Y\\\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[]\3\2"
          + "\2\2\\Z\3\2\2\2]^\7\26\2\2^\r\3\2\2\2_`\t\2\2\2`\17\3\2\2\2ac\5\22\n\2"
          + "ba\3\2\2\2bc\3\2\2\2cd\3\2\2\2de\7\32\2\2ef\7\30\2\2fg\7\31\2\2g\21\3"
          + "\2\2\2hi\7\32\2\2ij\7\3\2\2j\23\3\2\2\2kl\t\3\2\2l\25\3\2\2\2mn\t\4\2"
          + "\2n\27\3\2\2\2\16\31\36%,\629<@ELZb";
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

  public XMLParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  private static String[] makeRuleNames() {
    return new String[] {
      "document", "prolog", "content", "element", "nonEmptyClosingElement",
      "emptyClosingElement", "reference", "attribute", "prefix", "chardata",
      "misc"
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
    return "XMLParser.g4";
  }

  @Override
  public ATN getATN() {
    return _ATN;
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
        if (_la == XMLDeclOpen) {
          {
            setState(22);
            prolog();
          }
        }

        setState(28);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << COMMENT) | (1L << SEA_WS) | (1L << PI))) != 0)) {
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
        while ((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << COMMENT) | (1L << SEA_WS) | (1L << PI))) != 0)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Name) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == SEA_WS || _la == TEXT) {
          {
            setState(47);
            chardata();
          }
        }

        setState(62);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << COMMENT)
                        | (1L << CDATA)
                        | (1L << EntityRef)
                        | (1L << CharRef)
                        | (1L << OPEN)
                        | (1L << PI)))
                != 0)) {
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
              if (_la == SEA_WS || _la == TEXT) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ElementContext element() throws RecognitionException {
    ElementContext _localctx = new ElementContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_element);
    try {
      setState(67);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Name) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Name) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!(_la == EntityRef || _la == CharRef)) {
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

  public final AttributeContext attribute() throws RecognitionException {
    AttributeContext _localctx = new AttributeContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_attribute);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(96);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 11, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!(_la == SEA_WS || _la == TEXT)) {
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

  public final MiscContext misc() throws RecognitionException {
    MiscContext _localctx = new MiscContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_misc);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(107);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << COMMENT) | (1L << SEA_WS) | (1L << PI))) != 0))) {
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

  public static class DocumentContext extends ParserRuleContext {
    public DocumentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ElementContext element() {
      return getRuleContext(ElementContext.class, 0);
    }

    public PrologContext prolog() {
      return getRuleContext(PrologContext.class, 0);
    }

    public List<MiscContext> misc() {
      return getRuleContexts(MiscContext.class);
    }

    public MiscContext misc(int i) {
      return getRuleContext(MiscContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_document;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitDocument(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterDocument(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitDocument(this);
    }
  }

  public static class PrologContext extends ParserRuleContext {
    public PrologContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode XMLDeclOpen() {
      return getToken(XMLParser.XMLDeclOpen, 0);
    }

    public TerminalNode SPECIAL_CLOSE() {
      return getToken(XMLParser.SPECIAL_CLOSE, 0);
    }

    public List<AttributeContext> attribute() {
      return getRuleContexts(AttributeContext.class);
    }

    public AttributeContext attribute(int i) {
      return getRuleContext(AttributeContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_prolog;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitProlog(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterProlog(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitProlog(this);
    }
  }

  public static class ContentContext extends ParserRuleContext {
    public ContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ChardataContext> chardata() {
      return getRuleContexts(ChardataContext.class);
    }

    public ChardataContext chardata(int i) {
      return getRuleContext(ChardataContext.class, i);
    }

    public List<ElementContext> element() {
      return getRuleContexts(ElementContext.class);
    }

    public ElementContext element(int i) {
      return getRuleContext(ElementContext.class, i);
    }

    public List<ReferenceContext> reference() {
      return getRuleContexts(ReferenceContext.class);
    }

    public ReferenceContext reference(int i) {
      return getRuleContext(ReferenceContext.class, i);
    }

    public List<TerminalNode> CDATA() {
      return getTokens(XMLParser.CDATA);
    }

    public TerminalNode CDATA(int i) {
      return getToken(XMLParser.CDATA, i);
    }

    public List<TerminalNode> PI() {
      return getTokens(XMLParser.PI);
    }

    public TerminalNode PI(int i) {
      return getToken(XMLParser.PI, i);
    }

    public List<TerminalNode> COMMENT() {
      return getTokens(XMLParser.COMMENT);
    }

    public TerminalNode COMMENT(int i) {
      return getToken(XMLParser.COMMENT, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_content;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitContent(this);
    }
  }

  public static class ElementContext extends ParserRuleContext {
    public ElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public NonEmptyClosingElementContext nonEmptyClosingElement() {
      return getRuleContext(NonEmptyClosingElementContext.class, 0);
    }

    public EmptyClosingElementContext emptyClosingElement() {
      return getRuleContext(EmptyClosingElementContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_element;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitElement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterElement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitElement(this);
    }
  }

  public static class NonEmptyClosingElementContext extends ParserRuleContext {
    public NonEmptyClosingElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode OPEN() {
      return getToken(XMLParser.OPEN, 0);
    }

    public List<TerminalNode> Name() {
      return getTokens(XMLParser.Name);
    }

    public TerminalNode Name(int i) {
      return getToken(XMLParser.Name, i);
    }

    public List<TerminalNode> CLOSE() {
      return getTokens(XMLParser.CLOSE);
    }

    public TerminalNode CLOSE(int i) {
      return getToken(XMLParser.CLOSE, i);
    }

    public ContentContext content() {
      return getRuleContext(ContentContext.class, 0);
    }

    public TerminalNode OPEN_SLASH() {
      return getToken(XMLParser.OPEN_SLASH, 0);
    }

    public List<AttributeContext> attribute() {
      return getRuleContexts(AttributeContext.class);
    }

    public AttributeContext attribute(int i) {
      return getRuleContext(AttributeContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_nonEmptyClosingElement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitNonEmptyClosingElement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener)
        ((XMLParserListener) listener).enterNonEmptyClosingElement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener)
        ((XMLParserListener) listener).exitNonEmptyClosingElement(this);
    }
  }

  public static class EmptyClosingElementContext extends ParserRuleContext {
    public EmptyClosingElementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode OPEN() {
      return getToken(XMLParser.OPEN, 0);
    }

    public TerminalNode Name() {
      return getToken(XMLParser.Name, 0);
    }

    public TerminalNode SLASH_CLOSE() {
      return getToken(XMLParser.SLASH_CLOSE, 0);
    }

    public List<AttributeContext> attribute() {
      return getRuleContexts(AttributeContext.class);
    }

    public AttributeContext attribute(int i) {
      return getRuleContext(AttributeContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_emptyClosingElement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitEmptyClosingElement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener)
        ((XMLParserListener) listener).enterEmptyClosingElement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener)
        ((XMLParserListener) listener).exitEmptyClosingElement(this);
    }
  }

  public static class ReferenceContext extends ParserRuleContext {
    public ReferenceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode EntityRef() {
      return getToken(XMLParser.EntityRef, 0);
    }

    public TerminalNode CharRef() {
      return getToken(XMLParser.CharRef, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_reference;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitReference(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener)
        ((XMLParserListener) listener).enterReference(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitReference(this);
    }
  }

  public static class AttributeContext extends ParserRuleContext {
    public AttributeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Name() {
      return getToken(XMLParser.Name, 0);
    }

    public TerminalNode EQUALS() {
      return getToken(XMLParser.EQUALS, 0);
    }

    public TerminalNode STRING() {
      return getToken(XMLParser.STRING, 0);
    }

    public PrefixContext prefix() {
      return getRuleContext(PrefixContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attribute;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitAttribute(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener)
        ((XMLParserListener) listener).enterAttribute(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitAttribute(this);
    }
  }

  public static class PrefixContext extends ParserRuleContext {
    public PrefixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Name() {
      return getToken(XMLParser.Name, 0);
    }

    public TerminalNode COLON() {
      return getToken(XMLParser.COLON, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_prefix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitPrefix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterPrefix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitPrefix(this);
    }
  }

  public static class ChardataContext extends ParserRuleContext {
    public ChardataContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TEXT() {
      return getToken(XMLParser.TEXT, 0);
    }

    public TerminalNode SEA_WS() {
      return getToken(XMLParser.SEA_WS, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_chardata;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitChardata(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterChardata(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitChardata(this);
    }
  }

  public static class MiscContext extends ParserRuleContext {
    public MiscContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode COMMENT() {
      return getToken(XMLParser.COMMENT, 0);
    }

    public TerminalNode PI() {
      return getToken(XMLParser.PI, 0);
    }

    public TerminalNode SEA_WS() {
      return getToken(XMLParser.SEA_WS, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_misc;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof XMLParserVisitor)
        return ((XMLParserVisitor<? extends T>) visitor).visitMisc(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).enterMisc(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof XMLParserListener) ((XMLParserListener) listener).exitMisc(this);
    }
  }
}
