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

import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.MethodTree
import openjdk.source.util.JavacTask
import openjdk.source.util.TreePath
import openjdk.source.util.TreePathScanner
import openjdk.source.util.Trees

/**
 * Finds method declaration at the given cursor position.
 *
 * @author Akash Yadav
 */
class FindMethodAt(val task: JavacTask) : TreePathScanner<TreePath?, Long>() {

  private val sourcePositions = Trees.instance(task).sourcePositions
  private var root: CompilationUnitTree? = null

  override fun visitCompilationUnit(node: CompilationUnitTree?, p: Long): TreePath? {
    this.root = node
    return super.visitCompilationUnit(node, p)
  }

  override fun visitMethod(node: MethodTree?, p: Long): TreePath? {
    val smaller = super.visitMethod(node, p)
    if (smaller != null || node == null) {
      return smaller
    }

    if (node.body != null) {
      val bodyStart = sourcePositions.getStartPosition(root, node.body)
      val bodyEnd = sourcePositions.getEndPosition(root, node.body)
      if (p in bodyStart..bodyEnd) {
        return currentPath
      }
    }

    val start = sourcePositions.getStartPosition(root, node)
    val end = sourcePositions.getEndPosition(root, node)
    if (p in start..end) {
      return currentPath
    }

    return null
  }
}
