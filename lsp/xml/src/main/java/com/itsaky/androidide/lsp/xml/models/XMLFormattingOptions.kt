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

package com.itsaky.androidide.lsp.xml.models

import com.itsaky.androidide.lsp.xml.providers.format.FormatElementCategory
import com.itsaky.androidide.lsp.xml.providers.format.FormatElementCategory.PreserveSpace
import org.eclipse.lemminx.dom.DOMElement

/**
 * Options for XML code formatting.
 *
 * @author Akash Yadav
 */
open class XMLFormattingOptions {
  open val trimFinalNewLine: Boolean
    get() = com.itsaky.androidide.preferences.internal.trimFinalNewLine
  open val insertFinalNewLine: Boolean
    get() = com.itsaky.androidide.preferences.internal.insertFinalNewLine
  open val splitAttributes: Boolean
    get() = com.itsaky.androidide.preferences.internal.splitAttributes
  open val joinCDataLines: Boolean
    get() = com.itsaky.androidide.preferences.internal.joinCDataLines
  open val joinCommentLines: Boolean
    get() = com.itsaky.androidide.preferences.internal.joinCommentLines
  open val joinContentLines: Boolean
    get() = com.itsaky.androidide.preferences.internal.joinContentLines
  open val spaceBeforeEmptyCloseTag: Boolean
    get() = com.itsaky.androidide.preferences.internal.spaceBeforeEmptyCloseTag
  open val preserveEmptyContent: Boolean
    get() = com.itsaky.androidide.preferences.internal.preserveEmptyContent
  open val preserveAttributeLineBreaks: Boolean
    get() = com.itsaky.androidide.preferences.internal.preserveAttributeLineBreaks
  open val closingBracketNewLine: Boolean
    get() = com.itsaky.androidide.preferences.internal.closingBracketNewLine
  open val trimTrailingWhitespace: Boolean
    get() = com.itsaky.androidide.preferences.internal.trimTrailingWhitespace

  open val maxLineWidth: Int
    get() = com.itsaky.androidide.preferences.internal.maxLineWidth
  open val preservedNewLines: Int
    get() = com.itsaky.androidide.preferences.internal.preservedNewLines
  open val splitAttributesIndentSize: Int
    get() = com.itsaky.androidide.preferences.internal.splitAttributesIndentSize

  open val emptyElementsBehavior: EmptyElements
    get() = EmptyElements.valueOf(com.itsaky.androidide.preferences.internal.emptyElementsBehavior)

  private val preserveSpace =
    listOf(
      "xsl:text",
      "xsl:comment",
      "xsl:processing-instruction",
      "literallayout",
      "programlisting",
      "screen",
      "synopsis",
      "pre",
      "xd:pre"
    )

  fun getFormatElementCategory(element: DOMElement): FormatElementCategory? {
    return preserveSpace.find { it == element.tagName }?.let { PreserveSpace }
  }
}

enum class EmptyElements {
  Expand,
  Collapse,
  Ignore
}
