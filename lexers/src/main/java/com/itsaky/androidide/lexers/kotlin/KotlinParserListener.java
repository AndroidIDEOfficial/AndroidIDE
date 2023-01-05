// Generated from java-escape by ANTLR 4.11.1
package com.itsaky.androidide.lexers.kotlin;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link KotlinParser}.
 */
public interface KotlinParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 */
	void enterKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#kotlinFile}.
	 * @param ctx the parse tree
	 */
	void exitKotlinFile(KotlinParser.KotlinFileContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#script}.
	 * @param ctx the parse tree
	 */
	void enterScript(KotlinParser.ScriptContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#script}.
	 * @param ctx the parse tree
	 */
	void exitScript(KotlinParser.ScriptContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#fileAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterFileAnnotation(KotlinParser.FileAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#fileAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitFileAnnotation(KotlinParser.FileAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#packageHeader}.
	 * @param ctx the parse tree
	 */
	void enterPackageHeader(KotlinParser.PackageHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#packageHeader}.
	 * @param ctx the parse tree
	 */
	void exitPackageHeader(KotlinParser.PackageHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#importList}.
	 * @param ctx the parse tree
	 */
	void enterImportList(KotlinParser.ImportListContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#importList}.
	 * @param ctx the parse tree
	 */
	void exitImportList(KotlinParser.ImportListContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#importHeader}.
	 * @param ctx the parse tree
	 */
	void enterImportHeader(KotlinParser.ImportHeaderContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#importHeader}.
	 * @param ctx the parse tree
	 */
	void exitImportHeader(KotlinParser.ImportHeaderContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#importAlias}.
	 * @param ctx the parse tree
	 */
	void enterImportAlias(KotlinParser.ImportAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#importAlias}.
	 * @param ctx the parse tree
	 */
	void exitImportAlias(KotlinParser.ImportAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#topLevelObject}.
	 * @param ctx the parse tree
	 */
	void enterTopLevelObject(KotlinParser.TopLevelObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#topLevelObject}.
	 * @param ctx the parse tree
	 */
	void exitTopLevelObject(KotlinParser.TopLevelObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(KotlinParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(KotlinParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#primaryConstructor}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryConstructor(KotlinParser.PrimaryConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#primaryConstructor}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryConstructor(KotlinParser.PrimaryConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classParameters}.
	 * @param ctx the parse tree
	 */
	void enterClassParameters(KotlinParser.ClassParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classParameters}.
	 * @param ctx the parse tree
	 */
	void exitClassParameters(KotlinParser.ClassParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classParameter}.
	 * @param ctx the parse tree
	 */
	void enterClassParameter(KotlinParser.ClassParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classParameter}.
	 * @param ctx the parse tree
	 */
	void exitClassParameter(KotlinParser.ClassParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#delegationSpecifiers}.
	 * @param ctx the parse tree
	 */
	void enterDelegationSpecifiers(KotlinParser.DelegationSpecifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#delegationSpecifiers}.
	 * @param ctx the parse tree
	 */
	void exitDelegationSpecifiers(KotlinParser.DelegationSpecifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#annotatedDelegationSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedDelegationSpecifier(KotlinParser.AnnotatedDelegationSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#annotatedDelegationSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedDelegationSpecifier(KotlinParser.AnnotatedDelegationSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#delegationSpecifier}.
	 * @param ctx the parse tree
	 */
	void enterDelegationSpecifier(KotlinParser.DelegationSpecifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#delegationSpecifier}.
	 * @param ctx the parse tree
	 */
	void exitDelegationSpecifier(KotlinParser.DelegationSpecifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#constructorInvocation}.
	 * @param ctx the parse tree
	 */
	void enterConstructorInvocation(KotlinParser.ConstructorInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#constructorInvocation}.
	 * @param ctx the parse tree
	 */
	void exitConstructorInvocation(KotlinParser.ConstructorInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#explicitDelegation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitDelegation(KotlinParser.ExplicitDelegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#explicitDelegation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitDelegation(KotlinParser.ExplicitDelegationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(KotlinParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(KotlinParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classMemberDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterClassMemberDeclarations(KotlinParser.ClassMemberDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classMemberDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitClassMemberDeclarations(KotlinParser.ClassMemberDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassMemberDeclaration(KotlinParser.ClassMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassMemberDeclaration(KotlinParser.ClassMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#anonymousInitializer}.
	 * @param ctx the parse tree
	 */
	void enterAnonymousInitializer(KotlinParser.AnonymousInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#anonymousInitializer}.
	 * @param ctx the parse tree
	 */
	void exitAnonymousInitializer(KotlinParser.AnonymousInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#secondaryConstructor}.
	 * @param ctx the parse tree
	 */
	void enterSecondaryConstructor(KotlinParser.SecondaryConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#secondaryConstructor}.
	 * @param ctx the parse tree
	 */
	void exitSecondaryConstructor(KotlinParser.SecondaryConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#constructorDelegationCall}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDelegationCall(KotlinParser.ConstructorDelegationCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#constructorDelegationCall}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDelegationCall(KotlinParser.ConstructorDelegationCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#enumClassBody}.
	 * @param ctx the parse tree
	 */
	void enterEnumClassBody(KotlinParser.EnumClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#enumClassBody}.
	 * @param ctx the parse tree
	 */
	void exitEnumClassBody(KotlinParser.EnumClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#enumEntries}.
	 * @param ctx the parse tree
	 */
	void enterEnumEntries(KotlinParser.EnumEntriesContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#enumEntries}.
	 * @param ctx the parse tree
	 */
	void exitEnumEntries(KotlinParser.EnumEntriesContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#enumEntry}.
	 * @param ctx the parse tree
	 */
	void enterEnumEntry(KotlinParser.EnumEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#enumEntry}.
	 * @param ctx the parse tree
	 */
	void exitEnumEntry(KotlinParser.EnumEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(KotlinParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(KotlinParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionValueParameters}.
	 * @param ctx the parse tree
	 */
	void enterFunctionValueParameters(KotlinParser.FunctionValueParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionValueParameters}.
	 * @param ctx the parse tree
	 */
	void exitFunctionValueParameters(KotlinParser.FunctionValueParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionValueParameter}.
	 * @param ctx the parse tree
	 */
	void enterFunctionValueParameter(KotlinParser.FunctionValueParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionValueParameter}.
	 * @param ctx the parse tree
	 */
	void exitFunctionValueParameter(KotlinParser.FunctionValueParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(KotlinParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(KotlinParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#setterParameter}.
	 * @param ctx the parse tree
	 */
	void enterSetterParameter(KotlinParser.SetterParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#setterParameter}.
	 * @param ctx the parse tree
	 */
	void exitSetterParameter(KotlinParser.SetterParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void enterFunctionBody(KotlinParser.FunctionBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionBody}.
	 * @param ctx the parse tree
	 */
	void exitFunctionBody(KotlinParser.FunctionBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#objectDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterObjectDeclaration(KotlinParser.ObjectDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#objectDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitObjectDeclaration(KotlinParser.ObjectDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#companionObject}.
	 * @param ctx the parse tree
	 */
	void enterCompanionObject(KotlinParser.CompanionObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#companionObject}.
	 * @param ctx the parse tree
	 */
	void exitCompanionObject(KotlinParser.CompanionObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#propertyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPropertyDeclaration(KotlinParser.PropertyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#propertyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPropertyDeclaration(KotlinParser.PropertyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMultiVariableDeclaration(KotlinParser.MultiVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMultiVariableDeclaration(KotlinParser.MultiVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(KotlinParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(KotlinParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#propertyDelegate}.
	 * @param ctx the parse tree
	 */
	void enterPropertyDelegate(KotlinParser.PropertyDelegateContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#propertyDelegate}.
	 * @param ctx the parse tree
	 */
	void exitPropertyDelegate(KotlinParser.PropertyDelegateContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#getter}.
	 * @param ctx the parse tree
	 */
	void enterGetter(KotlinParser.GetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#getter}.
	 * @param ctx the parse tree
	 */
	void exitGetter(KotlinParser.GetterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#setter}.
	 * @param ctx the parse tree
	 */
	void enterSetter(KotlinParser.SetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#setter}.
	 * @param ctx the parse tree
	 */
	void exitSetter(KotlinParser.SetterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeAlias}.
	 * @param ctx the parse tree
	 */
	void enterTypeAlias(KotlinParser.TypeAliasContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeAlias}.
	 * @param ctx the parse tree
	 */
	void exitTypeAlias(KotlinParser.TypeAliasContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameters(KotlinParser.TypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameters(KotlinParser.TypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(KotlinParser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(KotlinParser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeParameterModifiers}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameterModifiers(KotlinParser.TypeParameterModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeParameterModifiers}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameterModifiers(KotlinParser.TypeParameterModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeParameterModifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameterModifier(KotlinParser.TypeParameterModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeParameterModifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameterModifier(KotlinParser.TypeParameterModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#type_}.
	 * @param ctx the parse tree
	 */
	void enterType_(KotlinParser.Type_Context ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#type_}.
	 * @param ctx the parse tree
	 */
	void exitType_(KotlinParser.Type_Context ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeModifiers}.
	 * @param ctx the parse tree
	 */
	void enterTypeModifiers(KotlinParser.TypeModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeModifiers}.
	 * @param ctx the parse tree
	 */
	void exitTypeModifiers(KotlinParser.TypeModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeModifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeModifier(KotlinParser.TypeModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeModifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeModifier(KotlinParser.TypeModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#parenthesizedType}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedType(KotlinParser.ParenthesizedTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#parenthesizedType}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedType(KotlinParser.ParenthesizedTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#nullableType}.
	 * @param ctx the parse tree
	 */
	void enterNullableType(KotlinParser.NullableTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#nullableType}.
	 * @param ctx the parse tree
	 */
	void exitNullableType(KotlinParser.NullableTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void enterTypeReference(KotlinParser.TypeReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeReference}.
	 * @param ctx the parse tree
	 */
	void exitTypeReference(KotlinParser.TypeReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionType}.
	 * @param ctx the parse tree
	 */
	void enterFunctionType(KotlinParser.FunctionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionType}.
	 * @param ctx the parse tree
	 */
	void exitFunctionType(KotlinParser.FunctionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#receiverType}.
	 * @param ctx the parse tree
	 */
	void enterReceiverType(KotlinParser.ReceiverTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#receiverType}.
	 * @param ctx the parse tree
	 */
	void exitReceiverType(KotlinParser.ReceiverTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#userType}.
	 * @param ctx the parse tree
	 */
	void enterUserType(KotlinParser.UserTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#userType}.
	 * @param ctx the parse tree
	 */
	void exitUserType(KotlinParser.UserTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#parenthesizedUserType}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedUserType(KotlinParser.ParenthesizedUserTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#parenthesizedUserType}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedUserType(KotlinParser.ParenthesizedUserTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#simpleUserType}.
	 * @param ctx the parse tree
	 */
	void enterSimpleUserType(KotlinParser.SimpleUserTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#simpleUserType}.
	 * @param ctx the parse tree
	 */
	void exitSimpleUserType(KotlinParser.SimpleUserTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionTypeParameters}.
	 * @param ctx the parse tree
	 */
	void enterFunctionTypeParameters(KotlinParser.FunctionTypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionTypeParameters}.
	 * @param ctx the parse tree
	 */
	void exitFunctionTypeParameters(KotlinParser.FunctionTypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeConstraints}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstraints(KotlinParser.TypeConstraintsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeConstraints}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstraints(KotlinParser.TypeConstraintsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeConstraint}.
	 * @param ctx the parse tree
	 */
	void enterTypeConstraint(KotlinParser.TypeConstraintContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeConstraint}.
	 * @param ctx the parse tree
	 */
	void exitTypeConstraint(KotlinParser.TypeConstraintContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(KotlinParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(KotlinParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#statements}.
	 * @param ctx the parse tree
	 */
	void enterStatements(KotlinParser.StatementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#statements}.
	 * @param ctx the parse tree
	 */
	void exitStatements(KotlinParser.StatementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(KotlinParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(KotlinParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(KotlinParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(KotlinParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#assignment}.
	 * @param ctx the parse tree
	 */
	void enterAssignment(KotlinParser.AssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#assignment}.
	 * @param ctx the parse tree
	 */
	void exitAssignment(KotlinParser.AssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(KotlinParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(KotlinParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(KotlinParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#disjunction}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(KotlinParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(KotlinParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#conjunction}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(KotlinParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#equality}.
	 * @param ctx the parse tree
	 */
	void enterEquality(KotlinParser.EqualityContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#equality}.
	 * @param ctx the parse tree
	 */
	void exitEquality(KotlinParser.EqualityContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(KotlinParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(KotlinParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#infixOperation}.
	 * @param ctx the parse tree
	 */
	void enterInfixOperation(KotlinParser.InfixOperationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#infixOperation}.
	 * @param ctx the parse tree
	 */
	void exitInfixOperation(KotlinParser.InfixOperationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#elvisExpression}.
	 * @param ctx the parse tree
	 */
	void enterElvisExpression(KotlinParser.ElvisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#elvisExpression}.
	 * @param ctx the parse tree
	 */
	void exitElvisExpression(KotlinParser.ElvisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#infixFunctionCall}.
	 * @param ctx the parse tree
	 */
	void enterInfixFunctionCall(KotlinParser.InfixFunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#infixFunctionCall}.
	 * @param ctx the parse tree
	 */
	void exitInfixFunctionCall(KotlinParser.InfixFunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#rangeExpression}.
	 * @param ctx the parse tree
	 */
	void enterRangeExpression(KotlinParser.RangeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#rangeExpression}.
	 * @param ctx the parse tree
	 */
	void exitRangeExpression(KotlinParser.RangeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(KotlinParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#additiveExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(KotlinParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(KotlinParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiplicativeExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(KotlinParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#asExpression}.
	 * @param ctx the parse tree
	 */
	void enterAsExpression(KotlinParser.AsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#asExpression}.
	 * @param ctx the parse tree
	 */
	void exitAsExpression(KotlinParser.AsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#prefixUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixUnaryExpression(KotlinParser.PrefixUnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#prefixUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixUnaryExpression(KotlinParser.PrefixUnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#unaryPrefix}.
	 * @param ctx the parse tree
	 */
	void enterUnaryPrefix(KotlinParser.UnaryPrefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#unaryPrefix}.
	 * @param ctx the parse tree
	 */
	void exitUnaryPrefix(KotlinParser.UnaryPrefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#postfixUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixUnaryExpression(KotlinParser.PostfixUnaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#postfixUnaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixUnaryExpression(KotlinParser.PostfixUnaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#postfixUnarySuffix}.
	 * @param ctx the parse tree
	 */
	void enterPostfixUnarySuffix(KotlinParser.PostfixUnarySuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#postfixUnarySuffix}.
	 * @param ctx the parse tree
	 */
	void exitPostfixUnarySuffix(KotlinParser.PostfixUnarySuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#directlyAssignableExpression}.
	 * @param ctx the parse tree
	 */
	void enterDirectlyAssignableExpression(KotlinParser.DirectlyAssignableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#directlyAssignableExpression}.
	 * @param ctx the parse tree
	 */
	void exitDirectlyAssignableExpression(KotlinParser.DirectlyAssignableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#assignableExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignableExpression(KotlinParser.AssignableExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#assignableExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignableExpression(KotlinParser.AssignableExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#assignableSuffix}.
	 * @param ctx the parse tree
	 */
	void enterAssignableSuffix(KotlinParser.AssignableSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#assignableSuffix}.
	 * @param ctx the parse tree
	 */
	void exitAssignableSuffix(KotlinParser.AssignableSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#indexingSuffix}.
	 * @param ctx the parse tree
	 */
	void enterIndexingSuffix(KotlinParser.IndexingSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#indexingSuffix}.
	 * @param ctx the parse tree
	 */
	void exitIndexingSuffix(KotlinParser.IndexingSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#navigationSuffix}.
	 * @param ctx the parse tree
	 */
	void enterNavigationSuffix(KotlinParser.NavigationSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#navigationSuffix}.
	 * @param ctx the parse tree
	 */
	void exitNavigationSuffix(KotlinParser.NavigationSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#callSuffix}.
	 * @param ctx the parse tree
	 */
	void enterCallSuffix(KotlinParser.CallSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#callSuffix}.
	 * @param ctx the parse tree
	 */
	void exitCallSuffix(KotlinParser.CallSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#annotatedLambda}.
	 * @param ctx the parse tree
	 */
	void enterAnnotatedLambda(KotlinParser.AnnotatedLambdaContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#annotatedLambda}.
	 * @param ctx the parse tree
	 */
	void exitAnnotatedLambda(KotlinParser.AnnotatedLambdaContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#valueArguments}.
	 * @param ctx the parse tree
	 */
	void enterValueArguments(KotlinParser.ValueArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#valueArguments}.
	 * @param ctx the parse tree
	 */
	void exitValueArguments(KotlinParser.ValueArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(KotlinParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(KotlinParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeProjection}.
	 * @param ctx the parse tree
	 */
	void enterTypeProjection(KotlinParser.TypeProjectionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeProjection}.
	 * @param ctx the parse tree
	 */
	void exitTypeProjection(KotlinParser.TypeProjectionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeProjectionModifiers}.
	 * @param ctx the parse tree
	 */
	void enterTypeProjectionModifiers(KotlinParser.TypeProjectionModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeProjectionModifiers}.
	 * @param ctx the parse tree
	 */
	void exitTypeProjectionModifiers(KotlinParser.TypeProjectionModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeProjectionModifier}.
	 * @param ctx the parse tree
	 */
	void enterTypeProjectionModifier(KotlinParser.TypeProjectionModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeProjectionModifier}.
	 * @param ctx the parse tree
	 */
	void exitTypeProjectionModifier(KotlinParser.TypeProjectionModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#valueArgument}.
	 * @param ctx the parse tree
	 */
	void enterValueArgument(KotlinParser.ValueArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#valueArgument}.
	 * @param ctx the parse tree
	 */
	void exitValueArgument(KotlinParser.ValueArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpression(KotlinParser.PrimaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#primaryExpression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpression(KotlinParser.PrimaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#parenthesizedExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(KotlinParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#parenthesizedExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(KotlinParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#collectionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterCollectionLiteral(KotlinParser.CollectionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#collectionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitCollectionLiteral(KotlinParser.CollectionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#literalConstant}.
	 * @param ctx the parse tree
	 */
	void enterLiteralConstant(KotlinParser.LiteralConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#literalConstant}.
	 * @param ctx the parse tree
	 */
	void exitLiteralConstant(KotlinParser.LiteralConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(KotlinParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#stringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(KotlinParser.StringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#lineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterLineStringLiteral(KotlinParser.LineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#lineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitLineStringLiteral(KotlinParser.LineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterMultiLineStringLiteral(KotlinParser.MultiLineStringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiLineStringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitMultiLineStringLiteral(KotlinParser.MultiLineStringLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#lineStringContent}.
	 * @param ctx the parse tree
	 */
	void enterLineStringContent(KotlinParser.LineStringContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#lineStringContent}.
	 * @param ctx the parse tree
	 */
	void exitLineStringContent(KotlinParser.LineStringContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#lineStringExpression}.
	 * @param ctx the parse tree
	 */
	void enterLineStringExpression(KotlinParser.LineStringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#lineStringExpression}.
	 * @param ctx the parse tree
	 */
	void exitLineStringExpression(KotlinParser.LineStringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiLineStringContent}.
	 * @param ctx the parse tree
	 */
	void enterMultiLineStringContent(KotlinParser.MultiLineStringContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiLineStringContent}.
	 * @param ctx the parse tree
	 */
	void exitMultiLineStringContent(KotlinParser.MultiLineStringContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiLineStringExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiLineStringExpression(KotlinParser.MultiLineStringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiLineStringExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiLineStringExpression(KotlinParser.MultiLineStringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#lambdaLiteral}.
	 * @param ctx the parse tree
	 */
	void enterLambdaLiteral(KotlinParser.LambdaLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#lambdaLiteral}.
	 * @param ctx the parse tree
	 */
	void exitLambdaLiteral(KotlinParser.LambdaLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameters(KotlinParser.LambdaParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#lambdaParameters}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameters(KotlinParser.LambdaParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#lambdaParameter}.
	 * @param ctx the parse tree
	 */
	void enterLambdaParameter(KotlinParser.LambdaParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#lambdaParameter}.
	 * @param ctx the parse tree
	 */
	void exitLambdaParameter(KotlinParser.LambdaParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#anonymousFunction}.
	 * @param ctx the parse tree
	 */
	void enterAnonymousFunction(KotlinParser.AnonymousFunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#anonymousFunction}.
	 * @param ctx the parse tree
	 */
	void exitAnonymousFunction(KotlinParser.AnonymousFunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFunctionLiteral(KotlinParser.FunctionLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFunctionLiteral(KotlinParser.FunctionLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#objectLiteral}.
	 * @param ctx the parse tree
	 */
	void enterObjectLiteral(KotlinParser.ObjectLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#objectLiteral}.
	 * @param ctx the parse tree
	 */
	void exitObjectLiteral(KotlinParser.ObjectLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#thisExpression}.
	 * @param ctx the parse tree
	 */
	void enterThisExpression(KotlinParser.ThisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#thisExpression}.
	 * @param ctx the parse tree
	 */
	void exitThisExpression(KotlinParser.ThisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#superExpression}.
	 * @param ctx the parse tree
	 */
	void enterSuperExpression(KotlinParser.SuperExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#superExpression}.
	 * @param ctx the parse tree
	 */
	void exitSuperExpression(KotlinParser.SuperExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#controlStructureBody}.
	 * @param ctx the parse tree
	 */
	void enterControlStructureBody(KotlinParser.ControlStructureBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#controlStructureBody}.
	 * @param ctx the parse tree
	 */
	void exitControlStructureBody(KotlinParser.ControlStructureBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#ifExpression}.
	 * @param ctx the parse tree
	 */
	void enterIfExpression(KotlinParser.IfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#ifExpression}.
	 * @param ctx the parse tree
	 */
	void exitIfExpression(KotlinParser.IfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#whenExpression}.
	 * @param ctx the parse tree
	 */
	void enterWhenExpression(KotlinParser.WhenExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#whenExpression}.
	 * @param ctx the parse tree
	 */
	void exitWhenExpression(KotlinParser.WhenExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#whenEntry}.
	 * @param ctx the parse tree
	 */
	void enterWhenEntry(KotlinParser.WhenEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#whenEntry}.
	 * @param ctx the parse tree
	 */
	void exitWhenEntry(KotlinParser.WhenEntryContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#whenCondition}.
	 * @param ctx the parse tree
	 */
	void enterWhenCondition(KotlinParser.WhenConditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#whenCondition}.
	 * @param ctx the parse tree
	 */
	void exitWhenCondition(KotlinParser.WhenConditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#rangeTest}.
	 * @param ctx the parse tree
	 */
	void enterRangeTest(KotlinParser.RangeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#rangeTest}.
	 * @param ctx the parse tree
	 */
	void exitRangeTest(KotlinParser.RangeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#typeTest}.
	 * @param ctx the parse tree
	 */
	void enterTypeTest(KotlinParser.TypeTestContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#typeTest}.
	 * @param ctx the parse tree
	 */
	void exitTypeTest(KotlinParser.TypeTestContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#tryExpression}.
	 * @param ctx the parse tree
	 */
	void enterTryExpression(KotlinParser.TryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#tryExpression}.
	 * @param ctx the parse tree
	 */
	void exitTryExpression(KotlinParser.TryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#catchBlock}.
	 * @param ctx the parse tree
	 */
	void enterCatchBlock(KotlinParser.CatchBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#catchBlock}.
	 * @param ctx the parse tree
	 */
	void exitCatchBlock(KotlinParser.CatchBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(KotlinParser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(KotlinParser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void enterLoopStatement(KotlinParser.LoopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#loopStatement}.
	 * @param ctx the parse tree
	 */
	void exitLoopStatement(KotlinParser.LoopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(KotlinParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(KotlinParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(KotlinParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(KotlinParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(KotlinParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(KotlinParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#jumpExpression}.
	 * @param ctx the parse tree
	 */
	void enterJumpExpression(KotlinParser.JumpExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#jumpExpression}.
	 * @param ctx the parse tree
	 */
	void exitJumpExpression(KotlinParser.JumpExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#callableReference}.
	 * @param ctx the parse tree
	 */
	void enterCallableReference(KotlinParser.CallableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#callableReference}.
	 * @param ctx the parse tree
	 */
	void exitCallableReference(KotlinParser.CallableReferenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#assignmentAndOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentAndOperator(KotlinParser.AssignmentAndOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#assignmentAndOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentAndOperator(KotlinParser.AssignmentAndOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#equalityOperator}.
	 * @param ctx the parse tree
	 */
	void enterEqualityOperator(KotlinParser.EqualityOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#equalityOperator}.
	 * @param ctx the parse tree
	 */
	void exitEqualityOperator(KotlinParser.EqualityOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(KotlinParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(KotlinParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#inOperator}.
	 * @param ctx the parse tree
	 */
	void enterInOperator(KotlinParser.InOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#inOperator}.
	 * @param ctx the parse tree
	 */
	void exitInOperator(KotlinParser.InOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#isOperator}.
	 * @param ctx the parse tree
	 */
	void enterIsOperator(KotlinParser.IsOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#isOperator}.
	 * @param ctx the parse tree
	 */
	void exitIsOperator(KotlinParser.IsOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#additiveOperator}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveOperator(KotlinParser.AdditiveOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#additiveOperator}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveOperator(KotlinParser.AdditiveOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeOperator(KotlinParser.MultiplicativeOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiplicativeOperator}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeOperator(KotlinParser.MultiplicativeOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#asOperator}.
	 * @param ctx the parse tree
	 */
	void enterAsOperator(KotlinParser.AsOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#asOperator}.
	 * @param ctx the parse tree
	 */
	void exitAsOperator(KotlinParser.AsOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#prefixUnaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterPrefixUnaryOperator(KotlinParser.PrefixUnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#prefixUnaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitPrefixUnaryOperator(KotlinParser.PrefixUnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#postfixUnaryOperator}.
	 * @param ctx the parse tree
	 */
	void enterPostfixUnaryOperator(KotlinParser.PostfixUnaryOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#postfixUnaryOperator}.
	 * @param ctx the parse tree
	 */
	void exitPostfixUnaryOperator(KotlinParser.PostfixUnaryOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#memberAccessOperator}.
	 * @param ctx the parse tree
	 */
	void enterMemberAccessOperator(KotlinParser.MemberAccessOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#memberAccessOperator}.
	 * @param ctx the parse tree
	 */
	void exitMemberAccessOperator(KotlinParser.MemberAccessOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void enterModifiers(KotlinParser.ModifiersContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#modifiers}.
	 * @param ctx the parse tree
	 */
	void exitModifiers(KotlinParser.ModifiersContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(KotlinParser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(KotlinParser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#classModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassModifier(KotlinParser.ClassModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#classModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassModifier(KotlinParser.ClassModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#memberModifier}.
	 * @param ctx the parse tree
	 */
	void enterMemberModifier(KotlinParser.MemberModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#memberModifier}.
	 * @param ctx the parse tree
	 */
	void exitMemberModifier(KotlinParser.MemberModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#visibilityModifier}.
	 * @param ctx the parse tree
	 */
	void enterVisibilityModifier(KotlinParser.VisibilityModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#visibilityModifier}.
	 * @param ctx the parse tree
	 */
	void exitVisibilityModifier(KotlinParser.VisibilityModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#varianceModifier}.
	 * @param ctx the parse tree
	 */
	void enterVarianceModifier(KotlinParser.VarianceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#varianceModifier}.
	 * @param ctx the parse tree
	 */
	void exitVarianceModifier(KotlinParser.VarianceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#functionModifier}.
	 * @param ctx the parse tree
	 */
	void enterFunctionModifier(KotlinParser.FunctionModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#functionModifier}.
	 * @param ctx the parse tree
	 */
	void exitFunctionModifier(KotlinParser.FunctionModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#propertyModifier}.
	 * @param ctx the parse tree
	 */
	void enterPropertyModifier(KotlinParser.PropertyModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#propertyModifier}.
	 * @param ctx the parse tree
	 */
	void exitPropertyModifier(KotlinParser.PropertyModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#inheritanceModifier}.
	 * @param ctx the parse tree
	 */
	void enterInheritanceModifier(KotlinParser.InheritanceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#inheritanceModifier}.
	 * @param ctx the parse tree
	 */
	void exitInheritanceModifier(KotlinParser.InheritanceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#parameterModifier}.
	 * @param ctx the parse tree
	 */
	void enterParameterModifier(KotlinParser.ParameterModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#parameterModifier}.
	 * @param ctx the parse tree
	 */
	void exitParameterModifier(KotlinParser.ParameterModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#reificationModifier}.
	 * @param ctx the parse tree
	 */
	void enterReificationModifier(KotlinParser.ReificationModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#reificationModifier}.
	 * @param ctx the parse tree
	 */
	void exitReificationModifier(KotlinParser.ReificationModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#platformModifier}.
	 * @param ctx the parse tree
	 */
	void enterPlatformModifier(KotlinParser.PlatformModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#platformModifier}.
	 * @param ctx the parse tree
	 */
	void exitPlatformModifier(KotlinParser.PlatformModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#label}.
	 * @param ctx the parse tree
	 */
	void enterLabel(KotlinParser.LabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#label}.
	 * @param ctx the parse tree
	 */
	void exitLabel(KotlinParser.LabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(KotlinParser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(KotlinParser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#singleAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterSingleAnnotation(KotlinParser.SingleAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#singleAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitSingleAnnotation(KotlinParser.SingleAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#multiAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterMultiAnnotation(KotlinParser.MultiAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#multiAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitMultiAnnotation(KotlinParser.MultiAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#annotationUseSiteTarget}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationUseSiteTarget(KotlinParser.AnnotationUseSiteTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#annotationUseSiteTarget}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationUseSiteTarget(KotlinParser.AnnotationUseSiteTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#unescapedAnnotation}.
	 * @param ctx the parse tree
	 */
	void enterUnescapedAnnotation(KotlinParser.UnescapedAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#unescapedAnnotation}.
	 * @param ctx the parse tree
	 */
	void exitUnescapedAnnotation(KotlinParser.UnescapedAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterSimpleIdentifier(KotlinParser.SimpleIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#simpleIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitSimpleIdentifier(KotlinParser.SimpleIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(KotlinParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(KotlinParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#shebangLine}.
	 * @param ctx the parse tree
	 */
	void enterShebangLine(KotlinParser.ShebangLineContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#shebangLine}.
	 * @param ctx the parse tree
	 */
	void exitShebangLine(KotlinParser.ShebangLineContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#quest}.
	 * @param ctx the parse tree
	 */
	void enterQuest(KotlinParser.QuestContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#quest}.
	 * @param ctx the parse tree
	 */
	void exitQuest(KotlinParser.QuestContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#elvis}.
	 * @param ctx the parse tree
	 */
	void enterElvis(KotlinParser.ElvisContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#elvis}.
	 * @param ctx the parse tree
	 */
	void exitElvis(KotlinParser.ElvisContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#safeNav}.
	 * @param ctx the parse tree
	 */
	void enterSafeNav(KotlinParser.SafeNavContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#safeNav}.
	 * @param ctx the parse tree
	 */
	void exitSafeNav(KotlinParser.SafeNavContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#excl}.
	 * @param ctx the parse tree
	 */
	void enterExcl(KotlinParser.ExclContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#excl}.
	 * @param ctx the parse tree
	 */
	void exitExcl(KotlinParser.ExclContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#semi}.
	 * @param ctx the parse tree
	 */
	void enterSemi(KotlinParser.SemiContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#semi}.
	 * @param ctx the parse tree
	 */
	void exitSemi(KotlinParser.SemiContext ctx);
	/**
	 * Enter a parse tree produced by {@link KotlinParser#semis}.
	 * @param ctx the parse tree
	 */
	void enterSemis(KotlinParser.SemisContext ctx);
	/**
	 * Exit a parse tree produced by {@link KotlinParser#semis}.
	 * @param ctx the parse tree
	 */
	void exitSemis(KotlinParser.SemisContext ctx);
}