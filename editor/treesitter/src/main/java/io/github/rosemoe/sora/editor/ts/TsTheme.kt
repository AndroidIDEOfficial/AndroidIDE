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

import androidx.collection.MutableIntLongMap
import com.itsaky.androidide.treesitter.TSQuery
import io.github.rosemoe.sora.lang.styling.TextStyle
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme

/**
 * Theme for tree-sitter. This is different from [io.github.rosemoe.sora.widget.schemes.EditorColorScheme].
 * It is only used for colorizing spans in tree-sitter module. The real colors are still stored in editor
 * color schemes.
 * As what tree-sitter do, we try to match the longest scope.
 * For example, if 'variable' and 'variable.builtin' rule are both defined, Query 'variable.builtin'
 * and 'variable.builtin.this' will get 'variable.builtin' rule.
 * The theme also provide a fallback. You may call [TsTheme.putStyleRule] with a rule of length 0 to
 *  set fallback color scheme.
 * Note that colors of 'locals.definition', 'locals.reference', etc. can not be set by this theme object.
 *
 * @author Rosemoe
 */
class TsTheme(private val tsQuery: TSQuery) {

  private val styles = mutableMapOf<String, Long>()
  private val mapping = MutableIntLongMap()

  /**
   * The text style for normal texts
   */
  var normalTextStyle = TextStyle.makeStyle(EditorColorScheme.TEXT_NORMAL)

  /**
   * Set text style for the given rule string.
   *
   * @param rule The rule for locating nodes
   * @param style The style value for those nodes
   * @see io.github.rosemoe.sora.lang.styling.TextStyle
   */
  fun putStyleRule(rule: String, style: Long) {
    styles[rule] = style
    mapping.clear()
  }

  /**
   * Remove rule
   * @param rule The rule for locating nodes
   */
  fun eraseStyleRule(rule: String) = putStyleRule(rule, 0L)

  fun resolveStyleForPattern(pattern: Int): Long {
    return mapping.getOrElse(pattern) {
      var mappedName = tsQuery.getCaptureNameForId(pattern)
      var style = styles[mappedName] ?: 0L
      while (style == 0L && mappedName.isNotEmpty()) {
        mappedName = mappedName.substringBeforeLast('.', "")
        style = styles[mappedName] ?: 0L
      }
      mapping.put(pattern, style)
      style
    }
  }

}

/**
 * Builder class for tree-sitter themes
 */
class TsThemeBuilder(tsQuery: TSQuery) {

  internal val theme = TsTheme(tsQuery)

  infix fun Long.applyTo(targetRule: String) {
    theme.putStyleRule(targetRule, this)
  }

  infix fun Long.applyTo(targetRules: Array<String>) {
    targetRules.forEach {
      applyTo(it)
    }
  }

}

/**
 * Build tree-sitter theme
 */
fun tsTheme(tsQuery: TSQuery, description: TsThemeBuilder.() -> Unit) =
  TsThemeBuilder(tsQuery).also { it.description() }.theme