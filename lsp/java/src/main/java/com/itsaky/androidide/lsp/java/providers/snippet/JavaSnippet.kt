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

package com.itsaky.androidide.lsp.java.providers.snippet

/**
 * Java snippet.
 *
 * @author Akash Yadav
 */
data class JavaSnippet(
  val prefix: String,
  val label: String,
  val description: String,
  val content: Array<String>
) {

  constructor(
    prefix: String,
    label: String,
    description: String,
    content: () -> Array<String>
  ) : this(prefix, label, description, content())

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is JavaSnippet) return false

    if (label != other.label) return false
    if (description != other.description) return false
    if (prefix != other.prefix) return false
    if (!content.contentEquals(other.content)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = label.hashCode()
    result = 31 * result + description.hashCode()
    result = 31 * result + prefix.hashCode()
    result = 31 * result + content.contentHashCode()
    return result
  }
}
