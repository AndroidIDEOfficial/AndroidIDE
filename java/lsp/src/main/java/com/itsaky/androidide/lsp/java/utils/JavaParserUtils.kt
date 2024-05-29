/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.itsaky.androidide.lsp.java.utils

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.BodyDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.body.ReceiverParameter
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.AssignExpr
import com.github.javaparser.ast.expr.BooleanLiteralExpr
import com.github.javaparser.ast.expr.CharLiteralExpr
import com.github.javaparser.ast.expr.DoubleLiteralExpr
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.FieldAccessExpr
import com.github.javaparser.ast.expr.IntegerLiteralExpr
import com.github.javaparser.ast.expr.LiteralExpr
import com.github.javaparser.ast.expr.LongLiteralExpr
import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.MemberValuePair
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.expr.Name
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.expr.NullLiteralExpr
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import com.github.javaparser.ast.expr.StringLiteralExpr
import com.github.javaparser.ast.expr.SuperExpr
import com.github.javaparser.ast.expr.VariableDeclarationExpr
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.stmt.ExpressionStmt
import com.github.javaparser.ast.stmt.ReturnStmt
import com.github.javaparser.ast.stmt.Statement
import com.github.javaparser.ast.type.ArrayType
import com.github.javaparser.ast.type.PrimitiveType
import com.github.javaparser.ast.type.PrimitiveType.Primitive.BOOLEAN
import com.github.javaparser.ast.type.PrimitiveType.Primitive.BYTE
import com.github.javaparser.ast.type.PrimitiveType.Primitive.CHAR
import com.github.javaparser.ast.type.PrimitiveType.Primitive.DOUBLE
import com.github.javaparser.ast.type.PrimitiveType.Primitive.FLOAT
import com.github.javaparser.ast.type.PrimitiveType.Primitive.INT
import com.github.javaparser.ast.type.PrimitiveType.Primitive.LONG
import com.github.javaparser.ast.type.PrimitiveType.Primitive.SHORT
import com.github.javaparser.ast.type.ReferenceType
import com.github.javaparser.ast.type.Type
import com.github.javaparser.ast.type.TypeParameter
import com.github.javaparser.printer.DefaultPrettyPrinter
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration
import com.github.javaparser.printer.configuration.PrinterConfiguration
import com.itsaky.androidide.lsp.java.utils.TypeUtils.toType
import com.itsaky.androidide.lsp.java.visitors.PrettyPrintingVisitor
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.Modifier
import jdkx.lang.model.element.TypeParameterElement
import jdkx.lang.model.element.VariableElement
import jdkx.lang.model.type.ExecutableType
import jdkx.lang.model.type.TypeKind
import jdkx.lang.model.type.TypeMirror
import jdkx.lang.model.type.TypeVariable
import openjdk.source.tree.AnnotationTree
import openjdk.source.tree.AssignmentTree
import openjdk.source.tree.BlockTree
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.ErroneousTree
import openjdk.source.tree.ExpressionStatementTree
import openjdk.source.tree.ExpressionTree
import openjdk.source.tree.IdentifierTree
import openjdk.source.tree.ImportTree
import openjdk.source.tree.LiteralTree
import openjdk.source.tree.MemberSelectTree
import openjdk.source.tree.MethodInvocationTree
import openjdk.source.tree.MethodTree
import openjdk.source.tree.PackageTree
import openjdk.source.tree.StatementTree
import openjdk.source.tree.Tree
import openjdk.source.tree.TypeParameterTree
import openjdk.source.tree.VariableTree
import org.jetbrains.annotations.Contract
import java.util.function.Predicate
import java.util.stream.IntStream
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

object JavaParserUtils {

  /**
   * Collects all the type names from the given node.
   *
   * @param type The method to get types from.
   * @return A set of fully qualified names of the types in the given node.
   */
  fun collectImports(type: ExecutableType): MutableSet<String> {
    val types = mutableSetOf<String>()
    val returnType = type.returnType
    if (returnType != null) {
      if (
        returnType.kind != TypeKind.VOID &&
        returnType.kind != TypeKind.TYPEVAR &&
        !returnType.kind.isPrimitive
      ) {
        val fqn = getTypeToImport(returnType)
        if (fqn != null) {
          types.add(fqn)
        }
      }
    }
    if (type.thrownTypes != null) {
      for (thrown in type.thrownTypes) {
        val fqn = getTypeToImport(thrown)
        if (fqn != null) {
          types.add(fqn)
        }
      }
    }
    for (t in type.parameterTypes) {
      if (t.kind.isPrimitive) {
        continue
      }
      val fqn = getTypeToImport(t)
      if (fqn != null) {
        types.add(fqn)
      }
    }
    return types
  }

  @Nullable
  private fun getTypeToImport(type: TypeMirror): String? {
    if (type.kind.isPrimitive) {
      return null
    }
    if (type.kind == TypeKind.TYPEVAR) {
      return null
    }
    var fqn = toType(type).toString()
    if (type.kind == TypeKind.ARRAY) {
      fqn = removeArray(fqn)
    }

    return removeDiamond(fqn)
  }

  fun printMethod(
    method: ExecutableElement,
    parameterizedType: ExecutableType?,
    source: MethodTree,
  ): MethodDeclaration {
    val methodDeclaration: MethodDeclaration = toMethodDeclaration(source, parameterizedType)
    printMethodInternal(methodDeclaration, method)
    return methodDeclaration
  }

  fun printMethod(
    method: ExecutableElement?,
    parameterizedType: ExecutableType?,
    source: ExecutableElement,
  ): MethodDeclaration {
    val methodDeclaration: MethodDeclaration = toMethodDeclaration(method, parameterizedType)
    printMethodInternal(methodDeclaration, source)
    return methodDeclaration
  }

  private fun printMethodInternal(
    methodDeclaration: MethodDeclaration,
    method: ExecutableElement,
  ) {
    methodDeclaration.addMarkerAnnotation(Override::class.java)
    val recentlyNonNull = methodDeclaration.getAnnotationByName("RecentlyNonNull")
    if (recentlyNonNull.isPresent) {
      methodDeclaration.remove(recentlyNonNull.get())
      methodDeclaration.addMarkerAnnotation(NonNull::class.java)
    }

    val blockStmt = BlockStmt()
    if (method.modifiers.contains(Modifier.ABSTRACT)) {
      methodDeclaration.removeModifier(com.github.javaparser.ast.Modifier.Keyword.ABSTRACT)
      if (methodDeclaration.type.isClassOrInterfaceType) {
        blockStmt.addStatement(ReturnStmt(NullLiteralExpr()))
      }
      if (methodDeclaration.type.isPrimitiveType) {
        val type = methodDeclaration.type.asPrimitiveType()
        blockStmt.addStatement(ReturnStmt(getReturnExpr(type)))
      }
    } else {
      val methodCallExpr = MethodCallExpr()
      methodCallExpr.name = methodDeclaration.name
      methodCallExpr.arguments =
        methodDeclaration.parameters
          .stream()
          .map { obj: Parameter -> obj.nameAsExpression }
          .collect(NodeList.toNodeList())
      methodCallExpr.setScope(SuperExpr())
      if (methodDeclaration.type.isVoidType) {
        blockStmt.addStatement(methodCallExpr)
      } else {
        blockStmt.addStatement(ReturnStmt(methodCallExpr))
      }
    }
    methodDeclaration.setBody(blockStmt)
  }

  private fun getReturnExpr(type: PrimitiveType): Expression {
    return when (type.type) {
      BOOLEAN -> BooleanLiteralExpr()
      BYTE,
      DOUBLE,
      CHAR,
      SHORT,
      LONG,
      FLOAT,
      INT -> IntegerLiteralExpr("0")

      else -> NullLiteralExpr()
    }
  }

  @Suppress("Since15")
  fun toCompilationUnit(tree: CompilationUnitTree): CompilationUnit {
    val compilationUnit = CompilationUnit()
    compilationUnit.setPackageDeclaration(toPackageDeclaration(tree.getPackage()))
    tree.imports.forEach { importTree: ImportTree? ->
      compilationUnit.addImport(toImportDeclaration(importTree!!))
    }
    compilationUnit.types =
      tree.typeDecls
        .stream()
        .map { toClassOrInterfaceDeclaration(it) }
        .collect(NodeList.toNodeList())
    return compilationUnit
  }

  @Suppress("Since15")
  fun toPackageDeclaration(tree: PackageTree): PackageDeclaration {
    val declaration = PackageDeclaration()
    declaration.setName(tree.packageName.toString())
    return declaration
  }

  fun toImportDeclaration(tree: ImportTree): ImportDeclaration {
    val name = tree.qualifiedIdentifier.toString()
    val isAsterisk = name.endsWith("*")
    return ImportDeclaration(name, tree.isStatic, isAsterisk)
  }

  fun toClassOrInterfaceDeclaration(tree: Tree?): ClassOrInterfaceDeclaration? {
    return if (tree is ClassTree) {
      toClassOrInterfaceDeclaration(tree as ClassTree?)
    } else null
  }

  fun toBlockStatement(tree: BlockTree): BlockStmt {
    val blockStmt = BlockStmt()
    blockStmt.statements =
      tree.statements.stream().map { toStatement(it) }.collect(NodeList.toNodeList())
    return blockStmt
  }

  fun toStatement(tree: StatementTree?): Statement? {
    if (tree is ExpressionStatementTree) {
      return toExpressionStatement((tree as ExpressionStatementTree?)!!)
    }
    return if (tree is VariableTree) {
      toVariableDeclarationExpression((tree as VariableTree?)!!)
    } else StaticJavaParser.parseStatement(tree.toString())
  }

  fun toExpressionStatement(tree: ExpressionStatementTree): ExpressionStmt {
    val expressionStmt = ExpressionStmt()
    expressionStmt.expression = toExpression(tree.expression)
    return expressionStmt
  }

  fun toExpression(tree: ExpressionTree?): Expression? {
    if (tree is MethodInvocationTree) {
      return toMethodCallExpression((tree as MethodInvocationTree?)!!)
    }
    if (tree is MemberSelectTree) {
      return toFieldAccessExpression((tree as MemberSelectTree?)!!)
    }
    if (tree is IdentifierTree) {
      return toNameExpr((tree as IdentifierTree?)!!)
    }
    if (tree is LiteralTree) {
      return toLiteralExpression((tree as LiteralTree?)!!)
    }
    if (tree is AssignmentTree) {
      return toAssignExpression((tree as AssignmentTree?)!!)
    }
    if (tree is ErroneousTree) {
      val erroneousTree = tree as ErroneousTree?
      if (erroneousTree!!.errorTrees.isNotEmpty()) {
        val errorTree = erroneousTree.errorTrees[0]
        return toExpression(errorTree as ExpressionTree)
      }
    }
    return null
  }

  private fun toAssignExpression(tree: AssignmentTree): AssignExpr {
    val assignExpr = AssignExpr()
    assignExpr.target = toExpression(tree.variable)
    assignExpr.value = toExpression(tree.expression)
    return assignExpr
  }

  fun toVariableDeclarationExpression(tree: VariableTree): ExpressionStmt {
    val expr = VariableDeclarationExpr()
    expr.modifiers =
      tree.modifiers.flags.stream().map { toModifier(it) }.collect(NodeList.toNodeList())
    val declarator = VariableDeclarator()
    declarator.setName(tree.name.toString())
    declarator.setInitializer(toExpression(tree.initializer))
    declarator.type = toType(tree.type)
    expr.addVariable(declarator)
    val stmt = ExpressionStmt()
    stmt.expression = expr
    return stmt
  }

  fun toNameExpr(tree: IdentifierTree): NameExpr {
    val nameExpr = NameExpr()
    nameExpr.setName(tree.name.toString())
    return nameExpr
  }

  fun toLiteralExpression(tree: LiteralTree): LiteralExpr? {
    val value = tree.value
    if (value is String) {
      return StringLiteralExpr(value)
    }
    if (value is Boolean) {
      return BooleanLiteralExpr(value)
    }
    if (value is Int) {
      return IntegerLiteralExpr(value.toString())
    }
    if (value is Char) {
      return CharLiteralExpr(value)
    }
    if (value is Long) {
      return LongLiteralExpr(value.toString())
    }
    return if (value is Double) {
      DoubleLiteralExpr(value)
    } else null
  }

  fun toMethodCallExpression(tree: MethodInvocationTree): MethodCallExpr {
    val expr = MethodCallExpr()
    if (tree.methodSelect is MemberSelectTree) {
      val methodSelect = tree.methodSelect as MemberSelectTree
      expr.setScope(toExpression(methodSelect.expression))
      expr.setName(methodSelect.identifier.toString())
    }
    expr.arguments = tree.arguments.stream().map { toExpression(it) }.collect(NodeList.toNodeList())
    expr.setTypeArguments(
      tree.typeArguments.stream().map { toType(it) }.collect(NodeList.toNodeList())
    )
    if (tree.methodSelect is IdentifierTree) {
      expr.name = toNameExpr((tree.methodSelect as IdentifierTree)).name
    }
    return expr
  }

  fun toFieldAccessExpression(tree: MemberSelectTree): FieldAccessExpr {
    val fieldAccessExpr = FieldAccessExpr()
    fieldAccessExpr.setName(tree.identifier.toString())
    fieldAccessExpr.scope = toExpression(tree.expression)
    return fieldAccessExpr
  }

  fun toClassOrInterfaceDeclaration(tree: ClassTree): ClassOrInterfaceDeclaration {
    val declaration = ClassOrInterfaceDeclaration()
    declaration.setName(tree.simpleName.toString())
    declaration.extendedTypes =
      NodeList.nodeList(TypeUtils.toClassOrInterfaceType(tree.extendsClause))
    declaration.typeParameters =
      tree.typeParameters.stream().map { toTypeParameter(it) }.collect(NodeList.toNodeList())
    declaration.typeParameters =
      tree.typeParameters.stream().map { toTypeParameter(it) }.collect(NodeList.toNodeList())
    declaration.implementedTypes =
      tree.implementsClause
        .stream()
        .map { TypeUtils.toClassOrInterfaceType(it) }
        .collect(NodeList.toNodeList())
    declaration.modifiers =
      tree.modifiers.flags.stream().map { toModifier(it) }.collect(NodeList.toNodeList())
    declaration.members =
      tree.members.stream().map { toBodyDeclaration(it) }.collect(NodeList.toNodeList())
    return declaration
  }

  fun toBodyDeclaration(tree: Tree?): BodyDeclaration<*>? {
    if (tree is MethodTree) {
      return toMethodDeclaration((tree as MethodTree?)!!, null)
    }
    return if (tree is VariableTree) {
      toFieldDeclaration((tree as VariableTree?)!!)
    } else null
  }

  fun toFieldDeclaration(tree: VariableTree): FieldDeclaration {
    val declaration = FieldDeclaration()
    declaration.modifiers =
      tree.modifiers.flags.stream().map { toModifier(it) }.collect(NodeList.toNodeList())
    val declarator = VariableDeclarator()
    declarator.setName(tree.name.toString())
    val initializer = toExpression(tree.initializer)
    if (initializer != null) {
      declarator.setInitializer(initializer)
    }
    val type = toType(tree.type)
    if (type != null) {
      declarator.type = type
    }
    declaration.addVariable(declarator)
    return declaration
  }

  fun toMethodDeclaration(method: MethodTree, type: ExecutableType?): MethodDeclaration {
    val methodDeclaration = MethodDeclaration()
    methodDeclaration.annotations =
      method.modifiers.annotations.stream().map { toAnnotation(it) }.collect(NodeList.toNodeList())
    methodDeclaration.setName(method.name.toString())
    val returnType =
      if (type != null) {
        toType(type.returnType)
      } else {
        toType(method.returnType)
      }
    if (returnType != null) {
      methodDeclaration.type = getTypeWithoutBounds(returnType)
    }
    methodDeclaration.modifiers =
      method.modifiers.flags.stream().map { toModifier(it) }.collect(NodeList.toNodeList())
    methodDeclaration.parameters =
      method.parameters
        .map { variable ->
          return@map toParameter(variable).also { param ->
            val firstType = getTypeWithoutBounds(param.type)
            param.type = firstType
          }
        }
        .toNodeList()
    methodDeclaration.typeParameters =
      method.typeParameters
        .mapNotNull {
          return@mapNotNull toType(it as Tree?)?.toTypeParameter()?.getOrNull()
        }
        .toNodeList()
    if (method.body != null) {
      methodDeclaration.setBody(toBlockStatement(method.body))
    }
    if (method.receiverParameter != null) {
      methodDeclaration.setReceiverParameter(toReceiverParameter(method.receiverParameter))
    }
    return methodDeclaration
  }

  fun toAnnotation(tree: AnnotationTree): AnnotationExpr {
    if (tree.arguments.isEmpty()) {
      val expr = MarkerAnnotationExpr()
      expr.setName(toType(tree.annotationType).toString())
      return expr
    }
    if (tree.arguments.size == 1) {
      val expr = SingleMemberAnnotationExpr()
      expr.setName(toType(tree.annotationType).toString())
      expr.memberValue = toExpression(tree.arguments[0])
      return expr
    }
    val expr = NormalAnnotationExpr()
    expr.setName(toType(tree.annotationType).toString())
    expr.pairs =
      tree.arguments
        .map {
          return@map if (it is AssignmentTree) {
            val assignExpr = toAssignExpression((it as AssignmentTree?)!!)
            val pair = MemberValuePair()
            pair.setName(assignExpr.target.toString())
            pair.value = assignExpr.value
            pair
          } else null
        }
        .toNodeList()
    return expr
  }

  fun toParameter(tree: VariableTree): Parameter {
    val parameter = Parameter()
    parameter.type = toType(tree.type)
    tree.modifiers.flags.map { toModifier(it) }.toNodeList().also { parameter.modifiers = it }
    parameter.setName(tree.name.toString())
    return parameter
  }

  /**
   * Convert a parameter into [Parameter] object. This method is called from source files, giving
   * their accurate names.
   */
  fun toParameter(type: TypeMirror?, name: VariableTree): Parameter {
    val parameter = Parameter()
    parameter.setType(EditHelper.printType(type))
    parameter.setName(name.name.toString())
    parameter.modifiers = name.modifiers.flags.map { toModifier(it) }.toNodeList()
    parameter.setName(name.name.toString())
    return parameter
  }

  fun toTypeParameter(type: TypeParameterTree): TypeParameter? {
    return StaticJavaParser.parseTypeParameter(type.toString())
  }

  fun toReceiverParameter(parameter: VariableTree): ReceiverParameter {
    val receiverParameter = ReceiverParameter()
    receiverParameter.setName(parameter.name.toString())
    receiverParameter.type = toType(parameter.type)
    return receiverParameter
  }

  fun toMethodDeclaration(method: ExecutableElement?, type: ExecutableType?): MethodDeclaration {
    val methodDeclaration = MethodDeclaration()
    val returnType =
      if (type != null) {
        toType(type.returnType)
      } else {
        toType(method!!.returnType)
      }
    if (returnType != null) {
      methodDeclaration.type = getTypeWithoutBounds(returnType)
    }
    methodDeclaration.isDefault = method!!.isDefault
    methodDeclaration.setName(method.simpleName.toString())
    methodDeclaration.setModifiers(
      *method.modifiers
        .stream()
        .map { com.github.javaparser.ast.Modifier.Keyword.valueOf(it!!.name) }
        .asArray()
    )
    methodDeclaration.parameters =
      IntStream.range(0, method.parameters.size)
        .mapToObj {
          return@mapToObj toParameter(type!!.parameterTypes[it],
            method.parameters[it]).also { parameter ->
            val firstType = getTypeWithoutBounds(parameter.type)
            parameter.type = firstType
          }
        }
        .collect(NodeList.toNodeList())
    methodDeclaration.typeParameters =
      type!!
        .typeVariables
        .mapNotNull { toType(it as TypeMirror?)?.toTypeParameter()?.getOrNull() }
        .toNodeList()
    return methodDeclaration
  }

  fun getFirstArrayType(type: Type): Type {
    if (type.isTypeParameter) {
      val typeParameter = type.asTypeParameter()
      if (typeParameter!!.typeBound.isNonEmpty) {
        val first = typeParameter.typeBound.first
        if (first!!.isPresent) {
          return ArrayType(first.get())
        }
      }
    }
    return type
  }

  fun getFirstType(type: Type): Type {
    if (type.isTypeParameter) {
      val typeParameter = type.asTypeParameter()
      if (typeParameter!!.typeBound.isNonEmpty) {
        val first = typeParameter.typeBound.first
        if (first!!.isPresent) {
          return first.get()
        }
      }
    }
    if (!type.isClassOrInterfaceType) {
      return type
    }

    val typeArguments = type.asClassOrInterfaceType().typeArguments
    if (!typeArguments!!.isPresent || !typeArguments.get().isNonEmpty) {
      return type
    }

    val first = typeArguments.get().first
    if (!first!!.isPresent || !first.get().isTypeParameter) {
      return type
    }

    val typeBound = first.get().asTypeParameter().typeBound
    if (!typeBound!!.isNonEmpty) {
      return type
    }

    val first1 = typeBound.first
    if (!first1!!.isPresent) {
      return type
    }

    type.asClassOrInterfaceType().setTypeArguments(first1.get())
    return type
  }

  fun getTypeWithoutBounds(type: Type): Type {
    if (type.isArrayType && !type.asArrayType().componentType.isTypeParameter) {
      return type
    }
    if (!type.isArrayType && !type.isTypeParameter) {
      return type
    }
    if (type is NodeWithSimpleName<*>) {
      return StaticJavaParser.parseClassOrInterfaceType(
        (type as NodeWithSimpleName<*>).nameAsString
      )
    }
    return if (type.isArrayType) {
      ArrayType(getTypeWithoutBounds(type.asArrayType().componentType))
    } else type
  }

  @Contract("_ -> new")
  fun toModifier(modifier: Modifier): com.github.javaparser.ast.Modifier {
    return com.github.javaparser.ast.Modifier(
      com.github.javaparser.ast.Modifier.Keyword.valueOf(modifier.name)
    )
  }

  /**
   * Convert a parameter into [Parameter] object. This method is called from compiled class files,
   * giving inaccurate parameter names
   */
  fun toParameter(type: TypeMirror?, name: VariableElement?): Parameter {
    val parameter = Parameter()
    parameter.setType(EditHelper.printType(type))
    if (parameter.type.isArrayType) {
      if ((type as openjdk.tools.javac.code.Type.ArrayType?)!!.isVarargs) {
        parameter.type = parameter.type.asArrayType().componentType
        parameter.isVarArgs = true
      }
    }
    parameter.setName(name!!.simpleName.toString())
    parameter.modifiers = name.modifiers.map { toModifier(it) }.toNodeList()
    parameter.setName(name.simpleName.toString())
    return parameter
  }

  fun toTypeParameter(type: TypeParameterElement): TypeParameter? {
    return StaticJavaParser.parseTypeParameter(type.toString())
  }

  fun toTypeParameter(typeVariable: TypeVariable): TypeParameter? {
    return StaticJavaParser.parseTypeParameter(typeVariable.toString())
  }

  fun getClassNames(type: Type): MutableList<String?> {
    val classNames: MutableList<String?> = ArrayList()
    if (type.isClassOrInterfaceType) {
      classNames.add(type.asClassOrInterfaceType().name.asString())
    }
    if (type.isWildcardType) {
      val wildcardType = type.asWildcardType()
      wildcardType!!.extendedType.ifPresent { t: ReferenceType? ->
        classNames.addAll(getClassNames(t!!))
      }
      wildcardType.superType.ifPresent { t: ReferenceType? ->
        classNames.addAll(getClassNames(t!!))
      }
    }
    if (type.isArrayType) {
      classNames.addAll(getClassNames(type.asArrayType().componentType))
    }
    if (type.isIntersectionType) {
      type
        .asIntersectionType()
        .elements
        .stream()
        .map { getClassNames(it) }
        .forEach { c: MutableList<String?>? -> classNames.addAll(c!!) }
    }
    return classNames
  }

  /**
   * Print a node declaration into its string representation
   *
   * @param node node to print
   * @param delegate callback to whether a class name should be printed as fully qualified names
   * @return String representation of the method declaration properly formatted
   */
  fun prettyPrint(node: Node?, delegate: Predicate<String>?): String? {
    val configuration: PrinterConfiguration = DefaultPrinterConfiguration()
    val visitor: PrettyPrintingVisitor =
      object : PrettyPrintingVisitor(configuration) {
        override fun visit(n: SimpleName?, arg: Void?) {
          printOrphanCommentsBeforeThisChildNode(n)
          printComment(n!!.comment, arg)
          val identifier = n.identifier
          if (delegate!!.test(identifier)) {
            printer.print(identifier)
          } else {
            printer.print(getSimpleName(identifier))
          }
        }

        override fun visit(n: Name?, arg: Void?) {
          super.visit(n, arg)
        }
      }
    val prettyPrinter = DefaultPrettyPrinter({ visitor }, configuration)
    return prettyPrinter.print(node)
  }

  @JvmStatic
  fun getSimpleName(className: String?): String {
    var name = className
    name = removeDiamond(name!!)
    val dot = name.lastIndexOf('.')
    if (dot == -1) {
      return name
    }
    return if (name.startsWith("? extends")) {
      "? extends " + name.substring(dot + 1)
    } else name.substring(dot + 1)
  }

  fun removeDiamond(className: String): String {
    var name = className
    if (name.contains("<")) {
      name = name.substring(0, name.indexOf('<'))
    }
    return name
  }

  fun removeArray(className: String): String {
    var name = className
    if (name.contains("[")) {
      name = name.substring(0, name.indexOf('['))
    }
    return name
  }
}

private fun <E : Node?> Collection<E>.toNodeList(): NodeList<E?> {
  return NodeList(this)
}

private inline fun <reified T> Stream<T>.asArray(): Array<T> {
  val arr = mutableListOf<T>()
  for (element in this) {
    arr.add(element)
  }

  return arr.toTypedArray()
}
