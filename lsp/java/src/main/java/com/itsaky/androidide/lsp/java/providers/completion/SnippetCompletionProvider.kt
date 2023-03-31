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
import com.itsaky.androidide.lsp.java.providers.snippet.JavaSnippet
import com.itsaky.androidide.lsp.java.providers.snippet.JavaSnippetRepository
import com.itsaky.androidide.lsp.java.providers.snippet.JavaSnippetScope
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.preferences.internal.tabSize
import io.github.rosemoe.sora.text.TextUtils
import java.nio.file.Path
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.MethodTree
import openjdk.source.util.TreePath

/**
 * Provides snippet completion for Java files.
 *
 * @author Akash Yadav
 */
class SnippetCompletionProvider(
  cursor: Long,
  completingFile: Path,
  compiler: JavaCompilerService,
  settings: IServerSettings
) : IJavaCompletionProvider(cursor, completingFile, compiler, settings) {

  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean
  ): CompletionResult {
    val scope = findSnippetScope(path) ?: return CompletionResult.EMPTY
    val indent = spacesBeforeCursor(task.root().sourceFile.getCharContent(true))
    return when (scope.leaf) {
      is CompilationUnitTree -> completeTopLevelSnippets(task, path, partial)
      is ClassTree -> completeMemberSnippets(task, path, partial)
      is MethodTree -> completeLocalSnippets(task, path, partial, indent)
      else -> CompletionResult.EMPTY
    }
  }

  private fun spacesBeforeCursor(charContent: CharSequence?): Int {
    charContent ?: return 0
    var start = cursor.toInt()
    while (start >= 0) {
      val c = charContent[start]
      if (c == '\n' || !c.isWhitespace()) {
        break
      }
      --start
    }
    return TextUtils.countLeadingSpaceCount(charContent.substring(start, cursor.toInt()), tabSize)
  }

  private fun completeTopLevelSnippets(
    task: CompileTask,
    path: TreePath,
    partial: String
  ): CompletionResult {
    return CompletionResult.EMPTY
  }

  private fun completeMemberSnippets(
    task: CompileTask,
    path: TreePath,
    partial: String
  ): CompletionResult {
    return CompletionResult.EMPTY
  }

  private fun completeLocalSnippets(
    task: CompileTask,
    path: TreePath,
    partial: String,
    indent: Int
  ): CompletionResult {
    val items = mutableListOf<CompletionItem>()
    val snippets =
      mutableListOf<JavaSnippet>().apply {
        JavaSnippetRepository.snippets[JavaSnippetScope.LOCAL]?.let { addAll(it) }
        JavaSnippetRepository.snippets[JavaSnippetScope.GLOBAL]?.let { addAll(it) }
      }

    for (snippet in snippets) {
      val matchLevel = matchLevel(snippet.prefix, partial)
      if (matchLevel == MatchLevel.NO_MATCH) {
        continue
      }

      items.add(snippetItem(snippet, matchLevel, partial, indent))
    }

    return CompletionResult(items)
  }

  private fun findSnippetScope(path: TreePath?): TreePath? {
    var scope = path
    while (scope != null) {
      if (scope.leaf.let { it is CompilationUnitTree || it is ClassTree || it is MethodTree }) {
        return scope
      }
      scope = scope.parentPath
    }
    return null
  }
}
