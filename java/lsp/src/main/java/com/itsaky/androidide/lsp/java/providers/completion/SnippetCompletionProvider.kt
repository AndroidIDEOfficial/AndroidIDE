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
import com.itsaky.androidide.lsp.java.providers.snippet.JavaSnippetRepository
import com.itsaky.androidide.lsp.java.providers.snippet.JavaSnippetScope
import com.itsaky.androidide.lsp.models.CompletionItem
import com.itsaky.androidide.lsp.models.CompletionResult
import com.itsaky.androidide.lsp.models.MatchLevel
import com.itsaky.androidide.lsp.snippets.ISnippet
import com.itsaky.androidide.preferences.internal.EditorPreferences
import io.github.rosemoe.sora.text.TextUtils
import openjdk.source.tree.ClassTree
import openjdk.source.tree.CompilationUnitTree
import openjdk.source.tree.MethodTree
import openjdk.source.util.TreePath
import java.nio.file.Path

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
    val snippets = mutableListOf<ISnippet>()

    // add global snippets, if any
    JavaSnippetRepository.snippets[JavaSnippetScope.GLOBAL]?.let { snippets.addAll(it) }

    val snippetScope =
      when (scope.leaf) {
        is CompilationUnitTree -> JavaSnippetScope.TOP_LEVEL
        is ClassTree -> JavaSnippetScope.MEMBER
        is MethodTree -> JavaSnippetScope.LOCAL
        else -> null
      }

    // add snippets for the current scope
    snippetScope?.let { JavaSnippetRepository.snippets[it]?.let { list -> snippets.addAll(list) } }

    val items = mutableListOf<CompletionItem>()

    for (snippet in snippets) {
      val matchLevel = matchLevel(snippet.prefix, partial)
      if (matchLevel == MatchLevel.NO_MATCH) {
        continue
      }

      items.add(snippetItem(snippet, matchLevel, partial, indent))
    }

    return CompletionResult(items)
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
    return TextUtils.countLeadingSpaceCount(charContent.substring(start, cursor.toInt()),
      EditorPreferences.tabSize)
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
