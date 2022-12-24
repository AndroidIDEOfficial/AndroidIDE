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

package com.itsaky.androidide.editor.schemes

import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

class IDEColorScheme : EditorColorScheme() {

  internal val colorIds = mutableMapOf<Int, Int>()
  internal val editorScheme = mutableMapOf<Int, Int>()
  internal val languages = mutableMapOf<String, LanguageScheme>()

  private var colorId = END_COLOR_ID

  var isDarkScheme: Boolean = false
    internal set

  var definitions: Map<String, Int> = emptyMap()
    internal set

  fun getLanguageScheme(type: String): LanguageScheme? {
    return this.languages[type]
  }

  internal fun putColor(color: Int): Int {
    this.colorIds[++colorId] = color
    return colorId
  }

  @Suppress("UNNECESSARY_SAFE_CALL")
  override fun getColor(type: Int): Int {
    // getColor is called in superclass constructor
    // in this case, the below properties will be null
    return this.editorScheme?.get(type) ?: this.colorIds?.get(type) ?: super.getColor(type)
  }
}

/**
 * Color scheme for a language.
 *
 * @property files The file types for this language color scheme.
 * @property styles The highlight styles.
 * @author Akash Yadav
 */
data class LanguageScheme(val files: List<String>, val styles: Map<String, StyleDef>)

/**
 * A color scheme style definition.
 *
 * @property fg The foreground color.
 * @property bg The background color.
 * @property bold Whether the highlighted region should have bold text.
 * @property italic Whether the highlighted region should have italic text.
 * @property strikeThrough Whether the highlighted region should have strikethrough text.
 * @property completion Whether code completions can be performed in the highlighted region.
 */
data class StyleDef(
  var fg: Int,
  var bg: Int = 0,
  var bold: Boolean = false,
  var italic: Boolean = false,
  var strikeThrough: Boolean = false,
  var completion: Boolean = true
)
