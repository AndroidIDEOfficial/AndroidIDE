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

package com.itsaky.androidide.preferences

import android.content.Context
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.preferences.internal.CLOSING_BRACKET_NEW_LINE
import com.itsaky.androidide.preferences.internal.EMPTY_ELEMENTS_BEHAVIOR
import com.itsaky.androidide.preferences.internal.INSERT_FINAL_NEW_LINE
import com.itsaky.androidide.preferences.internal.JOIN_CDATA_LINES
import com.itsaky.androidide.preferences.internal.JOIN_COMMENT_LINES
import com.itsaky.androidide.preferences.internal.JOIN_CONTENT_LINES
import com.itsaky.androidide.preferences.internal.MAX_LINE_WIDTH
import com.itsaky.androidide.preferences.internal.PRESERVED_NEW_LINES
import com.itsaky.androidide.preferences.internal.PRESERVE_ATTRIBUTE_LINE_BREAKS
import com.itsaky.androidide.preferences.internal.PRESERVE_EMPTY_CONTENT
import com.itsaky.androidide.preferences.internal.SPACE_BEFORE_EMPTY_CLOSE_TAG
import com.itsaky.androidide.preferences.internal.SPLIT_ATTRIBUTES
import com.itsaky.androidide.preferences.internal.SPLIT_ATTRIBUTES_INDENT_SIZE
import com.itsaky.androidide.preferences.internal.TRIM_FINAL_NEW_LINE
import com.itsaky.androidide.preferences.internal.TRIM_TRAILING_WHITESPACE
import com.itsaky.androidide.preferences.internal.closingBracketNewLine
import com.itsaky.androidide.preferences.internal.emptyElementsBehavior
import com.itsaky.androidide.preferences.internal.insertFinalNewLine
import com.itsaky.androidide.preferences.internal.joinCDataLines
import com.itsaky.androidide.preferences.internal.joinCommentLines
import com.itsaky.androidide.preferences.internal.joinContentLines
import com.itsaky.androidide.preferences.internal.maxLineWidth
import com.itsaky.androidide.preferences.internal.preserveAttributeLineBreaks
import com.itsaky.androidide.preferences.internal.preserveEmptyContent
import com.itsaky.androidide.preferences.internal.preservedNewLines
import com.itsaky.androidide.preferences.internal.spaceBeforeEmptyCloseTag
import com.itsaky.androidide.preferences.internal.splitAttributes
import com.itsaky.androidide.preferences.internal.splitAttributesIndentSize
import com.itsaky.androidide.preferences.internal.trimFinalNewLine
import com.itsaky.androidide.preferences.internal.trimTrailingWhitespace
import kotlinx.parcelize.IgnoredOnParcel
import org.eclipse.lemminx.dom.builder.EmptyElements
import kotlinx.parcelize.Parcelize

@Parcelize
class XMLPreferences(
  override val key: String = "idepref_editor_xml",
  override val title: Int = string.xml,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {
  init {
    addPreference(XMLFormattingOptions())
  }
}

@Parcelize
private class XMLFormattingOptions(
  override val key: String = "idepref_xml_formattingOptions",
  override val title: Int = string.xml_formatting_options,
  override val summary: Int? = string.xml_formatting_options_summary,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceScreen() {
  init {
    addPreference(TrimFinalNewLines())
    addPreference(InsertFinalNewLine())
    addPreference(SplitAttributes())
    addPreference(JoinCDataLines())
    addPreference(JoinCommentLines())
    addPreference(JoinContentLines())
    addPreference(SpaceBeforeEmptyCloseTag())
    addPreference(PreserveEmptyContent())
    addPreference(PreserveAttributeLineBreaks())
    addPreference(ClosingBracketNewLine())
    addPreference(TrimTrailingWhitespace())
    addPreference(MaxLineWidth())
    addPreference(PreservedNewLines())
    addPreference(SplitAttributesIndentSize())
    addPreference(EmptyElementsBehavior())
  }
}

@Parcelize
private class TrimFinalNewLines(
  override val key: String = TRIM_FINAL_NEW_LINE,
  override val title: Int = string.idepref_xml_trimFinalNewLine_title,
  override val summary: Int? = string.idepref_xml_trimFinalNewLine_summary
) : SwitchPreference(setValue = ::trimFinalNewLine::set, getValue = ::trimFinalNewLine::get)

@Parcelize
private class InsertFinalNewLine(
  override val key: String = INSERT_FINAL_NEW_LINE,
  override val title: Int = string.idepref_xml_insertFinalNewLine_title,
  override val summary: Int? = string.idepref_xml_insertFinalNewLine_summary
) : SwitchPreference(setValue = ::insertFinalNewLine::set, getValue = ::insertFinalNewLine::get)

@Parcelize
private class SplitAttributes(
  override val key: String = SPLIT_ATTRIBUTES,
  override val title: Int = string.idepref_xml_splitAttributes_title,
  override val summary: Int? = string.idepref_xml_splitAttributes_summary
) : SwitchPreference(setValue = ::splitAttributes::set, getValue = ::splitAttributes::get)

@Parcelize
private class JoinCDataLines(
  override val key: String = JOIN_CDATA_LINES,
  override val title: Int = string.idepref_xml_joinCDataLines_title,
  override val summary: Int? = string.idepref_xml_joinCDataLines_summary
) : SwitchPreference(setValue = ::joinCDataLines::set, getValue = ::joinCDataLines::get)

@Parcelize
private class JoinCommentLines(
  override val key: String = JOIN_COMMENT_LINES,
  override val title: Int = string.idepref_xml_joinComment_title,
  override val summary: Int? = string.idepref_xml_joinComment_summary
) : SwitchPreference(setValue = ::joinCommentLines::set, getValue = ::joinCommentLines::get)

@Parcelize
private class JoinContentLines(
  override val key: String = JOIN_CONTENT_LINES,
  override val title: Int = string.idepref_xml_joinContent_title,
  override val summary: Int? = string.idepref_xml_joinContent_summary
) : SwitchPreference(setValue = ::joinContentLines::set, getValue = ::joinContentLines::get)

@Parcelize
private class SpaceBeforeEmptyCloseTag(
  override val key: String = SPACE_BEFORE_EMPTY_CLOSE_TAG,
  override val title: Int = string.idepref_xml_spaceBeforeEmptyClose_title,
  override val summary: Int? = string.idepref_xml_spaceBeforeEmptyClose_summary
) :
  SwitchPreference(
    setValue = ::spaceBeforeEmptyCloseTag::set,
    getValue = ::spaceBeforeEmptyCloseTag::get
  )

@Parcelize
private class PreserveEmptyContent(
  override val key: String = PRESERVE_EMPTY_CONTENT,
  override val title: Int = string.idepref_xml_preserveEmptyContent_title,
  override val summary: Int? = string.idepref_xml_preserveEmptyContent_summary
) :
  SwitchPreference(setValue = ::preserveEmptyContent::set, getValue = ::preserveEmptyContent::get)

@Parcelize
private class PreserveAttributeLineBreaks(
  override val key: String = PRESERVE_ATTRIBUTE_LINE_BREAKS,
  override val title: Int = string.idepref_xml_preserveAttrLineBreaks_title,
  override val summary: Int? = string.idepref_xml_preserveAttrLineBreaks_summary
) :
  SwitchPreference(
    setValue = ::preserveAttributeLineBreaks::set,
    getValue = ::preserveAttributeLineBreaks::get
  )

@Parcelize
private class ClosingBracketNewLine(
  override val key: String = CLOSING_BRACKET_NEW_LINE,
  override val title: Int = string.idepref_xml_closingBrackNewLine_title,
  override val summary: Int? = string.idepref_xml_closingBrackNewLine_summary
) :
  SwitchPreference(
    setValue = ::closingBracketNewLine::set,
    getValue = ::closingBracketNewLine::get
  )

@Parcelize
private class TrimTrailingWhitespace(
  override val key: String = TRIM_TRAILING_WHITESPACE,
  override val title: Int = string.idepref_xml_trimTrailingWs_title,
  override val summary: Int? = string.idepref_xml_trimTrailingWs_summary
) :
  SwitchPreference(
    setValue = ::trimTrailingWhitespace::set,
    getValue = ::trimTrailingWhitespace::get
  )

@Parcelize
private class MaxLineWidth(
  override val key: String = MAX_LINE_WIDTH,
  override val title: Int = string.idepref_maxLineWidth_title,
  override val summary: Int? = string.idepref_maxLineWidth_summary
) :
  NumberInputEditTextPreference(
    hint = string.idepref_maxLineWidth_title,
    setValue = ::maxLineWidth::set,
    getValue = ::maxLineWidth::get
  )

@Parcelize
private class PreservedNewLines(
  override val key: String = PRESERVED_NEW_LINES,
  override val title: Int = string.idepref_preservedNewLines_title,
  override val summary: Int? = string.idepref_preservedNewLines_summary
) :
  NumberInputEditTextPreference(
    hint = string.idepref_preservedNewLines_title,
    setValue = ::preservedNewLines::set,
    getValue = ::preservedNewLines::get
  )

@Parcelize
private class SplitAttributesIndentSize(
  override val key: String = SPLIT_ATTRIBUTES_INDENT_SIZE,
  override val title: Int = string.idepref_splitAttrIndentSize_title,
  override val summary: Int? = string.idepref_splitAttrIndentSize_summary
) :
  NumberInputEditTextPreference(
    hint = string.idepref_splitAttrIndentSize_title,
    setValue = ::splitAttributesIndentSize::set,
    getValue = ::splitAttributesIndentSize::get
  )

@Parcelize
private class EmptyElementsBehavior(
  override val key: String = EMPTY_ELEMENTS_BEHAVIOR,
  override val title: Int = string.idepref_emptyElements_title,
  override val summary: Int? = string.idepref_emptyElements_summary
) : SingleChoicePreference() {

  @IgnoredOnParcel
  override val dialogCancellable = true

  override fun getInitiallySelectionItemPosition(context: Context): Int {
    return EmptyElements.valueOf(emptyElementsBehavior).ordinal
  }

  override fun getChoices(context: Context): Array<String> {
    return EmptyElements.values().map { it.toString() }.toTypedArray()
  }

  override fun onChoiceConfirmed(position: Int) {
    emptyElementsBehavior = EmptyElements.values()[position].toString()
  }
}
