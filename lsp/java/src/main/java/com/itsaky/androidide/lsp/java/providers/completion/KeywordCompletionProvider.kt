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

package com.itsaky.androidide.lsp.java.providers.completion

import com.itsaky.androidide.lsp.api.IServerSettings
import com.itsaky.androidide.lsp.java.compiler.CompileTask
import com.itsaky.androidide.lsp.java.compiler.JavaCompilerService
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel.NO_MATCH
import com.itsaky.androidide.progress.ProgressManager.Companion.abortIfCancelled
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.MethodTree
import openjdk.source.tree.Tree
import openjdk.source.util.TreePath
import java.nio.file.Path

/**
 * Provides keyword completions.
 *
 * @author Akash Yadav
 */
class KeywordCompletionProvider(
  completingFile: Path,
  cursor: Long,
  compiler: JavaCompilerService,
  settings: IServerSettings
) : IJavaCompletionProvider(cursor, completingFile, compiler, settings) {

  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {

    if (partial.isBlank()) {
      return CompletionResult.EMPTY
    }

    val level: Tree = findKeywordLevel(path)
    var keywords = arrayOf<String>()
    when (level) {
      is CompilationUnitTree -> keywords = TOP_LEVEL_KEYWORDS
      is ClassTree -> keywords = CLASS_BODY_KEYWORDS
      is MethodTree -> keywords = METHOD_BODY_KEYWORDS
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    val list = mutableListOf<CompletionItem>()
    for (k in keywords) {
      val matchLevel = matchLevel(k, partial)
      if (matchLevel == NO_MATCH) {
        continue
      }

      list.add(keyword(k, partial, 100))
    }

    return CompletionResult(list)
  }

  private fun findKeywordLevel(treePath: TreePath): Tree {
    var path: TreePath? = treePath
    while (path != null) {
      if (path.leaf is CompilationUnitTree || path.leaf is ClassTree || path.leaf is MethodTree) {
        return path.leaf
      }
      path = path.parentPath
    }
    throw RuntimeException("empty path")
  }

  companion object {
    private val TOP_LEVEL_KEYWORDS =
      arrayOf(
        "package",
        "import",
        "public",
        "private",
        "protected",
        "abstract",
        "class",
        "interface",
        "@interface",
        "extends",
        "implements"
      )
    private val CLASS_BODY_KEYWORDS =
      arrayOf(
        "public",
        "private",
        "protected",
        "static",
        "final",
        "native",
        "synchronized",
        "abstract",
        "default",
        "class",
        "interface",
        "void",
        "boolean",
        "int",
        "long",
        "float",
        "double",
        "true",
        "false",
        "null"
      )
    private val METHOD_BODY_KEYWORDS =
      arrayOf(
        "new",
        "assert",
        "try",
        "catch",
        "finally",
        "throw",
        "return",
        "break",
        "case",
        "continue",
        "default",
        "do",
        "while",
        "for",
        "switch",
        "if",
        "else",
        "instanceof",
        "var",
        "final",
        "class",
        "void",
        "boolean",
        "int",
        "long",
        "float",
        "double",
        "synchronized",
        "true",
        "false",
        "null"
      )
  }
}
