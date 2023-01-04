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

import android.content.Context
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguageSpec
import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.editor.ts.LocalsCaptureSpec
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec

/**
 * Provides language spec instances for tree sitter languages.
 *
 * @author Akash Yadav
 */
object LanguageSpecProvider {

  private const val BASE_SPEC_PATH = "editor/treesitter"
  private val log = ILogger.newInstance("LanguageSpecProvider")
  
  @JvmStatic
  @JvmOverloads
  fun getLanguageSpec(
    context: Context,
    type: String,
    lang: TSLanguage,
    localsCaptureSpec: LocalsCaptureSpec = LocalsCaptureSpec.DEFAULT
  ): TreeSitterLanguageSpec {
    val editorLangSpec = TsLanguageSpec(
      language = lang,
      highlightScmSource = readScheme(context, type, "highlights"),
      localsScmSource = readScheme(context, type, "locals"),
      codeBlocksScmSource = readScheme(context, type, "blocks"),
      bracketsScmSource = readScheme(context, type, "brackets"),
      localsCaptureSpec = localsCaptureSpec
    )
    return TreeSitterLanguageSpec(spec = editorLangSpec, indentsQueryScm = readScheme(context, type, "indents"))
  }

  private fun readScheme(context: Context, type: String, name: String): String {
    val assests = context.assets
    if (assests.list(BASE_SPEC_PATH)?.contains(type) == false) {
      log.warn("No scheme files defined for type '$type'")
      return ""
    }
    if (assests.list("$BASE_SPEC_PATH/$type")?.contains("$name.scm") == false) {
      log.warn("Scheme file '$name' for type '$type' not found")
      return ""
    }
    return assests.open("${BASE_SPEC_PATH}/${type}/${name}.scm").reader().readText()
  }
}
