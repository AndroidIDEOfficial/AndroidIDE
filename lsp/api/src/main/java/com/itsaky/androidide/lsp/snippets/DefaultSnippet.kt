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

package com.itsaky.androidide.lsp.snippets

/**
 * Java snippet.
 *
 * @author Akash Yadav
 */
data class DefaultSnippet(
  override val prefix: String,
  override val description: String,
  override val body: Array<String>
) : ISnippet {

  constructor(
    prefix: String,
    description: String,
    content: () -> Array<String>
  ) : this(prefix, description, content())

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DefaultSnippet) return false

    if (description != other.description) return false
    if (prefix != other.prefix) return false
    if (!body.contentEquals(other.body)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = prefix.hashCode()
    result = 31 * result + description.hashCode()
    result = 31 * result + body.contentHashCode()
    return result
  }
}
