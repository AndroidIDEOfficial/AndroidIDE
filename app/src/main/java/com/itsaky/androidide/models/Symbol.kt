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
package com.itsaky.androidide.models

/**
 * A symbol that is shown in the [SymbolInputView][com.itsaky.androidide.ui.SymbolInputView].
 *
 * @author Akash Yadav
 */
open class Symbol @JvmOverloads constructor(
  open val label: String,
  open val commit: String,
  open val offset: Int = 1
) {
  @JvmOverloads
  constructor(both: String, offset: Int = 1) : this(both, both, offset)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Symbol) return false

    if (label != other.label) return false
    if (commit != other.commit) return false
    if (offset != other.offset) return false

    return true
  }

  override fun hashCode(): Int {
    var result = label.hashCode()
    result = 31 * result + commit.hashCode()
    result = 31 * result + offset
    return result
  }

  override fun toString(): String {
    return "Symbol(label='$label', commit='$commit', offset=$offset)"
  }
}