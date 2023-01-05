// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.cpp;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class CPP14Parser extends Parser {
	static { RuntimeMetaData.checkVersion("4.11.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IntegerLiteral=1, CharacterLiteral=2, FloatingLiteral=3, StringLiteral=4, 
		BooleanLiteral=5, PointerLiteral=6, UserDefinedLiteral=7, MultiLineMacro=8, 
		Directive=9, Alignas=10, Alignof=11, Asm=12, Auto=13, Bool=14, Break=15, 
		Case=16, Catch=17, Char=18, Char16=19, Char32=20, Class=21, Const=22, 
		Constexpr=23, Const_cast=24, Continue=25, Decltype=26, Default=27, Delete=28, 
		Do=29, Double=30, Dynamic_cast=31, Else=32, Enum=33, Explicit=34, Export=35, 
		Extern=36, False_=37, Final=38, Float=39, For=40, Friend=41, Goto=42, 
		If=43, Inline=44, Int=45, Long=46, Mutable=47, Namespace=48, New=49, Noexcept=50, 
		Nullptr=51, Operator=52, Override=53, Private=54, Protected=55, Public=56, 
		Register=57, Reinterpret_cast=58, Return=59, Short=60, Signed=61, Sizeof=62, 
		Static=63, Static_assert=64, Static_cast=65, Struct=66, Switch=67, Template=68, 
		This=69, Thread_local=70, Throw=71, True_=72, Try=73, Typedef=74, Typeid_=75, 
		Typename_=76, Union=77, Unsigned=78, Using=79, Virtual=80, Void=81, Volatile=82, 
		Wchar=83, While=84, LeftParen=85, RightParen=86, LeftBracket=87, RightBracket=88, 
		LeftBrace=89, RightBrace=90, Plus=91, Minus=92, Star=93, Div=94, Mod=95, 
		Caret=96, And=97, Or=98, Tilde=99, Not=100, Assign=101, Less=102, Greater=103, 
		PlusAssign=104, MinusAssign=105, StarAssign=106, DivAssign=107, ModAssign=108, 
		XorAssign=109, AndAssign=110, OrAssign=111, LeftShiftAssign=112, RightShiftAssign=113, 
		Equal=114, NotEqual=115, LessEqual=116, GreaterEqual=117, AndAnd=118, 
		OrOr=119, PlusPlus=120, MinusMinus=121, Comma=122, ArrowStar=123, Arrow=124, 
		Question=125, Colon=126, Doublecolon=127, Semi=128, Dot=129, DotStar=130, 
		Ellipsis=131, Identifier=132, DecimalLiteral=133, OctalLiteral=134, HexadecimalLiteral=135, 
		BinaryLiteral=136, Integersuffix=137, UserDefinedIntegerLiteral=138, UserDefinedFloatingLiteral=139, 
		UserDefinedStringLiteral=140, UserDefinedCharacterLiteral=141, Whitespace=142, 
		Newline=143, BlockComment=144, LineComment=145;
	public static final int
		RULE_translationUnit = 0, RULE_primaryExpression = 1, RULE_idExpression = 2, 
		RULE_unqualifiedId = 3, RULE_qualifiedId = 4, RULE_nestedNameSpecifier = 5, 
		RULE_lambdaExpression = 6, RULE_lambdaIntroducer = 7, RULE_lambdaCapture = 8, 
		RULE_captureDefault = 9, RULE_captureList = 10, RULE_capture = 11, RULE_simpleCapture = 12, 
		RULE_initcapture = 13, RULE_lambdaDeclarator = 14, RULE_postfixExpression = 15, 
		RULE_typeIdOfTheTypeId = 16, RULE_expressionList = 17, RULE_pseudoDestructorName = 18, 
		RULE_unaryExpression = 19, RULE_unaryOperator = 20, RULE_newExpression = 21, 
		RULE_newPlacement = 22, RULE_newTypeId = 23, RULE_newDeclarator = 24, 
		RULE_noPointerNewDeclarator = 25, RULE_newInitializer = 26, RULE_deleteExpression = 27, 
		RULE_noExceptExpression = 28, RULE_castExpression = 29, RULE_pointerMemberExpression = 30, 
		RULE_multiplicativeExpression = 31, RULE_additiveExpression = 32, RULE_shiftExpression = 33, 
		RULE_shiftOperator = 34, RULE_relationalExpression = 35, RULE_equalityExpression = 36, 
		RULE_andExpression = 37, RULE_exclusiveOrExpression = 38, RULE_inclusiveOrExpression = 39, 
		RULE_logicalAndExpression = 40, RULE_logicalOrExpression = 41, RULE_conditionalExpression = 42, 
		RULE_assignmentExpression = 43, RULE_assignmentOperator = 44, RULE_expression = 45, 
		RULE_constantExpression = 46, RULE_statement = 47, RULE_labeledStatement = 48, 
		RULE_expressionStatement = 49, RULE_compoundStatement = 50, RULE_statementSeq = 51, 
		RULE_selectionStatement = 52, RULE_condition = 53, RULE_iterationStatement = 54, 
		RULE_forInitStatement = 55, RULE_forRangeDeclaration = 56, RULE_forRangeInitializer = 57, 
		RULE_jumpStatement = 58, RULE_declarationStatement = 59, RULE_declarationseq = 60, 
		RULE_declaration = 61, RULE_blockDeclaration = 62, RULE_aliasDeclaration = 63, 
		RULE_simpleDeclaration = 64, RULE_staticAssertDeclaration = 65, RULE_emptyDeclaration = 66, 
		RULE_attributeDeclaration = 67, RULE_declSpecifier = 68, RULE_declSpecifierSeq = 69, 
		RULE_storageClassSpecifier = 70, RULE_functionSpecifier = 71, RULE_typedefName = 72, 
		RULE_typeSpecifier = 73, RULE_trailingTypeSpecifier = 74, RULE_typeSpecifierSeq = 75, 
		RULE_trailingTypeSpecifierSeq = 76, RULE_simpleTypeLengthModifier = 77, 
		RULE_simpleTypeSignednessModifier = 78, RULE_simpleTypeSpecifier = 79, 
		RULE_theTypeName = 80, RULE_decltypeSpecifier = 81, RULE_elaboratedTypeSpecifier = 82, 
		RULE_enumName = 83, RULE_enumSpecifier = 84, RULE_enumHead = 85, RULE_opaqueEnumDeclaration = 86, 
		RULE_enumkey = 87, RULE_enumbase = 88, RULE_enumeratorList = 89, RULE_enumeratorDefinition = 90, 
		RULE_enumerator = 91, RULE_namespaceName = 92, RULE_originalNamespaceName = 93, 
		RULE_namespaceDefinition = 94, RULE_namespaceAlias = 95, RULE_namespaceAliasDefinition = 96, 
		RULE_qualifiednamespacespecifier = 97, RULE_usingDeclaration = 98, RULE_usingDirective = 99, 
		RULE_asmDefinition = 100, RULE_linkageSpecification = 101, RULE_attributeSpecifierSeq = 102, 
		RULE_attributeSpecifier = 103, RULE_alignmentspecifier = 104, RULE_attributeList = 105, 
		RULE_attribute = 106, RULE_attributeNamespace = 107, RULE_attributeArgumentClause = 108, 
		RULE_balancedTokenSeq = 109, RULE_balancedtoken = 110, RULE_initDeclaratorList = 111, 
		RULE_initDeclarator = 112, RULE_declarator = 113, RULE_pointerDeclarator = 114, 
		RULE_noPointerDeclarator = 115, RULE_parametersAndQualifiers = 116, RULE_trailingReturnType = 117, 
		RULE_pointerOperator = 118, RULE_cvqualifierseq = 119, RULE_cvQualifier = 120, 
		RULE_refqualifier = 121, RULE_declaratorid = 122, RULE_theTypeId = 123, 
		RULE_abstractDeclarator = 124, RULE_pointerAbstractDeclarator = 125, RULE_noPointerAbstractDeclarator = 126, 
		RULE_abstractPackDeclarator = 127, RULE_noPointerAbstractPackDeclarator = 128, 
		RULE_parameterDeclarationClause = 129, RULE_parameterDeclarationList = 130, 
		RULE_parameterDeclaration = 131, RULE_functionDefinition = 132, RULE_functionBody = 133, 
		RULE_initializer = 134, RULE_braceOrEqualInitializer = 135, RULE_initializerClause = 136, 
		RULE_initializerList = 137, RULE_bracedInitList = 138, RULE_className = 139, 
		RULE_classSpecifier = 140, RULE_classHead = 141, RULE_classHeadName = 142, 
		RULE_classVirtSpecifier = 143, RULE_classKey = 144, RULE_memberSpecification = 145, 
		RULE_memberdeclaration = 146, RULE_memberDeclaratorList = 147, RULE_memberDeclarator = 148, 
		RULE_virtualSpecifierSeq = 149, RULE_virtualSpecifier = 150, RULE_pureSpecifier = 151, 
		RULE_baseClause = 152, RULE_baseSpecifierList = 153, RULE_baseSpecifier = 154, 
		RULE_classOrDeclType = 155, RULE_baseTypeSpecifier = 156, RULE_accessSpecifier = 157, 
		RULE_conversionFunctionId = 158, RULE_conversionTypeId = 159, RULE_conversionDeclarator = 160, 
		RULE_constructorInitializer = 161, RULE_memInitializerList = 162, RULE_memInitializer = 163, 
		RULE_meminitializerid = 164, RULE_operatorFunctionId = 165, RULE_literalOperatorId = 166, 
		RULE_templateDeclaration = 167, RULE_templateparameterList = 168, RULE_templateParameter = 169, 
		RULE_typeParameter = 170, RULE_simpleTemplateId = 171, RULE_templateId = 172, 
		RULE_templateName = 173, RULE_templateArgumentList = 174, RULE_templateArgument = 175, 
		RULE_typeNameSpecifier = 176, RULE_explicitInstantiation = 177, RULE_explicitSpecialization = 178, 
		RULE_tryBlock = 179, RULE_functionTryBlock = 180, RULE_handlerSeq = 181, 
		RULE_handler = 182, RULE_exceptionDeclaration = 183, RULE_throwExpression = 184, 
		RULE_exceptionSpecification = 185, RULE_dynamicExceptionSpecification = 186, 
		RULE_typeIdList = 187, RULE_noeExceptSpecification = 188, RULE_theOperator = 189, 
		RULE_literal = 190;
	private static String[] makeRuleNames() {
		return new String[] {
			"translationUnit", "primaryExpression", "idExpression", "unqualifiedId", 
			"qualifiedId", "nestedNameSpecifier", "lambdaExpression", "lambdaIntroducer", 
			"lambdaCapture", "captureDefault", "captureList", "capture", "simpleCapture", 
			"initcapture", "lambdaDeclarator", "postfixExpression", "typeIdOfTheTypeId", 
			"expressionList", "pseudoDestructorName", "unaryExpression", "unaryOperator", 
			"newExpression", "newPlacement", "newTypeId", "newDeclarator", "noPointerNewDeclarator", 
			"newInitializer", "deleteExpression", "noExceptExpression", "castExpression", 
			"pointerMemberExpression", "multiplicativeExpression", "additiveExpression", 
			"shiftExpression", "shiftOperator", "relationalExpression", "equalityExpression", 
			"andExpression", "exclusiveOrExpression", "inclusiveOrExpression", "logicalAndExpression", 
			"logicalOrExpression", "conditionalExpression", "assignmentExpression", 
			"assignmentOperator", "expression", "constantExpression", "statement", 
			"labeledStatement", "expressionStatement", "compoundStatement", "statementSeq", 
			"selectionStatement", "condition", "iterationStatement", "forInitStatement", 
			"forRangeDeclaration", "forRangeInitializer", "jumpStatement", "declarationStatement", 
			"declarationseq", "declaration", "blockDeclaration", "aliasDeclaration", 
			"simpleDeclaration", "staticAssertDeclaration", "emptyDeclaration", "attributeDeclaration", 
			"declSpecifier", "declSpecifierSeq", "storageClassSpecifier", "functionSpecifier", 
			"typedefName", "typeSpecifier", "trailingTypeSpecifier", "typeSpecifierSeq", 
			"trailingTypeSpecifierSeq", "simpleTypeLengthModifier", "simpleTypeSignednessModifier", 
			"simpleTypeSpecifier", "theTypeName", "decltypeSpecifier", "elaboratedTypeSpecifier", 
			"enumName", "enumSpecifier", "enumHead", "opaqueEnumDeclaration", "enumkey", 
			"enumbase", "enumeratorList", "enumeratorDefinition", "enumerator", "namespaceName", 
			"originalNamespaceName", "namespaceDefinition", "namespaceAlias", "namespaceAliasDefinition", 
			"qualifiednamespacespecifier", "usingDeclaration", "usingDirective", 
			"asmDefinition", "linkageSpecification", "attributeSpecifierSeq", "attributeSpecifier", 
			"alignmentspecifier", "attributeList", "attribute", "attributeNamespace", 
			"attributeArgumentClause", "balancedTokenSeq", "balancedtoken", "initDeclaratorList", 
			"initDeclarator", "declarator", "pointerDeclarator", "noPointerDeclarator", 
			"parametersAndQualifiers", "trailingReturnType", "pointerOperator", "cvqualifierseq", 
			"cvQualifier", "refqualifier", "declaratorid", "theTypeId", "abstractDeclarator", 
			"pointerAbstractDeclarator", "noPointerAbstractDeclarator", "abstractPackDeclarator", 
			"noPointerAbstractPackDeclarator", "parameterDeclarationClause", "parameterDeclarationList", 
			"parameterDeclaration", "functionDefinition", "functionBody", "initializer", 
			"braceOrEqualInitializer", "initializerClause", "initializerList", "bracedInitList", 
			"className", "classSpecifier", "classHead", "classHeadName", "classVirtSpecifier", 
			"classKey", "memberSpecification", "memberdeclaration", "memberDeclaratorList", 
			"memberDeclarator", "virtualSpecifierSeq", "virtualSpecifier", "pureSpecifier", 
			"baseClause", "baseSpecifierList", "baseSpecifier", "classOrDeclType", 
			"baseTypeSpecifier", "accessSpecifier", "conversionFunctionId", "conversionTypeId", 
			"conversionDeclarator", "constructorInitializer", "memInitializerList", 
			"memInitializer", "meminitializerid", "operatorFunctionId", "literalOperatorId", 
			"templateDeclaration", "templateparameterList", "templateParameter", 
			"typeParameter", "simpleTemplateId", "templateId", "templateName", "templateArgumentList", 
			"templateArgument", "typeNameSpecifier", "explicitInstantiation", "explicitSpecialization", 
			"tryBlock", "functionTryBlock", "handlerSeq", "handler", "exceptionDeclaration", 
			"throwExpression", "exceptionSpecification", "dynamicExceptionSpecification", 
			"typeIdList", "noeExceptSpecification", "theOperator", "literal"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "'alignas'", 
			"'alignof'", "'asm'", "'auto'", "'bool'", "'break'", "'case'", "'catch'", 
			"'char'", "'char16_t'", "'char32_t'", "'class'", "'const'", "'constexpr'", 
			"'const_cast'", "'continue'", "'decltype'", "'default'", "'delete'", 
			"'do'", "'double'", "'dynamic_cast'", "'else'", "'enum'", "'explicit'", 
			"'export'", "'extern'", "'false'", "'final'", "'float'", "'for'", "'friend'", 
			"'goto'", "'if'", "'inline'", "'int'", "'long'", "'mutable'", "'namespace'", 
			"'new'", "'noexcept'", "'nullptr'", "'operator'", "'override'", "'private'", 
			"'protected'", "'public'", "'register'", "'reinterpret_cast'", "'return'", 
			"'short'", "'signed'", "'sizeof'", "'static'", "'static_assert'", "'static_cast'", 
			"'struct'", "'switch'", "'template'", "'this'", "'thread_local'", "'throw'", 
			"'true'", "'try'", "'typedef'", "'typeid'", "'typename'", "'union'", 
			"'unsigned'", "'using'", "'virtual'", "'void'", "'volatile'", "'wchar_t'", 
			"'while'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'+'", "'-'", "'*'", 
			"'/'", "'%'", "'^'", "'&'", "'|'", "'~'", null, "'='", "'<'", "'>'", 
			"'+='", "'-='", "'*='", "'/='", "'%='", "'^='", "'&='", "'|='", "'<<='", 
			"'>>='", "'=='", "'!='", "'<='", "'>='", null, null, "'++'", "'--'", 
			"','", "'->*'", "'->'", "'?'", "':'", "'::'", "';'", "'.'", "'.*'", "'...'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IntegerLiteral", "CharacterLiteral", "FloatingLiteral", "StringLiteral", 
			"BooleanLiteral", "PointerLiteral", "UserDefinedLiteral", "MultiLineMacro", 
			"Directive", "Alignas", "Alignof", "Asm", "Auto", "Bool", "Break", "Case", 
			"Catch", "Char", "Char16", "Char32", "Class", "Const", "Constexpr", "Const_cast", 
			"Continue", "Decltype", "Default", "Delete", "Do", "Double", "Dynamic_cast", 
			"Else", "Enum", "Explicit", "Export", "Extern", "False_", "Final", "Float", 
			"For", "Friend", "Goto", "If", "Inline", "Int", "Long", "Mutable", "Namespace", 
			"New", "Noexcept", "Nullptr", "Operator", "Override", "Private", "Protected", 
			"Public", "Register", "Reinterpret_cast", "Return", "Short", "Signed", 
			"Sizeof", "Static", "Static_assert", "Static_cast", "Struct", "Switch", 
			"Template", "This", "Thread_local", "Throw", "True_", "Try", "Typedef", 
			"Typeid_", "Typename_", "Union", "Unsigned", "Using", "Virtual", "Void", 
			"Volatile", "Wchar", "While", "LeftParen", "RightParen", "LeftBracket", 
			"RightBracket", "LeftBrace", "RightBrace", "Plus", "Minus", "Star", "Div", 
			"Mod", "Caret", "And", "Or", "Tilde", "Not", "Assign", "Less", "Greater", 
			"PlusAssign", "MinusAssign", "StarAssign", "DivAssign", "ModAssign", 
			"XorAssign", "AndAssign", "OrAssign", "LeftShiftAssign", "RightShiftAssign", 
			"Equal", "NotEqual", "LessEqual", "GreaterEqual", "AndAnd", "OrOr", "PlusPlus", 
			"MinusMinus", "Comma", "ArrowStar", "Arrow", "Question", "Colon", "Doublecolon", 
			"Semi", "Dot", "DotStar", "Ellipsis", "Identifier", "DecimalLiteral", 
			"OctalLiteral", "HexadecimalLiteral", "BinaryLiteral", "Integersuffix", 
			"UserDefinedIntegerLiteral", "UserDefinedFloatingLiteral", "UserDefinedStringLiteral", 
			"UserDefinedCharacterLiteral", "Whitespace", "Newline", "BlockComment", 
			"LineComment"
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

	public CPP14Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TranslationUnitContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CPP14Parser.EOF, 0); }
		public DeclarationseqContext declarationseq() {
			return getRuleContext(DeclarationseqContext.class,0);
		}
		public TranslationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_translationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTranslationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTranslationUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTranslationUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TranslationUnitContext translationUnit() throws RecognitionException {
		TranslationUnitContext _localctx = new TranslationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_translationUnit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0) {
				{
				setState(382);
				declarationseq();
				}
			}

			setState(385);
			match(EOF);
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
	public static class PrimaryExpressionContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public TerminalNode This() { return getToken(CPP14Parser.This, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public LambdaExpressionContext lambdaExpression() {
			return getRuleContext(LambdaExpressionContext.class,0);
		}
		public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPrimaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPrimaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPrimaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
		PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_primaryExpression);
		try {
			int _alt;
			setState(399);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(388); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(387);
						literal();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(390); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case This:
				enterOuterAlt(_localctx, 2);
				{
				setState(392);
				match(This);
				}
				break;
			case LeftParen:
				enterOuterAlt(_localctx, 3);
				{
				setState(393);
				match(LeftParen);
				setState(394);
				expression();
				setState(395);
				match(RightParen);
				}
				break;
			case Decltype:
			case Operator:
			case Tilde:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 4);
				{
				setState(397);
				idExpression();
				}
				break;
			case LeftBracket:
				enterOuterAlt(_localctx, 5);
				{
				setState(398);
				lambdaExpression();
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
	public static class IdExpressionContext extends ParserRuleContext {
		public UnqualifiedIdContext unqualifiedId() {
			return getRuleContext(UnqualifiedIdContext.class,0);
		}
		public QualifiedIdContext qualifiedId() {
			return getRuleContext(QualifiedIdContext.class,0);
		}
		public IdExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterIdExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitIdExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitIdExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdExpressionContext idExpression() throws RecognitionException {
		IdExpressionContext _localctx = new IdExpressionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_idExpression);
		try {
			setState(403);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(401);
				unqualifiedId();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(402);
				qualifiedId();
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
	public static class UnqualifiedIdContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public OperatorFunctionIdContext operatorFunctionId() {
			return getRuleContext(OperatorFunctionIdContext.class,0);
		}
		public ConversionFunctionIdContext conversionFunctionId() {
			return getRuleContext(ConversionFunctionIdContext.class,0);
		}
		public LiteralOperatorIdContext literalOperatorId() {
			return getRuleContext(LiteralOperatorIdContext.class,0);
		}
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public TemplateIdContext templateId() {
			return getRuleContext(TemplateIdContext.class,0);
		}
		public UnqualifiedIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unqualifiedId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnqualifiedId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnqualifiedId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitUnqualifiedId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnqualifiedIdContext unqualifiedId() throws RecognitionException {
		UnqualifiedIdContext _localctx = new UnqualifiedIdContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_unqualifiedId);
		try {
			setState(415);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(405);
				match(Identifier);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(406);
				operatorFunctionId();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(407);
				conversionFunctionId();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(408);
				literalOperatorId();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(409);
				match(Tilde);
				setState(412);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Identifier:
					{
					setState(410);
					className();
					}
					break;
				case Decltype:
					{
					setState(411);
					decltypeSpecifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(414);
				templateId();
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
	public static class QualifiedIdContext extends ParserRuleContext {
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public UnqualifiedIdContext unqualifiedId() {
			return getRuleContext(UnqualifiedIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public QualifiedIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiedId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterQualifiedId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitQualifiedId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitQualifiedId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiedIdContext qualifiedId() throws RecognitionException {
		QualifiedIdContext _localctx = new QualifiedIdContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_qualifiedId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(417);
			nestedNameSpecifier(0);
			setState(419);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Template) {
				{
				setState(418);
				match(Template);
				}
			}

			setState(421);
			unqualifiedId();
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
	public static class NestedNameSpecifierContext extends ParserRuleContext {
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public TheTypeNameContext theTypeName() {
			return getRuleContext(TheTypeNameContext.class,0);
		}
		public NamespaceNameContext namespaceName() {
			return getRuleContext(NamespaceNameContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public NestedNameSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedNameSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNestedNameSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNestedNameSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNestedNameSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NestedNameSpecifierContext nestedNameSpecifier() throws RecognitionException {
		return nestedNameSpecifier(0);
	}

	private NestedNameSpecifierContext nestedNameSpecifier(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NestedNameSpecifierContext _localctx = new NestedNameSpecifierContext(_ctx, _parentState);
		NestedNameSpecifierContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_nestedNameSpecifier, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(427);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(424);
				theTypeName();
				}
				break;
			case 2:
				{
				setState(425);
				namespaceName();
				}
				break;
			case 3:
				{
				setState(426);
				decltypeSpecifier();
				}
				break;
			}
			setState(429);
			match(Doublecolon);
			}
			_ctx.stop = _input.LT(-1);
			setState(442);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NestedNameSpecifierContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_nestedNameSpecifier);
					setState(431);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(437);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
					case 1:
						{
						setState(432);
						match(Identifier);
						}
						break;
					case 2:
						{
						setState(434);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==Template) {
							{
							setState(433);
							match(Template);
							}
						}

						setState(436);
						simpleTemplateId();
						}
						break;
					}
					setState(439);
					match(Doublecolon);
					}
					} 
				}
				setState(444);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LambdaExpressionContext extends ParserRuleContext {
		public LambdaIntroducerContext lambdaIntroducer() {
			return getRuleContext(LambdaIntroducerContext.class,0);
		}
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public LambdaDeclaratorContext lambdaDeclarator() {
			return getRuleContext(LambdaDeclaratorContext.class,0);
		}
		public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLambdaExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LambdaExpressionContext lambdaExpression() throws RecognitionException {
		LambdaExpressionContext _localctx = new LambdaExpressionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_lambdaExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(445);
			lambdaIntroducer();
			setState(447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LeftParen) {
				{
				setState(446);
				lambdaDeclarator();
				}
			}

			setState(449);
			compoundStatement();
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
	public static class LambdaIntroducerContext extends ParserRuleContext {
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public LambdaCaptureContext lambdaCapture() {
			return getRuleContext(LambdaCaptureContext.class,0);
		}
		public LambdaIntroducerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaIntroducer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaIntroducer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaIntroducer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLambdaIntroducer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LambdaIntroducerContext lambdaIntroducer() throws RecognitionException {
		LambdaIntroducerContext _localctx = new LambdaIntroducerContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_lambdaIntroducer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(451);
			match(LeftBracket);
			setState(453);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 69)) & ~0x3f) == 0 && ((1L << (_la - 69)) & -9223372032291373055L) != 0) {
				{
				setState(452);
				lambdaCapture();
				}
			}

			setState(455);
			match(RightBracket);
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
	public static class LambdaCaptureContext extends ParserRuleContext {
		public CaptureListContext captureList() {
			return getRuleContext(CaptureListContext.class,0);
		}
		public CaptureDefaultContext captureDefault() {
			return getRuleContext(CaptureDefaultContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public LambdaCaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaCapture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaCapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaCapture(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLambdaCapture(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LambdaCaptureContext lambdaCapture() throws RecognitionException {
		LambdaCaptureContext _localctx = new LambdaCaptureContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_lambdaCapture);
		int _la;
		try {
			setState(463);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(457);
				captureList();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(458);
				captureDefault();
				setState(461);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(459);
					match(Comma);
					setState(460);
					captureList();
					}
				}

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
	public static class CaptureDefaultContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public CaptureDefaultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureDefault; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCaptureDefault(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCaptureDefault(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCaptureDefault(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureDefaultContext captureDefault() throws RecognitionException {
		CaptureDefaultContext _localctx = new CaptureDefaultContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_captureDefault);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(465);
			_la = _input.LA(1);
			if ( !(_la==And || _la==Assign) ) {
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
	public static class CaptureListContext extends ParserRuleContext {
		public List<CaptureContext> capture() {
			return getRuleContexts(CaptureContext.class);
		}
		public CaptureContext capture(int i) {
			return getRuleContext(CaptureContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public CaptureListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCaptureList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCaptureList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCaptureList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureListContext captureList() throws RecognitionException {
		CaptureListContext _localctx = new CaptureListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_captureList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(467);
			capture();
			setState(472);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(468);
				match(Comma);
				setState(469);
				capture();
				}
				}
				setState(474);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(476);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(475);
				match(Ellipsis);
				}
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
	public static class CaptureContext extends ParserRuleContext {
		public SimpleCaptureContext simpleCapture() {
			return getRuleContext(SimpleCaptureContext.class,0);
		}
		public InitcaptureContext initcapture() {
			return getRuleContext(InitcaptureContext.class,0);
		}
		public CaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_capture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCapture(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCapture(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureContext capture() throws RecognitionException {
		CaptureContext _localctx = new CaptureContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_capture);
		try {
			setState(480);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(478);
				simpleCapture();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(479);
				initcapture();
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
	public static class SimpleCaptureContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode This() { return getToken(CPP14Parser.This, 0); }
		public SimpleCaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleCapture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleCapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleCapture(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSimpleCapture(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleCaptureContext simpleCapture() throws RecognitionException {
		SimpleCaptureContext _localctx = new SimpleCaptureContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_simpleCapture);
		int _la;
		try {
			setState(487);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case And:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(483);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==And) {
					{
					setState(482);
					match(And);
					}
				}

				setState(485);
				match(Identifier);
				}
				break;
			case This:
				enterOuterAlt(_localctx, 2);
				{
				setState(486);
				match(This);
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
	public static class InitcaptureContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public InitcaptureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initcapture; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitcapture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitcapture(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInitcapture(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitcaptureContext initcapture() throws RecognitionException {
		InitcaptureContext _localctx = new InitcaptureContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_initcapture);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(490);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==And) {
				{
				setState(489);
				match(And);
				}
			}

			setState(492);
			match(Identifier);
			setState(493);
			initializer();
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
	public static class LambdaDeclaratorContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ParameterDeclarationClauseContext parameterDeclarationClause() {
			return getRuleContext(ParameterDeclarationClauseContext.class,0);
		}
		public TerminalNode Mutable() { return getToken(CPP14Parser.Mutable, 0); }
		public ExceptionSpecificationContext exceptionSpecification() {
			return getRuleContext(ExceptionSpecificationContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TrailingReturnTypeContext trailingReturnType() {
			return getRuleContext(TrailingReturnTypeContext.class,0);
		}
		public LambdaDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_lambdaDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLambdaDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLambdaDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLambdaDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LambdaDeclaratorContext lambdaDeclarator() throws RecognitionException {
		LambdaDeclaratorContext _localctx = new LambdaDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_lambdaDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(495);
			match(LeftParen);
			setState(497);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1237504995584196377L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 297237575406461917L) != 0) {
				{
				setState(496);
				parameterDeclarationClause();
				}
			}

			setState(499);
			match(RightParen);
			setState(501);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Mutable) {
				{
				setState(500);
				match(Mutable);
				}
			}

			setState(504);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Noexcept || _la==Throw) {
				{
				setState(503);
				exceptionSpecification();
				}
			}

			setState(507);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(506);
				attributeSpecifierSeq();
				}
			}

			setState(510);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Arrow) {
				{
				setState(509);
				trailingReturnType();
				}
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
	public static class PostfixExpressionContext extends ParserRuleContext {
		public PrimaryExpressionContext primaryExpression() {
			return getRuleContext(PrimaryExpressionContext.class,0);
		}
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public TypeNameSpecifierContext typeNameSpecifier() {
			return getRuleContext(TypeNameSpecifierContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Dynamic_cast() { return getToken(CPP14Parser.Dynamic_cast, 0); }
		public TerminalNode Static_cast() { return getToken(CPP14Parser.Static_cast, 0); }
		public TerminalNode Reinterpret_cast() { return getToken(CPP14Parser.Reinterpret_cast, 0); }
		public TerminalNode Const_cast() { return getToken(CPP14Parser.Const_cast, 0); }
		public TypeIdOfTheTypeIdContext typeIdOfTheTypeId() {
			return getRuleContext(TypeIdOfTheTypeIdContext.class,0);
		}
		public PostfixExpressionContext postfixExpression() {
			return getRuleContext(PostfixExpressionContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public TerminalNode Dot() { return getToken(CPP14Parser.Dot, 0); }
		public TerminalNode Arrow() { return getToken(CPP14Parser.Arrow, 0); }
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public PseudoDestructorNameContext pseudoDestructorName() {
			return getRuleContext(PseudoDestructorNameContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode PlusPlus() { return getToken(CPP14Parser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(CPP14Parser.MinusMinus, 0); }
		public PostfixExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_postfixExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPostfixExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPostfixExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPostfixExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PostfixExpressionContext postfixExpression() throws RecognitionException {
		return postfixExpression(0);
	}

	private PostfixExpressionContext postfixExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PostfixExpressionContext _localctx = new PostfixExpressionContext(_ctx, _parentState);
		PostfixExpressionContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_postfixExpression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(542);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				{
				setState(513);
				primaryExpression();
				}
				break;
			case 2:
				{
				setState(516);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Decltype:
				case Double:
				case Float:
				case Int:
				case Long:
				case Short:
				case Signed:
				case Unsigned:
				case Void:
				case Wchar:
				case Doublecolon:
				case Identifier:
					{
					setState(514);
					simpleTypeSpecifier();
					}
					break;
				case Typename_:
					{
					setState(515);
					typeNameSpecifier();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(524);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LeftParen:
					{
					setState(518);
					match(LeftParen);
					setState(520);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0 || _la==Identifier) {
						{
						setState(519);
						expressionList();
						}
					}

					setState(522);
					match(RightParen);
					}
					break;
				case LeftBrace:
					{
					setState(523);
					bracedInitList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 3:
				{
				setState(526);
				_la = _input.LA(1);
				if ( !((((_la - 24)) & ~0x3f) == 0 && ((1L << (_la - 24)) & 2216203124865L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(527);
				match(Less);
				setState(528);
				theTypeId();
				setState(529);
				match(Greater);
				setState(530);
				match(LeftParen);
				setState(531);
				expression();
				setState(532);
				match(RightParen);
				}
				break;
			case 4:
				{
				setState(534);
				typeIdOfTheTypeId();
				setState(535);
				match(LeftParen);
				setState(538);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,29,_ctx) ) {
				case 1:
					{
					setState(536);
					expression();
					}
					break;
				case 2:
					{
					setState(537);
					theTypeId();
					}
					break;
				}
				setState(540);
				match(RightParen);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(571);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(569);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
					case 1:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(544);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(545);
						match(LeftBracket);
						setState(548);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
						case IntegerLiteral:
						case CharacterLiteral:
						case FloatingLiteral:
						case StringLiteral:
						case BooleanLiteral:
						case PointerLiteral:
						case UserDefinedLiteral:
						case Alignof:
						case Auto:
						case Bool:
						case Char:
						case Char16:
						case Char32:
						case Const_cast:
						case Decltype:
						case Delete:
						case Double:
						case Dynamic_cast:
						case Float:
						case Int:
						case Long:
						case New:
						case Noexcept:
						case Operator:
						case Reinterpret_cast:
						case Short:
						case Signed:
						case Sizeof:
						case Static_cast:
						case This:
						case Throw:
						case Typeid_:
						case Typename_:
						case Unsigned:
						case Void:
						case Wchar:
						case LeftParen:
						case LeftBracket:
						case Plus:
						case Minus:
						case Star:
						case And:
						case Or:
						case Tilde:
						case Not:
						case PlusPlus:
						case MinusMinus:
						case Doublecolon:
						case Identifier:
							{
							setState(546);
							expression();
							}
							break;
						case LeftBrace:
							{
							setState(547);
							bracedInitList();
							}
							break;
						default:
							throw new NoViableAltException(this);
						}
						setState(550);
						match(RightBracket);
						}
						break;
					case 2:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(552);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(553);
						match(LeftParen);
						setState(555);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0 || _la==Identifier) {
							{
							setState(554);
							expressionList();
							}
						}

						setState(557);
						match(RightParen);
						}
						break;
					case 3:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(558);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(559);
						_la = _input.LA(1);
						if ( !(_la==Arrow || _la==Dot) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(565);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,34,_ctx) ) {
						case 1:
							{
							setState(561);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==Template) {
								{
								setState(560);
								match(Template);
								}
							}

							setState(563);
							idExpression();
							}
							break;
						case 2:
							{
							setState(564);
							pseudoDestructorName();
							}
							break;
						}
						}
						break;
					case 4:
						{
						_localctx = new PostfixExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
						setState(567);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(568);
						_la = _input.LA(1);
						if ( !(_la==PlusPlus || _la==MinusMinus) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(573);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeIdOfTheTypeIdContext extends ParserRuleContext {
		public TerminalNode Typeid_() { return getToken(CPP14Parser.Typeid_, 0); }
		public TypeIdOfTheTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeIdOfTheTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeIdOfTheTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeIdOfTheTypeId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypeIdOfTheTypeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeIdOfTheTypeIdContext typeIdOfTheTypeId() throws RecognitionException {
		TypeIdOfTheTypeIdContext _localctx = new TypeIdOfTheTypeIdContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_typeIdOfTheTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(574);
			match(Typeid_);
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
	public static class ExpressionListContext extends ParserRuleContext {
		public InitializerListContext initializerList() {
			return getRuleContext(InitializerListContext.class,0);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_expressionList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(576);
			initializerList();
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
	public static class PseudoDestructorNameContext extends ParserRuleContext {
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public List<TheTypeNameContext> theTypeName() {
			return getRuleContexts(TheTypeNameContext.class);
		}
		public TheTypeNameContext theTypeName(int i) {
			return getRuleContext(TheTypeNameContext.class,i);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public PseudoDestructorNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pseudoDestructorName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPseudoDestructorName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPseudoDestructorName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPseudoDestructorName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PseudoDestructorNameContext pseudoDestructorName() throws RecognitionException {
		PseudoDestructorNameContext _localctx = new PseudoDestructorNameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_pseudoDestructorName);
		int _la;
		try {
			setState(597);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,39,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(579);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
				case 1:
					{
					setState(578);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(584);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(581);
					theTypeName();
					setState(582);
					match(Doublecolon);
					}
				}

				setState(586);
				match(Tilde);
				setState(587);
				theTypeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(588);
				nestedNameSpecifier(0);
				setState(589);
				match(Template);
				setState(590);
				simpleTemplateId();
				setState(591);
				match(Doublecolon);
				setState(592);
				match(Tilde);
				setState(593);
				theTypeName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(595);
				match(Tilde);
				setState(596);
				decltypeSpecifier();
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
	public static class UnaryExpressionContext extends ParserRuleContext {
		public PostfixExpressionContext postfixExpression() {
			return getRuleContext(PostfixExpressionContext.class,0);
		}
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public TerminalNode PlusPlus() { return getToken(CPP14Parser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(CPP14Parser.MinusMinus, 0); }
		public UnaryOperatorContext unaryOperator() {
			return getRuleContext(UnaryOperatorContext.class,0);
		}
		public TerminalNode Sizeof() { return getToken(CPP14Parser.Sizeof, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Alignof() { return getToken(CPP14Parser.Alignof, 0); }
		public NoExceptExpressionContext noExceptExpression() {
			return getRuleContext(NoExceptExpressionContext.class,0);
		}
		public NewExpressionContext newExpression() {
			return getRuleContext(NewExpressionContext.class,0);
		}
		public DeleteExpressionContext deleteExpression() {
			return getRuleContext(DeleteExpressionContext.class,0);
		}
		public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitUnaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryExpressionContext unaryExpression() throws RecognitionException {
		UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_unaryExpression);
		try {
			setState(626);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,42,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(599);
				postfixExpression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(604);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case PlusPlus:
					{
					setState(600);
					match(PlusPlus);
					}
					break;
				case MinusMinus:
					{
					setState(601);
					match(MinusMinus);
					}
					break;
				case Plus:
				case Minus:
				case Star:
				case And:
				case Or:
				case Tilde:
				case Not:
					{
					setState(602);
					unaryOperator();
					}
					break;
				case Sizeof:
					{
					setState(603);
					match(Sizeof);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(606);
				unaryExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(607);
				match(Sizeof);
				setState(616);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LeftParen:
					{
					setState(608);
					match(LeftParen);
					setState(609);
					theTypeId();
					setState(610);
					match(RightParen);
					}
					break;
				case Ellipsis:
					{
					setState(612);
					match(Ellipsis);
					setState(613);
					match(LeftParen);
					setState(614);
					match(Identifier);
					setState(615);
					match(RightParen);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(618);
				match(Alignof);
				setState(619);
				match(LeftParen);
				setState(620);
				theTypeId();
				setState(621);
				match(RightParen);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(623);
				noExceptExpression();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(624);
				newExpression();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(625);
				deleteExpression();
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
	public static class UnaryOperatorContext extends ParserRuleContext {
		public TerminalNode Or() { return getToken(CPP14Parser.Or, 0); }
		public TerminalNode Star() { return getToken(CPP14Parser.Star, 0); }
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode Plus() { return getToken(CPP14Parser.Plus, 0); }
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public TerminalNode Minus() { return getToken(CPP14Parser.Minus, 0); }
		public TerminalNode Not() { return getToken(CPP14Parser.Not, 0); }
		public UnaryOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unaryOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUnaryOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUnaryOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitUnaryOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnaryOperatorContext unaryOperator() throws RecognitionException {
		UnaryOperatorContext _localctx = new UnaryOperatorContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_unaryOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(628);
			_la = _input.LA(1);
			if ( !((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 967L) != 0) ) {
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
	public static class NewExpressionContext extends ParserRuleContext {
		public TerminalNode New() { return getToken(CPP14Parser.New, 0); }
		public NewTypeIdContext newTypeId() {
			return getRuleContext(NewTypeIdContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public NewPlacementContext newPlacement() {
			return getRuleContext(NewPlacementContext.class,0);
		}
		public NewInitializerContext newInitializer() {
			return getRuleContext(NewInitializerContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NewExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNewExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewExpressionContext newExpression() throws RecognitionException {
		NewExpressionContext _localctx = new NewExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_newExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(631);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Doublecolon) {
				{
				setState(630);
				match(Doublecolon);
				}
			}

			setState(633);
			match(New);
			setState(635);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,44,_ctx) ) {
			case 1:
				{
				setState(634);
				newPlacement();
				}
				break;
			}
			setState(642);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Decltype:
			case Double:
			case Enum:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Struct:
			case Typename_:
			case Union:
			case Unsigned:
			case Void:
			case Volatile:
			case Wchar:
			case Doublecolon:
			case Identifier:
				{
				setState(637);
				newTypeId();
				}
				break;
			case LeftParen:
				{
				{
				setState(638);
				match(LeftParen);
				setState(639);
				theTypeId();
				setState(640);
				match(RightParen);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(645);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LeftParen || _la==LeftBrace) {
				{
				setState(644);
				newInitializer();
				}
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
	public static class NewPlacementContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NewPlacementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newPlacement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewPlacement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewPlacement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNewPlacement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewPlacementContext newPlacement() throws RecognitionException {
		NewPlacementContext _localctx = new NewPlacementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_newPlacement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(647);
			match(LeftParen);
			setState(648);
			expressionList();
			setState(649);
			match(RightParen);
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
	public static class NewTypeIdContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public NewDeclaratorContext newDeclarator() {
			return getRuleContext(NewDeclaratorContext.class,0);
		}
		public NewTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewTypeId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNewTypeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewTypeIdContext newTypeId() throws RecognitionException {
		NewTypeIdContext _localctx = new NewTypeIdContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_newTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(651);
			typeSpecifierSeq();
			setState(653);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,47,_ctx) ) {
			case 1:
				{
				setState(652);
				newDeclarator();
				}
				break;
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
	public static class NewDeclaratorContext extends ParserRuleContext {
		public PointerOperatorContext pointerOperator() {
			return getRuleContext(PointerOperatorContext.class,0);
		}
		public NewDeclaratorContext newDeclarator() {
			return getRuleContext(NewDeclaratorContext.class,0);
		}
		public NoPointerNewDeclaratorContext noPointerNewDeclarator() {
			return getRuleContext(NoPointerNewDeclaratorContext.class,0);
		}
		public NewDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNewDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewDeclaratorContext newDeclarator() throws RecognitionException {
		NewDeclaratorContext _localctx = new NewDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_newDeclarator);
		try {
			setState(660);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Decltype:
			case Star:
			case And:
			case AndAnd:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(655);
				pointerOperator();
				setState(657);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,48,_ctx) ) {
				case 1:
					{
					setState(656);
					newDeclarator();
					}
					break;
				}
				}
				break;
			case LeftBracket:
				enterOuterAlt(_localctx, 2);
				{
				setState(659);
				noPointerNewDeclarator(0);
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
	public static class NoPointerNewDeclaratorContext extends ParserRuleContext {
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public NoPointerNewDeclaratorContext noPointerNewDeclarator() {
			return getRuleContext(NoPointerNewDeclaratorContext.class,0);
		}
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public NoPointerNewDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerNewDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerNewDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerNewDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNoPointerNewDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoPointerNewDeclaratorContext noPointerNewDeclarator() throws RecognitionException {
		return noPointerNewDeclarator(0);
	}

	private NoPointerNewDeclaratorContext noPointerNewDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerNewDeclaratorContext _localctx = new NoPointerNewDeclaratorContext(_ctx, _parentState);
		NoPointerNewDeclaratorContext _prevctx = _localctx;
		int _startState = 50;
		enterRecursionRule(_localctx, 50, RULE_noPointerNewDeclarator, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(663);
			match(LeftBracket);
			setState(664);
			expression();
			setState(665);
			match(RightBracket);
			setState(667);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,50,_ctx) ) {
			case 1:
				{
				setState(666);
				attributeSpecifierSeq();
				}
				break;
			}
			}
			_ctx.stop = _input.LT(-1);
			setState(678);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerNewDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerNewDeclarator);
					setState(669);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(670);
					match(LeftBracket);
					setState(671);
					constantExpression();
					setState(672);
					match(RightBracket);
					setState(674);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,51,_ctx) ) {
					case 1:
						{
						setState(673);
						attributeSpecifierSeq();
						}
						break;
					}
					}
					} 
				}
				setState(680);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,52,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NewInitializerContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public NewInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_newInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNewInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNewInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNewInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NewInitializerContext newInitializer() throws RecognitionException {
		NewInitializerContext _localctx = new NewInitializerContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_newInitializer);
		int _la;
		try {
			setState(687);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
				enterOuterAlt(_localctx, 1);
				{
				setState(681);
				match(LeftParen);
				setState(683);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0 || _la==Identifier) {
					{
					setState(682);
					expressionList();
					}
				}

				setState(685);
				match(RightParen);
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(686);
				bracedInitList();
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
	public static class DeleteExpressionContext extends ParserRuleContext {
		public TerminalNode Delete() { return getToken(CPP14Parser.Delete, 0); }
		public CastExpressionContext castExpression() {
			return getRuleContext(CastExpressionContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public DeleteExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeleteExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeleteExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeleteExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteExpressionContext deleteExpression() throws RecognitionException {
		DeleteExpressionContext _localctx = new DeleteExpressionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_deleteExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(690);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Doublecolon) {
				{
				setState(689);
				match(Doublecolon);
				}
			}

			setState(692);
			match(Delete);
			setState(695);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,56,_ctx) ) {
			case 1:
				{
				setState(693);
				match(LeftBracket);
				setState(694);
				match(RightBracket);
				}
				break;
			}
			setState(697);
			castExpression();
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
	public static class NoExceptExpressionContext extends ParserRuleContext {
		public TerminalNode Noexcept() { return getToken(CPP14Parser.Noexcept, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NoExceptExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noExceptExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoExceptExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoExceptExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNoExceptExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoExceptExpressionContext noExceptExpression() throws RecognitionException {
		NoExceptExpressionContext _localctx = new NoExceptExpressionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_noExceptExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(699);
			match(Noexcept);
			setState(700);
			match(LeftParen);
			setState(701);
			expression();
			setState(702);
			match(RightParen);
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
	public static class CastExpressionContext extends ParserRuleContext {
		public UnaryExpressionContext unaryExpression() {
			return getRuleContext(UnaryExpressionContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public CastExpressionContext castExpression() {
			return getRuleContext(CastExpressionContext.class,0);
		}
		public CastExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_castExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCastExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCastExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCastExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CastExpressionContext castExpression() throws RecognitionException {
		CastExpressionContext _localctx = new CastExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_castExpression);
		try {
			setState(710);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,57,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(704);
				unaryExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(705);
				match(LeftParen);
				setState(706);
				theTypeId();
				setState(707);
				match(RightParen);
				setState(708);
				castExpression();
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
	public static class PointerMemberExpressionContext extends ParserRuleContext {
		public List<CastExpressionContext> castExpression() {
			return getRuleContexts(CastExpressionContext.class);
		}
		public CastExpressionContext castExpression(int i) {
			return getRuleContext(CastExpressionContext.class,i);
		}
		public List<TerminalNode> DotStar() { return getTokens(CPP14Parser.DotStar); }
		public TerminalNode DotStar(int i) {
			return getToken(CPP14Parser.DotStar, i);
		}
		public List<TerminalNode> ArrowStar() { return getTokens(CPP14Parser.ArrowStar); }
		public TerminalNode ArrowStar(int i) {
			return getToken(CPP14Parser.ArrowStar, i);
		}
		public PointerMemberExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerMemberExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerMemberExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerMemberExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPointerMemberExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PointerMemberExpressionContext pointerMemberExpression() throws RecognitionException {
		PointerMemberExpressionContext _localctx = new PointerMemberExpressionContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_pointerMemberExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(712);
			castExpression();
			setState(717);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ArrowStar || _la==DotStar) {
				{
				{
				setState(713);
				_la = _input.LA(1);
				if ( !(_la==ArrowStar || _la==DotStar) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(714);
				castExpression();
				}
				}
				setState(719);
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
	public static class MultiplicativeExpressionContext extends ParserRuleContext {
		public List<PointerMemberExpressionContext> pointerMemberExpression() {
			return getRuleContexts(PointerMemberExpressionContext.class);
		}
		public PointerMemberExpressionContext pointerMemberExpression(int i) {
			return getRuleContext(PointerMemberExpressionContext.class,i);
		}
		public List<TerminalNode> Star() { return getTokens(CPP14Parser.Star); }
		public TerminalNode Star(int i) {
			return getToken(CPP14Parser.Star, i);
		}
		public List<TerminalNode> Div() { return getTokens(CPP14Parser.Div); }
		public TerminalNode Div(int i) {
			return getToken(CPP14Parser.Div, i);
		}
		public List<TerminalNode> Mod() { return getTokens(CPP14Parser.Mod); }
		public TerminalNode Mod(int i) {
			return getToken(CPP14Parser.Mod, i);
		}
		public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_multiplicativeExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMultiplicativeExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMultiplicativeExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMultiplicativeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MultiplicativeExpressionContext multiplicativeExpression() throws RecognitionException {
		MultiplicativeExpressionContext _localctx = new MultiplicativeExpressionContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_multiplicativeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(720);
			pointerMemberExpression();
			setState(725);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 7L) != 0) {
				{
				{
				setState(721);
				_la = _input.LA(1);
				if ( !((((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 7L) != 0) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(722);
				pointerMemberExpression();
				}
				}
				setState(727);
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
	public static class AdditiveExpressionContext extends ParserRuleContext {
		public List<MultiplicativeExpressionContext> multiplicativeExpression() {
			return getRuleContexts(MultiplicativeExpressionContext.class);
		}
		public MultiplicativeExpressionContext multiplicativeExpression(int i) {
			return getRuleContext(MultiplicativeExpressionContext.class,i);
		}
		public List<TerminalNode> Plus() { return getTokens(CPP14Parser.Plus); }
		public TerminalNode Plus(int i) {
			return getToken(CPP14Parser.Plus, i);
		}
		public List<TerminalNode> Minus() { return getTokens(CPP14Parser.Minus); }
		public TerminalNode Minus(int i) {
			return getToken(CPP14Parser.Minus, i);
		}
		public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_additiveExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAdditiveExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAdditiveExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAdditiveExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
		AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_additiveExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(728);
			multiplicativeExpression();
			setState(733);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Plus || _la==Minus) {
				{
				{
				setState(729);
				_la = _input.LA(1);
				if ( !(_la==Plus || _la==Minus) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(730);
				multiplicativeExpression();
				}
				}
				setState(735);
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
	public static class ShiftExpressionContext extends ParserRuleContext {
		public List<AdditiveExpressionContext> additiveExpression() {
			return getRuleContexts(AdditiveExpressionContext.class);
		}
		public AdditiveExpressionContext additiveExpression(int i) {
			return getRuleContext(AdditiveExpressionContext.class,i);
		}
		public List<ShiftOperatorContext> shiftOperator() {
			return getRuleContexts(ShiftOperatorContext.class);
		}
		public ShiftOperatorContext shiftOperator(int i) {
			return getRuleContext(ShiftOperatorContext.class,i);
		}
		public ShiftExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shiftExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterShiftExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitShiftExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitShiftExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShiftExpressionContext shiftExpression() throws RecognitionException {
		ShiftExpressionContext _localctx = new ShiftExpressionContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_shiftExpression);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(736);
			additiveExpression();
			setState(742);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(737);
					shiftOperator();
					setState(738);
					additiveExpression();
					}
					} 
				}
				setState(744);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,61,_ctx);
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
	public static class ShiftOperatorContext extends ParserRuleContext {
		public List<TerminalNode> Greater() { return getTokens(CPP14Parser.Greater); }
		public TerminalNode Greater(int i) {
			return getToken(CPP14Parser.Greater, i);
		}
		public List<TerminalNode> Less() { return getTokens(CPP14Parser.Less); }
		public TerminalNode Less(int i) {
			return getToken(CPP14Parser.Less, i);
		}
		public ShiftOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shiftOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterShiftOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitShiftOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitShiftOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShiftOperatorContext shiftOperator() throws RecognitionException {
		ShiftOperatorContext _localctx = new ShiftOperatorContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_shiftOperator);
		try {
			setState(749);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Greater:
				enterOuterAlt(_localctx, 1);
				{
				setState(745);
				match(Greater);
				setState(746);
				match(Greater);
				}
				break;
			case Less:
				enterOuterAlt(_localctx, 2);
				{
				setState(747);
				match(Less);
				setState(748);
				match(Less);
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
	public static class RelationalExpressionContext extends ParserRuleContext {
		public List<ShiftExpressionContext> shiftExpression() {
			return getRuleContexts(ShiftExpressionContext.class);
		}
		public ShiftExpressionContext shiftExpression(int i) {
			return getRuleContext(ShiftExpressionContext.class,i);
		}
		public List<TerminalNode> Less() { return getTokens(CPP14Parser.Less); }
		public TerminalNode Less(int i) {
			return getToken(CPP14Parser.Less, i);
		}
		public List<TerminalNode> Greater() { return getTokens(CPP14Parser.Greater); }
		public TerminalNode Greater(int i) {
			return getToken(CPP14Parser.Greater, i);
		}
		public List<TerminalNode> LessEqual() { return getTokens(CPP14Parser.LessEqual); }
		public TerminalNode LessEqual(int i) {
			return getToken(CPP14Parser.LessEqual, i);
		}
		public List<TerminalNode> GreaterEqual() { return getTokens(CPP14Parser.GreaterEqual); }
		public TerminalNode GreaterEqual(int i) {
			return getToken(CPP14Parser.GreaterEqual, i);
		}
		public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterRelationalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitRelationalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitRelationalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationalExpressionContext relationalExpression() throws RecognitionException {
		RelationalExpressionContext _localctx = new RelationalExpressionContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_relationalExpression);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(751);
			shiftExpression();
			setState(756);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(752);
					_la = _input.LA(1);
					if ( !((((_la - 102)) & ~0x3f) == 0 && ((1L << (_la - 102)) & 49155L) != 0) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(753);
					shiftExpression();
					}
					} 
				}
				setState(758);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,63,_ctx);
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
	public static class EqualityExpressionContext extends ParserRuleContext {
		public List<RelationalExpressionContext> relationalExpression() {
			return getRuleContexts(RelationalExpressionContext.class);
		}
		public RelationalExpressionContext relationalExpression(int i) {
			return getRuleContext(RelationalExpressionContext.class,i);
		}
		public List<TerminalNode> Equal() { return getTokens(CPP14Parser.Equal); }
		public TerminalNode Equal(int i) {
			return getToken(CPP14Parser.Equal, i);
		}
		public List<TerminalNode> NotEqual() { return getTokens(CPP14Parser.NotEqual); }
		public TerminalNode NotEqual(int i) {
			return getToken(CPP14Parser.NotEqual, i);
		}
		public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equalityExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEqualityExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEqualityExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEqualityExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EqualityExpressionContext equalityExpression() throws RecognitionException {
		EqualityExpressionContext _localctx = new EqualityExpressionContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_equalityExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(759);
			relationalExpression();
			setState(764);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Equal || _la==NotEqual) {
				{
				{
				setState(760);
				_la = _input.LA(1);
				if ( !(_la==Equal || _la==NotEqual) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(761);
				relationalExpression();
				}
				}
				setState(766);
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
	public static class AndExpressionContext extends ParserRuleContext {
		public List<EqualityExpressionContext> equalityExpression() {
			return getRuleContexts(EqualityExpressionContext.class);
		}
		public EqualityExpressionContext equalityExpression(int i) {
			return getRuleContext(EqualityExpressionContext.class,i);
		}
		public List<TerminalNode> And() { return getTokens(CPP14Parser.And); }
		public TerminalNode And(int i) {
			return getToken(CPP14Parser.And, i);
		}
		public AndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_andExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AndExpressionContext andExpression() throws RecognitionException {
		AndExpressionContext _localctx = new AndExpressionContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_andExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(767);
			equalityExpression();
			setState(772);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==And) {
				{
				{
				setState(768);
				match(And);
				setState(769);
				equalityExpression();
				}
				}
				setState(774);
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
	public static class ExclusiveOrExpressionContext extends ParserRuleContext {
		public List<AndExpressionContext> andExpression() {
			return getRuleContexts(AndExpressionContext.class);
		}
		public AndExpressionContext andExpression(int i) {
			return getRuleContext(AndExpressionContext.class,i);
		}
		public List<TerminalNode> Caret() { return getTokens(CPP14Parser.Caret); }
		public TerminalNode Caret(int i) {
			return getToken(CPP14Parser.Caret, i);
		}
		public ExclusiveOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exclusiveOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExclusiveOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExclusiveOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExclusiveOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExclusiveOrExpressionContext exclusiveOrExpression() throws RecognitionException {
		ExclusiveOrExpressionContext _localctx = new ExclusiveOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_exclusiveOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(775);
			andExpression();
			setState(780);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Caret) {
				{
				{
				setState(776);
				match(Caret);
				setState(777);
				andExpression();
				}
				}
				setState(782);
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
	public static class InclusiveOrExpressionContext extends ParserRuleContext {
		public List<ExclusiveOrExpressionContext> exclusiveOrExpression() {
			return getRuleContexts(ExclusiveOrExpressionContext.class);
		}
		public ExclusiveOrExpressionContext exclusiveOrExpression(int i) {
			return getRuleContext(ExclusiveOrExpressionContext.class,i);
		}
		public List<TerminalNode> Or() { return getTokens(CPP14Parser.Or); }
		public TerminalNode Or(int i) {
			return getToken(CPP14Parser.Or, i);
		}
		public InclusiveOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inclusiveOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInclusiveOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInclusiveOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInclusiveOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InclusiveOrExpressionContext inclusiveOrExpression() throws RecognitionException {
		InclusiveOrExpressionContext _localctx = new InclusiveOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_inclusiveOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(783);
			exclusiveOrExpression();
			setState(788);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Or) {
				{
				{
				setState(784);
				match(Or);
				setState(785);
				exclusiveOrExpression();
				}
				}
				setState(790);
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
	public static class LogicalAndExpressionContext extends ParserRuleContext {
		public List<InclusiveOrExpressionContext> inclusiveOrExpression() {
			return getRuleContexts(InclusiveOrExpressionContext.class);
		}
		public InclusiveOrExpressionContext inclusiveOrExpression(int i) {
			return getRuleContext(InclusiveOrExpressionContext.class,i);
		}
		public List<TerminalNode> AndAnd() { return getTokens(CPP14Parser.AndAnd); }
		public TerminalNode AndAnd(int i) {
			return getToken(CPP14Parser.AndAnd, i);
		}
		public LogicalAndExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalAndExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLogicalAndExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLogicalAndExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLogicalAndExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalAndExpressionContext logicalAndExpression() throws RecognitionException {
		LogicalAndExpressionContext _localctx = new LogicalAndExpressionContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_logicalAndExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(791);
			inclusiveOrExpression();
			setState(796);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AndAnd) {
				{
				{
				setState(792);
				match(AndAnd);
				setState(793);
				inclusiveOrExpression();
				}
				}
				setState(798);
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
	public static class LogicalOrExpressionContext extends ParserRuleContext {
		public List<LogicalAndExpressionContext> logicalAndExpression() {
			return getRuleContexts(LogicalAndExpressionContext.class);
		}
		public LogicalAndExpressionContext logicalAndExpression(int i) {
			return getRuleContext(LogicalAndExpressionContext.class,i);
		}
		public List<TerminalNode> OrOr() { return getTokens(CPP14Parser.OrOr); }
		public TerminalNode OrOr(int i) {
			return getToken(CPP14Parser.OrOr, i);
		}
		public LogicalOrExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logicalOrExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLogicalOrExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLogicalOrExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLogicalOrExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LogicalOrExpressionContext logicalOrExpression() throws RecognitionException {
		LogicalOrExpressionContext _localctx = new LogicalOrExpressionContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_logicalOrExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(799);
			logicalAndExpression();
			setState(804);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OrOr) {
				{
				{
				setState(800);
				match(OrOr);
				setState(801);
				logicalAndExpression();
				}
				}
				setState(806);
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
	public static class ConditionalExpressionContext extends ParserRuleContext {
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public TerminalNode Question() { return getToken(CPP14Parser.Question, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public AssignmentExpressionContext assignmentExpression() {
			return getRuleContext(AssignmentExpressionContext.class,0);
		}
		public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conditionalExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConditionalExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConditionalExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitConditionalExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionalExpressionContext conditionalExpression() throws RecognitionException {
		ConditionalExpressionContext _localctx = new ConditionalExpressionContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_conditionalExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(807);
			logicalOrExpression();
			setState(813);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Question) {
				{
				setState(808);
				match(Question);
				setState(809);
				expression();
				setState(810);
				match(Colon);
				setState(811);
				assignmentExpression();
				}
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
	public static class AssignmentExpressionContext extends ParserRuleContext {
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public LogicalOrExpressionContext logicalOrExpression() {
			return getRuleContext(LogicalOrExpressionContext.class,0);
		}
		public AssignmentOperatorContext assignmentOperator() {
			return getRuleContext(AssignmentOperatorContext.class,0);
		}
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public ThrowExpressionContext throwExpression() {
			return getRuleContext(ThrowExpressionContext.class,0);
		}
		public AssignmentExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAssignmentExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAssignmentExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAssignmentExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentExpressionContext assignmentExpression() throws RecognitionException {
		AssignmentExpressionContext _localctx = new AssignmentExpressionContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_assignmentExpression);
		try {
			setState(821);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,71,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(815);
				conditionalExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(816);
				logicalOrExpression();
				setState(817);
				assignmentOperator();
				setState(818);
				initializerClause();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(820);
				throwExpression();
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
	public static class AssignmentOperatorContext extends ParserRuleContext {
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TerminalNode StarAssign() { return getToken(CPP14Parser.StarAssign, 0); }
		public TerminalNode DivAssign() { return getToken(CPP14Parser.DivAssign, 0); }
		public TerminalNode ModAssign() { return getToken(CPP14Parser.ModAssign, 0); }
		public TerminalNode PlusAssign() { return getToken(CPP14Parser.PlusAssign, 0); }
		public TerminalNode MinusAssign() { return getToken(CPP14Parser.MinusAssign, 0); }
		public TerminalNode RightShiftAssign() { return getToken(CPP14Parser.RightShiftAssign, 0); }
		public TerminalNode LeftShiftAssign() { return getToken(CPP14Parser.LeftShiftAssign, 0); }
		public TerminalNode AndAssign() { return getToken(CPP14Parser.AndAssign, 0); }
		public TerminalNode XorAssign() { return getToken(CPP14Parser.XorAssign, 0); }
		public TerminalNode OrAssign() { return getToken(CPP14Parser.OrAssign, 0); }
		public AssignmentOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignmentOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAssignmentOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAssignmentOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAssignmentOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AssignmentOperatorContext assignmentOperator() throws RecognitionException {
		AssignmentOperatorContext _localctx = new AssignmentOperatorContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_assignmentOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(823);
			_la = _input.LA(1);
			if ( !((((_la - 101)) & ~0x3f) == 0 && ((1L << (_la - 101)) & 8185L) != 0) ) {
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
	public static class ExpressionContext extends ParserRuleContext {
		public List<AssignmentExpressionContext> assignmentExpression() {
			return getRuleContexts(AssignmentExpressionContext.class);
		}
		public AssignmentExpressionContext assignmentExpression(int i) {
			return getRuleContext(AssignmentExpressionContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(825);
			assignmentExpression();
			setState(830);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(826);
				match(Comma);
				setState(827);
				assignmentExpression();
				}
				}
				setState(832);
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
	public static class ConstantExpressionContext extends ParserRuleContext {
		public ConditionalExpressionContext conditionalExpression() {
			return getRuleContext(ConditionalExpressionContext.class,0);
		}
		public ConstantExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constantExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConstantExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConstantExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitConstantExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstantExpressionContext constantExpression() throws RecognitionException {
		ConstantExpressionContext _localctx = new ConstantExpressionContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_constantExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(833);
			conditionalExpression();
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
	public static class StatementContext extends ParserRuleContext {
		public LabeledStatementContext labeledStatement() {
			return getRuleContext(LabeledStatementContext.class,0);
		}
		public DeclarationStatementContext declarationStatement() {
			return getRuleContext(DeclarationStatementContext.class,0);
		}
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public SelectionStatementContext selectionStatement() {
			return getRuleContext(SelectionStatementContext.class,0);
		}
		public IterationStatementContext iterationStatement() {
			return getRuleContext(IterationStatementContext.class,0);
		}
		public JumpStatementContext jumpStatement() {
			return getRuleContext(JumpStatementContext.class,0);
		}
		public TryBlockContext tryBlock() {
			return getRuleContext(TryBlockContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_statement);
		try {
			setState(848);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,75,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(835);
				labeledStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(836);
				declarationStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(838);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,73,_ctx) ) {
				case 1:
					{
					setState(837);
					attributeSpecifierSeq();
					}
					break;
				}
				setState(846);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IntegerLiteral:
				case CharacterLiteral:
				case FloatingLiteral:
				case StringLiteral:
				case BooleanLiteral:
				case PointerLiteral:
				case UserDefinedLiteral:
				case Alignof:
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Const_cast:
				case Decltype:
				case Delete:
				case Double:
				case Dynamic_cast:
				case Float:
				case Int:
				case Long:
				case New:
				case Noexcept:
				case Operator:
				case Reinterpret_cast:
				case Short:
				case Signed:
				case Sizeof:
				case Static_cast:
				case This:
				case Throw:
				case Typeid_:
				case Typename_:
				case Unsigned:
				case Void:
				case Wchar:
				case LeftParen:
				case LeftBracket:
				case Plus:
				case Minus:
				case Star:
				case And:
				case Or:
				case Tilde:
				case Not:
				case PlusPlus:
				case MinusMinus:
				case Doublecolon:
				case Semi:
				case Identifier:
					{
					setState(840);
					expressionStatement();
					}
					break;
				case LeftBrace:
					{
					setState(841);
					compoundStatement();
					}
					break;
				case If:
				case Switch:
					{
					setState(842);
					selectionStatement();
					}
					break;
				case Do:
				case For:
				case While:
					{
					setState(843);
					iterationStatement();
					}
					break;
				case Break:
				case Continue:
				case Goto:
				case Return:
					{
					setState(844);
					jumpStatement();
					}
					break;
				case Try:
					{
					setState(845);
					tryBlock();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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
	public static class LabeledStatementContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Case() { return getToken(CPP14Parser.Case, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Default() { return getToken(CPP14Parser.Default, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public LabeledStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_labeledStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLabeledStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLabeledStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLabeledStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LabeledStatementContext labeledStatement() throws RecognitionException {
		LabeledStatementContext _localctx = new LabeledStatementContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_labeledStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(851);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(850);
				attributeSpecifierSeq();
				}
			}

			setState(857);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				{
				setState(853);
				match(Identifier);
				}
				break;
			case Case:
				{
				setState(854);
				match(Case);
				setState(855);
				constantExpression();
				}
				break;
			case Default:
				{
				setState(856);
				match(Default);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(859);
			match(Colon);
			setState(860);
			statement();
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
	public static class ExpressionStatementContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExpressionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionStatementContext expressionStatement() throws RecognitionException {
		ExpressionStatementContext _localctx = new ExpressionStatementContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_expressionStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(863);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133201L) != 0 || _la==Identifier) {
				{
				setState(862);
				expression();
				}
			}

			setState(865);
			match(Semi);
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
	public static class CompoundStatementContext extends ParserRuleContext {
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public StatementSeqContext statementSeq() {
			return getRuleContext(StatementSeqContext.class,0);
		}
		public CompoundStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compoundStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCompoundStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCompoundStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCompoundStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundStatementContext compoundStatement() throws RecognitionException {
		CompoundStatementContext _localctx = new CompoundStatementContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_compoundStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(867);
			match(LeftBrace);
			setState(869);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & -137360239606498050L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -8989184726396829969L) != 0 || (((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 25L) != 0) {
				{
				setState(868);
				statementSeq();
				}
			}

			setState(871);
			match(RightBrace);
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
	public static class StatementSeqContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public StatementSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statementSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStatementSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStatementSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitStatementSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementSeqContext statementSeq() throws RecognitionException {
		StatementSeqContext _localctx = new StatementSeqContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_statementSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(874); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(873);
				statement();
				}
				}
				setState(876); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((_la) & ~0x3f) == 0 && ((1L << _la) & -137360239606498050L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -8989184726396829969L) != 0 || (((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 25L) != 0 );
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
	public static class SelectionStatementContext extends ParserRuleContext {
		public TerminalNode If() { return getToken(CPP14Parser.If, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode Else() { return getToken(CPP14Parser.Else, 0); }
		public TerminalNode Switch() { return getToken(CPP14Parser.Switch, 0); }
		public SelectionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selectionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSelectionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSelectionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSelectionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectionStatementContext selectionStatement() throws RecognitionException {
		SelectionStatementContext _localctx = new SelectionStatementContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_selectionStatement);
		try {
			setState(893);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case If:
				enterOuterAlt(_localctx, 1);
				{
				setState(878);
				match(If);
				setState(879);
				match(LeftParen);
				setState(880);
				condition();
				setState(881);
				match(RightParen);
				setState(882);
				statement();
				setState(885);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,81,_ctx) ) {
				case 1:
					{
					setState(883);
					match(Else);
					setState(884);
					statement();
					}
					break;
				}
				}
				break;
			case Switch:
				enterOuterAlt(_localctx, 2);
				{
				setState(887);
				match(Switch);
				setState(888);
				match(LeftParen);
				setState(889);
				condition();
				setState(890);
				match(RightParen);
				setState(891);
				statement();
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
	public static class ConditionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ConditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCondition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCondition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCondition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConditionContext condition() throws RecognitionException {
		ConditionContext _localctx = new ConditionContext(_ctx, getState());
		enterRule(_localctx, 106, RULE_condition);
		int _la;
		try {
			setState(906);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,85,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(895);
				expression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(897);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(896);
					attributeSpecifierSeq();
					}
				}

				setState(899);
				declSpecifierSeq();
				setState(900);
				declarator();
				setState(904);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Assign:
					{
					setState(901);
					match(Assign);
					setState(902);
					initializerClause();
					}
					break;
				case LeftBrace:
					{
					setState(903);
					bracedInitList();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
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
	public static class IterationStatementContext extends ParserRuleContext {
		public TerminalNode While() { return getToken(CPP14Parser.While, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConditionContext condition() {
			return getRuleContext(ConditionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public TerminalNode Do() { return getToken(CPP14Parser.Do, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode For() { return getToken(CPP14Parser.For, 0); }
		public ForInitStatementContext forInitStatement() {
			return getRuleContext(ForInitStatementContext.class,0);
		}
		public ForRangeDeclarationContext forRangeDeclaration() {
			return getRuleContext(ForRangeDeclarationContext.class,0);
		}
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public ForRangeInitializerContext forRangeInitializer() {
			return getRuleContext(ForRangeInitializerContext.class,0);
		}
		public IterationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iterationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterIterationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitIterationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitIterationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IterationStatementContext iterationStatement() throws RecognitionException {
		IterationStatementContext _localctx = new IterationStatementContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_iterationStatement);
		int _la;
		try {
			setState(941);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case While:
				enterOuterAlt(_localctx, 1);
				{
				setState(908);
				match(While);
				setState(909);
				match(LeftParen);
				setState(910);
				condition();
				setState(911);
				match(RightParen);
				setState(912);
				statement();
				}
				break;
			case Do:
				enterOuterAlt(_localctx, 2);
				{
				setState(914);
				match(Do);
				setState(915);
				statement();
				setState(916);
				match(While);
				setState(917);
				match(LeftParen);
				setState(918);
				expression();
				setState(919);
				match(RightParen);
				setState(920);
				match(Semi);
				}
				break;
			case For:
				enterOuterAlt(_localctx, 3);
				{
				setState(922);
				match(For);
				setState(923);
				match(LeftParen);
				setState(936);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,88,_ctx) ) {
				case 1:
					{
					setState(924);
					forInitStatement();
					setState(926);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((_la) & ~0x3f) == 0 && ((1L << _la) & -714116761242538754L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384301683L) != 0 || _la==Identifier) {
						{
						setState(925);
						condition();
						}
					}

					setState(928);
					match(Semi);
					setState(930);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133201L) != 0 || _la==Identifier) {
						{
						setState(929);
						expression();
						}
					}

					}
					break;
				case 2:
					{
					setState(932);
					forRangeDeclaration();
					setState(933);
					match(Colon);
					setState(934);
					forRangeInitializer();
					}
					break;
				}
				setState(938);
				match(RightParen);
				setState(939);
				statement();
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
	public static class ForInitStatementContext extends ParserRuleContext {
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public SimpleDeclarationContext simpleDeclaration() {
			return getRuleContext(SimpleDeclarationContext.class,0);
		}
		public ForInitStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInitStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterForInitStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitForInitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitForInitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitStatementContext forInitStatement() throws RecognitionException {
		ForInitStatementContext _localctx = new ForInitStatementContext(_ctx, getState());
		enterRule(_localctx, 110, RULE_forInitStatement);
		try {
			setState(945);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,90,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(943);
				expressionStatement();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(944);
				simpleDeclaration();
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
	public static class ForRangeDeclarationContext extends ParserRuleContext {
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ForRangeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forRangeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterForRangeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitForRangeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitForRangeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForRangeDeclarationContext forRangeDeclaration() throws RecognitionException {
		ForRangeDeclarationContext _localctx = new ForRangeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 112, RULE_forRangeDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(948);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(947);
				attributeSpecifierSeq();
				}
			}

			setState(950);
			declSpecifierSeq();
			setState(951);
			declarator();
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
	public static class ForRangeInitializerContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public ForRangeInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forRangeInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterForRangeInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitForRangeInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitForRangeInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForRangeInitializerContext forRangeInitializer() throws RecognitionException {
		ForRangeInitializerContext _localctx = new ForRangeInitializerContext(_ctx, getState());
		enterRule(_localctx, 114, RULE_forRangeInitializer);
		try {
			setState(955);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case Alignof:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Const_cast:
			case Decltype:
			case Delete:
			case Double:
			case Dynamic_cast:
			case Float:
			case Int:
			case Long:
			case New:
			case Noexcept:
			case Operator:
			case Reinterpret_cast:
			case Short:
			case Signed:
			case Sizeof:
			case Static_cast:
			case This:
			case Throw:
			case Typeid_:
			case Typename_:
			case Unsigned:
			case Void:
			case Wchar:
			case LeftParen:
			case LeftBracket:
			case Plus:
			case Minus:
			case Star:
			case And:
			case Or:
			case Tilde:
			case Not:
			case PlusPlus:
			case MinusMinus:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(953);
				expression();
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(954);
				bracedInitList();
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
	public static class JumpStatementContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode Break() { return getToken(CPP14Parser.Break, 0); }
		public TerminalNode Continue() { return getToken(CPP14Parser.Continue, 0); }
		public TerminalNode Return() { return getToken(CPP14Parser.Return, 0); }
		public TerminalNode Goto() { return getToken(CPP14Parser.Goto, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public JumpStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jumpStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterJumpStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitJumpStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitJumpStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JumpStatementContext jumpStatement() throws RecognitionException {
		JumpStatementContext _localctx = new JumpStatementContext(_ctx, getState());
		enterRule(_localctx, 116, RULE_jumpStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(966);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Break:
				{
				setState(957);
				match(Break);
				}
				break;
			case Continue:
				{
				setState(958);
				match(Continue);
				}
				break;
			case Return:
				{
				setState(959);
				match(Return);
				setState(962);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IntegerLiteral:
				case CharacterLiteral:
				case FloatingLiteral:
				case StringLiteral:
				case BooleanLiteral:
				case PointerLiteral:
				case UserDefinedLiteral:
				case Alignof:
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Const_cast:
				case Decltype:
				case Delete:
				case Double:
				case Dynamic_cast:
				case Float:
				case Int:
				case Long:
				case New:
				case Noexcept:
				case Operator:
				case Reinterpret_cast:
				case Short:
				case Signed:
				case Sizeof:
				case Static_cast:
				case This:
				case Throw:
				case Typeid_:
				case Typename_:
				case Unsigned:
				case Void:
				case Wchar:
				case LeftParen:
				case LeftBracket:
				case Plus:
				case Minus:
				case Star:
				case And:
				case Or:
				case Tilde:
				case Not:
				case PlusPlus:
				case MinusMinus:
				case Doublecolon:
				case Identifier:
					{
					setState(960);
					expression();
					}
					break;
				case LeftBrace:
					{
					setState(961);
					bracedInitList();
					}
					break;
				case Semi:
					break;
				default:
					break;
				}
				}
				break;
			case Goto:
				{
				setState(964);
				match(Goto);
				setState(965);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(968);
			match(Semi);
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
	public static class DeclarationStatementContext extends ParserRuleContext {
		public BlockDeclarationContext blockDeclaration() {
			return getRuleContext(BlockDeclarationContext.class,0);
		}
		public DeclarationStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclarationStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclarationStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclarationStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationStatementContext declarationStatement() throws RecognitionException {
		DeclarationStatementContext _localctx = new DeclarationStatementContext(_ctx, getState());
		enterRule(_localctx, 118, RULE_declarationStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(970);
			blockDeclaration();
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
	public static class DeclarationseqContext extends ParserRuleContext {
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public DeclarationseqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarationseq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclarationseq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclarationseq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclarationseq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationseqContext declarationseq() throws RecognitionException {
		DeclarationseqContext _localctx = new DeclarationseqContext(_ctx, getState());
		enterRule(_localctx, 120, RULE_declarationseq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(973); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(972);
				declaration();
				}
				}
				setState(975); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0 );
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
	public static class DeclarationContext extends ParserRuleContext {
		public BlockDeclarationContext blockDeclaration() {
			return getRuleContext(BlockDeclarationContext.class,0);
		}
		public FunctionDefinitionContext functionDefinition() {
			return getRuleContext(FunctionDefinitionContext.class,0);
		}
		public TemplateDeclarationContext templateDeclaration() {
			return getRuleContext(TemplateDeclarationContext.class,0);
		}
		public ExplicitInstantiationContext explicitInstantiation() {
			return getRuleContext(ExplicitInstantiationContext.class,0);
		}
		public ExplicitSpecializationContext explicitSpecialization() {
			return getRuleContext(ExplicitSpecializationContext.class,0);
		}
		public LinkageSpecificationContext linkageSpecification() {
			return getRuleContext(LinkageSpecificationContext.class,0);
		}
		public NamespaceDefinitionContext namespaceDefinition() {
			return getRuleContext(NamespaceDefinitionContext.class,0);
		}
		public EmptyDeclarationContext emptyDeclaration() {
			return getRuleContext(EmptyDeclarationContext.class,0);
		}
		public AttributeDeclarationContext attributeDeclaration() {
			return getRuleContext(AttributeDeclarationContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 122, RULE_declaration);
		try {
			setState(986);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,96,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(977);
				blockDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(978);
				functionDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(979);
				templateDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(980);
				explicitInstantiation();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(981);
				explicitSpecialization();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(982);
				linkageSpecification();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(983);
				namespaceDefinition();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(984);
				emptyDeclaration();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(985);
				attributeDeclaration();
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
	public static class BlockDeclarationContext extends ParserRuleContext {
		public SimpleDeclarationContext simpleDeclaration() {
			return getRuleContext(SimpleDeclarationContext.class,0);
		}
		public AsmDefinitionContext asmDefinition() {
			return getRuleContext(AsmDefinitionContext.class,0);
		}
		public NamespaceAliasDefinitionContext namespaceAliasDefinition() {
			return getRuleContext(NamespaceAliasDefinitionContext.class,0);
		}
		public UsingDeclarationContext usingDeclaration() {
			return getRuleContext(UsingDeclarationContext.class,0);
		}
		public UsingDirectiveContext usingDirective() {
			return getRuleContext(UsingDirectiveContext.class,0);
		}
		public StaticAssertDeclarationContext staticAssertDeclaration() {
			return getRuleContext(StaticAssertDeclarationContext.class,0);
		}
		public AliasDeclarationContext aliasDeclaration() {
			return getRuleContext(AliasDeclarationContext.class,0);
		}
		public OpaqueEnumDeclarationContext opaqueEnumDeclaration() {
			return getRuleContext(OpaqueEnumDeclarationContext.class,0);
		}
		public BlockDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBlockDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBlockDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBlockDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockDeclarationContext blockDeclaration() throws RecognitionException {
		BlockDeclarationContext _localctx = new BlockDeclarationContext(_ctx, getState());
		enterRule(_localctx, 124, RULE_blockDeclaration);
		try {
			setState(996);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,97,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(988);
				simpleDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(989);
				asmDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(990);
				namespaceAliasDefinition();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(991);
				usingDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(992);
				usingDirective();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(993);
				staticAssertDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(994);
				aliasDeclaration();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(995);
				opaqueEnumDeclaration();
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
	public static class AliasDeclarationContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(CPP14Parser.Using, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public AliasDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_aliasDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAliasDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAliasDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAliasDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AliasDeclarationContext aliasDeclaration() throws RecognitionException {
		AliasDeclarationContext _localctx = new AliasDeclarationContext(_ctx, getState());
		enterRule(_localctx, 126, RULE_aliasDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(998);
			match(Using);
			setState(999);
			match(Identifier);
			setState(1001);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1000);
				attributeSpecifierSeq();
				}
			}

			setState(1003);
			match(Assign);
			setState(1004);
			theTypeId();
			setState(1005);
			match(Semi);
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
	public static class SimpleDeclarationContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public InitDeclaratorListContext initDeclaratorList() {
			return getRuleContext(InitDeclaratorListContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public SimpleDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSimpleDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleDeclarationContext simpleDeclaration() throws RecognitionException {
		SimpleDeclarationContext _localctx = new SimpleDeclarationContext(_ctx, getState());
		enterRule(_localctx, 128, RULE_simpleDeclaration);
		int _la;
		try {
			setState(1021);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Decltype:
			case Double:
			case Enum:
			case Explicit:
			case Extern:
			case Float:
			case Friend:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Operator:
			case Register:
			case Short:
			case Signed:
			case Static:
			case Struct:
			case Thread_local:
			case Typedef:
			case Typename_:
			case Union:
			case Unsigned:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case LeftParen:
			case Star:
			case And:
			case Tilde:
			case AndAnd:
			case Doublecolon:
			case Semi:
			case Ellipsis:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1008);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,99,_ctx) ) {
				case 1:
					{
					setState(1007);
					declSpecifierSeq();
					}
					break;
				}
				setState(1011);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Operator || (((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 215512868999425L) != 0) {
					{
					setState(1010);
					initDeclaratorList();
					}
				}

				setState(1013);
				match(Semi);
				}
				break;
			case Alignas:
			case LeftBracket:
				enterOuterAlt(_localctx, 2);
				{
				setState(1014);
				attributeSpecifierSeq();
				setState(1016);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,101,_ctx) ) {
				case 1:
					{
					setState(1015);
					declSpecifierSeq();
					}
					break;
				}
				setState(1018);
				initDeclaratorList();
				setState(1019);
				match(Semi);
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
	public static class StaticAssertDeclarationContext extends ParserRuleContext {
		public TerminalNode Static_assert() { return getToken(CPP14Parser.Static_assert, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public StaticAssertDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_staticAssertDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStaticAssertDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStaticAssertDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitStaticAssertDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StaticAssertDeclarationContext staticAssertDeclaration() throws RecognitionException {
		StaticAssertDeclarationContext _localctx = new StaticAssertDeclarationContext(_ctx, getState());
		enterRule(_localctx, 130, RULE_staticAssertDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1023);
			match(Static_assert);
			setState(1024);
			match(LeftParen);
			setState(1025);
			constantExpression();
			setState(1026);
			match(Comma);
			setState(1027);
			match(StringLiteral);
			setState(1028);
			match(RightParen);
			setState(1029);
			match(Semi);
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
	public static class EmptyDeclarationContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public EmptyDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptyDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEmptyDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEmptyDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEmptyDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptyDeclarationContext emptyDeclaration() throws RecognitionException {
		EmptyDeclarationContext _localctx = new EmptyDeclarationContext(_ctx, getState());
		enterRule(_localctx, 132, RULE_emptyDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1031);
			match(Semi);
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
	public static class AttributeDeclarationContext extends ParserRuleContext {
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttributeDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeDeclarationContext attributeDeclaration() throws RecognitionException {
		AttributeDeclarationContext _localctx = new AttributeDeclarationContext(_ctx, getState());
		enterRule(_localctx, 134, RULE_attributeDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1033);
			attributeSpecifierSeq();
			setState(1034);
			match(Semi);
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
	public static class DeclSpecifierContext extends ParserRuleContext {
		public StorageClassSpecifierContext storageClassSpecifier() {
			return getRuleContext(StorageClassSpecifierContext.class,0);
		}
		public TypeSpecifierContext typeSpecifier() {
			return getRuleContext(TypeSpecifierContext.class,0);
		}
		public FunctionSpecifierContext functionSpecifier() {
			return getRuleContext(FunctionSpecifierContext.class,0);
		}
		public TerminalNode Friend() { return getToken(CPP14Parser.Friend, 0); }
		public TerminalNode Typedef() { return getToken(CPP14Parser.Typedef, 0); }
		public TerminalNode Constexpr() { return getToken(CPP14Parser.Constexpr, 0); }
		public DeclSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclSpecifierContext declSpecifier() throws RecognitionException {
		DeclSpecifierContext _localctx = new DeclSpecifierContext(_ctx, getState());
		enterRule(_localctx, 136, RULE_declSpecifier);
		try {
			setState(1042);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Extern:
			case Mutable:
			case Register:
			case Static:
			case Thread_local:
				enterOuterAlt(_localctx, 1);
				{
				setState(1036);
				storageClassSpecifier();
				}
				break;
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Decltype:
			case Double:
			case Enum:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Struct:
			case Typename_:
			case Union:
			case Unsigned:
			case Void:
			case Volatile:
			case Wchar:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1037);
				typeSpecifier();
				}
				break;
			case Explicit:
			case Inline:
			case Virtual:
				enterOuterAlt(_localctx, 3);
				{
				setState(1038);
				functionSpecifier();
				}
				break;
			case Friend:
				enterOuterAlt(_localctx, 4);
				{
				setState(1039);
				match(Friend);
				}
				break;
			case Typedef:
				enterOuterAlt(_localctx, 5);
				{
				setState(1040);
				match(Typedef);
				}
				break;
			case Constexpr:
				enterOuterAlt(_localctx, 6);
				{
				setState(1041);
				match(Constexpr);
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
	public static class DeclSpecifierSeqContext extends ParserRuleContext {
		public List<DeclSpecifierContext> declSpecifier() {
			return getRuleContexts(DeclSpecifierContext.class);
		}
		public DeclSpecifierContext declSpecifier(int i) {
			return getRuleContext(DeclSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclSpecifierSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclSpecifierSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclSpecifierSeqContext declSpecifierSeq() throws RecognitionException {
		DeclSpecifierSeqContext _localctx = new DeclSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 138, RULE_declSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1045); 
			_errHandler.sync(this);
			_alt = 1+1;
			do {
				switch (_alt) {
				case 1+1:
					{
					{
					setState(1044);
					declSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1047); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,104,_ctx);
			} while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1050);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,105,_ctx) ) {
			case 1:
				{
				setState(1049);
				attributeSpecifierSeq();
				}
				break;
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
	public static class StorageClassSpecifierContext extends ParserRuleContext {
		public TerminalNode Register() { return getToken(CPP14Parser.Register, 0); }
		public TerminalNode Static() { return getToken(CPP14Parser.Static, 0); }
		public TerminalNode Thread_local() { return getToken(CPP14Parser.Thread_local, 0); }
		public TerminalNode Extern() { return getToken(CPP14Parser.Extern, 0); }
		public TerminalNode Mutable() { return getToken(CPP14Parser.Mutable, 0); }
		public StorageClassSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_storageClassSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterStorageClassSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitStorageClassSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitStorageClassSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StorageClassSpecifierContext storageClassSpecifier() throws RecognitionException {
		StorageClassSpecifierContext _localctx = new StorageClassSpecifierContext(_ctx, getState());
		enterRule(_localctx, 140, RULE_storageClassSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1052);
			_la = _input.LA(1);
			if ( !((((_la - 36)) & ~0x3f) == 0 && ((1L << (_la - 36)) & 17316186113L) != 0) ) {
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
	public static class FunctionSpecifierContext extends ParserRuleContext {
		public TerminalNode Inline() { return getToken(CPP14Parser.Inline, 0); }
		public TerminalNode Virtual() { return getToken(CPP14Parser.Virtual, 0); }
		public TerminalNode Explicit() { return getToken(CPP14Parser.Explicit, 0); }
		public FunctionSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitFunctionSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionSpecifierContext functionSpecifier() throws RecognitionException {
		FunctionSpecifierContext _localctx = new FunctionSpecifierContext(_ctx, getState());
		enterRule(_localctx, 142, RULE_functionSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1054);
			_la = _input.LA(1);
			if ( !((((_la - 34)) & ~0x3f) == 0 && ((1L << (_la - 34)) & 70368744178689L) != 0) ) {
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
	public static class TypedefNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TypedefNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typedefName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypedefName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypedefName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypedefName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypedefNameContext typedefName() throws RecognitionException {
		TypedefNameContext _localctx = new TypedefNameContext(_ctx, getState());
		enterRule(_localctx, 144, RULE_typedefName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1056);
			match(Identifier);
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
	public static class TypeSpecifierContext extends ParserRuleContext {
		public TrailingTypeSpecifierContext trailingTypeSpecifier() {
			return getRuleContext(TrailingTypeSpecifierContext.class,0);
		}
		public ClassSpecifierContext classSpecifier() {
			return getRuleContext(ClassSpecifierContext.class,0);
		}
		public EnumSpecifierContext enumSpecifier() {
			return getRuleContext(EnumSpecifierContext.class,0);
		}
		public TypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
		TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 146, RULE_typeSpecifier);
		try {
			setState(1061);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,106,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1058);
				trailingTypeSpecifier();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1059);
				classSpecifier();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1060);
				enumSpecifier();
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
	public static class TrailingTypeSpecifierContext extends ParserRuleContext {
		public SimpleTypeSpecifierContext simpleTypeSpecifier() {
			return getRuleContext(SimpleTypeSpecifierContext.class,0);
		}
		public ElaboratedTypeSpecifierContext elaboratedTypeSpecifier() {
			return getRuleContext(ElaboratedTypeSpecifierContext.class,0);
		}
		public TypeNameSpecifierContext typeNameSpecifier() {
			return getRuleContext(TypeNameSpecifierContext.class,0);
		}
		public CvQualifierContext cvQualifier() {
			return getRuleContext(CvQualifierContext.class,0);
		}
		public TrailingTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailingTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTrailingTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTrailingTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTrailingTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrailingTypeSpecifierContext trailingTypeSpecifier() throws RecognitionException {
		TrailingTypeSpecifierContext _localctx = new TrailingTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 148, RULE_trailingTypeSpecifier);
		try {
			setState(1067);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Decltype:
			case Double:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Unsigned:
			case Void:
			case Wchar:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1063);
				simpleTypeSpecifier();
				}
				break;
			case Class:
			case Enum:
			case Struct:
				enterOuterAlt(_localctx, 2);
				{
				setState(1064);
				elaboratedTypeSpecifier();
				}
				break;
			case Typename_:
				enterOuterAlt(_localctx, 3);
				{
				setState(1065);
				typeNameSpecifier();
				}
				break;
			case Const:
			case Volatile:
				enterOuterAlt(_localctx, 4);
				{
				setState(1066);
				cvQualifier();
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
	public static class TypeSpecifierSeqContext extends ParserRuleContext {
		public List<TypeSpecifierContext> typeSpecifier() {
			return getRuleContexts(TypeSpecifierContext.class);
		}
		public TypeSpecifierContext typeSpecifier(int i) {
			return getRuleContext(TypeSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TypeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeSpecifierSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypeSpecifierSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeSpecifierSeqContext typeSpecifierSeq() throws RecognitionException {
		TypeSpecifierSeqContext _localctx = new TypeSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 150, RULE_typeSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1070); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1069);
					typeSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1072); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,108,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1075);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,109,_ctx) ) {
			case 1:
				{
				setState(1074);
				attributeSpecifierSeq();
				}
				break;
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
	public static class TrailingTypeSpecifierSeqContext extends ParserRuleContext {
		public List<TrailingTypeSpecifierContext> trailingTypeSpecifier() {
			return getRuleContexts(TrailingTypeSpecifierContext.class);
		}
		public TrailingTypeSpecifierContext trailingTypeSpecifier(int i) {
			return getRuleContext(TrailingTypeSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TrailingTypeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailingTypeSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTrailingTypeSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTrailingTypeSpecifierSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTrailingTypeSpecifierSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrailingTypeSpecifierSeqContext trailingTypeSpecifierSeq() throws RecognitionException {
		TrailingTypeSpecifierSeqContext _localctx = new TrailingTypeSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 152, RULE_trailingTypeSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1078); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1077);
					trailingTypeSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1080); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,110,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			setState(1083);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,111,_ctx) ) {
			case 1:
				{
				setState(1082);
				attributeSpecifierSeq();
				}
				break;
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
	public static class SimpleTypeLengthModifierContext extends ParserRuleContext {
		public TerminalNode Short() { return getToken(CPP14Parser.Short, 0); }
		public TerminalNode Long() { return getToken(CPP14Parser.Long, 0); }
		public SimpleTypeLengthModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeLengthModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTypeLengthModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTypeLengthModifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSimpleTypeLengthModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeLengthModifierContext simpleTypeLengthModifier() throws RecognitionException {
		SimpleTypeLengthModifierContext _localctx = new SimpleTypeLengthModifierContext(_ctx, getState());
		enterRule(_localctx, 154, RULE_simpleTypeLengthModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1085);
			_la = _input.LA(1);
			if ( !(_la==Long || _la==Short) ) {
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
	public static class SimpleTypeSignednessModifierContext extends ParserRuleContext {
		public TerminalNode Unsigned() { return getToken(CPP14Parser.Unsigned, 0); }
		public TerminalNode Signed() { return getToken(CPP14Parser.Signed, 0); }
		public SimpleTypeSignednessModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeSignednessModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTypeSignednessModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTypeSignednessModifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSimpleTypeSignednessModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeSignednessModifierContext simpleTypeSignednessModifier() throws RecognitionException {
		SimpleTypeSignednessModifierContext _localctx = new SimpleTypeSignednessModifierContext(_ctx, getState());
		enterRule(_localctx, 156, RULE_simpleTypeSignednessModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1087);
			_la = _input.LA(1);
			if ( !(_la==Signed || _la==Unsigned) ) {
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
	public static class SimpleTypeSpecifierContext extends ParserRuleContext {
		public TheTypeNameContext theTypeName() {
			return getRuleContext(TheTypeNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public SimpleTypeSignednessModifierContext simpleTypeSignednessModifier() {
			return getRuleContext(SimpleTypeSignednessModifierContext.class,0);
		}
		public List<SimpleTypeLengthModifierContext> simpleTypeLengthModifier() {
			return getRuleContexts(SimpleTypeLengthModifierContext.class);
		}
		public SimpleTypeLengthModifierContext simpleTypeLengthModifier(int i) {
			return getRuleContext(SimpleTypeLengthModifierContext.class,i);
		}
		public TerminalNode Char() { return getToken(CPP14Parser.Char, 0); }
		public TerminalNode Char16() { return getToken(CPP14Parser.Char16, 0); }
		public TerminalNode Char32() { return getToken(CPP14Parser.Char32, 0); }
		public TerminalNode Wchar() { return getToken(CPP14Parser.Wchar, 0); }
		public TerminalNode Bool() { return getToken(CPP14Parser.Bool, 0); }
		public TerminalNode Int() { return getToken(CPP14Parser.Int, 0); }
		public TerminalNode Float() { return getToken(CPP14Parser.Float, 0); }
		public TerminalNode Double() { return getToken(CPP14Parser.Double, 0); }
		public TerminalNode Void() { return getToken(CPP14Parser.Void, 0); }
		public TerminalNode Auto() { return getToken(CPP14Parser.Auto, 0); }
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public SimpleTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSimpleTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTypeSpecifierContext simpleTypeSpecifier() throws RecognitionException {
		SimpleTypeSpecifierContext _localctx = new SimpleTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 158, RULE_simpleTypeSpecifier);
		int _la;
		try {
			int _alt;
			setState(1141);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,122,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1090);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,112,_ctx) ) {
				case 1:
					{
					setState(1089);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1092);
				theTypeName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1093);
				nestedNameSpecifier(0);
				setState(1094);
				match(Template);
				setState(1095);
				simpleTemplateId();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1097);
				simpleTypeSignednessModifier();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1099);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1098);
					simpleTypeSignednessModifier();
					}
				}

				setState(1102); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1101);
						simpleTypeLengthModifier();
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1104); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,114,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1107);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1106);
					simpleTypeSignednessModifier();
					}
				}

				setState(1109);
				match(Char);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1111);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1110);
					simpleTypeSignednessModifier();
					}
				}

				setState(1113);
				match(Char16);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1115);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1114);
					simpleTypeSignednessModifier();
					}
				}

				setState(1117);
				match(Char32);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(1119);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1118);
					simpleTypeSignednessModifier();
					}
				}

				setState(1121);
				match(Wchar);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(1122);
				match(Bool);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(1124);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Signed || _la==Unsigned) {
					{
					setState(1123);
					simpleTypeSignednessModifier();
					}
				}

				setState(1129);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==Long || _la==Short) {
					{
					{
					setState(1126);
					simpleTypeLengthModifier();
					}
					}
					setState(1131);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(1132);
				match(Int);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(1133);
				match(Float);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(1135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Long || _la==Short) {
					{
					setState(1134);
					simpleTypeLengthModifier();
					}
				}

				setState(1137);
				match(Double);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(1138);
				match(Void);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(1139);
				match(Auto);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(1140);
				decltypeSpecifier();
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
	public static class TheTypeNameContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public EnumNameContext enumName() {
			return getRuleContext(EnumNameContext.class,0);
		}
		public TypedefNameContext typedefName() {
			return getRuleContext(TypedefNameContext.class,0);
		}
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TheTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTheTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTheTypeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTheTypeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TheTypeNameContext theTypeName() throws RecognitionException {
		TheTypeNameContext _localctx = new TheTypeNameContext(_ctx, getState());
		enterRule(_localctx, 160, RULE_theTypeName);
		try {
			setState(1147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,123,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1143);
				className();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1144);
				enumName();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1145);
				typedefName();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1146);
				simpleTemplateId();
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
	public static class DecltypeSpecifierContext extends ParserRuleContext {
		public TerminalNode Decltype() { return getToken(CPP14Parser.Decltype, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode Auto() { return getToken(CPP14Parser.Auto, 0); }
		public DecltypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decltypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDecltypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDecltypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDecltypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecltypeSpecifierContext decltypeSpecifier() throws RecognitionException {
		DecltypeSpecifierContext _localctx = new DecltypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 162, RULE_decltypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1149);
			match(Decltype);
			setState(1150);
			match(LeftParen);
			setState(1153);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,124,_ctx) ) {
			case 1:
				{
				setState(1151);
				expression();
				}
				break;
			case 2:
				{
				setState(1152);
				match(Auto);
				}
				break;
			}
			setState(1155);
			match(RightParen);
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
	public static class ElaboratedTypeSpecifierContext extends ParserRuleContext {
		public ClassKeyContext classKey() {
			return getRuleContext(ClassKeyContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Enum() { return getToken(CPP14Parser.Enum, 0); }
		public ElaboratedTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elaboratedTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterElaboratedTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitElaboratedTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitElaboratedTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElaboratedTypeSpecifierContext elaboratedTypeSpecifier() throws RecognitionException {
		ElaboratedTypeSpecifierContext _localctx = new ElaboratedTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 164, RULE_elaboratedTypeSpecifier);
		int _la;
		try {
			setState(1179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Class:
			case Struct:
				enterOuterAlt(_localctx, 1);
				{
				setState(1157);
				classKey();
				setState(1172);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,128,_ctx) ) {
				case 1:
					{
					setState(1159);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Alignas || _la==LeftBracket) {
						{
						setState(1158);
						attributeSpecifierSeq();
						}
					}

					setState(1162);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,126,_ctx) ) {
					case 1:
						{
						setState(1161);
						nestedNameSpecifier(0);
						}
						break;
					}
					setState(1164);
					match(Identifier);
					}
					break;
				case 2:
					{
					setState(1165);
					simpleTemplateId();
					}
					break;
				case 3:
					{
					setState(1166);
					nestedNameSpecifier(0);
					setState(1168);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Template) {
						{
						setState(1167);
						match(Template);
						}
					}

					setState(1170);
					simpleTemplateId();
					}
					break;
				}
				}
				break;
			case Enum:
				enterOuterAlt(_localctx, 2);
				{
				setState(1174);
				match(Enum);
				setState(1176);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,129,_ctx) ) {
				case 1:
					{
					setState(1175);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1178);
				match(Identifier);
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
	public static class EnumNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public EnumNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumNameContext enumName() throws RecognitionException {
		EnumNameContext _localctx = new EnumNameContext(_ctx, getState());
		enterRule(_localctx, 166, RULE_enumName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1181);
			match(Identifier);
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
	public static class EnumSpecifierContext extends ParserRuleContext {
		public EnumHeadContext enumHead() {
			return getRuleContext(EnumHeadContext.class,0);
		}
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public EnumeratorListContext enumeratorList() {
			return getRuleContext(EnumeratorListContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public EnumSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumSpecifierContext enumSpecifier() throws RecognitionException {
		EnumSpecifierContext _localctx = new EnumSpecifierContext(_ctx, getState());
		enterRule(_localctx, 168, RULE_enumSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1183);
			enumHead();
			setState(1184);
			match(LeftBrace);
			setState(1189);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Identifier) {
				{
				setState(1185);
				enumeratorList();
				setState(1187);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(1186);
					match(Comma);
					}
				}

				}
			}

			setState(1191);
			match(RightBrace);
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
	public static class EnumHeadContext extends ParserRuleContext {
		public EnumkeyContext enumkey() {
			return getRuleContext(EnumkeyContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public EnumbaseContext enumbase() {
			return getRuleContext(EnumbaseContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public EnumHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumHeadContext enumHead() throws RecognitionException {
		EnumHeadContext _localctx = new EnumHeadContext(_ctx, getState());
		enterRule(_localctx, 170, RULE_enumHead);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1193);
			enumkey();
			setState(1195);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1194);
				attributeSpecifierSeq();
				}
			}

			setState(1201);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
				{
				setState(1198);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,134,_ctx) ) {
				case 1:
					{
					setState(1197);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1200);
				match(Identifier);
				}
			}

			setState(1204);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(1203);
				enumbase();
				}
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
	public static class OpaqueEnumDeclarationContext extends ParserRuleContext {
		public EnumkeyContext enumkey() {
			return getRuleContext(EnumkeyContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public EnumbaseContext enumbase() {
			return getRuleContext(EnumbaseContext.class,0);
		}
		public OpaqueEnumDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_opaqueEnumDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterOpaqueEnumDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitOpaqueEnumDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitOpaqueEnumDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OpaqueEnumDeclarationContext opaqueEnumDeclaration() throws RecognitionException {
		OpaqueEnumDeclarationContext _localctx = new OpaqueEnumDeclarationContext(_ctx, getState());
		enterRule(_localctx, 172, RULE_opaqueEnumDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1206);
			enumkey();
			setState(1208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1207);
				attributeSpecifierSeq();
				}
			}

			setState(1210);
			match(Identifier);
			setState(1212);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(1211);
				enumbase();
				}
			}

			setState(1214);
			match(Semi);
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
	public static class EnumkeyContext extends ParserRuleContext {
		public TerminalNode Enum() { return getToken(CPP14Parser.Enum, 0); }
		public TerminalNode Class() { return getToken(CPP14Parser.Class, 0); }
		public TerminalNode Struct() { return getToken(CPP14Parser.Struct, 0); }
		public EnumkeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumkey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumkey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumkey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumkey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumkeyContext enumkey() throws RecognitionException {
		EnumkeyContext _localctx = new EnumkeyContext(_ctx, getState());
		enterRule(_localctx, 174, RULE_enumkey);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1216);
			match(Enum);
			setState(1218);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Class || _la==Struct) {
				{
				setState(1217);
				_la = _input.LA(1);
				if ( !(_la==Class || _la==Struct) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
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
	public static class EnumbaseContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public EnumbaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumbase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumbase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumbase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumbase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumbaseContext enumbase() throws RecognitionException {
		EnumbaseContext _localctx = new EnumbaseContext(_ctx, getState());
		enterRule(_localctx, 176, RULE_enumbase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1220);
			match(Colon);
			setState(1221);
			typeSpecifierSeq();
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
	public static class EnumeratorListContext extends ParserRuleContext {
		public List<EnumeratorDefinitionContext> enumeratorDefinition() {
			return getRuleContexts(EnumeratorDefinitionContext.class);
		}
		public EnumeratorDefinitionContext enumeratorDefinition(int i) {
			return getRuleContext(EnumeratorDefinitionContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public EnumeratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumeratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumeratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumeratorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumeratorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumeratorListContext enumeratorList() throws RecognitionException {
		EnumeratorListContext _localctx = new EnumeratorListContext(_ctx, getState());
		enterRule(_localctx, 178, RULE_enumeratorList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1223);
			enumeratorDefinition();
			setState(1228);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1224);
					match(Comma);
					setState(1225);
					enumeratorDefinition();
					}
					} 
				}
				setState(1230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,140,_ctx);
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
	public static class EnumeratorDefinitionContext extends ParserRuleContext {
		public EnumeratorContext enumerator() {
			return getRuleContext(EnumeratorContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public EnumeratorDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumeratorDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumeratorDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumeratorDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumeratorDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumeratorDefinitionContext enumeratorDefinition() throws RecognitionException {
		EnumeratorDefinitionContext _localctx = new EnumeratorDefinitionContext(_ctx, getState());
		enterRule(_localctx, 180, RULE_enumeratorDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1231);
			enumerator();
			setState(1234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(1232);
				match(Assign);
				setState(1233);
				constantExpression();
				}
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
	public static class EnumeratorContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public EnumeratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumerator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterEnumerator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitEnumerator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitEnumerator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumeratorContext enumerator() throws RecognitionException {
		EnumeratorContext _localctx = new EnumeratorContext(_ctx, getState());
		enterRule(_localctx, 182, RULE_enumerator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1236);
			match(Identifier);
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
	public static class NamespaceNameContext extends ParserRuleContext {
		public OriginalNamespaceNameContext originalNamespaceName() {
			return getRuleContext(OriginalNamespaceNameContext.class,0);
		}
		public NamespaceAliasContext namespaceAlias() {
			return getRuleContext(NamespaceAliasContext.class,0);
		}
		public NamespaceNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNamespaceName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceNameContext namespaceName() throws RecognitionException {
		NamespaceNameContext _localctx = new NamespaceNameContext(_ctx, getState());
		enterRule(_localctx, 184, RULE_namespaceName);
		try {
			setState(1240);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,142,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1238);
				originalNamespaceName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1239);
				namespaceAlias();
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
	public static class OriginalNamespaceNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public OriginalNamespaceNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_originalNamespaceName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterOriginalNamespaceName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitOriginalNamespaceName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitOriginalNamespaceName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OriginalNamespaceNameContext originalNamespaceName() throws RecognitionException {
		OriginalNamespaceNameContext _localctx = new OriginalNamespaceNameContext(_ctx, getState());
		enterRule(_localctx, 186, RULE_originalNamespaceName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1242);
			match(Identifier);
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
	public static class NamespaceDefinitionContext extends ParserRuleContext {
		public DeclarationseqContext namespaceBody;
		public TerminalNode Namespace() { return getToken(CPP14Parser.Namespace, 0); }
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public TerminalNode Inline() { return getToken(CPP14Parser.Inline, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public OriginalNamespaceNameContext originalNamespaceName() {
			return getRuleContext(OriginalNamespaceNameContext.class,0);
		}
		public DeclarationseqContext declarationseq() {
			return getRuleContext(DeclarationseqContext.class,0);
		}
		public NamespaceDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNamespaceDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceDefinitionContext namespaceDefinition() throws RecognitionException {
		NamespaceDefinitionContext _localctx = new NamespaceDefinitionContext(_ctx, getState());
		enterRule(_localctx, 188, RULE_namespaceDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1245);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Inline) {
				{
				setState(1244);
				match(Inline);
				}
			}

			setState(1247);
			match(Namespace);
			setState(1250);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,144,_ctx) ) {
			case 1:
				{
				setState(1248);
				match(Identifier);
				}
				break;
			case 2:
				{
				setState(1249);
				originalNamespaceName();
				}
				break;
			}
			setState(1252);
			match(LeftBrace);
			setState(1254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0) {
				{
				setState(1253);
				((NamespaceDefinitionContext)_localctx).namespaceBody = declarationseq();
				}
			}

			setState(1256);
			match(RightBrace);
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
	public static class NamespaceAliasContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public NamespaceAliasContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceAlias; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceAlias(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceAlias(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNamespaceAlias(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceAliasContext namespaceAlias() throws RecognitionException {
		NamespaceAliasContext _localctx = new NamespaceAliasContext(_ctx, getState());
		enterRule(_localctx, 190, RULE_namespaceAlias);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1258);
			match(Identifier);
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
	public static class NamespaceAliasDefinitionContext extends ParserRuleContext {
		public TerminalNode Namespace() { return getToken(CPP14Parser.Namespace, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public QualifiednamespacespecifierContext qualifiednamespacespecifier() {
			return getRuleContext(QualifiednamespacespecifierContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public NamespaceAliasDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceAliasDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNamespaceAliasDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNamespaceAliasDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNamespaceAliasDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceAliasDefinitionContext namespaceAliasDefinition() throws RecognitionException {
		NamespaceAliasDefinitionContext _localctx = new NamespaceAliasDefinitionContext(_ctx, getState());
		enterRule(_localctx, 192, RULE_namespaceAliasDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1260);
			match(Namespace);
			setState(1261);
			match(Identifier);
			setState(1262);
			match(Assign);
			setState(1263);
			qualifiednamespacespecifier();
			setState(1264);
			match(Semi);
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
	public static class QualifiednamespacespecifierContext extends ParserRuleContext {
		public NamespaceNameContext namespaceName() {
			return getRuleContext(NamespaceNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public QualifiednamespacespecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qualifiednamespacespecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterQualifiednamespacespecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitQualifiednamespacespecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitQualifiednamespacespecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QualifiednamespacespecifierContext qualifiednamespacespecifier() throws RecognitionException {
		QualifiednamespacespecifierContext _localctx = new QualifiednamespacespecifierContext(_ctx, getState());
		enterRule(_localctx, 194, RULE_qualifiednamespacespecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,146,_ctx) ) {
			case 1:
				{
				setState(1266);
				nestedNameSpecifier(0);
				}
				break;
			}
			setState(1269);
			namespaceName();
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
	public static class UsingDeclarationContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(CPP14Parser.Using, 0); }
		public UnqualifiedIdContext unqualifiedId() {
			return getRuleContext(UnqualifiedIdContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Typename_() { return getToken(CPP14Parser.Typename_, 0); }
		public UsingDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usingDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUsingDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUsingDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitUsingDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UsingDeclarationContext usingDeclaration() throws RecognitionException {
		UsingDeclarationContext _localctx = new UsingDeclarationContext(_ctx, getState());
		enterRule(_localctx, 196, RULE_usingDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1271);
			match(Using);
			setState(1277);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,148,_ctx) ) {
			case 1:
				{
				{
				setState(1273);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Typename_) {
					{
					setState(1272);
					match(Typename_);
					}
				}

				setState(1275);
				nestedNameSpecifier(0);
				}
				}
				break;
			case 2:
				{
				setState(1276);
				match(Doublecolon);
				}
				break;
			}
			setState(1279);
			unqualifiedId();
			setState(1280);
			match(Semi);
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
	public static class UsingDirectiveContext extends ParserRuleContext {
		public TerminalNode Using() { return getToken(CPP14Parser.Using, 0); }
		public TerminalNode Namespace() { return getToken(CPP14Parser.Namespace, 0); }
		public NamespaceNameContext namespaceName() {
			return getRuleContext(NamespaceNameContext.class,0);
		}
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public UsingDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_usingDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterUsingDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitUsingDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitUsingDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UsingDirectiveContext usingDirective() throws RecognitionException {
		UsingDirectiveContext _localctx = new UsingDirectiveContext(_ctx, getState());
		enterRule(_localctx, 198, RULE_usingDirective);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1283);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1282);
				attributeSpecifierSeq();
				}
			}

			setState(1285);
			match(Using);
			setState(1286);
			match(Namespace);
			setState(1288);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,150,_ctx) ) {
			case 1:
				{
				setState(1287);
				nestedNameSpecifier(0);
				}
				break;
			}
			setState(1290);
			namespaceName();
			setState(1291);
			match(Semi);
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
	public static class AsmDefinitionContext extends ParserRuleContext {
		public TerminalNode Asm() { return getToken(CPP14Parser.Asm, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AsmDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asmDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAsmDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAsmDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAsmDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsmDefinitionContext asmDefinition() throws RecognitionException {
		AsmDefinitionContext _localctx = new AsmDefinitionContext(_ctx, getState());
		enterRule(_localctx, 200, RULE_asmDefinition);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1293);
			match(Asm);
			setState(1294);
			match(LeftParen);
			setState(1295);
			match(StringLiteral);
			setState(1296);
			match(RightParen);
			setState(1297);
			match(Semi);
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
	public static class LinkageSpecificationContext extends ParserRuleContext {
		public TerminalNode Extern() { return getToken(CPP14Parser.Extern, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public DeclarationseqContext declarationseq() {
			return getRuleContext(DeclarationseqContext.class,0);
		}
		public LinkageSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_linkageSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLinkageSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLinkageSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLinkageSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LinkageSpecificationContext linkageSpecification() throws RecognitionException {
		LinkageSpecificationContext _localctx = new LinkageSpecificationContext(_ctx, getState());
		enterRule(_localctx, 202, RULE_linkageSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1299);
			match(Extern);
			setState(1300);
			match(StringLiteral);
			setState(1307);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBrace:
				{
				setState(1301);
				match(LeftBrace);
				setState(1303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543754443169808157L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 459384754220313597L) != 0) {
					{
					setState(1302);
					declarationseq();
					}
				}

				setState(1305);
				match(RightBrace);
				}
				break;
			case Alignas:
			case Asm:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Decltype:
			case Double:
			case Enum:
			case Explicit:
			case Extern:
			case Float:
			case Friend:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Namespace:
			case Operator:
			case Register:
			case Short:
			case Signed:
			case Static:
			case Static_assert:
			case Struct:
			case Template:
			case Thread_local:
			case Typedef:
			case Typename_:
			case Union:
			case Unsigned:
			case Using:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case LeftParen:
			case LeftBracket:
			case Star:
			case And:
			case Tilde:
			case AndAnd:
			case Doublecolon:
			case Semi:
			case Ellipsis:
			case Identifier:
				{
				setState(1306);
				declaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class AttributeSpecifierSeqContext extends ParserRuleContext {
		public List<AttributeSpecifierContext> attributeSpecifier() {
			return getRuleContexts(AttributeSpecifierContext.class);
		}
		public AttributeSpecifierContext attributeSpecifier(int i) {
			return getRuleContext(AttributeSpecifierContext.class,i);
		}
		public AttributeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeSpecifierSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttributeSpecifierSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeSpecifierSeqContext attributeSpecifierSeq() throws RecognitionException {
		AttributeSpecifierSeqContext _localctx = new AttributeSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 204, RULE_attributeSpecifierSeq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1310); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1309);
					attributeSpecifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1312); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,153,_ctx);
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
	public static class AttributeSpecifierContext extends ParserRuleContext {
		public List<TerminalNode> LeftBracket() { return getTokens(CPP14Parser.LeftBracket); }
		public TerminalNode LeftBracket(int i) {
			return getToken(CPP14Parser.LeftBracket, i);
		}
		public List<TerminalNode> RightBracket() { return getTokens(CPP14Parser.RightBracket); }
		public TerminalNode RightBracket(int i) {
			return getToken(CPP14Parser.RightBracket, i);
		}
		public AttributeListContext attributeList() {
			return getRuleContext(AttributeListContext.class,0);
		}
		public AlignmentspecifierContext alignmentspecifier() {
			return getRuleContext(AlignmentspecifierContext.class,0);
		}
		public AttributeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttributeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeSpecifierContext attributeSpecifier() throws RecognitionException {
		AttributeSpecifierContext _localctx = new AttributeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 206, RULE_attributeSpecifier);
		int _la;
		try {
			setState(1322);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBracket:
				enterOuterAlt(_localctx, 1);
				{
				setState(1314);
				match(LeftBracket);
				setState(1315);
				match(LeftBracket);
				setState(1317);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1316);
					attributeList();
					}
				}

				setState(1319);
				match(RightBracket);
				setState(1320);
				match(RightBracket);
				}
				break;
			case Alignas:
				enterOuterAlt(_localctx, 2);
				{
				setState(1321);
				alignmentspecifier();
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
	public static class AlignmentspecifierContext extends ParserRuleContext {
		public TerminalNode Alignas() { return getToken(CPP14Parser.Alignas, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public AlignmentspecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alignmentspecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAlignmentspecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAlignmentspecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAlignmentspecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlignmentspecifierContext alignmentspecifier() throws RecognitionException {
		AlignmentspecifierContext _localctx = new AlignmentspecifierContext(_ctx, getState());
		enterRule(_localctx, 208, RULE_alignmentspecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1324);
			match(Alignas);
			setState(1325);
			match(LeftParen);
			setState(1328);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,156,_ctx) ) {
			case 1:
				{
				setState(1326);
				theTypeId();
				}
				break;
			case 2:
				{
				setState(1327);
				constantExpression();
				}
				break;
			}
			setState(1331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1330);
				match(Ellipsis);
				}
			}

			setState(1333);
			match(RightParen);
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
	public static class AttributeListContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public AttributeListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttributeList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeListContext attributeList() throws RecognitionException {
		AttributeListContext _localctx = new AttributeListContext(_ctx, getState());
		enterRule(_localctx, 210, RULE_attributeList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1335);
			attribute();
			setState(1340);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1336);
				match(Comma);
				setState(1337);
				attribute();
				}
				}
				setState(1342);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1344);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1343);
				match(Ellipsis);
				}
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
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public AttributeNamespaceContext attributeNamespace() {
			return getRuleContext(AttributeNamespaceContext.class,0);
		}
		public TerminalNode Doublecolon() { return getToken(CPP14Parser.Doublecolon, 0); }
		public AttributeArgumentClauseContext attributeArgumentClause() {
			return getRuleContext(AttributeArgumentClauseContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 212, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1349);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,160,_ctx) ) {
			case 1:
				{
				setState(1346);
				attributeNamespace();
				setState(1347);
				match(Doublecolon);
				}
				break;
			}
			setState(1351);
			match(Identifier);
			setState(1353);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LeftParen) {
				{
				setState(1352);
				attributeArgumentClause();
				}
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
	public static class AttributeNamespaceContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public AttributeNamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeNamespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttributeNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeNamespaceContext attributeNamespace() throws RecognitionException {
		AttributeNamespaceContext _localctx = new AttributeNamespaceContext(_ctx, getState());
		enterRule(_localctx, 214, RULE_attributeNamespace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1355);
			match(Identifier);
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
	public static class AttributeArgumentClauseContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public BalancedTokenSeqContext balancedTokenSeq() {
			return getRuleContext(BalancedTokenSeqContext.class,0);
		}
		public AttributeArgumentClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeArgumentClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAttributeArgumentClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAttributeArgumentClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAttributeArgumentClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeArgumentClauseContext attributeArgumentClause() throws RecognitionException {
		AttributeArgumentClauseContext _localctx = new AttributeArgumentClauseContext(_ctx, getState());
		enterRule(_localctx, 216, RULE_attributeArgumentClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1357);
			match(LeftParen);
			setState(1359);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & -2L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -88080385L) != 0 || (((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 262143L) != 0) {
				{
				setState(1358);
				balancedTokenSeq();
				}
			}

			setState(1361);
			match(RightParen);
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
	public static class BalancedTokenSeqContext extends ParserRuleContext {
		public List<BalancedtokenContext> balancedtoken() {
			return getRuleContexts(BalancedtokenContext.class);
		}
		public BalancedtokenContext balancedtoken(int i) {
			return getRuleContext(BalancedtokenContext.class,i);
		}
		public BalancedTokenSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_balancedTokenSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBalancedTokenSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBalancedTokenSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBalancedTokenSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BalancedTokenSeqContext balancedTokenSeq() throws RecognitionException {
		BalancedTokenSeqContext _localctx = new BalancedTokenSeqContext(_ctx, getState());
		enterRule(_localctx, 218, RULE_balancedTokenSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1364); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1363);
				balancedtoken();
				}
				}
				setState(1366); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( ((_la) & ~0x3f) == 0 && ((1L << _la) & -2L) != 0 || (((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & -88080385L) != 0 || (((_la - 128)) & ~0x3f) == 0 && ((1L << (_la - 128)) & 262143L) != 0 );
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
	public static class BalancedtokenContext extends ParserRuleContext {
		public List<TerminalNode> LeftParen() { return getTokens(CPP14Parser.LeftParen); }
		public TerminalNode LeftParen(int i) {
			return getToken(CPP14Parser.LeftParen, i);
		}
		public BalancedTokenSeqContext balancedTokenSeq() {
			return getRuleContext(BalancedTokenSeqContext.class,0);
		}
		public List<TerminalNode> RightParen() { return getTokens(CPP14Parser.RightParen); }
		public TerminalNode RightParen(int i) {
			return getToken(CPP14Parser.RightParen, i);
		}
		public List<TerminalNode> LeftBracket() { return getTokens(CPP14Parser.LeftBracket); }
		public TerminalNode LeftBracket(int i) {
			return getToken(CPP14Parser.LeftBracket, i);
		}
		public List<TerminalNode> RightBracket() { return getTokens(CPP14Parser.RightBracket); }
		public TerminalNode RightBracket(int i) {
			return getToken(CPP14Parser.RightBracket, i);
		}
		public List<TerminalNode> LeftBrace() { return getTokens(CPP14Parser.LeftBrace); }
		public TerminalNode LeftBrace(int i) {
			return getToken(CPP14Parser.LeftBrace, i);
		}
		public List<TerminalNode> RightBrace() { return getTokens(CPP14Parser.RightBrace); }
		public TerminalNode RightBrace(int i) {
			return getToken(CPP14Parser.RightBrace, i);
		}
		public BalancedtokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_balancedtoken; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBalancedtoken(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBalancedtoken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBalancedtoken(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BalancedtokenContext balancedtoken() throws RecognitionException {
		BalancedtokenContext _localctx = new BalancedtokenContext(_ctx, getState());
		enterRule(_localctx, 220, RULE_balancedtoken);
		int _la;
		try {
			int _alt;
			setState(1385);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
				enterOuterAlt(_localctx, 1);
				{
				setState(1368);
				match(LeftParen);
				setState(1369);
				balancedTokenSeq();
				setState(1370);
				match(RightParen);
				}
				break;
			case LeftBracket:
				enterOuterAlt(_localctx, 2);
				{
				setState(1372);
				match(LeftBracket);
				setState(1373);
				balancedTokenSeq();
				setState(1374);
				match(RightBracket);
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 3);
				{
				setState(1376);
				match(LeftBrace);
				setState(1377);
				balancedTokenSeq();
				setState(1378);
				match(RightBrace);
				}
				break;
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case MultiLineMacro:
			case Directive:
			case Alignas:
			case Alignof:
			case Asm:
			case Auto:
			case Bool:
			case Break:
			case Case:
			case Catch:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Constexpr:
			case Const_cast:
			case Continue:
			case Decltype:
			case Default:
			case Delete:
			case Do:
			case Double:
			case Dynamic_cast:
			case Else:
			case Enum:
			case Explicit:
			case Export:
			case Extern:
			case False_:
			case Final:
			case Float:
			case For:
			case Friend:
			case Goto:
			case If:
			case Inline:
			case Int:
			case Long:
			case Mutable:
			case Namespace:
			case New:
			case Noexcept:
			case Nullptr:
			case Operator:
			case Override:
			case Private:
			case Protected:
			case Public:
			case Register:
			case Reinterpret_cast:
			case Return:
			case Short:
			case Signed:
			case Sizeof:
			case Static:
			case Static_assert:
			case Static_cast:
			case Struct:
			case Switch:
			case Template:
			case This:
			case Thread_local:
			case Throw:
			case True_:
			case Try:
			case Typedef:
			case Typeid_:
			case Typename_:
			case Union:
			case Unsigned:
			case Using:
			case Virtual:
			case Void:
			case Volatile:
			case Wchar:
			case While:
			case Plus:
			case Minus:
			case Star:
			case Div:
			case Mod:
			case Caret:
			case And:
			case Or:
			case Tilde:
			case Not:
			case Assign:
			case Less:
			case Greater:
			case PlusAssign:
			case MinusAssign:
			case StarAssign:
			case DivAssign:
			case ModAssign:
			case XorAssign:
			case AndAssign:
			case OrAssign:
			case LeftShiftAssign:
			case RightShiftAssign:
			case Equal:
			case NotEqual:
			case LessEqual:
			case GreaterEqual:
			case AndAnd:
			case OrOr:
			case PlusPlus:
			case MinusMinus:
			case Comma:
			case ArrowStar:
			case Arrow:
			case Question:
			case Colon:
			case Doublecolon:
			case Semi:
			case Dot:
			case DotStar:
			case Ellipsis:
			case Identifier:
			case DecimalLiteral:
			case OctalLiteral:
			case HexadecimalLiteral:
			case BinaryLiteral:
			case Integersuffix:
			case UserDefinedIntegerLiteral:
			case UserDefinedFloatingLiteral:
			case UserDefinedStringLiteral:
			case UserDefinedCharacterLiteral:
			case Whitespace:
			case Newline:
			case BlockComment:
			case LineComment:
				enterOuterAlt(_localctx, 4);
				{
				setState(1381); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(1380);
						_la = _input.LA(1);
						if ( _la <= 0 || ((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 63L) != 0) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(1383); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,164,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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
	public static class InitDeclaratorListContext extends ParserRuleContext {
		public List<InitDeclaratorContext> initDeclarator() {
			return getRuleContexts(InitDeclaratorContext.class);
		}
		public InitDeclaratorContext initDeclarator(int i) {
			return getRuleContext(InitDeclaratorContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public InitDeclaratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initDeclaratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitDeclaratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitDeclaratorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInitDeclaratorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitDeclaratorListContext initDeclaratorList() throws RecognitionException {
		InitDeclaratorListContext _localctx = new InitDeclaratorListContext(_ctx, getState());
		enterRule(_localctx, 222, RULE_initDeclaratorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1387);
			initDeclarator();
			setState(1392);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1388);
				match(Comma);
				setState(1389);
				initDeclarator();
				}
				}
				setState(1394);
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
	public static class InitDeclaratorContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public InitializerContext initializer() {
			return getRuleContext(InitializerContext.class,0);
		}
		public InitDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInitDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitDeclaratorContext initDeclarator() throws RecognitionException {
		InitDeclaratorContext _localctx = new InitDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 224, RULE_initDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1395);
			declarator();
			setState(1397);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 65553L) != 0) {
				{
				setState(1396);
				initializer();
				}
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
	public static class DeclaratorContext extends ParserRuleContext {
		public PointerDeclaratorContext pointerDeclarator() {
			return getRuleContext(PointerDeclaratorContext.class,0);
		}
		public NoPointerDeclaratorContext noPointerDeclarator() {
			return getRuleContext(NoPointerDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TrailingReturnTypeContext trailingReturnType() {
			return getRuleContext(TrailingReturnTypeContext.class,0);
		}
		public DeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaratorContext declarator() throws RecognitionException {
		DeclaratorContext _localctx = new DeclaratorContext(_ctx, getState());
		enterRule(_localctx, 226, RULE_declarator);
		try {
			setState(1404);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,168,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1399);
				pointerDeclarator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1400);
				noPointerDeclarator(0);
				setState(1401);
				parametersAndQualifiers();
				setState(1402);
				trailingReturnType();
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
	public static class PointerDeclaratorContext extends ParserRuleContext {
		public NoPointerDeclaratorContext noPointerDeclarator() {
			return getRuleContext(NoPointerDeclaratorContext.class,0);
		}
		public List<PointerOperatorContext> pointerOperator() {
			return getRuleContexts(PointerOperatorContext.class);
		}
		public PointerOperatorContext pointerOperator(int i) {
			return getRuleContext(PointerOperatorContext.class,i);
		}
		public List<TerminalNode> Const() { return getTokens(CPP14Parser.Const); }
		public TerminalNode Const(int i) {
			return getToken(CPP14Parser.Const, i);
		}
		public PointerDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPointerDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PointerDeclaratorContext pointerDeclarator() throws RecognitionException {
		PointerDeclaratorContext _localctx = new PointerDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 228, RULE_pointerDeclarator);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1412);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,170,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1406);
					pointerOperator();
					setState(1408);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Const) {
						{
						setState(1407);
						match(Const);
						}
					}

					}
					} 
				}
				setState(1414);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,170,_ctx);
			}
			setState(1415);
			noPointerDeclarator(0);
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
	public static class NoPointerDeclaratorContext extends ParserRuleContext {
		public DeclaratoridContext declaratorid() {
			return getRuleContext(DeclaratoridContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public PointerDeclaratorContext pointerDeclarator() {
			return getRuleContext(PointerDeclaratorContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NoPointerDeclaratorContext noPointerDeclarator() {
			return getRuleContext(NoPointerDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public NoPointerDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNoPointerDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoPointerDeclaratorContext noPointerDeclarator() throws RecognitionException {
		return noPointerDeclarator(0);
	}

	private NoPointerDeclaratorContext noPointerDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerDeclaratorContext _localctx = new NoPointerDeclaratorContext(_ctx, _parentState);
		NoPointerDeclaratorContext _prevctx = _localctx;
		int _startState = 230;
		enterRecursionRule(_localctx, 230, RULE_noPointerDeclarator, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1426);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Decltype:
			case Operator:
			case Tilde:
			case Doublecolon:
			case Ellipsis:
			case Identifier:
				{
				setState(1418);
				declaratorid();
				setState(1420);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,171,_ctx) ) {
				case 1:
					{
					setState(1419);
					attributeSpecifierSeq();
					}
					break;
				}
				}
				break;
			case LeftParen:
				{
				setState(1422);
				match(LeftParen);
				setState(1423);
				pointerDeclarator();
				setState(1424);
				match(RightParen);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(1442);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,176,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerDeclarator);
					setState(1428);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(1438);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LeftParen:
						{
						setState(1429);
						parametersAndQualifiers();
						}
						break;
					case LeftBracket:
						{
						setState(1430);
						match(LeftBracket);
						setState(1432);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0 || _la==Identifier) {
							{
							setState(1431);
							constantExpression();
							}
						}

						setState(1434);
						match(RightBracket);
						setState(1436);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,174,_ctx) ) {
						case 1:
							{
							setState(1435);
							attributeSpecifierSeq();
							}
							break;
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(1444);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,176,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParametersAndQualifiersContext extends ParserRuleContext {
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public ParameterDeclarationClauseContext parameterDeclarationClause() {
			return getRuleContext(ParameterDeclarationClauseContext.class,0);
		}
		public CvqualifierseqContext cvqualifierseq() {
			return getRuleContext(CvqualifierseqContext.class,0);
		}
		public RefqualifierContext refqualifier() {
			return getRuleContext(RefqualifierContext.class,0);
		}
		public ExceptionSpecificationContext exceptionSpecification() {
			return getRuleContext(ExceptionSpecificationContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ParametersAndQualifiersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametersAndQualifiers; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParametersAndQualifiers(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParametersAndQualifiers(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitParametersAndQualifiers(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametersAndQualifiersContext parametersAndQualifiers() throws RecognitionException {
		ParametersAndQualifiersContext _localctx = new ParametersAndQualifiersContext(_ctx, getState());
		enterRule(_localctx, 232, RULE_parametersAndQualifiers);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1445);
			match(LeftParen);
			setState(1447);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1237504995584196377L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 297237575406461917L) != 0) {
				{
				setState(1446);
				parameterDeclarationClause();
				}
			}

			setState(1449);
			match(RightParen);
			setState(1451);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,178,_ctx) ) {
			case 1:
				{
				setState(1450);
				cvqualifierseq();
				}
				break;
			}
			setState(1454);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,179,_ctx) ) {
			case 1:
				{
				setState(1453);
				refqualifier();
				}
				break;
			}
			setState(1457);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,180,_ctx) ) {
			case 1:
				{
				setState(1456);
				exceptionSpecification();
				}
				break;
			}
			setState(1460);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,181,_ctx) ) {
			case 1:
				{
				setState(1459);
				attributeSpecifierSeq();
				}
				break;
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
	public static class TrailingReturnTypeContext extends ParserRuleContext {
		public TerminalNode Arrow() { return getToken(CPP14Parser.Arrow, 0); }
		public TrailingTypeSpecifierSeqContext trailingTypeSpecifierSeq() {
			return getRuleContext(TrailingTypeSpecifierSeqContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public TrailingReturnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_trailingReturnType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTrailingReturnType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTrailingReturnType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTrailingReturnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TrailingReturnTypeContext trailingReturnType() throws RecognitionException {
		TrailingReturnTypeContext _localctx = new TrailingReturnTypeContext(_ctx, getState());
		enterRule(_localctx, 234, RULE_trailingReturnType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1462);
			match(Arrow);
			setState(1463);
			trailingTypeSpecifierSeq();
			setState(1465);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,182,_ctx) ) {
			case 1:
				{
				setState(1464);
				abstractDeclarator();
				}
				break;
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
	public static class PointerOperatorContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode AndAnd() { return getToken(CPP14Parser.AndAnd, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode Star() { return getToken(CPP14Parser.Star, 0); }
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public CvqualifierseqContext cvqualifierseq() {
			return getRuleContext(CvqualifierseqContext.class,0);
		}
		public PointerOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPointerOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PointerOperatorContext pointerOperator() throws RecognitionException {
		PointerOperatorContext _localctx = new PointerOperatorContext(_ctx, getState());
		enterRule(_localctx, 236, RULE_pointerOperator);
		int _la;
		try {
			setState(1481);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case And:
			case AndAnd:
				enterOuterAlt(_localctx, 1);
				{
				setState(1467);
				_la = _input.LA(1);
				if ( !(_la==And || _la==AndAnd) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1469);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,183,_ctx) ) {
				case 1:
					{
					setState(1468);
					attributeSpecifierSeq();
					}
					break;
				}
				}
				break;
			case Decltype:
			case Star:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1472);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
					{
					setState(1471);
					nestedNameSpecifier(0);
					}
				}

				setState(1474);
				match(Star);
				setState(1476);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,185,_ctx) ) {
				case 1:
					{
					setState(1475);
					attributeSpecifierSeq();
					}
					break;
				}
				setState(1479);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,186,_ctx) ) {
				case 1:
					{
					setState(1478);
					cvqualifierseq();
					}
					break;
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
	public static class CvqualifierseqContext extends ParserRuleContext {
		public List<CvQualifierContext> cvQualifier() {
			return getRuleContexts(CvQualifierContext.class);
		}
		public CvQualifierContext cvQualifier(int i) {
			return getRuleContext(CvQualifierContext.class,i);
		}
		public CvqualifierseqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cvqualifierseq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCvqualifierseq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCvqualifierseq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCvqualifierseq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CvqualifierseqContext cvqualifierseq() throws RecognitionException {
		CvqualifierseqContext _localctx = new CvqualifierseqContext(_ctx, getState());
		enterRule(_localctx, 238, RULE_cvqualifierseq);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1484); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(1483);
					cvQualifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(1486); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,188,_ctx);
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
	public static class CvQualifierContext extends ParserRuleContext {
		public TerminalNode Const() { return getToken(CPP14Parser.Const, 0); }
		public TerminalNode Volatile() { return getToken(CPP14Parser.Volatile, 0); }
		public CvQualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cvQualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterCvQualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitCvQualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitCvQualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CvQualifierContext cvQualifier() throws RecognitionException {
		CvQualifierContext _localctx = new CvQualifierContext(_ctx, getState());
		enterRule(_localctx, 240, RULE_cvQualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1488);
			_la = _input.LA(1);
			if ( !(_la==Const || _la==Volatile) ) {
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
	public static class RefqualifierContext extends ParserRuleContext {
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode AndAnd() { return getToken(CPP14Parser.AndAnd, 0); }
		public RefqualifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refqualifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterRefqualifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitRefqualifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitRefqualifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefqualifierContext refqualifier() throws RecognitionException {
		RefqualifierContext _localctx = new RefqualifierContext(_ctx, getState());
		enterRule(_localctx, 242, RULE_refqualifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1490);
			_la = _input.LA(1);
			if ( !(_la==And || _la==AndAnd) ) {
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
	public static class DeclaratoridContext extends ParserRuleContext {
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public DeclaratoridContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaratorid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDeclaratorid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDeclaratorid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDeclaratorid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaratoridContext declaratorid() throws RecognitionException {
		DeclaratoridContext _localctx = new DeclaratoridContext(_ctx, getState());
		enterRule(_localctx, 244, RULE_declaratorid);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1493);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1492);
				match(Ellipsis);
				}
			}

			setState(1495);
			idExpression();
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
	public static class TheTypeIdContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public TheTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTheTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTheTypeId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTheTypeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TheTypeIdContext theTypeId() throws RecognitionException {
		TheTypeIdContext _localctx = new TheTypeIdContext(_ctx, getState());
		enterRule(_localctx, 246, RULE_theTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1497);
			typeSpecifierSeq();
			setState(1499);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,190,_ctx) ) {
			case 1:
				{
				setState(1498);
				abstractDeclarator();
				}
				break;
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
	public static class AbstractDeclaratorContext extends ParserRuleContext {
		public PointerAbstractDeclaratorContext pointerAbstractDeclarator() {
			return getRuleContext(PointerAbstractDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TrailingReturnTypeContext trailingReturnType() {
			return getRuleContext(TrailingReturnTypeContext.class,0);
		}
		public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() {
			return getRuleContext(NoPointerAbstractDeclaratorContext.class,0);
		}
		public AbstractPackDeclaratorContext abstractPackDeclarator() {
			return getRuleContext(AbstractPackDeclaratorContext.class,0);
		}
		public AbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abstractDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAbstractDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAbstractDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAbstractDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbstractDeclaratorContext abstractDeclarator() throws RecognitionException {
		AbstractDeclaratorContext _localctx = new AbstractDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 248, RULE_abstractDeclarator);
		try {
			setState(1509);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,192,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1501);
				pointerAbstractDeclarator();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1503);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,191,_ctx) ) {
				case 1:
					{
					setState(1502);
					noPointerAbstractDeclarator(0);
					}
					break;
				}
				setState(1505);
				parametersAndQualifiers();
				setState(1506);
				trailingReturnType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1508);
				abstractPackDeclarator();
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
	public static class PointerAbstractDeclaratorContext extends ParserRuleContext {
		public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() {
			return getRuleContext(NoPointerAbstractDeclaratorContext.class,0);
		}
		public List<PointerOperatorContext> pointerOperator() {
			return getRuleContexts(PointerOperatorContext.class);
		}
		public PointerOperatorContext pointerOperator(int i) {
			return getRuleContext(PointerOperatorContext.class,i);
		}
		public PointerAbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pointerAbstractDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPointerAbstractDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPointerAbstractDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPointerAbstractDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PointerAbstractDeclaratorContext pointerAbstractDeclarator() throws RecognitionException {
		PointerAbstractDeclaratorContext _localctx = new PointerAbstractDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 250, RULE_pointerAbstractDeclarator);
		int _la;
		try {
			setState(1520);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
			case LeftBracket:
				enterOuterAlt(_localctx, 1);
				{
				setState(1511);
				noPointerAbstractDeclarator(0);
				}
				break;
			case Decltype:
			case Star:
			case And:
			case AndAnd:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(1513); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(1512);
					pointerOperator();
					}
					}
					setState(1515); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Decltype || (((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 566969237521L) != 0 );
				setState(1518);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,194,_ctx) ) {
				case 1:
					{
					setState(1517);
					noPointerAbstractDeclarator(0);
					}
					break;
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
	public static class NoPointerAbstractDeclaratorContext extends ParserRuleContext {
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public PointerAbstractDeclaratorContext pointerAbstractDeclarator() {
			return getRuleContext(PointerAbstractDeclaratorContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public List<NoPointerAbstractDeclaratorContext> noPointerAbstractDeclarator() {
			return getRuleContexts(NoPointerAbstractDeclaratorContext.class);
		}
		public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator(int i) {
			return getRuleContext(NoPointerAbstractDeclaratorContext.class,i);
		}
		public NoPointerAbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerAbstractDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerAbstractDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerAbstractDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNoPointerAbstractDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() throws RecognitionException {
		return noPointerAbstractDeclarator(0);
	}

	private NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerAbstractDeclaratorContext _localctx = new NoPointerAbstractDeclaratorContext(_ctx, _parentState);
		NoPointerAbstractDeclaratorContext _prevctx = _localctx;
		int _startState = 252;
		enterRecursionRule(_localctx, 252, RULE_noPointerAbstractDeclarator, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1536);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,198,_ctx) ) {
			case 1:
				{
				setState(1523);
				parametersAndQualifiers();
				}
				break;
			case 2:
				{
				setState(1524);
				match(LeftBracket);
				setState(1526);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0 || _la==Identifier) {
					{
					setState(1525);
					constantExpression();
					}
				}

				setState(1528);
				match(RightBracket);
				setState(1530);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,197,_ctx) ) {
				case 1:
					{
					setState(1529);
					attributeSpecifierSeq();
					}
					break;
				}
				}
				break;
			case 3:
				{
				setState(1532);
				match(LeftParen);
				setState(1533);
				pointerAbstractDeclarator();
				setState(1534);
				match(RightParen);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(1553);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,202,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerAbstractDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerAbstractDeclarator);
					setState(1538);
					if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
					setState(1549);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,201,_ctx) ) {
					case 1:
						{
						setState(1539);
						parametersAndQualifiers();
						}
						break;
					case 2:
						{
						setState(1540);
						noPointerAbstractDeclarator(0);
						setState(1541);
						match(LeftBracket);
						setState(1543);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0 || _la==Identifier) {
							{
							setState(1542);
							constantExpression();
							}
						}

						setState(1545);
						match(RightBracket);
						setState(1547);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,200,_ctx) ) {
						case 1:
							{
							setState(1546);
							attributeSpecifierSeq();
							}
							break;
						}
						}
						break;
					}
					}
					} 
				}
				setState(1555);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,202,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AbstractPackDeclaratorContext extends ParserRuleContext {
		public NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() {
			return getRuleContext(NoPointerAbstractPackDeclaratorContext.class,0);
		}
		public List<PointerOperatorContext> pointerOperator() {
			return getRuleContexts(PointerOperatorContext.class);
		}
		public PointerOperatorContext pointerOperator(int i) {
			return getRuleContext(PointerOperatorContext.class,i);
		}
		public AbstractPackDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abstractPackDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAbstractPackDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAbstractPackDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAbstractPackDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AbstractPackDeclaratorContext abstractPackDeclarator() throws RecognitionException {
		AbstractPackDeclaratorContext _localctx = new AbstractPackDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 254, RULE_abstractPackDeclarator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1559);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Decltype || (((_la - 93)) & ~0x3f) == 0 && ((1L << (_la - 93)) & 566969237521L) != 0) {
				{
				{
				setState(1556);
				pointerOperator();
				}
				}
				setState(1561);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(1562);
			noPointerAbstractPackDeclarator(0);
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
	public static class NoPointerAbstractPackDeclaratorContext extends ParserRuleContext {
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() {
			return getRuleContext(NoPointerAbstractPackDeclaratorContext.class,0);
		}
		public ParametersAndQualifiersContext parametersAndQualifiers() {
			return getRuleContext(ParametersAndQualifiersContext.class,0);
		}
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public NoPointerAbstractPackDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noPointerAbstractPackDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoPointerAbstractPackDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoPointerAbstractPackDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNoPointerAbstractPackDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() throws RecognitionException {
		return noPointerAbstractPackDeclarator(0);
	}

	private NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NoPointerAbstractPackDeclaratorContext _localctx = new NoPointerAbstractPackDeclaratorContext(_ctx, _parentState);
		NoPointerAbstractPackDeclaratorContext _prevctx = _localctx;
		int _startState = 256;
		enterRecursionRule(_localctx, 256, RULE_noPointerAbstractPackDeclarator, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(1565);
			match(Ellipsis);
			}
			_ctx.stop = _input.LT(-1);
			setState(1581);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,207,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NoPointerAbstractPackDeclaratorContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_noPointerAbstractPackDeclarator);
					setState(1567);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(1577);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LeftParen:
						{
						setState(1568);
						parametersAndQualifiers();
						}
						break;
					case LeftBracket:
						{
						setState(1569);
						match(LeftBracket);
						setState(1571);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133137L) != 0 || _la==Identifier) {
							{
							setState(1570);
							constantExpression();
							}
						}

						setState(1573);
						match(RightBracket);
						setState(1575);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,205,_ctx) ) {
						case 1:
							{
							setState(1574);
							attributeSpecifierSeq();
							}
							break;
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					} 
				}
				setState(1583);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,207,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterDeclarationClauseContext extends ParserRuleContext {
		public ParameterDeclarationListContext parameterDeclarationList() {
			return getRuleContext(ParameterDeclarationListContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public ParameterDeclarationClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclarationClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParameterDeclarationClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParameterDeclarationClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitParameterDeclarationClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationClauseContext parameterDeclarationClause() throws RecognitionException {
		ParameterDeclarationClauseContext _localctx = new ParameterDeclarationClauseContext(_ctx, getState());
		enterRule(_localctx, 258, RULE_parameterDeclarationClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1584);
			parameterDeclarationList();
			setState(1589);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Comma || _la==Ellipsis) {
				{
				setState(1586);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(1585);
					match(Comma);
					}
				}

				setState(1588);
				match(Ellipsis);
				}
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
	public static class ParameterDeclarationListContext extends ParserRuleContext {
		public List<ParameterDeclarationContext> parameterDeclaration() {
			return getRuleContexts(ParameterDeclarationContext.class);
		}
		public ParameterDeclarationContext parameterDeclaration(int i) {
			return getRuleContext(ParameterDeclarationContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public ParameterDeclarationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclarationList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParameterDeclarationList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParameterDeclarationList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitParameterDeclarationList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationListContext parameterDeclarationList() throws RecognitionException {
		ParameterDeclarationListContext _localctx = new ParameterDeclarationListContext(_ctx, getState());
		enterRule(_localctx, 260, RULE_parameterDeclarationList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1591);
			parameterDeclaration();
			setState(1596);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,210,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1592);
					match(Comma);
					setState(1593);
					parameterDeclaration();
					}
					} 
				}
				setState(1598);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,210,_ctx);
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
	public static class ParameterDeclarationContext extends ParserRuleContext {
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public ParameterDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterParameterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitParameterDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitParameterDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterDeclarationContext parameterDeclaration() throws RecognitionException {
		ParameterDeclarationContext _localctx = new ParameterDeclarationContext(_ctx, getState());
		enterRule(_localctx, 262, RULE_parameterDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1600);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1599);
				attributeSpecifierSeq();
				}
			}

			setState(1602);
			declSpecifierSeq();
			{
			setState(1607);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,213,_ctx) ) {
			case 1:
				{
				setState(1603);
				declarator();
				}
				break;
			case 2:
				{
				setState(1605);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,212,_ctx) ) {
				case 1:
					{
					setState(1604);
					abstractDeclarator();
					}
					break;
				}
				}
				break;
			}
			setState(1611);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(1609);
				match(Assign);
				setState(1610);
				initializerClause();
				}
			}

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
	public static class FunctionDefinitionContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public FunctionBodyContext functionBody() {
			return getRuleContext(FunctionBodyContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public VirtualSpecifierSeqContext virtualSpecifierSeq() {
			return getRuleContext(VirtualSpecifierSeqContext.class,0);
		}
		public FunctionDefinitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionDefinition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionDefinition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionDefinition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitFunctionDefinition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionDefinitionContext functionDefinition() throws RecognitionException {
		FunctionDefinitionContext _localctx = new FunctionDefinitionContext(_ctx, getState());
		enterRule(_localctx, 264, RULE_functionDefinition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1614);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1613);
				attributeSpecifierSeq();
				}
			}

			setState(1617);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,216,_ctx) ) {
			case 1:
				{
				setState(1616);
				declSpecifierSeq();
				}
				break;
			}
			setState(1619);
			declarator();
			setState(1621);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Final || _la==Override) {
				{
				setState(1620);
				virtualSpecifierSeq();
				}
			}

			setState(1623);
			functionBody();
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
	public static class FunctionBodyContext extends ParserRuleContext {
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public ConstructorInitializerContext constructorInitializer() {
			return getRuleContext(ConstructorInitializerContext.class,0);
		}
		public FunctionTryBlockContext functionTryBlock() {
			return getRuleContext(FunctionTryBlockContext.class,0);
		}
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public TerminalNode Default() { return getToken(CPP14Parser.Default, 0); }
		public TerminalNode Delete() { return getToken(CPP14Parser.Delete, 0); }
		public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitFunctionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionBodyContext functionBody() throws RecognitionException {
		FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
		enterRule(_localctx, 266, RULE_functionBody);
		int _la;
		try {
			setState(1633);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBrace:
			case Colon:
				enterOuterAlt(_localctx, 1);
				{
				setState(1626);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Colon) {
					{
					setState(1625);
					constructorInitializer();
					}
				}

				setState(1628);
				compoundStatement();
				}
				break;
			case Try:
				enterOuterAlt(_localctx, 2);
				{
				setState(1629);
				functionTryBlock();
				}
				break;
			case Assign:
				enterOuterAlt(_localctx, 3);
				{
				setState(1630);
				match(Assign);
				setState(1631);
				_la = _input.LA(1);
				if ( !(_la==Default || _la==Delete) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(1632);
				match(Semi);
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
	public static class InitializerContext extends ParserRuleContext {
		public BraceOrEqualInitializerContext braceOrEqualInitializer() {
			return getRuleContext(BraceOrEqualInitializerContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public InitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitializerContext initializer() throws RecognitionException {
		InitializerContext _localctx = new InitializerContext(_ctx, getState());
		enterRule(_localctx, 268, RULE_initializer);
		try {
			setState(1640);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftBrace:
			case Assign:
				enterOuterAlt(_localctx, 1);
				{
				setState(1635);
				braceOrEqualInitializer();
				}
				break;
			case LeftParen:
				enterOuterAlt(_localctx, 2);
				{
				setState(1636);
				match(LeftParen);
				setState(1637);
				expressionList();
				setState(1638);
				match(RightParen);
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
	public static class BraceOrEqualInitializerContext extends ParserRuleContext {
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public InitializerClauseContext initializerClause() {
			return getRuleContext(InitializerClauseContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public BraceOrEqualInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_braceOrEqualInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBraceOrEqualInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBraceOrEqualInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBraceOrEqualInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BraceOrEqualInitializerContext braceOrEqualInitializer() throws RecognitionException {
		BraceOrEqualInitializerContext _localctx = new BraceOrEqualInitializerContext(_ctx, getState());
		enterRule(_localctx, 270, RULE_braceOrEqualInitializer);
		try {
			setState(1645);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Assign:
				enterOuterAlt(_localctx, 1);
				{
				setState(1642);
				match(Assign);
				setState(1643);
				initializerClause();
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(1644);
				bracedInitList();
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
	public static class InitializerClauseContext extends ParserRuleContext {
		public AssignmentExpressionContext assignmentExpression() {
			return getRuleContext(AssignmentExpressionContext.class,0);
		}
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public InitializerClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializerClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitializerClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitializerClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInitializerClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitializerClauseContext initializerClause() throws RecognitionException {
		InitializerClauseContext _localctx = new InitializerClauseContext(_ctx, getState());
		enterRule(_localctx, 272, RULE_initializerClause);
		try {
			setState(1649);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IntegerLiteral:
			case CharacterLiteral:
			case FloatingLiteral:
			case StringLiteral:
			case BooleanLiteral:
			case PointerLiteral:
			case UserDefinedLiteral:
			case Alignof:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Const_cast:
			case Decltype:
			case Delete:
			case Double:
			case Dynamic_cast:
			case Float:
			case Int:
			case Long:
			case New:
			case Noexcept:
			case Operator:
			case Reinterpret_cast:
			case Short:
			case Signed:
			case Sizeof:
			case Static_cast:
			case This:
			case Throw:
			case Typeid_:
			case Typename_:
			case Unsigned:
			case Void:
			case Wchar:
			case LeftParen:
			case LeftBracket:
			case Plus:
			case Minus:
			case Star:
			case And:
			case Or:
			case Tilde:
			case Not:
			case PlusPlus:
			case MinusMinus:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1647);
				assignmentExpression();
				}
				break;
			case LeftBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(1648);
				bracedInitList();
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
	public static class InitializerListContext extends ParserRuleContext {
		public List<InitializerClauseContext> initializerClause() {
			return getRuleContexts(InitializerClauseContext.class);
		}
		public InitializerClauseContext initializerClause(int i) {
			return getRuleContext(InitializerClauseContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public InitializerListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_initializerList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterInitializerList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitInitializerList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitInitializerList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitializerListContext initializerList() throws RecognitionException {
		InitializerListContext _localctx = new InitializerListContext(_ctx, getState());
		enterRule(_localctx, 274, RULE_initializerList);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(1651);
			initializerClause();
			setState(1653);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1652);
				match(Ellipsis);
				}
			}

			setState(1662);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(1655);
					match(Comma);
					setState(1656);
					initializerClause();
					setState(1658);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Ellipsis) {
						{
						setState(1657);
						match(Ellipsis);
						}
					}

					}
					} 
				}
				setState(1664);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,225,_ctx);
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
	public static class BracedInitListContext extends ParserRuleContext {
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public InitializerListContext initializerList() {
			return getRuleContext(InitializerListContext.class,0);
		}
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public BracedInitListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bracedInitList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBracedInitList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBracedInitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBracedInitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BracedInitListContext bracedInitList() throws RecognitionException {
		BracedInitListContext _localctx = new BracedInitListContext(_ctx, getState());
		enterRule(_localctx, 276, RULE_bracedInitList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1665);
			match(LeftBrace);
			setState(1670);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0 || _la==Identifier) {
				{
				setState(1666);
				initializerList();
				setState(1668);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(1667);
					match(Comma);
					}
				}

				}
			}

			setState(1672);
			match(RightBrace);
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
	public static class ClassNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public ClassNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_className; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassNameContext className() throws RecognitionException {
		ClassNameContext _localctx = new ClassNameContext(_ctx, getState());
		enterRule(_localctx, 278, RULE_className);
		try {
			setState(1676);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,228,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1674);
				match(Identifier);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1675);
				simpleTemplateId();
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
	public static class ClassSpecifierContext extends ParserRuleContext {
		public ClassHeadContext classHead() {
			return getRuleContext(ClassHeadContext.class,0);
		}
		public TerminalNode LeftBrace() { return getToken(CPP14Parser.LeftBrace, 0); }
		public TerminalNode RightBrace() { return getToken(CPP14Parser.RightBrace, 0); }
		public MemberSpecificationContext memberSpecification() {
			return getRuleContext(MemberSpecificationContext.class,0);
		}
		public ClassSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassSpecifierContext classSpecifier() throws RecognitionException {
		ClassSpecifierContext _localctx = new ClassSpecifierContext(_ctx, getState());
		enterRule(_localctx, 280, RULE_classSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1678);
			classHead();
			setState(1679);
			match(LeftBrace);
			setState(1681);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543877313594212121L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 463888353847684093L) != 0) {
				{
				setState(1680);
				memberSpecification();
				}
			}

			setState(1683);
			match(RightBrace);
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
	public static class ClassHeadContext extends ParserRuleContext {
		public ClassKeyContext classKey() {
			return getRuleContext(ClassKeyContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public ClassHeadNameContext classHeadName() {
			return getRuleContext(ClassHeadNameContext.class,0);
		}
		public BaseClauseContext baseClause() {
			return getRuleContext(BaseClauseContext.class,0);
		}
		public ClassVirtSpecifierContext classVirtSpecifier() {
			return getRuleContext(ClassVirtSpecifierContext.class,0);
		}
		public TerminalNode Union() { return getToken(CPP14Parser.Union, 0); }
		public ClassHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassHeadContext classHead() throws RecognitionException {
		ClassHeadContext _localctx = new ClassHeadContext(_ctx, getState());
		enterRule(_localctx, 282, RULE_classHead);
		int _la;
		try {
			setState(1708);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Class:
			case Struct:
				enterOuterAlt(_localctx, 1);
				{
				setState(1685);
				classKey();
				setState(1687);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(1686);
					attributeSpecifierSeq();
					}
				}

				setState(1693);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
					{
					setState(1689);
					classHeadName();
					setState(1691);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Final) {
						{
						setState(1690);
						classVirtSpecifier();
						}
					}

					}
				}

				setState(1696);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Colon) {
					{
					setState(1695);
					baseClause();
					}
				}

				}
				break;
			case Union:
				enterOuterAlt(_localctx, 2);
				{
				setState(1698);
				match(Union);
				setState(1700);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(1699);
					attributeSpecifierSeq();
					}
				}

				setState(1706);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Decltype || _la==Doublecolon || _la==Identifier) {
					{
					setState(1702);
					classHeadName();
					setState(1704);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Final) {
						{
						setState(1703);
						classVirtSpecifier();
						}
					}

					}
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
	public static class ClassHeadNameContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public ClassHeadNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classHeadName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassHeadName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassHeadName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassHeadName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassHeadNameContext classHeadName() throws RecognitionException {
		ClassHeadNameContext _localctx = new ClassHeadNameContext(_ctx, getState());
		enterRule(_localctx, 284, RULE_classHeadName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1711);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,238,_ctx) ) {
			case 1:
				{
				setState(1710);
				nestedNameSpecifier(0);
				}
				break;
			}
			setState(1713);
			className();
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
	public static class ClassVirtSpecifierContext extends ParserRuleContext {
		public TerminalNode Final() { return getToken(CPP14Parser.Final, 0); }
		public ClassVirtSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classVirtSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassVirtSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassVirtSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassVirtSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassVirtSpecifierContext classVirtSpecifier() throws RecognitionException {
		ClassVirtSpecifierContext _localctx = new ClassVirtSpecifierContext(_ctx, getState());
		enterRule(_localctx, 286, RULE_classVirtSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1715);
			match(Final);
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
	public static class ClassKeyContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(CPP14Parser.Class, 0); }
		public TerminalNode Struct() { return getToken(CPP14Parser.Struct, 0); }
		public ClassKeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classKey; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassKey(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassKey(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassKeyContext classKey() throws RecognitionException {
		ClassKeyContext _localctx = new ClassKeyContext(_ctx, getState());
		enterRule(_localctx, 288, RULE_classKey);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1717);
			_la = _input.LA(1);
			if ( !(_la==Class || _la==Struct) ) {
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
	public static class MemberSpecificationContext extends ParserRuleContext {
		public List<MemberdeclarationContext> memberdeclaration() {
			return getRuleContexts(MemberdeclarationContext.class);
		}
		public MemberdeclarationContext memberdeclaration(int i) {
			return getRuleContext(MemberdeclarationContext.class,i);
		}
		public List<AccessSpecifierContext> accessSpecifier() {
			return getRuleContexts(AccessSpecifierContext.class);
		}
		public AccessSpecifierContext accessSpecifier(int i) {
			return getRuleContext(AccessSpecifierContext.class,i);
		}
		public List<TerminalNode> Colon() { return getTokens(CPP14Parser.Colon); }
		public TerminalNode Colon(int i) {
			return getToken(CPP14Parser.Colon, i);
		}
		public MemberSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMemberSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberSpecificationContext memberSpecification() throws RecognitionException {
		MemberSpecificationContext _localctx = new MemberSpecificationContext(_ctx, getState());
		enterRule(_localctx, 290, RULE_memberSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1723); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(1723);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Alignas:
				case Auto:
				case Bool:
				case Char:
				case Char16:
				case Char32:
				case Class:
				case Const:
				case Constexpr:
				case Decltype:
				case Double:
				case Enum:
				case Explicit:
				case Extern:
				case Float:
				case Friend:
				case Inline:
				case Int:
				case Long:
				case Mutable:
				case Operator:
				case Register:
				case Short:
				case Signed:
				case Static:
				case Static_assert:
				case Struct:
				case Template:
				case Thread_local:
				case Typedef:
				case Typename_:
				case Union:
				case Unsigned:
				case Using:
				case Virtual:
				case Void:
				case Volatile:
				case Wchar:
				case LeftParen:
				case LeftBracket:
				case Star:
				case And:
				case Tilde:
				case AndAnd:
				case Colon:
				case Doublecolon:
				case Semi:
				case Ellipsis:
				case Identifier:
					{
					setState(1719);
					memberdeclaration();
					}
					break;
				case Private:
				case Protected:
				case Public:
					{
					setState(1720);
					accessSpecifier();
					setState(1721);
					match(Colon);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(1725); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & 1543877313594212121L) != 0 || (((_la - 74)) & ~0x3f) == 0 && ((1L << (_la - 74)) & 463888353847684093L) != 0 );
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
	public static class MemberdeclarationContext extends ParserRuleContext {
		public TerminalNode Semi() { return getToken(CPP14Parser.Semi, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclSpecifierSeqContext declSpecifierSeq() {
			return getRuleContext(DeclSpecifierSeqContext.class,0);
		}
		public MemberDeclaratorListContext memberDeclaratorList() {
			return getRuleContext(MemberDeclaratorListContext.class,0);
		}
		public FunctionDefinitionContext functionDefinition() {
			return getRuleContext(FunctionDefinitionContext.class,0);
		}
		public UsingDeclarationContext usingDeclaration() {
			return getRuleContext(UsingDeclarationContext.class,0);
		}
		public StaticAssertDeclarationContext staticAssertDeclaration() {
			return getRuleContext(StaticAssertDeclarationContext.class,0);
		}
		public TemplateDeclarationContext templateDeclaration() {
			return getRuleContext(TemplateDeclarationContext.class,0);
		}
		public AliasDeclarationContext aliasDeclaration() {
			return getRuleContext(AliasDeclarationContext.class,0);
		}
		public EmptyDeclarationContext emptyDeclaration() {
			return getRuleContext(EmptyDeclarationContext.class,0);
		}
		public MemberdeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberdeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberdeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberdeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMemberdeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberdeclarationContext memberdeclaration() throws RecognitionException {
		MemberdeclarationContext _localctx = new MemberdeclarationContext(_ctx, getState());
		enterRule(_localctx, 292, RULE_memberdeclaration);
		int _la;
		try {
			setState(1743);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,244,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1728);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,241,_ctx) ) {
				case 1:
					{
					setState(1727);
					attributeSpecifierSeq();
					}
					break;
				}
				setState(1731);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,242,_ctx) ) {
				case 1:
					{
					setState(1730);
					declSpecifierSeq();
					}
					break;
				}
				setState(1734);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 4503599694480384L) != 0 || (((_la - 85)) & ~0x3f) == 0 && ((1L << (_la - 85)) & 217711892254981L) != 0) {
					{
					setState(1733);
					memberDeclaratorList();
					}
				}

				setState(1736);
				match(Semi);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1737);
				functionDefinition();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1738);
				usingDeclaration();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(1739);
				staticAssertDeclaration();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(1740);
				templateDeclaration();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(1741);
				aliasDeclaration();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(1742);
				emptyDeclaration();
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
	public static class MemberDeclaratorListContext extends ParserRuleContext {
		public List<MemberDeclaratorContext> memberDeclarator() {
			return getRuleContexts(MemberDeclaratorContext.class);
		}
		public MemberDeclaratorContext memberDeclarator(int i) {
			return getRuleContext(MemberDeclaratorContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public MemberDeclaratorListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclaratorList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberDeclaratorList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberDeclaratorList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMemberDeclaratorList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberDeclaratorListContext memberDeclaratorList() throws RecognitionException {
		MemberDeclaratorListContext _localctx = new MemberDeclaratorListContext(_ctx, getState());
		enterRule(_localctx, 294, RULE_memberDeclaratorList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1745);
			memberDeclarator();
			setState(1750);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1746);
				match(Comma);
				setState(1747);
				memberDeclarator();
				}
				}
				setState(1752);
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
	public static class MemberDeclaratorContext extends ParserRuleContext {
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public VirtualSpecifierSeqContext virtualSpecifierSeq() {
			return getRuleContext(VirtualSpecifierSeqContext.class,0);
		}
		public PureSpecifierContext pureSpecifier() {
			return getRuleContext(PureSpecifierContext.class,0);
		}
		public BraceOrEqualInitializerContext braceOrEqualInitializer() {
			return getRuleContext(BraceOrEqualInitializerContext.class,0);
		}
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public MemberDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memberDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemberDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemberDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMemberDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemberDeclaratorContext memberDeclarator() throws RecognitionException {
		MemberDeclaratorContext _localctx = new MemberDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 296, RULE_memberDeclarator);
		int _la;
		try {
			setState(1773);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,252,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1753);
				declarator();
				setState(1763);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,249,_ctx) ) {
				case 1:
					{
					setState(1755);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Final || _la==Override) {
						{
						setState(1754);
						virtualSpecifierSeq();
						}
					}

					setState(1758);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Assign) {
						{
						setState(1757);
						pureSpecifier();
						}
					}

					}
					break;
				case 2:
					{
					setState(1761);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==LeftBrace || _la==Assign) {
						{
						setState(1760);
						braceOrEqualInitializer();
						}
					}

					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1766);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1765);
					match(Identifier);
					}
				}

				setState(1769);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(1768);
					attributeSpecifierSeq();
					}
				}

				setState(1771);
				match(Colon);
				setState(1772);
				constantExpression();
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
	public static class VirtualSpecifierSeqContext extends ParserRuleContext {
		public List<VirtualSpecifierContext> virtualSpecifier() {
			return getRuleContexts(VirtualSpecifierContext.class);
		}
		public VirtualSpecifierContext virtualSpecifier(int i) {
			return getRuleContext(VirtualSpecifierContext.class,i);
		}
		public VirtualSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_virtualSpecifierSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterVirtualSpecifierSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitVirtualSpecifierSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitVirtualSpecifierSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VirtualSpecifierSeqContext virtualSpecifierSeq() throws RecognitionException {
		VirtualSpecifierSeqContext _localctx = new VirtualSpecifierSeqContext(_ctx, getState());
		enterRule(_localctx, 298, RULE_virtualSpecifierSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1776); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1775);
				virtualSpecifier();
				}
				}
				setState(1778); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Final || _la==Override );
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
	public static class VirtualSpecifierContext extends ParserRuleContext {
		public TerminalNode Override() { return getToken(CPP14Parser.Override, 0); }
		public TerminalNode Final() { return getToken(CPP14Parser.Final, 0); }
		public VirtualSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_virtualSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterVirtualSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitVirtualSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitVirtualSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VirtualSpecifierContext virtualSpecifier() throws RecognitionException {
		VirtualSpecifierContext _localctx = new VirtualSpecifierContext(_ctx, getState());
		enterRule(_localctx, 300, RULE_virtualSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1780);
			_la = _input.LA(1);
			if ( !(_la==Final || _la==Override) ) {
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
	public static class PureSpecifierContext extends ParserRuleContext {
		public Token val;
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TerminalNode OctalLiteral() { return getToken(CPP14Parser.OctalLiteral, 0); }
		public PureSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pureSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterPureSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitPureSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitPureSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PureSpecifierContext pureSpecifier() throws RecognitionException {
		PureSpecifierContext _localctx = new PureSpecifierContext(_ctx, getState());
		enterRule(_localctx, 302, RULE_pureSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1782);
			match(Assign);
			setState(1783);
			((PureSpecifierContext)_localctx).val = match(OctalLiteral);
			if((((PureSpecifierContext)_localctx).val!=null?((PureSpecifierContext)_localctx).val.getText():null).compareTo("0")!=0) throw new InputMismatchException(this);
					
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
	public static class BaseClauseContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public BaseSpecifierListContext baseSpecifierList() {
			return getRuleContext(BaseSpecifierListContext.class,0);
		}
		public BaseClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseClause; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseClause(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseClause(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBaseClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseClauseContext baseClause() throws RecognitionException {
		BaseClauseContext _localctx = new BaseClauseContext(_ctx, getState());
		enterRule(_localctx, 304, RULE_baseClause);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1786);
			match(Colon);
			setState(1787);
			baseSpecifierList();
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
	public static class BaseSpecifierListContext extends ParserRuleContext {
		public List<BaseSpecifierContext> baseSpecifier() {
			return getRuleContexts(BaseSpecifierContext.class);
		}
		public BaseSpecifierContext baseSpecifier(int i) {
			return getRuleContext(BaseSpecifierContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public BaseSpecifierListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseSpecifierList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseSpecifierList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseSpecifierList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBaseSpecifierList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseSpecifierListContext baseSpecifierList() throws RecognitionException {
		BaseSpecifierListContext _localctx = new BaseSpecifierListContext(_ctx, getState());
		enterRule(_localctx, 306, RULE_baseSpecifierList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1789);
			baseSpecifier();
			setState(1791);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1790);
				match(Ellipsis);
				}
			}

			setState(1800);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1793);
				match(Comma);
				setState(1794);
				baseSpecifier();
				setState(1796);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1795);
					match(Ellipsis);
					}
				}

				}
				}
				setState(1802);
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
	public static class BaseSpecifierContext extends ParserRuleContext {
		public BaseTypeSpecifierContext baseTypeSpecifier() {
			return getRuleContext(BaseTypeSpecifierContext.class,0);
		}
		public TerminalNode Virtual() { return getToken(CPP14Parser.Virtual, 0); }
		public AccessSpecifierContext accessSpecifier() {
			return getRuleContext(AccessSpecifierContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public BaseSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBaseSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseSpecifierContext baseSpecifier() throws RecognitionException {
		BaseSpecifierContext _localctx = new BaseSpecifierContext(_ctx, getState());
		enterRule(_localctx, 308, RULE_baseSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1804);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Alignas || _la==LeftBracket) {
				{
				setState(1803);
				attributeSpecifierSeq();
				}
			}

			setState(1818);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Decltype:
			case Doublecolon:
			case Identifier:
				{
				setState(1806);
				baseTypeSpecifier();
				}
				break;
			case Virtual:
				{
				setState(1807);
				match(Virtual);
				setState(1809);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 126100789566373888L) != 0) {
					{
					setState(1808);
					accessSpecifier();
					}
				}

				setState(1811);
				baseTypeSpecifier();
				}
				break;
			case Private:
			case Protected:
			case Public:
				{
				setState(1812);
				accessSpecifier();
				setState(1814);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Virtual) {
					{
					setState(1813);
					match(Virtual);
					}
				}

				setState(1816);
				baseTypeSpecifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class ClassOrDeclTypeContext extends ParserRuleContext {
		public ClassNameContext className() {
			return getRuleContext(ClassNameContext.class,0);
		}
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public DecltypeSpecifierContext decltypeSpecifier() {
			return getRuleContext(DecltypeSpecifierContext.class,0);
		}
		public ClassOrDeclTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classOrDeclType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterClassOrDeclType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitClassOrDeclType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitClassOrDeclType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassOrDeclTypeContext classOrDeclType() throws RecognitionException {
		ClassOrDeclTypeContext _localctx = new ClassOrDeclTypeContext(_ctx, getState());
		enterRule(_localctx, 310, RULE_classOrDeclType);
		try {
			setState(1825);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,262,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1821);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,261,_ctx) ) {
				case 1:
					{
					setState(1820);
					nestedNameSpecifier(0);
					}
					break;
				}
				setState(1823);
				className();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1824);
				decltypeSpecifier();
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
	public static class BaseTypeSpecifierContext extends ParserRuleContext {
		public ClassOrDeclTypeContext classOrDeclType() {
			return getRuleContext(ClassOrDeclTypeContext.class,0);
		}
		public BaseTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_baseTypeSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterBaseTypeSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitBaseTypeSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitBaseTypeSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BaseTypeSpecifierContext baseTypeSpecifier() throws RecognitionException {
		BaseTypeSpecifierContext _localctx = new BaseTypeSpecifierContext(_ctx, getState());
		enterRule(_localctx, 312, RULE_baseTypeSpecifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1827);
			classOrDeclType();
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
	public static class AccessSpecifierContext extends ParserRuleContext {
		public TerminalNode Private() { return getToken(CPP14Parser.Private, 0); }
		public TerminalNode Protected() { return getToken(CPP14Parser.Protected, 0); }
		public TerminalNode Public() { return getToken(CPP14Parser.Public, 0); }
		public AccessSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterAccessSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitAccessSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitAccessSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessSpecifierContext accessSpecifier() throws RecognitionException {
		AccessSpecifierContext _localctx = new AccessSpecifierContext(_ctx, getState());
		enterRule(_localctx, 314, RULE_accessSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1829);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 126100789566373888L) != 0) ) {
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
	public static class ConversionFunctionIdContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(CPP14Parser.Operator, 0); }
		public ConversionTypeIdContext conversionTypeId() {
			return getRuleContext(ConversionTypeIdContext.class,0);
		}
		public ConversionFunctionIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversionFunctionId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConversionFunctionId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConversionFunctionId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitConversionFunctionId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConversionFunctionIdContext conversionFunctionId() throws RecognitionException {
		ConversionFunctionIdContext _localctx = new ConversionFunctionIdContext(_ctx, getState());
		enterRule(_localctx, 316, RULE_conversionFunctionId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1831);
			match(Operator);
			setState(1832);
			conversionTypeId();
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
	public static class ConversionTypeIdContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public ConversionDeclaratorContext conversionDeclarator() {
			return getRuleContext(ConversionDeclaratorContext.class,0);
		}
		public ConversionTypeIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversionTypeId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConversionTypeId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConversionTypeId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitConversionTypeId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConversionTypeIdContext conversionTypeId() throws RecognitionException {
		ConversionTypeIdContext _localctx = new ConversionTypeIdContext(_ctx, getState());
		enterRule(_localctx, 318, RULE_conversionTypeId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1834);
			typeSpecifierSeq();
			setState(1836);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,263,_ctx) ) {
			case 1:
				{
				setState(1835);
				conversionDeclarator();
				}
				break;
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
	public static class ConversionDeclaratorContext extends ParserRuleContext {
		public PointerOperatorContext pointerOperator() {
			return getRuleContext(PointerOperatorContext.class,0);
		}
		public ConversionDeclaratorContext conversionDeclarator() {
			return getRuleContext(ConversionDeclaratorContext.class,0);
		}
		public ConversionDeclaratorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conversionDeclarator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConversionDeclarator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConversionDeclarator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitConversionDeclarator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConversionDeclaratorContext conversionDeclarator() throws RecognitionException {
		ConversionDeclaratorContext _localctx = new ConversionDeclaratorContext(_ctx, getState());
		enterRule(_localctx, 320, RULE_conversionDeclarator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1838);
			pointerOperator();
			setState(1840);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,264,_ctx) ) {
			case 1:
				{
				setState(1839);
				conversionDeclarator();
				}
				break;
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
	public static class ConstructorInitializerContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(CPP14Parser.Colon, 0); }
		public MemInitializerListContext memInitializerList() {
			return getRuleContext(MemInitializerListContext.class,0);
		}
		public ConstructorInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructorInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterConstructorInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitConstructorInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitConstructorInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorInitializerContext constructorInitializer() throws RecognitionException {
		ConstructorInitializerContext _localctx = new ConstructorInitializerContext(_ctx, getState());
		enterRule(_localctx, 322, RULE_constructorInitializer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1842);
			match(Colon);
			setState(1843);
			memInitializerList();
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
	public static class MemInitializerListContext extends ParserRuleContext {
		public List<MemInitializerContext> memInitializer() {
			return getRuleContexts(MemInitializerContext.class);
		}
		public MemInitializerContext memInitializer(int i) {
			return getRuleContext(MemInitializerContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public MemInitializerListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memInitializerList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemInitializerList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemInitializerList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMemInitializerList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemInitializerListContext memInitializerList() throws RecognitionException {
		MemInitializerListContext _localctx = new MemInitializerListContext(_ctx, getState());
		enterRule(_localctx, 324, RULE_memInitializerList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1845);
			memInitializer();
			setState(1847);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1846);
				match(Ellipsis);
				}
			}

			setState(1856);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1849);
				match(Comma);
				setState(1850);
				memInitializer();
				setState(1852);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1851);
					match(Ellipsis);
					}
				}

				}
				}
				setState(1858);
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
	public static class MemInitializerContext extends ParserRuleContext {
		public MeminitializeridContext meminitializerid() {
			return getRuleContext(MeminitializeridContext.class,0);
		}
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public BracedInitListContext bracedInitList() {
			return getRuleContext(BracedInitListContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public MemInitializerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_memInitializer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMemInitializer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMemInitializer(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMemInitializer(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MemInitializerContext memInitializer() throws RecognitionException {
		MemInitializerContext _localctx = new MemInitializerContext(_ctx, getState());
		enterRule(_localctx, 326, RULE_memInitializer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1859);
			meminitializerid();
			setState(1866);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LeftParen:
				{
				setState(1860);
				match(LeftParen);
				setState(1862);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474400910417L) != 0 || _la==Identifier) {
					{
					setState(1861);
					expressionList();
					}
				}

				setState(1864);
				match(RightParen);
				}
				break;
			case LeftBrace:
				{
				setState(1865);
				bracedInitList();
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class MeminitializeridContext extends ParserRuleContext {
		public ClassOrDeclTypeContext classOrDeclType() {
			return getRuleContext(ClassOrDeclTypeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public MeminitializeridContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_meminitializerid; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterMeminitializerid(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitMeminitializerid(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitMeminitializerid(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MeminitializeridContext meminitializerid() throws RecognitionException {
		MeminitializeridContext _localctx = new MeminitializeridContext(_ctx, getState());
		enterRule(_localctx, 328, RULE_meminitializerid);
		try {
			setState(1870);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,270,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1868);
				classOrDeclType();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1869);
				match(Identifier);
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
	public static class OperatorFunctionIdContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(CPP14Parser.Operator, 0); }
		public TheOperatorContext theOperator() {
			return getRuleContext(TheOperatorContext.class,0);
		}
		public OperatorFunctionIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operatorFunctionId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterOperatorFunctionId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitOperatorFunctionId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitOperatorFunctionId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorFunctionIdContext operatorFunctionId() throws RecognitionException {
		OperatorFunctionIdContext _localctx = new OperatorFunctionIdContext(_ctx, getState());
		enterRule(_localctx, 330, RULE_operatorFunctionId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1872);
			match(Operator);
			setState(1873);
			theOperator();
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
	public static class LiteralOperatorIdContext extends ParserRuleContext {
		public TerminalNode Operator() { return getToken(CPP14Parser.Operator, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TerminalNode UserDefinedStringLiteral() { return getToken(CPP14Parser.UserDefinedStringLiteral, 0); }
		public LiteralOperatorIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalOperatorId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLiteralOperatorId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLiteralOperatorId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLiteralOperatorId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralOperatorIdContext literalOperatorId() throws RecognitionException {
		LiteralOperatorIdContext _localctx = new LiteralOperatorIdContext(_ctx, getState());
		enterRule(_localctx, 332, RULE_literalOperatorId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1875);
			match(Operator);
			setState(1879);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case StringLiteral:
				{
				setState(1876);
				match(StringLiteral);
				setState(1877);
				match(Identifier);
				}
				break;
			case UserDefinedStringLiteral:
				{
				setState(1878);
				match(UserDefinedStringLiteral);
				}
				break;
			default:
				throw new NoViableAltException(this);
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
	public static class TemplateDeclarationContext extends ParserRuleContext {
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TemplateparameterListContext templateparameterList() {
			return getRuleContext(TemplateparameterListContext.class,0);
		}
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public TemplateDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateDeclarationContext templateDeclaration() throws RecognitionException {
		TemplateDeclarationContext _localctx = new TemplateDeclarationContext(_ctx, getState());
		enterRule(_localctx, 334, RULE_templateDeclaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1881);
			match(Template);
			setState(1882);
			match(Less);
			setState(1883);
			templateparameterList();
			setState(1884);
			match(Greater);
			setState(1885);
			declaration();
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
	public static class TemplateparameterListContext extends ParserRuleContext {
		public List<TemplateParameterContext> templateParameter() {
			return getRuleContexts(TemplateParameterContext.class);
		}
		public TemplateParameterContext templateParameter(int i) {
			return getRuleContext(TemplateParameterContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TemplateparameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateparameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateparameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateparameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateparameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateparameterListContext templateparameterList() throws RecognitionException {
		TemplateparameterListContext _localctx = new TemplateparameterListContext(_ctx, getState());
		enterRule(_localctx, 336, RULE_templateparameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1887);
			templateParameter();
			setState(1892);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1888);
				match(Comma);
				setState(1889);
				templateParameter();
				}
				}
				setState(1894);
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
	public static class TemplateParameterContext extends ParserRuleContext {
		public TypeParameterContext typeParameter() {
			return getRuleContext(TypeParameterContext.class,0);
		}
		public ParameterDeclarationContext parameterDeclaration() {
			return getRuleContext(ParameterDeclarationContext.class,0);
		}
		public TemplateParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateParameterContext templateParameter() throws RecognitionException {
		TemplateParameterContext _localctx = new TemplateParameterContext(_ctx, getState());
		enterRule(_localctx, 338, RULE_templateParameter);
		try {
			setState(1897);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,273,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1895);
				typeParameter();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1896);
				parameterDeclaration();
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
	public static class TypeParameterContext extends ParserRuleContext {
		public TerminalNode Class() { return getToken(CPP14Parser.Class, 0); }
		public TerminalNode Typename_() { return getToken(CPP14Parser.Typename_, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TemplateparameterListContext templateparameterList() {
			return getRuleContext(TemplateparameterListContext.class,0);
		}
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypeParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeParameterContext typeParameter() throws RecognitionException {
		TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
		enterRule(_localctx, 340, RULE_typeParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1908);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Class:
			case Template:
				{
				setState(1904);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Template) {
					{
					setState(1899);
					match(Template);
					setState(1900);
					match(Less);
					setState(1901);
					templateparameterList();
					setState(1902);
					match(Greater);
					}
				}

				setState(1906);
				match(Class);
				}
				break;
			case Typename_:
				{
				setState(1907);
				match(Typename_);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(1921);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,279,_ctx) ) {
			case 1:
				{
				{
				setState(1911);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1910);
					match(Ellipsis);
					}
				}

				setState(1914);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1913);
					match(Identifier);
					}
				}

				}
				}
				break;
			case 2:
				{
				{
				setState(1917);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Identifier) {
					{
					setState(1916);
					match(Identifier);
					}
				}

				setState(1919);
				match(Assign);
				setState(1920);
				theTypeId();
				}
				}
				break;
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
	public static class SimpleTemplateIdContext extends ParserRuleContext {
		public TemplateNameContext templateName() {
			return getRuleContext(TemplateNameContext.class,0);
		}
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public TemplateArgumentListContext templateArgumentList() {
			return getRuleContext(TemplateArgumentListContext.class,0);
		}
		public SimpleTemplateIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTemplateId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterSimpleTemplateId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitSimpleTemplateId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitSimpleTemplateId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SimpleTemplateIdContext simpleTemplateId() throws RecognitionException {
		SimpleTemplateIdContext _localctx = new SimpleTemplateIdContext(_ctx, getState());
		enterRule(_localctx, 342, RULE_simpleTemplateId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1923);
			templateName();
			setState(1924);
			match(Less);
			setState(1926);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979472930990334L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384268307L) != 0 || _la==Identifier) {
				{
				setState(1925);
				templateArgumentList();
				}
			}

			setState(1928);
			match(Greater);
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
	public static class TemplateIdContext extends ParserRuleContext {
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public OperatorFunctionIdContext operatorFunctionId() {
			return getRuleContext(OperatorFunctionIdContext.class,0);
		}
		public LiteralOperatorIdContext literalOperatorId() {
			return getRuleContext(LiteralOperatorIdContext.class,0);
		}
		public TemplateArgumentListContext templateArgumentList() {
			return getRuleContext(TemplateArgumentListContext.class,0);
		}
		public TemplateIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateIdContext templateId() throws RecognitionException {
		TemplateIdContext _localctx = new TemplateIdContext(_ctx, getState());
		enterRule(_localctx, 344, RULE_templateId);
		int _la;
		try {
			setState(1941);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(1930);
				simpleTemplateId();
				}
				break;
			case Operator:
				enterOuterAlt(_localctx, 2);
				{
				setState(1933);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,281,_ctx) ) {
				case 1:
					{
					setState(1931);
					operatorFunctionId();
					}
					break;
				case 2:
					{
					setState(1932);
					literalOperatorId();
					}
					break;
				}
				setState(1935);
				match(Less);
				setState(1937);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979472930990334L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384268307L) != 0 || _la==Identifier) {
					{
					setState(1936);
					templateArgumentList();
					}
				}

				setState(1939);
				match(Greater);
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
	public static class TemplateNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public TemplateNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateNameContext templateName() throws RecognitionException {
		TemplateNameContext _localctx = new TemplateNameContext(_ctx, getState());
		enterRule(_localctx, 346, RULE_templateName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1943);
			match(Identifier);
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
	public static class TemplateArgumentListContext extends ParserRuleContext {
		public List<TemplateArgumentContext> templateArgument() {
			return getRuleContexts(TemplateArgumentContext.class);
		}
		public TemplateArgumentContext templateArgument(int i) {
			return getRuleContext(TemplateArgumentContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TemplateArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateArgumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateArgumentListContext templateArgumentList() throws RecognitionException {
		TemplateArgumentListContext _localctx = new TemplateArgumentListContext(_ctx, getState());
		enterRule(_localctx, 348, RULE_templateArgumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1945);
			templateArgument();
			setState(1947);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(1946);
				match(Ellipsis);
				}
			}

			setState(1956);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(1949);
				match(Comma);
				setState(1950);
				templateArgument();
				setState(1952);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(1951);
					match(Ellipsis);
					}
				}

				}
				}
				setState(1958);
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
	public static class TemplateArgumentContext extends ParserRuleContext {
		public TheTypeIdContext theTypeId() {
			return getRuleContext(TheTypeIdContext.class,0);
		}
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public IdExpressionContext idExpression() {
			return getRuleContext(IdExpressionContext.class,0);
		}
		public TemplateArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_templateArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTemplateArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTemplateArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTemplateArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateArgumentContext templateArgument() throws RecognitionException {
		TemplateArgumentContext _localctx = new TemplateArgumentContext(_ctx, getState());
		enterRule(_localctx, 350, RULE_templateArgument);
		try {
			setState(1962);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,287,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(1959);
				theTypeId();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(1960);
				constantExpression();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(1961);
				idExpression();
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
	public static class TypeNameSpecifierContext extends ParserRuleContext {
		public TerminalNode Typename_() { return getToken(CPP14Parser.Typename_, 0); }
		public NestedNameSpecifierContext nestedNameSpecifier() {
			return getRuleContext(NestedNameSpecifierContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(CPP14Parser.Identifier, 0); }
		public SimpleTemplateIdContext simpleTemplateId() {
			return getRuleContext(SimpleTemplateIdContext.class,0);
		}
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TypeNameSpecifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeNameSpecifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeNameSpecifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeNameSpecifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypeNameSpecifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeNameSpecifierContext typeNameSpecifier() throws RecognitionException {
		TypeNameSpecifierContext _localctx = new TypeNameSpecifierContext(_ctx, getState());
		enterRule(_localctx, 352, RULE_typeNameSpecifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1964);
			match(Typename_);
			setState(1965);
			nestedNameSpecifier(0);
			setState(1971);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,289,_ctx) ) {
			case 1:
				{
				setState(1966);
				match(Identifier);
				}
				break;
			case 2:
				{
				setState(1968);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Template) {
					{
					setState(1967);
					match(Template);
					}
				}

				setState(1970);
				simpleTemplateId();
				}
				break;
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
	public static class ExplicitInstantiationContext extends ParserRuleContext {
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public TerminalNode Extern() { return getToken(CPP14Parser.Extern, 0); }
		public ExplicitInstantiationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitInstantiation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExplicitInstantiation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExplicitInstantiation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExplicitInstantiation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplicitInstantiationContext explicitInstantiation() throws RecognitionException {
		ExplicitInstantiationContext _localctx = new ExplicitInstantiationContext(_ctx, getState());
		enterRule(_localctx, 354, RULE_explicitInstantiation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1974);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Extern) {
				{
				setState(1973);
				match(Extern);
				}
			}

			setState(1976);
			match(Template);
			setState(1977);
			declaration();
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
	public static class ExplicitSpecializationContext extends ParserRuleContext {
		public TerminalNode Template() { return getToken(CPP14Parser.Template, 0); }
		public TerminalNode Less() { return getToken(CPP14Parser.Less, 0); }
		public TerminalNode Greater() { return getToken(CPP14Parser.Greater, 0); }
		public DeclarationContext declaration() {
			return getRuleContext(DeclarationContext.class,0);
		}
		public ExplicitSpecializationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_explicitSpecialization; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExplicitSpecialization(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExplicitSpecialization(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExplicitSpecialization(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExplicitSpecializationContext explicitSpecialization() throws RecognitionException {
		ExplicitSpecializationContext _localctx = new ExplicitSpecializationContext(_ctx, getState());
		enterRule(_localctx, 356, RULE_explicitSpecialization);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1979);
			match(Template);
			setState(1980);
			match(Less);
			setState(1981);
			match(Greater);
			setState(1982);
			declaration();
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
	public static class TryBlockContext extends ParserRuleContext {
		public TerminalNode Try() { return getToken(CPP14Parser.Try, 0); }
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public HandlerSeqContext handlerSeq() {
			return getRuleContext(HandlerSeqContext.class,0);
		}
		public TryBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tryBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTryBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTryBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTryBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TryBlockContext tryBlock() throws RecognitionException {
		TryBlockContext _localctx = new TryBlockContext(_ctx, getState());
		enterRule(_localctx, 358, RULE_tryBlock);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1984);
			match(Try);
			setState(1985);
			compoundStatement();
			setState(1986);
			handlerSeq();
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
	public static class FunctionTryBlockContext extends ParserRuleContext {
		public TerminalNode Try() { return getToken(CPP14Parser.Try, 0); }
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public HandlerSeqContext handlerSeq() {
			return getRuleContext(HandlerSeqContext.class,0);
		}
		public ConstructorInitializerContext constructorInitializer() {
			return getRuleContext(ConstructorInitializerContext.class,0);
		}
		public FunctionTryBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionTryBlock; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterFunctionTryBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitFunctionTryBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitFunctionTryBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionTryBlockContext functionTryBlock() throws RecognitionException {
		FunctionTryBlockContext _localctx = new FunctionTryBlockContext(_ctx, getState());
		enterRule(_localctx, 360, RULE_functionTryBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1988);
			match(Try);
			setState(1990);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Colon) {
				{
				setState(1989);
				constructorInitializer();
				}
			}

			setState(1992);
			compoundStatement();
			setState(1993);
			handlerSeq();
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
	public static class HandlerSeqContext extends ParserRuleContext {
		public List<HandlerContext> handler() {
			return getRuleContexts(HandlerContext.class);
		}
		public HandlerContext handler(int i) {
			return getRuleContext(HandlerContext.class,i);
		}
		public HandlerSeqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_handlerSeq; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterHandlerSeq(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitHandlerSeq(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitHandlerSeq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HandlerSeqContext handlerSeq() throws RecognitionException {
		HandlerSeqContext _localctx = new HandlerSeqContext(_ctx, getState());
		enterRule(_localctx, 362, RULE_handlerSeq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(1996); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(1995);
				handler();
				}
				}
				setState(1998); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Catch );
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
	public static class HandlerContext extends ParserRuleContext {
		public TerminalNode Catch() { return getToken(CPP14Parser.Catch, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ExceptionDeclarationContext exceptionDeclaration() {
			return getRuleContext(ExceptionDeclarationContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public CompoundStatementContext compoundStatement() {
			return getRuleContext(CompoundStatementContext.class,0);
		}
		public HandlerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_handler; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterHandler(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitHandler(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitHandler(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HandlerContext handler() throws RecognitionException {
		HandlerContext _localctx = new HandlerContext(_ctx, getState());
		enterRule(_localctx, 364, RULE_handler);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2000);
			match(Catch);
			setState(2001);
			match(LeftParen);
			setState(2002);
			exceptionDeclaration();
			setState(2003);
			match(RightParen);
			setState(2004);
			compoundStatement();
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
	public static class ExceptionDeclarationContext extends ParserRuleContext {
		public TypeSpecifierSeqContext typeSpecifierSeq() {
			return getRuleContext(TypeSpecifierSeqContext.class,0);
		}
		public AttributeSpecifierSeqContext attributeSpecifierSeq() {
			return getRuleContext(AttributeSpecifierSeqContext.class,0);
		}
		public DeclaratorContext declarator() {
			return getRuleContext(DeclaratorContext.class,0);
		}
		public AbstractDeclaratorContext abstractDeclarator() {
			return getRuleContext(AbstractDeclaratorContext.class,0);
		}
		public TerminalNode Ellipsis() { return getToken(CPP14Parser.Ellipsis, 0); }
		public ExceptionDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptionDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExceptionDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExceptionDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExceptionDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExceptionDeclarationContext exceptionDeclaration() throws RecognitionException {
		ExceptionDeclarationContext _localctx = new ExceptionDeclarationContext(_ctx, getState());
		enterRule(_localctx, 366, RULE_exceptionDeclaration);
		int _la;
		try {
			setState(2015);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Alignas:
			case Auto:
			case Bool:
			case Char:
			case Char16:
			case Char32:
			case Class:
			case Const:
			case Decltype:
			case Double:
			case Enum:
			case Float:
			case Int:
			case Long:
			case Short:
			case Signed:
			case Struct:
			case Typename_:
			case Union:
			case Unsigned:
			case Void:
			case Volatile:
			case Wchar:
			case LeftBracket:
			case Doublecolon:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(2007);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Alignas || _la==LeftBracket) {
					{
					setState(2006);
					attributeSpecifierSeq();
					}
				}

				setState(2009);
				typeSpecifierSeq();
				setState(2012);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,294,_ctx) ) {
				case 1:
					{
					setState(2010);
					declarator();
					}
					break;
				case 2:
					{
					setState(2011);
					abstractDeclarator();
					}
					break;
				}
				}
				break;
			case Ellipsis:
				enterOuterAlt(_localctx, 2);
				{
				setState(2014);
				match(Ellipsis);
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
	public static class ThrowExpressionContext extends ParserRuleContext {
		public TerminalNode Throw() { return getToken(CPP14Parser.Throw, 0); }
		public AssignmentExpressionContext assignmentExpression() {
			return getRuleContext(AssignmentExpressionContext.class,0);
		}
		public ThrowExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_throwExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterThrowExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitThrowExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitThrowExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThrowExpressionContext throwExpression() throws RecognitionException {
		ThrowExpressionContext _localctx = new ThrowExpressionContext(_ctx, getState());
		enterRule(_localctx, 368, RULE_throwExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2017);
			match(Throw);
			setState(2019);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((_la) & ~0x3f) == 0 && ((1L << _la) & 8364979464334764286L) != 0 || (((_la - 65)) & ~0x3f) == 0 && ((1L << (_la - 65)) & 4719772474384133201L) != 0 || _la==Identifier) {
				{
				setState(2018);
				assignmentExpression();
				}
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
	public static class ExceptionSpecificationContext extends ParserRuleContext {
		public DynamicExceptionSpecificationContext dynamicExceptionSpecification() {
			return getRuleContext(DynamicExceptionSpecificationContext.class,0);
		}
		public NoeExceptSpecificationContext noeExceptSpecification() {
			return getRuleContext(NoeExceptSpecificationContext.class,0);
		}
		public ExceptionSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exceptionSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterExceptionSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitExceptionSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitExceptionSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExceptionSpecificationContext exceptionSpecification() throws RecognitionException {
		ExceptionSpecificationContext _localctx = new ExceptionSpecificationContext(_ctx, getState());
		enterRule(_localctx, 370, RULE_exceptionSpecification);
		try {
			setState(2023);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Throw:
				enterOuterAlt(_localctx, 1);
				{
				setState(2021);
				dynamicExceptionSpecification();
				}
				break;
			case Noexcept:
				enterOuterAlt(_localctx, 2);
				{
				setState(2022);
				noeExceptSpecification();
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
	public static class DynamicExceptionSpecificationContext extends ParserRuleContext {
		public TerminalNode Throw() { return getToken(CPP14Parser.Throw, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TypeIdListContext typeIdList() {
			return getRuleContext(TypeIdListContext.class,0);
		}
		public DynamicExceptionSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dynamicExceptionSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterDynamicExceptionSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitDynamicExceptionSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitDynamicExceptionSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DynamicExceptionSpecificationContext dynamicExceptionSpecification() throws RecognitionException {
		DynamicExceptionSpecificationContext _localctx = new DynamicExceptionSpecificationContext(_ctx, getState());
		enterRule(_localctx, 372, RULE_dynamicExceptionSpecification);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2025);
			match(Throw);
			setState(2026);
			match(LeftParen);
			setState(2028);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la - 13)) & ~0x3f) == 0 && ((1L << (_la - 13)) & -9213942612181769245L) != 0 || (((_la - 77)) & ~0x3f) == 0 && ((1L << (_la - 77)) & 37154696925806707L) != 0) {
				{
				setState(2027);
				typeIdList();
				}
			}

			setState(2030);
			match(RightParen);
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
	public static class TypeIdListContext extends ParserRuleContext {
		public List<TheTypeIdContext> theTypeId() {
			return getRuleContexts(TheTypeIdContext.class);
		}
		public TheTypeIdContext theTypeId(int i) {
			return getRuleContext(TheTypeIdContext.class,i);
		}
		public List<TerminalNode> Ellipsis() { return getTokens(CPP14Parser.Ellipsis); }
		public TerminalNode Ellipsis(int i) {
			return getToken(CPP14Parser.Ellipsis, i);
		}
		public List<TerminalNode> Comma() { return getTokens(CPP14Parser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(CPP14Parser.Comma, i);
		}
		public TypeIdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeIdList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTypeIdList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTypeIdList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTypeIdList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeIdListContext typeIdList() throws RecognitionException {
		TypeIdListContext _localctx = new TypeIdListContext(_ctx, getState());
		enterRule(_localctx, 374, RULE_typeIdList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2032);
			theTypeId();
			setState(2034);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Ellipsis) {
				{
				setState(2033);
				match(Ellipsis);
				}
			}

			setState(2043);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(2036);
				match(Comma);
				setState(2037);
				theTypeId();
				setState(2039);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Ellipsis) {
					{
					setState(2038);
					match(Ellipsis);
					}
				}

				}
				}
				setState(2045);
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
	public static class NoeExceptSpecificationContext extends ParserRuleContext {
		public TerminalNode Noexcept() { return getToken(CPP14Parser.Noexcept, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public ConstantExpressionContext constantExpression() {
			return getRuleContext(ConstantExpressionContext.class,0);
		}
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public NoeExceptSpecificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_noeExceptSpecification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterNoeExceptSpecification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitNoeExceptSpecification(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitNoeExceptSpecification(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NoeExceptSpecificationContext noeExceptSpecification() throws RecognitionException {
		NoeExceptSpecificationContext _localctx = new NoeExceptSpecificationContext(_ctx, getState());
		enterRule(_localctx, 376, RULE_noeExceptSpecification);
		try {
			setState(2052);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,302,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2046);
				match(Noexcept);
				setState(2047);
				match(LeftParen);
				setState(2048);
				constantExpression();
				setState(2049);
				match(RightParen);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2051);
				match(Noexcept);
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
	public static class TheOperatorContext extends ParserRuleContext {
		public TerminalNode New() { return getToken(CPP14Parser.New, 0); }
		public TerminalNode LeftBracket() { return getToken(CPP14Parser.LeftBracket, 0); }
		public TerminalNode RightBracket() { return getToken(CPP14Parser.RightBracket, 0); }
		public TerminalNode Delete() { return getToken(CPP14Parser.Delete, 0); }
		public TerminalNode Plus() { return getToken(CPP14Parser.Plus, 0); }
		public TerminalNode Minus() { return getToken(CPP14Parser.Minus, 0); }
		public TerminalNode Star() { return getToken(CPP14Parser.Star, 0); }
		public TerminalNode Div() { return getToken(CPP14Parser.Div, 0); }
		public TerminalNode Mod() { return getToken(CPP14Parser.Mod, 0); }
		public TerminalNode Caret() { return getToken(CPP14Parser.Caret, 0); }
		public TerminalNode And() { return getToken(CPP14Parser.And, 0); }
		public TerminalNode Or() { return getToken(CPP14Parser.Or, 0); }
		public TerminalNode Tilde() { return getToken(CPP14Parser.Tilde, 0); }
		public TerminalNode Not() { return getToken(CPP14Parser.Not, 0); }
		public TerminalNode Assign() { return getToken(CPP14Parser.Assign, 0); }
		public List<TerminalNode> Greater() { return getTokens(CPP14Parser.Greater); }
		public TerminalNode Greater(int i) {
			return getToken(CPP14Parser.Greater, i);
		}
		public List<TerminalNode> Less() { return getTokens(CPP14Parser.Less); }
		public TerminalNode Less(int i) {
			return getToken(CPP14Parser.Less, i);
		}
		public TerminalNode GreaterEqual() { return getToken(CPP14Parser.GreaterEqual, 0); }
		public TerminalNode PlusAssign() { return getToken(CPP14Parser.PlusAssign, 0); }
		public TerminalNode MinusAssign() { return getToken(CPP14Parser.MinusAssign, 0); }
		public TerminalNode StarAssign() { return getToken(CPP14Parser.StarAssign, 0); }
		public TerminalNode ModAssign() { return getToken(CPP14Parser.ModAssign, 0); }
		public TerminalNode XorAssign() { return getToken(CPP14Parser.XorAssign, 0); }
		public TerminalNode AndAssign() { return getToken(CPP14Parser.AndAssign, 0); }
		public TerminalNode OrAssign() { return getToken(CPP14Parser.OrAssign, 0); }
		public TerminalNode RightShiftAssign() { return getToken(CPP14Parser.RightShiftAssign, 0); }
		public TerminalNode LeftShiftAssign() { return getToken(CPP14Parser.LeftShiftAssign, 0); }
		public TerminalNode Equal() { return getToken(CPP14Parser.Equal, 0); }
		public TerminalNode NotEqual() { return getToken(CPP14Parser.NotEqual, 0); }
		public TerminalNode LessEqual() { return getToken(CPP14Parser.LessEqual, 0); }
		public TerminalNode AndAnd() { return getToken(CPP14Parser.AndAnd, 0); }
		public TerminalNode OrOr() { return getToken(CPP14Parser.OrOr, 0); }
		public TerminalNode PlusPlus() { return getToken(CPP14Parser.PlusPlus, 0); }
		public TerminalNode MinusMinus() { return getToken(CPP14Parser.MinusMinus, 0); }
		public TerminalNode Comma() { return getToken(CPP14Parser.Comma, 0); }
		public TerminalNode ArrowStar() { return getToken(CPP14Parser.ArrowStar, 0); }
		public TerminalNode Arrow() { return getToken(CPP14Parser.Arrow, 0); }
		public TerminalNode LeftParen() { return getToken(CPP14Parser.LeftParen, 0); }
		public TerminalNode RightParen() { return getToken(CPP14Parser.RightParen, 0); }
		public TheOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_theOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterTheOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitTheOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitTheOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TheOperatorContext theOperator() throws RecognitionException {
		TheOperatorContext _localctx = new TheOperatorContext(_ctx, getState());
		enterRule(_localctx, 378, RULE_theOperator);
		try {
			setState(2105);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,305,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(2054);
				match(New);
				setState(2057);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,303,_ctx) ) {
				case 1:
					{
					setState(2055);
					match(LeftBracket);
					setState(2056);
					match(RightBracket);
					}
					break;
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(2059);
				match(Delete);
				setState(2062);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,304,_ctx) ) {
				case 1:
					{
					setState(2060);
					match(LeftBracket);
					setState(2061);
					match(RightBracket);
					}
					break;
				}
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(2064);
				match(Plus);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(2065);
				match(Minus);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(2066);
				match(Star);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(2067);
				match(Div);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(2068);
				match(Mod);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(2069);
				match(Caret);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(2070);
				match(And);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(2071);
				match(Or);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(2072);
				match(Tilde);
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(2073);
				match(Not);
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(2074);
				match(Assign);
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(2075);
				match(Greater);
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(2076);
				match(Less);
				}
				break;
			case 16:
				enterOuterAlt(_localctx, 16);
				{
				setState(2077);
				match(GreaterEqual);
				}
				break;
			case 17:
				enterOuterAlt(_localctx, 17);
				{
				setState(2078);
				match(PlusAssign);
				}
				break;
			case 18:
				enterOuterAlt(_localctx, 18);
				{
				setState(2079);
				match(MinusAssign);
				}
				break;
			case 19:
				enterOuterAlt(_localctx, 19);
				{
				setState(2080);
				match(StarAssign);
				}
				break;
			case 20:
				enterOuterAlt(_localctx, 20);
				{
				setState(2081);
				match(ModAssign);
				}
				break;
			case 21:
				enterOuterAlt(_localctx, 21);
				{
				setState(2082);
				match(XorAssign);
				}
				break;
			case 22:
				enterOuterAlt(_localctx, 22);
				{
				setState(2083);
				match(AndAssign);
				}
				break;
			case 23:
				enterOuterAlt(_localctx, 23);
				{
				setState(2084);
				match(OrAssign);
				}
				break;
			case 24:
				enterOuterAlt(_localctx, 24);
				{
				setState(2085);
				match(Less);
				setState(2086);
				match(Less);
				}
				break;
			case 25:
				enterOuterAlt(_localctx, 25);
				{
				setState(2087);
				match(Greater);
				setState(2088);
				match(Greater);
				}
				break;
			case 26:
				enterOuterAlt(_localctx, 26);
				{
				setState(2089);
				match(RightShiftAssign);
				}
				break;
			case 27:
				enterOuterAlt(_localctx, 27);
				{
				setState(2090);
				match(LeftShiftAssign);
				}
				break;
			case 28:
				enterOuterAlt(_localctx, 28);
				{
				setState(2091);
				match(Equal);
				}
				break;
			case 29:
				enterOuterAlt(_localctx, 29);
				{
				setState(2092);
				match(NotEqual);
				}
				break;
			case 30:
				enterOuterAlt(_localctx, 30);
				{
				setState(2093);
				match(LessEqual);
				}
				break;
			case 31:
				enterOuterAlt(_localctx, 31);
				{
				setState(2094);
				match(AndAnd);
				}
				break;
			case 32:
				enterOuterAlt(_localctx, 32);
				{
				setState(2095);
				match(OrOr);
				}
				break;
			case 33:
				enterOuterAlt(_localctx, 33);
				{
				setState(2096);
				match(PlusPlus);
				}
				break;
			case 34:
				enterOuterAlt(_localctx, 34);
				{
				setState(2097);
				match(MinusMinus);
				}
				break;
			case 35:
				enterOuterAlt(_localctx, 35);
				{
				setState(2098);
				match(Comma);
				}
				break;
			case 36:
				enterOuterAlt(_localctx, 36);
				{
				setState(2099);
				match(ArrowStar);
				}
				break;
			case 37:
				enterOuterAlt(_localctx, 37);
				{
				setState(2100);
				match(Arrow);
				}
				break;
			case 38:
				enterOuterAlt(_localctx, 38);
				{
				setState(2101);
				match(LeftParen);
				setState(2102);
				match(RightParen);
				}
				break;
			case 39:
				enterOuterAlt(_localctx, 39);
				{
				setState(2103);
				match(LeftBracket);
				setState(2104);
				match(RightBracket);
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
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode IntegerLiteral() { return getToken(CPP14Parser.IntegerLiteral, 0); }
		public TerminalNode CharacterLiteral() { return getToken(CPP14Parser.CharacterLiteral, 0); }
		public TerminalNode FloatingLiteral() { return getToken(CPP14Parser.FloatingLiteral, 0); }
		public TerminalNode StringLiteral() { return getToken(CPP14Parser.StringLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(CPP14Parser.BooleanLiteral, 0); }
		public TerminalNode PointerLiteral() { return getToken(CPP14Parser.PointerLiteral, 0); }
		public TerminalNode UserDefinedLiteral() { return getToken(CPP14Parser.UserDefinedLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CPP14ParserListener ) ((CPP14ParserListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CPP14ParserVisitor ) return ((CPP14ParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 380, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2107);
			_la = _input.LA(1);
			if ( !(((_la) & ~0x3f) == 0 && ((1L << _la) & 254L) != 0) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return nestedNameSpecifier_sempred((NestedNameSpecifierContext)_localctx, predIndex);
		case 15:
			return postfixExpression_sempred((PostfixExpressionContext)_localctx, predIndex);
		case 25:
			return noPointerNewDeclarator_sempred((NoPointerNewDeclaratorContext)_localctx, predIndex);
		case 115:
			return noPointerDeclarator_sempred((NoPointerDeclaratorContext)_localctx, predIndex);
		case 126:
			return noPointerAbstractDeclarator_sempred((NoPointerAbstractDeclaratorContext)_localctx, predIndex);
		case 128:
			return noPointerAbstractPackDeclarator_sempred((NoPointerAbstractPackDeclaratorContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean nestedNameSpecifier_sempred(NestedNameSpecifierContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean postfixExpression_sempred(PostfixExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 4);
		case 4:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean noPointerNewDeclarator_sempred(NoPointerNewDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean noPointerDeclarator_sempred(NoPointerDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}
	private boolean noPointerAbstractDeclarator_sempred(NoPointerAbstractDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 7:
			return precpred(_ctx, 4);
		}
		return true;
	}
	private boolean noPointerAbstractPackDeclarator_sempred(NoPointerAbstractPackDeclaratorContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0091\u083e\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007"+
		"\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007"+
		"\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007"+
		"\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007"+
		"\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007"+
		"\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007"+
		"\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007"+
		"\"\u0002#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007"+
		"\'\u0002(\u0007(\u0002)\u0007)\u0002*\u0007*\u0002+\u0007+\u0002,\u0007"+
		",\u0002-\u0007-\u0002.\u0007.\u0002/\u0007/\u00020\u00070\u00021\u0007"+
		"1\u00022\u00072\u00023\u00073\u00024\u00074\u00025\u00075\u00026\u0007"+
		"6\u00027\u00077\u00028\u00078\u00029\u00079\u0002:\u0007:\u0002;\u0007"+
		";\u0002<\u0007<\u0002=\u0007=\u0002>\u0007>\u0002?\u0007?\u0002@\u0007"+
		"@\u0002A\u0007A\u0002B\u0007B\u0002C\u0007C\u0002D\u0007D\u0002E\u0007"+
		"E\u0002F\u0007F\u0002G\u0007G\u0002H\u0007H\u0002I\u0007I\u0002J\u0007"+
		"J\u0002K\u0007K\u0002L\u0007L\u0002M\u0007M\u0002N\u0007N\u0002O\u0007"+
		"O\u0002P\u0007P\u0002Q\u0007Q\u0002R\u0007R\u0002S\u0007S\u0002T\u0007"+
		"T\u0002U\u0007U\u0002V\u0007V\u0002W\u0007W\u0002X\u0007X\u0002Y\u0007"+
		"Y\u0002Z\u0007Z\u0002[\u0007[\u0002\\\u0007\\\u0002]\u0007]\u0002^\u0007"+
		"^\u0002_\u0007_\u0002`\u0007`\u0002a\u0007a\u0002b\u0007b\u0002c\u0007"+
		"c\u0002d\u0007d\u0002e\u0007e\u0002f\u0007f\u0002g\u0007g\u0002h\u0007"+
		"h\u0002i\u0007i\u0002j\u0007j\u0002k\u0007k\u0002l\u0007l\u0002m\u0007"+
		"m\u0002n\u0007n\u0002o\u0007o\u0002p\u0007p\u0002q\u0007q\u0002r\u0007"+
		"r\u0002s\u0007s\u0002t\u0007t\u0002u\u0007u\u0002v\u0007v\u0002w\u0007"+
		"w\u0002x\u0007x\u0002y\u0007y\u0002z\u0007z\u0002{\u0007{\u0002|\u0007"+
		"|\u0002}\u0007}\u0002~\u0007~\u0002\u007f\u0007\u007f\u0002\u0080\u0007"+
		"\u0080\u0002\u0081\u0007\u0081\u0002\u0082\u0007\u0082\u0002\u0083\u0007"+
		"\u0083\u0002\u0084\u0007\u0084\u0002\u0085\u0007\u0085\u0002\u0086\u0007"+
		"\u0086\u0002\u0087\u0007\u0087\u0002\u0088\u0007\u0088\u0002\u0089\u0007"+
		"\u0089\u0002\u008a\u0007\u008a\u0002\u008b\u0007\u008b\u0002\u008c\u0007"+
		"\u008c\u0002\u008d\u0007\u008d\u0002\u008e\u0007\u008e\u0002\u008f\u0007"+
		"\u008f\u0002\u0090\u0007\u0090\u0002\u0091\u0007\u0091\u0002\u0092\u0007"+
		"\u0092\u0002\u0093\u0007\u0093\u0002\u0094\u0007\u0094\u0002\u0095\u0007"+
		"\u0095\u0002\u0096\u0007\u0096\u0002\u0097\u0007\u0097\u0002\u0098\u0007"+
		"\u0098\u0002\u0099\u0007\u0099\u0002\u009a\u0007\u009a\u0002\u009b\u0007"+
		"\u009b\u0002\u009c\u0007\u009c\u0002\u009d\u0007\u009d\u0002\u009e\u0007"+
		"\u009e\u0002\u009f\u0007\u009f\u0002\u00a0\u0007\u00a0\u0002\u00a1\u0007"+
		"\u00a1\u0002\u00a2\u0007\u00a2\u0002\u00a3\u0007\u00a3\u0002\u00a4\u0007"+
		"\u00a4\u0002\u00a5\u0007\u00a5\u0002\u00a6\u0007\u00a6\u0002\u00a7\u0007"+
		"\u00a7\u0002\u00a8\u0007\u00a8\u0002\u00a9\u0007\u00a9\u0002\u00aa\u0007"+
		"\u00aa\u0002\u00ab\u0007\u00ab\u0002\u00ac\u0007\u00ac\u0002\u00ad\u0007"+
		"\u00ad\u0002\u00ae\u0007\u00ae\u0002\u00af\u0007\u00af\u0002\u00b0\u0007"+
		"\u00b0\u0002\u00b1\u0007\u00b1\u0002\u00b2\u0007\u00b2\u0002\u00b3\u0007"+
		"\u00b3\u0002\u00b4\u0007\u00b4\u0002\u00b5\u0007\u00b5\u0002\u00b6\u0007"+
		"\u00b6\u0002\u00b7\u0007\u00b7\u0002\u00b8\u0007\u00b8\u0002\u00b9\u0007"+
		"\u00b9\u0002\u00ba\u0007\u00ba\u0002\u00bb\u0007\u00bb\u0002\u00bc\u0007"+
		"\u00bc\u0002\u00bd\u0007\u00bd\u0002\u00be\u0007\u00be\u0001\u0000\u0003"+
		"\u0000\u0180\b\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0004\u0001\u0185"+
		"\b\u0001\u000b\u0001\f\u0001\u0186\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001\u0190\b\u0001"+
		"\u0001\u0002\u0001\u0002\u0003\u0002\u0194\b\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003"+
		"\u019d\b\u0003\u0001\u0003\u0003\u0003\u01a0\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0003\u0004\u01a4\b\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u01ac\b\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u01b3\b\u0005\u0001"+
		"\u0005\u0003\u0005\u01b6\b\u0005\u0001\u0005\u0005\u0005\u01b9\b\u0005"+
		"\n\u0005\f\u0005\u01bc\t\u0005\u0001\u0006\u0001\u0006\u0003\u0006\u01c0"+
		"\b\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0003\u0007\u01c6"+
		"\b\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0003"+
		"\b\u01ce\b\b\u0003\b\u01d0\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n"+
		"\u0005\n\u01d7\b\n\n\n\f\n\u01da\t\n\u0001\n\u0003\n\u01dd\b\n\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u01e1\b\u000b\u0001\f\u0003\f\u01e4\b\f\u0001"+
		"\f\u0001\f\u0003\f\u01e8\b\f\u0001\r\u0003\r\u01eb\b\r\u0001\r\u0001\r"+
		"\u0001\r\u0001\u000e\u0001\u000e\u0003\u000e\u01f2\b\u000e\u0001\u000e"+
		"\u0001\u000e\u0003\u000e\u01f6\b\u000e\u0001\u000e\u0003\u000e\u01f9\b"+
		"\u000e\u0001\u000e\u0003\u000e\u01fc\b\u000e\u0001\u000e\u0003\u000e\u01ff"+
		"\b\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0205"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u0209\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u020d\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u021b\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u021f\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u0225\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0003\u000f\u022c\b\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0003\u000f\u0232\b\u000f\u0001\u000f\u0001\u000f"+
		"\u0003\u000f\u0236\b\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u023a\b"+
		"\u000f\n\u000f\f\u000f\u023d\t\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0003\u0012\u0244\b\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0003\u0012\u0249\b\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0003\u0012\u0256\b\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u025d\b\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0269\b\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0003\u0013\u0273\b\u0013\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0003\u0015\u0278\b\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u027c\b"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003"+
		"\u0015\u0283\b\u0015\u0001\u0015\u0003\u0015\u0286\b\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0003\u0017"+
		"\u028e\b\u0017\u0001\u0018\u0001\u0018\u0003\u0018\u0292\b\u0018\u0001"+
		"\u0018\u0003\u0018\u0295\b\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0003\u0019\u029c\b\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u02a3\b\u0019\u0005\u0019\u02a5"+
		"\b\u0019\n\u0019\f\u0019\u02a8\t\u0019\u0001\u001a\u0001\u001a\u0003\u001a"+
		"\u02ac\b\u001a\u0001\u001a\u0001\u001a\u0003\u001a\u02b0\b\u001a\u0001"+
		"\u001b\u0003\u001b\u02b3\b\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0003"+
		"\u001b\u02b8\b\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0003\u001d\u02c7\b\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0005\u001e\u02cc\b\u001e\n\u001e\f\u001e\u02cf\t\u001e"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0005\u001f\u02d4\b\u001f\n\u001f"+
		"\f\u001f\u02d7\t\u001f\u0001 \u0001 \u0001 \u0005 \u02dc\b \n \f \u02df"+
		"\t \u0001!\u0001!\u0001!\u0001!\u0005!\u02e5\b!\n!\f!\u02e8\t!\u0001\""+
		"\u0001\"\u0001\"\u0001\"\u0003\"\u02ee\b\"\u0001#\u0001#\u0001#\u0005"+
		"#\u02f3\b#\n#\f#\u02f6\t#\u0001$\u0001$\u0001$\u0005$\u02fb\b$\n$\f$\u02fe"+
		"\t$\u0001%\u0001%\u0001%\u0005%\u0303\b%\n%\f%\u0306\t%\u0001&\u0001&"+
		"\u0001&\u0005&\u030b\b&\n&\f&\u030e\t&\u0001\'\u0001\'\u0001\'\u0005\'"+
		"\u0313\b\'\n\'\f\'\u0316\t\'\u0001(\u0001(\u0001(\u0005(\u031b\b(\n(\f"+
		"(\u031e\t(\u0001)\u0001)\u0001)\u0005)\u0323\b)\n)\f)\u0326\t)\u0001*"+
		"\u0001*\u0001*\u0001*\u0001*\u0001*\u0003*\u032e\b*\u0001+\u0001+\u0001"+
		"+\u0001+\u0001+\u0001+\u0003+\u0336\b+\u0001,\u0001,\u0001-\u0001-\u0001"+
		"-\u0005-\u033d\b-\n-\f-\u0340\t-\u0001.\u0001.\u0001/\u0001/\u0001/\u0003"+
		"/\u0347\b/\u0001/\u0001/\u0001/\u0001/\u0001/\u0001/\u0003/\u034f\b/\u0003"+
		"/\u0351\b/\u00010\u00030\u0354\b0\u00010\u00010\u00010\u00010\u00030\u035a"+
		"\b0\u00010\u00010\u00010\u00011\u00031\u0360\b1\u00011\u00011\u00012\u0001"+
		"2\u00032\u0366\b2\u00012\u00012\u00013\u00043\u036b\b3\u000b3\f3\u036c"+
		"\u00014\u00014\u00014\u00014\u00014\u00014\u00014\u00034\u0376\b4\u0001"+
		"4\u00014\u00014\u00014\u00014\u00014\u00034\u037e\b4\u00015\u00015\u0003"+
		"5\u0382\b5\u00015\u00015\u00015\u00015\u00015\u00035\u0389\b5\u00035\u038b"+
		"\b5\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u0001"+
		"6\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00016\u00036\u039f"+
		"\b6\u00016\u00016\u00036\u03a3\b6\u00016\u00016\u00016\u00016\u00036\u03a9"+
		"\b6\u00016\u00016\u00016\u00036\u03ae\b6\u00017\u00017\u00037\u03b2\b"+
		"7\u00018\u00038\u03b5\b8\u00018\u00018\u00018\u00019\u00019\u00039\u03bc"+
		"\b9\u0001:\u0001:\u0001:\u0001:\u0001:\u0003:\u03c3\b:\u0001:\u0001:\u0003"+
		":\u03c7\b:\u0001:\u0001:\u0001;\u0001;\u0001<\u0004<\u03ce\b<\u000b<\f"+
		"<\u03cf\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001=\u0001"+
		"=\u0003=\u03db\b=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001>\u0001"+
		">\u0003>\u03e5\b>\u0001?\u0001?\u0001?\u0003?\u03ea\b?\u0001?\u0001?\u0001"+
		"?\u0001?\u0001@\u0003@\u03f1\b@\u0001@\u0003@\u03f4\b@\u0001@\u0001@\u0001"+
		"@\u0003@\u03f9\b@\u0001@\u0001@\u0001@\u0003@\u03fe\b@\u0001A\u0001A\u0001"+
		"A\u0001A\u0001A\u0001A\u0001A\u0001A\u0001B\u0001B\u0001C\u0001C\u0001"+
		"C\u0001D\u0001D\u0001D\u0001D\u0001D\u0001D\u0003D\u0413\bD\u0001E\u0004"+
		"E\u0416\bE\u000bE\fE\u0417\u0001E\u0003E\u041b\bE\u0001F\u0001F\u0001"+
		"G\u0001G\u0001H\u0001H\u0001I\u0001I\u0001I\u0003I\u0426\bI\u0001J\u0001"+
		"J\u0001J\u0001J\u0003J\u042c\bJ\u0001K\u0004K\u042f\bK\u000bK\fK\u0430"+
		"\u0001K\u0003K\u0434\bK\u0001L\u0004L\u0437\bL\u000bL\fL\u0438\u0001L"+
		"\u0003L\u043c\bL\u0001M\u0001M\u0001N\u0001N\u0001O\u0003O\u0443\bO\u0001"+
		"O\u0001O\u0001O\u0001O\u0001O\u0001O\u0001O\u0003O\u044c\bO\u0001O\u0004"+
		"O\u044f\bO\u000bO\fO\u0450\u0001O\u0003O\u0454\bO\u0001O\u0001O\u0003"+
		"O\u0458\bO\u0001O\u0001O\u0003O\u045c\bO\u0001O\u0001O\u0003O\u0460\b"+
		"O\u0001O\u0001O\u0001O\u0003O\u0465\bO\u0001O\u0005O\u0468\bO\nO\fO\u046b"+
		"\tO\u0001O\u0001O\u0001O\u0003O\u0470\bO\u0001O\u0001O\u0001O\u0001O\u0003"+
		"O\u0476\bO\u0001P\u0001P\u0001P\u0001P\u0003P\u047c\bP\u0001Q\u0001Q\u0001"+
		"Q\u0001Q\u0003Q\u0482\bQ\u0001Q\u0001Q\u0001R\u0001R\u0003R\u0488\bR\u0001"+
		"R\u0003R\u048b\bR\u0001R\u0001R\u0001R\u0001R\u0003R\u0491\bR\u0001R\u0001"+
		"R\u0003R\u0495\bR\u0001R\u0001R\u0003R\u0499\bR\u0001R\u0003R\u049c\b"+
		"R\u0001S\u0001S\u0001T\u0001T\u0001T\u0001T\u0003T\u04a4\bT\u0003T\u04a6"+
		"\bT\u0001T\u0001T\u0001U\u0001U\u0003U\u04ac\bU\u0001U\u0003U\u04af\b"+
		"U\u0001U\u0003U\u04b2\bU\u0001U\u0003U\u04b5\bU\u0001V\u0001V\u0003V\u04b9"+
		"\bV\u0001V\u0001V\u0003V\u04bd\bV\u0001V\u0001V\u0001W\u0001W\u0003W\u04c3"+
		"\bW\u0001X\u0001X\u0001X\u0001Y\u0001Y\u0001Y\u0005Y\u04cb\bY\nY\fY\u04ce"+
		"\tY\u0001Z\u0001Z\u0001Z\u0003Z\u04d3\bZ\u0001[\u0001[\u0001\\\u0001\\"+
		"\u0003\\\u04d9\b\\\u0001]\u0001]\u0001^\u0003^\u04de\b^\u0001^\u0001^"+
		"\u0001^\u0003^\u04e3\b^\u0001^\u0001^\u0003^\u04e7\b^\u0001^\u0001^\u0001"+
		"_\u0001_\u0001`\u0001`\u0001`\u0001`\u0001`\u0001`\u0001a\u0003a\u04f4"+
		"\ba\u0001a\u0001a\u0001b\u0001b\u0003b\u04fa\bb\u0001b\u0001b\u0003b\u04fe"+
		"\bb\u0001b\u0001b\u0001b\u0001c\u0003c\u0504\bc\u0001c\u0001c\u0001c\u0003"+
		"c\u0509\bc\u0001c\u0001c\u0001c\u0001d\u0001d\u0001d\u0001d\u0001d\u0001"+
		"d\u0001e\u0001e\u0001e\u0001e\u0003e\u0518\be\u0001e\u0001e\u0003e\u051c"+
		"\be\u0001f\u0004f\u051f\bf\u000bf\ff\u0520\u0001g\u0001g\u0001g\u0003"+
		"g\u0526\bg\u0001g\u0001g\u0001g\u0003g\u052b\bg\u0001h\u0001h\u0001h\u0001"+
		"h\u0003h\u0531\bh\u0001h\u0003h\u0534\bh\u0001h\u0001h\u0001i\u0001i\u0001"+
		"i\u0005i\u053b\bi\ni\fi\u053e\ti\u0001i\u0003i\u0541\bi\u0001j\u0001j"+
		"\u0001j\u0003j\u0546\bj\u0001j\u0001j\u0003j\u054a\bj\u0001k\u0001k\u0001"+
		"l\u0001l\u0003l\u0550\bl\u0001l\u0001l\u0001m\u0004m\u0555\bm\u000bm\f"+
		"m\u0556\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001n\u0001"+
		"n\u0001n\u0001n\u0001n\u0001n\u0004n\u0566\bn\u000bn\fn\u0567\u0003n\u056a"+
		"\bn\u0001o\u0001o\u0001o\u0005o\u056f\bo\no\fo\u0572\to\u0001p\u0001p"+
		"\u0003p\u0576\bp\u0001q\u0001q\u0001q\u0001q\u0001q\u0003q\u057d\bq\u0001"+
		"r\u0001r\u0003r\u0581\br\u0005r\u0583\br\nr\fr\u0586\tr\u0001r\u0001r"+
		"\u0001s\u0001s\u0001s\u0003s\u058d\bs\u0001s\u0001s\u0001s\u0001s\u0003"+
		"s\u0593\bs\u0001s\u0001s\u0001s\u0001s\u0003s\u0599\bs\u0001s\u0001s\u0003"+
		"s\u059d\bs\u0003s\u059f\bs\u0005s\u05a1\bs\ns\fs\u05a4\ts\u0001t\u0001"+
		"t\u0003t\u05a8\bt\u0001t\u0001t\u0003t\u05ac\bt\u0001t\u0003t\u05af\b"+
		"t\u0001t\u0003t\u05b2\bt\u0001t\u0003t\u05b5\bt\u0001u\u0001u\u0001u\u0003"+
		"u\u05ba\bu\u0001v\u0001v\u0003v\u05be\bv\u0001v\u0003v\u05c1\bv\u0001"+
		"v\u0001v\u0003v\u05c5\bv\u0001v\u0003v\u05c8\bv\u0003v\u05ca\bv\u0001"+
		"w\u0004w\u05cd\bw\u000bw\fw\u05ce\u0001x\u0001x\u0001y\u0001y\u0001z\u0003"+
		"z\u05d6\bz\u0001z\u0001z\u0001{\u0001{\u0003{\u05dc\b{\u0001|\u0001|\u0003"+
		"|\u05e0\b|\u0001|\u0001|\u0001|\u0001|\u0003|\u05e6\b|\u0001}\u0001}\u0004"+
		"}\u05ea\b}\u000b}\f}\u05eb\u0001}\u0003}\u05ef\b}\u0003}\u05f1\b}\u0001"+
		"~\u0001~\u0001~\u0001~\u0003~\u05f7\b~\u0001~\u0001~\u0003~\u05fb\b~\u0001"+
		"~\u0001~\u0001~\u0001~\u0003~\u0601\b~\u0001~\u0001~\u0001~\u0001~\u0001"+
		"~\u0003~\u0608\b~\u0001~\u0001~\u0003~\u060c\b~\u0003~\u060e\b~\u0005"+
		"~\u0610\b~\n~\f~\u0613\t~\u0001\u007f\u0005\u007f\u0616\b\u007f\n\u007f"+
		"\f\u007f\u0619\t\u007f\u0001\u007f\u0001\u007f\u0001\u0080\u0001\u0080"+
		"\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0001\u0080\u0003\u0080"+
		"\u0624\b\u0080\u0001\u0080\u0001\u0080\u0003\u0080\u0628\b\u0080\u0003"+
		"\u0080\u062a\b\u0080\u0005\u0080\u062c\b\u0080\n\u0080\f\u0080\u062f\t"+
		"\u0080\u0001\u0081\u0001\u0081\u0003\u0081\u0633\b\u0081\u0001\u0081\u0003"+
		"\u0081\u0636\b\u0081\u0001\u0082\u0001\u0082\u0001\u0082\u0005\u0082\u063b"+
		"\b\u0082\n\u0082\f\u0082\u063e\t\u0082\u0001\u0083\u0003\u0083\u0641\b"+
		"\u0083\u0001\u0083\u0001\u0083\u0001\u0083\u0003\u0083\u0646\b\u0083\u0003"+
		"\u0083\u0648\b\u0083\u0001\u0083\u0001\u0083\u0003\u0083\u064c\b\u0083"+
		"\u0001\u0084\u0003\u0084\u064f\b\u0084\u0001\u0084\u0003\u0084\u0652\b"+
		"\u0084\u0001\u0084\u0001\u0084\u0003\u0084\u0656\b\u0084\u0001\u0084\u0001"+
		"\u0084\u0001\u0085\u0003\u0085\u065b\b\u0085\u0001\u0085\u0001\u0085\u0001"+
		"\u0085\u0001\u0085\u0001\u0085\u0003\u0085\u0662\b\u0085\u0001\u0086\u0001"+
		"\u0086\u0001\u0086\u0001\u0086\u0001\u0086\u0003\u0086\u0669\b\u0086\u0001"+
		"\u0087\u0001\u0087\u0001\u0087\u0003\u0087\u066e\b\u0087\u0001\u0088\u0001"+
		"\u0088\u0003\u0088\u0672\b\u0088\u0001\u0089\u0001\u0089\u0003\u0089\u0676"+
		"\b\u0089\u0001\u0089\u0001\u0089\u0001\u0089\u0003\u0089\u067b\b\u0089"+
		"\u0005\u0089\u067d\b\u0089\n\u0089\f\u0089\u0680\t\u0089\u0001\u008a\u0001"+
		"\u008a\u0001\u008a\u0003\u008a\u0685\b\u008a\u0003\u008a\u0687\b\u008a"+
		"\u0001\u008a\u0001\u008a\u0001\u008b\u0001\u008b\u0003\u008b\u068d\b\u008b"+
		"\u0001\u008c\u0001\u008c\u0001\u008c\u0003\u008c\u0692\b\u008c\u0001\u008c"+
		"\u0001\u008c\u0001\u008d\u0001\u008d\u0003\u008d\u0698\b\u008d\u0001\u008d"+
		"\u0001\u008d\u0003\u008d\u069c\b\u008d\u0003\u008d\u069e\b\u008d\u0001"+
		"\u008d\u0003\u008d\u06a1\b\u008d\u0001\u008d\u0001\u008d\u0003\u008d\u06a5"+
		"\b\u008d\u0001\u008d\u0001\u008d\u0003\u008d\u06a9\b\u008d\u0003\u008d"+
		"\u06ab\b\u008d\u0003\u008d\u06ad\b\u008d\u0001\u008e\u0003\u008e\u06b0"+
		"\b\u008e\u0001\u008e\u0001\u008e\u0001\u008f\u0001\u008f\u0001\u0090\u0001"+
		"\u0090\u0001\u0091\u0001\u0091\u0001\u0091\u0001\u0091\u0004\u0091\u06bc"+
		"\b\u0091\u000b\u0091\f\u0091\u06bd\u0001\u0092\u0003\u0092\u06c1\b\u0092"+
		"\u0001\u0092\u0003\u0092\u06c4\b\u0092\u0001\u0092\u0003\u0092\u06c7\b"+
		"\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001\u0092\u0001"+
		"\u0092\u0001\u0092\u0003\u0092\u06d0\b\u0092\u0001\u0093\u0001\u0093\u0001"+
		"\u0093\u0005\u0093\u06d5\b\u0093\n\u0093\f\u0093\u06d8\t\u0093\u0001\u0094"+
		"\u0001\u0094\u0003\u0094\u06dc\b\u0094\u0001\u0094\u0003\u0094\u06df\b"+
		"\u0094\u0001\u0094\u0003\u0094\u06e2\b\u0094\u0003\u0094\u06e4\b\u0094"+
		"\u0001\u0094\u0003\u0094\u06e7\b\u0094\u0001\u0094\u0003\u0094\u06ea\b"+
		"\u0094\u0001\u0094\u0001\u0094\u0003\u0094\u06ee\b\u0094\u0001\u0095\u0004"+
		"\u0095\u06f1\b\u0095\u000b\u0095\f\u0095\u06f2\u0001\u0096\u0001\u0096"+
		"\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0097\u0001\u0098\u0001\u0098"+
		"\u0001\u0098\u0001\u0099\u0001\u0099\u0003\u0099\u0700\b\u0099\u0001\u0099"+
		"\u0001\u0099\u0001\u0099\u0003\u0099\u0705\b\u0099\u0005\u0099\u0707\b"+
		"\u0099\n\u0099\f\u0099\u070a\t\u0099\u0001\u009a\u0003\u009a\u070d\b\u009a"+
		"\u0001\u009a\u0001\u009a\u0001\u009a\u0003\u009a\u0712\b\u009a\u0001\u009a"+
		"\u0001\u009a\u0001\u009a\u0003\u009a\u0717\b\u009a\u0001\u009a\u0001\u009a"+
		"\u0003\u009a\u071b\b\u009a\u0001\u009b\u0003\u009b\u071e\b\u009b\u0001"+
		"\u009b\u0001\u009b\u0003\u009b\u0722\b\u009b\u0001\u009c\u0001\u009c\u0001"+
		"\u009d\u0001\u009d\u0001\u009e\u0001\u009e\u0001\u009e\u0001\u009f\u0001"+
		"\u009f\u0003\u009f\u072d\b\u009f\u0001\u00a0\u0001\u00a0\u0003\u00a0\u0731"+
		"\b\u00a0\u0001\u00a1\u0001\u00a1\u0001\u00a1\u0001\u00a2\u0001\u00a2\u0003"+
		"\u00a2\u0738\b\u00a2\u0001\u00a2\u0001\u00a2\u0001\u00a2\u0003\u00a2\u073d"+
		"\b\u00a2\u0005\u00a2\u073f\b\u00a2\n\u00a2\f\u00a2\u0742\t\u00a2\u0001"+
		"\u00a3\u0001\u00a3\u0001\u00a3\u0003\u00a3\u0747\b\u00a3\u0001\u00a3\u0001"+
		"\u00a3\u0003\u00a3\u074b\b\u00a3\u0001\u00a4\u0001\u00a4\u0003\u00a4\u074f"+
		"\b\u00a4\u0001\u00a5\u0001\u00a5\u0001\u00a5\u0001\u00a6\u0001\u00a6\u0001"+
		"\u00a6\u0001\u00a6\u0003\u00a6\u0758\b\u00a6\u0001\u00a7\u0001\u00a7\u0001"+
		"\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a7\u0001\u00a8\u0001\u00a8\u0001"+
		"\u00a8\u0005\u00a8\u0763\b\u00a8\n\u00a8\f\u00a8\u0766\t\u00a8\u0001\u00a9"+
		"\u0001\u00a9\u0003\u00a9\u076a\b\u00a9\u0001\u00aa\u0001\u00aa\u0001\u00aa"+
		"\u0001\u00aa\u0001\u00aa\u0003\u00aa\u0771\b\u00aa\u0001\u00aa\u0001\u00aa"+
		"\u0003\u00aa\u0775\b\u00aa\u0001\u00aa\u0003\u00aa\u0778\b\u00aa\u0001"+
		"\u00aa\u0003\u00aa\u077b\b\u00aa\u0001\u00aa\u0003\u00aa\u077e\b\u00aa"+
		"\u0001\u00aa\u0001\u00aa\u0003\u00aa\u0782\b\u00aa\u0001\u00ab\u0001\u00ab"+
		"\u0001\u00ab\u0003\u00ab\u0787\b\u00ab\u0001\u00ab\u0001\u00ab\u0001\u00ac"+
		"\u0001\u00ac\u0001\u00ac\u0003\u00ac\u078e\b\u00ac\u0001\u00ac\u0001\u00ac"+
		"\u0003\u00ac\u0792\b\u00ac\u0001\u00ac\u0001\u00ac\u0003\u00ac\u0796\b"+
		"\u00ac\u0001\u00ad\u0001\u00ad\u0001\u00ae\u0001\u00ae\u0003\u00ae\u079c"+
		"\b\u00ae\u0001\u00ae\u0001\u00ae\u0001\u00ae\u0003\u00ae\u07a1\b\u00ae"+
		"\u0005\u00ae\u07a3\b\u00ae\n\u00ae\f\u00ae\u07a6\t\u00ae\u0001\u00af\u0001"+
		"\u00af\u0001\u00af\u0003\u00af\u07ab\b\u00af\u0001\u00b0\u0001\u00b0\u0001"+
		"\u00b0\u0001\u00b0\u0003\u00b0\u07b1\b\u00b0\u0001\u00b0\u0003\u00b0\u07b4"+
		"\b\u00b0\u0001\u00b1\u0003\u00b1\u07b7\b\u00b1\u0001\u00b1\u0001\u00b1"+
		"\u0001\u00b1\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001\u00b2\u0001\u00b2"+
		"\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b3\u0001\u00b4\u0001\u00b4"+
		"\u0003\u00b4\u07c7\b\u00b4\u0001\u00b4\u0001\u00b4\u0001\u00b4\u0001\u00b5"+
		"\u0004\u00b5\u07cd\b\u00b5\u000b\u00b5\f\u00b5\u07ce\u0001\u00b6\u0001"+
		"\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b6\u0001\u00b7\u0003"+
		"\u00b7\u07d8\b\u00b7\u0001\u00b7\u0001\u00b7\u0001\u00b7\u0003\u00b7\u07dd"+
		"\b\u00b7\u0001\u00b7\u0003\u00b7\u07e0\b\u00b7\u0001\u00b8\u0001\u00b8"+
		"\u0003\u00b8\u07e4\b\u00b8\u0001\u00b9\u0001\u00b9\u0003\u00b9\u07e8\b"+
		"\u00b9\u0001\u00ba\u0001\u00ba\u0001\u00ba\u0003\u00ba\u07ed\b\u00ba\u0001"+
		"\u00ba\u0001\u00ba\u0001\u00bb\u0001\u00bb\u0003\u00bb\u07f3\b\u00bb\u0001"+
		"\u00bb\u0001\u00bb\u0001\u00bb\u0003\u00bb\u07f8\b\u00bb\u0005\u00bb\u07fa"+
		"\b\u00bb\n\u00bb\f\u00bb\u07fd\t\u00bb\u0001\u00bc\u0001\u00bc\u0001\u00bc"+
		"\u0001\u00bc\u0001\u00bc\u0001\u00bc\u0003\u00bc\u0805\b\u00bc\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0003\u00bd\u080a\b\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0003\u00bd\u080f\b\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd\u0001\u00bd"+
		"\u0001\u00bd\u0001\u00bd\u0003\u00bd\u083a\b\u00bd\u0001\u00be\u0001\u00be"+
		"\u0001\u00be\u0001\u0417\u0006\n\u001e2\u00e6\u00fc\u0100\u00bf\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02468:<>@BDFHJLNPRTVXZ\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084"+
		"\u0086\u0088\u008a\u008c\u008e\u0090\u0092\u0094\u0096\u0098\u009a\u009c"+
		"\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4"+
		"\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc"+
		"\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da\u00dc\u00de\u00e0\u00e2\u00e4"+
		"\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc"+
		"\u00fe\u0100\u0102\u0104\u0106\u0108\u010a\u010c\u010e\u0110\u0112\u0114"+
		"\u0116\u0118\u011a\u011c\u011e\u0120\u0122\u0124\u0126\u0128\u012a\u012c"+
		"\u012e\u0130\u0132\u0134\u0136\u0138\u013a\u013c\u013e\u0140\u0142\u0144"+
		"\u0146\u0148\u014a\u014c\u014e\u0150\u0152\u0154\u0156\u0158\u015a\u015c"+
		"\u015e\u0160\u0162\u0164\u0166\u0168\u016a\u016c\u016e\u0170\u0172\u0174"+
		"\u0176\u0178\u017a\u017c\u0000\u0017\u0002\u0000aaee\u0004\u0000\u0018"+
		"\u0018\u001f\u001f::AA\u0002\u0000||\u0081\u0081\u0001\u0000xy\u0002\u0000"+
		"[]ad\u0002\u0000{{\u0082\u0082\u0001\u0000]_\u0001\u0000[\\\u0002\u0000"+
		"fgtu\u0001\u0000rs\u0002\u0000eehq\u0005\u0000$$//99??FF\u0003\u0000\""+
		"\",,PP\u0002\u0000..<<\u0002\u0000==NN\u0002\u0000\u0015\u0015BB\u0001"+
		"\u0000UZ\u0002\u0000aavv\u0002\u0000\u0016\u0016RR\u0001\u0000\u001b\u001c"+
		"\u0002\u0000&&55\u0001\u000068\u0001\u0000\u0001\u0007\u0927\u0000\u017f"+
		"\u0001\u0000\u0000\u0000\u0002\u018f\u0001\u0000\u0000\u0000\u0004\u0193"+
		"\u0001\u0000\u0000\u0000\u0006\u019f\u0001\u0000\u0000\u0000\b\u01a1\u0001"+
		"\u0000\u0000\u0000\n\u01a7\u0001\u0000\u0000\u0000\f\u01bd\u0001\u0000"+
		"\u0000\u0000\u000e\u01c3\u0001\u0000\u0000\u0000\u0010\u01cf\u0001\u0000"+
		"\u0000\u0000\u0012\u01d1\u0001\u0000\u0000\u0000\u0014\u01d3\u0001\u0000"+
		"\u0000\u0000\u0016\u01e0\u0001\u0000\u0000\u0000\u0018\u01e7\u0001\u0000"+
		"\u0000\u0000\u001a\u01ea\u0001\u0000\u0000\u0000\u001c\u01ef\u0001\u0000"+
		"\u0000\u0000\u001e\u021e\u0001\u0000\u0000\u0000 \u023e\u0001\u0000\u0000"+
		"\u0000\"\u0240\u0001\u0000\u0000\u0000$\u0255\u0001\u0000\u0000\u0000"+
		"&\u0272\u0001\u0000\u0000\u0000(\u0274\u0001\u0000\u0000\u0000*\u0277"+
		"\u0001\u0000\u0000\u0000,\u0287\u0001\u0000\u0000\u0000.\u028b\u0001\u0000"+
		"\u0000\u00000\u0294\u0001\u0000\u0000\u00002\u0296\u0001\u0000\u0000\u0000"+
		"4\u02af\u0001\u0000\u0000\u00006\u02b2\u0001\u0000\u0000\u00008\u02bb"+
		"\u0001\u0000\u0000\u0000:\u02c6\u0001\u0000\u0000\u0000<\u02c8\u0001\u0000"+
		"\u0000\u0000>\u02d0\u0001\u0000\u0000\u0000@\u02d8\u0001\u0000\u0000\u0000"+
		"B\u02e0\u0001\u0000\u0000\u0000D\u02ed\u0001\u0000\u0000\u0000F\u02ef"+
		"\u0001\u0000\u0000\u0000H\u02f7\u0001\u0000\u0000\u0000J\u02ff\u0001\u0000"+
		"\u0000\u0000L\u0307\u0001\u0000\u0000\u0000N\u030f\u0001\u0000\u0000\u0000"+
		"P\u0317\u0001\u0000\u0000\u0000R\u031f\u0001\u0000\u0000\u0000T\u0327"+
		"\u0001\u0000\u0000\u0000V\u0335\u0001\u0000\u0000\u0000X\u0337\u0001\u0000"+
		"\u0000\u0000Z\u0339\u0001\u0000\u0000\u0000\\\u0341\u0001\u0000\u0000"+
		"\u0000^\u0350\u0001\u0000\u0000\u0000`\u0353\u0001\u0000\u0000\u0000b"+
		"\u035f\u0001\u0000\u0000\u0000d\u0363\u0001\u0000\u0000\u0000f\u036a\u0001"+
		"\u0000\u0000\u0000h\u037d\u0001\u0000\u0000\u0000j\u038a\u0001\u0000\u0000"+
		"\u0000l\u03ad\u0001\u0000\u0000\u0000n\u03b1\u0001\u0000\u0000\u0000p"+
		"\u03b4\u0001\u0000\u0000\u0000r\u03bb\u0001\u0000\u0000\u0000t\u03c6\u0001"+
		"\u0000\u0000\u0000v\u03ca\u0001\u0000\u0000\u0000x\u03cd\u0001\u0000\u0000"+
		"\u0000z\u03da\u0001\u0000\u0000\u0000|\u03e4\u0001\u0000\u0000\u0000~"+
		"\u03e6\u0001\u0000\u0000\u0000\u0080\u03fd\u0001\u0000\u0000\u0000\u0082"+
		"\u03ff\u0001\u0000\u0000\u0000\u0084\u0407\u0001\u0000\u0000\u0000\u0086"+
		"\u0409\u0001\u0000\u0000\u0000\u0088\u0412\u0001\u0000\u0000\u0000\u008a"+
		"\u0415\u0001\u0000\u0000\u0000\u008c\u041c\u0001\u0000\u0000\u0000\u008e"+
		"\u041e\u0001\u0000\u0000\u0000\u0090\u0420\u0001\u0000\u0000\u0000\u0092"+
		"\u0425\u0001\u0000\u0000\u0000\u0094\u042b\u0001\u0000\u0000\u0000\u0096"+
		"\u042e\u0001\u0000\u0000\u0000\u0098\u0436\u0001\u0000\u0000\u0000\u009a"+
		"\u043d\u0001\u0000\u0000\u0000\u009c\u043f\u0001\u0000\u0000\u0000\u009e"+
		"\u0475\u0001\u0000\u0000\u0000\u00a0\u047b\u0001\u0000\u0000\u0000\u00a2"+
		"\u047d\u0001\u0000\u0000\u0000\u00a4\u049b\u0001\u0000\u0000\u0000\u00a6"+
		"\u049d\u0001\u0000\u0000\u0000\u00a8\u049f\u0001\u0000\u0000\u0000\u00aa"+
		"\u04a9\u0001\u0000\u0000\u0000\u00ac\u04b6\u0001\u0000\u0000\u0000\u00ae"+
		"\u04c0\u0001\u0000\u0000\u0000\u00b0\u04c4\u0001\u0000\u0000\u0000\u00b2"+
		"\u04c7\u0001\u0000\u0000\u0000\u00b4\u04cf\u0001\u0000\u0000\u0000\u00b6"+
		"\u04d4\u0001\u0000\u0000\u0000\u00b8\u04d8\u0001\u0000\u0000\u0000\u00ba"+
		"\u04da\u0001\u0000\u0000\u0000\u00bc\u04dd\u0001\u0000\u0000\u0000\u00be"+
		"\u04ea\u0001\u0000\u0000\u0000\u00c0\u04ec\u0001\u0000\u0000\u0000\u00c2"+
		"\u04f3\u0001\u0000\u0000\u0000\u00c4\u04f7\u0001\u0000\u0000\u0000\u00c6"+
		"\u0503\u0001\u0000\u0000\u0000\u00c8\u050d\u0001\u0000\u0000\u0000\u00ca"+
		"\u0513\u0001\u0000\u0000\u0000\u00cc\u051e\u0001\u0000\u0000\u0000\u00ce"+
		"\u052a\u0001\u0000\u0000\u0000\u00d0\u052c\u0001\u0000\u0000\u0000\u00d2"+
		"\u0537\u0001\u0000\u0000\u0000\u00d4\u0545\u0001\u0000\u0000\u0000\u00d6"+
		"\u054b\u0001\u0000\u0000\u0000\u00d8\u054d\u0001\u0000\u0000\u0000\u00da"+
		"\u0554\u0001\u0000\u0000\u0000\u00dc\u0569\u0001\u0000\u0000\u0000\u00de"+
		"\u056b\u0001\u0000\u0000\u0000\u00e0\u0573\u0001\u0000\u0000\u0000\u00e2"+
		"\u057c\u0001\u0000\u0000\u0000\u00e4\u0584\u0001\u0000\u0000\u0000\u00e6"+
		"\u0592\u0001\u0000\u0000\u0000\u00e8\u05a5\u0001\u0000\u0000\u0000\u00ea"+
		"\u05b6\u0001\u0000\u0000\u0000\u00ec\u05c9\u0001\u0000\u0000\u0000\u00ee"+
		"\u05cc\u0001\u0000\u0000\u0000\u00f0\u05d0\u0001\u0000\u0000\u0000\u00f2"+
		"\u05d2\u0001\u0000\u0000\u0000\u00f4\u05d5\u0001\u0000\u0000\u0000\u00f6"+
		"\u05d9\u0001\u0000\u0000\u0000\u00f8\u05e5\u0001\u0000\u0000\u0000\u00fa"+
		"\u05f0\u0001\u0000\u0000\u0000\u00fc\u0600\u0001\u0000\u0000\u0000\u00fe"+
		"\u0617\u0001\u0000\u0000\u0000\u0100\u061c\u0001\u0000\u0000\u0000\u0102"+
		"\u0630\u0001\u0000\u0000\u0000\u0104\u0637\u0001\u0000\u0000\u0000\u0106"+
		"\u0640\u0001\u0000\u0000\u0000\u0108\u064e\u0001\u0000\u0000\u0000\u010a"+
		"\u0661\u0001\u0000\u0000\u0000\u010c\u0668\u0001\u0000\u0000\u0000\u010e"+
		"\u066d\u0001\u0000\u0000\u0000\u0110\u0671\u0001\u0000\u0000\u0000\u0112"+
		"\u0673\u0001\u0000\u0000\u0000\u0114\u0681\u0001\u0000\u0000\u0000\u0116"+
		"\u068c\u0001\u0000\u0000\u0000\u0118\u068e\u0001\u0000\u0000\u0000\u011a"+
		"\u06ac\u0001\u0000\u0000\u0000\u011c\u06af\u0001\u0000\u0000\u0000\u011e"+
		"\u06b3\u0001\u0000\u0000\u0000\u0120\u06b5\u0001\u0000\u0000\u0000\u0122"+
		"\u06bb\u0001\u0000\u0000\u0000\u0124\u06cf\u0001\u0000\u0000\u0000\u0126"+
		"\u06d1\u0001\u0000\u0000\u0000\u0128\u06ed\u0001\u0000\u0000\u0000\u012a"+
		"\u06f0\u0001\u0000\u0000\u0000\u012c\u06f4\u0001\u0000\u0000\u0000\u012e"+
		"\u06f6\u0001\u0000\u0000\u0000\u0130\u06fa\u0001\u0000\u0000\u0000\u0132"+
		"\u06fd\u0001\u0000\u0000\u0000\u0134\u070c\u0001\u0000\u0000\u0000\u0136"+
		"\u0721\u0001\u0000\u0000\u0000\u0138\u0723\u0001\u0000\u0000\u0000\u013a"+
		"\u0725\u0001\u0000\u0000\u0000\u013c\u0727\u0001\u0000\u0000\u0000\u013e"+
		"\u072a\u0001\u0000\u0000\u0000\u0140\u072e\u0001\u0000\u0000\u0000\u0142"+
		"\u0732\u0001\u0000\u0000\u0000\u0144\u0735\u0001\u0000\u0000\u0000\u0146"+
		"\u0743\u0001\u0000\u0000\u0000\u0148\u074e\u0001\u0000\u0000\u0000\u014a"+
		"\u0750\u0001\u0000\u0000\u0000\u014c\u0753\u0001\u0000\u0000\u0000\u014e"+
		"\u0759\u0001\u0000\u0000\u0000\u0150\u075f\u0001\u0000\u0000\u0000\u0152"+
		"\u0769\u0001\u0000\u0000\u0000\u0154\u0774\u0001\u0000\u0000\u0000\u0156"+
		"\u0783\u0001\u0000\u0000\u0000\u0158\u0795\u0001\u0000\u0000\u0000\u015a"+
		"\u0797\u0001\u0000\u0000\u0000\u015c\u0799\u0001\u0000\u0000\u0000\u015e"+
		"\u07aa\u0001\u0000\u0000\u0000\u0160\u07ac\u0001\u0000\u0000\u0000\u0162"+
		"\u07b6\u0001\u0000\u0000\u0000\u0164\u07bb\u0001\u0000\u0000\u0000\u0166"+
		"\u07c0\u0001\u0000\u0000\u0000\u0168\u07c4\u0001\u0000\u0000\u0000\u016a"+
		"\u07cc\u0001\u0000\u0000\u0000\u016c\u07d0\u0001\u0000\u0000\u0000\u016e"+
		"\u07df\u0001\u0000\u0000\u0000\u0170\u07e1\u0001\u0000\u0000\u0000\u0172"+
		"\u07e7\u0001\u0000\u0000\u0000\u0174\u07e9\u0001\u0000\u0000\u0000\u0176"+
		"\u07f0\u0001\u0000\u0000\u0000\u0178\u0804\u0001\u0000\u0000\u0000\u017a"+
		"\u0839\u0001\u0000\u0000\u0000\u017c\u083b\u0001\u0000\u0000\u0000\u017e"+
		"\u0180\u0003x<\u0000\u017f\u017e\u0001\u0000\u0000\u0000\u017f\u0180\u0001"+
		"\u0000\u0000\u0000\u0180\u0181\u0001\u0000\u0000\u0000\u0181\u0182\u0005"+
		"\u0000\u0000\u0001\u0182\u0001\u0001\u0000\u0000\u0000\u0183\u0185\u0003"+
		"\u017c\u00be\u0000\u0184\u0183\u0001\u0000\u0000\u0000\u0185\u0186\u0001"+
		"\u0000\u0000\u0000\u0186\u0184\u0001\u0000\u0000\u0000\u0186\u0187\u0001"+
		"\u0000\u0000\u0000\u0187\u0190\u0001\u0000\u0000\u0000\u0188\u0190\u0005"+
		"E\u0000\u0000\u0189\u018a\u0005U\u0000\u0000\u018a\u018b\u0003Z-\u0000"+
		"\u018b\u018c\u0005V\u0000\u0000\u018c\u0190\u0001\u0000\u0000\u0000\u018d"+
		"\u0190\u0003\u0004\u0002\u0000\u018e\u0190\u0003\f\u0006\u0000\u018f\u0184"+
		"\u0001\u0000\u0000\u0000\u018f\u0188\u0001\u0000\u0000\u0000\u018f\u0189"+
		"\u0001\u0000\u0000\u0000\u018f\u018d\u0001\u0000\u0000\u0000\u018f\u018e"+
		"\u0001\u0000\u0000\u0000\u0190\u0003\u0001\u0000\u0000\u0000\u0191\u0194"+
		"\u0003\u0006\u0003\u0000\u0192\u0194\u0003\b\u0004\u0000\u0193\u0191\u0001"+
		"\u0000\u0000\u0000\u0193\u0192\u0001\u0000\u0000\u0000\u0194\u0005\u0001"+
		"\u0000\u0000\u0000\u0195\u01a0\u0005\u0084\u0000\u0000\u0196\u01a0\u0003"+
		"\u014a\u00a5\u0000\u0197\u01a0\u0003\u013c\u009e\u0000\u0198\u01a0\u0003"+
		"\u014c\u00a6\u0000\u0199\u019c\u0005c\u0000\u0000\u019a\u019d\u0003\u0116"+
		"\u008b\u0000\u019b\u019d\u0003\u00a2Q\u0000\u019c\u019a\u0001\u0000\u0000"+
		"\u0000\u019c\u019b\u0001\u0000\u0000\u0000\u019d\u01a0\u0001\u0000\u0000"+
		"\u0000\u019e\u01a0\u0003\u0158\u00ac\u0000\u019f\u0195\u0001\u0000\u0000"+
		"\u0000\u019f\u0196\u0001\u0000\u0000\u0000\u019f\u0197\u0001\u0000\u0000"+
		"\u0000\u019f\u0198\u0001\u0000\u0000\u0000\u019f\u0199\u0001\u0000\u0000"+
		"\u0000\u019f\u019e\u0001\u0000\u0000\u0000\u01a0\u0007\u0001\u0000\u0000"+
		"\u0000\u01a1\u01a3\u0003\n\u0005\u0000\u01a2\u01a4\u0005D\u0000\u0000"+
		"\u01a3\u01a2\u0001\u0000\u0000\u0000\u01a3\u01a4\u0001\u0000\u0000\u0000"+
		"\u01a4\u01a5\u0001\u0000\u0000\u0000\u01a5\u01a6\u0003\u0006\u0003\u0000"+
		"\u01a6\t\u0001\u0000\u0000\u0000\u01a7\u01ab\u0006\u0005\uffff\uffff\u0000"+
		"\u01a8\u01ac\u0003\u00a0P\u0000\u01a9\u01ac\u0003\u00b8\\\u0000\u01aa"+
		"\u01ac\u0003\u00a2Q\u0000\u01ab\u01a8\u0001\u0000\u0000\u0000\u01ab\u01a9"+
		"\u0001\u0000\u0000\u0000\u01ab\u01aa\u0001\u0000\u0000\u0000\u01ab\u01ac"+
		"\u0001\u0000\u0000\u0000\u01ac\u01ad\u0001\u0000\u0000\u0000\u01ad\u01ae"+
		"\u0005\u007f\u0000\u0000\u01ae\u01ba\u0001\u0000\u0000\u0000\u01af\u01b5"+
		"\n\u0001\u0000\u0000\u01b0\u01b6\u0005\u0084\u0000\u0000\u01b1\u01b3\u0005"+
		"D\u0000\u0000\u01b2\u01b1\u0001\u0000\u0000\u0000\u01b2\u01b3\u0001\u0000"+
		"\u0000\u0000\u01b3\u01b4\u0001\u0000\u0000\u0000\u01b4\u01b6\u0003\u0156"+
		"\u00ab\u0000\u01b5\u01b0\u0001\u0000\u0000\u0000\u01b5\u01b2\u0001\u0000"+
		"\u0000\u0000\u01b6\u01b7\u0001\u0000\u0000\u0000\u01b7\u01b9\u0005\u007f"+
		"\u0000\u0000\u01b8\u01af\u0001\u0000\u0000\u0000\u01b9\u01bc\u0001\u0000"+
		"\u0000\u0000\u01ba\u01b8\u0001\u0000\u0000\u0000\u01ba\u01bb\u0001\u0000"+
		"\u0000\u0000\u01bb\u000b\u0001\u0000\u0000\u0000\u01bc\u01ba\u0001\u0000"+
		"\u0000\u0000\u01bd\u01bf\u0003\u000e\u0007\u0000\u01be\u01c0\u0003\u001c"+
		"\u000e\u0000\u01bf\u01be\u0001\u0000\u0000\u0000\u01bf\u01c0\u0001\u0000"+
		"\u0000\u0000\u01c0\u01c1\u0001\u0000\u0000\u0000\u01c1\u01c2\u0003d2\u0000"+
		"\u01c2\r\u0001\u0000\u0000\u0000\u01c3\u01c5\u0005W\u0000\u0000\u01c4"+
		"\u01c6\u0003\u0010\b\u0000\u01c5\u01c4\u0001\u0000\u0000\u0000\u01c5\u01c6"+
		"\u0001\u0000\u0000\u0000\u01c6\u01c7\u0001\u0000\u0000\u0000\u01c7\u01c8"+
		"\u0005X\u0000\u0000\u01c8\u000f\u0001\u0000\u0000\u0000\u01c9\u01d0\u0003"+
		"\u0014\n\u0000\u01ca\u01cd\u0003\u0012\t\u0000\u01cb\u01cc\u0005z\u0000"+
		"\u0000\u01cc\u01ce\u0003\u0014\n\u0000\u01cd\u01cb\u0001\u0000\u0000\u0000"+
		"\u01cd\u01ce\u0001\u0000\u0000\u0000\u01ce\u01d0\u0001\u0000\u0000\u0000"+
		"\u01cf\u01c9\u0001\u0000\u0000\u0000\u01cf\u01ca\u0001\u0000\u0000\u0000"+
		"\u01d0\u0011\u0001\u0000\u0000\u0000\u01d1\u01d2\u0007\u0000\u0000\u0000"+
		"\u01d2\u0013\u0001\u0000\u0000\u0000\u01d3\u01d8\u0003\u0016\u000b\u0000"+
		"\u01d4\u01d5\u0005z\u0000\u0000\u01d5\u01d7\u0003\u0016\u000b\u0000\u01d6"+
		"\u01d4\u0001\u0000\u0000\u0000\u01d7\u01da\u0001\u0000\u0000\u0000\u01d8"+
		"\u01d6\u0001\u0000\u0000\u0000\u01d8\u01d9\u0001\u0000\u0000\u0000\u01d9"+
		"\u01dc\u0001\u0000\u0000\u0000\u01da\u01d8\u0001\u0000\u0000\u0000\u01db"+
		"\u01dd\u0005\u0083\u0000\u0000\u01dc\u01db\u0001\u0000\u0000\u0000\u01dc"+
		"\u01dd\u0001\u0000\u0000\u0000\u01dd\u0015\u0001\u0000\u0000\u0000\u01de"+
		"\u01e1\u0003\u0018\f\u0000\u01df\u01e1\u0003\u001a\r\u0000\u01e0\u01de"+
		"\u0001\u0000\u0000\u0000\u01e0\u01df\u0001\u0000\u0000\u0000\u01e1\u0017"+
		"\u0001\u0000\u0000\u0000\u01e2\u01e4\u0005a\u0000\u0000\u01e3\u01e2\u0001"+
		"\u0000\u0000\u0000\u01e3\u01e4\u0001\u0000\u0000\u0000\u01e4\u01e5\u0001"+
		"\u0000\u0000\u0000\u01e5\u01e8\u0005\u0084\u0000\u0000\u01e6\u01e8\u0005"+
		"E\u0000\u0000\u01e7\u01e3\u0001\u0000\u0000\u0000\u01e7\u01e6\u0001\u0000"+
		"\u0000\u0000\u01e8\u0019\u0001\u0000\u0000\u0000\u01e9\u01eb\u0005a\u0000"+
		"\u0000\u01ea\u01e9\u0001\u0000\u0000\u0000\u01ea\u01eb\u0001\u0000\u0000"+
		"\u0000\u01eb\u01ec\u0001\u0000\u0000\u0000\u01ec\u01ed\u0005\u0084\u0000"+
		"\u0000\u01ed\u01ee\u0003\u010c\u0086\u0000\u01ee\u001b\u0001\u0000\u0000"+
		"\u0000\u01ef\u01f1\u0005U\u0000\u0000\u01f0\u01f2\u0003\u0102\u0081\u0000"+
		"\u01f1\u01f0\u0001\u0000\u0000\u0000\u01f1\u01f2\u0001\u0000\u0000\u0000"+
		"\u01f2\u01f3\u0001\u0000\u0000\u0000\u01f3\u01f5\u0005V\u0000\u0000\u01f4"+
		"\u01f6\u0005/\u0000\u0000\u01f5\u01f4\u0001\u0000\u0000\u0000\u01f5\u01f6"+
		"\u0001\u0000\u0000\u0000\u01f6\u01f8\u0001\u0000\u0000\u0000\u01f7\u01f9"+
		"\u0003\u0172\u00b9\u0000\u01f8\u01f7\u0001\u0000\u0000\u0000\u01f8\u01f9"+
		"\u0001\u0000\u0000\u0000\u01f9\u01fb\u0001\u0000\u0000\u0000\u01fa\u01fc"+
		"\u0003\u00ccf\u0000\u01fb\u01fa\u0001\u0000\u0000\u0000\u01fb\u01fc\u0001"+
		"\u0000\u0000\u0000\u01fc\u01fe\u0001\u0000\u0000\u0000\u01fd\u01ff\u0003"+
		"\u00eau\u0000\u01fe\u01fd\u0001\u0000\u0000\u0000\u01fe\u01ff\u0001\u0000"+
		"\u0000\u0000\u01ff\u001d\u0001\u0000\u0000\u0000\u0200\u0201\u0006\u000f"+
		"\uffff\uffff\u0000\u0201\u021f\u0003\u0002\u0001\u0000\u0202\u0205\u0003"+
		"\u009eO\u0000\u0203\u0205\u0003\u0160\u00b0\u0000\u0204\u0202\u0001\u0000"+
		"\u0000\u0000\u0204\u0203\u0001\u0000\u0000\u0000\u0205\u020c\u0001\u0000"+
		"\u0000\u0000\u0206\u0208\u0005U\u0000\u0000\u0207\u0209\u0003\"\u0011"+
		"\u0000\u0208\u0207\u0001\u0000\u0000\u0000\u0208\u0209\u0001\u0000\u0000"+
		"\u0000\u0209\u020a\u0001\u0000\u0000\u0000\u020a\u020d\u0005V\u0000\u0000"+
		"\u020b\u020d\u0003\u0114\u008a\u0000\u020c\u0206\u0001\u0000\u0000\u0000"+
		"\u020c\u020b\u0001\u0000\u0000\u0000\u020d\u021f\u0001\u0000\u0000\u0000"+
		"\u020e\u020f\u0007\u0001\u0000\u0000\u020f\u0210\u0005f\u0000\u0000\u0210"+
		"\u0211\u0003\u00f6{\u0000\u0211\u0212\u0005g\u0000\u0000\u0212\u0213\u0005"+
		"U\u0000\u0000\u0213\u0214\u0003Z-\u0000\u0214\u0215\u0005V\u0000\u0000"+
		"\u0215\u021f\u0001\u0000\u0000\u0000\u0216\u0217\u0003 \u0010\u0000\u0217"+
		"\u021a\u0005U\u0000\u0000\u0218\u021b\u0003Z-\u0000\u0219\u021b\u0003"+
		"\u00f6{\u0000\u021a\u0218\u0001\u0000\u0000\u0000\u021a\u0219\u0001\u0000"+
		"\u0000\u0000\u021b\u021c\u0001\u0000\u0000\u0000\u021c\u021d\u0005V\u0000"+
		"\u0000\u021d\u021f\u0001\u0000\u0000\u0000\u021e\u0200\u0001\u0000\u0000"+
		"\u0000\u021e\u0204\u0001\u0000\u0000\u0000\u021e\u020e\u0001\u0000\u0000"+
		"\u0000\u021e\u0216\u0001\u0000\u0000\u0000\u021f\u023b\u0001\u0000\u0000"+
		"\u0000\u0220\u0221\n\u0007\u0000\u0000\u0221\u0224\u0005W\u0000\u0000"+
		"\u0222\u0225\u0003Z-\u0000\u0223\u0225\u0003\u0114\u008a\u0000\u0224\u0222"+
		"\u0001\u0000\u0000\u0000\u0224\u0223\u0001\u0000\u0000\u0000\u0225\u0226"+
		"\u0001\u0000\u0000\u0000\u0226\u0227\u0005X\u0000\u0000\u0227\u023a\u0001"+
		"\u0000\u0000\u0000\u0228\u0229\n\u0006\u0000\u0000\u0229\u022b\u0005U"+
		"\u0000\u0000\u022a\u022c\u0003\"\u0011\u0000\u022b\u022a\u0001\u0000\u0000"+
		"\u0000\u022b\u022c\u0001\u0000\u0000\u0000\u022c\u022d\u0001\u0000\u0000"+
		"\u0000\u022d\u023a\u0005V\u0000\u0000\u022e\u022f\n\u0004\u0000\u0000"+
		"\u022f\u0235\u0007\u0002\u0000\u0000\u0230\u0232\u0005D\u0000\u0000\u0231"+
		"\u0230\u0001\u0000\u0000\u0000\u0231\u0232\u0001\u0000\u0000\u0000\u0232"+
		"\u0233\u0001\u0000\u0000\u0000\u0233\u0236\u0003\u0004\u0002\u0000\u0234"+
		"\u0236\u0003$\u0012\u0000\u0235\u0231\u0001\u0000\u0000\u0000\u0235\u0234"+
		"\u0001\u0000\u0000\u0000\u0236\u023a\u0001\u0000\u0000\u0000\u0237\u0238"+
		"\n\u0003\u0000\u0000\u0238\u023a\u0007\u0003\u0000\u0000\u0239\u0220\u0001"+
		"\u0000\u0000\u0000\u0239\u0228\u0001\u0000\u0000\u0000\u0239\u022e\u0001"+
		"\u0000\u0000\u0000\u0239\u0237\u0001\u0000\u0000\u0000\u023a\u023d\u0001"+
		"\u0000\u0000\u0000\u023b\u0239\u0001\u0000\u0000\u0000\u023b\u023c\u0001"+
		"\u0000\u0000\u0000\u023c\u001f\u0001\u0000\u0000\u0000\u023d\u023b\u0001"+
		"\u0000\u0000\u0000\u023e\u023f\u0005K\u0000\u0000\u023f!\u0001\u0000\u0000"+
		"\u0000\u0240\u0241\u0003\u0112\u0089\u0000\u0241#\u0001\u0000\u0000\u0000"+
		"\u0242\u0244\u0003\n\u0005\u0000\u0243\u0242\u0001\u0000\u0000\u0000\u0243"+
		"\u0244\u0001\u0000\u0000\u0000\u0244\u0248\u0001\u0000\u0000\u0000\u0245"+
		"\u0246\u0003\u00a0P\u0000\u0246\u0247\u0005\u007f\u0000\u0000\u0247\u0249"+
		"\u0001\u0000\u0000\u0000\u0248\u0245\u0001\u0000\u0000\u0000\u0248\u0249"+
		"\u0001\u0000\u0000\u0000\u0249\u024a\u0001\u0000\u0000\u0000\u024a\u024b"+
		"\u0005c\u0000\u0000\u024b\u0256\u0003\u00a0P\u0000\u024c\u024d\u0003\n"+
		"\u0005\u0000\u024d\u024e\u0005D\u0000\u0000\u024e\u024f\u0003\u0156\u00ab"+
		"\u0000\u024f\u0250\u0005\u007f\u0000\u0000\u0250\u0251\u0005c\u0000\u0000"+
		"\u0251\u0252\u0003\u00a0P\u0000\u0252\u0256\u0001\u0000\u0000\u0000\u0253"+
		"\u0254\u0005c\u0000\u0000\u0254\u0256\u0003\u00a2Q\u0000\u0255\u0243\u0001"+
		"\u0000\u0000\u0000\u0255\u024c\u0001\u0000\u0000\u0000\u0255\u0253\u0001"+
		"\u0000\u0000\u0000\u0256%\u0001\u0000\u0000\u0000\u0257\u0273\u0003\u001e"+
		"\u000f\u0000\u0258\u025d\u0005x\u0000\u0000\u0259\u025d\u0005y\u0000\u0000"+
		"\u025a\u025d\u0003(\u0014\u0000\u025b\u025d\u0005>\u0000\u0000\u025c\u0258"+
		"\u0001\u0000\u0000\u0000\u025c\u0259\u0001\u0000\u0000\u0000\u025c\u025a"+
		"\u0001\u0000\u0000\u0000\u025c\u025b\u0001\u0000\u0000\u0000\u025d\u025e"+
		"\u0001\u0000\u0000\u0000\u025e\u0273\u0003&\u0013\u0000\u025f\u0268\u0005"+
		">\u0000\u0000\u0260\u0261\u0005U\u0000\u0000\u0261\u0262\u0003\u00f6{"+
		"\u0000\u0262\u0263\u0005V\u0000\u0000\u0263\u0269\u0001\u0000\u0000\u0000"+
		"\u0264\u0265\u0005\u0083\u0000\u0000\u0265\u0266\u0005U\u0000\u0000\u0266"+
		"\u0267\u0005\u0084\u0000\u0000\u0267\u0269\u0005V\u0000\u0000\u0268\u0260"+
		"\u0001\u0000\u0000\u0000\u0268\u0264\u0001\u0000\u0000\u0000\u0269\u0273"+
		"\u0001\u0000\u0000\u0000\u026a\u026b\u0005\u000b\u0000\u0000\u026b\u026c"+
		"\u0005U\u0000\u0000\u026c\u026d\u0003\u00f6{\u0000\u026d\u026e\u0005V"+
		"\u0000\u0000\u026e\u0273\u0001\u0000\u0000\u0000\u026f\u0273\u00038\u001c"+
		"\u0000\u0270\u0273\u0003*\u0015\u0000\u0271\u0273\u00036\u001b\u0000\u0272"+
		"\u0257\u0001\u0000\u0000\u0000\u0272\u025c\u0001\u0000\u0000\u0000\u0272"+
		"\u025f\u0001\u0000\u0000\u0000\u0272\u026a\u0001\u0000\u0000\u0000\u0272"+
		"\u026f\u0001\u0000\u0000\u0000\u0272\u0270\u0001\u0000\u0000\u0000\u0272"+
		"\u0271\u0001\u0000\u0000\u0000\u0273\'\u0001\u0000\u0000\u0000\u0274\u0275"+
		"\u0007\u0004\u0000\u0000\u0275)\u0001\u0000\u0000\u0000\u0276\u0278\u0005"+
		"\u007f\u0000\u0000\u0277\u0276\u0001\u0000\u0000\u0000\u0277\u0278\u0001"+
		"\u0000\u0000\u0000\u0278\u0279\u0001\u0000\u0000\u0000\u0279\u027b\u0005"+
		"1\u0000\u0000\u027a\u027c\u0003,\u0016\u0000\u027b\u027a\u0001\u0000\u0000"+
		"\u0000\u027b\u027c\u0001\u0000\u0000\u0000\u027c\u0282\u0001\u0000\u0000"+
		"\u0000\u027d\u0283\u0003.\u0017\u0000\u027e\u027f\u0005U\u0000\u0000\u027f"+
		"\u0280\u0003\u00f6{\u0000\u0280\u0281\u0005V\u0000\u0000\u0281\u0283\u0001"+
		"\u0000\u0000\u0000\u0282\u027d\u0001\u0000\u0000\u0000\u0282\u027e\u0001"+
		"\u0000\u0000\u0000\u0283\u0285\u0001\u0000\u0000\u0000\u0284\u0286\u0003"+
		"4\u001a\u0000\u0285\u0284\u0001\u0000\u0000\u0000\u0285\u0286\u0001\u0000"+
		"\u0000\u0000\u0286+\u0001\u0000\u0000\u0000\u0287\u0288\u0005U\u0000\u0000"+
		"\u0288\u0289\u0003\"\u0011\u0000\u0289\u028a\u0005V\u0000\u0000\u028a"+
		"-\u0001\u0000\u0000\u0000\u028b\u028d\u0003\u0096K\u0000\u028c\u028e\u0003"+
		"0\u0018\u0000\u028d\u028c\u0001\u0000\u0000\u0000\u028d\u028e\u0001\u0000"+
		"\u0000\u0000\u028e/\u0001\u0000\u0000\u0000\u028f\u0291\u0003\u00ecv\u0000"+
		"\u0290\u0292\u00030\u0018\u0000\u0291\u0290\u0001\u0000\u0000\u0000\u0291"+
		"\u0292\u0001\u0000\u0000\u0000\u0292\u0295\u0001\u0000\u0000\u0000\u0293"+
		"\u0295\u00032\u0019\u0000\u0294\u028f\u0001\u0000\u0000\u0000\u0294\u0293"+
		"\u0001\u0000\u0000\u0000\u02951\u0001\u0000\u0000\u0000\u0296\u0297\u0006"+
		"\u0019\uffff\uffff\u0000\u0297\u0298\u0005W\u0000\u0000\u0298\u0299\u0003"+
		"Z-\u0000\u0299\u029b\u0005X\u0000\u0000\u029a\u029c\u0003\u00ccf\u0000"+
		"\u029b\u029a\u0001\u0000\u0000\u0000\u029b\u029c\u0001\u0000\u0000\u0000"+
		"\u029c\u02a6\u0001\u0000\u0000\u0000\u029d\u029e\n\u0001\u0000\u0000\u029e"+
		"\u029f\u0005W\u0000\u0000\u029f\u02a0\u0003\\.\u0000\u02a0\u02a2\u0005"+
		"X\u0000\u0000\u02a1\u02a3\u0003\u00ccf\u0000\u02a2\u02a1\u0001\u0000\u0000"+
		"\u0000\u02a2\u02a3\u0001\u0000\u0000\u0000\u02a3\u02a5\u0001\u0000\u0000"+
		"\u0000\u02a4\u029d\u0001\u0000\u0000\u0000\u02a5\u02a8\u0001\u0000\u0000"+
		"\u0000\u02a6\u02a4\u0001\u0000\u0000\u0000\u02a6\u02a7\u0001\u0000\u0000"+
		"\u0000\u02a73\u0001\u0000\u0000\u0000\u02a8\u02a6\u0001\u0000\u0000\u0000"+
		"\u02a9\u02ab\u0005U\u0000\u0000\u02aa\u02ac\u0003\"\u0011\u0000\u02ab"+
		"\u02aa\u0001\u0000\u0000\u0000\u02ab\u02ac\u0001\u0000\u0000\u0000\u02ac"+
		"\u02ad\u0001\u0000\u0000\u0000\u02ad\u02b0\u0005V\u0000\u0000\u02ae\u02b0"+
		"\u0003\u0114\u008a\u0000\u02af\u02a9\u0001\u0000\u0000\u0000\u02af\u02ae"+
		"\u0001\u0000\u0000\u0000\u02b05\u0001\u0000\u0000\u0000\u02b1\u02b3\u0005"+
		"\u007f\u0000\u0000\u02b2\u02b1\u0001\u0000\u0000\u0000\u02b2\u02b3\u0001"+
		"\u0000\u0000\u0000\u02b3\u02b4\u0001\u0000\u0000\u0000\u02b4\u02b7\u0005"+
		"\u001c\u0000\u0000\u02b5\u02b6\u0005W\u0000\u0000\u02b6\u02b8\u0005X\u0000"+
		"\u0000\u02b7\u02b5\u0001\u0000\u0000\u0000\u02b7\u02b8\u0001\u0000\u0000"+
		"\u0000\u02b8\u02b9\u0001\u0000\u0000\u0000\u02b9\u02ba\u0003:\u001d\u0000"+
		"\u02ba7\u0001\u0000\u0000\u0000\u02bb\u02bc\u00052\u0000\u0000\u02bc\u02bd"+
		"\u0005U\u0000\u0000\u02bd\u02be\u0003Z-\u0000\u02be\u02bf\u0005V\u0000"+
		"\u0000\u02bf9\u0001\u0000\u0000\u0000\u02c0\u02c7\u0003&\u0013\u0000\u02c1"+
		"\u02c2\u0005U\u0000\u0000\u02c2\u02c3\u0003\u00f6{\u0000\u02c3\u02c4\u0005"+
		"V\u0000\u0000\u02c4\u02c5\u0003:\u001d\u0000\u02c5\u02c7\u0001\u0000\u0000"+
		"\u0000\u02c6\u02c0\u0001\u0000\u0000\u0000\u02c6\u02c1\u0001\u0000\u0000"+
		"\u0000\u02c7;\u0001\u0000\u0000\u0000\u02c8\u02cd\u0003:\u001d\u0000\u02c9"+
		"\u02ca\u0007\u0005\u0000\u0000\u02ca\u02cc\u0003:\u001d\u0000\u02cb\u02c9"+
		"\u0001\u0000\u0000\u0000\u02cc\u02cf\u0001\u0000\u0000\u0000\u02cd\u02cb"+
		"\u0001\u0000\u0000\u0000\u02cd\u02ce\u0001\u0000\u0000\u0000\u02ce=\u0001"+
		"\u0000\u0000\u0000\u02cf\u02cd\u0001\u0000\u0000\u0000\u02d0\u02d5\u0003"+
		"<\u001e\u0000\u02d1\u02d2\u0007\u0006\u0000\u0000\u02d2\u02d4\u0003<\u001e"+
		"\u0000\u02d3\u02d1\u0001\u0000\u0000\u0000\u02d4\u02d7\u0001\u0000\u0000"+
		"\u0000\u02d5\u02d3\u0001\u0000\u0000\u0000\u02d5\u02d6\u0001\u0000\u0000"+
		"\u0000\u02d6?\u0001\u0000\u0000\u0000\u02d7\u02d5\u0001\u0000\u0000\u0000"+
		"\u02d8\u02dd\u0003>\u001f\u0000\u02d9\u02da\u0007\u0007\u0000\u0000\u02da"+
		"\u02dc\u0003>\u001f\u0000\u02db\u02d9\u0001\u0000\u0000\u0000\u02dc\u02df"+
		"\u0001\u0000\u0000\u0000\u02dd\u02db\u0001\u0000\u0000\u0000\u02dd\u02de"+
		"\u0001\u0000\u0000\u0000\u02deA\u0001\u0000\u0000\u0000\u02df\u02dd\u0001"+
		"\u0000\u0000\u0000\u02e0\u02e6\u0003@ \u0000\u02e1\u02e2\u0003D\"\u0000"+
		"\u02e2\u02e3\u0003@ \u0000\u02e3\u02e5\u0001\u0000\u0000\u0000\u02e4\u02e1"+
		"\u0001\u0000\u0000\u0000\u02e5\u02e8\u0001\u0000\u0000\u0000\u02e6\u02e4"+
		"\u0001\u0000\u0000\u0000\u02e6\u02e7\u0001\u0000\u0000\u0000\u02e7C\u0001"+
		"\u0000\u0000\u0000\u02e8\u02e6\u0001\u0000\u0000\u0000\u02e9\u02ea\u0005"+
		"g\u0000\u0000\u02ea\u02ee\u0005g\u0000\u0000\u02eb\u02ec\u0005f\u0000"+
		"\u0000\u02ec\u02ee\u0005f\u0000\u0000\u02ed\u02e9\u0001\u0000\u0000\u0000"+
		"\u02ed\u02eb\u0001\u0000\u0000\u0000\u02eeE\u0001\u0000\u0000\u0000\u02ef"+
		"\u02f4\u0003B!\u0000\u02f0\u02f1\u0007\b\u0000\u0000\u02f1\u02f3\u0003"+
		"B!\u0000\u02f2\u02f0\u0001\u0000\u0000\u0000\u02f3\u02f6\u0001\u0000\u0000"+
		"\u0000\u02f4\u02f2\u0001\u0000\u0000\u0000\u02f4\u02f5\u0001\u0000\u0000"+
		"\u0000\u02f5G\u0001\u0000\u0000\u0000\u02f6\u02f4\u0001\u0000\u0000\u0000"+
		"\u02f7\u02fc\u0003F#\u0000\u02f8\u02f9\u0007\t\u0000\u0000\u02f9\u02fb"+
		"\u0003F#\u0000\u02fa\u02f8\u0001\u0000\u0000\u0000\u02fb\u02fe\u0001\u0000"+
		"\u0000\u0000\u02fc\u02fa\u0001\u0000\u0000\u0000\u02fc\u02fd\u0001\u0000"+
		"\u0000\u0000\u02fdI\u0001\u0000\u0000\u0000\u02fe\u02fc\u0001\u0000\u0000"+
		"\u0000\u02ff\u0304\u0003H$\u0000\u0300\u0301\u0005a\u0000\u0000\u0301"+
		"\u0303\u0003H$\u0000\u0302\u0300\u0001\u0000\u0000\u0000\u0303\u0306\u0001"+
		"\u0000\u0000\u0000\u0304\u0302\u0001\u0000\u0000\u0000\u0304\u0305\u0001"+
		"\u0000\u0000\u0000\u0305K\u0001\u0000\u0000\u0000\u0306\u0304\u0001\u0000"+
		"\u0000\u0000\u0307\u030c\u0003J%\u0000\u0308\u0309\u0005`\u0000\u0000"+
		"\u0309\u030b\u0003J%\u0000\u030a\u0308\u0001\u0000\u0000\u0000\u030b\u030e"+
		"\u0001\u0000\u0000\u0000\u030c\u030a\u0001\u0000\u0000\u0000\u030c\u030d"+
		"\u0001\u0000\u0000\u0000\u030dM\u0001\u0000\u0000\u0000\u030e\u030c\u0001"+
		"\u0000\u0000\u0000\u030f\u0314\u0003L&\u0000\u0310\u0311\u0005b\u0000"+
		"\u0000\u0311\u0313\u0003L&\u0000\u0312\u0310\u0001\u0000\u0000\u0000\u0313"+
		"\u0316\u0001\u0000\u0000\u0000\u0314\u0312\u0001\u0000\u0000\u0000\u0314"+
		"\u0315\u0001\u0000\u0000\u0000\u0315O\u0001\u0000\u0000\u0000\u0316\u0314"+
		"\u0001\u0000\u0000\u0000\u0317\u031c\u0003N\'\u0000\u0318\u0319\u0005"+
		"v\u0000\u0000\u0319\u031b\u0003N\'\u0000\u031a\u0318\u0001\u0000\u0000"+
		"\u0000\u031b\u031e\u0001\u0000\u0000\u0000\u031c\u031a\u0001\u0000\u0000"+
		"\u0000\u031c\u031d\u0001\u0000\u0000\u0000\u031dQ\u0001\u0000\u0000\u0000"+
		"\u031e\u031c\u0001\u0000\u0000\u0000\u031f\u0324\u0003P(\u0000\u0320\u0321"+
		"\u0005w\u0000\u0000\u0321\u0323\u0003P(\u0000\u0322\u0320\u0001\u0000"+
		"\u0000\u0000\u0323\u0326\u0001\u0000\u0000\u0000\u0324\u0322\u0001\u0000"+
		"\u0000\u0000\u0324\u0325\u0001\u0000\u0000\u0000\u0325S\u0001\u0000\u0000"+
		"\u0000\u0326\u0324\u0001\u0000\u0000\u0000\u0327\u032d\u0003R)\u0000\u0328"+
		"\u0329\u0005}\u0000\u0000\u0329\u032a\u0003Z-\u0000\u032a\u032b\u0005"+
		"~\u0000\u0000\u032b\u032c\u0003V+\u0000\u032c\u032e\u0001\u0000\u0000"+
		"\u0000\u032d\u0328\u0001\u0000\u0000\u0000\u032d\u032e\u0001\u0000\u0000"+
		"\u0000\u032eU\u0001\u0000\u0000\u0000\u032f\u0336\u0003T*\u0000\u0330"+
		"\u0331\u0003R)\u0000\u0331\u0332\u0003X,\u0000\u0332\u0333\u0003\u0110"+
		"\u0088\u0000\u0333\u0336\u0001\u0000\u0000\u0000\u0334\u0336\u0003\u0170"+
		"\u00b8\u0000\u0335\u032f\u0001\u0000\u0000\u0000\u0335\u0330\u0001\u0000"+
		"\u0000\u0000\u0335\u0334\u0001\u0000\u0000\u0000\u0336W\u0001\u0000\u0000"+
		"\u0000\u0337\u0338\u0007\n\u0000\u0000\u0338Y\u0001\u0000\u0000\u0000"+
		"\u0339\u033e\u0003V+\u0000\u033a\u033b\u0005z\u0000\u0000\u033b\u033d"+
		"\u0003V+\u0000\u033c\u033a\u0001\u0000\u0000\u0000\u033d\u0340\u0001\u0000"+
		"\u0000\u0000\u033e\u033c\u0001\u0000\u0000\u0000\u033e\u033f\u0001\u0000"+
		"\u0000\u0000\u033f[\u0001\u0000\u0000\u0000\u0340\u033e\u0001\u0000\u0000"+
		"\u0000\u0341\u0342\u0003T*\u0000\u0342]\u0001\u0000\u0000\u0000\u0343"+
		"\u0351\u0003`0\u0000\u0344\u0351\u0003v;\u0000\u0345\u0347\u0003\u00cc"+
		"f\u0000\u0346\u0345\u0001\u0000\u0000\u0000\u0346\u0347\u0001\u0000\u0000"+
		"\u0000\u0347\u034e\u0001\u0000\u0000\u0000\u0348\u034f\u0003b1\u0000\u0349"+
		"\u034f\u0003d2\u0000\u034a\u034f\u0003h4\u0000\u034b\u034f\u0003l6\u0000"+
		"\u034c\u034f\u0003t:\u0000\u034d\u034f\u0003\u0166\u00b3\u0000\u034e\u0348"+
		"\u0001\u0000\u0000\u0000\u034e\u0349\u0001\u0000\u0000\u0000\u034e\u034a"+
		"\u0001\u0000\u0000\u0000\u034e\u034b\u0001\u0000\u0000\u0000\u034e\u034c"+
		"\u0001\u0000\u0000\u0000\u034e\u034d\u0001\u0000\u0000\u0000\u034f\u0351"+
		"\u0001\u0000\u0000\u0000\u0350\u0343\u0001\u0000\u0000\u0000\u0350\u0344"+
		"\u0001\u0000\u0000\u0000\u0350\u0346\u0001\u0000\u0000\u0000\u0351_\u0001"+
		"\u0000\u0000\u0000\u0352\u0354\u0003\u00ccf\u0000\u0353\u0352\u0001\u0000"+
		"\u0000\u0000\u0353\u0354\u0001\u0000\u0000\u0000\u0354\u0359\u0001\u0000"+
		"\u0000\u0000\u0355\u035a\u0005\u0084\u0000\u0000\u0356\u0357\u0005\u0010"+
		"\u0000\u0000\u0357\u035a\u0003\\.\u0000\u0358\u035a\u0005\u001b\u0000"+
		"\u0000\u0359\u0355\u0001\u0000\u0000\u0000\u0359\u0356\u0001\u0000\u0000"+
		"\u0000\u0359\u0358\u0001\u0000\u0000\u0000\u035a\u035b\u0001\u0000\u0000"+
		"\u0000\u035b\u035c\u0005~\u0000\u0000\u035c\u035d\u0003^/\u0000\u035d"+
		"a\u0001\u0000\u0000\u0000\u035e\u0360\u0003Z-\u0000\u035f\u035e\u0001"+
		"\u0000\u0000\u0000\u035f\u0360\u0001\u0000\u0000\u0000\u0360\u0361\u0001"+
		"\u0000\u0000\u0000\u0361\u0362\u0005\u0080\u0000\u0000\u0362c\u0001\u0000"+
		"\u0000\u0000\u0363\u0365\u0005Y\u0000\u0000\u0364\u0366\u0003f3\u0000"+
		"\u0365\u0364\u0001\u0000\u0000\u0000\u0365\u0366\u0001\u0000\u0000\u0000"+
		"\u0366\u0367\u0001\u0000\u0000\u0000\u0367\u0368\u0005Z\u0000\u0000\u0368"+
		"e\u0001\u0000\u0000\u0000\u0369\u036b\u0003^/\u0000\u036a\u0369\u0001"+
		"\u0000\u0000\u0000\u036b\u036c\u0001\u0000\u0000\u0000\u036c\u036a\u0001"+
		"\u0000\u0000\u0000\u036c\u036d\u0001\u0000\u0000\u0000\u036dg\u0001\u0000"+
		"\u0000\u0000\u036e\u036f\u0005+\u0000\u0000\u036f\u0370\u0005U\u0000\u0000"+
		"\u0370\u0371\u0003j5\u0000\u0371\u0372\u0005V\u0000\u0000\u0372\u0375"+
		"\u0003^/\u0000\u0373\u0374\u0005 \u0000\u0000\u0374\u0376\u0003^/\u0000"+
		"\u0375\u0373\u0001\u0000\u0000\u0000\u0375\u0376\u0001\u0000\u0000\u0000"+
		"\u0376\u037e\u0001\u0000\u0000\u0000\u0377\u0378\u0005C\u0000\u0000\u0378"+
		"\u0379\u0005U\u0000\u0000\u0379\u037a\u0003j5\u0000\u037a\u037b\u0005"+
		"V\u0000\u0000\u037b\u037c\u0003^/\u0000\u037c\u037e\u0001\u0000\u0000"+
		"\u0000\u037d\u036e\u0001\u0000\u0000\u0000\u037d\u0377\u0001\u0000\u0000"+
		"\u0000\u037ei\u0001\u0000\u0000\u0000\u037f\u038b\u0003Z-\u0000\u0380"+
		"\u0382\u0003\u00ccf\u0000\u0381\u0380\u0001\u0000\u0000\u0000\u0381\u0382"+
		"\u0001\u0000\u0000\u0000\u0382\u0383\u0001\u0000\u0000\u0000\u0383\u0384"+
		"\u0003\u008aE\u0000\u0384\u0388\u0003\u00e2q\u0000\u0385\u0386\u0005e"+
		"\u0000\u0000\u0386\u0389\u0003\u0110\u0088\u0000\u0387\u0389\u0003\u0114"+
		"\u008a\u0000\u0388\u0385\u0001\u0000\u0000\u0000\u0388\u0387\u0001\u0000"+
		"\u0000\u0000\u0389\u038b\u0001\u0000\u0000\u0000\u038a\u037f\u0001\u0000"+
		"\u0000\u0000\u038a\u0381\u0001\u0000\u0000\u0000\u038bk\u0001\u0000\u0000"+
		"\u0000\u038c\u038d\u0005T\u0000\u0000\u038d\u038e\u0005U\u0000\u0000\u038e"+
		"\u038f\u0003j5\u0000\u038f\u0390\u0005V\u0000\u0000\u0390\u0391\u0003"+
		"^/\u0000\u0391\u03ae\u0001\u0000\u0000\u0000\u0392\u0393\u0005\u001d\u0000"+
		"\u0000\u0393\u0394\u0003^/\u0000\u0394\u0395\u0005T\u0000\u0000\u0395"+
		"\u0396\u0005U\u0000\u0000\u0396\u0397\u0003Z-\u0000\u0397\u0398\u0005"+
		"V\u0000\u0000\u0398\u0399\u0005\u0080\u0000\u0000\u0399\u03ae\u0001\u0000"+
		"\u0000\u0000\u039a\u039b\u0005(\u0000\u0000\u039b\u03a8\u0005U\u0000\u0000"+
		"\u039c\u039e\u0003n7\u0000\u039d\u039f\u0003j5\u0000\u039e\u039d\u0001"+
		"\u0000\u0000\u0000\u039e\u039f\u0001\u0000\u0000\u0000\u039f\u03a0\u0001"+
		"\u0000\u0000\u0000\u03a0\u03a2\u0005\u0080\u0000\u0000\u03a1\u03a3\u0003"+
		"Z-\u0000\u03a2\u03a1\u0001\u0000\u0000\u0000\u03a2\u03a3\u0001\u0000\u0000"+
		"\u0000\u03a3\u03a9\u0001\u0000\u0000\u0000\u03a4\u03a5\u0003p8\u0000\u03a5"+
		"\u03a6\u0005~\u0000\u0000\u03a6\u03a7\u0003r9\u0000\u03a7\u03a9\u0001"+
		"\u0000\u0000\u0000\u03a8\u039c\u0001\u0000\u0000\u0000\u03a8\u03a4\u0001"+
		"\u0000\u0000\u0000\u03a9\u03aa\u0001\u0000\u0000\u0000\u03aa\u03ab\u0005"+
		"V\u0000\u0000\u03ab\u03ac\u0003^/\u0000\u03ac\u03ae\u0001\u0000\u0000"+
		"\u0000\u03ad\u038c\u0001\u0000\u0000\u0000\u03ad\u0392\u0001\u0000\u0000"+
		"\u0000\u03ad\u039a\u0001\u0000\u0000\u0000\u03aem\u0001\u0000\u0000\u0000"+
		"\u03af\u03b2\u0003b1\u0000\u03b0\u03b2\u0003\u0080@\u0000\u03b1\u03af"+
		"\u0001\u0000\u0000\u0000\u03b1\u03b0\u0001\u0000\u0000\u0000\u03b2o\u0001"+
		"\u0000\u0000\u0000\u03b3\u03b5\u0003\u00ccf\u0000\u03b4\u03b3\u0001\u0000"+
		"\u0000\u0000\u03b4\u03b5\u0001\u0000\u0000\u0000\u03b5\u03b6\u0001\u0000"+
		"\u0000\u0000\u03b6\u03b7\u0003\u008aE\u0000\u03b7\u03b8\u0003\u00e2q\u0000"+
		"\u03b8q\u0001\u0000\u0000\u0000\u03b9\u03bc\u0003Z-\u0000\u03ba\u03bc"+
		"\u0003\u0114\u008a\u0000\u03bb\u03b9\u0001\u0000\u0000\u0000\u03bb\u03ba"+
		"\u0001\u0000\u0000\u0000\u03bcs\u0001\u0000\u0000\u0000\u03bd\u03c7\u0005"+
		"\u000f\u0000\u0000\u03be\u03c7\u0005\u0019\u0000\u0000\u03bf\u03c2\u0005"+
		";\u0000\u0000\u03c0\u03c3\u0003Z-\u0000\u03c1\u03c3\u0003\u0114\u008a"+
		"\u0000\u03c2\u03c0\u0001\u0000\u0000\u0000\u03c2\u03c1\u0001\u0000\u0000"+
		"\u0000\u03c2\u03c3\u0001\u0000\u0000\u0000\u03c3\u03c7\u0001\u0000\u0000"+
		"\u0000\u03c4\u03c5\u0005*\u0000\u0000\u03c5\u03c7\u0005\u0084\u0000\u0000"+
		"\u03c6\u03bd\u0001\u0000\u0000\u0000\u03c6\u03be\u0001\u0000\u0000\u0000"+
		"\u03c6\u03bf\u0001\u0000\u0000\u0000\u03c6\u03c4\u0001\u0000\u0000\u0000"+
		"\u03c7\u03c8\u0001\u0000\u0000\u0000\u03c8\u03c9\u0005\u0080\u0000\u0000"+
		"\u03c9u\u0001\u0000\u0000\u0000\u03ca\u03cb\u0003|>\u0000\u03cbw\u0001"+
		"\u0000\u0000\u0000\u03cc\u03ce\u0003z=\u0000\u03cd\u03cc\u0001\u0000\u0000"+
		"\u0000\u03ce\u03cf\u0001\u0000\u0000\u0000\u03cf\u03cd\u0001\u0000\u0000"+
		"\u0000\u03cf\u03d0\u0001\u0000\u0000\u0000\u03d0y\u0001\u0000\u0000\u0000"+
		"\u03d1\u03db\u0003|>\u0000\u03d2\u03db\u0003\u0108\u0084\u0000\u03d3\u03db"+
		"\u0003\u014e\u00a7\u0000\u03d4\u03db\u0003\u0162\u00b1\u0000\u03d5\u03db"+
		"\u0003\u0164\u00b2\u0000\u03d6\u03db\u0003\u00cae\u0000\u03d7\u03db\u0003"+
		"\u00bc^\u0000\u03d8\u03db\u0003\u0084B\u0000\u03d9\u03db\u0003\u0086C"+
		"\u0000\u03da\u03d1\u0001\u0000\u0000\u0000\u03da\u03d2\u0001\u0000\u0000"+
		"\u0000\u03da\u03d3\u0001\u0000\u0000\u0000\u03da\u03d4\u0001\u0000\u0000"+
		"\u0000\u03da\u03d5\u0001\u0000\u0000\u0000\u03da\u03d6\u0001\u0000\u0000"+
		"\u0000\u03da\u03d7\u0001\u0000\u0000\u0000\u03da\u03d8\u0001\u0000\u0000"+
		"\u0000\u03da\u03d9\u0001\u0000\u0000\u0000\u03db{\u0001\u0000\u0000\u0000"+
		"\u03dc\u03e5\u0003\u0080@\u0000\u03dd\u03e5\u0003\u00c8d\u0000\u03de\u03e5"+
		"\u0003\u00c0`\u0000\u03df\u03e5\u0003\u00c4b\u0000\u03e0\u03e5\u0003\u00c6"+
		"c\u0000\u03e1\u03e5\u0003\u0082A\u0000\u03e2\u03e5\u0003~?\u0000\u03e3"+
		"\u03e5\u0003\u00acV\u0000\u03e4\u03dc\u0001\u0000\u0000\u0000\u03e4\u03dd"+
		"\u0001\u0000\u0000\u0000\u03e4\u03de\u0001\u0000\u0000\u0000\u03e4\u03df"+
		"\u0001\u0000\u0000\u0000\u03e4\u03e0\u0001\u0000\u0000\u0000\u03e4\u03e1"+
		"\u0001\u0000\u0000\u0000\u03e4\u03e2\u0001\u0000\u0000\u0000\u03e4\u03e3"+
		"\u0001\u0000\u0000\u0000\u03e5}\u0001\u0000\u0000\u0000\u03e6\u03e7\u0005"+
		"O\u0000\u0000\u03e7\u03e9\u0005\u0084\u0000\u0000\u03e8\u03ea\u0003\u00cc"+
		"f\u0000\u03e9\u03e8\u0001\u0000\u0000\u0000\u03e9\u03ea\u0001\u0000\u0000"+
		"\u0000\u03ea\u03eb\u0001\u0000\u0000\u0000\u03eb\u03ec\u0005e\u0000\u0000"+
		"\u03ec\u03ed\u0003\u00f6{\u0000\u03ed\u03ee\u0005\u0080\u0000\u0000\u03ee"+
		"\u007f\u0001\u0000\u0000\u0000\u03ef\u03f1\u0003\u008aE\u0000\u03f0\u03ef"+
		"\u0001\u0000\u0000\u0000\u03f0\u03f1\u0001\u0000\u0000\u0000\u03f1\u03f3"+
		"\u0001\u0000\u0000\u0000\u03f2\u03f4\u0003\u00deo\u0000\u03f3\u03f2\u0001"+
		"\u0000\u0000\u0000\u03f3\u03f4\u0001\u0000\u0000\u0000\u03f4\u03f5\u0001"+
		"\u0000\u0000\u0000\u03f5\u03fe\u0005\u0080\u0000\u0000\u03f6\u03f8\u0003"+
		"\u00ccf\u0000\u03f7\u03f9\u0003\u008aE\u0000\u03f8\u03f7\u0001\u0000\u0000"+
		"\u0000\u03f8\u03f9\u0001\u0000\u0000\u0000\u03f9\u03fa\u0001\u0000\u0000"+
		"\u0000\u03fa\u03fb\u0003\u00deo\u0000\u03fb\u03fc\u0005\u0080\u0000\u0000"+
		"\u03fc\u03fe\u0001\u0000\u0000\u0000\u03fd\u03f0\u0001\u0000\u0000\u0000"+
		"\u03fd\u03f6\u0001\u0000\u0000\u0000\u03fe\u0081\u0001\u0000\u0000\u0000"+
		"\u03ff\u0400\u0005@\u0000\u0000\u0400\u0401\u0005U\u0000\u0000\u0401\u0402"+
		"\u0003\\.\u0000\u0402\u0403\u0005z\u0000\u0000\u0403\u0404\u0005\u0004"+
		"\u0000\u0000\u0404\u0405\u0005V\u0000\u0000\u0405\u0406\u0005\u0080\u0000"+
		"\u0000\u0406\u0083\u0001\u0000\u0000\u0000\u0407\u0408\u0005\u0080\u0000"+
		"\u0000\u0408\u0085\u0001\u0000\u0000\u0000\u0409\u040a\u0003\u00ccf\u0000"+
		"\u040a\u040b\u0005\u0080\u0000\u0000\u040b\u0087\u0001\u0000\u0000\u0000"+
		"\u040c\u0413\u0003\u008cF\u0000\u040d\u0413\u0003\u0092I\u0000\u040e\u0413"+
		"\u0003\u008eG\u0000\u040f\u0413\u0005)\u0000\u0000\u0410\u0413\u0005J"+
		"\u0000\u0000\u0411\u0413\u0005\u0017\u0000\u0000\u0412\u040c\u0001\u0000"+
		"\u0000\u0000\u0412\u040d\u0001\u0000\u0000\u0000\u0412\u040e\u0001\u0000"+
		"\u0000\u0000\u0412\u040f\u0001\u0000\u0000\u0000\u0412\u0410\u0001\u0000"+
		"\u0000\u0000\u0412\u0411\u0001\u0000\u0000\u0000\u0413\u0089\u0001\u0000"+
		"\u0000\u0000\u0414\u0416\u0003\u0088D\u0000\u0415\u0414\u0001\u0000\u0000"+
		"\u0000\u0416\u0417\u0001\u0000\u0000\u0000\u0417\u0418\u0001\u0000\u0000"+
		"\u0000\u0417\u0415\u0001\u0000\u0000\u0000\u0418\u041a\u0001\u0000\u0000"+
		"\u0000\u0419\u041b\u0003\u00ccf\u0000\u041a\u0419\u0001\u0000\u0000\u0000"+
		"\u041a\u041b\u0001\u0000\u0000\u0000\u041b\u008b\u0001\u0000\u0000\u0000"+
		"\u041c\u041d\u0007\u000b\u0000\u0000\u041d\u008d\u0001\u0000\u0000\u0000"+
		"\u041e\u041f\u0007\f\u0000\u0000\u041f\u008f\u0001\u0000\u0000\u0000\u0420"+
		"\u0421\u0005\u0084\u0000\u0000\u0421\u0091\u0001\u0000\u0000\u0000\u0422"+
		"\u0426\u0003\u0094J\u0000\u0423\u0426\u0003\u0118\u008c\u0000\u0424\u0426"+
		"\u0003\u00a8T\u0000\u0425\u0422\u0001\u0000\u0000\u0000\u0425\u0423\u0001"+
		"\u0000\u0000\u0000\u0425\u0424\u0001\u0000\u0000\u0000\u0426\u0093\u0001"+
		"\u0000\u0000\u0000\u0427\u042c\u0003\u009eO\u0000\u0428\u042c\u0003\u00a4"+
		"R\u0000\u0429\u042c\u0003\u0160\u00b0\u0000\u042a\u042c\u0003\u00f0x\u0000"+
		"\u042b\u0427\u0001\u0000\u0000\u0000\u042b\u0428\u0001\u0000\u0000\u0000"+
		"\u042b\u0429\u0001\u0000\u0000\u0000\u042b\u042a\u0001\u0000\u0000\u0000"+
		"\u042c\u0095\u0001\u0000\u0000\u0000\u042d\u042f\u0003\u0092I\u0000\u042e"+
		"\u042d\u0001\u0000\u0000\u0000\u042f\u0430\u0001\u0000\u0000\u0000\u0430"+
		"\u042e\u0001\u0000\u0000\u0000\u0430\u0431\u0001\u0000\u0000\u0000\u0431"+
		"\u0433\u0001\u0000\u0000\u0000\u0432\u0434\u0003\u00ccf\u0000\u0433\u0432"+
		"\u0001\u0000\u0000\u0000\u0433\u0434\u0001\u0000\u0000\u0000\u0434\u0097"+
		"\u0001\u0000\u0000\u0000\u0435\u0437\u0003\u0094J\u0000\u0436\u0435\u0001"+
		"\u0000\u0000\u0000\u0437\u0438\u0001\u0000\u0000\u0000\u0438\u0436\u0001"+
		"\u0000\u0000\u0000\u0438\u0439\u0001\u0000\u0000\u0000\u0439\u043b\u0001"+
		"\u0000\u0000\u0000\u043a\u043c\u0003\u00ccf\u0000\u043b\u043a\u0001\u0000"+
		"\u0000\u0000\u043b\u043c\u0001\u0000\u0000\u0000\u043c\u0099\u0001\u0000"+
		"\u0000\u0000\u043d\u043e\u0007\r\u0000\u0000\u043e\u009b\u0001\u0000\u0000"+
		"\u0000\u043f\u0440\u0007\u000e\u0000\u0000\u0440\u009d\u0001\u0000\u0000"+
		"\u0000\u0441\u0443\u0003\n\u0005\u0000\u0442\u0441\u0001\u0000\u0000\u0000"+
		"\u0442\u0443\u0001\u0000\u0000\u0000\u0443\u0444\u0001\u0000\u0000\u0000"+
		"\u0444\u0476\u0003\u00a0P\u0000\u0445\u0446\u0003\n\u0005\u0000\u0446"+
		"\u0447\u0005D\u0000\u0000\u0447\u0448\u0003\u0156\u00ab\u0000\u0448\u0476"+
		"\u0001\u0000\u0000\u0000\u0449\u0476\u0003\u009cN\u0000\u044a\u044c\u0003"+
		"\u009cN\u0000\u044b\u044a\u0001\u0000\u0000\u0000\u044b\u044c\u0001\u0000"+
		"\u0000\u0000\u044c\u044e\u0001\u0000\u0000\u0000\u044d\u044f\u0003\u009a"+
		"M\u0000\u044e\u044d\u0001\u0000\u0000\u0000\u044f\u0450\u0001\u0000\u0000"+
		"\u0000\u0450\u044e\u0001\u0000\u0000\u0000\u0450\u0451\u0001\u0000\u0000"+
		"\u0000\u0451\u0476\u0001\u0000\u0000\u0000\u0452\u0454\u0003\u009cN\u0000"+
		"\u0453\u0452\u0001\u0000\u0000\u0000\u0453\u0454\u0001\u0000\u0000\u0000"+
		"\u0454\u0455\u0001\u0000\u0000\u0000\u0455\u0476\u0005\u0012\u0000\u0000"+
		"\u0456\u0458\u0003\u009cN\u0000\u0457\u0456\u0001\u0000\u0000\u0000\u0457"+
		"\u0458\u0001\u0000\u0000\u0000\u0458\u0459\u0001\u0000\u0000\u0000\u0459"+
		"\u0476\u0005\u0013\u0000\u0000\u045a\u045c\u0003\u009cN\u0000\u045b\u045a"+
		"\u0001\u0000\u0000\u0000\u045b\u045c\u0001\u0000\u0000\u0000\u045c\u045d"+
		"\u0001\u0000\u0000\u0000\u045d\u0476\u0005\u0014\u0000\u0000\u045e\u0460"+
		"\u0003\u009cN\u0000\u045f\u045e\u0001\u0000\u0000\u0000\u045f\u0460\u0001"+
		"\u0000\u0000\u0000\u0460\u0461\u0001\u0000\u0000\u0000\u0461\u0476\u0005"+
		"S\u0000\u0000\u0462\u0476\u0005\u000e\u0000\u0000\u0463\u0465\u0003\u009c"+
		"N\u0000\u0464\u0463\u0001\u0000\u0000\u0000\u0464\u0465\u0001\u0000\u0000"+
		"\u0000\u0465\u0469\u0001\u0000\u0000\u0000\u0466\u0468\u0003\u009aM\u0000"+
		"\u0467\u0466\u0001\u0000\u0000\u0000\u0468\u046b\u0001\u0000\u0000\u0000"+
		"\u0469\u0467\u0001\u0000\u0000\u0000\u0469\u046a\u0001\u0000\u0000\u0000"+
		"\u046a\u046c\u0001\u0000\u0000\u0000\u046b\u0469\u0001\u0000\u0000\u0000"+
		"\u046c\u0476\u0005-\u0000\u0000\u046d\u0476\u0005\'\u0000\u0000\u046e"+
		"\u0470\u0003\u009aM\u0000\u046f\u046e\u0001\u0000\u0000\u0000\u046f\u0470"+
		"\u0001\u0000\u0000\u0000\u0470\u0471\u0001\u0000\u0000\u0000\u0471\u0476"+
		"\u0005\u001e\u0000\u0000\u0472\u0476\u0005Q\u0000\u0000\u0473\u0476\u0005"+
		"\r\u0000\u0000\u0474\u0476\u0003\u00a2Q\u0000\u0475\u0442\u0001\u0000"+
		"\u0000\u0000\u0475\u0445\u0001\u0000\u0000\u0000\u0475\u0449\u0001\u0000"+
		"\u0000\u0000\u0475\u044b\u0001\u0000\u0000\u0000\u0475\u0453\u0001\u0000"+
		"\u0000\u0000\u0475\u0457\u0001\u0000\u0000\u0000\u0475\u045b\u0001\u0000"+
		"\u0000\u0000\u0475\u045f\u0001\u0000\u0000\u0000\u0475\u0462\u0001\u0000"+
		"\u0000\u0000\u0475\u0464\u0001\u0000\u0000\u0000\u0475\u046d\u0001\u0000"+
		"\u0000\u0000\u0475\u046f\u0001\u0000\u0000\u0000\u0475\u0472\u0001\u0000"+
		"\u0000\u0000\u0475\u0473\u0001\u0000\u0000\u0000\u0475\u0474\u0001\u0000"+
		"\u0000\u0000\u0476\u009f\u0001\u0000\u0000\u0000\u0477\u047c\u0003\u0116"+
		"\u008b\u0000\u0478\u047c\u0003\u00a6S\u0000\u0479\u047c\u0003\u0090H\u0000"+
		"\u047a\u047c\u0003\u0156\u00ab\u0000\u047b\u0477\u0001\u0000\u0000\u0000"+
		"\u047b\u0478\u0001\u0000\u0000\u0000\u047b\u0479\u0001\u0000\u0000\u0000"+
		"\u047b\u047a\u0001\u0000\u0000\u0000\u047c\u00a1\u0001\u0000\u0000\u0000"+
		"\u047d\u047e\u0005\u001a\u0000\u0000\u047e\u0481\u0005U\u0000\u0000\u047f"+
		"\u0482\u0003Z-\u0000\u0480\u0482\u0005\r\u0000\u0000\u0481\u047f\u0001"+
		"\u0000\u0000\u0000\u0481\u0480\u0001\u0000\u0000\u0000\u0482\u0483\u0001"+
		"\u0000\u0000\u0000\u0483\u0484\u0005V\u0000\u0000\u0484\u00a3\u0001\u0000"+
		"\u0000\u0000\u0485\u0494\u0003\u0120\u0090\u0000\u0486\u0488\u0003\u00cc"+
		"f\u0000\u0487\u0486\u0001\u0000\u0000\u0000\u0487\u0488\u0001\u0000\u0000"+
		"\u0000\u0488\u048a\u0001\u0000\u0000\u0000\u0489\u048b\u0003\n\u0005\u0000"+
		"\u048a\u0489\u0001\u0000\u0000\u0000\u048a\u048b\u0001\u0000\u0000\u0000"+
		"\u048b\u048c\u0001\u0000\u0000\u0000\u048c\u0495\u0005\u0084\u0000\u0000"+
		"\u048d\u0495\u0003\u0156\u00ab\u0000\u048e\u0490\u0003\n\u0005\u0000\u048f"+
		"\u0491\u0005D\u0000\u0000\u0490\u048f\u0001\u0000\u0000\u0000\u0490\u0491"+
		"\u0001\u0000\u0000\u0000\u0491\u0492\u0001\u0000\u0000\u0000\u0492\u0493"+
		"\u0003\u0156\u00ab\u0000\u0493\u0495\u0001\u0000\u0000\u0000\u0494\u0487"+
		"\u0001\u0000\u0000\u0000\u0494\u048d\u0001\u0000\u0000\u0000\u0494\u048e"+
		"\u0001\u0000\u0000\u0000\u0495\u049c\u0001\u0000\u0000\u0000\u0496\u0498"+
		"\u0005!\u0000\u0000\u0497\u0499\u0003\n\u0005\u0000\u0498\u0497\u0001"+
		"\u0000\u0000\u0000\u0498\u0499\u0001\u0000\u0000\u0000\u0499\u049a\u0001"+
		"\u0000\u0000\u0000\u049a\u049c\u0005\u0084\u0000\u0000\u049b\u0485\u0001"+
		"\u0000\u0000\u0000\u049b\u0496\u0001\u0000\u0000\u0000\u049c\u00a5\u0001"+
		"\u0000\u0000\u0000\u049d\u049e\u0005\u0084\u0000\u0000\u049e\u00a7\u0001"+
		"\u0000\u0000\u0000\u049f\u04a0\u0003\u00aaU\u0000\u04a0\u04a5\u0005Y\u0000"+
		"\u0000\u04a1\u04a3\u0003\u00b2Y\u0000\u04a2\u04a4\u0005z\u0000\u0000\u04a3"+
		"\u04a2\u0001\u0000\u0000\u0000\u04a3\u04a4\u0001\u0000\u0000\u0000\u04a4"+
		"\u04a6\u0001\u0000\u0000\u0000\u04a5\u04a1\u0001\u0000\u0000\u0000\u04a5"+
		"\u04a6\u0001\u0000\u0000\u0000\u04a6\u04a7\u0001\u0000\u0000\u0000\u04a7"+
		"\u04a8\u0005Z\u0000\u0000\u04a8\u00a9\u0001\u0000\u0000\u0000\u04a9\u04ab"+
		"\u0003\u00aeW\u0000\u04aa\u04ac\u0003\u00ccf\u0000\u04ab\u04aa\u0001\u0000"+
		"\u0000\u0000\u04ab\u04ac\u0001\u0000\u0000\u0000\u04ac\u04b1\u0001\u0000"+
		"\u0000\u0000\u04ad\u04af\u0003\n\u0005\u0000\u04ae\u04ad\u0001\u0000\u0000"+
		"\u0000\u04ae\u04af\u0001\u0000\u0000\u0000\u04af\u04b0\u0001\u0000\u0000"+
		"\u0000\u04b0\u04b2\u0005\u0084\u0000\u0000\u04b1\u04ae\u0001\u0000\u0000"+
		"\u0000\u04b1\u04b2\u0001\u0000\u0000\u0000\u04b2\u04b4\u0001\u0000\u0000"+
		"\u0000\u04b3\u04b5\u0003\u00b0X\u0000\u04b4\u04b3\u0001\u0000\u0000\u0000"+
		"\u04b4\u04b5\u0001\u0000\u0000\u0000\u04b5\u00ab\u0001\u0000\u0000\u0000"+
		"\u04b6\u04b8\u0003\u00aeW\u0000\u04b7\u04b9\u0003\u00ccf\u0000\u04b8\u04b7"+
		"\u0001\u0000\u0000\u0000\u04b8\u04b9\u0001\u0000\u0000\u0000\u04b9\u04ba"+
		"\u0001\u0000\u0000\u0000\u04ba\u04bc\u0005\u0084\u0000\u0000\u04bb\u04bd"+
		"\u0003\u00b0X\u0000\u04bc\u04bb\u0001\u0000\u0000\u0000\u04bc\u04bd\u0001"+
		"\u0000\u0000\u0000\u04bd\u04be\u0001\u0000\u0000\u0000\u04be\u04bf\u0005"+
		"\u0080\u0000\u0000\u04bf\u00ad\u0001\u0000\u0000\u0000\u04c0\u04c2\u0005"+
		"!\u0000\u0000\u04c1\u04c3\u0007\u000f\u0000\u0000\u04c2\u04c1\u0001\u0000"+
		"\u0000\u0000\u04c2\u04c3\u0001\u0000\u0000\u0000\u04c3\u00af\u0001\u0000"+
		"\u0000\u0000\u04c4\u04c5\u0005~\u0000\u0000\u04c5\u04c6\u0003\u0096K\u0000"+
		"\u04c6\u00b1\u0001\u0000\u0000\u0000\u04c7\u04cc\u0003\u00b4Z\u0000\u04c8"+
		"\u04c9\u0005z\u0000\u0000\u04c9\u04cb\u0003\u00b4Z\u0000\u04ca\u04c8\u0001"+
		"\u0000\u0000\u0000\u04cb\u04ce\u0001\u0000\u0000\u0000\u04cc\u04ca\u0001"+
		"\u0000\u0000\u0000\u04cc\u04cd\u0001\u0000\u0000\u0000\u04cd\u00b3\u0001"+
		"\u0000\u0000\u0000\u04ce\u04cc\u0001\u0000\u0000\u0000\u04cf\u04d2\u0003"+
		"\u00b6[\u0000\u04d0\u04d1\u0005e\u0000\u0000\u04d1\u04d3\u0003\\.\u0000"+
		"\u04d2\u04d0\u0001\u0000\u0000\u0000\u04d2\u04d3\u0001\u0000\u0000\u0000"+
		"\u04d3\u00b5\u0001\u0000\u0000\u0000\u04d4\u04d5\u0005\u0084\u0000\u0000"+
		"\u04d5\u00b7\u0001\u0000\u0000\u0000\u04d6\u04d9\u0003\u00ba]\u0000\u04d7"+
		"\u04d9\u0003\u00be_\u0000\u04d8\u04d6\u0001\u0000\u0000\u0000\u04d8\u04d7"+
		"\u0001\u0000\u0000\u0000\u04d9\u00b9\u0001\u0000\u0000\u0000\u04da\u04db"+
		"\u0005\u0084\u0000\u0000\u04db\u00bb\u0001\u0000\u0000\u0000\u04dc\u04de"+
		"\u0005,\u0000\u0000\u04dd\u04dc\u0001\u0000\u0000\u0000\u04dd\u04de\u0001"+
		"\u0000\u0000\u0000\u04de\u04df\u0001\u0000\u0000\u0000\u04df\u04e2\u0005"+
		"0\u0000\u0000\u04e0\u04e3\u0005\u0084\u0000\u0000\u04e1\u04e3\u0003\u00ba"+
		"]\u0000\u04e2\u04e0\u0001\u0000\u0000\u0000\u04e2\u04e1\u0001\u0000\u0000"+
		"\u0000\u04e2\u04e3\u0001\u0000\u0000\u0000\u04e3\u04e4\u0001\u0000\u0000"+
		"\u0000\u04e4\u04e6\u0005Y\u0000\u0000\u04e5\u04e7\u0003x<\u0000\u04e6"+
		"\u04e5\u0001\u0000\u0000\u0000\u04e6\u04e7\u0001\u0000\u0000\u0000\u04e7"+
		"\u04e8\u0001\u0000\u0000\u0000\u04e8\u04e9\u0005Z\u0000\u0000\u04e9\u00bd"+
		"\u0001\u0000\u0000\u0000\u04ea\u04eb\u0005\u0084\u0000\u0000\u04eb\u00bf"+
		"\u0001\u0000\u0000\u0000\u04ec\u04ed\u00050\u0000\u0000\u04ed\u04ee\u0005"+
		"\u0084\u0000\u0000\u04ee\u04ef\u0005e\u0000\u0000\u04ef\u04f0\u0003\u00c2"+
		"a\u0000\u04f0\u04f1\u0005\u0080\u0000\u0000\u04f1\u00c1\u0001\u0000\u0000"+
		"\u0000\u04f2\u04f4\u0003\n\u0005\u0000\u04f3\u04f2\u0001\u0000\u0000\u0000"+
		"\u04f3\u04f4\u0001\u0000\u0000\u0000\u04f4\u04f5\u0001\u0000\u0000\u0000"+
		"\u04f5\u04f6\u0003\u00b8\\\u0000\u04f6\u00c3\u0001\u0000\u0000\u0000\u04f7"+
		"\u04fd\u0005O\u0000\u0000\u04f8\u04fa\u0005L\u0000\u0000\u04f9\u04f8\u0001"+
		"\u0000\u0000\u0000\u04f9\u04fa\u0001\u0000\u0000\u0000\u04fa\u04fb\u0001"+
		"\u0000\u0000\u0000\u04fb\u04fe\u0003\n\u0005\u0000\u04fc\u04fe\u0005\u007f"+
		"\u0000\u0000\u04fd\u04f9\u0001\u0000\u0000\u0000\u04fd\u04fc\u0001\u0000"+
		"\u0000\u0000\u04fe\u04ff\u0001\u0000\u0000\u0000\u04ff\u0500\u0003\u0006"+
		"\u0003\u0000\u0500\u0501\u0005\u0080\u0000\u0000\u0501\u00c5\u0001\u0000"+
		"\u0000\u0000\u0502\u0504\u0003\u00ccf\u0000\u0503\u0502\u0001\u0000\u0000"+
		"\u0000\u0503\u0504\u0001\u0000\u0000\u0000\u0504\u0505\u0001\u0000\u0000"+
		"\u0000\u0505\u0506\u0005O\u0000\u0000\u0506\u0508\u00050\u0000\u0000\u0507"+
		"\u0509\u0003\n\u0005\u0000\u0508\u0507\u0001\u0000\u0000\u0000\u0508\u0509"+
		"\u0001\u0000\u0000\u0000\u0509\u050a\u0001\u0000\u0000\u0000\u050a\u050b"+
		"\u0003\u00b8\\\u0000\u050b\u050c\u0005\u0080\u0000\u0000\u050c\u00c7\u0001"+
		"\u0000\u0000\u0000\u050d\u050e\u0005\f\u0000\u0000\u050e\u050f\u0005U"+
		"\u0000\u0000\u050f\u0510\u0005\u0004\u0000\u0000\u0510\u0511\u0005V\u0000"+
		"\u0000\u0511\u0512\u0005\u0080\u0000\u0000\u0512\u00c9\u0001\u0000\u0000"+
		"\u0000\u0513\u0514\u0005$\u0000\u0000\u0514\u051b\u0005\u0004\u0000\u0000"+
		"\u0515\u0517\u0005Y\u0000\u0000\u0516\u0518\u0003x<\u0000\u0517\u0516"+
		"\u0001\u0000\u0000\u0000\u0517\u0518\u0001\u0000\u0000\u0000\u0518\u0519"+
		"\u0001\u0000\u0000\u0000\u0519\u051c\u0005Z\u0000\u0000\u051a\u051c\u0003"+
		"z=\u0000\u051b\u0515\u0001\u0000\u0000\u0000\u051b\u051a\u0001\u0000\u0000"+
		"\u0000\u051c\u00cb\u0001\u0000\u0000\u0000\u051d\u051f\u0003\u00ceg\u0000"+
		"\u051e\u051d\u0001\u0000\u0000\u0000\u051f\u0520\u0001\u0000\u0000\u0000"+
		"\u0520\u051e\u0001\u0000\u0000\u0000\u0520\u0521\u0001\u0000\u0000\u0000"+
		"\u0521\u00cd\u0001\u0000\u0000\u0000\u0522\u0523\u0005W\u0000\u0000\u0523"+
		"\u0525\u0005W\u0000\u0000\u0524\u0526\u0003\u00d2i\u0000\u0525\u0524\u0001"+
		"\u0000\u0000\u0000\u0525\u0526\u0001\u0000\u0000\u0000\u0526\u0527\u0001"+
		"\u0000\u0000\u0000\u0527\u0528\u0005X\u0000\u0000\u0528\u052b\u0005X\u0000"+
		"\u0000\u0529\u052b\u0003\u00d0h\u0000\u052a\u0522\u0001\u0000\u0000\u0000"+
		"\u052a\u0529\u0001\u0000\u0000\u0000\u052b\u00cf\u0001\u0000\u0000\u0000"+
		"\u052c\u052d\u0005\n\u0000\u0000\u052d\u0530\u0005U\u0000\u0000\u052e"+
		"\u0531\u0003\u00f6{\u0000\u052f\u0531\u0003\\.\u0000\u0530\u052e\u0001"+
		"\u0000\u0000\u0000\u0530\u052f\u0001\u0000\u0000\u0000\u0531\u0533\u0001"+
		"\u0000\u0000\u0000\u0532\u0534\u0005\u0083\u0000\u0000\u0533\u0532\u0001"+
		"\u0000\u0000\u0000\u0533\u0534\u0001\u0000\u0000\u0000\u0534\u0535\u0001"+
		"\u0000\u0000\u0000\u0535\u0536\u0005V\u0000\u0000\u0536\u00d1\u0001\u0000"+
		"\u0000\u0000\u0537\u053c\u0003\u00d4j\u0000\u0538\u0539\u0005z\u0000\u0000"+
		"\u0539\u053b\u0003\u00d4j\u0000\u053a\u0538\u0001\u0000\u0000\u0000\u053b"+
		"\u053e\u0001\u0000\u0000\u0000\u053c\u053a\u0001\u0000\u0000\u0000\u053c"+
		"\u053d\u0001\u0000\u0000\u0000\u053d\u0540\u0001\u0000\u0000\u0000\u053e"+
		"\u053c\u0001\u0000\u0000\u0000\u053f\u0541\u0005\u0083\u0000\u0000\u0540"+
		"\u053f\u0001\u0000\u0000\u0000\u0540\u0541\u0001\u0000\u0000\u0000\u0541"+
		"\u00d3\u0001\u0000\u0000\u0000\u0542\u0543\u0003\u00d6k\u0000\u0543\u0544"+
		"\u0005\u007f\u0000\u0000\u0544\u0546\u0001\u0000\u0000\u0000\u0545\u0542"+
		"\u0001\u0000\u0000\u0000\u0545\u0546\u0001\u0000\u0000\u0000\u0546\u0547"+
		"\u0001\u0000\u0000\u0000\u0547\u0549\u0005\u0084\u0000\u0000\u0548\u054a"+
		"\u0003\u00d8l\u0000\u0549\u0548\u0001\u0000\u0000\u0000\u0549\u054a\u0001"+
		"\u0000\u0000\u0000\u054a\u00d5\u0001\u0000\u0000\u0000\u054b\u054c\u0005"+
		"\u0084\u0000\u0000\u054c\u00d7\u0001\u0000\u0000\u0000\u054d\u054f\u0005"+
		"U\u0000\u0000\u054e\u0550\u0003\u00dam\u0000\u054f\u054e\u0001\u0000\u0000"+
		"\u0000\u054f\u0550\u0001\u0000\u0000\u0000\u0550\u0551\u0001\u0000\u0000"+
		"\u0000\u0551\u0552\u0005V\u0000\u0000\u0552\u00d9\u0001\u0000\u0000\u0000"+
		"\u0553\u0555\u0003\u00dcn\u0000\u0554\u0553\u0001\u0000\u0000\u0000\u0555"+
		"\u0556\u0001\u0000\u0000\u0000\u0556\u0554\u0001\u0000\u0000\u0000\u0556"+
		"\u0557\u0001\u0000\u0000\u0000\u0557\u00db\u0001\u0000\u0000\u0000\u0558"+
		"\u0559\u0005U\u0000\u0000\u0559\u055a\u0003\u00dam\u0000\u055a\u055b\u0005"+
		"V\u0000\u0000\u055b\u056a\u0001\u0000\u0000\u0000\u055c\u055d\u0005W\u0000"+
		"\u0000\u055d\u055e\u0003\u00dam\u0000\u055e\u055f\u0005X\u0000\u0000\u055f"+
		"\u056a\u0001\u0000\u0000\u0000\u0560\u0561\u0005Y\u0000\u0000\u0561\u0562"+
		"\u0003\u00dam\u0000\u0562\u0563\u0005Z\u0000\u0000\u0563\u056a\u0001\u0000"+
		"\u0000\u0000\u0564\u0566\b\u0010\u0000\u0000\u0565\u0564\u0001\u0000\u0000"+
		"\u0000\u0566\u0567\u0001\u0000\u0000\u0000\u0567\u0565\u0001\u0000\u0000"+
		"\u0000\u0567\u0568\u0001\u0000\u0000\u0000\u0568\u056a\u0001\u0000\u0000"+
		"\u0000\u0569\u0558\u0001\u0000\u0000\u0000\u0569\u055c\u0001\u0000\u0000"+
		"\u0000\u0569\u0560\u0001\u0000\u0000\u0000\u0569\u0565\u0001\u0000\u0000"+
		"\u0000\u056a\u00dd\u0001\u0000\u0000\u0000\u056b\u0570\u0003\u00e0p\u0000"+
		"\u056c\u056d\u0005z\u0000\u0000\u056d\u056f\u0003\u00e0p\u0000\u056e\u056c"+
		"\u0001\u0000\u0000\u0000\u056f\u0572\u0001\u0000\u0000\u0000\u0570\u056e"+
		"\u0001\u0000\u0000\u0000\u0570\u0571\u0001\u0000\u0000\u0000\u0571\u00df"+
		"\u0001\u0000\u0000\u0000\u0572\u0570\u0001\u0000\u0000\u0000\u0573\u0575"+
		"\u0003\u00e2q\u0000\u0574\u0576\u0003\u010c\u0086\u0000\u0575\u0574\u0001"+
		"\u0000\u0000\u0000\u0575\u0576\u0001\u0000\u0000\u0000\u0576\u00e1\u0001"+
		"\u0000\u0000\u0000\u0577\u057d\u0003\u00e4r\u0000\u0578\u0579\u0003\u00e6"+
		"s\u0000\u0579\u057a\u0003\u00e8t\u0000\u057a\u057b\u0003\u00eau\u0000"+
		"\u057b\u057d\u0001\u0000\u0000\u0000\u057c\u0577\u0001\u0000\u0000\u0000"+
		"\u057c\u0578\u0001\u0000\u0000\u0000\u057d\u00e3\u0001\u0000\u0000\u0000"+
		"\u057e\u0580\u0003\u00ecv\u0000\u057f\u0581\u0005\u0016\u0000\u0000\u0580"+
		"\u057f\u0001\u0000\u0000\u0000\u0580\u0581\u0001\u0000\u0000\u0000\u0581"+
		"\u0583\u0001\u0000\u0000\u0000\u0582\u057e\u0001\u0000\u0000\u0000\u0583"+
		"\u0586\u0001\u0000\u0000\u0000\u0584\u0582\u0001\u0000\u0000\u0000\u0584"+
		"\u0585\u0001\u0000\u0000\u0000\u0585\u0587\u0001\u0000\u0000\u0000\u0586"+
		"\u0584\u0001\u0000\u0000\u0000\u0587\u0588\u0003\u00e6s\u0000\u0588\u00e5"+
		"\u0001\u0000\u0000\u0000\u0589\u058a\u0006s\uffff\uffff\u0000\u058a\u058c"+
		"\u0003\u00f4z\u0000\u058b\u058d\u0003\u00ccf\u0000\u058c\u058b\u0001\u0000"+
		"\u0000\u0000\u058c\u058d\u0001\u0000\u0000\u0000\u058d\u0593\u0001\u0000"+
		"\u0000\u0000\u058e\u058f\u0005U\u0000\u0000\u058f\u0590\u0003\u00e4r\u0000"+
		"\u0590\u0591\u0005V\u0000\u0000\u0591\u0593\u0001\u0000\u0000\u0000\u0592"+
		"\u0589\u0001\u0000\u0000\u0000\u0592\u058e\u0001\u0000\u0000\u0000\u0593"+
		"\u05a2\u0001\u0000\u0000\u0000\u0594\u059e\n\u0002\u0000\u0000\u0595\u059f"+
		"\u0003\u00e8t\u0000\u0596\u0598\u0005W\u0000\u0000\u0597\u0599\u0003\\"+
		".\u0000\u0598\u0597\u0001\u0000\u0000\u0000\u0598\u0599\u0001\u0000\u0000"+
		"\u0000\u0599\u059a\u0001\u0000\u0000\u0000\u059a\u059c\u0005X\u0000\u0000"+
		"\u059b\u059d\u0003\u00ccf\u0000\u059c\u059b\u0001\u0000\u0000\u0000\u059c"+
		"\u059d\u0001\u0000\u0000\u0000\u059d\u059f\u0001\u0000\u0000\u0000\u059e"+
		"\u0595\u0001\u0000\u0000\u0000\u059e\u0596\u0001\u0000\u0000\u0000\u059f"+
		"\u05a1\u0001\u0000\u0000\u0000\u05a0\u0594\u0001\u0000\u0000\u0000\u05a1"+
		"\u05a4\u0001\u0000\u0000\u0000\u05a2\u05a0\u0001\u0000\u0000\u0000\u05a2"+
		"\u05a3\u0001\u0000\u0000\u0000\u05a3\u00e7\u0001\u0000\u0000\u0000\u05a4"+
		"\u05a2\u0001\u0000\u0000\u0000\u05a5\u05a7\u0005U\u0000\u0000\u05a6\u05a8"+
		"\u0003\u0102\u0081\u0000\u05a7\u05a6\u0001\u0000\u0000\u0000\u05a7\u05a8"+
		"\u0001\u0000\u0000\u0000\u05a8\u05a9\u0001\u0000\u0000\u0000\u05a9\u05ab"+
		"\u0005V\u0000\u0000\u05aa\u05ac\u0003\u00eew\u0000\u05ab\u05aa\u0001\u0000"+
		"\u0000\u0000\u05ab\u05ac\u0001\u0000\u0000\u0000\u05ac\u05ae\u0001\u0000"+
		"\u0000\u0000\u05ad\u05af\u0003\u00f2y\u0000\u05ae\u05ad\u0001\u0000\u0000"+
		"\u0000\u05ae\u05af\u0001\u0000\u0000\u0000\u05af\u05b1\u0001\u0000\u0000"+
		"\u0000\u05b0\u05b2\u0003\u0172\u00b9\u0000\u05b1\u05b0\u0001\u0000\u0000"+
		"\u0000\u05b1\u05b2\u0001\u0000\u0000\u0000\u05b2\u05b4\u0001\u0000\u0000"+
		"\u0000\u05b3\u05b5\u0003\u00ccf\u0000\u05b4\u05b3\u0001\u0000\u0000\u0000"+
		"\u05b4\u05b5\u0001\u0000\u0000\u0000\u05b5\u00e9\u0001\u0000\u0000\u0000"+
		"\u05b6\u05b7\u0005|\u0000\u0000\u05b7\u05b9\u0003\u0098L\u0000\u05b8\u05ba"+
		"\u0003\u00f8|\u0000\u05b9\u05b8\u0001\u0000\u0000\u0000\u05b9\u05ba\u0001"+
		"\u0000\u0000\u0000\u05ba\u00eb\u0001\u0000\u0000\u0000\u05bb\u05bd\u0007"+
		"\u0011\u0000\u0000\u05bc\u05be\u0003\u00ccf\u0000\u05bd\u05bc\u0001\u0000"+
		"\u0000\u0000\u05bd\u05be\u0001\u0000\u0000\u0000\u05be\u05ca\u0001\u0000"+
		"\u0000\u0000\u05bf\u05c1\u0003\n\u0005\u0000\u05c0\u05bf\u0001\u0000\u0000"+
		"\u0000\u05c0\u05c1\u0001\u0000\u0000\u0000\u05c1\u05c2\u0001\u0000\u0000"+
		"\u0000\u05c2\u05c4\u0005]\u0000\u0000\u05c3\u05c5\u0003\u00ccf\u0000\u05c4"+
		"\u05c3\u0001\u0000\u0000\u0000\u05c4\u05c5\u0001\u0000\u0000\u0000\u05c5"+
		"\u05c7\u0001\u0000\u0000\u0000\u05c6\u05c8\u0003\u00eew\u0000\u05c7\u05c6"+
		"\u0001\u0000\u0000\u0000\u05c7\u05c8\u0001\u0000\u0000\u0000\u05c8\u05ca"+
		"\u0001\u0000\u0000\u0000\u05c9\u05bb\u0001\u0000\u0000\u0000\u05c9\u05c0"+
		"\u0001\u0000\u0000\u0000\u05ca\u00ed\u0001\u0000\u0000\u0000\u05cb\u05cd"+
		"\u0003\u00f0x\u0000\u05cc\u05cb\u0001\u0000\u0000\u0000\u05cd\u05ce\u0001"+
		"\u0000\u0000\u0000\u05ce\u05cc\u0001\u0000\u0000\u0000\u05ce\u05cf\u0001"+
		"\u0000\u0000\u0000\u05cf\u00ef\u0001\u0000\u0000\u0000\u05d0\u05d1\u0007"+
		"\u0012\u0000\u0000\u05d1\u00f1\u0001\u0000\u0000\u0000\u05d2\u05d3\u0007"+
		"\u0011\u0000\u0000\u05d3\u00f3\u0001\u0000\u0000\u0000\u05d4\u05d6\u0005"+
		"\u0083\u0000\u0000\u05d5\u05d4\u0001\u0000\u0000\u0000\u05d5\u05d6\u0001"+
		"\u0000\u0000\u0000\u05d6\u05d7\u0001\u0000\u0000\u0000\u05d7\u05d8\u0003"+
		"\u0004\u0002\u0000\u05d8\u00f5\u0001\u0000\u0000\u0000\u05d9\u05db\u0003"+
		"\u0096K\u0000\u05da\u05dc\u0003\u00f8|\u0000\u05db\u05da\u0001\u0000\u0000"+
		"\u0000\u05db\u05dc\u0001\u0000\u0000\u0000\u05dc\u00f7\u0001\u0000\u0000"+
		"\u0000\u05dd\u05e6\u0003\u00fa}\u0000\u05de\u05e0\u0003\u00fc~\u0000\u05df"+
		"\u05de\u0001\u0000\u0000\u0000\u05df\u05e0\u0001\u0000\u0000\u0000\u05e0"+
		"\u05e1\u0001\u0000\u0000\u0000\u05e1\u05e2\u0003\u00e8t\u0000\u05e2\u05e3"+
		"\u0003\u00eau\u0000\u05e3\u05e6\u0001\u0000\u0000\u0000\u05e4\u05e6\u0003"+
		"\u00fe\u007f\u0000\u05e5\u05dd\u0001\u0000\u0000\u0000\u05e5\u05df\u0001"+
		"\u0000\u0000\u0000\u05e5\u05e4\u0001\u0000\u0000\u0000\u05e6\u00f9\u0001"+
		"\u0000\u0000\u0000\u05e7\u05f1\u0003\u00fc~\u0000\u05e8\u05ea\u0003\u00ec"+
		"v\u0000\u05e9\u05e8\u0001\u0000\u0000\u0000\u05ea\u05eb\u0001\u0000\u0000"+
		"\u0000\u05eb\u05e9\u0001\u0000\u0000\u0000\u05eb\u05ec\u0001\u0000\u0000"+
		"\u0000\u05ec\u05ee\u0001\u0000\u0000\u0000\u05ed\u05ef\u0003\u00fc~\u0000"+
		"\u05ee\u05ed\u0001\u0000\u0000\u0000\u05ee\u05ef\u0001\u0000\u0000\u0000"+
		"\u05ef\u05f1\u0001\u0000\u0000\u0000\u05f0\u05e7\u0001\u0000\u0000\u0000"+
		"\u05f0\u05e9\u0001\u0000\u0000\u0000\u05f1\u00fb\u0001\u0000\u0000\u0000"+
		"\u05f2\u05f3\u0006~\uffff\uffff\u0000\u05f3\u0601\u0003\u00e8t\u0000\u05f4"+
		"\u05f6\u0005W\u0000\u0000\u05f5\u05f7\u0003\\.\u0000\u05f6\u05f5\u0001"+
		"\u0000\u0000\u0000\u05f6\u05f7\u0001\u0000\u0000\u0000\u05f7\u05f8\u0001"+
		"\u0000\u0000\u0000\u05f8\u05fa\u0005X\u0000\u0000\u05f9\u05fb\u0003\u00cc"+
		"f\u0000\u05fa\u05f9\u0001\u0000\u0000\u0000\u05fa\u05fb\u0001\u0000\u0000"+
		"\u0000\u05fb\u0601\u0001\u0000\u0000\u0000\u05fc\u05fd\u0005U\u0000\u0000"+
		"\u05fd\u05fe\u0003\u00fa}\u0000\u05fe\u05ff\u0005V\u0000\u0000\u05ff\u0601"+
		"\u0001\u0000\u0000\u0000\u0600\u05f2\u0001\u0000\u0000\u0000\u0600\u05f4"+
		"\u0001\u0000\u0000\u0000\u0600\u05fc\u0001\u0000\u0000\u0000\u0601\u0611"+
		"\u0001\u0000\u0000\u0000\u0602\u060d\n\u0004\u0000\u0000\u0603\u060e\u0003"+
		"\u00e8t\u0000\u0604\u0605\u0003\u00fc~\u0000\u0605\u0607\u0005W\u0000"+
		"\u0000\u0606\u0608\u0003\\.\u0000\u0607\u0606\u0001\u0000\u0000\u0000"+
		"\u0607\u0608\u0001\u0000\u0000\u0000\u0608\u0609\u0001\u0000\u0000\u0000"+
		"\u0609\u060b\u0005X\u0000\u0000\u060a\u060c\u0003\u00ccf\u0000\u060b\u060a"+
		"\u0001\u0000\u0000\u0000\u060b\u060c\u0001\u0000\u0000\u0000\u060c\u060e"+
		"\u0001\u0000\u0000\u0000\u060d\u0603\u0001\u0000\u0000\u0000\u060d\u0604"+
		"\u0001\u0000\u0000\u0000\u060e\u0610\u0001\u0000\u0000\u0000\u060f\u0602"+
		"\u0001\u0000\u0000\u0000\u0610\u0613\u0001\u0000\u0000\u0000\u0611\u060f"+
		"\u0001\u0000\u0000\u0000\u0611\u0612\u0001\u0000\u0000\u0000\u0612\u00fd"+
		"\u0001\u0000\u0000\u0000\u0613\u0611\u0001\u0000\u0000\u0000\u0614\u0616"+
		"\u0003\u00ecv\u0000\u0615\u0614\u0001\u0000\u0000\u0000\u0616\u0619\u0001"+
		"\u0000\u0000\u0000\u0617\u0615\u0001\u0000\u0000\u0000\u0617\u0618\u0001"+
		"\u0000\u0000\u0000\u0618\u061a\u0001\u0000\u0000\u0000\u0619\u0617\u0001"+
		"\u0000\u0000\u0000\u061a\u061b\u0003\u0100\u0080\u0000\u061b\u00ff\u0001"+
		"\u0000\u0000\u0000\u061c\u061d\u0006\u0080\uffff\uffff\u0000\u061d\u061e"+
		"\u0005\u0083\u0000\u0000\u061e\u062d\u0001\u0000\u0000\u0000\u061f\u0629"+
		"\n\u0002\u0000\u0000\u0620\u062a\u0003\u00e8t\u0000\u0621\u0623\u0005"+
		"W\u0000\u0000\u0622\u0624\u0003\\.\u0000\u0623\u0622\u0001\u0000\u0000"+
		"\u0000\u0623\u0624\u0001\u0000\u0000\u0000\u0624\u0625\u0001\u0000\u0000"+
		"\u0000\u0625\u0627\u0005X\u0000\u0000\u0626\u0628\u0003\u00ccf\u0000\u0627"+
		"\u0626\u0001\u0000\u0000\u0000\u0627\u0628\u0001\u0000\u0000\u0000\u0628"+
		"\u062a\u0001\u0000\u0000\u0000\u0629\u0620\u0001\u0000\u0000\u0000\u0629"+
		"\u0621\u0001\u0000\u0000\u0000\u062a\u062c\u0001\u0000\u0000\u0000\u062b"+
		"\u061f\u0001\u0000\u0000\u0000\u062c\u062f\u0001\u0000\u0000\u0000\u062d"+
		"\u062b\u0001\u0000\u0000\u0000\u062d\u062e\u0001\u0000\u0000\u0000\u062e"+
		"\u0101\u0001\u0000\u0000\u0000\u062f\u062d\u0001\u0000\u0000\u0000\u0630"+
		"\u0635\u0003\u0104\u0082\u0000\u0631\u0633\u0005z\u0000\u0000\u0632\u0631"+
		"\u0001\u0000\u0000\u0000\u0632\u0633\u0001\u0000\u0000\u0000\u0633\u0634"+
		"\u0001\u0000\u0000\u0000\u0634\u0636\u0005\u0083\u0000\u0000\u0635\u0632"+
		"\u0001\u0000\u0000\u0000\u0635\u0636\u0001\u0000\u0000\u0000\u0636\u0103"+
		"\u0001\u0000\u0000\u0000\u0637\u063c\u0003\u0106\u0083\u0000\u0638\u0639"+
		"\u0005z\u0000\u0000\u0639\u063b\u0003\u0106\u0083\u0000\u063a\u0638\u0001"+
		"\u0000\u0000\u0000\u063b\u063e\u0001\u0000\u0000\u0000\u063c\u063a\u0001"+
		"\u0000\u0000\u0000\u063c\u063d\u0001\u0000\u0000\u0000\u063d\u0105\u0001"+
		"\u0000\u0000\u0000\u063e\u063c\u0001\u0000\u0000\u0000\u063f\u0641\u0003"+
		"\u00ccf\u0000\u0640\u063f\u0001\u0000\u0000\u0000\u0640\u0641\u0001\u0000"+
		"\u0000\u0000\u0641\u0642\u0001\u0000\u0000\u0000\u0642\u0647\u0003\u008a"+
		"E\u0000\u0643\u0648\u0003\u00e2q\u0000\u0644\u0646\u0003\u00f8|\u0000"+
		"\u0645\u0644\u0001\u0000\u0000\u0000\u0645\u0646\u0001\u0000\u0000\u0000"+
		"\u0646\u0648\u0001\u0000\u0000\u0000\u0647\u0643\u0001\u0000\u0000\u0000"+
		"\u0647\u0645\u0001\u0000\u0000\u0000\u0648\u064b\u0001\u0000\u0000\u0000"+
		"\u0649\u064a\u0005e\u0000\u0000\u064a\u064c\u0003\u0110\u0088\u0000\u064b"+
		"\u0649\u0001\u0000\u0000\u0000\u064b\u064c\u0001\u0000\u0000\u0000\u064c"+
		"\u0107\u0001\u0000\u0000\u0000\u064d\u064f\u0003\u00ccf\u0000\u064e\u064d"+
		"\u0001\u0000\u0000\u0000\u064e\u064f\u0001\u0000\u0000\u0000\u064f\u0651"+
		"\u0001\u0000\u0000\u0000\u0650\u0652\u0003\u008aE\u0000\u0651\u0650\u0001"+
		"\u0000\u0000\u0000\u0651\u0652\u0001\u0000\u0000\u0000\u0652\u0653\u0001"+
		"\u0000\u0000\u0000\u0653\u0655\u0003\u00e2q\u0000\u0654\u0656\u0003\u012a"+
		"\u0095\u0000\u0655\u0654\u0001\u0000\u0000\u0000\u0655\u0656\u0001\u0000"+
		"\u0000\u0000\u0656\u0657\u0001\u0000\u0000\u0000\u0657\u0658\u0003\u010a"+
		"\u0085\u0000\u0658\u0109\u0001\u0000\u0000\u0000\u0659\u065b\u0003\u0142"+
		"\u00a1\u0000\u065a\u0659\u0001\u0000\u0000\u0000\u065a\u065b\u0001\u0000"+
		"\u0000\u0000\u065b\u065c\u0001\u0000\u0000\u0000\u065c\u0662\u0003d2\u0000"+
		"\u065d\u0662\u0003\u0168\u00b4\u0000\u065e\u065f\u0005e\u0000\u0000\u065f"+
		"\u0660\u0007\u0013\u0000\u0000\u0660\u0662\u0005\u0080\u0000\u0000\u0661"+
		"\u065a\u0001\u0000\u0000\u0000\u0661\u065d\u0001\u0000\u0000\u0000\u0661"+
		"\u065e\u0001\u0000\u0000\u0000\u0662\u010b\u0001\u0000\u0000\u0000\u0663"+
		"\u0669\u0003\u010e\u0087\u0000\u0664\u0665\u0005U\u0000\u0000\u0665\u0666"+
		"\u0003\"\u0011\u0000\u0666\u0667\u0005V\u0000\u0000\u0667\u0669\u0001"+
		"\u0000\u0000\u0000\u0668\u0663\u0001\u0000\u0000\u0000\u0668\u0664\u0001"+
		"\u0000\u0000\u0000\u0669\u010d\u0001\u0000\u0000\u0000\u066a\u066b\u0005"+
		"e\u0000\u0000\u066b\u066e\u0003\u0110\u0088\u0000\u066c\u066e\u0003\u0114"+
		"\u008a\u0000\u066d\u066a\u0001\u0000\u0000\u0000\u066d\u066c\u0001\u0000"+
		"\u0000\u0000\u066e\u010f\u0001\u0000\u0000\u0000\u066f\u0672\u0003V+\u0000"+
		"\u0670\u0672\u0003\u0114\u008a\u0000\u0671\u066f\u0001\u0000\u0000\u0000"+
		"\u0671\u0670\u0001\u0000\u0000\u0000\u0672\u0111\u0001\u0000\u0000\u0000"+
		"\u0673\u0675\u0003\u0110\u0088\u0000\u0674\u0676\u0005\u0083\u0000\u0000"+
		"\u0675\u0674\u0001\u0000\u0000\u0000\u0675\u0676\u0001\u0000\u0000\u0000"+
		"\u0676\u067e\u0001\u0000\u0000\u0000\u0677\u0678\u0005z\u0000\u0000\u0678"+
		"\u067a\u0003\u0110\u0088\u0000\u0679\u067b\u0005\u0083\u0000\u0000\u067a"+
		"\u0679\u0001\u0000\u0000\u0000\u067a\u067b\u0001\u0000\u0000\u0000\u067b"+
		"\u067d\u0001\u0000\u0000\u0000\u067c\u0677\u0001\u0000\u0000\u0000\u067d"+
		"\u0680\u0001\u0000\u0000\u0000\u067e\u067c\u0001\u0000\u0000\u0000\u067e"+
		"\u067f\u0001\u0000\u0000\u0000\u067f\u0113\u0001\u0000\u0000\u0000\u0680"+
		"\u067e\u0001\u0000\u0000\u0000\u0681\u0686\u0005Y\u0000\u0000\u0682\u0684"+
		"\u0003\u0112\u0089\u0000\u0683\u0685\u0005z\u0000\u0000\u0684\u0683\u0001"+
		"\u0000\u0000\u0000\u0684\u0685\u0001\u0000\u0000\u0000\u0685\u0687\u0001"+
		"\u0000\u0000\u0000\u0686\u0682\u0001\u0000\u0000\u0000\u0686\u0687\u0001"+
		"\u0000\u0000\u0000\u0687\u0688\u0001\u0000\u0000\u0000\u0688\u0689\u0005"+
		"Z\u0000\u0000\u0689\u0115\u0001\u0000\u0000\u0000\u068a\u068d\u0005\u0084"+
		"\u0000\u0000\u068b\u068d\u0003\u0156\u00ab\u0000\u068c\u068a\u0001\u0000"+
		"\u0000\u0000\u068c\u068b\u0001\u0000\u0000\u0000\u068d\u0117\u0001\u0000"+
		"\u0000\u0000\u068e\u068f\u0003\u011a\u008d\u0000\u068f\u0691\u0005Y\u0000"+
		"\u0000\u0690\u0692\u0003\u0122\u0091\u0000\u0691\u0690\u0001\u0000\u0000"+
		"\u0000\u0691\u0692\u0001\u0000\u0000\u0000\u0692\u0693\u0001\u0000\u0000"+
		"\u0000\u0693\u0694\u0005Z\u0000\u0000\u0694\u0119\u0001\u0000\u0000\u0000"+
		"\u0695\u0697\u0003\u0120\u0090\u0000\u0696\u0698\u0003\u00ccf\u0000\u0697"+
		"\u0696\u0001\u0000\u0000\u0000\u0697\u0698\u0001\u0000\u0000\u0000\u0698"+
		"\u069d\u0001\u0000\u0000\u0000\u0699\u069b\u0003\u011c\u008e\u0000\u069a"+
		"\u069c\u0003\u011e\u008f\u0000\u069b\u069a\u0001\u0000\u0000\u0000\u069b"+
		"\u069c\u0001\u0000\u0000\u0000\u069c\u069e\u0001\u0000\u0000\u0000\u069d"+
		"\u0699\u0001\u0000\u0000\u0000\u069d\u069e\u0001\u0000\u0000\u0000\u069e"+
		"\u06a0\u0001\u0000\u0000\u0000\u069f\u06a1\u0003\u0130\u0098\u0000\u06a0"+
		"\u069f\u0001\u0000\u0000\u0000\u06a0\u06a1\u0001\u0000\u0000\u0000\u06a1"+
		"\u06ad\u0001\u0000\u0000\u0000\u06a2\u06a4\u0005M\u0000\u0000\u06a3\u06a5"+
		"\u0003\u00ccf\u0000\u06a4\u06a3\u0001\u0000\u0000\u0000\u06a4\u06a5\u0001"+
		"\u0000\u0000\u0000\u06a5\u06aa\u0001\u0000\u0000\u0000\u06a6\u06a8\u0003"+
		"\u011c\u008e\u0000\u06a7\u06a9\u0003\u011e\u008f\u0000\u06a8\u06a7\u0001"+
		"\u0000\u0000\u0000\u06a8\u06a9\u0001\u0000\u0000\u0000\u06a9\u06ab\u0001"+
		"\u0000\u0000\u0000\u06aa\u06a6\u0001\u0000\u0000\u0000\u06aa\u06ab\u0001"+
		"\u0000\u0000\u0000\u06ab\u06ad\u0001\u0000\u0000\u0000\u06ac\u0695\u0001"+
		"\u0000\u0000\u0000\u06ac\u06a2\u0001\u0000\u0000\u0000\u06ad\u011b\u0001"+
		"\u0000\u0000\u0000\u06ae\u06b0\u0003\n\u0005\u0000\u06af\u06ae\u0001\u0000"+
		"\u0000\u0000\u06af\u06b0\u0001\u0000\u0000\u0000\u06b0\u06b1\u0001\u0000"+
		"\u0000\u0000\u06b1\u06b2\u0003\u0116\u008b\u0000\u06b2\u011d\u0001\u0000"+
		"\u0000\u0000\u06b3\u06b4\u0005&\u0000\u0000\u06b4\u011f\u0001\u0000\u0000"+
		"\u0000\u06b5\u06b6\u0007\u000f\u0000\u0000\u06b6\u0121\u0001\u0000\u0000"+
		"\u0000\u06b7\u06bc\u0003\u0124\u0092\u0000\u06b8\u06b9\u0003\u013a\u009d"+
		"\u0000\u06b9\u06ba\u0005~\u0000\u0000\u06ba\u06bc\u0001\u0000\u0000\u0000"+
		"\u06bb\u06b7\u0001\u0000\u0000\u0000\u06bb\u06b8\u0001\u0000\u0000\u0000"+
		"\u06bc\u06bd\u0001\u0000\u0000\u0000\u06bd\u06bb\u0001\u0000\u0000\u0000"+
		"\u06bd\u06be\u0001\u0000\u0000\u0000\u06be\u0123\u0001\u0000\u0000\u0000"+
		"\u06bf\u06c1\u0003\u00ccf\u0000\u06c0\u06bf\u0001\u0000\u0000\u0000\u06c0"+
		"\u06c1\u0001\u0000\u0000\u0000\u06c1\u06c3\u0001\u0000\u0000\u0000\u06c2"+
		"\u06c4\u0003\u008aE\u0000\u06c3\u06c2\u0001\u0000\u0000\u0000\u06c3\u06c4"+
		"\u0001\u0000\u0000\u0000\u06c4\u06c6\u0001\u0000\u0000\u0000\u06c5\u06c7"+
		"\u0003\u0126\u0093\u0000\u06c6\u06c5\u0001\u0000\u0000\u0000\u06c6\u06c7"+
		"\u0001\u0000\u0000\u0000\u06c7\u06c8\u0001\u0000\u0000\u0000\u06c8\u06d0"+
		"\u0005\u0080\u0000\u0000\u06c9\u06d0\u0003\u0108\u0084\u0000\u06ca\u06d0"+
		"\u0003\u00c4b\u0000\u06cb\u06d0\u0003\u0082A\u0000\u06cc\u06d0\u0003\u014e"+
		"\u00a7\u0000\u06cd\u06d0\u0003~?\u0000\u06ce\u06d0\u0003\u0084B\u0000"+
		"\u06cf\u06c0\u0001\u0000\u0000\u0000\u06cf\u06c9\u0001\u0000\u0000\u0000"+
		"\u06cf\u06ca\u0001\u0000\u0000\u0000\u06cf\u06cb\u0001\u0000\u0000\u0000"+
		"\u06cf\u06cc\u0001\u0000\u0000\u0000\u06cf\u06cd\u0001\u0000\u0000\u0000"+
		"\u06cf\u06ce\u0001\u0000\u0000\u0000\u06d0\u0125\u0001\u0000\u0000\u0000"+
		"\u06d1\u06d6\u0003\u0128\u0094\u0000\u06d2\u06d3\u0005z\u0000\u0000\u06d3"+
		"\u06d5\u0003\u0128\u0094\u0000\u06d4\u06d2\u0001\u0000\u0000\u0000\u06d5"+
		"\u06d8\u0001\u0000\u0000\u0000\u06d6\u06d4\u0001\u0000\u0000\u0000\u06d6"+
		"\u06d7\u0001\u0000\u0000\u0000\u06d7\u0127\u0001\u0000\u0000\u0000\u06d8"+
		"\u06d6\u0001\u0000\u0000\u0000\u06d9\u06e3\u0003\u00e2q\u0000\u06da\u06dc"+
		"\u0003\u012a\u0095\u0000\u06db\u06da\u0001\u0000\u0000\u0000\u06db\u06dc"+
		"\u0001\u0000\u0000\u0000\u06dc\u06de\u0001\u0000\u0000\u0000\u06dd\u06df"+
		"\u0003\u012e\u0097\u0000\u06de\u06dd\u0001\u0000\u0000\u0000\u06de\u06df"+
		"\u0001\u0000\u0000\u0000\u06df\u06e4\u0001\u0000\u0000\u0000\u06e0\u06e2"+
		"\u0003\u010e\u0087\u0000\u06e1\u06e0\u0001\u0000\u0000\u0000\u06e1\u06e2"+
		"\u0001\u0000\u0000\u0000\u06e2\u06e4\u0001\u0000\u0000\u0000\u06e3\u06db"+
		"\u0001\u0000\u0000\u0000\u06e3\u06e1\u0001\u0000\u0000\u0000\u06e4\u06ee"+
		"\u0001\u0000\u0000\u0000\u06e5\u06e7\u0005\u0084\u0000\u0000\u06e6\u06e5"+
		"\u0001\u0000\u0000\u0000\u06e6\u06e7\u0001\u0000\u0000\u0000\u06e7\u06e9"+
		"\u0001\u0000\u0000\u0000\u06e8\u06ea\u0003\u00ccf\u0000\u06e9\u06e8\u0001"+
		"\u0000\u0000\u0000\u06e9\u06ea\u0001\u0000\u0000\u0000\u06ea\u06eb\u0001"+
		"\u0000\u0000\u0000\u06eb\u06ec\u0005~\u0000\u0000\u06ec\u06ee\u0003\\"+
		".\u0000\u06ed\u06d9\u0001\u0000\u0000\u0000\u06ed\u06e6\u0001\u0000\u0000"+
		"\u0000\u06ee\u0129\u0001\u0000\u0000\u0000\u06ef\u06f1\u0003\u012c\u0096"+
		"\u0000\u06f0\u06ef\u0001\u0000\u0000\u0000\u06f1\u06f2\u0001\u0000\u0000"+
		"\u0000\u06f2\u06f0\u0001\u0000\u0000\u0000\u06f2\u06f3\u0001\u0000\u0000"+
		"\u0000\u06f3\u012b\u0001\u0000\u0000\u0000\u06f4\u06f5\u0007\u0014\u0000"+
		"\u0000\u06f5\u012d\u0001\u0000\u0000\u0000\u06f6\u06f7\u0005e\u0000\u0000"+
		"\u06f7\u06f8\u0005\u0086\u0000\u0000\u06f8\u06f9\u0006\u0097\uffff\uffff"+
		"\u0000\u06f9\u012f\u0001\u0000\u0000\u0000\u06fa\u06fb\u0005~\u0000\u0000"+
		"\u06fb\u06fc\u0003\u0132\u0099\u0000\u06fc\u0131\u0001\u0000\u0000\u0000"+
		"\u06fd\u06ff\u0003\u0134\u009a\u0000\u06fe\u0700\u0005\u0083\u0000\u0000"+
		"\u06ff\u06fe\u0001\u0000\u0000\u0000\u06ff\u0700\u0001\u0000\u0000\u0000"+
		"\u0700\u0708\u0001\u0000\u0000\u0000\u0701\u0702\u0005z\u0000\u0000\u0702"+
		"\u0704\u0003\u0134\u009a\u0000\u0703\u0705\u0005\u0083\u0000\u0000\u0704"+
		"\u0703\u0001\u0000\u0000\u0000\u0704\u0705\u0001\u0000\u0000\u0000\u0705"+
		"\u0707\u0001\u0000\u0000\u0000\u0706\u0701\u0001\u0000\u0000\u0000\u0707"+
		"\u070a\u0001\u0000\u0000\u0000\u0708\u0706\u0001\u0000\u0000\u0000\u0708"+
		"\u0709\u0001\u0000\u0000\u0000\u0709\u0133\u0001\u0000\u0000\u0000\u070a"+
		"\u0708\u0001\u0000\u0000\u0000\u070b\u070d\u0003\u00ccf\u0000\u070c\u070b"+
		"\u0001\u0000\u0000\u0000\u070c\u070d\u0001\u0000\u0000\u0000\u070d\u071a"+
		"\u0001\u0000\u0000\u0000\u070e\u071b\u0003\u0138\u009c\u0000\u070f\u0711"+
		"\u0005P\u0000\u0000\u0710\u0712\u0003\u013a\u009d\u0000\u0711\u0710\u0001"+
		"\u0000\u0000\u0000\u0711\u0712\u0001\u0000\u0000\u0000\u0712\u0713\u0001"+
		"\u0000\u0000\u0000\u0713\u071b\u0003\u0138\u009c\u0000\u0714\u0716\u0003"+
		"\u013a\u009d\u0000\u0715\u0717\u0005P\u0000\u0000\u0716\u0715\u0001\u0000"+
		"\u0000\u0000\u0716\u0717\u0001\u0000\u0000\u0000\u0717\u0718\u0001\u0000"+
		"\u0000\u0000\u0718\u0719\u0003\u0138\u009c\u0000\u0719\u071b\u0001\u0000"+
		"\u0000\u0000\u071a\u070e\u0001\u0000\u0000\u0000\u071a\u070f\u0001\u0000"+
		"\u0000\u0000\u071a\u0714\u0001\u0000\u0000\u0000\u071b\u0135\u0001\u0000"+
		"\u0000\u0000\u071c\u071e\u0003\n\u0005\u0000\u071d\u071c\u0001\u0000\u0000"+
		"\u0000\u071d\u071e\u0001\u0000\u0000\u0000\u071e\u071f\u0001\u0000\u0000"+
		"\u0000\u071f\u0722\u0003\u0116\u008b\u0000\u0720\u0722\u0003\u00a2Q\u0000"+
		"\u0721\u071d\u0001\u0000\u0000\u0000\u0721\u0720\u0001\u0000\u0000\u0000"+
		"\u0722\u0137\u0001\u0000\u0000\u0000\u0723\u0724\u0003\u0136\u009b\u0000"+
		"\u0724\u0139\u0001\u0000\u0000\u0000\u0725\u0726\u0007\u0015\u0000\u0000"+
		"\u0726\u013b\u0001\u0000\u0000\u0000\u0727\u0728\u00054\u0000\u0000\u0728"+
		"\u0729\u0003\u013e\u009f\u0000\u0729\u013d\u0001\u0000\u0000\u0000\u072a"+
		"\u072c\u0003\u0096K\u0000\u072b\u072d\u0003\u0140\u00a0\u0000\u072c\u072b"+
		"\u0001\u0000\u0000\u0000\u072c\u072d\u0001\u0000\u0000\u0000\u072d\u013f"+
		"\u0001\u0000\u0000\u0000\u072e\u0730\u0003\u00ecv\u0000\u072f\u0731\u0003"+
		"\u0140\u00a0\u0000\u0730\u072f\u0001\u0000\u0000\u0000\u0730\u0731\u0001"+
		"\u0000\u0000\u0000\u0731\u0141\u0001\u0000\u0000\u0000\u0732\u0733\u0005"+
		"~\u0000\u0000\u0733\u0734\u0003\u0144\u00a2\u0000\u0734\u0143\u0001\u0000"+
		"\u0000\u0000\u0735\u0737\u0003\u0146\u00a3\u0000\u0736\u0738\u0005\u0083"+
		"\u0000\u0000\u0737\u0736\u0001\u0000\u0000\u0000\u0737\u0738\u0001\u0000"+
		"\u0000\u0000\u0738\u0740\u0001\u0000\u0000\u0000\u0739\u073a\u0005z\u0000"+
		"\u0000\u073a\u073c\u0003\u0146\u00a3\u0000\u073b\u073d\u0005\u0083\u0000"+
		"\u0000\u073c\u073b\u0001\u0000\u0000\u0000\u073c\u073d\u0001\u0000\u0000"+
		"\u0000\u073d\u073f\u0001\u0000\u0000\u0000\u073e\u0739\u0001\u0000\u0000"+
		"\u0000\u073f\u0742\u0001\u0000\u0000\u0000\u0740\u073e\u0001\u0000\u0000"+
		"\u0000\u0740\u0741\u0001\u0000\u0000\u0000\u0741\u0145\u0001\u0000\u0000"+
		"\u0000\u0742\u0740\u0001\u0000\u0000\u0000\u0743\u074a\u0003\u0148\u00a4"+
		"\u0000\u0744\u0746\u0005U\u0000\u0000\u0745\u0747\u0003\"\u0011\u0000"+
		"\u0746\u0745\u0001\u0000\u0000\u0000\u0746\u0747\u0001\u0000\u0000\u0000"+
		"\u0747\u0748\u0001\u0000\u0000\u0000\u0748\u074b\u0005V\u0000\u0000\u0749"+
		"\u074b\u0003\u0114\u008a\u0000\u074a\u0744\u0001\u0000\u0000\u0000\u074a"+
		"\u0749\u0001\u0000\u0000\u0000\u074b\u0147\u0001\u0000\u0000\u0000\u074c"+
		"\u074f\u0003\u0136\u009b\u0000\u074d\u074f\u0005\u0084\u0000\u0000\u074e"+
		"\u074c\u0001\u0000\u0000\u0000\u074e\u074d\u0001\u0000\u0000\u0000\u074f"+
		"\u0149\u0001\u0000\u0000\u0000\u0750\u0751\u00054\u0000\u0000\u0751\u0752"+
		"\u0003\u017a\u00bd\u0000\u0752\u014b\u0001\u0000\u0000\u0000\u0753\u0757"+
		"\u00054\u0000\u0000\u0754\u0755\u0005\u0004\u0000\u0000\u0755\u0758\u0005"+
		"\u0084\u0000\u0000\u0756\u0758\u0005\u008c\u0000\u0000\u0757\u0754\u0001"+
		"\u0000\u0000\u0000\u0757\u0756\u0001\u0000\u0000\u0000\u0758\u014d\u0001"+
		"\u0000\u0000\u0000\u0759\u075a\u0005D\u0000\u0000\u075a\u075b\u0005f\u0000"+
		"\u0000\u075b\u075c\u0003\u0150\u00a8\u0000\u075c\u075d\u0005g\u0000\u0000"+
		"\u075d\u075e\u0003z=\u0000\u075e\u014f\u0001\u0000\u0000\u0000\u075f\u0764"+
		"\u0003\u0152\u00a9\u0000\u0760\u0761\u0005z\u0000\u0000\u0761\u0763\u0003"+
		"\u0152\u00a9\u0000\u0762\u0760\u0001\u0000\u0000\u0000\u0763\u0766\u0001"+
		"\u0000\u0000\u0000\u0764\u0762\u0001\u0000\u0000\u0000\u0764\u0765\u0001"+
		"\u0000\u0000\u0000\u0765\u0151\u0001\u0000\u0000\u0000\u0766\u0764\u0001"+
		"\u0000\u0000\u0000\u0767\u076a\u0003\u0154\u00aa\u0000\u0768\u076a\u0003"+
		"\u0106\u0083\u0000\u0769\u0767\u0001\u0000\u0000\u0000\u0769\u0768\u0001"+
		"\u0000\u0000\u0000\u076a\u0153\u0001\u0000\u0000\u0000\u076b\u076c\u0005"+
		"D\u0000\u0000\u076c\u076d\u0005f\u0000\u0000\u076d\u076e\u0003\u0150\u00a8"+
		"\u0000\u076e\u076f\u0005g\u0000\u0000\u076f\u0771\u0001\u0000\u0000\u0000"+
		"\u0770\u076b\u0001\u0000\u0000\u0000\u0770\u0771\u0001\u0000\u0000\u0000"+
		"\u0771\u0772\u0001\u0000\u0000\u0000\u0772\u0775\u0005\u0015\u0000\u0000"+
		"\u0773\u0775\u0005L\u0000\u0000\u0774\u0770\u0001\u0000\u0000\u0000\u0774"+
		"\u0773\u0001\u0000\u0000\u0000\u0775\u0781\u0001\u0000\u0000\u0000\u0776"+
		"\u0778\u0005\u0083\u0000\u0000\u0777\u0776\u0001\u0000\u0000\u0000\u0777"+
		"\u0778\u0001\u0000\u0000\u0000\u0778\u077a\u0001\u0000\u0000\u0000\u0779"+
		"\u077b\u0005\u0084\u0000\u0000\u077a\u0779\u0001\u0000\u0000\u0000\u077a"+
		"\u077b\u0001\u0000\u0000\u0000\u077b\u0782\u0001\u0000\u0000\u0000\u077c"+
		"\u077e\u0005\u0084\u0000\u0000\u077d\u077c\u0001\u0000\u0000\u0000\u077d"+
		"\u077e\u0001\u0000\u0000\u0000\u077e\u077f\u0001\u0000\u0000\u0000\u077f"+
		"\u0780\u0005e\u0000\u0000\u0780\u0782\u0003\u00f6{\u0000\u0781\u0777\u0001"+
		"\u0000\u0000\u0000\u0781\u077d\u0001\u0000\u0000\u0000\u0782\u0155\u0001"+
		"\u0000\u0000\u0000\u0783\u0784\u0003\u015a\u00ad\u0000\u0784\u0786\u0005"+
		"f\u0000\u0000\u0785\u0787\u0003\u015c\u00ae\u0000\u0786\u0785\u0001\u0000"+
		"\u0000\u0000\u0786\u0787\u0001\u0000\u0000\u0000\u0787\u0788\u0001\u0000"+
		"\u0000\u0000\u0788\u0789\u0005g\u0000\u0000\u0789\u0157\u0001\u0000\u0000"+
		"\u0000\u078a\u0796\u0003\u0156\u00ab\u0000\u078b\u078e\u0003\u014a\u00a5"+
		"\u0000\u078c\u078e\u0003\u014c\u00a6\u0000\u078d\u078b\u0001\u0000\u0000"+
		"\u0000\u078d\u078c\u0001\u0000\u0000\u0000\u078e\u078f\u0001\u0000\u0000"+
		"\u0000\u078f\u0791\u0005f\u0000\u0000\u0790\u0792\u0003\u015c\u00ae\u0000"+
		"\u0791\u0790\u0001\u0000\u0000\u0000\u0791\u0792\u0001\u0000\u0000\u0000"+
		"\u0792\u0793\u0001\u0000\u0000\u0000\u0793\u0794\u0005g\u0000\u0000\u0794"+
		"\u0796\u0001\u0000\u0000\u0000\u0795\u078a\u0001\u0000\u0000\u0000\u0795"+
		"\u078d\u0001\u0000\u0000\u0000\u0796\u0159\u0001\u0000\u0000\u0000\u0797"+
		"\u0798\u0005\u0084\u0000\u0000\u0798\u015b\u0001\u0000\u0000\u0000\u0799"+
		"\u079b\u0003\u015e\u00af\u0000\u079a\u079c\u0005\u0083\u0000\u0000\u079b"+
		"\u079a\u0001\u0000\u0000\u0000\u079b\u079c\u0001\u0000\u0000\u0000\u079c"+
		"\u07a4\u0001\u0000\u0000\u0000\u079d\u079e\u0005z\u0000\u0000\u079e\u07a0"+
		"\u0003\u015e\u00af\u0000\u079f\u07a1\u0005\u0083\u0000\u0000\u07a0\u079f"+
		"\u0001\u0000\u0000\u0000\u07a0\u07a1\u0001\u0000\u0000\u0000\u07a1\u07a3"+
		"\u0001\u0000\u0000\u0000\u07a2\u079d\u0001\u0000\u0000\u0000\u07a3\u07a6"+
		"\u0001\u0000\u0000\u0000\u07a4\u07a2\u0001\u0000\u0000\u0000\u07a4\u07a5"+
		"\u0001\u0000\u0000\u0000\u07a5\u015d\u0001\u0000\u0000\u0000\u07a6\u07a4"+
		"\u0001\u0000\u0000\u0000\u07a7\u07ab\u0003\u00f6{\u0000\u07a8\u07ab\u0003"+
		"\\.\u0000\u07a9\u07ab\u0003\u0004\u0002\u0000\u07aa\u07a7\u0001\u0000"+
		"\u0000\u0000\u07aa\u07a8\u0001\u0000\u0000\u0000\u07aa\u07a9\u0001\u0000"+
		"\u0000\u0000\u07ab\u015f\u0001\u0000\u0000\u0000\u07ac\u07ad\u0005L\u0000"+
		"\u0000\u07ad\u07b3\u0003\n\u0005\u0000\u07ae\u07b4\u0005\u0084\u0000\u0000"+
		"\u07af\u07b1\u0005D\u0000\u0000\u07b0\u07af\u0001\u0000\u0000\u0000\u07b0"+
		"\u07b1\u0001\u0000\u0000\u0000\u07b1\u07b2\u0001\u0000\u0000\u0000\u07b2"+
		"\u07b4\u0003\u0156\u00ab\u0000\u07b3\u07ae\u0001\u0000\u0000\u0000\u07b3"+
		"\u07b0\u0001\u0000\u0000\u0000\u07b4\u0161\u0001\u0000\u0000\u0000\u07b5"+
		"\u07b7\u0005$\u0000\u0000\u07b6\u07b5\u0001\u0000\u0000\u0000\u07b6\u07b7"+
		"\u0001\u0000\u0000\u0000\u07b7\u07b8\u0001\u0000\u0000\u0000\u07b8\u07b9"+
		"\u0005D\u0000\u0000\u07b9\u07ba\u0003z=\u0000\u07ba\u0163\u0001\u0000"+
		"\u0000\u0000\u07bb\u07bc\u0005D\u0000\u0000\u07bc\u07bd\u0005f\u0000\u0000"+
		"\u07bd\u07be\u0005g\u0000\u0000\u07be\u07bf\u0003z=\u0000\u07bf\u0165"+
		"\u0001\u0000\u0000\u0000\u07c0\u07c1\u0005I\u0000\u0000\u07c1\u07c2\u0003"+
		"d2\u0000\u07c2\u07c3\u0003\u016a\u00b5\u0000\u07c3\u0167\u0001\u0000\u0000"+
		"\u0000\u07c4\u07c6\u0005I\u0000\u0000\u07c5\u07c7\u0003\u0142\u00a1\u0000"+
		"\u07c6\u07c5\u0001\u0000\u0000\u0000\u07c6\u07c7\u0001\u0000\u0000\u0000"+
		"\u07c7\u07c8\u0001\u0000\u0000\u0000\u07c8\u07c9\u0003d2\u0000\u07c9\u07ca"+
		"\u0003\u016a\u00b5\u0000\u07ca\u0169\u0001\u0000\u0000\u0000\u07cb\u07cd"+
		"\u0003\u016c\u00b6\u0000\u07cc\u07cb\u0001\u0000\u0000\u0000\u07cd\u07ce"+
		"\u0001\u0000\u0000\u0000\u07ce\u07cc\u0001\u0000\u0000\u0000\u07ce\u07cf"+
		"\u0001\u0000\u0000\u0000\u07cf\u016b\u0001\u0000\u0000\u0000\u07d0\u07d1"+
		"\u0005\u0011\u0000\u0000\u07d1\u07d2\u0005U\u0000\u0000\u07d2\u07d3\u0003"+
		"\u016e\u00b7\u0000\u07d3\u07d4\u0005V\u0000\u0000\u07d4\u07d5\u0003d2"+
		"\u0000\u07d5\u016d\u0001\u0000\u0000\u0000\u07d6\u07d8\u0003\u00ccf\u0000"+
		"\u07d7\u07d6\u0001\u0000\u0000\u0000\u07d7\u07d8\u0001\u0000\u0000\u0000"+
		"\u07d8\u07d9\u0001\u0000\u0000\u0000\u07d9\u07dc\u0003\u0096K\u0000\u07da"+
		"\u07dd\u0003\u00e2q\u0000\u07db\u07dd\u0003\u00f8|\u0000\u07dc\u07da\u0001"+
		"\u0000\u0000\u0000\u07dc\u07db\u0001\u0000\u0000\u0000\u07dc\u07dd\u0001"+
		"\u0000\u0000\u0000\u07dd\u07e0\u0001\u0000\u0000\u0000\u07de\u07e0\u0005"+
		"\u0083\u0000\u0000\u07df\u07d7\u0001\u0000\u0000\u0000\u07df\u07de\u0001"+
		"\u0000\u0000\u0000\u07e0\u016f\u0001\u0000\u0000\u0000\u07e1\u07e3\u0005"+
		"G\u0000\u0000\u07e2\u07e4\u0003V+\u0000\u07e3\u07e2\u0001\u0000\u0000"+
		"\u0000\u07e3\u07e4\u0001\u0000\u0000\u0000\u07e4\u0171\u0001\u0000\u0000"+
		"\u0000\u07e5\u07e8\u0003\u0174\u00ba\u0000\u07e6\u07e8\u0003\u0178\u00bc"+
		"\u0000\u07e7\u07e5\u0001\u0000\u0000\u0000\u07e7\u07e6\u0001\u0000\u0000"+
		"\u0000\u07e8\u0173\u0001\u0000\u0000\u0000\u07e9\u07ea\u0005G\u0000\u0000"+
		"\u07ea\u07ec\u0005U\u0000\u0000\u07eb\u07ed\u0003\u0176\u00bb\u0000\u07ec"+
		"\u07eb\u0001\u0000\u0000\u0000\u07ec\u07ed\u0001\u0000\u0000\u0000\u07ed"+
		"\u07ee\u0001\u0000\u0000\u0000\u07ee\u07ef\u0005V\u0000\u0000\u07ef\u0175"+
		"\u0001\u0000\u0000\u0000\u07f0\u07f2\u0003\u00f6{\u0000\u07f1\u07f3\u0005"+
		"\u0083\u0000\u0000\u07f2\u07f1\u0001\u0000\u0000\u0000\u07f2\u07f3\u0001"+
		"\u0000\u0000\u0000\u07f3\u07fb\u0001\u0000\u0000\u0000\u07f4\u07f5\u0005"+
		"z\u0000\u0000\u07f5\u07f7\u0003\u00f6{\u0000\u07f6\u07f8\u0005\u0083\u0000"+
		"\u0000\u07f7\u07f6\u0001\u0000\u0000\u0000\u07f7\u07f8\u0001\u0000\u0000"+
		"\u0000\u07f8\u07fa\u0001\u0000\u0000\u0000\u07f9\u07f4\u0001\u0000\u0000"+
		"\u0000\u07fa\u07fd\u0001\u0000\u0000\u0000\u07fb\u07f9\u0001\u0000\u0000"+
		"\u0000\u07fb\u07fc\u0001\u0000\u0000\u0000\u07fc\u0177\u0001\u0000\u0000"+
		"\u0000\u07fd\u07fb\u0001\u0000\u0000\u0000\u07fe\u07ff\u00052\u0000\u0000"+
		"\u07ff\u0800\u0005U\u0000\u0000\u0800\u0801\u0003\\.\u0000\u0801\u0802"+
		"\u0005V\u0000\u0000\u0802\u0805\u0001\u0000\u0000\u0000\u0803\u0805\u0005"+
		"2\u0000\u0000\u0804\u07fe\u0001\u0000\u0000\u0000\u0804\u0803\u0001\u0000"+
		"\u0000\u0000\u0805\u0179\u0001\u0000\u0000\u0000\u0806\u0809\u00051\u0000"+
		"\u0000\u0807\u0808\u0005W\u0000\u0000\u0808\u080a\u0005X\u0000\u0000\u0809"+
		"\u0807\u0001\u0000\u0000\u0000\u0809\u080a\u0001\u0000\u0000\u0000\u080a"+
		"\u083a\u0001\u0000\u0000\u0000\u080b\u080e\u0005\u001c\u0000\u0000\u080c"+
		"\u080d\u0005W\u0000\u0000\u080d\u080f\u0005X\u0000\u0000\u080e\u080c\u0001"+
		"\u0000\u0000\u0000\u080e\u080f\u0001\u0000\u0000\u0000\u080f\u083a\u0001"+
		"\u0000\u0000\u0000\u0810\u083a\u0005[\u0000\u0000\u0811\u083a\u0005\\"+
		"\u0000\u0000\u0812\u083a\u0005]\u0000\u0000\u0813\u083a\u0005^\u0000\u0000"+
		"\u0814\u083a\u0005_\u0000\u0000\u0815\u083a\u0005`\u0000\u0000\u0816\u083a"+
		"\u0005a\u0000\u0000\u0817\u083a\u0005b\u0000\u0000\u0818\u083a\u0005c"+
		"\u0000\u0000\u0819\u083a\u0005d\u0000\u0000\u081a\u083a\u0005e\u0000\u0000"+
		"\u081b\u083a\u0005g\u0000\u0000\u081c\u083a\u0005f\u0000\u0000\u081d\u083a"+
		"\u0005u\u0000\u0000\u081e\u083a\u0005h\u0000\u0000\u081f\u083a\u0005i"+
		"\u0000\u0000\u0820\u083a\u0005j\u0000\u0000\u0821\u083a\u0005l\u0000\u0000"+
		"\u0822\u083a\u0005m\u0000\u0000\u0823\u083a\u0005n\u0000\u0000\u0824\u083a"+
		"\u0005o\u0000\u0000\u0825\u0826\u0005f\u0000\u0000\u0826\u083a\u0005f"+
		"\u0000\u0000\u0827\u0828\u0005g\u0000\u0000\u0828\u083a\u0005g\u0000\u0000"+
		"\u0829\u083a\u0005q\u0000\u0000\u082a\u083a\u0005p\u0000\u0000\u082b\u083a"+
		"\u0005r\u0000\u0000\u082c\u083a\u0005s\u0000\u0000\u082d\u083a\u0005t"+
		"\u0000\u0000\u082e\u083a\u0005v\u0000\u0000\u082f\u083a\u0005w\u0000\u0000"+
		"\u0830\u083a\u0005x\u0000\u0000\u0831\u083a\u0005y\u0000\u0000\u0832\u083a"+
		"\u0005z\u0000\u0000\u0833\u083a\u0005{\u0000\u0000\u0834\u083a\u0005|"+
		"\u0000\u0000\u0835\u0836\u0005U\u0000\u0000\u0836\u083a\u0005V\u0000\u0000"+
		"\u0837\u0838\u0005W\u0000\u0000\u0838\u083a\u0005X\u0000\u0000\u0839\u0806"+
		"\u0001\u0000\u0000\u0000\u0839\u080b\u0001\u0000\u0000\u0000\u0839\u0810"+
		"\u0001\u0000\u0000\u0000\u0839\u0811\u0001\u0000\u0000\u0000\u0839\u0812"+
		"\u0001\u0000\u0000\u0000\u0839\u0813\u0001\u0000\u0000\u0000\u0839\u0814"+
		"\u0001\u0000\u0000\u0000\u0839\u0815\u0001\u0000\u0000\u0000\u0839\u0816"+
		"\u0001\u0000\u0000\u0000\u0839\u0817\u0001\u0000\u0000\u0000\u0839\u0818"+
		"\u0001\u0000\u0000\u0000\u0839\u0819\u0001\u0000\u0000\u0000\u0839\u081a"+
		"\u0001\u0000\u0000\u0000\u0839\u081b\u0001\u0000\u0000\u0000\u0839\u081c"+
		"\u0001\u0000\u0000\u0000\u0839\u081d\u0001\u0000\u0000\u0000\u0839\u081e"+
		"\u0001\u0000\u0000\u0000\u0839\u081f\u0001\u0000\u0000\u0000\u0839\u0820"+
		"\u0001\u0000\u0000\u0000\u0839\u0821\u0001\u0000\u0000\u0000\u0839\u0822"+
		"\u0001\u0000\u0000\u0000\u0839\u0823\u0001\u0000\u0000\u0000\u0839\u0824"+
		"\u0001\u0000\u0000\u0000\u0839\u0825\u0001\u0000\u0000\u0000\u0839\u0827"+
		"\u0001\u0000\u0000\u0000\u0839\u0829\u0001\u0000\u0000\u0000\u0839\u082a"+
		"\u0001\u0000\u0000\u0000\u0839\u082b\u0001\u0000\u0000\u0000\u0839\u082c"+
		"\u0001\u0000\u0000\u0000\u0839\u082d\u0001\u0000\u0000\u0000\u0839\u082e"+
		"\u0001\u0000\u0000\u0000\u0839\u082f\u0001\u0000\u0000\u0000\u0839\u0830"+
		"\u0001\u0000\u0000\u0000\u0839\u0831\u0001\u0000\u0000\u0000\u0839\u0832"+
		"\u0001\u0000\u0000\u0000\u0839\u0833\u0001\u0000\u0000\u0000\u0839\u0834"+
		"\u0001\u0000\u0000\u0000\u0839\u0835\u0001\u0000\u0000\u0000\u0839\u0837"+
		"\u0001\u0000\u0000\u0000\u083a\u017b\u0001\u0000\u0000\u0000\u083b\u083c"+
		"\u0007\u0016\u0000\u0000\u083c\u017d\u0001\u0000\u0000\u0000\u0132\u017f"+
		"\u0186\u018f\u0193\u019c\u019f\u01a3\u01ab\u01b2\u01b5\u01ba\u01bf\u01c5"+
		"\u01cd\u01cf\u01d8\u01dc\u01e0\u01e3\u01e7\u01ea\u01f1\u01f5\u01f8\u01fb"+
		"\u01fe\u0204\u0208\u020c\u021a\u021e\u0224\u022b\u0231\u0235\u0239\u023b"+
		"\u0243\u0248\u0255\u025c\u0268\u0272\u0277\u027b\u0282\u0285\u028d\u0291"+
		"\u0294\u029b\u02a2\u02a6\u02ab\u02af\u02b2\u02b7\u02c6\u02cd\u02d5\u02dd"+
		"\u02e6\u02ed\u02f4\u02fc\u0304\u030c\u0314\u031c\u0324\u032d\u0335\u033e"+
		"\u0346\u034e\u0350\u0353\u0359\u035f\u0365\u036c\u0375\u037d\u0381\u0388"+
		"\u038a\u039e\u03a2\u03a8\u03ad\u03b1\u03b4\u03bb\u03c2\u03c6\u03cf\u03da"+
		"\u03e4\u03e9\u03f0\u03f3\u03f8\u03fd\u0412\u0417\u041a\u0425\u042b\u0430"+
		"\u0433\u0438\u043b\u0442\u044b\u0450\u0453\u0457\u045b\u045f\u0464\u0469"+
		"\u046f\u0475\u047b\u0481\u0487\u048a\u0490\u0494\u0498\u049b\u04a3\u04a5"+
		"\u04ab\u04ae\u04b1\u04b4\u04b8\u04bc\u04c2\u04cc\u04d2\u04d8\u04dd\u04e2"+
		"\u04e6\u04f3\u04f9\u04fd\u0503\u0508\u0517\u051b\u0520\u0525\u052a\u0530"+
		"\u0533\u053c\u0540\u0545\u0549\u054f\u0556\u0567\u0569\u0570\u0575\u057c"+
		"\u0580\u0584\u058c\u0592\u0598\u059c\u059e\u05a2\u05a7\u05ab\u05ae\u05b1"+
		"\u05b4\u05b9\u05bd\u05c0\u05c4\u05c7\u05c9\u05ce\u05d5\u05db\u05df\u05e5"+
		"\u05eb\u05ee\u05f0\u05f6\u05fa\u0600\u0607\u060b\u060d\u0611\u0617\u0623"+
		"\u0627\u0629\u062d\u0632\u0635\u063c\u0640\u0645\u0647\u064b\u064e\u0651"+
		"\u0655\u065a\u0661\u0668\u066d\u0671\u0675\u067a\u067e\u0684\u0686\u068c"+
		"\u0691\u0697\u069b\u069d\u06a0\u06a4\u06a8\u06aa\u06ac\u06af\u06bb\u06bd"+
		"\u06c0\u06c3\u06c6\u06cf\u06d6\u06db\u06de\u06e1\u06e3\u06e6\u06e9\u06ed"+
		"\u06f2\u06ff\u0704\u0708\u070c\u0711\u0716\u071a\u071d\u0721\u072c\u0730"+
		"\u0737\u073c\u0740\u0746\u074a\u074e\u0757\u0764\u0769\u0770\u0774\u0777"+
		"\u077a\u077d\u0781\u0786\u078d\u0791\u0795\u079b\u07a0\u07a4\u07aa\u07b0"+
		"\u07b3\u07b6\u07c6\u07ce\u07d7\u07dc\u07df\u07e3\u07e7\u07ec\u07f2\u07f7"+
		"\u07fb\u0804\u0809\u080e\u0839";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}