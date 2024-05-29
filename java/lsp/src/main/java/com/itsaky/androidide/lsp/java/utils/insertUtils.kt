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

package com.itsaky.androidide.lsp.java.utils

import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.parser.ParseTask
import com.itsaky.androidide.models.Position
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.Tree
import openjdk.source.util.JavacTask
import openjdk.source.util.Trees

/** @author Akash Yadav */
fun positionForImports(className: String, task: ParseTask): Position {
  return positionForImports(className, task.task, task.root)
}

fun positionForImports(className: String, task: CompileTask): Position {
  return positionForImports(className, task.task, task.root())
}

fun positionForImports(className: String, task: JavacTask, root: CompilationUnitTree): Position {
  val imports = root.imports
  for (i in imports) {
    val next = i.qualifiedIdentifier.toString()
    if (className < next) {
      return insertBefore(task, root, i)
    }
  }
  if (imports.isNotEmpty()) {
    val last = imports[imports.size - 1]
    return insertAfter(task, root, last)
  }

  return if (root.getPackage() != null) {
    insertAfter(task, root, root.getPackage())
  } else Position(0, 0)
}

fun insertBefore(task: JavacTask, root: CompilationUnitTree, tree: Tree): Position {
  val pos = Trees.instance(task).sourcePositions
  val offset = pos.getStartPosition(root, tree)
  val line = root.lineMap.getLineNumber(offset).toInt()
  return Position(line - 1, 0)
}

fun insertAfter(task: JavacTask, root: CompilationUnitTree, tree: Tree): Position {
  val pos = Trees.instance(task).sourcePositions
  val offset = pos.getStartPosition(root, tree)
  val line = root.lineMap.getLineNumber(offset).toInt()
  return Position(line, 0)
}
