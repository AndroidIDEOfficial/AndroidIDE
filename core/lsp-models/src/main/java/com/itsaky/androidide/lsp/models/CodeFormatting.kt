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

package com.itsaky.androidide.lsp.models

import com.itsaky.androidide.models.Range

/**
 * Parameters containing data required to format source code.
 *
 * @author Akash Yadav
 */
data class FormatCodeParams
@JvmOverloads
constructor(val content: CharSequence, val range: Range = Range.NONE)

/** The code formatting result. */
data class CodeFormatResult
@JvmOverloads
constructor(
  val isIndexed: Boolean = false,
  val edits: MutableList<out TextEdit> = mutableListOf(),
  val indexedTextEdits: MutableList<IndexedTextEdit> = mutableListOf()
) {

  companion object {

    /** Represents no formatting changes. */
    @JvmField val NONE = CodeFormatResult()

    /** Create a [CodeFormatResult] which replaces the whole [content] with the [formatted] text. */
    @JvmStatic
    fun forWholeContent(content: CharSequence, formatted: CharSequence): CodeFormatResult {
      val replacements = mutableListOf<IndexedTextEdit>()
      replacements.add(IndexedTextEdit(0, content.length, formatted))
      return CodeFormatResult(true, indexedTextEdits = replacements)
    }
  }
}
