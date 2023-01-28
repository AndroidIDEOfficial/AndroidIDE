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

import com.itsaky.androidide.utils.StopWatch
import openjdk.source.tree.CompilationUnitTree
import openjdk.tools.javac.api.JavacTaskImpl
import java.util.function.Consumer

/**
 * Default implementation of [CompilationTaskProcessor].
 *
 * @author Akash Yadav
 */
class DefaultCompilationTaskProcessor : CompilationTaskProcessor {

  override fun process(task: JavacTaskImpl, processCompilationUnit: Consumer<CompilationUnitTree>) {
    val watch = StopWatch("Process compilation task")
    val trees = task.parse()
    watch.lapFromLast("Parsed treees")

    trees.forEach(processCompilationUnit::accept)
    watch.lapFromLast("Processed trees")
    
//    val entered = JavacTaskUtil.enterTrees(task, trees)
//    watch.lapFromLast("Entered trees")
//
//    val analyzed = JavacTaskUtil.analyze(task, entered)
    task.analyze()
    watch.lapFromLast("Analyzed all trees")
  }
}
