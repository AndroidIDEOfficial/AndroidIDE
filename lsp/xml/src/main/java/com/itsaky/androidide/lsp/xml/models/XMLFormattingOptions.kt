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
import org.eclipse.lemminx.dom.builder.EmptyElements
import org.eclipse.lemminx.dom.builder.BaseXmlFormattingOptions
import org.eclipse.lemminx.dom.DOMElement

/**
 * Options for XML code formatting.
 *
 * @author Akash Yadav
 */
open class XMLFormattingOptions : BaseXmlFormattingOptions() {

  override val isTrimFinalNewLine: Boolean
    get() = com.itsaky.androidide.preferences.internal.trimFinalNewLine
  override val isInsertFinalNewLine: Boolean
    get() = com.itsaky.androidide.preferences.internal.insertFinalNewLine
  override val isSplitAttributes: Boolean
    get() = com.itsaky.androidide.preferences.internal.splitAttributes
  override val isJoinCDataLines: Boolean
    get() = com.itsaky.androidide.preferences.internal.joinCDataLines
  override val isJoinCommentLines: Boolean
    get() = com.itsaky.androidide.preferences.internal.joinCommentLines
  override val isJoinContentLines: Boolean
    get() = com.itsaky.androidide.preferences.internal.joinContentLines
  override val isSpaceBeforeEmptyCloseTag: Boolean
    get() = com.itsaky.androidide.preferences.internal.spaceBeforeEmptyCloseTag
  override val isPreserveEmptyContent: Boolean
    get() = com.itsaky.androidide.preferences.internal.preserveEmptyContent
  override val isPreserveAttributeLineBreaks: Boolean
    get() = com.itsaky.androidide.preferences.internal.preserveAttributeLineBreaks
  override val isClosingBracketNewLine: Boolean
    get() = com.itsaky.androidide.preferences.internal.closingBracketNewLine
  override val isTrimTrailingWhitespace: Boolean
    get() = com.itsaky.androidide.preferences.internal.trimTrailingWhitespace

  override val maxLineWidth: Int
    get() = com.itsaky.androidide.preferences.internal.maxLineWidth
  override val preservedNewLines: Int
    get() = com.itsaky.androidide.preferences.internal.preservedNewLines
  override val splitAttributesIndentSize: Int
    get() = com.itsaky.androidide.preferences.internal.splitAttributesIndentSize

  override val emptyElementsBehavior: EmptyElements
    get() = EmptyElements.valueOf(
      com.itsaky.androidide.preferences.internal.emptyElementsBehavior)

  private val preserveSpace =
    listOf("xsl:text", "xsl:comment", "xsl:processing-instruction",
      "literallayout", "programlisting", "screen", "synopsis", "pre", "xd:pre")

  fun getFormatElementCategory(element: DOMElement): FormatElementCategory? {
    return preserveSpace.find { it == element.tagName }?.let { PreserveSpace }
  }
}
