// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.cpp;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CPP14Parser}.
 */
public interface CPP14ParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#translationUnit}.
	 * @param ctx the parse tree
	 */
	void enterTranslationUnit(CPP14Parser.TranslationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#translationUnit}.
	 * @param ctx the parse tree
	 */
	void exitTranslationUnit(CPP14Parser.TranslationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(CPP14Parser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(CPP14Parser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#idExpression}.
	 * @param ctx the parse tree
	 */
	void enterIdExpression(CPP14Parser.IdExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#idExpression}.
	 * @param ctx the parse tree
	 */
	void exitIdExpression(CPP14Parser.IdExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#unqualifiedId}.
	 * @param ctx the parse tree
	 */
	void enterUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#unqualifiedId}.
	 * @param ctx the parse tree
	 */
	void exitUnqualifiedId(CPP14Parser.UnqualifiedIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#qualifiedId}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedId(CPP14Parser.QualifiedIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#qualifiedId}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedId(CPP14Parser.QualifiedIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#nestedNameSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterNestedNameSpecifier(CPP14Parser.NestedNameSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#nestedNameSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitNestedNameSpecifier(CPP14Parser.NestedNameSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpression(CPP14Parser.LambdaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#lambdaExpression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpression(CPP14Parser.LambdaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#lambdaIntroducer}.
	 * @param ctx the parse tree
	 */
	void enterLambdaIntroducer(CPP14Parser.LambdaIntroducerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#lambdaIntroducer}.
	 * @param ctx the parse tree
	 */
	void exitLambdaIntroducer(CPP14Parser.LambdaIntroducerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#lambdaCapture}.
	 * @param ctx the parse tree
	 */
	void enterLambdaCapture(CPP14Parser.LambdaCaptureContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#lambdaCapture}.
	 * @param ctx the parse tree
	 */
	void exitLambdaCapture(CPP14Parser.LambdaCaptureContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#captureDefault}.
	 * @param ctx the parse tree
	 */
	void enterCaptureDefault(CPP14Parser.CaptureDefaultContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#captureDefault}.
	 * @param ctx the parse tree
	 */
	void exitCaptureDefault(CPP14Parser.CaptureDefaultContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#captureList}.
	 * @param ctx the parse tree
	 */
	void enterCaptureList(CPP14Parser.CaptureListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#captureList}.
	 * @param ctx the parse tree
	 */
	void exitCaptureList(CPP14Parser.CaptureListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#capture}.
	 * @param ctx the parse tree
	 */
	void enterCapture(CPP14Parser.CaptureContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#capture}.
	 * @param ctx the parse tree
	 */
	void exitCapture(CPP14Parser.CaptureContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#simpleCapture}.
	 * @param ctx the parse tree
	 */
	void enterSimpleCapture(CPP14Parser.SimpleCaptureContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#simpleCapture}.
	 * @param ctx the parse tree
	 */
	void exitSimpleCapture(CPP14Parser.SimpleCaptureContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#initcapture}.
	 * @param ctx the parse tree
	 */
	void enterInitcapture(CPP14Parser.InitcaptureContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#initcapture}.
	 * @param ctx the parse tree
	 */
	void exitInitcapture(CPP14Parser.InitcaptureContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#lambdaDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterLambdaDeclarator(CPP14Parser.LambdaDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#lambdaDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitLambdaDeclarator(CPP14Parser.LambdaDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpression(CPP14Parser.PostfixExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#postfixExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpression(CPP14Parser.PostfixExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typeIdOfTheTypeId}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdOfTheTypeId(CPP14Parser.TypeIdOfTheTypeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typeIdOfTheTypeId}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdOfTheTypeId(CPP14Parser.TypeIdOfTheTypeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(CPP14Parser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(CPP14Parser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#pseudoDestructorName}.
	 * @param ctx the parse tree
	 */
	void enterPseudoDestructorName(CPP14Parser.PseudoDestructorNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#pseudoDestructorName}.
	 * @param ctx the parse tree
	 */
	void exitPseudoDestructorName(CPP14Parser.PseudoDestructorNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryExpression(CPP14Parser.UnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#unaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryExpression(CPP14Parser.UnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOperator(CPP14Parser.UnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#unaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOperator(CPP14Parser.UnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#newExpression}.
	 * @param ctx the parse tree
	 */
	void enterNewExpression(CPP14Parser.NewExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#newExpression}.
	 * @param ctx the parse tree
	 */
	void exitNewExpression(CPP14Parser.NewExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#newPlacement}.
	 * @param ctx the parse tree
	 */
	void enterNewPlacement(CPP14Parser.NewPlacementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#newPlacement}.
	 * @param ctx the parse tree
	 */
	void exitNewPlacement(CPP14Parser.NewPlacementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#newTypeId}.
	 * @param ctx the parse tree
	 */
	void enterNewTypeId(CPP14Parser.NewTypeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#newTypeId}.
	 * @param ctx the parse tree
	 */
	void exitNewTypeId(CPP14Parser.NewTypeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#newDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterNewDeclarator(CPP14Parser.NewDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#newDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitNewDeclarator(CPP14Parser.NewDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#noPointerNewDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterNoPointerNewDeclarator(CPP14Parser.NoPointerNewDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#noPointerNewDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitNoPointerNewDeclarator(CPP14Parser.NoPointerNewDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#newInitializer}.
	 * @param ctx the parse tree
	 */
	void enterNewInitializer(CPP14Parser.NewInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#newInitializer}.
	 * @param ctx the parse tree
	 */
	void exitNewInitializer(CPP14Parser.NewInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#deleteExpression}.
	 * @param ctx the parse tree
	 */
	void enterDeleteExpression(CPP14Parser.DeleteExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#deleteExpression}.
	 * @param ctx the parse tree
	 */
	void exitDeleteExpression(CPP14Parser.DeleteExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#noExceptExpression}.
	 * @param ctx the parse tree
	 */
	void enterNoExceptExpression(CPP14Parser.NoExceptExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#noExceptExpression}.
	 * @param ctx the parse tree
	 */
	void exitNoExceptExpression(CPP14Parser.NoExceptExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#castExpression}.
	 * @param ctx the parse tree
	 */
	void enterCastExpression(CPP14Parser.CastExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#castExpression}.
	 * @param ctx the parse tree
	 */
	void exitCastExpression(CPP14Parser.CastExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#pointerMemberExpression}.
	 * @param ctx the parse tree
	 */
	void enterPointerMemberExpression(CPP14Parser.PointerMemberExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#pointerMemberExpression}.
	 * @param ctx the parse tree
	 */
	void exitPointerMemberExpression(CPP14Parser.PointerMemberExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(CPP14Parser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(CPP14Parser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(CPP14Parser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(CPP14Parser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void enterShiftExpression(CPP14Parser.ShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#shiftExpression}.
	 * @param ctx the parse tree
	 */
	void exitShiftExpression(CPP14Parser.ShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#shiftOperator}.
	 * @param ctx the parse tree
	 */
	void enterShiftOperator(CPP14Parser.ShiftOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#shiftOperator}.
	 * @param ctx the parse tree
	 */
	void exitShiftOperator(CPP14Parser.ShiftOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(CPP14Parser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#relationalExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(CPP14Parser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(CPP14Parser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#equalityExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(CPP14Parser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#andExpression}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(CPP14Parser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#andExpression}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(CPP14Parser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterExclusiveOrExpression(CPP14Parser.ExclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#exclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitExclusiveOrExpression(CPP14Parser.ExclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterInclusiveOrExpression(CPP14Parser.InclusiveOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#inclusiveOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitInclusiveOrExpression(CPP14Parser.InclusiveOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpression(CPP14Parser.LogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#logicalAndExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpression(CPP14Parser.LogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpression(CPP14Parser.LogicalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#logicalOrExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpression(CPP14Parser.LogicalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void enterConditionalExpression(CPP14Parser.ConditionalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#conditionalExpression}.
	 * @param ctx the parse tree
	 */
	void exitConditionalExpression(CPP14Parser.ConditionalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpression(CPP14Parser.AssignmentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#assignmentExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpression(CPP14Parser.AssignmentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(CPP14Parser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(CPP14Parser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CPP14Parser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CPP14Parser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(CPP14Parser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(CPP14Parser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(CPP14Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(CPP14Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void enterLabeledStatement(CPP14Parser.LabeledStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#labeledStatement}.
	 * @param ctx the parse tree
	 */
	void exitLabeledStatement(CPP14Parser.LabeledStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(CPP14Parser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(CPP14Parser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void enterCompoundStatement(CPP14Parser.CompoundStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#compoundStatement}.
	 * @param ctx the parse tree
	 */
	void exitCompoundStatement(CPP14Parser.CompoundStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#statementSeq}.
	 * @param ctx the parse tree
	 */
	void enterStatementSeq(CPP14Parser.StatementSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#statementSeq}.
	 * @param ctx the parse tree
	 */
	void exitStatementSeq(CPP14Parser.StatementSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void enterSelectionStatement(CPP14Parser.SelectionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#selectionStatement}.
	 * @param ctx the parse tree
	 */
	void exitSelectionStatement(CPP14Parser.SelectionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#condition}.
	 * @param ctx the parse tree
	 */
	void enterCondition(CPP14Parser.ConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#condition}.
	 * @param ctx the parse tree
	 */
	void exitCondition(CPP14Parser.ConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void enterIterationStatement(CPP14Parser.IterationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#iterationStatement}.
	 * @param ctx the parse tree
	 */
	void exitIterationStatement(CPP14Parser.IterationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#forInitStatement}.
	 * @param ctx the parse tree
	 */
	void enterForInitStatement(CPP14Parser.ForInitStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#forInitStatement}.
	 * @param ctx the parse tree
	 */
	void exitForInitStatement(CPP14Parser.ForInitStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#forRangeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterForRangeDeclaration(CPP14Parser.ForRangeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#forRangeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitForRangeDeclaration(CPP14Parser.ForRangeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#forRangeInitializer}.
	 * @param ctx the parse tree
	 */
	void enterForRangeInitializer(CPP14Parser.ForRangeInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#forRangeInitializer}.
	 * @param ctx the parse tree
	 */
	void exitForRangeInitializer(CPP14Parser.ForRangeInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void enterJumpStatement(CPP14Parser.JumpStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#jumpStatement}.
	 * @param ctx the parse tree
	 */
	void exitJumpStatement(CPP14Parser.JumpStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationStatement(CPP14Parser.DeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationStatement(CPP14Parser.DeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declarationseq}.
	 * @param ctx the parse tree
	 */
	void enterDeclarationseq(CPP14Parser.DeclarationseqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declarationseq}.
	 * @param ctx the parse tree
	 */
	void exitDeclarationseq(CPP14Parser.DeclarationseqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(CPP14Parser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(CPP14Parser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#blockDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterBlockDeclaration(CPP14Parser.BlockDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#blockDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitBlockDeclaration(CPP14Parser.BlockDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#aliasDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAliasDeclaration(CPP14Parser.AliasDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#aliasDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAliasDeclaration(CPP14Parser.AliasDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#simpleDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#simpleDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitSimpleDeclaration(CPP14Parser.SimpleDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterStaticAssertDeclaration(CPP14Parser.StaticAssertDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#staticAssertDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitStaticAssertDeclaration(CPP14Parser.StaticAssertDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#emptyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEmptyDeclaration(CPP14Parser.EmptyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#emptyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEmptyDeclaration(CPP14Parser.EmptyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attributeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAttributeDeclaration(CPP14Parser.AttributeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attributeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAttributeDeclaration(CPP14Parser.AttributeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterDeclSpecifier(CPP14Parser.DeclSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitDeclSpecifier(CPP14Parser.DeclSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterDeclSpecifierSeq(CPP14Parser.DeclSpecifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitDeclSpecifierSeq(CPP14Parser.DeclSpecifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterStorageClassSpecifier(CPP14Parser.StorageClassSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#storageClassSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitStorageClassSpecifier(CPP14Parser.StorageClassSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#functionSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionSpecifier(CPP14Parser.FunctionSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#functionSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionSpecifier(CPP14Parser.FunctionSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typedefName}.
	 * @param ctx the parse tree
	 */
	void enterTypedefName(CPP14Parser.TypedefNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typedefName}.
	 * @param ctx the parse tree
	 */
	void exitTypedefName(CPP14Parser.TypedefNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifier(CPP14Parser.TypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifier(CPP14Parser.TypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#trailingTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTrailingTypeSpecifier(CPP14Parser.TrailingTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#trailingTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTrailingTypeSpecifier(CPP14Parser.TrailingTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typeSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterTypeSpecifierSeq(CPP14Parser.TypeSpecifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typeSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitTypeSpecifierSeq(CPP14Parser.TypeSpecifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#trailingTypeSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterTrailingTypeSpecifierSeq(CPP14Parser.TrailingTypeSpecifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#trailingTypeSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitTrailingTypeSpecifierSeq(CPP14Parser.TrailingTypeSpecifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#simpleTypeLengthModifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeLengthModifier(CPP14Parser.SimpleTypeLengthModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#simpleTypeLengthModifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeLengthModifier(CPP14Parser.SimpleTypeLengthModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#simpleTypeSignednessModifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeSignednessModifier(CPP14Parser.SimpleTypeSignednessModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#simpleTypeSignednessModifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeSignednessModifier(CPP14Parser.SimpleTypeSignednessModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#simpleTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTypeSpecifier(CPP14Parser.SimpleTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#simpleTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTypeSpecifier(CPP14Parser.SimpleTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#theTypeName}.
	 * @param ctx the parse tree
	 */
	void enterTheTypeName(CPP14Parser.TheTypeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#theTypeName}.
	 * @param ctx the parse tree
	 */
	void exitTheTypeName(CPP14Parser.TheTypeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#decltypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterDecltypeSpecifier(CPP14Parser.DecltypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#decltypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitDecltypeSpecifier(CPP14Parser.DecltypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#elaboratedTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterElaboratedTypeSpecifier(CPP14Parser.ElaboratedTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#elaboratedTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitElaboratedTypeSpecifier(CPP14Parser.ElaboratedTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumName}.
	 * @param ctx the parse tree
	 */
	void enterEnumName(CPP14Parser.EnumNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumName}.
	 * @param ctx the parse tree
	 */
	void exitEnumName(CPP14Parser.EnumNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterEnumSpecifier(CPP14Parser.EnumSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitEnumSpecifier(CPP14Parser.EnumSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumHead}.
	 * @param ctx the parse tree
	 */
	void enterEnumHead(CPP14Parser.EnumHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumHead}.
	 * @param ctx the parse tree
	 */
	void exitEnumHead(CPP14Parser.EnumHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#opaqueEnumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterOpaqueEnumDeclaration(CPP14Parser.OpaqueEnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#opaqueEnumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitOpaqueEnumDeclaration(CPP14Parser.OpaqueEnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumkey}.
	 * @param ctx the parse tree
	 */
	void enterEnumkey(CPP14Parser.EnumkeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumkey}.
	 * @param ctx the parse tree
	 */
	void exitEnumkey(CPP14Parser.EnumkeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumbase}.
	 * @param ctx the parse tree
	 */
	void enterEnumbase(CPP14Parser.EnumbaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumbase}.
	 * @param ctx the parse tree
	 */
	void exitEnumbase(CPP14Parser.EnumbaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumeratorList}.
	 * @param ctx the parse tree
	 */
	void enterEnumeratorList(CPP14Parser.EnumeratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumeratorList}.
	 * @param ctx the parse tree
	 */
	void exitEnumeratorList(CPP14Parser.EnumeratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumeratorDefinition}.
	 * @param ctx the parse tree
	 */
	void enterEnumeratorDefinition(CPP14Parser.EnumeratorDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumeratorDefinition}.
	 * @param ctx the parse tree
	 */
	void exitEnumeratorDefinition(CPP14Parser.EnumeratorDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#enumerator}.
	 * @param ctx the parse tree
	 */
	void enterEnumerator(CPP14Parser.EnumeratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#enumerator}.
	 * @param ctx the parse tree
	 */
	void exitEnumerator(CPP14Parser.EnumeratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#namespaceName}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceName(CPP14Parser.NamespaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#namespaceName}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceName(CPP14Parser.NamespaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#originalNamespaceName}.
	 * @param ctx the parse tree
	 */
	void enterOriginalNamespaceName(CPP14Parser.OriginalNamespaceNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#originalNamespaceName}.
	 * @param ctx the parse tree
	 */
	void exitOriginalNamespaceName(CPP14Parser.OriginalNamespaceNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#namespaceDefinition}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceDefinition(CPP14Parser.NamespaceDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#namespaceDefinition}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceDefinition(CPP14Parser.NamespaceDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#namespaceAlias}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceAlias(CPP14Parser.NamespaceAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#namespaceAlias}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceAlias(CPP14Parser.NamespaceAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#namespaceAliasDefinition}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceAliasDefinition(CPP14Parser.NamespaceAliasDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#namespaceAliasDefinition}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceAliasDefinition(CPP14Parser.NamespaceAliasDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#qualifiednamespacespecifier}.
	 * @param ctx the parse tree
	 */
	void enterQualifiednamespacespecifier(CPP14Parser.QualifiednamespacespecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#qualifiednamespacespecifier}.
	 * @param ctx the parse tree
	 */
	void exitQualifiednamespacespecifier(CPP14Parser.QualifiednamespacespecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#usingDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterUsingDeclaration(CPP14Parser.UsingDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#usingDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitUsingDeclaration(CPP14Parser.UsingDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#usingDirective}.
	 * @param ctx the parse tree
	 */
	void enterUsingDirective(CPP14Parser.UsingDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#usingDirective}.
	 * @param ctx the parse tree
	 */
	void exitUsingDirective(CPP14Parser.UsingDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#asmDefinition}.
	 * @param ctx the parse tree
	 */
	void enterAsmDefinition(CPP14Parser.AsmDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#asmDefinition}.
	 * @param ctx the parse tree
	 */
	void exitAsmDefinition(CPP14Parser.AsmDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#linkageSpecification}.
	 * @param ctx the parse tree
	 */
	void enterLinkageSpecification(CPP14Parser.LinkageSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#linkageSpecification}.
	 * @param ctx the parse tree
	 */
	void exitLinkageSpecification(CPP14Parser.LinkageSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attributeSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSpecifierSeq(CPP14Parser.AttributeSpecifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attributeSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSpecifierSeq(CPP14Parser.AttributeSpecifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attributeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSpecifier(CPP14Parser.AttributeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attributeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSpecifier(CPP14Parser.AttributeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#alignmentspecifier}.
	 * @param ctx the parse tree
	 */
	void enterAlignmentspecifier(CPP14Parser.AlignmentspecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#alignmentspecifier}.
	 * @param ctx the parse tree
	 */
	void exitAlignmentspecifier(CPP14Parser.AlignmentspecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attributeList}.
	 * @param ctx the parse tree
	 */
	void enterAttributeList(CPP14Parser.AttributeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attributeList}.
	 * @param ctx the parse tree
	 */
	void exitAttributeList(CPP14Parser.AttributeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(CPP14Parser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(CPP14Parser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attributeNamespace}.
	 * @param ctx the parse tree
	 */
	void enterAttributeNamespace(CPP14Parser.AttributeNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attributeNamespace}.
	 * @param ctx the parse tree
	 */
	void exitAttributeNamespace(CPP14Parser.AttributeNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#attributeArgumentClause}.
	 * @param ctx the parse tree
	 */
	void enterAttributeArgumentClause(CPP14Parser.AttributeArgumentClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#attributeArgumentClause}.
	 * @param ctx the parse tree
	 */
	void exitAttributeArgumentClause(CPP14Parser.AttributeArgumentClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#balancedTokenSeq}.
	 * @param ctx the parse tree
	 */
	void enterBalancedTokenSeq(CPP14Parser.BalancedTokenSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#balancedTokenSeq}.
	 * @param ctx the parse tree
	 */
	void exitBalancedTokenSeq(CPP14Parser.BalancedTokenSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#balancedtoken}.
	 * @param ctx the parse tree
	 */
	void enterBalancedtoken(CPP14Parser.BalancedtokenContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#balancedtoken}.
	 * @param ctx the parse tree
	 */
	void exitBalancedtoken(CPP14Parser.BalancedtokenContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#initDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterInitDeclaratorList(CPP14Parser.InitDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#initDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitInitDeclaratorList(CPP14Parser.InitDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#initDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterInitDeclarator(CPP14Parser.InitDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#initDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitInitDeclarator(CPP14Parser.InitDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declarator}.
	 * @param ctx the parse tree
	 */
	void enterDeclarator(CPP14Parser.DeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declarator}.
	 * @param ctx the parse tree
	 */
	void exitDeclarator(CPP14Parser.DeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#pointerDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterPointerDeclarator(CPP14Parser.PointerDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#pointerDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitPointerDeclarator(CPP14Parser.PointerDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#noPointerDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterNoPointerDeclarator(CPP14Parser.NoPointerDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#noPointerDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitNoPointerDeclarator(CPP14Parser.NoPointerDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#parametersAndQualifiers}.
	 * @param ctx the parse tree
	 */
	void enterParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#parametersAndQualifiers}.
	 * @param ctx the parse tree
	 */
	void exitParametersAndQualifiers(CPP14Parser.ParametersAndQualifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#trailingReturnType}.
	 * @param ctx the parse tree
	 */
	void enterTrailingReturnType(CPP14Parser.TrailingReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#trailingReturnType}.
	 * @param ctx the parse tree
	 */
	void exitTrailingReturnType(CPP14Parser.TrailingReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#pointerOperator}.
	 * @param ctx the parse tree
	 */
	void enterPointerOperator(CPP14Parser.PointerOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#pointerOperator}.
	 * @param ctx the parse tree
	 */
	void exitPointerOperator(CPP14Parser.PointerOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#cvqualifierseq}.
	 * @param ctx the parse tree
	 */
	void enterCvqualifierseq(CPP14Parser.CvqualifierseqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#cvqualifierseq}.
	 * @param ctx the parse tree
	 */
	void exitCvqualifierseq(CPP14Parser.CvqualifierseqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#cvQualifier}.
	 * @param ctx the parse tree
	 */
	void enterCvQualifier(CPP14Parser.CvQualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#cvQualifier}.
	 * @param ctx the parse tree
	 */
	void exitCvQualifier(CPP14Parser.CvQualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#refqualifier}.
	 * @param ctx the parse tree
	 */
	void enterRefqualifier(CPP14Parser.RefqualifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#refqualifier}.
	 * @param ctx the parse tree
	 */
	void exitRefqualifier(CPP14Parser.RefqualifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#declaratorid}.
	 * @param ctx the parse tree
	 */
	void enterDeclaratorid(CPP14Parser.DeclaratoridContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#declaratorid}.
	 * @param ctx the parse tree
	 */
	void exitDeclaratorid(CPP14Parser.DeclaratoridContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#theTypeId}.
	 * @param ctx the parse tree
	 */
	void enterTheTypeId(CPP14Parser.TheTypeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#theTypeId}.
	 * @param ctx the parse tree
	 */
	void exitTheTypeId(CPP14Parser.TheTypeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#abstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterAbstractDeclarator(CPP14Parser.AbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#abstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitAbstractDeclarator(CPP14Parser.AbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#pointerAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterPointerAbstractDeclarator(CPP14Parser.PointerAbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#pointerAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitPointerAbstractDeclarator(CPP14Parser.PointerAbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#noPointerAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterNoPointerAbstractDeclarator(CPP14Parser.NoPointerAbstractDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#noPointerAbstractDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitNoPointerAbstractDeclarator(CPP14Parser.NoPointerAbstractDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#abstractPackDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterAbstractPackDeclarator(CPP14Parser.AbstractPackDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#abstractPackDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitAbstractPackDeclarator(CPP14Parser.AbstractPackDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#noPointerAbstractPackDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterNoPointerAbstractPackDeclarator(CPP14Parser.NoPointerAbstractPackDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#noPointerAbstractPackDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitNoPointerAbstractPackDeclarator(CPP14Parser.NoPointerAbstractPackDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#parameterDeclarationClause}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclarationClause(CPP14Parser.ParameterDeclarationClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#parameterDeclarationClause}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclarationClause(CPP14Parser.ParameterDeclarationClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclarationList(CPP14Parser.ParameterDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#parameterDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclarationList(CPP14Parser.ParameterDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterParameterDeclaration(CPP14Parser.ParameterDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#parameterDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitParameterDeclaration(CPP14Parser.ParameterDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#functionDefinition}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDefinition(CPP14Parser.FunctionDefinitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(CPP14Parser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(CPP14Parser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#initializer}.
	 * @param ctx the parse tree
	 */
	void enterInitializer(CPP14Parser.InitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#initializer}.
	 * @param ctx the parse tree
	 */
	void exitInitializer(CPP14Parser.InitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#braceOrEqualInitializer}.
	 * @param ctx the parse tree
	 */
	void enterBraceOrEqualInitializer(CPP14Parser.BraceOrEqualInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#braceOrEqualInitializer}.
	 * @param ctx the parse tree
	 */
	void exitBraceOrEqualInitializer(CPP14Parser.BraceOrEqualInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#initializerClause}.
	 * @param ctx the parse tree
	 */
	void enterInitializerClause(CPP14Parser.InitializerClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#initializerClause}.
	 * @param ctx the parse tree
	 */
	void exitInitializerClause(CPP14Parser.InitializerClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#initializerList}.
	 * @param ctx the parse tree
	 */
	void enterInitializerList(CPP14Parser.InitializerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#initializerList}.
	 * @param ctx the parse tree
	 */
	void exitInitializerList(CPP14Parser.InitializerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#bracedInitList}.
	 * @param ctx the parse tree
	 */
	void enterBracedInitList(CPP14Parser.BracedInitListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#bracedInitList}.
	 * @param ctx the parse tree
	 */
	void exitBracedInitList(CPP14Parser.BracedInitListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#className}.
	 * @param ctx the parse tree
	 */
	void enterClassName(CPP14Parser.ClassNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#className}.
	 * @param ctx the parse tree
	 */
	void exitClassName(CPP14Parser.ClassNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#classSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterClassSpecifier(CPP14Parser.ClassSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#classSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitClassSpecifier(CPP14Parser.ClassSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#classHead}.
	 * @param ctx the parse tree
	 */
	void enterClassHead(CPP14Parser.ClassHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#classHead}.
	 * @param ctx the parse tree
	 */
	void exitClassHead(CPP14Parser.ClassHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#classHeadName}.
	 * @param ctx the parse tree
	 */
	void enterClassHeadName(CPP14Parser.ClassHeadNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#classHeadName}.
	 * @param ctx the parse tree
	 */
	void exitClassHeadName(CPP14Parser.ClassHeadNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#classVirtSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterClassVirtSpecifier(CPP14Parser.ClassVirtSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#classVirtSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitClassVirtSpecifier(CPP14Parser.ClassVirtSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#classKey}.
	 * @param ctx the parse tree
	 */
	void enterClassKey(CPP14Parser.ClassKeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#classKey}.
	 * @param ctx the parse tree
	 */
	void exitClassKey(CPP14Parser.ClassKeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#memberSpecification}.
	 * @param ctx the parse tree
	 */
	void enterMemberSpecification(CPP14Parser.MemberSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#memberSpecification}.
	 * @param ctx the parse tree
	 */
	void exitMemberSpecification(CPP14Parser.MemberSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#memberdeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#memberDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaratorList(CPP14Parser.MemberDeclaratorListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#memberDeclaratorList}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaratorList(CPP14Parser.MemberDeclaratorListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#memberDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclarator(CPP14Parser.MemberDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#memberDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclarator(CPP14Parser.MemberDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#virtualSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void enterVirtualSpecifierSeq(CPP14Parser.VirtualSpecifierSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#virtualSpecifierSeq}.
	 * @param ctx the parse tree
	 */
	void exitVirtualSpecifierSeq(CPP14Parser.VirtualSpecifierSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#virtualSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterVirtualSpecifier(CPP14Parser.VirtualSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#virtualSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitVirtualSpecifier(CPP14Parser.VirtualSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#pureSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterPureSpecifier(CPP14Parser.PureSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#pureSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitPureSpecifier(CPP14Parser.PureSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#baseClause}.
	 * @param ctx the parse tree
	 */
	void enterBaseClause(CPP14Parser.BaseClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#baseClause}.
	 * @param ctx the parse tree
	 */
	void exitBaseClause(CPP14Parser.BaseClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#baseSpecifierList}.
	 * @param ctx the parse tree
	 */
	void enterBaseSpecifierList(CPP14Parser.BaseSpecifierListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#baseSpecifierList}.
	 * @param ctx the parse tree
	 */
	void exitBaseSpecifierList(CPP14Parser.BaseSpecifierListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#baseSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterBaseSpecifier(CPP14Parser.BaseSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#baseSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitBaseSpecifier(CPP14Parser.BaseSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#classOrDeclType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrDeclType(CPP14Parser.ClassOrDeclTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#classOrDeclType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrDeclType(CPP14Parser.ClassOrDeclTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#baseTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterBaseTypeSpecifier(CPP14Parser.BaseTypeSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#baseTypeSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitBaseTypeSpecifier(CPP14Parser.BaseTypeSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#accessSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAccessSpecifier(CPP14Parser.AccessSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#accessSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAccessSpecifier(CPP14Parser.AccessSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#conversionFunctionId}.
	 * @param ctx the parse tree
	 */
	void enterConversionFunctionId(CPP14Parser.ConversionFunctionIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#conversionFunctionId}.
	 * @param ctx the parse tree
	 */
	void exitConversionFunctionId(CPP14Parser.ConversionFunctionIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#conversionTypeId}.
	 * @param ctx the parse tree
	 */
	void enterConversionTypeId(CPP14Parser.ConversionTypeIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#conversionTypeId}.
	 * @param ctx the parse tree
	 */
	void exitConversionTypeId(CPP14Parser.ConversionTypeIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#conversionDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterConversionDeclarator(CPP14Parser.ConversionDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#conversionDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitConversionDeclarator(CPP14Parser.ConversionDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#constructorInitializer}.
	 * @param ctx the parse tree
	 */
	void enterConstructorInitializer(CPP14Parser.ConstructorInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#constructorInitializer}.
	 * @param ctx the parse tree
	 */
	void exitConstructorInitializer(CPP14Parser.ConstructorInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#memInitializerList}.
	 * @param ctx the parse tree
	 */
	void enterMemInitializerList(CPP14Parser.MemInitializerListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#memInitializerList}.
	 * @param ctx the parse tree
	 */
	void exitMemInitializerList(CPP14Parser.MemInitializerListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#memInitializer}.
	 * @param ctx the parse tree
	 */
	void enterMemInitializer(CPP14Parser.MemInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#memInitializer}.
	 * @param ctx the parse tree
	 */
	void exitMemInitializer(CPP14Parser.MemInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#meminitializerid}.
	 * @param ctx the parse tree
	 */
	void enterMeminitializerid(CPP14Parser.MeminitializeridContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#meminitializerid}.
	 * @param ctx the parse tree
	 */
	void exitMeminitializerid(CPP14Parser.MeminitializeridContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#operatorFunctionId}.
	 * @param ctx the parse tree
	 */
	void enterOperatorFunctionId(CPP14Parser.OperatorFunctionIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#operatorFunctionId}.
	 * @param ctx the parse tree
	 */
	void exitOperatorFunctionId(CPP14Parser.OperatorFunctionIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#literalOperatorId}.
	 * @param ctx the parse tree
	 */
	void enterLiteralOperatorId(CPP14Parser.LiteralOperatorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#literalOperatorId}.
	 * @param ctx the parse tree
	 */
	void exitLiteralOperatorId(CPP14Parser.LiteralOperatorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTemplateDeclaration(CPP14Parser.TemplateDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTemplateDeclaration(CPP14Parser.TemplateDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateparameterList}.
	 * @param ctx the parse tree
	 */
	void enterTemplateparameterList(CPP14Parser.TemplateparameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateparameterList}.
	 * @param ctx the parse tree
	 */
	void exitTemplateparameterList(CPP14Parser.TemplateparameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateParameter}.
	 * @param ctx the parse tree
	 */
	void enterTemplateParameter(CPP14Parser.TemplateParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateParameter}.
	 * @param ctx the parse tree
	 */
	void exitTemplateParameter(CPP14Parser.TemplateParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(CPP14Parser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(CPP14Parser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#simpleTemplateId}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTemplateId(CPP14Parser.SimpleTemplateIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#simpleTemplateId}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTemplateId(CPP14Parser.SimpleTemplateIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateId}.
	 * @param ctx the parse tree
	 */
	void enterTemplateId(CPP14Parser.TemplateIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateId}.
	 * @param ctx the parse tree
	 */
	void exitTemplateId(CPP14Parser.TemplateIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateName}.
	 * @param ctx the parse tree
	 */
	void enterTemplateName(CPP14Parser.TemplateNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateName}.
	 * @param ctx the parse tree
	 */
	void exitTemplateName(CPP14Parser.TemplateNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateArgumentList}.
	 * @param ctx the parse tree
	 */
	void enterTemplateArgumentList(CPP14Parser.TemplateArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateArgumentList}.
	 * @param ctx the parse tree
	 */
	void exitTemplateArgumentList(CPP14Parser.TemplateArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#templateArgument}.
	 * @param ctx the parse tree
	 */
	void enterTemplateArgument(CPP14Parser.TemplateArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#templateArgument}.
	 * @param ctx the parse tree
	 */
	void exitTemplateArgument(CPP14Parser.TemplateArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typeNameSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeNameSpecifier(CPP14Parser.TypeNameSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typeNameSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeNameSpecifier(CPP14Parser.TypeNameSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#explicitInstantiation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitInstantiation(CPP14Parser.ExplicitInstantiationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#explicitInstantiation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitInstantiation(CPP14Parser.ExplicitInstantiationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#explicitSpecialization}.
	 * @param ctx the parse tree
	 */
	void enterExplicitSpecialization(CPP14Parser.ExplicitSpecializationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#explicitSpecialization}.
	 * @param ctx the parse tree
	 */
	void exitExplicitSpecialization(CPP14Parser.ExplicitSpecializationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#tryBlock}.
	 * @param ctx the parse tree
	 */
	void enterTryBlock(CPP14Parser.TryBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#tryBlock}.
	 * @param ctx the parse tree
	 */
	void exitTryBlock(CPP14Parser.TryBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#functionTryBlock}.
	 * @param ctx the parse tree
	 */
	void enterFunctionTryBlock(CPP14Parser.FunctionTryBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#functionTryBlock}.
	 * @param ctx the parse tree
	 */
	void exitFunctionTryBlock(CPP14Parser.FunctionTryBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#handlerSeq}.
	 * @param ctx the parse tree
	 */
	void enterHandlerSeq(CPP14Parser.HandlerSeqContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#handlerSeq}.
	 * @param ctx the parse tree
	 */
	void exitHandlerSeq(CPP14Parser.HandlerSeqContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#handler}.
	 * @param ctx the parse tree
	 */
	void enterHandler(CPP14Parser.HandlerContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#handler}.
	 * @param ctx the parse tree
	 */
	void exitHandler(CPP14Parser.HandlerContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#exceptionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterExceptionDeclaration(CPP14Parser.ExceptionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#exceptionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitExceptionDeclaration(CPP14Parser.ExceptionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#throwExpression}.
	 * @param ctx the parse tree
	 */
	void enterThrowExpression(CPP14Parser.ThrowExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#throwExpression}.
	 * @param ctx the parse tree
	 */
	void exitThrowExpression(CPP14Parser.ThrowExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#exceptionSpecification}.
	 * @param ctx the parse tree
	 */
	void enterExceptionSpecification(CPP14Parser.ExceptionSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#exceptionSpecification}.
	 * @param ctx the parse tree
	 */
	void exitExceptionSpecification(CPP14Parser.ExceptionSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#dynamicExceptionSpecification}.
	 * @param ctx the parse tree
	 */
	void enterDynamicExceptionSpecification(CPP14Parser.DynamicExceptionSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#dynamicExceptionSpecification}.
	 * @param ctx the parse tree
	 */
	void exitDynamicExceptionSpecification(CPP14Parser.DynamicExceptionSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#typeIdList}.
	 * @param ctx the parse tree
	 */
	void enterTypeIdList(CPP14Parser.TypeIdListContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#typeIdList}.
	 * @param ctx the parse tree
	 */
	void exitTypeIdList(CPP14Parser.TypeIdListContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#noeExceptSpecification}.
	 * @param ctx the parse tree
	 */
	void enterNoeExceptSpecification(CPP14Parser.NoeExceptSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#noeExceptSpecification}.
	 * @param ctx the parse tree
	 */
	void exitNoeExceptSpecification(CPP14Parser.NoeExceptSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#theOperator}.
	 * @param ctx the parse tree
	 */
	void enterTheOperator(CPP14Parser.TheOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#theOperator}.
	 * @param ctx the parse tree
	 */
	void exitTheOperator(CPP14Parser.TheOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link CPP14Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(CPP14Parser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link CPP14Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(CPP14Parser.LiteralContext ctx);
}