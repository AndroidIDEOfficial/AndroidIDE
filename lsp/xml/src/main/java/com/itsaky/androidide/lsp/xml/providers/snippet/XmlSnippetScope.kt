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

package com.itsaky.androidide.lsp.xml.providers.snippet

import com.itsaky.androidide.lsp.snippets.ISnippetScope

val XML_SNIPPET_SCOPES : Array<IXmlSnippetScope> =
  arrayOf(
    DefaultXmlSnippetScope(XmlResourceType.LAYOUT, XmlScope.TAG),
    DefaultXmlSnippetScope(XmlResourceType.LAYOUT, XmlScope.INSIDE),
    DefaultXmlSnippetScope(XmlResourceType.MANIFEST, XmlScope.INSIDE)
  )

abstract class IXmlSnippetScope : ISnippetScope {
  abstract val type: XmlResourceType
  abstract val scope: XmlScope

  override val filename: String
    get() = "${type.name.lowercase()}-${scope.name.lowercase()}"
}

class DefaultXmlSnippetScope(override val type: XmlResourceType, override val scope: XmlScope) :
  IXmlSnippetScope()

enum class XmlResourceType {
  LAYOUT,
  MANIFEST
}

enum class XmlScope {
  TAG,
  INSIDE
}
