// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.cpp;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CPP14Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CPP14ParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#translationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTranslationUnit(CPP14Parser.TranslationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#primaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimaryExpression(CPP14Parser.PrimaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#idExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpression(CPP14Parser.IdExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unqualifiedId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#qualifiedId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedId(CPP14Parser.QualifiedIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#nestedNameSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNestedNameSpecifier(CPP14Parser.NestedNameSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdaExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaExpression(CPP14Parser.LambdaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdaIntroducer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaIntroducer(CPP14Parser.LambdaIntroducerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdaCapture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaCapture(CPP14Parser.LambdaCaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#captureDefault}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureDefault(CPP14Parser.CaptureDefaultContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#captureList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCaptureList(CPP14Parser.CaptureListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#capture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCapture(CPP14Parser.CaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpleCapture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleCapture(CPP14Parser.SimpleCaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initcapture}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitcapture(CPP14Parser.InitcaptureContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#lambdaDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLambdaDeclarator(CPP14Parser.LambdaDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#postfixExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfixExpression(CPP14Parser.PostfixExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeIdOfTheTypeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeIdOfTheTypeId(CPP14Parser.TypeIdOfTheTypeIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(CPP14Parser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pseudoDestructorName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPseudoDestructorName(CPP14Parser.PseudoDestructorNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unaryExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryExpression(CPP14Parser.UnaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#unaryOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOperator(CPP14Parser.UnaryOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpression(CPP14Parser.NewExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newPlacement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewPlacement(CPP14Parser.NewPlacementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newTypeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewTypeId(CPP14Parser.NewTypeIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewDeclarator(CPP14Parser.NewDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noPointerNewDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoPointerNewDeclarator(CPP14Parser.NoPointerNewDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#newInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewInitializer(CPP14Parser.NewInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#deleteExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteExpression(CPP14Parser.DeleteExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noExceptExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoExceptExpression(CPP14Parser.NoExceptExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#castExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCastExpression(CPP14Parser.CastExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pointerMemberExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerMemberExpression(CPP14Parser.PointerMemberExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(CPP14Parser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#additiveExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(CPP14Parser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#shiftExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftExpression(CPP14Parser.ShiftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#shiftOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftOperator(CPP14Parser.ShiftOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#relationalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(CPP14Parser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#equalityExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(CPP14Parser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#andExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(CPP14Parser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExclusiveOrExpression(CPP14Parser.ExclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInclusiveOrExpression(CPP14Parser.InclusiveOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#logicalAndExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(CPP14Parser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#logicalOrExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(CPP14Parser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conditionalExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionalExpression(CPP14Parser.ConditionalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#assignmentExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpression(CPP14Parser.AssignmentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(CPP14Parser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(CPP14Parser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(CPP14Parser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(CPP14Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#labeledStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabeledStatement(CPP14Parser.LabeledStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(CPP14Parser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(CPP14Parser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#statementSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementSeq(CPP14Parser.StatementSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#selectionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectionStatement(CPP14Parser.SelectionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(CPP14Parser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#iterationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterationStatement(CPP14Parser.IterationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#forInitStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInitStatement(CPP14Parser.ForInitStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#forRangeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForRangeDeclaration(CPP14Parser.ForRangeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#forRangeInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForRangeInitializer(CPP14Parser.ForRangeInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#jumpStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJumpStatement(CPP14Parser.JumpStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationStatement(CPP14Parser.DeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declarationseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarationseq(CPP14Parser.DeclarationseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(CPP14Parser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#blockDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockDeclaration(CPP14Parser.BlockDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#aliasDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAliasDeclaration(CPP14Parser.AliasDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpleDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStaticAssertDeclaration(CPP14Parser.StaticAssertDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#emptyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyDeclaration(CPP14Parser.EmptyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeDeclaration(CPP14Parser.AttributeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclSpecifier(CPP14Parser.DeclSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declSpecifierSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclSpecifierSeq(CPP14Parser.DeclSpecifierSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStorageClassSpecifier(CPP14Parser.StorageClassSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functionSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionSpecifier(CPP14Parser.FunctionSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typedefName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypedefName(CPP14Parser.TypedefNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifier(CPP14Parser.TypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#trailingTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailingTypeSpecifier(CPP14Parser.TrailingTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeSpecifierSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSpecifierSeq(CPP14Parser.TypeSpecifierSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#trailingTypeSpecifierSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailingTypeSpecifierSeq(CPP14Parser.TrailingTypeSpecifierSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpleTypeLengthModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeLengthModifier(CPP14Parser.SimpleTypeLengthModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpleTypeSignednessModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeSignednessModifier(CPP14Parser.SimpleTypeSignednessModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpleTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTypeSpecifier(CPP14Parser.SimpleTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#theTypeName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTheTypeName(CPP14Parser.TheTypeNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#decltypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecltypeSpecifier(CPP14Parser.DecltypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#elaboratedTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElaboratedTypeSpecifier(CPP14Parser.ElaboratedTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumName(CPP14Parser.EnumNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumSpecifier(CPP14Parser.EnumSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumHead(CPP14Parser.EnumHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#opaqueEnumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOpaqueEnumDeclaration(CPP14Parser.OpaqueEnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumkey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumkey(CPP14Parser.EnumkeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumbase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumbase(CPP14Parser.EnumbaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumeratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeratorList(CPP14Parser.EnumeratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumeratorDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumeratorDefinition(CPP14Parser.EnumeratorDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#enumerator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumerator(CPP14Parser.EnumeratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespaceName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceName(CPP14Parser.NamespaceNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#originalNamespaceName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOriginalNamespaceName(CPP14Parser.OriginalNamespaceNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespaceDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceDefinition(CPP14Parser.NamespaceDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespaceAlias}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceAlias(CPP14Parser.NamespaceAliasContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#namespaceAliasDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespaceAliasDefinition(CPP14Parser.NamespaceAliasDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#qualifiednamespacespecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiednamespacespecifier(CPP14Parser.QualifiednamespacespecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#usingDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsingDeclaration(CPP14Parser.UsingDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#usingDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUsingDirective(CPP14Parser.UsingDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#asmDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsmDefinition(CPP14Parser.AsmDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#linkageSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLinkageSpecification(CPP14Parser.LinkageSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeSpecifierSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeSpecifierSeq(CPP14Parser.AttributeSpecifierSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeSpecifier(CPP14Parser.AttributeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#alignmentspecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlignmentspecifier(CPP14Parser.AlignmentspecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeList(CPP14Parser.AttributeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute(CPP14Parser.AttributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeNamespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeNamespace(CPP14Parser.AttributeNamespaceContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#attributeArgumentClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttributeArgumentClause(CPP14Parser.AttributeArgumentClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#balancedTokenSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBalancedTokenSeq(CPP14Parser.BalancedTokenSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#balancedtoken}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBalancedtoken(CPP14Parser.BalancedtokenContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initDeclaratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclaratorList(CPP14Parser.InitDeclaratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitDeclarator(CPP14Parser.InitDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclarator(CPP14Parser.DeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pointerDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerDeclarator(CPP14Parser.PointerDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noPointerDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoPointerDeclarator(CPP14Parser.NoPointerDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parametersAndQualifiers}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#trailingReturnType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrailingReturnType(CPP14Parser.TrailingReturnTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pointerOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerOperator(CPP14Parser.PointerOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#cvqualifierseq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCvqualifierseq(CPP14Parser.CvqualifierseqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#cvQualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCvQualifier(CPP14Parser.CvQualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#refqualifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefqualifier(CPP14Parser.RefqualifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#declaratorid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaratorid(CPP14Parser.DeclaratoridContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#theTypeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTheTypeId(CPP14Parser.TheTypeIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#abstractDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstractDeclarator(CPP14Parser.AbstractDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pointerAbstractDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPointerAbstractDeclarator(CPP14Parser.PointerAbstractDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noPointerAbstractDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoPointerAbstractDeclarator(CPP14Parser.NoPointerAbstractDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#abstractPackDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstractPackDeclarator(CPP14Parser.AbstractPackDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noPointerAbstractPackDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoPointerAbstractPackDeclarator(CPP14Parser.NoPointerAbstractPackDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parameterDeclarationClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclarationClause(CPP14Parser.ParameterDeclarationClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclarationList(CPP14Parser.ParameterDeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#parameterDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameterDeclaration(CPP14Parser.ParameterDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functionDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functionBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionBody(CPP14Parser.FunctionBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializer(CPP14Parser.InitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#braceOrEqualInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBraceOrEqualInitializer(CPP14Parser.BraceOrEqualInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initializerClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializerClause(CPP14Parser.InitializerClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#initializerList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInitializerList(CPP14Parser.InitializerListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#bracedInitList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracedInitList(CPP14Parser.BracedInitListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#className}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassName(CPP14Parser.ClassNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassSpecifier(CPP14Parser.ClassSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassHead(CPP14Parser.ClassHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classHeadName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassHeadName(CPP14Parser.ClassHeadNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classVirtSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassVirtSpecifier(CPP14Parser.ClassVirtSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classKey}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassKey(CPP14Parser.ClassKeyContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberSpecification(CPP14Parser.MemberSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberDeclaratorList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaratorList(CPP14Parser.MemberDeclaratorListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memberDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclarator(CPP14Parser.MemberDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#virtualSpecifierSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtualSpecifierSeq(CPP14Parser.VirtualSpecifierSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#virtualSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVirtualSpecifier(CPP14Parser.VirtualSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#pureSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPureSpecifier(CPP14Parser.PureSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#baseClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseClause(CPP14Parser.BaseClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#baseSpecifierList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseSpecifierList(CPP14Parser.BaseSpecifierListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#baseSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseSpecifier(CPP14Parser.BaseSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#classOrDeclType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrDeclType(CPP14Parser.ClassOrDeclTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#baseTypeSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBaseTypeSpecifier(CPP14Parser.BaseTypeSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#accessSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccessSpecifier(CPP14Parser.AccessSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conversionFunctionId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversionFunctionId(CPP14Parser.ConversionFunctionIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conversionTypeId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversionTypeId(CPP14Parser.ConversionTypeIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#conversionDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConversionDeclarator(CPP14Parser.ConversionDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#constructorInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorInitializer(CPP14Parser.ConstructorInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memInitializerList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemInitializerList(CPP14Parser.MemInitializerListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#memInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemInitializer(CPP14Parser.MemInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#meminitializerid}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMeminitializerid(CPP14Parser.MeminitializeridContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#operatorFunctionId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorFunctionId(CPP14Parser.OperatorFunctionIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#literalOperatorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralOperatorId(CPP14Parser.LiteralOperatorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateDeclaration(CPP14Parser.TemplateDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateparameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateparameterList(CPP14Parser.TemplateparameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateParameter(CPP14Parser.TemplateParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameter(CPP14Parser.TypeParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#simpleTemplateId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTemplateId(CPP14Parser.SimpleTemplateIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateId(CPP14Parser.TemplateIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateName(CPP14Parser.TemplateNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateArgumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateArgumentList(CPP14Parser.TemplateArgumentListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#templateArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateArgument(CPP14Parser.TemplateArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeNameSpecifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNameSpecifier(CPP14Parser.TypeNameSpecifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#explicitInstantiation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitInstantiation(CPP14Parser.ExplicitInstantiationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#explicitSpecialization}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitSpecialization(CPP14Parser.ExplicitSpecializationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#tryBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryBlock(CPP14Parser.TryBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#functionTryBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionTryBlock(CPP14Parser.FunctionTryBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#handlerSeq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandlerSeq(CPP14Parser.HandlerSeqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#handler}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHandler(CPP14Parser.HandlerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#exceptionDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionDeclaration(CPP14Parser.ExceptionDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#throwExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrowExpression(CPP14Parser.ThrowExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#exceptionSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExceptionSpecification(CPP14Parser.ExceptionSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#dynamicExceptionSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDynamicExceptionSpecification(CPP14Parser.DynamicExceptionSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#typeIdList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeIdList(CPP14Parser.TypeIdListContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#noeExceptSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNoeExceptSpecification(CPP14Parser.NoeExceptSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#theOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTheOperator(CPP14Parser.TheOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CPP14Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(CPP14Parser.LiteralContext ctx);
}