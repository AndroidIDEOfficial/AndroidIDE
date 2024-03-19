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

import com.itsaky.androidide.treesitter.TSLanguage
import com.itsaky.androidide.treesitter.TSQuery
import com.itsaky.androidide.treesitter.TSQueryError
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import java.io.Closeable

/**
 * Extension of [TsLanguageSpec] for AndroidIDE.
 *
 * @author Akash Yadav
 */
class TreeSitterLanguageSpec
@JvmOverloads constructor(
  val spec: TsLanguageSpec,
  indentsQueryScm: String = ""
) : Closeable {

  // <editor-fold desc="Proxy properties">
  val language: TSLanguage
    get() = spec.language
  // </editor-fold>

  val indentsQuery: TSQuery? = if (indentsQueryScm.isBlank()) {
    TSQuery.EMPTY
  } else {
    TSQuery.create(language, indentsQueryScm)
      .let { if (it.canAccess()) it else null }
  }

  init {
    indentsQuery?.validateOrThrow(name = "indents")
  }

  override fun close() {
    indentsQuery?.close()
    if (spec.language.isExternal) {
      spec.language.close()
    }
    spec.close()
  }
}

private fun TSQuery.validateOrThrow(name: String) {
  if (errorType != TSQueryError.None) {
    throw IllegalArgumentException(
      "query(name:$name) parsing failed: ${errorType.name} at text offset $errorOffset")
  }
}
