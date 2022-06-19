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

package com.itsaky.androidide.language.incremental

import com.itsaky.lsp.models.DiagnosticItem

/**
 * Tokenization state of a line.
 *
 * @author Akash Yadav
 */
class LineState {

  companion object {
    const val NORMAL = 0
    const val INCOMPLETE = 1
  }

  @JvmField var state = NORMAL
  @JvmField var hasBraces = false
  @JvmField var diagnostics: DiagnosticsState? = null

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LineState

    if (state != other.state) return false
    if (hasBraces != other.hasBraces) return false
    if (diagnostics != other.diagnostics) return false

    return true
  }

  override fun hashCode(): Int {
    var result = state
    result = 31 * result + hasBraces.hashCode()
    result = 31 * result + (diagnostics?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "LineState(state=$state, hasBraces=$hasBraces, diagnostics=$diagnostics)"
  }
}

data class DiagnosticsState(
  val continueDiagnostic: Boolean = false,
  val diagnostic: DiagnosticItem? = null
)
