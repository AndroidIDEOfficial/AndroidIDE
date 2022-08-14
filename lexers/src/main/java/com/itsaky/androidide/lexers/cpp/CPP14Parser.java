// Generated from CPP14Parser.g4 by ANTLR 4.9.3
package com.itsaky.androidide.lexers.cpp;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
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
public class CPP14Parser extends Parser {
  public static final int IntegerLiteral = 1,
      CharacterLiteral = 2,
      FloatingLiteral = 3,
      StringLiteral = 4,
      BooleanLiteral = 5,
      PointerLiteral = 6,
      UserDefinedLiteral = 7,
      MultiLineMacro = 8,
      Directive = 9,
      Alignas = 10,
      Alignof = 11,
      Asm = 12,
      Auto = 13,
      Bool = 14,
      Break = 15,
      Case = 16,
      Catch = 17,
      Char = 18,
      Char16 = 19,
      Char32 = 20,
      Class = 21,
      Const = 22,
      Constexpr = 23,
      Const_cast = 24,
      Continue = 25,
      Decltype = 26,
      Default = 27,
      Delete = 28,
      Do = 29,
      Double = 30,
      Dynamic_cast = 31,
      Else = 32,
      Enum = 33,
      Explicit = 34,
      Export = 35,
      Extern = 36,
      False_ = 37,
      Final = 38,
      Float = 39,
      For = 40,
      Friend = 41,
      Goto = 42,
      If = 43,
      Inline = 44,
      Int = 45,
      Long = 46,
      Mutable = 47,
      Namespace = 48,
      New = 49,
      Noexcept = 50,
      Nullptr = 51,
      Operator = 52,
      Override = 53,
      Private = 54,
      Protected = 55,
      Public = 56,
      Register = 57,
      Reinterpret_cast = 58,
      Return = 59,
      Short = 60,
      Signed = 61,
      Sizeof = 62,
      Static = 63,
      Static_assert = 64,
      Static_cast = 65,
      Struct = 66,
      Switch = 67,
      Template = 68,
      This = 69,
      Thread_local = 70,
      Throw = 71,
      True_ = 72,
      Try = 73,
      Typedef = 74,
      Typeid_ = 75,
      Typename_ = 76,
      Union = 77,
      Unsigned = 78,
      Using = 79,
      Virtual = 80,
      Void = 81,
      Volatile = 82,
      Wchar = 83,
      While = 84,
      LeftParen = 85,
      RightParen = 86,
      LeftBracket = 87,
      RightBracket = 88,
      LeftBrace = 89,
      RightBrace = 90,
      Plus = 91,
      Minus = 92,
      Star = 93,
      Div = 94,
      Mod = 95,
      Caret = 96,
      And = 97,
      Or = 98,
      Tilde = 99,
      Not = 100,
      Assign = 101,
      Less = 102,
      Greater = 103,
      PlusAssign = 104,
      MinusAssign = 105,
      StarAssign = 106,
      DivAssign = 107,
      ModAssign = 108,
      XorAssign = 109,
      AndAssign = 110,
      OrAssign = 111,
      LeftShiftAssign = 112,
      RightShiftAssign = 113,
      Equal = 114,
      NotEqual = 115,
      LessEqual = 116,
      GreaterEqual = 117,
      AndAnd = 118,
      OrOr = 119,
      PlusPlus = 120,
      MinusMinus = 121,
      Comma = 122,
      ArrowStar = 123,
      Arrow = 124,
      Question = 125,
      Colon = 126,
      Doublecolon = 127,
      Semi = 128,
      Dot = 129,
      DotStar = 130,
      Ellipsis = 131,
      Identifier = 132,
      DecimalLiteral = 133,
      OctalLiteral = 134,
      HexadecimalLiteral = 135,
      BinaryLiteral = 136,
      Integersuffix = 137,
      UserDefinedIntegerLiteral = 138,
      UserDefinedFloatingLiteral = 139,
      UserDefinedStringLiteral = 140,
      UserDefinedCharacterLiteral = 141,
      Whitespace = 142,
      Newline = 143,
      BlockComment = 144,
      LineComment = 145;
  public static final int RULE_translationUnit = 0,
      RULE_primaryExpression = 1,
      RULE_idExpression = 2,
      RULE_unqualifiedId = 3,
      RULE_qualifiedId = 4,
      RULE_nestedNameSpecifier = 5,
      RULE_lambdaExpression = 6,
      RULE_lambdaIntroducer = 7,
      RULE_lambdaCapture = 8,
      RULE_captureDefault = 9,
      RULE_captureList = 10,
      RULE_capture = 11,
      RULE_simpleCapture = 12,
      RULE_initcapture = 13,
      RULE_lambdaDeclarator = 14,
      RULE_postfixExpression = 15,
      RULE_typeIdOfTheTypeId = 16,
      RULE_expressionList = 17,
      RULE_pseudoDestructorName = 18,
      RULE_unaryExpression = 19,
      RULE_unaryOperator = 20,
      RULE_newExpression = 21,
      RULE_newPlacement = 22,
      RULE_newTypeId = 23,
      RULE_newDeclarator = 24,
      RULE_noPointerNewDeclarator = 25,
      RULE_newInitializer = 26,
      RULE_deleteExpression = 27,
      RULE_noExceptExpression = 28,
      RULE_castExpression = 29,
      RULE_pointerMemberExpression = 30,
      RULE_multiplicativeExpression = 31,
      RULE_additiveExpression = 32,
      RULE_shiftExpression = 33,
      RULE_shiftOperator = 34,
      RULE_relationalExpression = 35,
      RULE_equalityExpression = 36,
      RULE_andExpression = 37,
      RULE_exclusiveOrExpression = 38,
      RULE_inclusiveOrExpression = 39,
      RULE_logicalAndExpression = 40,
      RULE_logicalOrExpression = 41,
      RULE_conditionalExpression = 42,
      RULE_assignmentExpression = 43,
      RULE_assignmentOperator = 44,
      RULE_expression = 45,
      RULE_constantExpression = 46,
      RULE_statement = 47,
      RULE_labeledStatement = 48,
      RULE_expressionStatement = 49,
      RULE_compoundStatement = 50,
      RULE_statementSeq = 51,
      RULE_selectionStatement = 52,
      RULE_condition = 53,
      RULE_iterationStatement = 54,
      RULE_forInitStatement = 55,
      RULE_forRangeDeclaration = 56,
      RULE_forRangeInitializer = 57,
      RULE_jumpStatement = 58,
      RULE_declarationStatement = 59,
      RULE_declarationseq = 60,
      RULE_declaration = 61,
      RULE_blockDeclaration = 62,
      RULE_aliasDeclaration = 63,
      RULE_simpleDeclaration = 64,
      RULE_staticAssertDeclaration = 65,
      RULE_emptyDeclaration = 66,
      RULE_attributeDeclaration = 67,
      RULE_declSpecifier = 68,
      RULE_declSpecifierSeq = 69,
      RULE_storageClassSpecifier = 70,
      RULE_functionSpecifier = 71,
      RULE_typedefName = 72,
      RULE_typeSpecifier = 73,
      RULE_trailingTypeSpecifier = 74,
      RULE_typeSpecifierSeq = 75,
      RULE_trailingTypeSpecifierSeq = 76,
      RULE_simpleTypeLengthModifier = 77,
      RULE_simpleTypeSignednessModifier = 78,
      RULE_simpleTypeSpecifier = 79,
      RULE_theTypeName = 80,
      RULE_decltypeSpecifier = 81,
      RULE_elaboratedTypeSpecifier = 82,
      RULE_enumName = 83,
      RULE_enumSpecifier = 84,
      RULE_enumHead = 85,
      RULE_opaqueEnumDeclaration = 86,
      RULE_enumkey = 87,
      RULE_enumbase = 88,
      RULE_enumeratorList = 89,
      RULE_enumeratorDefinition = 90,
      RULE_enumerator = 91,
      RULE_namespaceName = 92,
      RULE_originalNamespaceName = 93,
      RULE_namespaceDefinition = 94,
      RULE_namespaceAlias = 95,
      RULE_namespaceAliasDefinition = 96,
      RULE_qualifiednamespacespecifier = 97,
      RULE_usingDeclaration = 98,
      RULE_usingDirective = 99,
      RULE_asmDefinition = 100,
      RULE_linkageSpecification = 101,
      RULE_attributeSpecifierSeq = 102,
      RULE_attributeSpecifier = 103,
      RULE_alignmentspecifier = 104,
      RULE_attributeList = 105,
      RULE_attribute = 106,
      RULE_attributeNamespace = 107,
      RULE_attributeArgumentClause = 108,
      RULE_balancedTokenSeq = 109,
      RULE_balancedtoken = 110,
      RULE_initDeclaratorList = 111,
      RULE_initDeclarator = 112,
      RULE_declarator = 113,
      RULE_pointerDeclarator = 114,
      RULE_noPointerDeclarator = 115,
      RULE_parametersAndQualifiers = 116,
      RULE_trailingReturnType = 117,
      RULE_pointerOperator = 118,
      RULE_cvqualifierseq = 119,
      RULE_cvQualifier = 120,
      RULE_refqualifier = 121,
      RULE_declaratorid = 122,
      RULE_theTypeId = 123,
      RULE_abstractDeclarator = 124,
      RULE_pointerAbstractDeclarator = 125,
      RULE_noPointerAbstractDeclarator = 126,
      RULE_abstractPackDeclarator = 127,
      RULE_noPointerAbstractPackDeclarator = 128,
      RULE_parameterDeclarationClause = 129,
      RULE_parameterDeclarationList = 130,
      RULE_parameterDeclaration = 131,
      RULE_functionDefinition = 132,
      RULE_functionBody = 133,
      RULE_initializer = 134,
      RULE_braceOrEqualInitializer = 135,
      RULE_initializerClause = 136,
      RULE_initializerList = 137,
      RULE_bracedInitList = 138,
      RULE_className = 139,
      RULE_classSpecifier = 140,
      RULE_classHead = 141,
      RULE_classHeadName = 142,
      RULE_classVirtSpecifier = 143,
      RULE_classKey = 144,
      RULE_memberSpecification = 145,
      RULE_memberdeclaration = 146,
      RULE_memberDeclaratorList = 147,
      RULE_memberDeclarator = 148,
      RULE_virtualSpecifierSeq = 149,
      RULE_virtualSpecifier = 150,
      RULE_pureSpecifier = 151,
      RULE_baseClause = 152,
      RULE_baseSpecifierList = 153,
      RULE_baseSpecifier = 154,
      RULE_classOrDeclType = 155,
      RULE_baseTypeSpecifier = 156,
      RULE_accessSpecifier = 157,
      RULE_conversionFunctionId = 158,
      RULE_conversionTypeId = 159,
      RULE_conversionDeclarator = 160,
      RULE_constructorInitializer = 161,
      RULE_memInitializerList = 162,
      RULE_memInitializer = 163,
      RULE_meminitializerid = 164,
      RULE_operatorFunctionId = 165,
      RULE_literalOperatorId = 166,
      RULE_templateDeclaration = 167,
      RULE_templateparameterList = 168,
      RULE_templateParameter = 169,
      RULE_typeParameter = 170,
      RULE_simpleTemplateId = 171,
      RULE_templateId = 172,
      RULE_templateName = 173,
      RULE_templateArgumentList = 174,
      RULE_templateArgument = 175,
      RULE_typeNameSpecifier = 176,
      RULE_explicitInstantiation = 177,
      RULE_explicitSpecialization = 178,
      RULE_tryBlock = 179,
      RULE_functionTryBlock = 180,
      RULE_handlerSeq = 181,
      RULE_handler = 182,
      RULE_exceptionDeclaration = 183,
      RULE_throwExpression = 184,
      RULE_exceptionSpecification = 185,
      RULE_dynamicExceptionSpecification = 186,
      RULE_typeIdList = 187,
      RULE_noeExceptSpecification = 188,
      RULE_theOperator = 189,
      RULE_literal = 190;
  public static final String[] ruleNames = makeRuleNames();
  /**
   * @deprecated Use {@link #VOCABULARY} instead.
   */
  @Deprecated public static final String[] tokenNames;

  public static final String _serializedATN =
      "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u0093\u0840\4\2\t"
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
          + "\4\u00ae\t\u00ae\4\u00af\t\u00af\4\u00b0\t\u00b0\4\u00b1\t\u00b1\4\u00b2"
          + "\t\u00b2\4\u00b3\t\u00b3\4\u00b4\t\u00b4\4\u00b5\t\u00b5\4\u00b6\t\u00b6"
          + "\4\u00b7\t\u00b7\4\u00b8\t\u00b8\4\u00b9\t\u00b9\4\u00ba\t\u00ba\4\u00bb"
          + "\t\u00bb\4\u00bc\t\u00bc\4\u00bd\t\u00bd\4\u00be\t\u00be\4\u00bf\t\u00bf"
          + "\4\u00c0\t\u00c0\3\2\5\2\u0182\n\2\3\2\3\2\3\3\6\3\u0187\n\3\r\3\16\3"
          + "\u0188\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u0192\n\3\3\4\3\4\5\4\u0196\n\4"
          + "\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\u019f\n\5\3\5\5\5\u01a2\n\5\3\6\3\6\5"
          + "\6\u01a6\n\6\3\6\3\6\3\7\3\7\3\7\3\7\5\7\u01ae\n\7\3\7\3\7\3\7\3\7\3\7"
          + "\5\7\u01b5\n\7\3\7\5\7\u01b8\n\7\3\7\7\7\u01bb\n\7\f\7\16\7\u01be\13\7"
          + "\3\b\3\b\5\b\u01c2\n\b\3\b\3\b\3\t\3\t\5\t\u01c8\n\t\3\t\3\t\3\n\3\n\3"
          + "\n\3\n\5\n\u01d0\n\n\5\n\u01d2\n\n\3\13\3\13\3\f\3\f\3\f\7\f\u01d9\n\f"
          + "\f\f\16\f\u01dc\13\f\3\f\5\f\u01df\n\f\3\r\3\r\5\r\u01e3\n\r\3\16\5\16"
          + "\u01e6\n\16\3\16\3\16\5\16\u01ea\n\16\3\17\5\17\u01ed\n\17\3\17\3\17\3"
          + "\17\3\20\3\20\5\20\u01f4\n\20\3\20\3\20\5\20\u01f8\n\20\3\20\5\20\u01fb"
          + "\n\20\3\20\5\20\u01fe\n\20\3\20\5\20\u0201\n\20\3\21\3\21\3\21\3\21\5"
          + "\21\u0207\n\21\3\21\3\21\5\21\u020b\n\21\3\21\3\21\5\21\u020f\n\21\3\21"
          + "\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u021d\n\21"
          + "\3\21\3\21\5\21\u0221\n\21\3\21\3\21\3\21\3\21\5\21\u0227\n\21\3\21\3"
          + "\21\3\21\3\21\3\21\5\21\u022e\n\21\3\21\3\21\3\21\3\21\5\21\u0234\n\21"
          + "\3\21\3\21\5\21\u0238\n\21\3\21\3\21\7\21\u023c\n\21\f\21\16\21\u023f"
          + "\13\21\3\22\3\22\3\23\3\23\3\24\5\24\u0246\n\24\3\24\3\24\3\24\5\24\u024b"
          + "\n\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u0258"
          + "\n\24\3\25\3\25\3\25\3\25\3\25\5\25\u025f\n\25\3\25\3\25\3\25\3\25\3\25"
          + "\3\25\3\25\3\25\3\25\3\25\5\25\u026b\n\25\3\25\3\25\3\25\3\25\3\25\3\25"
          + "\3\25\3\25\5\25\u0275\n\25\3\26\3\26\3\27\5\27\u027a\n\27\3\27\3\27\5"
          + "\27\u027e\n\27\3\27\3\27\3\27\3\27\3\27\5\27\u0285\n\27\3\27\5\27\u0288"
          + "\n\27\3\30\3\30\3\30\3\30\3\31\3\31\5\31\u0290\n\31\3\32\3\32\5\32\u0294"
          + "\n\32\3\32\5\32\u0297\n\32\3\33\3\33\3\33\3\33\3\33\5\33\u029e\n\33\3"
          + "\33\3\33\3\33\3\33\3\33\5\33\u02a5\n\33\7\33\u02a7\n\33\f\33\16\33\u02aa"
          + "\13\33\3\34\3\34\5\34\u02ae\n\34\3\34\3\34\5\34\u02b2\n\34\3\35\5\35\u02b5"
          + "\n\35\3\35\3\35\3\35\5\35\u02ba\n\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36"
          + "\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u02c9\n\37\3 \3 \3 \7 \u02ce\n \f"
          + " \16 \u02d1\13 \3!\3!\3!\7!\u02d6\n!\f!\16!\u02d9\13!\3\"\3\"\3\"\7\""
          + "\u02de\n\"\f\"\16\"\u02e1\13\"\3#\3#\3#\3#\7#\u02e7\n#\f#\16#\u02ea\13"
          + "#\3$\3$\3$\3$\5$\u02f0\n$\3%\3%\3%\7%\u02f5\n%\f%\16%\u02f8\13%\3&\3&"
          + "\3&\7&\u02fd\n&\f&\16&\u0300\13&\3\'\3\'\3\'\7\'\u0305\n\'\f\'\16\'\u0308"
          + "\13\'\3(\3(\3(\7(\u030d\n(\f(\16(\u0310\13(\3)\3)\3)\7)\u0315\n)\f)\16"
          + ")\u0318\13)\3*\3*\3*\7*\u031d\n*\f*\16*\u0320\13*\3+\3+\3+\7+\u0325\n"
          + "+\f+\16+\u0328\13+\3,\3,\3,\3,\3,\3,\5,\u0330\n,\3-\3-\3-\3-\3-\3-\5-"
          + "\u0338\n-\3.\3.\3/\3/\3/\7/\u033f\n/\f/\16/\u0342\13/\3\60\3\60\3\61\3"
          + "\61\3\61\5\61\u0349\n\61\3\61\3\61\3\61\3\61\3\61\3\61\5\61\u0351\n\61"
          + "\5\61\u0353\n\61\3\62\5\62\u0356\n\62\3\62\3\62\3\62\3\62\5\62\u035c\n"
          + "\62\3\62\3\62\3\62\3\63\5\63\u0362\n\63\3\63\3\63\3\64\3\64\5\64\u0368"
          + "\n\64\3\64\3\64\3\65\6\65\u036d\n\65\r\65\16\65\u036e\3\66\3\66\3\66\3"
          + "\66\3\66\3\66\3\66\5\66\u0378\n\66\3\66\3\66\3\66\3\66\3\66\3\66\5\66"
          + "\u0380\n\66\3\67\3\67\5\67\u0384\n\67\3\67\3\67\3\67\3\67\3\67\5\67\u038b"
          + "\n\67\5\67\u038d\n\67\38\38\38\38\38\38\38\38\38\38\38\38\38\38\38\38"
          + "\38\38\58\u03a1\n8\38\38\58\u03a5\n8\38\38\38\38\58\u03ab\n8\38\38\38"
          + "\58\u03b0\n8\39\39\59\u03b4\n9\3:\5:\u03b7\n:\3:\3:\3:\3;\3;\5;\u03be"
          + "\n;\3<\3<\3<\3<\3<\5<\u03c5\n<\3<\3<\5<\u03c9\n<\3<\3<\3=\3=\3>\6>\u03d0"
          + "\n>\r>\16>\u03d1\3?\3?\3?\3?\3?\3?\3?\3?\3?\5?\u03dd\n?\3@\3@\3@\3@\3"
          + "@\3@\3@\3@\5@\u03e7\n@\3A\3A\3A\5A\u03ec\nA\3A\3A\3A\3A\3B\5B\u03f3\n"
          + "B\3B\5B\u03f6\nB\3B\3B\3B\5B\u03fb\nB\3B\3B\3B\5B\u0400\nB\3C\3C\3C\3"
          + "C\3C\3C\3C\3C\3D\3D\3E\3E\3E\3F\3F\3F\3F\3F\3F\5F\u0415\nF\3G\6G\u0418"
          + "\nG\rG\16G\u0419\3G\5G\u041d\nG\3H\3H\3I\3I\3J\3J\3K\3K\3K\5K\u0428\n"
          + "K\3L\3L\3L\3L\5L\u042e\nL\3M\6M\u0431\nM\rM\16M\u0432\3M\5M\u0436\nM\3"
          + "N\6N\u0439\nN\rN\16N\u043a\3N\5N\u043e\nN\3O\3O\3P\3P\3Q\5Q\u0445\nQ\3"
          + "Q\3Q\3Q\3Q\3Q\3Q\3Q\5Q\u044e\nQ\3Q\6Q\u0451\nQ\rQ\16Q\u0452\3Q\5Q\u0456"
          + "\nQ\3Q\3Q\5Q\u045a\nQ\3Q\3Q\5Q\u045e\nQ\3Q\3Q\5Q\u0462\nQ\3Q\3Q\3Q\5Q"
          + "\u0467\nQ\3Q\7Q\u046a\nQ\fQ\16Q\u046d\13Q\3Q\3Q\3Q\5Q\u0472\nQ\3Q\3Q\3"
          + "Q\3Q\5Q\u0478\nQ\3R\3R\3R\3R\5R\u047e\nR\3S\3S\3S\3S\5S\u0484\nS\3S\3"
          + "S\3T\3T\5T\u048a\nT\3T\5T\u048d\nT\3T\3T\3T\3T\5T\u0493\nT\3T\3T\5T\u0497"
          + "\nT\3T\3T\5T\u049b\nT\3T\5T\u049e\nT\3U\3U\3V\3V\3V\3V\5V\u04a6\nV\5V"
          + "\u04a8\nV\3V\3V\3W\3W\5W\u04ae\nW\3W\5W\u04b1\nW\3W\5W\u04b4\nW\3W\5W"
          + "\u04b7\nW\3X\3X\5X\u04bb\nX\3X\3X\5X\u04bf\nX\3X\3X\3Y\3Y\5Y\u04c5\nY"
          + "\3Z\3Z\3Z\3[\3[\3[\7[\u04cd\n[\f[\16[\u04d0\13[\3\\\3\\\3\\\5\\\u04d5"
          + "\n\\\3]\3]\3^\3^\5^\u04db\n^\3_\3_\3`\5`\u04e0\n`\3`\3`\3`\5`\u04e5\n"
          + "`\3`\3`\5`\u04e9\n`\3`\3`\3a\3a\3b\3b\3b\3b\3b\3b\3c\5c\u04f6\nc\3c\3"
          + "c\3d\3d\5d\u04fc\nd\3d\3d\5d\u0500\nd\3d\3d\3d\3e\5e\u0506\ne\3e\3e\3"
          + "e\5e\u050b\ne\3e\3e\3e\3f\3f\3f\3f\3f\3f\3g\3g\3g\3g\5g\u051a\ng\3g\3"
          + "g\5g\u051e\ng\3h\6h\u0521\nh\rh\16h\u0522\3i\3i\3i\5i\u0528\ni\3i\3i\3"
          + "i\5i\u052d\ni\3j\3j\3j\3j\5j\u0533\nj\3j\5j\u0536\nj\3j\3j\3k\3k\3k\7"
          + "k\u053d\nk\fk\16k\u0540\13k\3k\5k\u0543\nk\3l\3l\3l\5l\u0548\nl\3l\3l"
          + "\5l\u054c\nl\3m\3m\3n\3n\5n\u0552\nn\3n\3n\3o\6o\u0557\no\ro\16o\u0558"
          + "\3p\3p\3p\3p\3p\3p\3p\3p\3p\3p\3p\3p\3p\6p\u0568\np\rp\16p\u0569\5p\u056c"
          + "\np\3q\3q\3q\7q\u0571\nq\fq\16q\u0574\13q\3r\3r\5r\u0578\nr\3s\3s\3s\3"
          + "s\3s\5s\u057f\ns\3t\3t\5t\u0583\nt\7t\u0585\nt\ft\16t\u0588\13t\3t\3t"
          + "\3u\3u\3u\5u\u058f\nu\3u\3u\3u\3u\5u\u0595\nu\3u\3u\3u\3u\5u\u059b\nu"
          + "\3u\3u\5u\u059f\nu\5u\u05a1\nu\7u\u05a3\nu\fu\16u\u05a6\13u\3v\3v\5v\u05aa"
          + "\nv\3v\3v\5v\u05ae\nv\3v\5v\u05b1\nv\3v\5v\u05b4\nv\3v\5v\u05b7\nv\3w"
          + "\3w\3w\5w\u05bc\nw\3x\3x\5x\u05c0\nx\3x\5x\u05c3\nx\3x\3x\5x\u05c7\nx"
          + "\3x\5x\u05ca\nx\5x\u05cc\nx\3y\6y\u05cf\ny\ry\16y\u05d0\3z\3z\3{\3{\3"
          + "|\5|\u05d8\n|\3|\3|\3}\3}\5}\u05de\n}\3~\3~\5~\u05e2\n~\3~\3~\3~\3~\5"
          + "~\u05e8\n~\3\177\3\177\6\177\u05ec\n\177\r\177\16\177\u05ed\3\177\5\177"
          + "\u05f1\n\177\5\177\u05f3\n\177\3\u0080\3\u0080\3\u0080\3\u0080\5\u0080"
          + "\u05f9\n\u0080\3\u0080\3\u0080\5\u0080\u05fd\n\u0080\3\u0080\3\u0080\3"
          + "\u0080\3\u0080\5\u0080\u0603\n\u0080\3\u0080\3\u0080\3\u0080\3\u0080\3"
          + "\u0080\5\u0080\u060a\n\u0080\3\u0080\3\u0080\5\u0080\u060e\n\u0080\5\u0080"
          + "\u0610\n\u0080\7\u0080\u0612\n\u0080\f\u0080\16\u0080\u0615\13\u0080\3"
          + "\u0081\7\u0081\u0618\n\u0081\f\u0081\16\u0081\u061b\13\u0081\3\u0081\3"
          + "\u0081\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\3\u0082\5\u0082"
          + "\u0626\n\u0082\3\u0082\3\u0082\5\u0082\u062a\n\u0082\5\u0082\u062c\n\u0082"
          + "\7\u0082\u062e\n\u0082\f\u0082\16\u0082\u0631\13\u0082\3\u0083\3\u0083"
          + "\5\u0083\u0635\n\u0083\3\u0083\5\u0083\u0638\n\u0083\3\u0084\3\u0084\3"
          + "\u0084\7\u0084\u063d\n\u0084\f\u0084\16\u0084\u0640\13\u0084\3\u0085\5"
          + "\u0085\u0643\n\u0085\3\u0085\3\u0085\3\u0085\5\u0085\u0648\n\u0085\5\u0085"
          + "\u064a\n\u0085\3\u0085\3\u0085\5\u0085\u064e\n\u0085\3\u0086\5\u0086\u0651"
          + "\n\u0086\3\u0086\5\u0086\u0654\n\u0086\3\u0086\3\u0086\5\u0086\u0658\n"
          + "\u0086\3\u0086\3\u0086\3\u0087\5\u0087\u065d\n\u0087\3\u0087\3\u0087\3"
          + "\u0087\3\u0087\3\u0087\5\u0087\u0664\n\u0087\3\u0088\3\u0088\3\u0088\3"
          + "\u0088\3\u0088\5\u0088\u066b\n\u0088\3\u0089\3\u0089\3\u0089\5\u0089\u0670"
          + "\n\u0089\3\u008a\3\u008a\5\u008a\u0674\n\u008a\3\u008b\3\u008b\5\u008b"
          + "\u0678\n\u008b\3\u008b\3\u008b\3\u008b\5\u008b\u067d\n\u008b\7\u008b\u067f"
          + "\n\u008b\f\u008b\16\u008b\u0682\13\u008b\3\u008c\3\u008c\3\u008c\5\u008c"
          + "\u0687\n\u008c\5\u008c\u0689\n\u008c\3\u008c\3\u008c\3\u008d\3\u008d\5"
          + "\u008d\u068f\n\u008d\3\u008e\3\u008e\3\u008e\5\u008e\u0694\n\u008e\3\u008e"
          + "\3\u008e\3\u008f\3\u008f\5\u008f\u069a\n\u008f\3\u008f\3\u008f\5\u008f"
          + "\u069e\n\u008f\5\u008f\u06a0\n\u008f\3\u008f\5\u008f\u06a3\n\u008f\3\u008f"
          + "\3\u008f\5\u008f\u06a7\n\u008f\3\u008f\3\u008f\5\u008f\u06ab\n\u008f\5"
          + "\u008f\u06ad\n\u008f\5\u008f\u06af\n\u008f\3\u0090\5\u0090\u06b2\n\u0090"
          + "\3\u0090\3\u0090\3\u0091\3\u0091\3\u0092\3\u0092\3\u0093\3\u0093\3\u0093"
          + "\3\u0093\6\u0093\u06be\n\u0093\r\u0093\16\u0093\u06bf\3\u0094\5\u0094"
          + "\u06c3\n\u0094\3\u0094\5\u0094\u06c6\n\u0094\3\u0094\5\u0094\u06c9\n\u0094"
          + "\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\3\u0094\5\u0094\u06d2"
          + "\n\u0094\3\u0095\3\u0095\3\u0095\7\u0095\u06d7\n\u0095\f\u0095\16\u0095"
          + "\u06da\13\u0095\3\u0096\3\u0096\5\u0096\u06de\n\u0096\3\u0096\5\u0096"
          + "\u06e1\n\u0096\3\u0096\5\u0096\u06e4\n\u0096\5\u0096\u06e6\n\u0096\3\u0096"
          + "\5\u0096\u06e9\n\u0096\3\u0096\5\u0096\u06ec\n\u0096\3\u0096\3\u0096\5"
          + "\u0096\u06f0\n\u0096\3\u0097\6\u0097\u06f3\n\u0097\r\u0097\16\u0097\u06f4"
          + "\3\u0098\3\u0098\3\u0099\3\u0099\3\u0099\3\u0099\3\u009a\3\u009a\3\u009a"
          + "\3\u009b\3\u009b\5\u009b\u0702\n\u009b\3\u009b\3\u009b\3\u009b\5\u009b"
          + "\u0707\n\u009b\7\u009b\u0709\n\u009b\f\u009b\16\u009b\u070c\13\u009b\3"
          + "\u009c\5\u009c\u070f\n\u009c\3\u009c\3\u009c\3\u009c\5\u009c\u0714\n\u009c"
          + "\3\u009c\3\u009c\3\u009c\5\u009c\u0719\n\u009c\3\u009c\3\u009c\5\u009c"
          + "\u071d\n\u009c\3\u009d\5\u009d\u0720\n\u009d\3\u009d\3\u009d\5\u009d\u0724"
          + "\n\u009d\3\u009e\3\u009e\3\u009f\3\u009f\3\u00a0\3\u00a0\3\u00a0\3\u00a1"
          + "\3\u00a1\5\u00a1\u072f\n\u00a1\3\u00a2\3\u00a2\5\u00a2\u0733\n\u00a2\3"
          + "\u00a3\3\u00a3\3\u00a3\3\u00a4\3\u00a4\5\u00a4\u073a\n\u00a4\3\u00a4\3"
          + "\u00a4\3\u00a4\5\u00a4\u073f\n\u00a4\7\u00a4\u0741\n\u00a4\f\u00a4\16"
          + "\u00a4\u0744\13\u00a4\3\u00a5\3\u00a5\3\u00a5\5\u00a5\u0749\n\u00a5\3"
          + "\u00a5\3\u00a5\5\u00a5\u074d\n\u00a5\3\u00a6\3\u00a6\5\u00a6\u0751\n\u00a6"
          + "\3\u00a7\3\u00a7\3\u00a7\3\u00a8\3\u00a8\3\u00a8\3\u00a8\5\u00a8\u075a"
          + "\n\u00a8\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00a9\3\u00aa\3\u00aa"
          + "\3\u00aa\7\u00aa\u0765\n\u00aa\f\u00aa\16\u00aa\u0768\13\u00aa\3\u00ab"
          + "\3\u00ab\5\u00ab\u076c\n\u00ab\3\u00ac\3\u00ac\3\u00ac\3\u00ac\3\u00ac"
          + "\5\u00ac\u0773\n\u00ac\3\u00ac\3\u00ac\5\u00ac\u0777\n\u00ac\3\u00ac\5"
          + "\u00ac\u077a\n\u00ac\3\u00ac\5\u00ac\u077d\n\u00ac\3\u00ac\5\u00ac\u0780"
          + "\n\u00ac\3\u00ac\3\u00ac\5\u00ac\u0784\n\u00ac\3\u00ad\3\u00ad\3\u00ad"
          + "\5\u00ad\u0789\n\u00ad\3\u00ad\3\u00ad\3\u00ae\3\u00ae\3\u00ae\5\u00ae"
          + "\u0790\n\u00ae\3\u00ae\3\u00ae\5\u00ae\u0794\n\u00ae\3\u00ae\3\u00ae\5"
          + "\u00ae\u0798\n\u00ae\3\u00af\3\u00af\3\u00b0\3\u00b0\5\u00b0\u079e\n\u00b0"
          + "\3\u00b0\3\u00b0\3\u00b0\5\u00b0\u07a3\n\u00b0\7\u00b0\u07a5\n\u00b0\f"
          + "\u00b0\16\u00b0\u07a8\13\u00b0\3\u00b1\3\u00b1\3\u00b1\5\u00b1\u07ad\n"
          + "\u00b1\3\u00b2\3\u00b2\3\u00b2\3\u00b2\5\u00b2\u07b3\n\u00b2\3\u00b2\5"
          + "\u00b2\u07b6\n\u00b2\3\u00b3\5\u00b3\u07b9\n\u00b3\3\u00b3\3\u00b3\3\u00b3"
          + "\3\u00b4\3\u00b4\3\u00b4\3\u00b4\3\u00b4\3\u00b5\3\u00b5\3\u00b5\3\u00b5"
          + "\3\u00b6\3\u00b6\5\u00b6\u07c9\n\u00b6\3\u00b6\3\u00b6\3\u00b6\3\u00b7"
          + "\6\u00b7\u07cf\n\u00b7\r\u00b7\16\u00b7\u07d0\3\u00b8\3\u00b8\3\u00b8"
          + "\3\u00b8\3\u00b8\3\u00b8\3\u00b9\5\u00b9\u07da\n\u00b9\3\u00b9\3\u00b9"
          + "\3\u00b9\5\u00b9\u07df\n\u00b9\3\u00b9\5\u00b9\u07e2\n\u00b9\3\u00ba\3"
          + "\u00ba\5\u00ba\u07e6\n\u00ba\3\u00bb\3\u00bb\5\u00bb\u07ea\n\u00bb\3\u00bc"
          + "\3\u00bc\3\u00bc\5\u00bc\u07ef\n\u00bc\3\u00bc\3\u00bc\3\u00bd\3\u00bd"
          + "\5\u00bd\u07f5\n\u00bd\3\u00bd\3\u00bd\3\u00bd\5\u00bd\u07fa\n\u00bd\7"
          + "\u00bd\u07fc\n\u00bd\f\u00bd\16\u00bd\u07ff\13\u00bd\3\u00be\3\u00be\3"
          + "\u00be\3\u00be\3\u00be\3\u00be\5\u00be\u0807\n\u00be\3\u00bf\3\u00bf\3"
          + "\u00bf\5\u00bf\u080c\n\u00bf\3\u00bf\3\u00bf\3\u00bf\5\u00bf\u0811\n\u00bf"
          + "\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf"
          + "\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf"
          + "\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf"
          + "\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf"
          + "\3\u00bf\3\u00bf\3\u00bf\3\u00bf\3\u00bf\5\u00bf\u083c\n\u00bf\3\u00c0"
          + "\3\u00c0\3\u00c0\3\u0419\b\f \64\u00e8\u00fe\u0102\u00c1\2\4\6\b\n\f\16"
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
          + "\u0154\u0156\u0158\u015a\u015c\u015e\u0160\u0162\u0164\u0166\u0168\u016a"
          + "\u016c\u016e\u0170\u0172\u0174\u0176\u0178\u017a\u017c\u017e\2\31\4\2"
          + "ccgg\6\2\32\32!!<<CC\4\2~~\u0083\u0083\3\2z{\4\2]_cf\4\2}}\u0084\u0084"
          + "\3\2_a\3\2]^\4\2hivw\3\2tu\4\2ggjs\7\2&&\61\61;;AAHH\5\2$$..RR\4\2\60"
          + "\60>>\4\2??PP\4\2\27\27DD\3\2W\\\4\2ccxx\4\2\30\30TT\3\2\35\36\4\2((\67"
          + "\67\3\28:\3\2\3\t\2\u0929\2\u0181\3\2\2\2\4\u0191\3\2\2\2\6\u0195\3\2"
          + "\2\2\b\u01a1\3\2\2\2\n\u01a3\3\2\2\2\f\u01a9\3\2\2\2\16\u01bf\3\2\2\2"
          + "\20\u01c5\3\2\2\2\22\u01d1\3\2\2\2\24\u01d3\3\2\2\2\26\u01d5\3\2\2\2\30"
          + "\u01e2\3\2\2\2\32\u01e9\3\2\2\2\34\u01ec\3\2\2\2\36\u01f1\3\2\2\2 \u0220"
          + "\3\2\2\2\"\u0240\3\2\2\2$\u0242\3\2\2\2&\u0257\3\2\2\2(\u0274\3\2\2\2"
          + "*\u0276\3\2\2\2,\u0279\3\2\2\2.\u0289\3\2\2\2\60\u028d\3\2\2\2\62\u0296"
          + "\3\2\2\2\64\u0298\3\2\2\2\66\u02b1\3\2\2\28\u02b4\3\2\2\2:\u02bd\3\2\2"
          + "\2<\u02c8\3\2\2\2>\u02ca\3\2\2\2@\u02d2\3\2\2\2B\u02da\3\2\2\2D\u02e2"
          + "\3\2\2\2F\u02ef\3\2\2\2H\u02f1\3\2\2\2J\u02f9\3\2\2\2L\u0301\3\2\2\2N"
          + "\u0309\3\2\2\2P\u0311\3\2\2\2R\u0319\3\2\2\2T\u0321\3\2\2\2V\u0329\3\2"
          + "\2\2X\u0337\3\2\2\2Z\u0339\3\2\2\2\\\u033b\3\2\2\2^\u0343\3\2\2\2`\u0352"
          + "\3\2\2\2b\u0355\3\2\2\2d\u0361\3\2\2\2f\u0365\3\2\2\2h\u036c\3\2\2\2j"
          + "\u037f\3\2\2\2l\u038c\3\2\2\2n\u03af\3\2\2\2p\u03b3\3\2\2\2r\u03b6\3\2"
          + "\2\2t\u03bd\3\2\2\2v\u03c8\3\2\2\2x\u03cc\3\2\2\2z\u03cf\3\2\2\2|\u03dc"
          + "\3\2\2\2~\u03e6\3\2\2\2\u0080\u03e8\3\2\2\2\u0082\u03ff\3\2\2\2\u0084"
          + "\u0401\3\2\2\2\u0086\u0409\3\2\2\2\u0088\u040b\3\2\2\2\u008a\u0414\3\2"
          + "\2\2\u008c\u0417\3\2\2\2\u008e\u041e\3\2\2\2\u0090\u0420\3\2\2\2\u0092"
          + "\u0422\3\2\2\2\u0094\u0427\3\2\2\2\u0096\u042d\3\2\2\2\u0098\u0430\3\2"
          + "\2\2\u009a\u0438\3\2\2\2\u009c\u043f\3\2\2\2\u009e\u0441\3\2\2\2\u00a0"
          + "\u0477\3\2\2\2\u00a2\u047d\3\2\2\2\u00a4\u047f\3\2\2\2\u00a6\u049d\3\2"
          + "\2\2\u00a8\u049f\3\2\2\2\u00aa\u04a1\3\2\2\2\u00ac\u04ab\3\2\2\2\u00ae"
          + "\u04b8\3\2\2\2\u00b0\u04c2\3\2\2\2\u00b2\u04c6\3\2\2\2\u00b4\u04c9\3\2"
          + "\2\2\u00b6\u04d1\3\2\2\2\u00b8\u04d6\3\2\2\2\u00ba\u04da\3\2\2\2\u00bc"
          + "\u04dc\3\2\2\2\u00be\u04df\3\2\2\2\u00c0\u04ec\3\2\2\2\u00c2\u04ee\3\2"
          + "\2\2\u00c4\u04f5\3\2\2\2\u00c6\u04f9\3\2\2\2\u00c8\u0505\3\2\2\2\u00ca"
          + "\u050f\3\2\2\2\u00cc\u0515\3\2\2\2\u00ce\u0520\3\2\2\2\u00d0\u052c\3\2"
          + "\2\2\u00d2\u052e\3\2\2\2\u00d4\u0539\3\2\2\2\u00d6\u0547\3\2\2\2\u00d8"
          + "\u054d\3\2\2\2\u00da\u054f\3\2\2\2\u00dc\u0556\3\2\2\2\u00de\u056b\3\2"
          + "\2\2\u00e0\u056d\3\2\2\2\u00e2\u0575\3\2\2\2\u00e4\u057e\3\2\2\2\u00e6"
          + "\u0586\3\2\2\2\u00e8\u0594\3\2\2\2\u00ea\u05a7\3\2\2\2\u00ec\u05b8\3\2"
          + "\2\2\u00ee\u05cb\3\2\2\2\u00f0\u05ce\3\2\2\2\u00f2\u05d2\3\2\2\2\u00f4"
          + "\u05d4\3\2\2\2\u00f6\u05d7\3\2\2\2\u00f8\u05db\3\2\2\2\u00fa\u05e7\3\2"
          + "\2\2\u00fc\u05f2\3\2\2\2\u00fe\u0602\3\2\2\2\u0100\u0619\3\2\2\2\u0102"
          + "\u061e\3\2\2\2\u0104\u0632\3\2\2\2\u0106\u0639\3\2\2\2\u0108\u0642\3\2"
          + "\2\2\u010a\u0650\3\2\2\2\u010c\u0663\3\2\2\2\u010e\u066a\3\2\2\2\u0110"
          + "\u066f\3\2\2\2\u0112\u0673\3\2\2\2\u0114\u0675\3\2\2\2\u0116\u0683\3\2"
          + "\2\2\u0118\u068e\3\2\2\2\u011a\u0690\3\2\2\2\u011c\u06ae\3\2\2\2\u011e"
          + "\u06b1\3\2\2\2\u0120\u06b5\3\2\2\2\u0122\u06b7\3\2\2\2\u0124\u06bd\3\2"
          + "\2\2\u0126\u06d1\3\2\2\2\u0128\u06d3\3\2\2\2\u012a\u06ef\3\2\2\2\u012c"
          + "\u06f2\3\2\2\2\u012e\u06f6\3\2\2\2\u0130\u06f8\3\2\2\2\u0132\u06fc\3\2"
          + "\2\2\u0134\u06ff\3\2\2\2\u0136\u070e\3\2\2\2\u0138\u0723\3\2\2\2\u013a"
          + "\u0725\3\2\2\2\u013c\u0727\3\2\2\2\u013e\u0729\3\2\2\2\u0140\u072c\3\2"
          + "\2\2\u0142\u0730\3\2\2\2\u0144\u0734\3\2\2\2\u0146\u0737\3\2\2\2\u0148"
          + "\u0745\3\2\2\2\u014a\u0750\3\2\2\2\u014c\u0752\3\2\2\2\u014e\u0755\3\2"
          + "\2\2\u0150\u075b\3\2\2\2\u0152\u0761\3\2\2\2\u0154\u076b\3\2\2\2\u0156"
          + "\u0776\3\2\2\2\u0158\u0785\3\2\2\2\u015a\u0797\3\2\2\2\u015c\u0799\3\2"
          + "\2\2\u015e\u079b\3\2\2\2\u0160\u07ac\3\2\2\2\u0162\u07ae\3\2\2\2\u0164"
          + "\u07b8\3\2\2\2\u0166\u07bd\3\2\2\2\u0168\u07c2\3\2\2\2\u016a\u07c6\3\2"
          + "\2\2\u016c\u07ce\3\2\2\2\u016e\u07d2\3\2\2\2\u0170\u07e1\3\2\2\2\u0172"
          + "\u07e3\3\2\2\2\u0174\u07e9\3\2\2\2\u0176\u07eb\3\2\2\2\u0178\u07f2\3\2"
          + "\2\2\u017a\u0806\3\2\2\2\u017c\u083b\3\2\2\2\u017e\u083d\3\2\2\2\u0180"
          + "\u0182\5z>\2\u0181\u0180\3\2\2\2\u0181\u0182\3\2\2\2\u0182\u0183\3\2\2"
          + "\2\u0183\u0184\7\2\2\3\u0184\3\3\2\2\2\u0185\u0187\5\u017e\u00c0\2\u0186"
          + "\u0185\3\2\2\2\u0187\u0188\3\2\2\2\u0188\u0186\3\2\2\2\u0188\u0189\3\2"
          + "\2\2\u0189\u0192\3\2\2\2\u018a\u0192\7G\2\2\u018b\u018c\7W\2\2\u018c\u018d"
          + "\5\\/\2\u018d\u018e\7X\2\2\u018e\u0192\3\2\2\2\u018f\u0192\5\6\4\2\u0190"
          + "\u0192\5\16\b\2\u0191\u0186\3\2\2\2\u0191\u018a\3\2\2\2\u0191\u018b\3"
          + "\2\2\2\u0191\u018f\3\2\2\2\u0191\u0190\3\2\2\2\u0192\5\3\2\2\2\u0193\u0196"
          + "\5\b\5\2\u0194\u0196\5\n\6\2\u0195\u0193\3\2\2\2\u0195\u0194\3\2\2\2\u0196"
          + "\7\3\2\2\2\u0197\u01a2\7\u0086\2\2\u0198\u01a2\5\u014c\u00a7\2\u0199\u01a2"
          + "\5\u013e\u00a0\2\u019a\u01a2\5\u014e\u00a8\2\u019b\u019e\7e\2\2\u019c"
          + "\u019f\5\u0118\u008d\2\u019d\u019f\5\u00a4S\2\u019e\u019c\3\2\2\2\u019e"
          + "\u019d\3\2\2\2\u019f\u01a2\3\2\2\2\u01a0\u01a2\5\u015a\u00ae\2\u01a1\u0197"
          + "\3\2\2\2\u01a1\u0198\3\2\2\2\u01a1\u0199\3\2\2\2\u01a1\u019a\3\2\2\2\u01a1"
          + "\u019b\3\2\2\2\u01a1\u01a0\3\2\2\2\u01a2\t\3\2\2\2\u01a3\u01a5\5\f\7\2"
          + "\u01a4\u01a6\7F\2\2\u01a5\u01a4\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7"
          + "\3\2\2\2\u01a7\u01a8\5\b\5\2\u01a8\13\3\2\2\2\u01a9\u01ad\b\7\1\2\u01aa"
          + "\u01ae\5\u00a2R\2\u01ab\u01ae\5\u00ba^\2\u01ac\u01ae\5\u00a4S\2\u01ad"
          + "\u01aa\3\2\2\2\u01ad\u01ab\3\2\2\2\u01ad\u01ac\3\2\2\2\u01ad\u01ae\3\2"
          + "\2\2\u01ae\u01af\3\2\2\2\u01af\u01b0\7\u0081\2\2\u01b0\u01bc\3\2\2\2\u01b1"
          + "\u01b7\f\3\2\2\u01b2\u01b8\7\u0086\2\2\u01b3\u01b5\7F\2\2\u01b4\u01b3"
          + "\3\2\2\2\u01b4\u01b5\3\2\2\2\u01b5\u01b6\3\2\2\2\u01b6\u01b8\5\u0158\u00ad"
          + "\2\u01b7\u01b2\3\2\2\2\u01b7\u01b4\3\2\2\2\u01b8\u01b9\3\2\2\2\u01b9\u01bb"
          + "\7\u0081\2\2\u01ba\u01b1\3\2\2\2\u01bb\u01be\3\2\2\2\u01bc\u01ba\3\2\2"
          + "\2\u01bc\u01bd\3\2\2\2\u01bd\r\3\2\2\2\u01be\u01bc\3\2\2\2\u01bf\u01c1"
          + "\5\20\t\2\u01c0\u01c2\5\36\20\2\u01c1\u01c0\3\2\2\2\u01c1\u01c2\3\2\2"
          + "\2\u01c2\u01c3\3\2\2\2\u01c3\u01c4\5f\64\2\u01c4\17\3\2\2\2\u01c5\u01c7"
          + "\7Y\2\2\u01c6\u01c8\5\22\n\2\u01c7\u01c6\3\2\2\2\u01c7\u01c8\3\2\2\2\u01c8"
          + "\u01c9\3\2\2\2\u01c9\u01ca\7Z\2\2\u01ca\21\3\2\2\2\u01cb\u01d2\5\26\f"
          + "\2\u01cc\u01cf\5\24\13\2\u01cd\u01ce\7|\2\2\u01ce\u01d0\5\26\f\2\u01cf"
          + "\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d2\3\2\2\2\u01d1\u01cb\3\2"
          + "\2\2\u01d1\u01cc\3\2\2\2\u01d2\23\3\2\2\2\u01d3\u01d4\t\2\2\2\u01d4\25"
          + "\3\2\2\2\u01d5\u01da\5\30\r\2\u01d6\u01d7\7|\2\2\u01d7\u01d9\5\30\r\2"
          + "\u01d8\u01d6\3\2\2\2\u01d9\u01dc\3\2\2\2\u01da\u01d8\3\2\2\2\u01da\u01db"
          + "\3\2\2\2\u01db\u01de\3\2\2\2\u01dc\u01da\3\2\2\2\u01dd\u01df\7\u0085\2"
          + "\2\u01de\u01dd\3\2\2\2\u01de\u01df\3\2\2\2\u01df\27\3\2\2\2\u01e0\u01e3"
          + "\5\32\16\2\u01e1\u01e3\5\34\17\2\u01e2\u01e0\3\2\2\2\u01e2\u01e1\3\2\2"
          + "\2\u01e3\31\3\2\2\2\u01e4\u01e6\7c\2\2\u01e5\u01e4\3\2\2\2\u01e5\u01e6"
          + "\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01ea\7\u0086\2\2\u01e8\u01ea\7G\2"
          + "\2\u01e9\u01e5\3\2\2\2\u01e9\u01e8\3\2\2\2\u01ea\33\3\2\2\2\u01eb\u01ed"
          + "\7c\2\2\u01ec\u01eb\3\2\2\2\u01ec\u01ed\3\2\2\2\u01ed\u01ee\3\2\2\2\u01ee"
          + "\u01ef\7\u0086\2\2\u01ef\u01f0\5\u010e\u0088\2\u01f0\35\3\2\2\2\u01f1"
          + "\u01f3\7W\2\2\u01f2\u01f4\5\u0104\u0083\2\u01f3\u01f2\3\2\2\2\u01f3\u01f4"
          + "\3\2\2\2\u01f4\u01f5\3\2\2\2\u01f5\u01f7\7X\2\2\u01f6\u01f8\7\61\2\2\u01f7"
          + "\u01f6\3\2\2\2\u01f7\u01f8\3\2\2\2\u01f8\u01fa\3\2\2\2\u01f9\u01fb\5\u0174"
          + "\u00bb\2\u01fa\u01f9\3\2\2\2\u01fa\u01fb\3\2\2\2\u01fb\u01fd\3\2\2\2\u01fc"
          + "\u01fe\5\u00ceh\2\u01fd\u01fc\3\2\2\2\u01fd\u01fe\3\2\2\2\u01fe\u0200"
          + "\3\2\2\2\u01ff\u0201\5\u00ecw\2\u0200\u01ff\3\2\2\2\u0200\u0201\3\2\2"
          + "\2\u0201\37\3\2\2\2\u0202\u0203\b\21\1\2\u0203\u0221\5\4\3\2\u0204\u0207"
          + "\5\u00a0Q\2\u0205\u0207\5\u0162\u00b2\2\u0206\u0204\3\2\2\2\u0206\u0205"
          + "\3\2\2\2\u0207\u020e\3\2\2\2\u0208\u020a\7W\2\2\u0209\u020b\5$\23\2\u020a"
          + "\u0209\3\2\2\2\u020a\u020b\3\2\2\2\u020b\u020c\3\2\2\2\u020c\u020f\7X"
          + "\2\2\u020d\u020f\5\u0116\u008c\2\u020e\u0208\3\2\2\2\u020e\u020d\3\2\2"
          + "\2\u020f\u0221\3\2\2\2\u0210\u0211\t\3\2\2\u0211\u0212\7h\2\2\u0212\u0213"
          + "\5\u00f8}\2\u0213\u0214\7i\2\2\u0214\u0215\7W\2\2\u0215\u0216\5\\/\2\u0216"
          + "\u0217\7X\2\2\u0217\u0221\3\2\2\2\u0218\u0219\5\"\22\2\u0219\u021c\7W"
          + "\2\2\u021a\u021d\5\\/\2\u021b\u021d\5\u00f8}\2\u021c\u021a\3\2\2\2\u021c"
          + "\u021b\3\2\2\2\u021d\u021e\3\2\2\2\u021e\u021f\7X\2\2\u021f\u0221\3\2"
          + "\2\2\u0220\u0202\3\2\2\2\u0220\u0206\3\2\2\2\u0220\u0210\3\2\2\2\u0220"
          + "\u0218\3\2\2\2\u0221\u023d\3\2\2\2\u0222\u0223\f\t\2\2\u0223\u0226\7Y"
          + "\2\2\u0224\u0227\5\\/\2\u0225\u0227\5\u0116\u008c\2\u0226\u0224\3\2\2"
          + "\2\u0226\u0225\3\2\2\2\u0227\u0228\3\2\2\2\u0228\u0229\7Z\2\2\u0229\u023c"
          + "\3\2\2\2\u022a\u022b\f\b\2\2\u022b\u022d\7W\2\2\u022c\u022e\5$\23\2\u022d"
          + "\u022c\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u022f\3\2\2\2\u022f\u023c\7X"
          + "\2\2\u0230\u0231\f\6\2\2\u0231\u0237\t\4\2\2\u0232\u0234\7F\2\2\u0233"
          + "\u0232\3\2\2\2\u0233\u0234\3\2\2\2\u0234\u0235\3\2\2\2\u0235\u0238\5\6"
          + "\4\2\u0236\u0238\5&\24\2\u0237\u0233\3\2\2\2\u0237\u0236\3\2\2\2\u0238"
          + "\u023c\3\2\2\2\u0239\u023a\f\5\2\2\u023a\u023c\t\5\2\2\u023b\u0222\3\2"
          + "\2\2\u023b\u022a\3\2\2\2\u023b\u0230\3\2\2\2\u023b\u0239\3\2\2\2\u023c"
          + "\u023f\3\2\2\2\u023d\u023b\3\2\2\2\u023d\u023e\3\2\2\2\u023e!\3\2\2\2"
          + "\u023f\u023d\3\2\2\2\u0240\u0241\7M\2\2\u0241#\3\2\2\2\u0242\u0243\5\u0114"
          + "\u008b\2\u0243%\3\2\2\2\u0244\u0246\5\f\7\2\u0245\u0244\3\2\2\2\u0245"
          + "\u0246\3\2\2\2\u0246\u024a\3\2\2\2\u0247\u0248\5\u00a2R\2\u0248\u0249"
          + "\7\u0081\2\2\u0249\u024b\3\2\2\2\u024a\u0247\3\2\2\2\u024a\u024b\3\2\2"
          + "\2\u024b\u024c\3\2\2\2\u024c\u024d\7e\2\2\u024d\u0258\5\u00a2R\2\u024e"
          + "\u024f\5\f\7\2\u024f\u0250\7F\2\2\u0250\u0251\5\u0158\u00ad\2\u0251\u0252"
          + "\7\u0081\2\2\u0252\u0253\7e\2\2\u0253\u0254\5\u00a2R\2\u0254\u0258\3\2"
          + "\2\2\u0255\u0256\7e\2\2\u0256\u0258\5\u00a4S\2\u0257\u0245\3\2\2\2\u0257"
          + "\u024e\3\2\2\2\u0257\u0255\3\2\2\2\u0258\'\3\2\2\2\u0259\u0275\5 \21\2"
          + "\u025a\u025f\7z\2\2\u025b\u025f\7{\2\2\u025c\u025f\5*\26\2\u025d\u025f"
          + "\7@\2\2\u025e\u025a\3\2\2\2\u025e\u025b\3\2\2\2\u025e\u025c\3\2\2\2\u025e"
          + "\u025d\3\2\2\2\u025f\u0260\3\2\2\2\u0260\u0275\5(\25\2\u0261\u026a\7@"
          + "\2\2\u0262\u0263\7W\2\2\u0263\u0264\5\u00f8}\2\u0264\u0265\7X\2\2\u0265"
          + "\u026b\3\2\2\2\u0266\u0267\7\u0085\2\2\u0267\u0268\7W\2\2\u0268\u0269"
          + "\7\u0086\2\2\u0269\u026b\7X\2\2\u026a\u0262\3\2\2\2\u026a\u0266\3\2\2"
          + "\2\u026b\u0275\3\2\2\2\u026c\u026d\7\r\2\2\u026d\u026e\7W\2\2\u026e\u026f"
          + "\5\u00f8}\2\u026f\u0270\7X\2\2\u0270\u0275\3\2\2\2\u0271\u0275\5:\36\2"
          + "\u0272\u0275\5,\27\2\u0273\u0275\58\35\2\u0274\u0259\3\2\2\2\u0274\u025e"
          + "\3\2\2\2\u0274\u0261\3\2\2\2\u0274\u026c\3\2\2\2\u0274\u0271\3\2\2\2\u0274"
          + "\u0272\3\2\2\2\u0274\u0273\3\2\2\2\u0275)\3\2\2\2\u0276\u0277\t\6\2\2"
          + "\u0277+\3\2\2\2\u0278\u027a\7\u0081\2\2\u0279\u0278\3\2\2\2\u0279\u027a"
          + "\3\2\2\2\u027a\u027b\3\2\2\2\u027b\u027d\7\63\2\2\u027c\u027e\5.\30\2"
          + "\u027d\u027c\3\2\2\2\u027d\u027e\3\2\2\2\u027e\u0284\3\2\2\2\u027f\u0285"
          + "\5\60\31\2\u0280\u0281\7W\2\2\u0281\u0282\5\u00f8}\2\u0282\u0283\7X\2"
          + "\2\u0283\u0285\3\2\2\2\u0284\u027f\3\2\2\2\u0284\u0280\3\2\2\2\u0285\u0287"
          + "\3\2\2\2\u0286\u0288\5\66\34\2\u0287\u0286\3\2\2\2\u0287\u0288\3\2\2\2"
          + "\u0288-\3\2\2\2\u0289\u028a\7W\2\2\u028a\u028b\5$\23\2\u028b\u028c\7X"
          + "\2\2\u028c/\3\2\2\2\u028d\u028f\5\u0098M\2\u028e\u0290\5\62\32\2\u028f"
          + "\u028e\3\2\2\2\u028f\u0290\3\2\2\2\u0290\61\3\2\2\2\u0291\u0293\5\u00ee"
          + "x\2\u0292\u0294\5\62\32\2\u0293\u0292\3\2\2\2\u0293\u0294\3\2\2\2\u0294"
          + "\u0297\3\2\2\2\u0295\u0297\5\64\33\2\u0296\u0291\3\2\2\2\u0296\u0295\3"
          + "\2\2\2\u0297\63\3\2\2\2\u0298\u0299\b\33\1\2\u0299\u029a\7Y\2\2\u029a"
          + "\u029b\5\\/\2\u029b\u029d\7Z\2\2\u029c\u029e\5\u00ceh\2\u029d\u029c\3"
          + "\2\2\2\u029d\u029e\3\2\2\2\u029e\u02a8\3\2\2\2\u029f\u02a0\f\3\2\2\u02a0"
          + "\u02a1\7Y\2\2\u02a1\u02a2\5^\60\2\u02a2\u02a4\7Z\2\2\u02a3\u02a5\5\u00ce"
          + "h\2\u02a4\u02a3\3\2\2\2\u02a4\u02a5\3\2\2\2\u02a5\u02a7\3\2\2\2\u02a6"
          + "\u029f\3\2\2\2\u02a7\u02aa\3\2\2\2\u02a8\u02a6\3\2\2\2\u02a8\u02a9\3\2"
          + "\2\2\u02a9\65\3\2\2\2\u02aa\u02a8\3\2\2\2\u02ab\u02ad\7W\2\2\u02ac\u02ae"
          + "\5$\23\2\u02ad\u02ac\3\2\2\2\u02ad\u02ae\3\2\2\2\u02ae\u02af\3\2\2\2\u02af"
          + "\u02b2\7X\2\2\u02b0\u02b2\5\u0116\u008c\2\u02b1\u02ab\3\2\2\2\u02b1\u02b0"
          + "\3\2\2\2\u02b2\67\3\2\2\2\u02b3\u02b5\7\u0081\2\2\u02b4\u02b3\3\2\2\2"
          + "\u02b4\u02b5\3\2\2\2\u02b5\u02b6\3\2\2\2\u02b6\u02b9\7\36\2\2\u02b7\u02b8"
          + "\7Y\2\2\u02b8\u02ba\7Z\2\2\u02b9\u02b7\3\2\2\2\u02b9\u02ba\3\2\2\2\u02ba"
          + "\u02bb\3\2\2\2\u02bb\u02bc\5<\37\2\u02bc9\3\2\2\2\u02bd\u02be\7\64\2\2"
          + "\u02be\u02bf\7W\2\2\u02bf\u02c0\5\\/\2\u02c0\u02c1\7X\2\2\u02c1;\3\2\2"
          + "\2\u02c2\u02c9\5(\25\2\u02c3\u02c4\7W\2\2\u02c4\u02c5\5\u00f8}\2\u02c5"
          + "\u02c6\7X\2\2\u02c6\u02c7\5<\37\2\u02c7\u02c9\3\2\2\2\u02c8\u02c2\3\2"
          + "\2\2\u02c8\u02c3\3\2\2\2\u02c9=\3\2\2\2\u02ca\u02cf\5<\37\2\u02cb\u02cc"
          + "\t\7\2\2\u02cc\u02ce\5<\37\2\u02cd\u02cb\3\2\2\2\u02ce\u02d1\3\2\2\2\u02cf"
          + "\u02cd\3\2\2\2\u02cf\u02d0\3\2\2\2\u02d0?\3\2\2\2\u02d1\u02cf\3\2\2\2"
          + "\u02d2\u02d7\5> \2\u02d3\u02d4\t\b\2\2\u02d4\u02d6\5> \2\u02d5\u02d3\3"
          + "\2\2\2\u02d6\u02d9\3\2\2\2\u02d7\u02d5\3\2\2\2\u02d7\u02d8\3\2\2\2\u02d8"
          + "A\3\2\2\2\u02d9\u02d7\3\2\2\2\u02da\u02df\5@!\2\u02db\u02dc\t\t\2\2\u02dc"
          + "\u02de\5@!\2\u02dd\u02db\3\2\2\2\u02de\u02e1\3\2\2\2\u02df\u02dd\3\2\2"
          + "\2\u02df\u02e0\3\2\2\2\u02e0C\3\2\2\2\u02e1\u02df\3\2\2\2\u02e2\u02e8"
          + "\5B\"\2\u02e3\u02e4\5F$\2\u02e4\u02e5\5B\"\2\u02e5\u02e7\3\2\2\2\u02e6"
          + "\u02e3\3\2\2\2\u02e7\u02ea\3\2\2\2\u02e8\u02e6\3\2\2\2\u02e8\u02e9\3\2"
          + "\2\2\u02e9E\3\2\2\2\u02ea\u02e8\3\2\2\2\u02eb\u02ec\7i\2\2\u02ec\u02f0"
          + "\7i\2\2\u02ed\u02ee\7h\2\2\u02ee\u02f0\7h\2\2\u02ef\u02eb\3\2\2\2\u02ef"
          + "\u02ed\3\2\2\2\u02f0G\3\2\2\2\u02f1\u02f6\5D#\2\u02f2\u02f3\t\n\2\2\u02f3"
          + "\u02f5\5D#\2\u02f4\u02f2\3\2\2\2\u02f5\u02f8\3\2\2\2\u02f6\u02f4\3\2\2"
          + "\2\u02f6\u02f7\3\2\2\2\u02f7I\3\2\2\2\u02f8\u02f6\3\2\2\2\u02f9\u02fe"
          + "\5H%\2\u02fa\u02fb\t\13\2\2\u02fb\u02fd\5H%\2\u02fc\u02fa\3\2\2\2\u02fd"
          + "\u0300\3\2\2\2\u02fe\u02fc\3\2\2\2\u02fe\u02ff\3\2\2\2\u02ffK\3\2\2\2"
          + "\u0300\u02fe\3\2\2\2\u0301\u0306\5J&\2\u0302\u0303\7c\2\2\u0303\u0305"
          + "\5J&\2\u0304\u0302\3\2\2\2\u0305\u0308\3\2\2\2\u0306\u0304\3\2\2\2\u0306"
          + "\u0307\3\2\2\2\u0307M\3\2\2\2\u0308\u0306\3\2\2\2\u0309\u030e\5L\'\2\u030a"
          + "\u030b\7b\2\2\u030b\u030d\5L\'\2\u030c\u030a\3\2\2\2\u030d\u0310\3\2\2"
          + "\2\u030e\u030c\3\2\2\2\u030e\u030f\3\2\2\2\u030fO\3\2\2\2\u0310\u030e"
          + "\3\2\2\2\u0311\u0316\5N(\2\u0312\u0313\7d\2\2\u0313\u0315\5N(\2\u0314"
          + "\u0312\3\2\2\2\u0315\u0318\3\2\2\2\u0316\u0314\3\2\2\2\u0316\u0317\3\2"
          + "\2\2\u0317Q\3\2\2\2\u0318\u0316\3\2\2\2\u0319\u031e\5P)\2\u031a\u031b"
          + "\7x\2\2\u031b\u031d\5P)\2\u031c\u031a\3\2\2\2\u031d\u0320\3\2\2\2\u031e"
          + "\u031c\3\2\2\2\u031e\u031f\3\2\2\2\u031fS\3\2\2\2\u0320\u031e\3\2\2\2"
          + "\u0321\u0326\5R*\2\u0322\u0323\7y\2\2\u0323\u0325\5R*\2\u0324\u0322\3"
          + "\2\2\2\u0325\u0328\3\2\2\2\u0326\u0324\3\2\2\2\u0326\u0327\3\2\2\2\u0327"
          + "U\3\2\2\2\u0328\u0326\3\2\2\2\u0329\u032f\5T+\2\u032a\u032b\7\177\2\2"
          + "\u032b\u032c\5\\/\2\u032c\u032d\7\u0080\2\2\u032d\u032e\5X-\2\u032e\u0330"
          + "\3\2\2\2\u032f\u032a\3\2\2\2\u032f\u0330\3\2\2\2\u0330W\3\2\2\2\u0331"
          + "\u0338\5V,\2\u0332\u0333\5T+\2\u0333\u0334\5Z.\2\u0334\u0335\5\u0112\u008a"
          + "\2\u0335\u0338\3\2\2\2\u0336\u0338\5\u0172\u00ba\2\u0337\u0331\3\2\2\2"
          + "\u0337\u0332\3\2\2\2\u0337\u0336\3\2\2\2\u0338Y\3\2\2\2\u0339\u033a\t"
          + "\f\2\2\u033a[\3\2\2\2\u033b\u0340\5X-\2\u033c\u033d\7|\2\2\u033d\u033f"
          + "\5X-\2\u033e\u033c\3\2\2\2\u033f\u0342\3\2\2\2\u0340\u033e\3\2\2\2\u0340"
          + "\u0341\3\2\2\2\u0341]\3\2\2\2\u0342\u0340\3\2\2\2\u0343\u0344\5V,\2\u0344"
          + "_\3\2\2\2\u0345\u0353\5b\62\2\u0346\u0353\5x=\2\u0347\u0349\5\u00ceh\2"
          + "\u0348\u0347\3\2\2\2\u0348\u0349\3\2\2\2\u0349\u0350\3\2\2\2\u034a\u0351"
          + "\5d\63\2\u034b\u0351\5f\64\2\u034c\u0351\5j\66\2\u034d\u0351\5n8\2\u034e"
          + "\u0351\5v<\2\u034f\u0351\5\u0168\u00b5\2\u0350\u034a\3\2\2\2\u0350\u034b"
          + "\3\2\2\2\u0350\u034c\3\2\2\2\u0350\u034d\3\2\2\2\u0350\u034e\3\2\2\2\u0350"
          + "\u034f\3\2\2\2\u0351\u0353\3\2\2\2\u0352\u0345\3\2\2\2\u0352\u0346\3\2"
          + "\2\2\u0352\u0348\3\2\2\2\u0353a\3\2\2\2\u0354\u0356\5\u00ceh\2\u0355\u0354"
          + "\3\2\2\2\u0355\u0356\3\2\2\2\u0356\u035b\3\2\2\2\u0357\u035c\7\u0086\2"
          + "\2\u0358\u0359\7\22\2\2\u0359\u035c\5^\60\2\u035a\u035c\7\35\2\2\u035b"
          + "\u0357\3\2\2\2\u035b\u0358\3\2\2\2\u035b\u035a\3\2\2\2\u035c\u035d\3\2"
          + "\2\2\u035d\u035e\7\u0080\2\2\u035e\u035f\5`\61\2\u035fc\3\2\2\2\u0360"
          + "\u0362\5\\/\2\u0361\u0360\3\2\2\2\u0361\u0362\3\2\2\2\u0362\u0363\3\2"
          + "\2\2\u0363\u0364\7\u0082\2\2\u0364e\3\2\2\2\u0365\u0367\7[\2\2\u0366\u0368"
          + "\5h\65\2\u0367\u0366\3\2\2\2\u0367\u0368\3\2\2\2\u0368\u0369\3\2\2\2\u0369"
          + "\u036a\7\\\2\2\u036ag\3\2\2\2\u036b\u036d\5`\61\2\u036c\u036b\3\2\2\2"
          + "\u036d\u036e\3\2\2\2\u036e\u036c\3\2\2\2\u036e\u036f\3\2\2\2\u036fi\3"
          + "\2\2\2\u0370\u0371\7-\2\2\u0371\u0372\7W\2\2\u0372\u0373\5l\67\2\u0373"
          + "\u0374\7X\2\2\u0374\u0377\5`\61\2\u0375\u0376\7\"\2\2\u0376\u0378\5`\61"
          + "\2\u0377\u0375\3\2\2\2\u0377\u0378\3\2\2\2\u0378\u0380\3\2\2\2\u0379\u037a"
          + "\7E\2\2\u037a\u037b\7W\2\2\u037b\u037c\5l\67\2\u037c\u037d\7X\2\2\u037d"
          + "\u037e\5`\61\2\u037e\u0380\3\2\2\2\u037f\u0370\3\2\2\2\u037f\u0379\3\2"
          + "\2\2\u0380k\3\2\2\2\u0381\u038d\5\\/\2\u0382\u0384\5\u00ceh\2\u0383\u0382"
          + "\3\2\2\2\u0383\u0384\3\2\2\2\u0384\u0385\3\2\2\2\u0385\u0386\5\u008cG"
          + "\2\u0386\u038a\5\u00e4s\2\u0387\u0388\7g\2\2\u0388\u038b\5\u0112\u008a"
          + "\2\u0389\u038b\5\u0116\u008c\2\u038a\u0387\3\2\2\2\u038a\u0389\3\2\2\2"
          + "\u038b\u038d\3\2\2\2\u038c\u0381\3\2\2\2\u038c\u0383\3\2\2\2\u038dm\3"
          + "\2\2\2\u038e\u038f\7V\2\2\u038f\u0390\7W\2\2\u0390\u0391\5l\67\2\u0391"
          + "\u0392\7X\2\2\u0392\u0393\5`\61\2\u0393\u03b0\3\2\2\2\u0394\u0395\7\37"
          + "\2\2\u0395\u0396\5`\61\2\u0396\u0397\7V\2\2\u0397\u0398\7W\2\2\u0398\u0399"
          + "\5\\/\2\u0399\u039a\7X\2\2\u039a\u039b\7\u0082\2\2\u039b\u03b0\3\2\2\2"
          + "\u039c\u039d\7*\2\2\u039d\u03aa\7W\2\2\u039e\u03a0\5p9\2\u039f\u03a1\5"
          + "l\67\2\u03a0\u039f\3\2\2\2\u03a0\u03a1\3\2\2\2\u03a1\u03a2\3\2\2\2\u03a2"
          + "\u03a4\7\u0082\2\2\u03a3\u03a5\5\\/\2\u03a4\u03a3\3\2\2\2\u03a4\u03a5"
          + "\3\2\2\2\u03a5\u03ab\3\2\2\2\u03a6\u03a7\5r:\2\u03a7\u03a8\7\u0080\2\2"
          + "\u03a8\u03a9\5t;\2\u03a9\u03ab\3\2\2\2\u03aa\u039e\3\2\2\2\u03aa\u03a6"
          + "\3\2\2\2\u03ab\u03ac\3\2\2\2\u03ac\u03ad\7X\2\2\u03ad\u03ae\5`\61\2\u03ae"
          + "\u03b0\3\2\2\2\u03af\u038e\3\2\2\2\u03af\u0394\3\2\2\2\u03af\u039c\3\2"
          + "\2\2\u03b0o\3\2\2\2\u03b1\u03b4\5d\63\2\u03b2\u03b4\5\u0082B\2\u03b3\u03b1"
          + "\3\2\2\2\u03b3\u03b2\3\2\2\2\u03b4q\3\2\2\2\u03b5\u03b7\5\u00ceh\2\u03b6"
          + "\u03b5\3\2\2\2\u03b6\u03b7\3\2\2\2\u03b7\u03b8\3\2\2\2\u03b8\u03b9\5\u008c"
          + "G\2\u03b9\u03ba\5\u00e4s\2\u03bas\3\2\2\2\u03bb\u03be\5\\/\2\u03bc\u03be"
          + "\5\u0116\u008c\2\u03bd\u03bb\3\2\2\2\u03bd\u03bc\3\2\2\2\u03beu\3\2\2"
          + "\2\u03bf\u03c9\7\21\2\2\u03c0\u03c9\7\33\2\2\u03c1\u03c4\7=\2\2\u03c2"
          + "\u03c5\5\\/\2\u03c3\u03c5\5\u0116\u008c\2\u03c4\u03c2\3\2\2\2\u03c4\u03c3"
          + "\3\2\2\2\u03c4\u03c5\3\2\2\2\u03c5\u03c9\3\2\2\2\u03c6\u03c7\7,\2\2\u03c7"
          + "\u03c9\7\u0086\2\2\u03c8\u03bf\3\2\2\2\u03c8\u03c0\3\2\2\2\u03c8\u03c1"
          + "\3\2\2\2\u03c8\u03c6\3\2\2\2\u03c9\u03ca\3\2\2\2\u03ca\u03cb\7\u0082\2"
          + "\2\u03cbw\3\2\2\2\u03cc\u03cd\5~@\2\u03cdy\3\2\2\2\u03ce\u03d0\5|?\2\u03cf"
          + "\u03ce\3\2\2\2\u03d0\u03d1\3\2\2\2\u03d1\u03cf\3\2\2\2\u03d1\u03d2\3\2"
          + "\2\2\u03d2{\3\2\2\2\u03d3\u03dd\5~@\2\u03d4\u03dd\5\u010a\u0086\2\u03d5"
          + "\u03dd\5\u0150\u00a9\2\u03d6\u03dd\5\u0164\u00b3\2\u03d7\u03dd\5\u0166"
          + "\u00b4\2\u03d8\u03dd\5\u00ccg\2\u03d9\u03dd\5\u00be`\2\u03da\u03dd\5\u0086"
          + "D\2\u03db\u03dd\5\u0088E\2\u03dc\u03d3\3\2\2\2\u03dc\u03d4\3\2\2\2\u03dc"
          + "\u03d5\3\2\2\2\u03dc\u03d6\3\2\2\2\u03dc\u03d7\3\2\2\2\u03dc\u03d8\3\2"
          + "\2\2\u03dc\u03d9\3\2\2\2\u03dc\u03da\3\2\2\2\u03dc\u03db\3\2\2\2\u03dd"
          + "}\3\2\2\2\u03de\u03e7\5\u0082B\2\u03df\u03e7\5\u00caf\2\u03e0\u03e7\5"
          + "\u00c2b\2\u03e1\u03e7\5\u00c6d\2\u03e2\u03e7\5\u00c8e\2\u03e3\u03e7\5"
          + "\u0084C\2\u03e4\u03e7\5\u0080A\2\u03e5\u03e7\5\u00aeX\2\u03e6\u03de\3"
          + "\2\2\2\u03e6\u03df\3\2\2\2\u03e6\u03e0\3\2\2\2\u03e6\u03e1\3\2\2\2\u03e6"
          + "\u03e2\3\2\2\2\u03e6\u03e3\3\2\2\2\u03e6\u03e4\3\2\2\2\u03e6\u03e5\3\2"
          + "\2\2\u03e7\177\3\2\2\2\u03e8\u03e9\7Q\2\2\u03e9\u03eb\7\u0086\2\2\u03ea"
          + "\u03ec\5\u00ceh\2\u03eb\u03ea\3\2\2\2\u03eb\u03ec\3\2\2\2\u03ec\u03ed"
          + "\3\2\2\2\u03ed\u03ee\7g\2\2\u03ee\u03ef\5\u00f8}\2\u03ef\u03f0\7\u0082"
          + "\2\2\u03f0\u0081\3\2\2\2\u03f1\u03f3\5\u008cG\2\u03f2\u03f1\3\2\2\2\u03f2"
          + "\u03f3\3\2\2\2\u03f3\u03f5\3\2\2\2\u03f4\u03f6\5\u00e0q\2\u03f5\u03f4"
          + "\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6\u03f7\3\2\2\2\u03f7\u0400\7\u0082\2"
          + "\2\u03f8\u03fa\5\u00ceh\2\u03f9\u03fb\5\u008cG\2\u03fa\u03f9\3\2\2\2\u03fa"
          + "\u03fb\3\2\2\2\u03fb\u03fc\3\2\2\2\u03fc\u03fd\5\u00e0q\2\u03fd\u03fe"
          + "\7\u0082\2\2\u03fe\u0400\3\2\2\2\u03ff\u03f2\3\2\2\2\u03ff\u03f8\3\2\2"
          + "\2\u0400\u0083\3\2\2\2\u0401\u0402\7B\2\2\u0402\u0403\7W\2\2\u0403\u0404"
          + "\5^\60\2\u0404\u0405\7|\2\2\u0405\u0406\7\6\2\2\u0406\u0407\7X\2\2\u0407"
          + "\u0408\7\u0082\2\2\u0408\u0085\3\2\2\2\u0409\u040a\7\u0082\2\2\u040a\u0087"
          + "\3\2\2\2\u040b\u040c\5\u00ceh\2\u040c\u040d\7\u0082\2\2\u040d\u0089\3"
          + "\2\2\2\u040e\u0415\5\u008eH\2\u040f\u0415\5\u0094K\2\u0410\u0415\5\u0090"
          + "I\2\u0411\u0415\7+\2\2\u0412\u0415\7L\2\2\u0413\u0415\7\31\2\2\u0414\u040e"
          + "\3\2\2\2\u0414\u040f\3\2\2\2\u0414\u0410\3\2\2\2\u0414\u0411\3\2\2\2\u0414"
          + "\u0412\3\2\2\2\u0414\u0413\3\2\2\2\u0415\u008b\3\2\2\2\u0416\u0418\5\u008a"
          + "F\2\u0417\u0416\3\2\2\2\u0418\u0419\3\2\2\2\u0419\u041a\3\2\2\2\u0419"
          + "\u0417\3\2\2\2\u041a\u041c\3\2\2\2\u041b\u041d\5\u00ceh\2\u041c\u041b"
          + "\3\2\2\2\u041c\u041d\3\2\2\2\u041d\u008d\3\2\2\2\u041e\u041f\t\r\2\2\u041f"
          + "\u008f\3\2\2\2\u0420\u0421\t\16\2\2\u0421\u0091\3\2\2\2\u0422\u0423\7"
          + "\u0086\2\2\u0423\u0093\3\2\2\2\u0424\u0428\5\u0096L\2\u0425\u0428\5\u011a"
          + "\u008e\2\u0426\u0428\5\u00aaV\2\u0427\u0424\3\2\2\2\u0427\u0425\3\2\2"
          + "\2\u0427\u0426\3\2\2\2\u0428\u0095\3\2\2\2\u0429\u042e\5\u00a0Q\2\u042a"
          + "\u042e\5\u00a6T\2\u042b\u042e\5\u0162\u00b2\2\u042c\u042e\5\u00f2z\2\u042d"
          + "\u0429\3\2\2\2\u042d\u042a\3\2\2\2\u042d\u042b\3\2\2\2\u042d\u042c\3\2"
          + "\2\2\u042e\u0097\3\2\2\2\u042f\u0431\5\u0094K\2\u0430\u042f\3\2\2\2\u0431"
          + "\u0432\3\2\2\2\u0432\u0430\3\2\2\2\u0432\u0433\3\2\2\2\u0433\u0435\3\2"
          + "\2\2\u0434\u0436\5\u00ceh\2\u0435\u0434\3\2\2\2\u0435\u0436\3\2\2\2\u0436"
          + "\u0099\3\2\2\2\u0437\u0439\5\u0096L\2\u0438\u0437\3\2\2\2\u0439\u043a"
          + "\3\2\2\2\u043a\u0438\3\2\2\2\u043a\u043b\3\2\2\2\u043b\u043d\3\2\2\2\u043c"
          + "\u043e\5\u00ceh\2\u043d\u043c\3\2\2\2\u043d\u043e\3\2\2\2\u043e\u009b"
          + "\3\2\2\2\u043f\u0440\t\17\2\2\u0440\u009d\3\2\2\2\u0441\u0442\t\20\2\2"
          + "\u0442\u009f\3\2\2\2\u0443\u0445\5\f\7\2\u0444\u0443\3\2\2\2\u0444\u0445"
          + "\3\2\2\2\u0445\u0446\3\2\2\2\u0446\u0478\5\u00a2R\2\u0447\u0448\5\f\7"
          + "\2\u0448\u0449\7F\2\2\u0449\u044a\5\u0158\u00ad\2\u044a\u0478\3\2\2\2"
          + "\u044b\u0478\5\u009eP\2\u044c\u044e\5\u009eP\2\u044d\u044c\3\2\2\2\u044d"
          + "\u044e\3\2\2\2\u044e\u0450\3\2\2\2\u044f\u0451\5\u009cO\2\u0450\u044f"
          + "\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u0450\3\2\2\2\u0452\u0453\3\2\2\2\u0453"
          + "\u0478\3\2\2\2\u0454\u0456\5\u009eP\2\u0455\u0454\3\2\2\2\u0455\u0456"
          + "\3\2\2\2\u0456\u0457\3\2\2\2\u0457\u0478\7\24\2\2\u0458\u045a\5\u009e"
          + "P\2\u0459\u0458\3\2\2\2\u0459\u045a\3\2\2\2\u045a\u045b\3\2\2\2\u045b"
          + "\u0478\7\25\2\2\u045c\u045e\5\u009eP\2\u045d\u045c\3\2\2\2\u045d\u045e"
          + "\3\2\2\2\u045e\u045f\3\2\2\2\u045f\u0478\7\26\2\2\u0460\u0462\5\u009e"
          + "P\2\u0461\u0460\3\2\2\2\u0461\u0462\3\2\2\2\u0462\u0463\3\2\2\2\u0463"
          + "\u0478\7U\2\2\u0464\u0478\7\20\2\2\u0465\u0467\5\u009eP\2\u0466\u0465"
          + "\3\2\2\2\u0466\u0467\3\2\2\2\u0467\u046b\3\2\2\2\u0468\u046a\5\u009cO"
          + "\2\u0469\u0468\3\2\2\2\u046a\u046d\3\2\2\2\u046b\u0469\3\2\2\2\u046b\u046c"
          + "\3\2\2\2\u046c\u046e\3\2\2\2\u046d\u046b\3\2\2\2\u046e\u0478\7/\2\2\u046f"
          + "\u0478\7)\2\2\u0470\u0472\5\u009cO\2\u0471\u0470\3\2\2\2\u0471\u0472\3"
          + "\2\2\2\u0472\u0473\3\2\2\2\u0473\u0478\7 \2\2\u0474\u0478\7S\2\2\u0475"
          + "\u0478\7\17\2\2\u0476\u0478\5\u00a4S\2\u0477\u0444\3\2\2\2\u0477\u0447"
          + "\3\2\2\2\u0477\u044b\3\2\2\2\u0477\u044d\3\2\2\2\u0477\u0455\3\2\2\2\u0477"
          + "\u0459\3\2\2\2\u0477\u045d\3\2\2\2\u0477\u0461\3\2\2\2\u0477\u0464\3\2"
          + "\2\2\u0477\u0466\3\2\2\2\u0477\u046f\3\2\2\2\u0477\u0471\3\2\2\2\u0477"
          + "\u0474\3\2\2\2\u0477\u0475\3\2\2\2\u0477\u0476\3\2\2\2\u0478\u00a1\3\2"
          + "\2\2\u0479\u047e\5\u0118\u008d\2\u047a\u047e\5\u00a8U\2\u047b\u047e\5"
          + "\u0092J\2\u047c\u047e\5\u0158\u00ad\2\u047d\u0479\3\2\2\2\u047d\u047a"
          + "\3\2\2\2\u047d\u047b\3\2\2\2\u047d\u047c\3\2\2\2\u047e\u00a3\3\2\2\2\u047f"
          + "\u0480\7\34\2\2\u0480\u0483\7W\2\2\u0481\u0484\5\\/\2\u0482\u0484\7\17"
          + "\2\2\u0483\u0481\3\2\2\2\u0483\u0482\3\2\2\2\u0484\u0485\3\2\2\2\u0485"
          + "\u0486\7X\2\2\u0486\u00a5\3\2\2\2\u0487\u0496\5\u0122\u0092\2\u0488\u048a"
          + "\5\u00ceh\2\u0489\u0488\3\2\2\2\u0489\u048a\3\2\2\2\u048a\u048c\3\2\2"
          + "\2\u048b\u048d\5\f\7\2\u048c\u048b\3\2\2\2\u048c\u048d\3\2\2\2\u048d\u048e"
          + "\3\2\2\2\u048e\u0497\7\u0086\2\2\u048f\u0497\5\u0158\u00ad\2\u0490\u0492"
          + "\5\f\7\2\u0491\u0493\7F\2\2\u0492\u0491\3\2\2\2\u0492\u0493\3\2\2\2\u0493"
          + "\u0494\3\2\2\2\u0494\u0495\5\u0158\u00ad\2\u0495\u0497\3\2\2\2\u0496\u0489"
          + "\3\2\2\2\u0496\u048f\3\2\2\2\u0496\u0490\3\2\2\2\u0497\u049e\3\2\2\2\u0498"
          + "\u049a\7#\2\2\u0499\u049b\5\f\7\2\u049a\u0499\3\2\2\2\u049a\u049b\3\2"
          + "\2\2\u049b\u049c\3\2\2\2\u049c\u049e\7\u0086\2\2\u049d\u0487\3\2\2\2\u049d"
          + "\u0498\3\2\2\2\u049e\u00a7\3\2\2\2\u049f\u04a0\7\u0086\2\2\u04a0\u00a9"
          + "\3\2\2\2\u04a1\u04a2\5\u00acW\2\u04a2\u04a7\7[\2\2\u04a3\u04a5\5\u00b4"
          + "[\2\u04a4\u04a6\7|\2\2\u04a5\u04a4\3\2\2\2\u04a5\u04a6\3\2\2\2\u04a6\u04a8"
          + "\3\2\2\2\u04a7\u04a3\3\2\2\2\u04a7\u04a8\3\2\2\2\u04a8\u04a9\3\2\2\2\u04a9"
          + "\u04aa\7\\\2\2\u04aa\u00ab\3\2\2\2\u04ab\u04ad\5\u00b0Y\2\u04ac\u04ae"
          + "\5\u00ceh\2\u04ad\u04ac\3\2\2\2\u04ad\u04ae\3\2\2\2\u04ae\u04b3\3\2\2"
          + "\2\u04af\u04b1\5\f\7\2\u04b0\u04af\3\2\2\2\u04b0\u04b1\3\2\2\2\u04b1\u04b2"
          + "\3\2\2\2\u04b2\u04b4\7\u0086\2\2\u04b3\u04b0\3\2\2\2\u04b3\u04b4\3\2\2"
          + "\2\u04b4\u04b6\3\2\2\2\u04b5\u04b7\5\u00b2Z\2\u04b6\u04b5\3\2\2\2\u04b6"
          + "\u04b7\3\2\2\2\u04b7\u00ad\3\2\2\2\u04b8\u04ba\5\u00b0Y\2\u04b9\u04bb"
          + "\5\u00ceh\2\u04ba\u04b9\3\2\2\2\u04ba\u04bb\3\2\2\2\u04bb\u04bc\3\2\2"
          + "\2\u04bc\u04be\7\u0086\2\2\u04bd\u04bf\5\u00b2Z\2\u04be\u04bd\3\2\2\2"
          + "\u04be\u04bf\3\2\2\2\u04bf\u04c0\3\2\2\2\u04c0\u04c1\7\u0082\2\2\u04c1"
          + "\u00af\3\2\2\2\u04c2\u04c4\7#\2\2\u04c3\u04c5\t\21\2\2\u04c4\u04c3\3\2"
          + "\2\2\u04c4\u04c5\3\2\2\2\u04c5\u00b1\3\2\2\2\u04c6\u04c7\7\u0080\2\2\u04c7"
          + "\u04c8\5\u0098M\2\u04c8\u00b3\3\2\2\2\u04c9\u04ce\5\u00b6\\\2\u04ca\u04cb"
          + "\7|\2\2\u04cb\u04cd\5\u00b6\\\2\u04cc\u04ca\3\2\2\2\u04cd\u04d0\3\2\2"
          + "\2\u04ce\u04cc\3\2\2\2\u04ce\u04cf\3\2\2\2\u04cf\u00b5\3\2\2\2\u04d0\u04ce"
          + "\3\2\2\2\u04d1\u04d4\5\u00b8]\2\u04d2\u04d3\7g\2\2\u04d3\u04d5\5^\60\2"
          + "\u04d4\u04d2\3\2\2\2\u04d4\u04d5\3\2\2\2\u04d5\u00b7\3\2\2\2\u04d6\u04d7"
          + "\7\u0086\2\2\u04d7\u00b9\3\2\2\2\u04d8\u04db\5\u00bc_\2\u04d9\u04db\5"
          + "\u00c0a\2\u04da\u04d8\3\2\2\2\u04da\u04d9\3\2\2\2\u04db\u00bb\3\2\2\2"
          + "\u04dc\u04dd\7\u0086\2\2\u04dd\u00bd\3\2\2\2\u04de\u04e0\7.\2\2\u04df"
          + "\u04de\3\2\2\2\u04df\u04e0\3\2\2\2\u04e0\u04e1\3\2\2\2\u04e1\u04e4\7\62"
          + "\2\2\u04e2\u04e5\7\u0086\2\2\u04e3\u04e5\5\u00bc_\2\u04e4\u04e2\3\2\2"
          + "\2\u04e4\u04e3\3\2\2\2\u04e4\u04e5\3\2\2\2\u04e5\u04e6\3\2\2\2\u04e6\u04e8"
          + "\7[\2\2\u04e7\u04e9\5z>\2\u04e8\u04e7\3\2\2\2\u04e8\u04e9\3\2\2\2\u04e9"
          + "\u04ea\3\2\2\2\u04ea\u04eb\7\\\2\2\u04eb\u00bf\3\2\2\2\u04ec\u04ed\7\u0086"
          + "\2\2\u04ed\u00c1\3\2\2\2\u04ee\u04ef\7\62\2\2\u04ef\u04f0\7\u0086\2\2"
          + "\u04f0\u04f1\7g\2\2\u04f1\u04f2\5\u00c4c\2\u04f2\u04f3\7\u0082\2\2\u04f3"
          + "\u00c3\3\2\2\2\u04f4\u04f6\5\f\7\2\u04f5\u04f4\3\2\2\2\u04f5\u04f6\3\2"
          + "\2\2\u04f6\u04f7\3\2\2\2\u04f7\u04f8\5\u00ba^\2\u04f8\u00c5\3\2\2\2\u04f9"
          + "\u04ff\7Q\2\2\u04fa\u04fc\7N\2\2\u04fb\u04fa\3\2\2\2\u04fb\u04fc\3\2\2"
          + "\2\u04fc\u04fd\3\2\2\2\u04fd\u0500\5\f\7\2\u04fe\u0500\7\u0081\2\2\u04ff"
          + "\u04fb\3\2\2\2\u04ff\u04fe\3\2\2\2\u0500\u0501\3\2\2\2\u0501\u0502\5\b"
          + "\5\2\u0502\u0503\7\u0082\2\2\u0503\u00c7\3\2\2\2\u0504\u0506\5\u00ceh"
          + "\2\u0505\u0504\3\2\2\2\u0505\u0506\3\2\2\2\u0506\u0507\3\2\2\2\u0507\u0508"
          + "\7Q\2\2\u0508\u050a\7\62\2\2\u0509\u050b\5\f\7\2\u050a\u0509\3\2\2\2\u050a"
          + "\u050b\3\2\2\2\u050b\u050c\3\2\2\2\u050c\u050d\5\u00ba^\2\u050d\u050e"
          + "\7\u0082\2\2\u050e\u00c9\3\2\2\2\u050f\u0510\7\16\2\2\u0510\u0511\7W\2"
          + "\2\u0511\u0512\7\6\2\2\u0512\u0513\7X\2\2\u0513\u0514\7\u0082\2\2\u0514"
          + "\u00cb\3\2\2\2\u0515\u0516\7&\2\2\u0516\u051d\7\6\2\2\u0517\u0519\7[\2"
          + "\2\u0518\u051a\5z>\2\u0519\u0518\3\2\2\2\u0519\u051a\3\2\2\2\u051a\u051b"
          + "\3\2\2\2\u051b\u051e\7\\\2\2\u051c\u051e\5|?\2\u051d\u0517\3\2\2\2\u051d"
          + "\u051c\3\2\2\2\u051e\u00cd\3\2\2\2\u051f\u0521\5\u00d0i\2\u0520\u051f"
          + "\3\2\2\2\u0521\u0522\3\2\2\2\u0522\u0520\3\2\2\2\u0522\u0523\3\2\2\2\u0523"
          + "\u00cf\3\2\2\2\u0524\u0525\7Y\2\2\u0525\u0527\7Y\2\2\u0526\u0528\5\u00d4"
          + "k\2\u0527\u0526\3\2\2\2\u0527\u0528\3\2\2\2\u0528\u0529\3\2\2\2\u0529"
          + "\u052a\7Z\2\2\u052a\u052d\7Z\2\2\u052b\u052d\5\u00d2j\2\u052c\u0524\3"
          + "\2\2\2\u052c\u052b\3\2\2\2\u052d\u00d1\3\2\2\2\u052e\u052f\7\f\2\2\u052f"
          + "\u0532\7W\2\2\u0530\u0533\5\u00f8}\2\u0531\u0533\5^\60\2\u0532\u0530\3"
          + "\2\2\2\u0532\u0531\3\2\2\2\u0533\u0535\3\2\2\2\u0534\u0536\7\u0085\2\2"
          + "\u0535\u0534\3\2\2\2\u0535\u0536\3\2\2\2\u0536\u0537\3\2\2\2\u0537\u0538"
          + "\7X\2\2\u0538\u00d3\3\2\2\2\u0539\u053e\5\u00d6l\2\u053a\u053b\7|\2\2"
          + "\u053b\u053d\5\u00d6l\2\u053c\u053a\3\2\2\2\u053d\u0540\3\2\2\2\u053e"
          + "\u053c\3\2\2\2\u053e\u053f\3\2\2\2\u053f\u0542\3\2\2\2\u0540\u053e\3\2"
          + "\2\2\u0541\u0543\7\u0085\2\2\u0542\u0541\3\2\2\2\u0542\u0543\3\2\2\2\u0543"
          + "\u00d5\3\2\2\2\u0544\u0545\5\u00d8m\2\u0545\u0546\7\u0081\2\2\u0546\u0548"
          + "\3\2\2\2\u0547\u0544\3\2\2\2\u0547\u0548\3\2\2\2\u0548\u0549\3\2\2\2\u0549"
          + "\u054b\7\u0086\2\2\u054a\u054c\5\u00dan\2\u054b\u054a\3\2\2\2\u054b\u054c"
          + "\3\2\2\2\u054c\u00d7\3\2\2\2\u054d\u054e\7\u0086\2\2\u054e\u00d9\3\2\2"
          + "\2\u054f\u0551\7W\2\2\u0550\u0552\5\u00dco\2\u0551\u0550\3\2\2\2\u0551"
          + "\u0552\3\2\2\2\u0552\u0553\3\2\2\2\u0553\u0554\7X\2\2\u0554\u00db\3\2"
          + "\2\2\u0555\u0557\5\u00dep\2\u0556\u0555\3\2\2\2\u0557\u0558\3\2\2\2\u0558"
          + "\u0556\3\2\2\2\u0558\u0559\3\2\2\2\u0559\u00dd\3\2\2\2\u055a\u055b\7W"
          + "\2\2\u055b\u055c\5\u00dco\2\u055c\u055d\7X\2\2\u055d\u056c\3\2\2\2\u055e"
          + "\u055f\7Y\2\2\u055f\u0560\5\u00dco\2\u0560\u0561\7Z\2\2\u0561\u056c\3"
          + "\2\2\2\u0562\u0563\7[\2\2\u0563\u0564\5\u00dco\2\u0564\u0565\7\\\2\2\u0565"
          + "\u056c\3\2\2\2\u0566\u0568\n\22\2\2\u0567\u0566\3\2\2\2\u0568\u0569\3"
          + "\2\2\2\u0569\u0567\3\2\2\2\u0569\u056a\3\2\2\2\u056a\u056c\3\2\2\2\u056b"
          + "\u055a\3\2\2\2\u056b\u055e\3\2\2\2\u056b\u0562\3\2\2\2\u056b\u0567\3\2"
          + "\2\2\u056c\u00df\3\2\2\2\u056d\u0572\5\u00e2r\2\u056e\u056f\7|\2\2\u056f"
          + "\u0571\5\u00e2r\2\u0570\u056e\3\2\2\2\u0571\u0574\3\2\2\2\u0572\u0570"
          + "\3\2\2\2\u0572\u0573\3\2\2\2\u0573\u00e1\3\2\2\2\u0574\u0572\3\2\2\2\u0575"
          + "\u0577\5\u00e4s\2\u0576\u0578\5\u010e\u0088\2\u0577\u0576\3\2\2\2\u0577"
          + "\u0578\3\2\2\2\u0578\u00e3\3\2\2\2\u0579\u057f\5\u00e6t\2\u057a\u057b"
          + "\5\u00e8u\2\u057b\u057c\5\u00eav\2\u057c\u057d\5\u00ecw\2\u057d\u057f"
          + "\3\2\2\2\u057e\u0579\3\2\2\2\u057e\u057a\3\2\2\2\u057f\u00e5\3\2\2\2\u0580"
          + "\u0582\5\u00eex\2\u0581\u0583\7\30\2\2\u0582\u0581\3\2\2\2\u0582\u0583"
          + "\3\2\2\2\u0583\u0585\3\2\2\2\u0584\u0580\3\2\2\2\u0585\u0588\3\2\2\2\u0586"
          + "\u0584\3\2\2\2\u0586\u0587\3\2\2\2\u0587\u0589\3\2\2\2\u0588\u0586\3\2"
          + "\2\2\u0589\u058a\5\u00e8u\2\u058a\u00e7\3\2\2\2\u058b\u058c\bu\1\2\u058c"
          + "\u058e\5\u00f6|\2\u058d\u058f\5\u00ceh\2\u058e\u058d\3\2\2\2\u058e\u058f"
          + "\3\2\2\2\u058f\u0595\3\2\2\2\u0590\u0591\7W\2\2\u0591\u0592\5\u00e6t\2"
          + "\u0592\u0593\7X\2\2\u0593\u0595\3\2\2\2\u0594\u058b\3\2\2\2\u0594\u0590"
          + "\3\2\2\2\u0595\u05a4\3\2\2\2\u0596\u05a0\f\4\2\2\u0597\u05a1\5\u00eav"
          + "\2\u0598\u059a\7Y\2\2\u0599\u059b\5^\60\2\u059a\u0599\3\2\2\2\u059a\u059b"
          + "\3\2\2\2\u059b\u059c\3\2\2\2\u059c\u059e\7Z\2\2\u059d\u059f\5\u00ceh\2"
          + "\u059e\u059d\3\2\2\2\u059e\u059f\3\2\2\2\u059f\u05a1\3\2\2\2\u05a0\u0597"
          + "\3\2\2\2\u05a0\u0598\3\2\2\2\u05a1\u05a3\3\2\2\2\u05a2\u0596\3\2\2\2\u05a3"
          + "\u05a6\3\2\2\2\u05a4\u05a2\3\2\2\2\u05a4\u05a5\3\2\2\2\u05a5\u00e9\3\2"
          + "\2\2\u05a6\u05a4\3\2\2\2\u05a7\u05a9\7W\2\2\u05a8\u05aa\5\u0104\u0083"
          + "\2\u05a9\u05a8\3\2\2\2\u05a9\u05aa\3\2\2\2\u05aa\u05ab\3\2\2\2\u05ab\u05ad"
          + "\7X\2\2\u05ac\u05ae\5\u00f0y\2\u05ad\u05ac\3\2\2\2\u05ad\u05ae\3\2\2\2"
          + "\u05ae\u05b0\3\2\2\2\u05af\u05b1\5\u00f4{\2\u05b0\u05af\3\2\2\2\u05b0"
          + "\u05b1\3\2\2\2\u05b1\u05b3\3\2\2\2\u05b2\u05b4\5\u0174\u00bb\2\u05b3\u05b2"
          + "\3\2\2\2\u05b3\u05b4\3\2\2\2\u05b4\u05b6\3\2\2\2\u05b5\u05b7\5\u00ceh"
          + "\2\u05b6\u05b5\3\2\2\2\u05b6\u05b7\3\2\2\2\u05b7\u00eb\3\2\2\2\u05b8\u05b9"
          + "\7~\2\2\u05b9\u05bb\5\u009aN\2\u05ba\u05bc\5\u00fa~\2\u05bb\u05ba\3\2"
          + "\2\2\u05bb\u05bc\3\2\2\2\u05bc\u00ed\3\2\2\2\u05bd\u05bf\t\23\2\2\u05be"
          + "\u05c0\5\u00ceh\2\u05bf\u05be\3\2\2\2\u05bf\u05c0\3\2\2\2\u05c0\u05cc"
          + "\3\2\2\2\u05c1\u05c3\5\f\7\2\u05c2\u05c1\3\2\2\2\u05c2\u05c3\3\2\2\2\u05c3"
          + "\u05c4\3\2\2\2\u05c4\u05c6\7_\2\2\u05c5\u05c7\5\u00ceh\2\u05c6\u05c5\3"
          + "\2\2\2\u05c6\u05c7\3\2\2\2\u05c7\u05c9\3\2\2\2\u05c8\u05ca\5\u00f0y\2"
          + "\u05c9\u05c8\3\2\2\2\u05c9\u05ca\3\2\2\2\u05ca\u05cc\3\2\2\2\u05cb\u05bd"
          + "\3\2\2\2\u05cb\u05c2\3\2\2\2\u05cc\u00ef\3\2\2\2\u05cd\u05cf\5\u00f2z"
          + "\2\u05ce\u05cd\3\2\2\2\u05cf\u05d0\3\2\2\2\u05d0\u05ce\3\2\2\2\u05d0\u05d1"
          + "\3\2\2\2\u05d1\u00f1\3\2\2\2\u05d2\u05d3\t\24\2\2\u05d3\u00f3\3\2\2\2"
          + "\u05d4\u05d5\t\23\2\2\u05d5\u00f5\3\2\2\2\u05d6\u05d8\7\u0085\2\2\u05d7"
          + "\u05d6\3\2\2\2\u05d7\u05d8\3\2\2\2\u05d8\u05d9\3\2\2\2\u05d9\u05da\5\6"
          + "\4\2\u05da\u00f7\3\2\2\2\u05db\u05dd\5\u0098M\2\u05dc\u05de\5\u00fa~\2"
          + "\u05dd\u05dc\3\2\2\2\u05dd\u05de\3\2\2\2\u05de\u00f9\3\2\2\2\u05df\u05e8"
          + "\5\u00fc\177\2\u05e0\u05e2\5\u00fe\u0080\2\u05e1\u05e0\3\2\2\2\u05e1\u05e2"
          + "\3\2\2\2\u05e2\u05e3\3\2\2\2\u05e3\u05e4\5\u00eav\2\u05e4\u05e5\5\u00ec"
          + "w\2\u05e5\u05e8\3\2\2\2\u05e6\u05e8\5\u0100\u0081\2\u05e7\u05df\3\2\2"
          + "\2\u05e7\u05e1\3\2\2\2\u05e7\u05e6\3\2\2\2\u05e8\u00fb\3\2\2\2\u05e9\u05f3"
          + "\5\u00fe\u0080\2\u05ea\u05ec\5\u00eex\2\u05eb\u05ea\3\2\2\2\u05ec\u05ed"
          + "\3\2\2\2\u05ed\u05eb\3\2\2\2\u05ed\u05ee\3\2\2\2\u05ee\u05f0\3\2\2\2\u05ef"
          + "\u05f1\5\u00fe\u0080\2\u05f0\u05ef\3\2\2\2\u05f0\u05f1\3\2\2\2\u05f1\u05f3"
          + "\3\2\2\2\u05f2\u05e9\3\2\2\2\u05f2\u05eb\3\2\2\2\u05f3\u00fd\3\2\2\2\u05f4"
          + "\u05f5\b\u0080\1\2\u05f5\u0603\5\u00eav\2\u05f6\u05f8\7Y\2\2\u05f7\u05f9"
          + "\5^\60\2\u05f8\u05f7\3\2\2\2\u05f8\u05f9\3\2\2\2\u05f9\u05fa\3\2\2\2\u05fa"
          + "\u05fc\7Z\2\2\u05fb\u05fd\5\u00ceh\2\u05fc\u05fb\3\2\2\2\u05fc\u05fd\3"
          + "\2\2\2\u05fd\u0603\3\2\2\2\u05fe\u05ff\7W\2\2\u05ff\u0600\5\u00fc\177"
          + "\2\u0600\u0601\7X\2\2\u0601\u0603\3\2\2\2\u0602\u05f4\3\2\2\2\u0602\u05f6"
          + "\3\2\2\2\u0602\u05fe\3\2\2\2\u0603\u0613\3\2\2\2\u0604\u060f\f\6\2\2\u0605"
          + "\u0610\5\u00eav\2\u0606\u0607\5\u00fe\u0080\2\u0607\u0609\7Y\2\2\u0608"
          + "\u060a\5^\60\2\u0609\u0608\3\2\2\2\u0609\u060a\3\2\2\2\u060a\u060b\3\2"
          + "\2\2\u060b\u060d\7Z\2\2\u060c\u060e\5\u00ceh\2\u060d\u060c\3\2\2\2\u060d"
          + "\u060e\3\2\2\2\u060e\u0610\3\2\2\2\u060f\u0605\3\2\2\2\u060f\u0606\3\2"
          + "\2\2\u0610\u0612\3\2\2\2\u0611\u0604\3\2\2\2\u0612\u0615\3\2\2\2\u0613"
          + "\u0611\3\2\2\2\u0613\u0614\3\2\2\2\u0614\u00ff\3\2\2\2\u0615\u0613\3\2"
          + "\2\2\u0616\u0618\5\u00eex\2\u0617\u0616\3\2\2\2\u0618\u061b\3\2\2\2\u0619"
          + "\u0617\3\2\2\2\u0619\u061a\3\2\2\2\u061a\u061c\3\2\2\2\u061b\u0619\3\2"
          + "\2\2\u061c\u061d\5\u0102\u0082\2\u061d\u0101\3\2\2\2\u061e\u061f\b\u0082"
          + "\1\2\u061f\u0620\7\u0085\2\2\u0620\u062f\3\2\2\2\u0621\u062b\f\4\2\2\u0622"
          + "\u062c\5\u00eav\2\u0623\u0625\7Y\2\2\u0624\u0626\5^\60\2\u0625\u0624\3"
          + "\2\2\2\u0625\u0626\3\2\2\2\u0626\u0627\3\2\2\2\u0627\u0629\7Z\2\2\u0628"
          + "\u062a\5\u00ceh\2\u0629\u0628\3\2\2\2\u0629\u062a\3\2\2\2\u062a\u062c"
          + "\3\2\2\2\u062b\u0622\3\2\2\2\u062b\u0623\3\2\2\2\u062c\u062e\3\2\2\2\u062d"
          + "\u0621\3\2\2\2\u062e\u0631\3\2\2\2\u062f\u062d\3\2\2\2\u062f\u0630\3\2"
          + "\2\2\u0630\u0103\3\2\2\2\u0631\u062f\3\2\2\2\u0632\u0637\5\u0106\u0084"
          + "\2\u0633\u0635\7|\2\2\u0634\u0633\3\2\2\2\u0634\u0635\3\2\2\2\u0635\u0636"
          + "\3\2\2\2\u0636\u0638\7\u0085\2\2\u0637\u0634\3\2\2\2\u0637\u0638\3\2\2"
          + "\2\u0638\u0105\3\2\2\2\u0639\u063e\5\u0108\u0085\2\u063a\u063b\7|\2\2"
          + "\u063b\u063d\5\u0108\u0085\2\u063c\u063a\3\2\2\2\u063d\u0640\3\2\2\2\u063e"
          + "\u063c\3\2\2\2\u063e\u063f\3\2\2\2\u063f\u0107\3\2\2\2\u0640\u063e\3\2"
          + "\2\2\u0641\u0643\5\u00ceh\2\u0642\u0641\3\2\2\2\u0642\u0643\3\2\2\2\u0643"
          + "\u0644\3\2\2\2\u0644\u0649\5\u008cG\2\u0645\u064a\5\u00e4s\2\u0646\u0648"
          + "\5\u00fa~\2\u0647\u0646\3\2\2\2\u0647\u0648\3\2\2\2\u0648\u064a\3\2\2"
          + "\2\u0649\u0645\3\2\2\2\u0649\u0647\3\2\2\2\u064a\u064d\3\2\2\2\u064b\u064c"
          + "\7g\2\2\u064c\u064e\5\u0112\u008a\2\u064d\u064b\3\2\2\2\u064d\u064e\3"
          + "\2\2\2\u064e\u0109\3\2\2\2\u064f\u0651\5\u00ceh\2\u0650\u064f\3\2\2\2"
          + "\u0650\u0651\3\2\2\2\u0651\u0653\3\2\2\2\u0652\u0654\5\u008cG\2\u0653"
          + "\u0652\3\2\2\2\u0653\u0654\3\2\2\2\u0654\u0655\3\2\2\2\u0655\u0657\5\u00e4"
          + "s\2\u0656\u0658\5\u012c\u0097\2\u0657\u0656\3\2\2\2\u0657\u0658\3\2\2"
          + "\2\u0658\u0659\3\2\2\2\u0659\u065a\5\u010c\u0087\2\u065a\u010b\3\2\2\2"
          + "\u065b\u065d\5\u0144\u00a3\2\u065c\u065b\3\2\2\2\u065c\u065d\3\2\2\2\u065d"
          + "\u065e\3\2\2\2\u065e\u0664\5f\64\2\u065f\u0664\5\u016a\u00b6\2\u0660\u0661"
          + "\7g\2\2\u0661\u0662\t\25\2\2\u0662\u0664\7\u0082\2\2\u0663\u065c\3\2\2"
          + "\2\u0663\u065f\3\2\2\2\u0663\u0660\3\2\2\2\u0664\u010d\3\2\2\2\u0665\u066b"
          + "\5\u0110\u0089\2\u0666\u0667\7W\2\2\u0667\u0668\5$\23\2\u0668\u0669\7"
          + "X\2\2\u0669\u066b\3\2\2\2\u066a\u0665\3\2\2\2\u066a\u0666\3\2\2\2\u066b"
          + "\u010f\3\2\2\2\u066c\u066d\7g\2\2\u066d\u0670\5\u0112\u008a\2\u066e\u0670"
          + "\5\u0116\u008c\2\u066f\u066c\3\2\2\2\u066f\u066e\3\2\2\2\u0670\u0111\3"
          + "\2\2\2\u0671\u0674\5X-\2\u0672\u0674\5\u0116\u008c\2\u0673\u0671\3\2\2"
          + "\2\u0673\u0672\3\2\2\2\u0674\u0113\3\2\2\2\u0675\u0677\5\u0112\u008a\2"
          + "\u0676\u0678\7\u0085\2\2\u0677\u0676\3\2\2\2\u0677\u0678\3\2\2\2\u0678"
          + "\u0680\3\2\2\2\u0679\u067a\7|\2\2\u067a\u067c\5\u0112\u008a\2\u067b\u067d"
          + "\7\u0085\2\2\u067c\u067b\3\2\2\2\u067c\u067d\3\2\2\2\u067d\u067f\3\2\2"
          + "\2\u067e\u0679\3\2\2\2\u067f\u0682\3\2\2\2\u0680\u067e\3\2\2\2\u0680\u0681"
          + "\3\2\2\2\u0681\u0115\3\2\2\2\u0682\u0680\3\2\2\2\u0683\u0688\7[\2\2\u0684"
          + "\u0686\5\u0114\u008b\2\u0685\u0687\7|\2\2\u0686\u0685\3\2\2\2\u0686\u0687"
          + "\3\2\2\2\u0687\u0689\3\2\2\2\u0688\u0684\3\2\2\2\u0688\u0689\3\2\2\2\u0689"
          + "\u068a\3\2\2\2\u068a\u068b\7\\\2\2\u068b\u0117\3\2\2\2\u068c\u068f\7\u0086"
          + "\2\2\u068d\u068f\5\u0158\u00ad\2\u068e\u068c\3\2\2\2\u068e\u068d\3\2\2"
          + "\2\u068f\u0119\3\2\2\2\u0690\u0691\5\u011c\u008f\2\u0691\u0693\7[\2\2"
          + "\u0692\u0694\5\u0124\u0093\2\u0693\u0692\3\2\2\2\u0693\u0694\3\2\2\2\u0694"
          + "\u0695\3\2\2\2\u0695\u0696\7\\\2\2\u0696\u011b\3\2\2\2\u0697\u0699\5\u0122"
          + "\u0092\2\u0698\u069a\5\u00ceh\2\u0699\u0698\3\2\2\2\u0699\u069a\3\2\2"
          + "\2\u069a\u069f\3\2\2\2\u069b\u069d\5\u011e\u0090\2\u069c\u069e\5\u0120"
          + "\u0091\2\u069d\u069c\3\2\2\2\u069d\u069e\3\2\2\2\u069e\u06a0\3\2\2\2\u069f"
          + "\u069b\3\2\2\2\u069f\u06a0\3\2\2\2\u06a0\u06a2\3\2\2\2\u06a1\u06a3\5\u0132"
          + "\u009a\2\u06a2\u06a1\3\2\2\2\u06a2\u06a3\3\2\2\2\u06a3\u06af\3\2\2\2\u06a4"
          + "\u06a6\7O\2\2\u06a5\u06a7\5\u00ceh\2\u06a6\u06a5\3\2\2\2\u06a6\u06a7\3"
          + "\2\2\2\u06a7\u06ac\3\2\2\2\u06a8\u06aa\5\u011e\u0090\2\u06a9\u06ab\5\u0120"
          + "\u0091\2\u06aa\u06a9\3\2\2\2\u06aa\u06ab\3\2\2\2\u06ab\u06ad\3\2\2\2\u06ac"
          + "\u06a8\3\2\2\2\u06ac\u06ad\3\2\2\2\u06ad\u06af\3\2\2\2\u06ae\u0697\3\2"
          + "\2\2\u06ae\u06a4\3\2\2\2\u06af\u011d\3\2\2\2\u06b0\u06b2\5\f\7\2\u06b1"
          + "\u06b0\3\2\2\2\u06b1\u06b2\3\2\2\2\u06b2\u06b3\3\2\2\2\u06b3\u06b4\5\u0118"
          + "\u008d\2\u06b4\u011f\3\2\2\2\u06b5\u06b6\7(\2\2\u06b6\u0121\3\2\2\2\u06b7"
          + "\u06b8\t\21\2\2\u06b8\u0123\3\2\2\2\u06b9\u06be\5\u0126\u0094\2\u06ba"
          + "\u06bb\5\u013c\u009f\2\u06bb\u06bc\7\u0080\2\2\u06bc\u06be\3\2\2\2\u06bd"
          + "\u06b9\3\2\2\2\u06bd\u06ba\3\2\2\2\u06be\u06bf\3\2\2\2\u06bf\u06bd\3\2"
          + "\2\2\u06bf\u06c0\3\2\2\2\u06c0\u0125\3\2\2\2\u06c1\u06c3\5\u00ceh\2\u06c2"
          + "\u06c1\3\2\2\2\u06c2\u06c3\3\2\2\2\u06c3\u06c5\3\2\2\2\u06c4\u06c6\5\u008c"
          + "G\2\u06c5\u06c4\3\2\2\2\u06c5\u06c6\3\2\2\2\u06c6\u06c8\3\2\2\2\u06c7"
          + "\u06c9\5\u0128\u0095\2\u06c8\u06c7\3\2\2\2\u06c8\u06c9\3\2\2\2\u06c9\u06ca"
          + "\3\2\2\2\u06ca\u06d2\7\u0082\2\2\u06cb\u06d2\5\u010a\u0086\2\u06cc\u06d2"
          + "\5\u00c6d\2\u06cd\u06d2\5\u0084C\2\u06ce\u06d2\5\u0150\u00a9\2\u06cf\u06d2"
          + "\5\u0080A\2\u06d0\u06d2\5\u0086D\2\u06d1\u06c2\3\2\2\2\u06d1\u06cb\3\2"
          + "\2\2\u06d1\u06cc\3\2\2\2\u06d1\u06cd\3\2\2\2\u06d1\u06ce\3\2\2\2\u06d1"
          + "\u06cf\3\2\2\2\u06d1\u06d0\3\2\2\2\u06d2\u0127\3\2\2\2\u06d3\u06d8\5\u012a"
          + "\u0096\2\u06d4\u06d5\7|\2\2\u06d5\u06d7\5\u012a\u0096\2\u06d6\u06d4\3"
          + "\2\2\2\u06d7\u06da\3\2\2\2\u06d8\u06d6\3\2\2\2\u06d8\u06d9\3\2\2\2\u06d9"
          + "\u0129\3\2\2\2\u06da\u06d8\3\2\2\2\u06db\u06e5\5\u00e4s\2\u06dc\u06de"
          + "\5\u012c\u0097\2\u06dd\u06dc\3\2\2\2\u06dd\u06de\3\2\2\2\u06de\u06e0\3"
          + "\2\2\2\u06df\u06e1\5\u0130\u0099\2\u06e0\u06df\3\2\2\2\u06e0\u06e1\3\2"
          + "\2\2\u06e1\u06e6\3\2\2\2\u06e2\u06e4\5\u0110\u0089\2\u06e3\u06e2\3\2\2"
          + "\2\u06e3\u06e4\3\2\2\2\u06e4\u06e6\3\2\2\2\u06e5\u06dd\3\2\2\2\u06e5\u06e3"
          + "\3\2\2\2\u06e6\u06f0\3\2\2\2\u06e7\u06e9\7\u0086\2\2\u06e8\u06e7\3\2\2"
          + "\2\u06e8\u06e9\3\2\2\2\u06e9\u06eb\3\2\2\2\u06ea\u06ec\5\u00ceh\2\u06eb"
          + "\u06ea\3\2\2\2\u06eb\u06ec\3\2\2\2\u06ec\u06ed\3\2\2\2\u06ed\u06ee\7\u0080"
          + "\2\2\u06ee\u06f0\5^\60\2\u06ef\u06db\3\2\2\2\u06ef\u06e8\3\2\2\2\u06f0"
          + "\u012b\3\2\2\2\u06f1\u06f3\5\u012e\u0098\2\u06f2\u06f1\3\2\2\2\u06f3\u06f4"
          + "\3\2\2\2\u06f4\u06f2\3\2\2\2\u06f4\u06f5\3\2\2\2\u06f5\u012d\3\2\2\2\u06f6"
          + "\u06f7\t\26\2\2\u06f7\u012f\3\2\2\2\u06f8\u06f9\7g\2\2\u06f9\u06fa\7\u0088"
          + "\2\2\u06fa\u06fb\b\u0099\1\2\u06fb\u0131\3\2\2\2\u06fc\u06fd\7\u0080\2"
          + "\2\u06fd\u06fe\5\u0134\u009b\2\u06fe\u0133\3\2\2\2\u06ff\u0701\5\u0136"
          + "\u009c\2\u0700\u0702\7\u0085\2\2\u0701\u0700\3\2\2\2\u0701\u0702\3\2\2"
          + "\2\u0702\u070a\3\2\2\2\u0703\u0704\7|\2\2\u0704\u0706\5\u0136\u009c\2"
          + "\u0705\u0707\7\u0085\2\2\u0706\u0705\3\2\2\2\u0706\u0707\3\2\2\2\u0707"
          + "\u0709\3\2\2\2\u0708\u0703\3\2\2\2\u0709\u070c\3\2\2\2\u070a\u0708\3\2"
          + "\2\2\u070a\u070b\3\2\2\2\u070b\u0135\3\2\2\2\u070c\u070a\3\2\2\2\u070d"
          + "\u070f\5\u00ceh\2\u070e\u070d\3\2\2\2\u070e\u070f\3\2\2\2\u070f\u071c"
          + "\3\2\2\2\u0710\u071d\5\u013a\u009e\2\u0711\u0713\7R\2\2\u0712\u0714\5"
          + "\u013c\u009f\2\u0713\u0712\3\2\2\2\u0713\u0714\3\2\2\2\u0714\u0715\3\2"
          + "\2\2\u0715\u071d\5\u013a\u009e\2\u0716\u0718\5\u013c\u009f\2\u0717\u0719"
          + "\7R\2\2\u0718\u0717\3\2\2\2\u0718\u0719\3\2\2\2\u0719\u071a\3\2\2\2\u071a"
          + "\u071b\5\u013a\u009e\2\u071b\u071d\3\2\2\2\u071c\u0710\3\2\2\2\u071c\u0711"
          + "\3\2\2\2\u071c\u0716\3\2\2\2\u071d\u0137\3\2\2\2\u071e\u0720\5\f\7\2\u071f"
          + "\u071e\3\2\2\2\u071f\u0720\3\2\2\2\u0720\u0721\3\2\2\2\u0721\u0724\5\u0118"
          + "\u008d\2\u0722\u0724\5\u00a4S\2\u0723\u071f\3\2\2\2\u0723\u0722\3\2\2"
          + "\2\u0724\u0139\3\2\2\2\u0725\u0726\5\u0138\u009d\2\u0726\u013b\3\2\2\2"
          + "\u0727\u0728\t\27\2\2\u0728\u013d\3\2\2\2\u0729\u072a\7\66\2\2\u072a\u072b"
          + "\5\u0140\u00a1\2\u072b\u013f\3\2\2\2\u072c\u072e\5\u0098M\2\u072d\u072f"
          + "\5\u0142\u00a2\2\u072e\u072d\3\2\2\2\u072e\u072f\3\2\2\2\u072f\u0141\3"
          + "\2\2\2\u0730\u0732\5\u00eex\2\u0731\u0733\5\u0142\u00a2\2\u0732\u0731"
          + "\3\2\2\2\u0732\u0733\3\2\2\2\u0733\u0143\3\2\2\2\u0734\u0735\7\u0080\2"
          + "\2\u0735\u0736\5\u0146\u00a4\2\u0736\u0145\3\2\2\2\u0737\u0739\5\u0148"
          + "\u00a5\2\u0738\u073a\7\u0085\2\2\u0739\u0738\3\2\2\2\u0739\u073a\3\2\2"
          + "\2\u073a\u0742\3\2\2\2\u073b\u073c\7|\2\2\u073c\u073e\5\u0148\u00a5\2"
          + "\u073d\u073f\7\u0085\2\2\u073e\u073d\3\2\2\2\u073e\u073f\3\2\2\2\u073f"
          + "\u0741\3\2\2\2\u0740\u073b\3\2\2\2\u0741\u0744\3\2\2\2\u0742\u0740\3\2"
          + "\2\2\u0742\u0743\3\2\2\2\u0743\u0147\3\2\2\2\u0744\u0742\3\2\2\2\u0745"
          + "\u074c\5\u014a\u00a6\2\u0746\u0748\7W\2\2\u0747\u0749\5$\23\2\u0748\u0747"
          + "\3\2\2\2\u0748\u0749\3\2\2\2\u0749\u074a\3\2\2\2\u074a\u074d\7X\2\2\u074b"
          + "\u074d\5\u0116\u008c\2\u074c\u0746\3\2\2\2\u074c\u074b\3\2\2\2\u074d\u0149"
          + "\3\2\2\2\u074e\u0751\5\u0138\u009d\2\u074f\u0751\7\u0086\2\2\u0750\u074e"
          + "\3\2\2\2\u0750\u074f\3\2\2\2\u0751\u014b\3\2\2\2\u0752\u0753\7\66\2\2"
          + "\u0753\u0754\5\u017c\u00bf\2\u0754\u014d\3\2\2\2\u0755\u0759\7\66\2\2"
          + "\u0756\u0757\7\6\2\2\u0757\u075a\7\u0086\2\2\u0758\u075a\7\u008e\2\2\u0759"
          + "\u0756\3\2\2\2\u0759\u0758\3\2\2\2\u075a\u014f\3\2\2\2\u075b\u075c\7F"
          + "\2\2\u075c\u075d\7h\2\2\u075d\u075e\5\u0152\u00aa\2\u075e\u075f\7i\2\2"
          + "\u075f\u0760\5|?\2\u0760\u0151\3\2\2\2\u0761\u0766\5\u0154\u00ab\2\u0762"
          + "\u0763\7|\2\2\u0763\u0765\5\u0154\u00ab\2\u0764\u0762\3\2\2\2\u0765\u0768"
          + "\3\2\2\2\u0766\u0764\3\2\2\2\u0766\u0767\3\2\2\2\u0767\u0153\3\2\2\2\u0768"
          + "\u0766\3\2\2\2\u0769\u076c\5\u0156\u00ac\2\u076a\u076c\5\u0108\u0085\2"
          + "\u076b\u0769\3\2\2\2\u076b\u076a\3\2\2\2\u076c\u0155\3\2\2\2\u076d\u076e"
          + "\7F\2\2\u076e\u076f\7h\2\2\u076f\u0770\5\u0152\u00aa\2\u0770\u0771\7i"
          + "\2\2\u0771\u0773\3\2\2\2\u0772\u076d\3\2\2\2\u0772\u0773\3\2\2\2\u0773"
          + "\u0774\3\2\2\2\u0774\u0777\7\27\2\2\u0775\u0777\7N\2\2\u0776\u0772\3\2"
          + "\2\2\u0776\u0775\3\2\2\2\u0777\u0783\3\2\2\2\u0778\u077a\7\u0085\2\2\u0779"
          + "\u0778\3\2\2\2\u0779\u077a\3\2\2\2\u077a\u077c\3\2\2\2\u077b\u077d\7\u0086"
          + "\2\2\u077c\u077b\3\2\2\2\u077c\u077d\3\2\2\2\u077d\u0784\3\2\2\2\u077e"
          + "\u0780\7\u0086\2\2\u077f\u077e\3\2\2\2\u077f\u0780\3\2\2\2\u0780\u0781"
          + "\3\2\2\2\u0781\u0782\7g\2\2\u0782\u0784\5\u00f8}\2\u0783\u0779\3\2\2\2"
          + "\u0783\u077f\3\2\2\2\u0784\u0157\3\2\2\2\u0785\u0786\5\u015c\u00af\2\u0786"
          + "\u0788\7h\2\2\u0787\u0789\5\u015e\u00b0\2\u0788\u0787\3\2\2\2\u0788\u0789"
          + "\3\2\2\2\u0789\u078a\3\2\2\2\u078a\u078b\7i\2\2\u078b\u0159\3\2\2\2\u078c"
          + "\u0798\5\u0158\u00ad\2\u078d\u0790\5\u014c\u00a7\2\u078e\u0790\5\u014e"
          + "\u00a8\2\u078f\u078d\3\2\2\2\u078f\u078e\3\2\2\2\u0790\u0791\3\2\2\2\u0791"
          + "\u0793\7h\2\2\u0792\u0794\5\u015e\u00b0\2\u0793\u0792\3\2\2\2\u0793\u0794"
          + "\3\2\2\2\u0794\u0795\3\2\2\2\u0795\u0796\7i\2\2\u0796\u0798\3\2\2\2\u0797"
          + "\u078c\3\2\2\2\u0797\u078f\3\2\2\2\u0798\u015b\3\2\2\2\u0799\u079a\7\u0086"
          + "\2\2\u079a\u015d\3\2\2\2\u079b\u079d\5\u0160\u00b1\2\u079c\u079e\7\u0085"
          + "\2\2\u079d\u079c\3\2\2\2\u079d\u079e\3\2\2\2\u079e\u07a6\3\2\2\2\u079f"
          + "\u07a0\7|\2\2\u07a0\u07a2\5\u0160\u00b1\2\u07a1\u07a3\7\u0085\2\2\u07a2"
          + "\u07a1\3\2\2\2\u07a2\u07a3\3\2\2\2\u07a3\u07a5\3\2\2\2\u07a4\u079f\3\2"
          + "\2\2\u07a5\u07a8\3\2\2\2\u07a6\u07a4\3\2\2\2\u07a6\u07a7\3\2\2\2\u07a7"
          + "\u015f\3\2\2\2\u07a8\u07a6\3\2\2\2\u07a9\u07ad\5\u00f8}\2\u07aa\u07ad"
          + "\5^\60\2\u07ab\u07ad\5\6\4\2\u07ac\u07a9\3\2\2\2\u07ac\u07aa\3\2\2\2\u07ac"
          + "\u07ab\3\2\2\2\u07ad\u0161\3\2\2\2\u07ae\u07af\7N\2\2\u07af\u07b5\5\f"
          + "\7\2\u07b0\u07b6\7\u0086\2\2\u07b1\u07b3\7F\2\2\u07b2\u07b1\3\2\2\2\u07b2"
          + "\u07b3\3\2\2\2\u07b3\u07b4\3\2\2\2\u07b4\u07b6\5\u0158\u00ad\2\u07b5\u07b0"
          + "\3\2\2\2\u07b5\u07b2\3\2\2\2\u07b6\u0163\3\2\2\2\u07b7\u07b9\7&\2\2\u07b8"
          + "\u07b7\3\2\2\2\u07b8\u07b9\3\2\2\2\u07b9\u07ba\3\2\2\2\u07ba\u07bb\7F"
          + "\2\2\u07bb\u07bc\5|?\2\u07bc\u0165\3\2\2\2\u07bd\u07be\7F\2\2\u07be\u07bf"
          + "\7h\2\2\u07bf\u07c0\7i\2\2\u07c0\u07c1\5|?\2\u07c1\u0167\3\2\2\2\u07c2"
          + "\u07c3\7K\2\2\u07c3\u07c4\5f\64\2\u07c4\u07c5\5\u016c\u00b7\2\u07c5\u0169"
          + "\3\2\2\2\u07c6\u07c8\7K\2\2\u07c7\u07c9\5\u0144\u00a3\2\u07c8\u07c7\3"
          + "\2\2\2\u07c8\u07c9\3\2\2\2\u07c9\u07ca\3\2\2\2\u07ca\u07cb\5f\64\2\u07cb"
          + "\u07cc\5\u016c\u00b7\2\u07cc\u016b\3\2\2\2\u07cd\u07cf\5\u016e\u00b8\2"
          + "\u07ce\u07cd\3\2\2\2\u07cf\u07d0\3\2\2\2\u07d0\u07ce\3\2\2\2\u07d0\u07d1"
          + "\3\2\2\2\u07d1\u016d\3\2\2\2\u07d2\u07d3\7\23\2\2\u07d3\u07d4\7W\2\2\u07d4"
          + "\u07d5\5\u0170\u00b9\2\u07d5\u07d6\7X\2\2\u07d6\u07d7\5f\64\2\u07d7\u016f"
          + "\3\2\2\2\u07d8\u07da\5\u00ceh\2\u07d9\u07d8\3\2\2\2\u07d9\u07da\3\2\2"
          + "\2\u07da\u07db\3\2\2\2\u07db\u07de\5\u0098M\2\u07dc\u07df\5\u00e4s\2\u07dd"
          + "\u07df\5\u00fa~\2\u07de\u07dc\3\2\2\2\u07de\u07dd\3\2\2\2\u07de\u07df"
          + "\3\2\2\2\u07df\u07e2\3\2\2\2\u07e0\u07e2\7\u0085\2\2\u07e1\u07d9\3\2\2"
          + "\2\u07e1\u07e0\3\2\2\2\u07e2\u0171\3\2\2\2\u07e3\u07e5\7I\2\2\u07e4\u07e6"
          + "\5X-\2\u07e5\u07e4\3\2\2\2\u07e5\u07e6\3\2\2\2\u07e6\u0173\3\2\2\2\u07e7"
          + "\u07ea\5\u0176\u00bc\2\u07e8\u07ea\5\u017a\u00be\2\u07e9\u07e7\3\2\2\2"
          + "\u07e9\u07e8\3\2\2\2\u07ea\u0175\3\2\2\2\u07eb\u07ec\7I\2\2\u07ec\u07ee"
          + "\7W\2\2\u07ed\u07ef\5\u0178\u00bd\2\u07ee\u07ed\3\2\2\2\u07ee\u07ef\3"
          + "\2\2\2\u07ef\u07f0\3\2\2\2\u07f0\u07f1\7X\2\2\u07f1\u0177\3\2\2\2\u07f2"
          + "\u07f4\5\u00f8}\2\u07f3\u07f5\7\u0085\2\2\u07f4\u07f3\3\2\2\2\u07f4\u07f5"
          + "\3\2\2\2\u07f5\u07fd\3\2\2\2\u07f6\u07f7\7|\2\2\u07f7\u07f9\5\u00f8}\2"
          + "\u07f8\u07fa\7\u0085\2\2\u07f9\u07f8\3\2\2\2\u07f9\u07fa\3\2\2\2\u07fa"
          + "\u07fc\3\2\2\2\u07fb\u07f6\3\2\2\2\u07fc\u07ff\3\2\2\2\u07fd\u07fb\3\2"
          + "\2\2\u07fd\u07fe\3\2\2\2\u07fe\u0179\3\2\2\2\u07ff\u07fd\3\2\2\2\u0800"
          + "\u0801\7\64\2\2\u0801\u0802\7W\2\2\u0802\u0803\5^\60\2\u0803\u0804\7X"
          + "\2\2\u0804\u0807\3\2\2\2\u0805\u0807\7\64\2\2\u0806\u0800\3\2\2\2\u0806"
          + "\u0805\3\2\2\2\u0807\u017b\3\2\2\2\u0808\u080b\7\63\2\2\u0809\u080a\7"
          + "Y\2\2\u080a\u080c\7Z\2\2\u080b\u0809\3\2\2\2\u080b\u080c\3\2\2\2\u080c"
          + "\u083c\3\2\2\2\u080d\u0810\7\36\2\2\u080e\u080f\7Y\2\2\u080f\u0811\7Z"
          + "\2\2\u0810\u080e\3\2\2\2\u0810\u0811\3\2\2\2\u0811\u083c\3\2\2\2\u0812"
          + "\u083c\7]\2\2\u0813\u083c\7^\2\2\u0814\u083c\7_\2\2\u0815\u083c\7`\2\2"
          + "\u0816\u083c\7a\2\2\u0817\u083c\7b\2\2\u0818\u083c\7c\2\2\u0819\u083c"
          + "\7d\2\2\u081a\u083c\7e\2\2\u081b\u083c\7f\2\2\u081c\u083c\7g\2\2\u081d"
          + "\u083c\7i\2\2\u081e\u083c\7h\2\2\u081f\u083c\7w\2\2\u0820\u083c\7j\2\2"
          + "\u0821\u083c\7k\2\2\u0822\u083c\7l\2\2\u0823\u083c\7n\2\2\u0824\u083c"
          + "\7o\2\2\u0825\u083c\7p\2\2\u0826\u083c\7q\2\2\u0827\u0828\7h\2\2\u0828"
          + "\u083c\7h\2\2\u0829\u082a\7i\2\2\u082a\u083c\7i\2\2\u082b\u083c\7s\2\2"
          + "\u082c\u083c\7r\2\2\u082d\u083c\7t\2\2\u082e\u083c\7u\2\2\u082f\u083c"
          + "\7v\2\2\u0830\u083c\7x\2\2\u0831\u083c\7y\2\2\u0832\u083c\7z\2\2\u0833"
          + "\u083c\7{\2\2\u0834\u083c\7|\2\2\u0835\u083c\7}\2\2\u0836\u083c\7~\2\2"
          + "\u0837\u0838\7W\2\2\u0838\u083c\7X\2\2\u0839\u083a\7Y\2\2\u083a\u083c"
          + "\7Z\2\2\u083b\u0808\3\2\2\2\u083b\u080d\3\2\2\2\u083b\u0812\3\2\2\2\u083b"
          + "\u0813\3\2\2\2\u083b\u0814\3\2\2\2\u083b\u0815\3\2\2\2\u083b\u0816\3\2"
          + "\2\2\u083b\u0817\3\2\2\2\u083b\u0818\3\2\2\2\u083b\u0819\3\2\2\2\u083b"
          + "\u081a\3\2\2\2\u083b\u081b\3\2\2\2\u083b\u081c\3\2\2\2\u083b\u081d\3\2"
          + "\2\2\u083b\u081e\3\2\2\2\u083b\u081f\3\2\2\2\u083b\u0820\3\2\2\2\u083b"
          + "\u0821\3\2\2\2\u083b\u0822\3\2\2\2\u083b\u0823\3\2\2\2\u083b\u0824\3\2"
          + "\2\2\u083b\u0825\3\2\2\2\u083b\u0826\3\2\2\2\u083b\u0827\3\2\2\2\u083b"
          + "\u0829\3\2\2\2\u083b\u082b\3\2\2\2\u083b\u082c\3\2\2\2\u083b\u082d\3\2"
          + "\2\2\u083b\u082e\3\2\2\2\u083b\u082f\3\2\2\2\u083b\u0830\3\2\2\2\u083b"
          + "\u0831\3\2\2\2\u083b\u0832\3\2\2\2\u083b\u0833\3\2\2\2\u083b\u0834\3\2"
          + "\2\2\u083b\u0835\3\2\2\2\u083b\u0836\3\2\2\2\u083b\u0837\3\2\2\2\u083b"
          + "\u0839\3\2\2\2\u083c\u017d\3\2\2\2\u083d\u083e\t\30\2\2\u083e\u017f\3"
          + "\2\2\2\u0134\u0181\u0188\u0191\u0195\u019e\u01a1\u01a5\u01ad\u01b4\u01b7"
          + "\u01bc\u01c1\u01c7\u01cf\u01d1\u01da\u01de\u01e2\u01e5\u01e9\u01ec\u01f3"
          + "\u01f7\u01fa\u01fd\u0200\u0206\u020a\u020e\u021c\u0220\u0226\u022d\u0233"
          + "\u0237\u023b\u023d\u0245\u024a\u0257\u025e\u026a\u0274\u0279\u027d\u0284"
          + "\u0287\u028f\u0293\u0296\u029d\u02a4\u02a8\u02ad\u02b1\u02b4\u02b9\u02c8"
          + "\u02cf\u02d7\u02df\u02e8\u02ef\u02f6\u02fe\u0306\u030e\u0316\u031e\u0326"
          + "\u032f\u0337\u0340\u0348\u0350\u0352\u0355\u035b\u0361\u0367\u036e\u0377"
          + "\u037f\u0383\u038a\u038c\u03a0\u03a4\u03aa\u03af\u03b3\u03b6\u03bd\u03c4"
          + "\u03c8\u03d1\u03dc\u03e6\u03eb\u03f2\u03f5\u03fa\u03ff\u0414\u0419\u041c"
          + "\u0427\u042d\u0432\u0435\u043a\u043d\u0444\u044d\u0452\u0455\u0459\u045d"
          + "\u0461\u0466\u046b\u0471\u0477\u047d\u0483\u0489\u048c\u0492\u0496\u049a"
          + "\u049d\u04a5\u04a7\u04ad\u04b0\u04b3\u04b6\u04ba\u04be\u04c4\u04ce\u04d4"
          + "\u04da\u04df\u04e4\u04e8\u04f5\u04fb\u04ff\u0505\u050a\u0519\u051d\u0522"
          + "\u0527\u052c\u0532\u0535\u053e\u0542\u0547\u054b\u0551\u0558\u0569\u056b"
          + "\u0572\u0577\u057e\u0582\u0586\u058e\u0594\u059a\u059e\u05a0\u05a4\u05a9"
          + "\u05ad\u05b0\u05b3\u05b6\u05bb\u05bf\u05c2\u05c6\u05c9\u05cb\u05d0\u05d7"
          + "\u05dd\u05e1\u05e7\u05ed\u05f0\u05f2\u05f8\u05fc\u0602\u0609\u060d\u060f"
          + "\u0613\u0619\u0625\u0629\u062b\u062f\u0634\u0637\u063e\u0642\u0647\u0649"
          + "\u064d\u0650\u0653\u0657\u065c\u0663\u066a\u066f\u0673\u0677\u067c\u0680"
          + "\u0686\u0688\u068e\u0693\u0699\u069d\u069f\u06a2\u06a6\u06aa\u06ac\u06ae"
          + "\u06b1\u06bd\u06bf\u06c2\u06c5\u06c8\u06d1\u06d8\u06dd\u06e0\u06e3\u06e5"
          + "\u06e8\u06eb\u06ef\u06f4\u0701\u0706\u070a\u070e\u0713\u0718\u071c\u071f"
          + "\u0723\u072e\u0732\u0739\u073e\u0742\u0748\u074c\u0750\u0759\u0766\u076b"
          + "\u0772\u0776\u0779\u077c\u077f\u0783\u0788\u078f\u0793\u0797\u079d\u07a2"
          + "\u07a6\u07ac\u07b2\u07b5\u07b8\u07c8\u07d0\u07d9\u07de\u07e1\u07e5\u07e9"
          + "\u07ee\u07f4\u07f9\u07fd\u0806\u080b\u0810\u083b";
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

  public CPP14Parser(TokenStream input) {
    super(input);
    _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
  }

  private static String[] makeRuleNames() {
    return new String[] {
      "translationUnit",
      "primaryExpression",
      "idExpression",
      "unqualifiedId",
      "qualifiedId",
      "nestedNameSpecifier",
      "lambdaExpression",
      "lambdaIntroducer",
      "lambdaCapture",
      "captureDefault",
      "captureList",
      "capture",
      "simpleCapture",
      "initcapture",
      "lambdaDeclarator",
      "postfixExpression",
      "typeIdOfTheTypeId",
      "expressionList",
      "pseudoDestructorName",
      "unaryExpression",
      "unaryOperator",
      "newExpression",
      "newPlacement",
      "newTypeId",
      "newDeclarator",
      "noPointerNewDeclarator",
      "newInitializer",
      "deleteExpression",
      "noExceptExpression",
      "castExpression",
      "pointerMemberExpression",
      "multiplicativeExpression",
      "additiveExpression",
      "shiftExpression",
      "shiftOperator",
      "relationalExpression",
      "equalityExpression",
      "andExpression",
      "exclusiveOrExpression",
      "inclusiveOrExpression",
      "logicalAndExpression",
      "logicalOrExpression",
      "conditionalExpression",
      "assignmentExpression",
      "assignmentOperator",
      "expression",
      "constantExpression",
      "statement",
      "labeledStatement",
      "expressionStatement",
      "compoundStatement",
      "statementSeq",
      "selectionStatement",
      "condition",
      "iterationStatement",
      "forInitStatement",
      "forRangeDeclaration",
      "forRangeInitializer",
      "jumpStatement",
      "declarationStatement",
      "declarationseq",
      "declaration",
      "blockDeclaration",
      "aliasDeclaration",
      "simpleDeclaration",
      "staticAssertDeclaration",
      "emptyDeclaration",
      "attributeDeclaration",
      "declSpecifier",
      "declSpecifierSeq",
      "storageClassSpecifier",
      "functionSpecifier",
      "typedefName",
      "typeSpecifier",
      "trailingTypeSpecifier",
      "typeSpecifierSeq",
      "trailingTypeSpecifierSeq",
      "simpleTypeLengthModifier",
      "simpleTypeSignednessModifier",
      "simpleTypeSpecifier",
      "theTypeName",
      "decltypeSpecifier",
      "elaboratedTypeSpecifier",
      "enumName",
      "enumSpecifier",
      "enumHead",
      "opaqueEnumDeclaration",
      "enumkey",
      "enumbase",
      "enumeratorList",
      "enumeratorDefinition",
      "enumerator",
      "namespaceName",
      "originalNamespaceName",
      "namespaceDefinition",
      "namespaceAlias",
      "namespaceAliasDefinition",
      "qualifiednamespacespecifier",
      "usingDeclaration",
      "usingDirective",
      "asmDefinition",
      "linkageSpecification",
      "attributeSpecifierSeq",
      "attributeSpecifier",
      "alignmentspecifier",
      "attributeList",
      "attribute",
      "attributeNamespace",
      "attributeArgumentClause",
      "balancedTokenSeq",
      "balancedtoken",
      "initDeclaratorList",
      "initDeclarator",
      "declarator",
      "pointerDeclarator",
      "noPointerDeclarator",
      "parametersAndQualifiers",
      "trailingReturnType",
      "pointerOperator",
      "cvqualifierseq",
      "cvQualifier",
      "refqualifier",
      "declaratorid",
      "theTypeId",
      "abstractDeclarator",
      "pointerAbstractDeclarator",
      "noPointerAbstractDeclarator",
      "abstractPackDeclarator",
      "noPointerAbstractPackDeclarator",
      "parameterDeclarationClause",
      "parameterDeclarationList",
      "parameterDeclaration",
      "functionDefinition",
      "functionBody",
      "initializer",
      "braceOrEqualInitializer",
      "initializerClause",
      "initializerList",
      "bracedInitList",
      "className",
      "classSpecifier",
      "classHead",
      "classHeadName",
      "classVirtSpecifier",
      "classKey",
      "memberSpecification",
      "memberdeclaration",
      "memberDeclaratorList",
      "memberDeclarator",
      "virtualSpecifierSeq",
      "virtualSpecifier",
      "pureSpecifier",
      "baseClause",
      "baseSpecifierList",
      "baseSpecifier",
      "classOrDeclType",
      "baseTypeSpecifier",
      "accessSpecifier",
      "conversionFunctionId",
      "conversionTypeId",
      "conversionDeclarator",
      "constructorInitializer",
      "memInitializerList",
      "memInitializer",
      "meminitializerid",
      "operatorFunctionId",
      "literalOperatorId",
      "templateDeclaration",
      "templateparameterList",
      "templateParameter",
      "typeParameter",
      "simpleTemplateId",
      "templateId",
      "templateName",
      "templateArgumentList",
      "templateArgument",
      "typeNameSpecifier",
      "explicitInstantiation",
      "explicitSpecialization",
      "tryBlock",
      "functionTryBlock",
      "handlerSeq",
      "handler",
      "exceptionDeclaration",
      "throwExpression",
      "exceptionSpecification",
      "dynamicExceptionSpecification",
      "typeIdList",
      "noeExceptSpecification",
      "theOperator",
      "literal"
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
      null,
      null,
      null,
      null,
      "'alignas'",
      "'alignof'",
      "'asm'",
      "'auto'",
      "'bool'",
      "'break'",
      "'case'",
      "'catch'",
      "'char'",
      "'char16_t'",
      "'char32_t'",
      "'class'",
      "'const'",
      "'constexpr'",
      "'const_cast'",
      "'continue'",
      "'decltype'",
      "'default'",
      "'delete'",
      "'do'",
      "'double'",
      "'dynamic_cast'",
      "'else'",
      "'enum'",
      "'explicit'",
      "'export'",
      "'extern'",
      "'false'",
      "'final'",
      "'float'",
      "'for'",
      "'friend'",
      "'goto'",
      "'if'",
      "'inline'",
      "'int'",
      "'long'",
      "'mutable'",
      "'namespace'",
      "'new'",
      "'noexcept'",
      "'nullptr'",
      "'operator'",
      "'override'",
      "'private'",
      "'protected'",
      "'public'",
      "'register'",
      "'reinterpret_cast'",
      "'return'",
      "'short'",
      "'signed'",
      "'sizeof'",
      "'static'",
      "'static_assert'",
      "'static_cast'",
      "'struct'",
      "'switch'",
      "'template'",
      "'this'",
      "'thread_local'",
      "'throw'",
      "'true'",
      "'try'",
      "'typedef'",
      "'typeid'",
      "'typename'",
      "'union'",
      "'unsigned'",
      "'using'",
      "'virtual'",
      "'void'",
      "'volatile'",
      "'wchar_t'",
      "'while'",
      "'('",
      "')'",
      "'['",
      "']'",
      "'{'",
      "'}'",
      "'+'",
      "'-'",
      "'*'",
      "'/'",
      "'%'",
      "'^'",
      "'&'",
      "'|'",
      "'~'",
      null,
      "'='",
      "'<'",
      "'>'",
      "'+='",
      "'-='",
      "'*='",
      "'/='",
      "'%='",
      "'^='",
      "'&='",
      "'|='",
      "'<<='",
      "'>>='",
      "'=='",
      "'!='",
      "'<='",
      "'>='",
      null,
      null,
      "'++'",
      "'--'",
      "','",
      "'->*'",
      "'->'",
      "'?'",
      "':'",
      "'::'",
      "';'",
      "'.'",
      "'.*'",
      "'...'"
    };
  }

  private static String[] makeSymbolicNames() {
    return new String[] {
      null,
      "IntegerLiteral",
      "CharacterLiteral",
      "FloatingLiteral",
      "StringLiteral",
      "BooleanLiteral",
      "PointerLiteral",
      "UserDefinedLiteral",
      "MultiLineMacro",
      "Directive",
      "Alignas",
      "Alignof",
      "Asm",
      "Auto",
      "Bool",
      "Break",
      "Case",
      "Catch",
      "Char",
      "Char16",
      "Char32",
      "Class",
      "Const",
      "Constexpr",
      "Const_cast",
      "Continue",
      "Decltype",
      "Default",
      "Delete",
      "Do",
      "Double",
      "Dynamic_cast",
      "Else",
      "Enum",
      "Explicit",
      "Export",
      "Extern",
      "False_",
      "Final",
      "Float",
      "For",
      "Friend",
      "Goto",
      "If",
      "Inline",
      "Int",
      "Long",
      "Mutable",
      "Namespace",
      "New",
      "Noexcept",
      "Nullptr",
      "Operator",
      "Override",
      "Private",
      "Protected",
      "Public",
      "Register",
      "Reinterpret_cast",
      "Return",
      "Short",
      "Signed",
      "Sizeof",
      "Static",
      "Static_assert",
      "Static_cast",
      "Struct",
      "Switch",
      "Template",
      "This",
      "Thread_local",
      "Throw",
      "True_",
      "Try",
      "Typedef",
      "Typeid_",
      "Typename_",
      "Union",
      "Unsigned",
      "Using",
      "Virtual",
      "Void",
      "Volatile",
      "Wchar",
      "While",
      "LeftParen",
      "RightParen",
      "LeftBracket",
      "RightBracket",
      "LeftBrace",
      "RightBrace",
      "Plus",
      "Minus",
      "Star",
      "Div",
      "Mod",
      "Caret",
      "And",
      "Or",
      "Tilde",
      "Not",
      "Assign",
      "Less",
      "Greater",
      "PlusAssign",
      "MinusAssign",
      "StarAssign",
      "DivAssign",
      "ModAssign",
      "XorAssign",
      "AndAssign",
      "OrAssign",
      "LeftShiftAssign",
      "RightShiftAssign",
      "Equal",
      "NotEqual",
      "LessEqual",
      "GreaterEqual",
      "AndAnd",
      "OrOr",
      "PlusPlus",
      "MinusMinus",
      "Comma",
      "ArrowStar",
      "Arrow",
      "Question",
      "Colon",
      "Doublecolon",
      "Semi",
      "Dot",
      "DotStar",
      "Ellipsis",
      "Identifier",
      "DecimalLiteral",
      "OctalLiteral",
      "HexadecimalLiteral",
      "BinaryLiteral",
      "Integersuffix",
      "UserDefinedIntegerLiteral",
      "UserDefinedFloatingLiteral",
      "UserDefinedStringLiteral",
      "UserDefinedCharacterLiteral",
      "Whitespace",
      "Newline",
      "BlockComment",
      "LineComment"
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
    return "CPP14Parser.g4";
  }

  @Override
  public ATN getATN() {
    return _ATN;
  }

  public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
    switch (ruleIndex) {
      case 5:
        return nestedNameSpecifier_sempred((NestedNameSpecifierContext) _localctx, predIndex);
      case 15:
        return postfixExpression_sempred((PostfixExpressionContext) _localctx, predIndex);
      case 25:
        return noPointerNewDeclarator_sempred((NoPointerNewDeclaratorContext) _localctx, predIndex);
      case 115:
        return noPointerDeclarator_sempred((NoPointerDeclaratorContext) _localctx, predIndex);
      case 126:
        return noPointerAbstractDeclarator_sempred(
            (NoPointerAbstractDeclaratorContext) _localctx, predIndex);
      case 128:
        return noPointerAbstractPackDeclarator_sempred(
            (NoPointerAbstractPackDeclaratorContext) _localctx, predIndex);
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

  private boolean noPointerNewDeclarator_sempred(
      NoPointerNewDeclaratorContext _localctx, int predIndex) {
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

  private boolean noPointerAbstractDeclarator_sempred(
      NoPointerAbstractDeclaratorContext _localctx, int predIndex) {
    switch (predIndex) {
      case 7:
        return precpred(_ctx, 4);
    }
    return true;
  }

  private boolean noPointerAbstractPackDeclarator_sempred(
      NoPointerAbstractPackDeclaratorContext _localctx, int predIndex) {
    switch (predIndex) {
      case 8:
        return precpred(_ctx, 2);
    }
    return true;
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
        if (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Asm - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Namespace - 10))
                            | (1L << (Operator - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Static_assert - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Template - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Using - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftParen - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Star - 74))
                            | (1L << (And - 74))
                            | (1L << (Tilde - 74))
                            | (1L << (AndAnd - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Semi - 74))
                            | (1L << (Ellipsis - 74))
                            | (1L << (Identifier - 74))))
                    != 0)) {
          {
            setState(382);
            declarationseq();
          }
        }

        setState(385);
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
              _alt = getInterpreter().adaptivePredict(_input, 1, _ctx);
            } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final IdExpressionContext idExpression() throws RecognitionException {
    IdExpressionContext _localctx = new IdExpressionContext(_ctx, getState());
    enterRule(_localctx, 4, RULE_idExpression);
    try {
      setState(403);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final UnqualifiedIdContext unqualifiedId() throws RecognitionException {
    UnqualifiedIdContext _localctx = new UnqualifiedIdContext(_ctx, getState());
    enterRule(_localctx, 6, RULE_unqualifiedId);
    try {
      setState(415);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 5, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Template) {
          {
            setState(418);
            match(Template);
          }
        }

        setState(421);
        unqualifiedId();
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

  public final NestedNameSpecifierContext nestedNameSpecifier() throws RecognitionException {
    return nestedNameSpecifier(0);
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
        if (_la == LeftParen) {
          {
            setState(446);
            lambdaDeclarator();
          }
        }

        setState(449);
        compoundStatement();
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
        if (((((_la - 69)) & ~0x3f) == 0
            && ((1L << (_la - 69))
                    & ((1L << (This - 69))
                        | (1L << (And - 69))
                        | (1L << (Assign - 69))
                        | (1L << (Identifier - 69))))
                != 0)) {
          {
            setState(452);
            lambdaCapture();
          }
        }

        setState(455);
        match(RightBracket);
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

  public final LambdaCaptureContext lambdaCapture() throws RecognitionException {
    LambdaCaptureContext _localctx = new LambdaCaptureContext(_ctx, getState());
    enterRule(_localctx, 16, RULE_lambdaCapture);
    int _la;
    try {
      setState(463);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
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
            if (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!(_la == And || _la == Assign)) {
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
        while (_la == Comma) {
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
        if (_la == Ellipsis) {
          {
            setState(475);
            match(Ellipsis);
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

  public final CaptureContext capture() throws RecognitionException {
    CaptureContext _localctx = new CaptureContext(_ctx, getState());
    enterRule(_localctx, 22, RULE_capture);
    try {
      setState(480);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 17, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
            if (_la == And) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == And) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Identifier - 74))))
                    != 0)) {
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
        if (_la == Mutable) {
          {
            setState(500);
            match(Mutable);
          }
        }

        setState(504);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == Noexcept || _la == Throw) {
          {
            setState(503);
            exceptionSpecification();
          }
        }

        setState(507);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == Alignas || _la == LeftBracket) {
          {
            setState(506);
            attributeSpecifierSeq();
          }
        }

        setState(510);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == Arrow) {
          {
            setState(509);
            trailingReturnType();
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

  public final PostfixExpressionContext postfixExpression() throws RecognitionException {
    return postfixExpression(0);
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final PseudoDestructorNameContext pseudoDestructorName() throws RecognitionException {
    PseudoDestructorNameContext _localctx = new PseudoDestructorNameContext(_ctx, getState());
    enterRule(_localctx, 36, RULE_pseudoDestructorName);
    int _la;
    try {
      setState(597);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 39, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(579);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 37, _ctx)) {
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
            if (_la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final UnaryExpressionContext unaryExpression() throws RecognitionException {
    UnaryExpressionContext _localctx = new UnaryExpressionContext(_ctx, getState());
    enterRule(_localctx, 38, RULE_unaryExpression);
    try {
      setState(626);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 42, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!(((((_la - 91)) & ~0x3f) == 0
            && ((1L << (_la - 91))
                    & ((1L << (Plus - 91))
                        | (1L << (Minus - 91))
                        | (1L << (Star - 91))
                        | (1L << (And - 91))
                        | (1L << (Or - 91))
                        | (1L << (Tilde - 91))
                        | (1L << (Not - 91))))
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
        if (_la == Doublecolon) {
          {
            setState(630);
            match(Doublecolon);
          }
        }

        setState(633);
        match(New);
        setState(635);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 44, _ctx)) {
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
        if (_la == LeftParen || _la == LeftBrace) {
          {
            setState(644);
            newInitializer();
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 47, _ctx)) {
          case 1:
            {
              setState(652);
              newDeclarator();
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
            switch (getInterpreter().adaptivePredict(_input, 48, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final NoPointerNewDeclaratorContext noPointerNewDeclarator() throws RecognitionException {
    return noPointerNewDeclarator(0);
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
            if ((((_la) & ~0x3f) == 0
                    && ((1L << _la)
                            & ((1L << IntegerLiteral)
                                | (1L << CharacterLiteral)
                                | (1L << FloatingLiteral)
                                | (1L << StringLiteral)
                                | (1L << BooleanLiteral)
                                | (1L << PointerLiteral)
                                | (1L << UserDefinedLiteral)
                                | (1L << Alignof)
                                | (1L << Auto)
                                | (1L << Bool)
                                | (1L << Char)
                                | (1L << Char16)
                                | (1L << Char32)
                                | (1L << Const_cast)
                                | (1L << Decltype)
                                | (1L << Delete)
                                | (1L << Double)
                                | (1L << Dynamic_cast)
                                | (1L << Float)
                                | (1L << Int)
                                | (1L << Long)
                                | (1L << New)
                                | (1L << Noexcept)
                                | (1L << Operator)
                                | (1L << Reinterpret_cast)
                                | (1L << Short)
                                | (1L << Signed)
                                | (1L << Sizeof)))
                        != 0)
                || ((((_la - 65)) & ~0x3f) == 0
                    && ((1L << (_la - 65))
                            & ((1L << (Static_cast - 65))
                                | (1L << (This - 65))
                                | (1L << (Throw - 65))
                                | (1L << (Typeid_ - 65))
                                | (1L << (Typename_ - 65))
                                | (1L << (Unsigned - 65))
                                | (1L << (Void - 65))
                                | (1L << (Wchar - 65))
                                | (1L << (LeftParen - 65))
                                | (1L << (LeftBracket - 65))
                                | (1L << (LeftBrace - 65))
                                | (1L << (Plus - 65))
                                | (1L << (Minus - 65))
                                | (1L << (Star - 65))
                                | (1L << (And - 65))
                                | (1L << (Or - 65))
                                | (1L << (Tilde - 65))
                                | (1L << (Not - 65))
                                | (1L << (PlusPlus - 65))
                                | (1L << (MinusMinus - 65))
                                | (1L << (Doublecolon - 65))))
                        != 0)
                || _la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Doublecolon) {
          {
            setState(689);
            match(Doublecolon);
          }
        }

        setState(692);
        match(Delete);
        setState(695);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 56, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final CastExpressionContext castExpression() throws RecognitionException {
    CastExpressionContext _localctx = new CastExpressionContext(_ctx, getState());
    enterRule(_localctx, 58, RULE_castExpression);
    try {
      setState(710);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 57, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final PointerMemberExpressionContext pointerMemberExpression()
      throws RecognitionException {
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
        while (_la == ArrowStar || _la == DotStar) {
          {
            {
              setState(713);
              _la = _input.LA(1);
              if (!(_la == ArrowStar || _la == DotStar)) {
                _errHandler.recoverInline(this);
              } else {
                if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
        while (((((_la - 93)) & ~0x3f) == 0
            && ((1L << (_la - 93))
                    & ((1L << (Star - 93)) | (1L << (Div - 93)) | (1L << (Mod - 93))))
                != 0)) {
          {
            {
              setState(721);
              _la = _input.LA(1);
              if (!(((((_la - 93)) & ~0x3f) == 0
                  && ((1L << (_la - 93))
                          & ((1L << (Star - 93)) | (1L << (Div - 93)) | (1L << (Mod - 93))))
                      != 0))) {
                _errHandler.recoverInline(this);
              } else {
                if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
        while (_la == Plus || _la == Minus) {
          {
            {
              setState(729);
              _la = _input.LA(1);
              if (!(_la == Plus || _la == Minus)) {
                _errHandler.recoverInline(this);
              } else {
                if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        _alt = getInterpreter().adaptivePredict(_input, 61, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
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
          _alt = getInterpreter().adaptivePredict(_input, 61, _ctx);
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        _alt = getInterpreter().adaptivePredict(_input, 63, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(752);
                _la = _input.LA(1);
                if (!(((((_la - 102)) & ~0x3f) == 0
                    && ((1L << (_la - 102))
                            & ((1L << (Less - 102))
                                | (1L << (Greater - 102))
                                | (1L << (LessEqual - 102))
                                | (1L << (GreaterEqual - 102))))
                        != 0))) {
                  _errHandler.recoverInline(this);
                } else {
                  if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
          _alt = getInterpreter().adaptivePredict(_input, 63, _ctx);
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
        while (_la == Equal || _la == NotEqual) {
          {
            {
              setState(760);
              _la = _input.LA(1);
              if (!(_la == Equal || _la == NotEqual)) {
                _errHandler.recoverInline(this);
              } else {
                if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == And) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Caret) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Or) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == AndAnd) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == OrOr) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Question) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final AssignmentExpressionContext assignmentExpression() throws RecognitionException {
    AssignmentExpressionContext _localctx = new AssignmentExpressionContext(_ctx, getState());
    enterRule(_localctx, 86, RULE_assignmentExpression);
    try {
      setState(821);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 71, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!(((((_la - 101)) & ~0x3f) == 0
            && ((1L << (_la - 101))
                    & ((1L << (Assign - 101))
                        | (1L << (PlusAssign - 101))
                        | (1L << (MinusAssign - 101))
                        | (1L << (StarAssign - 101))
                        | (1L << (DivAssign - 101))
                        | (1L << (ModAssign - 101))
                        | (1L << (XorAssign - 101))
                        | (1L << (AndAssign - 101))
                        | (1L << (OrAssign - 101))
                        | (1L << (LeftShiftAssign - 101))
                        | (1L << (RightShiftAssign - 101))))
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
        while (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    enterRule(_localctx, 94, RULE_statement);
    try {
      setState(848);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 75, _ctx)) {
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
            switch (getInterpreter().adaptivePredict(_input, 73, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Alignas || _la == LeftBracket) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << Alignof)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Const_cast)
                            | (1L << Decltype)
                            | (1L << Delete)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Float)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Operator)
                            | (1L << Reinterpret_cast)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)))
                    != 0)
            || ((((_la - 65)) & ~0x3f) == 0
                && ((1L << (_la - 65))
                        & ((1L << (Static_cast - 65))
                            | (1L << (This - 65))
                            | (1L << (Throw - 65))
                            | (1L << (Typeid_ - 65))
                            | (1L << (Typename_ - 65))
                            | (1L << (Unsigned - 65))
                            | (1L << (Void - 65))
                            | (1L << (Wchar - 65))
                            | (1L << (LeftParen - 65))
                            | (1L << (LeftBracket - 65))
                            | (1L << (Plus - 65))
                            | (1L << (Minus - 65))
                            | (1L << (Star - 65))
                            | (1L << (And - 65))
                            | (1L << (Or - 65))
                            | (1L << (Tilde - 65))
                            | (1L << (Not - 65))
                            | (1L << (PlusPlus - 65))
                            | (1L << (MinusMinus - 65))
                            | (1L << (Doublecolon - 65))))
                    != 0)
            || _la == Identifier) {
          {
            setState(862);
            expression();
          }
        }

        setState(865);
        match(Semi);
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
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << Alignas)
                            | (1L << Alignof)
                            | (1L << Asm)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Break)
                            | (1L << Case)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Class)
                            | (1L << Const)
                            | (1L << Constexpr)
                            | (1L << Const_cast)
                            | (1L << Continue)
                            | (1L << Decltype)
                            | (1L << Default)
                            | (1L << Delete)
                            | (1L << Do)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Enum)
                            | (1L << Explicit)
                            | (1L << Extern)
                            | (1L << Float)
                            | (1L << For)
                            | (1L << Friend)
                            | (1L << Goto)
                            | (1L << If)
                            | (1L << Inline)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << Mutable)
                            | (1L << Namespace)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Operator)
                            | (1L << Register)
                            | (1L << Reinterpret_cast)
                            | (1L << Return)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)
                            | (1L << Static)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (Static_assert - 64))
                            | (1L << (Static_cast - 64))
                            | (1L << (Struct - 64))
                            | (1L << (Switch - 64))
                            | (1L << (This - 64))
                            | (1L << (Thread_local - 64))
                            | (1L << (Throw - 64))
                            | (1L << (Try - 64))
                            | (1L << (Typedef - 64))
                            | (1L << (Typeid_ - 64))
                            | (1L << (Typename_ - 64))
                            | (1L << (Union - 64))
                            | (1L << (Unsigned - 64))
                            | (1L << (Using - 64))
                            | (1L << (Virtual - 64))
                            | (1L << (Void - 64))
                            | (1L << (Volatile - 64))
                            | (1L << (Wchar - 64))
                            | (1L << (While - 64))
                            | (1L << (LeftParen - 64))
                            | (1L << (LeftBracket - 64))
                            | (1L << (LeftBrace - 64))
                            | (1L << (Plus - 64))
                            | (1L << (Minus - 64))
                            | (1L << (Star - 64))
                            | (1L << (And - 64))
                            | (1L << (Or - 64))
                            | (1L << (Tilde - 64))
                            | (1L << (Not - 64))
                            | (1L << (AndAnd - 64))
                            | (1L << (PlusPlus - 64))
                            | (1L << (MinusMinus - 64))
                            | (1L << (Doublecolon - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (Semi - 128))
                            | (1L << (Ellipsis - 128))
                            | (1L << (Identifier - 128))))
                    != 0)) {
          {
            setState(868);
            statementSeq();
          }
        }

        setState(871);
        match(RightBrace);
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
        } while ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << Alignas)
                            | (1L << Alignof)
                            | (1L << Asm)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Break)
                            | (1L << Case)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Class)
                            | (1L << Const)
                            | (1L << Constexpr)
                            | (1L << Const_cast)
                            | (1L << Continue)
                            | (1L << Decltype)
                            | (1L << Default)
                            | (1L << Delete)
                            | (1L << Do)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Enum)
                            | (1L << Explicit)
                            | (1L << Extern)
                            | (1L << Float)
                            | (1L << For)
                            | (1L << Friend)
                            | (1L << Goto)
                            | (1L << If)
                            | (1L << Inline)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << Mutable)
                            | (1L << Namespace)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Operator)
                            | (1L << Register)
                            | (1L << Reinterpret_cast)
                            | (1L << Return)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)
                            | (1L << Static)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (Static_assert - 64))
                            | (1L << (Static_cast - 64))
                            | (1L << (Struct - 64))
                            | (1L << (Switch - 64))
                            | (1L << (This - 64))
                            | (1L << (Thread_local - 64))
                            | (1L << (Throw - 64))
                            | (1L << (Try - 64))
                            | (1L << (Typedef - 64))
                            | (1L << (Typeid_ - 64))
                            | (1L << (Typename_ - 64))
                            | (1L << (Union - 64))
                            | (1L << (Unsigned - 64))
                            | (1L << (Using - 64))
                            | (1L << (Virtual - 64))
                            | (1L << (Void - 64))
                            | (1L << (Volatile - 64))
                            | (1L << (Wchar - 64))
                            | (1L << (While - 64))
                            | (1L << (LeftParen - 64))
                            | (1L << (LeftBracket - 64))
                            | (1L << (LeftBrace - 64))
                            | (1L << (Plus - 64))
                            | (1L << (Minus - 64))
                            | (1L << (Star - 64))
                            | (1L << (And - 64))
                            | (1L << (Or - 64))
                            | (1L << (Tilde - 64))
                            | (1L << (Not - 64))
                            | (1L << (AndAnd - 64))
                            | (1L << (PlusPlus - 64))
                            | (1L << (MinusMinus - 64))
                            | (1L << (Doublecolon - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (Semi - 128))
                            | (1L << (Ellipsis - 128))
                            | (1L << (Identifier - 128))))
                    != 0));
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
            switch (getInterpreter().adaptivePredict(_input, 81, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ConditionContext condition() throws RecognitionException {
    ConditionContext _localctx = new ConditionContext(_ctx, getState());
    enterRule(_localctx, 106, RULE_condition);
    int _la;
    try {
      setState(906);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 85, _ctx)) {
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
            if (_la == Alignas || _la == LeftBracket) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
            switch (getInterpreter().adaptivePredict(_input, 88, _ctx)) {
              case 1:
                {
                  setState(924);
                  forInitStatement();
                  setState(926);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  if ((((_la) & ~0x3f) == 0
                          && ((1L << _la)
                                  & ((1L << IntegerLiteral)
                                      | (1L << CharacterLiteral)
                                      | (1L << FloatingLiteral)
                                      | (1L << StringLiteral)
                                      | (1L << BooleanLiteral)
                                      | (1L << PointerLiteral)
                                      | (1L << UserDefinedLiteral)
                                      | (1L << Alignas)
                                      | (1L << Alignof)
                                      | (1L << Auto)
                                      | (1L << Bool)
                                      | (1L << Char)
                                      | (1L << Char16)
                                      | (1L << Char32)
                                      | (1L << Class)
                                      | (1L << Const)
                                      | (1L << Constexpr)
                                      | (1L << Const_cast)
                                      | (1L << Decltype)
                                      | (1L << Delete)
                                      | (1L << Double)
                                      | (1L << Dynamic_cast)
                                      | (1L << Enum)
                                      | (1L << Explicit)
                                      | (1L << Extern)
                                      | (1L << Float)
                                      | (1L << Friend)
                                      | (1L << Inline)
                                      | (1L << Int)
                                      | (1L << Long)
                                      | (1L << Mutable)
                                      | (1L << New)
                                      | (1L << Noexcept)
                                      | (1L << Operator)
                                      | (1L << Register)
                                      | (1L << Reinterpret_cast)
                                      | (1L << Short)
                                      | (1L << Signed)
                                      | (1L << Sizeof)
                                      | (1L << Static)))
                              != 0)
                      || ((((_la - 65)) & ~0x3f) == 0
                          && ((1L << (_la - 65))
                                  & ((1L << (Static_cast - 65))
                                      | (1L << (Struct - 65))
                                      | (1L << (This - 65))
                                      | (1L << (Thread_local - 65))
                                      | (1L << (Throw - 65))
                                      | (1L << (Typedef - 65))
                                      | (1L << (Typeid_ - 65))
                                      | (1L << (Typename_ - 65))
                                      | (1L << (Union - 65))
                                      | (1L << (Unsigned - 65))
                                      | (1L << (Virtual - 65))
                                      | (1L << (Void - 65))
                                      | (1L << (Volatile - 65))
                                      | (1L << (Wchar - 65))
                                      | (1L << (LeftParen - 65))
                                      | (1L << (LeftBracket - 65))
                                      | (1L << (Plus - 65))
                                      | (1L << (Minus - 65))
                                      | (1L << (Star - 65))
                                      | (1L << (And - 65))
                                      | (1L << (Or - 65))
                                      | (1L << (Tilde - 65))
                                      | (1L << (Not - 65))
                                      | (1L << (PlusPlus - 65))
                                      | (1L << (MinusMinus - 65))
                                      | (1L << (Doublecolon - 65))))
                              != 0)
                      || _la == Identifier) {
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
                  if ((((_la) & ~0x3f) == 0
                          && ((1L << _la)
                                  & ((1L << IntegerLiteral)
                                      | (1L << CharacterLiteral)
                                      | (1L << FloatingLiteral)
                                      | (1L << StringLiteral)
                                      | (1L << BooleanLiteral)
                                      | (1L << PointerLiteral)
                                      | (1L << UserDefinedLiteral)
                                      | (1L << Alignof)
                                      | (1L << Auto)
                                      | (1L << Bool)
                                      | (1L << Char)
                                      | (1L << Char16)
                                      | (1L << Char32)
                                      | (1L << Const_cast)
                                      | (1L << Decltype)
                                      | (1L << Delete)
                                      | (1L << Double)
                                      | (1L << Dynamic_cast)
                                      | (1L << Float)
                                      | (1L << Int)
                                      | (1L << Long)
                                      | (1L << New)
                                      | (1L << Noexcept)
                                      | (1L << Operator)
                                      | (1L << Reinterpret_cast)
                                      | (1L << Short)
                                      | (1L << Signed)
                                      | (1L << Sizeof)))
                              != 0)
                      || ((((_la - 65)) & ~0x3f) == 0
                          && ((1L << (_la - 65))
                                  & ((1L << (Static_cast - 65))
                                      | (1L << (This - 65))
                                      | (1L << (Throw - 65))
                                      | (1L << (Typeid_ - 65))
                                      | (1L << (Typename_ - 65))
                                      | (1L << (Unsigned - 65))
                                      | (1L << (Void - 65))
                                      | (1L << (Wchar - 65))
                                      | (1L << (LeftParen - 65))
                                      | (1L << (LeftBracket - 65))
                                      | (1L << (Plus - 65))
                                      | (1L << (Minus - 65))
                                      | (1L << (Star - 65))
                                      | (1L << (And - 65))
                                      | (1L << (Or - 65))
                                      | (1L << (Tilde - 65))
                                      | (1L << (Not - 65))
                                      | (1L << (PlusPlus - 65))
                                      | (1L << (MinusMinus - 65))
                                      | (1L << (Doublecolon - 65))))
                              != 0)
                      || _la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ForInitStatementContext forInitStatement() throws RecognitionException {
    ForInitStatementContext _localctx = new ForInitStatementContext(_ctx, getState());
    enterRule(_localctx, 110, RULE_forInitStatement);
    try {
      setState(945);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 90, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Alignas || _la == LeftBracket) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        } while (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Asm - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Namespace - 10))
                            | (1L << (Operator - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Static_assert - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Template - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Using - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftParen - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Star - 74))
                            | (1L << (And - 74))
                            | (1L << (Tilde - 74))
                            | (1L << (AndAnd - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Semi - 74))
                            | (1L << (Ellipsis - 74))
                            | (1L << (Identifier - 74))))
                    != 0));
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
    enterRule(_localctx, 122, RULE_declaration);
    try {
      setState(986);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 96, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BlockDeclarationContext blockDeclaration() throws RecognitionException {
    BlockDeclarationContext _localctx = new BlockDeclarationContext(_ctx, getState());
    enterRule(_localctx, 124, RULE_blockDeclaration);
    try {
      setState(996);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 97, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Alignas || _la == LeftBracket) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
            switch (getInterpreter().adaptivePredict(_input, 99, _ctx)) {
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
            if (_la == Decltype
                || _la == Operator
                || ((((_la - 85)) & ~0x3f) == 0
                    && ((1L << (_la - 85))
                            & ((1L << (LeftParen - 85))
                                | (1L << (Star - 85))
                                | (1L << (And - 85))
                                | (1L << (Tilde - 85))
                                | (1L << (AndAnd - 85))
                                | (1L << (Doublecolon - 85))
                                | (1L << (Ellipsis - 85))
                                | (1L << (Identifier - 85))))
                        != 0)) {
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
            switch (getInterpreter().adaptivePredict(_input, 101, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final StaticAssertDeclarationContext staticAssertDeclaration()
      throws RecognitionException {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        _alt = 1 + 1;
        do {
          switch (_alt) {
            case 1 + 1:
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
          _alt = getInterpreter().adaptivePredict(_input, 104, _ctx);
        } while (_alt != 1 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
        setState(1050);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 105, _ctx)) {
          case 1:
            {
              setState(1049);
              attributeSpecifierSeq();
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

  public final StorageClassSpecifierContext storageClassSpecifier() throws RecognitionException {
    StorageClassSpecifierContext _localctx = new StorageClassSpecifierContext(_ctx, getState());
    enterRule(_localctx, 140, RULE_storageClassSpecifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1052);
        _la = _input.LA(1);
        if (!(((((_la - 36)) & ~0x3f) == 0
            && ((1L << (_la - 36))
                    & ((1L << (Extern - 36))
                        | (1L << (Mutable - 36))
                        | (1L << (Register - 36))
                        | (1L << (Static - 36))
                        | (1L << (Thread_local - 36))))
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

  public final FunctionSpecifierContext functionSpecifier() throws RecognitionException {
    FunctionSpecifierContext _localctx = new FunctionSpecifierContext(_ctx, getState());
    enterRule(_localctx, 142, RULE_functionSpecifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1054);
        _la = _input.LA(1);
        if (!(((((_la - 34)) & ~0x3f) == 0
            && ((1L << (_la - 34))
                    & ((1L << (Explicit - 34)) | (1L << (Inline - 34)) | (1L << (Virtual - 34))))
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

  public final TypedefNameContext typedefName() throws RecognitionException {
    TypedefNameContext _localctx = new TypedefNameContext(_ctx, getState());
    enterRule(_localctx, 144, RULE_typedefName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1056);
        match(Identifier);
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

  public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
    TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
    enterRule(_localctx, 146, RULE_typeSpecifier);
    try {
      setState(1061);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 106, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
          _alt = getInterpreter().adaptivePredict(_input, 108, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
        setState(1075);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 109, _ctx)) {
          case 1:
            {
              setState(1074);
              attributeSpecifierSeq();
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

  public final TrailingTypeSpecifierSeqContext trailingTypeSpecifierSeq()
      throws RecognitionException {
    TrailingTypeSpecifierSeqContext _localctx =
        new TrailingTypeSpecifierSeqContext(_ctx, getState());
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
          _alt = getInterpreter().adaptivePredict(_input, 110, _ctx);
        } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
        setState(1083);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 111, _ctx)) {
          case 1:
            {
              setState(1082);
              attributeSpecifierSeq();
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

  public final SimpleTypeLengthModifierContext simpleTypeLengthModifier()
      throws RecognitionException {
    SimpleTypeLengthModifierContext _localctx =
        new SimpleTypeLengthModifierContext(_ctx, getState());
    enterRule(_localctx, 154, RULE_simpleTypeLengthModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1085);
        _la = _input.LA(1);
        if (!(_la == Long || _la == Short)) {
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

  public final SimpleTypeSignednessModifierContext simpleTypeSignednessModifier()
      throws RecognitionException {
    SimpleTypeSignednessModifierContext _localctx =
        new SimpleTypeSignednessModifierContext(_ctx, getState());
    enterRule(_localctx, 156, RULE_simpleTypeSignednessModifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1087);
        _la = _input.LA(1);
        if (!(_la == Signed || _la == Unsigned)) {
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

  public final SimpleTypeSpecifierContext simpleTypeSpecifier() throws RecognitionException {
    SimpleTypeSpecifierContext _localctx = new SimpleTypeSpecifierContext(_ctx, getState());
    enterRule(_localctx, 158, RULE_simpleTypeSpecifier);
    int _la;
    try {
      int _alt;
      setState(1141);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 122, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(1090);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 112, _ctx)) {
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
            if (_la == Signed || _la == Unsigned) {
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
              _alt = getInterpreter().adaptivePredict(_input, 114, _ctx);
            } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
          }
          break;
        case 5:
          enterOuterAlt(_localctx, 5);
          {
            setState(1107);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Signed || _la == Unsigned) {
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
            if (_la == Signed || _la == Unsigned) {
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
            if (_la == Signed || _la == Unsigned) {
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
            if (_la == Signed || _la == Unsigned) {
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
            if (_la == Signed || _la == Unsigned) {
              {
                setState(1123);
                simpleTypeSignednessModifier();
              }
            }

            setState(1129);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la == Long || _la == Short) {
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
            if (_la == Long || _la == Short) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final TheTypeNameContext theTypeName() throws RecognitionException {
    TheTypeNameContext _localctx = new TheTypeNameContext(_ctx, getState());
    enterRule(_localctx, 160, RULE_theTypeName);
    try {
      setState(1147);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 123, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 124, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ElaboratedTypeSpecifierContext elaboratedTypeSpecifier()
      throws RecognitionException {
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
            switch (getInterpreter().adaptivePredict(_input, 128, _ctx)) {
              case 1:
                {
                  setState(1159);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  if (_la == Alignas || _la == LeftBracket) {
                    {
                      setState(1158);
                      attributeSpecifierSeq();
                    }
                  }

                  setState(1162);
                  _errHandler.sync(this);
                  switch (getInterpreter().adaptivePredict(_input, 126, _ctx)) {
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
                  if (_la == Template) {
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
            switch (getInterpreter().adaptivePredict(_input, 129, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Identifier) {
          {
            setState(1185);
            enumeratorList();
            setState(1187);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Alignas || _la == LeftBracket) {
          {
            setState(1194);
            attributeSpecifierSeq();
          }
        }

        setState(1201);
        _errHandler.sync(this);
        _la = _input.LA(1);
        if (_la == Decltype || _la == Doublecolon || _la == Identifier) {
          {
            setState(1198);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 134, _ctx)) {
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
        if (_la == Colon) {
          {
            setState(1203);
            enumbase();
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
        if (_la == Alignas || _la == LeftBracket) {
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
        if (_la == Colon) {
          {
            setState(1211);
            enumbase();
          }
        }

        setState(1214);
        match(Semi);
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
        if (_la == Class || _la == Struct) {
          {
            setState(1217);
            _la = _input.LA(1);
            if (!(_la == Class || _la == Struct)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) matchedEOF = true;
              _errHandler.reportMatch(this);
              consume();
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        _alt = getInterpreter().adaptivePredict(_input, 140, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
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
          _alt = getInterpreter().adaptivePredict(_input, 140, _ctx);
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
        if (_la == Assign) {
          {
            setState(1232);
            match(Assign);
            setState(1233);
            constantExpression();
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

  public final EnumeratorContext enumerator() throws RecognitionException {
    EnumeratorContext _localctx = new EnumeratorContext(_ctx, getState());
    enterRule(_localctx, 182, RULE_enumerator);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1236);
        match(Identifier);
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

  public final NamespaceNameContext namespaceName() throws RecognitionException {
    NamespaceNameContext _localctx = new NamespaceNameContext(_ctx, getState());
    enterRule(_localctx, 184, RULE_namespaceName);
    try {
      setState(1240);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 142, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Inline) {
          {
            setState(1244);
            match(Inline);
          }
        }

        setState(1247);
        match(Namespace);
        setState(1250);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 144, _ctx)) {
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
        if (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Asm - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Namespace - 10))
                            | (1L << (Operator - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Static_assert - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Template - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Using - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftParen - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Star - 74))
                            | (1L << (And - 74))
                            | (1L << (Tilde - 74))
                            | (1L << (AndAnd - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Semi - 74))
                            | (1L << (Ellipsis - 74))
                            | (1L << (Identifier - 74))))
                    != 0)) {
          {
            setState(1253);
            ((NamespaceDefinitionContext) _localctx).namespaceBody = declarationseq();
          }
        }

        setState(1256);
        match(RightBrace);
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

  public final NamespaceAliasContext namespaceAlias() throws RecognitionException {
    NamespaceAliasContext _localctx = new NamespaceAliasContext(_ctx, getState());
    enterRule(_localctx, 190, RULE_namespaceAlias);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1258);
        match(Identifier);
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

  public final NamespaceAliasDefinitionContext namespaceAliasDefinition()
      throws RecognitionException {
    NamespaceAliasDefinitionContext _localctx =
        new NamespaceAliasDefinitionContext(_ctx, getState());
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final QualifiednamespacespecifierContext qualifiednamespacespecifier()
      throws RecognitionException {
    QualifiednamespacespecifierContext _localctx =
        new QualifiednamespacespecifierContext(_ctx, getState());
    enterRule(_localctx, 194, RULE_qualifiednamespacespecifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1267);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 146, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 148, _ctx)) {
          case 1:
            {
              {
                setState(1273);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Typename_) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Alignas || _la == LeftBracket) {
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
        switch (getInterpreter().adaptivePredict(_input, 150, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
              if (((((_la - 10)) & ~0x3f) == 0
                      && ((1L << (_la - 10))
                              & ((1L << (Alignas - 10))
                                  | (1L << (Asm - 10))
                                  | (1L << (Auto - 10))
                                  | (1L << (Bool - 10))
                                  | (1L << (Char - 10))
                                  | (1L << (Char16 - 10))
                                  | (1L << (Char32 - 10))
                                  | (1L << (Class - 10))
                                  | (1L << (Const - 10))
                                  | (1L << (Constexpr - 10))
                                  | (1L << (Decltype - 10))
                                  | (1L << (Double - 10))
                                  | (1L << (Enum - 10))
                                  | (1L << (Explicit - 10))
                                  | (1L << (Extern - 10))
                                  | (1L << (Float - 10))
                                  | (1L << (Friend - 10))
                                  | (1L << (Inline - 10))
                                  | (1L << (Int - 10))
                                  | (1L << (Long - 10))
                                  | (1L << (Mutable - 10))
                                  | (1L << (Namespace - 10))
                                  | (1L << (Operator - 10))
                                  | (1L << (Register - 10))
                                  | (1L << (Short - 10))
                                  | (1L << (Signed - 10))
                                  | (1L << (Static - 10))
                                  | (1L << (Static_assert - 10))
                                  | (1L << (Struct - 10))
                                  | (1L << (Template - 10))
                                  | (1L << (Thread_local - 10))))
                          != 0)
                  || ((((_la - 74)) & ~0x3f) == 0
                      && ((1L << (_la - 74))
                              & ((1L << (Typedef - 74))
                                  | (1L << (Typename_ - 74))
                                  | (1L << (Union - 74))
                                  | (1L << (Unsigned - 74))
                                  | (1L << (Using - 74))
                                  | (1L << (Virtual - 74))
                                  | (1L << (Void - 74))
                                  | (1L << (Volatile - 74))
                                  | (1L << (Wchar - 74))
                                  | (1L << (LeftParen - 74))
                                  | (1L << (LeftBracket - 74))
                                  | (1L << (Star - 74))
                                  | (1L << (And - 74))
                                  | (1L << (Tilde - 74))
                                  | (1L << (AndAnd - 74))
                                  | (1L << (Doublecolon - 74))
                                  | (1L << (Semi - 74))
                                  | (1L << (Ellipsis - 74))
                                  | (1L << (Identifier - 74))))
                          != 0)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
          _alt = getInterpreter().adaptivePredict(_input, 153, _ctx);
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
            if (_la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 156, _ctx)) {
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
        if (_la == Ellipsis) {
          {
            setState(1330);
            match(Ellipsis);
          }
        }

        setState(1333);
        match(RightParen);
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
        while (_la == Comma) {
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
        if (_la == Ellipsis) {
          {
            setState(1343);
            match(Ellipsis);
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

  public final AttributeContext attribute() throws RecognitionException {
    AttributeContext _localctx = new AttributeContext(_ctx, getState());
    enterRule(_localctx, 212, RULE_attribute);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1349);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 160, _ctx)) {
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
        if (_la == LeftParen) {
          {
            setState(1352);
            attributeArgumentClause();
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

  public final AttributeNamespaceContext attributeNamespace() throws RecognitionException {
    AttributeNamespaceContext _localctx = new AttributeNamespaceContext(_ctx, getState());
    enterRule(_localctx, 214, RULE_attributeNamespace);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1355);
        match(Identifier);
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

  public final AttributeArgumentClauseContext attributeArgumentClause()
      throws RecognitionException {
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
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << MultiLineMacro)
                            | (1L << Directive)
                            | (1L << Alignas)
                            | (1L << Alignof)
                            | (1L << Asm)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Break)
                            | (1L << Case)
                            | (1L << Catch)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Class)
                            | (1L << Const)
                            | (1L << Constexpr)
                            | (1L << Const_cast)
                            | (1L << Continue)
                            | (1L << Decltype)
                            | (1L << Default)
                            | (1L << Delete)
                            | (1L << Do)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Else)
                            | (1L << Enum)
                            | (1L << Explicit)
                            | (1L << Export)
                            | (1L << Extern)
                            | (1L << False_)
                            | (1L << Final)
                            | (1L << Float)
                            | (1L << For)
                            | (1L << Friend)
                            | (1L << Goto)
                            | (1L << If)
                            | (1L << Inline)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << Mutable)
                            | (1L << Namespace)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Nullptr)
                            | (1L << Operator)
                            | (1L << Override)
                            | (1L << Private)
                            | (1L << Protected)
                            | (1L << Public)
                            | (1L << Register)
                            | (1L << Reinterpret_cast)
                            | (1L << Return)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)
                            | (1L << Static)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (Static_assert - 64))
                            | (1L << (Static_cast - 64))
                            | (1L << (Struct - 64))
                            | (1L << (Switch - 64))
                            | (1L << (Template - 64))
                            | (1L << (This - 64))
                            | (1L << (Thread_local - 64))
                            | (1L << (Throw - 64))
                            | (1L << (True_ - 64))
                            | (1L << (Try - 64))
                            | (1L << (Typedef - 64))
                            | (1L << (Typeid_ - 64))
                            | (1L << (Typename_ - 64))
                            | (1L << (Union - 64))
                            | (1L << (Unsigned - 64))
                            | (1L << (Using - 64))
                            | (1L << (Virtual - 64))
                            | (1L << (Void - 64))
                            | (1L << (Volatile - 64))
                            | (1L << (Wchar - 64))
                            | (1L << (While - 64))
                            | (1L << (LeftParen - 64))
                            | (1L << (LeftBracket - 64))
                            | (1L << (LeftBrace - 64))
                            | (1L << (Plus - 64))
                            | (1L << (Minus - 64))
                            | (1L << (Star - 64))
                            | (1L << (Div - 64))
                            | (1L << (Mod - 64))
                            | (1L << (Caret - 64))
                            | (1L << (And - 64))
                            | (1L << (Or - 64))
                            | (1L << (Tilde - 64))
                            | (1L << (Not - 64))
                            | (1L << (Assign - 64))
                            | (1L << (Less - 64))
                            | (1L << (Greater - 64))
                            | (1L << (PlusAssign - 64))
                            | (1L << (MinusAssign - 64))
                            | (1L << (StarAssign - 64))
                            | (1L << (DivAssign - 64))
                            | (1L << (ModAssign - 64))
                            | (1L << (XorAssign - 64))
                            | (1L << (AndAssign - 64))
                            | (1L << (OrAssign - 64))
                            | (1L << (LeftShiftAssign - 64))
                            | (1L << (RightShiftAssign - 64))
                            | (1L << (Equal - 64))
                            | (1L << (NotEqual - 64))
                            | (1L << (LessEqual - 64))
                            | (1L << (GreaterEqual - 64))
                            | (1L << (AndAnd - 64))
                            | (1L << (OrOr - 64))
                            | (1L << (PlusPlus - 64))
                            | (1L << (MinusMinus - 64))
                            | (1L << (Comma - 64))
                            | (1L << (ArrowStar - 64))
                            | (1L << (Arrow - 64))
                            | (1L << (Question - 64))
                            | (1L << (Colon - 64))
                            | (1L << (Doublecolon - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (Semi - 128))
                            | (1L << (Dot - 128))
                            | (1L << (DotStar - 128))
                            | (1L << (Ellipsis - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (DecimalLiteral - 128))
                            | (1L << (OctalLiteral - 128))
                            | (1L << (HexadecimalLiteral - 128))
                            | (1L << (BinaryLiteral - 128))
                            | (1L << (Integersuffix - 128))
                            | (1L << (UserDefinedIntegerLiteral - 128))
                            | (1L << (UserDefinedFloatingLiteral - 128))
                            | (1L << (UserDefinedStringLiteral - 128))
                            | (1L << (UserDefinedCharacterLiteral - 128))
                            | (1L << (Whitespace - 128))
                            | (1L << (Newline - 128))
                            | (1L << (BlockComment - 128))
                            | (1L << (LineComment - 128))))
                    != 0)) {
          {
            setState(1358);
            balancedTokenSeq();
          }
        }

        setState(1361);
        match(RightParen);
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
        } while ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << MultiLineMacro)
                            | (1L << Directive)
                            | (1L << Alignas)
                            | (1L << Alignof)
                            | (1L << Asm)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Break)
                            | (1L << Case)
                            | (1L << Catch)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Class)
                            | (1L << Const)
                            | (1L << Constexpr)
                            | (1L << Const_cast)
                            | (1L << Continue)
                            | (1L << Decltype)
                            | (1L << Default)
                            | (1L << Delete)
                            | (1L << Do)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Else)
                            | (1L << Enum)
                            | (1L << Explicit)
                            | (1L << Export)
                            | (1L << Extern)
                            | (1L << False_)
                            | (1L << Final)
                            | (1L << Float)
                            | (1L << For)
                            | (1L << Friend)
                            | (1L << Goto)
                            | (1L << If)
                            | (1L << Inline)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << Mutable)
                            | (1L << Namespace)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Nullptr)
                            | (1L << Operator)
                            | (1L << Override)
                            | (1L << Private)
                            | (1L << Protected)
                            | (1L << Public)
                            | (1L << Register)
                            | (1L << Reinterpret_cast)
                            | (1L << Return)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)
                            | (1L << Static)))
                    != 0)
            || ((((_la - 64)) & ~0x3f) == 0
                && ((1L << (_la - 64))
                        & ((1L << (Static_assert - 64))
                            | (1L << (Static_cast - 64))
                            | (1L << (Struct - 64))
                            | (1L << (Switch - 64))
                            | (1L << (Template - 64))
                            | (1L << (This - 64))
                            | (1L << (Thread_local - 64))
                            | (1L << (Throw - 64))
                            | (1L << (True_ - 64))
                            | (1L << (Try - 64))
                            | (1L << (Typedef - 64))
                            | (1L << (Typeid_ - 64))
                            | (1L << (Typename_ - 64))
                            | (1L << (Union - 64))
                            | (1L << (Unsigned - 64))
                            | (1L << (Using - 64))
                            | (1L << (Virtual - 64))
                            | (1L << (Void - 64))
                            | (1L << (Volatile - 64))
                            | (1L << (Wchar - 64))
                            | (1L << (While - 64))
                            | (1L << (LeftParen - 64))
                            | (1L << (LeftBracket - 64))
                            | (1L << (LeftBrace - 64))
                            | (1L << (Plus - 64))
                            | (1L << (Minus - 64))
                            | (1L << (Star - 64))
                            | (1L << (Div - 64))
                            | (1L << (Mod - 64))
                            | (1L << (Caret - 64))
                            | (1L << (And - 64))
                            | (1L << (Or - 64))
                            | (1L << (Tilde - 64))
                            | (1L << (Not - 64))
                            | (1L << (Assign - 64))
                            | (1L << (Less - 64))
                            | (1L << (Greater - 64))
                            | (1L << (PlusAssign - 64))
                            | (1L << (MinusAssign - 64))
                            | (1L << (StarAssign - 64))
                            | (1L << (DivAssign - 64))
                            | (1L << (ModAssign - 64))
                            | (1L << (XorAssign - 64))
                            | (1L << (AndAssign - 64))
                            | (1L << (OrAssign - 64))
                            | (1L << (LeftShiftAssign - 64))
                            | (1L << (RightShiftAssign - 64))
                            | (1L << (Equal - 64))
                            | (1L << (NotEqual - 64))
                            | (1L << (LessEqual - 64))
                            | (1L << (GreaterEqual - 64))
                            | (1L << (AndAnd - 64))
                            | (1L << (OrOr - 64))
                            | (1L << (PlusPlus - 64))
                            | (1L << (MinusMinus - 64))
                            | (1L << (Comma - 64))
                            | (1L << (ArrowStar - 64))
                            | (1L << (Arrow - 64))
                            | (1L << (Question - 64))
                            | (1L << (Colon - 64))
                            | (1L << (Doublecolon - 64))))
                    != 0)
            || ((((_la - 128)) & ~0x3f) == 0
                && ((1L << (_la - 128))
                        & ((1L << (Semi - 128))
                            | (1L << (Dot - 128))
                            | (1L << (DotStar - 128))
                            | (1L << (Ellipsis - 128))
                            | (1L << (Identifier - 128))
                            | (1L << (DecimalLiteral - 128))
                            | (1L << (OctalLiteral - 128))
                            | (1L << (HexadecimalLiteral - 128))
                            | (1L << (BinaryLiteral - 128))
                            | (1L << (Integersuffix - 128))
                            | (1L << (UserDefinedIntegerLiteral - 128))
                            | (1L << (UserDefinedFloatingLiteral - 128))
                            | (1L << (UserDefinedStringLiteral - 128))
                            | (1L << (UserDefinedCharacterLiteral - 128))
                            | (1L << (Whitespace - 128))
                            | (1L << (Newline - 128))
                            | (1L << (BlockComment - 128))
                            | (1L << (LineComment - 128))))
                    != 0));
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
                      if (_la <= 0
                          || (((((_la - 85)) & ~0x3f) == 0
                              && ((1L << (_la - 85))
                                      & ((1L << (LeftParen - 85))
                                          | (1L << (RightParen - 85))
                                          | (1L << (LeftBracket - 85))
                                          | (1L << (RightBracket - 85))
                                          | (1L << (LeftBrace - 85))
                                          | (1L << (RightBrace - 85))))
                                  != 0))) {
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
              setState(1383);
              _errHandler.sync(this);
              _alt = getInterpreter().adaptivePredict(_input, 164, _ctx);
            } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
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
        while (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (((((_la - 85)) & ~0x3f) == 0
            && ((1L << (_la - 85))
                    & ((1L << (LeftParen - 85)) | (1L << (LeftBrace - 85)) | (1L << (Assign - 85))))
                != 0)) {
          {
            setState(1396);
            initializer();
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

  public final DeclaratorContext declarator() throws RecognitionException {
    DeclaratorContext _localctx = new DeclaratorContext(_ctx, getState());
    enterRule(_localctx, 226, RULE_declarator);
    try {
      setState(1404);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 168, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        _alt = getInterpreter().adaptivePredict(_input, 170, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1406);
                pointerOperator();
                setState(1408);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Const) {
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
          _alt = getInterpreter().adaptivePredict(_input, 170, _ctx);
        }
        setState(1415);
        noPointerDeclarator(0);
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

  public final NoPointerDeclaratorContext noPointerDeclarator() throws RecognitionException {
    return noPointerDeclarator(0);
  }

  public final ParametersAndQualifiersContext parametersAndQualifiers()
      throws RecognitionException {
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
        if (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Identifier - 74))))
                    != 0)) {
          {
            setState(1446);
            parameterDeclarationClause();
          }
        }

        setState(1449);
        match(RightParen);
        setState(1451);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 178, _ctx)) {
          case 1:
            {
              setState(1450);
              cvqualifierseq();
            }
            break;
        }
        setState(1454);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 179, _ctx)) {
          case 1:
            {
              setState(1453);
              refqualifier();
            }
            break;
        }
        setState(1457);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 180, _ctx)) {
          case 1:
            {
              setState(1456);
              exceptionSpecification();
            }
            break;
        }
        setState(1460);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 181, _ctx)) {
          case 1:
            {
              setState(1459);
              attributeSpecifierSeq();
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
        switch (getInterpreter().adaptivePredict(_input, 182, _ctx)) {
          case 1:
            {
              setState(1464);
              abstractDeclarator();
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
            if (!(_la == And || _la == AndAnd)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) matchedEOF = true;
              _errHandler.reportMatch(this);
              consume();
            }
            setState(1469);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 183, _ctx)) {
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
            if (_la == Decltype || _la == Doublecolon || _la == Identifier) {
              {
                setState(1471);
                nestedNameSpecifier(0);
              }
            }

            setState(1474);
            match(Star);
            setState(1476);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 185, _ctx)) {
              case 1:
                {
                  setState(1475);
                  attributeSpecifierSeq();
                }
                break;
            }
            setState(1479);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 186, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
          _alt = getInterpreter().adaptivePredict(_input, 188, _ctx);
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

  public final CvQualifierContext cvQualifier() throws RecognitionException {
    CvQualifierContext _localctx = new CvQualifierContext(_ctx, getState());
    enterRule(_localctx, 240, RULE_cvQualifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1488);
        _la = _input.LA(1);
        if (!(_la == Const || _la == Volatile)) {
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

  public final RefqualifierContext refqualifier() throws RecognitionException {
    RefqualifierContext _localctx = new RefqualifierContext(_ctx, getState());
    enterRule(_localctx, 242, RULE_refqualifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1490);
        _la = _input.LA(1);
        if (!(_la == And || _la == AndAnd)) {
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
        if (_la == Ellipsis) {
          {
            setState(1492);
            match(Ellipsis);
          }
        }

        setState(1495);
        idExpression();
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
        switch (getInterpreter().adaptivePredict(_input, 190, _ctx)) {
          case 1:
            {
              setState(1498);
              abstractDeclarator();
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

  public final AbstractDeclaratorContext abstractDeclarator() throws RecognitionException {
    AbstractDeclaratorContext _localctx = new AbstractDeclaratorContext(_ctx, getState());
    enterRule(_localctx, 248, RULE_abstractDeclarator);
    try {
      setState(1509);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 192, _ctx)) {
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
            switch (getInterpreter().adaptivePredict(_input, 191, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final PointerAbstractDeclaratorContext pointerAbstractDeclarator()
      throws RecognitionException {
    PointerAbstractDeclaratorContext _localctx =
        new PointerAbstractDeclaratorContext(_ctx, getState());
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
            } while (_la == Decltype
                || ((((_la - 93)) & ~0x3f) == 0
                    && ((1L << (_la - 93))
                            & ((1L << (Star - 93))
                                | (1L << (And - 93))
                                | (1L << (AndAnd - 93))
                                | (1L << (Doublecolon - 93))
                                | (1L << (Identifier - 93))))
                        != 0));
            setState(1518);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 194, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator()
      throws RecognitionException {
    return noPointerAbstractDeclarator(0);
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
        while (_la == Decltype
            || ((((_la - 93)) & ~0x3f) == 0
                && ((1L << (_la - 93))
                        & ((1L << (Star - 93))
                            | (1L << (And - 93))
                            | (1L << (AndAnd - 93))
                            | (1L << (Doublecolon - 93))
                            | (1L << (Identifier - 93))))
                    != 0)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator()
      throws RecognitionException {
    return noPointerAbstractPackDeclarator(0);
  }

  public final ParameterDeclarationClauseContext parameterDeclarationClause()
      throws RecognitionException {
    ParameterDeclarationClauseContext _localctx =
        new ParameterDeclarationClauseContext(_ctx, getState());
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
        if (_la == Comma || _la == Ellipsis) {
          {
            setState(1586);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ParameterDeclarationListContext parameterDeclarationList()
      throws RecognitionException {
    ParameterDeclarationListContext _localctx =
        new ParameterDeclarationListContext(_ctx, getState());
    enterRule(_localctx, 260, RULE_parameterDeclarationList);
    try {
      int _alt;
      enterOuterAlt(_localctx, 1);
      {
        setState(1591);
        parameterDeclaration();
        setState(1596);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 210, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
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
          _alt = getInterpreter().adaptivePredict(_input, 210, _ctx);
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
        if (_la == Alignas || _la == LeftBracket) {
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
          switch (getInterpreter().adaptivePredict(_input, 213, _ctx)) {
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
                switch (getInterpreter().adaptivePredict(_input, 212, _ctx)) {
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
          if (_la == Assign) {
            {
              setState(1609);
              match(Assign);
              setState(1610);
              initializerClause();
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
        if (_la == Alignas || _la == LeftBracket) {
          {
            setState(1613);
            attributeSpecifierSeq();
          }
        }

        setState(1617);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 216, _ctx)) {
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
        if (_la == Final || _la == Override) {
          {
            setState(1620);
            virtualSpecifierSeq();
          }
        }

        setState(1623);
        functionBody();
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
            if (_la == Colon) {
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
            if (!(_la == Default || _la == Delete)) {
              _errHandler.recoverInline(this);
            } else {
              if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final BraceOrEqualInitializerContext braceOrEqualInitializer()
      throws RecognitionException {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Ellipsis) {
          {
            setState(1652);
            match(Ellipsis);
          }
        }

        setState(1662);
        _errHandler.sync(this);
        _alt = getInterpreter().adaptivePredict(_input, 225, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            {
              {
                setState(1655);
                match(Comma);
                setState(1656);
                initializerClause();
                setState(1658);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Ellipsis) {
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
          _alt = getInterpreter().adaptivePredict(_input, 225, _ctx);
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
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << Alignof)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Const_cast)
                            | (1L << Decltype)
                            | (1L << Delete)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Float)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Operator)
                            | (1L << Reinterpret_cast)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)))
                    != 0)
            || ((((_la - 65)) & ~0x3f) == 0
                && ((1L << (_la - 65))
                        & ((1L << (Static_cast - 65))
                            | (1L << (This - 65))
                            | (1L << (Throw - 65))
                            | (1L << (Typeid_ - 65))
                            | (1L << (Typename_ - 65))
                            | (1L << (Unsigned - 65))
                            | (1L << (Void - 65))
                            | (1L << (Wchar - 65))
                            | (1L << (LeftParen - 65))
                            | (1L << (LeftBracket - 65))
                            | (1L << (LeftBrace - 65))
                            | (1L << (Plus - 65))
                            | (1L << (Minus - 65))
                            | (1L << (Star - 65))
                            | (1L << (And - 65))
                            | (1L << (Or - 65))
                            | (1L << (Tilde - 65))
                            | (1L << (Not - 65))
                            | (1L << (PlusPlus - 65))
                            | (1L << (MinusMinus - 65))
                            | (1L << (Doublecolon - 65))))
                    != 0)
            || _la == Identifier) {
          {
            setState(1666);
            initializerList();
            setState(1668);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ClassNameContext className() throws RecognitionException {
    ClassNameContext _localctx = new ClassNameContext(_ctx, getState());
    enterRule(_localctx, 278, RULE_className);
    try {
      setState(1676);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 228, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Operator - 10))
                            | (1L << (Private - 10))
                            | (1L << (Protected - 10))
                            | (1L << (Public - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Static_assert - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Template - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Using - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftParen - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Star - 74))
                            | (1L << (And - 74))
                            | (1L << (Tilde - 74))
                            | (1L << (AndAnd - 74))
                            | (1L << (Colon - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Semi - 74))
                            | (1L << (Ellipsis - 74))
                            | (1L << (Identifier - 74))))
                    != 0)) {
          {
            setState(1680);
            memberSpecification();
          }
        }

        setState(1683);
        match(RightBrace);
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
            if (_la == Alignas || _la == LeftBracket) {
              {
                setState(1686);
                attributeSpecifierSeq();
              }
            }

            setState(1693);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Decltype || _la == Doublecolon || _la == Identifier) {
              {
                setState(1689);
                classHeadName();
                setState(1691);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Final) {
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
            if (_la == Colon) {
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
            if (_la == Alignas || _la == LeftBracket) {
              {
                setState(1699);
                attributeSpecifierSeq();
              }
            }

            setState(1706);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Decltype || _la == Doublecolon || _la == Identifier) {
              {
                setState(1702);
                classHeadName();
                setState(1704);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Final) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ClassHeadNameContext classHeadName() throws RecognitionException {
    ClassHeadNameContext _localctx = new ClassHeadNameContext(_ctx, getState());
    enterRule(_localctx, 284, RULE_classHeadName);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1711);
        _errHandler.sync(this);
        switch (getInterpreter().adaptivePredict(_input, 238, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!(_la == Class || _la == Struct)) {
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
        } while (((((_la - 10)) & ~0x3f) == 0
                && ((1L << (_la - 10))
                        & ((1L << (Alignas - 10))
                            | (1L << (Auto - 10))
                            | (1L << (Bool - 10))
                            | (1L << (Char - 10))
                            | (1L << (Char16 - 10))
                            | (1L << (Char32 - 10))
                            | (1L << (Class - 10))
                            | (1L << (Const - 10))
                            | (1L << (Constexpr - 10))
                            | (1L << (Decltype - 10))
                            | (1L << (Double - 10))
                            | (1L << (Enum - 10))
                            | (1L << (Explicit - 10))
                            | (1L << (Extern - 10))
                            | (1L << (Float - 10))
                            | (1L << (Friend - 10))
                            | (1L << (Inline - 10))
                            | (1L << (Int - 10))
                            | (1L << (Long - 10))
                            | (1L << (Mutable - 10))
                            | (1L << (Operator - 10))
                            | (1L << (Private - 10))
                            | (1L << (Protected - 10))
                            | (1L << (Public - 10))
                            | (1L << (Register - 10))
                            | (1L << (Short - 10))
                            | (1L << (Signed - 10))
                            | (1L << (Static - 10))
                            | (1L << (Static_assert - 10))
                            | (1L << (Struct - 10))
                            | (1L << (Template - 10))
                            | (1L << (Thread_local - 10))))
                    != 0)
            || ((((_la - 74)) & ~0x3f) == 0
                && ((1L << (_la - 74))
                        & ((1L << (Typedef - 74))
                            | (1L << (Typename_ - 74))
                            | (1L << (Union - 74))
                            | (1L << (Unsigned - 74))
                            | (1L << (Using - 74))
                            | (1L << (Virtual - 74))
                            | (1L << (Void - 74))
                            | (1L << (Volatile - 74))
                            | (1L << (Wchar - 74))
                            | (1L << (LeftParen - 74))
                            | (1L << (LeftBracket - 74))
                            | (1L << (Star - 74))
                            | (1L << (And - 74))
                            | (1L << (Tilde - 74))
                            | (1L << (AndAnd - 74))
                            | (1L << (Colon - 74))
                            | (1L << (Doublecolon - 74))
                            | (1L << (Semi - 74))
                            | (1L << (Ellipsis - 74))
                            | (1L << (Identifier - 74))))
                    != 0));
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

  public final MemberdeclarationContext memberdeclaration() throws RecognitionException {
    MemberdeclarationContext _localctx = new MemberdeclarationContext(_ctx, getState());
    enterRule(_localctx, 292, RULE_memberdeclaration);
    int _la;
    try {
      setState(1743);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 244, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(1728);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 241, _ctx)) {
              case 1:
                {
                  setState(1727);
                  attributeSpecifierSeq();
                }
                break;
            }
            setState(1731);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 242, _ctx)) {
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
            if ((((_la) & ~0x3f) == 0
                    && ((1L << _la) & ((1L << Alignas) | (1L << Decltype) | (1L << Operator))) != 0)
                || ((((_la - 85)) & ~0x3f) == 0
                    && ((1L << (_la - 85))
                            & ((1L << (LeftParen - 85))
                                | (1L << (LeftBracket - 85))
                                | (1L << (Star - 85))
                                | (1L << (And - 85))
                                | (1L << (Tilde - 85))
                                | (1L << (AndAnd - 85))
                                | (1L << (Colon - 85))
                                | (1L << (Doublecolon - 85))
                                | (1L << (Ellipsis - 85))
                                | (1L << (Identifier - 85))))
                        != 0)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final MemberDeclaratorContext memberDeclarator() throws RecognitionException {
    MemberDeclaratorContext _localctx = new MemberDeclaratorContext(_ctx, getState());
    enterRule(_localctx, 296, RULE_memberDeclarator);
    int _la;
    try {
      setState(1773);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 252, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(1753);
            declarator();
            setState(1763);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 249, _ctx)) {
              case 1:
                {
                  setState(1755);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  if (_la == Final || _la == Override) {
                    {
                      setState(1754);
                      virtualSpecifierSeq();
                    }
                  }

                  setState(1758);
                  _errHandler.sync(this);
                  _la = _input.LA(1);
                  if (_la == Assign) {
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
                  if (_la == LeftBrace || _la == Assign) {
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
            if (_la == Identifier) {
              {
                setState(1765);
                match(Identifier);
              }
            }

            setState(1769);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if (_la == Alignas || _la == LeftBracket) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        } while (_la == Final || _la == Override);
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

  public final VirtualSpecifierContext virtualSpecifier() throws RecognitionException {
    VirtualSpecifierContext _localctx = new VirtualSpecifierContext(_ctx, getState());
    enterRule(_localctx, 300, RULE_virtualSpecifier);
    int _la;
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1780);
        _la = _input.LA(1);
        if (!(_la == Final || _la == Override)) {
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

  public final PureSpecifierContext pureSpecifier() throws RecognitionException {
    PureSpecifierContext _localctx = new PureSpecifierContext(_ctx, getState());
    enterRule(_localctx, 302, RULE_pureSpecifier);
    try {
      enterOuterAlt(_localctx, 1);
      {
        setState(1782);
        match(Assign);
        setState(1783);
        ((PureSpecifierContext) _localctx).val = match(OctalLiteral);
        if ((((PureSpecifierContext) _localctx).val != null
                    ? ((PureSpecifierContext) _localctx).val.getText()
                    : null)
                .compareTo("0")
            != 0) throw new InputMismatchException(this);
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Ellipsis) {
          {
            setState(1790);
            match(Ellipsis);
          }
        }

        setState(1800);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == Comma) {
          {
            {
              setState(1793);
              match(Comma);
              setState(1794);
              baseSpecifier();
              setState(1796);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == Ellipsis) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Alignas || _la == LeftBracket) {
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
              if ((((_la) & ~0x3f) == 0
                  && ((1L << _la) & ((1L << Private) | (1L << Protected) | (1L << Public))) != 0)) {
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
              if (_la == Virtual) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final ClassOrDeclTypeContext classOrDeclType() throws RecognitionException {
    ClassOrDeclTypeContext _localctx = new ClassOrDeclTypeContext(_ctx, getState());
    enterRule(_localctx, 310, RULE_classOrDeclType);
    try {
      setState(1825);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 262, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(1821);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 261, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la) & ((1L << Private) | (1L << Protected) | (1L << Public))) != 0))) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 263, _ctx)) {
          case 1:
            {
              setState(1835);
              conversionDeclarator();
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
        switch (getInterpreter().adaptivePredict(_input, 264, _ctx)) {
          case 1:
            {
              setState(1839);
              conversionDeclarator();
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Ellipsis) {
          {
            setState(1846);
            match(Ellipsis);
          }
        }

        setState(1856);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == Comma) {
          {
            {
              setState(1849);
              match(Comma);
              setState(1850);
              memInitializer();
              setState(1852);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == Ellipsis) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
              if ((((_la) & ~0x3f) == 0
                      && ((1L << _la)
                              & ((1L << IntegerLiteral)
                                  | (1L << CharacterLiteral)
                                  | (1L << FloatingLiteral)
                                  | (1L << StringLiteral)
                                  | (1L << BooleanLiteral)
                                  | (1L << PointerLiteral)
                                  | (1L << UserDefinedLiteral)
                                  | (1L << Alignof)
                                  | (1L << Auto)
                                  | (1L << Bool)
                                  | (1L << Char)
                                  | (1L << Char16)
                                  | (1L << Char32)
                                  | (1L << Const_cast)
                                  | (1L << Decltype)
                                  | (1L << Delete)
                                  | (1L << Double)
                                  | (1L << Dynamic_cast)
                                  | (1L << Float)
                                  | (1L << Int)
                                  | (1L << Long)
                                  | (1L << New)
                                  | (1L << Noexcept)
                                  | (1L << Operator)
                                  | (1L << Reinterpret_cast)
                                  | (1L << Short)
                                  | (1L << Signed)
                                  | (1L << Sizeof)))
                          != 0)
                  || ((((_la - 65)) & ~0x3f) == 0
                      && ((1L << (_la - 65))
                              & ((1L << (Static_cast - 65))
                                  | (1L << (This - 65))
                                  | (1L << (Throw - 65))
                                  | (1L << (Typeid_ - 65))
                                  | (1L << (Typename_ - 65))
                                  | (1L << (Unsigned - 65))
                                  | (1L << (Void - 65))
                                  | (1L << (Wchar - 65))
                                  | (1L << (LeftParen - 65))
                                  | (1L << (LeftBracket - 65))
                                  | (1L << (LeftBrace - 65))
                                  | (1L << (Plus - 65))
                                  | (1L << (Minus - 65))
                                  | (1L << (Star - 65))
                                  | (1L << (And - 65))
                                  | (1L << (Or - 65))
                                  | (1L << (Tilde - 65))
                                  | (1L << (Not - 65))
                                  | (1L << (PlusPlus - 65))
                                  | (1L << (MinusMinus - 65))
                                  | (1L << (Doublecolon - 65))))
                          != 0)
                  || _la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final MeminitializeridContext meminitializerid() throws RecognitionException {
    MeminitializeridContext _localctx = new MeminitializeridContext(_ctx, getState());
    enterRule(_localctx, 328, RULE_meminitializerid);
    try {
      setState(1870);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 270, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        while (_la == Comma) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final TemplateParameterContext templateParameter() throws RecognitionException {
    TemplateParameterContext _localctx = new TemplateParameterContext(_ctx, getState());
    enterRule(_localctx, 338, RULE_templateParameter);
    try {
      setState(1897);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 273, _ctx)) {
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
              if (_la == Template) {
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
        switch (getInterpreter().adaptivePredict(_input, 279, _ctx)) {
          case 1:
            {
              {
                setState(1911);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Ellipsis) {
                  {
                    setState(1910);
                    match(Ellipsis);
                  }
                }

                setState(1914);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == Identifier) {
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
                if (_la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << Alignof)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Class)
                            | (1L << Const)
                            | (1L << Const_cast)
                            | (1L << Decltype)
                            | (1L << Delete)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Enum)
                            | (1L << Float)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Operator)
                            | (1L << Reinterpret_cast)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)))
                    != 0)
            || ((((_la - 65)) & ~0x3f) == 0
                && ((1L << (_la - 65))
                        & ((1L << (Static_cast - 65))
                            | (1L << (Struct - 65))
                            | (1L << (This - 65))
                            | (1L << (Typeid_ - 65))
                            | (1L << (Typename_ - 65))
                            | (1L << (Union - 65))
                            | (1L << (Unsigned - 65))
                            | (1L << (Void - 65))
                            | (1L << (Volatile - 65))
                            | (1L << (Wchar - 65))
                            | (1L << (LeftParen - 65))
                            | (1L << (LeftBracket - 65))
                            | (1L << (Plus - 65))
                            | (1L << (Minus - 65))
                            | (1L << (Star - 65))
                            | (1L << (And - 65))
                            | (1L << (Or - 65))
                            | (1L << (Tilde - 65))
                            | (1L << (Not - 65))
                            | (1L << (PlusPlus - 65))
                            | (1L << (MinusMinus - 65))
                            | (1L << (Doublecolon - 65))))
                    != 0)
            || _la == Identifier) {
          {
            setState(1925);
            templateArgumentList();
          }
        }

        setState(1928);
        match(Greater);
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
            switch (getInterpreter().adaptivePredict(_input, 281, _ctx)) {
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
            if ((((_la) & ~0x3f) == 0
                    && ((1L << _la)
                            & ((1L << IntegerLiteral)
                                | (1L << CharacterLiteral)
                                | (1L << FloatingLiteral)
                                | (1L << StringLiteral)
                                | (1L << BooleanLiteral)
                                | (1L << PointerLiteral)
                                | (1L << UserDefinedLiteral)
                                | (1L << Alignof)
                                | (1L << Auto)
                                | (1L << Bool)
                                | (1L << Char)
                                | (1L << Char16)
                                | (1L << Char32)
                                | (1L << Class)
                                | (1L << Const)
                                | (1L << Const_cast)
                                | (1L << Decltype)
                                | (1L << Delete)
                                | (1L << Double)
                                | (1L << Dynamic_cast)
                                | (1L << Enum)
                                | (1L << Float)
                                | (1L << Int)
                                | (1L << Long)
                                | (1L << New)
                                | (1L << Noexcept)
                                | (1L << Operator)
                                | (1L << Reinterpret_cast)
                                | (1L << Short)
                                | (1L << Signed)
                                | (1L << Sizeof)))
                        != 0)
                || ((((_la - 65)) & ~0x3f) == 0
                    && ((1L << (_la - 65))
                            & ((1L << (Static_cast - 65))
                                | (1L << (Struct - 65))
                                | (1L << (This - 65))
                                | (1L << (Typeid_ - 65))
                                | (1L << (Typename_ - 65))
                                | (1L << (Union - 65))
                                | (1L << (Unsigned - 65))
                                | (1L << (Void - 65))
                                | (1L << (Volatile - 65))
                                | (1L << (Wchar - 65))
                                | (1L << (LeftParen - 65))
                                | (1L << (LeftBracket - 65))
                                | (1L << (Plus - 65))
                                | (1L << (Minus - 65))
                                | (1L << (Star - 65))
                                | (1L << (And - 65))
                                | (1L << (Or - 65))
                                | (1L << (Tilde - 65))
                                | (1L << (Not - 65))
                                | (1L << (PlusPlus - 65))
                                | (1L << (MinusMinus - 65))
                                | (1L << (Doublecolon - 65))))
                        != 0)
                || _la == Identifier) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Ellipsis) {
          {
            setState(1946);
            match(Ellipsis);
          }
        }

        setState(1956);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == Comma) {
          {
            {
              setState(1949);
              match(Comma);
              setState(1950);
              templateArgument();
              setState(1952);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == Ellipsis) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final TemplateArgumentContext templateArgument() throws RecognitionException {
    TemplateArgumentContext _localctx = new TemplateArgumentContext(_ctx, getState());
    enterRule(_localctx, 350, RULE_templateArgument);
    try {
      setState(1962);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 287, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 289, _ctx)) {
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
              if (_la == Template) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Extern) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (_la == Colon) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        } while (_la == Catch);
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
            if (_la == Alignas || _la == LeftBracket) {
              {
                setState(2006);
                attributeSpecifierSeq();
              }
            }

            setState(2009);
            typeSpecifierSeq();
            setState(2012);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 294, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if ((((_la) & ~0x3f) == 0
                && ((1L << _la)
                        & ((1L << IntegerLiteral)
                            | (1L << CharacterLiteral)
                            | (1L << FloatingLiteral)
                            | (1L << StringLiteral)
                            | (1L << BooleanLiteral)
                            | (1L << PointerLiteral)
                            | (1L << UserDefinedLiteral)
                            | (1L << Alignof)
                            | (1L << Auto)
                            | (1L << Bool)
                            | (1L << Char)
                            | (1L << Char16)
                            | (1L << Char32)
                            | (1L << Const_cast)
                            | (1L << Decltype)
                            | (1L << Delete)
                            | (1L << Double)
                            | (1L << Dynamic_cast)
                            | (1L << Float)
                            | (1L << Int)
                            | (1L << Long)
                            | (1L << New)
                            | (1L << Noexcept)
                            | (1L << Operator)
                            | (1L << Reinterpret_cast)
                            | (1L << Short)
                            | (1L << Signed)
                            | (1L << Sizeof)))
                    != 0)
            || ((((_la - 65)) & ~0x3f) == 0
                && ((1L << (_la - 65))
                        & ((1L << (Static_cast - 65))
                            | (1L << (This - 65))
                            | (1L << (Throw - 65))
                            | (1L << (Typeid_ - 65))
                            | (1L << (Typename_ - 65))
                            | (1L << (Unsigned - 65))
                            | (1L << (Void - 65))
                            | (1L << (Wchar - 65))
                            | (1L << (LeftParen - 65))
                            | (1L << (LeftBracket - 65))
                            | (1L << (Plus - 65))
                            | (1L << (Minus - 65))
                            | (1L << (Star - 65))
                            | (1L << (And - 65))
                            | (1L << (Or - 65))
                            | (1L << (Tilde - 65))
                            | (1L << (Not - 65))
                            | (1L << (PlusPlus - 65))
                            | (1L << (MinusMinus - 65))
                            | (1L << (Doublecolon - 65))))
                    != 0)
            || _la == Identifier) {
          {
            setState(2018);
            assignmentExpression();
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final DynamicExceptionSpecificationContext dynamicExceptionSpecification()
      throws RecognitionException {
    DynamicExceptionSpecificationContext _localctx =
        new DynamicExceptionSpecificationContext(_ctx, getState());
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
        if (((((_la - 13)) & ~0x3f) == 0
                && ((1L << (_la - 13))
                        & ((1L << (Auto - 13))
                            | (1L << (Bool - 13))
                            | (1L << (Char - 13))
                            | (1L << (Char16 - 13))
                            | (1L << (Char32 - 13))
                            | (1L << (Class - 13))
                            | (1L << (Const - 13))
                            | (1L << (Decltype - 13))
                            | (1L << (Double - 13))
                            | (1L << (Enum - 13))
                            | (1L << (Float - 13))
                            | (1L << (Int - 13))
                            | (1L << (Long - 13))
                            | (1L << (Short - 13))
                            | (1L << (Signed - 13))
                            | (1L << (Struct - 13))
                            | (1L << (Typename_ - 13))))
                    != 0)
            || ((((_la - 77)) & ~0x3f) == 0
                && ((1L << (_la - 77))
                        & ((1L << (Union - 77))
                            | (1L << (Unsigned - 77))
                            | (1L << (Void - 77))
                            | (1L << (Volatile - 77))
                            | (1L << (Wchar - 77))
                            | (1L << (Doublecolon - 77))
                            | (1L << (Identifier - 77))))
                    != 0)) {
          {
            setState(2027);
            typeIdList();
          }
        }

        setState(2030);
        match(RightParen);
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
        if (_la == Ellipsis) {
          {
            setState(2033);
            match(Ellipsis);
          }
        }

        setState(2043);
        _errHandler.sync(this);
        _la = _input.LA(1);
        while (_la == Comma) {
          {
            {
              setState(2036);
              match(Comma);
              setState(2037);
              theTypeId();
              setState(2039);
              _errHandler.sync(this);
              _la = _input.LA(1);
              if (_la == Ellipsis) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final NoeExceptSpecificationContext noeExceptSpecification() throws RecognitionException {
    NoeExceptSpecificationContext _localctx = new NoeExceptSpecificationContext(_ctx, getState());
    enterRule(_localctx, 376, RULE_noeExceptSpecification);
    try {
      setState(2052);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 302, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
  }

  public final TheOperatorContext theOperator() throws RecognitionException {
    TheOperatorContext _localctx = new TheOperatorContext(_ctx, getState());
    enterRule(_localctx, 378, RULE_theOperator);
    try {
      setState(2105);
      _errHandler.sync(this);
      switch (getInterpreter().adaptivePredict(_input, 305, _ctx)) {
        case 1:
          enterOuterAlt(_localctx, 1);
          {
            setState(2054);
            match(New);
            setState(2057);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 303, _ctx)) {
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
            switch (getInterpreter().adaptivePredict(_input, 304, _ctx)) {
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
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      exitRule();
    }
    return _localctx;
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
        if (!((((_la) & ~0x3f) == 0
            && ((1L << _la)
                    & ((1L << IntegerLiteral)
                        | (1L << CharacterLiteral)
                        | (1L << FloatingLiteral)
                        | (1L << StringLiteral)
                        | (1L << BooleanLiteral)
                        | (1L << PointerLiteral)
                        | (1L << UserDefinedLiteral)))
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
          switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
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
        _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              {
                _localctx = new NestedNameSpecifierContext(_parentctx, _parentState);
                pushNewRecursionContext(_localctx, _startState, RULE_nestedNameSpecifier);
                setState(431);
                if (!(precpred(_ctx, 1)))
                  throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                setState(437);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 9, _ctx)) {
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
                      if (_la == Template) {
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
          _alt = getInterpreter().adaptivePredict(_input, 10, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
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
        switch (getInterpreter().adaptivePredict(_input, 30, _ctx)) {
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
                    if ((((_la) & ~0x3f) == 0
                            && ((1L << _la)
                                    & ((1L << IntegerLiteral)
                                        | (1L << CharacterLiteral)
                                        | (1L << FloatingLiteral)
                                        | (1L << StringLiteral)
                                        | (1L << BooleanLiteral)
                                        | (1L << PointerLiteral)
                                        | (1L << UserDefinedLiteral)
                                        | (1L << Alignof)
                                        | (1L << Auto)
                                        | (1L << Bool)
                                        | (1L << Char)
                                        | (1L << Char16)
                                        | (1L << Char32)
                                        | (1L << Const_cast)
                                        | (1L << Decltype)
                                        | (1L << Delete)
                                        | (1L << Double)
                                        | (1L << Dynamic_cast)
                                        | (1L << Float)
                                        | (1L << Int)
                                        | (1L << Long)
                                        | (1L << New)
                                        | (1L << Noexcept)
                                        | (1L << Operator)
                                        | (1L << Reinterpret_cast)
                                        | (1L << Short)
                                        | (1L << Signed)
                                        | (1L << Sizeof)))
                                != 0)
                        || ((((_la - 65)) & ~0x3f) == 0
                            && ((1L << (_la - 65))
                                    & ((1L << (Static_cast - 65))
                                        | (1L << (This - 65))
                                        | (1L << (Throw - 65))
                                        | (1L << (Typeid_ - 65))
                                        | (1L << (Typename_ - 65))
                                        | (1L << (Unsigned - 65))
                                        | (1L << (Void - 65))
                                        | (1L << (Wchar - 65))
                                        | (1L << (LeftParen - 65))
                                        | (1L << (LeftBracket - 65))
                                        | (1L << (LeftBrace - 65))
                                        | (1L << (Plus - 65))
                                        | (1L << (Minus - 65))
                                        | (1L << (Star - 65))
                                        | (1L << (And - 65))
                                        | (1L << (Or - 65))
                                        | (1L << (Tilde - 65))
                                        | (1L << (Not - 65))
                                        | (1L << (PlusPlus - 65))
                                        | (1L << (MinusMinus - 65))
                                        | (1L << (Doublecolon - 65))))
                                != 0)
                        || _la == Identifier) {
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
              if (!(((((_la - 24)) & ~0x3f) == 0
                  && ((1L << (_la - 24))
                          & ((1L << (Const_cast - 24))
                              | (1L << (Dynamic_cast - 24))
                              | (1L << (Reinterpret_cast - 24))
                              | (1L << (Static_cast - 24))))
                      != 0))) {
                _errHandler.recoverInline(this);
              } else {
                if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
              switch (getInterpreter().adaptivePredict(_input, 29, _ctx)) {
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
        _alt = getInterpreter().adaptivePredict(_input, 36, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              setState(569);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 35, _ctx)) {
                case 1:
                  {
                    _localctx = new PostfixExpressionContext(_parentctx, _parentState);
                    pushNewRecursionContext(_localctx, _startState, RULE_postfixExpression);
                    setState(544);
                    if (!(precpred(_ctx, 7)))
                      throw new FailedPredicateException(this, "precpred(_ctx, 7)");
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
                    if (!(precpred(_ctx, 6)))
                      throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                    setState(553);
                    match(LeftParen);
                    setState(555);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if ((((_la) & ~0x3f) == 0
                            && ((1L << _la)
                                    & ((1L << IntegerLiteral)
                                        | (1L << CharacterLiteral)
                                        | (1L << FloatingLiteral)
                                        | (1L << StringLiteral)
                                        | (1L << BooleanLiteral)
                                        | (1L << PointerLiteral)
                                        | (1L << UserDefinedLiteral)
                                        | (1L << Alignof)
                                        | (1L << Auto)
                                        | (1L << Bool)
                                        | (1L << Char)
                                        | (1L << Char16)
                                        | (1L << Char32)
                                        | (1L << Const_cast)
                                        | (1L << Decltype)
                                        | (1L << Delete)
                                        | (1L << Double)
                                        | (1L << Dynamic_cast)
                                        | (1L << Float)
                                        | (1L << Int)
                                        | (1L << Long)
                                        | (1L << New)
                                        | (1L << Noexcept)
                                        | (1L << Operator)
                                        | (1L << Reinterpret_cast)
                                        | (1L << Short)
                                        | (1L << Signed)
                                        | (1L << Sizeof)))
                                != 0)
                        || ((((_la - 65)) & ~0x3f) == 0
                            && ((1L << (_la - 65))
                                    & ((1L << (Static_cast - 65))
                                        | (1L << (This - 65))
                                        | (1L << (Throw - 65))
                                        | (1L << (Typeid_ - 65))
                                        | (1L << (Typename_ - 65))
                                        | (1L << (Unsigned - 65))
                                        | (1L << (Void - 65))
                                        | (1L << (Wchar - 65))
                                        | (1L << (LeftParen - 65))
                                        | (1L << (LeftBracket - 65))
                                        | (1L << (LeftBrace - 65))
                                        | (1L << (Plus - 65))
                                        | (1L << (Minus - 65))
                                        | (1L << (Star - 65))
                                        | (1L << (And - 65))
                                        | (1L << (Or - 65))
                                        | (1L << (Tilde - 65))
                                        | (1L << (Not - 65))
                                        | (1L << (PlusPlus - 65))
                                        | (1L << (MinusMinus - 65))
                                        | (1L << (Doublecolon - 65))))
                                != 0)
                        || _la == Identifier) {
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
                    if (!(precpred(_ctx, 4)))
                      throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                    setState(559);
                    _la = _input.LA(1);
                    if (!(_la == Arrow || _la == Dot)) {
                      _errHandler.recoverInline(this);
                    } else {
                      if (_input.LA(1) == Token.EOF) matchedEOF = true;
                      _errHandler.reportMatch(this);
                      consume();
                    }
                    setState(565);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 34, _ctx)) {
                      case 1:
                        {
                          setState(561);
                          _errHandler.sync(this);
                          _la = _input.LA(1);
                          if (_la == Template) {
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
                    if (!(precpred(_ctx, 3)))
                      throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                    setState(568);
                    _la = _input.LA(1);
                    if (!(_la == PlusPlus || _la == MinusMinus)) {
                      _errHandler.recoverInline(this);
                    } else {
                      if (_input.LA(1) == Token.EOF) matchedEOF = true;
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
          _alt = getInterpreter().adaptivePredict(_input, 36, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
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
          switch (getInterpreter().adaptivePredict(_input, 50, _ctx)) {
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
        _alt = getInterpreter().adaptivePredict(_input, 52, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              {
                _localctx = new NoPointerNewDeclaratorContext(_parentctx, _parentState);
                pushNewRecursionContext(_localctx, _startState, RULE_noPointerNewDeclarator);
                setState(669);
                if (!(precpred(_ctx, 1)))
                  throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                setState(670);
                match(LeftBracket);
                setState(671);
                constantExpression();
                setState(672);
                match(RightBracket);
                setState(674);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 51, _ctx)) {
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
          _alt = getInterpreter().adaptivePredict(_input, 52, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
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
              switch (getInterpreter().adaptivePredict(_input, 171, _ctx)) {
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
        _alt = getInterpreter().adaptivePredict(_input, 176, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              {
                _localctx = new NoPointerDeclaratorContext(_parentctx, _parentState);
                pushNewRecursionContext(_localctx, _startState, RULE_noPointerDeclarator);
                setState(1428);
                if (!(precpred(_ctx, 2)))
                  throw new FailedPredicateException(this, "precpred(_ctx, 2)");
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
                      if ((((_la) & ~0x3f) == 0
                              && ((1L << _la)
                                      & ((1L << IntegerLiteral)
                                          | (1L << CharacterLiteral)
                                          | (1L << FloatingLiteral)
                                          | (1L << StringLiteral)
                                          | (1L << BooleanLiteral)
                                          | (1L << PointerLiteral)
                                          | (1L << UserDefinedLiteral)
                                          | (1L << Alignof)
                                          | (1L << Auto)
                                          | (1L << Bool)
                                          | (1L << Char)
                                          | (1L << Char16)
                                          | (1L << Char32)
                                          | (1L << Const_cast)
                                          | (1L << Decltype)
                                          | (1L << Delete)
                                          | (1L << Double)
                                          | (1L << Dynamic_cast)
                                          | (1L << Float)
                                          | (1L << Int)
                                          | (1L << Long)
                                          | (1L << New)
                                          | (1L << Noexcept)
                                          | (1L << Operator)
                                          | (1L << Reinterpret_cast)
                                          | (1L << Short)
                                          | (1L << Signed)
                                          | (1L << Sizeof)))
                                  != 0)
                          || ((((_la - 65)) & ~0x3f) == 0
                              && ((1L << (_la - 65))
                                      & ((1L << (Static_cast - 65))
                                          | (1L << (This - 65))
                                          | (1L << (Typeid_ - 65))
                                          | (1L << (Typename_ - 65))
                                          | (1L << (Unsigned - 65))
                                          | (1L << (Void - 65))
                                          | (1L << (Wchar - 65))
                                          | (1L << (LeftParen - 65))
                                          | (1L << (LeftBracket - 65))
                                          | (1L << (Plus - 65))
                                          | (1L << (Minus - 65))
                                          | (1L << (Star - 65))
                                          | (1L << (And - 65))
                                          | (1L << (Or - 65))
                                          | (1L << (Tilde - 65))
                                          | (1L << (Not - 65))
                                          | (1L << (PlusPlus - 65))
                                          | (1L << (MinusMinus - 65))
                                          | (1L << (Doublecolon - 65))))
                                  != 0)
                          || _la == Identifier) {
                        {
                          setState(1431);
                          constantExpression();
                        }
                      }

                      setState(1434);
                      match(RightBracket);
                      setState(1436);
                      _errHandler.sync(this);
                      switch (getInterpreter().adaptivePredict(_input, 174, _ctx)) {
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
          _alt = getInterpreter().adaptivePredict(_input, 176, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
  }

  private NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator(int _p)
      throws RecognitionException {
    ParserRuleContext _parentctx = _ctx;
    int _parentState = getState();
    NoPointerAbstractDeclaratorContext _localctx =
        new NoPointerAbstractDeclaratorContext(_ctx, _parentState);
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
        switch (getInterpreter().adaptivePredict(_input, 198, _ctx)) {
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
              if ((((_la) & ~0x3f) == 0
                      && ((1L << _la)
                              & ((1L << IntegerLiteral)
                                  | (1L << CharacterLiteral)
                                  | (1L << FloatingLiteral)
                                  | (1L << StringLiteral)
                                  | (1L << BooleanLiteral)
                                  | (1L << PointerLiteral)
                                  | (1L << UserDefinedLiteral)
                                  | (1L << Alignof)
                                  | (1L << Auto)
                                  | (1L << Bool)
                                  | (1L << Char)
                                  | (1L << Char16)
                                  | (1L << Char32)
                                  | (1L << Const_cast)
                                  | (1L << Decltype)
                                  | (1L << Delete)
                                  | (1L << Double)
                                  | (1L << Dynamic_cast)
                                  | (1L << Float)
                                  | (1L << Int)
                                  | (1L << Long)
                                  | (1L << New)
                                  | (1L << Noexcept)
                                  | (1L << Operator)
                                  | (1L << Reinterpret_cast)
                                  | (1L << Short)
                                  | (1L << Signed)
                                  | (1L << Sizeof)))
                          != 0)
                  || ((((_la - 65)) & ~0x3f) == 0
                      && ((1L << (_la - 65))
                              & ((1L << (Static_cast - 65))
                                  | (1L << (This - 65))
                                  | (1L << (Typeid_ - 65))
                                  | (1L << (Typename_ - 65))
                                  | (1L << (Unsigned - 65))
                                  | (1L << (Void - 65))
                                  | (1L << (Wchar - 65))
                                  | (1L << (LeftParen - 65))
                                  | (1L << (LeftBracket - 65))
                                  | (1L << (Plus - 65))
                                  | (1L << (Minus - 65))
                                  | (1L << (Star - 65))
                                  | (1L << (And - 65))
                                  | (1L << (Or - 65))
                                  | (1L << (Tilde - 65))
                                  | (1L << (Not - 65))
                                  | (1L << (PlusPlus - 65))
                                  | (1L << (MinusMinus - 65))
                                  | (1L << (Doublecolon - 65))))
                          != 0)
                  || _la == Identifier) {
                {
                  setState(1525);
                  constantExpression();
                }
              }

              setState(1528);
              match(RightBracket);
              setState(1530);
              _errHandler.sync(this);
              switch (getInterpreter().adaptivePredict(_input, 197, _ctx)) {
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
        _alt = getInterpreter().adaptivePredict(_input, 202, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              {
                _localctx = new NoPointerAbstractDeclaratorContext(_parentctx, _parentState);
                pushNewRecursionContext(_localctx, _startState, RULE_noPointerAbstractDeclarator);
                setState(1538);
                if (!(precpred(_ctx, 4)))
                  throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                setState(1549);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 201, _ctx)) {
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
                      if ((((_la) & ~0x3f) == 0
                              && ((1L << _la)
                                      & ((1L << IntegerLiteral)
                                          | (1L << CharacterLiteral)
                                          | (1L << FloatingLiteral)
                                          | (1L << StringLiteral)
                                          | (1L << BooleanLiteral)
                                          | (1L << PointerLiteral)
                                          | (1L << UserDefinedLiteral)
                                          | (1L << Alignof)
                                          | (1L << Auto)
                                          | (1L << Bool)
                                          | (1L << Char)
                                          | (1L << Char16)
                                          | (1L << Char32)
                                          | (1L << Const_cast)
                                          | (1L << Decltype)
                                          | (1L << Delete)
                                          | (1L << Double)
                                          | (1L << Dynamic_cast)
                                          | (1L << Float)
                                          | (1L << Int)
                                          | (1L << Long)
                                          | (1L << New)
                                          | (1L << Noexcept)
                                          | (1L << Operator)
                                          | (1L << Reinterpret_cast)
                                          | (1L << Short)
                                          | (1L << Signed)
                                          | (1L << Sizeof)))
                                  != 0)
                          || ((((_la - 65)) & ~0x3f) == 0
                              && ((1L << (_la - 65))
                                      & ((1L << (Static_cast - 65))
                                          | (1L << (This - 65))
                                          | (1L << (Typeid_ - 65))
                                          | (1L << (Typename_ - 65))
                                          | (1L << (Unsigned - 65))
                                          | (1L << (Void - 65))
                                          | (1L << (Wchar - 65))
                                          | (1L << (LeftParen - 65))
                                          | (1L << (LeftBracket - 65))
                                          | (1L << (Plus - 65))
                                          | (1L << (Minus - 65))
                                          | (1L << (Star - 65))
                                          | (1L << (And - 65))
                                          | (1L << (Or - 65))
                                          | (1L << (Tilde - 65))
                                          | (1L << (Not - 65))
                                          | (1L << (PlusPlus - 65))
                                          | (1L << (MinusMinus - 65))
                                          | (1L << (Doublecolon - 65))))
                                  != 0)
                          || _la == Identifier) {
                        {
                          setState(1542);
                          constantExpression();
                        }
                      }

                      setState(1545);
                      match(RightBracket);
                      setState(1547);
                      _errHandler.sync(this);
                      switch (getInterpreter().adaptivePredict(_input, 200, _ctx)) {
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
          _alt = getInterpreter().adaptivePredict(_input, 202, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
  }

  private NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator(int _p)
      throws RecognitionException {
    ParserRuleContext _parentctx = _ctx;
    int _parentState = getState();
    NoPointerAbstractPackDeclaratorContext _localctx =
        new NoPointerAbstractPackDeclaratorContext(_ctx, _parentState);
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
        _alt = getInterpreter().adaptivePredict(_input, 207, _ctx);
        while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
          if (_alt == 1) {
            if (_parseListeners != null) triggerExitRuleEvent();
            _prevctx = _localctx;
            {
              {
                _localctx = new NoPointerAbstractPackDeclaratorContext(_parentctx, _parentState);
                pushNewRecursionContext(
                    _localctx, _startState, RULE_noPointerAbstractPackDeclarator);
                setState(1567);
                if (!(precpred(_ctx, 2)))
                  throw new FailedPredicateException(this, "precpred(_ctx, 2)");
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
                      if ((((_la) & ~0x3f) == 0
                              && ((1L << _la)
                                      & ((1L << IntegerLiteral)
                                          | (1L << CharacterLiteral)
                                          | (1L << FloatingLiteral)
                                          | (1L << StringLiteral)
                                          | (1L << BooleanLiteral)
                                          | (1L << PointerLiteral)
                                          | (1L << UserDefinedLiteral)
                                          | (1L << Alignof)
                                          | (1L << Auto)
                                          | (1L << Bool)
                                          | (1L << Char)
                                          | (1L << Char16)
                                          | (1L << Char32)
                                          | (1L << Const_cast)
                                          | (1L << Decltype)
                                          | (1L << Delete)
                                          | (1L << Double)
                                          | (1L << Dynamic_cast)
                                          | (1L << Float)
                                          | (1L << Int)
                                          | (1L << Long)
                                          | (1L << New)
                                          | (1L << Noexcept)
                                          | (1L << Operator)
                                          | (1L << Reinterpret_cast)
                                          | (1L << Short)
                                          | (1L << Signed)
                                          | (1L << Sizeof)))
                                  != 0)
                          || ((((_la - 65)) & ~0x3f) == 0
                              && ((1L << (_la - 65))
                                      & ((1L << (Static_cast - 65))
                                          | (1L << (This - 65))
                                          | (1L << (Typeid_ - 65))
                                          | (1L << (Typename_ - 65))
                                          | (1L << (Unsigned - 65))
                                          | (1L << (Void - 65))
                                          | (1L << (Wchar - 65))
                                          | (1L << (LeftParen - 65))
                                          | (1L << (LeftBracket - 65))
                                          | (1L << (Plus - 65))
                                          | (1L << (Minus - 65))
                                          | (1L << (Star - 65))
                                          | (1L << (And - 65))
                                          | (1L << (Or - 65))
                                          | (1L << (Tilde - 65))
                                          | (1L << (Not - 65))
                                          | (1L << (PlusPlus - 65))
                                          | (1L << (MinusMinus - 65))
                                          | (1L << (Doublecolon - 65))))
                                  != 0)
                          || _la == Identifier) {
                        {
                          setState(1570);
                          constantExpression();
                        }
                      }

                      setState(1573);
                      match(RightBracket);
                      setState(1575);
                      _errHandler.sync(this);
                      switch (getInterpreter().adaptivePredict(_input, 205, _ctx)) {
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
          _alt = getInterpreter().adaptivePredict(_input, 207, _ctx);
        }
      }
    } catch (RecognitionException re) {
      _localctx.exception = re;
      _errHandler.reportError(this, re);
      _errHandler.recover(this, re);
    } finally {
      unrollRecursionContexts(_parentctx);
    }
    return _localctx;
  }

  public static class TranslationUnitContext extends ParserRuleContext {
    public TranslationUnitContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode EOF() {
      return getToken(CPP14Parser.EOF, 0);
    }

    public DeclarationseqContext declarationseq() {
      return getRuleContext(DeclarationseqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_translationUnit;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTranslationUnit(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTranslationUnit(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTranslationUnit(this);
    }
  }

  public static class PrimaryExpressionContext extends ParserRuleContext {
    public PrimaryExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<LiteralContext> literal() {
      return getRuleContexts(LiteralContext.class);
    }

    public LiteralContext literal(int i) {
      return getRuleContext(LiteralContext.class, i);
    }

    public TerminalNode This() {
      return getToken(CPP14Parser.This, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public IdExpressionContext idExpression() {
      return getRuleContext(IdExpressionContext.class, 0);
    }

    public LambdaExpressionContext lambdaExpression() {
      return getRuleContext(LambdaExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_primaryExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPrimaryExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPrimaryExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPrimaryExpression(this);
    }
  }

  public static class IdExpressionContext extends ParserRuleContext {
    public IdExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public UnqualifiedIdContext unqualifiedId() {
      return getRuleContext(UnqualifiedIdContext.class, 0);
    }

    public QualifiedIdContext qualifiedId() {
      return getRuleContext(QualifiedIdContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_idExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitIdExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterIdExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitIdExpression(this);
    }
  }

  public static class UnqualifiedIdContext extends ParserRuleContext {
    public UnqualifiedIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public OperatorFunctionIdContext operatorFunctionId() {
      return getRuleContext(OperatorFunctionIdContext.class, 0);
    }

    public ConversionFunctionIdContext conversionFunctionId() {
      return getRuleContext(ConversionFunctionIdContext.class, 0);
    }

    public LiteralOperatorIdContext literalOperatorId() {
      return getRuleContext(LiteralOperatorIdContext.class, 0);
    }

    public TerminalNode Tilde() {
      return getToken(CPP14Parser.Tilde, 0);
    }

    public ClassNameContext className() {
      return getRuleContext(ClassNameContext.class, 0);
    }

    public DecltypeSpecifierContext decltypeSpecifier() {
      return getRuleContext(DecltypeSpecifierContext.class, 0);
    }

    public TemplateIdContext templateId() {
      return getRuleContext(TemplateIdContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_unqualifiedId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitUnqualifiedId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterUnqualifiedId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitUnqualifiedId(this);
    }
  }

  public static class QualifiedIdContext extends ParserRuleContext {
    public QualifiedIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public UnqualifiedIdContext unqualifiedId() {
      return getRuleContext(UnqualifiedIdContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_qualifiedId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitQualifiedId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterQualifiedId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitQualifiedId(this);
    }
  }

  public static class NestedNameSpecifierContext extends ParserRuleContext {
    public NestedNameSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Doublecolon() {
      return getToken(CPP14Parser.Doublecolon, 0);
    }

    public TheTypeNameContext theTypeName() {
      return getRuleContext(TheTypeNameContext.class, 0);
    }

    public NamespaceNameContext namespaceName() {
      return getRuleContext(NamespaceNameContext.class, 0);
    }

    public DecltypeSpecifierContext decltypeSpecifier() {
      return getRuleContext(DecltypeSpecifierContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_nestedNameSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNestedNameSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNestedNameSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNestedNameSpecifier(this);
    }
  }

  public static class LambdaExpressionContext extends ParserRuleContext {
    public LambdaExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public LambdaIntroducerContext lambdaIntroducer() {
      return getRuleContext(LambdaIntroducerContext.class, 0);
    }

    public CompoundStatementContext compoundStatement() {
      return getRuleContext(CompoundStatementContext.class, 0);
    }

    public LambdaDeclaratorContext lambdaDeclarator() {
      return getRuleContext(LambdaDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLambdaExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLambdaExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLambdaExpression(this);
    }
  }

  public static class LambdaIntroducerContext extends ParserRuleContext {
    public LambdaIntroducerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public LambdaCaptureContext lambdaCapture() {
      return getRuleContext(LambdaCaptureContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaIntroducer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLambdaIntroducer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLambdaIntroducer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLambdaIntroducer(this);
    }
  }

  public static class LambdaCaptureContext extends ParserRuleContext {
    public LambdaCaptureContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public CaptureListContext captureList() {
      return getRuleContext(CaptureListContext.class, 0);
    }

    public CaptureDefaultContext captureDefault() {
      return getRuleContext(CaptureDefaultContext.class, 0);
    }

    public TerminalNode Comma() {
      return getToken(CPP14Parser.Comma, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaCapture;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLambdaCapture(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLambdaCapture(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLambdaCapture(this);
    }
  }

  public static class CaptureDefaultContext extends ParserRuleContext {
    public CaptureDefaultContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_captureDefault;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCaptureDefault(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCaptureDefault(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCaptureDefault(this);
    }
  }

  public static class CaptureListContext extends ParserRuleContext {
    public CaptureListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<CaptureContext> capture() {
      return getRuleContexts(CaptureContext.class);
    }

    public CaptureContext capture(int i) {
      return getRuleContext(CaptureContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_captureList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCaptureList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCaptureList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCaptureList(this);
    }
  }

  public static class CaptureContext extends ParserRuleContext {
    public CaptureContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleCaptureContext simpleCapture() {
      return getRuleContext(SimpleCaptureContext.class, 0);
    }

    public InitcaptureContext initcapture() {
      return getRuleContext(InitcaptureContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_capture;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCapture(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCapture(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCapture(this);
    }
  }

  public static class SimpleCaptureContext extends ParserRuleContext {
    public SimpleCaptureContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    public TerminalNode This() {
      return getToken(CPP14Parser.This, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleCapture;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSimpleCapture(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSimpleCapture(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSimpleCapture(this);
    }
  }

  public static class InitcaptureContext extends ParserRuleContext {
    public InitcaptureContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public InitializerContext initializer() {
      return getRuleContext(InitializerContext.class, 0);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_initcapture;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInitcapture(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInitcapture(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInitcapture(this);
    }
  }

  public static class LambdaDeclaratorContext extends ParserRuleContext {
    public LambdaDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public ParameterDeclarationClauseContext parameterDeclarationClause() {
      return getRuleContext(ParameterDeclarationClauseContext.class, 0);
    }

    public TerminalNode Mutable() {
      return getToken(CPP14Parser.Mutable, 0);
    }

    public ExceptionSpecificationContext exceptionSpecification() {
      return getRuleContext(ExceptionSpecificationContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TrailingReturnTypeContext trailingReturnType() {
      return getRuleContext(TrailingReturnTypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_lambdaDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLambdaDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLambdaDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLambdaDeclarator(this);
    }
  }

  public static class PostfixExpressionContext extends ParserRuleContext {
    public PostfixExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PrimaryExpressionContext primaryExpression() {
      return getRuleContext(PrimaryExpressionContext.class, 0);
    }

    public SimpleTypeSpecifierContext simpleTypeSpecifier() {
      return getRuleContext(SimpleTypeSpecifierContext.class, 0);
    }

    public TypeNameSpecifierContext typeNameSpecifier() {
      return getRuleContext(TypeNameSpecifierContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public TerminalNode Less() {
      return getToken(CPP14Parser.Less, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public TerminalNode Greater() {
      return getToken(CPP14Parser.Greater, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode Dynamic_cast() {
      return getToken(CPP14Parser.Dynamic_cast, 0);
    }

    public TerminalNode Static_cast() {
      return getToken(CPP14Parser.Static_cast, 0);
    }

    public TerminalNode Reinterpret_cast() {
      return getToken(CPP14Parser.Reinterpret_cast, 0);
    }

    public TerminalNode Const_cast() {
      return getToken(CPP14Parser.Const_cast, 0);
    }

    public TypeIdOfTheTypeIdContext typeIdOfTheTypeId() {
      return getRuleContext(TypeIdOfTheTypeIdContext.class, 0);
    }

    public PostfixExpressionContext postfixExpression() {
      return getRuleContext(PostfixExpressionContext.class, 0);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public TerminalNode Dot() {
      return getToken(CPP14Parser.Dot, 0);
    }

    public TerminalNode Arrow() {
      return getToken(CPP14Parser.Arrow, 0);
    }

    public IdExpressionContext idExpression() {
      return getRuleContext(IdExpressionContext.class, 0);
    }

    public PseudoDestructorNameContext pseudoDestructorName() {
      return getRuleContext(PseudoDestructorNameContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public TerminalNode PlusPlus() {
      return getToken(CPP14Parser.PlusPlus, 0);
    }

    public TerminalNode MinusMinus() {
      return getToken(CPP14Parser.MinusMinus, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_postfixExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPostfixExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPostfixExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPostfixExpression(this);
    }
  }

  public static class TypeIdOfTheTypeIdContext extends ParserRuleContext {
    public TypeIdOfTheTypeIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Typeid_() {
      return getToken(CPP14Parser.Typeid_, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeIdOfTheTypeId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypeIdOfTheTypeId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypeIdOfTheTypeId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypeIdOfTheTypeId(this);
    }
  }

  public static class ExpressionListContext extends ParserRuleContext {
    public ExpressionListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public InitializerListContext initializerList() {
      return getRuleContext(InitializerListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_expressionList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExpressionList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExpressionList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExpressionList(this);
    }
  }

  public static class PseudoDestructorNameContext extends ParserRuleContext {
    public PseudoDestructorNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Tilde() {
      return getToken(CPP14Parser.Tilde, 0);
    }

    public List<TheTypeNameContext> theTypeName() {
      return getRuleContexts(TheTypeNameContext.class);
    }

    public TheTypeNameContext theTypeName(int i) {
      return getRuleContext(TheTypeNameContext.class, i);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public TerminalNode Doublecolon() {
      return getToken(CPP14Parser.Doublecolon, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    public DecltypeSpecifierContext decltypeSpecifier() {
      return getRuleContext(DecltypeSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_pseudoDestructorName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPseudoDestructorName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPseudoDestructorName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPseudoDestructorName(this);
    }
  }

  public static class UnaryExpressionContext extends ParserRuleContext {
    public UnaryExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PostfixExpressionContext postfixExpression() {
      return getRuleContext(PostfixExpressionContext.class, 0);
    }

    public UnaryExpressionContext unaryExpression() {
      return getRuleContext(UnaryExpressionContext.class, 0);
    }

    public TerminalNode PlusPlus() {
      return getToken(CPP14Parser.PlusPlus, 0);
    }

    public TerminalNode MinusMinus() {
      return getToken(CPP14Parser.MinusMinus, 0);
    }

    public UnaryOperatorContext unaryOperator() {
      return getRuleContext(UnaryOperatorContext.class, 0);
    }

    public TerminalNode Sizeof() {
      return getToken(CPP14Parser.Sizeof, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode Alignof() {
      return getToken(CPP14Parser.Alignof, 0);
    }

    public NoExceptExpressionContext noExceptExpression() {
      return getRuleContext(NoExceptExpressionContext.class, 0);
    }

    public NewExpressionContext newExpression() {
      return getRuleContext(NewExpressionContext.class, 0);
    }

    public DeleteExpressionContext deleteExpression() {
      return getRuleContext(DeleteExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_unaryExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitUnaryExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterUnaryExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitUnaryExpression(this);
    }
  }

  public static class UnaryOperatorContext extends ParserRuleContext {
    public UnaryOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Or() {
      return getToken(CPP14Parser.Or, 0);
    }

    public TerminalNode Star() {
      return getToken(CPP14Parser.Star, 0);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    public TerminalNode Plus() {
      return getToken(CPP14Parser.Plus, 0);
    }

    public TerminalNode Tilde() {
      return getToken(CPP14Parser.Tilde, 0);
    }

    public TerminalNode Minus() {
      return getToken(CPP14Parser.Minus, 0);
    }

    public TerminalNode Not() {
      return getToken(CPP14Parser.Not, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_unaryOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitUnaryOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterUnaryOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitUnaryOperator(this);
    }
  }

  public static class NewExpressionContext extends ParserRuleContext {
    public NewExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode New() {
      return getToken(CPP14Parser.New, 0);
    }

    public NewTypeIdContext newTypeId() {
      return getRuleContext(NewTypeIdContext.class, 0);
    }

    public TerminalNode Doublecolon() {
      return getToken(CPP14Parser.Doublecolon, 0);
    }

    public NewPlacementContext newPlacement() {
      return getRuleContext(NewPlacementContext.class, 0);
    }

    public NewInitializerContext newInitializer() {
      return getRuleContext(NewInitializerContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_newExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNewExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNewExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNewExpression(this);
    }
  }

  public static class NewPlacementContext extends ParserRuleContext {
    public NewPlacementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_newPlacement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNewPlacement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNewPlacement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNewPlacement(this);
    }
  }

  public static class NewTypeIdContext extends ParserRuleContext {
    public NewTypeIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeSpecifierSeqContext typeSpecifierSeq() {
      return getRuleContext(TypeSpecifierSeqContext.class, 0);
    }

    public NewDeclaratorContext newDeclarator() {
      return getRuleContext(NewDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_newTypeId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNewTypeId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNewTypeId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNewTypeId(this);
    }
  }

  public static class NewDeclaratorContext extends ParserRuleContext {
    public NewDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PointerOperatorContext pointerOperator() {
      return getRuleContext(PointerOperatorContext.class, 0);
    }

    public NewDeclaratorContext newDeclarator() {
      return getRuleContext(NewDeclaratorContext.class, 0);
    }

    public NoPointerNewDeclaratorContext noPointerNewDeclarator() {
      return getRuleContext(NoPointerNewDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_newDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNewDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNewDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNewDeclarator(this);
    }
  }

  public static class NoPointerNewDeclaratorContext extends ParserRuleContext {
    public NoPointerNewDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public NoPointerNewDeclaratorContext noPointerNewDeclarator() {
      return getRuleContext(NoPointerNewDeclaratorContext.class, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_noPointerNewDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNoPointerNewDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNoPointerNewDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNoPointerNewDeclarator(this);
    }
  }

  public static class NewInitializerContext extends ParserRuleContext {
    public NewInitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_newInitializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNewInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNewInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNewInitializer(this);
    }
  }

  public static class DeleteExpressionContext extends ParserRuleContext {
    public DeleteExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Delete() {
      return getToken(CPP14Parser.Delete, 0);
    }

    public CastExpressionContext castExpression() {
      return getRuleContext(CastExpressionContext.class, 0);
    }

    public TerminalNode Doublecolon() {
      return getToken(CPP14Parser.Doublecolon, 0);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_deleteExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeleteExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeleteExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeleteExpression(this);
    }
  }

  public static class NoExceptExpressionContext extends ParserRuleContext {
    public NoExceptExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Noexcept() {
      return getToken(CPP14Parser.Noexcept, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_noExceptExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNoExceptExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNoExceptExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNoExceptExpression(this);
    }
  }

  public static class CastExpressionContext extends ParserRuleContext {
    public CastExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public UnaryExpressionContext unaryExpression() {
      return getRuleContext(UnaryExpressionContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public CastExpressionContext castExpression() {
      return getRuleContext(CastExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_castExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCastExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCastExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCastExpression(this);
    }
  }

  public static class PointerMemberExpressionContext extends ParserRuleContext {
    public PointerMemberExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<CastExpressionContext> castExpression() {
      return getRuleContexts(CastExpressionContext.class);
    }

    public CastExpressionContext castExpression(int i) {
      return getRuleContext(CastExpressionContext.class, i);
    }

    public List<TerminalNode> DotStar() {
      return getTokens(CPP14Parser.DotStar);
    }

    public TerminalNode DotStar(int i) {
      return getToken(CPP14Parser.DotStar, i);
    }

    public List<TerminalNode> ArrowStar() {
      return getTokens(CPP14Parser.ArrowStar);
    }

    public TerminalNode ArrowStar(int i) {
      return getToken(CPP14Parser.ArrowStar, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_pointerMemberExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPointerMemberExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPointerMemberExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPointerMemberExpression(this);
    }
  }

  public static class MultiplicativeExpressionContext extends ParserRuleContext {
    public MultiplicativeExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<PointerMemberExpressionContext> pointerMemberExpression() {
      return getRuleContexts(PointerMemberExpressionContext.class);
    }

    public PointerMemberExpressionContext pointerMemberExpression(int i) {
      return getRuleContext(PointerMemberExpressionContext.class, i);
    }

    public List<TerminalNode> Star() {
      return getTokens(CPP14Parser.Star);
    }

    public TerminalNode Star(int i) {
      return getToken(CPP14Parser.Star, i);
    }

    public List<TerminalNode> Div() {
      return getTokens(CPP14Parser.Div);
    }

    public TerminalNode Div(int i) {
      return getToken(CPP14Parser.Div, i);
    }

    public List<TerminalNode> Mod() {
      return getTokens(CPP14Parser.Mod);
    }

    public TerminalNode Mod(int i) {
      return getToken(CPP14Parser.Mod, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_multiplicativeExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMultiplicativeExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMultiplicativeExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMultiplicativeExpression(this);
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

    public List<TerminalNode> Plus() {
      return getTokens(CPP14Parser.Plus);
    }

    public TerminalNode Plus(int i) {
      return getToken(CPP14Parser.Plus, i);
    }

    public List<TerminalNode> Minus() {
      return getTokens(CPP14Parser.Minus);
    }

    public TerminalNode Minus(int i) {
      return getToken(CPP14Parser.Minus, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_additiveExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAdditiveExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAdditiveExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAdditiveExpression(this);
    }
  }

  public static class ShiftExpressionContext extends ParserRuleContext {
    public ShiftExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AdditiveExpressionContext> additiveExpression() {
      return getRuleContexts(AdditiveExpressionContext.class);
    }

    public AdditiveExpressionContext additiveExpression(int i) {
      return getRuleContext(AdditiveExpressionContext.class, i);
    }

    public List<ShiftOperatorContext> shiftOperator() {
      return getRuleContexts(ShiftOperatorContext.class);
    }

    public ShiftOperatorContext shiftOperator(int i) {
      return getRuleContext(ShiftOperatorContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_shiftExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitShiftExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterShiftExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitShiftExpression(this);
    }
  }

  public static class ShiftOperatorContext extends ParserRuleContext {
    public ShiftOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TerminalNode> Greater() {
      return getTokens(CPP14Parser.Greater);
    }

    public TerminalNode Greater(int i) {
      return getToken(CPP14Parser.Greater, i);
    }

    public List<TerminalNode> Less() {
      return getTokens(CPP14Parser.Less);
    }

    public TerminalNode Less(int i) {
      return getToken(CPP14Parser.Less, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_shiftOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitShiftOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterShiftOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitShiftOperator(this);
    }
  }

  public static class RelationalExpressionContext extends ParserRuleContext {
    public RelationalExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ShiftExpressionContext> shiftExpression() {
      return getRuleContexts(ShiftExpressionContext.class);
    }

    public ShiftExpressionContext shiftExpression(int i) {
      return getRuleContext(ShiftExpressionContext.class, i);
    }

    public List<TerminalNode> Less() {
      return getTokens(CPP14Parser.Less);
    }

    public TerminalNode Less(int i) {
      return getToken(CPP14Parser.Less, i);
    }

    public List<TerminalNode> Greater() {
      return getTokens(CPP14Parser.Greater);
    }

    public TerminalNode Greater(int i) {
      return getToken(CPP14Parser.Greater, i);
    }

    public List<TerminalNode> LessEqual() {
      return getTokens(CPP14Parser.LessEqual);
    }

    public TerminalNode LessEqual(int i) {
      return getToken(CPP14Parser.LessEqual, i);
    }

    public List<TerminalNode> GreaterEqual() {
      return getTokens(CPP14Parser.GreaterEqual);
    }

    public TerminalNode GreaterEqual(int i) {
      return getToken(CPP14Parser.GreaterEqual, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_relationalExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitRelationalExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterRelationalExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitRelationalExpression(this);
    }
  }

  public static class EqualityExpressionContext extends ParserRuleContext {
    public EqualityExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<RelationalExpressionContext> relationalExpression() {
      return getRuleContexts(RelationalExpressionContext.class);
    }

    public RelationalExpressionContext relationalExpression(int i) {
      return getRuleContext(RelationalExpressionContext.class, i);
    }

    public List<TerminalNode> Equal() {
      return getTokens(CPP14Parser.Equal);
    }

    public TerminalNode Equal(int i) {
      return getToken(CPP14Parser.Equal, i);
    }

    public List<TerminalNode> NotEqual() {
      return getTokens(CPP14Parser.NotEqual);
    }

    public TerminalNode NotEqual(int i) {
      return getToken(CPP14Parser.NotEqual, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_equalityExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEqualityExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEqualityExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEqualityExpression(this);
    }
  }

  public static class AndExpressionContext extends ParserRuleContext {
    public AndExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<EqualityExpressionContext> equalityExpression() {
      return getRuleContexts(EqualityExpressionContext.class);
    }

    public EqualityExpressionContext equalityExpression(int i) {
      return getRuleContext(EqualityExpressionContext.class, i);
    }

    public List<TerminalNode> And() {
      return getTokens(CPP14Parser.And);
    }

    public TerminalNode And(int i) {
      return getToken(CPP14Parser.And, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_andExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAndExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAndExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAndExpression(this);
    }
  }

  public static class ExclusiveOrExpressionContext extends ParserRuleContext {
    public ExclusiveOrExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AndExpressionContext> andExpression() {
      return getRuleContexts(AndExpressionContext.class);
    }

    public AndExpressionContext andExpression(int i) {
      return getRuleContext(AndExpressionContext.class, i);
    }

    public List<TerminalNode> Caret() {
      return getTokens(CPP14Parser.Caret);
    }

    public TerminalNode Caret(int i) {
      return getToken(CPP14Parser.Caret, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_exclusiveOrExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExclusiveOrExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExclusiveOrExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExclusiveOrExpression(this);
    }
  }

  public static class InclusiveOrExpressionContext extends ParserRuleContext {
    public InclusiveOrExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ExclusiveOrExpressionContext> exclusiveOrExpression() {
      return getRuleContexts(ExclusiveOrExpressionContext.class);
    }

    public ExclusiveOrExpressionContext exclusiveOrExpression(int i) {
      return getRuleContext(ExclusiveOrExpressionContext.class, i);
    }

    public List<TerminalNode> Or() {
      return getTokens(CPP14Parser.Or);
    }

    public TerminalNode Or(int i) {
      return getToken(CPP14Parser.Or, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_inclusiveOrExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInclusiveOrExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInclusiveOrExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInclusiveOrExpression(this);
    }
  }

  public static class LogicalAndExpressionContext extends ParserRuleContext {
    public LogicalAndExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<InclusiveOrExpressionContext> inclusiveOrExpression() {
      return getRuleContexts(InclusiveOrExpressionContext.class);
    }

    public InclusiveOrExpressionContext inclusiveOrExpression(int i) {
      return getRuleContext(InclusiveOrExpressionContext.class, i);
    }

    public List<TerminalNode> AndAnd() {
      return getTokens(CPP14Parser.AndAnd);
    }

    public TerminalNode AndAnd(int i) {
      return getToken(CPP14Parser.AndAnd, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_logicalAndExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLogicalAndExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLogicalAndExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLogicalAndExpression(this);
    }
  }

  public static class LogicalOrExpressionContext extends ParserRuleContext {
    public LogicalOrExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<LogicalAndExpressionContext> logicalAndExpression() {
      return getRuleContexts(LogicalAndExpressionContext.class);
    }

    public LogicalAndExpressionContext logicalAndExpression(int i) {
      return getRuleContext(LogicalAndExpressionContext.class, i);
    }

    public List<TerminalNode> OrOr() {
      return getTokens(CPP14Parser.OrOr);
    }

    public TerminalNode OrOr(int i) {
      return getToken(CPP14Parser.OrOr, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_logicalOrExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLogicalOrExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLogicalOrExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLogicalOrExpression(this);
    }
  }

  public static class ConditionalExpressionContext extends ParserRuleContext {
    public ConditionalExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public LogicalOrExpressionContext logicalOrExpression() {
      return getRuleContext(LogicalOrExpressionContext.class, 0);
    }

    public TerminalNode Question() {
      return getToken(CPP14Parser.Question, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public AssignmentExpressionContext assignmentExpression() {
      return getRuleContext(AssignmentExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_conditionalExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitConditionalExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterConditionalExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitConditionalExpression(this);
    }
  }

  public static class AssignmentExpressionContext extends ParserRuleContext {
    public AssignmentExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ConditionalExpressionContext conditionalExpression() {
      return getRuleContext(ConditionalExpressionContext.class, 0);
    }

    public LogicalOrExpressionContext logicalOrExpression() {
      return getRuleContext(LogicalOrExpressionContext.class, 0);
    }

    public AssignmentOperatorContext assignmentOperator() {
      return getRuleContext(AssignmentOperatorContext.class, 0);
    }

    public InitializerClauseContext initializerClause() {
      return getRuleContext(InitializerClauseContext.class, 0);
    }

    public ThrowExpressionContext throwExpression() {
      return getRuleContext(ThrowExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_assignmentExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAssignmentExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAssignmentExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAssignmentExpression(this);
    }
  }

  public static class AssignmentOperatorContext extends ParserRuleContext {
    public AssignmentOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public TerminalNode StarAssign() {
      return getToken(CPP14Parser.StarAssign, 0);
    }

    public TerminalNode DivAssign() {
      return getToken(CPP14Parser.DivAssign, 0);
    }

    public TerminalNode ModAssign() {
      return getToken(CPP14Parser.ModAssign, 0);
    }

    public TerminalNode PlusAssign() {
      return getToken(CPP14Parser.PlusAssign, 0);
    }

    public TerminalNode MinusAssign() {
      return getToken(CPP14Parser.MinusAssign, 0);
    }

    public TerminalNode RightShiftAssign() {
      return getToken(CPP14Parser.RightShiftAssign, 0);
    }

    public TerminalNode LeftShiftAssign() {
      return getToken(CPP14Parser.LeftShiftAssign, 0);
    }

    public TerminalNode AndAssign() {
      return getToken(CPP14Parser.AndAssign, 0);
    }

    public TerminalNode XorAssign() {
      return getToken(CPP14Parser.XorAssign, 0);
    }

    public TerminalNode OrAssign() {
      return getToken(CPP14Parser.OrAssign, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_assignmentOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAssignmentOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAssignmentOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAssignmentOperator(this);
    }
  }

  public static class ExpressionContext extends ParserRuleContext {
    public ExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AssignmentExpressionContext> assignmentExpression() {
      return getRuleContexts(AssignmentExpressionContext.class);
    }

    public AssignmentExpressionContext assignmentExpression(int i) {
      return getRuleContext(AssignmentExpressionContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_expression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExpression(this);
    }
  }

  public static class ConstantExpressionContext extends ParserRuleContext {
    public ConstantExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ConditionalExpressionContext conditionalExpression() {
      return getRuleContext(ConditionalExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constantExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitConstantExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterConstantExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitConstantExpression(this);
    }
  }

  public static class StatementContext extends ParserRuleContext {
    public StatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public LabeledStatementContext labeledStatement() {
      return getRuleContext(LabeledStatementContext.class, 0);
    }

    public DeclarationStatementContext declarationStatement() {
      return getRuleContext(DeclarationStatementContext.class, 0);
    }

    public ExpressionStatementContext expressionStatement() {
      return getRuleContext(ExpressionStatementContext.class, 0);
    }

    public CompoundStatementContext compoundStatement() {
      return getRuleContext(CompoundStatementContext.class, 0);
    }

    public SelectionStatementContext selectionStatement() {
      return getRuleContext(SelectionStatementContext.class, 0);
    }

    public IterationStatementContext iterationStatement() {
      return getRuleContext(IterationStatementContext.class, 0);
    }

    public JumpStatementContext jumpStatement() {
      return getRuleContext(JumpStatementContext.class, 0);
    }

    public TryBlockContext tryBlock() {
      return getRuleContext(TryBlockContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_statement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitStatement(this);
    }
  }

  public static class LabeledStatementContext extends ParserRuleContext {
    public LabeledStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public StatementContext statement() {
      return getRuleContext(StatementContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode Case() {
      return getToken(CPP14Parser.Case, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public TerminalNode Default() {
      return getToken(CPP14Parser.Default, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_labeledStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLabeledStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLabeledStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLabeledStatement(this);
    }
  }

  public static class ExpressionStatementContext extends ParserRuleContext {
    public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_expressionStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExpressionStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExpressionStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExpressionStatement(this);
    }
  }

  public static class CompoundStatementContext extends ParserRuleContext {
    public CompoundStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftBrace() {
      return getToken(CPP14Parser.LeftBrace, 0);
    }

    public TerminalNode RightBrace() {
      return getToken(CPP14Parser.RightBrace, 0);
    }

    public StatementSeqContext statementSeq() {
      return getRuleContext(StatementSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_compoundStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCompoundStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCompoundStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCompoundStatement(this);
    }
  }

  public static class StatementSeqContext extends ParserRuleContext {
    public StatementSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<StatementContext> statement() {
      return getRuleContexts(StatementContext.class);
    }

    public StatementContext statement(int i) {
      return getRuleContext(StatementContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_statementSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitStatementSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterStatementSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitStatementSeq(this);
    }
  }

  public static class SelectionStatementContext extends ParserRuleContext {
    public SelectionStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode If() {
      return getToken(CPP14Parser.If, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ConditionContext condition() {
      return getRuleContext(ConditionContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public List<StatementContext> statement() {
      return getRuleContexts(StatementContext.class);
    }

    public StatementContext statement(int i) {
      return getRuleContext(StatementContext.class, i);
    }

    public TerminalNode Else() {
      return getToken(CPP14Parser.Else, 0);
    }

    public TerminalNode Switch() {
      return getToken(CPP14Parser.Switch, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_selectionStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSelectionStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSelectionStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSelectionStatement(this);
    }
  }

  public static class ConditionContext extends ParserRuleContext {
    public ConditionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public DeclSpecifierSeqContext declSpecifierSeq() {
      return getRuleContext(DeclSpecifierSeqContext.class, 0);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public InitializerClauseContext initializerClause() {
      return getRuleContext(InitializerClauseContext.class, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_condition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCondition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCondition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCondition(this);
    }
  }

  public static class IterationStatementContext extends ParserRuleContext {
    public IterationStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode While() {
      return getToken(CPP14Parser.While, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ConditionContext condition() {
      return getRuleContext(ConditionContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public StatementContext statement() {
      return getRuleContext(StatementContext.class, 0);
    }

    public TerminalNode Do() {
      return getToken(CPP14Parser.Do, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public TerminalNode For() {
      return getToken(CPP14Parser.For, 0);
    }

    public ForInitStatementContext forInitStatement() {
      return getRuleContext(ForInitStatementContext.class, 0);
    }

    public ForRangeDeclarationContext forRangeDeclaration() {
      return getRuleContext(ForRangeDeclarationContext.class, 0);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public ForRangeInitializerContext forRangeInitializer() {
      return getRuleContext(ForRangeInitializerContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_iterationStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitIterationStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterIterationStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitIterationStatement(this);
    }
  }

  public static class ForInitStatementContext extends ParserRuleContext {
    public ForInitStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ExpressionStatementContext expressionStatement() {
      return getRuleContext(ExpressionStatementContext.class, 0);
    }

    public SimpleDeclarationContext simpleDeclaration() {
      return getRuleContext(SimpleDeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forInitStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitForInitStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterForInitStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitForInitStatement(this);
    }
  }

  public static class ForRangeDeclarationContext extends ParserRuleContext {
    public ForRangeDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclSpecifierSeqContext declSpecifierSeq() {
      return getRuleContext(DeclSpecifierSeqContext.class, 0);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forRangeDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitForRangeDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterForRangeDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitForRangeDeclaration(this);
    }
  }

  public static class ForRangeInitializerContext extends ParserRuleContext {
    public ForRangeInitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_forRangeInitializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitForRangeInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterForRangeInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitForRangeInitializer(this);
    }
  }

  public static class JumpStatementContext extends ParserRuleContext {
    public JumpStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public TerminalNode Break() {
      return getToken(CPP14Parser.Break, 0);
    }

    public TerminalNode Continue() {
      return getToken(CPP14Parser.Continue, 0);
    }

    public TerminalNode Return() {
      return getToken(CPP14Parser.Return, 0);
    }

    public TerminalNode Goto() {
      return getToken(CPP14Parser.Goto, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_jumpStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitJumpStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterJumpStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitJumpStatement(this);
    }
  }

  public static class DeclarationStatementContext extends ParserRuleContext {
    public DeclarationStatementContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BlockDeclarationContext blockDeclaration() {
      return getRuleContext(BlockDeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declarationStatement;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclarationStatement(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclarationStatement(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclarationStatement(this);
    }
  }

  public static class DeclarationseqContext extends ParserRuleContext {
    public DeclarationseqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<DeclarationContext> declaration() {
      return getRuleContexts(DeclarationContext.class);
    }

    public DeclarationContext declaration(int i) {
      return getRuleContext(DeclarationContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declarationseq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclarationseq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclarationseq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclarationseq(this);
    }
  }

  public static class DeclarationContext extends ParserRuleContext {
    public DeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BlockDeclarationContext blockDeclaration() {
      return getRuleContext(BlockDeclarationContext.class, 0);
    }

    public FunctionDefinitionContext functionDefinition() {
      return getRuleContext(FunctionDefinitionContext.class, 0);
    }

    public TemplateDeclarationContext templateDeclaration() {
      return getRuleContext(TemplateDeclarationContext.class, 0);
    }

    public ExplicitInstantiationContext explicitInstantiation() {
      return getRuleContext(ExplicitInstantiationContext.class, 0);
    }

    public ExplicitSpecializationContext explicitSpecialization() {
      return getRuleContext(ExplicitSpecializationContext.class, 0);
    }

    public LinkageSpecificationContext linkageSpecification() {
      return getRuleContext(LinkageSpecificationContext.class, 0);
    }

    public NamespaceDefinitionContext namespaceDefinition() {
      return getRuleContext(NamespaceDefinitionContext.class, 0);
    }

    public EmptyDeclarationContext emptyDeclaration() {
      return getRuleContext(EmptyDeclarationContext.class, 0);
    }

    public AttributeDeclarationContext attributeDeclaration() {
      return getRuleContext(AttributeDeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclaration(this);
    }
  }

  public static class BlockDeclarationContext extends ParserRuleContext {
    public BlockDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleDeclarationContext simpleDeclaration() {
      return getRuleContext(SimpleDeclarationContext.class, 0);
    }

    public AsmDefinitionContext asmDefinition() {
      return getRuleContext(AsmDefinitionContext.class, 0);
    }

    public NamespaceAliasDefinitionContext namespaceAliasDefinition() {
      return getRuleContext(NamespaceAliasDefinitionContext.class, 0);
    }

    public UsingDeclarationContext usingDeclaration() {
      return getRuleContext(UsingDeclarationContext.class, 0);
    }

    public UsingDirectiveContext usingDirective() {
      return getRuleContext(UsingDirectiveContext.class, 0);
    }

    public StaticAssertDeclarationContext staticAssertDeclaration() {
      return getRuleContext(StaticAssertDeclarationContext.class, 0);
    }

    public AliasDeclarationContext aliasDeclaration() {
      return getRuleContext(AliasDeclarationContext.class, 0);
    }

    public OpaqueEnumDeclarationContext opaqueEnumDeclaration() {
      return getRuleContext(OpaqueEnumDeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_blockDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBlockDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBlockDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBlockDeclaration(this);
    }
  }

  public static class AliasDeclarationContext extends ParserRuleContext {
    public AliasDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Using() {
      return getToken(CPP14Parser.Using, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_aliasDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAliasDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAliasDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAliasDeclaration(this);
    }
  }

  public static class SimpleDeclarationContext extends ParserRuleContext {
    public SimpleDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public DeclSpecifierSeqContext declSpecifierSeq() {
      return getRuleContext(DeclSpecifierSeqContext.class, 0);
    }

    public InitDeclaratorListContext initDeclaratorList() {
      return getRuleContext(InitDeclaratorListContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSimpleDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSimpleDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSimpleDeclaration(this);
    }
  }

  public static class StaticAssertDeclarationContext extends ParserRuleContext {
    public StaticAssertDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Static_assert() {
      return getToken(CPP14Parser.Static_assert, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public TerminalNode Comma() {
      return getToken(CPP14Parser.Comma, 0);
    }

    public TerminalNode StringLiteral() {
      return getToken(CPP14Parser.StringLiteral, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_staticAssertDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitStaticAssertDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterStaticAssertDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitStaticAssertDeclaration(this);
    }
  }

  public static class EmptyDeclarationContext extends ParserRuleContext {
    public EmptyDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_emptyDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEmptyDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEmptyDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEmptyDeclaration(this);
    }
  }

  public static class AttributeDeclarationContext extends ParserRuleContext {
    public AttributeDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attributeDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttributeDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttributeDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttributeDeclaration(this);
    }
  }

  public static class DeclSpecifierContext extends ParserRuleContext {
    public DeclSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public StorageClassSpecifierContext storageClassSpecifier() {
      return getRuleContext(StorageClassSpecifierContext.class, 0);
    }

    public TypeSpecifierContext typeSpecifier() {
      return getRuleContext(TypeSpecifierContext.class, 0);
    }

    public FunctionSpecifierContext functionSpecifier() {
      return getRuleContext(FunctionSpecifierContext.class, 0);
    }

    public TerminalNode Friend() {
      return getToken(CPP14Parser.Friend, 0);
    }

    public TerminalNode Typedef() {
      return getToken(CPP14Parser.Typedef, 0);
    }

    public TerminalNode Constexpr() {
      return getToken(CPP14Parser.Constexpr, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclSpecifier(this);
    }
  }

  public static class DeclSpecifierSeqContext extends ParserRuleContext {
    public DeclSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<DeclSpecifierContext> declSpecifier() {
      return getRuleContexts(DeclSpecifierContext.class);
    }

    public DeclSpecifierContext declSpecifier(int i) {
      return getRuleContext(DeclSpecifierContext.class, i);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declSpecifierSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclSpecifierSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclSpecifierSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclSpecifierSeq(this);
    }
  }

  public static class StorageClassSpecifierContext extends ParserRuleContext {
    public StorageClassSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Register() {
      return getToken(CPP14Parser.Register, 0);
    }

    public TerminalNode Static() {
      return getToken(CPP14Parser.Static, 0);
    }

    public TerminalNode Thread_local() {
      return getToken(CPP14Parser.Thread_local, 0);
    }

    public TerminalNode Extern() {
      return getToken(CPP14Parser.Extern, 0);
    }

    public TerminalNode Mutable() {
      return getToken(CPP14Parser.Mutable, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_storageClassSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitStorageClassSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterStorageClassSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitStorageClassSpecifier(this);
    }
  }

  public static class FunctionSpecifierContext extends ParserRuleContext {
    public FunctionSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Inline() {
      return getToken(CPP14Parser.Inline, 0);
    }

    public TerminalNode Virtual() {
      return getToken(CPP14Parser.Virtual, 0);
    }

    public TerminalNode Explicit() {
      return getToken(CPP14Parser.Explicit, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitFunctionSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterFunctionSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitFunctionSpecifier(this);
    }
  }

  public static class TypedefNameContext extends ParserRuleContext {
    public TypedefNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typedefName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypedefName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypedefName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypedefName(this);
    }
  }

  public static class TypeSpecifierContext extends ParserRuleContext {
    public TypeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TrailingTypeSpecifierContext trailingTypeSpecifier() {
      return getRuleContext(TrailingTypeSpecifierContext.class, 0);
    }

    public ClassSpecifierContext classSpecifier() {
      return getRuleContext(ClassSpecifierContext.class, 0);
    }

    public EnumSpecifierContext enumSpecifier() {
      return getRuleContext(EnumSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypeSpecifier(this);
    }
  }

  public static class TrailingTypeSpecifierContext extends ParserRuleContext {
    public TrailingTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleTypeSpecifierContext simpleTypeSpecifier() {
      return getRuleContext(SimpleTypeSpecifierContext.class, 0);
    }

    public ElaboratedTypeSpecifierContext elaboratedTypeSpecifier() {
      return getRuleContext(ElaboratedTypeSpecifierContext.class, 0);
    }

    public TypeNameSpecifierContext typeNameSpecifier() {
      return getRuleContext(TypeNameSpecifierContext.class, 0);
    }

    public CvQualifierContext cvQualifier() {
      return getRuleContext(CvQualifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_trailingTypeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTrailingTypeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTrailingTypeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTrailingTypeSpecifier(this);
    }
  }

  public static class TypeSpecifierSeqContext extends ParserRuleContext {
    public TypeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TypeSpecifierContext> typeSpecifier() {
      return getRuleContexts(TypeSpecifierContext.class);
    }

    public TypeSpecifierContext typeSpecifier(int i) {
      return getRuleContext(TypeSpecifierContext.class, i);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeSpecifierSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypeSpecifierSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypeSpecifierSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypeSpecifierSeq(this);
    }
  }

  public static class TrailingTypeSpecifierSeqContext extends ParserRuleContext {
    public TrailingTypeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TrailingTypeSpecifierContext> trailingTypeSpecifier() {
      return getRuleContexts(TrailingTypeSpecifierContext.class);
    }

    public TrailingTypeSpecifierContext trailingTypeSpecifier(int i) {
      return getRuleContext(TrailingTypeSpecifierContext.class, i);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_trailingTypeSpecifierSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTrailingTypeSpecifierSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTrailingTypeSpecifierSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTrailingTypeSpecifierSeq(this);
    }
  }

  public static class SimpleTypeLengthModifierContext extends ParserRuleContext {
    public SimpleTypeLengthModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Short() {
      return getToken(CPP14Parser.Short, 0);
    }

    public TerminalNode Long() {
      return getToken(CPP14Parser.Long, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleTypeLengthModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSimpleTypeLengthModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSimpleTypeLengthModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSimpleTypeLengthModifier(this);
    }
  }

  public static class SimpleTypeSignednessModifierContext extends ParserRuleContext {
    public SimpleTypeSignednessModifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Unsigned() {
      return getToken(CPP14Parser.Unsigned, 0);
    }

    public TerminalNode Signed() {
      return getToken(CPP14Parser.Signed, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleTypeSignednessModifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSimpleTypeSignednessModifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSimpleTypeSignednessModifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSimpleTypeSignednessModifier(this);
    }
  }

  public static class SimpleTypeSpecifierContext extends ParserRuleContext {
    public SimpleTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TheTypeNameContext theTypeName() {
      return getRuleContext(TheTypeNameContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    public SimpleTypeSignednessModifierContext simpleTypeSignednessModifier() {
      return getRuleContext(SimpleTypeSignednessModifierContext.class, 0);
    }

    public List<SimpleTypeLengthModifierContext> simpleTypeLengthModifier() {
      return getRuleContexts(SimpleTypeLengthModifierContext.class);
    }

    public SimpleTypeLengthModifierContext simpleTypeLengthModifier(int i) {
      return getRuleContext(SimpleTypeLengthModifierContext.class, i);
    }

    public TerminalNode Char() {
      return getToken(CPP14Parser.Char, 0);
    }

    public TerminalNode Char16() {
      return getToken(CPP14Parser.Char16, 0);
    }

    public TerminalNode Char32() {
      return getToken(CPP14Parser.Char32, 0);
    }

    public TerminalNode Wchar() {
      return getToken(CPP14Parser.Wchar, 0);
    }

    public TerminalNode Bool() {
      return getToken(CPP14Parser.Bool, 0);
    }

    public TerminalNode Int() {
      return getToken(CPP14Parser.Int, 0);
    }

    public TerminalNode Float() {
      return getToken(CPP14Parser.Float, 0);
    }

    public TerminalNode Double() {
      return getToken(CPP14Parser.Double, 0);
    }

    public TerminalNode Void() {
      return getToken(CPP14Parser.Void, 0);
    }

    public TerminalNode Auto() {
      return getToken(CPP14Parser.Auto, 0);
    }

    public DecltypeSpecifierContext decltypeSpecifier() {
      return getRuleContext(DecltypeSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleTypeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSimpleTypeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSimpleTypeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSimpleTypeSpecifier(this);
    }
  }

  public static class TheTypeNameContext extends ParserRuleContext {
    public TheTypeNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassNameContext className() {
      return getRuleContext(ClassNameContext.class, 0);
    }

    public EnumNameContext enumName() {
      return getRuleContext(EnumNameContext.class, 0);
    }

    public TypedefNameContext typedefName() {
      return getRuleContext(TypedefNameContext.class, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_theTypeName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTheTypeName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTheTypeName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTheTypeName(this);
    }
  }

  public static class DecltypeSpecifierContext extends ParserRuleContext {
    public DecltypeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Decltype() {
      return getToken(CPP14Parser.Decltype, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public ExpressionContext expression() {
      return getRuleContext(ExpressionContext.class, 0);
    }

    public TerminalNode Auto() {
      return getToken(CPP14Parser.Auto, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_decltypeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDecltypeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDecltypeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDecltypeSpecifier(this);
    }
  }

  public static class ElaboratedTypeSpecifierContext extends ParserRuleContext {
    public ElaboratedTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassKeyContext classKey() {
      return getRuleContext(ClassKeyContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public TerminalNode Enum() {
      return getToken(CPP14Parser.Enum, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_elaboratedTypeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitElaboratedTypeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterElaboratedTypeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitElaboratedTypeSpecifier(this);
    }
  }

  public static class EnumNameContext extends ParserRuleContext {
    public EnumNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumName(this);
    }
  }

  public static class EnumSpecifierContext extends ParserRuleContext {
    public EnumSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public EnumHeadContext enumHead() {
      return getRuleContext(EnumHeadContext.class, 0);
    }

    public TerminalNode LeftBrace() {
      return getToken(CPP14Parser.LeftBrace, 0);
    }

    public TerminalNode RightBrace() {
      return getToken(CPP14Parser.RightBrace, 0);
    }

    public EnumeratorListContext enumeratorList() {
      return getRuleContext(EnumeratorListContext.class, 0);
    }

    public TerminalNode Comma() {
      return getToken(CPP14Parser.Comma, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumSpecifier(this);
    }
  }

  public static class EnumHeadContext extends ParserRuleContext {
    public EnumHeadContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public EnumkeyContext enumkey() {
      return getRuleContext(EnumkeyContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public EnumbaseContext enumbase() {
      return getRuleContext(EnumbaseContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumHead;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumHead(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumHead(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumHead(this);
    }
  }

  public static class OpaqueEnumDeclarationContext extends ParserRuleContext {
    public OpaqueEnumDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public EnumkeyContext enumkey() {
      return getRuleContext(EnumkeyContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public EnumbaseContext enumbase() {
      return getRuleContext(EnumbaseContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_opaqueEnumDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitOpaqueEnumDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterOpaqueEnumDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitOpaqueEnumDeclaration(this);
    }
  }

  public static class EnumkeyContext extends ParserRuleContext {
    public EnumkeyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Enum() {
      return getToken(CPP14Parser.Enum, 0);
    }

    public TerminalNode Class() {
      return getToken(CPP14Parser.Class, 0);
    }

    public TerminalNode Struct() {
      return getToken(CPP14Parser.Struct, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumkey;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumkey(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumkey(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumkey(this);
    }
  }

  public static class EnumbaseContext extends ParserRuleContext {
    public EnumbaseContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public TypeSpecifierSeqContext typeSpecifierSeq() {
      return getRuleContext(TypeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumbase;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumbase(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumbase(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumbase(this);
    }
  }

  public static class EnumeratorListContext extends ParserRuleContext {
    public EnumeratorListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<EnumeratorDefinitionContext> enumeratorDefinition() {
      return getRuleContexts(EnumeratorDefinitionContext.class);
    }

    public EnumeratorDefinitionContext enumeratorDefinition(int i) {
      return getRuleContext(EnumeratorDefinitionContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumeratorList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumeratorList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumeratorList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumeratorList(this);
    }
  }

  public static class EnumeratorDefinitionContext extends ParserRuleContext {
    public EnumeratorDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public EnumeratorContext enumerator() {
      return getRuleContext(EnumeratorContext.class, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumeratorDefinition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumeratorDefinition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumeratorDefinition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumeratorDefinition(this);
    }
  }

  public static class EnumeratorContext extends ParserRuleContext {
    public EnumeratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_enumerator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitEnumerator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterEnumerator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitEnumerator(this);
    }
  }

  public static class NamespaceNameContext extends ParserRuleContext {
    public NamespaceNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public OriginalNamespaceNameContext originalNamespaceName() {
      return getRuleContext(OriginalNamespaceNameContext.class, 0);
    }

    public NamespaceAliasContext namespaceAlias() {
      return getRuleContext(NamespaceAliasContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_namespaceName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNamespaceName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNamespaceName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNamespaceName(this);
    }
  }

  public static class OriginalNamespaceNameContext extends ParserRuleContext {
    public OriginalNamespaceNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_originalNamespaceName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitOriginalNamespaceName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterOriginalNamespaceName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitOriginalNamespaceName(this);
    }
  }

  public static class NamespaceDefinitionContext extends ParserRuleContext {
    public DeclarationseqContext namespaceBody;

    public NamespaceDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Namespace() {
      return getToken(CPP14Parser.Namespace, 0);
    }

    public TerminalNode LeftBrace() {
      return getToken(CPP14Parser.LeftBrace, 0);
    }

    public TerminalNode RightBrace() {
      return getToken(CPP14Parser.RightBrace, 0);
    }

    public TerminalNode Inline() {
      return getToken(CPP14Parser.Inline, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public OriginalNamespaceNameContext originalNamespaceName() {
      return getRuleContext(OriginalNamespaceNameContext.class, 0);
    }

    public DeclarationseqContext declarationseq() {
      return getRuleContext(DeclarationseqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_namespaceDefinition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNamespaceDefinition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNamespaceDefinition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNamespaceDefinition(this);
    }
  }

  public static class NamespaceAliasContext extends ParserRuleContext {
    public NamespaceAliasContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_namespaceAlias;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNamespaceAlias(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNamespaceAlias(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNamespaceAlias(this);
    }
  }

  public static class NamespaceAliasDefinitionContext extends ParserRuleContext {
    public NamespaceAliasDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Namespace() {
      return getToken(CPP14Parser.Namespace, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public QualifiednamespacespecifierContext qualifiednamespacespecifier() {
      return getRuleContext(QualifiednamespacespecifierContext.class, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_namespaceAliasDefinition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNamespaceAliasDefinition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNamespaceAliasDefinition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNamespaceAliasDefinition(this);
    }
  }

  public static class QualifiednamespacespecifierContext extends ParserRuleContext {
    public QualifiednamespacespecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public NamespaceNameContext namespaceName() {
      return getRuleContext(NamespaceNameContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_qualifiednamespacespecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitQualifiednamespacespecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterQualifiednamespacespecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitQualifiednamespacespecifier(this);
    }
  }

  public static class UsingDeclarationContext extends ParserRuleContext {
    public UsingDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Using() {
      return getToken(CPP14Parser.Using, 0);
    }

    public UnqualifiedIdContext unqualifiedId() {
      return getRuleContext(UnqualifiedIdContext.class, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public TerminalNode Doublecolon() {
      return getToken(CPP14Parser.Doublecolon, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public TerminalNode Typename_() {
      return getToken(CPP14Parser.Typename_, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_usingDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitUsingDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterUsingDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitUsingDeclaration(this);
    }
  }

  public static class UsingDirectiveContext extends ParserRuleContext {
    public UsingDirectiveContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Using() {
      return getToken(CPP14Parser.Using, 0);
    }

    public TerminalNode Namespace() {
      return getToken(CPP14Parser.Namespace, 0);
    }

    public NamespaceNameContext namespaceName() {
      return getRuleContext(NamespaceNameContext.class, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_usingDirective;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitUsingDirective(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterUsingDirective(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitUsingDirective(this);
    }
  }

  public static class AsmDefinitionContext extends ParserRuleContext {
    public AsmDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Asm() {
      return getToken(CPP14Parser.Asm, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode StringLiteral() {
      return getToken(CPP14Parser.StringLiteral, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_asmDefinition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAsmDefinition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAsmDefinition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAsmDefinition(this);
    }
  }

  public static class LinkageSpecificationContext extends ParserRuleContext {
    public LinkageSpecificationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Extern() {
      return getToken(CPP14Parser.Extern, 0);
    }

    public TerminalNode StringLiteral() {
      return getToken(CPP14Parser.StringLiteral, 0);
    }

    public TerminalNode LeftBrace() {
      return getToken(CPP14Parser.LeftBrace, 0);
    }

    public TerminalNode RightBrace() {
      return getToken(CPP14Parser.RightBrace, 0);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    public DeclarationseqContext declarationseq() {
      return getRuleContext(DeclarationseqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_linkageSpecification;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLinkageSpecification(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLinkageSpecification(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLinkageSpecification(this);
    }
  }

  public static class AttributeSpecifierSeqContext extends ParserRuleContext {
    public AttributeSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AttributeSpecifierContext> attributeSpecifier() {
      return getRuleContexts(AttributeSpecifierContext.class);
    }

    public AttributeSpecifierContext attributeSpecifier(int i) {
      return getRuleContext(AttributeSpecifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attributeSpecifierSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttributeSpecifierSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttributeSpecifierSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttributeSpecifierSeq(this);
    }
  }

  public static class AttributeSpecifierContext extends ParserRuleContext {
    public AttributeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TerminalNode> LeftBracket() {
      return getTokens(CPP14Parser.LeftBracket);
    }

    public TerminalNode LeftBracket(int i) {
      return getToken(CPP14Parser.LeftBracket, i);
    }

    public List<TerminalNode> RightBracket() {
      return getTokens(CPP14Parser.RightBracket);
    }

    public TerminalNode RightBracket(int i) {
      return getToken(CPP14Parser.RightBracket, i);
    }

    public AttributeListContext attributeList() {
      return getRuleContext(AttributeListContext.class, 0);
    }

    public AlignmentspecifierContext alignmentspecifier() {
      return getRuleContext(AlignmentspecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attributeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttributeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttributeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttributeSpecifier(this);
    }
  }

  public static class AlignmentspecifierContext extends ParserRuleContext {
    public AlignmentspecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Alignas() {
      return getToken(CPP14Parser.Alignas, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_alignmentspecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAlignmentspecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAlignmentspecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAlignmentspecifier(this);
    }
  }

  public static class AttributeListContext extends ParserRuleContext {
    public AttributeListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<AttributeContext> attribute() {
      return getRuleContexts(AttributeContext.class);
    }

    public AttributeContext attribute(int i) {
      return getRuleContext(AttributeContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attributeList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttributeList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttributeList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttributeList(this);
    }
  }

  public static class AttributeContext extends ParserRuleContext {
    public AttributeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public AttributeNamespaceContext attributeNamespace() {
      return getRuleContext(AttributeNamespaceContext.class, 0);
    }

    public TerminalNode Doublecolon() {
      return getToken(CPP14Parser.Doublecolon, 0);
    }

    public AttributeArgumentClauseContext attributeArgumentClause() {
      return getRuleContext(AttributeArgumentClauseContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attribute;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttribute(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttribute(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttribute(this);
    }
  }

  public static class AttributeNamespaceContext extends ParserRuleContext {
    public AttributeNamespaceContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attributeNamespace;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttributeNamespace(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttributeNamespace(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttributeNamespace(this);
    }
  }

  public static class AttributeArgumentClauseContext extends ParserRuleContext {
    public AttributeArgumentClauseContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public BalancedTokenSeqContext balancedTokenSeq() {
      return getRuleContext(BalancedTokenSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_attributeArgumentClause;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAttributeArgumentClause(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAttributeArgumentClause(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAttributeArgumentClause(this);
    }
  }

  public static class BalancedTokenSeqContext extends ParserRuleContext {
    public BalancedTokenSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<BalancedtokenContext> balancedtoken() {
      return getRuleContexts(BalancedtokenContext.class);
    }

    public BalancedtokenContext balancedtoken(int i) {
      return getRuleContext(BalancedtokenContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_balancedTokenSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBalancedTokenSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBalancedTokenSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBalancedTokenSeq(this);
    }
  }

  public static class BalancedtokenContext extends ParserRuleContext {
    public BalancedtokenContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TerminalNode> LeftParen() {
      return getTokens(CPP14Parser.LeftParen);
    }

    public TerminalNode LeftParen(int i) {
      return getToken(CPP14Parser.LeftParen, i);
    }

    public BalancedTokenSeqContext balancedTokenSeq() {
      return getRuleContext(BalancedTokenSeqContext.class, 0);
    }

    public List<TerminalNode> RightParen() {
      return getTokens(CPP14Parser.RightParen);
    }

    public TerminalNode RightParen(int i) {
      return getToken(CPP14Parser.RightParen, i);
    }

    public List<TerminalNode> LeftBracket() {
      return getTokens(CPP14Parser.LeftBracket);
    }

    public TerminalNode LeftBracket(int i) {
      return getToken(CPP14Parser.LeftBracket, i);
    }

    public List<TerminalNode> RightBracket() {
      return getTokens(CPP14Parser.RightBracket);
    }

    public TerminalNode RightBracket(int i) {
      return getToken(CPP14Parser.RightBracket, i);
    }

    public List<TerminalNode> LeftBrace() {
      return getTokens(CPP14Parser.LeftBrace);
    }

    public TerminalNode LeftBrace(int i) {
      return getToken(CPP14Parser.LeftBrace, i);
    }

    public List<TerminalNode> RightBrace() {
      return getTokens(CPP14Parser.RightBrace);
    }

    public TerminalNode RightBrace(int i) {
      return getToken(CPP14Parser.RightBrace, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_balancedtoken;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBalancedtoken(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBalancedtoken(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBalancedtoken(this);
    }
  }

  public static class InitDeclaratorListContext extends ParserRuleContext {
    public InitDeclaratorListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<InitDeclaratorContext> initDeclarator() {
      return getRuleContexts(InitDeclaratorContext.class);
    }

    public InitDeclaratorContext initDeclarator(int i) {
      return getRuleContext(InitDeclaratorContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_initDeclaratorList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInitDeclaratorList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInitDeclaratorList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInitDeclaratorList(this);
    }
  }

  public static class InitDeclaratorContext extends ParserRuleContext {
    public InitDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public InitializerContext initializer() {
      return getRuleContext(InitializerContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_initDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInitDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInitDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInitDeclarator(this);
    }
  }

  public static class DeclaratorContext extends ParserRuleContext {
    public DeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PointerDeclaratorContext pointerDeclarator() {
      return getRuleContext(PointerDeclaratorContext.class, 0);
    }

    public NoPointerDeclaratorContext noPointerDeclarator() {
      return getRuleContext(NoPointerDeclaratorContext.class, 0);
    }

    public ParametersAndQualifiersContext parametersAndQualifiers() {
      return getRuleContext(ParametersAndQualifiersContext.class, 0);
    }

    public TrailingReturnTypeContext trailingReturnType() {
      return getRuleContext(TrailingReturnTypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclarator(this);
    }
  }

  public static class PointerDeclaratorContext extends ParserRuleContext {
    public PointerDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public NoPointerDeclaratorContext noPointerDeclarator() {
      return getRuleContext(NoPointerDeclaratorContext.class, 0);
    }

    public List<PointerOperatorContext> pointerOperator() {
      return getRuleContexts(PointerOperatorContext.class);
    }

    public PointerOperatorContext pointerOperator(int i) {
      return getRuleContext(PointerOperatorContext.class, i);
    }

    public List<TerminalNode> Const() {
      return getTokens(CPP14Parser.Const);
    }

    public TerminalNode Const(int i) {
      return getToken(CPP14Parser.Const, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_pointerDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPointerDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPointerDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPointerDeclarator(this);
    }
  }

  public static class NoPointerDeclaratorContext extends ParserRuleContext {
    public NoPointerDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclaratoridContext declaratorid() {
      return getRuleContext(DeclaratoridContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public PointerDeclaratorContext pointerDeclarator() {
      return getRuleContext(PointerDeclaratorContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public NoPointerDeclaratorContext noPointerDeclarator() {
      return getRuleContext(NoPointerDeclaratorContext.class, 0);
    }

    public ParametersAndQualifiersContext parametersAndQualifiers() {
      return getRuleContext(ParametersAndQualifiersContext.class, 0);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_noPointerDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNoPointerDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNoPointerDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNoPointerDeclarator(this);
    }
  }

  public static class ParametersAndQualifiersContext extends ParserRuleContext {
    public ParametersAndQualifiersContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public ParameterDeclarationClauseContext parameterDeclarationClause() {
      return getRuleContext(ParameterDeclarationClauseContext.class, 0);
    }

    public CvqualifierseqContext cvqualifierseq() {
      return getRuleContext(CvqualifierseqContext.class, 0);
    }

    public RefqualifierContext refqualifier() {
      return getRuleContext(RefqualifierContext.class, 0);
    }

    public ExceptionSpecificationContext exceptionSpecification() {
      return getRuleContext(ExceptionSpecificationContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parametersAndQualifiers;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitParametersAndQualifiers(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterParametersAndQualifiers(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitParametersAndQualifiers(this);
    }
  }

  public static class TrailingReturnTypeContext extends ParserRuleContext {
    public TrailingReturnTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Arrow() {
      return getToken(CPP14Parser.Arrow, 0);
    }

    public TrailingTypeSpecifierSeqContext trailingTypeSpecifierSeq() {
      return getRuleContext(TrailingTypeSpecifierSeqContext.class, 0);
    }

    public AbstractDeclaratorContext abstractDeclarator() {
      return getRuleContext(AbstractDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_trailingReturnType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTrailingReturnType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTrailingReturnType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTrailingReturnType(this);
    }
  }

  public static class PointerOperatorContext extends ParserRuleContext {
    public PointerOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    public TerminalNode AndAnd() {
      return getToken(CPP14Parser.AndAnd, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TerminalNode Star() {
      return getToken(CPP14Parser.Star, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public CvqualifierseqContext cvqualifierseq() {
      return getRuleContext(CvqualifierseqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_pointerOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPointerOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPointerOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPointerOperator(this);
    }
  }

  public static class CvqualifierseqContext extends ParserRuleContext {
    public CvqualifierseqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<CvQualifierContext> cvQualifier() {
      return getRuleContexts(CvQualifierContext.class);
    }

    public CvQualifierContext cvQualifier(int i) {
      return getRuleContext(CvQualifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_cvqualifierseq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCvqualifierseq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCvqualifierseq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCvqualifierseq(this);
    }
  }

  public static class CvQualifierContext extends ParserRuleContext {
    public CvQualifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Const() {
      return getToken(CPP14Parser.Const, 0);
    }

    public TerminalNode Volatile() {
      return getToken(CPP14Parser.Volatile, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_cvQualifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitCvQualifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterCvQualifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitCvQualifier(this);
    }
  }

  public static class RefqualifierContext extends ParserRuleContext {
    public RefqualifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    public TerminalNode AndAnd() {
      return getToken(CPP14Parser.AndAnd, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_refqualifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitRefqualifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterRefqualifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitRefqualifier(this);
    }
  }

  public static class DeclaratoridContext extends ParserRuleContext {
    public DeclaratoridContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public IdExpressionContext idExpression() {
      return getRuleContext(IdExpressionContext.class, 0);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_declaratorid;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDeclaratorid(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDeclaratorid(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDeclaratorid(this);
    }
  }

  public static class TheTypeIdContext extends ParserRuleContext {
    public TheTypeIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeSpecifierSeqContext typeSpecifierSeq() {
      return getRuleContext(TypeSpecifierSeqContext.class, 0);
    }

    public AbstractDeclaratorContext abstractDeclarator() {
      return getRuleContext(AbstractDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_theTypeId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTheTypeId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTheTypeId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTheTypeId(this);
    }
  }

  public static class AbstractDeclaratorContext extends ParserRuleContext {
    public AbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PointerAbstractDeclaratorContext pointerAbstractDeclarator() {
      return getRuleContext(PointerAbstractDeclaratorContext.class, 0);
    }

    public ParametersAndQualifiersContext parametersAndQualifiers() {
      return getRuleContext(ParametersAndQualifiersContext.class, 0);
    }

    public TrailingReturnTypeContext trailingReturnType() {
      return getRuleContext(TrailingReturnTypeContext.class, 0);
    }

    public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() {
      return getRuleContext(NoPointerAbstractDeclaratorContext.class, 0);
    }

    public AbstractPackDeclaratorContext abstractPackDeclarator() {
      return getRuleContext(AbstractPackDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_abstractDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAbstractDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAbstractDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAbstractDeclarator(this);
    }
  }

  public static class PointerAbstractDeclaratorContext extends ParserRuleContext {
    public PointerAbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator() {
      return getRuleContext(NoPointerAbstractDeclaratorContext.class, 0);
    }

    public List<PointerOperatorContext> pointerOperator() {
      return getRuleContexts(PointerOperatorContext.class);
    }

    public PointerOperatorContext pointerOperator(int i) {
      return getRuleContext(PointerOperatorContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_pointerAbstractDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPointerAbstractDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPointerAbstractDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPointerAbstractDeclarator(this);
    }
  }

  public static class NoPointerAbstractDeclaratorContext extends ParserRuleContext {
    public NoPointerAbstractDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParametersAndQualifiersContext parametersAndQualifiers() {
      return getRuleContext(ParametersAndQualifiersContext.class, 0);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public PointerAbstractDeclaratorContext pointerAbstractDeclarator() {
      return getRuleContext(PointerAbstractDeclaratorContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public List<NoPointerAbstractDeclaratorContext> noPointerAbstractDeclarator() {
      return getRuleContexts(NoPointerAbstractDeclaratorContext.class);
    }

    public NoPointerAbstractDeclaratorContext noPointerAbstractDeclarator(int i) {
      return getRuleContext(NoPointerAbstractDeclaratorContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_noPointerAbstractDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNoPointerAbstractDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNoPointerAbstractDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNoPointerAbstractDeclarator(this);
    }
  }

  public static class AbstractPackDeclaratorContext extends ParserRuleContext {
    public AbstractPackDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() {
      return getRuleContext(NoPointerAbstractPackDeclaratorContext.class, 0);
    }

    public List<PointerOperatorContext> pointerOperator() {
      return getRuleContexts(PointerOperatorContext.class);
    }

    public PointerOperatorContext pointerOperator(int i) {
      return getRuleContext(PointerOperatorContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_abstractPackDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAbstractPackDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAbstractPackDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAbstractPackDeclarator(this);
    }
  }

  public static class NoPointerAbstractPackDeclaratorContext extends ParserRuleContext {
    public NoPointerAbstractPackDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    public NoPointerAbstractPackDeclaratorContext noPointerAbstractPackDeclarator() {
      return getRuleContext(NoPointerAbstractPackDeclaratorContext.class, 0);
    }

    public ParametersAndQualifiersContext parametersAndQualifiers() {
      return getRuleContext(ParametersAndQualifiersContext.class, 0);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_noPointerAbstractPackDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor)
            .visitNoPointerAbstractPackDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNoPointerAbstractPackDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNoPointerAbstractPackDeclarator(this);
    }
  }

  public static class ParameterDeclarationClauseContext extends ParserRuleContext {
    public ParameterDeclarationClauseContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ParameterDeclarationListContext parameterDeclarationList() {
      return getRuleContext(ParameterDeclarationListContext.class, 0);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    public TerminalNode Comma() {
      return getToken(CPP14Parser.Comma, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameterDeclarationClause;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitParameterDeclarationClause(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterParameterDeclarationClause(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitParameterDeclarationClause(this);
    }
  }

  public static class ParameterDeclarationListContext extends ParserRuleContext {
    public ParameterDeclarationListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<ParameterDeclarationContext> parameterDeclaration() {
      return getRuleContexts(ParameterDeclarationContext.class);
    }

    public ParameterDeclarationContext parameterDeclaration(int i) {
      return getRuleContext(ParameterDeclarationContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameterDeclarationList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitParameterDeclarationList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterParameterDeclarationList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitParameterDeclarationList(this);
    }
  }

  public static class ParameterDeclarationContext extends ParserRuleContext {
    public ParameterDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclSpecifierSeqContext declSpecifierSeq() {
      return getRuleContext(DeclSpecifierSeqContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public InitializerClauseContext initializerClause() {
      return getRuleContext(InitializerClauseContext.class, 0);
    }

    public AbstractDeclaratorContext abstractDeclarator() {
      return getRuleContext(AbstractDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_parameterDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitParameterDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterParameterDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitParameterDeclaration(this);
    }
  }

  public static class FunctionDefinitionContext extends ParserRuleContext {
    public FunctionDefinitionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public FunctionBodyContext functionBody() {
      return getRuleContext(FunctionBodyContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public DeclSpecifierSeqContext declSpecifierSeq() {
      return getRuleContext(DeclSpecifierSeqContext.class, 0);
    }

    public VirtualSpecifierSeqContext virtualSpecifierSeq() {
      return getRuleContext(VirtualSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionDefinition;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitFunctionDefinition(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterFunctionDefinition(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitFunctionDefinition(this);
    }
  }

  public static class FunctionBodyContext extends ParserRuleContext {
    public FunctionBodyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public CompoundStatementContext compoundStatement() {
      return getRuleContext(CompoundStatementContext.class, 0);
    }

    public ConstructorInitializerContext constructorInitializer() {
      return getRuleContext(ConstructorInitializerContext.class, 0);
    }

    public FunctionTryBlockContext functionTryBlock() {
      return getRuleContext(FunctionTryBlockContext.class, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public TerminalNode Default() {
      return getToken(CPP14Parser.Default, 0);
    }

    public TerminalNode Delete() {
      return getToken(CPP14Parser.Delete, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionBody;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitFunctionBody(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterFunctionBody(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitFunctionBody(this);
    }
  }

  public static class InitializerContext extends ParserRuleContext {
    public InitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BraceOrEqualInitializerContext braceOrEqualInitializer() {
      return getRuleContext(BraceOrEqualInitializerContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_initializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInitializer(this);
    }
  }

  public static class BraceOrEqualInitializerContext extends ParserRuleContext {
    public BraceOrEqualInitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public InitializerClauseContext initializerClause() {
      return getRuleContext(InitializerClauseContext.class, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_braceOrEqualInitializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBraceOrEqualInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBraceOrEqualInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBraceOrEqualInitializer(this);
    }
  }

  public static class InitializerClauseContext extends ParserRuleContext {
    public InitializerClauseContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public AssignmentExpressionContext assignmentExpression() {
      return getRuleContext(AssignmentExpressionContext.class, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_initializerClause;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInitializerClause(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInitializerClause(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInitializerClause(this);
    }
  }

  public static class InitializerListContext extends ParserRuleContext {
    public InitializerListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<InitializerClauseContext> initializerClause() {
      return getRuleContexts(InitializerClauseContext.class);
    }

    public InitializerClauseContext initializerClause(int i) {
      return getRuleContext(InitializerClauseContext.class, i);
    }

    public List<TerminalNode> Ellipsis() {
      return getTokens(CPP14Parser.Ellipsis);
    }

    public TerminalNode Ellipsis(int i) {
      return getToken(CPP14Parser.Ellipsis, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_initializerList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitInitializerList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterInitializerList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitInitializerList(this);
    }
  }

  public static class BracedInitListContext extends ParserRuleContext {
    public BracedInitListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode LeftBrace() {
      return getToken(CPP14Parser.LeftBrace, 0);
    }

    public TerminalNode RightBrace() {
      return getToken(CPP14Parser.RightBrace, 0);
    }

    public InitializerListContext initializerList() {
      return getRuleContext(InitializerListContext.class, 0);
    }

    public TerminalNode Comma() {
      return getToken(CPP14Parser.Comma, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_bracedInitList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBracedInitList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBracedInitList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBracedInitList(this);
    }
  }

  public static class ClassNameContext extends ParserRuleContext {
    public ClassNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_className;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassName(this);
    }
  }

  public static class ClassSpecifierContext extends ParserRuleContext {
    public ClassSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassHeadContext classHead() {
      return getRuleContext(ClassHeadContext.class, 0);
    }

    public TerminalNode LeftBrace() {
      return getToken(CPP14Parser.LeftBrace, 0);
    }

    public TerminalNode RightBrace() {
      return getToken(CPP14Parser.RightBrace, 0);
    }

    public MemberSpecificationContext memberSpecification() {
      return getRuleContext(MemberSpecificationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassSpecifier(this);
    }
  }

  public static class ClassHeadContext extends ParserRuleContext {
    public ClassHeadContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassKeyContext classKey() {
      return getRuleContext(ClassKeyContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public ClassHeadNameContext classHeadName() {
      return getRuleContext(ClassHeadNameContext.class, 0);
    }

    public BaseClauseContext baseClause() {
      return getRuleContext(BaseClauseContext.class, 0);
    }

    public ClassVirtSpecifierContext classVirtSpecifier() {
      return getRuleContext(ClassVirtSpecifierContext.class, 0);
    }

    public TerminalNode Union() {
      return getToken(CPP14Parser.Union, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classHead;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassHead(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassHead(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassHead(this);
    }
  }

  public static class ClassHeadNameContext extends ParserRuleContext {
    public ClassHeadNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassNameContext className() {
      return getRuleContext(ClassNameContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classHeadName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassHeadName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassHeadName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassHeadName(this);
    }
  }

  public static class ClassVirtSpecifierContext extends ParserRuleContext {
    public ClassVirtSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Final() {
      return getToken(CPP14Parser.Final, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classVirtSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassVirtSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassVirtSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassVirtSpecifier(this);
    }
  }

  public static class ClassKeyContext extends ParserRuleContext {
    public ClassKeyContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Class() {
      return getToken(CPP14Parser.Class, 0);
    }

    public TerminalNode Struct() {
      return getToken(CPP14Parser.Struct, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classKey;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassKey(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassKey(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassKey(this);
    }
  }

  public static class MemberSpecificationContext extends ParserRuleContext {
    public MemberSpecificationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<MemberdeclarationContext> memberdeclaration() {
      return getRuleContexts(MemberdeclarationContext.class);
    }

    public MemberdeclarationContext memberdeclaration(int i) {
      return getRuleContext(MemberdeclarationContext.class, i);
    }

    public List<AccessSpecifierContext> accessSpecifier() {
      return getRuleContexts(AccessSpecifierContext.class);
    }

    public AccessSpecifierContext accessSpecifier(int i) {
      return getRuleContext(AccessSpecifierContext.class, i);
    }

    public List<TerminalNode> Colon() {
      return getTokens(CPP14Parser.Colon);
    }

    public TerminalNode Colon(int i) {
      return getToken(CPP14Parser.Colon, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberSpecification;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMemberSpecification(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMemberSpecification(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMemberSpecification(this);
    }
  }

  public static class MemberdeclarationContext extends ParserRuleContext {
    public MemberdeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Semi() {
      return getToken(CPP14Parser.Semi, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public DeclSpecifierSeqContext declSpecifierSeq() {
      return getRuleContext(DeclSpecifierSeqContext.class, 0);
    }

    public MemberDeclaratorListContext memberDeclaratorList() {
      return getRuleContext(MemberDeclaratorListContext.class, 0);
    }

    public FunctionDefinitionContext functionDefinition() {
      return getRuleContext(FunctionDefinitionContext.class, 0);
    }

    public UsingDeclarationContext usingDeclaration() {
      return getRuleContext(UsingDeclarationContext.class, 0);
    }

    public StaticAssertDeclarationContext staticAssertDeclaration() {
      return getRuleContext(StaticAssertDeclarationContext.class, 0);
    }

    public TemplateDeclarationContext templateDeclaration() {
      return getRuleContext(TemplateDeclarationContext.class, 0);
    }

    public AliasDeclarationContext aliasDeclaration() {
      return getRuleContext(AliasDeclarationContext.class, 0);
    }

    public EmptyDeclarationContext emptyDeclaration() {
      return getRuleContext(EmptyDeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberdeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMemberdeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMemberdeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMemberdeclaration(this);
    }
  }

  public static class MemberDeclaratorListContext extends ParserRuleContext {
    public MemberDeclaratorListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<MemberDeclaratorContext> memberDeclarator() {
      return getRuleContexts(MemberDeclaratorContext.class);
    }

    public MemberDeclaratorContext memberDeclarator(int i) {
      return getRuleContext(MemberDeclaratorContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberDeclaratorList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMemberDeclaratorList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMemberDeclaratorList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMemberDeclaratorList(this);
    }
  }

  public static class MemberDeclaratorContext extends ParserRuleContext {
    public MemberDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public VirtualSpecifierSeqContext virtualSpecifierSeq() {
      return getRuleContext(VirtualSpecifierSeqContext.class, 0);
    }

    public PureSpecifierContext pureSpecifier() {
      return getRuleContext(PureSpecifierContext.class, 0);
    }

    public BraceOrEqualInitializerContext braceOrEqualInitializer() {
      return getRuleContext(BraceOrEqualInitializerContext.class, 0);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memberDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMemberDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMemberDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMemberDeclarator(this);
    }
  }

  public static class VirtualSpecifierSeqContext extends ParserRuleContext {
    public VirtualSpecifierSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<VirtualSpecifierContext> virtualSpecifier() {
      return getRuleContexts(VirtualSpecifierContext.class);
    }

    public VirtualSpecifierContext virtualSpecifier(int i) {
      return getRuleContext(VirtualSpecifierContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_virtualSpecifierSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitVirtualSpecifierSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterVirtualSpecifierSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitVirtualSpecifierSeq(this);
    }
  }

  public static class VirtualSpecifierContext extends ParserRuleContext {
    public VirtualSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Override() {
      return getToken(CPP14Parser.Override, 0);
    }

    public TerminalNode Final() {
      return getToken(CPP14Parser.Final, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_virtualSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitVirtualSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterVirtualSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitVirtualSpecifier(this);
    }
  }

  public static class PureSpecifierContext extends ParserRuleContext {
    public Token val;

    public PureSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public TerminalNode OctalLiteral() {
      return getToken(CPP14Parser.OctalLiteral, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_pureSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitPureSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterPureSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitPureSpecifier(this);
    }
  }

  public static class BaseClauseContext extends ParserRuleContext {
    public BaseClauseContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public BaseSpecifierListContext baseSpecifierList() {
      return getRuleContext(BaseSpecifierListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_baseClause;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBaseClause(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBaseClause(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBaseClause(this);
    }
  }

  public static class BaseSpecifierListContext extends ParserRuleContext {
    public BaseSpecifierListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<BaseSpecifierContext> baseSpecifier() {
      return getRuleContexts(BaseSpecifierContext.class);
    }

    public BaseSpecifierContext baseSpecifier(int i) {
      return getRuleContext(BaseSpecifierContext.class, i);
    }

    public List<TerminalNode> Ellipsis() {
      return getTokens(CPP14Parser.Ellipsis);
    }

    public TerminalNode Ellipsis(int i) {
      return getToken(CPP14Parser.Ellipsis, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_baseSpecifierList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBaseSpecifierList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBaseSpecifierList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBaseSpecifierList(this);
    }
  }

  public static class BaseSpecifierContext extends ParserRuleContext {
    public BaseSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public BaseTypeSpecifierContext baseTypeSpecifier() {
      return getRuleContext(BaseTypeSpecifierContext.class, 0);
    }

    public TerminalNode Virtual() {
      return getToken(CPP14Parser.Virtual, 0);
    }

    public AccessSpecifierContext accessSpecifier() {
      return getRuleContext(AccessSpecifierContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_baseSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBaseSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBaseSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBaseSpecifier(this);
    }
  }

  public static class ClassOrDeclTypeContext extends ParserRuleContext {
    public ClassOrDeclTypeContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassNameContext className() {
      return getRuleContext(ClassNameContext.class, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public DecltypeSpecifierContext decltypeSpecifier() {
      return getRuleContext(DecltypeSpecifierContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_classOrDeclType;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitClassOrDeclType(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterClassOrDeclType(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitClassOrDeclType(this);
    }
  }

  public static class BaseTypeSpecifierContext extends ParserRuleContext {
    public BaseTypeSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassOrDeclTypeContext classOrDeclType() {
      return getRuleContext(ClassOrDeclTypeContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_baseTypeSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitBaseTypeSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterBaseTypeSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitBaseTypeSpecifier(this);
    }
  }

  public static class AccessSpecifierContext extends ParserRuleContext {
    public AccessSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Private() {
      return getToken(CPP14Parser.Private, 0);
    }

    public TerminalNode Protected() {
      return getToken(CPP14Parser.Protected, 0);
    }

    public TerminalNode Public() {
      return getToken(CPP14Parser.Public, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_accessSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitAccessSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterAccessSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitAccessSpecifier(this);
    }
  }

  public static class ConversionFunctionIdContext extends ParserRuleContext {
    public ConversionFunctionIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Operator() {
      return getToken(CPP14Parser.Operator, 0);
    }

    public ConversionTypeIdContext conversionTypeId() {
      return getRuleContext(ConversionTypeIdContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_conversionFunctionId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitConversionFunctionId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterConversionFunctionId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitConversionFunctionId(this);
    }
  }

  public static class ConversionTypeIdContext extends ParserRuleContext {
    public ConversionTypeIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeSpecifierSeqContext typeSpecifierSeq() {
      return getRuleContext(TypeSpecifierSeqContext.class, 0);
    }

    public ConversionDeclaratorContext conversionDeclarator() {
      return getRuleContext(ConversionDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_conversionTypeId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitConversionTypeId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterConversionTypeId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitConversionTypeId(this);
    }
  }

  public static class ConversionDeclaratorContext extends ParserRuleContext {
    public ConversionDeclaratorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public PointerOperatorContext pointerOperator() {
      return getRuleContext(PointerOperatorContext.class, 0);
    }

    public ConversionDeclaratorContext conversionDeclarator() {
      return getRuleContext(ConversionDeclaratorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_conversionDeclarator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitConversionDeclarator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterConversionDeclarator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitConversionDeclarator(this);
    }
  }

  public static class ConstructorInitializerContext extends ParserRuleContext {
    public ConstructorInitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Colon() {
      return getToken(CPP14Parser.Colon, 0);
    }

    public MemInitializerListContext memInitializerList() {
      return getRuleContext(MemInitializerListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_constructorInitializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitConstructorInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterConstructorInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitConstructorInitializer(this);
    }
  }

  public static class MemInitializerListContext extends ParserRuleContext {
    public MemInitializerListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<MemInitializerContext> memInitializer() {
      return getRuleContexts(MemInitializerContext.class);
    }

    public MemInitializerContext memInitializer(int i) {
      return getRuleContext(MemInitializerContext.class, i);
    }

    public List<TerminalNode> Ellipsis() {
      return getTokens(CPP14Parser.Ellipsis);
    }

    public TerminalNode Ellipsis(int i) {
      return getToken(CPP14Parser.Ellipsis, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memInitializerList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMemInitializerList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMemInitializerList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMemInitializerList(this);
    }
  }

  public static class MemInitializerContext extends ParserRuleContext {
    public MemInitializerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public MeminitializeridContext meminitializerid() {
      return getRuleContext(MeminitializeridContext.class, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public BracedInitListContext bracedInitList() {
      return getRuleContext(BracedInitListContext.class, 0);
    }

    public ExpressionListContext expressionList() {
      return getRuleContext(ExpressionListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_memInitializer;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMemInitializer(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMemInitializer(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMemInitializer(this);
    }
  }

  public static class MeminitializeridContext extends ParserRuleContext {
    public MeminitializeridContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public ClassOrDeclTypeContext classOrDeclType() {
      return getRuleContext(ClassOrDeclTypeContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_meminitializerid;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitMeminitializerid(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterMeminitializerid(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitMeminitializerid(this);
    }
  }

  public static class OperatorFunctionIdContext extends ParserRuleContext {
    public OperatorFunctionIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Operator() {
      return getToken(CPP14Parser.Operator, 0);
    }

    public TheOperatorContext theOperator() {
      return getRuleContext(TheOperatorContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_operatorFunctionId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitOperatorFunctionId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterOperatorFunctionId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitOperatorFunctionId(this);
    }
  }

  public static class LiteralOperatorIdContext extends ParserRuleContext {
    public LiteralOperatorIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Operator() {
      return getToken(CPP14Parser.Operator, 0);
    }

    public TerminalNode StringLiteral() {
      return getToken(CPP14Parser.StringLiteral, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public TerminalNode UserDefinedStringLiteral() {
      return getToken(CPP14Parser.UserDefinedStringLiteral, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_literalOperatorId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLiteralOperatorId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLiteralOperatorId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLiteralOperatorId(this);
    }
  }

  public static class TemplateDeclarationContext extends ParserRuleContext {
    public TemplateDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public TerminalNode Less() {
      return getToken(CPP14Parser.Less, 0);
    }

    public TemplateparameterListContext templateparameterList() {
      return getRuleContext(TemplateparameterListContext.class, 0);
    }

    public TerminalNode Greater() {
      return getToken(CPP14Parser.Greater, 0);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateDeclaration(this);
    }
  }

  public static class TemplateparameterListContext extends ParserRuleContext {
    public TemplateparameterListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TemplateParameterContext> templateParameter() {
      return getRuleContexts(TemplateParameterContext.class);
    }

    public TemplateParameterContext templateParameter(int i) {
      return getRuleContext(TemplateParameterContext.class, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateparameterList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateparameterList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateparameterList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateparameterList(this);
    }
  }

  public static class TemplateParameterContext extends ParserRuleContext {
    public TemplateParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeParameterContext typeParameter() {
      return getRuleContext(TypeParameterContext.class, 0);
    }

    public ParameterDeclarationContext parameterDeclaration() {
      return getRuleContext(ParameterDeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateParameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateParameter(this);
    }
  }

  public static class TypeParameterContext extends ParserRuleContext {
    public TypeParameterContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Class() {
      return getToken(CPP14Parser.Class, 0);
    }

    public TerminalNode Typename_() {
      return getToken(CPP14Parser.Typename_, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public TerminalNode Less() {
      return getToken(CPP14Parser.Less, 0);
    }

    public TemplateparameterListContext templateparameterList() {
      return getRuleContext(TemplateparameterListContext.class, 0);
    }

    public TerminalNode Greater() {
      return getToken(CPP14Parser.Greater, 0);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeParameter;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypeParameter(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypeParameter(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypeParameter(this);
    }
  }

  public static class SimpleTemplateIdContext extends ParserRuleContext {
    public SimpleTemplateIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TemplateNameContext templateName() {
      return getRuleContext(TemplateNameContext.class, 0);
    }

    public TerminalNode Less() {
      return getToken(CPP14Parser.Less, 0);
    }

    public TerminalNode Greater() {
      return getToken(CPP14Parser.Greater, 0);
    }

    public TemplateArgumentListContext templateArgumentList() {
      return getRuleContext(TemplateArgumentListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_simpleTemplateId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitSimpleTemplateId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterSimpleTemplateId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitSimpleTemplateId(this);
    }
  }

  public static class TemplateIdContext extends ParserRuleContext {
    public TemplateIdContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    public TerminalNode Less() {
      return getToken(CPP14Parser.Less, 0);
    }

    public TerminalNode Greater() {
      return getToken(CPP14Parser.Greater, 0);
    }

    public OperatorFunctionIdContext operatorFunctionId() {
      return getRuleContext(OperatorFunctionIdContext.class, 0);
    }

    public LiteralOperatorIdContext literalOperatorId() {
      return getRuleContext(LiteralOperatorIdContext.class, 0);
    }

    public TemplateArgumentListContext templateArgumentList() {
      return getRuleContext(TemplateArgumentListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateId;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateId(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateId(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateId(this);
    }
  }

  public static class TemplateNameContext extends ParserRuleContext {
    public TemplateNameContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateName;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateName(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateName(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateName(this);
    }
  }

  public static class TemplateArgumentListContext extends ParserRuleContext {
    public TemplateArgumentListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TemplateArgumentContext> templateArgument() {
      return getRuleContexts(TemplateArgumentContext.class);
    }

    public TemplateArgumentContext templateArgument(int i) {
      return getRuleContext(TemplateArgumentContext.class, i);
    }

    public List<TerminalNode> Ellipsis() {
      return getTokens(CPP14Parser.Ellipsis);
    }

    public TerminalNode Ellipsis(int i) {
      return getToken(CPP14Parser.Ellipsis, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateArgumentList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateArgumentList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateArgumentList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateArgumentList(this);
    }
  }

  public static class TemplateArgumentContext extends ParserRuleContext {
    public TemplateArgumentContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TheTypeIdContext theTypeId() {
      return getRuleContext(TheTypeIdContext.class, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public IdExpressionContext idExpression() {
      return getRuleContext(IdExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_templateArgument;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTemplateArgument(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTemplateArgument(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTemplateArgument(this);
    }
  }

  public static class TypeNameSpecifierContext extends ParserRuleContext {
    public TypeNameSpecifierContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Typename_() {
      return getToken(CPP14Parser.Typename_, 0);
    }

    public NestedNameSpecifierContext nestedNameSpecifier() {
      return getRuleContext(NestedNameSpecifierContext.class, 0);
    }

    public TerminalNode Identifier() {
      return getToken(CPP14Parser.Identifier, 0);
    }

    public SimpleTemplateIdContext simpleTemplateId() {
      return getRuleContext(SimpleTemplateIdContext.class, 0);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeNameSpecifier;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypeNameSpecifier(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypeNameSpecifier(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypeNameSpecifier(this);
    }
  }

  public static class ExplicitInstantiationContext extends ParserRuleContext {
    public ExplicitInstantiationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    public TerminalNode Extern() {
      return getToken(CPP14Parser.Extern, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_explicitInstantiation;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExplicitInstantiation(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExplicitInstantiation(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExplicitInstantiation(this);
    }
  }

  public static class ExplicitSpecializationContext extends ParserRuleContext {
    public ExplicitSpecializationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Template() {
      return getToken(CPP14Parser.Template, 0);
    }

    public TerminalNode Less() {
      return getToken(CPP14Parser.Less, 0);
    }

    public TerminalNode Greater() {
      return getToken(CPP14Parser.Greater, 0);
    }

    public DeclarationContext declaration() {
      return getRuleContext(DeclarationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_explicitSpecialization;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExplicitSpecialization(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExplicitSpecialization(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExplicitSpecialization(this);
    }
  }

  public static class TryBlockContext extends ParserRuleContext {
    public TryBlockContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Try() {
      return getToken(CPP14Parser.Try, 0);
    }

    public CompoundStatementContext compoundStatement() {
      return getRuleContext(CompoundStatementContext.class, 0);
    }

    public HandlerSeqContext handlerSeq() {
      return getRuleContext(HandlerSeqContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_tryBlock;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTryBlock(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTryBlock(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTryBlock(this);
    }
  }

  public static class FunctionTryBlockContext extends ParserRuleContext {
    public FunctionTryBlockContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Try() {
      return getToken(CPP14Parser.Try, 0);
    }

    public CompoundStatementContext compoundStatement() {
      return getRuleContext(CompoundStatementContext.class, 0);
    }

    public HandlerSeqContext handlerSeq() {
      return getRuleContext(HandlerSeqContext.class, 0);
    }

    public ConstructorInitializerContext constructorInitializer() {
      return getRuleContext(ConstructorInitializerContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_functionTryBlock;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitFunctionTryBlock(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterFunctionTryBlock(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitFunctionTryBlock(this);
    }
  }

  public static class HandlerSeqContext extends ParserRuleContext {
    public HandlerSeqContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<HandlerContext> handler() {
      return getRuleContexts(HandlerContext.class);
    }

    public HandlerContext handler(int i) {
      return getRuleContext(HandlerContext.class, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_handlerSeq;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitHandlerSeq(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterHandlerSeq(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitHandlerSeq(this);
    }
  }

  public static class HandlerContext extends ParserRuleContext {
    public HandlerContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Catch() {
      return getToken(CPP14Parser.Catch, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ExceptionDeclarationContext exceptionDeclaration() {
      return getRuleContext(ExceptionDeclarationContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public CompoundStatementContext compoundStatement() {
      return getRuleContext(CompoundStatementContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_handler;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitHandler(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterHandler(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitHandler(this);
    }
  }

  public static class ExceptionDeclarationContext extends ParserRuleContext {
    public ExceptionDeclarationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TypeSpecifierSeqContext typeSpecifierSeq() {
      return getRuleContext(TypeSpecifierSeqContext.class, 0);
    }

    public AttributeSpecifierSeqContext attributeSpecifierSeq() {
      return getRuleContext(AttributeSpecifierSeqContext.class, 0);
    }

    public DeclaratorContext declarator() {
      return getRuleContext(DeclaratorContext.class, 0);
    }

    public AbstractDeclaratorContext abstractDeclarator() {
      return getRuleContext(AbstractDeclaratorContext.class, 0);
    }

    public TerminalNode Ellipsis() {
      return getToken(CPP14Parser.Ellipsis, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_exceptionDeclaration;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExceptionDeclaration(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExceptionDeclaration(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExceptionDeclaration(this);
    }
  }

  public static class ThrowExpressionContext extends ParserRuleContext {
    public ThrowExpressionContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Throw() {
      return getToken(CPP14Parser.Throw, 0);
    }

    public AssignmentExpressionContext assignmentExpression() {
      return getRuleContext(AssignmentExpressionContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_throwExpression;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitThrowExpression(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterThrowExpression(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitThrowExpression(this);
    }
  }

  public static class ExceptionSpecificationContext extends ParserRuleContext {
    public ExceptionSpecificationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public DynamicExceptionSpecificationContext dynamicExceptionSpecification() {
      return getRuleContext(DynamicExceptionSpecificationContext.class, 0);
    }

    public NoeExceptSpecificationContext noeExceptSpecification() {
      return getRuleContext(NoeExceptSpecificationContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_exceptionSpecification;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitExceptionSpecification(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterExceptionSpecification(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitExceptionSpecification(this);
    }
  }

  public static class DynamicExceptionSpecificationContext extends ParserRuleContext {
    public DynamicExceptionSpecificationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Throw() {
      return getToken(CPP14Parser.Throw, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    public TypeIdListContext typeIdList() {
      return getRuleContext(TypeIdListContext.class, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_dynamicExceptionSpecification;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitDynamicExceptionSpecification(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterDynamicExceptionSpecification(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitDynamicExceptionSpecification(this);
    }
  }

  public static class TypeIdListContext extends ParserRuleContext {
    public TypeIdListContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public List<TheTypeIdContext> theTypeId() {
      return getRuleContexts(TheTypeIdContext.class);
    }

    public TheTypeIdContext theTypeId(int i) {
      return getRuleContext(TheTypeIdContext.class, i);
    }

    public List<TerminalNode> Ellipsis() {
      return getTokens(CPP14Parser.Ellipsis);
    }

    public TerminalNode Ellipsis(int i) {
      return getToken(CPP14Parser.Ellipsis, i);
    }

    public List<TerminalNode> Comma() {
      return getTokens(CPP14Parser.Comma);
    }

    public TerminalNode Comma(int i) {
      return getToken(CPP14Parser.Comma, i);
    }

    @Override
    public int getRuleIndex() {
      return RULE_typeIdList;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTypeIdList(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTypeIdList(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTypeIdList(this);
    }
  }

  public static class NoeExceptSpecificationContext extends ParserRuleContext {
    public NoeExceptSpecificationContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode Noexcept() {
      return getToken(CPP14Parser.Noexcept, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public ConstantExpressionContext constantExpression() {
      return getRuleContext(ConstantExpressionContext.class, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_noeExceptSpecification;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitNoeExceptSpecification(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterNoeExceptSpecification(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitNoeExceptSpecification(this);
    }
  }

  public static class TheOperatorContext extends ParserRuleContext {
    public TheOperatorContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode New() {
      return getToken(CPP14Parser.New, 0);
    }

    public TerminalNode LeftBracket() {
      return getToken(CPP14Parser.LeftBracket, 0);
    }

    public TerminalNode RightBracket() {
      return getToken(CPP14Parser.RightBracket, 0);
    }

    public TerminalNode Delete() {
      return getToken(CPP14Parser.Delete, 0);
    }

    public TerminalNode Plus() {
      return getToken(CPP14Parser.Plus, 0);
    }

    public TerminalNode Minus() {
      return getToken(CPP14Parser.Minus, 0);
    }

    public TerminalNode Star() {
      return getToken(CPP14Parser.Star, 0);
    }

    public TerminalNode Div() {
      return getToken(CPP14Parser.Div, 0);
    }

    public TerminalNode Mod() {
      return getToken(CPP14Parser.Mod, 0);
    }

    public TerminalNode Caret() {
      return getToken(CPP14Parser.Caret, 0);
    }

    public TerminalNode And() {
      return getToken(CPP14Parser.And, 0);
    }

    public TerminalNode Or() {
      return getToken(CPP14Parser.Or, 0);
    }

    public TerminalNode Tilde() {
      return getToken(CPP14Parser.Tilde, 0);
    }

    public TerminalNode Not() {
      return getToken(CPP14Parser.Not, 0);
    }

    public TerminalNode Assign() {
      return getToken(CPP14Parser.Assign, 0);
    }

    public List<TerminalNode> Greater() {
      return getTokens(CPP14Parser.Greater);
    }

    public TerminalNode Greater(int i) {
      return getToken(CPP14Parser.Greater, i);
    }

    public List<TerminalNode> Less() {
      return getTokens(CPP14Parser.Less);
    }

    public TerminalNode Less(int i) {
      return getToken(CPP14Parser.Less, i);
    }

    public TerminalNode GreaterEqual() {
      return getToken(CPP14Parser.GreaterEqual, 0);
    }

    public TerminalNode PlusAssign() {
      return getToken(CPP14Parser.PlusAssign, 0);
    }

    public TerminalNode MinusAssign() {
      return getToken(CPP14Parser.MinusAssign, 0);
    }

    public TerminalNode StarAssign() {
      return getToken(CPP14Parser.StarAssign, 0);
    }

    public TerminalNode ModAssign() {
      return getToken(CPP14Parser.ModAssign, 0);
    }

    public TerminalNode XorAssign() {
      return getToken(CPP14Parser.XorAssign, 0);
    }

    public TerminalNode AndAssign() {
      return getToken(CPP14Parser.AndAssign, 0);
    }

    public TerminalNode OrAssign() {
      return getToken(CPP14Parser.OrAssign, 0);
    }

    public TerminalNode RightShiftAssign() {
      return getToken(CPP14Parser.RightShiftAssign, 0);
    }

    public TerminalNode LeftShiftAssign() {
      return getToken(CPP14Parser.LeftShiftAssign, 0);
    }

    public TerminalNode Equal() {
      return getToken(CPP14Parser.Equal, 0);
    }

    public TerminalNode NotEqual() {
      return getToken(CPP14Parser.NotEqual, 0);
    }

    public TerminalNode LessEqual() {
      return getToken(CPP14Parser.LessEqual, 0);
    }

    public TerminalNode AndAnd() {
      return getToken(CPP14Parser.AndAnd, 0);
    }

    public TerminalNode OrOr() {
      return getToken(CPP14Parser.OrOr, 0);
    }

    public TerminalNode PlusPlus() {
      return getToken(CPP14Parser.PlusPlus, 0);
    }

    public TerminalNode MinusMinus() {
      return getToken(CPP14Parser.MinusMinus, 0);
    }

    public TerminalNode Comma() {
      return getToken(CPP14Parser.Comma, 0);
    }

    public TerminalNode ArrowStar() {
      return getToken(CPP14Parser.ArrowStar, 0);
    }

    public TerminalNode Arrow() {
      return getToken(CPP14Parser.Arrow, 0);
    }

    public TerminalNode LeftParen() {
      return getToken(CPP14Parser.LeftParen, 0);
    }

    public TerminalNode RightParen() {
      return getToken(CPP14Parser.RightParen, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_theOperator;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitTheOperator(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterTheOperator(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitTheOperator(this);
    }
  }

  public static class LiteralContext extends ParserRuleContext {
    public LiteralContext(ParserRuleContext parent, int invokingState) {
      super(parent, invokingState);
    }

    public TerminalNode IntegerLiteral() {
      return getToken(CPP14Parser.IntegerLiteral, 0);
    }

    public TerminalNode CharacterLiteral() {
      return getToken(CPP14Parser.CharacterLiteral, 0);
    }

    public TerminalNode FloatingLiteral() {
      return getToken(CPP14Parser.FloatingLiteral, 0);
    }

    public TerminalNode StringLiteral() {
      return getToken(CPP14Parser.StringLiteral, 0);
    }

    public TerminalNode BooleanLiteral() {
      return getToken(CPP14Parser.BooleanLiteral, 0);
    }

    public TerminalNode PointerLiteral() {
      return getToken(CPP14Parser.PointerLiteral, 0);
    }

    public TerminalNode UserDefinedLiteral() {
      return getToken(CPP14Parser.UserDefinedLiteral, 0);
    }

    @Override
    public int getRuleIndex() {
      return RULE_literal;
    }

    @Override
    public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
      if (visitor instanceof CPP14ParserVisitor)
        return ((CPP14ParserVisitor<? extends T>) visitor).visitLiteral(this);
      else return visitor.visitChildren(this);
    }

    @Override
    public void enterRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).enterLiteral(this);
    }

    @Override
    public void exitRule(ParseTreeListener listener) {
      if (listener instanceof CPP14ParserListener)
        ((CPP14ParserListener) listener).exitLiteral(this);
    }
  }
}
