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

package org.eclipse.lemminx.dom.builder

import org.eclipse.lemminx.dom.builder.EmptyElements.Collapse

/**
 * Formatting options for the [XmlBuilder].
 *
 * @author Akash Yadav
 */
open class BaseXmlFormattingOptions {

  open val isTrimFinalNewLine: Boolean = false
  open val isInsertFinalNewLine: Boolean = true
  open val isSplitAttributes: Boolean = true
  open val isJoinCDataLines: Boolean = false
  open val isJoinCommentLines: Boolean = true
  open val isJoinContentLines: Boolean = false
  open val isSpaceBeforeEmptyCloseTag: Boolean = true
  open val isPreserveEmptyContent: Boolean = true
  open val isPreserveAttributeLineBreaks: Boolean = true
  open val isClosingBracketNewLine: Boolean = false
  open val isTrimTrailingWhitespace: Boolean = true
  open val isUseSoftTab: Boolean = true

  open val tabSize = 4
  open val maxLineWidth: Int = 80
  open val preservedNewLines: Int = 2

  open val splitAttributesIndentSize: Int
    get() = tabSize / 2

  open val emptyElementsBehavior: EmptyElements
    get() = Collapse
}