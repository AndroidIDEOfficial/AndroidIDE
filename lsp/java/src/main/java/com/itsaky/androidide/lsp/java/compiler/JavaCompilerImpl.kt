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

import com.itsaky.androidide.javac.services.compiler.ReusableContext
import com.itsaky.androidide.javac.services.compiler.ReusableJavaCompiler
import com.itsaky.androidide.lsp.java.parser.ts.TSJavaParser
import com.itsaky.androidide.lsp.java.parser.ts.TSMethodPruner.prune
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.utils.VMUtils
import com.itsaky.androidide.utils.withStopWatch
import jdkx.tools.JavaFileObject
import jdkx.tools.JavaFileObject.Kind.SOURCE
import openjdk.tools.javac.api.ClientCodeWrapper
import openjdk.tools.javac.tree.JCTree.JCCompilationUnit
import openjdk.tools.javac.util.Context
import kotlin.io.path.name

class JavaCompilerImpl(context: Context?) : ReusableJavaCompiler(context) {

  override fun parse(filename: JavaFileObject?, content: CharSequence?): JCCompilationUnit {

    if (VMUtils.isJvm()) {
      return super.parse(filename, content)
    }

    val file = ClientCodeWrapper.instance(context).unwrap(filename)
    val compilerConfig = JavaCompilerConfig.instance(context)

    // Preconditions
    if (
      content == null ||
        compilerConfig.files == null ||
        filename?.kind != SOURCE ||
        compilerConfig.files?.contains(file) == false
    ) {
      return super.parse(filename, content)
    }

    // If the file is NOT being parsed for a completion request,
    // we should not prune method bodies of active documents
    if (compilerConfig.completionInfo == null && FileManager.isActive(filename.toUri())) {
      return super.parse(filename, content)
    }

    val pruned = withStopWatch("${if(file is SourceFileObject) "[${file.path.name}] " else ""}Prune method bodies") { watch ->
      val contentBuilder = StringBuilder(content)

      return@withStopWatch TSJavaParser.parse(file).use { parseResult ->

        prune(
          contentBuilder,
          parseResult.tree,
          compilerConfig.completionInfo?.cursor?.index ?: -1
        )

        watch.log()

        return@use contentBuilder
      }
    }

    return super.parse(filename, pruned)
  }

  companion object {
    @JvmStatic
    fun preRegister(context: ReusableContext, replace: Boolean = false) {
      if (replace) {
        context.drop(compilerKey)
      }
      context.put(compilerKey, Context.Factory { JavaCompilerImpl(it) })
    }
  }
}
