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

package com.itsaky.androidide.editor.language.treesitter

import android.os.Bundle
import com.itsaky.androidide.editor.language.IDELanguage
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import io.github.rosemoe.sora.editor.ts.TsLanguage
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import io.github.rosemoe.sora.lang.completion.CompletionPublisher
import io.github.rosemoe.sora.lang.format.Formatter
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.widget.SymbolPairMatch

/**
 * Tree Sitter language implementation.
 *
 * @author Akash Yadav
 */
class TreeSitterLanguage(
  private val lang: IDELanguage,
  languageSpec: TsLanguageSpec,
  type: String,
) :
  TsLanguage(
    languageSpec = languageSpec,
    tab = false,
    themeDescription = {
      IDEColorSchemeProvider.readScheme { scheme ->
        val langScheme =
          checkNotNull(scheme.languages[type]) { "No color scheme found for file type '$type'" }
        langScheme.styles.forEach { it.value.makeStyle() applyTo it.key }
      }
    }
  ) {

  override fun getInterruptionLevel(): Int {
    return lang.interruptionLevel
  }

  override fun requireAutoComplete(
    content: ContentReference,
    position: CharPosition,
    publisher: CompletionPublisher,
    extraArguments: Bundle
  ) {
    lang.requireAutoComplete(content, position, publisher, extraArguments)
  }

  override fun useTab(): Boolean {
    return lang.useTab()
  }

  override fun getFormatter(): Formatter {
    return lang.formatter
  }

  override fun getSymbolPairs(): SymbolPairMatch {
    return lang.symbolPairs
  }

  override fun getNewlineHandlers(): Array<NewlineHandler> {
    return lang.newlineHandlers ?: emptyArray()
  }

  override fun destroy() {
    super.destroy()
    lang.destroy()
  }
}
