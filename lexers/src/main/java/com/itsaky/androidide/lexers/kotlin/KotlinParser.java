// Generated from KotlinParser.g4 by ANTLR 4.9.3
package com.itsaky.androidide.lexers.kotlin;

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
import org.antlr.v4.runtime.misc.Utils;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class KotlinParser extends Parser {
  public static final int ShebangLine = 1,
      DelimitedComment = 2,
      LineComment = 3,
      WS = 4,
      NL = 5,
      RESERVED = 6,
      DOT = 7,
      COMMA = 8,
      LPAREN = 9,
      RPAREN = 10,
      LSQUARE = 11,
      RSQUARE = 12,
      LCURL = 13,
      RCURL = 14,
      MULT = 15,
      MOD = 16,
      DIV = 17,
      ADD = 18,
      SUB = 19,
      INCR = 20,
      DECR = 21,
      CONJ = 22,
      DISJ = 23,
      EXCL_WS = 24,
      EXCL_NO_WS = 25,
      COLON = 26,
      SEMICOLON = 27,
      ASSIGNMENT = 28,
      ADD_ASSIGNMENT = 29,
      SUB_ASSIGNMENT = 30,
      MULT_ASSIGNMENT = 31,
      DIV_ASSIGNMENT = 32,
      MOD_ASSIGNMENT = 33,
      ARROW = 34,
      DOUBLE_ARROW = 35,
      RANGE = 36,
      COLONCOLON = 37,
      DOUBLE_SEMICOLON = 38,
      HASH = 39,
      AT_NO_WS = 40,
      AT_POST_WS = 41,
      AT_PRE_WS = 42,
      AT_BOTH_WS = 43,
      QUEST_WS = 44,
      QUEST_NO_WS = 45,
      LANGLE = 46,
      RANGLE = 47,
      LE = 48,
      GE = 49,
      EXCL_EQ = 50,
      EXCL_EQEQ = 51,
      AS_SAFE = 52,
      EQEQ = 53,
      EQEQEQ = 54,
      SINGLE_QUOTE = 55,
      RETURN_AT = 56,
      CONTINUE_AT = 57,
      BREAK_AT = 58,
      THIS_AT = 59,
      SUPER_AT = 60,
      FILE = 61,
      FIELD = 62,
      PROPERTY = 63,
      GET = 64,
      SET = 65,
      RECEIVER = 66,
      PARAM = 67,
      SETPARAM = 68,
      DELEGATE = 69,
      PACKAGE = 70,
      IMPORT = 71,
      CLASS = 72,
      INTERFACE = 73,
      FUN = 74,
      OBJECT = 75,
      VAL = 76,
      VAR = 77,
      TYPE_ALIAS = 78,
      CONSTRUCTOR = 79,
      BY = 80,
      COMPANION = 81,
      INIT = 82,
      THIS = 83,
      SUPER = 84,
      TYPEOF = 85,
      WHERE = 86,
      IF = 87,
      ELSE = 88,
      WHEN = 89,
      TRY = 90,
      CATCH = 91,
      FINALLY = 92,
      FOR = 93,
      DO = 94,
      WHILE = 95,
      THROW = 96,
      RETURN = 97,
      CONTINUE = 98,
      BREAK = 99,
      AS = 100,
      IS = 101,
      IN = 102,
      NOT_IS = 103,
      NOT_IN = 104,
      OUT = 105,
      DYNAMIC = 106,
      PUBLIC = 107,
      PRIVATE = 108,
      PROTECTED = 109,
      INTERNAL = 110,
      ENUM = 111,
      SEALED = 112,
      ANNOTATION = 113,
      DATA = 114,
      INNER = 115,
      VALUE = 116,
      TAILREC = 117,
      OPERATOR = 118,
      INLINE = 119,
      INFIX = 120,
      EXTERNAL = 121,
      SUSPEND = 122,
      OVERRIDE = 123,
      ABSTRACT = 124,
      FINAL = 125,
      OPEN = 126,
      CONST = 127,
      LATEINIT = 128,
      VARARG = 129,
      NOINLINE = 130,
      CROSSINLINE = 131,
      REIFIED = 132,
      EXPECT = 133,
      ACTUAL = 134,
      RealLiteral = 135,
      FloatLiteral = 136,
      DoubleLiteral = 137,
      IntegerLiteral = 138,
      HexLiteral = 139,
      BinLiteral = 140,
      UnsignedLiteral = 141,
      LongLiteral = 142,
      BooleanLiteral = 143,
      NullLiteral = 144,
      CharacterLiteral = 145,
      Identifier = 146,
      IdentifierOrSoftKey = 147,
      FieldIdentifier = 148,
      QUOTE_OPEN = 149,
      TRIPLE_QUOTE_OPEN = 150,
      UNICODE_CLASS_LL = 151,
      UNICODE_CLASS_LM = 152,
      UNICODE_CLASS_LO = 153,
      UNICODE_CLASS_LT = 154,
      UNICODE_CLASS_LU = 155,
      UNICODE_CLASS_ND = 156,
      UNICODE_CLASS_NL = 157,
      QUOTE_CLOSE = 158,
      LineStrRef = 159,
      LineStrText = 160,
      LineStrEscapedChar = 161,
      LineStrExprStart = 162,
      TRIPLE_QUOTE_CLOSE = 163,
      MultiLineStringQuote = 164,
      MultiLineStrRef = 165,
      MultiLineStrText = 166,
      MultiLineStrExprStart = 167,
      Inside_Comment = 168,
      Inside_WS = 169,
      Inside_NL = 170,
      ErrorCharacter = 171;
  public static final int RULE_kotlinFile = 0,
      RULE_script = 1,
      RULE_shebangLine = 2,
      RULE_fileAnnotation = 3,
      RULE_packageHeader = 4,
      RULE_importList = 5,
      RULE_importHeader = 6,
      RULE_importAlias = 7,
      RULE_topLevelObject = 8,
      RULE_typeAlias = 9,
      RULE_declaration = 10,
      RULE_classDeclaration = 11,
      RULE_primaryConstructor = 12,
      RULE_classBody = 13,
      RULE_classParameters = 14,
      RULE_classParameter = 15,
      RULE_delegationSpecifiers = 16,
      RULE_delegationSpecifier = 17,
      RULE_constructorInvocation = 18,
      RULE_annotatedDelegationSpecifier = 19,
      RULE_explicitDelegation = 20,
      RULE_typeParameters = 21,
      RULE_typeParameter = 22,
      RULE_typeConstraints = 23,
      RULE_typeConstraint = 24,
      RULE_classMemberDeclarations = 25,
      RULE_classMemberDeclaration = 26,
      RULE_anonymousInitializer = 27,
      RULE_companionObject = 28,
      RULE_functionValueParameters = 29,
      RULE_functionValueParameter = 30,
      RULE_functionDeclaration = 31,
      RULE_functionBody = 32,
      RULE_variableDeclaration = 33,
      RULE_multiVariableDeclaration = 34,
      RULE_propertyDeclaration = 35,
      RULE_propertyDelegate = 36,
      RULE_getter = 37,
      RULE_setter = 38,
      RULE_parametersWithOptionalType = 39,
      RULE_functionValueParameterWithOptionalType = 40,
      RULE_parameterWithOptionalType = 41,
      RULE_parameter = 42,
      RULE_objectDeclaration = 43,
      RULE_secondaryConstructor = 44,
      RULE_constructorDelegationCall = 45,
      RULE_enumClassBody = 46,
      RULE_enumEntries = 47,
      RULE_enumEntry = 48,
      RULE_type = 49,
      RULE_typeReference = 50,
      RULE_nullableType = 51,
      RULE_quest = 52,
      RULE_userType = 53,
      RULE_simpleUserType = 54,
      RULE_typeProjection = 55,
      RULE_typeProjectionModifiers = 56,
      RULE_typeProjectionModifier = 57,
      RULE_functionType = 58,
      RULE_functionTypeParameters = 59,
      RULE_parenthesizedType = 60,
      RULE_receiverType = 61,
      RULE_parenthesizedUserType = 62,
      RULE_statements = 63,
      RULE_statement = 64,
      RULE_label = 65,
      RULE_controlStructureBody = 66,
      RULE_block = 67,
      RULE_loopStatement = 68,
      RULE_forStatement = 69,
      RULE_whileStatement = 70,
      RULE_doWhileStatement = 71,
      RULE_assignment = 72,
      RULE_semi = 73,
      RULE_semis = 74,
      RULE_expression = 75,
      RULE_disjunction = 76,
      RULE_conjunction = 77,
      RULE_equality = 78,
      RULE_comparison = 79,
      RULE_genericCallLikeComparison = 80,
      RULE_infixOperation = 81,
      RULE_elvisExpression = 82,
      RULE_elvis = 83,
      RULE_infixFunctionCall = 84,
      RULE_rangeExpression = 85,
      RULE_additiveExpression = 86,
      RULE_multiplicativeExpression = 87,
      RULE_asExpression = 88,
      RULE_prefixUnaryExpression = 89,
      RULE_unaryPrefix = 90,
      RULE_postfixUnaryExpression = 91,
      RULE_postfixUnarySuffix = 92,
      RULE_directlyAssignableExpression = 93,
      RULE_parenthesizedDirectlyAssignableExpression = 94,
      RULE_assignableExpression = 95,
      RULE_parenthesizedAssignableExpression = 96,
      RULE_assignableSuffix = 97,
      RULE_indexingSuffix = 98,
      RULE_navigationSuffix = 99,
      RULE_callSuffix = 100,
      RULE_annotatedLambda = 101,
      RULE_typeArguments = 102,
      RULE_valueArguments = 103,
      RULE_valueArgument = 104,
      RULE_primaryExpression = 105,
      RULE_parenthesizedExpression = 106,
      RULE_collectionLiteral = 107,
      RULE_literalConstant = 108,
      RULE_stringLiteral = 109,
      RULE_lineStringLiteral = 110,
      RULE_multiLineStringLiteral = 111,
      RULE_lineStringContent = 112,
      RULE_lineStringExpression = 113,
      RULE_multiLineStringContent = 114,
      RULE_multiLineStringExpression = 115,
      RULE_lambdaLiteral = 116,
      RULE_lambdaParameters = 117,
      RULE_lambdaParameter = 118,
      RULE_anonymousFunction = 119,
      RULE_functionLiteral = 120,
      RULE_objectLiteral = 121,
      RULE_thisExpression = 122,
      RULE_superExpression = 123,
      RULE_ifExpression = 124,
      RULE_whenSubject = 125,
      RULE_whenExpression = 126,
      RULE_whenEntry = 127,
      RULE_whenCondition = 128,
      RULE_rangeTest = 129,
      RULE_typeTest = 130,
      RULE_tryExpression = 131,
      RULE_catchBlock = 132,
      RULE_finallyBlock = 133,
      RULE_jumpExpression = 134,
      RULE_callableReference = 135,
      RULE_assignmentAndOperator = 136,
      RULE_equalityOperator = 137,
      RULE_comparisonOperator = 138,
      RULE_inOperator = 139,
      RULE_isOperator = 140,
      RULE_additiveOperator = 141,
      RULE_multiplicativeOperator = 142,
      RULE_asOperator = 143,
      RULE_prefixUnaryOperator = 144,
      RULE_postfixUnaryOperator = 145,
      RULE_excl = 146,
      RULE_memberAccessOperator = 147,
      RULE_safeNav = 148,
      RULE_modifiers = 149,
      RULE_parameterModifiers = 150,
      RULE_modifier = 151,
      RULE_typeModifiers = 152,
      RULE_typeModifier = 153,
      RULE_classModifier = 154,
      RULE_memberModifier = 155,
      RULE_visibilityModifier = 156,
      RULE_varianceModifier = 157,
      RULE_typeParameterModifiers = 158,
      RULE_typeParameterModifier = 159,
      RULE_functionModifier = 160,
      RULE_propertyModifier = 161,
      RULE_inheritanceModifier = 162,
      RULE_parameterModifier = 163,
      RULE_reificationModifier = 164,
      RULE_platformModifier = 165,
      RULE_annotation = 166,
      RULE_singleAnnotation = 167,
      RULE_multiAnnotation = 168,
      RULE_annotationUseSiteTarget = 169,
      RULE_unescapedAnnotation = 170,
      RULE_simpleIdentifier = 171,
      RULE_identifier = 172;
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  protected static final DFA[] _decisionToDFA;
  protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
  private static final String[] _LITERAL_NAMES = makeLiteralNames();
  private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
  public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
  private static final int _serializedATNSegments = 2;
  private static final String _serializedATNSegment0 =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u00ad\u0d80\4\2\t"
          + "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
          + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
          + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
          + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
          + "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"
          + ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"
          + "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="
          + "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"
          + "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"
          + "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"
          + "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"
          + "k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"
          + "w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"
          + "\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"
          + "\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"
          + "\4\u008a\t\u008a\4\u008b\t\u008b\4\u008c\t\u008c\4\u008d\t\u008d\4\u008e"
          + "\t\u008e\4\u008f\t\u008f\4\u0090\t\u0090\4\u0091\t\u0091\4\u0092\t\u0092"
          + "\4\u0093\t\u0093\4\u0094\t\u0094\4\u0095\t\u0095\4\u0096\t\u0096\4\u0097"
          + "\t\u0097\4\u0098\t\u0098\4\u0099\t\u0099\4\u009a\t\u009a\4\u009b\t\u009b"
          + "\4\u009c\t\u009c\4\u009d\t\u009d\4\u009e\t\u009e\4\u009f\t\u009f\4\u00a0"
          + "\t\u00a0\4\u00a1\t\u00a1\4\u00a2\t\u00a2\4\u00a3\t\u00a3\4\u00a4\t\u00a4"
          + "\4\u00a5\t\u00a5\4\u00a6\t\u00a6\4\u00a7\t\u00a7\4\u00a8\t\u00a8\4\u00a9"
          + "\t\u00a9\4\u00aa\t\u00aa\4\u00ab\t\u00ab\4\u00ac\t\u00ac\4\u00ad\t\u00ad"
          + "\4\u00ae\t\u00ae\3\2\5\2\u015e\n\2\3\2\7\2\u0161\n\2\f\2\16\2\u0164\13"
          + "\2\3\2\7\2\u0167\n\2\f\2\16\2\u016a\13\2\3\2\3\2\3\2\7\2\u016f\n\2\f\2"
          + "\16\2\u0172\13\2\3\2\3\2\3\3\5\3\u0177\n\3\3\3\7\3\u017a\n\3\f\3\16\3"
          + "\u017d\13\3\3\3\7\3\u0180\n\3\f\3\16\3\u0183\13\3\3\3\3\3\3\3\3\3\3\3"
          + "\7\3\u018a\n\3\f\3\16\3\u018d\13\3\3\3\3\3\3\4\3\4\6\4\u0193\n\4\r\4\16"
          + "\4\u0194\3\5\3\5\3\5\7\5\u019a\n\5\f\5\16\5\u019d\13\5\3\5\3\5\7\5\u01a1"
          + "\n\5\f\5\16\5\u01a4\13\5\3\5\3\5\6\5\u01a8\n\5\r\5\16\5\u01a9\3\5\3\5"
          + "\3\5\5\5\u01af\n\5\3\5\7\5\u01b2\n\5\f\5\16\5\u01b5\13\5\3\6\3\6\3\6\5"
          + "\6\u01ba\n\6\5\6\u01bc\n\6\3\7\7\7\u01bf\n\7\f\7\16\7\u01c2\13\7\3\b\3"
          + "\b\3\b\3\b\3\b\5\b\u01c9\n\b\3\b\5\b\u01cc\n\b\3\t\3\t\3\t\3\n\3\n\5\n"
          + "\u01d3\n\n\3\13\5\13\u01d6\n\13\3\13\3\13\7\13\u01da\n\13\f\13\16\13\u01dd"
          + "\13\13\3\13\3\13\7\13\u01e1\n\13\f\13\16\13\u01e4\13\13\3\13\5\13\u01e7"
          + "\n\13\3\13\7\13\u01ea\n\13\f\13\16\13\u01ed\13\13\3\13\3\13\7\13\u01f1"
          + "\n\13\f\13\16\13\u01f4\13\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\5\f\u01fd\n"
          + "\f\3\r\5\r\u0200\n\r\3\r\3\r\3\r\7\r\u0205\n\r\f\r\16\r\u0208\13\r\5\r"
          + "\u020a\n\r\3\r\5\r\u020d\n\r\3\r\7\r\u0210\n\r\f\r\16\r\u0213\13\r\3\r"
          + "\3\r\7\r\u0217\n\r\f\r\16\r\u021a\13\r\3\r\5\r\u021d\n\r\3\r\7\r\u0220"
          + "\n\r\f\r\16\r\u0223\13\r\3\r\5\r\u0226\n\r\3\r\7\r\u0229\n\r\f\r\16\r"
          + "\u022c\13\r\3\r\3\r\7\r\u0230\n\r\f\r\16\r\u0233\13\r\3\r\5\r\u0236\n"
          + "\r\3\r\7\r\u0239\n\r\f\r\16\r\u023c\13\r\3\r\5\r\u023f\n\r\3\r\7\r\u0242"
          + "\n\r\f\r\16\r\u0245\13\r\3\r\3\r\7\r\u0249\n\r\f\r\16\r\u024c\13\r\3\r"
          + "\5\r\u024f\n\r\3\16\5\16\u0252\n\16\3\16\3\16\7\16\u0256\n\16\f\16\16"
          + "\16\u0259\13\16\5\16\u025b\n\16\3\16\3\16\3\17\3\17\7\17\u0261\n\17\f"
          + "\17\16\17\u0264\13\17\3\17\3\17\7\17\u0268\n\17\f\17\16\17\u026b\13\17"
          + "\3\17\3\17\3\20\3\20\7\20\u0271\n\20\f\20\16\20\u0274\13\20\3\20\3\20"
          + "\7\20\u0278\n\20\f\20\16\20\u027b\13\20\3\20\3\20\7\20\u027f\n\20\f\20"
          + "\16\20\u0282\13\20\3\20\7\20\u0285\n\20\f\20\16\20\u0288\13\20\3\20\7"
          + "\20\u028b\n\20\f\20\16\20\u028e\13\20\3\20\5\20\u0291\n\20\5\20\u0293"
          + "\n\20\3\20\7\20\u0296\n\20\f\20\16\20\u0299\13\20\3\20\3\20\3\21\5\21"
          + "\u029e\n\21\3\21\5\21\u02a1\n\21\3\21\7\21\u02a4\n\21\f\21\16\21\u02a7"
          + "\13\21\3\21\3\21\3\21\7\21\u02ac\n\21\f\21\16\21\u02af\13\21\3\21\3\21"
          + "\7\21\u02b3\n\21\f\21\16\21\u02b6\13\21\3\21\3\21\7\21\u02ba\n\21\f\21"
          + "\16\21\u02bd\13\21\3\21\5\21\u02c0\n\21\3\22\3\22\7\22\u02c4\n\22\f\22"
          + "\16\22\u02c7\13\22\3\22\3\22\7\22\u02cb\n\22\f\22\16\22\u02ce\13\22\3"
          + "\22\7\22\u02d1\n\22\f\22\16\22\u02d4\13\22\3\23\3\23\3\23\3\23\5\23\u02da"
          + "\n\23\3\24\3\24\3\24\3\25\7\25\u02e0\n\25\f\25\16\25\u02e3\13\25\3\25"
          + "\7\25\u02e6\n\25\f\25\16\25\u02e9\13\25\3\25\3\25\3\26\3\26\5\26\u02ef"
          + "\n\26\3\26\7\26\u02f2\n\26\f\26\16\26\u02f5\13\26\3\26\3\26\7\26\u02f9"
          + "\n\26\f\26\16\26\u02fc\13\26\3\26\3\26\3\27\3\27\7\27\u0302\n\27\f\27"
          + "\16\27\u0305\13\27\3\27\3\27\7\27\u0309\n\27\f\27\16\27\u030c\13\27\3"
          + "\27\3\27\7\27\u0310\n\27\f\27\16\27\u0313\13\27\3\27\7\27\u0316\n\27\f"
          + "\27\16\27\u0319\13\27\3\27\7\27\u031c\n\27\f\27\16\27\u031f\13\27\3\27"
          + "\5\27\u0322\n\27\3\27\7\27\u0325\n\27\f\27\16\27\u0328\13\27\3\27\3\27"
          + "\3\30\5\30\u032d\n\30\3\30\7\30\u0330\n\30\f\30\16\30\u0333\13\30\3\30"
          + "\3\30\7\30\u0337\n\30\f\30\16\30\u033a\13\30\3\30\3\30\7\30\u033e\n\30"
          + "\f\30\16\30\u0341\13\30\3\30\5\30\u0344\n\30\3\31\3\31\7\31\u0348\n\31"
          + "\f\31\16\31\u034b\13\31\3\31\3\31\7\31\u034f\n\31\f\31\16\31\u0352\13"
          + "\31\3\31\3\31\7\31\u0356\n\31\f\31\16\31\u0359\13\31\3\31\7\31\u035c\n"
          + "\31\f\31\16\31\u035f\13\31\3\32\7\32\u0362\n\32\f\32\16\32\u0365\13\32"
          + "\3\32\3\32\7\32\u0369\n\32\f\32\16\32\u036c\13\32\3\32\3\32\7\32\u0370"
          + "\n\32\f\32\16\32\u0373\13\32\3\32\3\32\3\33\3\33\5\33\u0379\n\33\7\33"
          + "\u037b\n\33\f\33\16\33\u037e\13\33\3\34\3\34\3\34\3\34\5\34\u0384\n\34"
          + "\3\35\3\35\7\35\u0388\n\35\f\35\16\35\u038b\13\35\3\35\3\35\3\36\5\36"
          + "\u0390\n\36\3\36\3\36\7\36\u0394\n\36\f\36\16\36\u0397\13\36\3\36\3\36"
          + "\7\36\u039b\n\36\f\36\16\36\u039e\13\36\3\36\5\36\u03a1\n\36\3\36\7\36"
          + "\u03a4\n\36\f\36\16\36\u03a7\13\36\3\36\3\36\7\36\u03ab\n\36\f\36\16\36"
          + "\u03ae\13\36\3\36\5\36\u03b1\n\36\3\36\7\36\u03b4\n\36\f\36\16\36\u03b7"
          + "\13\36\3\36\5\36\u03ba\n\36\3\37\3\37\7\37\u03be\n\37\f\37\16\37\u03c1"
          + "\13\37\3\37\3\37\7\37\u03c5\n\37\f\37\16\37\u03c8\13\37\3\37\3\37\7\37"
          + "\u03cc\n\37\f\37\16\37\u03cf\13\37\3\37\7\37\u03d2\n\37\f\37\16\37\u03d5"
          + "\13\37\3\37\7\37\u03d8\n\37\f\37\16\37\u03db\13\37\3\37\5\37\u03de\n\37"
          + "\5\37\u03e0\n\37\3\37\7\37\u03e3\n\37\f\37\16\37\u03e6\13\37\3\37\3\37"
          + "\3 \5 \u03eb\n \3 \3 \7 \u03ef\n \f \16 \u03f2\13 \3 \3 \7 \u03f6\n \f"
          + " \16 \u03f9\13 \3 \5 \u03fc\n \3!\5!\u03ff\n!\3!\3!\7!\u0403\n!\f!\16"
          + "!\u0406\13!\3!\5!\u0409\n!\3!\7!\u040c\n!\f!\16!\u040f\13!\3!\3!\7!\u0413"
          + "\n!\f!\16!\u0416\13!\3!\3!\5!\u041a\n!\3!\7!\u041d\n!\f!\16!\u0420\13"
          + "!\3!\3!\7!\u0424\n!\f!\16!\u0427\13!\3!\3!\7!\u042b\n!\f!\16!\u042e\13"
          + "!\3!\3!\7!\u0432\n!\f!\16!\u0435\13!\3!\5!\u0438\n!\3!\7!\u043b\n!\f!"
          + "\16!\u043e\13!\3!\5!\u0441\n!\3!\7!\u0444\n!\f!\16!\u0447\13!\3!\5!\u044a"
          + "\n!\3\"\3\"\3\"\7\"\u044f\n\"\f\"\16\"\u0452\13\"\3\"\5\"\u0455\n\"\3"
          + "#\7#\u0458\n#\f#\16#\u045b\13#\3#\7#\u045e\n#\f#\16#\u0461\13#\3#\3#\7"
          + "#\u0465\n#\f#\16#\u0468\13#\3#\3#\7#\u046c\n#\f#\16#\u046f\13#\3#\5#\u0472"
          + "\n#\3$\3$\7$\u0476\n$\f$\16$\u0479\13$\3$\3$\7$\u047d\n$\f$\16$\u0480"
          + "\13$\3$\3$\7$\u0484\n$\f$\16$\u0487\13$\3$\7$\u048a\n$\f$\16$\u048d\13"
          + "$\3$\7$\u0490\n$\f$\16$\u0493\13$\3$\5$\u0496\n$\3$\7$\u0499\n$\f$\16"
          + "$\u049c\13$\3$\3$\3%\5%\u04a1\n%\3%\3%\7%\u04a5\n%\f%\16%\u04a8\13%\3"
          + "%\5%\u04ab\n%\3%\7%\u04ae\n%\f%\16%\u04b1\13%\3%\3%\7%\u04b5\n%\f%\16"
          + "%\u04b8\13%\3%\3%\5%\u04bc\n%\3%\7%\u04bf\n%\f%\16%\u04c2\13%\3%\3%\5"
          + "%\u04c6\n%\3%\7%\u04c9\n%\f%\16%\u04cc\13%\3%\5%\u04cf\n%\3%\7%\u04d2"
          + "\n%\f%\16%\u04d5\13%\3%\3%\7%\u04d9\n%\f%\16%\u04dc\13%\3%\3%\5%\u04e0"
          + "\n%\5%\u04e2\n%\3%\6%\u04e5\n%\r%\16%\u04e6\3%\5%\u04ea\n%\3%\7%\u04ed"
          + "\n%\f%\16%\u04f0\13%\3%\5%\u04f3\n%\3%\7%\u04f6\n%\f%\16%\u04f9\13%\3"
          + "%\5%\u04fc\n%\3%\5%\u04ff\n%\3%\5%\u0502\n%\3%\7%\u0505\n%\f%\16%\u0508"
          + "\13%\3%\5%\u050b\n%\3%\5%\u050e\n%\5%\u0510\n%\3&\3&\7&\u0514\n&\f&\16"
          + "&\u0517\13&\3&\3&\3\'\5\'\u051c\n\'\3\'\3\'\7\'\u0520\n\'\f\'\16\'\u0523"
          + "\13\'\3\'\3\'\7\'\u0527\n\'\f\'\16\'\u052a\13\'\3\'\3\'\7\'\u052e\n\'"
          + "\f\'\16\'\u0531\13\'\3\'\3\'\7\'\u0535\n\'\f\'\16\'\u0538\13\'\3\'\5\'"
          + "\u053b\n\'\3\'\7\'\u053e\n\'\f\'\16\'\u0541\13\'\3\'\5\'\u0544\n\'\3("
          + "\5(\u0547\n(\3(\3(\7(\u054b\n(\f(\16(\u054e\13(\3(\3(\7(\u0552\n(\f(\16"
          + "(\u0555\13(\3(\3(\7(\u0559\n(\f(\16(\u055c\13(\3(\5(\u055f\n(\3(\7(\u0562"
          + "\n(\f(\16(\u0565\13(\3(\3(\7(\u0569\n(\f(\16(\u056c\13(\3(\3(\7(\u0570"
          + "\n(\f(\16(\u0573\13(\3(\5(\u0576\n(\3(\7(\u0579\n(\f(\16(\u057c\13(\3"
          + "(\3(\5(\u0580\n(\3)\3)\7)\u0584\n)\f)\16)\u0587\13)\3)\3)\7)\u058b\n)"
          + "\f)\16)\u058e\13)\3)\3)\7)\u0592\n)\f)\16)\u0595\13)\3)\7)\u0598\n)\f"
          + ")\16)\u059b\13)\3)\7)\u059e\n)\f)\16)\u05a1\13)\3)\5)\u05a4\n)\5)\u05a6"
          + "\n)\3)\7)\u05a9\n)\f)\16)\u05ac\13)\3)\3)\3*\5*\u05b1\n*\3*\3*\7*\u05b5"
          + "\n*\f*\16*\u05b8\13*\3*\3*\7*\u05bc\n*\f*\16*\u05bf\13*\3*\5*\u05c2\n"
          + "*\3+\3+\7+\u05c6\n+\f+\16+\u05c9\13+\3+\3+\7+\u05cd\n+\f+\16+\u05d0\13"
          + "+\3+\5+\u05d3\n+\3,\3,\7,\u05d7\n,\f,\16,\u05da\13,\3,\3,\7,\u05de\n,"
          + "\f,\16,\u05e1\13,\3,\3,\3-\5-\u05e6\n-\3-\3-\7-\u05ea\n-\f-\16-\u05ed"
          + "\13-\3-\3-\7-\u05f1\n-\f-\16-\u05f4\13-\3-\3-\7-\u05f8\n-\f-\16-\u05fb"
          + "\13-\3-\5-\u05fe\n-\3-\7-\u0601\n-\f-\16-\u0604\13-\3-\5-\u0607\n-\3."
          + "\5.\u060a\n.\3.\3.\7.\u060e\n.\f.\16.\u0611\13.\3.\3.\7.\u0615\n.\f.\16"
          + ".\u0618\13.\3.\3.\7.\u061c\n.\f.\16.\u061f\13.\3.\5.\u0622\n.\3.\7.\u0625"
          + "\n.\f.\16.\u0628\13.\3.\5.\u062b\n.\3/\3/\7/\u062f\n/\f/\16/\u0632\13"
          + "/\3/\3/\3\60\3\60\7\60\u0638\n\60\f\60\16\60\u063b\13\60\3\60\5\60\u063e"
          + "\n\60\3\60\7\60\u0641\n\60\f\60\16\60\u0644\13\60\3\60\3\60\7\60\u0648"
          + "\n\60\f\60\16\60\u064b\13\60\3\60\5\60\u064e\n\60\3\60\7\60\u0651\n\60"
          + "\f\60\16\60\u0654\13\60\3\60\3\60\3\61\3\61\7\61\u065a\n\61\f\61\16\61"
          + "\u065d\13\61\3\61\3\61\7\61\u0661\n\61\f\61\16\61\u0664\13\61\3\61\7\61"
          + "\u0667\n\61\f\61\16\61\u066a\13\61\3\61\7\61\u066d\n\61\f\61\16\61\u0670"
          + "\13\61\3\61\5\61\u0673\n\61\3\62\3\62\7\62\u0677\n\62\f\62\16\62\u067a"
          + "\13\62\5\62\u067c\n\62\3\62\3\62\7\62\u0680\n\62\f\62\16\62\u0683\13\62"
          + "\3\62\5\62\u0686\n\62\3\62\7\62\u0689\n\62\f\62\16\62\u068c\13\62\3\62"
          + "\5\62\u068f\n\62\3\63\5\63\u0692\n\63\3\63\3\63\3\63\3\63\5\63\u0698\n"
          + "\63\3\64\3\64\5\64\u069c\n\64\3\65\3\65\5\65\u06a0\n\65\3\65\7\65\u06a3"
          + "\n\65\f\65\16\65\u06a6\13\65\3\65\6\65\u06a9\n\65\r\65\16\65\u06aa\3\66"
          + "\3\66\3\67\3\67\7\67\u06b1\n\67\f\67\16\67\u06b4\13\67\3\67\3\67\7\67"
          + "\u06b8\n\67\f\67\16\67\u06bb\13\67\3\67\7\67\u06be\n\67\f\67\16\67\u06c1"
          + "\13\67\38\38\78\u06c5\n8\f8\168\u06c8\138\38\58\u06cb\n8\39\59\u06ce\n"
          + "9\39\39\59\u06d2\n9\3:\6:\u06d5\n:\r:\16:\u06d6\3;\3;\7;\u06db\n;\f;\16"
          + ";\u06de\13;\3;\5;\u06e1\n;\3<\3<\7<\u06e5\n<\f<\16<\u06e8\13<\3<\3<\7"
          + "<\u06ec\n<\f<\16<\u06ef\13<\5<\u06f1\n<\3<\3<\7<\u06f5\n<\f<\16<\u06f8"
          + "\13<\3<\3<\7<\u06fc\n<\f<\16<\u06ff\13<\3<\3<\3=\3=\7=\u0705\n=\f=\16"
          + "=\u0708\13=\3=\3=\5=\u070c\n=\3=\7=\u070f\n=\f=\16=\u0712\13=\3=\3=\7"
          + "=\u0716\n=\f=\16=\u0719\13=\3=\3=\5=\u071d\n=\7=\u071f\n=\f=\16=\u0722"
          + "\13=\3=\7=\u0725\n=\f=\16=\u0728\13=\3=\5=\u072b\n=\3=\7=\u072e\n=\f="
          + "\16=\u0731\13=\3=\3=\3>\3>\7>\u0737\n>\f>\16>\u073a\13>\3>\3>\7>\u073e"
          + "\n>\f>\16>\u0741\13>\3>\3>\3?\5?\u0746\n?\3?\3?\3?\5?\u074b\n?\3@\3@\7"
          + "@\u074f\n@\f@\16@\u0752\13@\3@\3@\5@\u0756\n@\3@\7@\u0759\n@\f@\16@\u075c"
          + "\13@\3@\3@\3A\3A\3A\3A\7A\u0764\nA\fA\16A\u0767\13A\5A\u0769\nA\3A\5A"
          + "\u076c\nA\3B\3B\7B\u0770\nB\fB\16B\u0773\13B\3B\3B\3B\3B\5B\u0779\nB\3"
          + "C\3C\3C\7C\u077e\nC\fC\16C\u0781\13C\3D\3D\5D\u0785\nD\3E\3E\7E\u0789"
          + "\nE\fE\16E\u078c\13E\3E\3E\7E\u0790\nE\fE\16E\u0793\13E\3E\3E\3F\3F\3"
          + "F\5F\u079a\nF\3G\3G\7G\u079e\nG\fG\16G\u07a1\13G\3G\3G\7G\u07a5\nG\fG"
          + "\16G\u07a8\13G\3G\3G\5G\u07ac\nG\3G\3G\3G\3G\7G\u07b2\nG\fG\16G\u07b5"
          + "\13G\3G\5G\u07b8\nG\3H\3H\7H\u07bc\nH\fH\16H\u07bf\13H\3H\3H\3H\3H\7H"
          + "\u07c5\nH\fH\16H\u07c8\13H\3H\3H\5H\u07cc\nH\3I\3I\7I\u07d0\nI\fI\16I"
          + "\u07d3\13I\3I\5I\u07d6\nI\3I\7I\u07d9\nI\fI\16I\u07dc\13I\3I\3I\7I\u07e0"
          + "\nI\fI\16I\u07e3\13I\3I\3I\3I\3I\3J\3J\3J\3J\3J\3J\5J\u07ef\nJ\3J\7J\u07f2"
          + "\nJ\fJ\16J\u07f5\13J\3J\3J\3K\3K\7K\u07fb\nK\fK\16K\u07fe\13K\3K\5K\u0801"
          + "\nK\3L\6L\u0804\nL\rL\16L\u0805\3L\5L\u0809\nL\3M\3M\3N\3N\7N\u080f\n"
          + "N\fN\16N\u0812\13N\3N\3N\7N\u0816\nN\fN\16N\u0819\13N\3N\7N\u081c\nN\f"
          + "N\16N\u081f\13N\3O\3O\7O\u0823\nO\fO\16O\u0826\13O\3O\3O\7O\u082a\nO\f"
          + "O\16O\u082d\13O\3O\7O\u0830\nO\fO\16O\u0833\13O\3P\3P\3P\7P\u0838\nP\f"
          + "P\16P\u083b\13P\3P\3P\7P\u083f\nP\fP\16P\u0842\13P\3Q\3Q\3Q\7Q\u0847\n"
          + "Q\fQ\16Q\u084a\13Q\3Q\3Q\7Q\u084e\nQ\fQ\16Q\u0851\13Q\3R\3R\7R\u0855\n"
          + "R\fR\16R\u0858\13R\3S\3S\3S\7S\u085d\nS\fS\16S\u0860\13S\3S\3S\3S\3S\7"
          + "S\u0866\nS\fS\16S\u0869\13S\3S\3S\7S\u086d\nS\fS\16S\u0870\13S\3T\3T\7"
          + "T\u0874\nT\fT\16T\u0877\13T\3T\3T\7T\u087b\nT\fT\16T\u087e\13T\3T\3T\7"
          + "T\u0882\nT\fT\16T\u0885\13T\3U\3U\3U\3V\3V\3V\7V\u088d\nV\fV\16V\u0890"
          + "\13V\3V\3V\7V\u0894\nV\fV\16V\u0897\13V\3W\3W\3W\7W\u089c\nW\fW\16W\u089f"
          + "\13W\3W\7W\u08a2\nW\fW\16W\u08a5\13W\3X\3X\3X\7X\u08aa\nX\fX\16X\u08ad"
          + "\13X\3X\3X\7X\u08b1\nX\fX\16X\u08b4\13X\3Y\3Y\3Y\7Y\u08b9\nY\fY\16Y\u08bc"
          + "\13Y\3Y\3Y\7Y\u08c0\nY\fY\16Y\u08c3\13Y\3Z\3Z\7Z\u08c7\nZ\fZ\16Z\u08ca"
          + "\13Z\3Z\3Z\7Z\u08ce\nZ\fZ\16Z\u08d1\13Z\3Z\3Z\7Z\u08d5\nZ\fZ\16Z\u08d8"
          + "\13Z\3[\7[\u08db\n[\f[\16[\u08de\13[\3[\3[\3\\\3\\\3\\\3\\\7\\\u08e6\n"
          + "\\\f\\\16\\\u08e9\13\\\5\\\u08eb\n\\\3]\3]\7]\u08ef\n]\f]\16]\u08f2\13"
          + "]\3^\3^\3^\3^\3^\5^\u08f9\n^\3_\3_\3_\3_\3_\5_\u0900\n_\3`\3`\7`\u0904"
          + "\n`\f`\16`\u0907\13`\3`\3`\7`\u090b\n`\f`\16`\u090e\13`\3`\3`\3a\3a\5"
          + "a\u0914\na\3b\3b\7b\u0918\nb\fb\16b\u091b\13b\3b\3b\7b\u091f\nb\fb\16"
          + "b\u0922\13b\3b\3b\3c\3c\3c\5c\u0929\nc\3d\3d\7d\u092d\nd\fd\16d\u0930"
          + "\13d\3d\3d\7d\u0934\nd\fd\16d\u0937\13d\3d\3d\7d\u093b\nd\fd\16d\u093e"
          + "\13d\3d\7d\u0941\nd\fd\16d\u0944\13d\3d\7d\u0947\nd\fd\16d\u094a\13d\3"
          + "d\5d\u094d\nd\3d\7d\u0950\nd\fd\16d\u0953\13d\3d\3d\3e\3e\7e\u0959\ne"
          + "\fe\16e\u095c\13e\3e\3e\3e\5e\u0961\ne\3f\5f\u0964\nf\3f\5f\u0967\nf\3"
          + "f\3f\5f\u096b\nf\3g\7g\u096e\ng\fg\16g\u0971\13g\3g\5g\u0974\ng\3g\7g"
          + "\u0977\ng\fg\16g\u097a\13g\3g\3g\3h\3h\7h\u0980\nh\fh\16h\u0983\13h\3"
          + "h\3h\7h\u0987\nh\fh\16h\u098a\13h\3h\3h\7h\u098e\nh\fh\16h\u0991\13h\3"
          + "h\7h\u0994\nh\fh\16h\u0997\13h\3h\7h\u099a\nh\fh\16h\u099d\13h\3h\5h\u09a0"
          + "\nh\3h\7h\u09a3\nh\fh\16h\u09a6\13h\3h\3h\3i\3i\7i\u09ac\ni\fi\16i\u09af"
          + "\13i\3i\3i\7i\u09b3\ni\fi\16i\u09b6\13i\3i\3i\7i\u09ba\ni\fi\16i\u09bd"
          + "\13i\3i\7i\u09c0\ni\fi\16i\u09c3\13i\3i\7i\u09c6\ni\fi\16i\u09c9\13i\3"
          + "i\5i\u09cc\ni\3i\7i\u09cf\ni\fi\16i\u09d2\13i\5i\u09d4\ni\3i\3i\3j\5j"
          + "\u09d9\nj\3j\7j\u09dc\nj\fj\16j\u09df\13j\3j\3j\7j\u09e3\nj\fj\16j\u09e6"
          + "\13j\3j\3j\7j\u09ea\nj\fj\16j\u09ed\13j\5j\u09ef\nj\3j\5j\u09f2\nj\3j"
          + "\7j\u09f5\nj\fj\16j\u09f8\13j\3j\3j\3k\3k\3k\3k\3k\3k\3k\3k\3k\3k\3k\3"
          + "k\3k\3k\5k\u0a0a\nk\3l\3l\7l\u0a0e\nl\fl\16l\u0a11\13l\3l\3l\7l\u0a15"
          + "\nl\fl\16l\u0a18\13l\3l\3l\3m\3m\7m\u0a1e\nm\fm\16m\u0a21\13m\3m\3m\7"
          + "m\u0a25\nm\fm\16m\u0a28\13m\3m\3m\7m\u0a2c\nm\fm\16m\u0a2f\13m\3m\7m\u0a32"
          + "\nm\fm\16m\u0a35\13m\3m\7m\u0a38\nm\fm\16m\u0a3b\13m\3m\5m\u0a3e\nm\3"
          + "m\7m\u0a41\nm\fm\16m\u0a44\13m\5m\u0a46\nm\3m\3m\3n\3n\3o\3o\5o\u0a4e"
          + "\no\3p\3p\3p\7p\u0a53\np\fp\16p\u0a56\13p\3p\3p\3q\3q\3q\3q\7q\u0a5e\n"
          + "q\fq\16q\u0a61\13q\3q\3q\3r\3r\3s\3s\7s\u0a69\ns\fs\16s\u0a6c\13s\3s\3"
          + "s\7s\u0a70\ns\fs\16s\u0a73\13s\3s\3s\3t\3t\3u\3u\7u\u0a7b\nu\fu\16u\u0a7e"
          + "\13u\3u\3u\7u\u0a82\nu\fu\16u\u0a85\13u\3u\3u\3v\3v\7v\u0a8b\nv\fv\16"
          + "v\u0a8e\13v\3v\5v\u0a91\nv\3v\7v\u0a94\nv\fv\16v\u0a97\13v\3v\3v\7v\u0a9b"
          + "\nv\fv\16v\u0a9e\13v\5v\u0aa0\nv\3v\3v\7v\u0aa4\nv\fv\16v\u0aa7\13v\3"
          + "v\3v\3w\3w\7w\u0aad\nw\fw\16w\u0ab0\13w\3w\3w\7w\u0ab4\nw\fw\16w\u0ab7"
          + "\13w\3w\7w\u0aba\nw\fw\16w\u0abd\13w\3w\7w\u0ac0\nw\fw\16w\u0ac3\13w\3"
          + "w\5w\u0ac6\nw\3x\3x\3x\7x\u0acb\nx\fx\16x\u0ace\13x\3x\3x\7x\u0ad2\nx"
          + "\fx\16x\u0ad5\13x\3x\5x\u0ad8\nx\5x\u0ada\nx\3y\3y\7y\u0ade\ny\fy\16y"
          + "\u0ae1\13y\3y\3y\7y\u0ae5\ny\fy\16y\u0ae8\13y\3y\3y\5y\u0aec\ny\3y\7y"
          + "\u0aef\ny\fy\16y\u0af2\13y\3y\3y\7y\u0af6\ny\fy\16y\u0af9\13y\3y\3y\7"
          + "y\u0afd\ny\fy\16y\u0b00\13y\3y\5y\u0b03\ny\3y\7y\u0b06\ny\fy\16y\u0b09"
          + "\13y\3y\5y\u0b0c\ny\3y\7y\u0b0f\ny\fy\16y\u0b12\13y\3y\5y\u0b15\ny\3z"
          + "\3z\5z\u0b19\nz\3{\3{\7{\u0b1d\n{\f{\16{\u0b20\13{\3{\3{\7{\u0b24\n{\f"
          + "{\16{\u0b27\13{\3{\3{\7{\u0b2b\n{\f{\16{\u0b2e\13{\5{\u0b30\n{\3{\7{\u0b33"
          + "\n{\f{\16{\u0b36\13{\3{\5{\u0b39\n{\3|\3|\3}\3}\3}\7}\u0b40\n}\f}\16}"
          + "\u0b43\13}\3}\3}\7}\u0b47\n}\f}\16}\u0b4a\13}\3}\3}\5}\u0b4e\n}\3}\3}"
          + "\5}\u0b52\n}\3}\5}\u0b55\n}\3~\3~\7~\u0b59\n~\f~\16~\u0b5c\13~\3~\3~\7"
          + "~\u0b60\n~\f~\16~\u0b63\13~\3~\3~\7~\u0b67\n~\f~\16~\u0b6a\13~\3~\3~\7"
          + "~\u0b6e\n~\f~\16~\u0b71\13~\3~\3~\5~\u0b75\n~\3~\7~\u0b78\n~\f~\16~\u0b7b"
          + "\13~\3~\5~\u0b7e\n~\3~\7~\u0b81\n~\f~\16~\u0b84\13~\3~\3~\7~\u0b88\n~"
          + "\f~\16~\u0b8b\13~\3~\3~\5~\u0b8f\n~\3~\5~\u0b92\n~\3\177\3\177\7\177\u0b96"
          + "\n\177\f\177\16\177\u0b99\13\177\3\177\7\177\u0b9c\n\177\f\177\16\177"
          + "\u0b9f\13\177\3\177\3\177\7\177\u0ba3\n\177\f\177\16\177\u0ba6\13\177"
          + "\3\177\3\177\7\177\u0baa\n\177\f\177\16\177\u0bad\13\177\3\177\3\177\7"
          + "\177\u0bb1\n\177\f\177\16\177\u0bb4\13\177\5\177\u0bb6\n\177\3\177\3\177"
          + "\3\177\3\u0080\3\u0080\7\u0080\u0bbd\n\u0080\f\u0080\16\u0080\u0bc0\13"
          + "\u0080\3\u0080\5\u0080\u0bc3\n\u0080\3\u0080\7\u0080\u0bc6\n\u0080\f\u0080"
          + "\16\u0080\u0bc9\13\u0080\3\u0080\3\u0080\7\u0080\u0bcd\n\u0080\f\u0080"
          + "\16\u0080\u0bd0\13\u0080\3\u0080\3\u0080\7\u0080\u0bd4\n\u0080\f\u0080"
          + "\16\u0080\u0bd7\13\u0080\7\u0080\u0bd9\n\u0080\f\u0080\16\u0080\u0bdc"
          + "\13\u0080\3\u0080\7\u0080\u0bdf\n\u0080\f\u0080\16\u0080\u0be2\13\u0080"
          + "\3\u0080\3\u0080\3\u0081\3\u0081\7\u0081\u0be8\n\u0081\f\u0081\16\u0081"
          + "\u0beb\13\u0081\3\u0081\3\u0081\7\u0081\u0bef\n\u0081\f\u0081\16\u0081"
          + "\u0bf2\13\u0081\3\u0081\7\u0081\u0bf5\n\u0081\f\u0081\16\u0081\u0bf8\13"
          + "\u0081\3\u0081\7\u0081\u0bfb\n\u0081\f\u0081\16\u0081\u0bfe\13\u0081\3"
          + "\u0081\5\u0081\u0c01\n\u0081\3\u0081\7\u0081\u0c04\n\u0081\f\u0081\16"
          + "\u0081\u0c07\13\u0081\3\u0081\3\u0081\7\u0081\u0c0b\n\u0081\f\u0081\16"
          + "\u0081\u0c0e\13\u0081\3\u0081\3\u0081\5\u0081\u0c12\n\u0081\3\u0081\3"
          + "\u0081\7\u0081\u0c16\n\u0081\f\u0081\16\u0081\u0c19\13\u0081\3\u0081\3"
          + "\u0081\7\u0081\u0c1d\n\u0081\f\u0081\16\u0081\u0c20\13\u0081\3\u0081\3"
          + "\u0081\5\u0081\u0c24\n\u0081\5\u0081\u0c26\n\u0081\3\u0082\3\u0082\3\u0082"
          + "\5\u0082\u0c2b\n\u0082\3\u0083\3\u0083\7\u0083\u0c2f\n\u0083\f\u0083\16"
          + "\u0083\u0c32\13\u0083\3\u0083\3\u0083\3\u0084\3\u0084\7\u0084\u0c38\n"
          + "\u0084\f\u0084\16\u0084\u0c3b\13\u0084\3\u0084\3\u0084\3\u0085\3\u0085"
          + "\7\u0085\u0c41\n\u0085\f\u0085\16\u0085\u0c44\13\u0085\3\u0085\3\u0085"
          + "\7\u0085\u0c48\n\u0085\f\u0085\16\u0085\u0c4b\13\u0085\3\u0085\6\u0085"
          + "\u0c4e\n\u0085\r\u0085\16\u0085\u0c4f\3\u0085\7\u0085\u0c53\n\u0085\f"
          + "\u0085\16\u0085\u0c56\13\u0085\3\u0085\5\u0085\u0c59\n\u0085\3\u0085\7"
          + "\u0085\u0c5c\n\u0085\f\u0085\16\u0085\u0c5f\13\u0085\3\u0085\5\u0085\u0c62"
          + "\n\u0085\3\u0086\3\u0086\7\u0086\u0c66\n\u0086\f\u0086\16\u0086\u0c69"
          + "\13\u0086\3\u0086\3\u0086\7\u0086\u0c6d\n\u0086\f\u0086\16\u0086\u0c70"
          + "\13\u0086\3\u0086\3\u0086\3\u0086\3\u0086\7\u0086\u0c76\n\u0086\f\u0086"
          + "\16\u0086\u0c79\13\u0086\3\u0086\5\u0086\u0c7c\n\u0086\3\u0086\3\u0086"
          + "\7\u0086\u0c80\n\u0086\f\u0086\16\u0086\u0c83\13\u0086\3\u0086\3\u0086"
          + "\3\u0087\3\u0087\7\u0087\u0c89\n\u0087\f\u0087\16\u0087\u0c8c\13\u0087"
          + "\3\u0087\3\u0087\3\u0088\3\u0088\7\u0088\u0c92\n\u0088\f\u0088\16\u0088"
          + "\u0c95\13\u0088\3\u0088\3\u0088\3\u0088\5\u0088\u0c9a\n\u0088\3\u0088"
          + "\3\u0088\3\u0088\3\u0088\5\u0088\u0ca0\n\u0088\3\u0089\5\u0089\u0ca3\n"
          + "\u0089\3\u0089\3\u0089\7\u0089\u0ca7\n\u0089\f\u0089\16\u0089\u0caa\13"
          + "\u0089\3\u0089\3\u0089\5\u0089\u0cae\n\u0089\3\u008a\3\u008a\3\u008b\3"
          + "\u008b\3\u008c\3\u008c\3\u008d\3\u008d\3\u008e\3\u008e\3\u008f\3\u008f"
          + "\3\u0090\3\u0090\3\u0091\3\u0091\3\u0092\3\u0092\3\u0092\3\u0092\3\u0092"
          + "\5\u0092\u0cc5\n\u0092\3\u0093\3\u0093\3\u0093\3\u0093\5\u0093\u0ccb\n"
          + "\u0093\3\u0094\3\u0094\3\u0095\7\u0095\u0cd0\n\u0095\f\u0095\16\u0095"
          + "\u0cd3\13\u0095\3\u0095\3\u0095\7\u0095\u0cd7\n\u0095\f\u0095\16\u0095"
          + "\u0cda\13\u0095\3\u0095\3\u0095\5\u0095\u0cde\n\u0095\3\u0096\3\u0096"
          + "\3\u0096\3\u0097\3\u0097\6\u0097\u0ce5\n\u0097\r\u0097\16\u0097\u0ce6"
          + "\3\u0098\3\u0098\6\u0098\u0ceb\n\u0098\r\u0098\16\u0098\u0cec\3\u0099"
          + "\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\3\u0099\5\u0099\u0cf7"
          + "\n\u0099\3\u0099\7\u0099\u0cfa\n\u0099\f\u0099\16\u0099\u0cfd\13\u0099"
          + "\3\u009a\6\u009a\u0d00\n\u009a\r\u009a\16\u009a\u0d01\3\u009b\3\u009b"
          + "\3\u009b\7\u009b\u0d07\n\u009b\f\u009b\16\u009b\u0d0a\13\u009b\5\u009b"
          + "\u0d0c\n\u009b\3\u009c\3\u009c\3\u009d\3\u009d\3\u009e\3\u009e\3\u009f"
          + "\3\u009f\3\u00a0\6\u00a0\u0d17\n\u00a0\r\u00a0\16\u00a0\u0d18\3\u00a1"
          + "\3\u00a1\7\u00a1\u0d1d\n\u00a1\f\u00a1\16\u00a1\u0d20\13\u00a1\3\u00a1"
          + "\3\u00a1\7\u00a1\u0d24\n\u00a1\f\u00a1\16\u00a1\u0d27\13\u00a1\3\u00a1"
          + "\5\u00a1\u0d2a\n\u00a1\3\u00a2\3\u00a2\3\u00a3\3\u00a3\3\u00a4\3\u00a4"
          + "\3\u00a5\3\u00a5\3\u00a6\3\u00a6\3\u00a7\3\u00a7\3\u00a8\3\u00a8\5\u00a8"
          + "\u0d3a\n\u00a8\3\u00a8\7\u00a8\u0d3d\n\u00a8\f\u00a8\16\u00a8\u0d40\13"
          + "\u00a8\3\u00a9\3\u00a9\7\u00a9\u0d44\n\u00a9\f\u00a9\16\u00a9\u0d47\13"
          + "\u00a9\3\u00a9\3\u00a9\5\u00a9\u0d4b\n\u00a9\3\u00a9\3\u00a9\3\u00aa\3"
          + "\u00aa\7\u00aa\u0d51\n\u00aa\f\u00aa\16\u00aa\u0d54\13\u00aa\3\u00aa\3"
          + "\u00aa\5\u00aa\u0d58\n\u00aa\3\u00aa\3\u00aa\6\u00aa\u0d5c\n\u00aa\r\u00aa"
          + "\16\u00aa\u0d5d\3\u00aa\3\u00aa\3\u00ab\3\u00ab\3\u00ab\7\u00ab\u0d65"
          + "\n\u00ab\f\u00ab\16\u00ab\u0d68\13\u00ab\3\u00ab\3\u00ab\3\u00ac\3\u00ac"
          + "\5\u00ac\u0d6e\n\u00ac\3\u00ad\3\u00ad\3\u00ae\3\u00ae\7\u00ae\u0d74\n"
          + "\u00ae\f\u00ae\16\u00ae\u0d77\13\u00ae\3\u00ae\3\u00ae\7\u00ae\u0d7b\n"
          + "\u00ae\f\u00ae\16\u00ae\u0d7e\13\u00ae\3\u00ae\2\2\u00af\2\4\6\b\n\f\16"
          + "\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bd"
          + "fhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090\u0092"
          + "\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8\u00aa"
          + "\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0\u00c2"
          + "\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8\u00da"
          + "\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0\u00f2"
          + "\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108\u010a"
          + "\u010c\u010e\u0110\u0112\u0114\u0116\u0118\u011a\u011c\u011e\u0120\u0122"
          + "\u0124\u0126\u0128\u012a\u012c\u012e\u0130\u0132\u0134\u0136\u0138\u013a"
          + "\u013c\u013e\u0140\u0142\u0144\u0146\u0148\u014a\u014c\u014e\u0150\u0152"
          + "\u0154\u0156\u0158\u015a\2 \4\2**,,\3\2NO\3\2UV\3\2./\3\2*+\4\2\7\7\35"
          + "\35\4\2\u0089\u0089\u008c\u0093\3\2\u00a1\u00a3\3\2\u00a6\u00a8\4\2=="
          + "UU\4\2::cc\3\2\37#\4\2\64\65\678\3\2\60\63\4\2hhjj\4\2ggii\3\2\24\25\3"
          + "\2\21\23\4\2\66\66ff\3\2\32\33\3\2qv\4\2}}\u0082\u0082\3\2mp\4\2hhkk\3"
          + "\2w|\3\2~\u0080\3\2\u0083\u0085\3\2\u0087\u0088\3\2@G\t\2?GIIQTXX]^k\u0088"
          + "\u0094\u0094\2\u0f1c\2\u015d\3\2\2\2\4\u0176\3\2\2\2\6\u0190\3\2\2\2\b"
          + "\u0196\3\2\2\2\n\u01bb\3\2\2\2\f\u01c0\3\2\2\2\16\u01c3\3\2\2\2\20\u01cd"
          + "\3\2\2\2\22\u01d0\3\2\2\2\24\u01d5\3\2\2\2\26\u01fc\3\2\2\2\30\u01ff\3"
          + "\2\2\2\32\u025a\3\2\2\2\34\u025e\3\2\2\2\36\u026e\3\2\2\2 \u029d\3\2\2"
          + "\2\"\u02c1\3\2\2\2$\u02d9\3\2\2\2&\u02db\3\2\2\2(\u02e1\3\2\2\2*\u02ee"
          + "\3\2\2\2,\u02ff\3\2\2\2.\u032c\3\2\2\2\60\u0345\3\2\2\2\62\u0363\3\2\2"
          + "\2\64\u037c\3\2\2\2\66\u0383\3\2\2\28\u0385\3\2\2\2:\u038f\3\2\2\2<\u03bb"
          + "\3\2\2\2>\u03ea\3\2\2\2@\u03fe\3\2\2\2B\u0454\3\2\2\2D\u0459\3\2\2\2F"
          + "\u0473\3\2\2\2H\u04a0\3\2\2\2J\u0511\3\2\2\2L\u051b\3\2\2\2N\u0546\3\2"
          + "\2\2P\u0581\3\2\2\2R\u05b0\3\2\2\2T\u05c3\3\2\2\2V\u05d4\3\2\2\2X\u05e5"
          + "\3\2\2\2Z\u0609\3\2\2\2\\\u062c\3\2\2\2^\u0635\3\2\2\2`\u0657\3\2\2\2"
          + "b\u067b\3\2\2\2d\u0691\3\2\2\2f\u069b\3\2\2\2h\u069f\3\2\2\2j\u06ac\3"
          + "\2\2\2l\u06ae\3\2\2\2n\u06c2\3\2\2\2p\u06d1\3\2\2\2r\u06d4\3\2\2\2t\u06e0"
          + "\3\2\2\2v\u06f0\3\2\2\2x\u0702\3\2\2\2z\u0734\3\2\2\2|\u0745\3\2\2\2~"
          + "\u074c\3\2\2\2\u0080\u0768\3\2\2\2\u0082\u0771\3\2\2\2\u0084\u077a\3\2"
          + "\2\2\u0086\u0784\3\2\2\2\u0088\u0786\3\2\2\2\u008a\u0799\3\2\2\2\u008c"
          + "\u079b\3\2\2\2\u008e\u07b9\3\2\2\2\u0090\u07cd\3\2\2\2\u0092\u07ee\3\2"
          + "\2\2\u0094\u0800\3\2\2\2\u0096\u0808\3\2\2\2\u0098\u080a\3\2\2\2\u009a"
          + "\u080c\3\2\2\2\u009c\u0820\3\2\2\2\u009e\u0834\3\2\2\2\u00a0\u0843\3\2"
          + "\2\2\u00a2\u0852\3\2\2\2\u00a4\u0859\3\2\2\2\u00a6\u0871\3\2\2\2\u00a8"
          + "\u0886\3\2\2\2\u00aa\u0889\3\2\2\2\u00ac\u0898\3\2\2\2\u00ae\u08a6\3\2"
          + "\2\2\u00b0\u08b5\3\2\2\2\u00b2\u08c4\3\2\2\2\u00b4\u08dc\3\2\2\2\u00b6"
          + "\u08ea\3\2\2\2\u00b8\u08ec\3\2\2\2\u00ba\u08f8\3\2\2\2\u00bc\u08ff\3\2"
          + "\2\2\u00be\u0901\3\2\2\2\u00c0\u0913\3\2\2\2\u00c2\u0915\3\2\2\2\u00c4"
          + "\u0928\3\2\2\2\u00c6\u092a\3\2\2\2\u00c8\u0956\3\2\2\2\u00ca\u0963\3\2"
          + "\2\2\u00cc\u096f\3\2\2\2\u00ce\u097d\3\2\2\2\u00d0\u09a9\3\2\2\2\u00d2"
          + "\u09d8\3\2\2\2\u00d4\u0a09\3\2\2\2\u00d6\u0a0b\3\2\2\2\u00d8\u0a1b\3\2"
          + "\2\2\u00da\u0a49\3\2\2\2\u00dc\u0a4d\3\2\2\2\u00de\u0a4f\3\2\2\2\u00e0"
          + "\u0a59\3\2\2\2\u00e2\u0a64\3\2\2\2\u00e4\u0a66\3\2\2\2\u00e6\u0a76\3\2"
          + "\2\2\u00e8\u0a78\3\2\2\2\u00ea\u0a88\3\2\2\2\u00ec\u0aaa\3\2\2\2\u00ee"
          + "\u0ad9\3\2\2\2\u00f0\u0adb\3\2\2\2\u00f2\u0b18\3\2\2\2\u00f4\u0b1a\3\2"
          + "\2\2\u00f6\u0b3a\3\2\2\2\u00f8\u0b54\3\2\2\2\u00fa\u0b56\3\2\2\2\u00fc"
          + "\u0b93\3\2\2\2\u00fe\u0bba\3\2\2\2\u0100\u0c25\3\2\2\2\u0102\u0c2a\3\2"
          + "\2\2\u0104\u0c2c\3\2\2\2\u0106\u0c35\3\2\2\2\u0108\u0c3e\3\2\2\2\u010a"
          + "\u0c63\3\2\2\2\u010c\u0c86\3\2\2\2\u010e\u0c9f\3\2\2\2\u0110\u0ca2\3\2"
          + "\2\2\u0112\u0caf\3\2\2\2\u0114\u0cb1\3\2\2\2\u0116\u0cb3\3\2\2\2\u0118"
          + "\u0cb5\3\2\2\2\u011a\u0cb7\3\2\2\2\u011c\u0cb9\3\2\2\2\u011e\u0cbb\3\2"
          + "\2\2\u0120\u0cbd\3\2\2\2\u0122\u0cc4\3\2\2\2\u0124\u0cca\3\2\2\2\u0126"
          + "\u0ccc\3\2\2\2\u0128\u0cdd\3\2\2\2\u012a\u0cdf\3\2\2\2\u012c\u0ce4\3\2"
          + "\2\2\u012e\u0cea\3\2\2\2\u0130\u0cf6\3\2\2\2\u0132\u0cff\3\2\2\2\u0134"
          + "\u0d0b\3\2\2\2\u0136\u0d0d\3\2\2\2\u0138\u0d0f\3\2\2\2\u013a\u0d11\3\2"
          + "\2\2\u013c\u0d13\3\2\2\2\u013e\u0d16\3\2\2\2\u0140\u0d29\3\2\2\2\u0142"
          + "\u0d2b\3\2\2\2\u0144\u0d2d\3\2\2\2\u0146\u0d2f\3\2\2\2\u0148\u0d31\3\2"
          + "\2\2\u014a\u0d33\3\2\2\2\u014c\u0d35\3\2\2\2\u014e\u0d39\3\2\2\2\u0150"
          + "\u0d4a\3\2\2\2\u0152\u0d57\3\2\2\2\u0154\u0d61\3\2\2\2\u0156\u0d6d\3\2"
          + "\2\2\u0158\u0d6f\3\2\2\2\u015a\u0d71\3\2\2\2\u015c\u015e\5\6\4\2\u015d"
          + "\u015c\3\2\2\2\u015d\u015e\3\2\2\2\u015e\u0162\3\2\2\2\u015f\u0161\7\7"
          + "\2\2\u0160\u015f\3\2\2\2\u0161\u0164\3\2\2\2\u0162\u0160\3\2\2\2\u0162"
          + "\u0163\3\2\2\2\u0163\u0168\3\2\2\2\u0164\u0162\3\2\2\2\u0165\u0167\5\b"
          + "\5\2\u0166\u0165\3\2\2\2\u0167\u016a\3\2\2\2\u0168\u0166\3\2\2\2\u0168"
          + "\u0169\3\2\2\2\u0169\u016b\3\2\2\2\u016a\u0168\3\2\2\2\u016b\u016c\5\n"
          + "\6\2\u016c\u0170\5\f\7\2\u016d\u016f\5\22\n\2\u016e\u016d\3\2\2\2\u016f"
          + "\u0172\3\2\2\2\u0170\u016e\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0173\3\2"
          + "\2\2\u0172\u0170\3\2\2\2\u0173\u0174\7\2\2\3\u0174\3\3\2\2\2\u0175\u0177"
          + "\5\6\4\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u017b\3\2\2\2\u0178"
          + "\u017a\7\7\2\2\u0179\u0178\3\2\2\2\u017a\u017d\3\2\2\2\u017b\u0179\3\2"
          + "\2\2\u017b\u017c\3\2\2\2\u017c\u0181\3\2\2\2\u017d\u017b\3\2\2\2\u017e"
          + "\u0180\5\b\5\2\u017f\u017e\3\2\2\2\u0180\u0183\3\2\2\2\u0181\u017f\3\2"
          + "\2\2\u0181\u0182\3\2\2\2\u0182\u0184\3\2\2\2\u0183\u0181\3\2\2\2\u0184"
          + "\u0185\5\n\6\2\u0185\u018b\5\f\7\2\u0186\u0187\5\u0082B\2\u0187\u0188"
          + "\5\u0094K\2\u0188\u018a\3\2\2\2\u0189\u0186\3\2\2\2\u018a\u018d\3\2\2"
          + "\2\u018b\u0189\3\2\2\2\u018b\u018c\3\2\2\2\u018c\u018e\3\2\2\2\u018d\u018b"
          + "\3\2\2\2\u018e\u018f\7\2\2\3\u018f\5\3\2\2\2\u0190\u0192\7\3\2\2\u0191"
          + "\u0193\7\7\2\2\u0192\u0191\3\2\2\2\u0193\u0194\3\2\2\2\u0194\u0192\3\2"
          + "\2\2\u0194\u0195\3\2\2\2\u0195\7\3\2\2\2\u0196\u0197\t\2\2\2\u0197\u019b"
          + "\7?\2\2\u0198\u019a\7\7\2\2\u0199\u0198\3\2\2\2\u019a\u019d\3\2\2\2\u019b"
          + "\u0199\3\2\2\2\u019b\u019c\3\2\2\2\u019c\u019e\3\2\2\2\u019d\u019b\3\2"
          + "\2\2\u019e\u01a2\7\34\2\2\u019f\u01a1\7\7\2\2\u01a0\u019f\3\2\2\2\u01a1"
          + "\u01a4\3\2\2\2\u01a2\u01a0\3\2\2\2\u01a2\u01a3\3\2\2\2\u01a3\u01ae\3\2"
          + "\2\2\u01a4\u01a2\3\2\2\2\u01a5\u01a7\7\r\2\2\u01a6\u01a8\5\u0156\u00ac"
          + "\2\u01a7\u01a6\3\2\2\2\u01a8\u01a9\3\2\2\2\u01a9\u01a7\3\2\2\2\u01a9\u01aa"
          + "\3\2\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01ac\7\16\2\2\u01ac\u01af\3\2\2\2"
          + "\u01ad\u01af\5\u0156\u00ac\2\u01ae\u01a5\3\2\2\2\u01ae\u01ad\3\2\2\2\u01af"
          + "\u01b3\3\2\2\2\u01b0\u01b2\7\7\2\2\u01b1\u01b0\3\2\2\2\u01b2\u01b5\3\2"
          + "\2\2\u01b3\u01b1\3\2\2\2\u01b3\u01b4\3\2\2\2\u01b4\t\3\2\2\2\u01b5\u01b3"
          + "\3\2\2\2\u01b6\u01b7\7H\2\2\u01b7\u01b9\5\u015a\u00ae\2\u01b8\u01ba\5"
          + "\u0094K\2\u01b9\u01b8\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bc\3\2\2\2"
          + "\u01bb\u01b6\3\2\2\2\u01bb\u01bc\3\2\2\2\u01bc\13\3\2\2\2\u01bd\u01bf"
          + "\5\16\b\2\u01be\u01bd\3\2\2\2\u01bf\u01c2\3\2\2\2\u01c0\u01be\3\2\2\2"
          + "\u01c0\u01c1\3\2\2\2\u01c1\r\3\2\2\2\u01c2\u01c0\3\2\2\2\u01c3\u01c4\7"
          + "I\2\2\u01c4\u01c8\5\u015a\u00ae\2\u01c5\u01c6\7\t\2\2\u01c6\u01c9\7\21"
          + "\2\2\u01c7\u01c9\5\20\t\2\u01c8\u01c5\3\2\2\2\u01c8\u01c7\3\2\2\2\u01c8"
          + "\u01c9\3\2\2\2\u01c9\u01cb\3\2\2\2\u01ca\u01cc\5\u0094K\2\u01cb\u01ca"
          + "\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc\17\3\2\2\2\u01cd\u01ce\7f\2\2\u01ce"
          + "\u01cf\5\u0158\u00ad\2\u01cf\21\3\2\2\2\u01d0\u01d2\5\26\f\2\u01d1\u01d3"
          + "\5\u0096L\2\u01d2\u01d1\3\2\2\2\u01d2\u01d3\3\2\2\2\u01d3\23\3\2\2\2\u01d4"
          + "\u01d6\5\u012c\u0097\2\u01d5\u01d4\3\2\2\2\u01d5\u01d6\3\2\2\2\u01d6\u01d7"
          + "\3\2\2\2\u01d7\u01db\7P\2\2\u01d8\u01da\7\7\2\2\u01d9\u01d8\3\2\2\2\u01da"
          + "\u01dd\3\2\2\2\u01db\u01d9\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\u01de\3\2"
          + "\2\2\u01dd\u01db\3\2\2\2\u01de\u01e6\5\u0158\u00ad\2\u01df\u01e1\7\7\2"
          + "\2\u01e0\u01df\3\2\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e3"
          + "\3\2\2\2\u01e3\u01e5\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5\u01e7\5,\27\2\u01e6"
          + "\u01e2\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01eb\3\2\2\2\u01e8\u01ea\7\7"
          + "\2\2\u01e9\u01e8\3\2\2\2\u01ea\u01ed\3\2\2\2\u01eb\u01e9\3\2\2\2\u01eb"
          + "\u01ec\3\2\2\2\u01ec\u01ee\3\2\2\2\u01ed\u01eb\3\2\2\2\u01ee\u01f2\7\36"
          + "\2\2\u01ef\u01f1\7\7\2\2\u01f0\u01ef\3\2\2\2\u01f1\u01f4\3\2\2\2\u01f2"
          + "\u01f0\3\2\2\2\u01f2\u01f3\3\2\2\2\u01f3\u01f5\3\2\2\2\u01f4\u01f2\3\2"
          + "\2\2\u01f5\u01f6\5d\63\2\u01f6\25\3\2\2\2\u01f7\u01fd\5\30\r\2\u01f8\u01fd"
          + "\5X-\2\u01f9\u01fd\5@!\2\u01fa\u01fd\5H%\2\u01fb\u01fd\5\24\13\2\u01fc"
          + "\u01f7\3\2\2\2\u01fc\u01f8\3\2\2\2\u01fc\u01f9\3\2\2\2\u01fc\u01fa\3\2"
          + "\2\2\u01fc\u01fb\3\2\2\2\u01fd\27\3\2\2\2\u01fe\u0200\5\u012c\u0097\2"
          + "\u01ff\u01fe\3\2\2\2\u01ff\u0200\3\2\2\2\u0200\u020c\3\2\2\2\u0201\u020d"
          + "\7J\2\2\u0202\u0206\7L\2\2\u0203\u0205\7\7\2\2\u0204\u0203\3\2\2\2\u0205"
          + "\u0208\3\2\2\2\u0206\u0204\3\2\2\2\u0206\u0207\3\2\2\2\u0207\u020a\3\2"
          + "\2\2\u0208\u0206\3\2\2\2\u0209\u0202\3\2\2\2\u0209\u020a\3\2\2\2\u020a"
          + "\u020b\3\2\2\2\u020b\u020d\7K\2\2\u020c\u0201\3\2\2\2\u020c\u0209\3\2"
          + "\2\2\u020d\u0211\3\2\2\2\u020e\u0210\7\7\2\2\u020f\u020e\3\2\2\2\u0210"
          + "\u0213\3\2\2\2\u0211\u020f\3\2\2\2\u0211\u0212\3\2\2\2\u0212\u0214\3\2"
          + "\2\2\u0213\u0211\3\2\2\2\u0214\u021c\5\u0158\u00ad\2\u0215\u0217\7\7\2"
          + "\2\u0216\u0215\3\2\2\2\u0217\u021a\3\2\2\2\u0218\u0216\3\2\2\2\u0218\u0219"
          + "\3\2\2\2\u0219\u021b\3\2\2\2\u021a\u0218\3\2\2\2\u021b\u021d\5,\27\2\u021c"
          + "\u0218\3\2\2\2\u021c\u021d\3\2\2\2\u021d\u0225\3\2\2\2\u021e\u0220\7\7"
          + "\2\2\u021f\u021e\3\2\2\2\u0220\u0223\3\2\2\2\u0221\u021f\3\2\2\2\u0221"
          + "\u0222\3\2\2\2\u0222\u0224\3\2\2\2\u0223\u0221\3\2\2\2\u0224\u0226\5\32"
          + "\16\2\u0225\u0221\3\2\2\2\u0225\u0226\3\2\2\2\u0226\u0235\3\2\2\2\u0227"
          + "\u0229\7\7\2\2\u0228\u0227\3\2\2\2\u0229\u022c\3\2\2\2\u022a\u0228\3\2"
          + "\2\2\u022a\u022b\3\2\2\2\u022b\u022d\3\2\2\2\u022c\u022a\3\2\2\2\u022d"
          + "\u0231\7\34\2\2\u022e\u0230\7\7\2\2\u022f\u022e\3\2\2\2\u0230\u0233\3"
          + "\2\2\2\u0231\u022f\3\2\2\2\u0231\u0232\3\2\2\2\u0232\u0234\3\2\2\2\u0233"
          + "\u0231\3\2\2\2\u0234\u0236\5\"\22\2\u0235\u022a\3\2\2\2\u0235\u0236\3"
          + "\2\2\2\u0236\u023e\3\2\2\2\u0237\u0239\7\7\2\2\u0238\u0237\3\2\2\2\u0239"
          + "\u023c\3\2\2\2\u023a\u0238\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u023d\3\2"
          + "\2\2\u023c\u023a\3\2\2\2\u023d\u023f\5\60\31\2\u023e\u023a\3\2\2\2\u023e"
          + "\u023f\3\2\2\2\u023f\u024e\3\2\2\2\u0240\u0242\7\7\2\2\u0241\u0240\3\2"
          + "\2\2\u0242\u0245\3\2\2\2\u0243\u0241\3\2\2\2\u0243\u0244\3\2\2\2\u0244"
          + "\u0246\3\2\2\2\u0245\u0243\3\2\2\2\u0246\u024f\5\34\17\2\u0247\u0249\7"
          + "\7\2\2\u0248\u0247\3\2\2\2\u0249\u024c\3\2\2\2\u024a\u0248\3\2\2\2\u024a"
          + "\u024b\3\2\2\2\u024b\u024d\3\2\2\2\u024c\u024a\3\2\2\2\u024d\u024f\5^"
          + "\60\2\u024e\u0243\3\2\2\2\u024e\u024a\3\2\2\2\u024e\u024f\3\2\2\2\u024f"
          + "\31\3\2\2\2\u0250\u0252\5\u012c\u0097\2\u0251\u0250\3\2\2\2\u0251\u0252"
          + "\3\2\2\2\u0252\u0253\3\2\2\2\u0253\u0257\7Q\2\2\u0254\u0256\7\7\2\2\u0255"
          + "\u0254\3\2\2\2\u0256\u0259\3\2\2\2\u0257\u0255\3\2\2\2\u0257\u0258\3\2"
          + "\2\2\u0258\u025b\3\2\2\2\u0259\u0257\3\2\2\2\u025a\u0251\3\2\2\2\u025a"
          + "\u025b\3\2\2\2\u025b\u025c\3\2\2\2\u025c\u025d\5\36\20\2\u025d\33\3\2"
          + "\2\2\u025e\u0262\7\17\2\2\u025f\u0261\7\7\2\2\u0260\u025f\3\2\2\2\u0261"
          + "\u0264\3\2\2\2\u0262\u0260\3\2\2\2\u0262\u0263\3\2\2\2\u0263\u0265\3\2"
          + "\2\2\u0264\u0262\3\2\2\2\u0265\u0269\5\64\33\2\u0266\u0268\7\7\2\2\u0267"
          + "\u0266\3\2\2\2\u0268\u026b\3\2\2\2\u0269\u0267\3\2\2\2\u0269\u026a\3\2"
          + "\2\2\u026a\u026c\3\2\2\2\u026b\u0269\3\2\2\2\u026c\u026d\7\20\2\2\u026d"
          + "\35\3\2\2\2\u026e\u0272\7\13\2\2\u026f\u0271\7\7\2\2\u0270\u026f\3\2\2"
          + "\2\u0271\u0274\3\2\2\2\u0272\u0270\3\2\2\2\u0272\u0273\3\2\2\2\u0273\u0292"
          + "\3\2\2\2\u0274\u0272\3\2\2\2\u0275\u0286\5 \21\2\u0276\u0278\7\7\2\2\u0277"
          + "\u0276\3\2\2\2\u0278\u027b\3\2\2\2\u0279\u0277\3\2\2\2\u0279\u027a\3\2"
          + "\2\2\u027a\u027c\3\2\2\2\u027b\u0279\3\2\2\2\u027c\u0280\7\n\2\2\u027d"
          + "\u027f\7\7\2\2\u027e\u027d\3\2\2\2\u027f\u0282\3\2\2\2\u0280\u027e\3\2"
          + "\2\2\u0280\u0281\3\2\2\2\u0281\u0283\3\2\2\2\u0282\u0280\3\2\2\2\u0283"
          + "\u0285\5 \21\2\u0284\u0279\3\2\2\2\u0285\u0288\3\2\2\2\u0286\u0284\3\2"
          + "\2\2\u0286\u0287\3\2\2\2\u0287\u0290\3\2\2\2\u0288\u0286\3\2\2\2\u0289"
          + "\u028b\7\7\2\2\u028a\u0289\3\2\2\2\u028b\u028e\3\2\2\2\u028c\u028a\3\2"
          + "\2\2\u028c\u028d\3\2\2\2\u028d\u028f\3\2\2\2\u028e\u028c\3\2\2\2\u028f"
          + "\u0291\7\n\2\2\u0290\u028c\3\2\2\2\u0290\u0291\3\2\2\2\u0291\u0293\3\2"
          + "\2\2\u0292\u0275\3\2\2\2\u0292\u0293\3\2\2\2\u0293\u0297\3\2\2\2\u0294"
          + "\u0296\7\7\2\2\u0295\u0294\3\2\2\2\u0296\u0299\3\2\2\2\u0297\u0295\3\2"
          + "\2\2\u0297\u0298\3\2\2\2\u0298\u029a\3\2\2\2\u0299\u0297\3\2\2\2\u029a"
          + "\u029b\7\f\2\2\u029b\37\3\2\2\2\u029c\u029e\5\u012c\u0097\2\u029d\u029c"
          + "\3\2\2\2\u029d\u029e\3\2\2\2\u029e\u02a0\3\2\2\2\u029f\u02a1\t\3\2\2\u02a0"
          + "\u029f\3\2\2\2\u02a0\u02a1\3\2\2\2\u02a1\u02a5\3\2\2\2\u02a2\u02a4\7\7"
          + "\2\2\u02a3\u02a2\3\2\2\2\u02a4\u02a7\3\2\2\2\u02a5\u02a3\3\2\2\2\u02a5"
          + "\u02a6\3\2\2\2\u02a6\u02a8\3\2\2\2\u02a7\u02a5\3\2\2\2\u02a8\u02a9\5\u0158"
          + "\u00ad\2\u02a9\u02ad\7\34\2\2\u02aa\u02ac\7\7\2\2\u02ab\u02aa\3\2\2\2"
          + "\u02ac\u02af\3\2\2\2\u02ad\u02ab\3\2\2\2\u02ad\u02ae\3\2\2\2\u02ae\u02b0"
          + "\3\2\2\2\u02af\u02ad\3\2\2\2\u02b0\u02bf\5d\63\2\u02b1\u02b3\7\7\2\2\u02b2"
          + "\u02b1\3\2\2\2\u02b3\u02b6\3\2\2\2\u02b4\u02b2\3\2\2\2\u02b4\u02b5\3\2"
          + "\2\2\u02b5\u02b7\3\2\2\2\u02b6\u02b4\3\2\2\2\u02b7\u02bb\7\36\2\2\u02b8"
          + "\u02ba\7\7\2\2\u02b9\u02b8\3\2\2\2\u02ba\u02bd\3\2\2\2\u02bb\u02b9\3\2"
          + "\2\2\u02bb\u02bc\3\2\2\2\u02bc\u02be\3\2\2\2\u02bd\u02bb\3\2\2\2\u02be"
          + "\u02c0\5\u0098M\2\u02bf\u02b4\3\2\2\2\u02bf\u02c0\3\2\2\2\u02c0!\3\2\2"
          + "\2\u02c1\u02d2\5(\25\2\u02c2\u02c4\7\7\2\2\u02c3\u02c2\3\2\2\2\u02c4\u02c7"
          + "\3\2\2\2\u02c5\u02c3\3\2\2\2\u02c5\u02c6\3\2\2\2\u02c6\u02c8\3\2\2\2\u02c7"
          + "\u02c5\3\2\2\2\u02c8\u02cc\7\n\2\2\u02c9\u02cb\7\7\2\2\u02ca\u02c9\3\2"
          + "\2\2\u02cb\u02ce\3\2\2\2\u02cc\u02ca\3\2\2\2\u02cc\u02cd\3\2\2\2\u02cd"
          + "\u02cf\3\2\2\2\u02ce\u02cc\3\2\2\2\u02cf\u02d1\5(\25\2\u02d0\u02c5\3\2"
          + "\2\2\u02d1\u02d4\3\2\2\2\u02d2\u02d0\3\2\2\2\u02d2\u02d3\3\2\2\2\u02d3"
          + "#\3\2\2\2\u02d4\u02d2\3\2\2\2\u02d5\u02da\5&\24\2\u02d6\u02da\5*\26\2"
          + "\u02d7\u02da\5l\67\2\u02d8\u02da\5v<\2\u02d9\u02d5\3\2\2\2\u02d9\u02d6"
          + "\3\2\2\2\u02d9\u02d7\3\2\2\2\u02d9\u02d8\3\2\2\2\u02da%\3\2\2\2\u02db"
          + "\u02dc\5l\67\2\u02dc\u02dd\5\u00d0i\2\u02dd\'\3\2\2\2\u02de\u02e0\5\u014e"
          + "\u00a8\2\u02df\u02de\3\2\2\2\u02e0\u02e3\3\2\2\2\u02e1\u02df\3\2\2\2\u02e1"
          + "\u02e2\3\2\2\2\u02e2\u02e7\3\2\2\2\u02e3\u02e1\3\2\2\2\u02e4\u02e6\7\7"
          + "\2\2\u02e5\u02e4\3\2\2\2\u02e6\u02e9\3\2\2\2\u02e7\u02e5\3\2\2\2\u02e7"
          + "\u02e8\3\2\2\2\u02e8\u02ea\3\2\2\2\u02e9\u02e7\3\2\2\2\u02ea\u02eb\5$"
          + "\23\2\u02eb)\3\2\2\2\u02ec\u02ef\5l\67\2\u02ed\u02ef\5v<\2\u02ee\u02ec"
          + "\3\2\2\2\u02ee\u02ed\3\2\2\2\u02ef\u02f3\3\2\2\2\u02f0\u02f2\7\7\2\2\u02f1"
          + "\u02f0\3\2\2\2\u02f2\u02f5\3\2\2\2\u02f3\u02f1\3\2\2\2\u02f3\u02f4\3\2"
          + "\2\2\u02f4\u02f6\3\2\2\2\u02f5\u02f3\3\2\2\2\u02f6\u02fa\7R\2\2\u02f7"
          + "\u02f9\7\7\2\2\u02f8\u02f7\3\2\2\2\u02f9\u02fc\3\2\2\2\u02fa\u02f8\3\2"
          + "\2\2\u02fa\u02fb\3\2\2\2\u02fb\u02fd\3\2\2\2\u02fc\u02fa\3\2\2\2\u02fd"
          + "\u02fe\5\u0098M\2\u02fe+\3\2\2\2\u02ff\u0303\7\60\2\2\u0300\u0302\7\7"
          + "\2\2\u0301\u0300\3\2\2\2\u0302\u0305\3\2\2\2\u0303\u0301\3\2\2\2\u0303"
          + "\u0304\3\2\2\2\u0304\u0306\3\2\2\2\u0305\u0303\3\2\2\2\u0306\u0317\5."
          + "\30\2\u0307\u0309\7\7\2\2\u0308\u0307\3\2\2\2\u0309\u030c\3\2\2\2\u030a"
          + "\u0308\3\2\2\2\u030a\u030b\3\2\2\2\u030b\u030d\3\2\2\2\u030c\u030a\3\2"
          + "\2\2\u030d\u0311\7\n\2\2\u030e\u0310\7\7\2\2\u030f\u030e\3\2\2\2\u0310"
          + "\u0313\3\2\2\2\u0311\u030f\3\2\2\2\u0311\u0312\3\2\2\2\u0312\u0314\3\2"
          + "\2\2\u0313\u0311\3\2\2\2\u0314\u0316\5.\30\2\u0315\u030a\3\2\2\2\u0316"
          + "\u0319\3\2\2\2\u0317\u0315\3\2\2\2\u0317\u0318\3\2\2\2\u0318\u0321\3\2"
          + "\2\2\u0319\u0317\3\2\2\2\u031a\u031c\7\7\2\2\u031b\u031a\3\2\2\2\u031c"
          + "\u031f\3\2\2\2\u031d\u031b\3\2\2\2\u031d\u031e\3\2\2\2\u031e\u0320\3\2"
          + "\2\2\u031f\u031d\3\2\2\2\u0320\u0322\7\n\2\2\u0321\u031d\3\2\2\2\u0321"
          + "\u0322\3\2\2\2\u0322\u0326\3\2\2\2\u0323\u0325\7\7\2\2\u0324\u0323\3\2"
          + "\2\2\u0325\u0328\3\2\2\2\u0326\u0324\3\2\2\2\u0326\u0327\3\2\2\2\u0327"
          + "\u0329\3\2\2\2\u0328\u0326\3\2\2\2\u0329\u032a\7\61\2\2\u032a-\3\2\2\2"
          + "\u032b\u032d\5\u013e\u00a0\2\u032c\u032b\3\2\2\2\u032c\u032d\3\2\2\2\u032d"
          + "\u0331\3\2\2\2\u032e\u0330\7\7\2\2\u032f\u032e\3\2\2\2\u0330\u0333\3\2"
          + "\2\2\u0331\u032f\3\2\2\2\u0331\u0332\3\2\2\2\u0332\u0334\3\2\2\2\u0333"
          + "\u0331\3\2\2\2\u0334\u0343\5\u0158\u00ad\2\u0335\u0337\7\7\2\2\u0336\u0335"
          + "\3\2\2\2\u0337\u033a\3\2\2\2\u0338\u0336\3\2\2\2\u0338\u0339\3\2\2\2\u0339"
          + "\u033b\3\2\2\2\u033a\u0338\3\2\2\2\u033b\u033f\7\34\2\2\u033c\u033e\7"
          + "\7\2\2\u033d\u033c\3\2\2\2\u033e\u0341\3\2\2\2\u033f\u033d\3\2\2\2\u033f"
          + "\u0340\3\2\2\2\u0340\u0342\3\2\2\2\u0341\u033f\3\2\2\2\u0342\u0344\5d"
          + "\63\2\u0343\u0338\3\2\2\2\u0343\u0344\3\2\2\2\u0344/\3\2\2\2\u0345\u0349"
          + "\7X\2\2\u0346\u0348\7\7\2\2\u0347\u0346\3\2\2\2\u0348\u034b\3\2\2\2\u0349"
          + "\u0347\3\2\2\2\u0349\u034a\3\2\2\2\u034a\u034c\3\2\2\2\u034b\u0349\3\2"
          + "\2\2\u034c\u035d\5\62\32\2\u034d\u034f\7\7\2\2\u034e\u034d\3\2\2\2\u034f"
          + "\u0352\3\2\2\2\u0350\u034e\3\2\2\2\u0350\u0351\3\2\2\2\u0351\u0353\3\2"
          + "\2\2\u0352\u0350\3\2\2\2\u0353\u0357\7\n\2\2\u0354\u0356\7\7\2\2\u0355"
          + "\u0354\3\2\2\2\u0356\u0359\3\2\2\2\u0357\u0355\3\2\2\2\u0357\u0358\3\2"
          + "\2\2\u0358\u035a\3\2\2\2\u0359\u0357\3\2\2\2\u035a\u035c\5\62\32\2\u035b"
          + "\u0350\3\2\2\2\u035c\u035f\3\2\2\2\u035d\u035b\3\2\2\2\u035d\u035e\3\2"
          + "\2\2\u035e\61\3\2\2\2\u035f\u035d\3\2\2\2\u0360\u0362\5\u014e\u00a8\2"
          + "\u0361\u0360\3\2\2\2\u0362\u0365\3\2\2\2\u0363\u0361\3\2\2\2\u0363\u0364"
          + "\3\2\2\2\u0364\u0366\3\2\2\2\u0365\u0363\3\2\2\2\u0366\u036a\5\u0158\u00ad"
          + "\2\u0367\u0369\7\7\2\2\u0368\u0367\3\2\2\2\u0369\u036c\3\2\2\2\u036a\u0368"
          + "\3\2\2\2\u036a\u036b\3\2\2\2\u036b\u036d\3\2\2\2\u036c\u036a\3\2\2\2\u036d"
          + "\u0371\7\34\2\2\u036e\u0370\7\7\2\2\u036f\u036e\3\2\2\2\u0370\u0373\3"
          + "\2\2\2\u0371\u036f\3\2\2\2\u0371\u0372\3\2\2\2\u0372\u0374\3\2\2\2\u0373"
          + "\u0371\3\2\2\2\u0374\u0375\5d\63\2\u0375\63\3\2\2\2\u0376\u0378\5\66\34"
          + "\2\u0377\u0379\5\u0096L\2\u0378\u0377\3\2\2\2\u0378\u0379\3\2\2\2\u0379"
          + "\u037b\3\2\2\2\u037a\u0376\3\2\2\2\u037b\u037e\3\2\2\2\u037c\u037a\3\2"
          + "\2\2\u037c\u037d\3\2\2\2\u037d\65\3\2\2\2\u037e\u037c\3\2\2\2\u037f\u0384"
          + "\5\26\f\2\u0380\u0384\5:\36\2\u0381\u0384\58\35\2\u0382\u0384\5Z.\2\u0383"
          + "\u037f\3\2\2\2\u0383\u0380\3\2\2\2\u0383\u0381\3\2\2\2\u0383\u0382\3\2"
          + "\2\2\u0384\67\3\2\2\2\u0385\u0389\7T\2\2\u0386\u0388\7\7\2\2\u0387\u0386"
          + "\3\2\2\2\u0388\u038b\3\2\2\2\u0389\u0387\3\2\2\2\u0389\u038a\3\2\2\2\u038a"
          + "\u038c\3\2\2\2\u038b\u0389\3\2\2\2\u038c\u038d\5\u0088E\2\u038d9\3\2\2"
          + "\2\u038e\u0390\5\u012c\u0097\2\u038f\u038e\3\2\2\2\u038f\u0390\3\2\2\2"
          + "\u0390\u0391\3\2\2\2\u0391\u0395\7S\2\2\u0392\u0394\7\7\2\2\u0393\u0392"
          + "\3\2\2\2\u0394\u0397\3\2\2\2\u0395\u0393\3\2\2\2\u0395\u0396\3\2\2\2\u0396"
          + "\u0398\3\2\2\2\u0397\u0395\3\2\2\2\u0398\u03a0\7M\2\2\u0399\u039b\7\7"
          + "\2\2\u039a\u0399\3\2\2\2\u039b\u039e\3\2\2\2\u039c\u039a\3\2\2\2\u039c"
          + "\u039d\3\2\2\2\u039d\u039f\3\2\2\2\u039e\u039c\3\2\2\2\u039f\u03a1\5\u0158"
          + "\u00ad\2\u03a0\u039c\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1\u03b0\3\2\2\2\u03a2"
          + "\u03a4\7\7\2\2\u03a3\u03a2\3\2\2\2\u03a4\u03a7\3\2\2\2\u03a5\u03a3\3\2"
          + "\2\2\u03a5\u03a6\3\2\2\2\u03a6\u03a8\3\2\2\2\u03a7\u03a5\3\2\2\2\u03a8"
          + "\u03ac\7\34\2\2\u03a9\u03ab\7\7\2\2\u03aa\u03a9\3\2\2\2\u03ab\u03ae\3"
          + "\2\2\2\u03ac\u03aa\3\2\2\2\u03ac\u03ad\3\2\2\2\u03ad\u03af\3\2\2\2\u03ae"
          + "\u03ac\3\2\2\2\u03af\u03b1\5\"\22\2\u03b0\u03a5\3\2\2\2\u03b0\u03b1\3"
          + "\2\2\2\u03b1\u03b9\3\2\2\2\u03b2\u03b4\7\7\2\2\u03b3\u03b2\3\2\2\2\u03b4"
          + "\u03b7\3\2\2\2\u03b5\u03b3\3\2\2\2\u03b5\u03b6\3\2\2\2\u03b6\u03b8\3\2"
          + "\2\2\u03b7\u03b5\3\2\2\2\u03b8\u03ba\5\34\17\2\u03b9\u03b5\3\2\2\2\u03b9"
          + "\u03ba\3\2\2\2\u03ba;\3\2\2\2\u03bb\u03bf\7\13\2\2\u03bc\u03be\7\7\2\2"
          + "\u03bd\u03bc\3\2\2\2\u03be\u03c1\3\2\2\2\u03bf\u03bd\3\2\2\2\u03bf\u03c0"
          + "\3\2\2\2\u03c0\u03df\3\2\2\2\u03c1\u03bf\3\2\2\2\u03c2\u03d3\5> \2\u03c3"
          + "\u03c5\7\7\2\2\u03c4\u03c3\3\2\2\2\u03c5\u03c8\3\2\2\2\u03c6\u03c4\3\2"
          + "\2\2\u03c6\u03c7\3\2\2\2\u03c7\u03c9\3\2\2\2\u03c8\u03c6\3\2\2\2\u03c9"
          + "\u03cd\7\n\2\2\u03ca\u03cc\7\7\2\2\u03cb\u03ca\3\2\2\2\u03cc\u03cf\3\2"
          + "\2\2\u03cd\u03cb\3\2\2\2\u03cd\u03ce\3\2\2\2\u03ce\u03d0\3\2\2\2\u03cf"
          + "\u03cd\3\2\2\2\u03d0\u03d2\5> \2\u03d1\u03c6\3\2\2\2\u03d2\u03d5\3\2\2"
          + "\2\u03d3\u03d1\3\2\2\2\u03d3\u03d4\3\2\2\2\u03d4\u03dd\3\2\2\2\u03d5\u03d3"
          + "\3\2\2\2\u03d6\u03d8\7\7\2\2\u03d7\u03d6\3\2\2\2\u03d8\u03db\3\2\2\2\u03d9"
          + "\u03d7\3\2\2\2\u03d9\u03da\3\2\2\2\u03da\u03dc\3\2\2\2\u03db\u03d9\3\2"
          + "\2\2\u03dc\u03de\7\n\2\2\u03dd\u03d9\3\2\2\2\u03dd\u03de\3\2\2\2\u03de"
          + "\u03e0\3\2\2\2\u03df\u03c2\3\2\2\2\u03df\u03e0\3\2\2\2\u03e0\u03e4\3\2"
          + "\2\2\u03e1\u03e3\7\7\2\2\u03e2\u03e1\3\2\2\2\u03e3\u03e6\3\2\2\2\u03e4"
          + "\u03e2\3\2\2\2\u03e4\u03e5\3\2\2\2\u03e5\u03e7\3\2\2\2\u03e6\u03e4\3\2"
          + "\2\2\u03e7\u03e8\7\f\2\2\u03e8=\3\2\2\2\u03e9\u03eb\5\u012e\u0098\2\u03ea"
          + "\u03e9\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ec\3\2\2\2\u03ec\u03fb\5V"
          + ",\2\u03ed\u03ef\7\7\2\2\u03ee\u03ed\3\2\2\2\u03ef\u03f2\3\2\2\2\u03f0"
          + "\u03ee\3\2\2\2\u03f0\u03f1\3\2\2\2\u03f1\u03f3\3\2\2\2\u03f2\u03f0\3\2"
          + "\2\2\u03f3\u03f7\7\36\2\2\u03f4\u03f6\7\7\2\2\u03f5\u03f4\3\2\2\2\u03f6"
          + "\u03f9\3\2\2\2\u03f7\u03f5\3\2\2\2\u03f7\u03f8\3\2\2\2\u03f8\u03fa\3\2"
          + "\2\2\u03f9\u03f7\3\2\2\2\u03fa\u03fc\5\u0098M\2\u03fb\u03f0\3\2\2\2\u03fb"
          + "\u03fc\3\2\2\2\u03fc?\3\2\2\2\u03fd\u03ff\5\u012c\u0097\2\u03fe\u03fd"
          + "\3\2\2\2\u03fe\u03ff\3\2\2\2\u03ff\u0400\3\2\2\2\u0400\u0408\7L\2\2\u0401"
          + "\u0403\7\7\2\2\u0402\u0401\3\2\2\2\u0403\u0406\3\2\2\2\u0404\u0402\3\2"
          + "\2\2\u0404\u0405\3\2\2\2\u0405\u0407\3\2\2\2\u0406\u0404\3\2\2\2\u0407"
          + "\u0409\5,\27\2\u0408\u0404\3\2\2\2\u0408\u0409\3\2\2\2\u0409\u0419\3\2"
          + "\2\2\u040a\u040c\7\7\2\2\u040b\u040a\3\2\2\2\u040c\u040f\3\2\2\2\u040d"
          + "\u040b\3\2\2\2\u040d\u040e\3\2\2\2\u040e\u0410\3\2\2\2\u040f\u040d\3\2"
          + "\2\2\u0410\u0414\5|?\2\u0411\u0413\7\7\2\2\u0412\u0411\3\2\2\2\u0413\u0416"
          + "\3\2\2\2\u0414\u0412\3\2\2\2\u0414\u0415\3\2\2\2\u0415\u0417\3\2\2\2\u0416"
          + "\u0414\3\2\2\2\u0417\u0418\7\t\2\2\u0418\u041a\3\2\2\2\u0419\u040d\3\2"
          + "\2\2\u0419\u041a\3\2\2\2\u041a\u041e\3\2\2\2\u041b\u041d\7\7\2\2\u041c"
          + "\u041b\3\2\2\2\u041d\u0420\3\2\2\2\u041e\u041c\3\2\2\2\u041e\u041f\3\2"
          + "\2\2\u041f\u0421\3\2\2\2\u0420\u041e\3\2\2\2\u0421\u0425\5\u0158\u00ad"
          + "\2\u0422\u0424\7\7\2\2\u0423\u0422\3\2\2\2\u0424\u0427\3\2\2\2\u0425\u0423"
          + "\3\2\2\2\u0425\u0426\3\2\2\2\u0426\u0428\3\2\2\2\u0427\u0425\3\2\2\2\u0428"
          + "\u0437\5<\37\2\u0429\u042b\7\7\2\2\u042a\u0429\3\2\2\2\u042b\u042e\3\2"
          + "\2\2\u042c\u042a\3\2\2\2\u042c\u042d\3\2\2\2\u042d\u042f\3\2\2\2\u042e"
          + "\u042c\3\2\2\2\u042f\u0433\7\34\2\2\u0430\u0432\7\7\2\2\u0431\u0430\3"
          + "\2\2\2\u0432\u0435\3\2\2\2\u0433\u0431\3\2\2\2\u0433\u0434\3\2\2\2\u0434"
          + "\u0436\3\2\2\2\u0435\u0433\3\2\2\2\u0436\u0438\5d\63\2\u0437\u042c\3\2"
          + "\2\2\u0437\u0438\3\2\2\2\u0438\u0440\3\2\2\2\u0439\u043b\7\7\2\2\u043a"
          + "\u0439\3\2\2\2\u043b\u043e\3\2\2\2\u043c\u043a\3\2\2\2\u043c\u043d\3\2"
          + "\2\2\u043d\u043f\3\2\2\2\u043e\u043c\3\2\2\2\u043f\u0441\5\60\31\2\u0440"
          + "\u043c\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u0449\3\2\2\2\u0442\u0444\7\7"
          + "\2\2\u0443\u0442\3\2\2\2\u0444\u0447\3\2\2\2\u0445\u0443\3\2\2\2\u0445"
          + "\u0446\3\2\2\2\u0446\u0448\3\2\2\2\u0447\u0445\3\2\2\2\u0448\u044a\5B"
          + "\"\2\u0449\u0445\3\2\2\2\u0449\u044a\3\2\2\2\u044aA\3\2\2\2\u044b\u0455"
          + "\5\u0088E\2\u044c\u0450\7\36\2\2\u044d\u044f\7\7\2\2\u044e\u044d\3\2\2"
          + "\2\u044f\u0452\3\2\2\2\u0450\u044e\3\2\2\2\u0450\u0451\3\2\2\2\u0451\u0453"
          + "\3\2\2\2\u0452\u0450\3\2\2\2\u0453\u0455\5\u0098M\2\u0454\u044b\3\2\2"
          + "\2\u0454\u044c\3\2\2\2\u0455C\3\2\2\2\u0456\u0458\5\u014e\u00a8\2\u0457"
          + "\u0456\3\2\2\2\u0458\u045b\3\2\2\2\u0459\u0457\3\2\2\2\u0459\u045a\3\2"
          + "\2\2\u045a\u045f\3\2\2\2\u045b\u0459\3\2\2\2\u045c\u045e\7\7\2\2\u045d"
          + "\u045c\3\2\2\2\u045e\u0461\3\2\2\2\u045f\u045d\3\2\2\2\u045f\u0460\3\2"
          + "\2\2\u0460\u0462\3\2\2\2\u0461\u045f\3\2\2\2\u0462\u0471\5\u0158\u00ad"
          + "\2\u0463\u0465\7\7\2\2\u0464\u0463\3\2\2\2\u0465\u0468\3\2\2\2\u0466\u0464"
          + "\3\2\2\2\u0466\u0467\3\2\2\2\u0467\u0469\3\2\2\2\u0468\u0466\3\2\2\2\u0469"
          + "\u046d\7\34\2\2\u046a\u046c\7\7\2\2\u046b\u046a\3\2\2\2\u046c\u046f\3"
          + "\2\2\2\u046d\u046b\3\2\2\2\u046d\u046e\3\2\2\2\u046e\u0470\3\2\2\2\u046f"
          + "\u046d\3\2\2\2\u0470\u0472\5d\63\2\u0471\u0466\3\2\2\2\u0471\u0472\3\2"
          + "\2\2\u0472E\3\2\2\2\u0473\u0477\7\13\2\2\u0474\u0476\7\7\2\2\u0475\u0474"
          + "\3\2\2\2\u0476\u0479\3\2\2\2\u0477\u0475\3\2\2\2\u0477\u0478\3\2\2\2\u0478"
          + "\u047a\3\2\2\2\u0479\u0477\3\2\2\2\u047a\u048b\5D#\2\u047b\u047d\7\7\2"
          + "\2\u047c\u047b\3\2\2\2\u047d\u0480\3\2\2\2\u047e\u047c\3\2\2\2\u047e\u047f"
          + "\3\2\2\2\u047f\u0481\3\2\2\2\u0480\u047e\3\2\2\2\u0481\u0485\7\n\2\2\u0482"
          + "\u0484\7\7\2\2\u0483\u0482\3\2\2\2\u0484\u0487\3\2\2\2\u0485\u0483\3\2"
          + "\2\2\u0485\u0486\3\2\2\2\u0486\u0488\3\2\2\2\u0487\u0485\3\2\2\2\u0488"
          + "\u048a\5D#\2\u0489\u047e\3\2\2\2\u048a\u048d\3\2\2\2\u048b\u0489\3\2\2"
          + "\2\u048b\u048c\3\2\2\2\u048c\u0495\3\2\2\2\u048d\u048b\3\2\2\2\u048e\u0490"
          + "\7\7\2\2\u048f\u048e\3\2\2\2\u0490\u0493\3\2\2\2\u0491\u048f\3\2\2\2\u0491"
          + "\u0492\3\2\2\2\u0492\u0494\3\2\2\2\u0493\u0491\3\2\2\2\u0494\u0496\7\n"
          + "\2\2\u0495\u0491\3\2\2\2\u0495\u0496\3\2\2\2\u0496\u049a\3\2\2\2\u0497"
          + "\u0499\7\7\2\2\u0498\u0497\3\2\2\2\u0499\u049c\3\2\2\2\u049a\u0498\3\2"
          + "\2\2\u049a\u049b\3\2\2\2\u049b\u049d\3\2\2\2\u049c\u049a\3\2\2\2\u049d"
          + "\u049e\7\f\2\2\u049eG\3\2\2\2\u049f\u04a1\5\u012c\u0097\2\u04a0\u049f"
          + "\3\2\2\2\u04a0\u04a1\3\2\2\2\u04a1\u04a2\3\2\2\2\u04a2\u04aa\t\3\2\2\u04a3"
          + "\u04a5\7\7\2\2\u04a4\u04a3\3\2\2\2\u04a5\u04a8\3\2\2\2\u04a6\u04a4\3\2"
          + "\2\2\u04a6\u04a7\3\2\2\2\u04a7\u04a9\3\2\2\2\u04a8\u04a6\3\2\2\2\u04a9"
          + "\u04ab\5,\27\2\u04aa\u04a6\3\2\2\2\u04aa\u04ab\3\2\2\2\u04ab\u04bb\3\2"
          + "\2\2\u04ac\u04ae\7\7\2\2\u04ad\u04ac\3\2\2\2\u04ae\u04b1\3\2\2\2\u04af"
          + "\u04ad\3\2\2\2\u04af\u04b0\3\2\2\2\u04b0\u04b2\3\2\2\2\u04b1\u04af\3\2"
          + "\2\2\u04b2\u04b6\5|?\2\u04b3\u04b5\7\7\2\2\u04b4\u04b3\3\2\2\2\u04b5\u04b8"
          + "\3\2\2\2\u04b6\u04b4\3\2\2\2\u04b6\u04b7\3\2\2\2\u04b7\u04b9\3\2\2\2\u04b8"
          + "\u04b6\3\2\2\2\u04b9\u04ba\7\t\2\2\u04ba\u04bc\3\2\2\2\u04bb\u04af\3\2"
          + "\2\2\u04bb\u04bc\3\2\2\2\u04bc\u04c0\3\2\2\2\u04bd\u04bf\7\7\2\2\u04be"
          + "\u04bd\3\2\2\2\u04bf\u04c2\3\2\2\2\u04c0\u04be\3\2\2\2\u04c0\u04c1\3\2"
          + "\2\2\u04c1\u04c5\3\2\2\2\u04c2\u04c0\3\2\2\2\u04c3\u04c6\5F$\2\u04c4\u04c6"
          + "\5D#\2\u04c5\u04c3\3\2\2\2\u04c5\u04c4\3\2\2\2\u04c6\u04ce\3\2\2\2\u04c7"
          + "\u04c9\7\7\2\2\u04c8\u04c7\3\2\2\2\u04c9\u04cc\3\2\2\2\u04ca\u04c8\3\2"
          + "\2\2\u04ca\u04cb\3\2\2\2\u04cb\u04cd\3\2\2\2\u04cc\u04ca\3\2\2\2\u04cd"
          + "\u04cf\5\60\31\2\u04ce\u04ca\3\2\2\2\u04ce\u04cf\3\2\2\2\u04cf\u04e1\3"
          + "\2\2\2\u04d0\u04d2\7\7\2\2\u04d1\u04d0\3\2\2\2\u04d2\u04d5\3\2\2\2\u04d3"
          + "\u04d1\3\2\2\2\u04d3\u04d4\3\2\2\2\u04d4\u04df\3\2\2\2\u04d5\u04d3\3\2"
          + "\2\2\u04d6\u04da\7\36\2\2\u04d7\u04d9\7\7\2\2\u04d8\u04d7\3\2\2\2\u04d9"
          + "\u04dc\3\2\2\2\u04da\u04d8\3\2\2\2\u04da\u04db\3\2\2\2\u04db\u04dd\3\2"
          + "\2\2\u04dc\u04da\3\2\2\2\u04dd\u04e0\5\u0098M\2\u04de\u04e0\5J&\2\u04df"
          + "\u04d6\3\2\2\2\u04df\u04de\3\2\2\2\u04e0\u04e2\3\2\2\2\u04e1\u04d3\3\2"
          + "\2\2\u04e1\u04e2\3\2\2\2\u04e2\u04e9\3\2\2\2\u04e3\u04e5\7\7\2\2\u04e4"
          + "\u04e3\3\2\2\2\u04e5\u04e6\3\2\2\2\u04e6\u04e4\3\2\2\2\u04e6\u04e7\3\2"
          + "\2\2\u04e7\u04e8\3\2\2\2\u04e8\u04ea\7\35\2\2\u04e9\u04e4\3\2\2\2\u04e9"
          + "\u04ea\3\2\2\2\u04ea\u04ee\3\2\2\2\u04eb\u04ed\7\7\2\2\u04ec\u04eb\3\2"
          + "\2\2\u04ed\u04f0\3\2\2\2\u04ee\u04ec\3\2\2\2\u04ee\u04ef\3\2\2\2\u04ef"
          + "\u050f\3\2\2\2\u04f0\u04ee\3\2\2\2\u04f1\u04f3\5L\'\2\u04f2\u04f1\3\2"
          + "\2\2\u04f2\u04f3\3\2\2\2\u04f3\u04fe\3\2\2\2\u04f4\u04f6\7\7\2\2\u04f5"
          + "\u04f4\3\2\2\2\u04f6\u04f9\3\2\2\2\u04f7\u04f5\3\2\2\2\u04f7\u04f8\3\2"
          + "\2\2\u04f8\u04fb\3\2\2\2\u04f9\u04f7\3\2\2\2\u04fa\u04fc\5\u0094K\2\u04fb"
          + "\u04fa\3\2\2\2\u04fb\u04fc\3\2\2\2\u04fc\u04fd\3\2\2\2\u04fd\u04ff\5N"
          + "(\2\u04fe\u04f7\3\2\2\2\u04fe\u04ff\3\2\2\2\u04ff\u0510\3\2\2\2\u0500"
          + "\u0502\5N(\2\u0501\u0500\3\2\2\2\u0501\u0502\3\2\2\2\u0502\u050d\3\2\2"
          + "\2\u0503\u0505\7\7\2\2\u0504\u0503\3\2\2\2\u0505\u0508\3\2\2\2\u0506\u0504"
          + "\3\2\2\2\u0506\u0507\3\2\2\2\u0507\u050a\3\2\2\2\u0508\u0506\3\2\2\2\u0509"
          + "\u050b\5\u0094K\2\u050a\u0509\3\2\2\2\u050a\u050b\3\2\2\2\u050b\u050c"
          + "\3\2\2\2\u050c\u050e\5L\'\2\u050d\u0506\3\2\2\2\u050d\u050e\3\2\2\2\u050e"
          + "\u0510\3\2\2\2\u050f\u04f2\3\2\2\2\u050f\u0501\3\2\2\2\u0510I\3\2\2\2"
          + "\u0511\u0515\7R\2\2\u0512\u0514\7\7\2\2\u0513\u0512\3\2\2\2\u0514\u0517"
          + "\3\2\2\2\u0515\u0513\3\2\2\2\u0515\u0516\3\2\2\2\u0516\u0518\3\2\2\2\u0517"
          + "\u0515\3\2\2\2\u0518\u0519\5\u0098M\2\u0519K\3\2\2\2\u051a\u051c\5\u012c"
          + "\u0097\2\u051b\u051a\3\2\2\2\u051b\u051c\3\2\2\2\u051c\u051d\3\2\2\2\u051d"
          + "\u0543\7B\2\2\u051e\u0520\7\7\2\2\u051f\u051e\3\2\2\2\u0520\u0523\3\2"
          + "\2\2\u0521\u051f\3\2\2\2\u0521\u0522\3\2\2\2\u0522\u0524\3\2\2\2\u0523"
          + "\u0521\3\2\2\2\u0524\u0528\7\13\2\2\u0525\u0527\7\7\2\2\u0526\u0525\3"
          + "\2\2\2\u0527\u052a\3\2\2\2\u0528\u0526\3\2\2\2\u0528\u0529\3\2\2\2\u0529"
          + "\u052b\3\2\2\2\u052a\u0528\3\2\2\2\u052b\u053a\7\f\2\2\u052c\u052e\7\7"
          + "\2\2\u052d\u052c\3\2\2\2\u052e\u0531\3\2\2\2\u052f\u052d\3\2\2\2\u052f"
          + "\u0530\3\2\2\2\u0530\u0532\3\2\2\2\u0531\u052f\3\2\2\2\u0532\u0536\7\34"
          + "\2\2\u0533\u0535\7\7\2\2\u0534\u0533\3\2\2\2\u0535\u0538\3\2\2\2\u0536"
          + "\u0534\3\2\2\2\u0536\u0537\3\2\2\2\u0537\u0539\3\2\2\2\u0538\u0536\3\2"
          + "\2\2\u0539\u053b\5d\63\2\u053a\u052f\3\2\2\2\u053a\u053b\3\2\2\2\u053b"
          + "\u053f\3\2\2\2\u053c\u053e\7\7\2\2\u053d\u053c\3\2\2\2\u053e\u0541\3\2"
          + "\2\2\u053f\u053d\3\2\2\2\u053f\u0540\3\2\2\2\u0540\u0542\3\2\2\2\u0541"
          + "\u053f\3\2\2\2\u0542\u0544\5B\"\2\u0543\u0521\3\2\2\2\u0543\u0544\3\2"
          + "\2\2\u0544M\3\2\2\2\u0545\u0547\5\u012c\u0097\2\u0546\u0545\3\2\2\2\u0546"
          + "\u0547\3\2\2\2\u0547\u0548\3\2\2\2\u0548\u057f\7C\2\2\u0549\u054b\7\7"
          + "\2\2\u054a\u0549\3\2\2\2\u054b\u054e\3\2\2\2\u054c\u054a\3\2\2\2\u054c"
          + "\u054d\3\2\2\2\u054d\u054f\3\2\2\2\u054e\u054c\3\2\2\2\u054f\u0553\7\13"
          + "\2\2\u0550\u0552\7\7\2\2\u0551\u0550\3\2\2\2\u0552\u0555\3\2\2\2\u0553"
          + "\u0551\3\2\2\2\u0553\u0554\3\2\2\2\u0554\u0556\3\2\2\2\u0555\u0553\3\2"
          + "\2\2\u0556\u055e\5R*\2\u0557\u0559\7\7\2\2\u0558\u0557\3\2\2\2\u0559\u055c"
          + "\3\2\2\2\u055a\u0558\3\2\2\2\u055a\u055b\3\2\2\2\u055b\u055d\3\2\2\2\u055c"
          + "\u055a\3\2\2\2\u055d\u055f\7\n\2\2\u055e\u055a\3\2\2\2\u055e\u055f\3\2"
          + "\2\2\u055f\u0563\3\2\2\2\u0560\u0562\7\7\2\2\u0561\u0560\3\2\2\2\u0562"
          + "\u0565\3\2\2\2\u0563\u0561\3\2\2\2\u0563\u0564\3\2\2\2\u0564\u0566\3\2"
          + "\2\2\u0565\u0563\3\2\2\2\u0566\u0575\7\f\2\2\u0567\u0569\7\7\2\2\u0568"
          + "\u0567\3\2\2\2\u0569\u056c\3\2\2\2\u056a\u0568\3\2\2\2\u056a\u056b\3\2"
          + "\2\2\u056b\u056d\3\2\2\2\u056c\u056a\3\2\2\2\u056d\u0571\7\34\2\2\u056e"
          + "\u0570\7\7\2\2\u056f\u056e\3\2\2\2\u0570\u0573\3\2\2\2\u0571\u056f\3\2"
          + "\2\2\u0571\u0572\3\2\2\2\u0572\u0574\3\2\2\2\u0573\u0571\3\2\2\2\u0574"
          + "\u0576\5d\63\2\u0575\u056a\3\2\2\2\u0575\u0576\3\2\2\2\u0576\u057a\3\2"
          + "\2\2\u0577\u0579\7\7\2\2\u0578\u0577\3\2\2\2\u0579\u057c\3\2\2\2\u057a"
          + "\u0578\3\2\2\2\u057a\u057b\3\2\2\2\u057b\u057d\3\2\2\2\u057c\u057a\3\2"
          + "\2\2\u057d\u057e\5B\"\2\u057e\u0580\3\2\2\2\u057f\u054c\3\2\2\2\u057f"
          + "\u0580\3\2\2\2\u0580O\3\2\2\2\u0581\u0585\7\13\2\2\u0582\u0584\7\7\2\2"
          + "\u0583\u0582\3\2\2\2\u0584\u0587\3\2\2\2\u0585\u0583\3\2\2\2\u0585\u0586"
          + "\3\2\2\2\u0586\u05a5\3\2\2\2\u0587\u0585\3\2\2\2\u0588\u0599\5R*\2\u0589"
          + "\u058b\7\7\2\2\u058a\u0589\3\2\2\2\u058b\u058e\3\2\2\2\u058c\u058a\3\2"
          + "\2\2\u058c\u058d\3\2\2\2\u058d\u058f\3\2\2\2\u058e\u058c\3\2\2\2\u058f"
          + "\u0593\7\n\2\2\u0590\u0592\7\7\2\2\u0591\u0590\3\2\2\2\u0592\u0595\3\2"
          + "\2\2\u0593\u0591\3\2\2\2\u0593\u0594\3\2\2\2\u0594\u0596\3\2\2\2\u0595"
          + "\u0593\3\2\2\2\u0596\u0598\5R*\2\u0597\u058c\3\2\2\2\u0598\u059b\3\2\2"
          + "\2\u0599\u0597\3\2\2\2\u0599\u059a\3\2\2\2\u059a\u05a3\3\2\2\2\u059b\u0599"
          + "\3\2\2\2\u059c\u059e\7\7\2\2\u059d\u059c\3\2\2\2\u059e\u05a1\3\2\2\2\u059f"
          + "\u059d\3\2\2\2\u059f\u05a0\3\2\2\2\u05a0\u05a2\3\2\2\2\u05a1\u059f\3\2"
          + "\2\2\u05a2\u05a4\7\n\2\2\u05a3\u059f\3\2\2\2\u05a3\u05a4\3\2\2\2\u05a4"
          + "\u05a6\3\2\2\2\u05a5\u0588\3\2\2\2\u05a5\u05a6\3\2\2\2\u05a6\u05aa\3\2"
          + "\2\2\u05a7\u05a9\7\7\2\2\u05a8\u05a7\3\2\2\2\u05a9\u05ac\3\2\2\2\u05aa"
          + "\u05a8\3\2\2\2\u05aa\u05ab\3\2\2\2\u05ab\u05ad\3\2\2\2\u05ac\u05aa\3\2"
          + "\2\2\u05ad\u05ae\7\f\2\2\u05aeQ\3\2\2\2\u05af\u05b1\5\u012e\u0098\2\u05b0"
          + "\u05af\3\2\2\2\u05b0\u05b1\3\2\2\2\u05b1\u05b2\3\2\2\2\u05b2\u05c1\5T"
          + "+\2\u05b3\u05b5\7\7\2\2\u05b4\u05b3\3\2\2\2\u05b5\u05b8\3\2\2\2\u05b6"
          + "\u05b4\3\2\2\2\u05b6\u05b7\3\2\2\2\u05b7\u05b9\3\2\2\2\u05b8\u05b6\3\2"
          + "\2\2\u05b9\u05bd\7\36\2\2\u05ba\u05bc\7\7\2\2\u05bb\u05ba\3\2\2\2\u05bc"
          + "\u05bf\3\2\2\2\u05bd\u05bb\3\2\2\2\u05bd\u05be\3\2\2\2\u05be\u05c0\3\2"
          + "\2\2\u05bf\u05bd\3\2\2\2\u05c0\u05c2\5\u0098M\2\u05c1\u05b6\3\2\2\2\u05c1"
          + "\u05c2\3\2\2\2\u05c2S\3\2\2\2\u05c3\u05c7\5\u0158\u00ad\2\u05c4\u05c6"
          + "\7\7\2\2\u05c5\u05c4\3\2\2\2\u05c6\u05c9\3\2\2\2\u05c7\u05c5\3\2\2\2\u05c7"
          + "\u05c8\3\2\2\2\u05c8\u05d2\3\2\2\2\u05c9\u05c7\3\2\2\2\u05ca\u05ce\7\34"
          + "\2\2\u05cb\u05cd\7\7\2\2\u05cc\u05cb\3\2\2\2\u05cd\u05d0\3\2\2\2\u05ce"
          + "\u05cc\3\2\2\2\u05ce\u05cf\3\2\2\2\u05cf\u05d1\3\2\2\2\u05d0\u05ce\3\2"
          + "\2\2\u05d1\u05d3\5d\63\2\u05d2\u05ca\3\2\2\2\u05d2\u05d3\3\2\2\2\u05d3"
          + "U\3\2\2\2\u05d4\u05d8\5\u0158\u00ad\2\u05d5\u05d7\7\7\2\2\u05d6\u05d5"
          + "\3\2\2\2\u05d7\u05da\3\2\2\2\u05d8\u05d6\3\2\2\2\u05d8\u05d9\3\2\2\2\u05d9"
          + "\u05db\3\2\2\2\u05da\u05d8\3\2\2\2\u05db\u05df\7\34\2\2\u05dc\u05de\7"
          + "\7\2\2\u05dd\u05dc\3\2\2\2\u05de\u05e1\3\2\2\2\u05df\u05dd\3\2\2\2\u05df"
          + "\u05e0\3\2\2\2\u05e0\u05e2\3\2\2\2\u05e1\u05df\3\2\2\2\u05e2\u05e3\5d"
          + "\63\2\u05e3W\3\2\2\2\u05e4\u05e6\5\u012c\u0097\2\u05e5\u05e4\3\2\2\2\u05e5"
          + "\u05e6\3\2\2\2\u05e6\u05e7\3\2\2\2\u05e7\u05eb\7M\2\2\u05e8\u05ea\7\7"
          + "\2\2\u05e9\u05e8\3\2\2\2\u05ea\u05ed\3\2\2\2\u05eb\u05e9\3\2\2\2\u05eb"
          + "\u05ec\3\2\2\2\u05ec\u05ee\3\2\2\2\u05ed\u05eb\3\2\2\2\u05ee\u05fd\5\u0158"
          + "\u00ad\2\u05ef\u05f1\7\7\2\2\u05f0\u05ef\3\2\2\2\u05f1\u05f4\3\2\2\2\u05f2"
          + "\u05f0\3\2\2\2\u05f2\u05f3\3\2\2\2\u05f3\u05f5\3\2\2\2\u05f4\u05f2\3\2"
          + "\2\2\u05f5\u05f9\7\34\2\2\u05f6\u05f8\7\7\2\2\u05f7\u05f6\3\2\2\2\u05f8"
          + "\u05fb\3\2\2\2\u05f9\u05f7\3\2\2\2\u05f9\u05fa\3\2\2\2\u05fa\u05fc\3\2"
          + "\2\2\u05fb\u05f9\3\2\2\2\u05fc\u05fe\5\"\22\2\u05fd\u05f2\3\2\2\2\u05fd"
          + "\u05fe\3\2\2\2\u05fe\u0606\3\2\2\2\u05ff\u0601\7\7\2\2\u0600\u05ff\3\2"
          + "\2\2\u0601\u0604\3\2\2\2\u0602\u0600\3\2\2\2\u0602\u0603\3\2\2\2\u0603"
          + "\u0605\3\2\2\2\u0604\u0602\3\2\2\2\u0605\u0607\5\34\17\2\u0606\u0602\3"
          + "\2\2\2\u0606\u0607\3\2\2\2\u0607Y\3\2\2\2\u0608\u060a\5\u012c\u0097\2"
          + "\u0609\u0608\3\2\2\2\u0609\u060a\3\2\2\2\u060a\u060b\3\2\2\2\u060b\u060f"
          + "\7Q\2\2\u060c\u060e\7\7\2\2\u060d\u060c\3\2\2\2\u060e\u0611\3\2\2\2\u060f"
          + "\u060d\3\2\2\2\u060f\u0610\3\2\2\2\u0610\u0612\3\2\2\2\u0611\u060f\3\2"
          + "\2\2\u0612\u0621\5<\37\2\u0613\u0615\7\7\2\2\u0614\u0613\3\2\2\2\u0615"
          + "\u0618\3\2\2\2\u0616\u0614\3\2\2\2\u0616\u0617\3\2\2\2\u0617\u0619\3\2"
          + "\2\2\u0618\u0616\3\2\2\2\u0619\u061d\7\34\2\2\u061a\u061c\7\7\2\2\u061b"
          + "\u061a\3\2\2\2\u061c\u061f\3\2\2\2\u061d\u061b\3\2\2\2\u061d\u061e\3\2"
          + "\2\2\u061e\u0620\3\2\2\2\u061f\u061d\3\2\2\2\u0620\u0622\5\\/\2\u0621"
          + "\u0616\3\2\2\2\u0621\u0622\3\2\2\2\u0622\u0626\3\2\2\2\u0623\u0625\7\7"
          + "\2\2\u0624\u0623\3\2\2\2\u0625\u0628\3\2\2\2\u0626\u0624\3\2\2\2\u0626"
          + "\u0627\3\2\2\2\u0627\u062a\3\2\2\2\u0628\u0626\3\2\2\2\u0629\u062b\5\u0088"
          + "E\2\u062a\u0629\3\2\2\2\u062a\u062b\3\2\2\2\u062b[\3\2\2\2\u062c\u0630"
          + "\t\4\2\2\u062d\u062f\7\7\2\2\u062e\u062d\3\2\2\2\u062f\u0632\3\2\2\2\u0630"
          + "\u062e\3\2\2\2\u0630\u0631\3\2\2\2\u0631\u0633\3\2\2\2\u0632\u0630\3\2"
          + "\2\2\u0633\u0634\5\u00d0i\2\u0634]\3\2\2\2\u0635\u0639\7\17\2\2\u0636"
          + "\u0638\7\7\2\2\u0637\u0636\3\2\2\2\u0638\u063b\3\2\2\2\u0639\u0637\3\2"
          + "\2\2\u0639\u063a\3\2\2\2\u063a\u063d\3\2\2\2\u063b\u0639\3\2\2\2\u063c"
          + "\u063e\5`\61\2\u063d\u063c\3\2\2\2\u063d\u063e\3\2\2\2\u063e\u064d\3\2"
          + "\2\2\u063f\u0641\7\7\2\2\u0640\u063f\3\2\2\2\u0641\u0644\3\2\2\2\u0642"
          + "\u0640\3\2\2\2\u0642\u0643\3\2\2\2\u0643\u0645\3\2\2\2\u0644\u0642\3\2"
          + "\2\2\u0645\u0649\7\35\2\2\u0646\u0648\7\7\2\2\u0647\u0646\3\2\2\2\u0648"
          + "\u064b\3\2\2\2\u0649\u0647\3\2\2\2\u0649\u064a\3\2\2\2\u064a\u064c\3\2"
          + "\2\2\u064b\u0649\3\2\2\2\u064c\u064e\5\64\33\2\u064d\u0642\3\2\2\2\u064d"
          + "\u064e\3\2\2\2\u064e\u0652\3\2\2\2\u064f\u0651\7\7\2\2\u0650\u064f\3\2"
          + "\2\2\u0651\u0654\3\2\2\2\u0652\u0650\3\2\2\2\u0652\u0653\3\2\2\2\u0653"
          + "\u0655\3\2\2\2\u0654\u0652\3\2\2\2\u0655\u0656\7\20\2\2\u0656_\3\2\2\2"
          + "\u0657\u0668\5b\62\2\u0658\u065a\7\7\2\2\u0659\u0658\3\2\2\2\u065a\u065d"
          + "\3\2\2\2\u065b\u0659\3\2\2\2\u065b\u065c\3\2\2\2\u065c\u065e\3\2\2\2\u065d"
          + "\u065b\3\2\2\2\u065e\u0662\7\n\2\2\u065f\u0661\7\7\2\2\u0660\u065f\3\2"
          + "\2\2\u0661\u0664\3\2\2\2\u0662\u0660\3\2\2\2\u0662\u0663\3\2\2\2\u0663"
          + "\u0665\3\2\2\2\u0664\u0662\3\2\2\2\u0665\u0667\5b\62\2\u0666\u065b\3\2"
          + "\2\2\u0667\u066a\3\2\2\2\u0668\u0666\3\2\2\2\u0668\u0669\3\2\2\2\u0669"
          + "\u066e\3\2\2\2\u066a\u0668\3\2\2\2\u066b\u066d\7\7\2\2\u066c\u066b\3\2"
          + "\2\2\u066d\u0670\3\2\2\2\u066e\u066c\3\2\2\2\u066e\u066f\3\2\2\2\u066f"
          + "\u0672\3\2\2\2\u0670\u066e\3\2\2\2\u0671\u0673\7\n\2\2\u0672\u0671\3\2"
          + "\2\2\u0672\u0673\3\2\2\2\u0673a\3\2\2\2\u0674\u0678\5\u012c\u0097\2\u0675"
          + "\u0677\7\7\2\2\u0676\u0675\3\2\2\2\u0677\u067a\3\2\2\2\u0678\u0676\3\2"
          + "\2\2\u0678\u0679\3\2\2\2\u0679\u067c\3\2\2\2\u067a\u0678\3\2\2\2\u067b"
          + "\u0674\3\2\2\2\u067b\u067c\3\2\2\2\u067c\u067d\3\2\2\2\u067d\u0685\5\u0158"
          + "\u00ad\2\u067e\u0680\7\7\2\2\u067f\u067e\3\2\2\2\u0680\u0683\3\2\2\2\u0681"
          + "\u067f\3\2\2\2\u0681\u0682\3\2\2\2\u0682\u0684\3\2\2\2\u0683\u0681\3\2"
          + "\2\2\u0684\u0686\5\u00d0i\2\u0685\u0681\3\2\2\2\u0685\u0686\3\2\2\2\u0686"
          + "\u068e\3\2\2\2\u0687\u0689\7\7\2\2\u0688\u0687\3\2\2\2\u0689\u068c\3\2"
          + "\2\2\u068a\u0688\3\2\2\2\u068a\u068b\3\2\2\2\u068b\u068d\3\2\2\2\u068c"
          + "\u068a\3\2\2\2\u068d\u068f\5\34\17\2\u068e\u068a\3\2\2\2\u068e\u068f\3"
          + "\2\2\2\u068fc\3\2\2\2\u0690\u0692\5\u0132\u009a\2\u0691\u0690\3\2\2\2"
          + "\u0691\u0692\3\2\2\2\u0692\u0697\3\2\2\2\u0693\u0698\5z>\2\u0694\u0698"
          + "\5h\65\2\u0695\u0698\5f\64\2\u0696\u0698\5v<\2\u0697\u0693\3\2\2\2\u0697"
          + "\u0694\3\2\2\2\u0697\u0695\3\2\2\2\u0697\u0696\3\2\2\2\u0698e\3\2\2\2"
          + "\u0699\u069c\5l\67\2\u069a\u069c\7l\2\2\u069b\u0699\3\2\2\2\u069b\u069a"
          + "\3\2\2\2\u069cg\3\2\2\2\u069d\u06a0\5f\64\2\u069e\u06a0\5z>\2\u069f\u069d"
          + "\3\2\2\2\u069f\u069e\3\2\2\2\u06a0\u06a4\3\2\2\2\u06a1\u06a3\7\7\2\2\u06a2"
          + "\u06a1\3\2\2\2\u06a3\u06a6\3\2\2\2\u06a4\u06a2\3\2\2\2\u06a4\u06a5\3\2"
          + "\2\2\u06a5\u06a8\3\2\2\2\u06a6\u06a4\3\2\2\2\u06a7\u06a9\5j\66\2\u06a8"
          + "\u06a7\3\2\2\2\u06a9\u06aa\3\2\2\2\u06aa\u06a8\3\2\2\2\u06aa\u06ab\3\2"
          + "\2\2\u06abi\3\2\2\2\u06ac\u06ad\t\5\2\2\u06adk\3\2\2\2\u06ae\u06bf\5n"
          + "8\2\u06af\u06b1\7\7\2\2\u06b0\u06af\3\2\2\2\u06b1\u06b4\3\2\2\2\u06b2"
          + "\u06b0\3\2\2\2\u06b2\u06b3\3\2\2\2\u06b3\u06b5\3\2\2\2\u06b4\u06b2\3\2"
          + "\2\2\u06b5\u06b9\7\t\2\2\u06b6\u06b8\7\7\2\2\u06b7\u06b6\3\2\2\2\u06b8"
          + "\u06bb\3\2\2\2\u06b9\u06b7\3\2\2\2\u06b9\u06ba\3\2\2\2\u06ba\u06bc\3\2"
          + "\2\2\u06bb\u06b9\3\2\2\2\u06bc\u06be\5n8\2\u06bd\u06b2\3\2\2\2\u06be\u06c1"
          + "\3\2\2\2\u06bf\u06bd\3\2\2\2\u06bf\u06c0\3\2\2\2\u06c0m\3\2\2\2\u06c1"
          + "\u06bf\3\2\2\2\u06c2\u06ca\5\u0158\u00ad\2\u06c3\u06c5\7\7\2\2\u06c4\u06c3"
          + "\3\2\2\2\u06c5\u06c8\3\2\2\2\u06c6\u06c4\3\2\2\2\u06c6\u06c7\3\2\2\2\u06c7"
          + "\u06c9\3\2\2\2\u06c8\u06c6\3\2\2\2\u06c9\u06cb\5\u00ceh\2\u06ca\u06c6"
          + "\3\2\2\2\u06ca\u06cb\3\2\2\2\u06cbo\3\2\2\2\u06cc\u06ce\5r:\2\u06cd\u06cc"
          + "\3\2\2\2\u06cd\u06ce\3\2\2\2\u06ce\u06cf\3\2\2\2\u06cf\u06d2\5d\63\2\u06d0"
          + "\u06d2\7\21\2\2\u06d1\u06cd\3\2\2\2\u06d1\u06d0\3\2\2\2\u06d2q\3\2\2\2"
          + "\u06d3\u06d5\5t;\2\u06d4\u06d3\3\2\2\2\u06d5\u06d6\3\2\2\2\u06d6\u06d4"
          + "\3\2\2\2\u06d6\u06d7\3\2\2\2\u06d7s\3\2\2\2\u06d8\u06dc\5\u013c\u009f"
          + "\2\u06d9\u06db\7\7\2\2\u06da\u06d9\3\2\2\2\u06db\u06de\3\2\2\2\u06dc\u06da"
          + "\3\2\2\2\u06dc\u06dd\3\2\2\2\u06dd\u06e1\3\2\2\2\u06de\u06dc\3\2\2\2\u06df"
          + "\u06e1\5\u014e\u00a8\2\u06e0\u06d8\3\2\2\2\u06e0\u06df\3\2\2\2\u06e1u"
          + "\3\2\2\2\u06e2\u06e6\5|?\2\u06e3\u06e5\7\7\2\2\u06e4\u06e3\3\2\2\2\u06e5"
          + "\u06e8\3\2\2\2\u06e6\u06e4\3\2\2\2\u06e6\u06e7\3\2\2\2\u06e7\u06e9\3\2"
          + "\2\2\u06e8\u06e6\3\2\2\2\u06e9\u06ed\7\t\2\2\u06ea\u06ec\7\7\2\2\u06eb"
          + "\u06ea\3\2\2\2\u06ec\u06ef\3\2\2\2\u06ed\u06eb\3\2\2\2\u06ed\u06ee\3\2"
          + "\2\2\u06ee\u06f1\3\2\2\2\u06ef\u06ed\3\2\2\2\u06f0\u06e2\3\2\2\2\u06f0"
          + "\u06f1\3\2\2\2\u06f1\u06f2\3\2\2\2\u06f2\u06f6\5x=\2\u06f3\u06f5\7\7\2"
          + "\2\u06f4\u06f3\3\2\2\2\u06f5\u06f8\3\2\2\2\u06f6\u06f4\3\2\2\2\u06f6\u06f7"
          + "\3\2\2\2\u06f7\u06f9\3\2\2\2\u06f8\u06f6\3\2\2\2\u06f9\u06fd\7$\2\2\u06fa"
          + "\u06fc\7\7\2\2\u06fb\u06fa\3\2\2\2\u06fc\u06ff\3\2\2\2\u06fd\u06fb\3\2"
          + "\2\2\u06fd\u06fe\3\2\2\2\u06fe\u0700\3\2\2\2\u06ff\u06fd\3\2\2\2\u0700"
          + "\u0701\5d\63\2\u0701w\3\2\2\2\u0702\u0706\7\13\2\2\u0703\u0705\7\7\2\2"
          + "\u0704\u0703\3\2\2\2\u0705\u0708\3\2\2\2\u0706\u0704\3\2\2\2\u0706\u0707"
          + "\3\2\2\2\u0707\u070b\3\2\2\2\u0708\u0706\3\2\2\2\u0709\u070c\5V,\2\u070a"
          + "\u070c\5d\63\2\u070b\u0709\3\2\2\2\u070b\u070a\3\2\2\2\u070b\u070c\3\2"
          + "\2\2\u070c\u0720\3\2\2\2\u070d\u070f\7\7\2\2\u070e\u070d\3\2\2\2\u070f"
          + "\u0712\3\2\2\2\u0710\u070e\3\2\2\2\u0710\u0711\3\2\2\2\u0711\u0713\3\2"
          + "\2\2\u0712\u0710\3\2\2\2\u0713\u0717\7\n\2\2\u0714\u0716\7\7\2\2\u0715"
          + "\u0714\3\2\2\2\u0716\u0719\3\2\2\2\u0717\u0715\3\2\2\2\u0717\u0718\3\2"
          + "\2\2\u0718\u071c\3\2\2\2\u0719\u0717\3\2\2\2\u071a\u071d\5V,\2\u071b\u071d"
          + "\5d\63\2\u071c\u071a\3\2\2\2\u071c\u071b\3\2\2\2\u071d\u071f\3\2\2\2\u071e"
          + "\u0710\3\2\2\2\u071f\u0722\3\2\2\2\u0720\u071e\3\2\2\2\u0720\u0721\3\2"
          + "\2\2\u0721\u072a\3\2\2\2\u0722\u0720\3\2\2\2\u0723\u0725\7\7\2\2\u0724"
          + "\u0723\3\2\2\2\u0725\u0728\3\2\2\2\u0726\u0724\3\2\2\2\u0726\u0727\3\2"
          + "\2\2\u0727\u0729\3\2\2\2\u0728\u0726\3\2\2\2\u0729\u072b\7\n\2\2\u072a"
          + "\u0726\3\2\2\2\u072a\u072b\3\2\2\2\u072b\u072f\3\2\2\2\u072c\u072e\7\7"
          + "\2\2\u072d\u072c\3\2\2\2\u072e\u0731\3\2\2\2\u072f\u072d\3\2\2\2\u072f"
          + "\u0730\3\2\2\2\u0730\u0732\3\2\2\2\u0731\u072f\3\2\2\2\u0732\u0733\7\f"
          + "\2\2\u0733y\3\2\2\2\u0734\u0738\7\13\2\2\u0735\u0737\7\7\2\2\u0736\u0735"
          + "\3\2\2\2\u0737\u073a\3\2\2\2\u0738\u0736\3\2\2\2\u0738\u0739\3\2\2\2\u0739"
          + "\u073b\3\2\2\2\u073a\u0738\3\2\2\2\u073b\u073f\5d\63\2\u073c\u073e\7\7"
          + "\2\2\u073d\u073c\3\2\2\2\u073e\u0741\3\2\2\2\u073f\u073d\3\2\2\2\u073f"
          + "\u0740\3\2\2\2\u0740\u0742\3\2\2\2\u0741\u073f\3\2\2\2\u0742\u0743\7\f"
          + "\2\2\u0743{\3\2\2\2\u0744\u0746\5\u0132\u009a\2\u0745\u0744\3\2\2\2\u0745"
          + "\u0746\3\2\2\2\u0746\u074a\3\2\2\2\u0747\u074b\5z>\2\u0748\u074b\5h\65"
          + "\2\u0749\u074b\5f\64\2\u074a\u0747\3\2\2\2\u074a\u0748\3\2\2\2\u074a\u0749"
          + "\3\2\2\2\u074b}\3\2\2\2\u074c\u0750\7\13\2\2\u074d\u074f\7\7\2\2\u074e"
          + "\u074d\3\2\2\2\u074f\u0752\3\2\2\2\u0750\u074e\3\2\2\2\u0750\u0751\3\2"
          + "\2\2\u0751\u0755\3\2\2\2\u0752\u0750\3\2\2\2\u0753\u0756\5l\67\2\u0754"
          + "\u0756\5~@\2\u0755\u0753\3\2\2\2\u0755\u0754\3\2\2\2\u0756\u075a\3\2\2"
          + "\2\u0757\u0759\7\7\2\2\u0758\u0757\3\2\2\2\u0759\u075c\3\2\2\2\u075a\u0758"
          + "\3\2\2\2\u075a\u075b\3\2\2\2\u075b\u075d\3\2\2\2\u075c\u075a\3\2\2\2\u075d"
          + "\u075e\7\f\2\2\u075e\177\3\2\2\2\u075f\u0765\5\u0082B\2\u0760\u0761\5"
          + "\u0096L\2\u0761\u0762\5\u0082B\2\u0762\u0764\3\2\2\2\u0763\u0760\3\2\2"
          + "\2\u0764\u0767\3\2\2\2\u0765\u0763\3\2\2\2\u0765\u0766\3\2\2\2\u0766\u0769"
          + "\3\2\2\2\u0767\u0765\3\2\2\2\u0768\u075f\3\2\2\2\u0768\u0769\3\2\2\2\u0769"
          + "\u076b\3\2\2\2\u076a\u076c\5\u0096L\2\u076b\u076a\3\2\2\2\u076b\u076c"
          + "\3\2\2\2\u076c\u0081\3\2\2\2\u076d\u0770\5\u0084C\2\u076e\u0770\5\u014e"
          + "\u00a8\2\u076f\u076d\3\2\2\2\u076f\u076e\3\2\2\2\u0770\u0773\3\2\2\2\u0771"
          + "\u076f\3\2\2\2\u0771\u0772\3\2\2\2\u0772\u0778\3\2\2\2\u0773\u0771\3\2"
          + "\2\2\u0774\u0779\5\26\f\2\u0775\u0779\5\u0092J\2\u0776\u0779\5\u008aF"
          + "\2\u0777\u0779\5\u0098M\2\u0778\u0774\3\2\2\2\u0778\u0775\3\2\2\2\u0778"
          + "\u0776\3\2\2\2\u0778\u0777\3\2\2\2\u0779\u0083\3\2\2\2\u077a\u077b\5\u0158"
          + "\u00ad\2\u077b\u077f\t\6\2\2\u077c\u077e\7\7\2\2\u077d\u077c\3\2\2\2\u077e"
          + "\u0781\3\2\2\2\u077f\u077d\3\2\2\2\u077f\u0780\3\2\2\2\u0780\u0085\3\2"
          + "\2\2\u0781\u077f\3\2\2\2\u0782\u0785\5\u0088E\2\u0783\u0785\5\u0082B\2"
          + "\u0784\u0782\3\2\2\2\u0784\u0783\3\2\2\2\u0785\u0087\3\2\2\2\u0786\u078a"
          + "\7\17\2\2\u0787\u0789\7\7\2\2\u0788\u0787\3\2\2\2\u0789\u078c\3\2\2\2"
          + "\u078a\u0788\3\2\2\2\u078a\u078b\3\2\2\2\u078b\u078d\3\2\2\2\u078c\u078a"
          + "\3\2\2\2\u078d\u0791\5\u0080A\2\u078e\u0790\7\7\2\2\u078f\u078e\3\2\2"
          + "\2\u0790\u0793\3\2\2\2\u0791\u078f\3\2\2\2\u0791\u0792\3\2\2\2\u0792\u0794"
          + "\3\2\2\2\u0793\u0791\3\2\2\2\u0794\u0795\7\20\2\2\u0795\u0089\3\2\2\2"
          + "\u0796\u079a\5\u008cG\2\u0797\u079a\5\u008eH\2\u0798\u079a\5\u0090I\2"
          + "\u0799\u0796\3\2\2\2\u0799\u0797\3\2\2\2\u0799\u0798\3\2\2\2\u079a\u008b"
          + "\3\2\2\2\u079b\u079f\7_\2\2\u079c\u079e\7\7\2\2\u079d\u079c\3\2\2\2\u079e"
          + "\u07a1\3\2\2\2\u079f\u079d\3\2\2\2\u079f\u07a0\3\2\2\2\u07a0\u07a2\3\2"
          + "\2\2\u07a1\u079f\3\2\2\2\u07a2\u07a6\7\13\2\2\u07a3\u07a5\5\u014e\u00a8"
          + "\2\u07a4\u07a3\3\2\2\2\u07a5\u07a8\3\2\2\2\u07a6\u07a4\3\2\2\2\u07a6\u07a7"
          + "\3\2\2\2\u07a7\u07ab\3\2\2\2\u07a8\u07a6\3\2\2\2\u07a9\u07ac\5D#\2\u07aa"
          + "\u07ac\5F$\2\u07ab\u07a9\3\2\2\2\u07ab\u07aa\3\2\2\2\u07ac\u07ad\3\2\2"
          + "\2\u07ad\u07ae\7h\2\2\u07ae\u07af\5\u0098M\2\u07af\u07b3\7\f\2\2\u07b0"
          + "\u07b2\7\7\2\2\u07b1\u07b0\3\2\2\2\u07b2\u07b5\3\2\2\2\u07b3\u07b1\3\2"
          + "\2\2\u07b3\u07b4\3\2\2\2\u07b4\u07b7\3\2\2\2\u07b5\u07b3\3\2\2\2\u07b6"
          + "\u07b8\5\u0086D\2\u07b7\u07b6\3\2\2\2\u07b7\u07b8\3\2\2\2\u07b8\u008d"
          + "\3\2\2\2\u07b9\u07bd\7a\2\2\u07ba\u07bc\7\7\2\2\u07bb\u07ba\3\2\2\2\u07bc"
          + "\u07bf\3\2\2\2\u07bd\u07bb\3\2\2\2\u07bd\u07be\3\2\2\2\u07be\u07c0\3\2"
          + "\2\2\u07bf\u07bd\3\2\2\2\u07c0\u07c1\7\13\2\2\u07c1\u07c2\5\u0098M\2\u07c2"
          + "\u07c6\7\f\2\2\u07c3\u07c5\7\7\2\2\u07c4\u07c3\3\2\2\2\u07c5\u07c8\3\2"
          + "\2\2\u07c6\u07c4\3\2\2\2\u07c6\u07c7\3\2\2\2\u07c7\u07cb\3\2\2\2\u07c8"
          + "\u07c6\3\2\2\2\u07c9\u07cc\5\u0086D\2\u07ca\u07cc\7\35\2\2\u07cb\u07c9"
          + "\3\2\2\2\u07cb\u07ca\3\2\2\2\u07cc\u008f\3\2\2\2\u07cd\u07d1\7`\2\2\u07ce"
          + "\u07d0\7\7\2\2\u07cf\u07ce\3\2\2\2\u07d0\u07d3\3\2\2\2\u07d1\u07cf\3\2"
          + "\2\2\u07d1\u07d2\3\2\2\2\u07d2\u07d5\3\2\2\2\u07d3\u07d1\3\2\2\2\u07d4"
          + "\u07d6\5\u0086D\2\u07d5\u07d4\3\2\2\2\u07d5\u07d6\3\2\2\2\u07d6\u07da"
          + "\3\2\2\2\u07d7\u07d9\7\7\2\2\u07d8\u07d7\3\2\2\2\u07d9\u07dc\3\2\2\2\u07da"
          + "\u07d8\3\2\2\2\u07da\u07db\3\2\2\2\u07db\u07dd\3\2\2\2\u07dc\u07da\3\2"
          + "\2\2\u07dd\u07e1\7a\2\2\u07de\u07e0\7\7\2\2\u07df\u07de\3\2\2\2\u07e0"
          + "\u07e3\3\2\2\2\u07e1\u07df\3\2\2\2\u07e1\u07e2\3\2\2\2\u07e2\u07e4\3\2"
          + "\2\2\u07e3\u07e1\3\2\2\2\u07e4\u07e5\7\13\2\2\u07e5\u07e6\5\u0098M\2\u07e6"
          + "\u07e7\7\f\2\2\u07e7\u0091\3\2\2\2\u07e8\u07e9\5\u00bc_\2\u07e9\u07ea"
          + "\7\36\2\2\u07ea\u07ef\3\2\2\2\u07eb\u07ec\5\u00c0a\2\u07ec\u07ed\5\u0112"
          + "\u008a\2\u07ed\u07ef\3\2\2\2\u07ee\u07e8\3\2\2\2\u07ee\u07eb\3\2\2\2\u07ef"
          + "\u07f3\3\2\2\2\u07f0\u07f2\7\7\2\2\u07f1\u07f0\3\2\2\2\u07f2\u07f5\3\2"
          + "\2\2\u07f3\u07f1\3\2\2\2\u07f3\u07f4\3\2\2\2\u07f4\u07f6\3\2\2\2\u07f5"
          + "\u07f3\3\2\2\2\u07f6\u07f7\5\u0098M\2\u07f7\u0093\3\2\2\2\u07f8\u07fc"
          + "\t\7\2\2\u07f9\u07fb\7\7\2\2\u07fa\u07f9\3\2\2\2\u07fb\u07fe\3\2\2\2\u07fc"
          + "\u07fa\3\2\2\2\u07fc\u07fd\3\2\2\2\u07fd\u0801\3\2\2\2\u07fe\u07fc\3\2"
          + "\2\2\u07ff\u0801\7\2\2\3\u0800\u07f8\3\2\2\2\u0800\u07ff\3\2\2\2\u0801"
          + "\u0095\3\2\2\2\u0802\u0804\t\7\2\2\u0803\u0802\3\2\2\2\u0804\u0805\3\2"
          + "\2\2\u0805\u0803\3\2\2\2\u0805\u0806\3\2\2\2\u0806\u0809\3\2\2\2\u0807"
          + "\u0809\7\2\2\3\u0808\u0803\3\2\2\2\u0808\u0807\3\2\2\2\u0809\u0097\3\2"
          + "\2\2\u080a\u080b\5\u009aN\2\u080b\u0099\3\2\2\2\u080c\u081d\5\u009cO\2"
          + "\u080d\u080f\7\7\2\2\u080e\u080d\3\2\2\2\u080f\u0812\3\2\2\2\u0810\u080e"
          + "\3\2\2\2\u0810\u0811\3\2\2\2\u0811\u0813\3\2\2\2\u0812\u0810\3\2\2\2\u0813"
          + "\u0817\7\31\2\2\u0814\u0816\7\7\2\2\u0815\u0814\3\2\2\2\u0816\u0819\3"
          + "\2\2\2\u0817\u0815\3\2\2\2\u0817\u0818\3\2\2\2\u0818\u081a\3\2\2\2\u0819"
          + "\u0817\3\2\2\2\u081a\u081c\5\u009cO\2\u081b\u0810\3\2\2\2\u081c\u081f"
          + "\3\2\2\2\u081d\u081b\3\2\2\2\u081d\u081e\3\2\2\2\u081e\u009b\3\2\2\2\u081f"
          + "\u081d\3\2\2\2\u0820\u0831\5\u009eP\2\u0821\u0823\7\7\2\2\u0822\u0821"
          + "\3\2\2\2\u0823\u0826\3\2\2\2\u0824\u0822\3\2\2\2\u0824\u0825\3\2\2\2\u0825"
          + "\u0827\3\2\2\2\u0826\u0824\3\2\2\2\u0827\u082b\7\30\2\2\u0828\u082a\7"
          + "\7\2\2\u0829\u0828\3\2\2\2\u082a\u082d\3\2\2\2\u082b\u0829\3\2\2\2\u082b"
          + "\u082c\3\2\2\2\u082c\u082e\3\2\2\2\u082d\u082b\3\2\2\2\u082e\u0830\5\u009e"
          + "P\2\u082f\u0824\3\2\2\2\u0830\u0833\3\2\2\2\u0831\u082f\3\2\2\2\u0831"
          + "\u0832\3\2\2\2\u0832\u009d\3\2\2\2\u0833\u0831\3\2\2\2\u0834\u0840\5\u00a0"
          + "Q\2\u0835\u0839\5\u0114\u008b\2\u0836\u0838\7\7\2\2\u0837\u0836\3\2\2"
          + "\2\u0838\u083b\3\2\2\2\u0839\u0837\3\2\2\2\u0839\u083a\3\2\2\2\u083a\u083c"
          + "\3\2\2\2\u083b\u0839\3\2\2\2\u083c\u083d\5\u00a0Q\2\u083d\u083f\3\2\2"
          + "\2\u083e\u0835\3\2\2\2\u083f\u0842\3\2\2\2\u0840\u083e\3\2\2\2\u0840\u0841"
          + "\3\2\2\2\u0841\u009f\3\2\2\2\u0842\u0840\3\2\2\2\u0843\u084f\5\u00a2R"
          + "\2\u0844\u0848\5\u0116\u008c\2\u0845\u0847\7\7\2\2\u0846\u0845\3\2\2\2"
          + "\u0847\u084a\3\2\2\2\u0848\u0846\3\2\2\2\u0848\u0849\3\2\2\2\u0849\u084b"
          + "\3\2\2\2\u084a\u0848\3\2\2\2\u084b\u084c\5\u00a2R\2\u084c\u084e\3\2\2"
          + "\2\u084d\u0844\3\2\2\2\u084e\u0851\3\2\2\2\u084f\u084d\3\2\2\2\u084f\u0850"
          + "\3\2\2\2\u0850\u00a1\3\2\2\2\u0851";
  private static final String _serializedATNSegment1 =
      "\u084f\3\2\2\2\u0852\u0856\5\u00a4S\2\u0853\u0855\5\u00caf\2\u0854\u0853"
          + "\3\2\2\2\u0855\u0858\3\2\2\2\u0856\u0854\3\2\2\2\u0856\u0857\3\2\2\2\u0857"
          + "\u00a3\3\2\2\2\u0858\u0856\3\2\2\2\u0859\u086e\5\u00a6T\2\u085a\u085e"
          + "\5\u0118\u008d\2\u085b\u085d\7\7\2\2\u085c\u085b\3\2\2\2\u085d\u0860\3"
          + "\2\2\2\u085e\u085c\3\2\2\2\u085e\u085f\3\2\2\2\u085f\u0861\3\2\2\2\u0860"
          + "\u085e\3\2\2\2\u0861\u0862\5\u00a6T\2\u0862\u086d\3\2\2\2\u0863\u0867"
          + "\5\u011a\u008e\2\u0864\u0866\7\7\2\2\u0865\u0864\3\2\2\2\u0866\u0869\3"
          + "\2\2\2\u0867\u0865\3\2\2\2\u0867\u0868\3\2\2\2\u0868\u086a\3\2\2\2\u0869"
          + "\u0867\3\2\2\2\u086a\u086b\5d\63\2\u086b\u086d\3\2\2\2\u086c\u085a\3\2"
          + "\2\2\u086c\u0863\3\2\2\2\u086d\u0870\3\2\2\2\u086e\u086c\3\2\2\2\u086e"
          + "\u086f\3\2\2\2\u086f\u00a5\3\2\2\2\u0870\u086e\3\2\2\2\u0871\u0883\5\u00aa"
          + "V\2\u0872\u0874\7\7\2\2\u0873\u0872\3\2\2\2\u0874\u0877\3\2\2\2\u0875"
          + "\u0873\3\2\2\2\u0875\u0876\3\2\2\2\u0876\u0878\3\2\2\2\u0877\u0875\3\2"
          + "\2\2\u0878\u087c\5\u00a8U\2\u0879\u087b\7\7\2\2\u087a\u0879\3\2\2\2\u087b"
          + "\u087e\3\2\2\2\u087c\u087a\3\2\2\2\u087c\u087d\3\2\2\2\u087d\u087f\3\2"
          + "\2\2\u087e\u087c\3\2\2\2\u087f\u0880\5\u00aaV\2\u0880\u0882\3\2\2\2\u0881"
          + "\u0875\3\2\2\2\u0882\u0885\3\2\2\2\u0883\u0881\3\2\2\2\u0883\u0884\3\2"
          + "\2\2\u0884\u00a7\3\2\2\2\u0885\u0883\3\2\2\2\u0886\u0887\7/\2\2\u0887"
          + "\u0888\7\34\2\2\u0888\u00a9\3\2\2\2\u0889\u0895\5\u00acW\2\u088a\u088e"
          + "\5\u0158\u00ad\2\u088b\u088d\7\7\2\2\u088c\u088b\3\2\2\2\u088d\u0890\3"
          + "\2\2\2\u088e\u088c\3\2\2\2\u088e\u088f\3\2\2\2\u088f\u0891\3\2\2\2\u0890"
          + "\u088e\3\2\2\2\u0891\u0892\5\u00acW\2\u0892\u0894\3\2\2\2\u0893\u088a"
          + "\3\2\2\2\u0894\u0897\3\2\2\2\u0895\u0893\3\2\2\2\u0895\u0896\3\2\2\2\u0896"
          + "\u00ab\3\2\2\2\u0897\u0895\3\2\2\2\u0898\u08a3\5\u00aeX\2\u0899\u089d"
          + "\7&\2\2\u089a\u089c\7\7\2\2\u089b\u089a\3\2\2\2\u089c\u089f\3\2\2\2\u089d"
          + "\u089b\3\2\2\2\u089d\u089e\3\2\2\2\u089e\u08a0\3\2\2\2\u089f\u089d\3\2"
          + "\2\2\u08a0\u08a2\5\u00aeX\2\u08a1\u0899\3\2\2\2\u08a2\u08a5\3\2\2\2\u08a3"
          + "\u08a1\3\2\2\2\u08a3\u08a4\3\2\2\2\u08a4\u00ad\3\2\2\2\u08a5\u08a3\3\2"
          + "\2\2\u08a6\u08b2\5\u00b0Y\2\u08a7\u08ab\5\u011c\u008f\2\u08a8\u08aa\7"
          + "\7\2\2\u08a9\u08a8\3\2\2\2\u08aa\u08ad\3\2\2\2\u08ab\u08a9\3\2\2\2\u08ab"
          + "\u08ac\3\2\2\2\u08ac\u08ae\3\2\2\2\u08ad\u08ab\3\2\2\2\u08ae\u08af\5\u00b0"
          + "Y\2\u08af\u08b1\3\2\2\2\u08b0\u08a7\3\2\2\2\u08b1\u08b4\3\2\2\2\u08b2"
          + "\u08b0\3\2\2\2\u08b2\u08b3\3\2\2\2\u08b3\u00af\3\2\2\2\u08b4\u08b2\3\2"
          + "\2\2\u08b5\u08c1\5\u00b2Z\2\u08b6\u08ba\5\u011e\u0090\2\u08b7\u08b9\7"
          + "\7\2\2\u08b8\u08b7\3\2\2\2\u08b9\u08bc\3\2\2\2\u08ba\u08b8\3\2\2\2\u08ba"
          + "\u08bb\3\2\2\2\u08bb\u08bd\3\2\2\2\u08bc\u08ba\3\2\2\2\u08bd\u08be\5\u00b2"
          + "Z\2\u08be\u08c0\3\2\2\2\u08bf\u08b6\3\2\2\2\u08c0\u08c3\3\2\2\2\u08c1"
          + "\u08bf\3\2\2\2\u08c1\u08c2\3\2\2\2\u08c2\u00b1\3\2\2\2\u08c3\u08c1\3\2"
          + "\2\2\u08c4\u08d6\5\u00b4[\2\u08c5\u08c7\7\7\2\2\u08c6\u08c5\3\2\2\2\u08c7"
          + "\u08ca\3\2\2\2\u08c8\u08c6\3\2\2\2\u08c8\u08c9\3\2\2\2\u08c9\u08cb\3\2"
          + "\2\2\u08ca\u08c8\3\2\2\2\u08cb\u08cf\5\u0120\u0091\2\u08cc\u08ce\7\7\2"
          + "\2\u08cd\u08cc\3\2\2\2\u08ce\u08d1\3\2\2\2\u08cf\u08cd\3\2\2\2\u08cf\u08d0"
          + "\3\2\2\2\u08d0\u08d2\3\2\2\2\u08d1\u08cf\3\2\2\2\u08d2\u08d3\5d\63\2\u08d3"
          + "\u08d5\3\2\2\2\u08d4\u08c8\3\2\2\2\u08d5\u08d8\3\2\2\2\u08d6\u08d4\3\2"
          + "\2\2\u08d6\u08d7\3\2\2\2\u08d7\u00b3\3\2\2\2\u08d8\u08d6\3\2\2\2\u08d9"
          + "\u08db\5\u00b6\\\2\u08da\u08d9\3\2\2\2\u08db\u08de\3\2\2\2\u08dc\u08da"
          + "\3\2\2\2\u08dc\u08dd\3\2\2\2\u08dd\u08df\3\2\2\2\u08de\u08dc\3\2\2\2\u08df"
          + "\u08e0\5\u00b8]\2\u08e0\u00b5\3\2\2\2\u08e1\u08eb\5\u014e\u00a8\2\u08e2"
          + "\u08eb\5\u0084C\2\u08e3\u08e7\5\u0122\u0092\2\u08e4\u08e6\7\7\2\2\u08e5"
          + "\u08e4\3\2\2\2\u08e6\u08e9\3\2\2\2\u08e7\u08e5\3\2\2\2\u08e7\u08e8\3\2"
          + "\2\2\u08e8\u08eb\3\2\2\2\u08e9\u08e7\3\2\2\2\u08ea\u08e1\3\2\2\2\u08ea"
          + "\u08e2\3\2\2\2\u08ea\u08e3\3\2\2\2\u08eb\u00b7\3\2\2\2\u08ec\u08f0\5\u00d4"
          + "k\2\u08ed\u08ef\5\u00ba^\2\u08ee\u08ed\3\2\2\2\u08ef\u08f2\3\2\2\2\u08f0"
          + "\u08ee\3\2\2\2\u08f0\u08f1\3\2\2\2\u08f1\u00b9\3\2\2\2\u08f2\u08f0\3\2"
          + "\2\2\u08f3\u08f9\5\u0124\u0093\2\u08f4\u08f9\5\u00ceh\2\u08f5\u08f9\5"
          + "\u00caf\2\u08f6\u08f9\5\u00c6d\2\u08f7\u08f9\5\u00c8e\2\u08f8\u08f3\3"
          + "\2\2\2\u08f8\u08f4\3\2\2\2\u08f8\u08f5\3\2\2\2\u08f8\u08f6\3\2\2\2\u08f8"
          + "\u08f7\3\2\2\2\u08f9\u00bb\3\2\2\2\u08fa\u08fb\5\u00b8]\2\u08fb\u08fc"
          + "\5\u00c4c\2\u08fc\u0900\3\2\2\2\u08fd\u0900\5\u0158\u00ad\2\u08fe\u0900"
          + "\5\u00be`\2\u08ff\u08fa\3\2\2\2\u08ff\u08fd\3\2\2\2\u08ff\u08fe\3\2\2"
          + "\2\u0900\u00bd\3\2\2\2\u0901\u0905\7\13\2\2\u0902\u0904\7\7\2\2\u0903"
          + "\u0902\3\2\2\2\u0904\u0907\3\2\2\2\u0905\u0903\3\2\2\2\u0905\u0906\3\2"
          + "\2\2\u0906\u0908\3\2\2\2\u0907\u0905\3\2\2\2\u0908\u090c\5\u00bc_\2\u0909"
          + "\u090b\7\7\2\2\u090a\u0909\3\2\2\2\u090b\u090e\3\2\2\2\u090c\u090a\3\2"
          + "\2\2\u090c\u090d\3\2\2\2\u090d\u090f\3\2\2\2\u090e\u090c\3\2\2\2\u090f"
          + "\u0910\7\f\2\2\u0910\u00bf\3\2\2\2\u0911\u0914\5\u00b4[\2\u0912\u0914"
          + "\5\u00c2b\2\u0913\u0911\3\2\2\2\u0913\u0912\3\2\2\2\u0914\u00c1\3\2\2"
          + "\2\u0915\u0919\7\13\2\2\u0916\u0918\7\7\2\2\u0917\u0916\3\2\2\2\u0918"
          + "\u091b\3\2\2\2\u0919\u0917\3\2\2\2\u0919\u091a\3\2\2\2\u091a\u091c\3\2"
          + "\2\2\u091b\u0919\3\2\2\2\u091c\u0920\5\u00c0a\2\u091d\u091f\7\7\2\2\u091e"
          + "\u091d\3\2\2\2\u091f\u0922\3\2\2\2\u0920\u091e\3\2\2\2\u0920\u0921\3\2"
          + "\2\2\u0921\u0923\3\2\2\2\u0922\u0920\3\2\2\2\u0923\u0924\7\f\2\2\u0924"
          + "\u00c3\3\2\2\2\u0925\u0929\5\u00ceh\2\u0926\u0929\5\u00c6d\2\u0927\u0929"
          + "\5\u00c8e\2\u0928\u0925\3\2\2\2\u0928\u0926\3\2\2\2\u0928\u0927\3\2\2"
          + "\2\u0929\u00c5\3\2\2\2\u092a\u092e\7\r\2\2\u092b\u092d\7\7\2\2\u092c\u092b"
          + "\3\2\2\2\u092d\u0930\3\2\2\2\u092e\u092c\3\2\2\2\u092e\u092f\3\2\2\2\u092f"
          + "\u0931\3\2\2\2\u0930\u092e\3\2\2\2\u0931\u0942\5\u0098M\2\u0932\u0934"
          + "\7\7\2\2\u0933\u0932\3\2\2\2\u0934\u0937\3\2\2\2\u0935\u0933\3\2\2\2\u0935"
          + "\u0936\3\2\2\2\u0936\u0938\3\2\2\2\u0937\u0935\3\2\2\2\u0938\u093c\7\n"
          + "\2\2\u0939\u093b\7\7\2\2\u093a\u0939\3\2\2\2\u093b\u093e\3\2\2\2\u093c"
          + "\u093a\3\2\2\2\u093c\u093d\3\2\2\2\u093d\u093f\3\2\2\2\u093e\u093c\3\2"
          + "\2\2\u093f\u0941\5\u0098M\2\u0940\u0935\3\2\2\2\u0941\u0944\3\2\2\2\u0942"
          + "\u0940\3\2\2\2\u0942\u0943\3\2\2\2\u0943\u094c\3\2\2\2\u0944\u0942\3\2"
          + "\2\2\u0945\u0947\7\7\2\2\u0946\u0945\3\2\2\2\u0947\u094a\3\2\2\2\u0948"
          + "\u0946\3\2\2\2\u0948\u0949\3\2\2\2\u0949\u094b\3\2\2\2\u094a\u0948\3\2"
          + "\2\2\u094b\u094d\7\n\2\2\u094c\u0948\3\2\2\2\u094c\u094d\3\2\2\2\u094d"
          + "\u0951\3\2\2\2\u094e\u0950\7\7\2\2\u094f\u094e\3\2\2\2\u0950\u0953\3\2"
          + "\2\2\u0951\u094f\3\2\2\2\u0951\u0952\3\2\2\2\u0952\u0954\3\2\2\2\u0953"
          + "\u0951\3\2\2\2\u0954\u0955\7\16\2\2\u0955\u00c7\3\2\2\2\u0956\u095a\5"
          + "\u0128\u0095\2\u0957\u0959\7\7\2\2\u0958\u0957\3\2\2\2\u0959\u095c\3\2"
          + "\2\2\u095a\u0958\3\2\2\2\u095a\u095b\3\2\2\2\u095b\u0960\3\2\2\2\u095c"
          + "\u095a\3\2\2\2\u095d\u0961\5\u0158\u00ad\2\u095e\u0961\5\u00d6l\2\u095f"
          + "\u0961\7J\2\2\u0960\u095d\3\2\2\2\u0960\u095e\3\2\2\2\u0960\u095f\3\2"
          + "\2\2\u0961\u00c9\3\2\2\2\u0962\u0964\5\u00ceh\2\u0963\u0962\3\2\2\2\u0963"
          + "\u0964\3\2\2\2\u0964\u096a\3\2\2\2\u0965\u0967\5\u00d0i\2\u0966\u0965"
          + "\3\2\2\2\u0966\u0967\3\2\2\2\u0967\u0968\3\2\2\2\u0968\u096b\5\u00ccg"
          + "\2\u0969\u096b\5\u00d0i\2\u096a\u0966\3\2\2\2\u096a\u0969\3\2\2\2\u096b"
          + "\u00cb\3\2\2\2\u096c\u096e\5\u014e\u00a8\2\u096d\u096c\3\2\2\2\u096e\u0971"
          + "\3\2\2\2\u096f\u096d\3\2\2\2\u096f\u0970\3\2\2\2\u0970\u0973\3\2\2\2\u0971"
          + "\u096f\3\2\2\2\u0972\u0974\5\u0084C\2\u0973\u0972\3\2\2\2\u0973\u0974"
          + "\3\2\2\2\u0974\u0978\3\2\2\2\u0975\u0977\7\7\2\2\u0976\u0975\3\2\2\2\u0977"
          + "\u097a\3\2\2\2\u0978\u0976\3\2\2\2\u0978\u0979\3\2\2\2\u0979\u097b\3\2"
          + "\2\2\u097a\u0978\3\2\2\2\u097b\u097c\5\u00eav\2\u097c\u00cd\3\2\2\2\u097d"
          + "\u0981\7\60\2\2\u097e\u0980\7\7\2\2\u097f\u097e\3\2\2\2\u0980\u0983\3"
          + "\2\2\2\u0981\u097f\3\2\2\2\u0981\u0982\3\2\2\2\u0982\u0984\3\2\2\2\u0983"
          + "\u0981\3\2\2\2\u0984\u0995\5p9\2\u0985\u0987\7\7\2\2\u0986\u0985\3\2\2"
          + "\2\u0987\u098a\3\2\2\2\u0988\u0986\3\2\2\2\u0988\u0989\3\2\2\2\u0989\u098b"
          + "\3\2\2\2\u098a\u0988\3\2\2\2\u098b\u098f\7\n\2\2\u098c\u098e\7\7\2\2\u098d"
          + "\u098c\3\2\2\2\u098e\u0991\3\2\2\2\u098f\u098d\3\2\2\2\u098f\u0990\3\2"
          + "\2\2\u0990\u0992\3\2\2\2\u0991\u098f\3\2\2\2\u0992\u0994\5p9\2\u0993\u0988"
          + "\3\2\2\2\u0994\u0997\3\2\2\2\u0995\u0993\3\2\2\2\u0995\u0996\3\2\2\2\u0996"
          + "\u099f\3\2\2\2\u0997\u0995\3\2\2\2\u0998\u099a\7\7\2\2\u0999\u0998\3\2"
          + "\2\2\u099a\u099d\3\2\2\2\u099b\u0999\3\2\2\2\u099b\u099c\3\2\2\2\u099c"
          + "\u099e\3\2\2\2\u099d\u099b\3\2\2\2\u099e\u09a0\7\n\2\2\u099f\u099b\3\2"
          + "\2\2\u099f\u09a0\3\2\2\2\u09a0\u09a4\3\2\2\2\u09a1\u09a3\7\7\2\2\u09a2"
          + "\u09a1\3\2\2\2\u09a3\u09a6\3\2\2\2\u09a4\u09a2\3\2\2\2\u09a4\u09a5\3\2"
          + "\2\2\u09a5\u09a7\3\2\2\2\u09a6\u09a4\3\2\2\2\u09a7\u09a8\7\61\2\2\u09a8"
          + "\u00cf\3\2\2\2\u09a9\u09ad\7\13\2\2\u09aa\u09ac\7\7\2\2\u09ab\u09aa\3"
          + "\2\2\2\u09ac\u09af\3\2\2\2\u09ad\u09ab\3\2\2\2\u09ad\u09ae\3\2\2\2\u09ae"
          + "\u09d3\3\2\2\2\u09af\u09ad\3\2\2\2\u09b0\u09c1\5\u00d2j\2\u09b1\u09b3"
          + "\7\7\2\2\u09b2\u09b1\3\2\2\2\u09b3\u09b6\3\2\2\2\u09b4\u09b2\3\2\2\2\u09b4"
          + "\u09b5\3\2\2\2\u09b5\u09b7\3\2\2\2\u09b6\u09b4\3\2\2\2\u09b7\u09bb\7\n"
          + "\2\2\u09b8\u09ba\7\7\2\2\u09b9\u09b8\3\2\2\2\u09ba\u09bd\3\2\2\2\u09bb"
          + "\u09b9\3\2\2\2\u09bb\u09bc\3\2\2\2\u09bc\u09be\3\2\2\2\u09bd\u09bb\3\2"
          + "\2\2\u09be\u09c0\5\u00d2j\2\u09bf\u09b4\3\2\2\2\u09c0\u09c3\3\2\2\2\u09c1"
          + "\u09bf\3\2\2\2\u09c1\u09c2\3\2\2\2\u09c2\u09cb\3\2\2\2\u09c3\u09c1\3\2"
          + "\2\2\u09c4\u09c6\7\7\2\2\u09c5\u09c4\3\2\2\2\u09c6\u09c9\3\2\2\2\u09c7"
          + "\u09c5\3\2\2\2\u09c7\u09c8\3\2\2\2\u09c8\u09ca\3\2\2\2\u09c9\u09c7\3\2"
          + "\2\2\u09ca\u09cc\7\n\2\2\u09cb\u09c7\3\2\2\2\u09cb\u09cc\3\2\2\2\u09cc"
          + "\u09d0\3\2\2\2\u09cd\u09cf\7\7\2\2\u09ce\u09cd\3\2\2\2\u09cf\u09d2\3\2"
          + "\2\2\u09d0\u09ce\3\2\2\2\u09d0\u09d1\3\2\2\2\u09d1\u09d4\3\2\2\2\u09d2"
          + "\u09d0\3\2\2\2\u09d3\u09b0\3\2\2\2\u09d3\u09d4\3\2\2\2\u09d4\u09d5\3\2"
          + "\2\2\u09d5\u09d6\7\f\2\2\u09d6\u00d1\3\2\2\2\u09d7\u09d9\5\u014e\u00a8"
          + "\2\u09d8\u09d7\3\2\2\2\u09d8\u09d9\3\2\2\2\u09d9\u09dd\3\2\2\2\u09da\u09dc"
          + "\7\7\2\2\u09db\u09da\3\2\2\2\u09dc\u09df\3\2\2\2\u09dd\u09db\3\2\2\2\u09dd"
          + "\u09de\3\2\2\2\u09de\u09ee\3\2\2\2\u09df\u09dd\3\2\2\2\u09e0\u09e4\5\u0158"
          + "\u00ad\2\u09e1\u09e3\7\7\2\2\u09e2\u09e1\3\2\2\2\u09e3\u09e6\3\2\2\2\u09e4"
          + "\u09e2\3\2\2\2\u09e4\u09e5\3\2\2\2\u09e5\u09e7\3\2\2\2\u09e6\u09e4\3\2"
          + "\2\2\u09e7\u09eb\7\36\2\2\u09e8\u09ea\7\7\2\2\u09e9\u09e8\3\2\2\2\u09ea"
          + "\u09ed\3\2\2\2\u09eb\u09e9\3\2\2\2\u09eb\u09ec\3\2\2\2\u09ec\u09ef\3\2"
          + "\2\2\u09ed\u09eb\3\2\2\2\u09ee\u09e0\3\2\2\2\u09ee\u09ef\3\2\2\2\u09ef"
          + "\u09f1\3\2\2\2\u09f0\u09f2\7\21\2\2\u09f1\u09f0\3\2\2\2\u09f1\u09f2\3"
          + "\2\2\2\u09f2\u09f6\3\2\2\2\u09f3\u09f5\7\7\2\2\u09f4\u09f3\3\2\2\2\u09f5"
          + "\u09f8\3\2\2\2\u09f6\u09f4\3\2\2\2\u09f6\u09f7\3\2\2\2\u09f7\u09f9\3\2"
          + "\2\2\u09f8\u09f6\3\2\2\2\u09f9\u09fa\5\u0098M\2\u09fa\u00d3\3\2\2\2\u09fb"
          + "\u0a0a\5\u00d6l\2\u09fc\u0a0a\5\u0158\u00ad\2\u09fd\u0a0a\5\u00dan\2\u09fe"
          + "\u0a0a\5\u00dco\2\u09ff\u0a0a\5\u0110\u0089\2\u0a00\u0a0a\5\u00f2z\2\u0a01"
          + "\u0a0a\5\u00f4{\2\u0a02\u0a0a\5\u00d8m\2\u0a03\u0a0a\5\u00f6|\2\u0a04"
          + "\u0a0a\5\u00f8}\2\u0a05\u0a0a\5\u00fa~\2\u0a06\u0a0a\5\u00fe\u0080\2\u0a07"
          + "\u0a0a\5\u0108\u0085\2\u0a08\u0a0a\5\u010e\u0088\2\u0a09\u09fb\3\2\2\2"
          + "\u0a09\u09fc\3\2\2\2\u0a09\u09fd\3\2\2\2\u0a09\u09fe\3\2\2\2\u0a09\u09ff"
          + "\3\2\2\2\u0a09\u0a00\3\2\2\2\u0a09\u0a01\3\2\2\2\u0a09\u0a02\3\2\2\2\u0a09"
          + "\u0a03\3\2\2\2\u0a09\u0a04\3\2\2\2\u0a09\u0a05\3\2\2\2\u0a09\u0a06\3\2"
          + "\2\2\u0a09\u0a07\3\2\2\2\u0a09\u0a08\3\2\2\2\u0a0a\u00d5\3\2\2\2\u0a0b"
          + "\u0a0f\7\13\2\2\u0a0c\u0a0e\7\7\2\2\u0a0d\u0a0c\3\2\2\2\u0a0e\u0a11\3"
          + "\2\2\2\u0a0f\u0a0d\3\2\2\2\u0a0f\u0a10\3\2\2\2\u0a10\u0a12\3\2\2\2\u0a11"
          + "\u0a0f\3\2\2\2\u0a12\u0a16\5\u0098M\2\u0a13\u0a15\7\7\2\2\u0a14\u0a13"
          + "\3\2\2\2\u0a15\u0a18\3\2\2\2\u0a16\u0a14\3\2\2\2\u0a16\u0a17\3\2\2\2\u0a17"
          + "\u0a19\3\2\2\2\u0a18\u0a16\3\2\2\2\u0a19\u0a1a\7\f\2\2\u0a1a\u00d7\3\2"
          + "\2\2\u0a1b\u0a1f\7\r\2\2\u0a1c\u0a1e\7\7\2\2\u0a1d\u0a1c\3\2\2\2\u0a1e"
          + "\u0a21\3\2\2\2\u0a1f\u0a1d\3\2\2\2\u0a1f\u0a20\3\2\2\2\u0a20\u0a45\3\2"
          + "\2\2\u0a21\u0a1f\3\2\2\2\u0a22\u0a33\5\u0098M\2\u0a23\u0a25\7\7\2\2\u0a24"
          + "\u0a23\3\2\2\2\u0a25\u0a28\3\2\2\2\u0a26\u0a24\3\2\2\2\u0a26\u0a27\3\2"
          + "\2\2\u0a27\u0a29\3\2\2\2\u0a28\u0a26\3\2\2\2\u0a29\u0a2d\7\n\2\2\u0a2a"
          + "\u0a2c\7\7\2\2\u0a2b\u0a2a\3\2\2\2\u0a2c\u0a2f\3\2\2\2\u0a2d\u0a2b\3\2"
          + "\2\2\u0a2d\u0a2e\3\2\2\2\u0a2e\u0a30\3\2\2\2\u0a2f\u0a2d\3\2\2\2\u0a30"
          + "\u0a32\5\u0098M\2\u0a31\u0a26\3\2\2\2\u0a32\u0a35\3\2\2\2\u0a33\u0a31"
          + "\3\2\2\2\u0a33\u0a34\3\2\2\2\u0a34\u0a3d\3\2\2\2\u0a35\u0a33\3\2\2\2\u0a36"
          + "\u0a38\7\7\2\2\u0a37\u0a36\3\2\2\2\u0a38\u0a3b\3\2\2\2\u0a39\u0a37\3\2"
          + "\2\2\u0a39\u0a3a\3\2\2\2\u0a3a\u0a3c\3\2\2\2\u0a3b\u0a39\3\2\2\2\u0a3c"
          + "\u0a3e\7\n\2\2\u0a3d\u0a39\3\2\2\2\u0a3d\u0a3e\3\2\2\2\u0a3e\u0a42\3\2"
          + "\2\2\u0a3f\u0a41\7\7\2\2\u0a40\u0a3f\3\2\2\2\u0a41\u0a44\3\2\2\2\u0a42"
          + "\u0a40\3\2\2\2\u0a42\u0a43\3\2\2\2\u0a43\u0a46\3\2\2\2\u0a44\u0a42\3\2"
          + "\2\2\u0a45\u0a22\3\2\2\2\u0a45\u0a46\3\2\2\2\u0a46\u0a47\3\2\2\2\u0a47"
          + "\u0a48\7\16\2\2\u0a48\u00d9\3\2\2\2\u0a49\u0a4a\t\b\2\2\u0a4a\u00db\3"
          + "\2\2\2\u0a4b\u0a4e\5\u00dep\2\u0a4c\u0a4e\5\u00e0q\2\u0a4d\u0a4b\3\2\2"
          + "\2\u0a4d\u0a4c\3\2\2\2\u0a4e\u00dd\3\2\2\2\u0a4f\u0a54\7\u0097\2\2\u0a50"
          + "\u0a53\5\u00e2r\2\u0a51\u0a53\5\u00e4s\2\u0a52\u0a50\3\2\2\2\u0a52\u0a51"
          + "\3\2\2\2\u0a53\u0a56\3\2\2\2\u0a54\u0a52\3\2\2\2\u0a54\u0a55\3\2\2\2\u0a55"
          + "\u0a57\3\2\2\2\u0a56\u0a54\3\2\2\2\u0a57\u0a58\7\u00a0\2\2\u0a58\u00df"
          + "\3\2\2\2\u0a59\u0a5f\7\u0098\2\2\u0a5a\u0a5e\5\u00e6t\2\u0a5b\u0a5e\5"
          + "\u00e8u\2\u0a5c\u0a5e\7\u00a6\2\2\u0a5d\u0a5a\3\2\2\2\u0a5d\u0a5b\3\2"
          + "\2\2\u0a5d\u0a5c\3\2\2\2\u0a5e\u0a61\3\2\2\2\u0a5f\u0a5d\3\2\2\2\u0a5f"
          + "\u0a60\3\2\2\2\u0a60\u0a62\3\2\2\2\u0a61\u0a5f\3\2\2\2\u0a62\u0a63\7\u00a5"
          + "\2\2\u0a63\u00e1\3\2\2\2\u0a64\u0a65\t\t\2\2\u0a65\u00e3\3\2\2\2\u0a66"
          + "\u0a6a\7\u00a4\2\2\u0a67\u0a69\7\7\2\2\u0a68\u0a67\3\2\2\2\u0a69\u0a6c"
          + "\3\2\2\2\u0a6a\u0a68\3\2\2\2\u0a6a\u0a6b\3\2\2\2\u0a6b\u0a6d\3\2\2\2\u0a6c"
          + "\u0a6a\3\2\2\2\u0a6d\u0a71\5\u0098M\2\u0a6e\u0a70\7\7\2\2\u0a6f\u0a6e"
          + "\3\2\2\2\u0a70\u0a73\3\2\2\2\u0a71\u0a6f\3\2\2\2\u0a71\u0a72\3\2\2\2\u0a72"
          + "\u0a74\3\2\2\2\u0a73\u0a71\3\2\2\2\u0a74\u0a75\7\20\2\2\u0a75\u00e5\3"
          + "\2\2\2\u0a76\u0a77\t\n\2\2\u0a77\u00e7\3\2\2\2\u0a78\u0a7c\7\u00a9\2\2"
          + "\u0a79\u0a7b\7\7\2\2\u0a7a\u0a79\3\2\2\2\u0a7b\u0a7e\3\2\2\2\u0a7c\u0a7a"
          + "\3\2\2\2\u0a7c\u0a7d\3\2\2\2\u0a7d\u0a7f\3\2\2\2\u0a7e\u0a7c\3\2\2\2\u0a7f"
          + "\u0a83\5\u0098M\2\u0a80\u0a82\7\7\2\2\u0a81\u0a80\3\2\2\2\u0a82\u0a85"
          + "\3\2\2\2\u0a83\u0a81\3\2\2\2\u0a83\u0a84\3\2\2\2\u0a84\u0a86\3\2\2\2\u0a85"
          + "\u0a83\3\2\2\2\u0a86\u0a87\7\20\2\2\u0a87\u00e9\3\2\2\2\u0a88\u0a8c\7"
          + "\17\2\2\u0a89\u0a8b\7\7\2\2\u0a8a\u0a89\3\2\2\2\u0a8b\u0a8e\3\2\2\2\u0a8c"
          + "\u0a8a\3\2\2\2\u0a8c\u0a8d\3\2\2\2\u0a8d\u0a9f\3\2\2\2\u0a8e\u0a8c\3\2"
          + "\2\2\u0a8f\u0a91\5\u00ecw\2\u0a90\u0a8f\3\2\2\2\u0a90\u0a91\3\2\2\2\u0a91"
          + "\u0a95\3\2\2\2\u0a92\u0a94\7\7\2\2\u0a93\u0a92\3\2\2\2\u0a94\u0a97\3\2"
          + "\2\2\u0a95\u0a93\3\2\2\2\u0a95\u0a96\3\2\2\2\u0a96\u0a98\3\2\2\2\u0a97"
          + "\u0a95\3\2\2\2\u0a98\u0a9c\7$\2\2\u0a99\u0a9b\7\7\2\2\u0a9a\u0a99\3\2"
          + "\2\2\u0a9b\u0a9e\3\2\2\2\u0a9c\u0a9a\3\2\2\2\u0a9c\u0a9d\3\2\2\2\u0a9d"
          + "\u0aa0\3\2\2\2\u0a9e\u0a9c\3\2\2\2\u0a9f\u0a90\3\2\2\2\u0a9f\u0aa0\3\2"
          + "\2\2\u0aa0\u0aa1\3\2\2\2\u0aa1\u0aa5\5\u0080A\2\u0aa2\u0aa4\7\7\2\2\u0aa3"
          + "\u0aa2\3\2\2\2\u0aa4\u0aa7\3\2\2\2\u0aa5\u0aa3\3\2\2\2\u0aa5\u0aa6\3\2"
          + "\2\2\u0aa6\u0aa8\3\2\2\2\u0aa7\u0aa5\3\2\2\2\u0aa8\u0aa9\7\20\2\2\u0aa9"
          + "\u00eb\3\2\2\2\u0aaa\u0abb\5\u00eex\2\u0aab\u0aad\7\7\2\2\u0aac\u0aab"
          + "\3\2\2\2\u0aad\u0ab0\3\2\2\2\u0aae\u0aac\3\2\2\2\u0aae\u0aaf\3\2\2\2\u0aaf"
          + "\u0ab1\3\2\2\2\u0ab0\u0aae\3\2\2\2\u0ab1\u0ab5\7\n\2\2\u0ab2\u0ab4\7\7"
          + "\2\2\u0ab3\u0ab2\3\2\2\2\u0ab4\u0ab7\3\2\2\2\u0ab5\u0ab3\3\2\2\2\u0ab5"
          + "\u0ab6\3\2\2\2\u0ab6\u0ab8\3\2\2\2\u0ab7\u0ab5\3\2\2\2\u0ab8\u0aba\5\u00ee"
          + "x\2\u0ab9\u0aae\3\2\2\2\u0aba\u0abd\3\2\2\2\u0abb\u0ab9\3\2\2\2\u0abb"
          + "\u0abc\3\2\2\2\u0abc\u0ac5\3\2\2\2\u0abd\u0abb\3\2\2\2\u0abe\u0ac0\7\7"
          + "\2\2\u0abf\u0abe\3\2\2\2\u0ac0\u0ac3\3\2\2\2\u0ac1\u0abf\3\2\2\2\u0ac1"
          + "\u0ac2\3\2\2\2\u0ac2\u0ac4\3\2\2\2\u0ac3\u0ac1\3\2\2\2\u0ac4\u0ac6\7\n"
          + "\2\2\u0ac5\u0ac1\3\2\2\2\u0ac5\u0ac6\3\2\2\2\u0ac6\u00ed\3\2\2\2\u0ac7"
          + "\u0ada\5D#\2\u0ac8\u0ad7\5F$\2\u0ac9\u0acb\7\7\2\2\u0aca\u0ac9\3\2\2\2"
          + "\u0acb\u0ace\3\2\2\2\u0acc\u0aca\3\2\2\2\u0acc\u0acd\3\2\2\2\u0acd\u0acf"
          + "\3\2\2\2\u0ace\u0acc\3\2\2\2\u0acf\u0ad3\7\34\2\2\u0ad0\u0ad2\7\7\2\2"
          + "\u0ad1\u0ad0\3\2\2\2\u0ad2\u0ad5\3\2\2\2\u0ad3\u0ad1\3\2\2\2\u0ad3\u0ad4"
          + "\3\2\2\2\u0ad4\u0ad6\3\2\2\2\u0ad5\u0ad3\3\2\2\2\u0ad6\u0ad8\5d\63\2\u0ad7"
          + "\u0acc\3\2\2\2\u0ad7\u0ad8\3\2\2\2\u0ad8\u0ada\3\2\2\2\u0ad9\u0ac7\3\2"
          + "\2\2\u0ad9\u0ac8\3\2\2\2\u0ada\u00ef\3\2\2\2\u0adb\u0aeb\7L\2\2\u0adc"
          + "\u0ade\7\7\2\2\u0add\u0adc\3\2\2\2\u0ade\u0ae1\3\2\2\2\u0adf\u0add\3\2"
          + "\2\2\u0adf\u0ae0\3\2\2\2\u0ae0\u0ae2\3\2\2\2\u0ae1\u0adf\3\2\2\2\u0ae2"
          + "\u0ae6\5d\63\2\u0ae3\u0ae5\7\7\2\2\u0ae4\u0ae3\3\2\2\2\u0ae5\u0ae8\3\2"
          + "\2\2\u0ae6\u0ae4\3\2\2\2\u0ae6\u0ae7\3\2\2\2\u0ae7\u0ae9\3\2\2\2\u0ae8"
          + "\u0ae6\3\2\2\2\u0ae9\u0aea\7\t\2\2\u0aea\u0aec\3\2\2\2\u0aeb\u0adf\3\2"
          + "\2\2\u0aeb\u0aec\3\2\2\2\u0aec\u0af0\3\2\2\2\u0aed\u0aef\7\7\2\2\u0aee"
          + "\u0aed\3\2\2\2\u0aef\u0af2\3\2\2\2\u0af0\u0aee\3\2\2\2\u0af0\u0af1\3\2"
          + "\2\2\u0af1\u0af3\3\2\2\2\u0af2\u0af0\3\2\2\2\u0af3\u0b02\5P)\2\u0af4\u0af6"
          + "\7\7\2\2\u0af5\u0af4\3\2\2\2\u0af6\u0af9\3\2\2\2\u0af7\u0af5\3\2\2\2\u0af7"
          + "\u0af8\3\2\2\2\u0af8\u0afa\3\2\2\2\u0af9\u0af7\3\2\2\2\u0afa\u0afe\7\34"
          + "\2\2\u0afb\u0afd\7\7\2\2\u0afc\u0afb\3\2\2\2\u0afd\u0b00\3\2\2\2\u0afe"
          + "\u0afc\3\2\2\2\u0afe\u0aff\3\2\2\2\u0aff\u0b01\3\2\2\2\u0b00\u0afe\3\2"
          + "\2\2\u0b01\u0b03\5d\63\2\u0b02\u0af7\3\2\2\2\u0b02\u0b03\3\2\2\2\u0b03"
          + "\u0b0b\3\2\2\2\u0b04\u0b06\7\7\2\2\u0b05\u0b04\3\2\2\2\u0b06\u0b09\3\2"
          + "\2\2\u0b07\u0b05\3\2\2\2\u0b07\u0b08\3\2\2\2\u0b08\u0b0a\3\2\2\2\u0b09"
          + "\u0b07\3\2\2\2\u0b0a\u0b0c\5\60\31\2\u0b0b\u0b07\3\2\2\2\u0b0b\u0b0c\3"
          + "\2\2\2\u0b0c\u0b14\3\2\2\2\u0b0d\u0b0f\7\7\2\2\u0b0e\u0b0d\3\2\2\2\u0b0f"
          + "\u0b12\3\2\2\2\u0b10\u0b0e\3\2\2\2\u0b10\u0b11\3\2\2\2\u0b11\u0b13\3\2"
          + "\2\2\u0b12\u0b10\3\2\2\2\u0b13\u0b15\5B\"\2\u0b14\u0b10\3\2\2\2\u0b14"
          + "\u0b15\3\2\2\2\u0b15\u00f1\3\2\2\2\u0b16\u0b19\5\u00eav\2\u0b17\u0b19"
          + "\5\u00f0y\2\u0b18\u0b16\3\2\2\2\u0b18\u0b17\3\2\2\2\u0b19\u00f3\3\2\2"
          + "\2\u0b1a\u0b2f\7M\2\2\u0b1b\u0b1d\7\7\2\2\u0b1c\u0b1b\3\2\2\2\u0b1d\u0b20"
          + "\3\2\2\2\u0b1e\u0b1c\3\2\2\2\u0b1e\u0b1f\3\2\2\2\u0b1f\u0b21\3\2\2\2\u0b20"
          + "\u0b1e\3\2\2\2\u0b21\u0b25\7\34\2\2\u0b22\u0b24\7\7\2\2\u0b23\u0b22\3"
          + "\2\2\2\u0b24\u0b27\3\2\2\2\u0b25\u0b23\3\2\2\2\u0b25\u0b26\3\2\2\2\u0b26"
          + "\u0b28\3\2\2\2\u0b27\u0b25\3\2\2\2\u0b28\u0b2c\5\"\22\2\u0b29\u0b2b\7"
          + "\7\2\2\u0b2a\u0b29\3\2\2\2\u0b2b\u0b2e\3\2\2\2\u0b2c\u0b2a\3\2\2\2\u0b2c"
          + "\u0b2d\3\2\2\2\u0b2d\u0b30\3\2\2\2\u0b2e\u0b2c\3\2\2\2\u0b2f\u0b1e\3\2"
          + "\2\2\u0b2f\u0b30\3\2\2\2\u0b30\u0b38\3\2\2\2\u0b31\u0b33\7\7\2\2\u0b32"
          + "\u0b31\3\2\2\2\u0b33\u0b36\3\2\2\2\u0b34\u0b32\3\2\2\2\u0b34\u0b35\3\2"
          + "\2\2\u0b35\u0b37\3\2\2\2\u0b36\u0b34\3\2\2\2\u0b37\u0b39\5\34\17\2\u0b38"
          + "\u0b34\3\2\2\2\u0b38\u0b39\3\2\2\2\u0b39\u00f5\3\2\2\2\u0b3a\u0b3b\t\13"
          + "\2\2\u0b3b\u00f7\3\2\2\2\u0b3c\u0b4d\7V\2\2\u0b3d\u0b41\7\60\2\2\u0b3e"
          + "\u0b40\7\7\2\2\u0b3f\u0b3e\3\2\2\2\u0b40\u0b43\3\2\2\2\u0b41\u0b3f\3\2"
          + "\2\2\u0b41\u0b42\3\2\2\2\u0b42\u0b44\3\2\2\2\u0b43\u0b41\3\2\2\2\u0b44"
          + "\u0b48\5d\63\2\u0b45\u0b47\7\7\2\2\u0b46\u0b45\3\2\2\2\u0b47\u0b4a\3\2"
          + "\2\2\u0b48\u0b46\3\2\2\2\u0b48\u0b49\3\2\2\2\u0b49\u0b4b\3\2\2\2\u0b4a"
          + "\u0b48\3\2\2\2\u0b4b\u0b4c\7\61\2\2\u0b4c\u0b4e\3\2\2\2\u0b4d\u0b3d\3"
          + "\2\2\2\u0b4d\u0b4e\3\2\2\2\u0b4e\u0b51\3\2\2\2\u0b4f\u0b50\7*\2\2\u0b50"
          + "\u0b52\5\u0158\u00ad\2\u0b51\u0b4f\3\2\2\2\u0b51\u0b52\3\2\2\2\u0b52\u0b55"
          + "\3\2\2\2\u0b53\u0b55\7>\2\2\u0b54\u0b3c\3\2\2\2\u0b54\u0b53\3\2\2\2\u0b55"
          + "\u00f9\3\2\2\2\u0b56\u0b5a\7Y\2\2\u0b57\u0b59\7\7\2\2\u0b58\u0b57\3\2"
          + "\2\2\u0b59\u0b5c\3\2\2\2\u0b5a\u0b58\3\2\2\2\u0b5a\u0b5b\3\2\2\2\u0b5b"
          + "\u0b5d\3\2\2\2\u0b5c\u0b5a\3\2\2\2\u0b5d\u0b61\7\13\2\2\u0b5e\u0b60\7"
          + "\7\2\2\u0b5f\u0b5e\3\2\2\2\u0b60\u0b63\3\2\2\2\u0b61\u0b5f\3\2\2\2\u0b61"
          + "\u0b62\3\2\2\2\u0b62\u0b64\3\2\2\2\u0b63\u0b61\3\2\2\2\u0b64\u0b68\5\u0098"
          + "M\2\u0b65\u0b67\7\7\2\2\u0b66\u0b65\3\2\2\2\u0b67\u0b6a\3\2\2\2\u0b68"
          + "\u0b66\3\2\2\2\u0b68\u0b69\3\2\2\2\u0b69\u0b6b\3\2\2\2\u0b6a\u0b68\3\2"
          + "\2\2\u0b6b\u0b6f\7\f\2\2\u0b6c\u0b6e\7\7\2\2\u0b6d\u0b6c\3\2\2\2\u0b6e"
          + "\u0b71\3\2\2\2\u0b6f\u0b6d\3\2\2\2\u0b6f\u0b70\3\2\2\2\u0b70\u0b91\3\2"
          + "\2\2\u0b71\u0b6f\3\2\2\2\u0b72\u0b92\5\u0086D\2\u0b73\u0b75\5\u0086D\2"
          + "\u0b74\u0b73\3\2\2\2\u0b74\u0b75\3\2\2\2\u0b75\u0b79\3\2\2\2\u0b76\u0b78"
          + "\7\7\2\2\u0b77\u0b76\3\2\2\2\u0b78\u0b7b\3\2\2\2\u0b79\u0b77\3\2\2\2\u0b79"
          + "\u0b7a\3\2\2\2\u0b7a\u0b7d\3\2\2\2\u0b7b\u0b79\3\2\2\2\u0b7c\u0b7e\7\35"
          + "\2\2\u0b7d\u0b7c\3\2\2\2\u0b7d\u0b7e\3\2\2\2\u0b7e\u0b82\3\2\2\2\u0b7f"
          + "\u0b81\7\7\2\2\u0b80\u0b7f\3\2\2\2\u0b81\u0b84\3\2\2\2\u0b82\u0b80\3\2"
          + "\2\2\u0b82\u0b83\3\2\2\2\u0b83\u0b85\3\2\2\2\u0b84\u0b82\3\2\2\2\u0b85"
          + "\u0b89\7Z\2\2\u0b86\u0b88\7\7\2\2\u0b87\u0b86\3\2\2\2\u0b88\u0b8b\3\2"
          + "\2\2\u0b89\u0b87\3\2\2\2\u0b89\u0b8a\3\2\2\2\u0b8a\u0b8e\3\2\2\2\u0b8b"
          + "\u0b89\3\2\2\2\u0b8c\u0b8f\5\u0086D\2\u0b8d\u0b8f\7\35\2\2\u0b8e\u0b8c"
          + "\3\2\2\2\u0b8e\u0b8d\3\2\2\2\u0b8f\u0b92\3\2\2\2\u0b90\u0b92\7\35\2\2"
          + "\u0b91\u0b72\3\2\2\2\u0b91\u0b74\3\2\2\2\u0b91\u0b90\3\2\2\2\u0b92\u00fb"
          + "\3\2\2\2\u0b93\u0bb5\7\13\2\2\u0b94\u0b96\5\u014e\u00a8\2\u0b95\u0b94"
          + "\3\2\2\2\u0b96\u0b99\3\2\2\2\u0b97\u0b95\3\2\2\2\u0b97\u0b98\3\2\2\2\u0b98"
          + "\u0b9d\3\2\2\2\u0b99\u0b97\3\2\2\2\u0b9a\u0b9c\7\7\2\2\u0b9b\u0b9a\3\2"
          + "\2\2\u0b9c\u0b9f\3\2\2\2\u0b9d\u0b9b\3\2\2\2\u0b9d\u0b9e\3\2\2\2\u0b9e"
          + "\u0ba0\3\2\2\2\u0b9f\u0b9d\3\2\2\2\u0ba0\u0ba4\7N\2\2\u0ba1\u0ba3\7\7"
          + "\2\2\u0ba2\u0ba1\3\2\2\2\u0ba3\u0ba6\3\2\2\2\u0ba4\u0ba2\3\2\2\2\u0ba4"
          + "\u0ba5\3\2\2\2\u0ba5\u0ba7\3\2\2\2\u0ba6\u0ba4\3\2\2\2\u0ba7\u0bab\5D"
          + "#\2\u0ba8\u0baa\7\7\2\2\u0ba9\u0ba8\3\2\2\2\u0baa\u0bad\3\2\2\2\u0bab"
          + "\u0ba9\3\2\2\2\u0bab\u0bac\3\2\2\2\u0bac\u0bae\3\2\2\2\u0bad\u0bab\3\2"
          + "\2\2\u0bae\u0bb2\7\36\2\2\u0baf\u0bb1\7\7\2\2\u0bb0\u0baf\3\2\2\2\u0bb1"
          + "\u0bb4\3\2\2\2\u0bb2\u0bb0\3\2\2\2\u0bb2\u0bb3\3\2\2\2\u0bb3\u0bb6\3\2"
          + "\2\2\u0bb4\u0bb2\3\2\2\2\u0bb5\u0b97\3\2\2\2\u0bb5\u0bb6\3\2\2\2\u0bb6"
          + "\u0bb7\3\2\2\2\u0bb7\u0bb8\5\u0098M\2\u0bb8\u0bb9\7\f\2\2\u0bb9\u00fd"
          + "\3\2\2\2\u0bba\u0bbe\7[\2\2\u0bbb\u0bbd\7\7\2\2\u0bbc\u0bbb\3\2\2\2\u0bbd"
          + "\u0bc0\3\2\2\2\u0bbe\u0bbc\3\2\2\2\u0bbe\u0bbf\3\2\2\2\u0bbf\u0bc2\3\2"
          + "\2\2\u0bc0\u0bbe\3\2\2\2\u0bc1\u0bc3\5\u00fc\177\2\u0bc2\u0bc1\3\2\2\2"
          + "\u0bc2\u0bc3\3\2\2\2\u0bc3\u0bc7\3\2\2\2\u0bc4\u0bc6\7\7\2\2\u0bc5\u0bc4"
          + "\3\2\2\2\u0bc6\u0bc9\3\2\2\2\u0bc7\u0bc5\3\2\2\2\u0bc7\u0bc8\3\2\2\2\u0bc8"
          + "\u0bca\3\2\2\2\u0bc9\u0bc7\3\2\2\2\u0bca\u0bce\7\17\2\2\u0bcb\u0bcd\7"
          + "\7\2\2\u0bcc\u0bcb\3\2\2\2\u0bcd\u0bd0\3\2\2\2\u0bce\u0bcc\3\2\2\2\u0bce"
          + "\u0bcf\3\2\2\2\u0bcf\u0bda\3\2\2\2\u0bd0\u0bce\3\2\2\2\u0bd1\u0bd5\5\u0100"
          + "\u0081\2\u0bd2\u0bd4\7\7\2\2\u0bd3\u0bd2\3\2\2\2\u0bd4\u0bd7\3\2\2\2\u0bd5"
          + "\u0bd3\3\2\2\2\u0bd5\u0bd6\3\2\2\2\u0bd6\u0bd9\3\2\2\2\u0bd7\u0bd5\3\2"
          + "\2\2\u0bd8\u0bd1\3\2\2\2\u0bd9\u0bdc\3\2\2\2\u0bda\u0bd8\3\2\2\2\u0bda"
          + "\u0bdb\3\2\2\2\u0bdb\u0be0\3\2\2\2\u0bdc\u0bda\3\2\2\2\u0bdd\u0bdf\7\7"
          + "\2\2\u0bde\u0bdd\3\2\2\2\u0bdf\u0be2\3\2\2\2\u0be0\u0bde\3\2\2\2\u0be0"
          + "\u0be1\3\2\2\2\u0be1\u0be3\3\2\2\2\u0be2\u0be0\3\2\2\2\u0be3\u0be4\7\20"
          + "\2\2\u0be4\u00ff\3\2\2\2\u0be5\u0bf6\5\u0102\u0082\2\u0be6\u0be8\7\7\2"
          + "\2\u0be7\u0be6\3\2\2\2\u0be8\u0beb\3\2\2\2\u0be9\u0be7\3\2\2\2\u0be9\u0bea"
          + "\3\2\2\2\u0bea\u0bec\3\2\2\2\u0beb\u0be9\3\2\2\2\u0bec\u0bf0\7\n\2\2\u0bed"
          + "\u0bef\7\7\2\2\u0bee\u0bed\3\2\2\2\u0bef\u0bf2\3\2\2\2\u0bf0\u0bee\3\2"
          + "\2\2\u0bf0\u0bf1\3\2\2\2\u0bf1\u0bf3\3\2\2\2\u0bf2\u0bf0\3\2\2\2\u0bf3"
          + "\u0bf5\5\u0102\u0082\2\u0bf4\u0be9\3\2\2\2\u0bf5\u0bf8\3\2\2\2\u0bf6\u0bf4"
          + "\3\2\2\2\u0bf6\u0bf7\3\2\2\2\u0bf7\u0c00\3\2\2\2\u0bf8\u0bf6\3\2\2\2\u0bf9"
          + "\u0bfb\7\7\2\2\u0bfa\u0bf9\3\2\2\2\u0bfb\u0bfe\3\2\2\2\u0bfc\u0bfa\3\2"
          + "\2\2\u0bfc\u0bfd\3\2\2\2\u0bfd\u0bff\3\2\2\2\u0bfe\u0bfc\3\2\2\2\u0bff"
          + "\u0c01\7\n\2\2\u0c00\u0bfc\3\2\2\2\u0c00\u0c01\3\2\2\2\u0c01\u0c05\3\2"
          + "\2\2\u0c02\u0c04\7\7\2\2\u0c03\u0c02\3\2\2\2\u0c04\u0c07\3\2\2\2\u0c05"
          + "\u0c03\3\2\2\2\u0c05\u0c06\3\2\2\2\u0c06\u0c08\3\2\2\2\u0c07\u0c05\3\2"
          + "\2\2\u0c08\u0c0c\7$\2\2\u0c09\u0c0b\7\7\2\2\u0c0a\u0c09\3\2\2\2\u0c0b"
          + "\u0c0e\3\2\2\2\u0c0c\u0c0a\3\2\2\2\u0c0c\u0c0d\3\2\2\2\u0c0d\u0c0f\3\2"
          + "\2\2\u0c0e\u0c0c\3\2\2\2\u0c0f\u0c11\5\u0086D\2\u0c10\u0c12\5\u0094K\2"
          + "\u0c11\u0c10\3\2\2\2\u0c11\u0c12\3\2\2\2\u0c12\u0c26\3\2\2\2\u0c13\u0c17"
          + "\7Z\2\2\u0c14\u0c16\7\7\2\2\u0c15\u0c14\3\2\2\2\u0c16\u0c19\3\2\2\2\u0c17"
          + "\u0c15\3\2\2\2\u0c17\u0c18\3\2\2\2\u0c18\u0c1a\3\2\2\2\u0c19\u0c17\3\2"
          + "\2\2\u0c1a\u0c1e\7$\2\2\u0c1b\u0c1d\7\7\2\2\u0c1c\u0c1b\3\2\2\2\u0c1d"
          + "\u0c20\3\2\2\2\u0c1e\u0c1c\3\2\2\2\u0c1e\u0c1f\3\2\2\2\u0c1f\u0c21\3\2"
          + "\2\2\u0c20\u0c1e\3\2\2\2\u0c21\u0c23\5\u0086D\2\u0c22\u0c24\5\u0094K\2"
          + "\u0c23\u0c22\3\2\2\2\u0c23\u0c24\3\2\2\2\u0c24\u0c26\3\2\2\2\u0c25\u0be5"
          + "\3\2\2\2\u0c25\u0c13\3\2\2\2\u0c26\u0101\3\2\2\2\u0c27\u0c2b\5\u0098M"
          + "\2\u0c28\u0c2b\5\u0104\u0083\2\u0c29\u0c2b\5\u0106\u0084\2\u0c2a\u0c27"
          + "\3\2\2\2\u0c2a\u0c28\3\2\2\2\u0c2a\u0c29\3\2\2\2\u0c2b\u0103\3\2\2\2\u0c2c"
          + "\u0c30\5\u0118\u008d\2\u0c2d\u0c2f\7\7\2\2\u0c2e\u0c2d\3\2\2\2\u0c2f\u0c32"
          + "\3\2\2\2\u0c30\u0c2e\3\2\2\2\u0c30\u0c31\3\2\2\2\u0c31\u0c33\3\2\2\2\u0c32"
          + "\u0c30\3\2\2\2\u0c33\u0c34\5\u0098M\2\u0c34\u0105\3\2\2\2\u0c35\u0c39"
          + "\5\u011a\u008e\2\u0c36\u0c38\7\7\2\2\u0c37\u0c36\3\2\2\2\u0c38\u0c3b\3"
          + "\2\2\2\u0c39\u0c37\3\2\2\2\u0c39\u0c3a\3\2\2\2\u0c3a\u0c3c\3\2\2\2\u0c3b"
          + "\u0c39\3\2\2\2\u0c3c\u0c3d\5d\63\2\u0c3d\u0107\3\2\2\2\u0c3e\u0c42\7\\"
          + "\2\2\u0c3f\u0c41\7\7\2\2\u0c40\u0c3f\3\2\2\2\u0c41\u0c44\3\2\2\2\u0c42"
          + "\u0c40\3\2\2\2\u0c42\u0c43\3\2\2\2\u0c43\u0c45\3\2\2\2\u0c44\u0c42\3\2"
          + "\2\2\u0c45\u0c61\5\u0088E\2\u0c46\u0c48\7\7\2\2\u0c47\u0c46\3\2\2\2\u0c48"
          + "\u0c4b\3\2\2\2\u0c49\u0c47\3\2\2\2\u0c49\u0c4a\3\2\2\2\u0c4a\u0c4c\3\2"
          + "\2\2\u0c4b\u0c49\3\2\2\2\u0c4c\u0c4e\5\u010a\u0086\2\u0c4d\u0c49\3\2\2"
          + "\2\u0c4e\u0c4f\3\2\2\2\u0c4f\u0c4d\3\2\2\2\u0c4f\u0c50\3\2\2\2\u0c50\u0c58"
          + "\3\2\2\2\u0c51\u0c53\7\7\2\2\u0c52\u0c51\3\2\2\2\u0c53\u0c56\3\2\2\2\u0c54"
          + "\u0c52\3\2\2\2\u0c54\u0c55\3\2\2\2\u0c55\u0c57\3\2\2\2\u0c56\u0c54\3\2"
          + "\2\2\u0c57\u0c59\5\u010c\u0087\2\u0c58\u0c54\3\2\2\2\u0c58\u0c59\3\2\2"
          + "\2\u0c59\u0c62\3\2\2\2\u0c5a\u0c5c\7\7\2\2\u0c5b\u0c5a\3\2\2\2\u0c5c\u0c5f"
          + "\3\2\2\2\u0c5d\u0c5b\3\2\2\2\u0c5d\u0c5e\3\2\2\2\u0c5e\u0c60\3\2\2\2\u0c5f"
          + "\u0c5d\3\2\2\2\u0c60\u0c62\5\u010c\u0087\2\u0c61\u0c4d\3\2\2\2\u0c61\u0c5d"
          + "\3\2\2\2\u0c62\u0109\3\2\2\2\u0c63\u0c67\7]\2\2\u0c64\u0c66\7\7\2\2\u0c65"
          + "\u0c64\3\2\2\2\u0c66\u0c69\3\2\2\2\u0c67\u0c65\3\2\2\2\u0c67\u0c68\3\2"
          + "\2\2\u0c68\u0c6a\3\2\2\2\u0c69\u0c67\3\2\2\2\u0c6a\u0c6e\7\13\2\2\u0c6b"
          + "\u0c6d\5\u014e\u00a8\2\u0c6c\u0c6b\3\2\2\2\u0c6d\u0c70\3\2\2\2\u0c6e\u0c6c"
          + "\3\2\2\2\u0c6e\u0c6f\3\2\2\2\u0c6f\u0c71\3\2\2\2\u0c70\u0c6e\3\2\2\2\u0c71"
          + "\u0c72\5\u0158\u00ad\2\u0c72\u0c73\7\34\2\2\u0c73\u0c7b\5d\63\2\u0c74"
          + "\u0c76\7\7\2\2\u0c75\u0c74\3\2\2\2\u0c76\u0c79\3\2\2\2\u0c77\u0c75\3\2"
          + "\2\2\u0c77\u0c78\3\2\2\2\u0c78\u0c7a\3\2\2\2\u0c79\u0c77\3\2\2\2\u0c7a"
          + "\u0c7c\7\n\2\2\u0c7b\u0c77\3\2\2\2\u0c7b\u0c7c\3\2\2\2\u0c7c\u0c7d\3\2"
          + "\2\2\u0c7d\u0c81\7\f\2\2\u0c7e\u0c80\7\7\2\2\u0c7f\u0c7e\3\2\2\2\u0c80"
          + "\u0c83\3\2\2\2\u0c81\u0c7f\3\2\2\2\u0c81\u0c82\3\2\2\2\u0c82\u0c84\3\2"
          + "\2\2\u0c83\u0c81\3\2\2\2\u0c84\u0c85\5\u0088E\2\u0c85\u010b\3\2\2\2\u0c86"
          + "\u0c8a\7^\2\2\u0c87\u0c89\7\7\2\2\u0c88\u0c87\3\2\2\2\u0c89\u0c8c\3\2"
          + "\2\2\u0c8a\u0c88\3\2\2\2\u0c8a\u0c8b\3\2\2\2\u0c8b\u0c8d\3\2\2\2\u0c8c"
          + "\u0c8a\3\2\2\2\u0c8d\u0c8e\5\u0088E\2\u0c8e\u010d\3\2\2\2\u0c8f\u0c93"
          + "\7b\2\2\u0c90\u0c92\7\7\2\2\u0c91\u0c90\3\2\2\2\u0c92\u0c95\3\2\2\2\u0c93"
          + "\u0c91\3\2\2\2\u0c93\u0c94\3\2\2\2\u0c94\u0c96\3\2\2\2\u0c95\u0c93\3\2"
          + "\2\2\u0c96\u0ca0\5\u0098M\2\u0c97\u0c99\t\f\2\2\u0c98\u0c9a\5\u0098M\2"
          + "\u0c99\u0c98\3\2\2\2\u0c99\u0c9a\3\2\2\2\u0c9a\u0ca0\3\2\2\2\u0c9b\u0ca0"
          + "\7d\2\2\u0c9c\u0ca0\7;\2\2\u0c9d\u0ca0\7e\2\2\u0c9e\u0ca0\7<\2\2\u0c9f"
          + "\u0c8f\3\2\2\2\u0c9f\u0c97\3\2\2\2\u0c9f\u0c9b\3\2\2\2\u0c9f\u0c9c\3\2"
          + "\2\2\u0c9f\u0c9d\3\2\2\2\u0c9f\u0c9e\3\2\2\2\u0ca0\u010f\3\2\2\2\u0ca1"
          + "\u0ca3\5|?\2\u0ca2\u0ca1\3\2\2\2\u0ca2\u0ca3\3\2\2\2\u0ca3\u0ca4\3\2\2"
          + "\2\u0ca4\u0ca8\7\'\2\2\u0ca5\u0ca7\7\7\2\2\u0ca6\u0ca5\3\2\2\2\u0ca7\u0caa"
          + "\3\2\2\2\u0ca8\u0ca6\3\2\2\2\u0ca8\u0ca9\3\2\2\2\u0ca9\u0cad\3\2\2\2\u0caa"
          + "\u0ca8\3\2\2\2\u0cab\u0cae\5\u0158\u00ad\2\u0cac\u0cae\7J\2\2\u0cad\u0cab"
          + "\3\2\2\2\u0cad\u0cac\3\2\2\2\u0cae\u0111\3\2\2\2\u0caf\u0cb0\t\r\2\2\u0cb0"
          + "\u0113\3\2\2\2\u0cb1\u0cb2\t\16\2\2\u0cb2\u0115\3\2\2\2\u0cb3\u0cb4\t"
          + "\17\2\2\u0cb4\u0117\3\2\2\2\u0cb5\u0cb6\t\20\2\2\u0cb6\u0119\3\2\2\2\u0cb7"
          + "\u0cb8\t\21\2\2\u0cb8\u011b\3\2\2\2\u0cb9\u0cba\t\22\2\2\u0cba\u011d\3"
          + "\2\2\2\u0cbb\u0cbc\t\23\2\2\u0cbc\u011f\3\2\2\2\u0cbd\u0cbe\t\24\2\2\u0cbe"
          + "\u0121\3\2\2\2\u0cbf\u0cc5\7\26\2\2\u0cc0\u0cc5\7\27\2\2\u0cc1\u0cc5\7"
          + "\25\2\2\u0cc2\u0cc5\7\24\2\2\u0cc3\u0cc5\5\u0126\u0094\2\u0cc4\u0cbf\3"
          + "\2\2\2\u0cc4\u0cc0\3\2\2\2\u0cc4\u0cc1\3\2\2\2\u0cc4\u0cc2\3\2\2\2\u0cc4"
          + "\u0cc3\3\2\2\2\u0cc5\u0123\3\2\2\2\u0cc6\u0ccb\7\26\2\2\u0cc7\u0ccb\7"
          + "\27\2\2\u0cc8\u0cc9\7\33\2\2\u0cc9\u0ccb\5\u0126\u0094\2\u0cca\u0cc6\3"
          + "\2\2\2\u0cca\u0cc7\3\2\2\2\u0cca\u0cc8\3\2\2\2\u0ccb\u0125\3\2\2\2\u0ccc"
          + "\u0ccd\t\25\2\2\u0ccd\u0127\3\2\2\2\u0cce\u0cd0\7\7\2\2\u0ccf\u0cce\3"
          + "\2\2\2\u0cd0\u0cd3\3\2\2\2\u0cd1\u0ccf\3\2\2\2\u0cd1\u0cd2\3\2\2\2\u0cd2"
          + "\u0cd4\3\2\2\2\u0cd3\u0cd1\3\2\2\2\u0cd4\u0cde\7\t\2\2\u0cd5\u0cd7\7\7"
          + "\2\2\u0cd6\u0cd5\3\2\2\2\u0cd7\u0cda\3\2\2\2\u0cd8\u0cd6\3\2\2\2\u0cd8"
          + "\u0cd9\3\2\2\2\u0cd9\u0cdb\3\2\2\2\u0cda\u0cd8\3\2\2\2\u0cdb\u0cde\5\u012a"
          + "\u0096\2\u0cdc\u0cde\7\'\2\2\u0cdd\u0cd1\3\2\2\2\u0cdd\u0cd8\3\2\2\2\u0cdd"
          + "\u0cdc\3\2\2\2\u0cde\u0129\3\2\2\2\u0cdf\u0ce0\7/\2\2\u0ce0\u0ce1\7\t"
          + "\2\2\u0ce1\u012b\3\2\2\2\u0ce2\u0ce5\5\u014e\u00a8\2\u0ce3\u0ce5\5\u0130"
          + "\u0099\2\u0ce4\u0ce2\3\2\2\2\u0ce4\u0ce3\3\2\2\2\u0ce5\u0ce6\3\2\2\2\u0ce6"
          + "\u0ce4\3\2\2\2\u0ce6\u0ce7\3\2\2\2\u0ce7\u012d\3\2\2\2\u0ce8\u0ceb\5\u014e"
          + "\u00a8\2\u0ce9\u0ceb\5\u0148\u00a5\2\u0cea\u0ce8\3\2\2\2\u0cea\u0ce9\3"
          + "\2\2\2\u0ceb\u0cec\3\2\2\2\u0cec\u0cea\3\2\2\2\u0cec\u0ced\3\2\2\2\u0ced"
          + "\u012f\3\2\2\2\u0cee\u0cf7\5\u0136\u009c\2\u0cef\u0cf7\5\u0138\u009d\2"
          + "\u0cf0\u0cf7\5\u013a\u009e\2\u0cf1\u0cf7\5\u0142\u00a2\2\u0cf2\u0cf7\5"
          + "\u0144\u00a3\2\u0cf3\u0cf7\5\u0146\u00a4\2\u0cf4\u0cf7\5\u0148\u00a5\2"
          + "\u0cf5\u0cf7\5\u014c\u00a7\2\u0cf6\u0cee\3\2\2\2\u0cf6\u0cef\3\2\2\2\u0cf6"
          + "\u0cf0\3\2\2\2\u0cf6\u0cf1\3\2\2\2\u0cf6\u0cf2\3\2\2\2\u0cf6\u0cf3\3\2"
          + "\2\2\u0cf6\u0cf4\3\2\2\2\u0cf6\u0cf5\3\2\2\2\u0cf7\u0cfb\3\2\2\2\u0cf8"
          + "\u0cfa\7\7\2\2\u0cf9\u0cf8\3\2\2\2\u0cfa\u0cfd\3\2\2\2\u0cfb\u0cf9\3\2"
          + "\2\2\u0cfb\u0cfc\3\2\2\2\u0cfc\u0131\3\2\2\2\u0cfd\u0cfb\3\2\2\2\u0cfe"
          + "\u0d00\5\u0134\u009b\2\u0cff\u0cfe\3\2\2\2\u0d00\u0d01\3\2\2\2\u0d01\u0cff"
          + "\3\2\2\2\u0d01\u0d02\3\2\2\2\u0d02\u0133\3\2\2\2\u0d03\u0d0c\5\u014e\u00a8"
          + "\2\u0d04\u0d08\7|\2\2\u0d05\u0d07\7\7\2\2\u0d06\u0d05\3\2\2\2\u0d07\u0d0a"
          + "\3\2\2\2\u0d08\u0d06\3\2\2\2\u0d08\u0d09\3\2\2\2\u0d09\u0d0c\3\2\2\2\u0d0a"
          + "\u0d08\3\2\2\2\u0d0b\u0d03\3\2\2\2\u0d0b\u0d04\3\2\2\2\u0d0c\u0135\3\2"
          + "\2\2\u0d0d\u0d0e\t\26\2\2\u0d0e\u0137\3\2\2\2\u0d0f\u0d10\t\27\2\2\u0d10"
          + "\u0139\3\2\2\2\u0d11\u0d12\t\30\2\2\u0d12\u013b\3\2\2\2\u0d13\u0d14\t"
          + "\31\2\2\u0d14\u013d\3\2\2\2\u0d15\u0d17\5\u0140\u00a1\2\u0d16\u0d15\3"
          + "\2\2\2\u0d17\u0d18\3\2\2\2\u0d18\u0d16\3\2\2\2\u0d18\u0d19\3\2\2\2\u0d19"
          + "\u013f\3\2\2\2\u0d1a\u0d1e\5\u014a\u00a6\2\u0d1b\u0d1d\7\7\2\2\u0d1c\u0d1b"
          + "\3\2\2\2\u0d1d\u0d20\3\2\2\2\u0d1e\u0d1c\3\2\2\2\u0d1e\u0d1f\3\2\2\2\u0d1f"
          + "\u0d2a\3\2\2\2\u0d20\u0d1e\3\2\2\2\u0d21\u0d25\5\u013c\u009f\2\u0d22\u0d24"
          + "\7\7\2\2\u0d23\u0d22\3\2\2\2\u0d24\u0d27\3\2\2\2\u0d25\u0d23\3\2\2\2\u0d25"
          + "\u0d26\3\2\2\2\u0d26\u0d2a\3\2\2\2\u0d27\u0d25\3\2\2\2\u0d28\u0d2a\5\u014e"
          + "\u00a8\2\u0d29\u0d1a\3\2\2\2\u0d29\u0d21\3\2\2\2\u0d29\u0d28\3\2\2\2\u0d2a"
          + "\u0141\3\2\2\2\u0d2b\u0d2c\t\32\2\2\u0d2c\u0143\3\2\2\2\u0d2d\u0d2e\7"
          + "\u0081\2\2\u0d2e\u0145\3\2\2\2\u0d2f\u0d30\t\33\2\2\u0d30\u0147\3\2\2"
          + "\2\u0d31\u0d32\t\34\2\2\u0d32\u0149\3\2\2\2\u0d33\u0d34\7\u0086\2\2\u0d34"
          + "\u014b\3\2\2\2\u0d35\u0d36\t\35\2\2\u0d36\u014d\3\2\2\2\u0d37\u0d3a\5"
          + "\u0150\u00a9\2\u0d38\u0d3a\5\u0152\u00aa\2\u0d39\u0d37\3\2\2\2\u0d39\u0d38"
          + "\3\2\2\2\u0d3a\u0d3e\3\2\2\2\u0d3b\u0d3d\7\7\2\2\u0d3c\u0d3b\3\2\2\2\u0d3d"
          + "\u0d40\3\2\2\2\u0d3e\u0d3c\3\2\2\2\u0d3e\u0d3f\3\2\2\2\u0d3f\u014f\3\2"
          + "\2\2\u0d40\u0d3e\3\2\2\2\u0d41\u0d45\5\u0154\u00ab\2\u0d42\u0d44\7\7\2"
          + "\2\u0d43\u0d42\3\2\2\2\u0d44\u0d47\3\2\2\2\u0d45\u0d43\3\2\2\2\u0d45\u0d46"
          + "\3\2\2\2\u0d46\u0d4b\3\2\2\2\u0d47\u0d45\3\2\2\2\u0d48\u0d4b\7*\2\2\u0d49"
          + "\u0d4b\7,\2\2\u0d4a\u0d41\3\2\2\2\u0d4a\u0d48\3\2\2\2\u0d4a\u0d49\3\2"
          + "\2\2\u0d4b\u0d4c\3\2\2\2\u0d4c\u0d4d\5\u0156\u00ac\2\u0d4d\u0151\3\2\2"
          + "\2\u0d4e\u0d52\5\u0154\u00ab\2\u0d4f\u0d51\7\7\2\2\u0d50\u0d4f\3\2\2\2"
          + "\u0d51\u0d54\3\2\2\2\u0d52\u0d50\3\2\2\2\u0d52\u0d53\3\2\2\2\u0d53\u0d58"
          + "\3\2\2\2\u0d54\u0d52\3\2\2\2\u0d55\u0d58\7*\2\2\u0d56\u0d58\7,\2\2\u0d57"
          + "\u0d4e\3\2\2\2\u0d57\u0d55\3\2\2\2\u0d57\u0d56\3\2\2\2\u0d58\u0d59\3\2"
          + "\2\2\u0d59\u0d5b\7\r\2\2\u0d5a\u0d5c\5\u0156\u00ac\2\u0d5b\u0d5a\3\2\2"
          + "\2\u0d5c\u0d5d\3\2\2\2\u0d5d\u0d5b\3\2\2\2\u0d5d\u0d5e\3\2\2\2\u0d5e\u0d5f"
          + "\3\2\2\2\u0d5f\u0d60\7\16\2\2\u0d60\u0153\3\2\2\2\u0d61\u0d62\t\2\2\2"
          + "\u0d62\u0d66\t\36\2\2\u0d63\u0d65\7\7\2\2\u0d64\u0d63\3\2\2\2\u0d65\u0d68"
          + "\3\2\2\2\u0d66\u0d64\3\2\2\2\u0d66\u0d67\3\2\2\2\u0d67\u0d69\3\2\2\2\u0d68"
          + "\u0d66\3\2\2\2\u0d69\u0d6a\7\34\2\2\u0d6a\u0155\3\2\2\2\u0d6b\u0d6e\5"
          + "&\24\2\u0d6c\u0d6e\5l\67\2\u0d6d\u0d6b\3\2\2\2\u0d6d\u0d6c\3\2\2\2\u0d6e"
          + "\u0157\3\2\2\2\u0d6f\u0d70\t\37\2\2\u0d70\u0159\3\2\2\2\u0d71\u0d7c\5"
          + "\u0158\u00ad\2\u0d72\u0d74\7\7\2\2\u0d73\u0d72\3\2\2\2\u0d74\u0d77\3\2"
          + "\2\2\u0d75\u0d73\3\2\2\2\u0d75\u0d76\3\2\2\2\u0d76\u0d78\3\2\2\2\u0d77"
          + "\u0d75\3\2\2\2\u0d78\u0d79\7\t\2\2\u0d79\u0d7b\5\u0158\u00ad\2\u0d7a\u0d75"
          + "\3\2\2\2\u0d7b\u0d7e\3\2\2\2\u0d7c\u0d7a\3\2\2\2\u0d7c\u0d7d\3\2\2\2\u0d7d"
          + "\u015b\3\2\2\2\u0d7e\u0d7c\3\2\2\2\u0214\u015d\u0162\u0168\u0170\u0176"
          + "\u017b\u0181\u018b\u0194\u019b\u01a2\u01a9\u01ae\u01b3\u01b9\u01bb\u01c0"
          + "\u01c8\u01cb\u01d2\u01d5\u01db\u01e2\u01e6\u01eb\u01f2\u01fc\u01ff\u0206"
          + "\u0209\u020c\u0211\u0218\u021c\u0221\u0225\u022a\u0231\u0235\u023a\u023e"
          + "\u0243\u024a\u024e\u0251\u0257\u025a\u0262\u0269\u0272\u0279\u0280\u0286"
          + "\u028c\u0290\u0292\u0297\u029d\u02a0\u02a5\u02ad\u02b4\u02bb\u02bf\u02c5"
          + "\u02cc\u02d2\u02d9\u02e1\u02e7\u02ee\u02f3\u02fa\u0303\u030a\u0311\u0317"
          + "\u031d\u0321\u0326\u032c\u0331\u0338\u033f\u0343\u0349\u0350\u0357\u035d"
          + "\u0363\u036a\u0371\u0378\u037c\u0383\u0389\u038f\u0395\u039c\u03a0\u03a5"
          + "\u03ac\u03b0\u03b5\u03b9\u03bf\u03c6\u03cd\u03d3\u03d9\u03dd\u03df\u03e4"
          + "\u03ea\u03f0\u03f7\u03fb\u03fe\u0404\u0408\u040d\u0414\u0419\u041e\u0425"
          + "\u042c\u0433\u0437\u043c\u0440\u0445\u0449\u0450\u0454\u0459\u045f\u0466"
          + "\u046d\u0471\u0477\u047e\u0485\u048b\u0491\u0495\u049a\u04a0\u04a6\u04aa"
          + "\u04af\u04b6\u04bb\u04c0\u04c5\u04ca\u04ce\u04d3\u04da\u04df\u04e1\u04e6"
          + "\u04e9\u04ee\u04f2\u04f7\u04fb\u04fe\u0501\u0506\u050a\u050d\u050f\u0515"
          + "\u051b\u0521\u0528\u052f\u0536\u053a\u053f\u0543\u0546\u054c\u0553\u055a"
          + "\u055e\u0563\u056a\u0571\u0575\u057a\u057f\u0585\u058c\u0593\u0599\u059f"
          + "\u05a3\u05a5\u05aa\u05b0\u05b6\u05bd\u05c1\u05c7\u05ce\u05d2\u05d8\u05df"
          + "\u05e5\u05eb\u05f2\u05f9\u05fd\u0602\u0606\u0609\u060f\u0616\u061d\u0621"
          + "\u0626\u062a\u0630\u0639\u063d\u0642\u0649\u064d\u0652\u065b\u0662\u0668"
          + "\u066e\u0672\u0678\u067b\u0681\u0685\u068a\u068e\u0691\u0697\u069b\u069f"
          + "\u06a4\u06aa\u06b2\u06b9\u06bf\u06c6\u06ca\u06cd\u06d1\u06d6\u06dc\u06e0"
          + "\u06e6\u06ed\u06f0\u06f6\u06fd\u0706\u070b\u0710\u0717\u071c\u0720\u0726"
          + "\u072a\u072f\u0738\u073f\u0745\u074a\u0750\u0755\u075a\u0765\u0768\u076b"
          + "\u076f\u0771\u0778\u077f\u0784\u078a\u0791\u0799\u079f\u07a6\u07ab\u07b3"
          + "\u07b7\u07bd\u07c6\u07cb\u07d1\u07d5\u07da\u07e1\u07ee\u07f3\u07fc\u0800"
          + "\u0805\u0808\u0810\u0817\u081d\u0824\u082b\u0831\u0839\u0840\u0848\u084f"
          + "\u0856\u085e\u0867\u086c\u086e\u0875\u087c\u0883\u088e\u0895\u089d\u08a3"
          + "\u08ab\u08b2\u08ba\u08c1\u08c8\u08cf\u08d6\u08dc\u08e7\u08ea\u08f0\u08f8"
          + "\u08ff\u0905\u090c\u0913\u0919\u0920\u0928\u092e\u0935\u093c\u0942\u0948"
          + "\u094c\u0951\u095a\u0960\u0963\u0966\u096a\u096f\u0973\u0978\u0981\u0988"
          + "\u098f\u0995\u099b\u099f\u09a4\u09ad\u09b4\u09bb\u09c1\u09c7\u09cb\u09d0"
          + "\u09d3\u09d8\u09dd\u09e4\u09eb\u09ee\u09f1\u09f6\u0a09\u0a0f\u0a16\u0a1f"
          + "\u0a26\u0a2d\u0a33\u0a39\u0a3d\u0a42\u0a45\u0a4d\u0a52\u0a54\u0a5d\u0a5f"
          + "\u0a6a\u0a71\u0a7c\u0a83\u0a8c\u0a90\u0a95\u0a9c\u0a9f\u0aa5\u0aae\u0ab5"
          + "\u0abb\u0ac1\u0ac5\u0acc\u0ad3\u0ad7\u0ad9\u0adf\u0ae6\u0aeb\u0af0\u0af7"
          + "\u0afe\u0b02\u0b07\u0b0b\u0b10\u0b14\u0b18\u0b1e\u0b25\u0b2c\u0b2f\u0b34"
          + "\u0b38\u0b41\u0b48\u0b4d\u0b51\u0b54\u0b5a\u0b61\u0b68\u0b6f\u0b74\u0b79"
          + "\u0b7d\u0b82\u0b89\u0b8e\u0b91\u0b97\u0b9d\u0ba4\u0bab\u0bb2\u0bb5\u0bbe"
          + "\u0bc2\u0bc7\u0bce\u0bd5\u0bda\u0be0\u0be9\u0bf0\u0bf6\u0bfc\u0c00\u0c05"
          + "\u0c0c\u0c11\u0c17\u0c1e\u0c23\u0c25\u0c2a\u0c30\u0c39\u0c42\u0c49\u0c4f"
          + "\u0c54\u0c58\u0c5d\u0c61\u0c67\u0c6e\u0c77\u0c7b\u0c81\u0c8a\u0c93\u0c99"
          + "\u0c9f\u0ca2\u0ca8\u0cad\u0cc4\u0cca\u0cd1\u0cd8\u0cdd\u0ce4\u0ce6\u0cea"
          + "\u0cec\u0cf6\u0cfb\u0d01\u0d08\u0d0b\u0d18\u0d1e\u0d25\u0d29\u0d39\u0d3e"
          + "\u0d45\u0d4a\u0d52\u0d57\u0d5d\u0d66\u0d6d\u0d75\u0d7c";
  public static final String _serializedATN =
      Utils.join(new String[] {_serializedATNSegment0, _serializedATNSegment1}, "");
  public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());

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

  public KotlinParser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  private static String[] makeRuleNames() {
    return new String[] {
      "kotlinFile",
      "script",
      "shebangLine",
      "fileAnnotation",
      "packageHeader",
      "importList",
      "importHeader",
      "importAlias",
      "topLevelObject",
      "typeAlias",
      "declaration",
      "classDeclaration",
      "primaryConstructor",
      "classBody",
      "classParameters",
      "classParameter",
      "delegationSpecifiers",
      "delegationSpecifier",
      "constructorInvocation",
      "annotatedDelegationSpecifier",
      "explicitDelegation",
      "typeParameters",
      "typeParameter",
      "typeConstraints",
      "typeConstraint",
      "classMemberDeclarations",
      "classMemberDeclaration",
      "anonymousInitializer",
      "companionObject",
      "functionValueParameters",
      "functionValueParameter",
      "functionDeclaration",
      "functionBody",
      "variableDeclaration",
      "multiVariableDeclaration",
      "propertyDeclaration",
      "propertyDelegate",
      "getter",
      "setter",
      "parametersWithOptionalType",
      "functionValueParameterWithOptionalType",
      "parameterWithOptionalType",
      "parameter",
      "objectDeclaration",
      "secondaryConstructor",
      "constructorDelegationCall",
      "enumClassBody",
      "enumEntries",
      "enumEntry",
      "type",
      "typeReference",
      "nullableType",
      "quest",
      "userType",
      "simpleUserType",
      "typeProjection",
      "typeProjectionModifiers",
      "typeProjectionModifier",
      "functionType",
      "functionTypeParameters",
      "parenthesizedType",
      "receiverType",
      "parenthesizedUserType",
      "statements",
      "statement",
      "label",
      "controlStructureBody",
      "block",
      "loopStatement",
      "forStatement",
      "whileStatement",
      "doWhileStatement",
      "assignment",
      "semi",
      "semis",
      "expression",
      "disjunction",
      "conjunction",
      "equality",
      "comparison",
      "genericCallLikeComparison",
      "infixOperation",
      "elvisExpression",
      "elvis",
      "infixFunctionCall",
      "rangeExpression",
      "additiveExpression",
      "multiplicativeExpression",
      "asExpression",
      "prefixUnaryExpression",
      "unaryPrefix",
      "postfixUnaryExpression",
      "postfixUnarySuffix",
      "directlyAssignableExpression",
      "parenthesizedDirectlyAssignableExpression",
      "assignableExpression",
      "parenthesizedAssignableExpression",
      "assignableSuffix",
      "indexingSuffix",
      "navigationSuffix",
      "callSuffix",
      "annotatedLambda",
      "typeArguments",
      "valueArguments",
      "valueArgument",
      "primaryExpression",
      "parenthesizedExpression",
      "collectionLiteral",
      "literalConstant",
      "stringLiteral",
      "lineStringLiteral",
      "multiLineStringLiteral",
      "lineStringContent",
      "lineStringExpression",
      "multiLineStringContent",
      "multiLineStringExpression",
      "lambdaLiteral",
      "lambdaParameters",
      "lambdaParameter",
      "anonymousFunction",
      "functionLiteral",
      "objectLiteral",
      "thisExpression",
      "superExpression",
      "ifExpression",
      "whenSubject",
      "whenExpression",
      "whenEntry",
      "whenCondition",
      "rangeTest",
      "typeTest",
      "tryExpression",
      "catchBlock",
      "finallyBlock",
      "jumpExpression",
      "callableReference",
      "assignmentAndOperator",
      "equalityOperator",
      "comparisonOperator",
      "inOperator",
      "isOperator",
      "additiveOperator",
      "multiplicativeOperator",
      "asOperator",
      "prefixUnaryOperator",
      "postfixUnaryOperator",
      "excl",
      "memberAccessOperator",
      "safeNav",
      "modifiers",
      "parameterModifiers",
      "modifier",
      "typeModifiers",
      "typeModifier",
      "classModifier",
      "memberModifier",
      "visibilityModifier",
      "varianceModifier",
      "typeParameterModifiers",
      "typeParameterModifier",
      "functionModifier",
      "propertyModifier",
      "inheritanceModifier",
      "parameterModifier",
      "reificationModifier",
      "platformModifier",
      "annotation",
      "singleAnnotation",
      "multiAnnotation",
      "annotationUseSiteTarget",
      "unescapedAnnotation",
      "simpleIdentifier",
      "identifier"
    };
  }

  private static String[] makeLiteralNames() {
    return new String[] {
      null,
      null,
      null,
      null,
      null,
      null,
      "'...'",
      "'.'",
      "','",
      "'('",
      "')'",
      "'['",
      "']'",
      "'{'",
      "'}'",
      "'*'",
      "'%'",
      "'/'",
      "'+'",
      "'-'",
      "'++'",
      "'--'",
      "'&&'",
      "'||'",
      null,
      "'!'",
      "':'",
      "';'",
      "'='",
      "'+='",
      "'-='",
      "'*='",
      "'/='",
      "'%='",
      "'->'",
      "'=>'",
      "'..'",
      "'::'",
      "';;'",
      "'#'",
      "'@'",
      null,
      null,
      null,
      null,
      "'?'",
      "'<'",
      "'>'",
      "'<='",
      "'>='",
      "'!='",
      "'!=='",
      "'as?'",
      "'=='",
      "'==='",
      "'''",
      null,
      null,
      null,
      null,
      null,
      "'file'",
      "'field'",
      "'property'",
      "'get'",
      "'set'",
      "'receiver'",
      "'param'",
      "'setparam'",
      "'delegate'",
      "'package'",
      "'import'",
      "'class'",
      "'interface'",
      "'fun'",
      "'object'",
      "'val'",
      "'var'",
      "'typealias'",
      "'constructor'",
      "'by'",
      "'companion'",
      "'init'",
      "'this'",
      "'super'",
      "'typeof'",
      "'where'",
      "'if'",
      "'else'",
      "'when'",
      "'try'",
      "'catch'",
      "'finally'",
      "'for'",
      "'do'",
      "'while'",
      "'throw'",
      "'return'",
      "'continue'",
      "'break'",
      "'as'",
      "'is'",
      "'in'",
      null,
      null,
      "'out'",
      "'dynamic'",
      "'public'",
      "'private'",
      "'protected'",
      "'internal'",
      "'enum'",
      "'sealed'",
      "'annotation'",
      "'data'",
      "'inner'",
      "'value'",
      "'tailrec'",
      "'operator'",
      "'inline'",
      "'infix'",
      "'external'",
      "'suspend'",
      "'override'",
      "'abstract'",
      "'final'",
      "'open'",
      "'const'",
      "'lateinit'",
      "'vararg'",
      "'noinline'",
      "'crossinline'",
      "'reified'",
      "'expect'",
      "'actual'",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      "'null'",
      null,
      null,
      null,
      null,
      null,
      "'\"\"\"'"
    };
  }

  private static String[] makeSymbolicNames() {
    return new String[] {
      null,
      "ShebangLine",
      "DelimitedComment",
      "LineComment",
      "WS",
      "NL",
      "RESERVED",
      "DOT",
      "COMMA",
      "LPAREN",
      "RPAREN",
      "LSQUARE",
      "RSQUARE",
      "LCURL",
      "RCURL",
      "MULT",
      "MOD",
      "DIV",
      "ADD",
      "SUB",
      "INCR",
      "DECR",
      "CONJ",
      "DISJ",
      "EXCL_WS",
      "EXCL_NO_WS",
      "COLON",
      "SEMICOLON",
      "ASSIGNMENT",
      "ADD_ASSIGNMENT",
      "SUB_ASSIGNMENT",
      "MULT_ASSIGNMENT",
      "DIV_ASSIGNMENT",
      "MOD_ASSIGNMENT",
      "ARROW",
      "DOUBLE_ARROW",
      "RANGE",
      "COLONCOLON",
      "DOUBLE_SEMICOLON",
      "HASH",
      "AT_NO_WS",
      "AT_POST_WS",
      "AT_PRE_WS",
      "AT_BOTH_WS",
      "QUEST_WS",
      "QUEST_NO_WS",
      "LANGLE",
      "RANGLE",
      "LE",
      "GE",
      "EXCL_EQ",
      "EXCL_EQEQ",
      "AS_SAFE",
      "EQEQ",
      "EQEQEQ",
      "SINGLE_QUOTE",
      "RETURN_AT",
      "CONTINUE_AT",
      "BREAK_AT",
      "THIS_AT",
      "SUPER_AT",
      "FILE",
      "FIELD",
      "PROPERTY",
      "GET",
      "SET",
      "RECEIVER",
      "PARAM",
      "SETPARAM",
      "DELEGATE",
      "PACKAGE",
      "IMPORT",
      "CLASS",
      "INTERFACE",
      "FUN",
      "OBJECT",
      "VAL",
      "VAR",
      "TYPE_ALIAS",
      "CONSTRUCTOR",
      "BY",
      "COMPANION",
      "INIT",
      "THIS",
      "SUPER",
      "TYPEOF",
      "WHERE",
      "IF",
      "ELSE",
      "WHEN",
      "TRY",
      "CATCH",
      "FINALLY",
      "FOR",
      "DO",
      "WHILE",
      "THROW",
      "RETURN",
      "CONTINUE",
      "BREAK",
      "AS",
      "IS",
      "IN",
      "NOT_IS",
      "NOT_IN",
      "OUT",
      "DYNAMIC",
      "PUBLIC",
      "PRIVATE",
      "PROTECTED",
      "INTERNAL",
      "ENUM",
      "SEALED",
      "ANNOTATION",
      "DATA",
      "INNER",
      "VALUE",
      "TAILREC",
      "OPERATOR",
      "INLINE",
      "INFIX",
      "EXTERNAL",
      "SUSPEND",
      "OVERRIDE",
      "ABSTRACT",
      "FINAL",
      "OPEN",
      "CONST",
      "LATEINIT",
      "VARARG",
      "NOINLINE",
      "CROSSINLINE",
      "REIFIED",
      "EXPECT",
      "ACTUAL",
      "RealLiteral",
      "FloatLiteral",
      "DoubleLiteral",
      "IntegerLiteral",
      "HexLiteral",
      "BinLiteral",
      "UnsignedLiteral",
      "LongLiteral",
      "BooleanLiteral",
      "NullLiteral",
      "CharacterLiteral",
      "Identifier",
      "IdentifierOrSoftKey",
      "FieldIdentifier",
      "QUOTE_OPEN",
      "TRIPLE_QUOTE_OPEN",
      "UNICODE_CLASS_LL",
      "UNICODE_CLASS_LM",
      "UNICODE_CLASS_LO",
      "UNICODE_CLASS_LT",
      "UNICODE_CLASS_LU",
      "UNICODE_CLASS_ND",
      "UNICODE_CLASS_NL",
      "QUOTE_CLOSE",
      "LineStrRef",
      "LineStrText",
      "LineStrEscapedChar",
      "LineStrExprStart",
      "TRIPLE_QUOTE_CLOSE",
      "MultiLineStringQuote",
      "MultiLineStrRef",
      "MultiLineStrText",
      "MultiLineStrExprStart",
      "Inside_Comment",
      "Inside_WS",
      "Inside_NL",
      "ErrorCharacter"
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
    return "KotlinParser.g4";
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public final KotlinFileContext kotlinFile() throws RecognitionException {
    KotlinFileContext _localctx = new KotlinFileContext(_ctx, getState());
    enterRule(_localctx, 0, RULE_kotlinFile);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(347);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == ShebangLine) {
          {
            setState(346);
            shebangLine();
          }
        }

        setState(352);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(349);
              match(NL);
            }
          }
          setState(354);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(358);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(355);
                fileAnnotation();
              }
            }
          }
          setState(360);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 2, _ctx);
        }
        setState(361);
        packageHeader();
        setState(362);
        importList();
        setState(366);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 72)) & ~0x3f) == 0
                && ((1L << (_la - 72))
                        & ((1L << (CLASS - 72))
                            | (1L << (INTERFACE - 72))
                            | (1L << (FUN - 72))
                            | (1L << (OBJECT - 72))
                            | (1L << (VAL - 72))
                            | (1L << (VAR - 72))
                            | (1L << (TYPE_ALIAS - 72))
                            | (1L << (PUBLIC - 72))
                            | (1L << (PRIVATE - 72))
                            | (1L << (PROTECTED - 72))
                            | (1L << (INTERNAL - 72))
                            | (1L << (ENUM - 72))
                            | (1L << (SEALED - 72))
                            | (1L << (ANNOTATION - 72))
                            | (1L << (DATA - 72))
                            | (1L << (INNER - 72))
                            | (1L << (VALUE - 72))
                            | (1L << (TAILREC - 72))
                            | (1L << (OPERATOR - 72))
                            | (1L << (INLINE - 72))
                            | (1L << (INFIX - 72))
                            | (1L << (EXTERNAL - 72))
                            | (1L << (SUSPEND - 72))
                            | (1L << (OVERRIDE - 72))
                            | (1L << (ABSTRACT - 72))
                            | (1L << (FINAL - 72))
                            | (1L << (OPEN - 72))
                            | (1L << (CONST - 72))
                            | (1L << (LATEINIT - 72))
                            | (1L << (VARARG - 72))
                            | (1L << (NOINLINE - 72))
                            | (1L << (CROSSINLINE - 72))
                            | (1L << (EXPECT - 72))
                            | (1L << (ACTUAL - 72))))
                    != 0)) {
          {
            {
              setState(363);
              topLevelObject();
            }
          }
          setState(368);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(369);
        match(EOF);
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

  public final ScriptContext script() throws RecognitionException {
    ScriptContext _localctx = new ScriptContext(_ctx, getState());
    enterRule(_localctx, 2, RULE_script);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(372);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == ShebangLine) {
          {
            setState(371);
            shebangLine();
          }
        }

        setState(377);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(374);
              match(NL);
            }
          }
          setState(379);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(383);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(380);
                fileAnnotation();
              }
            }
          }
          setState(385);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
        }
        setState(386);
        packageHeader();
        setState(387);
        importList();
        setState(393);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << LPAREN)
                            | (1L << LSQUARE)
                            | (1L << LCURL)
                            | (1L << ADD)
                            | (1L << SUB)
                            | (1L << INCR)
                            | (1L << DECR)
                            | (1L << EXCL_WS)
                            | (1L << EXCL_NO_WS)
                            | (1L << COLONCOLON)
                            | (1L << AT_NO_WS)
                            | (1L << AT_PRE_WS)
                            | (1L << RETURN_AT)
                            | (1L << CONTINUE_AT)
                            | (1L << BREAK_AT)
                            | (1L << THIS_AT)
                            | (1L << SUPER_AT)
                            | (1L << FILE)
                            | (1L << FIELD)
                            | (1L << PROPERTY)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (GET - 64))
                            | (1L << (SET - 64))
                            | (1L << (RECEIVER - 64))
                            | (1L << (PARAM - 64))
                            | (1L << (SETPARAM - 64))
                            | (1L << (DELEGATE - 64))
                            | (1L << (IMPORT - 64))
                            | (1L << (CLASS - 64))
                            | (1L << (INTERFACE - 64))
                            | (1L << (FUN - 64))
                            | (1L << (OBJECT - 64))
                            | (1L << (VAL - 64))
                            | (1L << (VAR - 64))
                            | (1L << (TYPE_ALIAS - 64))
                            | (1L << (CONSTRUCTOR - 64))
                            | (1L << (BY - 64))
                            | (1L << (COMPANION - 64))
                            | (1L << (INIT - 64))
                            | (1L << (THIS - 64))
                            | (1L << (SUPER - 64))
                            | (1L << (WHERE - 64))
                            | (1L << (IF - 64))
                            | (1L << (WHEN - 64))
                            | (1L << (TRY - 64))
                            | (1L << (CATCH - 64))
                            | (1L << (FINALLY - 64))
                            | (1L << (FOR - 64))
                            | (1L << (DO - 64))
                            | (1L << (WHILE - 64))
                            | (1L << (THROW - 64))
                            | (1L << (RETURN - 64))
                            | (1L << (CONTINUE - 64))
                            | (1L << (BREAK - 64))
                            | (1L << (OUT - 64))
                            | (1L << (DYNAMIC - 64))
                            | (1L << (PUBLIC - 64))
                            | (1L << (PRIVATE - 64))
                            | (1L << (PROTECTED - 64))
                            | (1L << (INTERNAL - 64))
                            | (1L << (ENUM - 64))
                            | (1L << (SEALED - 64))
                            | (1L << (ANNOTATION - 64))
                            | (1L << (DATA - 64))
                            | (1L << (INNER - 64))
                            | (1L << (VALUE - 64))
                            | (1L << (TAILREC - 64))
                            | (1L << (OPERATOR - 64))
                            | (1L << (INLINE - 64))
                            | (1L << (INFIX - 64))
                            | (1L << (EXTERNAL - 64))
                            | (1L << (SUSPEND - 64))
                            | (1L << (OVERRIDE - 64))
                            | (1L << (ABSTRACT - 64))
                            | (1L << (FINAL - 64))
                            | (1L << (OPEN - 64))
                            | (1L << (CONST - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (LATEINIT - 128))
                            | (1L << (VARARG - 128))
                            | (1L << (NOINLINE - 128))
                            | (1L << (CROSSINLINE - 128))
                            | (1L << (REIFIED - 128))
                            | (1L << (EXPECT - 128))
                            | (1L << (ACTUAL - 128))
                            | (1L << (RealLiteral - 128))
                            | (1L << (IntegerLiteral - 128))
                            | (1L << (HexLiteral - 128))
                            | (1L << (BinLiteral - 128))
                            | (1L << (UnsignedLiteral - 128))
                            | (1L << (LongLiteral - 128))
                            | (1L << (BooleanLiteral - 128))
                            | (1L << (NullLiteral - 128))
                            | (1L << (CharacterLiteral - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (QUOTE_OPEN - 128))
                            | (1L << (TRIPLE_QUOTE_OPEN - 128))))
                    != 0)) {
          {
            {
              setState(388);
              statement();
              setState(389);
              semi();
            }
          }
          setState(395);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(396);
        match(EOF);
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

  public final ShebangLineContext shebangLine() throws RecognitionException {
    ShebangLineContext _localctx = new ShebangLineContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_shebangLine);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(398);
        match(ShebangLine);
        setState(400);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(399);
                  match(NL);
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(402);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 8, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final FileAnnotationContext fileAnnotation() throws RecognitionException {
    FileAnnotationContext _localctx = new FileAnnotationContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_fileAnnotation);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(404);
        _la = _input.LA(1);
        if (!(_la == AT_NO_WS || _la == AT_PRE_WS)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
        setState(405);
        match(FILE);
        setState(409);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(406);
              match(NL);
            }
          }
          setState(411);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(412);
        match(COLON);
        setState(416);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(413);
              match(NL);
            }
          }
          setState(418);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(428);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case LSQUARE:
            {
              setState(419);
              match(LSQUARE);
              setState(421);
              _errHandler.sync(this);
              _la = _input.LA(1);
              do {
                {
                  {
                    setState(420);
                    unescapedAnnotation();
                  }
                }
                setState(423);
                _errHandler.sync(this);
                _la = _input.LA(1);
              } while (((((_la - 61)) & ~0x3f) == 0
                      && ((1L << (_la - 61))
                              & ((1L << (FILE - 61))
                                  | (1L << (FIELD - 61))
                                  | (1L << (PROPERTY - 61))
                                  | (1L << (GET - 61))
                                  | (1L << (SET - 61))
                                  | (1L << (RECEIVER - 61))
                                  | (1L << (PARAM - 61))
                                  | (1L << (SETPARAM - 61))
                                  | (1L << (DELEGATE - 61))
                                  | (1L << (IMPORT - 61))
                                  | (1L << (CONSTRUCTOR - 61))
                                  | (1L << (BY - 61))
                                  | (1L << (COMPANION - 61))
                                  | (1L << (INIT - 61))
                                  | (1L << (WHERE - 61))
                                  | (1L << (CATCH - 61))
                                  | (1L << (FINALLY - 61))
                                  | (1L << (OUT - 61))
                                  | (1L << (DYNAMIC - 61))
                                  | (1L << (PUBLIC - 61))
                                  | (1L << (PRIVATE - 61))
                                  | (1L << (PROTECTED - 61))
                                  | (1L << (INTERNAL - 61))
                                  | (1L << (ENUM - 61))
                                  | (1L << (SEALED - 61))
                                  | (1L << (ANNOTATION - 61))
                                  | (1L << (DATA - 61))
                                  | (1L << (INNER - 61))
                                  | (1L << (VALUE - 61))
                                  | (1L << (TAILREC - 61))
                                  | (1L << (OPERATOR - 61))
                                  | (1L << (INLINE - 61))
                                  | (1L << (INFIX - 61))
                                  | (1L << (EXTERNAL - 61))
                                  | (1L << (SUSPEND - 61))
                                  | (1L << (OVERRIDE - 61))
                                  | (1L << (ABSTRACT - 61))))
                          != 0)
                  || ((((_la - 125)) & ~0x3f) == 0
                      && ((1L << (_la - 125))
                              & ((1L << (FINAL - 125))
                                  | (1L << (OPEN - 125))
                                  | (1L << (CONST - 125))
                                  | (1L << (LATEINIT - 125))
                                  | (1L << (VARARG - 125))
                                  | (1L << (NOINLINE - 125))
                                  | (1L << (CROSSINLINE - 125))
                                  | (1L << (REIFIED - 125))
                                  | (1L << (EXPECT - 125))
                                  | (1L << (ACTUAL - 125))
                                  | (1L << (Identifier - 125))))
                          != 0));
              setState(425);
              match(RSQUARE);
            }
            break;
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case WHERE:
          case CATCH:
          case FINALLY:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case Identifier:
            {
              setState(427);
              unescapedAnnotation();
            }
            break;
          default:
            throw new NoViableAltException(this);
        }
        setState(433);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(430);
              match(NL);
            }
          }
          setState(435);
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

  public final PackageHeaderContext packageHeader() throws RecognitionException {
    PackageHeaderContext _localctx = new PackageHeaderContext(_ctx, getState());
    enterRule(_localctx, 8, RULE_packageHeader);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(441);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == PACKAGE) {
          {
            setState(436);
            match(PACKAGE);
            setState(437);
            identifier();
            setState(439);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
              case 1:
                {
                  setState(438);
                  semi();
                }
                break;
            }
          }
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

  public final ImportListContext importList() throws RecognitionException {
    ImportListContext _localctx = new ImportListContext(_ctx, getState());
    enterRule(_localctx, 10, RULE_importList);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(446);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(443);
                importHeader();
              }
            }
          }
          setState(448);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 16, _ctx);
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

  public final ImportHeaderContext importHeader() throws RecognitionException {
    ImportHeaderContext _localctx = new ImportHeaderContext(_ctx, getState());
    enterRule(_localctx, 12, RULE_importHeader);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(449);
        match(IMPORT);
        setState(450);
        identifier();
        setState(454);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case DOT:
            {
              setState(451);
              match(DOT);
              setState(452);
              match(MULT);
            }
            break;
          case AS:
            {
              setState(453);
              importAlias();
            }
            break;
          case EOF:
          case NL:
          case LPAREN:
          case LSQUARE:
          case LCURL:
          case ADD:
          case SUB:
          case INCR:
          case DECR:
          case EXCL_WS:
          case EXCL_NO_WS:
          case SEMICOLON:
          case COLONCOLON:
          case AT_NO_WS:
          case AT_PRE_WS:
          case RETURN_AT:
          case CONTINUE_AT:
          case BREAK_AT:
          case THIS_AT:
          case SUPER_AT:
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CLASS:
          case INTERFACE:
          case FUN:
          case OBJECT:
          case VAL:
          case VAR:
          case TYPE_ALIAS:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case THIS:
          case SUPER:
          case WHERE:
          case IF:
          case WHEN:
          case TRY:
          case CATCH:
          case FINALLY:
          case FOR:
          case DO:
          case WHILE:
          case THROW:
          case RETURN:
          case CONTINUE:
          case BREAK:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case RealLiteral:
          case IntegerLiteral:
          case HexLiteral:
          case BinLiteral:
          case UnsignedLiteral:
          case LongLiteral:
          case BooleanLiteral:
          case NullLiteral:
          case CharacterLiteral:
          case Identifier:
          case QUOTE_OPEN:
          case TRIPLE_QUOTE_OPEN:
            break;
          default:
            break;
        }
        setState(457);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 18, _ctx)) {
          case 1:
            {
              setState(456);
              semi();
            }
            break;
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

  public final ImportAliasContext importAlias() throws RecognitionException {
    ImportAliasContext _localctx = new ImportAliasContext(_ctx, getState());
    enterRule(_localctx, 14, RULE_importAlias);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(459);
        match(AS);
        setState(460);
        simpleIdentifier();
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

  public final TopLevelObjectContext topLevelObject() throws RecognitionException {
    TopLevelObjectContext _localctx = new TopLevelObjectContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_topLevelObject);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(462);
        declaration();
        setState(464);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 19, _ctx)) {
          case 1:
            {
              setState(463);
              semis();
            }
            break;
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

  public final TypeAliasContext typeAlias() throws RecognitionException {
    TypeAliasContext _localctx = new TypeAliasContext(_ctx, getState());
    enterRule(_localctx, 18, RULE_typeAlias);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(467);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(466);
            modifiers();
          }
        }

        setState(469);
        match(TYPE_ALIAS);
        setState(473);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(470);
              match(NL);
            }
          }
          setState(475);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(476);
        simpleIdentifier();
        setState(484);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 23, _ctx)) {
          case 1:
            {
              setState(480);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(477);
                    match(NL);
                  }
                }
                setState(482);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(483);
              typeParameters();
            }
            break;
        }
        setState(489);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(486);
              match(NL);
            }
          }
          setState(491);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(492);
        match(ASSIGNMENT);
        setState(496);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(493);
              match(NL);
            }
          }
          setState(498);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(499);
        type();
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

  public final DeclarationContext declaration() throws RecognitionException {
    DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
    enterRule(_localctx, 20, RULE_declaration);
    try {
      setState(506);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 26, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(501);
            classDeclaration();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(502);
            objectDeclaration();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(503);
            functionDeclaration();
          }
          break;
        case 4:
          enterOuterAlt(_localctx, 4);
          {
            setState(504);
            propertyDeclaration();
          }
          break;
        case 5:
          enterOuterAlt(_localctx, 5);
          {
            setState(505);
            typeAlias();
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

  public final ClassDeclarationContext classDeclaration() throws RecognitionException {
    ClassDeclarationContext _localctx = new ClassDeclarationContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_classDeclaration);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(509);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(508);
            modifiers();
          }
        }

        setState(522);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case CLASS:
            {
              setState(511);
              match(CLASS);
            }
            break;
          case INTERFACE:
          case FUN:
            {
              setState(519);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == FUN) {
                {
                  setState(512);
                  match(FUN);
                  setState(516);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(513);
                        match(NL);
                      }
                    }
                    setState(518);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                }
              }

              setState(521);
              match(INTERFACE);
            }
            break;
          default:
            throw new NoViableAltException(this);
        }
        setState(527);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(524);
              match(NL);
            }
          }
          setState(529);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(530);
        simpleIdentifier();
        setState(538);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 33, _ctx)) {
          case 1:
            {
              setState(534);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(531);
                    match(NL);
                  }
                }
                setState(536);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(537);
              typeParameters();
            }
            break;
        }
        setState(547);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 35, _ctx)) {
          case 1:
            {
              setState(543);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(540);
                    match(NL);
                  }
                }
                setState(545);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(546);
              primaryConstructor();
            }
            break;
        }
        setState(563);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 38, _ctx)) {
          case 1:
            {
              setState(552);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(549);
                    match(NL);
                  }
                }
                setState(554);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(555);
              match(COLON);
              setState(559);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(556);
                      match(NL);
                    }
                  }
                }
                setState(561);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 37, _ctx);
              }
              setState(562);
              delegationSpecifiers();
            }
            break;
        }
        setState(572);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 40, _ctx)) {
          case 1:
            {
              setState(568);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(565);
                    match(NL);
                  }
                }
                setState(570);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(571);
              typeConstraints();
            }
            break;
        }
        setState(588);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 43, _ctx)) {
          case 1:
            {
              setState(577);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(574);
                    match(NL);
                  }
                }
                setState(579);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(580);
              classBody();
            }
            break;
          case 2:
            {
              setState(584);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(581);
                    match(NL);
                  }
                }
                setState(586);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(587);
              enumClassBody();
            }
            break;
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

  public final PrimaryConstructorContext primaryConstructor() throws RecognitionException {
    PrimaryConstructorContext _localctx = new PrimaryConstructorContext(_ctx, getState());
    enterRule(_localctx, 24, RULE_primaryConstructor);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(600);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 79)) & ~0x3f) == 0
                && ((1L << (_la - 79))
                        & ((1L << (CONSTRUCTOR - 79))
                            | (1L << (PUBLIC - 79))
                            | (1L << (PRIVATE - 79))
                            | (1L << (PROTECTED - 79))
                            | (1L << (INTERNAL - 79))
                            | (1L << (ENUM - 79))
                            | (1L << (SEALED - 79))
                            | (1L << (ANNOTATION - 79))
                            | (1L << (DATA - 79))
                            | (1L << (INNER - 79))
                            | (1L << (VALUE - 79))
                            | (1L << (TAILREC - 79))
                            | (1L << (OPERATOR - 79))
                            | (1L << (INLINE - 79))
                            | (1L << (INFIX - 79))
                            | (1L << (EXTERNAL - 79))
                            | (1L << (SUSPEND - 79))
                            | (1L << (OVERRIDE - 79))
                            | (1L << (ABSTRACT - 79))
                            | (1L << (FINAL - 79))
                            | (1L << (OPEN - 79))
                            | (1L << (CONST - 79))
                            | (1L << (LATEINIT - 79))
                            | (1L << (VARARG - 79))
                            | (1L << (NOINLINE - 79))
                            | (1L << (CROSSINLINE - 79))
                            | (1L << (EXPECT - 79))
                            | (1L << (ACTUAL - 79))))
                    != 0)) {
          {
            setState(591);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == AT_NO_WS
                || _la == AT_PRE_WS
                || ((((_la - 107)) & ~0x3f) == 0
                    && ((1L << (_la - 107))
                            & ((1L << (PUBLIC - 107))
                                | (1L << (PRIVATE - 107))
                                | (1L << (PROTECTED - 107))
                                | (1L << (INTERNAL - 107))
                                | (1L << (ENUM - 107))
                                | (1L << (SEALED - 107))
                                | (1L << (ANNOTATION - 107))
                                | (1L << (DATA - 107))
                                | (1L << (INNER - 107))
                                | (1L << (VALUE - 107))
                                | (1L << (TAILREC - 107))
                                | (1L << (OPERATOR - 107))
                                | (1L << (INLINE - 107))
                                | (1L << (INFIX - 107))
                                | (1L << (EXTERNAL - 107))
                                | (1L << (SUSPEND - 107))
                                | (1L << (OVERRIDE - 107))
                                | (1L << (ABSTRACT - 107))
                                | (1L << (FINAL - 107))
                                | (1L << (OPEN - 107))
                                | (1L << (CONST - 107))
                                | (1L << (LATEINIT - 107))
                                | (1L << (VARARG - 107))
                                | (1L << (NOINLINE - 107))
                                | (1L << (CROSSINLINE - 107))
                                | (1L << (EXPECT - 107))
                                | (1L << (ACTUAL - 107))))
                        != 0)) {
              {
                setState(590);
                modifiers();
              }
            }

            setState(593);
            match(CONSTRUCTOR);
            setState(597);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(594);
                  match(NL);
                }
              }
              setState(599);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
        }

        setState(602);
        classParameters();
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

  public final ClassBodyContext classBody() throws RecognitionException {
    ClassBodyContext _localctx = new ClassBodyContext(_ctx, getState());
    enterRule(_localctx, 26, RULE_classBody);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(604);
        match(LCURL);
        setState(608);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 47, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(605);
                match(NL);
              }
            }
          }
          setState(610);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 47, _ctx);
        }
        setState(611);
        classMemberDeclarations();
        setState(615);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(612);
              match(NL);
            }
          }
          setState(617);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(618);
        match(RCURL);
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

  public final ClassParametersContext classParameters() throws RecognitionException {
    ClassParametersContext _localctx = new ClassParametersContext(_ctx, getState());
    enterRule(_localctx, 28, RULE_classParameters);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(620);
        match(LPAREN);
        setState(624);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 49, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(621);
                match(NL);
              }
            }
          }
          setState(626);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 49, _ctx);
        }
        setState(656);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 55, _ctx)) {
          case 1:
            {
              setState(627);
              classParameter();
              setState(644);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 52, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(631);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                      while (_la == NL) {
                        {
                          {
                            setState(628);
                            match(NL);
                          }
                        }
                        setState(633);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                      }
                      setState(634);
                      match(COMMA);
                      setState(638);
                      _errHandler.sync(this);
                      _alt = getInterpreter().adaptivePredict(_input, 51, _ctx);
                      while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                          {
                            {
                              setState(635);
                              match(NL);
                            }
                          }
                        }
                        setState(640);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 51, _ctx);
                      }
                      setState(641);
                      classParameter();
                    }
                  }
                }
                setState(646);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 52, _ctx);
              }
              setState(654);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 54, _ctx)) {
                case 1:
                  {
                    setState(650);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(647);
                          match(NL);
                        }
                      }
                      setState(652);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(653);
                    match(COMMA);
                  }
                  break;
              }
            }
            break;
        }
        setState(661);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(658);
              match(NL);
            }
          }
          setState(663);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(664);
        match(RPAREN);
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

  public final ClassParameterContext classParameter() throws RecognitionException {
    ClassParameterContext _localctx = new ClassParameterContext(_ctx, getState());
    enterRule(_localctx, 30, RULE_classParameter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(667);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 57, _ctx)) {
          case 1:
            {
              setState(666);
              modifiers();
            }
            break;
        }
        setState(670);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == VAL || _la == VAR) {
          {
            setState(669);
            _la = _input.LA(1);
            if (!(_la == VAL || _la == VAR)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) matchedEOF = true;
              _errHandler.reportMatch(this);
              consume();
            }
          }
        }

        setState(675);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(672);
              match(NL);
            }
          }
          setState(677);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(678);
        simpleIdentifier();
        setState(679);
        match(COLON);
        setState(683);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(680);
              match(NL);
            }
          }
          setState(685);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(686);
        type();
        setState(701);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 63, _ctx)) {
          case 1:
            {
              setState(690);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(687);
                    match(NL);
                  }
                }
                setState(692);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(693);
              match(ASSIGNMENT);
              setState(697);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(694);
                    match(NL);
                  }
                }
                setState(699);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(700);
              expression();
            }
            break;
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

  public final DelegationSpecifiersContext delegationSpecifiers() throws RecognitionException {
    DelegationSpecifiersContext _localctx = new DelegationSpecifiersContext(_ctx, getState());
    enterRule(_localctx, 32, RULE_delegationSpecifiers);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(703);
        annotatedDelegationSpecifier();
        setState(720);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 66, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(707);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(704);
                      match(NL);
                    }
                  }
                  setState(709);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(710);
                match(COMMA);
                setState(714);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 65, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                  if (_alt == 1) {
                    {
                      {
                        setState(711);
                        match(NL);
                      }
                    }
                  }
                  setState(716);
                  _errHandler.sync(this);
                  _alt = getInterpreter().adaptivePredict(_input, 65, _ctx);
                }
                setState(717);
                annotatedDelegationSpecifier();
              }
            }
          }
          setState(722);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 66, _ctx);
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

  public final DelegationSpecifierContext delegationSpecifier() throws RecognitionException {
    DelegationSpecifierContext _localctx = new DelegationSpecifierContext(_ctx, getState());
    enterRule(_localctx, 34, RULE_delegationSpecifier);
    try {
      setState(727);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 67, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(723);
            constructorInvocation();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(724);
            explicitDelegation();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(725);
            userType();
          }
          break;
        case 4:
          enterOuterAlt(_localctx, 4);
          {
            setState(726);
            functionType();
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

  public final ConstructorInvocationContext constructorInvocation() throws RecognitionException {
    ConstructorInvocationContext _localctx = new ConstructorInvocationContext(_ctx, getState());
    enterRule(_localctx, 36, RULE_constructorInvocation);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(729);
        userType();
        setState(730);
        valueArguments();
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

  public final AnnotatedDelegationSpecifierContext annotatedDelegationSpecifier()
      throws RecognitionException {
    AnnotatedDelegationSpecifierContext _localctx =
        new AnnotatedDelegationSpecifierContext(_ctx, getState());
    enterRule(_localctx, 38, RULE_annotatedDelegationSpecifier);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(735);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 68, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(732);
                annotation();
              }
            }
          }
          setState(737);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 68, _ctx);
        }
        setState(741);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(738);
              match(NL);
            }
          }
          setState(743);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(744);
        delegationSpecifier();
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

  public final ExplicitDelegationContext explicitDelegation() throws RecognitionException {
    ExplicitDelegationContext _localctx = new ExplicitDelegationContext(_ctx, getState());
    enterRule(_localctx, 40, RULE_explicitDelegation);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(748);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 70, _ctx)) {
          case 1:
            {
              setState(746);
              userType();
            }
            break;
          case 2:
            {
              setState(747);
              functionType();
            }
            break;
        }
        setState(753);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(750);
              match(NL);
            }
          }
          setState(755);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(756);
        match(BY);
        setState(760);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(757);
              match(NL);
            }
          }
          setState(762);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(763);
        expression();
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

  public final TypeParametersContext typeParameters() throws RecognitionException {
    TypeParametersContext _localctx = new TypeParametersContext(_ctx, getState());
    enterRule(_localctx, 42, RULE_typeParameters);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(765);
        match(LANGLE);
        setState(769);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 73, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(766);
                match(NL);
              }
            }
          }
          setState(771);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 73, _ctx);
        }
        setState(772);
        typeParameter();
        setState(789);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 76, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(776);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(773);
                      match(NL);
                    }
                  }
                  setState(778);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(779);
                match(COMMA);
                setState(783);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 75, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                  if (_alt == 1) {
                    {
                      {
                        setState(780);
                        match(NL);
                      }
                    }
                  }
                  setState(785);
                  _errHandler.sync(this);
                  _alt = getInterpreter().adaptivePredict(_input, 75, _ctx);
                }
                setState(786);
                typeParameter();
              }
            }
          }
          setState(791);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 76, _ctx);
        }
        setState(799);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 78, _ctx)) {
          case 1:
            {
              setState(795);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(792);
                    match(NL);
                  }
                }
                setState(797);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(798);
              match(COMMA);
            }
            break;
        }
        setState(804);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(801);
              match(NL);
            }
          }
          setState(806);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(807);
        match(RANGLE);
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

  public final TypeParameterContext typeParameter() throws RecognitionException {
    TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
    enterRule(_localctx, 44, RULE_typeParameter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(810);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 80, _ctx)) {
          case 1:
            {
              setState(809);
              typeParameterModifiers();
            }
            break;
        }
        setState(815);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(812);
              match(NL);
            }
          }
          setState(817);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(818);
        simpleIdentifier();
        setState(833);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 84, _ctx)) {
          case 1:
            {
              setState(822);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(819);
                    match(NL);
                  }
                }
                setState(824);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(825);
              match(COLON);
              setState(829);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(826);
                    match(NL);
                  }
                }
                setState(831);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(832);
              type();
            }
            break;
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

  public final TypeConstraintsContext typeConstraints() throws RecognitionException {
    TypeConstraintsContext _localctx = new TypeConstraintsContext(_ctx, getState());
    enterRule(_localctx, 46, RULE_typeConstraints);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(835);
        match(WHERE);
        setState(839);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(836);
              match(NL);
            }
          }
          setState(841);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(842);
        typeConstraint();
        setState(859);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 88, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(846);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(843);
                      match(NL);
                    }
                  }
                  setState(848);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(849);
                match(COMMA);
                setState(853);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(850);
                      match(NL);
                    }
                  }
                  setState(855);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(856);
                typeConstraint();
              }
            }
          }
          setState(861);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 88, _ctx);
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

  public final TypeConstraintContext typeConstraint() throws RecognitionException {
    TypeConstraintContext _localctx = new TypeConstraintContext(_ctx, getState());
    enterRule(_localctx, 48, RULE_typeConstraint);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(865);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == AT_NO_WS || _la == AT_PRE_WS) {
          {
            {
              setState(862);
              annotation();
            }
          }
          setState(867);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(868);
        simpleIdentifier();
        setState(872);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(869);
              match(NL);
            }
          }
          setState(874);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(875);
        match(COLON);
        setState(879);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(876);
              match(NL);
            }
          }
          setState(881);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(882);
        type();
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

  public final ClassMemberDeclarationsContext classMemberDeclarations()
      throws RecognitionException {
    ClassMemberDeclarationsContext _localctx = new ClassMemberDeclarationsContext(_ctx, getState());
    enterRule(_localctx, 50, RULE_classMemberDeclarations);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(890);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 72)) & ~0x3f) == 0
                && ((1L << (_la - 72))
                        & ((1L << (CLASS - 72))
                            | (1L << (INTERFACE - 72))
                            | (1L << (FUN - 72))
                            | (1L << (OBJECT - 72))
                            | (1L << (VAL - 72))
                            | (1L << (VAR - 72))
                            | (1L << (TYPE_ALIAS - 72))
                            | (1L << (CONSTRUCTOR - 72))
                            | (1L << (COMPANION - 72))
                            | (1L << (INIT - 72))
                            | (1L << (PUBLIC - 72))
                            | (1L << (PRIVATE - 72))
                            | (1L << (PROTECTED - 72))
                            | (1L << (INTERNAL - 72))
                            | (1L << (ENUM - 72))
                            | (1L << (SEALED - 72))
                            | (1L << (ANNOTATION - 72))
                            | (1L << (DATA - 72))
                            | (1L << (INNER - 72))
                            | (1L << (VALUE - 72))
                            | (1L << (TAILREC - 72))
                            | (1L << (OPERATOR - 72))
                            | (1L << (INLINE - 72))
                            | (1L << (INFIX - 72))
                            | (1L << (EXTERNAL - 72))
                            | (1L << (SUSPEND - 72))
                            | (1L << (OVERRIDE - 72))
                            | (1L << (ABSTRACT - 72))
                            | (1L << (FINAL - 72))
                            | (1L << (OPEN - 72))
                            | (1L << (CONST - 72))
                            | (1L << (LATEINIT - 72))
                            | (1L << (VARARG - 72))
                            | (1L << (NOINLINE - 72))
                            | (1L << (CROSSINLINE - 72))
                            | (1L << (EXPECT - 72))
                            | (1L << (ACTUAL - 72))))
                    != 0)) {
          {
            {
              setState(884);
              classMemberDeclaration();
              setState(886);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 92, _ctx)) {
                case 1:
                  {
                    setState(885);
                    semis();
                  }
                  break;
              }
            }
          }
          setState(892);
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

  public final ClassMemberDeclarationContext classMemberDeclaration() throws RecognitionException {
    ClassMemberDeclarationContext _localctx = new ClassMemberDeclarationContext(_ctx, getState());
    enterRule(_localctx, 52, RULE_classMemberDeclaration);
    try {
      setState(897);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 94, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(893);
            declaration();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(894);
            companionObject();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(895);
            anonymousInitializer();
          }
          break;
        case 4:
          enterOuterAlt(_localctx, 4);
          {
            setState(896);
            secondaryConstructor();
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

  public final AnonymousInitializerContext anonymousInitializer() throws RecognitionException {
    AnonymousInitializerContext _localctx = new AnonymousInitializerContext(_ctx, getState());
    enterRule(_localctx, 54, RULE_anonymousInitializer);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(899);
        match(INIT);
        setState(903);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(900);
              match(NL);
            }
          }
          setState(905);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(906);
        block();
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

  public final CompanionObjectContext companionObject() throws RecognitionException {
    CompanionObjectContext _localctx = new CompanionObjectContext(_ctx, getState());
    enterRule(_localctx, 56, RULE_companionObject);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(909);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(908);
            modifiers();
          }
        }

        setState(911);
        match(COMPANION);
        setState(915);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(912);
              match(NL);
            }
          }
          setState(917);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(918);
        match(OBJECT);
        setState(926);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 99, _ctx)) {
          case 1:
            {
              setState(922);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(919);
                    match(NL);
                  }
                }
                setState(924);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(925);
              simpleIdentifier();
            }
            break;
        }
        setState(942);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 102, _ctx)) {
          case 1:
            {
              setState(931);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(928);
                    match(NL);
                  }
                }
                setState(933);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(934);
              match(COLON);
              setState(938);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 101, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(935);
                      match(NL);
                    }
                  }
                }
                setState(940);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 101, _ctx);
              }
              setState(941);
              delegationSpecifiers();
            }
            break;
        }
        setState(951);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 104, _ctx)) {
          case 1:
            {
              setState(947);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(944);
                    match(NL);
                  }
                }
                setState(949);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(950);
              classBody();
            }
            break;
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

  public final FunctionValueParametersContext functionValueParameters()
      throws RecognitionException {
    FunctionValueParametersContext _localctx = new FunctionValueParametersContext(_ctx, getState());
    enterRule(_localctx, 58, RULE_functionValueParameters);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(953);
        match(LPAREN);
        setState(957);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 105, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(954);
                match(NL);
              }
            }
          }
          setState(959);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 105, _ctx);
        }
        setState(989);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (((((_la - 40)) & ~0x3f) == 0
                && ((1L << (_la - 40))
                        & ((1L << (AT_NO_WS - 40))
                            | (1L << (AT_PRE_WS - 40))
                            | (1L << (FILE - 40))
                            | (1L << (FIELD - 40))
                            | (1L << (PROPERTY - 40))
                            | (1L << (GET - 40))
                            | (1L << (SET - 40))
                            | (1L << (RECEIVER - 40))
                            | (1L << (PARAM - 40))
                            | (1L << (SETPARAM - 40))
                            | (1L << (DELEGATE - 40))
                            | (1L << (IMPORT - 40))
                            | (1L << (CONSTRUCTOR - 40))
                            | (1L << (BY - 40))
                            | (1L << (COMPANION - 40))
                            | (1L << (INIT - 40))
                            | (1L << (WHERE - 40))
                            | (1L << (CATCH - 40))
                            | (1L << (FINALLY - 40))))
                    != 0)
            || ((((_la - 105)) & ~0x3f) == 0
                && ((1L << (_la - 105))
                        & ((1L << (OUT - 105))
                            | (1L << (DYNAMIC - 105))
                            | (1L << (PUBLIC - 105))
                            | (1L << (PRIVATE - 105))
                            | (1L << (PROTECTED - 105))
                            | (1L << (INTERNAL - 105))
                            | (1L << (ENUM - 105))
                            | (1L << (SEALED - 105))
                            | (1L << (ANNOTATION - 105))
                            | (1L << (DATA - 105))
                            | (1L << (INNER - 105))
                            | (1L << (VALUE - 105))
                            | (1L << (TAILREC - 105))
                            | (1L << (OPERATOR - 105))
                            | (1L << (INLINE - 105))
                            | (1L << (INFIX - 105))
                            | (1L << (EXTERNAL - 105))
                            | (1L << (SUSPEND - 105))
                            | (1L << (OVERRIDE - 105))
                            | (1L << (ABSTRACT - 105))
                            | (1L << (FINAL - 105))
                            | (1L << (OPEN - 105))
                            | (1L << (CONST - 105))
                            | (1L << (LATEINIT - 105))
                            | (1L << (VARARG - 105))
                            | (1L << (NOINLINE - 105))
                            | (1L << (CROSSINLINE - 105))
                            | (1L << (REIFIED - 105))
                            | (1L << (EXPECT - 105))
                            | (1L << (ACTUAL - 105))
                            | (1L << (Identifier - 105))))
                    != 0)) {
          {
            setState(960);
            functionValueParameter();
            setState(977);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 108, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(964);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(961);
                          match(NL);
                        }
                      }
                      setState(966);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(967);
                    match(COMMA);
                    setState(971);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(968);
                          match(NL);
                        }
                      }
                      setState(973);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(974);
                    functionValueParameter();
                  }
                }
              }
              setState(979);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 108, _ctx);
            }
            setState(987);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 110, _ctx)) {
              case 1:
                {
                  setState(983);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(980);
                        match(NL);
                      }
                    }
                    setState(985);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(986);
                  match(COMMA);
                }
                break;
            }
          }
        }

        setState(994);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(991);
              match(NL);
            }
          }
          setState(996);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(997);
        match(RPAREN);
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

  public final FunctionValueParameterContext functionValueParameter() throws RecognitionException {
    FunctionValueParameterContext _localctx = new FunctionValueParameterContext(_ctx, getState());
    enterRule(_localctx, 60, RULE_functionValueParameter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1000);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 113, _ctx)) {
          case 1:
            {
              setState(999);
              parameterModifiers();
            }
            break;
        }
        setState(1002);
        parameter();
        setState(1017);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 116, _ctx)) {
          case 1:
            {
              setState(1006);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1003);
                    match(NL);
                  }
                }
                setState(1008);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1009);
              match(ASSIGNMENT);
              setState(1013);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1010);
                    match(NL);
                  }
                }
                setState(1015);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1016);
              expression();
            }
            break;
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

  public final FunctionDeclarationContext functionDeclaration() throws RecognitionException {
    FunctionDeclarationContext _localctx = new FunctionDeclarationContext(_ctx, getState());
    enterRule(_localctx, 62, RULE_functionDeclaration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1020);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(1019);
            modifiers();
          }
        }

        setState(1022);
        match(FUN);
        setState(1030);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 119, _ctx)) {
          case 1:
            {
              setState(1026);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1023);
                    match(NL);
                  }
                }
                setState(1028);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1029);
              typeParameters();
            }
            break;
        }
        setState(1047);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 122, _ctx)) {
          case 1:
            {
              setState(1035);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1032);
                    match(NL);
                  }
                }
                setState(1037);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1038);
              receiverType();
              setState(1042);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1039);
                    match(NL);
                  }
                }
                setState(1044);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1045);
              match(DOT);
            }
            break;
        }
        setState(1052);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1049);
              match(NL);
            }
          }
          setState(1054);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1055);
        simpleIdentifier();
        setState(1059);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1056);
              match(NL);
            }
          }
          setState(1061);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1062);
        functionValueParameters();
        setState(1077);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 127, _ctx)) {
          case 1:
            {
              setState(1066);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1063);
                    match(NL);
                  }
                }
                setState(1068);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1069);
              match(COLON);
              setState(1073);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1070);
                    match(NL);
                  }
                }
                setState(1075);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1076);
              type();
            }
            break;
        }
        setState(1086);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 129, _ctx)) {
          case 1:
            {
              setState(1082);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1079);
                    match(NL);
                  }
                }
                setState(1084);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1085);
              typeConstraints();
            }
            break;
        }
        setState(1095);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 131, _ctx)) {
          case 1:
            {
              setState(1091);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1088);
                    match(NL);
                  }
                }
                setState(1093);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1094);
              functionBody();
            }
            break;
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

  public final FunctionBodyContext functionBody() throws RecognitionException {
    FunctionBodyContext _localctx = new FunctionBodyContext(_ctx, getState());
    enterRule(_localctx, 64, RULE_functionBody);
    int _la;
    try {
      setState(1106);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LCURL:
          enterOuterAlt(_localctx, 1);
          {
            setState(1097);
            block();
          }
          break;
        case ASSIGNMENT:
          enterOuterAlt(_localctx, 2);
          {
            setState(1098);
            match(ASSIGNMENT);
            setState(1102);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(1099);
                  match(NL);
                }
              }
              setState(1104);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(1105);
            expression();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
    VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
    enterRule(_localctx, 66, RULE_variableDeclaration);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1111);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == AT_NO_WS || _la == AT_PRE_WS) {
          {
            {
              setState(1108);
              annotation();
            }
          }
          setState(1113);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1117);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1114);
              match(NL);
            }
          }
          setState(1119);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1120);
        simpleIdentifier();
        setState(1135);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 138, _ctx)) {
          case 1:
            {
              setState(1124);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1121);
                    match(NL);
                  }
                }
                setState(1126);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1127);
              match(COLON);
              setState(1131);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1128);
                    match(NL);
                  }
                }
                setState(1133);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1134);
              type();
            }
            break;
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

  public final MultiVariableDeclarationContext multiVariableDeclaration()
      throws RecognitionException {
    MultiVariableDeclarationContext _localctx =
        new MultiVariableDeclarationContext(_ctx, getState());
    enterRule(_localctx, 68, RULE_multiVariableDeclaration);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1137);
        match(LPAREN);
        setState(1141);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 139, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1138);
                match(NL);
              }
            }
          }
          setState(1143);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 139, _ctx);
        }
        setState(1144);
        variableDeclaration();
        setState(1161);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 142, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1148);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1145);
                      match(NL);
                    }
                  }
                  setState(1150);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1151);
                match(COMMA);
                setState(1155);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 141, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                  if (_alt == 1) {
                    {
                      {
                        setState(1152);
                        match(NL);
                      }
                    }
                  }
                  setState(1157);
                  _errHandler.sync(this);
                  _alt = getInterpreter().adaptivePredict(_input, 141, _ctx);
                }
                setState(1158);
                variableDeclaration();
              }
            }
          }
          setState(1163);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 142, _ctx);
        }
        setState(1171);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 144, _ctx)) {
          case 1:
            {
              setState(1167);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1164);
                    match(NL);
                  }
                }
                setState(1169);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1170);
              match(COMMA);
            }
            break;
        }
        setState(1176);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1173);
              match(NL);
            }
          }
          setState(1178);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1179);
        match(RPAREN);
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

  public final PropertyDeclarationContext propertyDeclaration() throws RecognitionException {
    PropertyDeclarationContext _localctx = new PropertyDeclarationContext(_ctx, getState());
    enterRule(_localctx, 70, RULE_propertyDeclaration);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1182);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(1181);
            modifiers();
          }
        }

        setState(1184);
        _la = _input.LA(1);
        if (!(_la == VAL || _la == VAR)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
        setState(1192);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 148, _ctx)) {
          case 1:
            {
              setState(1188);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1185);
                    match(NL);
                  }
                }
                setState(1190);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1191);
              typeParameters();
            }
            break;
        }
        setState(1209);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 151, _ctx)) {
          case 1:
            {
              setState(1197);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1194);
                    match(NL);
                  }
                }
                setState(1199);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1200);
              receiverType();
              setState(1204);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1201);
                    match(NL);
                  }
                }
                setState(1206);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1207);
              match(DOT);
            }
            break;
        }
        {
          setState(1214);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 152, _ctx);
          while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
            if (_alt == 1) {
              {
                {
                  setState(1211);
                  match(NL);
                }
              }
            }
            setState(1216);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 152, _ctx);
          }
          setState(1219);
          _errHandler.sync(this);
          switch (_input.LA(1)) {
            case LPAREN:
              {
                setState(1217);
                multiVariableDeclaration();
              }
              break;
            case NL:
            case AT_NO_WS:
            case AT_PRE_WS:
            case FILE:
            case FIELD:
            case PROPERTY:
            case GET:
            case SET:
            case RECEIVER:
            case PARAM:
            case SETPARAM:
            case DELEGATE:
            case IMPORT:
            case CONSTRUCTOR:
            case BY:
            case COMPANION:
            case INIT:
            case WHERE:
            case CATCH:
            case FINALLY:
            case OUT:
            case DYNAMIC:
            case PUBLIC:
            case PRIVATE:
            case PROTECTED:
            case INTERNAL:
            case ENUM:
            case SEALED:
            case ANNOTATION:
            case DATA:
            case INNER:
            case VALUE:
            case TAILREC:
            case OPERATOR:
            case INLINE:
            case INFIX:
            case EXTERNAL:
            case SUSPEND:
            case OVERRIDE:
            case ABSTRACT:
            case FINAL:
            case OPEN:
            case CONST:
            case LATEINIT:
            case VARARG:
            case NOINLINE:
            case CROSSINLINE:
            case REIFIED:
            case EXPECT:
            case ACTUAL:
            case Identifier:
              {
                setState(1218);
                variableDeclaration();
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
        }
        setState(1228);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 155, _ctx)) {
          case 1:
            {
              setState(1224);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1221);
                    match(NL);
                  }
                }
                setState(1226);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1227);
              typeConstraints();
            }
            break;
        }
        setState(1247);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 159, _ctx)) {
          case 1:
            {
              setState(1233);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1230);
                    match(NL);
                  }
                }
                setState(1235);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1245);
              _errHandler.sync(this);
              switch (_input.LA(1)) {
                case ASSIGNMENT:
                  {
                    setState(1236);
                    match(ASSIGNMENT);
                    setState(1240);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1237);
                          match(NL);
                        }
                      }
                      setState(1242);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1243);
                    expression();
                  }
                  break;
                case BY:
                  {
                    setState(1244);
                    propertyDelegate();
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
            }
            break;
        }
        setState(1255);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 161, _ctx)) {
          case 1:
            {
              setState(1250);
              _errHandler.sync(this);
              _la = _input.LA(1);
              do {
                {
                  {
                    setState(1249);
                    match(NL);
                  }
                }
                setState(1252);
                _errHandler.sync(this);
                _la = _input.LA(1);
              } while (_la == NL);
              setState(1254);
              match(SEMICOLON);
            }
            break;
        }
        setState(1260);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 162, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1257);
                match(NL);
              }
            }
          }
          setState(1262);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 162, _ctx);
        }
        setState(1293);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 171, _ctx)) {
          case 1:
            {
              setState(1264);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 163, _ctx)) {
                case 1:
                  {
                    setState(1263);
                    getter();
                  }
                  break;
              }
              setState(1276);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 166, _ctx)) {
                case 1:
                  {
                    setState(1269);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 164, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                      if (_alt == 1) {
                        {
                          {
                            setState(1266);
                            match(NL);
                          }
                        }
                      }
                      setState(1271);
                      _errHandler.sync(this);
                      _alt = getInterpreter().adaptivePredict(_input, 164, _ctx);
                    }
                    setState(1273);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (((((_la - -1)) & ~0x3f) == 0
                        && ((1L << (_la - -1))
                                & ((1L << (EOF - -1))
                                    | (1L << (NL - -1))
                                    | (1L << (SEMICOLON - -1))))
                            != 0)) {
                      {
                        setState(1272);
                        semi();
                      }
                    }

                    setState(1275);
                    setter();
                  }
                  break;
              }
            }
            break;
          case 2:
            {
              setState(1279);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 167, _ctx)) {
                case 1:
                  {
                    setState(1278);
                    setter();
                  }
                  break;
              }
              setState(1291);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 170, _ctx)) {
                case 1:
                  {
                    setState(1284);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 168, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                      if (_alt == 1) {
                        {
                          {
                            setState(1281);
                            match(NL);
                          }
                        }
                      }
                      setState(1286);
                      _errHandler.sync(this);
                      _alt = getInterpreter().adaptivePredict(_input, 168, _ctx);
                    }
                    setState(1288);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (((((_la - -1)) & ~0x3f) == 0
                        && ((1L << (_la - -1))
                                & ((1L << (EOF - -1))
                                    | (1L << (NL - -1))
                                    | (1L << (SEMICOLON - -1))))
                            != 0)) {
                      {
                        setState(1287);
                        semi();
                      }
                    }

                    setState(1290);
                    getter();
                  }
                  break;
              }
            }
            break;
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

  public final PropertyDelegateContext propertyDelegate() throws RecognitionException {
    PropertyDelegateContext _localctx = new PropertyDelegateContext(_ctx, getState());
    enterRule(_localctx, 72, RULE_propertyDelegate);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1295);
        match(BY);
        setState(1299);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1296);
              match(NL);
            }
          }
          setState(1301);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1302);
        expression();
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

  public final GetterContext getter() throws RecognitionException {
    GetterContext _localctx = new GetterContext(_ctx, getState());
    enterRule(_localctx, 74, RULE_getter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1305);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(1304);
            modifiers();
          }
        }

        setState(1307);
        match(GET);
        setState(1345);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 180, _ctx)) {
          case 1:
            {
              setState(1311);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1308);
                    match(NL);
                  }
                }
                setState(1313);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1314);
              match(LPAREN);
              setState(1318);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1315);
                    match(NL);
                  }
                }
                setState(1320);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1321);
              match(RPAREN);
              setState(1336);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 178, _ctx)) {
                case 1:
                  {
                    setState(1325);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1322);
                          match(NL);
                        }
                      }
                      setState(1327);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1328);
                    match(COLON);
                    setState(1332);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1329);
                          match(NL);
                        }
                      }
                      setState(1334);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1335);
                    type();
                  }
                  break;
              }
              setState(1341);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1338);
                    match(NL);
                  }
                }
                setState(1343);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1344);
              functionBody();
            }
            break;
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

  public final SetterContext setter() throws RecognitionException {
    SetterContext _localctx = new SetterContext(_ctx, getState());
    enterRule(_localctx, 76, RULE_setter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1348);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(1347);
            modifiers();
          }
        }

        setState(1350);
        match(SET);
        setState(1405);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 191, _ctx)) {
          case 1:
            {
              setState(1354);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1351);
                    match(NL);
                  }
                }
                setState(1356);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1357);
              match(LPAREN);
              setState(1361);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1358);
                    match(NL);
                  }
                }
                setState(1363);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1364);
              functionValueParameterWithOptionalType();
              setState(1372);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 185, _ctx)) {
                case 1:
                  {
                    setState(1368);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1365);
                          match(NL);
                        }
                      }
                      setState(1370);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1371);
                    match(COMMA);
                  }
                  break;
              }
              setState(1377);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1374);
                    match(NL);
                  }
                }
                setState(1379);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1380);
              match(RPAREN);
              setState(1395);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 189, _ctx)) {
                case 1:
                  {
                    setState(1384);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1381);
                          match(NL);
                        }
                      }
                      setState(1386);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1387);
                    match(COLON);
                    setState(1391);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1388);
                          match(NL);
                        }
                      }
                      setState(1393);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1394);
                    type();
                  }
                  break;
              }
              setState(1400);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1397);
                    match(NL);
                  }
                }
                setState(1402);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1403);
              functionBody();
            }
            break;
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

  public final ParametersWithOptionalTypeContext parametersWithOptionalType()
      throws RecognitionException {
    ParametersWithOptionalTypeContext _localctx =
        new ParametersWithOptionalTypeContext(_ctx, getState());
    enterRule(_localctx, 78, RULE_parametersWithOptionalType);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1407);
        match(LPAREN);
        setState(1411);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 192, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1408);
                match(NL);
              }
            }
          }
          setState(1413);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 192, _ctx);
        }
        setState(1443);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (((((_la - 40)) & ~0x3f) == 0
                && ((1L << (_la - 40))
                        & ((1L << (AT_NO_WS - 40))
                            | (1L << (AT_PRE_WS - 40))
                            | (1L << (FILE - 40))
                            | (1L << (FIELD - 40))
                            | (1L << (PROPERTY - 40))
                            | (1L << (GET - 40))
                            | (1L << (SET - 40))
                            | (1L << (RECEIVER - 40))
                            | (1L << (PARAM - 40))
                            | (1L << (SETPARAM - 40))
                            | (1L << (DELEGATE - 40))
                            | (1L << (IMPORT - 40))
                            | (1L << (CONSTRUCTOR - 40))
                            | (1L << (BY - 40))
                            | (1L << (COMPANION - 40))
                            | (1L << (INIT - 40))
                            | (1L << (WHERE - 40))
                            | (1L << (CATCH - 40))
                            | (1L << (FINALLY - 40))))
                    != 0)
            || ((((_la - 105)) & ~0x3f) == 0
                && ((1L << (_la - 105))
                        & ((1L << (OUT - 105))
                            | (1L << (DYNAMIC - 105))
                            | (1L << (PUBLIC - 105))
                            | (1L << (PRIVATE - 105))
                            | (1L << (PROTECTED - 105))
                            | (1L << (INTERNAL - 105))
                            | (1L << (ENUM - 105))
                            | (1L << (SEALED - 105))
                            | (1L << (ANNOTATION - 105))
                            | (1L << (DATA - 105))
                            | (1L << (INNER - 105))
                            | (1L << (VALUE - 105))
                            | (1L << (TAILREC - 105))
                            | (1L << (OPERATOR - 105))
                            | (1L << (INLINE - 105))
                            | (1L << (INFIX - 105))
                            | (1L << (EXTERNAL - 105))
                            | (1L << (SUSPEND - 105))
                            | (1L << (OVERRIDE - 105))
                            | (1L << (ABSTRACT - 105))
                            | (1L << (FINAL - 105))
                            | (1L << (OPEN - 105))
                            | (1L << (CONST - 105))
                            | (1L << (LATEINIT - 105))
                            | (1L << (VARARG - 105))
                            | (1L << (NOINLINE - 105))
                            | (1L << (CROSSINLINE - 105))
                            | (1L << (REIFIED - 105))
                            | (1L << (EXPECT - 105))
                            | (1L << (ACTUAL - 105))
                            | (1L << (Identifier - 105))))
                    != 0)) {
          {
            setState(1414);
            functionValueParameterWithOptionalType();
            setState(1431);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 195, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(1418);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1415);
                          match(NL);
                        }
                      }
                      setState(1420);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1421);
                    match(COMMA);
                    setState(1425);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(1422);
                          match(NL);
                        }
                      }
                      setState(1427);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(1428);
                    functionValueParameterWithOptionalType();
                  }
                }
              }
              setState(1433);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 195, _ctx);
            }
            setState(1441);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 197, _ctx)) {
              case 1:
                {
                  setState(1437);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(1434);
                        match(NL);
                      }
                    }
                    setState(1439);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(1440);
                  match(COMMA);
                }
                break;
            }
          }
        }

        setState(1448);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1445);
              match(NL);
            }
          }
          setState(1450);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1451);
        match(RPAREN);
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

  public final FunctionValueParameterWithOptionalTypeContext
      functionValueParameterWithOptionalType() throws RecognitionException {
    FunctionValueParameterWithOptionalTypeContext _localctx =
        new FunctionValueParameterWithOptionalTypeContext(_ctx, getState());
    enterRule(_localctx, 80, RULE_functionValueParameterWithOptionalType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1454);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 200, _ctx)) {
          case 1:
            {
              setState(1453);
              parameterModifiers();
            }
            break;
        }
        setState(1456);
        parameterWithOptionalType();
        setState(1471);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 203, _ctx)) {
          case 1:
            {
              setState(1460);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1457);
                    match(NL);
                  }
                }
                setState(1462);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1463);
              match(ASSIGNMENT);
              setState(1467);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1464);
                    match(NL);
                  }
                }
                setState(1469);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1470);
              expression();
            }
            break;
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

  public final ParameterWithOptionalTypeContext parameterWithOptionalType()
      throws RecognitionException {
    ParameterWithOptionalTypeContext _localctx =
        new ParameterWithOptionalTypeContext(_ctx, getState());
    enterRule(_localctx, 82, RULE_parameterWithOptionalType);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1473);
        simpleIdentifier();
        setState(1477);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 204, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1474);
                match(NL);
              }
            }
          }
          setState(1479);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 204, _ctx);
        }
        setState(1488);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == COLON) {
          {
            setState(1480);
            match(COLON);
            setState(1484);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(1481);
                  match(NL);
                }
              }
              setState(1486);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(1487);
            type();
          }
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

  public final ParameterContext parameter() throws RecognitionException {
    ParameterContext _localctx = new ParameterContext(_ctx, getState());
    enterRule(_localctx, 84, RULE_parameter);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1490);
        simpleIdentifier();
        setState(1494);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1491);
              match(NL);
            }
          }
          setState(1496);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1497);
        match(COLON);
        setState(1501);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1498);
              match(NL);
            }
          }
          setState(1503);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1504);
        type();
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

  public final ObjectDeclarationContext objectDeclaration() throws RecognitionException {
    ObjectDeclarationContext _localctx = new ObjectDeclarationContext(_ctx, getState());
    enterRule(_localctx, 86, RULE_objectDeclaration);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1507);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(1506);
            modifiers();
          }
        }

        setState(1509);
        match(OBJECT);
        setState(1513);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1510);
              match(NL);
            }
          }
          setState(1515);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1516);
        simpleIdentifier();
        setState(1531);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 213, _ctx)) {
          case 1:
            {
              setState(1520);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1517);
                    match(NL);
                  }
                }
                setState(1522);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1523);
              match(COLON);
              setState(1527);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 212, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(1524);
                      match(NL);
                    }
                  }
                }
                setState(1529);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 212, _ctx);
              }
              setState(1530);
              delegationSpecifiers();
            }
            break;
        }
        setState(1540);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 215, _ctx)) {
          case 1:
            {
              setState(1536);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1533);
                    match(NL);
                  }
                }
                setState(1538);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1539);
              classBody();
            }
            break;
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

  public final SecondaryConstructorContext secondaryConstructor() throws RecognitionException {
    SecondaryConstructorContext _localctx = new SecondaryConstructorContext(_ctx, getState());
    enterRule(_localctx, 88, RULE_secondaryConstructor);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1543);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == AT_NO_WS
            || _la == AT_PRE_WS
            || ((((_la - 107)) & ~0x3f) == 0
                && ((1L << (_la - 107))
                        & ((1L << (PUBLIC - 107))
                            | (1L << (PRIVATE - 107))
                            | (1L << (PROTECTED - 107))
                            | (1L << (INTERNAL - 107))
                            | (1L << (ENUM - 107))
                            | (1L << (SEALED - 107))
                            | (1L << (ANNOTATION - 107))
                            | (1L << (DATA - 107))
                            | (1L << (INNER - 107))
                            | (1L << (VALUE - 107))
                            | (1L << (TAILREC - 107))
                            | (1L << (OPERATOR - 107))
                            | (1L << (INLINE - 107))
                            | (1L << (INFIX - 107))
                            | (1L << (EXTERNAL - 107))
                            | (1L << (SUSPEND - 107))
                            | (1L << (OVERRIDE - 107))
                            | (1L << (ABSTRACT - 107))
                            | (1L << (FINAL - 107))
                            | (1L << (OPEN - 107))
                            | (1L << (CONST - 107))
                            | (1L << (LATEINIT - 107))
                            | (1L << (VARARG - 107))
                            | (1L << (NOINLINE - 107))
                            | (1L << (CROSSINLINE - 107))
                            | (1L << (EXPECT - 107))
                            | (1L << (ACTUAL - 107))))
                    != 0)) {
          {
            setState(1542);
            modifiers();
          }
        }

        setState(1545);
        match(CONSTRUCTOR);
        setState(1549);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1546);
              match(NL);
            }
          }
          setState(1551);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1552);
        functionValueParameters();
        setState(1567);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 220, _ctx)) {
          case 1:
            {
              setState(1556);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1553);
                    match(NL);
                  }
                }
                setState(1558);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1559);
              match(COLON);
              setState(1563);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1560);
                    match(NL);
                  }
                }
                setState(1565);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1566);
              constructorDelegationCall();
            }
            break;
        }
        setState(1572);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 221, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1569);
                match(NL);
              }
            }
          }
          setState(1574);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 221, _ctx);
        }
        setState(1576);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LCURL) {
          {
            setState(1575);
            block();
          }
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

  public final ConstructorDelegationCallContext constructorDelegationCall()
      throws RecognitionException {
    ConstructorDelegationCallContext _localctx =
        new ConstructorDelegationCallContext(_ctx, getState());
    enterRule(_localctx, 90, RULE_constructorDelegationCall);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1578);
        _la = _input.LA(1);
        if (!(_la == THIS || _la == SUPER)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
        setState(1582);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1579);
              match(NL);
            }
          }
          setState(1584);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1585);
        valueArguments();
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

  public final EnumClassBodyContext enumClassBody() throws RecognitionException {
    EnumClassBodyContext _localctx = new EnumClassBodyContext(_ctx, getState());
    enterRule(_localctx, 92, RULE_enumClassBody);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1587);
        match(LCURL);
        setState(1591);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 224, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1588);
                match(NL);
              }
            }
          }
          setState(1593);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 224, _ctx);
        }
        setState(1595);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (((((_la - 40)) & ~0x3f) == 0
                && ((1L << (_la - 40))
                        & ((1L << (AT_NO_WS - 40))
                            | (1L << (AT_PRE_WS - 40))
                            | (1L << (FILE - 40))
                            | (1L << (FIELD - 40))
                            | (1L << (PROPERTY - 40))
                            | (1L << (GET - 40))
                            | (1L << (SET - 40))
                            | (1L << (RECEIVER - 40))
                            | (1L << (PARAM - 40))
                            | (1L << (SETPARAM - 40))
                            | (1L << (DELEGATE - 40))
                            | (1L << (IMPORT - 40))
                            | (1L << (CONSTRUCTOR - 40))
                            | (1L << (BY - 40))
                            | (1L << (COMPANION - 40))
                            | (1L << (INIT - 40))
                            | (1L << (WHERE - 40))
                            | (1L << (CATCH - 40))
                            | (1L << (FINALLY - 40))))
                    != 0)
            || ((((_la - 105)) & ~0x3f) == 0
                && ((1L << (_la - 105))
                        & ((1L << (OUT - 105))
                            | (1L << (DYNAMIC - 105))
                            | (1L << (PUBLIC - 105))
                            | (1L << (PRIVATE - 105))
                            | (1L << (PROTECTED - 105))
                            | (1L << (INTERNAL - 105))
                            | (1L << (ENUM - 105))
                            | (1L << (SEALED - 105))
                            | (1L << (ANNOTATION - 105))
                            | (1L << (DATA - 105))
                            | (1L << (INNER - 105))
                            | (1L << (VALUE - 105))
                            | (1L << (TAILREC - 105))
                            | (1L << (OPERATOR - 105))
                            | (1L << (INLINE - 105))
                            | (1L << (INFIX - 105))
                            | (1L << (EXTERNAL - 105))
                            | (1L << (SUSPEND - 105))
                            | (1L << (OVERRIDE - 105))
                            | (1L << (ABSTRACT - 105))
                            | (1L << (FINAL - 105))
                            | (1L << (OPEN - 105))
                            | (1L << (CONST - 105))
                            | (1L << (LATEINIT - 105))
                            | (1L << (VARARG - 105))
                            | (1L << (NOINLINE - 105))
                            | (1L << (CROSSINLINE - 105))
                            | (1L << (REIFIED - 105))
                            | (1L << (EXPECT - 105))
                            | (1L << (ACTUAL - 105))
                            | (1L << (Identifier - 105))))
                    != 0)) {
          {
            setState(1594);
            enumEntries();
          }
        }

        setState(1611);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 228, _ctx)) {
          case 1:
            {
              setState(1600);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1597);
                    match(NL);
                  }
                }
                setState(1602);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1603);
              match(SEMICOLON);
              setState(1607);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 227, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(1604);
                      match(NL);
                    }
                  }
                }
                setState(1609);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 227, _ctx);
              }
              setState(1610);
              classMemberDeclarations();
            }
            break;
        }
        setState(1616);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1613);
              match(NL);
            }
          }
          setState(1618);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1619);
        match(RCURL);
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

  public final EnumEntriesContext enumEntries() throws RecognitionException {
    EnumEntriesContext _localctx = new EnumEntriesContext(_ctx, getState());
    enterRule(_localctx, 94, RULE_enumEntries);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1621);
        enumEntry();
        setState(1638);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 232, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1625);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1622);
                      match(NL);
                    }
                  }
                  setState(1627);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1628);
                match(COMMA);
                setState(1632);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1629);
                      match(NL);
                    }
                  }
                  setState(1634);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1635);
                enumEntry();
              }
            }
          }
          setState(1640);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 232, _ctx);
        }
        setState(1644);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 233, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1641);
                match(NL);
              }
            }
          }
          setState(1646);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 233, _ctx);
        }
        setState(1648);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == COMMA) {
          {
            setState(1647);
            match(COMMA);
          }
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

  public final EnumEntryContext enumEntry() throws RecognitionException {
    EnumEntryContext _localctx = new EnumEntryContext(_ctx, getState());
    enterRule(_localctx, 96, RULE_enumEntry);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1657);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 236, _ctx)) {
          case 1:
            {
              setState(1650);
              modifiers();
              setState(1654);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1651);
                    match(NL);
                  }
                }
                setState(1656);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
        }
        setState(1659);
        simpleIdentifier();
        setState(1667);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 238, _ctx)) {
          case 1:
            {
              setState(1663);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1660);
                    match(NL);
                  }
                }
                setState(1665);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1666);
              valueArguments();
            }
            break;
        }
        setState(1676);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 240, _ctx)) {
          case 1:
            {
              setState(1672);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1669);
                    match(NL);
                  }
                }
                setState(1674);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1675);
              classBody();
            }
            break;
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

  public final TypeContext type() throws RecognitionException {
    TypeContext _localctx = new TypeContext(_ctx, getState());
    enterRule(_localctx, 98, RULE_type);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1679);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 241, _ctx)) {
          case 1:
            {
              setState(1678);
              typeModifiers();
            }
            break;
        }
        setState(1685);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 242, _ctx)) {
          case 1:
            {
              setState(1681);
              parenthesizedType();
            }
            break;
          case 2:
            {
              setState(1682);
              nullableType();
            }
            break;
          case 3:
            {
              setState(1683);
              typeReference();
            }
            break;
          case 4:
            {
              setState(1684);
              functionType();
            }
            break;
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

  public final TypeReferenceContext typeReference() throws RecognitionException {
    TypeReferenceContext _localctx = new TypeReferenceContext(_ctx, getState());
    enterRule(_localctx, 100, RULE_typeReference);
    try {
      setState(1689);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 243, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(1687);
            userType();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(1688);
            match(DYNAMIC);
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

  public final NullableTypeContext nullableType() throws RecognitionException {
    NullableTypeContext _localctx = new NullableTypeContext(_ctx, getState());
    enterRule(_localctx, 102, RULE_nullableType);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1693);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case WHERE:
          case CATCH:
          case FINALLY:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case Identifier:
            {
              setState(1691);
              typeReference();
            }
            break;
          case LPAREN:
            {
              setState(1692);
              parenthesizedType();
            }
            break;
          default:
            throw new NoViableAltException(this);
        }
        setState(1698);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1695);
              match(NL);
            }
          }
          setState(1700);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1702);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(1701);
                  quest();
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(1704);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 246, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final QuestContext quest() throws RecognitionException {
    QuestContext _localctx = new QuestContext(_ctx, getState());
    enterRule(_localctx, 104, RULE_quest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1706);
        _la = _input.LA(1);
        if (!(_la == QUEST_WS || _la == QUEST_NO_WS)) {
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

  public final UserTypeContext userType() throws RecognitionException {
    UserTypeContext _localctx = new UserTypeContext(_ctx, getState());
    enterRule(_localctx, 106, RULE_userType);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1708);
        simpleUserType();
        setState(1725);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 249, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1712);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1709);
                      match(NL);
                    }
                  }
                  setState(1714);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1715);
                match(DOT);
                setState(1719);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1716);
                      match(NL);
                    }
                  }
                  setState(1721);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1722);
                simpleUserType();
              }
            }
          }
          setState(1727);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 249, _ctx);
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

  public final SimpleUserTypeContext simpleUserType() throws RecognitionException {
    SimpleUserTypeContext _localctx = new SimpleUserTypeContext(_ctx, getState());
    enterRule(_localctx, 108, RULE_simpleUserType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1728);
        simpleIdentifier();
        setState(1736);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 251, _ctx)) {
          case 1:
            {
              setState(1732);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1729);
                    match(NL);
                  }
                }
                setState(1734);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1735);
              typeArguments();
            }
            break;
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

  public final TypeProjectionContext typeProjection() throws RecognitionException {
    TypeProjectionContext _localctx = new TypeProjectionContext(_ctx, getState());
    enterRule(_localctx, 110, RULE_typeProjection);
    try {
      setState(1743);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LPAREN:
        case AT_NO_WS:
        case AT_PRE_WS:
        case FILE:
        case FIELD:
        case PROPERTY:
        case GET:
        case SET:
        case RECEIVER:
        case PARAM:
        case SETPARAM:
        case DELEGATE:
        case IMPORT:
        case CONSTRUCTOR:
        case BY:
        case COMPANION:
        case INIT:
        case WHERE:
        case CATCH:
        case FINALLY:
        case IN:
        case OUT:
        case DYNAMIC:
        case PUBLIC:
        case PRIVATE:
        case PROTECTED:
        case INTERNAL:
        case ENUM:
        case SEALED:
        case ANNOTATION:
        case DATA:
        case INNER:
        case VALUE:
        case TAILREC:
        case OPERATOR:
        case INLINE:
        case INFIX:
        case EXTERNAL:
        case SUSPEND:
        case OVERRIDE:
        case ABSTRACT:
        case FINAL:
        case OPEN:
        case CONST:
        case LATEINIT:
        case VARARG:
        case NOINLINE:
        case CROSSINLINE:
        case REIFIED:
        case EXPECT:
        case ACTUAL:
        case Identifier:
          enterOuterAlt(_localctx, 1);
          {
            setState(1739);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 252, _ctx)) {
              case 1:
                {
                  setState(1738);
                  typeProjectionModifiers();
                }
                break;
            }
            setState(1741);
            type();
          }
          break;
        case MULT:
          enterOuterAlt(_localctx, 2);
          {
            setState(1742);
            match(MULT);
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final TypeProjectionModifiersContext typeProjectionModifiers()
      throws RecognitionException {
    TypeProjectionModifiersContext _localctx = new TypeProjectionModifiersContext(_ctx, getState());
    enterRule(_localctx, 112, RULE_typeProjectionModifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1746);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(1745);
                  typeProjectionModifier();
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(1748);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 254, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final TypeProjectionModifierContext typeProjectionModifier() throws RecognitionException {
    TypeProjectionModifierContext _localctx = new TypeProjectionModifierContext(_ctx, getState());
    enterRule(_localctx, 114, RULE_typeProjectionModifier);
    int _la;
    try {
      setState(1758);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case IN:
        case OUT:
          enterOuterAlt(_localctx, 1);
          {
            setState(1750);
            varianceModifier();
            setState(1754);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(1751);
                  match(NL);
                }
              }
              setState(1756);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
          break;
        case AT_NO_WS:
        case AT_PRE_WS:
          enterOuterAlt(_localctx, 2);
          {
            setState(1757);
            annotation();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final FunctionTypeContext functionType() throws RecognitionException {
    FunctionTypeContext _localctx = new FunctionTypeContext(_ctx, getState());
    enterRule(_localctx, 116, RULE_functionType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1774);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 259, _ctx)) {
          case 1:
            {
              setState(1760);
              receiverType();
              setState(1764);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1761);
                    match(NL);
                  }
                }
                setState(1766);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1767);
              match(DOT);
              setState(1771);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1768);
                    match(NL);
                  }
                }
                setState(1773);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
        }
        setState(1776);
        functionTypeParameters();
        setState(1780);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1777);
              match(NL);
            }
          }
          setState(1782);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1783);
        match(ARROW);
        setState(1787);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1784);
              match(NL);
            }
          }
          setState(1789);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1790);
        type();
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

  public final FunctionTypeParametersContext functionTypeParameters() throws RecognitionException {
    FunctionTypeParametersContext _localctx = new FunctionTypeParametersContext(_ctx, getState());
    enterRule(_localctx, 118, RULE_functionTypeParameters);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1792);
        match(LPAREN);
        setState(1796);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 262, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1793);
                match(NL);
              }
            }
          }
          setState(1798);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 262, _ctx);
        }
        setState(1801);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 263, _ctx)) {
          case 1:
            {
              setState(1799);
              parameter();
            }
            break;
          case 2:
            {
              setState(1800);
              type();
            }
            break;
        }
        setState(1822);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 267, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1806);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1803);
                      match(NL);
                    }
                  }
                  setState(1808);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1809);
                match(COMMA);
                setState(1813);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(1810);
                      match(NL);
                    }
                  }
                  setState(1815);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(1818);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 266, _ctx)) {
                  case 1:
                    {
                      setState(1816);
                      parameter();
                    }
                    break;
                  case 2:
                    {
                      setState(1817);
                      type();
                    }
                    break;
                }
              }
            }
          }
          setState(1824);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 267, _ctx);
        }
        setState(1832);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 269, _ctx)) {
          case 1:
            {
              setState(1828);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(1825);
                    match(NL);
                  }
                }
                setState(1830);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(1831);
              match(COMMA);
            }
            break;
        }
        setState(1837);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1834);
              match(NL);
            }
          }
          setState(1839);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1840);
        match(RPAREN);
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

  public final ParenthesizedTypeContext parenthesizedType() throws RecognitionException {
    ParenthesizedTypeContext _localctx = new ParenthesizedTypeContext(_ctx, getState());
    enterRule(_localctx, 120, RULE_parenthesizedType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1842);
        match(LPAREN);
        setState(1846);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1843);
              match(NL);
            }
          }
          setState(1848);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1849);
        type();
        setState(1853);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1850);
              match(NL);
            }
          }
          setState(1855);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1856);
        match(RPAREN);
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

  public final ReceiverTypeContext receiverType() throws RecognitionException {
    ReceiverTypeContext _localctx = new ReceiverTypeContext(_ctx, getState());
    enterRule(_localctx, 122, RULE_receiverType);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1859);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 273, _ctx)) {
          case 1:
            {
              setState(1858);
              typeModifiers();
            }
            break;
        }
        setState(1864);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 274, _ctx)) {
          case 1:
            {
              setState(1861);
              parenthesizedType();
            }
            break;
          case 2:
            {
              setState(1862);
              nullableType();
            }
            break;
          case 3:
            {
              setState(1863);
              typeReference();
            }
            break;
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

  public final ParenthesizedUserTypeContext parenthesizedUserType() throws RecognitionException {
    ParenthesizedUserTypeContext _localctx = new ParenthesizedUserTypeContext(_ctx, getState());
    enterRule(_localctx, 124, RULE_parenthesizedUserType);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1866);
        match(LPAREN);
        setState(1870);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1867);
              match(NL);
            }
          }
          setState(1872);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1875);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case WHERE:
          case CATCH:
          case FINALLY:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case Identifier:
            {
              setState(1873);
              userType();
            }
            break;
          case LPAREN:
            {
              setState(1874);
              parenthesizedUserType();
            }
            break;
          default:
            throw new NoViableAltException(this);
        }
        setState(1880);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1877);
              match(NL);
            }
          }
          setState(1882);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1883);
        match(RPAREN);
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

  public final StatementsContext statements() throws RecognitionException {
    StatementsContext _localctx = new StatementsContext(_ctx, getState());
    enterRule(_localctx, 126, RULE_statements);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1894);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << LPAREN)
                            | (1L << LSQUARE)
                            | (1L << LCURL)
                            | (1L << ADD)
                            | (1L << SUB)
                            | (1L << INCR)
                            | (1L << DECR)
                            | (1L << EXCL_WS)
                            | (1L << EXCL_NO_WS)
                            | (1L << COLONCOLON)
                            | (1L << AT_NO_WS)
                            | (1L << AT_PRE_WS)
                            | (1L << RETURN_AT)
                            | (1L << CONTINUE_AT)
                            | (1L << BREAK_AT)
                            | (1L << THIS_AT)
                            | (1L << SUPER_AT)
                            | (1L << FILE)
                            | (1L << FIELD)
                            | (1L << PROPERTY)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (GET - 64))
                            | (1L << (SET - 64))
                            | (1L << (RECEIVER - 64))
                            | (1L << (PARAM - 64))
                            | (1L << (SETPARAM - 64))
                            | (1L << (DELEGATE - 64))
                            | (1L << (IMPORT - 64))
                            | (1L << (CLASS - 64))
                            | (1L << (INTERFACE - 64))
                            | (1L << (FUN - 64))
                            | (1L << (OBJECT - 64))
                            | (1L << (VAL - 64))
                            | (1L << (VAR - 64))
                            | (1L << (TYPE_ALIAS - 64))
                            | (1L << (CONSTRUCTOR - 64))
                            | (1L << (BY - 64))
                            | (1L << (COMPANION - 64))
                            | (1L << (INIT - 64))
                            | (1L << (THIS - 64))
                            | (1L << (SUPER - 64))
                            | (1L << (WHERE - 64))
                            | (1L << (IF - 64))
                            | (1L << (WHEN - 64))
                            | (1L << (TRY - 64))
                            | (1L << (CATCH - 64))
                            | (1L << (FINALLY - 64))
                            | (1L << (FOR - 64))
                            | (1L << (DO - 64))
                            | (1L << (WHILE - 64))
                            | (1L << (THROW - 64))
                            | (1L << (RETURN - 64))
                            | (1L << (CONTINUE - 64))
                            | (1L << (BREAK - 64))
                            | (1L << (OUT - 64))
                            | (1L << (DYNAMIC - 64))
                            | (1L << (PUBLIC - 64))
                            | (1L << (PRIVATE - 64))
                            | (1L << (PROTECTED - 64))
                            | (1L << (INTERNAL - 64))
                            | (1L << (ENUM - 64))
                            | (1L << (SEALED - 64))
                            | (1L << (ANNOTATION - 64))
                            | (1L << (DATA - 64))
                            | (1L << (INNER - 64))
                            | (1L << (VALUE - 64))
                            | (1L << (TAILREC - 64))
                            | (1L << (OPERATOR - 64))
                            | (1L << (INLINE - 64))
                            | (1L << (INFIX - 64))
                            | (1L << (EXTERNAL - 64))
                            | (1L << (SUSPEND - 64))
                            | (1L << (OVERRIDE - 64))
                            | (1L << (ABSTRACT - 64))
                            | (1L << (FINAL - 64))
                            | (1L << (OPEN - 64))
                            | (1L << (CONST - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (LATEINIT - 128))
                            | (1L << (VARARG - 128))
                            | (1L << (NOINLINE - 128))
                            | (1L << (CROSSINLINE - 128))
                            | (1L << (REIFIED - 128))
                            | (1L << (EXPECT - 128))
                            | (1L << (ACTUAL - 128))
                            | (1L << (RealLiteral - 128))
                            | (1L << (IntegerLiteral - 128))
                            | (1L << (HexLiteral - 128))
                            | (1L << (BinLiteral - 128))
                            | (1L << (UnsignedLiteral - 128))
                            | (1L << (LongLiteral - 128))
                            | (1L << (BooleanLiteral - 128))
                            | (1L << (NullLiteral - 128))
                            | (1L << (CharacterLiteral - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (QUOTE_OPEN - 128))
                            | (1L << (TRIPLE_QUOTE_OPEN - 128))))
                    != 0)) {
          {
            setState(1885);
            statement();
            setState(1891);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 278, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(1886);
                    semis();
                    setState(1887);
                    statement();
                  }
                }
              }
              setState(1893);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 278, _ctx);
            }
          }
        }

        setState(1897);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 280, _ctx)) {
          case 1:
            {
              setState(1896);
              semis();
            }
            break;
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

  public final StatementContext statement() throws RecognitionException {
    StatementContext _localctx = new StatementContext(_ctx, getState());
    enterRule(_localctx, 128, RULE_statement);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1903);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 282, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              setState(1901);
              _errHandler.sync(this);
              switch (_input.LA(1)) {
                case FILE:
                case FIELD:
                case PROPERTY:
                case GET:
                case SET:
                case RECEIVER:
                case PARAM:
                case SETPARAM:
                case DELEGATE:
                case IMPORT:
                case CONSTRUCTOR:
                case BY:
                case COMPANION:
                case INIT:
                case WHERE:
                case CATCH:
                case FINALLY:
                case OUT:
                case DYNAMIC:
                case PUBLIC:
                case PRIVATE:
                case PROTECTED:
                case INTERNAL:
                case ENUM:
                case SEALED:
                case ANNOTATION:
                case DATA:
                case INNER:
                case VALUE:
                case TAILREC:
                case OPERATOR:
                case INLINE:
                case INFIX:
                case EXTERNAL:
                case SUSPEND:
                case OVERRIDE:
                case ABSTRACT:
                case FINAL:
                case OPEN:
                case CONST:
                case LATEINIT:
                case VARARG:
                case NOINLINE:
                case CROSSINLINE:
                case REIFIED:
                case EXPECT:
                case ACTUAL:
                case Identifier:
                  {
                    setState(1899);
                    label();
                  }
                  break;
                case AT_NO_WS:
                case AT_PRE_WS:
                  {
                    setState(1900);
                    annotation();
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
            }
          }
          setState(1905);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 282, _ctx);
        }
        setState(1910);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 283, _ctx)) {
          case 1:
            {
              setState(1906);
              declaration();
            }
            break;
          case 2:
            {
              setState(1907);
              assignment();
            }
            break;
          case 3:
            {
              setState(1908);
              loopStatement();
            }
            break;
          case 4:
            {
              setState(1909);
              expression();
            }
            break;
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

  public final LabelContext label() throws RecognitionException {
    LabelContext _localctx = new LabelContext(_ctx, getState());
    enterRule(_localctx, 130, RULE_label);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1912);
        simpleIdentifier();
        setState(1913);
        _la = _input.LA(1);
        if (!(_la == AT_NO_WS || _la == AT_POST_WS)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
        setState(1917);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 284, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1914);
                match(NL);
              }
            }
          }
          setState(1919);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 284, _ctx);
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

  public final ControlStructureBodyContext controlStructureBody() throws RecognitionException {
    ControlStructureBodyContext _localctx = new ControlStructureBodyContext(_ctx, getState());
    enterRule(_localctx, 132, RULE_controlStructureBody);
    try {
      setState(1922);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 285, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(1920);
            block();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(1921);
            statement();
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

  public final BlockContext block() throws RecognitionException {
    BlockContext _localctx = new BlockContext(_ctx, getState());
    enterRule(_localctx, 134, RULE_block);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1924);
        match(LCURL);
        setState(1928);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 286, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1925);
                match(NL);
              }
            }
          }
          setState(1930);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 286, _ctx);
        }
        setState(1931);
        statements();
        setState(1935);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1932);
              match(NL);
            }
          }
          setState(1937);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1938);
        match(RCURL);
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

  public final LoopStatementContext loopStatement() throws RecognitionException {
    LoopStatementContext _localctx = new LoopStatementContext(_ctx, getState());
    enterRule(_localctx, 136, RULE_loopStatement);
    try {
      setState(1943);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case FOR:
          enterOuterAlt(_localctx, 1);
          {
            setState(1940);
            forStatement();
          }
          break;
        case WHILE:
          enterOuterAlt(_localctx, 2);
          {
            setState(1941);
            whileStatement();
          }
          break;
        case DO:
          enterOuterAlt(_localctx, 3);
          {
            setState(1942);
            doWhileStatement();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final ForStatementContext forStatement() throws RecognitionException {
    ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
    enterRule(_localctx, 138, RULE_forStatement);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1945);
        match(FOR);
        setState(1949);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1946);
              match(NL);
            }
          }
          setState(1951);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1952);
        match(LPAREN);
        setState(1956);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 290, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1953);
                annotation();
              }
            }
          }
          setState(1958);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 290, _ctx);
        }
        setState(1961);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case NL:
          case AT_NO_WS:
          case AT_PRE_WS:
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case WHERE:
          case CATCH:
          case FINALLY:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case Identifier:
            {
              setState(1959);
              variableDeclaration();
            }
            break;
          case LPAREN:
            {
              setState(1960);
              multiVariableDeclaration();
            }
            break;
          default:
            throw new NoViableAltException(this);
        }
        setState(1963);
        match(IN);
        setState(1964);
        expression();
        setState(1965);
        match(RPAREN);
        setState(1969);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 292, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1966);
                match(NL);
              }
            }
          }
          setState(1971);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 292, _ctx);
        }
        setState(1973);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 293, _ctx)) {
          case 1:
            {
              setState(1972);
              controlStructureBody();
            }
            break;
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

  public final WhileStatementContext whileStatement() throws RecognitionException {
    WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
    enterRule(_localctx, 140, RULE_whileStatement);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1975);
        match(WHILE);
        setState(1979);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1976);
              match(NL);
            }
          }
          setState(1981);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1982);
        match(LPAREN);
        setState(1983);
        expression();
        setState(1984);
        match(RPAREN);
        setState(1988);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(1985);
              match(NL);
            }
          }
          setState(1990);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(1993);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case LPAREN:
          case LSQUARE:
          case LCURL:
          case ADD:
          case SUB:
          case INCR:
          case DECR:
          case EXCL_WS:
          case EXCL_NO_WS:
          case COLONCOLON:
          case AT_NO_WS:
          case AT_PRE_WS:
          case RETURN_AT:
          case CONTINUE_AT:
          case BREAK_AT:
          case THIS_AT:
          case SUPER_AT:
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CLASS:
          case INTERFACE:
          case FUN:
          case OBJECT:
          case VAL:
          case VAR:
          case TYPE_ALIAS:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case THIS:
          case SUPER:
          case WHERE:
          case IF:
          case WHEN:
          case TRY:
          case CATCH:
          case FINALLY:
          case FOR:
          case DO:
          case WHILE:
          case THROW:
          case RETURN:
          case CONTINUE:
          case BREAK:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case RealLiteral:
          case IntegerLiteral:
          case HexLiteral:
          case BinLiteral:
          case UnsignedLiteral:
          case LongLiteral:
          case BooleanLiteral:
          case NullLiteral:
          case CharacterLiteral:
          case Identifier:
          case QUOTE_OPEN:
          case TRIPLE_QUOTE_OPEN:
            {
              setState(1991);
              controlStructureBody();
            }
            break;
          case SEMICOLON:
            {
              setState(1992);
              match(SEMICOLON);
            }
            break;
          default:
            throw new NoViableAltException(this);
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

  public final DoWhileStatementContext doWhileStatement() throws RecognitionException {
    DoWhileStatementContext _localctx = new DoWhileStatementContext(_ctx, getState());
    enterRule(_localctx, 142, RULE_doWhileStatement);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1995);
        match(DO);
        setState(1999);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 297, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1996);
                match(NL);
              }
            }
          }
          setState(2001);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 297, _ctx);
        }
        setState(2003);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 298, _ctx)) {
          case 1:
            {
              setState(2002);
              controlStructureBody();
            }
            break;
        }
        setState(2008);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2005);
              match(NL);
            }
          }
          setState(2010);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2011);
        match(WHILE);
        setState(2015);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2012);
              match(NL);
            }
          }
          setState(2017);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2018);
        match(LPAREN);
        setState(2019);
        expression();
        setState(2020);
        match(RPAREN);
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

  public final AssignmentContext assignment() throws RecognitionException {
    AssignmentContext _localctx = new AssignmentContext(_ctx, getState());
    enterRule(_localctx, 144, RULE_assignment);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2028);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 301, _ctx)) {
          case 1:
            {
              setState(2022);
              directlyAssignableExpression();
              setState(2023);
              match(ASSIGNMENT);
            }
            break;
          case 2:
            {
              setState(2025);
              assignableExpression();
              setState(2026);
              assignmentAndOperator();
            }
            break;
        }
        setState(2033);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2030);
              match(NL);
            }
          }
          setState(2035);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2036);
        expression();
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

  public final SemiContext semi() throws RecognitionException {
    SemiContext _localctx = new SemiContext(_ctx, getState());
    enterRule(_localctx, 146, RULE_semi);
    int _la;
    try {
      int _alt;
      setState(2046);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case NL:
        case SEMICOLON:
          enterOuterAlt(_localctx, 1);
          {
            setState(2038);
            _la = _input.LA(1);
            if (!(_la == NL || _la == SEMICOLON)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) matchedEOF = true;
              _errHandler.reportMatch(this);
              consume();
            }
            setState(2042);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 303, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(2039);
                    match(NL);
                  }
                }
              }
              setState(2044);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 303, _ctx);
            }
          }
          break;
        case EOF:
          enterOuterAlt(_localctx, 2);
          {
            setState(2045);
            match(EOF);
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final SemisContext semis() throws RecognitionException {
    SemisContext _localctx = new SemisContext(_ctx, getState());
    enterRule(_localctx, 148, RULE_semis);
    int _la;
    try {
      int _alt;
      setState(2054);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case NL:
        case SEMICOLON:
          enterOuterAlt(_localctx, 1);
          {
            setState(2049);
            _errHandler.sync(this);
            _alt = 1;
            do {
              switch (_alt) {
                case 1:
                  {
                    {
                      setState(2048);
                      _la = _input.LA(1);
                      if (!(_la == NL || _la == SEMICOLON)) {
                        _errHandler.recoverInline(this);
                      } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                      }
                    }
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
              setState(2051);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 305, _ctx);
            } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
          }
          break;
        case EOF:
          enterOuterAlt(_localctx, 2);
          {
            setState(2053);
            match(EOF);
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final ExpressionContext expression() throws RecognitionException {
    ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
    enterRule(_localctx, 150, RULE_expression);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2056);
        disjunction();
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

  public final DisjunctionContext disjunction() throws RecognitionException {
    DisjunctionContext _localctx = new DisjunctionContext(_ctx, getState());
    enterRule(_localctx, 152, RULE_disjunction);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2058);
        conjunction();
        setState(2075);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 309, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2062);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2059);
                      match(NL);
                    }
                  }
                  setState(2064);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2065);
                match(DISJ);
                setState(2069);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2066);
                      match(NL);
                    }
                  }
                  setState(2071);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2072);
                conjunction();
              }
            }
          }
          setState(2077);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 309, _ctx);
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

  public final ConjunctionContext conjunction() throws RecognitionException {
    ConjunctionContext _localctx = new ConjunctionContext(_ctx, getState());
    enterRule(_localctx, 154, RULE_conjunction);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2078);
        equality();
        setState(2095);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 312, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2082);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2079);
                      match(NL);
                    }
                  }
                  setState(2084);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2085);
                match(CONJ);
                setState(2089);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2086);
                      match(NL);
                    }
                  }
                  setState(2091);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2092);
                equality();
              }
            }
          }
          setState(2097);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 312, _ctx);
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

  public final EqualityContext equality() throws RecognitionException {
    EqualityContext _localctx = new EqualityContext(_ctx, getState());
    enterRule(_localctx, 156, RULE_equality);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2098);
        comparison();
        setState(2110);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 314, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2099);
                equalityOperator();
                setState(2103);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2100);
                      match(NL);
                    }
                  }
                  setState(2105);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2106);
                comparison();
              }
            }
          }
          setState(2112);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 314, _ctx);
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

  public final ComparisonContext comparison() throws RecognitionException {
    ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
    enterRule(_localctx, 158, RULE_comparison);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2113);
        genericCallLikeComparison();
        setState(2125);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 316, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2114);
                comparisonOperator();
                setState(2118);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2115);
                      match(NL);
                    }
                  }
                  setState(2120);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2121);
                genericCallLikeComparison();
              }
            }
          }
          setState(2127);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 316, _ctx);
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

  public final GenericCallLikeComparisonContext genericCallLikeComparison()
      throws RecognitionException {
    GenericCallLikeComparisonContext _localctx =
        new GenericCallLikeComparisonContext(_ctx, getState());
    enterRule(_localctx, 160, RULE_genericCallLikeComparison);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2128);
        infixOperation();
        setState(2132);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 317, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2129);
                callSuffix();
              }
            }
          }
          setState(2134);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 317, _ctx);
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

  public final InfixOperationContext infixOperation() throws RecognitionException {
    InfixOperationContext _localctx = new InfixOperationContext(_ctx, getState());
    enterRule(_localctx, 162, RULE_infixOperation);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2135);
        elvisExpression();
        setState(2156);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 321, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              setState(2154);
              _errHandler.sync(this);
              switch (_input.LA(1)) {
                case IN:
                case NOT_IN:
                  {
                    setState(2136);
                    inOperator();
                    setState(2140);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(2137);
                          match(NL);
                        }
                      }
                      setState(2142);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(2143);
                    elvisExpression();
                  }
                  break;
                case IS:
                case NOT_IS:
                  {
                    setState(2145);
                    isOperator();
                    setState(2149);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(2146);
                          match(NL);
                        }
                      }
                      setState(2151);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(2152);
                    type();
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
            }
          }
          setState(2158);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 321, _ctx);
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

  public final ElvisExpressionContext elvisExpression() throws RecognitionException {
    ElvisExpressionContext _localctx = new ElvisExpressionContext(_ctx, getState());
    enterRule(_localctx, 164, RULE_elvisExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2159);
        infixFunctionCall();
        setState(2177);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 324, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2163);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2160);
                      match(NL);
                    }
                  }
                  setState(2165);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2166);
                elvis();
                setState(2170);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2167);
                      match(NL);
                    }
                  }
                  setState(2172);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2173);
                infixFunctionCall();
              }
            }
          }
          setState(2179);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 324, _ctx);
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

  public final ElvisContext elvis() throws RecognitionException {
    ElvisContext _localctx = new ElvisContext(_ctx, getState());
    enterRule(_localctx, 166, RULE_elvis);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2180);
        match(QUEST_NO_WS);
        setState(2181);
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

  public final InfixFunctionCallContext infixFunctionCall() throws RecognitionException {
    InfixFunctionCallContext _localctx = new InfixFunctionCallContext(_ctx, getState());
    enterRule(_localctx, 168, RULE_infixFunctionCall);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2183);
        rangeExpression();
        setState(2195);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 326, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2184);
                simpleIdentifier();
                setState(2188);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2185);
                      match(NL);
                    }
                  }
                  setState(2190);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2191);
                rangeExpression();
              }
            }
          }
          setState(2197);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 326, _ctx);
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

  public final RangeExpressionContext rangeExpression() throws RecognitionException {
    RangeExpressionContext _localctx = new RangeExpressionContext(_ctx, getState());
    enterRule(_localctx, 170, RULE_rangeExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2198);
        additiveExpression();
        setState(2209);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 328, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2199);
                match(RANGE);
                setState(2203);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2200);
                      match(NL);
                    }
                  }
                  setState(2205);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2206);
                additiveExpression();
              }
            }
          }
          setState(2211);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 328, _ctx);
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

  public final AdditiveExpressionContext additiveExpression() throws RecognitionException {
    AdditiveExpressionContext _localctx = new AdditiveExpressionContext(_ctx, getState());
    enterRule(_localctx, 172, RULE_additiveExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2212);
        multiplicativeExpression();
        setState(2224);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 330, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2213);
                additiveOperator();
                setState(2217);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2214);
                      match(NL);
                    }
                  }
                  setState(2219);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2220);
                multiplicativeExpression();
              }
            }
          }
          setState(2226);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 330, _ctx);
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

  public final MultiplicativeExpressionContext multiplicativeExpression()
      throws RecognitionException {
    MultiplicativeExpressionContext _localctx =
        new MultiplicativeExpressionContext(_ctx, getState());
    enterRule(_localctx, 174, RULE_multiplicativeExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2227);
        asExpression();
        setState(2239);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 332, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2228);
                multiplicativeOperator();
                setState(2232);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2229);
                      match(NL);
                    }
                  }
                  setState(2234);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2235);
                asExpression();
              }
            }
          }
          setState(2241);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 332, _ctx);
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

  public final AsExpressionContext asExpression() throws RecognitionException {
    AsExpressionContext _localctx = new AsExpressionContext(_ctx, getState());
    enterRule(_localctx, 176, RULE_asExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2242);
        prefixUnaryExpression();
        setState(2260);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 335, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2246);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2243);
                      match(NL);
                    }
                  }
                  setState(2248);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2249);
                asOperator();
                setState(2253);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2250);
                      match(NL);
                    }
                  }
                  setState(2255);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2256);
                type();
              }
            }
          }
          setState(2262);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 335, _ctx);
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

  public final PrefixUnaryExpressionContext prefixUnaryExpression() throws RecognitionException {
    PrefixUnaryExpressionContext _localctx = new PrefixUnaryExpressionContext(_ctx, getState());
    enterRule(_localctx, 178, RULE_prefixUnaryExpression);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2266);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 336, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2263);
                unaryPrefix();
              }
            }
          }
          setState(2268);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 336, _ctx);
        }
        setState(2269);
        postfixUnaryExpression();
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

  public final UnaryPrefixContext unaryPrefix() throws RecognitionException {
    UnaryPrefixContext _localctx = new UnaryPrefixContext(_ctx, getState());
    enterRule(_localctx, 180, RULE_unaryPrefix);
    int _la;
    try {
      setState(2280);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case AT_NO_WS:
        case AT_PRE_WS:
          enterOuterAlt(_localctx, 1);
          {
            setState(2271);
            annotation();
          }
          break;
        case FILE:
        case FIELD:
        case PROPERTY:
        case GET:
        case SET:
        case RECEIVER:
        case PARAM:
        case SETPARAM:
        case DELEGATE:
        case IMPORT:
        case CONSTRUCTOR:
        case BY:
        case COMPANION:
        case INIT:
        case WHERE:
        case CATCH:
        case FINALLY:
        case OUT:
        case DYNAMIC:
        case PUBLIC:
        case PRIVATE:
        case PROTECTED:
        case INTERNAL:
        case ENUM:
        case SEALED:
        case ANNOTATION:
        case DATA:
        case INNER:
        case VALUE:
        case TAILREC:
        case OPERATOR:
        case INLINE:
        case INFIX:
        case EXTERNAL:
        case SUSPEND:
        case OVERRIDE:
        case ABSTRACT:
        case FINAL:
        case OPEN:
        case CONST:
        case LATEINIT:
        case VARARG:
        case NOINLINE:
        case CROSSINLINE:
        case REIFIED:
        case EXPECT:
        case ACTUAL:
        case Identifier:
          enterOuterAlt(_localctx, 2);
          {
            setState(2272);
            label();
          }
          break;
        case ADD:
        case SUB:
        case INCR:
        case DECR:
        case EXCL_WS:
        case EXCL_NO_WS:
          enterOuterAlt(_localctx, 3);
          {
            setState(2273);
            prefixUnaryOperator();
            setState(2277);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(2274);
                  match(NL);
                }
              }
              setState(2279);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final PostfixUnaryExpressionContext postfixUnaryExpression() throws RecognitionException {
    PostfixUnaryExpressionContext _localctx = new PostfixUnaryExpressionContext(_ctx, getState());
    enterRule(_localctx, 182, RULE_postfixUnaryExpression);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2282);
        primaryExpression();
        setState(2286);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 339, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2283);
                postfixUnarySuffix();
              }
            }
          }
          setState(2288);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 339, _ctx);
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

  public final PostfixUnarySuffixContext postfixUnarySuffix() throws RecognitionException {
    PostfixUnarySuffixContext _localctx = new PostfixUnarySuffixContext(_ctx, getState());
    enterRule(_localctx, 184, RULE_postfixUnarySuffix);
    try {
      setState(2294);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 340, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(2289);
            postfixUnaryOperator();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(2290);
            typeArguments();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(2291);
            callSuffix();
          }
          break;
        case 4:
          enterOuterAlt(_localctx, 4);
          {
            setState(2292);
            indexingSuffix();
          }
          break;
        case 5:
          enterOuterAlt(_localctx, 5);
          {
            setState(2293);
            navigationSuffix();
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

  public final DirectlyAssignableExpressionContext directlyAssignableExpression()
      throws RecognitionException {
    DirectlyAssignableExpressionContext _localctx =
        new DirectlyAssignableExpressionContext(_ctx, getState());
    enterRule(_localctx, 186, RULE_directlyAssignableExpression);
    try {
      setState(2301);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 341, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(2296);
            postfixUnaryExpression();
            setState(2297);
            assignableSuffix();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(2299);
            simpleIdentifier();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(2300);
            parenthesizedDirectlyAssignableExpression();
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

  public final ParenthesizedDirectlyAssignableExpressionContext
      parenthesizedDirectlyAssignableExpression() throws RecognitionException {
    ParenthesizedDirectlyAssignableExpressionContext _localctx =
        new ParenthesizedDirectlyAssignableExpressionContext(_ctx, getState());
    enterRule(_localctx, 188, RULE_parenthesizedDirectlyAssignableExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2303);
        match(LPAREN);
        setState(2307);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2304);
              match(NL);
            }
          }
          setState(2309);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2310);
        directlyAssignableExpression();
        setState(2314);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2311);
              match(NL);
            }
          }
          setState(2316);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2317);
        match(RPAREN);
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

  public final AssignableExpressionContext assignableExpression() throws RecognitionException {
    AssignableExpressionContext _localctx = new AssignableExpressionContext(_ctx, getState());
    enterRule(_localctx, 190, RULE_assignableExpression);
    try {
      setState(2321);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 344, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(2319);
            prefixUnaryExpression();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(2320);
            parenthesizedAssignableExpression();
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

  public final ParenthesizedAssignableExpressionContext parenthesizedAssignableExpression()
      throws RecognitionException {
    ParenthesizedAssignableExpressionContext _localctx =
        new ParenthesizedAssignableExpressionContext(_ctx, getState());
    enterRule(_localctx, 192, RULE_parenthesizedAssignableExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2323);
        match(LPAREN);
        setState(2327);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2324);
              match(NL);
            }
          }
          setState(2329);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2330);
        assignableExpression();
        setState(2334);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2331);
              match(NL);
            }
          }
          setState(2336);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2337);
        match(RPAREN);
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

  public final AssignableSuffixContext assignableSuffix() throws RecognitionException {
    AssignableSuffixContext _localctx = new AssignableSuffixContext(_ctx, getState());
    enterRule(_localctx, 194, RULE_assignableSuffix);
    try {
      setState(2342);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LANGLE:
          enterOuterAlt(_localctx, 1);
          {
            setState(2339);
            typeArguments();
          }
          break;
        case LSQUARE:
          enterOuterAlt(_localctx, 2);
          {
            setState(2340);
            indexingSuffix();
          }
          break;
        case NL:
        case DOT:
        case COLONCOLON:
        case QUEST_NO_WS:
          enterOuterAlt(_localctx, 3);
          {
            setState(2341);
            navigationSuffix();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final IndexingSuffixContext indexingSuffix() throws RecognitionException {
    IndexingSuffixContext _localctx = new IndexingSuffixContext(_ctx, getState());
    enterRule(_localctx, 196, RULE_indexingSuffix);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2344);
        match(LSQUARE);
        setState(2348);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2345);
              match(NL);
            }
          }
          setState(2350);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2351);
        expression();
        setState(2368);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 351, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2355);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2352);
                      match(NL);
                    }
                  }
                  setState(2357);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2358);
                match(COMMA);
                setState(2362);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2359);
                      match(NL);
                    }
                  }
                  setState(2364);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2365);
                expression();
              }
            }
          }
          setState(2370);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 351, _ctx);
        }
        setState(2378);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 353, _ctx)) {
          case 1:
            {
              setState(2374);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2371);
                    match(NL);
                  }
                }
                setState(2376);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2377);
              match(COMMA);
            }
            break;
        }
        setState(2383);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2380);
              match(NL);
            }
          }
          setState(2385);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2386);
        match(RSQUARE);
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

  public final NavigationSuffixContext navigationSuffix() throws RecognitionException {
    NavigationSuffixContext _localctx = new NavigationSuffixContext(_ctx, getState());
    enterRule(_localctx, 198, RULE_navigationSuffix);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2388);
        memberAccessOperator();
        setState(2392);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2389);
              match(NL);
            }
          }
          setState(2394);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2398);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case WHERE:
          case CATCH:
          case FINALLY:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case Identifier:
            {
              setState(2395);
              simpleIdentifier();
            }
            break;
          case LPAREN:
            {
              setState(2396);
              parenthesizedExpression();
            }
            break;
          case CLASS:
            {
              setState(2397);
              match(CLASS);
            }
            break;
          default:
            throw new NoViableAltException(this);
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

  public final CallSuffixContext callSuffix() throws RecognitionException {
    CallSuffixContext _localctx = new CallSuffixContext(_ctx, getState());
    enterRule(_localctx, 200, RULE_callSuffix);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2401);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LANGLE) {
          {
            setState(2400);
            typeArguments();
          }
        }

        setState(2408);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 359, _ctx)) {
          case 1:
            {
              setState(2404);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == LPAREN) {
                {
                  setState(2403);
                  valueArguments();
                }
              }

              setState(2406);
              annotatedLambda();
            }
            break;
          case 2:
            {
              setState(2407);
              valueArguments();
            }
            break;
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

  public final AnnotatedLambdaContext annotatedLambda() throws RecognitionException {
    AnnotatedLambdaContext _localctx = new AnnotatedLambdaContext(_ctx, getState());
    enterRule(_localctx, 202, RULE_annotatedLambda);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2413);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == AT_NO_WS || _la == AT_PRE_WS) {
          {
            {
              setState(2410);
              annotation();
            }
          }
          setState(2415);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2417);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (((((_la - 61)) & ~0x3f) == 0
                && ((1L << (_la - 61))
                        & ((1L << (FILE - 61))
                            | (1L << (FIELD - 61))
                            | (1L << (PROPERTY - 61))
                            | (1L << (GET - 61))
                            | (1L << (SET - 61))
                            | (1L << (RECEIVER - 61))
                            | (1L << (PARAM - 61))
                            | (1L << (SETPARAM - 61))
                            | (1L << (DELEGATE - 61))
                            | (1L << (IMPORT - 61))
                            | (1L << (CONSTRUCTOR - 61))
                            | (1L << (BY - 61))
                            | (1L << (COMPANION - 61))
                            | (1L << (INIT - 61))
                            | (1L << (WHERE - 61))
                            | (1L << (CATCH - 61))
                            | (1L << (FINALLY - 61))
                            | (1L << (OUT - 61))
                            | (1L << (DYNAMIC - 61))
                            | (1L << (PUBLIC - 61))
                            | (1L << (PRIVATE - 61))
                            | (1L << (PROTECTED - 61))
                            | (1L << (INTERNAL - 61))
                            | (1L << (ENUM - 61))
                            | (1L << (SEALED - 61))
                            | (1L << (ANNOTATION - 61))
                            | (1L << (DATA - 61))
                            | (1L << (INNER - 61))
                            | (1L << (VALUE - 61))
                            | (1L << (TAILREC - 61))
                            | (1L << (OPERATOR - 61))
                            | (1L << (INLINE - 61))
                            | (1L << (INFIX - 61))
                            | (1L << (EXTERNAL - 61))
                            | (1L << (SUSPEND - 61))
                            | (1L << (OVERRIDE - 61))
                            | (1L << (ABSTRACT - 61))))
                    != 0)
            || ((((_la - 125)) & ~0x3f) == 0
                && ((1L << (_la - 125))
                        & ((1L << (FINAL - 125))
                            | (1L << (OPEN - 125))
                            | (1L << (CONST - 125))
                            | (1L << (LATEINIT - 125))
                            | (1L << (VARARG - 125))
                            | (1L << (NOINLINE - 125))
                            | (1L << (CROSSINLINE - 125))
                            | (1L << (REIFIED - 125))
                            | (1L << (EXPECT - 125))
                            | (1L << (ACTUAL - 125))
                            | (1L << (Identifier - 125))))
                    != 0)) {
          {
            setState(2416);
            label();
          }
        }

        setState(2422);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2419);
              match(NL);
            }
          }
          setState(2424);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2425);
        lambdaLiteral();
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

  public final TypeArgumentsContext typeArguments() throws RecognitionException {
    TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
    enterRule(_localctx, 204, RULE_typeArguments);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2427);
        match(LANGLE);
        setState(2431);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2428);
              match(NL);
            }
          }
          setState(2433);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2434);
        typeProjection();
        setState(2451);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 366, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2438);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2435);
                      match(NL);
                    }
                  }
                  setState(2440);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2441);
                match(COMMA);
                setState(2445);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2442);
                      match(NL);
                    }
                  }
                  setState(2447);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2448);
                typeProjection();
              }
            }
          }
          setState(2453);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 366, _ctx);
        }
        setState(2461);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 368, _ctx)) {
          case 1:
            {
              setState(2457);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2454);
                    match(NL);
                  }
                }
                setState(2459);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2460);
              match(COMMA);
            }
            break;
        }
        setState(2466);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2463);
              match(NL);
            }
          }
          setState(2468);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2469);
        match(RANGLE);
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

  public final ValueArgumentsContext valueArguments() throws RecognitionException {
    ValueArgumentsContext _localctx = new ValueArgumentsContext(_ctx, getState());
    enterRule(_localctx, 206, RULE_valueArguments);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2471);
        match(LPAREN);
        setState(2475);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 370, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2472);
                match(NL);
              }
            }
          }
          setState(2477);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 370, _ctx);
        }
        setState(2513);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << NL)
                            | (1L << LPAREN)
                            | (1L << LSQUARE)
                            | (1L << LCURL)
                            | (1L << MULT)
                            | (1L << ADD)
                            | (1L << SUB)
                            | (1L << INCR)
                            | (1L << DECR)
                            | (1L << EXCL_WS)
                            | (1L << EXCL_NO_WS)
                            | (1L << COLONCOLON)
                            | (1L << AT_NO_WS)
                            | (1L << AT_PRE_WS)
                            | (1L << RETURN_AT)
                            | (1L << CONTINUE_AT)
                            | (1L << BREAK_AT)
                            | (1L << THIS_AT)
                            | (1L << SUPER_AT)
                            | (1L << FILE)
                            | (1L << FIELD)
                            | (1L << PROPERTY)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (GET - 64))
                            | (1L << (SET - 64))
                            | (1L << (RECEIVER - 64))
                            | (1L << (PARAM - 64))
                            | (1L << (SETPARAM - 64))
                            | (1L << (DELEGATE - 64))
                            | (1L << (IMPORT - 64))
                            | (1L << (FUN - 64))
                            | (1L << (OBJECT - 64))
                            | (1L << (CONSTRUCTOR - 64))
                            | (1L << (BY - 64))
                            | (1L << (COMPANION - 64))
                            | (1L << (INIT - 64))
                            | (1L << (THIS - 64))
                            | (1L << (SUPER - 64))
                            | (1L << (WHERE - 64))
                            | (1L << (IF - 64))
                            | (1L << (WHEN - 64))
                            | (1L << (TRY - 64))
                            | (1L << (CATCH - 64))
                            | (1L << (FINALLY - 64))
                            | (1L << (THROW - 64))
                            | (1L << (RETURN - 64))
                            | (1L << (CONTINUE - 64))
                            | (1L << (BREAK - 64))
                            | (1L << (OUT - 64))
                            | (1L << (DYNAMIC - 64))
                            | (1L << (PUBLIC - 64))
                            | (1L << (PRIVATE - 64))
                            | (1L << (PROTECTED - 64))
                            | (1L << (INTERNAL - 64))
                            | (1L << (ENUM - 64))
                            | (1L << (SEALED - 64))
                            | (1L << (ANNOTATION - 64))
                            | (1L << (DATA - 64))
                            | (1L << (INNER - 64))
                            | (1L << (VALUE - 64))
                            | (1L << (TAILREC - 64))
                            | (1L << (OPERATOR - 64))
                            | (1L << (INLINE - 64))
                            | (1L << (INFIX - 64))
                            | (1L << (EXTERNAL - 64))
                            | (1L << (SUSPEND - 64))
                            | (1L << (OVERRIDE - 64))
                            | (1L << (ABSTRACT - 64))
                            | (1L << (FINAL - 64))
                            | (1L << (OPEN - 64))
                            | (1L << (CONST - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (LATEINIT - 128))
                            | (1L << (VARARG - 128))
                            | (1L << (NOINLINE - 128))
                            | (1L << (CROSSINLINE - 128))
                            | (1L << (REIFIED - 128))
                            | (1L << (EXPECT - 128))
                            | (1L << (ACTUAL - 128))
                            | (1L << (RealLiteral - 128))
                            | (1L << (IntegerLiteral - 128))
                            | (1L << (HexLiteral - 128))
                            | (1L << (BinLiteral - 128))
                            | (1L << (UnsignedLiteral - 128))
                            | (1L << (LongLiteral - 128))
                            | (1L << (BooleanLiteral - 128))
                            | (1L << (NullLiteral - 128))
                            | (1L << (CharacterLiteral - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (QUOTE_OPEN - 128))
                            | (1L << (TRIPLE_QUOTE_OPEN - 128))))
                    != 0)) {
          {
            setState(2478);
            valueArgument();
            setState(2495);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 373, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(2482);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(2479);
                          match(NL);
                        }
                      }
                      setState(2484);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(2485);
                    match(COMMA);
                    setState(2489);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 372, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                      if (_alt == 1) {
                        {
                          {
                            setState(2486);
                            match(NL);
                          }
                        }
                      }
                      setState(2491);
                      _errHandler.sync(this);
                      _alt = getInterpreter().adaptivePredict(_input, 372, _ctx);
                    }
                    setState(2492);
                    valueArgument();
                  }
                }
              }
              setState(2497);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 373, _ctx);
            }
            setState(2505);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 375, _ctx)) {
              case 1:
                {
                  setState(2501);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(2498);
                        match(NL);
                      }
                    }
                    setState(2503);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(2504);
                  match(COMMA);
                }
                break;
            }
            setState(2510);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(2507);
                  match(NL);
                }
              }
              setState(2512);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
        }

        setState(2515);
        match(RPAREN);
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

  public final ValueArgumentContext valueArgument() throws RecognitionException {
    ValueArgumentContext _localctx = new ValueArgumentContext(_ctx, getState());
    enterRule(_localctx, 208, RULE_valueArgument);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2518);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 378, _ctx)) {
          case 1:
            {
              setState(2517);
              annotation();
            }
            break;
        }
        setState(2523);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 379, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2520);
                match(NL);
              }
            }
          }
          setState(2525);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 379, _ctx);
        }
        setState(2540);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 382, _ctx)) {
          case 1:
            {
              setState(2526);
              simpleIdentifier();
              setState(2530);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2527);
                    match(NL);
                  }
                }
                setState(2532);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2533);
              match(ASSIGNMENT);
              setState(2537);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 381, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(2534);
                      match(NL);
                    }
                  }
                }
                setState(2539);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 381, _ctx);
              }
            }
            break;
        }
        setState(2543);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == MULT) {
          {
            setState(2542);
            match(MULT);
          }
        }

        setState(2548);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2545);
              match(NL);
            }
          }
          setState(2550);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2551);
        expression();
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

  public final PrimaryExpressionContext primaryExpression() throws RecognitionException {
    PrimaryExpressionContext _localctx = new PrimaryExpressionContext(_ctx, getState());
    enterRule(_localctx, 210, RULE_primaryExpression);
    try {
      setState(2567);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 385, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(2553);
            parenthesizedExpression();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(2554);
            simpleIdentifier();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(2555);
            literalConstant();
          }
          break;
        case 4:
          enterOuterAlt(_localctx, 4);
          {
            setState(2556);
            stringLiteral();
          }
          break;
        case 5:
          enterOuterAlt(_localctx, 5);
          {
            setState(2557);
            callableReference();
          }
          break;
        case 6:
          enterOuterAlt(_localctx, 6);
          {
            setState(2558);
            functionLiteral();
          }
          break;
        case 7:
          enterOuterAlt(_localctx, 7);
          {
            setState(2559);
            objectLiteral();
          }
          break;
        case 8:
          enterOuterAlt(_localctx, 8);
          {
            setState(2560);
            collectionLiteral();
          }
          break;
        case 9:
          enterOuterAlt(_localctx, 9);
          {
            setState(2561);
            thisExpression();
          }
          break;
        case 10:
          enterOuterAlt(_localctx, 10);
          {
            setState(2562);
            superExpression();
          }
          break;
        case 11:
          enterOuterAlt(_localctx, 11);
          {
            setState(2563);
            ifExpression();
          }
          break;
        case 12:
          enterOuterAlt(_localctx, 12);
          {
            setState(2564);
            whenExpression();
          }
          break;
        case 13:
          enterOuterAlt(_localctx, 13);
          {
            setState(2565);
            tryExpression();
          }
          break;
        case 14:
          enterOuterAlt(_localctx, 14);
          {
            setState(2566);
            jumpExpression();
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

  public final ParenthesizedExpressionContext parenthesizedExpression()
      throws RecognitionException {
    ParenthesizedExpressionContext _localctx = new ParenthesizedExpressionContext(_ctx, getState());
    enterRule(_localctx, 212, RULE_parenthesizedExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2569);
        match(LPAREN);
        setState(2573);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2570);
              match(NL);
            }
          }
          setState(2575);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2576);
        expression();
        setState(2580);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2577);
              match(NL);
            }
          }
          setState(2582);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2583);
        match(RPAREN);
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

  public final CollectionLiteralContext collectionLiteral() throws RecognitionException {
    CollectionLiteralContext _localctx = new CollectionLiteralContext(_ctx, getState());
    enterRule(_localctx, 214, RULE_collectionLiteral);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2585);
        match(LSQUARE);
        setState(2589);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2586);
              match(NL);
            }
          }
          setState(2591);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2627);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << LPAREN)
                            | (1L << LSQUARE)
                            | (1L << LCURL)
                            | (1L << ADD)
                            | (1L << SUB)
                            | (1L << INCR)
                            | (1L << DECR)
                            | (1L << EXCL_WS)
                            | (1L << EXCL_NO_WS)
                            | (1L << COLONCOLON)
                            | (1L << AT_NO_WS)
                            | (1L << AT_PRE_WS)
                            | (1L << RETURN_AT)
                            | (1L << CONTINUE_AT)
                            | (1L << BREAK_AT)
                            | (1L << THIS_AT)
                            | (1L << SUPER_AT)
                            | (1L << FILE)
                            | (1L << FIELD)
                            | (1L << PROPERTY)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (GET - 64))
                            | (1L << (SET - 64))
                            | (1L << (RECEIVER - 64))
                            | (1L << (PARAM - 64))
                            | (1L << (SETPARAM - 64))
                            | (1L << (DELEGATE - 64))
                            | (1L << (IMPORT - 64))
                            | (1L << (FUN - 64))
                            | (1L << (OBJECT - 64))
                            | (1L << (CONSTRUCTOR - 64))
                            | (1L << (BY - 64))
                            | (1L << (COMPANION - 64))
                            | (1L << (INIT - 64))
                            | (1L << (THIS - 64))
                            | (1L << (SUPER - 64))
                            | (1L << (WHERE - 64))
                            | (1L << (IF - 64))
                            | (1L << (WHEN - 64))
                            | (1L << (TRY - 64))
                            | (1L << (CATCH - 64))
                            | (1L << (FINALLY - 64))
                            | (1L << (THROW - 64))
                            | (1L << (RETURN - 64))
                            | (1L << (CONTINUE - 64))
                            | (1L << (BREAK - 64))
                            | (1L << (OUT - 64))
                            | (1L << (DYNAMIC - 64))
                            | (1L << (PUBLIC - 64))
                            | (1L << (PRIVATE - 64))
                            | (1L << (PROTECTED - 64))
                            | (1L << (INTERNAL - 64))
                            | (1L << (ENUM - 64))
                            | (1L << (SEALED - 64))
                            | (1L << (ANNOTATION - 64))
                            | (1L << (DATA - 64))
                            | (1L << (INNER - 64))
                            | (1L << (VALUE - 64))
                            | (1L << (TAILREC - 64))
                            | (1L << (OPERATOR - 64))
                            | (1L << (INLINE - 64))
                            | (1L << (INFIX - 64))
                            | (1L << (EXTERNAL - 64))
                            | (1L << (SUSPEND - 64))
                            | (1L << (OVERRIDE - 64))
                            | (1L << (ABSTRACT - 64))
                            | (1L << (FINAL - 64))
                            | (1L << (OPEN - 64))
                            | (1L << (CONST - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (LATEINIT - 128))
                            | (1L << (VARARG - 128))
                            | (1L << (NOINLINE - 128))
                            | (1L << (CROSSINLINE - 128))
                            | (1L << (REIFIED - 128))
                            | (1L << (EXPECT - 128))
                            | (1L << (ACTUAL - 128))
                            | (1L << (RealLiteral - 128))
                            | (1L << (IntegerLiteral - 128))
                            | (1L << (HexLiteral - 128))
                            | (1L << (BinLiteral - 128))
                            | (1L << (UnsignedLiteral - 128))
                            | (1L << (LongLiteral - 128))
                            | (1L << (BooleanLiteral - 128))
                            | (1L << (NullLiteral - 128))
                            | (1L << (CharacterLiteral - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (QUOTE_OPEN - 128))
                            | (1L << (TRIPLE_QUOTE_OPEN - 128))))
                    != 0)) {
          {
            setState(2592);
            expression();
            setState(2609);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 391, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(2596);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(2593);
                          match(NL);
                        }
                      }
                      setState(2598);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(2599);
                    match(COMMA);
                    setState(2603);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(2600);
                          match(NL);
                        }
                      }
                      setState(2605);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(2606);
                    expression();
                  }
                }
              }
              setState(2611);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 391, _ctx);
            }
            setState(2619);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 393, _ctx)) {
              case 1:
                {
                  setState(2615);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(2612);
                        match(NL);
                      }
                    }
                    setState(2617);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(2618);
                  match(COMMA);
                }
                break;
            }
            setState(2624);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(2621);
                  match(NL);
                }
              }
              setState(2626);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
        }

        setState(2629);
        match(RSQUARE);
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

  public final LiteralConstantContext literalConstant() throws RecognitionException {
    LiteralConstantContext _localctx = new LiteralConstantContext(_ctx, getState());
    enterRule(_localctx, 216, RULE_literalConstant);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2631);
        _la = _input.LA(1);
        if (!(((((_la - 135)) & ~0x3f) == 0
            && ((1L << (_la - 135))
                    & ((1L << (RealLiteral - 135))
                        | (1L << (IntegerLiteral - 135))
                        | (1L << (HexLiteral - 135))
                        | (1L << (BinLiteral - 135))
                        | (1L << (UnsignedLiteral - 135))
                        | (1L << (LongLiteral - 135))
                        | (1L << (BooleanLiteral - 135))
                        | (1L << (NullLiteral - 135))
                        | (1L << (CharacterLiteral - 135))))
                != 0))) {
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

  public final StringLiteralContext stringLiteral() throws RecognitionException {
    StringLiteralContext _localctx = new StringLiteralContext(_ctx, getState());
    enterRule(_localctx, 218, RULE_stringLiteral);
    try {
      setState(2635);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case QUOTE_OPEN:
          enterOuterAlt(_localctx, 1);
          {
            setState(2633);
            lineStringLiteral();
          }
          break;
        case TRIPLE_QUOTE_OPEN:
          enterOuterAlt(_localctx, 2);
          {
            setState(2634);
            multiLineStringLiteral();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final LineStringLiteralContext lineStringLiteral() throws RecognitionException {
    LineStringLiteralContext _localctx = new LineStringLiteralContext(_ctx, getState());
    enterRule(_localctx, 220, RULE_lineStringLiteral);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2637);
        match(QUOTE_OPEN);
        setState(2642);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (((((_la - 159)) & ~0x3f) == 0
            && ((1L << (_la - 159))
                    & ((1L << (LineStrRef - 159))
                        | (1L << (LineStrText - 159))
                        | (1L << (LineStrEscapedChar - 159))
                        | (1L << (LineStrExprStart - 159))))
                != 0)) {
          {
            setState(2640);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
              case LineStrRef:
              case LineStrText:
              case LineStrEscapedChar:
                {
                  setState(2638);
                  lineStringContent();
                }
                break;
              case LineStrExprStart:
                {
                  setState(2639);
                  lineStringExpression();
                }
                break;
              default:
                throw new NoViableAltException(this);
            }
          }
          setState(2644);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2645);
        match(QUOTE_CLOSE);
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

  public final MultiLineStringLiteralContext multiLineStringLiteral() throws RecognitionException {
    MultiLineStringLiteralContext _localctx = new MultiLineStringLiteralContext(_ctx, getState());
    enterRule(_localctx, 222, RULE_multiLineStringLiteral);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2647);
        match(TRIPLE_QUOTE_OPEN);
        setState(2653);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (((((_la - 164)) & ~0x3f) == 0
            && ((1L << (_la - 164))
                    & ((1L << (MultiLineStringQuote - 164))
                        | (1L << (MultiLineStrRef - 164))
                        | (1L << (MultiLineStrText - 164))
                        | (1L << (MultiLineStrExprStart - 164))))
                != 0)) {
          {
            setState(2651);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 399, _ctx)) {
              case 1:
                {
                  setState(2648);
                  multiLineStringContent();
                }
                break;
              case 2:
                {
                  setState(2649);
                  multiLineStringExpression();
                }
                break;
              case 3:
                {
                  setState(2650);
                  match(MultiLineStringQuote);
                }
                break;
            }
          }
          setState(2655);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2656);
        match(TRIPLE_QUOTE_CLOSE);
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

  public final LineStringContentContext lineStringContent() throws RecognitionException {
    LineStringContentContext _localctx = new LineStringContentContext(_ctx, getState());
    enterRule(_localctx, 224, RULE_lineStringContent);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2658);
        _la = _input.LA(1);
        if (!(((((_la - 159)) & ~0x3f) == 0
            && ((1L << (_la - 159))
                    & ((1L << (LineStrRef - 159))
                        | (1L << (LineStrText - 159))
                        | (1L << (LineStrEscapedChar - 159))))
                != 0))) {
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

  public final LineStringExpressionContext lineStringExpression() throws RecognitionException {
    LineStringExpressionContext _localctx = new LineStringExpressionContext(_ctx, getState());
    enterRule(_localctx, 226, RULE_lineStringExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2660);
        match(LineStrExprStart);
        setState(2664);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2661);
              match(NL);
            }
          }
          setState(2666);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2667);
        expression();
        setState(2671);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2668);
              match(NL);
            }
          }
          setState(2673);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2674);
        match(RCURL);
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

  public final MultiLineStringContentContext multiLineStringContent() throws RecognitionException {
    MultiLineStringContentContext _localctx = new MultiLineStringContentContext(_ctx, getState());
    enterRule(_localctx, 228, RULE_multiLineStringContent);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2676);
        _la = _input.LA(1);
        if (!(((((_la - 164)) & ~0x3f) == 0
            && ((1L << (_la - 164))
                    & ((1L << (MultiLineStringQuote - 164))
                        | (1L << (MultiLineStrRef - 164))
                        | (1L << (MultiLineStrText - 164))))
                != 0))) {
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

  public final MultiLineStringExpressionContext multiLineStringExpression()
      throws RecognitionException {
    MultiLineStringExpressionContext _localctx =
        new MultiLineStringExpressionContext(_ctx, getState());
    enterRule(_localctx, 230, RULE_multiLineStringExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2678);
        match(MultiLineStrExprStart);
        setState(2682);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2679);
              match(NL);
            }
          }
          setState(2684);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2685);
        expression();
        setState(2689);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2686);
              match(NL);
            }
          }
          setState(2691);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2692);
        match(RCURL);
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

  public final LambdaLiteralContext lambdaLiteral() throws RecognitionException {
    LambdaLiteralContext _localctx = new LambdaLiteralContext(_ctx, getState());
    enterRule(_localctx, 232, RULE_lambdaLiteral);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2694);
        match(LCURL);
        setState(2698);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 405, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2695);
                match(NL);
              }
            }
          }
          setState(2700);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 405, _ctx);
        }
        setState(2717);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 409, _ctx)) {
          case 1:
            {
              setState(2702);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 406, _ctx)) {
                case 1:
                  {
                    setState(2701);
                    lambdaParameters();
                  }
                  break;
              }
              setState(2707);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2704);
                    match(NL);
                  }
                }
                setState(2709);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2710);
              match(ARROW);
              setState(2714);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 408, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(2711);
                      match(NL);
                    }
                  }
                }
                setState(2716);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 408, _ctx);
              }
            }
            break;
        }
        setState(2719);
        statements();
        setState(2723);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2720);
              match(NL);
            }
          }
          setState(2725);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2726);
        match(RCURL);
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

  public final LambdaParametersContext lambdaParameters() throws RecognitionException {
    LambdaParametersContext _localctx = new LambdaParametersContext(_ctx, getState());
    enterRule(_localctx, 234, RULE_lambdaParameters);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2728);
        lambdaParameter();
        setState(2745);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 413, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2732);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(2729);
                      match(NL);
                    }
                  }
                  setState(2734);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(2735);
                match(COMMA);
                setState(2739);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 412, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                  if (_alt == 1) {
                    {
                      {
                        setState(2736);
                        match(NL);
                      }
                    }
                  }
                  setState(2741);
                  _errHandler.sync(this);
                  _alt = getInterpreter().adaptivePredict(_input, 412, _ctx);
                }
                setState(2742);
                lambdaParameter();
              }
            }
          }
          setState(2747);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 413, _ctx);
        }
        setState(2755);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 415, _ctx)) {
          case 1:
            {
              setState(2751);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2748);
                    match(NL);
                  }
                }
                setState(2753);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2754);
              match(COMMA);
            }
            break;
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

  public final LambdaParameterContext lambdaParameter() throws RecognitionException {
    LambdaParameterContext _localctx = new LambdaParameterContext(_ctx, getState());
    enterRule(_localctx, 236, RULE_lambdaParameter);
    int _la;
    try {
      setState(2775);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case NL:
        case AT_NO_WS:
        case AT_PRE_WS:
        case FILE:
        case FIELD:
        case PROPERTY:
        case GET:
        case SET:
        case RECEIVER:
        case PARAM:
        case SETPARAM:
        case DELEGATE:
        case IMPORT:
        case CONSTRUCTOR:
        case BY:
        case COMPANION:
        case INIT:
        case WHERE:
        case CATCH:
        case FINALLY:
        case OUT:
        case DYNAMIC:
        case PUBLIC:
        case PRIVATE:
        case PROTECTED:
        case INTERNAL:
        case ENUM:
        case SEALED:
        case ANNOTATION:
        case DATA:
        case INNER:
        case VALUE:
        case TAILREC:
        case OPERATOR:
        case INLINE:
        case INFIX:
        case EXTERNAL:
        case SUSPEND:
        case OVERRIDE:
        case ABSTRACT:
        case FINAL:
        case OPEN:
        case CONST:
        case LATEINIT:
        case VARARG:
        case NOINLINE:
        case CROSSINLINE:
        case REIFIED:
        case EXPECT:
        case ACTUAL:
        case Identifier:
          enterOuterAlt(_localctx, 1);
          {
            setState(2757);
            variableDeclaration();
          }
          break;
        case LPAREN:
          enterOuterAlt(_localctx, 2);
          {
            setState(2758);
            multiVariableDeclaration();
            setState(2773);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 418, _ctx)) {
              case 1:
                {
                  setState(2762);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(2759);
                        match(NL);
                      }
                    }
                    setState(2764);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(2765);
                  match(COLON);
                  setState(2769);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(2766);
                        match(NL);
                      }
                    }
                    setState(2771);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(2772);
                  type();
                }
                break;
            }
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final AnonymousFunctionContext anonymousFunction() throws RecognitionException {
    AnonymousFunctionContext _localctx = new AnonymousFunctionContext(_ctx, getState());
    enterRule(_localctx, 238, RULE_anonymousFunction);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2777);
        match(FUN);
        setState(2793);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 422, _ctx)) {
          case 1:
            {
              setState(2781);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2778);
                    match(NL);
                  }
                }
                setState(2783);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2784);
              type();
              setState(2788);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2785);
                    match(NL);
                  }
                }
                setState(2790);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2791);
              match(DOT);
            }
            break;
        }
        setState(2798);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2795);
              match(NL);
            }
          }
          setState(2800);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2801);
        parametersWithOptionalType();
        setState(2816);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 426, _ctx)) {
          case 1:
            {
              setState(2805);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2802);
                    match(NL);
                  }
                }
                setState(2807);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2808);
              match(COLON);
              setState(2812);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2809);
                    match(NL);
                  }
                }
                setState(2814);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2815);
              type();
            }
            break;
        }
        setState(2825);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 428, _ctx)) {
          case 1:
            {
              setState(2821);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2818);
                    match(NL);
                  }
                }
                setState(2823);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2824);
              typeConstraints();
            }
            break;
        }
        setState(2834);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 430, _ctx)) {
          case 1:
            {
              setState(2830);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2827);
                    match(NL);
                  }
                }
                setState(2832);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2833);
              functionBody();
            }
            break;
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

  public final FunctionLiteralContext functionLiteral() throws RecognitionException {
    FunctionLiteralContext _localctx = new FunctionLiteralContext(_ctx, getState());
    enterRule(_localctx, 240, RULE_functionLiteral);
    try {
      setState(2838);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LCURL:
          enterOuterAlt(_localctx, 1);
          {
            setState(2836);
            lambdaLiteral();
          }
          break;
        case FUN:
          enterOuterAlt(_localctx, 2);
          {
            setState(2837);
            anonymousFunction();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final ObjectLiteralContext objectLiteral() throws RecognitionException {
    ObjectLiteralContext _localctx = new ObjectLiteralContext(_ctx, getState());
    enterRule(_localctx, 242, RULE_objectLiteral);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2840);
        match(OBJECT);
        setState(2861);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 435, _ctx)) {
          case 1:
            {
              setState(2844);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2841);
                    match(NL);
                  }
                }
                setState(2846);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2847);
              match(COLON);
              setState(2851);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 433, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(2848);
                      match(NL);
                    }
                  }
                }
                setState(2853);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 433, _ctx);
              }
              setState(2854);
              delegationSpecifiers();
              setState(2858);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 434, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(2855);
                      match(NL);
                    }
                  }
                }
                setState(2860);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 434, _ctx);
              }
            }
            break;
        }
        setState(2870);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 437, _ctx)) {
          case 1:
            {
              setState(2866);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2863);
                    match(NL);
                  }
                }
                setState(2868);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2869);
              classBody();
            }
            break;
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

  public final ThisExpressionContext thisExpression() throws RecognitionException {
    ThisExpressionContext _localctx = new ThisExpressionContext(_ctx, getState());
    enterRule(_localctx, 244, RULE_thisExpression);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(2872);
        _la = _input.LA(1);
        if (!(_la == THIS_AT || _la == THIS)) {
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

  public final SuperExpressionContext superExpression() throws RecognitionException {
    SuperExpressionContext _localctx = new SuperExpressionContext(_ctx, getState());
    enterRule(_localctx, 246, RULE_superExpression);
    int _la;
    try {
      setState(2898);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case SUPER:
          enterOuterAlt(_localctx, 1);
          {
            setState(2874);
            match(SUPER);
            setState(2891);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 440, _ctx)) {
              case 1:
                {
                  setState(2875);
                  match(LANGLE);
                  setState(2879);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(2876);
                        match(NL);
                      }
                    }
                    setState(2881);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(2882);
                  type();
                  setState(2886);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(2883);
                        match(NL);
                      }
                    }
                    setState(2888);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(2889);
                  match(RANGLE);
                }
                break;
            }
            setState(2895);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 441, _ctx)) {
              case 1:
                {
                  setState(2893);
                  match(AT_NO_WS);
                  setState(2894);
                  simpleIdentifier();
                }
                break;
            }
          }
          break;
        case SUPER_AT:
          enterOuterAlt(_localctx, 2);
          {
            setState(2897);
            match(SUPER_AT);
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final IfExpressionContext ifExpression() throws RecognitionException {
    IfExpressionContext _localctx = new IfExpressionContext(_ctx, getState());
    enterRule(_localctx, 248, RULE_ifExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2900);
        match(IF);
        setState(2904);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2901);
              match(NL);
            }
          }
          setState(2906);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2907);
        match(LPAREN);
        setState(2911);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2908);
              match(NL);
            }
          }
          setState(2913);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2914);
        expression();
        setState(2918);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(2915);
              match(NL);
            }
          }
          setState(2920);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(2921);
        match(RPAREN);
        setState(2925);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 446, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(2922);
                match(NL);
              }
            }
          }
          setState(2927);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 446, _ctx);
        }
        setState(2959);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 453, _ctx)) {
          case 1:
            {
              setState(2928);
              controlStructureBody();
            }
            break;
          case 2:
            {
              setState(2930);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if ((((_la) & ~0x3f) == 0
                      && ((1L << _la)
                              & ((1L << LPAREN)
                                  | (1L << LSQUARE)
                                  | (1L << LCURL)
                                  | (1L << ADD)
                                  | (1L << SUB)
                                  | (1L << INCR)
                                  | (1L << DECR)
                                  | (1L << EXCL_WS)
                                  | (1L << EXCL_NO_WS)
                                  | (1L << COLONCOLON)
                                  | (1L << AT_NO_WS)
                                  | (1L << AT_PRE_WS)
                                  | (1L << RETURN_AT)
                                  | (1L << CONTINUE_AT)
                                  | (1L << BREAK_AT)
                                  | (1L << THIS_AT)
                                  | (1L << SUPER_AT)
                                  | (1L << FILE)
                                  | (1L << FIELD)
                                  | (1L << PROPERTY)))
                          != 0)
                  || ((((_la - 64)) & ~0x3f) == 0
                      && ((1L << (_la - 64))
                              & ((1L << (GET - 64))
                                  | (1L << (SET - 64))
                                  | (1L << (RECEIVER - 64))
                                  | (1L << (PARAM - 64))
                                  | (1L << (SETPARAM - 64))
                                  | (1L << (DELEGATE - 64))
                                  | (1L << (IMPORT - 64))
                                  | (1L << (CLASS - 64))
                                  | (1L << (INTERFACE - 64))
                                  | (1L << (FUN - 64))
                                  | (1L << (OBJECT - 64))
                                  | (1L << (VAL - 64))
                                  | (1L << (VAR - 64))
                                  | (1L << (TYPE_ALIAS - 64))
                                  | (1L << (CONSTRUCTOR - 64))
                                  | (1L << (BY - 64))
                                  | (1L << (COMPANION - 64))
                                  | (1L << (INIT - 64))
                                  | (1L << (THIS - 64))
                                  | (1L << (SUPER - 64))
                                  | (1L << (WHERE - 64))
                                  | (1L << (IF - 64))
                                  | (1L << (WHEN - 64))
                                  | (1L << (TRY - 64))
                                  | (1L << (CATCH - 64))
                                  | (1L << (FINALLY - 64))
                                  | (1L << (FOR - 64))
                                  | (1L << (DO - 64))
                                  | (1L << (WHILE - 64))
                                  | (1L << (THROW - 64))
                                  | (1L << (RETURN - 64))
                                  | (1L << (CONTINUE - 64))
                                  | (1L << (BREAK - 64))
                                  | (1L << (OUT - 64))
                                  | (1L << (DYNAMIC - 64))
                                  | (1L << (PUBLIC - 64))
                                  | (1L << (PRIVATE - 64))
                                  | (1L << (PROTECTED - 64))
                                  | (1L << (INTERNAL - 64))
                                  | (1L << (ENUM - 64))
                                  | (1L << (SEALED - 64))
                                  | (1L << (ANNOTATION - 64))
                                  | (1L << (DATA - 64))
                                  | (1L << (INNER - 64))
                                  | (1L << (VALUE - 64))
                                  | (1L << (TAILREC - 64))
                                  | (1L << (OPERATOR - 64))
                                  | (1L << (INLINE - 64))
                                  | (1L << (INFIX - 64))
                                  | (1L << (EXTERNAL - 64))
                                  | (1L << (SUSPEND - 64))
                                  | (1L << (OVERRIDE - 64))
                                  | (1L << (ABSTRACT - 64))
                                  | (1L << (FINAL - 64))
                                  | (1L << (OPEN - 64))
                                  | (1L << (CONST - 64))))
                          != 0)
                  || ((((_la - 128)) & ~0x3f) == 0
                      && ((1L << (_la - 128))
                              & ((1L << (LATEINIT - 128))
                                  | (1L << (VARARG - 128))
                                  | (1L << (NOINLINE - 128))
                                  | (1L << (CROSSINLINE - 128))
                                  | (1L << (REIFIED - 128))
                                  | (1L << (EXPECT - 128))
                                  | (1L << (ACTUAL - 128))
                                  | (1L << (RealLiteral - 128))
                                  | (1L << (IntegerLiteral - 128))
                                  | (1L << (HexLiteral - 128))
                                  | (1L << (BinLiteral - 128))
                                  | (1L << (UnsignedLiteral - 128))
                                  | (1L << (LongLiteral - 128))
                                  | (1L << (BooleanLiteral - 128))
                                  | (1L << (NullLiteral - 128))
                                  | (1L << (CharacterLiteral - 128))
                                  | (1L << (Identifier - 128))
                                  | (1L << (QUOTE_OPEN - 128))
                                  | (1L << (TRIPLE_QUOTE_OPEN - 128))))
                          != 0)) {
                {
                  setState(2929);
                  controlStructureBody();
                }
              }

              setState(2935);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 448, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(2932);
                      match(NL);
                    }
                  }
                }
                setState(2937);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 448, _ctx);
              }
              setState(2939);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == SEMICOLON) {
                {
                  setState(2938);
                  match(SEMICOLON);
                }
              }

              setState(2944);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2941);
                    match(NL);
                  }
                }
                setState(2946);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2947);
              match(ELSE);
              setState(2951);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2948);
                    match(NL);
                  }
                }
                setState(2953);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2956);
              _errHandler.sync(this);
              switch (_input.LA(1)) {
                case LPAREN:
                case LSQUARE:
                case LCURL:
                case ADD:
                case SUB:
                case INCR:
                case DECR:
                case EXCL_WS:
                case EXCL_NO_WS:
                case COLONCOLON:
                case AT_NO_WS:
                case AT_PRE_WS:
                case RETURN_AT:
                case CONTINUE_AT:
                case BREAK_AT:
                case THIS_AT:
                case SUPER_AT:
                case FILE:
                case FIELD:
                case PROPERTY:
                case GET:
                case SET:
                case RECEIVER:
                case PARAM:
                case SETPARAM:
                case DELEGATE:
                case IMPORT:
                case CLASS:
                case INTERFACE:
                case FUN:
                case OBJECT:
                case VAL:
                case VAR:
                case TYPE_ALIAS:
                case CONSTRUCTOR:
                case BY:
                case COMPANION:
                case INIT:
                case THIS:
                case SUPER:
                case WHERE:
                case IF:
                case WHEN:
                case TRY:
                case CATCH:
                case FINALLY:
                case FOR:
                case DO:
                case WHILE:
                case THROW:
                case RETURN:
                case CONTINUE:
                case BREAK:
                case OUT:
                case DYNAMIC:
                case PUBLIC:
                case PRIVATE:
                case PROTECTED:
                case INTERNAL:
                case ENUM:
                case SEALED:
                case ANNOTATION:
                case DATA:
                case INNER:
                case VALUE:
                case TAILREC:
                case OPERATOR:
                case INLINE:
                case INFIX:
                case EXTERNAL:
                case SUSPEND:
                case OVERRIDE:
                case ABSTRACT:
                case FINAL:
                case OPEN:
                case CONST:
                case LATEINIT:
                case VARARG:
                case NOINLINE:
                case CROSSINLINE:
                case REIFIED:
                case EXPECT:
                case ACTUAL:
                case RealLiteral:
                case IntegerLiteral:
                case HexLiteral:
                case BinLiteral:
                case UnsignedLiteral:
                case LongLiteral:
                case BooleanLiteral:
                case NullLiteral:
                case CharacterLiteral:
                case Identifier:
                case QUOTE_OPEN:
                case TRIPLE_QUOTE_OPEN:
                  {
                    setState(2954);
                    controlStructureBody();
                  }
                  break;
                case SEMICOLON:
                  {
                    setState(2955);
                    match(SEMICOLON);
                  }
                  break;
                default:
                  throw new NoViableAltException(this);
              }
            }
            break;
          case 3:
            {
              setState(2958);
              match(SEMICOLON);
            }
            break;
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

  public final WhenSubjectContext whenSubject() throws RecognitionException {
    WhenSubjectContext _localctx = new WhenSubjectContext(_ctx, getState());
    enterRule(_localctx, 250, RULE_whenSubject);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(2961);
        match(LPAREN);
        setState(2995);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 459, _ctx)) {
          case 1:
            {
              setState(2965);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == AT_NO_WS || _la == AT_PRE_WS) {
                {
                  {
                    setState(2962);
                    annotation();
                  }
                }
                setState(2967);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2971);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2968);
                    match(NL);
                  }
                }
                setState(2973);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2974);
              match(VAL);
              setState(2978);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 456, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(2975);
                      match(NL);
                    }
                  }
                }
                setState(2980);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 456, _ctx);
              }
              setState(2981);
              variableDeclaration();
              setState(2985);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2982);
                    match(NL);
                  }
                }
                setState(2987);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(2988);
              match(ASSIGNMENT);
              setState(2992);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(2989);
                    match(NL);
                  }
                }
                setState(2994);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
        }
        setState(2997);
        expression();
        setState(2998);
        match(RPAREN);
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

  public final WhenExpressionContext whenExpression() throws RecognitionException {
    WhenExpressionContext _localctx = new WhenExpressionContext(_ctx, getState());
    enterRule(_localctx, 252, RULE_whenExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3000);
        match(WHEN);
        setState(3004);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 460, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(3001);
                match(NL);
              }
            }
          }
          setState(3006);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 460, _ctx);
        }
        setState(3008);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == LPAREN) {
          {
            setState(3007);
            whenSubject();
          }
        }

        setState(3013);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3010);
              match(NL);
            }
          }
          setState(3015);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3016);
        match(LCURL);
        setState(3020);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 463, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(3017);
                match(NL);
              }
            }
          }
          setState(3022);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 463, _ctx);
        }
        setState(3032);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << LPAREN)
                            | (1L << LSQUARE)
                            | (1L << LCURL)
                            | (1L << ADD)
                            | (1L << SUB)
                            | (1L << INCR)
                            | (1L << DECR)
                            | (1L << EXCL_WS)
                            | (1L << EXCL_NO_WS)
                            | (1L << COLONCOLON)
                            | (1L << AT_NO_WS)
                            | (1L << AT_PRE_WS)
                            | (1L << RETURN_AT)
                            | (1L << CONTINUE_AT)
                            | (1L << BREAK_AT)
                            | (1L << THIS_AT)
                            | (1L << SUPER_AT)
                            | (1L << FILE)
                            | (1L << FIELD)
                            | (1L << PROPERTY)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (GET - 64))
                            | (1L << (SET - 64))
                            | (1L << (RECEIVER - 64))
                            | (1L << (PARAM - 64))
                            | (1L << (SETPARAM - 64))
                            | (1L << (DELEGATE - 64))
                            | (1L << (IMPORT - 64))
                            | (1L << (FUN - 64))
                            | (1L << (OBJECT - 64))
                            | (1L << (CONSTRUCTOR - 64))
                            | (1L << (BY - 64))
                            | (1L << (COMPANION - 64))
                            | (1L << (INIT - 64))
                            | (1L << (THIS - 64))
                            | (1L << (SUPER - 64))
                            | (1L << (WHERE - 64))
                            | (1L << (IF - 64))
                            | (1L << (ELSE - 64))
                            | (1L << (WHEN - 64))
                            | (1L << (TRY - 64))
                            | (1L << (CATCH - 64))
                            | (1L << (FINALLY - 64))
                            | (1L << (THROW - 64))
                            | (1L << (RETURN - 64))
                            | (1L << (CONTINUE - 64))
                            | (1L << (BREAK - 64))
                            | (1L << (IS - 64))
                            | (1L << (IN - 64))
                            | (1L << (NOT_IS - 64))
                            | (1L << (NOT_IN - 64))
                            | (1L << (OUT - 64))
                            | (1L << (DYNAMIC - 64))
                            | (1L << (PUBLIC - 64))
                            | (1L << (PRIVATE - 64))
                            | (1L << (PROTECTED - 64))
                            | (1L << (INTERNAL - 64))
                            | (1L << (ENUM - 64))
                            | (1L << (SEALED - 64))
                            | (1L << (ANNOTATION - 64))
                            | (1L << (DATA - 64))
                            | (1L << (INNER - 64))
                            | (1L << (VALUE - 64))
                            | (1L << (TAILREC - 64))
                            | (1L << (OPERATOR - 64))
                            | (1L << (INLINE - 64))
                            | (1L << (INFIX - 64))
                            | (1L << (EXTERNAL - 64))
                            | (1L << (SUSPEND - 64))
                            | (1L << (OVERRIDE - 64))
                            | (1L << (ABSTRACT - 64))
                            | (1L << (FINAL - 64))
                            | (1L << (OPEN - 64))
                            | (1L << (CONST - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (LATEINIT - 128))
                            | (1L << (VARARG - 128))
                            | (1L << (NOINLINE - 128))
                            | (1L << (CROSSINLINE - 128))
                            | (1L << (REIFIED - 128))
                            | (1L << (EXPECT - 128))
                            | (1L << (ACTUAL - 128))
                            | (1L << (RealLiteral - 128))
                            | (1L << (IntegerLiteral - 128))
                            | (1L << (HexLiteral - 128))
                            | (1L << (BinLiteral - 128))
                            | (1L << (UnsignedLiteral - 128))
                            | (1L << (LongLiteral - 128))
                            | (1L << (BooleanLiteral - 128))
                            | (1L << (NullLiteral - 128))
                            | (1L << (CharacterLiteral - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (QUOTE_OPEN - 128))
                            | (1L << (TRIPLE_QUOTE_OPEN - 128))))
                    != 0)) {
          {
            {
              setState(3023);
              whenEntry();
              setState(3027);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 464, _ctx);
              while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                if (_alt == 1) {
                  {
                    {
                      setState(3024);
                      match(NL);
                    }
                  }
                }
                setState(3029);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 464, _ctx);
              }
            }
          }
          setState(3034);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3038);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3035);
              match(NL);
            }
          }
          setState(3040);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3041);
        match(RCURL);
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

  public final WhenEntryContext whenEntry() throws RecognitionException {
    WhenEntryContext _localctx = new WhenEntryContext(_ctx, getState());
    enterRule(_localctx, 254, RULE_whenEntry);
    int _la;
    try {
      int _alt;
      setState(3107);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LPAREN:
        case LSQUARE:
        case LCURL:
        case ADD:
        case SUB:
        case INCR:
        case DECR:
        case EXCL_WS:
        case EXCL_NO_WS:
        case COLONCOLON:
        case AT_NO_WS:
        case AT_PRE_WS:
        case RETURN_AT:
        case CONTINUE_AT:
        case BREAK_AT:
        case THIS_AT:
        case SUPER_AT:
        case FILE:
        case FIELD:
        case PROPERTY:
        case GET:
        case SET:
        case RECEIVER:
        case PARAM:
        case SETPARAM:
        case DELEGATE:
        case IMPORT:
        case FUN:
        case OBJECT:
        case CONSTRUCTOR:
        case BY:
        case COMPANION:
        case INIT:
        case THIS:
        case SUPER:
        case WHERE:
        case IF:
        case WHEN:
        case TRY:
        case CATCH:
        case FINALLY:
        case THROW:
        case RETURN:
        case CONTINUE:
        case BREAK:
        case IS:
        case IN:
        case NOT_IS:
        case NOT_IN:
        case OUT:
        case DYNAMIC:
        case PUBLIC:
        case PRIVATE:
        case PROTECTED:
        case INTERNAL:
        case ENUM:
        case SEALED:
        case ANNOTATION:
        case DATA:
        case INNER:
        case VALUE:
        case TAILREC:
        case OPERATOR:
        case INLINE:
        case INFIX:
        case EXTERNAL:
        case SUSPEND:
        case OVERRIDE:
        case ABSTRACT:
        case FINAL:
        case OPEN:
        case CONST:
        case LATEINIT:
        case VARARG:
        case NOINLINE:
        case CROSSINLINE:
        case REIFIED:
        case EXPECT:
        case ACTUAL:
        case RealLiteral:
        case IntegerLiteral:
        case HexLiteral:
        case BinLiteral:
        case UnsignedLiteral:
        case LongLiteral:
        case BooleanLiteral:
        case NullLiteral:
        case CharacterLiteral:
        case Identifier:
        case QUOTE_OPEN:
        case TRIPLE_QUOTE_OPEN:
          enterOuterAlt(_localctx, 1);
          {
            setState(3043);
            whenCondition();
            setState(3060);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 469, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(3047);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(3044);
                          match(NL);
                        }
                      }
                      setState(3049);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(3050);
                    match(COMMA);
                    setState(3054);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(3051);
                          match(NL);
                        }
                      }
                      setState(3056);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(3057);
                    whenCondition();
                  }
                }
              }
              setState(3062);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 469, _ctx);
            }
            setState(3070);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 471, _ctx)) {
              case 1:
                {
                  setState(3066);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  while (_la == NL) {
                    {
                      {
                        setState(3063);
                        match(NL);
                      }
                    }
                    setState(3068);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                  }
                  setState(3069);
                  match(COMMA);
                }
                break;
            }
            setState(3075);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3072);
                  match(NL);
                }
              }
              setState(3077);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3078);
            match(ARROW);
            setState(3082);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3079);
                  match(NL);
                }
              }
              setState(3084);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3085);
            controlStructureBody();
            setState(3087);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 474, _ctx)) {
              case 1:
                {
                  setState(3086);
                  semi();
                }
                break;
            }
          }
          break;
        case ELSE:
          enterOuterAlt(_localctx, 2);
          {
            setState(3089);
            match(ELSE);
            setState(3093);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3090);
                  match(NL);
                }
              }
              setState(3095);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3096);
            match(ARROW);
            setState(3100);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3097);
                  match(NL);
                }
              }
              setState(3102);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3103);
            controlStructureBody();
            setState(3105);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 477, _ctx)) {
              case 1:
                {
                  setState(3104);
                  semi();
                }
                break;
            }
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final WhenConditionContext whenCondition() throws RecognitionException {
    WhenConditionContext _localctx = new WhenConditionContext(_ctx, getState());
    enterRule(_localctx, 256, RULE_whenCondition);
    try {
      setState(3112);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case LPAREN:
        case LSQUARE:
        case LCURL:
        case ADD:
        case SUB:
        case INCR:
        case DECR:
        case EXCL_WS:
        case EXCL_NO_WS:
        case COLONCOLON:
        case AT_NO_WS:
        case AT_PRE_WS:
        case RETURN_AT:
        case CONTINUE_AT:
        case BREAK_AT:
        case THIS_AT:
        case SUPER_AT:
        case FILE:
        case FIELD:
        case PROPERTY:
        case GET:
        case SET:
        case RECEIVER:
        case PARAM:
        case SETPARAM:
        case DELEGATE:
        case IMPORT:
        case FUN:
        case OBJECT:
        case CONSTRUCTOR:
        case BY:
        case COMPANION:
        case INIT:
        case THIS:
        case SUPER:
        case WHERE:
        case IF:
        case WHEN:
        case TRY:
        case CATCH:
        case FINALLY:
        case THROW:
        case RETURN:
        case CONTINUE:
        case BREAK:
        case OUT:
        case DYNAMIC:
        case PUBLIC:
        case PRIVATE:
        case PROTECTED:
        case INTERNAL:
        case ENUM:
        case SEALED:
        case ANNOTATION:
        case DATA:
        case INNER:
        case VALUE:
        case TAILREC:
        case OPERATOR:
        case INLINE:
        case INFIX:
        case EXTERNAL:
        case SUSPEND:
        case OVERRIDE:
        case ABSTRACT:
        case FINAL:
        case OPEN:
        case CONST:
        case LATEINIT:
        case VARARG:
        case NOINLINE:
        case CROSSINLINE:
        case REIFIED:
        case EXPECT:
        case ACTUAL:
        case RealLiteral:
        case IntegerLiteral:
        case HexLiteral:
        case BinLiteral:
        case UnsignedLiteral:
        case LongLiteral:
        case BooleanLiteral:
        case NullLiteral:
        case CharacterLiteral:
        case Identifier:
        case QUOTE_OPEN:
        case TRIPLE_QUOTE_OPEN:
          enterOuterAlt(_localctx, 1);
          {
            setState(3109);
            expression();
          }
          break;
        case IN:
        case NOT_IN:
          enterOuterAlt(_localctx, 2);
          {
            setState(3110);
            rangeTest();
          }
          break;
        case IS:
        case NOT_IS:
          enterOuterAlt(_localctx, 3);
          {
            setState(3111);
            typeTest();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final RangeTestContext rangeTest() throws RecognitionException {
    RangeTestContext _localctx = new RangeTestContext(_ctx, getState());
    enterRule(_localctx, 258, RULE_rangeTest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3114);
        inOperator();
        setState(3118);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3115);
              match(NL);
            }
          }
          setState(3120);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3121);
        expression();
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

  public final TypeTestContext typeTest() throws RecognitionException {
    TypeTestContext _localctx = new TypeTestContext(_ctx, getState());
    enterRule(_localctx, 260, RULE_typeTest);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3123);
        isOperator();
        setState(3127);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3124);
              match(NL);
            }
          }
          setState(3129);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3130);
        type();
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

  public final TryExpressionContext tryExpression() throws RecognitionException {
    TryExpressionContext _localctx = new TryExpressionContext(_ctx, getState());
    enterRule(_localctx, 262, RULE_tryExpression);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3132);
        match(TRY);
        setState(3136);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3133);
              match(NL);
            }
          }
          setState(3138);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3139);
        block();
        setState(3167);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 488, _ctx)) {
          case 1:
            {
              setState(3147);
              _errHandler.sync(this);
              _alt = 1;
              do {
                switch (_alt) {
                  case 1:
                    {
                      {
                        setState(3143);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == NL) {
                          {
                            {
                              setState(3140);
                              match(NL);
                            }
                          }
                          setState(3145);
                          _errHandler.sync(this);
                          _la = _input.LA(1);
                        }
                        setState(3146);
                        catchBlock();
                      }
                    }
                    break;
                  default:
                    throw new NoViableAltException(this);
                }
                setState(3149);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 484, _ctx);
              } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
              setState(3158);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 486, _ctx)) {
                case 1:
                  {
                    setState(3154);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == NL) {
                      {
                        {
                          setState(3151);
                          match(NL);
                        }
                      }
                      setState(3156);
                      _errHandler.sync(this);
                      _la = _input.LA(1);
                    }
                    setState(3157);
                    finallyBlock();
                  }
                  break;
              }
            }
            break;
          case 2:
            {
              setState(3163);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(3160);
                    match(NL);
                  }
                }
                setState(3165);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
              setState(3166);
              finallyBlock();
            }
            break;
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

  public final CatchBlockContext catchBlock() throws RecognitionException {
    CatchBlockContext _localctx = new CatchBlockContext(_ctx, getState());
    enterRule(_localctx, 264, RULE_catchBlock);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3169);
        match(CATCH);
        setState(3173);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3170);
              match(NL);
            }
          }
          setState(3175);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3176);
        match(LPAREN);
        setState(3180);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == AT_NO_WS || _la == AT_PRE_WS) {
          {
            {
              setState(3177);
              annotation();
            }
          }
          setState(3182);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3183);
        simpleIdentifier();
        setState(3184);
        match(COLON);
        setState(3185);
        type();
        setState(3193);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == NL || _la == COMMA) {
          {
            setState(3189);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3186);
                  match(NL);
                }
              }
              setState(3191);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3192);
            match(COMMA);
          }
        }

        setState(3195);
        match(RPAREN);
        setState(3199);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3196);
              match(NL);
            }
          }
          setState(3201);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3202);
        block();
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

  public final FinallyBlockContext finallyBlock() throws RecognitionException {
    FinallyBlockContext _localctx = new FinallyBlockContext(_ctx, getState());
    enterRule(_localctx, 266, RULE_finallyBlock);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3204);
        match(FINALLY);
        setState(3208);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3205);
              match(NL);
            }
          }
          setState(3210);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3211);
        block();
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

  public final JumpExpressionContext jumpExpression() throws RecognitionException {
    JumpExpressionContext _localctx = new JumpExpressionContext(_ctx, getState());
    enterRule(_localctx, 268, RULE_jumpExpression);
    int _la;
    try {
      setState(3229);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case THROW:
          enterOuterAlt(_localctx, 1);
          {
            setState(3213);
            match(THROW);
            setState(3217);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3214);
                  match(NL);
                }
              }
              setState(3219);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3220);
            expression();
          }
          break;
        case RETURN_AT:
        case RETURN:
          enterOuterAlt(_localctx, 2);
          {
            setState(3221);
            _la = _input.LA(1);
            if (!(_la == RETURN_AT || _la == RETURN)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) matchedEOF = true;
              _errHandler.reportMatch(this);
              consume();
            }
            setState(3223);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 496, _ctx)) {
              case 1:
                {
                  setState(3222);
                  expression();
                }
                break;
            }
          }
          break;
        case CONTINUE:
          enterOuterAlt(_localctx, 3);
          {
            setState(3225);
            match(CONTINUE);
          }
          break;
        case CONTINUE_AT:
          enterOuterAlt(_localctx, 4);
          {
            setState(3226);
            match(CONTINUE_AT);
          }
          break;
        case BREAK:
          enterOuterAlt(_localctx, 5);
          {
            setState(3227);
            match(BREAK);
          }
          break;
        case BREAK_AT:
          enterOuterAlt(_localctx, 6);
          {
            setState(3228);
            match(BREAK_AT);
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final CallableReferenceContext callableReference() throws RecognitionException {
    CallableReferenceContext _localctx = new CallableReferenceContext(_ctx, getState());
    enterRule(_localctx, 270, RULE_callableReference);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3232);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << LPAREN)
                            | (1L << AT_NO_WS)
                            | (1L << AT_PRE_WS)
                            | (1L << FILE)
                            | (1L << FIELD)
                            | (1L << PROPERTY)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (GET - 64))
                            | (1L << (SET - 64))
                            | (1L << (RECEIVER - 64))
                            | (1L << (PARAM - 64))
                            | (1L << (SETPARAM - 64))
                            | (1L << (DELEGATE - 64))
                            | (1L << (IMPORT - 64))
                            | (1L << (CONSTRUCTOR - 64))
                            | (1L << (BY - 64))
                            | (1L << (COMPANION - 64))
                            | (1L << (INIT - 64))
                            | (1L << (WHERE - 64))
                            | (1L << (CATCH - 64))
                            | (1L << (FINALLY - 64))
                            | (1L << (OUT - 64))
                            | (1L << (DYNAMIC - 64))
                            | (1L << (PUBLIC - 64))
                            | (1L << (PRIVATE - 64))
                            | (1L << (PROTECTED - 64))
                            | (1L << (INTERNAL - 64))
                            | (1L << (ENUM - 64))
                            | (1L << (SEALED - 64))
                            | (1L << (ANNOTATION - 64))
                            | (1L << (DATA - 64))
                            | (1L << (INNER - 64))
                            | (1L << (VALUE - 64))
                            | (1L << (TAILREC - 64))
                            | (1L << (OPERATOR - 64))
                            | (1L << (INLINE - 64))
                            | (1L << (INFIX - 64))
                            | (1L << (EXTERNAL - 64))
                            | (1L << (SUSPEND - 64))
                            | (1L << (OVERRIDE - 64))
                            | (1L << (ABSTRACT - 64))
                            | (1L << (FINAL - 64))
                            | (1L << (OPEN - 64))
                            | (1L << (CONST - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (LATEINIT - 128))
                            | (1L << (VARARG - 128))
                            | (1L << (NOINLINE - 128))
                            | (1L << (CROSSINLINE - 128))
                            | (1L << (REIFIED - 128))
                            | (1L << (EXPECT - 128))
                            | (1L << (ACTUAL - 128))
                            | (1L << (Identifier - 128))))
                    != 0)) {
          {
            setState(3231);
            receiverType();
          }
        }

        setState(3234);
        match(COLONCOLON);
        setState(3238);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3235);
              match(NL);
            }
          }
          setState(3240);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3243);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case FILE:
          case FIELD:
          case PROPERTY:
          case GET:
          case SET:
          case RECEIVER:
          case PARAM:
          case SETPARAM:
          case DELEGATE:
          case IMPORT:
          case CONSTRUCTOR:
          case BY:
          case COMPANION:
          case INIT:
          case WHERE:
          case CATCH:
          case FINALLY:
          case OUT:
          case DYNAMIC:
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
          case OVERRIDE:
          case ABSTRACT:
          case FINAL:
          case OPEN:
          case CONST:
          case LATEINIT:
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
          case REIFIED:
          case EXPECT:
          case ACTUAL:
          case Identifier:
            {
              setState(3241);
              simpleIdentifier();
            }
            break;
          case CLASS:
            {
              setState(3242);
              match(CLASS);
            }
            break;
          default:
            throw new NoViableAltException(this);
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

  public final AssignmentAndOperatorContext assignmentAndOperator() throws RecognitionException {
    AssignmentAndOperatorContext _localctx = new AssignmentAndOperatorContext(_ctx, getState());
    enterRule(_localctx, 272, RULE_assignmentAndOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3245);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << ADD_ASSIGNMENT)
                        | (1L << SUB_ASSIGNMENT)
                        | (1L << MULT_ASSIGNMENT)
                        | (1L << DIV_ASSIGNMENT)
                        | (1L << MOD_ASSIGNMENT)))
                != 0))) {
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

  public final EqualityOperatorContext equalityOperator() throws RecognitionException {
    EqualityOperatorContext _localctx = new EqualityOperatorContext(_ctx, getState());
    enterRule(_localctx, 274, RULE_equalityOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3247);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << EXCL_EQ) | (1L << EXCL_EQEQ) | (1L << EQEQ) | (1L << EQEQEQ)))
                != 0))) {
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

  public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
    ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
    enterRule(_localctx, 276, RULE_comparisonOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3249);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << LANGLE) | (1L << RANGLE) | (1L << LE) | (1L << GE))) != 0))) {
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

  public final InOperatorContext inOperator() throws RecognitionException {
    InOperatorContext _localctx = new InOperatorContext(_ctx, getState());
    enterRule(_localctx, 278, RULE_inOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3251);
        _la = _input.LA(1);
        if (!(_la == IN || _la == NOT_IN)) {
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

  public final IsOperatorContext isOperator() throws RecognitionException {
    IsOperatorContext _localctx = new IsOperatorContext(_ctx, getState());
    enterRule(_localctx, 280, RULE_isOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3253);
        _la = _input.LA(1);
        if (!(_la == IS || _la == NOT_IS)) {
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

  public final AdditiveOperatorContext additiveOperator() throws RecognitionException {
    AdditiveOperatorContext _localctx = new AdditiveOperatorContext(_ctx, getState());
    enterRule(_localctx, 282, RULE_additiveOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3255);
        _la = _input.LA(1);
        if (!(_la == ADD || _la == SUB)) {
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

  public final MultiplicativeOperatorContext multiplicativeOperator() throws RecognitionException {
    MultiplicativeOperatorContext _localctx = new MultiplicativeOperatorContext(_ctx, getState());
    enterRule(_localctx, 284, RULE_multiplicativeOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3257);
        _la = _input.LA(1);
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << MULT) | (1L << MOD) | (1L << DIV))) != 0))) {
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

  public final AsOperatorContext asOperator() throws RecognitionException {
    AsOperatorContext _localctx = new AsOperatorContext(_ctx, getState());
    enterRule(_localctx, 286, RULE_asOperator);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3259);
        _la = _input.LA(1);
        if (!(_la == AS_SAFE || _la == AS)) {
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

  public final PrefixUnaryOperatorContext prefixUnaryOperator() throws RecognitionException {
    PrefixUnaryOperatorContext _localctx = new PrefixUnaryOperatorContext(_ctx, getState());
    enterRule(_localctx, 288, RULE_prefixUnaryOperator);
    try {
      setState(3266);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case INCR:
          enterOuterAlt(_localctx, 1);
          {
            setState(3261);
            match(INCR);
          }
          break;
        case DECR:
          enterOuterAlt(_localctx, 2);
          {
            setState(3262);
            match(DECR);
          }
          break;
        case SUB:
          enterOuterAlt(_localctx, 3);
          {
            setState(3263);
            match(SUB);
          }
          break;
        case ADD:
          enterOuterAlt(_localctx, 4);
          {
            setState(3264);
            match(ADD);
          }
          break;
        case EXCL_WS:
        case EXCL_NO_WS:
          enterOuterAlt(_localctx, 5);
          {
            setState(3265);
            excl();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final PostfixUnaryOperatorContext postfixUnaryOperator() throws RecognitionException {
    PostfixUnaryOperatorContext _localctx = new PostfixUnaryOperatorContext(_ctx, getState());
    enterRule(_localctx, 290, RULE_postfixUnaryOperator);
    try {
      setState(3272);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case INCR:
          enterOuterAlt(_localctx, 1);
          {
            setState(3268);
            match(INCR);
          }
          break;
        case DECR:
          enterOuterAlt(_localctx, 2);
          {
            setState(3269);
            match(DECR);
          }
          break;
        case EXCL_NO_WS:
          enterOuterAlt(_localctx, 3);
          {
            setState(3270);
            match(EXCL_NO_WS);
            setState(3271);
            excl();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final ExclContext excl() throws RecognitionException {
    ExclContext _localctx = new ExclContext(_ctx, getState());
    enterRule(_localctx, 292, RULE_excl);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3274);
        _la = _input.LA(1);
        if (!(_la == EXCL_WS || _la == EXCL_NO_WS)) {
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

  public final MemberAccessOperatorContext memberAccessOperator() throws RecognitionException {
    MemberAccessOperatorContext _localctx = new MemberAccessOperatorContext(_ctx, getState());
    enterRule(_localctx, 294, RULE_memberAccessOperator);
    int _la;
    try {
      setState(3291);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 505, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(3279);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3276);
                  match(NL);
                }
              }
              setState(3281);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3282);
            match(DOT);
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(3286);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3283);
                  match(NL);
                }
              }
              setState(3288);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
            setState(3289);
            safeNav();
          }
          break;
        case 3:
          enterOuterAlt(_localctx, 3);
          {
            setState(3290);
            match(COLONCOLON);
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

  public final SafeNavContext safeNav() throws RecognitionException {
    SafeNavContext _localctx = new SafeNavContext(_ctx, getState());
    enterRule(_localctx, 296, RULE_safeNav);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3293);
        match(QUEST_NO_WS);
        setState(3294);
        match(DOT);
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

  public final ModifiersContext modifiers() throws RecognitionException {
    ModifiersContext _localctx = new ModifiersContext(_ctx, getState());
    enterRule(_localctx, 298, RULE_modifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3298);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                setState(3298);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                  case AT_NO_WS:
                  case AT_PRE_WS:
                    {
                      setState(3296);
                      annotation();
                    }
                    break;
                  case PUBLIC:
                  case PRIVATE:
                  case PROTECTED:
                  case INTERNAL:
                  case ENUM:
                  case SEALED:
                  case ANNOTATION:
                  case DATA:
                  case INNER:
                  case VALUE:
                  case TAILREC:
                  case OPERATOR:
                  case INLINE:
                  case INFIX:
                  case EXTERNAL:
                  case SUSPEND:
                  case OVERRIDE:
                  case ABSTRACT:
                  case FINAL:
                  case OPEN:
                  case CONST:
                  case LATEINIT:
                  case VARARG:
                  case NOINLINE:
                  case CROSSINLINE:
                  case EXPECT:
                  case ACTUAL:
                    {
                      setState(3297);
                      modifier();
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
          setState(3300);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 507, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final ParameterModifiersContext parameterModifiers() throws RecognitionException {
    ParameterModifiersContext _localctx = new ParameterModifiersContext(_ctx, getState());
    enterRule(_localctx, 300, RULE_parameterModifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3304);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                setState(3304);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                  case AT_NO_WS:
                  case AT_PRE_WS:
                    {
                      setState(3302);
                      annotation();
                    }
                    break;
                  case VARARG:
                  case NOINLINE:
                  case CROSSINLINE:
                    {
                      setState(3303);
                      parameterModifier();
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
          setState(3306);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 509, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final ModifierContext modifier() throws RecognitionException {
    ModifierContext _localctx = new ModifierContext(_ctx, getState());
    enterRule(_localctx, 302, RULE_modifier);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3316);
        _errHandler.sync(this);
        switch (_input.LA(1)) {
          case ENUM:
          case SEALED:
          case ANNOTATION:
          case DATA:
          case INNER:
          case VALUE:
            {
              setState(3308);
              classModifier();
            }
            break;
          case OVERRIDE:
          case LATEINIT:
            {
              setState(3309);
              memberModifier();
            }
            break;
          case PUBLIC:
          case PRIVATE:
          case PROTECTED:
          case INTERNAL:
            {
              setState(3310);
              visibilityModifier();
            }
            break;
          case TAILREC:
          case OPERATOR:
          case INLINE:
          case INFIX:
          case EXTERNAL:
          case SUSPEND:
            {
              setState(3311);
              functionModifier();
            }
            break;
          case CONST:
            {
              setState(3312);
              propertyModifier();
            }
            break;
          case ABSTRACT:
          case FINAL:
          case OPEN:
            {
              setState(3313);
              inheritanceModifier();
            }
            break;
          case VARARG:
          case NOINLINE:
          case CROSSINLINE:
            {
              setState(3314);
              parameterModifier();
            }
            break;
          case EXPECT:
          case ACTUAL:
            {
              setState(3315);
              platformModifier();
            }
            break;
          default:
            throw new NoViableAltException(this);
        }
        setState(3321);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 511, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(3318);
                match(NL);
              }
            }
          }
          setState(3323);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 511, _ctx);
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

  public final TypeModifiersContext typeModifiers() throws RecognitionException {
    TypeModifiersContext _localctx = new TypeModifiersContext(_ctx, getState());
    enterRule(_localctx, 304, RULE_typeModifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3325);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(3324);
                  typeModifier();
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(3327);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 512, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final TypeModifierContext typeModifier() throws RecognitionException {
    TypeModifierContext _localctx = new TypeModifierContext(_ctx, getState());
    enterRule(_localctx, 306, RULE_typeModifier);
    int _la;
    try {
      setState(3337);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case AT_NO_WS:
        case AT_PRE_WS:
          enterOuterAlt(_localctx, 1);
          {
            setState(3329);
            annotation();
          }
          break;
        case SUSPEND:
          enterOuterAlt(_localctx, 2);
          {
            setState(3330);
            match(SUSPEND);
            setState(3334);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == NL) {
              {
                {
                  setState(3331);
                  match(NL);
                }
              }
              setState(3336);
              _errHandler.sync(this);
              _la = _input.LA(1);
            }
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final ClassModifierContext classModifier() throws RecognitionException {
    ClassModifierContext _localctx = new ClassModifierContext(_ctx, getState());
    enterRule(_localctx, 308, RULE_classModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3339);
        _la = _input.LA(1);
        if (!(((((_la - 111)) & ~0x3f) == 0
            && ((1L << (_la - 111))
                    & ((1L << (ENUM - 111))
                        | (1L << (SEALED - 111))
                        | (1L << (ANNOTATION - 111))
                        | (1L << (DATA - 111))
                        | (1L << (INNER - 111))
                        | (1L << (VALUE - 111))))
                != 0))) {
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

  public final MemberModifierContext memberModifier() throws RecognitionException {
    MemberModifierContext _localctx = new MemberModifierContext(_ctx, getState());
    enterRule(_localctx, 310, RULE_memberModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3341);
        _la = _input.LA(1);
        if (!(_la == OVERRIDE || _la == LATEINIT)) {
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

  public final VisibilityModifierContext visibilityModifier() throws RecognitionException {
    VisibilityModifierContext _localctx = new VisibilityModifierContext(_ctx, getState());
    enterRule(_localctx, 312, RULE_visibilityModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3343);
        _la = _input.LA(1);
        if (!(((((_la - 107)) & ~0x3f) == 0
            && ((1L << (_la - 107))
                    & ((1L << (PUBLIC - 107))
                        | (1L << (PRIVATE - 107))
                        | (1L << (PROTECTED - 107))
                        | (1L << (INTERNAL - 107))))
                != 0))) {
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

  public final VarianceModifierContext varianceModifier() throws RecognitionException {
    VarianceModifierContext _localctx = new VarianceModifierContext(_ctx, getState());
    enterRule(_localctx, 314, RULE_varianceModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3345);
        _la = _input.LA(1);
        if (!(_la == IN || _la == OUT)) {
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

  public final TypeParameterModifiersContext typeParameterModifiers() throws RecognitionException {
    TypeParameterModifiersContext _localctx = new TypeParameterModifiersContext(_ctx, getState());
    enterRule(_localctx, 316, RULE_typeParameterModifiers);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3348);
        _errHandler.sync(this);
        _alt = 1;
        do {
          switch (_alt) {
            case 1:
              {
                {
                  setState(3347);
                  typeParameterModifier();
                }
              }
              break;
            default:
              throw new NoViableAltException(this);
          }
          setState(3350);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 515, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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

  public final TypeParameterModifierContext typeParameterModifier() throws RecognitionException {
    TypeParameterModifierContext _localctx = new TypeParameterModifierContext(_ctx, getState());
    enterRule(_localctx, 318, RULE_typeParameterModifier);
    try {
      int _alt;
      setState(3367);
      _errHandler.sync(this);
      switch (_input.LA(1)) {
        case REIFIED:
          enterOuterAlt(_localctx, 1);
          {
            setState(3352);
            reificationModifier();
            setState(3356);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 516, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(3353);
                    match(NL);
                  }
                }
              }
              setState(3358);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 516, _ctx);
            }
          }
          break;
        case IN:
        case OUT:
          enterOuterAlt(_localctx, 2);
          {
            setState(3359);
            varianceModifier();
            setState(3363);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input, 517, _ctx);
            while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
              if (_alt == 1) {
                {
                  {
                    setState(3360);
                    match(NL);
                  }
                }
              }
              setState(3365);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 517, _ctx);
            }
          }
          break;
        case AT_NO_WS:
        case AT_PRE_WS:
          enterOuterAlt(_localctx, 3);
          {
            setState(3366);
            annotation();
          }
          break;
        default:
          throw new NoViableAltException(this);
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

  public final FunctionModifierContext functionModifier() throws RecognitionException {
    FunctionModifierContext _localctx = new FunctionModifierContext(_ctx, getState());
    enterRule(_localctx, 320, RULE_functionModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3369);
        _la = _input.LA(1);
        if (!(((((_la - 117)) & ~0x3f) == 0
            && ((1L << (_la - 117))
                    & ((1L << (TAILREC - 117))
                        | (1L << (OPERATOR - 117))
                        | (1L << (INLINE - 117))
                        | (1L << (INFIX - 117))
                        | (1L << (EXTERNAL - 117))
                        | (1L << (SUSPEND - 117))))
                != 0))) {
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

  public final PropertyModifierContext propertyModifier() throws RecognitionException {
    PropertyModifierContext _localctx = new PropertyModifierContext(_ctx, getState());
    enterRule(_localctx, 322, RULE_propertyModifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3371);
        match(CONST);
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

  public final InheritanceModifierContext inheritanceModifier() throws RecognitionException {
    InheritanceModifierContext _localctx = new InheritanceModifierContext(_ctx, getState());
    enterRule(_localctx, 324, RULE_inheritanceModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3373);
        _la = _input.LA(1);
        if (!(((((_la - 124)) & ~0x3f) == 0
            && ((1L << (_la - 124))
                    & ((1L << (ABSTRACT - 124)) | (1L << (FINAL - 124)) | (1L << (OPEN - 124))))
                != 0))) {
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

  public final ParameterModifierContext parameterModifier() throws RecognitionException {
    ParameterModifierContext _localctx = new ParameterModifierContext(_ctx, getState());
    enterRule(_localctx, 326, RULE_parameterModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3375);
        _la = _input.LA(1);
        if (!(((((_la - 129)) & ~0x3f) == 0
            && ((1L << (_la - 129))
                    & ((1L << (VARARG - 129))
                        | (1L << (NOINLINE - 129))
                        | (1L << (CROSSINLINE - 129))))
                != 0))) {
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

  public final ReificationModifierContext reificationModifier() throws RecognitionException {
    ReificationModifierContext _localctx = new ReificationModifierContext(_ctx, getState());
    enterRule(_localctx, 328, RULE_reificationModifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3377);
        match(REIFIED);
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

  public final PlatformModifierContext platformModifier() throws RecognitionException {
    PlatformModifierContext _localctx = new PlatformModifierContext(_ctx, getState());
    enterRule(_localctx, 330, RULE_platformModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3379);
        _la = _input.LA(1);
        if (!(_la == EXPECT || _la == ACTUAL)) {
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

  public final AnnotationContext annotation() throws RecognitionException {
    AnnotationContext _localctx = new AnnotationContext(_ctx, getState());
    enterRule(_localctx, 332, RULE_annotation);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3383);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 519, _ctx)) {
          case 1:
            {
              setState(3381);
              singleAnnotation();
            }
            break;
          case 2:
            {
              setState(3382);
              multiAnnotation();
            }
            break;
        }
        setState(3388);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 520, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(3385);
                match(NL);
              }
            }
          }
          setState(3390);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 520, _ctx);
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

  public final SingleAnnotationContext singleAnnotation() throws RecognitionException {
    SingleAnnotationContext _localctx = new SingleAnnotationContext(_ctx, getState());
    enterRule(_localctx, 334, RULE_singleAnnotation);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3400);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 522, _ctx)) {
          case 1:
            {
              setState(3391);
              annotationUseSiteTarget();
              setState(3395);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(3392);
                    match(NL);
                  }
                }
                setState(3397);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
          case 2:
            {
              setState(3398);
              match(AT_NO_WS);
            }
            break;
          case 3:
            {
              setState(3399);
              match(AT_PRE_WS);
            }
            break;
        }
        setState(3402);
        unescapedAnnotation();
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

  public final MultiAnnotationContext multiAnnotation() throws RecognitionException {
    MultiAnnotationContext _localctx = new MultiAnnotationContext(_ctx, getState());
    enterRule(_localctx, 336, RULE_multiAnnotation);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3413);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 524, _ctx)) {
          case 1:
            {
              setState(3404);
              annotationUseSiteTarget();
              setState(3408);
              _errHandler.sync(this);
              _la = _input.LA(1);
              while (_la == NL) {
                {
                  {
                    setState(3405);
                    match(NL);
                  }
                }
                setState(3410);
                _errHandler.sync(this);
                _la = _input.LA(1);
              }
            }
            break;
          case 2:
            {
              setState(3411);
              match(AT_NO_WS);
            }
            break;
          case 3:
            {
              setState(3412);
              match(AT_PRE_WS);
            }
            break;
        }
        setState(3415);
        match(LSQUARE);
        setState(3417);
        _errHandler.sync(this);
        _la = _input.LA(1);
        do {
          {
            {
              setState(3416);
              unescapedAnnotation();
            }
          }
          setState(3419);
          _errHandler.sync(this);
          _la = _input.LA(1);
        } while (((((_la - 61)) & ~0x3f) == 0
                && ((1L << (_la - 61))
                        & ((1L << (FILE - 61))
                            | (1L << (FIELD - 61))
                            | (1L << (PROPERTY - 61))
                            | (1L << (GET - 61))
                            | (1L << (SET - 61))
                            | (1L << (RECEIVER - 61))
                            | (1L << (PARAM - 61))
                            | (1L << (SETPARAM - 61))
                            | (1L << (DELEGATE - 61))
                            | (1L << (IMPORT - 61))
                            | (1L << (CONSTRUCTOR - 61))
                            | (1L << (BY - 61))
                            | (1L << (COMPANION - 61))
                            | (1L << (INIT - 61))
                            | (1L << (WHERE - 61))
                            | (1L << (CATCH - 61))
                            | (1L << (FINALLY - 61))
                            | (1L << (OUT - 61))
                            | (1L << (DYNAMIC - 61))
                            | (1L << (PUBLIC - 61))
                            | (1L << (PRIVATE - 61))
                            | (1L << (PROTECTED - 61))
                            | (1L << (INTERNAL - 61))
                            | (1L << (ENUM - 61))
                            | (1L << (SEALED - 61))
                            | (1L << (ANNOTATION - 61))
                            | (1L << (DATA - 61))
                            | (1L << (INNER - 61))
                            | (1L << (VALUE - 61))
                            | (1L << (TAILREC - 61))
                            | (1L << (OPERATOR - 61))
                            | (1L << (INLINE - 61))
                            | (1L << (INFIX - 61))
                            | (1L << (EXTERNAL - 61))
                            | (1L << (SUSPEND - 61))
                            | (1L << (OVERRIDE - 61))
                            | (1L << (ABSTRACT - 61))))
                    != 0)
            || ((((_la - 125)) & ~0x3f) == 0
                && ((1L << (_la - 125))
                        & ((1L << (FINAL - 125))
                            | (1L << (OPEN - 125))
                            | (1L << (CONST - 125))
                            | (1L << (LATEINIT - 125))
                            | (1L << (VARARG - 125))
                            | (1L << (NOINLINE - 125))
                            | (1L << (CROSSINLINE - 125))
                            | (1L << (REIFIED - 125))
                            | (1L << (EXPECT - 125))
                            | (1L << (ACTUAL - 125))
                            | (1L << (Identifier - 125))))
                    != 0));
        setState(3421);
        match(RSQUARE);
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

  public final AnnotationUseSiteTargetContext annotationUseSiteTarget()
      throws RecognitionException {
    AnnotationUseSiteTargetContext _localctx = new AnnotationUseSiteTargetContext(_ctx, getState());
    enterRule(_localctx, 338, RULE_annotationUseSiteTarget);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3423);
        _la = _input.LA(1);
        if (!(_la == AT_NO_WS || _la == AT_PRE_WS)) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
        setState(3424);
        _la = _input.LA(1);
        if (!(((((_la - 62)) & ~0x3f) == 0
            && ((1L << (_la - 62))
                    & ((1L << (FIELD - 62))
                        | (1L << (PROPERTY - 62))
                        | (1L << (GET - 62))
                        | (1L << (SET - 62))
                        | (1L << (RECEIVER - 62))
                        | (1L << (PARAM - 62))
                        | (1L << (SETPARAM - 62))
                        | (1L << (DELEGATE - 62))))
                != 0))) {
          _errHandler.recoverInline(this);
        } else {
          if (_input.LA(1) == Token.EOF) matchedEOF = true;
          _errHandler.reportMatch(this);
          consume();
        }
        setState(3428);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == NL) {
          {
            {
              setState(3425);
              match(NL);
            }
          }
          setState(3430);
          _errHandler.sync(this);
          _la = _input.LA(1);
        }
        setState(3431);
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

  public final UnescapedAnnotationContext unescapedAnnotation() throws RecognitionException {
    UnescapedAnnotationContext _localctx = new UnescapedAnnotationContext(_ctx, getState());
    enterRule(_localctx, 340, RULE_unescapedAnnotation);
    try {
      setState(3435);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 527, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(3433);
            constructorInvocation();
          }
          break;
        case 2:
          enterOuterAlt(_localctx, 2);
          {
            setState(3434);
            userType();
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

  public final SimpleIdentifierContext simpleIdentifier() throws RecognitionException {
    SimpleIdentifierContext _localctx = new SimpleIdentifierContext(_ctx, getState());
    enterRule(_localctx, 342, RULE_simpleIdentifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(3437);
        _la = _input.LA(1);
        if (!(((((_la - 61)) & ~0x3f) == 0
                && ((1L << (_la - 61))
                        & ((1L << (FILE - 61))
                            | (1L << (FIELD - 61))
                            | (1L << (PROPERTY - 61))
                            | (1L << (GET - 61))
                            | (1L << (SET - 61))
                            | (1L << (RECEIVER - 61))
                            | (1L << (PARAM - 61))
                            | (1L << (SETPARAM - 61))
                            | (1L << (DELEGATE - 61))
                            | (1L << (IMPORT - 61))
                            | (1L << (CONSTRUCTOR - 61))
                            | (1L << (BY - 61))
                            | (1L << (COMPANION - 61))
                            | (1L << (INIT - 61))
                            | (1L << (WHERE - 61))
                            | (1L << (CATCH - 61))
                            | (1L << (FINALLY - 61))
                            | (1L << (OUT - 61))
                            | (1L << (DYNAMIC - 61))
                            | (1L << (PUBLIC - 61))
                            | (1L << (PRIVATE - 61))
                            | (1L << (PROTECTED - 61))
                            | (1L << (INTERNAL - 61))
                            | (1L << (ENUM - 61))
                            | (1L << (SEALED - 61))
                            | (1L << (ANNOTATION - 61))
                            | (1L << (DATA - 61))
                            | (1L << (INNER - 61))
                            | (1L << (VALUE - 61))
                            | (1L << (TAILREC - 61))
                            | (1L << (OPERATOR - 61))
                            | (1L << (INLINE - 61))
                            | (1L << (INFIX - 61))
                            | (1L << (EXTERNAL - 61))
                            | (1L << (SUSPEND - 61))
                            | (1L << (OVERRIDE - 61))
                            | (1L << (ABSTRACT - 61))))
                    != 0)
            || ((((_la - 125)) & ~0x3f) == 0
                && ((1L << (_la - 125))
                        & ((1L << (FINAL - 125))
                            | (1L << (OPEN - 125))
                            | (1L << (CONST - 125))
                            | (1L << (LATEINIT - 125))
                            | (1L << (VARARG - 125))
                            | (1L << (NOINLINE - 125))
                            | (1L << (CROSSINLINE - 125))
                            | (1L << (REIFIED - 125))
                            | (1L << (EXPECT - 125))
                            | (1L << (ACTUAL - 125))
                            | (1L << (Identifier - 125))))
                    != 0))) {
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

  public final IdentifierContext identifier() throws RecognitionException {
    IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
    enterRule(_localctx, 344, RULE_identifier);
    int _la;
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(3439);
        simpleIdentifier();
        setState(3450);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 529, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(3443);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == NL) {
                  {
                    {
                      setState(3440);
                      match(NL);
                    }
                  }
                  setState(3445);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                }
                setState(3446);
                match(DOT);
                setState(3447);
                simpleIdentifier();
              }
            }
          }
          setState(3452);
          _errHandler.sync(this);
          _alt = getInterpreter().adaptivePredict(_input, 529, _ctx);
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

  public static class KotlinFileContext extends ParserRuleContext {
    public KotlinFileContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PackageHeaderContext packageHeader() {
      return getRuleContext(PackageHeaderContext.class, 0);
    }

    public ImportListContext importList() {
      return getRuleContext(ImportListContext.class, 0);
    }

    public TerminalNode EOF() {
      return getToken(KotlinParser.EOF, 0);
    }

    public ShebangLineContext shebangLine() {
      return getRuleContext(ShebangLineContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<FileAnnotationContext> fileAnnotation() {
      return getRuleContexts(FileAnnotationContext.class);
    }

    public FileAnnotationContext fileAnnotation(int i) {
      return getRuleContext(FileAnnotationContext.class, i);
    }

    public List<TopLevelObjectContext> topLevelObject() {
      return getRuleContexts(TopLevelObjectContext.class);
    }

    public TopLevelObjectContext topLevelObject(int i) {
      return getRuleContext(TopLevelObjectContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_kotlinFile;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitKotlinFile(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterKotlinFile(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitKotlinFile(this);
    }
  }

  public static class ScriptContext extends ParserRuleContext {
    public ScriptContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PackageHeaderContext packageHeader() {
      return getRuleContext(PackageHeaderContext.class, 0);
    }

    public ImportListContext importList() {
      return getRuleContext(ImportListContext.class, 0);
    }

    public TerminalNode EOF() {
      return getToken(KotlinParser.EOF, 0);
    }

    public ShebangLineContext shebangLine() {
      return getRuleContext(ShebangLineContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<FileAnnotationContext> fileAnnotation() {
      return getRuleContexts(FileAnnotationContext.class);
    }

    public FileAnnotationContext fileAnnotation(int i) {
      return getRuleContext(FileAnnotationContext.class, i);
    }

    public List<StatementContext> statement() {
      return getRuleContexts(StatementContext.class);
    }

    public StatementContext statement(int i) {
      return getRuleContext(StatementContext.class, i);
    }

    public List<SemiContext> semi() {
      return getRuleContexts(SemiContext.class);
    }

    public SemiContext semi(int i) {
      return getRuleContext(SemiContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_script;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitScript(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterScript(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitScript(this);
    }
  }

  public static class ShebangLineContext extends ParserRuleContext {
    public ShebangLineContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode ShebangLine() {
      return getToken(KotlinParser.ShebangLine, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_shebangLine;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitShebangLine(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterShebangLine(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitShebangLine(this);
    }
  }

  public static class FileAnnotationContext extends ParserRuleContext {
    public FileAnnotationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode FILE() {
      return getToken(KotlinParser.FILE, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TerminalNode AT_NO_WS() {
      return getToken(KotlinParser.AT_NO_WS, 0);
    }

    public TerminalNode AT_PRE_WS() {
      return getToken(KotlinParser.AT_PRE_WS, 0);
    }

    public TerminalNode LSQUARE() {
      return getToken(KotlinParser.LSQUARE, 0);
    }

    public TerminalNode RSQUARE() {
      return getToken(KotlinParser.RSQUARE, 0);
    }

    public List<UnescapedAnnotationContext> unescapedAnnotation() {
      return getRuleContexts(UnescapedAnnotationContext.class);
    }

    public UnescapedAnnotationContext unescapedAnnotation(int i) {
      return getRuleContext(UnescapedAnnotationContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_fileAnnotation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFileAnnotation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFileAnnotation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFileAnnotation(this);
    }
  }

  public static class PackageHeaderContext extends ParserRuleContext {
    public PackageHeaderContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode PACKAGE() {
      return getToken(KotlinParser.PACKAGE, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public SemiContext semi() {
      return getRuleContext(SemiContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_packageHeader;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPackageHeader(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPackageHeader(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPackageHeader(this);
    }
  }

  public static class ImportListContext extends ParserRuleContext {
    public ImportListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ImportHeaderContext> importHeader() {
      return getRuleContexts(ImportHeaderContext.class);
    }

    public ImportHeaderContext importHeader(int i) {
      return getRuleContext(ImportHeaderContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_importList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitImportList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterImportList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitImportList(this);
    }
  }

  public static class ImportHeaderContext extends ParserRuleContext {
    public ImportHeaderContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode IMPORT() {
      return getToken(KotlinParser.IMPORT, 0);
    }

    public IdentifierContext identifier() {
      return getRuleContext(IdentifierContext.class, 0);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    public TerminalNode MULT() {
      return getToken(KotlinParser.MULT, 0);
    }

    public ImportAliasContext importAlias() {
      return getRuleContext(ImportAliasContext.class, 0);
    }

    public SemiContext semi() {
      return getRuleContext(SemiContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_importHeader;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitImportHeader(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterImportHeader(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitImportHeader(this);
    }
  }

  public static class ImportAliasContext extends ParserRuleContext {
    public ImportAliasContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode AS() {
      return getToken(KotlinParser.AS, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_importAlias;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitImportAlias(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterImportAlias(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitImportAlias(this);
    }
  }

  public static class TopLevelObjectContext extends ParserRuleContext {
    public TopLevelObjectContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    public SemisContext semis() {
      return getRuleContext(SemisContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_topLevelObject;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTopLevelObject(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTopLevelObject(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTopLevelObject(this);
    }
  }

  public static class TypeAliasContext extends ParserRuleContext {
    public TypeAliasContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TYPE_ALIAS() {
      return getToken(KotlinParser.TYPE_ALIAS, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeAlias;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeAlias(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeAlias(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeAlias(this);
    }
  }

  public static class DeclarationContext extends ParserRuleContext {
    public DeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassDeclarationContext classDeclaration() {
      return getRuleContext(ClassDeclarationContext.class, 0);
    }

    public ObjectDeclarationContext objectDeclaration() {
      return getRuleContext(ObjectDeclarationContext.class, 0);
    }

    public FunctionDeclarationContext functionDeclaration() {
      return getRuleContext(FunctionDeclarationContext.class, 0);
    }

    public PropertyDeclarationContext propertyDeclaration() {
      return getRuleContext(PropertyDeclarationContext.class, 0);
    }

    public TypeAliasContext typeAlias() {
      return getRuleContext(TypeAliasContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitDeclaration(this);
    }
  }

  public static class ClassDeclarationContext extends ParserRuleContext {
    public ClassDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode CLASS() {
      return getToken(KotlinParser.CLASS, 0);
    }

    public TerminalNode INTERFACE() {
      return getToken(KotlinParser.INTERFACE, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public PrimaryConstructorContext primaryConstructor() {
      return getRuleContext(PrimaryConstructorContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public DelegationSpecifiersContext delegationSpecifiers() {
      return getRuleContext(DelegationSpecifiersContext.class, 0);
    }

    public TypeConstraintsContext typeConstraints() {
      return getRuleContext(TypeConstraintsContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    public EnumClassBodyContext enumClassBody() {
      return getRuleContext(EnumClassBodyContext.class, 0);
    }

    public TerminalNode FUN() {
      return getToken(KotlinParser.FUN, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassDeclaration(this);
    }
  }

  public static class PrimaryConstructorContext extends ParserRuleContext {
    public PrimaryConstructorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassParametersContext classParameters() {
      return getRuleContext(ClassParametersContext.class, 0);
    }

    public TerminalNode CONSTRUCTOR() {
      return getToken(KotlinParser.CONSTRUCTOR, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_primaryConstructor;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPrimaryConstructor(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPrimaryConstructor(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPrimaryConstructor(this);
    }
  }

  public static class ClassBodyContext extends ParserRuleContext {
    public ClassBodyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LCURL() {
      return getToken(KotlinParser.LCURL, 0);
    }

    public ClassMemberDeclarationsContext classMemberDeclarations() {
      return getRuleContext(ClassMemberDeclarationsContext.class, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classBody;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassBody(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassBody(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassBody(this);
    }
  }

  public static class ClassParametersContext extends ParserRuleContext {
    public ClassParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<ClassParameterContext> classParameter() {
      return getRuleContexts(ClassParameterContext.class);
    }

    public ClassParameterContext classParameter(int i) {
      return getRuleContext(ClassParameterContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classParameters;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassParameters(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassParameters(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassParameters(this);
    }
  }

  public static class ClassParameterContext extends ParserRuleContext {
    public ClassParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode VAL() {
      return getToken(KotlinParser.VAL, 0);
    }

    public TerminalNode VAR() {
      return getToken(KotlinParser.VAR, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classParameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassParameter(this);
    }
  }

  public static class DelegationSpecifiersContext extends ParserRuleContext {
    public DelegationSpecifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AnnotatedDelegationSpecifierContext> annotatedDelegationSpecifier() {
      return getRuleContexts(AnnotatedDelegationSpecifierContext.class);
    }

    public AnnotatedDelegationSpecifierContext annotatedDelegationSpecifier(int i) {
      return getRuleContext(AnnotatedDelegationSpecifierContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_delegationSpecifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitDelegationSpecifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterDelegationSpecifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitDelegationSpecifiers(this);
    }
  }

  public static class DelegationSpecifierContext extends ParserRuleContext {
    public DelegationSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ConstructorInvocationContext constructorInvocation() {
      return getRuleContext(ConstructorInvocationContext.class, 0);
    }

    public ExplicitDelegationContext explicitDelegation() {
      return getRuleContext(ExplicitDelegationContext.class, 0);
    }

    public UserTypeContext userType() {
      return getRuleContext(UserTypeContext.class, 0);
    }

    public FunctionTypeContext functionType() {
      return getRuleContext(FunctionTypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_delegationSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitDelegationSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterDelegationSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitDelegationSpecifier(this);
    }
  }

  public static class ConstructorInvocationContext extends ParserRuleContext {
    public ConstructorInvocationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public UserTypeContext userType() {
      return getRuleContext(UserTypeContext.class, 0);
    }

    public ValueArgumentsContext valueArguments() {
      return getRuleContext(ValueArgumentsContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constructorInvocation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitConstructorInvocation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterConstructorInvocation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitConstructorInvocation(this);
    }
  }

  public static class AnnotatedDelegationSpecifierContext extends ParserRuleContext {
    public AnnotatedDelegationSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DelegationSpecifierContext delegationSpecifier() {
      return getRuleContext(DelegationSpecifierContext.class, 0);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotatedDelegationSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAnnotatedDelegationSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAnnotatedDelegationSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAnnotatedDelegationSpecifier(this);
    }
  }

  public static class ExplicitDelegationContext extends ParserRuleContext {
    public ExplicitDelegationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode BY() {
      return getToken(KotlinParser.BY, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public UserTypeContext userType() {
      return getRuleContext(UserTypeContext.class, 0);
    }

    public FunctionTypeContext functionType() {
      return getRuleContext(FunctionTypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_explicitDelegation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitExplicitDelegation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterExplicitDelegation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitExplicitDelegation(this);
    }
  }

  public static class TypeParametersContext extends ParserRuleContext {
    public TypeParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LANGLE() {
      return getToken(KotlinParser.LANGLE, 0);
    }

    public List<TypeParameterContext> typeParameter() {
      return getRuleContexts(TypeParameterContext.class);
    }

    public TypeParameterContext typeParameter(int i) {
      return getRuleContext(TypeParameterContext.class, i);
    }

    public TerminalNode RANGLE() {
      return getToken(KotlinParser.RANGLE, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameters;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeParameters(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeParameters(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeParameters(this);
    }
  }

  public static class TypeParameterContext extends ParserRuleContext {
    public TypeParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TypeParameterModifiersContext typeParameterModifiers() {
      return getRuleContext(TypeParameterModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeParameter(this);
    }
  }

  public static class TypeConstraintsContext extends ParserRuleContext {
    public TypeConstraintsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode WHERE() {
      return getToken(KotlinParser.WHERE, 0);
    }

    public List<TypeConstraintContext> typeConstraint() {
      return getRuleContexts(TypeConstraintContext.class);
    }

    public TypeConstraintContext typeConstraint(int i) {
      return getRuleContext(TypeConstraintContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeConstraints;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeConstraints(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeConstraints(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeConstraints(this);
    }
  }

  public static class TypeConstraintContext extends ParserRuleContext {
    public TypeConstraintContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeConstraint;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeConstraint(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeConstraint(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeConstraint(this);
    }
  }

  public static class ClassMemberDeclarationsContext extends ParserRuleContext {
    public ClassMemberDeclarationsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ClassMemberDeclarationContext> classMemberDeclaration() {
      return getRuleContexts(ClassMemberDeclarationContext.class);
    }

    public ClassMemberDeclarationContext classMemberDeclaration(int i) {
      return getRuleContext(ClassMemberDeclarationContext.class, i);
    }

    public List<SemisContext> semis() {
      return getRuleContexts(SemisContext.class);
    }

    public SemisContext semis(int i) {
      return getRuleContext(SemisContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classMemberDeclarations;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassMemberDeclarations(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassMemberDeclarations(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassMemberDeclarations(this);
    }
  }

  public static class ClassMemberDeclarationContext extends ParserRuleContext {
    public ClassMemberDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    public CompanionObjectContext companionObject() {
      return getRuleContext(CompanionObjectContext.class, 0);
    }

    public AnonymousInitializerContext anonymousInitializer() {
      return getRuleContext(AnonymousInitializerContext.class, 0);
    }

    public SecondaryConstructorContext secondaryConstructor() {
      return getRuleContext(SecondaryConstructorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classMemberDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassMemberDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassMemberDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassMemberDeclaration(this);
    }
  }

  public static class AnonymousInitializerContext extends ParserRuleContext {
    public AnonymousInitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode INIT() {
      return getToken(KotlinParser.INIT, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_anonymousInitializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAnonymousInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAnonymousInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAnonymousInitializer(this);
    }
  }

  public static class CompanionObjectContext extends ParserRuleContext {
    public CompanionObjectContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode COMPANION() {
      return getToken(KotlinParser.COMPANION, 0);
    }

    public TerminalNode OBJECT() {
      return getToken(KotlinParser.OBJECT, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public DelegationSpecifiersContext delegationSpecifiers() {
      return getRuleContext(DelegationSpecifiersContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_companionObject;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitCompanionObject(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterCompanionObject(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitCompanionObject(this);
    }
  }

  public static class FunctionValueParametersContext extends ParserRuleContext {
    public FunctionValueParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<FunctionValueParameterContext> functionValueParameter() {
      return getRuleContexts(FunctionValueParameterContext.class);
    }

    public FunctionValueParameterContext functionValueParameter(int i) {
      return getRuleContext(FunctionValueParameterContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionValueParameters;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionValueParameters(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionValueParameters(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionValueParameters(this);
    }
  }

  public static class FunctionValueParameterContext extends ParserRuleContext {
    public FunctionValueParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParameterContext parameter() {
      return getRuleContext(ParameterContext.class, 0);
    }

    public ParameterModifiersContext parameterModifiers() {
      return getRuleContext(ParameterModifiersContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionValueParameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionValueParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionValueParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionValueParameter(this);
    }
  }

  public static class FunctionDeclarationContext extends ParserRuleContext {
    public FunctionDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode FUN() {
      return getToken(KotlinParser.FUN, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public FunctionValueParametersContext functionValueParameters() {
      return getRuleContext(FunctionValueParametersContext.class, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public ReceiverTypeContext receiverType() {
      return getRuleContext(ReceiverTypeContext.class, 0);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TypeConstraintsContext typeConstraints() {
      return getRuleContext(TypeConstraintsContext.class, 0);
    }

    public FunctionBodyContext functionBody() {
      return getRuleContext(FunctionBodyContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionDeclaration(this);
    }
  }

  public static class FunctionBodyContext extends ParserRuleContext {
    public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionBody;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionBody(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionBody(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionBody(this);
    }
  }

  public static class VariableDeclarationContext extends ParserRuleContext {
    public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_variableDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitVariableDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterVariableDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitVariableDeclaration(this);
    }
  }

  public static class MultiVariableDeclarationContext extends ParserRuleContext {
    public MultiVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public List<VariableDeclarationContext> variableDeclaration() {
      return getRuleContexts(VariableDeclarationContext.class);
    }

    public VariableDeclarationContext variableDeclaration(int i) {
      return getRuleContext(VariableDeclarationContext.class, i);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiVariableDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiVariableDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiVariableDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiVariableDeclaration(this);
    }
  }

  public static class PropertyDeclarationContext extends ParserRuleContext {
    public PropertyDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode VAL() {
      return getToken(KotlinParser.VAL, 0);
    }

    public TerminalNode VAR() {
      return getToken(KotlinParser.VAR, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public TypeParametersContext typeParameters() {
      return getRuleContext(TypeParametersContext.class, 0);
    }

    public ReceiverTypeContext receiverType() {
      return getRuleContext(ReceiverTypeContext.class, 0);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    public TypeConstraintsContext typeConstraints() {
      return getRuleContext(TypeConstraintsContext.class, 0);
    }

    public TerminalNode SEMICOLON() {
      return getToken(KotlinParser.SEMICOLON, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public MultiVariableDeclarationContext multiVariableDeclaration() {
      return getRuleContext(MultiVariableDeclarationContext.class, 0);
    }

    public VariableDeclarationContext variableDeclaration() {
      return getRuleContext(VariableDeclarationContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public PropertyDelegateContext propertyDelegate() {
      return getRuleContext(PropertyDelegateContext.class, 0);
    }

    public GetterContext getter() {
      return getRuleContext(GetterContext.class, 0);
    }

    public SetterContext setter() {
      return getRuleContext(SetterContext.class, 0);
    }

    public SemiContext semi() {
      return getRuleContext(SemiContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPropertyDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPropertyDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPropertyDeclaration(this);
    }
  }

  public static class PropertyDelegateContext extends ParserRuleContext {
    public PropertyDelegateContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode BY() {
      return getToken(KotlinParser.BY, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyDelegate;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPropertyDelegate(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPropertyDelegate(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPropertyDelegate(this);
    }
  }

  public static class GetterContext extends ParserRuleContext {
    public GetterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode GET() {
      return getToken(KotlinParser.GET, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public FunctionBodyContext functionBody() {
      return getRuleContext(FunctionBodyContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_getter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitGetter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterGetter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitGetter(this);
    }
  }

  public static class SetterContext extends ParserRuleContext {
    public SetterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode SET() {
      return getToken(KotlinParser.SET, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public FunctionValueParameterWithOptionalTypeContext functionValueParameterWithOptionalType() {
      return getRuleContext(FunctionValueParameterWithOptionalTypeContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public FunctionBodyContext functionBody() {
      return getRuleContext(FunctionBodyContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COMMA() {
      return getToken(KotlinParser.COMMA, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_setter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSetter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSetter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSetter(this);
    }
  }

  public static class ParametersWithOptionalTypeContext extends ParserRuleContext {
    public ParametersWithOptionalTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<FunctionValueParameterWithOptionalTypeContext>
        functionValueParameterWithOptionalType() {
      return getRuleContexts(FunctionValueParameterWithOptionalTypeContext.class);
    }

    public FunctionValueParameterWithOptionalTypeContext functionValueParameterWithOptionalType(
        int i) {
      return getRuleContext(FunctionValueParameterWithOptionalTypeContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parametersWithOptionalType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParametersWithOptionalType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParametersWithOptionalType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParametersWithOptionalType(this);
    }
  }

  public static class FunctionValueParameterWithOptionalTypeContext extends ParserRuleContext {
    public FunctionValueParameterWithOptionalTypeContext(
        ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParameterWithOptionalTypeContext parameterWithOptionalType() {
      return getRuleContext(ParameterWithOptionalTypeContext.class, 0);
    }

    public ParameterModifiersContext parameterModifiers() {
      return getRuleContext(ParameterModifiersContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionValueParameterWithOptionalType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor)
            .visitFunctionValueParameterWithOptionalType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionValueParameterWithOptionalType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionValueParameterWithOptionalType(this);
    }
  }

  public static class ParameterWithOptionalTypeContext extends ParserRuleContext {
    public ParameterWithOptionalTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameterWithOptionalType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParameterWithOptionalType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParameterWithOptionalType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParameterWithOptionalType(this);
    }
  }

  public static class ParameterContext extends ParserRuleContext {
    public ParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParameter(this);
    }
  }

  public static class ObjectDeclarationContext extends ParserRuleContext {
    public ObjectDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode OBJECT() {
      return getToken(KotlinParser.OBJECT, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public DelegationSpecifiersContext delegationSpecifiers() {
      return getRuleContext(DelegationSpecifiersContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_objectDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitObjectDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterObjectDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitObjectDeclaration(this);
    }
  }

  public static class SecondaryConstructorContext extends ParserRuleContext {
    public SecondaryConstructorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode CONSTRUCTOR() {
      return getToken(KotlinParser.CONSTRUCTOR, 0);
    }

    public FunctionValueParametersContext functionValueParameters() {
      return getRuleContext(FunctionValueParametersContext.class, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public ConstructorDelegationCallContext constructorDelegationCall() {
      return getRuleContext(ConstructorDelegationCallContext.class, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_secondaryConstructor;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSecondaryConstructor(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSecondaryConstructor(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSecondaryConstructor(this);
    }
  }

  public static class ConstructorDelegationCallContext extends ParserRuleContext {
    public ConstructorDelegationCallContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ValueArgumentsContext valueArguments() {
      return getRuleContext(ValueArgumentsContext.class, 0);
    }

    public TerminalNode THIS() {
      return getToken(KotlinParser.THIS, 0);
    }

    public TerminalNode SUPER() {
      return getToken(KotlinParser.SUPER, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constructorDelegationCall;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitConstructorDelegationCall(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterConstructorDelegationCall(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitConstructorDelegationCall(this);
    }
  }

  public static class EnumClassBodyContext extends ParserRuleContext {
    public EnumClassBodyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LCURL() {
      return getToken(KotlinParser.LCURL, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public EnumEntriesContext enumEntries() {
      return getRuleContext(EnumEntriesContext.class, 0);
    }

    public TerminalNode SEMICOLON() {
      return getToken(KotlinParser.SEMICOLON, 0);
    }

    public ClassMemberDeclarationsContext classMemberDeclarations() {
      return getRuleContext(ClassMemberDeclarationsContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumClassBody;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitEnumClassBody(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterEnumClassBody(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitEnumClassBody(this);
    }
  }

  public static class EnumEntriesContext extends ParserRuleContext {
    public EnumEntriesContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<EnumEntryContext> enumEntry() {
      return getRuleContexts(EnumEntryContext.class);
    }

    public EnumEntryContext enumEntry(int i) {
      return getRuleContext(EnumEntryContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumEntries;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitEnumEntries(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterEnumEntries(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitEnumEntries(this);
    }
  }

  public static class EnumEntryContext extends ParserRuleContext {
    public EnumEntryContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public ModifiersContext modifiers() {
      return getRuleContext(ModifiersContext.class, 0);
    }

    public ValueArgumentsContext valueArguments() {
      return getRuleContext(ValueArgumentsContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumEntry;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitEnumEntry(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterEnumEntry(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitEnumEntry(this);
    }
  }

  public static class TypeContext extends ParserRuleContext {
    public TypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParenthesizedTypeContext parenthesizedType() {
      return getRuleContext(ParenthesizedTypeContext.class, 0);
    }

    public NullableTypeContext nullableType() {
      return getRuleContext(NullableTypeContext.class, 0);
    }

    public TypeReferenceContext typeReference() {
      return getRuleContext(TypeReferenceContext.class, 0);
    }

    public FunctionTypeContext functionType() {
      return getRuleContext(FunctionTypeContext.class, 0);
    }

    public TypeModifiersContext typeModifiers() {
      return getRuleContext(TypeModifiersContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_type;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitType(this);
    }
  }

  public static class TypeReferenceContext extends ParserRuleContext {
    public TypeReferenceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public UserTypeContext userType() {
      return getRuleContext(UserTypeContext.class, 0);
    }

    public TerminalNode DYNAMIC() {
      return getToken(KotlinParser.DYNAMIC, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeReference;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeReference(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeReference(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeReference(this);
    }
  }

  public static class NullableTypeContext extends ParserRuleContext {
    public NullableTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeReferenceContext typeReference() {
      return getRuleContext(TypeReferenceContext.class, 0);
    }

    public ParenthesizedTypeContext parenthesizedType() {
      return getRuleContext(ParenthesizedTypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<QuestContext> quest() {
      return getRuleContexts(QuestContext.class);
    }

    public QuestContext quest(int i) {
      return getRuleContext(QuestContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_nullableType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitNullableType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterNullableType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitNullableType(this);
    }
  }

  public static class QuestContext extends ParserRuleContext {
    public QuestContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode QUEST_NO_WS() {
      return getToken(KotlinParser.QUEST_NO_WS, 0);
    }

    public TerminalNode QUEST_WS() {
      return getToken(KotlinParser.QUEST_WS, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_quest;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitQuest(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterQuest(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitQuest(this);
    }
  }

  public static class UserTypeContext extends ParserRuleContext {
    public UserTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<SimpleUserTypeContext> simpleUserType() {
      return getRuleContexts(SimpleUserTypeContext.class);
    }

    public SimpleUserTypeContext simpleUserType(int i) {
      return getRuleContext(SimpleUserTypeContext.class, i);
    }

    public List<TerminalNode> DOT() {
      return getTokens(KotlinParser.DOT);
    }

    public TerminalNode DOT(int i) {
      return getToken(KotlinParser.DOT, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_userType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitUserType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterUserType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitUserType(this);
    }
  }

  public static class SimpleUserTypeContext extends ParserRuleContext {
    public SimpleUserTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TypeArgumentsContext typeArguments() {
      return getRuleContext(TypeArgumentsContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleUserType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSimpleUserType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSimpleUserType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSimpleUserType(this);
    }
  }

  public static class TypeProjectionContext extends ParserRuleContext {
    public TypeProjectionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TypeProjectionModifiersContext typeProjectionModifiers() {
      return getRuleContext(TypeProjectionModifiersContext.class, 0);
    }

    public TerminalNode MULT() {
      return getToken(KotlinParser.MULT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeProjection;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeProjection(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeProjection(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeProjection(this);
    }
  }

  public static class TypeProjectionModifiersContext extends ParserRuleContext {
    public TypeProjectionModifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TypeProjectionModifierContext> typeProjectionModifier() {
      return getRuleContexts(TypeProjectionModifierContext.class);
    }

    public TypeProjectionModifierContext typeProjectionModifier(int i) {
      return getRuleContext(TypeProjectionModifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeProjectionModifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeProjectionModifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeProjectionModifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeProjectionModifiers(this);
    }
  }

  public static class TypeProjectionModifierContext extends ParserRuleContext {
    public TypeProjectionModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public VarianceModifierContext varianceModifier() {
      return getRuleContext(VarianceModifierContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeProjectionModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeProjectionModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeProjectionModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeProjectionModifier(this);
    }
  }

  public static class FunctionTypeContext extends ParserRuleContext {
    public FunctionTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public FunctionTypeParametersContext functionTypeParameters() {
      return getRuleContext(FunctionTypeParametersContext.class, 0);
    }

    public TerminalNode ARROW() {
      return getToken(KotlinParser.ARROW, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public ReceiverTypeContext receiverType() {
      return getRuleContext(ReceiverTypeContext.class, 0);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionType(this);
    }
  }

  public static class FunctionTypeParametersContext extends ParserRuleContext {
    public FunctionTypeParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<ParameterContext> parameter() {
      return getRuleContexts(ParameterContext.class);
    }

    public ParameterContext parameter(int i) {
      return getRuleContext(ParameterContext.class, i);
    }

    public List<TypeContext> type() {
      return getRuleContexts(TypeContext.class);
    }

    public TypeContext type(int i) {
      return getRuleContext(TypeContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionTypeParameters;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionTypeParameters(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionTypeParameters(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionTypeParameters(this);
    }
  }

  public static class ParenthesizedTypeContext extends ParserRuleContext {
    public ParenthesizedTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parenthesizedType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParenthesizedType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParenthesizedType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParenthesizedType(this);
    }
  }

  public static class ReceiverTypeContext extends ParserRuleContext {
    public ReceiverTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParenthesizedTypeContext parenthesizedType() {
      return getRuleContext(ParenthesizedTypeContext.class, 0);
    }

    public NullableTypeContext nullableType() {
      return getRuleContext(NullableTypeContext.class, 0);
    }

    public TypeReferenceContext typeReference() {
      return getRuleContext(TypeReferenceContext.class, 0);
    }

    public TypeModifiersContext typeModifiers() {
      return getRuleContext(TypeModifiersContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_receiverType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitReceiverType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterReceiverType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitReceiverType(this);
    }
  }

  public static class ParenthesizedUserTypeContext extends ParserRuleContext {
    public ParenthesizedUserTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public UserTypeContext userType() {
      return getRuleContext(UserTypeContext.class, 0);
    }

    public ParenthesizedUserTypeContext parenthesizedUserType() {
      return getRuleContext(ParenthesizedUserTypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parenthesizedUserType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParenthesizedUserType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParenthesizedUserType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParenthesizedUserType(this);
    }
  }

  public static class StatementsContext extends ParserRuleContext {
    public StatementsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<StatementContext> statement() {
      return getRuleContexts(StatementContext.class);
    }

    public StatementContext statement(int i) {
      return getRuleContext(StatementContext.class, i);
    }

    public List<SemisContext> semis() {
      return getRuleContexts(SemisContext.class);
    }

    public SemisContext semis(int i) {
      return getRuleContext(SemisContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_statements;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitStatements(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterStatements(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitStatements(this);
    }
  }

  public static class StatementContext extends ParserRuleContext {
    public StatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    public AssignmentContext assignment() {
      return getRuleContext(AssignmentContext.class, 0);
    }

    public LoopStatementContext loopStatement() {
      return getRuleContext(LoopStatementContext.class, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<LabelContext> label() {
      return getRuleContexts(LabelContext.class);
    }

    public LabelContext label(int i) {
      return getRuleContext(LabelContext.class, i);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_statement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitStatement(this);
    }
  }

  public static class LabelContext extends ParserRuleContext {
    public LabelContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode AT_NO_WS() {
      return getToken(KotlinParser.AT_NO_WS, 0);
    }

    public TerminalNode AT_POST_WS() {
      return getToken(KotlinParser.AT_POST_WS, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_label;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLabel(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLabel(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLabel(this);
    }
  }

  public static class ControlStructureBodyContext extends ParserRuleContext {
    public ControlStructureBodyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public StatementContext statement() {
      return getRuleContext(StatementContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_controlStructureBody;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitControlStructureBody(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterControlStructureBody(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitControlStructureBody(this);
    }
  }

  public static class BlockContext extends ParserRuleContext {
    public BlockContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LCURL() {
      return getToken(KotlinParser.LCURL, 0);
    }

    public StatementsContext statements() {
      return getRuleContext(StatementsContext.class, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_block;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitBlock(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterBlock(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitBlock(this);
    }
  }

  public static class LoopStatementContext extends ParserRuleContext {
    public LoopStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ForStatementContext forStatement() {
      return getRuleContext(ForStatementContext.class, 0);
    }

    public WhileStatementContext whileStatement() {
      return getRuleContext(WhileStatementContext.class, 0);
    }

    public DoWhileStatementContext doWhileStatement() {
      return getRuleContext(DoWhileStatementContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_loopStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLoopStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLoopStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLoopStatement(this);
    }
  }

  public static class ForStatementContext extends ParserRuleContext {
    public ForStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode FOR() {
      return getToken(KotlinParser.FOR, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode IN() {
      return getToken(KotlinParser.IN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public VariableDeclarationContext variableDeclaration() {
      return getRuleContext(VariableDeclarationContext.class, 0);
    }

    public MultiVariableDeclarationContext multiVariableDeclaration() {
      return getRuleContext(MultiVariableDeclarationContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public ControlStructureBodyContext controlStructureBody() {
      return getRuleContext(ControlStructureBodyContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitForStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterForStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitForStatement(this);
    }
  }

  public static class WhileStatementContext extends ParserRuleContext {
    public WhileStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode WHILE() {
      return getToken(KotlinParser.WHILE, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public ControlStructureBodyContext controlStructureBody() {
      return getRuleContext(ControlStructureBodyContext.class, 0);
    }

    public TerminalNode SEMICOLON() {
      return getToken(KotlinParser.SEMICOLON, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_whileStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitWhileStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterWhileStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitWhileStatement(this);
    }
  }

  public static class DoWhileStatementContext extends ParserRuleContext {
    public DoWhileStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode DO() {
      return getToken(KotlinParser.DO, 0);
    }

    public TerminalNode WHILE() {
      return getToken(KotlinParser.WHILE, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public ControlStructureBodyContext controlStructureBody() {
      return getRuleContext(ControlStructureBodyContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_doWhileStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitDoWhileStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterDoWhileStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitDoWhileStatement(this);
    }
  }

  public static class AssignmentContext extends ParserRuleContext {
    public AssignmentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public DirectlyAssignableExpressionContext directlyAssignableExpression() {
      return getRuleContext(DirectlyAssignableExpressionContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public AssignableExpressionContext assignableExpression() {
      return getRuleContext(AssignableExpressionContext.class, 0);
    }

    public AssignmentAndOperatorContext assignmentAndOperator() {
      return getRuleContext(AssignmentAndOperatorContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_assignment;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAssignment(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAssignment(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAssignment(this);
    }
  }

  public static class SemiContext extends ParserRuleContext {
    public SemiContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode SEMICOLON() {
      return getToken(KotlinParser.SEMICOLON, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode EOF() {
      return getToken(KotlinParser.EOF, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_semi;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSemi(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSemi(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSemi(this);
    }
  }

  public static class SemisContext extends ParserRuleContext {
    public SemisContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TerminalNode> SEMICOLON() {
      return getTokens(KotlinParser.SEMICOLON);
    }

    public TerminalNode SEMICOLON(int i) {
      return getToken(KotlinParser.SEMICOLON, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode EOF() {
      return getToken(KotlinParser.EOF, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_semis;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSemis(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSemis(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSemis(this);
    }
  }

  public static class ExpressionContext extends ParserRuleContext {
    public ExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DisjunctionContext disjunction() {
      return getRuleContext(DisjunctionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_expression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitExpression(this);
    }
  }

  public static class DisjunctionContext extends ParserRuleContext {
    public DisjunctionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ConjunctionContext> conjunction() {
      return getRuleContexts(ConjunctionContext.class);
    }

    public ConjunctionContext conjunction(int i) {
      return getRuleContext(ConjunctionContext.class, i);
    }

    public List<TerminalNode> DISJ() {
      return getTokens(KotlinParser.DISJ);
    }

    public TerminalNode DISJ(int i) {
      return getToken(KotlinParser.DISJ, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_disjunction;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitDisjunction(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterDisjunction(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitDisjunction(this);
    }
  }

  public static class ConjunctionContext extends ParserRuleContext {
    public ConjunctionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<EqualityContext> equality() {
      return getRuleContexts(EqualityContext.class);
    }

    public EqualityContext equality(int i) {
      return getRuleContext(EqualityContext.class, i);
    }

    public List<TerminalNode> CONJ() {
      return getTokens(KotlinParser.CONJ);
    }

    public TerminalNode CONJ(int i) {
      return getToken(KotlinParser.CONJ, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_conjunction;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitConjunction(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterConjunction(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitConjunction(this);
    }
  }

  public static class EqualityContext extends ParserRuleContext {
    public EqualityContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ComparisonContext> comparison() {
      return getRuleContexts(ComparisonContext.class);
    }

    public ComparisonContext comparison(int i) {
      return getRuleContext(ComparisonContext.class, i);
    }

    public List<EqualityOperatorContext> equalityOperator() {
      return getRuleContexts(EqualityOperatorContext.class);
    }

    public EqualityOperatorContext equalityOperator(int i) {
      return getRuleContext(EqualityOperatorContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_equality;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitEquality(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterEquality(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitEquality(this);
    }
  }

  public static class ComparisonContext extends ParserRuleContext {
    public ComparisonContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<GenericCallLikeComparisonContext> genericCallLikeComparison() {
      return getRuleContexts(GenericCallLikeComparisonContext.class);
    }

    public GenericCallLikeComparisonContext genericCallLikeComparison(int i) {
      return getRuleContext(GenericCallLikeComparisonContext.class, i);
    }

    public List<ComparisonOperatorContext> comparisonOperator() {
      return getRuleContexts(ComparisonOperatorContext.class);
    }

    public ComparisonOperatorContext comparisonOperator(int i) {
      return getRuleContext(ComparisonOperatorContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_comparison;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitComparison(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterComparison(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitComparison(this);
    }
  }

  public static class GenericCallLikeComparisonContext extends ParserRuleContext {
    public GenericCallLikeComparisonContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public InfixOperationContext infixOperation() {
      return getRuleContext(InfixOperationContext.class, 0);
    }

    public List<CallSuffixContext> callSuffix() {
      return getRuleContexts(CallSuffixContext.class);
    }

    public CallSuffixContext callSuffix(int i) {
      return getRuleContext(CallSuffixContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_genericCallLikeComparison;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitGenericCallLikeComparison(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterGenericCallLikeComparison(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitGenericCallLikeComparison(this);
    }
  }

  public static class InfixOperationContext extends ParserRuleContext {
    public InfixOperationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ElvisExpressionContext> elvisExpression() {
      return getRuleContexts(ElvisExpressionContext.class);
    }

    public ElvisExpressionContext elvisExpression(int i) {
      return getRuleContext(ElvisExpressionContext.class, i);
    }

    public List<InOperatorContext> inOperator() {
      return getRuleContexts(InOperatorContext.class);
    }

    public InOperatorContext inOperator(int i) {
      return getRuleContext(InOperatorContext.class, i);
    }

    public List<IsOperatorContext> isOperator() {
      return getRuleContexts(IsOperatorContext.class);
    }

    public IsOperatorContext isOperator(int i) {
      return getRuleContext(IsOperatorContext.class, i);
    }

    public List<TypeContext> type() {
      return getRuleContexts(TypeContext.class);
    }

    public TypeContext type(int i) {
      return getRuleContext(TypeContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_infixOperation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitInfixOperation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterInfixOperation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitInfixOperation(this);
    }
  }

  public static class ElvisExpressionContext extends ParserRuleContext {
    public ElvisExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<InfixFunctionCallContext> infixFunctionCall() {
      return getRuleContexts(InfixFunctionCallContext.class);
    }

    public InfixFunctionCallContext infixFunctionCall(int i) {
      return getRuleContext(InfixFunctionCallContext.class, i);
    }

    public List<ElvisContext> elvis() {
      return getRuleContexts(ElvisContext.class);
    }

    public ElvisContext elvis(int i) {
      return getRuleContext(ElvisContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elvisExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitElvisExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterElvisExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitElvisExpression(this);
    }
  }

  public static class ElvisContext extends ParserRuleContext {
    public ElvisContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode QUEST_NO_WS() {
      return getToken(KotlinParser.QUEST_NO_WS, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elvis;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitElvis(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterElvis(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitElvis(this);
    }
  }

  public static class InfixFunctionCallContext extends ParserRuleContext {
    public InfixFunctionCallContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<RangeExpressionContext> rangeExpression() {
      return getRuleContexts(RangeExpressionContext.class);
    }

    public RangeExpressionContext rangeExpression(int i) {
      return getRuleContext(RangeExpressionContext.class, i);
    }

    public List<SimpleIdentifierContext> simpleIdentifier() {
      return getRuleContexts(SimpleIdentifierContext.class);
    }

    public SimpleIdentifierContext simpleIdentifier(int i) {
      return getRuleContext(SimpleIdentifierContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_infixFunctionCall;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitInfixFunctionCall(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterInfixFunctionCall(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitInfixFunctionCall(this);
    }
  }

  public static class RangeExpressionContext extends ParserRuleContext {
    public RangeExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AdditiveExpressionContext> additiveExpression() {
      return getRuleContexts(AdditiveExpressionContext.class);
    }

    public AdditiveExpressionContext additiveExpression(int i) {
      return getRuleContext(AdditiveExpressionContext.class, i);
    }

    public List<TerminalNode> RANGE() {
      return getTokens(KotlinParser.RANGE);
    }

    public TerminalNode RANGE(int i) {
      return getToken(KotlinParser.RANGE, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_rangeExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitRangeExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterRangeExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitRangeExpression(this);
    }
  }

  public static class AdditiveExpressionContext extends ParserRuleContext {
    public AdditiveExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<MultiplicativeExpressionContext> multiplicativeExpression() {
      return getRuleContexts(MultiplicativeExpressionContext.class);
    }

    public MultiplicativeExpressionContext multiplicativeExpression(int i) {
      return getRuleContext(MultiplicativeExpressionContext.class, i);
    }

    public List<AdditiveOperatorContext> additiveOperator() {
      return getRuleContexts(AdditiveOperatorContext.class);
    }

    public AdditiveOperatorContext additiveOperator(int i) {
      return getRuleContext(AdditiveOperatorContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_additiveExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAdditiveExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAdditiveExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAdditiveExpression(this);
    }
  }

  public static class MultiplicativeExpressionContext extends ParserRuleContext {
    public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AsExpressionContext> asExpression() {
      return getRuleContexts(AsExpressionContext.class);
    }

    public AsExpressionContext asExpression(int i) {
      return getRuleContext(AsExpressionContext.class, i);
    }

    public List<MultiplicativeOperatorContext> multiplicativeOperator() {
      return getRuleContexts(MultiplicativeOperatorContext.class);
    }

    public MultiplicativeOperatorContext multiplicativeOperator(int i) {
      return getRuleContext(MultiplicativeOperatorContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiplicativeExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiplicativeExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiplicativeExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiplicativeExpression(this);
    }
  }

  public static class AsExpressionContext extends ParserRuleContext {
    public AsExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PrefixUnaryExpressionContext prefixUnaryExpression() {
      return getRuleContext(PrefixUnaryExpressionContext.class, 0);
    }

    public List<AsOperatorContext> asOperator() {
      return getRuleContexts(AsOperatorContext.class);
    }

    public AsOperatorContext asOperator(int i) {
      return getRuleContext(AsOperatorContext.class, i);
    }

    public List<TypeContext> type() {
      return getRuleContexts(TypeContext.class);
    }

    public TypeContext type(int i) {
      return getRuleContext(TypeContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_asExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAsExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAsExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAsExpression(this);
    }
  }

  public static class PrefixUnaryExpressionContext extends ParserRuleContext {
    public PrefixUnaryExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PostfixUnaryExpressionContext postfixUnaryExpression() {
      return getRuleContext(PostfixUnaryExpressionContext.class, 0);
    }

    public List<UnaryPrefixContext> unaryPrefix() {
      return getRuleContexts(UnaryPrefixContext.class);
    }

    public UnaryPrefixContext unaryPrefix(int i) {
      return getRuleContext(UnaryPrefixContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_prefixUnaryExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPrefixUnaryExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPrefixUnaryExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPrefixUnaryExpression(this);
    }
  }

  public static class UnaryPrefixContext extends ParserRuleContext {
    public UnaryPrefixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public LabelContext label() {
      return getRuleContext(LabelContext.class, 0);
    }

    public PrefixUnaryOperatorContext prefixUnaryOperator() {
      return getRuleContext(PrefixUnaryOperatorContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_unaryPrefix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitUnaryPrefix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterUnaryPrefix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitUnaryPrefix(this);
    }
  }

  public static class PostfixUnaryExpressionContext extends ParserRuleContext {
    public PostfixUnaryExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PrimaryExpressionContext primaryExpression() {
      return getRuleContext(PrimaryExpressionContext.class, 0);
    }

    public List<PostfixUnarySuffixContext> postfixUnarySuffix() {
      return getRuleContexts(PostfixUnarySuffixContext.class);
    }

    public PostfixUnarySuffixContext postfixUnarySuffix(int i) {
      return getRuleContext(PostfixUnarySuffixContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_postfixUnaryExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPostfixUnaryExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPostfixUnaryExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPostfixUnaryExpression(this);
    }
  }

  public static class PostfixUnarySuffixContext extends ParserRuleContext {
    public PostfixUnarySuffixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PostfixUnaryOperatorContext postfixUnaryOperator() {
      return getRuleContext(PostfixUnaryOperatorContext.class, 0);
    }

    public TypeArgumentsContext typeArguments() {
      return getRuleContext(TypeArgumentsContext.class, 0);
    }

    public CallSuffixContext callSuffix() {
      return getRuleContext(CallSuffixContext.class, 0);
    }

    public IndexingSuffixContext indexingSuffix() {
      return getRuleContext(IndexingSuffixContext.class, 0);
    }

    public NavigationSuffixContext navigationSuffix() {
      return getRuleContext(NavigationSuffixContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_postfixUnarySuffix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPostfixUnarySuffix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPostfixUnarySuffix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPostfixUnarySuffix(this);
    }
  }

  public static class DirectlyAssignableExpressionContext extends ParserRuleContext {
    public DirectlyAssignableExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PostfixUnaryExpressionContext postfixUnaryExpression() {
      return getRuleContext(PostfixUnaryExpressionContext.class, 0);
    }

    public AssignableSuffixContext assignableSuffix() {
      return getRuleContext(AssignableSuffixContext.class, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public ParenthesizedDirectlyAssignableExpressionContext
        parenthesizedDirectlyAssignableExpression() {
      return getRuleContext(ParenthesizedDirectlyAssignableExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_directlyAssignableExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitDirectlyAssignableExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterDirectlyAssignableExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitDirectlyAssignableExpression(this);
    }
  }

  public static class ParenthesizedDirectlyAssignableExpressionContext extends ParserRuleContext {
    public ParenthesizedDirectlyAssignableExpressionContext(
        ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public DirectlyAssignableExpressionContext directlyAssignableExpression() {
      return getRuleContext(DirectlyAssignableExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parenthesizedDirectlyAssignableExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor)
            .visitParenthesizedDirectlyAssignableExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParenthesizedDirectlyAssignableExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParenthesizedDirectlyAssignableExpression(this);
    }
  }

  public static class AssignableExpressionContext extends ParserRuleContext {
    public AssignableExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PrefixUnaryExpressionContext prefixUnaryExpression() {
      return getRuleContext(PrefixUnaryExpressionContext.class, 0);
    }

    public ParenthesizedAssignableExpressionContext parenthesizedAssignableExpression() {
      return getRuleContext(ParenthesizedAssignableExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_assignableExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAssignableExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAssignableExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAssignableExpression(this);
    }
  }

  public static class ParenthesizedAssignableExpressionContext extends ParserRuleContext {
    public ParenthesizedAssignableExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public AssignableExpressionContext assignableExpression() {
      return getRuleContext(AssignableExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parenthesizedAssignableExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor)
            .visitParenthesizedAssignableExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParenthesizedAssignableExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParenthesizedAssignableExpression(this);
    }
  }

  public static class AssignableSuffixContext extends ParserRuleContext {
    public AssignableSuffixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeArgumentsContext typeArguments() {
      return getRuleContext(TypeArgumentsContext.class, 0);
    }

    public IndexingSuffixContext indexingSuffix() {
      return getRuleContext(IndexingSuffixContext.class, 0);
    }

    public NavigationSuffixContext navigationSuffix() {
      return getRuleContext(NavigationSuffixContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_assignableSuffix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAssignableSuffix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAssignableSuffix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAssignableSuffix(this);
    }
  }

  public static class IndexingSuffixContext extends ParserRuleContext {
    public IndexingSuffixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LSQUARE() {
      return getToken(KotlinParser.LSQUARE, 0);
    }

    public List<ExpressionContext> expression() {
      return getRuleContexts(ExpressionContext.class);
    }

    public ExpressionContext expression(int i) {
      return getRuleContext(ExpressionContext.class, i);
    }

    public TerminalNode RSQUARE() {
      return getToken(KotlinParser.RSQUARE, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_indexingSuffix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitIndexingSuffix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterIndexingSuffix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitIndexingSuffix(this);
    }
  }

  public static class NavigationSuffixContext extends ParserRuleContext {
    public NavigationSuffixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public MemberAccessOperatorContext memberAccessOperator() {
      return getRuleContext(MemberAccessOperatorContext.class, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public ParenthesizedExpressionContext parenthesizedExpression() {
      return getRuleContext(ParenthesizedExpressionContext.class, 0);
    }

    public TerminalNode CLASS() {
      return getToken(KotlinParser.CLASS, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_navigationSuffix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitNavigationSuffix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterNavigationSuffix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitNavigationSuffix(this);
    }
  }

  public static class CallSuffixContext extends ParserRuleContext {
    public CallSuffixContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public AnnotatedLambdaContext annotatedLambda() {
      return getRuleContext(AnnotatedLambdaContext.class, 0);
    }

    public ValueArgumentsContext valueArguments() {
      return getRuleContext(ValueArgumentsContext.class, 0);
    }

    public TypeArgumentsContext typeArguments() {
      return getRuleContext(TypeArgumentsContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_callSuffix;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitCallSuffix(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterCallSuffix(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitCallSuffix(this);
    }
  }

  public static class AnnotatedLambdaContext extends ParserRuleContext {
    public AnnotatedLambdaContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public LambdaLiteralContext lambdaLiteral() {
      return getRuleContext(LambdaLiteralContext.class, 0);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public LabelContext label() {
      return getRuleContext(LabelContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotatedLambda;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAnnotatedLambda(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAnnotatedLambda(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAnnotatedLambda(this);
    }
  }

  public static class TypeArgumentsContext extends ParserRuleContext {
    public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LANGLE() {
      return getToken(KotlinParser.LANGLE, 0);
    }

    public List<TypeProjectionContext> typeProjection() {
      return getRuleContexts(TypeProjectionContext.class);
    }

    public TypeProjectionContext typeProjection(int i) {
      return getRuleContext(TypeProjectionContext.class, i);
    }

    public TerminalNode RANGLE() {
      return getToken(KotlinParser.RANGLE, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeArguments;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeArguments(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeArguments(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeArguments(this);
    }
  }

  public static class ValueArgumentsContext extends ParserRuleContext {
    public ValueArgumentsContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<ValueArgumentContext> valueArgument() {
      return getRuleContexts(ValueArgumentContext.class);
    }

    public ValueArgumentContext valueArgument(int i) {
      return getRuleContext(ValueArgumentContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_valueArguments;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitValueArguments(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterValueArguments(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitValueArguments(this);
    }
  }

  public static class ValueArgumentContext extends ParserRuleContext {
    public ValueArgumentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public TerminalNode MULT() {
      return getToken(KotlinParser.MULT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_valueArgument;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitValueArgument(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterValueArgument(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitValueArgument(this);
    }
  }

  public static class PrimaryExpressionContext extends ParserRuleContext {
    public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParenthesizedExpressionContext parenthesizedExpression() {
      return getRuleContext(ParenthesizedExpressionContext.class, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public LiteralConstantContext literalConstant() {
      return getRuleContext(LiteralConstantContext.class, 0);
    }

    public StringLiteralContext stringLiteral() {
      return getRuleContext(StringLiteralContext.class, 0);
    }

    public CallableReferenceContext callableReference() {
      return getRuleContext(CallableReferenceContext.class, 0);
    }

    public FunctionLiteralContext functionLiteral() {
      return getRuleContext(FunctionLiteralContext.class, 0);
    }

    public ObjectLiteralContext objectLiteral() {
      return getRuleContext(ObjectLiteralContext.class, 0);
    }

    public CollectionLiteralContext collectionLiteral() {
      return getRuleContext(CollectionLiteralContext.class, 0);
    }

    public ThisExpressionContext thisExpression() {
      return getRuleContext(ThisExpressionContext.class, 0);
    }

    public SuperExpressionContext superExpression() {
      return getRuleContext(SuperExpressionContext.class, 0);
    }

    public IfExpressionContext ifExpression() {
      return getRuleContext(IfExpressionContext.class, 0);
    }

    public WhenExpressionContext whenExpression() {
      return getRuleContext(WhenExpressionContext.class, 0);
    }

    public TryExpressionContext tryExpression() {
      return getRuleContext(TryExpressionContext.class, 0);
    }

    public JumpExpressionContext jumpExpression() {
      return getRuleContext(JumpExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_primaryExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPrimaryExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPrimaryExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPrimaryExpression(this);
    }
  }

  public static class ParenthesizedExpressionContext extends ParserRuleContext {
    public ParenthesizedExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parenthesizedExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParenthesizedExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParenthesizedExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParenthesizedExpression(this);
    }
  }

  public static class CollectionLiteralContext extends ParserRuleContext {
    public CollectionLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LSQUARE() {
      return getToken(KotlinParser.LSQUARE, 0);
    }

    public TerminalNode RSQUARE() {
      return getToken(KotlinParser.RSQUARE, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<ExpressionContext> expression() {
      return getRuleContexts(ExpressionContext.class);
    }

    public ExpressionContext expression(int i) {
      return getRuleContext(ExpressionContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_collectionLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitCollectionLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterCollectionLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitCollectionLiteral(this);
    }
  }

  public static class LiteralConstantContext extends ParserRuleContext {
    public LiteralConstantContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode BooleanLiteral() {
      return getToken(KotlinParser.BooleanLiteral, 0);
    }

    public TerminalNode IntegerLiteral() {
      return getToken(KotlinParser.IntegerLiteral, 0);
    }

    public TerminalNode HexLiteral() {
      return getToken(KotlinParser.HexLiteral, 0);
    }

    public TerminalNode BinLiteral() {
      return getToken(KotlinParser.BinLiteral, 0);
    }

    public TerminalNode CharacterLiteral() {
      return getToken(KotlinParser.CharacterLiteral, 0);
    }

    public TerminalNode RealLiteral() {
      return getToken(KotlinParser.RealLiteral, 0);
    }

    public TerminalNode NullLiteral() {
      return getToken(KotlinParser.NullLiteral, 0);
    }

    public TerminalNode LongLiteral() {
      return getToken(KotlinParser.LongLiteral, 0);
    }

    public TerminalNode UnsignedLiteral() {
      return getToken(KotlinParser.UnsignedLiteral, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_literalConstant;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLiteralConstant(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLiteralConstant(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLiteralConstant(this);
    }
  }

  public static class StringLiteralContext extends ParserRuleContext {
    public StringLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public LineStringLiteralContext lineStringLiteral() {
      return getRuleContext(LineStringLiteralContext.class, 0);
    }

    public MultiLineStringLiteralContext multiLineStringLiteral() {
      return getRuleContext(MultiLineStringLiteralContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_stringLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitStringLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterStringLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitStringLiteral(this);
    }
  }

  public static class LineStringLiteralContext extends ParserRuleContext {
    public LineStringLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode QUOTE_OPEN() {
      return getToken(KotlinParser.QUOTE_OPEN, 0);
    }

    public TerminalNode QUOTE_CLOSE() {
      return getToken(KotlinParser.QUOTE_CLOSE, 0);
    }

    public List<LineStringContentContext> lineStringContent() {
      return getRuleContexts(LineStringContentContext.class);
    }

    public LineStringContentContext lineStringContent(int i) {
      return getRuleContext(LineStringContentContext.class, i);
    }

    public List<LineStringExpressionContext> lineStringExpression() {
      return getRuleContexts(LineStringExpressionContext.class);
    }

    public LineStringExpressionContext lineStringExpression(int i) {
      return getRuleContext(LineStringExpressionContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lineStringLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLineStringLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLineStringLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLineStringLiteral(this);
    }
  }

  public static class MultiLineStringLiteralContext extends ParserRuleContext {
    public MultiLineStringLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TRIPLE_QUOTE_OPEN() {
      return getToken(KotlinParser.TRIPLE_QUOTE_OPEN, 0);
    }

    public TerminalNode TRIPLE_QUOTE_CLOSE() {
      return getToken(KotlinParser.TRIPLE_QUOTE_CLOSE, 0);
    }

    public List<MultiLineStringContentContext> multiLineStringContent() {
      return getRuleContexts(MultiLineStringContentContext.class);
    }

    public MultiLineStringContentContext multiLineStringContent(int i) {
      return getRuleContext(MultiLineStringContentContext.class, i);
    }

    public List<MultiLineStringExpressionContext> multiLineStringExpression() {
      return getRuleContexts(MultiLineStringExpressionContext.class);
    }

    public MultiLineStringExpressionContext multiLineStringExpression(int i) {
      return getRuleContext(MultiLineStringExpressionContext.class, i);
    }

    public List<TerminalNode> MultiLineStringQuote() {
      return getTokens(KotlinParser.MultiLineStringQuote);
    }

    public TerminalNode MultiLineStringQuote(int i) {
      return getToken(KotlinParser.MultiLineStringQuote, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiLineStringLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiLineStringLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiLineStringLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiLineStringLiteral(this);
    }
  }

  public static class LineStringContentContext extends ParserRuleContext {
    public LineStringContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LineStrText() {
      return getToken(KotlinParser.LineStrText, 0);
    }

    public TerminalNode LineStrEscapedChar() {
      return getToken(KotlinParser.LineStrEscapedChar, 0);
    }

    public TerminalNode LineStrRef() {
      return getToken(KotlinParser.LineStrRef, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lineStringContent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLineStringContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLineStringContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLineStringContent(this);
    }
  }

  public static class LineStringExpressionContext extends ParserRuleContext {
    public LineStringExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LineStrExprStart() {
      return getToken(KotlinParser.LineStrExprStart, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lineStringExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLineStringExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLineStringExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLineStringExpression(this);
    }
  }

  public static class MultiLineStringContentContext extends ParserRuleContext {
    public MultiLineStringContentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode MultiLineStrText() {
      return getToken(KotlinParser.MultiLineStrText, 0);
    }

    public TerminalNode MultiLineStringQuote() {
      return getToken(KotlinParser.MultiLineStringQuote, 0);
    }

    public TerminalNode MultiLineStrRef() {
      return getToken(KotlinParser.MultiLineStrRef, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiLineStringContent;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiLineStringContent(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiLineStringContent(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiLineStringContent(this);
    }
  }

  public static class MultiLineStringExpressionContext extends ParserRuleContext {
    public MultiLineStringExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode MultiLineStrExprStart() {
      return getToken(KotlinParser.MultiLineStrExprStart, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiLineStringExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiLineStringExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiLineStringExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiLineStringExpression(this);
    }
  }

  public static class LambdaLiteralContext extends ParserRuleContext {
    public LambdaLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LCURL() {
      return getToken(KotlinParser.LCURL, 0);
    }

    public StatementsContext statements() {
      return getRuleContext(StatementsContext.class, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode ARROW() {
      return getToken(KotlinParser.ARROW, 0);
    }

    public LambdaParametersContext lambdaParameters() {
      return getRuleContext(LambdaParametersContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLambdaLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLambdaLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLambdaLiteral(this);
    }
  }

  public static class LambdaParametersContext extends ParserRuleContext {
    public LambdaParametersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<LambdaParameterContext> lambdaParameter() {
      return getRuleContexts(LambdaParameterContext.class);
    }

    public LambdaParameterContext lambdaParameter(int i) {
      return getRuleContext(LambdaParameterContext.class, i);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaParameters;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLambdaParameters(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLambdaParameters(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLambdaParameters(this);
    }
  }

  public static class LambdaParameterContext extends ParserRuleContext {
    public LambdaParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public VariableDeclarationContext variableDeclaration() {
      return getRuleContext(VariableDeclarationContext.class, 0);
    }

    public MultiVariableDeclarationContext multiVariableDeclaration() {
      return getRuleContext(MultiVariableDeclarationContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaParameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitLambdaParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterLambdaParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitLambdaParameter(this);
    }
  }

  public static class AnonymousFunctionContext extends ParserRuleContext {
    public AnonymousFunctionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode FUN() {
      return getToken(KotlinParser.FUN, 0);
    }

    public ParametersWithOptionalTypeContext parametersWithOptionalType() {
      return getRuleContext(ParametersWithOptionalTypeContext.class, 0);
    }

    public List<TypeContext> type() {
      return getRuleContexts(TypeContext.class);
    }

    public TypeContext type(int i) {
      return getRuleContext(TypeContext.class, i);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeConstraintsContext typeConstraints() {
      return getRuleContext(TypeConstraintsContext.class, 0);
    }

    public FunctionBodyContext functionBody() {
      return getRuleContext(FunctionBodyContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_anonymousFunction;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAnonymousFunction(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAnonymousFunction(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAnonymousFunction(this);
    }
  }

  public static class FunctionLiteralContext extends ParserRuleContext {
    public FunctionLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public LambdaLiteralContext lambdaLiteral() {
      return getRuleContext(LambdaLiteralContext.class, 0);
    }

    public AnonymousFunctionContext anonymousFunction() {
      return getRuleContext(AnonymousFunctionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionLiteral(this);
    }
  }

  public static class ObjectLiteralContext extends ParserRuleContext {
    public ObjectLiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode OBJECT() {
      return getToken(KotlinParser.OBJECT, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public DelegationSpecifiersContext delegationSpecifiers() {
      return getRuleContext(DelegationSpecifiersContext.class, 0);
    }

    public ClassBodyContext classBody() {
      return getRuleContext(ClassBodyContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_objectLiteral;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitObjectLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterObjectLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitObjectLiteral(this);
    }
  }

  public static class ThisExpressionContext extends ParserRuleContext {
    public ThisExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode THIS() {
      return getToken(KotlinParser.THIS, 0);
    }

    public TerminalNode THIS_AT() {
      return getToken(KotlinParser.THIS_AT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_thisExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitThisExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterThisExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitThisExpression(this);
    }
  }

  public static class SuperExpressionContext extends ParserRuleContext {
    public SuperExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode SUPER() {
      return getToken(KotlinParser.SUPER, 0);
    }

    public TerminalNode LANGLE() {
      return getToken(KotlinParser.LANGLE, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode RANGLE() {
      return getToken(KotlinParser.RANGLE, 0);
    }

    public TerminalNode AT_NO_WS() {
      return getToken(KotlinParser.AT_NO_WS, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode SUPER_AT() {
      return getToken(KotlinParser.SUPER_AT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_superExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSuperExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSuperExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSuperExpression(this);
    }
  }

  public static class IfExpressionContext extends ParserRuleContext {
    public IfExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode IF() {
      return getToken(KotlinParser.IF, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public List<ControlStructureBodyContext> controlStructureBody() {
      return getRuleContexts(ControlStructureBodyContext.class);
    }

    public ControlStructureBodyContext controlStructureBody(int i) {
      return getRuleContext(ControlStructureBodyContext.class, i);
    }

    public TerminalNode ELSE() {
      return getToken(KotlinParser.ELSE, 0);
    }

    public List<TerminalNode> SEMICOLON() {
      return getTokens(KotlinParser.SEMICOLON);
    }

    public TerminalNode SEMICOLON(int i) {
      return getToken(KotlinParser.SEMICOLON, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_ifExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitIfExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterIfExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitIfExpression(this);
    }
  }

  public static class WhenSubjectContext extends ParserRuleContext {
    public WhenSubjectContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public TerminalNode VAL() {
      return getToken(KotlinParser.VAL, 0);
    }

    public VariableDeclarationContext variableDeclaration() {
      return getRuleContext(VariableDeclarationContext.class, 0);
    }

    public TerminalNode ASSIGNMENT() {
      return getToken(KotlinParser.ASSIGNMENT, 0);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_whenSubject;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitWhenSubject(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterWhenSubject(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitWhenSubject(this);
    }
  }

  public static class WhenExpressionContext extends ParserRuleContext {
    public WhenExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode WHEN() {
      return getToken(KotlinParser.WHEN, 0);
    }

    public TerminalNode LCURL() {
      return getToken(KotlinParser.LCURL, 0);
    }

    public TerminalNode RCURL() {
      return getToken(KotlinParser.RCURL, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public WhenSubjectContext whenSubject() {
      return getRuleContext(WhenSubjectContext.class, 0);
    }

    public List<WhenEntryContext> whenEntry() {
      return getRuleContexts(WhenEntryContext.class);
    }

    public WhenEntryContext whenEntry(int i) {
      return getRuleContext(WhenEntryContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_whenExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitWhenExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterWhenExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitWhenExpression(this);
    }
  }

  public static class WhenEntryContext extends ParserRuleContext {
    public WhenEntryContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<WhenConditionContext> whenCondition() {
      return getRuleContexts(WhenConditionContext.class);
    }

    public WhenConditionContext whenCondition(int i) {
      return getRuleContext(WhenConditionContext.class, i);
    }

    public TerminalNode ARROW() {
      return getToken(KotlinParser.ARROW, 0);
    }

    public ControlStructureBodyContext controlStructureBody() {
      return getRuleContext(ControlStructureBodyContext.class, 0);
    }

    public List<TerminalNode> COMMA() {
      return getTokens(KotlinParser.COMMA);
    }

    public TerminalNode COMMA(int i) {
      return getToken(KotlinParser.COMMA, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public SemiContext semi() {
      return getRuleContext(SemiContext.class, 0);
    }

    public TerminalNode ELSE() {
      return getToken(KotlinParser.ELSE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_whenEntry;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitWhenEntry(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterWhenEntry(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitWhenEntry(this);
    }
  }

  public static class WhenConditionContext extends ParserRuleContext {
    public WhenConditionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public RangeTestContext rangeTest() {
      return getRuleContext(RangeTestContext.class, 0);
    }

    public TypeTestContext typeTest() {
      return getRuleContext(TypeTestContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_whenCondition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitWhenCondition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterWhenCondition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitWhenCondition(this);
    }
  }

  public static class RangeTestContext extends ParserRuleContext {
    public RangeTestContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public InOperatorContext inOperator() {
      return getRuleContext(InOperatorContext.class, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_rangeTest;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitRangeTest(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterRangeTest(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitRangeTest(this);
    }
  }

  public static class TypeTestContext extends ParserRuleContext {
    public TypeTestContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public IsOperatorContext isOperator() {
      return getRuleContext(IsOperatorContext.class, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeTest;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeTest(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeTest(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeTest(this);
    }
  }

  public static class TryExpressionContext extends ParserRuleContext {
    public TryExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TRY() {
      return getToken(KotlinParser.TRY, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public FinallyBlockContext finallyBlock() {
      return getRuleContext(FinallyBlockContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<CatchBlockContext> catchBlock() {
      return getRuleContexts(CatchBlockContext.class);
    }

    public CatchBlockContext catchBlock(int i) {
      return getRuleContext(CatchBlockContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_tryExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTryExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTryExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTryExpression(this);
    }
  }

  public static class CatchBlockContext extends ParserRuleContext {
    public CatchBlockContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode CATCH() {
      return getToken(KotlinParser.CATCH, 0);
    }

    public TerminalNode LPAREN() {
      return getToken(KotlinParser.LPAREN, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TypeContext type() {
      return getRuleContext(TypeContext.class, 0);
    }

    public TerminalNode RPAREN() {
      return getToken(KotlinParser.RPAREN, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public TerminalNode COMMA() {
      return getToken(KotlinParser.COMMA, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_catchBlock;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitCatchBlock(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterCatchBlock(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitCatchBlock(this);
    }
  }

  public static class FinallyBlockContext extends ParserRuleContext {
    public FinallyBlockContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode FINALLY() {
      return getToken(KotlinParser.FINALLY, 0);
    }

    public BlockContext block() {
      return getRuleContext(BlockContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_finallyBlock;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFinallyBlock(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFinallyBlock(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFinallyBlock(this);
    }
  }

  public static class JumpExpressionContext extends ParserRuleContext {
    public JumpExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode THROW() {
      return getToken(KotlinParser.THROW, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public TerminalNode RETURN() {
      return getToken(KotlinParser.RETURN, 0);
    }

    public TerminalNode RETURN_AT() {
      return getToken(KotlinParser.RETURN_AT, 0);
    }

    public TerminalNode CONTINUE() {
      return getToken(KotlinParser.CONTINUE, 0);
    }

    public TerminalNode CONTINUE_AT() {
      return getToken(KotlinParser.CONTINUE_AT, 0);
    }

    public TerminalNode BREAK() {
      return getToken(KotlinParser.BREAK, 0);
    }

    public TerminalNode BREAK_AT() {
      return getToken(KotlinParser.BREAK_AT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_jumpExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitJumpExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterJumpExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitJumpExpression(this);
    }
  }

  public static class CallableReferenceContext extends ParserRuleContext {
    public CallableReferenceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode COLONCOLON() {
      return getToken(KotlinParser.COLONCOLON, 0);
    }

    public SimpleIdentifierContext simpleIdentifier() {
      return getRuleContext(SimpleIdentifierContext.class, 0);
    }

    public TerminalNode CLASS() {
      return getToken(KotlinParser.CLASS, 0);
    }

    public ReceiverTypeContext receiverType() {
      return getRuleContext(ReceiverTypeContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_callableReference;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitCallableReference(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterCallableReference(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitCallableReference(this);
    }
  }

  public static class AssignmentAndOperatorContext extends ParserRuleContext {
    public AssignmentAndOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode ADD_ASSIGNMENT() {
      return getToken(KotlinParser.ADD_ASSIGNMENT, 0);
    }

    public TerminalNode SUB_ASSIGNMENT() {
      return getToken(KotlinParser.SUB_ASSIGNMENT, 0);
    }

    public TerminalNode MULT_ASSIGNMENT() {
      return getToken(KotlinParser.MULT_ASSIGNMENT, 0);
    }

    public TerminalNode DIV_ASSIGNMENT() {
      return getToken(KotlinParser.DIV_ASSIGNMENT, 0);
    }

    public TerminalNode MOD_ASSIGNMENT() {
      return getToken(KotlinParser.MOD_ASSIGNMENT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_assignmentAndOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAssignmentAndOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAssignmentAndOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAssignmentAndOperator(this);
    }
  }

  public static class EqualityOperatorContext extends ParserRuleContext {
    public EqualityOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode EXCL_EQ() {
      return getToken(KotlinParser.EXCL_EQ, 0);
    }

    public TerminalNode EXCL_EQEQ() {
      return getToken(KotlinParser.EXCL_EQEQ, 0);
    }

    public TerminalNode EQEQ() {
      return getToken(KotlinParser.EQEQ, 0);
    }

    public TerminalNode EQEQEQ() {
      return getToken(KotlinParser.EQEQEQ, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_equalityOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitEqualityOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterEqualityOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitEqualityOperator(this);
    }
  }

  public static class ComparisonOperatorContext extends ParserRuleContext {
    public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LANGLE() {
      return getToken(KotlinParser.LANGLE, 0);
    }

    public TerminalNode RANGLE() {
      return getToken(KotlinParser.RANGLE, 0);
    }

    public TerminalNode LE() {
      return getToken(KotlinParser.LE, 0);
    }

    public TerminalNode GE() {
      return getToken(KotlinParser.GE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_comparisonOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitComparisonOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterComparisonOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitComparisonOperator(this);
    }
  }

  public static class InOperatorContext extends ParserRuleContext {
    public InOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode IN() {
      return getToken(KotlinParser.IN, 0);
    }

    public TerminalNode NOT_IN() {
      return getToken(KotlinParser.NOT_IN, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitInOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterInOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitInOperator(this);
    }
  }

  public static class IsOperatorContext extends ParserRuleContext {
    public IsOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode IS() {
      return getToken(KotlinParser.IS, 0);
    }

    public TerminalNode NOT_IS() {
      return getToken(KotlinParser.NOT_IS, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_isOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitIsOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterIsOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitIsOperator(this);
    }
  }

  public static class AdditiveOperatorContext extends ParserRuleContext {
    public AdditiveOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode ADD() {
      return getToken(KotlinParser.ADD, 0);
    }

    public TerminalNode SUB() {
      return getToken(KotlinParser.SUB, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_additiveOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAdditiveOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAdditiveOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAdditiveOperator(this);
    }
  }

  public static class MultiplicativeOperatorContext extends ParserRuleContext {
    public MultiplicativeOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode MULT() {
      return getToken(KotlinParser.MULT, 0);
    }

    public TerminalNode DIV() {
      return getToken(KotlinParser.DIV, 0);
    }

    public TerminalNode MOD() {
      return getToken(KotlinParser.MOD, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiplicativeOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiplicativeOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiplicativeOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiplicativeOperator(this);
    }
  }

  public static class AsOperatorContext extends ParserRuleContext {
    public AsOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode AS() {
      return getToken(KotlinParser.AS, 0);
    }

    public TerminalNode AS_SAFE() {
      return getToken(KotlinParser.AS_SAFE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_asOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAsOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAsOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAsOperator(this);
    }
  }

  public static class PrefixUnaryOperatorContext extends ParserRuleContext {
    public PrefixUnaryOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode INCR() {
      return getToken(KotlinParser.INCR, 0);
    }

    public TerminalNode DECR() {
      return getToken(KotlinParser.DECR, 0);
    }

    public TerminalNode SUB() {
      return getToken(KotlinParser.SUB, 0);
    }

    public TerminalNode ADD() {
      return getToken(KotlinParser.ADD, 0);
    }

    public ExclContext excl() {
      return getRuleContext(ExclContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_prefixUnaryOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPrefixUnaryOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPrefixUnaryOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPrefixUnaryOperator(this);
    }
  }

  public static class PostfixUnaryOperatorContext extends ParserRuleContext {
    public PostfixUnaryOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode INCR() {
      return getToken(KotlinParser.INCR, 0);
    }

    public TerminalNode DECR() {
      return getToken(KotlinParser.DECR, 0);
    }

    public TerminalNode EXCL_NO_WS() {
      return getToken(KotlinParser.EXCL_NO_WS, 0);
    }

    public ExclContext excl() {
      return getRuleContext(ExclContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_postfixUnaryOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPostfixUnaryOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPostfixUnaryOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPostfixUnaryOperator(this);
    }
  }

  public static class ExclContext extends ParserRuleContext {
    public ExclContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode EXCL_NO_WS() {
      return getToken(KotlinParser.EXCL_NO_WS, 0);
    }

    public TerminalNode EXCL_WS() {
      return getToken(KotlinParser.EXCL_WS, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_excl;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitExcl(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterExcl(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitExcl(this);
    }
  }

  public static class MemberAccessOperatorContext extends ParserRuleContext {
    public MemberAccessOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public SafeNavContext safeNav() {
      return getRuleContext(SafeNavContext.class, 0);
    }

    public TerminalNode COLONCOLON() {
      return getToken(KotlinParser.COLONCOLON, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberAccessOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMemberAccessOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMemberAccessOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMemberAccessOperator(this);
    }
  }

  public static class SafeNavContext extends ParserRuleContext {
    public SafeNavContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode QUEST_NO_WS() {
      return getToken(KotlinParser.QUEST_NO_WS, 0);
    }

    public TerminalNode DOT() {
      return getToken(KotlinParser.DOT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_safeNav;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSafeNav(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSafeNav(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSafeNav(this);
    }
  }

  public static class ModifiersContext extends ParserRuleContext {
    public ModifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public List<ModifierContext> modifier() {
      return getRuleContexts(ModifierContext.class);
    }

    public ModifierContext modifier(int i) {
      return getRuleContext(ModifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_modifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitModifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterModifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitModifiers(this);
    }
  }

  public static class ParameterModifiersContext extends ParserRuleContext {
    public ParameterModifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AnnotationContext> annotation() {
      return getRuleContexts(AnnotationContext.class);
    }

    public AnnotationContext annotation(int i) {
      return getRuleContext(AnnotationContext.class, i);
    }

    public List<ParameterModifierContext> parameterModifier() {
      return getRuleContexts(ParameterModifierContext.class);
    }

    public ParameterModifierContext parameterModifier(int i) {
      return getRuleContext(ParameterModifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameterModifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParameterModifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParameterModifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParameterModifiers(this);
    }
  }

  public static class ModifierContext extends ParserRuleContext {
    public ModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassModifierContext classModifier() {
      return getRuleContext(ClassModifierContext.class, 0);
    }

    public MemberModifierContext memberModifier() {
      return getRuleContext(MemberModifierContext.class, 0);
    }

    public VisibilityModifierContext visibilityModifier() {
      return getRuleContext(VisibilityModifierContext.class, 0);
    }

    public FunctionModifierContext functionModifier() {
      return getRuleContext(FunctionModifierContext.class, 0);
    }

    public PropertyModifierContext propertyModifier() {
      return getRuleContext(PropertyModifierContext.class, 0);
    }

    public InheritanceModifierContext inheritanceModifier() {
      return getRuleContext(InheritanceModifierContext.class, 0);
    }

    public ParameterModifierContext parameterModifier() {
      return getRuleContext(ParameterModifierContext.class, 0);
    }

    public PlatformModifierContext platformModifier() {
      return getRuleContext(PlatformModifierContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_modifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitModifier(this);
    }
  }

  public static class TypeModifiersContext extends ParserRuleContext {
    public TypeModifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TypeModifierContext> typeModifier() {
      return getRuleContexts(TypeModifierContext.class);
    }

    public TypeModifierContext typeModifier(int i) {
      return getRuleContext(TypeModifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeModifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeModifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeModifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeModifiers(this);
    }
  }

  public static class TypeModifierContext extends ParserRuleContext {
    public TypeModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    public TerminalNode SUSPEND() {
      return getToken(KotlinParser.SUSPEND, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeModifier(this);
    }
  }

  public static class ClassModifierContext extends ParserRuleContext {
    public ClassModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode ENUM() {
      return getToken(KotlinParser.ENUM, 0);
    }

    public TerminalNode SEALED() {
      return getToken(KotlinParser.SEALED, 0);
    }

    public TerminalNode ANNOTATION() {
      return getToken(KotlinParser.ANNOTATION, 0);
    }

    public TerminalNode DATA() {
      return getToken(KotlinParser.DATA, 0);
    }

    public TerminalNode INNER() {
      return getToken(KotlinParser.INNER, 0);
    }

    public TerminalNode VALUE() {
      return getToken(KotlinParser.VALUE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitClassModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterClassModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitClassModifier(this);
    }
  }

  public static class MemberModifierContext extends ParserRuleContext {
    public MemberModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode OVERRIDE() {
      return getToken(KotlinParser.OVERRIDE, 0);
    }

    public TerminalNode LATEINIT() {
      return getToken(KotlinParser.LATEINIT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMemberModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMemberModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMemberModifier(this);
    }
  }

  public static class VisibilityModifierContext extends ParserRuleContext {
    public VisibilityModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode PUBLIC() {
      return getToken(KotlinParser.PUBLIC, 0);
    }

    public TerminalNode PRIVATE() {
      return getToken(KotlinParser.PRIVATE, 0);
    }

    public TerminalNode INTERNAL() {
      return getToken(KotlinParser.INTERNAL, 0);
    }

    public TerminalNode PROTECTED() {
      return getToken(KotlinParser.PROTECTED, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_visibilityModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitVisibilityModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterVisibilityModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitVisibilityModifier(this);
    }
  }

  public static class VarianceModifierContext extends ParserRuleContext {
    public VarianceModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode IN() {
      return getToken(KotlinParser.IN, 0);
    }

    public TerminalNode OUT() {
      return getToken(KotlinParser.OUT, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_varianceModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitVarianceModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterVarianceModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitVarianceModifier(this);
    }
  }

  public static class TypeParameterModifiersContext extends ParserRuleContext {
    public TypeParameterModifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TypeParameterModifierContext> typeParameterModifier() {
      return getRuleContexts(TypeParameterModifierContext.class);
    }

    public TypeParameterModifierContext typeParameterModifier(int i) {
      return getRuleContext(TypeParameterModifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameterModifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeParameterModifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeParameterModifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeParameterModifiers(this);
    }
  }

  public static class TypeParameterModifierContext extends ParserRuleContext {
    public TypeParameterModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ReificationModifierContext reificationModifier() {
      return getRuleContext(ReificationModifierContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    public VarianceModifierContext varianceModifier() {
      return getRuleContext(VarianceModifierContext.class, 0);
    }

    public AnnotationContext annotation() {
      return getRuleContext(AnnotationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameterModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitTypeParameterModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterTypeParameterModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitTypeParameterModifier(this);
    }
  }

  public static class FunctionModifierContext extends ParserRuleContext {
    public FunctionModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode TAILREC() {
      return getToken(KotlinParser.TAILREC, 0);
    }

    public TerminalNode OPERATOR() {
      return getToken(KotlinParser.OPERATOR, 0);
    }

    public TerminalNode INFIX() {
      return getToken(KotlinParser.INFIX, 0);
    }

    public TerminalNode INLINE() {
      return getToken(KotlinParser.INLINE, 0);
    }

    public TerminalNode EXTERNAL() {
      return getToken(KotlinParser.EXTERNAL, 0);
    }

    public TerminalNode SUSPEND() {
      return getToken(KotlinParser.SUSPEND, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitFunctionModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterFunctionModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitFunctionModifier(this);
    }
  }

  public static class PropertyModifierContext extends ParserRuleContext {
    public PropertyModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode CONST() {
      return getToken(KotlinParser.CONST, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_propertyModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPropertyModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPropertyModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPropertyModifier(this);
    }
  }

  public static class InheritanceModifierContext extends ParserRuleContext {
    public InheritanceModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode ABSTRACT() {
      return getToken(KotlinParser.ABSTRACT, 0);
    }

    public TerminalNode FINAL() {
      return getToken(KotlinParser.FINAL, 0);
    }

    public TerminalNode OPEN() {
      return getToken(KotlinParser.OPEN, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inheritanceModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitInheritanceModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterInheritanceModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitInheritanceModifier(this);
    }
  }

  public static class ParameterModifierContext extends ParserRuleContext {
    public ParameterModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode VARARG() {
      return getToken(KotlinParser.VARARG, 0);
    }

    public TerminalNode NOINLINE() {
      return getToken(KotlinParser.NOINLINE, 0);
    }

    public TerminalNode CROSSINLINE() {
      return getToken(KotlinParser.CROSSINLINE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameterModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitParameterModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterParameterModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitParameterModifier(this);
    }
  }

  public static class ReificationModifierContext extends ParserRuleContext {
    public ReificationModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode REIFIED() {
      return getToken(KotlinParser.REIFIED, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_reificationModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitReificationModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterReificationModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitReificationModifier(this);
    }
  }

  public static class PlatformModifierContext extends ParserRuleContext {
    public PlatformModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode EXPECT() {
      return getToken(KotlinParser.EXPECT, 0);
    }

    public TerminalNode ACTUAL() {
      return getToken(KotlinParser.ACTUAL, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_platformModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitPlatformModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterPlatformModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitPlatformModifier(this);
    }
  }

  public static class AnnotationContext extends ParserRuleContext {
    public AnnotationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SingleAnnotationContext singleAnnotation() {
      return getRuleContext(SingleAnnotationContext.class, 0);
    }

    public MultiAnnotationContext multiAnnotation() {
      return getRuleContext(MultiAnnotationContext.class, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAnnotation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAnnotation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAnnotation(this);
    }
  }

  public static class SingleAnnotationContext extends ParserRuleContext {
    public SingleAnnotationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public UnescapedAnnotationContext unescapedAnnotation() {
      return getRuleContext(UnescapedAnnotationContext.class, 0);
    }

    public AnnotationUseSiteTargetContext annotationUseSiteTarget() {
      return getRuleContext(AnnotationUseSiteTargetContext.class, 0);
    }

    public TerminalNode AT_NO_WS() {
      return getToken(KotlinParser.AT_NO_WS, 0);
    }

    public TerminalNode AT_PRE_WS() {
      return getToken(KotlinParser.AT_PRE_WS, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_singleAnnotation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSingleAnnotation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSingleAnnotation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSingleAnnotation(this);
    }
  }

  public static class MultiAnnotationContext extends ParserRuleContext {
    public MultiAnnotationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LSQUARE() {
      return getToken(KotlinParser.LSQUARE, 0);
    }

    public TerminalNode RSQUARE() {
      return getToken(KotlinParser.RSQUARE, 0);
    }

    public AnnotationUseSiteTargetContext annotationUseSiteTarget() {
      return getRuleContext(AnnotationUseSiteTargetContext.class, 0);
    }

    public TerminalNode AT_NO_WS() {
      return getToken(KotlinParser.AT_NO_WS, 0);
    }

    public TerminalNode AT_PRE_WS() {
      return getToken(KotlinParser.AT_PRE_WS, 0);
    }

    public List<UnescapedAnnotationContext> unescapedAnnotation() {
      return getRuleContexts(UnescapedAnnotationContext.class);
    }

    public UnescapedAnnotationContext unescapedAnnotation(int i) {
      return getRuleContext(UnescapedAnnotationContext.class, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiAnnotation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitMultiAnnotation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterMultiAnnotation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitMultiAnnotation(this);
    }
  }

  public static class AnnotationUseSiteTargetContext extends ParserRuleContext {
    public AnnotationUseSiteTargetContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode COLON() {
      return getToken(KotlinParser.COLON, 0);
    }

    public TerminalNode AT_NO_WS() {
      return getToken(KotlinParser.AT_NO_WS, 0);
    }

    public TerminalNode AT_PRE_WS() {
      return getToken(KotlinParser.AT_PRE_WS, 0);
    }

    public TerminalNode FIELD() {
      return getToken(KotlinParser.FIELD, 0);
    }

    public TerminalNode PROPERTY() {
      return getToken(KotlinParser.PROPERTY, 0);
    }

    public TerminalNode GET() {
      return getToken(KotlinParser.GET, 0);
    }

    public TerminalNode SET() {
      return getToken(KotlinParser.SET, 0);
    }

    public TerminalNode RECEIVER() {
      return getToken(KotlinParser.RECEIVER, 0);
    }

    public TerminalNode PARAM() {
      return getToken(KotlinParser.PARAM, 0);
    }

    public TerminalNode SETPARAM() {
      return getToken(KotlinParser.SETPARAM, 0);
    }

    public TerminalNode DELEGATE() {
      return getToken(KotlinParser.DELEGATE, 0);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_annotationUseSiteTarget;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitAnnotationUseSiteTarget(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterAnnotationUseSiteTarget(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitAnnotationUseSiteTarget(this);
    }
  }

  public static class UnescapedAnnotationContext extends ParserRuleContext {
    public UnescapedAnnotationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ConstructorInvocationContext constructorInvocation() {
      return getRuleContext(ConstructorInvocationContext.class, 0);
    }

    public UserTypeContext userType() {
      return getRuleContext(UserTypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_unescapedAnnotation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitUnescapedAnnotation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterUnescapedAnnotation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitUnescapedAnnotation(this);
    }
  }

  public static class SimpleIdentifierContext extends ParserRuleContext {
    public SimpleIdentifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(KotlinParser.Identifier, 0);
    }

    public TerminalNode ABSTRACT() {
      return getToken(KotlinParser.ABSTRACT, 0);
    }

    public TerminalNode ANNOTATION() {
      return getToken(KotlinParser.ANNOTATION, 0);
    }

    public TerminalNode BY() {
      return getToken(KotlinParser.BY, 0);
    }

    public TerminalNode CATCH() {
      return getToken(KotlinParser.CATCH, 0);
    }

    public TerminalNode COMPANION() {
      return getToken(KotlinParser.COMPANION, 0);
    }

    public TerminalNode CONSTRUCTOR() {
      return getToken(KotlinParser.CONSTRUCTOR, 0);
    }

    public TerminalNode CROSSINLINE() {
      return getToken(KotlinParser.CROSSINLINE, 0);
    }

    public TerminalNode DATA() {
      return getToken(KotlinParser.DATA, 0);
    }

    public TerminalNode DYNAMIC() {
      return getToken(KotlinParser.DYNAMIC, 0);
    }

    public TerminalNode ENUM() {
      return getToken(KotlinParser.ENUM, 0);
    }

    public TerminalNode EXTERNAL() {
      return getToken(KotlinParser.EXTERNAL, 0);
    }

    public TerminalNode FINAL() {
      return getToken(KotlinParser.FINAL, 0);
    }

    public TerminalNode FINALLY() {
      return getToken(KotlinParser.FINALLY, 0);
    }

    public TerminalNode GET() {
      return getToken(KotlinParser.GET, 0);
    }

    public TerminalNode IMPORT() {
      return getToken(KotlinParser.IMPORT, 0);
    }

    public TerminalNode INFIX() {
      return getToken(KotlinParser.INFIX, 0);
    }

    public TerminalNode INIT() {
      return getToken(KotlinParser.INIT, 0);
    }

    public TerminalNode INLINE() {
      return getToken(KotlinParser.INLINE, 0);
    }

    public TerminalNode INNER() {
      return getToken(KotlinParser.INNER, 0);
    }

    public TerminalNode INTERNAL() {
      return getToken(KotlinParser.INTERNAL, 0);
    }

    public TerminalNode LATEINIT() {
      return getToken(KotlinParser.LATEINIT, 0);
    }

    public TerminalNode NOINLINE() {
      return getToken(KotlinParser.NOINLINE, 0);
    }

    public TerminalNode OPEN() {
      return getToken(KotlinParser.OPEN, 0);
    }

    public TerminalNode OPERATOR() {
      return getToken(KotlinParser.OPERATOR, 0);
    }

    public TerminalNode OUT() {
      return getToken(KotlinParser.OUT, 0);
    }

    public TerminalNode OVERRIDE() {
      return getToken(KotlinParser.OVERRIDE, 0);
    }

    public TerminalNode PRIVATE() {
      return getToken(KotlinParser.PRIVATE, 0);
    }

    public TerminalNode PROTECTED() {
      return getToken(KotlinParser.PROTECTED, 0);
    }

    public TerminalNode PUBLIC() {
      return getToken(KotlinParser.PUBLIC, 0);
    }

    public TerminalNode REIFIED() {
      return getToken(KotlinParser.REIFIED, 0);
    }

    public TerminalNode SEALED() {
      return getToken(KotlinParser.SEALED, 0);
    }

    public TerminalNode TAILREC() {
      return getToken(KotlinParser.TAILREC, 0);
    }

    public TerminalNode SET() {
      return getToken(KotlinParser.SET, 0);
    }

    public TerminalNode VARARG() {
      return getToken(KotlinParser.VARARG, 0);
    }

    public TerminalNode WHERE() {
      return getToken(KotlinParser.WHERE, 0);
    }

    public TerminalNode FIELD() {
      return getToken(KotlinParser.FIELD, 0);
    }

    public TerminalNode PROPERTY() {
      return getToken(KotlinParser.PROPERTY, 0);
    }

    public TerminalNode RECEIVER() {
      return getToken(KotlinParser.RECEIVER, 0);
    }

    public TerminalNode PARAM() {
      return getToken(KotlinParser.PARAM, 0);
    }

    public TerminalNode SETPARAM() {
      return getToken(KotlinParser.SETPARAM, 0);
    }

    public TerminalNode DELEGATE() {
      return getToken(KotlinParser.DELEGATE, 0);
    }

    public TerminalNode FILE() {
      return getToken(KotlinParser.FILE, 0);
    }

    public TerminalNode EXPECT() {
      return getToken(KotlinParser.EXPECT, 0);
    }

    public TerminalNode ACTUAL() {
      return getToken(KotlinParser.ACTUAL, 0);
    }

    public TerminalNode CONST() {
      return getToken(KotlinParser.CONST, 0);
    }

    public TerminalNode SUSPEND() {
      return getToken(KotlinParser.SUSPEND, 0);
    }

    public TerminalNode VALUE() {
      return getToken(KotlinParser.VALUE, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleIdentifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitSimpleIdentifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterSimpleIdentifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitSimpleIdentifier(this);
    }
  }

  public static class IdentifierContext extends ParserRuleContext {
    public IdentifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<SimpleIdentifierContext> simpleIdentifier() {
      return getRuleContexts(SimpleIdentifierContext.class);
    }

    public SimpleIdentifierContext simpleIdentifier(int i) {
      return getRuleContext(SimpleIdentifierContext.class, i);
    }

    public List<TerminalNode> DOT() {
      return getTokens(KotlinParser.DOT);
    }

    public TerminalNode DOT(int i) {
      return getToken(KotlinParser.DOT, i);
    }

    public List<TerminalNode> NL() {
      return getTokens(KotlinParser.NL);
    }

    public TerminalNode NL(int i) {
      return getToken(KotlinParser.NL, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_identifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof KotlinParserVisitor)
        return ((KotlinParserVisitor<? extends T>) visitor).visitIdentifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).enterIdentifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof KotlinParserListener)
        ((KotlinParserListener) listener).exitIdentifier(this);
    }
  }
}
