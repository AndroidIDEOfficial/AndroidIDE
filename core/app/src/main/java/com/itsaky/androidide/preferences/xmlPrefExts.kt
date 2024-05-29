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

import androidx.preference.Preference
import com.itsaky.androidide.preferences.internal.XmlPreferences
import com.itsaky.androidide.resources.R.string
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.eclipse.lemminx.dom.builder.EmptyElements

@Parcelize
class XMLPreferencesScreen(
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
  override val key: String = XmlPreferences.TRIM_FINAL_NEW_LINE,
  override val title: Int = string.idepref_xml_trimFinalNewLine_title,
  override val summary: Int? = string.idepref_xml_trimFinalNewLine_summary
) : SwitchPreference(setValue = XmlPreferences::trimFinalNewLine::set,
  getValue = XmlPreferences::trimFinalNewLine::get)

@Parcelize
private class InsertFinalNewLine(
  override val key: String = XmlPreferences.INSERT_FINAL_NEW_LINE,
  override val title: Int = string.idepref_xml_insertFinalNewLine_title,
  override val summary: Int? = string.idepref_xml_insertFinalNewLine_summary
) : SwitchPreference(setValue = XmlPreferences::insertFinalNewLine::set,
  getValue = XmlPreferences::insertFinalNewLine::get)

@Parcelize
private class SplitAttributes(
  override val key: String = XmlPreferences.SPLIT_ATTRIBUTES,
  override val title: Int = string.idepref_xml_splitAttributes_title,
  override val summary: Int? = string.idepref_xml_splitAttributes_summary
) : SwitchPreference(setValue = XmlPreferences::splitAttributes::set,
  getValue = XmlPreferences::splitAttributes::get)

@Parcelize
private class JoinCDataLines(
  override val key: String = XmlPreferences.JOIN_CDATA_LINES,
  override val title: Int = string.idepref_xml_joinCDataLines_title,
  override val summary: Int? = string.idepref_xml_joinCDataLines_summary
) : SwitchPreference(setValue = XmlPreferences::joinCDataLines::set,
  getValue = XmlPreferences::joinCDataLines::get)

@Parcelize
private class JoinCommentLines(
  override val key: String = XmlPreferences.JOIN_COMMENT_LINES,
  override val title: Int = string.idepref_xml_joinComment_title,
  override val summary: Int? = string.idepref_xml_joinComment_summary
) : SwitchPreference(setValue = XmlPreferences::joinCommentLines::set,
  getValue = XmlPreferences::joinCommentLines::get)

@Parcelize
private class JoinContentLines(
  override val key: String = XmlPreferences.JOIN_CONTENT_LINES,
  override val title: Int = string.idepref_xml_joinContent_title,
  override val summary: Int? = string.idepref_xml_joinContent_summary
) : SwitchPreference(setValue = XmlPreferences::joinContentLines::set,
  getValue = XmlPreferences::joinContentLines::get)

@Parcelize
private class SpaceBeforeEmptyCloseTag(
  override val key: String = XmlPreferences.SPACE_BEFORE_EMPTY_CLOSE_TAG,
  override val title: Int = string.idepref_xml_spaceBeforeEmptyClose_title,
  override val summary: Int? = string.idepref_xml_spaceBeforeEmptyClose_summary
) :
  SwitchPreference(
    setValue = XmlPreferences::spaceBeforeEmptyCloseTag::set,
    getValue = XmlPreferences::spaceBeforeEmptyCloseTag::get
  )

@Parcelize
private class PreserveEmptyContent(
  override val key: String = XmlPreferences.PRESERVE_EMPTY_CONTENT,
  override val title: Int = string.idepref_xml_preserveEmptyContent_title,
  override val summary: Int? = string.idepref_xml_preserveEmptyContent_summary
) :
  SwitchPreference(setValue = XmlPreferences::preserveEmptyContent::set,
    getValue = XmlPreferences::preserveEmptyContent::get)

@Parcelize
private class PreserveAttributeLineBreaks(
  override val key: String = XmlPreferences.PRESERVE_ATTRIBUTE_LINE_BREAKS,
  override val title: Int = string.idepref_xml_preserveAttrLineBreaks_title,
  override val summary: Int? = string.idepref_xml_preserveAttrLineBreaks_summary
) :
  SwitchPreference(
    setValue = XmlPreferences::preserveAttributeLineBreaks::set,
    getValue = XmlPreferences::preserveAttributeLineBreaks::get
  )

@Parcelize
private class ClosingBracketNewLine(
  override val key: String = XmlPreferences.CLOSING_BRACKET_NEW_LINE,
  override val title: Int = string.idepref_xml_closingBrackNewLine_title,
  override val summary: Int? = string.idepref_xml_closingBrackNewLine_summary
) :
  SwitchPreference(
    setValue = XmlPreferences::closingBracketNewLine::set,
    getValue = XmlPreferences::closingBracketNewLine::get
  )

@Parcelize
private class TrimTrailingWhitespace(
  override val key: String = XmlPreferences.TRIM_TRAILING_WHITESPACE,
  override val title: Int = string.idepref_xml_trimTrailingWs_title,
  override val summary: Int? = string.idepref_xml_trimTrailingWs_summary
) :
  SwitchPreference(
    setValue = XmlPreferences::trimTrailingWhitespace::set,
    getValue = XmlPreferences::trimTrailingWhitespace::get
  )

@Parcelize
private class MaxLineWidth(
  override val key: String = XmlPreferences.MAX_LINE_WIDTH,
  override val title: Int = string.idepref_maxLineWidth_title,
  override val summary: Int? = string.idepref_maxLineWidth_summary
) :
  NumberInputEditTextPreference(
    hint = string.idepref_maxLineWidth_title,
    setValue = XmlPreferences::maxLineWidth::set,
    getValue = XmlPreferences::maxLineWidth::get
  )

@Parcelize
private class PreservedNewLines(
  override val key: String = XmlPreferences.PRESERVED_NEW_LINES,
  override val title: Int = string.idepref_preservedNewLines_title,
  override val summary: Int? = string.idepref_preservedNewLines_summary
) :
  NumberInputEditTextPreference(
    hint = string.idepref_preservedNewLines_title,
    setValue = XmlPreferences::preservedNewLines::set,
    getValue = XmlPreferences::preservedNewLines::get
  )

@Parcelize
private class SplitAttributesIndentSize(
  override val key: String = XmlPreferences.SPLIT_ATTRIBUTES_INDENT_SIZE,
  override val title: Int = string.idepref_splitAttrIndentSize_title,
  override val summary: Int? = string.idepref_splitAttrIndentSize_summary
) :
  NumberInputEditTextPreference(
    hint = string.idepref_splitAttrIndentSize_title,
    setValue = XmlPreferences::splitAttributesIndentSize::set,
    getValue = XmlPreferences::splitAttributesIndentSize::get
  )

@Parcelize
private class EmptyElementsBehavior(
  override val key: String = XmlPreferences.EMPTY_ELEMENTS_BEHAVIOR,
  override val title: Int = string.idepref_emptyElements_title,
  override val summary: Int? = string.idepref_emptyElements_summary
) : SingleChoicePreference() {

  @IgnoredOnParcel
  override val dialogCancellable = true

  override fun getEntries(preference: Preference): Array<PreferenceChoices.Entry> {
    val entries = EmptyElements.entries
    val currentBehavior = EmptyElements.valueOf(XmlPreferences.emptyElementsBehavior)

    return Array(entries.size) { index ->
      PreferenceChoices.Entry(
        label = entries[index].toString(),
        _isChecked = currentBehavior == entries[index],
        data = entries[index]
      )
    }
  }

  override fun onChoiceConfirmed(
    preference: Preference,
    entry: PreferenceChoices.Entry?,
    position: Int
  ) {
    XmlPreferences.emptyElementsBehavior = (entry?.data as? EmptyElements?)?.toString()
      ?: "Collapse"
  }
}
