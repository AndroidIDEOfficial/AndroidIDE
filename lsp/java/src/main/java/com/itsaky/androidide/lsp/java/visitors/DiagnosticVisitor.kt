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

import com.itsaky.androidide.utils.ILogger
import com.sun.source.tree.BlockTree
import com.sun.source.tree.ClassTree
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.DoWhileLoopTree
import com.sun.source.tree.ForLoopTree
import com.sun.source.tree.IdentifierTree
import com.sun.source.tree.IfTree
import com.sun.source.tree.MemberReferenceTree
import com.sun.source.tree.MemberSelectTree
import com.sun.source.tree.MethodInvocationTree
import com.sun.source.tree.MethodTree
import com.sun.source.tree.NewClassTree
import com.sun.source.tree.ThrowTree
import com.sun.source.tree.Tree
import com.sun.source.tree.TryTree
import com.sun.source.tree.VariableTree
import com.sun.source.tree.WhileLoopTree
import com.sun.source.util.TreePath
import com.sun.source.util.TreeScanner
import com.sun.source.util.Trees
import com.sun.tools.javac.api.JavacTaskImpl
import java.util.*
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class DiagnosticVisitor(task: JavacTaskImpl?) :
  TreeScanner<Void?, MutableMap<TreePath?, String>>() {
  private val log = ILogger.newInstance(javaClass.simpleName)
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
    val prev = this.path
    this.path = path
    try {
      path.leaf.accept(this, null)
    } finally {
      this.path = prev // So we can call scan(path, _) recursively
    }
  }

  override fun scan(tree: Tree?, p: MutableMap<TreePath?, String>?): Void? {
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
    val element = trees.getElement(path) ?: return
    privateDeclarations[element] = path!!
  }

  private fun foundLocalVariable() {
    val element = trees.getElement(path) ?: return
    localVariables[element] = path!!
  }

  private fun foundReference() {
    val toEl = trees.getElement(path) ?: return
    if (toEl.asType().kind == TypeKind.ERROR) {
      foundPseudoReference(toEl)
      return
    }

    sweep(toEl)
  }

  private fun foundPseudoReference(toEl: Element) {
    val parent = toEl.enclosingElement as? TypeElement ?: return
    val memberName = toEl.simpleName
    for (member in parent.enclosedElements) {
      if (member.simpleName.contentEquals(memberName)) {
        sweep(member)
      }
    }
  }

  private fun sweep(toEl: Element) {
    val firstUse = used.add(toEl)
    val notScanned = firstUse && privateDeclarations.containsKey(toEl)
    if (notScanned) {
      scanPath(privateDeclarations[toEl]!!)
    }
  }

  private fun isReachable(path: TreePath): Boolean {
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

    // Check if t has been referenced by a reachable element
    val el = trees.getElement(path)
    return used.contains(el)
  }

  private fun isLocalVariable(path: TreePath): Boolean {
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
    if (t == null || notThrown == null) {
      return null
    }

    // Create a new method scope
    val pushDeclared = declaredExceptions
    val pushObserved = observedExceptions
    declaredExceptions = declared(t)
    observedExceptions = HashSet()
    // Recursively scan for 'throw' and method calls
    super.visitMethod(t, notThrown)
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
    val target = trees.getElement(path)
    if (target is ExecutableElement) {
      for (type in target.thrownTypes) {
        addThrown(type)
      }
    }

    return super.visitMethodInvocation(t, notThrown)
  }

  override fun visitBlock(node: BlockTree?, p: MutableMap<TreePath?, String>?): Void? {
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
    if (type is DeclaredType) {
      val el = type.asElement() as TypeElement
      val name = el.qualifiedName.toString()
      observedExceptions.add(name)
    }
  }
}
