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

import com.itsaky.androidide.editor.schemes.LanguageScheme
import io.github.rosemoe.sora.editor.ts.TsAnalyzeManager
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import io.github.rosemoe.sora.editor.ts.TsTheme
import io.github.rosemoe.sora.lang.styling.Styles

/**
 * [TsAnalyzeManager] implementation for tree sitter languages.
 *
 * @author Akash Yadav
 */
class TreeSitterAnalyzeManager(
  languageSpec: TsLanguageSpec,
  theme: TsTheme
) : TsAnalyzeManager(languageSpec, theme) {

  override var styles: Styles = Styles()
    set(value) {
      field = value
      resetSpanFactory(value, langScheme)
    }

  internal var langScheme: LanguageScheme? = null
    set(value) {
      field = value
      resetSpanFactory(styles, value)
    }

  init {
    resetSpanFactory(styles, langScheme)
  }

  private fun resetSpanFactory(styles: Styles, langScheme: LanguageScheme?) {
    spanFactory = TreeSitterSpanFactory(reference, languageSpec.tsQuery, styles, langScheme)
  }
}