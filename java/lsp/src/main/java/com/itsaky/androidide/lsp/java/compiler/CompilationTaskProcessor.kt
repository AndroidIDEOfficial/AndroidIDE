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

package com.itsaky.androidide.lsp.java.compiler

import openjdk.source.tree.CompilationUnitTree
import openjdk.tools.javac.api.JavacTaskImpl
import java.util.function.*

/**
 * A compilation task processor process the [JavacTaskImpl]. Usually, a processor decides what files
 * should be parsed and analyzed.
 *
 * @author Akash Yadav
 */
fun interface CompilationTaskProcessor {

  /**
   * Process the given [JavacTaskImpl]. The processor is responsible for parsing and analyzing the
   * task. For each parsed [CompilationUnitTree], [processCompilationUnit] must be called.
   */
  @Throws(Throwable::class)
  fun process(task: JavacTaskImpl, processCompilationUnit: Consumer<CompilationUnitTree>)
}
