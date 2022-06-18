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
// Generated from JavaParser.g4 by ANTLR 4.9.2
package com.itsaky.androidide.lexers.java;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced by {@link
 * JavaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for operations with no return
 *     type.
 */
public interface JavaParserVisitor<T> extends ParseTreeVisitor<T> {
  /**
   * Visit a parse tree produced by {@link JavaParser#compilationUnit}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCompilationUnit(JavaParser.CompilationUnitContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#packageDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPackageDeclaration(JavaParser.PackageDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#importDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitImportDeclaration(JavaParser.ImportDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeDeclaration(JavaParser.TypeDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#modifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitModifier(JavaParser.ModifierContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classOrInterfaceModifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassOrInterfaceModifier(JavaParser.ClassOrInterfaceModifierContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#variableModifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitVariableModifier(JavaParser.VariableModifierContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassDeclaration(JavaParser.ClassDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeParameters}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeParameters(JavaParser.TypeParametersContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeParameter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeParameter(JavaParser.TypeParameterContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeBound}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeBound(JavaParser.TypeBoundContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#enumDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEnumDeclaration(JavaParser.EnumDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#enumConstants}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEnumConstants(JavaParser.EnumConstantsContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#enumConstant}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEnumConstant(JavaParser.EnumConstantContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#enumBodyDeclarations}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEnumBodyDeclarations(JavaParser.EnumBodyDeclarationsContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#interfaceDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInterfaceDeclaration(JavaParser.InterfaceDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classBody}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassBody(JavaParser.ClassBodyContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#interfaceBody}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInterfaceBody(JavaParser.InterfaceBodyContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classBodyDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassBodyDeclaration(JavaParser.ClassBodyDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#memberDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMemberDeclaration(JavaParser.MemberDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#methodDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMethodDeclaration(JavaParser.MethodDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#methodBody}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMethodBody(JavaParser.MethodBodyContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeTypeOrVoid}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeTypeOrVoid(JavaParser.TypeTypeOrVoidContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#genericMethodDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGenericMethodDeclaration(JavaParser.GenericMethodDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#genericConstructorDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGenericConstructorDeclaration(JavaParser.GenericConstructorDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#constructorDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConstructorDeclaration(JavaParser.ConstructorDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#fieldDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFieldDeclaration(JavaParser.FieldDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#interfaceBodyDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInterfaceBodyDeclaration(JavaParser.InterfaceBodyDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#interfaceMemberDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInterfaceMemberDeclaration(JavaParser.InterfaceMemberDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#constDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConstDeclaration(JavaParser.ConstDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#constantDeclarator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitConstantDeclarator(JavaParser.ConstantDeclaratorContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#interfaceMethodDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInterfaceMethodDeclaration(JavaParser.InterfaceMethodDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#interfaceMethodModifier}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInterfaceMethodModifier(JavaParser.InterfaceMethodModifierContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#genericInterfaceMethodDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitGenericInterfaceMethodDeclaration(JavaParser.GenericInterfaceMethodDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#variableDeclarators}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitVariableDeclarators(JavaParser.VariableDeclaratorsContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#variableDeclarator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitVariableDeclarator(JavaParser.VariableDeclaratorContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#variableDeclaratorId}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitVariableDeclaratorId(JavaParser.VariableDeclaratorIdContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#variableInitializer}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitVariableInitializer(JavaParser.VariableInitializerContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#arrayInitializer}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitArrayInitializer(JavaParser.ArrayInitializerContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classOrInterfaceType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassOrInterfaceType(JavaParser.ClassOrInterfaceTypeContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeArgument}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeArgument(JavaParser.TypeArgumentContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#qualifiedNameList}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQualifiedNameList(JavaParser.QualifiedNameListContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#formalParameters}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFormalParameters(JavaParser.FormalParametersContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#formalParameterList}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFormalParameterList(JavaParser.FormalParameterListContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#formalParameter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFormalParameter(JavaParser.FormalParameterContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#lastFormalParameter}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLastFormalParameter(JavaParser.LastFormalParameterContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#qualifiedName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitQualifiedName(JavaParser.QualifiedNameContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#literal}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLiteral(JavaParser.LiteralContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#integerLiteral}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitIntegerLiteral(JavaParser.IntegerLiteralContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#floatLiteral}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFloatLiteral(JavaParser.FloatLiteralContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#altAnnotationQualifiedName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAltAnnotationQualifiedName(JavaParser.AltAnnotationQualifiedNameContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotation}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotation(JavaParser.AnnotationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#elementValuePairs}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitElementValuePairs(JavaParser.ElementValuePairsContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#elementValuePair}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitElementValuePair(JavaParser.ElementValuePairContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#elementValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitElementValue(JavaParser.ElementValueContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#elementValueArrayInitializer}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitElementValueArrayInitializer(JavaParser.ElementValueArrayInitializerContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationTypeDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationTypeDeclaration(JavaParser.AnnotationTypeDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationTypeBody}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationTypeBody(JavaParser.AnnotationTypeBodyContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationTypeElementDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationTypeElementDeclaration(JavaParser.AnnotationTypeElementDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationTypeElementRest}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationTypeElementRest(JavaParser.AnnotationTypeElementRestContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationMethodOrConstantRest}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationMethodOrConstantRest(JavaParser.AnnotationMethodOrConstantRestContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationMethodRest}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationMethodRest(JavaParser.AnnotationMethodRestContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#annotationConstantRest}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitAnnotationConstantRest(JavaParser.AnnotationConstantRestContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#defaultValue}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitDefaultValue(JavaParser.DefaultValueContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#block}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlock(JavaParser.BlockContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#blockStatement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitBlockStatement(JavaParser.BlockStatementContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#localVariableDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLocalVariableDeclaration(JavaParser.LocalVariableDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#localTypeDeclaration}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLocalTypeDeclaration(JavaParser.LocalTypeDeclarationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#statement}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitStatement(JavaParser.StatementContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#catchClause}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCatchClause(JavaParser.CatchClauseContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#catchType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCatchType(JavaParser.CatchTypeContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#finallyBlock}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitFinallyBlock(JavaParser.FinallyBlockContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#resourceSpecification}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitResourceSpecification(JavaParser.ResourceSpecificationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#resources}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitResources(JavaParser.ResourcesContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#resource}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitResource(JavaParser.ResourceContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#switchBlockStatementGroup}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSwitchBlockStatementGroup(JavaParser.SwitchBlockStatementGroupContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#switchLabel}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSwitchLabel(JavaParser.SwitchLabelContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#forControl}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitForControl(JavaParser.ForControlContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#forInit}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitForInit(JavaParser.ForInitContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#enhancedForControl}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitEnhancedForControl(JavaParser.EnhancedForControlContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#parExpression}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitParExpression(JavaParser.ParExpressionContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#expressionList}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExpressionList(JavaParser.ExpressionListContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#methodCall}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitMethodCall(JavaParser.MethodCallContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#expression}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExpression(JavaParser.ExpressionContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#lambdaExpression}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLambdaExpression(JavaParser.LambdaExpressionContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#lambdaParameters}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLambdaParameters(JavaParser.LambdaParametersContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#lambdaBody}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitLambdaBody(JavaParser.LambdaBodyContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#primary}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPrimary(JavaParser.PrimaryContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassType(JavaParser.ClassTypeContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#creator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCreator(JavaParser.CreatorContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#createdName}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitCreatedName(JavaParser.CreatedNameContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#innerCreator}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitInnerCreator(JavaParser.InnerCreatorContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#arrayCreatorRest}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitArrayCreatorRest(JavaParser.ArrayCreatorRestContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#classCreatorRest}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitClassCreatorRest(JavaParser.ClassCreatorRestContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#explicitGenericInvocation}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExplicitGenericInvocation(JavaParser.ExplicitGenericInvocationContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeArgumentsOrDiamond}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeArgumentsOrDiamond(JavaParser.TypeArgumentsOrDiamondContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#nonWildcardTypeArgumentsOrDiamond}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonWildcardTypeArgumentsOrDiamond(JavaParser.NonWildcardTypeArgumentsOrDiamondContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#nonWildcardTypeArguments}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitNonWildcardTypeArguments(JavaParser.NonWildcardTypeArgumentsContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeList}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeList(JavaParser.TypeListContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeType(JavaParser.TypeTypeContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#primitiveType}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitPrimitiveType(JavaParser.PrimitiveTypeContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#typeArguments}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitTypeArguments(JavaParser.TypeArgumentsContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#superSuffix}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitSuperSuffix(JavaParser.SuperSuffixContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#explicitGenericInvocationSuffix}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitExplicitGenericInvocationSuffix(JavaParser.ExplicitGenericInvocationSuffixContext ctx);
  /**
   * Visit a parse tree produced by {@link JavaParser#arguments}.
   *
   * @param ctx the parse tree
   * @return the visitor result
   */
  T visitArguments(JavaParser.ArgumentsContext ctx);
}
