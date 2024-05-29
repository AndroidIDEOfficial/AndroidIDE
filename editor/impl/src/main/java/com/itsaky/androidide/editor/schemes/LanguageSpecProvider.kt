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
import com.itsaky.androidide.editor.language.treesitter.predicates.AnyOfPredicate
import com.itsaky.androidide.editor.language.treesitter.predicates.EqualPredicate
import com.itsaky.androidide.editor.language.treesitter.predicates.MatchPredicate
import com.itsaky.androidide.editor.language.treesitter.predicates.NotEqualPredicate
import com.itsaky.androidide.editor.language.treesitter.predicates.NotMatchPredicate
import com.itsaky.androidide.treesitter.TSLanguage
import io.github.rosemoe.sora.editor.ts.LocalsCaptureSpec
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException

/**
 * Provides language spec instances for tree sitter languages.
 *
 * @author Akash Yadav
 */
object LanguageSpecProvider {

  private const val BASE_SPEC_PATH = "editor/treesitter"
  private val log = LoggerFactory.getLogger(LanguageSpecProvider::class.java)

  @JvmStatic
  @JvmOverloads
  fun getLanguageSpec(
    context: Context,
    type: String,
    lang: TSLanguage,
    localsCaptureSpec: LocalsCaptureSpec = LocalsCaptureSpec.DEFAULT
  ): TreeSitterLanguageSpec {
    val editorLangSpec =
      TsLanguageSpec(
        language = lang,
        highlightScmSource = readScheme(context, type, "highlights"),
        localsScmSource = readScheme(context, type, "locals"),
        codeBlocksScmSource = readScheme(context, type, "blocks"),
        bracketsScmSource = readScheme(context, type, "brackets"),
        localsCaptureSpec = localsCaptureSpec,
        predicates =
        listOf(
          MatchPredicate,
          NotMatchPredicate,
          EqualPredicate,
          NotEqualPredicate,
          AnyOfPredicate
        )
      )
    return TreeSitterLanguageSpec(
      spec = editorLangSpec,
      indentsQueryScm = readScheme(context, type, "indents")
    )
  }

  private fun readScheme(context: Context, type: String, name: String): String {
    return try {
      context.assets.open("${BASE_SPEC_PATH}/${type}/${name}.scm").reader().readText()
    } catch (e: Exception) {
      if (e !is FileNotFoundException) {
        // log everything except FileNotFoundException
        log.error("Failed to read scheme file {} for type {}", name, type, e)
      }
      ""
    }
  }
}
