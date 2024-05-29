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

package com.itsaky.androidide.preferences.internal

/**
 * @author Akash Yadav
 */
@Suppress("MemberVisibilityCanBePrivate")
object XmlPreferences {

  const val TRIM_FINAL_NEW_LINE = "idepref_xml_trimFinalNewLine"
  const val INSERT_FINAL_NEW_LINE = "idepref_xml_insertFinalNewLine"
  const val SPLIT_ATTRIBUTES = "idepref_xml_splitAttributes"
  const val JOIN_CDATA_LINES = "idepref_xml_joinCDataLines"
  const val JOIN_COMMENT_LINES = "idepref_xml_joinCommentLines"
  const val JOIN_CONTENT_LINES = "idepref_xml_joinContentLines"
  const val SPACE_BEFORE_EMPTY_CLOSE_TAG = "idepref_xml_spaceBeforeEmptyCloseTag"
  const val PRESERVE_EMPTY_CONTENT = "idepref_xml_preserveEmptyContent"
  const val PRESERVE_ATTRIBUTE_LINE_BREAKS = "idepref_xml_preserveAttributeLineBreaks"
  const val CLOSING_BRACKET_NEW_LINE = "idepref_xml_closingBracketNewLine"
  const val TRIM_TRAILING_WHITESPACE = "idepref_xml_trimTrailingWhitespace"
  const val MAX_LINE_WIDTH = "idepref_xml_maxLineWidth"
  const val PRESERVED_NEW_LINES = "idepref_xml_preservedNewLines"
  const val SPLIT_ATTRIBUTES_INDENT_SIZE = "idepref_xml_splitAttributesIndentSize"
  const val EMPTY_ELEMENTS_BEHAVIOR = "idepref_xml_emptyElementsBehavior"

  var trimFinalNewLine: Boolean
    get() = prefManager.getBoolean(TRIM_FINAL_NEW_LINE, false)
    set(value) {
      prefManager.putBoolean(TRIM_FINAL_NEW_LINE, value)
    }

  var insertFinalNewLine: Boolean
    get() = prefManager.getBoolean(INSERT_FINAL_NEW_LINE, true)
    set(value) {
      prefManager.putBoolean(INSERT_FINAL_NEW_LINE, value)
    }

  var splitAttributes: Boolean
    get() = prefManager.getBoolean(SPLIT_ATTRIBUTES, true)
    set(value) {
      prefManager.putBoolean(SPLIT_ATTRIBUTES, value)
    }

  var joinCDataLines: Boolean
    get() = prefManager.getBoolean(JOIN_CDATA_LINES, false)
    set(value) {
      prefManager.putBoolean(JOIN_CDATA_LINES, value)
    }

  var joinCommentLines: Boolean
    get() = prefManager.getBoolean(JOIN_COMMENT_LINES, true)
    set(value) {
      prefManager.putBoolean(JOIN_COMMENT_LINES, value)
    }

  var joinContentLines: Boolean
    get() = prefManager.getBoolean(JOIN_CONTENT_LINES, false)
    set(value) {
      prefManager.putBoolean(JOIN_CONTENT_LINES, value)
    }

  var spaceBeforeEmptyCloseTag: Boolean
    get() = prefManager.getBoolean(SPACE_BEFORE_EMPTY_CLOSE_TAG, true)
    set(value) {
      prefManager.putBoolean(SPACE_BEFORE_EMPTY_CLOSE_TAG, value)
    }

  var preserveEmptyContent: Boolean
    get() = prefManager.getBoolean(PRESERVE_EMPTY_CONTENT, true)
    set(value) {
      prefManager.putBoolean(PRESERVE_EMPTY_CONTENT, value)
    }

  var preserveAttributeLineBreaks: Boolean
    get() = prefManager.getBoolean(PRESERVE_ATTRIBUTE_LINE_BREAKS, true)
    set(value) {
      prefManager.putBoolean(PRESERVE_ATTRIBUTE_LINE_BREAKS, value)
    }

  var closingBracketNewLine: Boolean
    get() = prefManager.getBoolean(CLOSING_BRACKET_NEW_LINE, false)
    set(value) {
      prefManager.putBoolean(CLOSING_BRACKET_NEW_LINE, value)
    }

  var trimTrailingWhitespace: Boolean
    get() = prefManager.getBoolean(TRIM_TRAILING_WHITESPACE, true)
    set(value) {
      prefManager.putBoolean(TRIM_TRAILING_WHITESPACE, value)
    }

  var maxLineWidth: Int
    get() = prefManager.getInt(MAX_LINE_WIDTH, 80)
    set(value) {
      prefManager.putInt(MAX_LINE_WIDTH, value)
    }

  var preservedNewLines: Int
    get() = prefManager.getInt(PRESERVED_NEW_LINES, 2)
    set(value) {
      prefManager.putInt(PRESERVED_NEW_LINES, value)
    }

  var splitAttributesIndentSize: Int
    get() = prefManager.getInt(SPLIT_ATTRIBUTES_INDENT_SIZE, EditorPreferences.tabSize / 2)
    set(value) {
      prefManager.putInt(SPLIT_ATTRIBUTES_INDENT_SIZE, value)
    }

  var emptyElementsBehavior: String
    get() = prefManager.getString(EMPTY_ELEMENTS_BEHAVIOR, "Collapse")
    set(value) {
      prefManager.putString(EMPTY_ELEMENTS_BEHAVIOR, value)
    }
}