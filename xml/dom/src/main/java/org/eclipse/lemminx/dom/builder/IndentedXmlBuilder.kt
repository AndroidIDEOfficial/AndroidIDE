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

/**
 * An implementation of [XmlBuilder] which can handle indentations while building the XML content.
 *
 * @author Akash Yadav
 */
class IndentedXmlBuilder(private val autoIndent: Boolean = false, whitespacesIndent: String?, lineDelimiter: String?,
                         options: BaseXmlFormattingOptions?
) : XmlBuilder(whitespacesIndent, lineDelimiter, options) {

  constructor(autoIndent: Boolean) : this(autoIndent, "", System.lineSeparator(), BaseXmlFormattingOptions())

  constructor() : this(false)

  private var indentLevel = 0

  override fun startElement(prefix: String?, name: String?, close: Boolean
  ): XmlBuilder {
    if (autoIndent) {
      lfIndent()
    }
    return super.startElement(prefix, name, close).also {
      if (autoIndent) {
        incrIndent()
      }
    }
  }

  override fun addAttributeContents(name: String?, equalsSign: Boolean,
                                    originalValue: String?,
                                    surroundWithQuotes: Boolean
  ) {
    if (autoIndent) {
      lfIndent()
    }
    super.addAttributeContents(name, equalsSign, originalValue,
      surroundWithQuotes)
  }

  override fun selfCloseElement(): XmlBuilder {
    if (autoIndent) {
      dcrIndent()
    }
    return super.selfCloseElement()
  }

  override fun endElement(prefix: String?, name: String?,
                          isEndTagClosed: Boolean
  ): XmlBuilder {
    if (autoIndent) {
      dcrIndent()
      lfIndent()
    }
    return super.endElement(prefix, name, isEndTagClosed)
  }

  /**
   * Increment the indentation level.
   */
  fun incrIndent() {
    ++indentLevel
  }

  /**
   * Decrement the indentation level.
   */
  fun dcrIndent() {
    --indentLevel
  }

  /**
   * Appends a line feed and indents the next line.
   */
  fun lfIndent() {
    linefeed()
    indent()
  }

  /**
   * Appends indentation to the indent line.
   */
  fun indent() {
    indent(indentLevel)
  }
}