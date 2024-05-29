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

package com.itsaky.androidide.editor.language.utils

import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.widget.SymbolPairMatch
import io.github.rosemoe.sora.widget.SymbolPairMatch.SymbolPair.SymbolPairEx

/**
 * Common symbol pairs that can be used in any language.
 *
 * @author Akash Yadav
 */
internal open class CommonSymbolPairs : SymbolPairMatch() {

  private val isSelected =
    object : SymbolPairEx {
      override fun shouldDoAutoSurround(content: Content?): Boolean {
        return content?.cursor?.isSelected ?: false
      }
    }

  init {
    super.putPair('{', SymbolPair("{", "}"))
    super.putPair('(', SymbolPair("(", ")"))
    super.putPair('[', SymbolPair("[", "]"))
    super.putPair('"', SymbolPair("\"", "\"", isSelected))
    super.putPair('\'', SymbolPair("'", "'", isSelected))
  }
}
