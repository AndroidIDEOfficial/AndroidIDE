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
package com.itsaky.androidide.language

import com.itsaky.androidide.projects.ProjectManager.findModuleForFile
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.api.ILanguageServer
import com.itsaky.lsp.models.CompletionItem
import com.itsaky.lsp.models.CompletionParams
import com.itsaky.lsp.models.CompletionResult
import com.itsaky.lsp.models.FailureType.COMPLETION
import com.itsaky.lsp.models.LSPFailure
import com.itsaky.lsp.models.Position
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException
import io.github.rosemoe.sora.lang.completion.CompletionHelper
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.util.MyCharacter
import java.nio.file.Path
import java.util.function.*

/**
 * Common implementation of completion provider which requests completions to provided language
 * server.
 *
 * @author Akash Yadav
 */
class CommonCompletionProvider(private val server: ILanguageServer) {
  /**
   * Computes completion items using the provided language server instance.
   *
   * @param content The reference to the content of the editor.
   * @param file The file to compute completions for.
   * @param position The position of the cursor in the content.
   * @return The computed completion items. May return an empty list if the there was an error
   * computing the completion items.
   */
  fun complete(
    content: ContentReference?,
    file: Path,
    position: CharPosition,
    prefixMatcher: Predicate<Char?>
  ): List<CompletionItem> {
    val completionResult =
      try {
        val prefix =
          CompletionHelper.computePrefix(content, position) { t: Char -> prefixMatcher.test(t) }
        val completer = server.completionProvider
        if (completer.canComplete(file)) {
          var fileModule: Project? = null
          try {
            fileModule = findModuleForFile(file.toFile())
          } catch (e: Throwable) {
            if (e !is InterruptedException) {
              // This can occur if the completion was cancelled
              LOG.error("Unable to find module for current file", e)
            }
          }
          val params =
            CompletionParams(Position(position.line, position.column, position.index), file)
          params.content = content
          params.prefix = prefix
          params.module = fileModule
          completer.complete(params)
        } else {
          CompletionResult.EMPTY
        }
      } catch (e: Throwable) {
        // Do not log if completion was interrupted or cancelled
        if (!(e is InterruptedException || e is CompletionCancelledException)) {
          if (!server.handleFailure(LSPFailure(COMPLETION, e))) {
            LOG.error("Unable to compute completions", e)
          }
        }
        CompletionResult.EMPTY
      }

    if (completionResult == CompletionResult.EMPTY) {
      return listOf()
    }

    return completionResult.items
  }

  companion object {
    private val LOG = ILogger.newInstance("CommonCompletionProvider")
    @JvmStatic
    fun checkJavaCompletionChar(c: Char): Boolean {
      return MyCharacter.isJavaIdentifierPart(c.code) || c == '.'
    }

    @JvmStatic
    fun checkXMLCompletionChar(c: Char): Boolean {
      return MyCharacter.isJavaIdentifierPart(c.code) || c == '<' || c == '/'
    }
  }
}
