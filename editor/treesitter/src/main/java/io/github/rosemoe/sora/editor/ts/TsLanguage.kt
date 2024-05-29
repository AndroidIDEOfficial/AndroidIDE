/*******************************************************************************
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2023  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 ******************************************************************************/

package io.github.rosemoe.sora.editor.ts

import android.os.Bundle
import io.github.rosemoe.sora.lang.EmptyLanguage
import io.github.rosemoe.sora.lang.Language
import io.github.rosemoe.sora.lang.completion.CompletionPublisher
import io.github.rosemoe.sora.lang.format.Formatter
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler
import io.github.rosemoe.sora.text.CharPosition
import io.github.rosemoe.sora.text.ContentReference
import io.github.rosemoe.sora.widget.SymbolPairMatch

/**
 * Tree-sitter based language.
 *
 * @param languageSpec The language specification for parsing and highlighting
 * @param themeDescription Theme for colorizing nodes
 * @param tab whether tab should be used
 *
 * @see TsTheme
 * @see TsLanguageSpec
 *
 * @author Rosemoe
 */
open class TsLanguage(
  val languageSpec: TsLanguageSpec,
  val tab: Boolean = false,
  themeDescription: TsThemeBuilder.() -> Unit
) : Language {

  init {
    if (languageSpec.closed) {
      throw IllegalStateException("spec is closed")
    }
  }

  protected var tsTheme = TsThemeBuilder(languageSpec.tsQuery).apply { themeDescription() }.theme

  open val analyzer by lazy {
    TsAnalyzeManager(languageSpec, tsTheme)
  }

  /**
   * Update tree-sitter colorizing theme with the given description
   */
  fun updateTheme(themeDescription: TsThemeBuilder.() -> Unit) = languageSpec.let {
    if (it.closed) {
      throw IllegalStateException("spec is closed")
    }
    updateTheme(TsThemeBuilder(languageSpec.tsQuery).apply { themeDescription() }.theme)
  }

  /**
   * Update tree-sitter colorizing theme
   */
  fun updateTheme(theme: TsTheme) {
    this.tsTheme = theme
    analyzer.updateTheme(theme)
  }

  override fun getAnalyzeManager() = analyzer

  override fun getInterruptionLevel() = Language.INTERRUPTION_LEVEL_STRONG

  override fun requireAutoComplete(
    content: ContentReference,
    position: CharPosition,
    publisher: CompletionPublisher,
    extraArguments: Bundle
  ) {
    // Nothing
  }

  override fun getIndentAdvance(content: ContentReference, line: Int, column: Int) = 0

  override fun useTab() = tab

  override fun getFormatter(): Formatter = EmptyLanguage.EmptyFormatter.INSTANCE

  override fun getSymbolPairs(): SymbolPairMatch = EmptyLanguage.EMPTY_SYMBOL_PAIRS

  override fun getNewlineHandlers() = emptyArray<NewlineHandler>()

  override fun destroy() {
    languageSpec.close()
  }

}