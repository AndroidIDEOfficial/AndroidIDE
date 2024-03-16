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
package com.itsaky.androidide.lsp.java.visitors

import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import jdkx.lang.model.element.Element
import jdkx.lang.model.element.ExecutableElement
import jdkx.lang.model.element.Modifier
import jdkx.lang.model.element.TypeElement
import jdkx.lang.model.type.DeclaredType
import jdkx.lang.model.type.TypeKind
import jdkx.lang.model.type.TypeMirror
import openjdk.source.tree.BlockTree
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.DoWhileLoopTree
import openjdk.source.tree.ForLoopTree
import openjdk.source.tree.IdentifierTree
import openjdk.source.tree.IfTree
import openjdk.source.tree.MemberReferenceTree
import openjdk.source.tree.MemberSelectTree
import openjdk.source.tree.MethodInvocationTree
import openjdk.source.tree.MethodTree
import openjdk.source.tree.NewClassTree
import openjdk.source.tree.ThrowTree
import openjdk.source.tree.Tree
import openjdk.source.tree.TryTree
import openjdk.source.tree.VariableTree
import openjdk.source.tree.WhileLoopTree
import openjdk.source.util.TreePath
import openjdk.source.util.TreeScanner
import openjdk.source.util.Trees
import openjdk.tools.javac.api.JavacTaskImpl
import java.util.Objects

class DiagnosticVisitor(task: JavacTaskImpl?) :
  TreeScanner<Void?, MutableMap<TreePath?, String>>() {

  private val trees = Trees.instance(task)
  private val privateDeclarations = mutableMapOf<Element, TreePath>()
  private val localVariables = mutableMapOf<Element, TreePath>()
  private val used = mutableSetOf<Element>()
  private var declaredExceptions = mutableMapOf<String, TreePath>()
  private var observedExceptions = mutableSetOf<String>()
  val emptyBlocks = mutableMapOf<TreePath, String>()

  // Copied from TreePathScanner
  // We need to be able to call scan(path, _) recursively
  private var path: TreePath? = null

  private fun scanPath(path: TreePath) {
    abortIfCancelled()
    val prev = this.path
    this.path = path
    try {
      path.leaf.accept(this, null)
    } finally {
      this.path = prev // So we can call scan(path, _) recursively
    }
  }

  override fun scan(tree: Tree?, p: MutableMap<TreePath?, String>?): Void? {
    abortIfCancelled()
    if (tree == null) {
      return null
    }

    val prev = path
    path = TreePath(path, tree)
    return try {
      tree.accept(this, p)
    } finally {
      path = prev
    }
  }

  fun notUsed(): Set<Element> {
    val unused = mutableSetOf<Element>()
    unused.addAll(privateDeclarations.keys)
    unused.addAll(localVariables.keys)
    unused.removeAll(used)
    // Remove if there are any null elements somehow ended up being added
    // during async work which calls `lint`
    unused.removeIf { Objects.isNull(it) }
    // Remove if <error > field was injected while forming the AST
    unused.removeIf { it.toString() == "<error>" }
    return unused
  }

  private fun foundPrivateDeclaration() {
    abortIfCancelled()
    val element = trees.getElement(path) ?: return
    privateDeclarations[element] = path!!
  }

  private fun foundLocalVariable() {
    abortIfCancelled()
    val element = trees.getElement(path) ?: return
    localVariables[element] = path!!
  }

  private fun foundReference() {
    abortIfCancelled()
    val toEl = trees.getElement(path) ?: return
    if (toEl.asType().kind == TypeKind.ERROR) {
      foundPseudoReference(toEl)
      return
    }

    sweep(toEl)
  }

  private fun foundPseudoReference(toEl: Element) {
    abortIfCancelled()
    val parent = toEl.enclosingElement as? TypeElement ?: return
    val memberName = toEl.simpleName
    for (member in parent.enclosedElements) {
      if (member.simpleName.contentEquals(memberName)) {
        sweep(member)
      }
    }
  }

  private fun sweep(toEl: Element) {
    abortIfCancelled()
    val firstUse = used.add(toEl)
    val notScanned = firstUse && privateDeclarations.containsKey(toEl)
    if (notScanned) {
      scanPath(privateDeclarations[toEl]!!)
    }
  }

  private fun isReachable(path: TreePath): Boolean {
    abortIfCancelled()
    // Check if t is reachable because it's public
    val leaf = path.leaf
    if (leaf is VariableTree) {
      val isPrivate = leaf.modifiers.flags.contains(Modifier.PRIVATE)
      if (!isPrivate || isLocalVariable(path)) {
        return true
      }
    }

    if (leaf is MethodTree) {
      val isPrivate = leaf.modifiers.flags.contains(Modifier.PRIVATE)
      val isEmptyConstructor = leaf.parameters.isEmpty() && leaf.returnType == null
      if (!isPrivate || isEmptyConstructor) {
        return true
      }
    }

    if (leaf is ClassTree) {
      val isPrivate = leaf.modifiers.flags.contains(Modifier.PRIVATE)
      if (!isPrivate) {
        return true
      }
    }

    abortIfCancelled()

    // Check if t has been referenced by a reachable element
    val el = trees.getElement(path)
    return used.contains(el)
  }

  private fun isLocalVariable(path: TreePath): Boolean {
    abortIfCancelled()
    val kind = path.leaf.kind
    if (kind != Tree.Kind.VARIABLE) {
      return false
    }

    val parent = path.parentPath.leaf.kind
    if (parent == Tree.Kind.CLASS || parent == Tree.Kind.INTERFACE) {
      return false
    }

    if (parent == Tree.Kind.METHOD) {
      val method = path.parentPath.leaf as MethodTree
      return method.body != null
    }

    return true
  }

  private fun declared(t: MethodTree): MutableMap<String, TreePath> {
    abortIfCancelled()
    val names = mutableMapOf<String, TreePath>()
    for (e in t.throws) {
      val path = TreePath(path, e)
      val to = trees.getElement(path) as? TypeElement ?: continue
      val name = to.qualifiedName.toString()
      names[name] = path
    }
    return names
  }

  override fun visitCompilationUnit(
    t: CompilationUnitTree,
    notThrown: MutableMap<TreePath?, String>
  ): Void? {
    abortIfCancelled()
    return super.visitCompilationUnit(t, notThrown)
  }

  override fun visitVariable(t: VariableTree?, notThrown: MutableMap<TreePath?, String>?): Void? {
    when {
      isLocalVariable(path!!) -> {
        foundLocalVariable()
        super.visitVariable(t, notThrown)
      }

      isReachable(path!!) -> super.visitVariable(t, notThrown)
      else -> foundPrivateDeclaration()
    }
    return null
  }

  override fun visitMethod(t: MethodTree?, notThrown: MutableMap<TreePath?, String>?): Void? {
    abortIfCancelled()
    if (t == null || notThrown == null) {
      return null
    }

    // Create a new method scope
    val pushDeclared = declaredExceptions
    val pushObserved = observedExceptions
    declaredExceptions = declared(t)
    observedExceptions = HashSet()

    abortIfCancelled()
    // Recursively scan for 'throw' and method calls
    super.visitMethod(t, notThrown)
    abortIfCancelled()

    // Check for exceptions that were never thrown
    for (exception in declaredExceptions.keys) {
      if (!observedExceptions.contains(exception)) {
        notThrown[declaredExceptions[exception]] = exception
      }
    }
    declaredExceptions = pushDeclared
    observedExceptions = pushObserved
    if (!isReachable(path!!)) {
      foundPrivateDeclaration()
    }
    return null
  }

  override fun visitClass(t: ClassTree?, notThrown: MutableMap<TreePath?, String>?): Void? {
    if (isReachable(path!!)) {
      super.visitClass(t, notThrown)
    } else {
      foundPrivateDeclaration()
    }
    return null
  }

  override fun visitIdentifier(
    t: IdentifierTree?,
    notThrown: MutableMap<TreePath?, String>?
  ): Void? {
    foundReference()
    return super.visitIdentifier(t, notThrown)
  }

  override fun visitMemberSelect(
    t: MemberSelectTree?,
    notThrown: MutableMap<TreePath?, String>?
  ): Void? {
    foundReference()
    return super.visitMemberSelect(t, notThrown)
  }

  override fun visitMemberReference(
    t: MemberReferenceTree?,
    notThrown: MutableMap<TreePath?, String>?
  ): Void? {
    foundReference()
    return super.visitMemberReference(t, notThrown)
  }

  override fun visitNewClass(t: NewClassTree?, notThrown: MutableMap<TreePath?, String>?): Void? {
    foundReference()
    return super.visitNewClass(t, notThrown)
  }

  override fun visitThrow(t: ThrowTree?, notThrown: MutableMap<TreePath?, String>?): Void? {
    abortIfCancelled()
    if (t == null) {
      return null
    }

    val path = TreePath(path, t.expression)
    val type = trees.getTypeMirror(path)
    addThrown(type)
    return super.visitThrow(t, notThrown)
  }

  override fun visitMethodInvocation(
    t: MethodInvocationTree?,
    notThrown: MutableMap<TreePath?, String>?
  ): Void? {
    abortIfCancelled()
    val target = trees.getElement(path)
    if (target is ExecutableElement) {
      for (type in target.thrownTypes) {
        addThrown(type)
      }
    }

    return super.visitMethodInvocation(t, notThrown)
  }

  override fun visitBlock(node: BlockTree?, p: MutableMap<TreePath?, String>?): Void? {
    abortIfCancelled()
    if (node != null && node.statements.isEmpty()) {
      val name: String? =
        when (val parent = path!!.parentPath.leaf) {
          is IfTree -> fromIfTree(node, parent)
          is TryTree -> fromTryTree(node, parent)
          is ForLoopTree -> fromForTree(parent, node)
          is WhileLoopTree -> fromWhileTree(parent, node)
          is DoWhileLoopTree -> fromDoWhileTree(parent, node)
          else -> null
        }

      if (name != null) {
        emptyBlocks[path!!] = name
      }
    }
    return super.visitBlock(node, p)
  }

  private fun fromDoWhileTree(parent: DoWhileLoopTree, node: BlockTree?) =
    if (parent.statement == node) {
      "do"
    } else {
      null
    }

  private fun fromWhileTree(parent: WhileLoopTree, node: BlockTree?) =
    if (parent.statement == node) {
      "while"
    } else {
      null
    }

  private fun fromForTree(parent: ForLoopTree, node: BlockTree?) =
    if (parent.statement == node) {
      "for"
    } else {
      null
    }

  private fun fromTryTree(node: BlockTree, parent: TryTree) =
    when (node) {
      parent.block -> "try"
      parent.finallyBlock -> "finally"
      else -> {
        val catch =
          if (parent.catches.find { it.block == node } != null) {
            "catch"
          } else {
            null
          }
        catch
      }
    }

  private fun fromIfTree(node: BlockTree, parent: IfTree) =
    when (node) {
      parent.thenStatement -> "if"
      parent.elseStatement -> "else"
      else -> null
    }

  private fun addThrown(type: TypeMirror) {
    abortIfCancelled()
    if (type is DeclaredType) {
      val el = type.asElement() as TypeElement
      val name = el.qualifiedName.toString()
      observedExceptions.add(name)
    }
  }
}
