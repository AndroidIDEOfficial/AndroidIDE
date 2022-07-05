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

package com.itsaky.lsp.java.visitors

import androidx.core.util.Pair
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.models.Position
import com.itsaky.lsp.models.Range
import com.sun.source.tree.CompilationUnitTree
import com.sun.source.tree.LineMap
import com.sun.source.tree.MethodTree
import com.sun.source.util.TreePath
import com.sun.source.util.TreePathScanner
import com.sun.source.util.Trees
import com.sun.tools.javac.api.JavacTaskImpl

/**
 * Visits all methods and adds them to the given list of pair of method range and its tree.
 *
 * @author Akash Yadav
 */
class MethodRangeScanner(val task: JavacTaskImpl) :
  TreePathScanner<Unit, MutableList<Pair<Range, TreePath>>>() {

  private val log = ILogger.newInstance(javaClass.simpleName)

  var root: CompilationUnitTree? = null
  var lines: LineMap? = null
  val pos = Trees.instance(task).sourcePositions

  override fun visitCompilationUnit(
    node: CompilationUnitTree?,
    p: MutableList<Pair<Range, TreePath>>?
  ) {
    this.root = node
    this.lines = node?.lineMap
    return super.visitCompilationUnit(node, p)
  }

  override fun visitMethod(node: MethodTree?, list: MutableList<Pair<Range, TreePath>>) {
    val result = super.visitMethod(node, list)
    if (node == null || this.root == null) {
      return result
    }

    val start = getStartPosition(node)
    val end = getEndPosition(node)

    if (start == null || end == null) {
      log.warn("Method '${node.name}' skipped. Invalid position.")
      return result
    }

    list.add(Pair.create(Range(start!!, end!!), currentPath))
    return result
  }

  fun getStartPosition(node: MethodTree): Position? {
    val position = this.pos.getStartPosition(this.root!!, node)
    if (position.toInt() == -1) {
      return null
    }
    return getPosition(position)
  }

  fun getEndPosition(node: MethodTree): Position? {
    val position = this.pos.getEndPosition(this.root!!, node)
    if (position.toInt() == -1) {
      return null
    }
    return getPosition(position)
  }

  fun getPosition(position: Long): Position {
    val line = lines!!.getLineNumber(position).toInt()
    val column = lines!!.getColumnNumber(position).toInt()
    return Position(line, column).apply { index = position.toInt() }
  }
}
