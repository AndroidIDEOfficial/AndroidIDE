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
 * Represents a line-column based text edit. The text in the given [range] must be replaced with the
 * [newText].
 */
data class TextEdit(var range: Range, var newText: String) {
  companion object {
    @JvmField val NONE: TextEdit = TextEdit(Range.NONE, "")
  }
}

/**
 * Represents an index-based text edit. The text from index [start] to [end] must be replaced with
 * [newText].
 */
data class IndexedTextEdit @JvmOverloads constructor(var start: Int = -1, var end: Int = -1, var newText: CharSequence = "") {
  companion object {
    @JvmStatic
    val NONE = IndexedTextEdit()
  }
}
