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

package com.itsaky.androidide.javac.services.visitors

import com.itsaky.androidide.javac.services.visitors.FindAnonymousVisitor.Mode.CHECK
import com.itsaky.androidide.javac.services.visitors.FindAnonymousVisitor.Mode.COLLECT
import openjdk.source.tree.ClassTree
import openjdk.source.tree.MethodTree
import openjdk.source.tree.Tree
import openjdk.source.tree.VariableTree

/**
 * Partial reparse helper visitor. Finds anonymous and local classes in given method tree.
 *
 * @author Akash Yadav
 */
class FindAnonymousVisitor : ErrorAwareTreeScanner<Unit, Unit>() {

  private enum class Mode {
    COLLECT,
    CHECK
  }

  var noInner = 0
  var hasLocalClass = false
  val docOwners: MutableSet<Tree> = HashSet<Tree>()
  private var mode = COLLECT

  fun reset() {
    noInner = 0
    hasLocalClass = false
    mode = CHECK
  }

  override fun visitClass(node: ClassTree, p: Unit?): Unit? {
    if (node.simpleName.isNotEmpty()) {
      hasLocalClass = true
    }

    noInner++
    handleDoc(node)
    return super.visitClass(node, p)
  }

  override fun visitMethod(node: MethodTree, p: Unit?): Unit? {
    handleDoc(node)
    return super.visitMethod(node, p)
  }

  override fun visitVariable(node: VariableTree, p: Unit?): Unit? {
    handleDoc(node)
    return super.visitVariable(node, p)
  }

  private fun handleDoc(tree: Tree) {
    if (mode == COLLECT) {
      docOwners.add(tree)
    }
  }
}
