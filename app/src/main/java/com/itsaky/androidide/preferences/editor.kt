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
import android.view.LayoutInflater
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itsaky.androidide.R
import com.itsaky.androidide.databinding.LayoutTextSizeSliderBinding
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.preferences.internal.AUTO_SAVE
import com.itsaky.androidide.preferences.internal.COLOR_SCHEME
import com.itsaky.androidide.preferences.internal.COMPLETIONS_MATCH_LOWER
import com.itsaky.androidide.preferences.internal.DELETE_EMPTY_LINES
import com.itsaky.androidide.preferences.internal.DELETE_TABS_ON_BACKSPACE
import com.itsaky.androidide.preferences.internal.FLAG_PASSWORD
import com.itsaky.androidide.preferences.internal.FONT_LIGATURES
import com.itsaky.androidide.preferences.internal.FONT_SIZE
import com.itsaky.androidide.preferences.internal.PIN_LINE_NUMBERS
import com.itsaky.androidide.preferences.internal.PRINTABLE_CHARS
import com.itsaky.androidide.preferences.internal.STICKY_SCROLL_ENABLED
import com.itsaky.androidide.preferences.internal.TAB_SIZE
import com.itsaky.androidide.preferences.internal.USE_CUSTOM_FONT
import com.itsaky.androidide.preferences.internal.USE_ICU
import com.itsaky.androidide.preferences.internal.USE_MAGNIFER
import com.itsaky.androidide.preferences.internal.USE_SOFT_TAB
import com.itsaky.androidide.preferences.internal.WORD_WRAP
import com.itsaky.androidide.preferences.internal.autoSave
import com.itsaky.androidide.preferences.internal.colorScheme
import com.itsaky.androidide.preferences.internal.completionsMatchLower
import com.itsaky.androidide.preferences.internal.deleteEmptyLines
import com.itsaky.androidide.preferences.internal.deleteTabsOnBackspace
import com.itsaky.androidide.preferences.internal.drawEmptyLineWs
import com.itsaky.androidide.preferences.internal.drawInnerWs
import com.itsaky.androidide.preferences.internal.drawLeadingWs
import com.itsaky.androidide.preferences.internal.drawLineBreak
import com.itsaky.androidide.preferences.internal.drawTrailingWs
import com.itsaky.androidide.preferences.internal.fontLigatures
import com.itsaky.androidide.preferences.internal.fontSize
import com.itsaky.androidide.preferences.internal.pinLineNumbers
import com.itsaky.androidide.preferences.internal.stickyScrollEnabled
import com.itsaky.androidide.preferences.internal.tabSize
import com.itsaky.androidide.preferences.internal.useCustomFont
import com.itsaky.androidide.preferences.internal.useIcu
import com.itsaky.androidide.preferences.internal.useMagnifier
import com.itsaky.androidide.preferences.internal.useSoftTab
import com.itsaky.androidide.preferences.internal.visiblePasswordFlag
import com.itsaky.androidide.preferences.internal.wordwrap
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KMutableProperty0

@Parcelize
class EditorPreferences(
  override val key: String = "idepref_editor",
  override val title: Int = string.idepref_editor_title,
  override val summary: Int? = string.idepref_editor_summary,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceScreen() {

  init {
    addPreference(CommonConfigurations())
    addPreference(JavaCodeConfigurations())
    addPreference(XMLPreferences())
  }
}

@Parcelize
private class CommonConfigurations(
  override val key: String = "idepref_editor_common",
  override val title: Int = string.idepref_editor_category_common,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {

  init {
    addPreference(TextSize())
    addPreference(TabSize())
    addPreference(ColorSchemePreference())
    addPreference(NonPrintablePaintingFlags())
    addPreference(FontLigatures())
    addPreference(UseCustomFont())
    addPreference(UseSoftTab())
    addPreference(WordWrap())
    addPreference(UseMagnifier())
    addPreference(UseICU())
    addPreference(AutoSave())
    addPreference(VisibiblePasswordFlag())
    addPreference(DeleteEmptyLines())
    addPreference(DeleteTabs())
    addPreference(StickyScrollEnabled())
    addPreference(PinLineNumbersEnabled())
    addPreference(CompletionsMatchLower())
  }
}

@Parcelize
private class TextSize(
  override val key: String = FONT_SIZE,
  override val title: Int = string.idepref_editor_fontsize_title,
  override val summary: Int? = string.idepref_editor_fontsize_summary,
  override val icon: Int? = drawable.ic_text_size,
  override val dialogTitle: Int = string.title_change_text_size,
  override val dialogMessage: Int? = string.msg_editor_font_size,
) : DialogPreference() {

  override fun onConfigureDialog(preference: Preference, dialog: MaterialAlertDialogBuilder) {
    val binding = LayoutTextSizeSliderBinding.inflate(LayoutInflater.from(preference.context))
    var size = fontSize
    if (size < 6 || size > 32) {
      size = 14f
    }
    changeTextSize(binding, size)
    binding.slider.setLabelFormatter { it.toString() }

    dialog.setView(binding.root)
    dialog.setPositiveButton(android.R.string.ok) { iface, _ ->
      iface.dismiss()
      changeTextSize(binding, binding.slider.value)
    }
    dialog.setNegativeButton(android.R.string.cancel, null)
    dialog.setNeutralButton(string.reset) { iface, _ ->
      iface.dismiss()
      changeTextSize(binding, 14f)
    }
  }

  private fun changeTextSize(binding: LayoutTextSizeSliderBinding, size: Float) {
    fontSize = size
    binding.slider.value = size
  }
}

@Parcelize
private class FontLigatures(
  override val key: String = FONT_LIGATURES,
  override val title: Int = string.idepref_editor_ligatures_title,
  override val summary: Int? = string.idepref_editor_ligatures_summary,
  override val icon: Int? = drawable.ic_font_ligatures,
) : SwitchPreference(setValue = ::fontLigatures::set, getValue = ::fontLigatures::get)

@Parcelize
private class UseSoftTab(
  override val key: String = USE_SOFT_TAB,
  override val title: Int = string.idepref_editor_useSoftTabs_title,
  override val summary: Int? = string.idepref_editor_useSoftTabs_summary,
  override val icon: Int? = drawable.ic_space,
) : SwitchPreference(setValue = ::useSoftTab::set, getValue = ::useSoftTab::get)

@Parcelize
private class TabSize(
  override val key: String = TAB_SIZE,
  override val title: Int = string.title_tab_size,
  override val summary: Int? = string.msg_tab_size,
  override val icon: Int? = drawable.ic_font_ligatures,
) : SingleChoicePreference() {

  @IgnoredOnParcel
  override val dialogCancellable = true

  @IgnoredOnParcel
  private val choices = arrayOf("2", "4", "6", "8")

  override fun getChoices(context: Context): Array<String> {
    return choices
  }

  override fun getInitiallySelectionItemPosition(context: Context): Int {
    var current = tabSize / 2 - 1
    if (current < 0 || current >= choices.size) {
      current = 1
    }
    return current
  }

  override fun onChoiceConfirmed(position: Int) {
    var size = (position + 1) * 2
    if (size < 2 || size > 8) {
      size = 4
    }
    tabSize = size
  }
}

@Parcelize
private class ColorSchemePreference(
  override val key: String = COLOR_SCHEME,
  override val title: Int = R.string.idepref_editor_colorScheme,
  override val summary: Int? = R.string.idepref_editor_colorScheme_summary,
  override val icon: Int? = R.drawable.ic_color_scheme
) : SingleChoicePreference() {

  @IgnoredOnParcel
  override val dialogCancellable = true

  @IgnoredOnParcel
  private val schemes = IDEColorSchemeProvider.list()

  override fun getChoices(context: Context): Array<String> {
    return schemes.map { it.name }.toTypedArray()
  }

  override fun getInitiallySelectionItemPosition(context: Context): Int {
    return schemes.indexOfFirst { it.key == colorScheme }
  }

  override fun onChoiceConfirmed(position: Int) {
    colorScheme = schemes[position].key
  }
}

@Parcelize
private class NonPrintablePaintingFlags(
  override val key: String = PRINTABLE_CHARS,
  override val title: Int = string.idepref_editor_paintingflags_title,
  override val summary: Int? = string.idepref_editor_paintingflags_summary,
  override val icon: Int? = drawable.ic_drawing,
) : PropertyBasedMultiChoicePreference() {

  override fun getProperties(): Map<String, KMutableProperty0<Boolean>> {
    return linkedMapOf(
      "Leading" to ::drawLeadingWs,
      "Trailing" to ::drawTrailingWs,
      "Inner" to ::drawInnerWs,
      "Empty lines" to ::drawEmptyLineWs,
      "Line breaks" to ::drawLineBreak
    )
  }
}

@Parcelize
private class WordWrap(
  override val key: String = WORD_WRAP,
  override val title: Int = string.idepref_editor_word_wrap_title,
  override val summary: Int? = string.idepref_editor_word_wrap_summary,
  override val icon: Int? = drawable.ic_wrap_text,
) : SwitchPreference(setValue = ::wordwrap::set, getValue = ::wordwrap::get)

@Parcelize
private class UseMagnifier(
  override val key: String = USE_MAGNIFER,
  override val title: Int = string.idepref_editor_use_magnifier_title,
  override val summary: Int? = string.idepref_editor_use_magnifier_summary,
  override val icon: Int? = drawable.ic_loupe,
) : SwitchPreference(setValue = ::useMagnifier::set, getValue = ::useMagnifier::get)

@Parcelize
private class AutoSave(
  override val key: String = AUTO_SAVE,
  override val title: Int = string.idepref_editor_autoSave_title,
  override val summary: Int? = string.idepref_editor_autoSave_summary,
  override val icon: Int? = drawable.ic_save,
) : SwitchPreference(setValue = ::autoSave::set, getValue = ::autoSave::get)

@Parcelize
private class CompletionsMatchLower(
  override val key: String = COMPLETIONS_MATCH_LOWER,
  override val title: Int = string.idepref_java_matchLower_title,
  override val summary: Int? = string.idepref_java_matchLower_summary,
  override val icon: Int? = drawable.ic_text_lower,
) : SwitchPreference(
  setValue = ::completionsMatchLower::set,
  getValue = ::completionsMatchLower::get
)

@Parcelize
private class VisibiblePasswordFlag(
  override val key: String = FLAG_PASSWORD,
  override val title: Int = string.idepref_visiblePassword_title,
  override val summary: Int? = string.idepref_visiblePassword_summary,
  override val icon: Int? = drawable.ic_password_input,
) : SwitchPreference(setValue = ::visiblePasswordFlag::set, getValue = ::visiblePasswordFlag::get)

@Parcelize
private class UseICU(
  override val key: String = USE_ICU,
  override val title: Int = string.idepref_useIcu_title,
  override val summary: Int? = string.idepref_useIcu_summary,
  override val icon: Int? = drawable.ic_expand_selection,
) : SwitchPreference(setValue = ::useIcu::set, getValue = ::useIcu::get)

@Parcelize
private class UseCustomFont(
  override val key: String = USE_CUSTOM_FONT,
  override val title: Int = string.idepref_customFont_title,
  override val summary: Int? = string.idepref_customFont_summary,
  override val icon: Int? = drawable.ic_custom_font,
) : SwitchPreference(setValue = ::useCustomFont::set, getValue = ::useCustomFont::get)

@Parcelize
private class DeleteEmptyLines(
  override val key: String = DELETE_EMPTY_LINES,
  override val title: Int = R.string.idepref_deleteEmptyLines_title,
  override val summary: Int? = R.string.idepref_deleteEmptyLines_summary,
  override val icon: Int? = drawable.ic_backspace
) : SwitchPreference(setValue = ::deleteEmptyLines::set, getValue = ::deleteEmptyLines::get)

@Parcelize
private class DeleteTabs(
  override val key: String = DELETE_TABS_ON_BACKSPACE,
  override val title: Int = R.string.idepref_deleteTabs_title,
  override val summary: Int? = R.string.idepref_deleteTabs_summary,
  override val icon: Int? = drawable.ic_backspace
) :
  SwitchPreference(setValue = ::deleteTabsOnBackspace::set, getValue = ::deleteTabsOnBackspace::get)

@Parcelize
private class StickyScrollEnabled(
  override val key: String = STICKY_SCROLL_ENABLED,
  override val title: Int = R.string.idepref_editor_stickScroll_title,
  override val summary: Int? = R.string.idepref_editor_stickyScroll_summary,
  override val icon: Int? = drawable.ic_sticky_scroll
) : SwitchPreference(setValue = ::stickyScrollEnabled::set, getValue = ::stickyScrollEnabled::get)

@Parcelize
private class PinLineNumbersEnabled(
  override val key: String = PIN_LINE_NUMBERS,
  override val title: Int = R.string.idepref_editor_pinLineNumbers_title,
  override val summary: Int? = R.string.idepref_editor_pinLineNumbers_summary,
  override val icon: Int? = drawable.ic_pin
) : SwitchPreference(setValue = ::pinLineNumbers::set, getValue = ::pinLineNumbers::get)
