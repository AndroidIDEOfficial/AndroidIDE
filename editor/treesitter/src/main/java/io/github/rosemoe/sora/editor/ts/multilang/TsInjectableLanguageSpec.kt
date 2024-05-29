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

package io.github.rosemoe.sora.editor.ts.multilang

import com.itsaky.androidide.treesitter.TSLanguage
import io.github.rosemoe.sora.editor.ts.LocalsCaptureSpec
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import io.github.rosemoe.sora.editor.ts.TsThemeBuilder
import io.github.rosemoe.sora.editor.ts.predicate.TsPredicate
import io.github.rosemoe.sora.editor.ts.predicate.builtin.MatchPredicate

class TsInjectableLanguageSpec(
  language: TSLanguage,
  highlightScmSource: String,
  themeDescription: TsThemeBuilder.() -> Unit,
  val languageName: LanguagePriorityCheck,
  val indentHelper: TsIndentHelper? = null,
  codeBlocksScmSource: String = "",
  bracketsScmSource: String = "",
  localsScmSource: String = "",
  localsCaptureSpec: LocalsCaptureSpec = LocalsCaptureSpec.DEFAULT,
  predicates: List<TsPredicate> = listOf(MatchPredicate)
) : TsLanguageSpec(language, highlightScmSource, codeBlocksScmSource, bracketsScmSource,
  localsScmSource, localsCaptureSpec, predicates) {

  var theme = TsThemeBuilder(tsQuery).apply { themeDescription() }.theme

  fun updateTheme(themeDescription: TsThemeBuilder.() -> Unit) = run {
    if (closed) {
      throw IllegalStateException("spec is closed")
    }
    theme = TsThemeBuilder(tsQuery).apply { themeDescription() }.theme
  }

}