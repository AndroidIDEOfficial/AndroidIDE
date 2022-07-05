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

package com.itsaky.lsp.java.partial

import com.sun.source.tree.CompilationUnitTree
import com.sun.tools.javac.api.JavacTaskImpl
import com.sun.tools.javac.api.JavacTrees
import javax.tools.DiagnosticListener
import javax.tools.JavaFileObject

/**
 * Information about a compilation.
 *
 * @author Akash Yadav
 */
data class CompilationInfo(
  @JvmField val task: JavacTaskImpl,
  @JvmField val diagnosticListener: DiagnosticListener<JavaFileObject>,
  @JvmField val cu: CompilationUnitTree,
) {
  val trees: JavacTrees = JavacTrees.instance(task)
}
