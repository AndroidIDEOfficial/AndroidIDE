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
import com.itsaky.androidide.preferences.internal.XmlPreferences
import org.eclipse.lemminx.dom.DOMElement
import org.eclipse.lemminx.dom.builder.BaseXmlFormattingOptions
import org.eclipse.lemminx.dom.builder.EmptyElements

/**
 * Options for XML code formatting.
 *
 * @author Akash Yadav
 */
open class XMLFormattingOptions : BaseXmlFormattingOptions() {

  override val isTrimFinalNewLine: Boolean
    get() = XmlPreferences.trimFinalNewLine
  override val isInsertFinalNewLine: Boolean
    get() = XmlPreferences.insertFinalNewLine
  override val isSplitAttributes: Boolean
    get() = XmlPreferences.splitAttributes
  override val isJoinCDataLines: Boolean
    get() = XmlPreferences.joinCDataLines
  override val isJoinCommentLines: Boolean
    get() = XmlPreferences.joinCommentLines
  override val isJoinContentLines: Boolean
    get() = XmlPreferences.joinContentLines
  override val isSpaceBeforeEmptyCloseTag: Boolean
    get() = XmlPreferences.spaceBeforeEmptyCloseTag
  override val isPreserveEmptyContent: Boolean
    get() = XmlPreferences.preserveEmptyContent
  override val isPreserveAttributeLineBreaks: Boolean
    get() = XmlPreferences.preserveAttributeLineBreaks
  override val isClosingBracketNewLine: Boolean
    get() = XmlPreferences.closingBracketNewLine
  override val isTrimTrailingWhitespace: Boolean
    get() = XmlPreferences.trimTrailingWhitespace

  override val maxLineWidth: Int
    get() = XmlPreferences.maxLineWidth
  override val preservedNewLines: Int
    get() = XmlPreferences.preservedNewLines
  override val splitAttributesIndentSize: Int
    get() = XmlPreferences.splitAttributesIndentSize

  override val emptyElementsBehavior: EmptyElements
    get() = EmptyElements.valueOf(
      XmlPreferences.emptyElementsBehavior)

  private val preserveSpace =
    listOf("xsl:text", "xsl:comment", "xsl:processing-instruction",
      "literallayout", "programlisting", "screen", "synopsis", "pre", "xd:pre")

  fun getFormatElementCategory(element: DOMElement): FormatElementCategory? {
    return preserveSpace.find { it == element.tagName }?.let { PreserveSpace }
  }
}
