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
class XMLFormattingOptions {
  val trimFinalNewLine: Boolean = true
  val insertFinalNewLine: Boolean = false
  val splitAttributes: Boolean = true
  val joinCDataLines: Boolean = false
  val joinCommentLines: Boolean = false
  val joinContentLines: Boolean = false
  val spaceBeforeEmptyCloseTag: Boolean = true
  val preserveEmptyContent: Boolean = false
  val preserveAttributeLineBreaks: Boolean = true
  val closingBracketNewLine: Boolean = false
  val trimTrailingWhitespace: Boolean = false

  val maxLineWidth: Int = 100
  val preservedNewLines = 2
  val splitAttributesIndentSize = 2

  val emptyElementsBehavior = EmptyElements.collapse

  val preserveSpace =
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
  expand,
  collapse,
  ignore
}
