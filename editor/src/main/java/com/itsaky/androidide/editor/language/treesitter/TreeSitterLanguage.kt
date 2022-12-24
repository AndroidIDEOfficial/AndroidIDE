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

import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.preferences.internal.useSoftTab
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.editor.ts.TsLanguage
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec

/**
 * Tree Sitter language implementation.
 *
 * @author Akash Yadav
 */
class TreeSitterLanguage(
  languageSpec: TsLanguageSpec,
  type: String,
) :
  TsLanguage(
    languageSpec = languageSpec,
    tab = false,
    themeDescription = {
      val scheme = IDEColorSchemeProvider.scheme
      val lang =
        checkNotNull(scheme.languages[type]) { "No color scheme found for file type '$type'" }
      val log = ILogger.newInstance("TreeSitterThemeBuilder")
      lang.styles.forEach {
        log.debug("${it.value.makeStyle()} applyTo ${it.key}")
        it.value.makeStyle() applyTo it.key }
    }
  ) {
  
  override fun useTab(): Boolean {
    return !useSoftTab
  }
}
