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

import androidx.core.util.Pair
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.LineMap
import openjdk.source.tree.MethodTree
import openjdk.source.util.TreePath
import openjdk.source.util.TreePathScanner
import openjdk.source.util.Trees
import openjdk.tools.javac.api.JavacTaskImpl
import org.slf4j.LoggerFactory

/**
 * Visits all methods and adds them to the given list of pair of method range and its tree.
 *
 * @author Akash Yadav
 */
class MethodRangeScanner(val task: JavacTaskImpl) :
  TreePathScanner<Unit, MutableList<Pair<Range, TreePath>>>() {

  var root: CompilationUnitTree? = null
  var lines: LineMap? = null
  val pos = Trees.instance(task).sourcePositions

  companion object {

    private val log = LoggerFactory.getLogger(MethodRangeScanner::class.java)
  }

  override fun visitCompilationUnit(
    node: CompilationUnitTree?,
    p: MutableList<Pair<Range, TreePath>>?
  ) {
    this.root = node
    this.lines = node?.lineMap
    return super.visitCompilationUnit(node, p)
  }

  override fun visitMethod(node: MethodTree?, list: MutableList<Pair<Range, TreePath>>) {
    // Do not call super.visitMethod
    // We only want methods defined directly in declared (not anonymous) classes.
    if (node == null || this.root == null) {
      return
    }

    val start = getStartPosition(node)
    val end = getEndPosition(node)

    if (start == null || end == null) {
      log.warn("Method '{}' skipped. Invalid position.", node.name)
      return
    }

    list.add(Pair.create(Range(start, end), currentPath))
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
