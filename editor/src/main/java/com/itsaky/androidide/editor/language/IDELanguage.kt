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
package com.itsaky.androidide.editor.language

import android.os.Bundle
import com.itsaky.androidide.editor.api.IEditor
import com.itsaky.androidide.editor.ui.IDECompletionPublisher
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.preferences.internal.EditorPreferences
import com.itsaky.androidide.progress.ICancelChecker
import io.github.rosemoe.sora.lang.Language
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException
import io.github.rosemoe.sora.lang.completion.CompletionPublisher
import io.github.rosemoe.sora.lang.format.Formatter
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference
import org.slf4j.LoggerFactory
import java.nio.file.Paths

/**
 * Base class for language implementations in the IDE.
 *
 * @author Akash Yadav
 */
abstract class IDELanguage : Language {

  private var formatter: Formatter? = null

  protected open val languageServer: ILanguageServer?
    get() = null

  open fun getTabSize(): Int {
    return EditorPreferences.tabSize
  }

  @Throws(CompletionCancelledException::class)
  override fun requireAutoComplete(
    content: ContentReference,
    position: CharPosition,
    publisher: CompletionPublisher,
    extraArguments: Bundle
  ) {
    try {
      val cancelChecker = CompletionCancelChecker(publisher)
      Lookup.getDefault().register(ICancelChecker::class.java, cancelChecker)
      doComplete(content, position, publisher, cancelChecker, extraArguments)
    } finally {
      Lookup.getDefault().unregister(
        ICancelChecker::class.java)
    }
  }

  private fun doComplete(
    content: ContentReference,
    position: CharPosition,
    publisher: CompletionPublisher,
    cancelChecker: CompletionCancelChecker,
    extraArguments: Bundle
  ) {
    val server = languageServer ?: return
    val path = extraArguments.getString(IEditor.KEY_FILE, null)
    if (path == null) {
      log.warn("Cannot provide completions. No file provided.")
      return
    }

    val completionProvider = CommonCompletionProvider(server, cancelChecker)
    val file = Paths.get(path)
    val completionItems = completionProvider.complete(content, file,
      position) { checkIsCompletionChar(it) }
    publisher.setUpdateThreshold(1)
    (publisher as IDECompletionPublisher).addLSPItems(completionItems)
  }

  /**
   * Check if the given character is a completion character.
   *
   * @param c The character to check.
   * @return `true` if the character is completion char, `false` otherwise.
   */
  protected open fun checkIsCompletionChar(c: Char): Boolean {
    return false
  }

  override fun useTab(): Boolean {
    return !EditorPreferences.useSoftTab
  }

  override fun getFormatter(): Formatter {
    return formatter ?: LSPFormatter(languageServer).also { formatter = it }
  }

  override fun getIndentAdvance(
    content: ContentReference,
    line: Int,
    column: Int
  ): Int {
    return getIndentAdvance(content.getLine(line).substring(0, column))
  }

  open fun getIndentAdvance(line: String): Int {
    return 0
  }

  companion object {

    private val log = LoggerFactory.getLogger(IDELanguage::class.java)
  }
}