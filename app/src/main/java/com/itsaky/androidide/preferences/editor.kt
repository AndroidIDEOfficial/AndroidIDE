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
import com.itsaky.androidide.R.drawable
import com.itsaky.androidide.R.string
import com.itsaky.androidide.databinding.LayoutTextSizeSliderBinding
import com.itsaky.androidide.preferences.internal.AUTO_SAVE
import com.itsaky.androidide.preferences.internal.COMPLETIONS_MATCH_LOWER
import com.itsaky.androidide.preferences.internal.FLAG_PASSWORD
import com.itsaky.androidide.preferences.internal.FONT_LIGATURES
import com.itsaky.androidide.preferences.internal.FONT_SIZE
import com.itsaky.androidide.preferences.internal.GOOGLE_CODE_STYLE
import com.itsaky.androidide.preferences.internal.PRINTABLE_CHARS
import com.itsaky.androidide.preferences.internal.TAB_SIZE
import com.itsaky.androidide.preferences.internal.USE_ICU
import com.itsaky.androidide.preferences.internal.USE_MAGNIFER
import com.itsaky.androidide.preferences.internal.USE_SOFT_TAB
import com.itsaky.androidide.preferences.internal.WORD_WRAP
import com.itsaky.androidide.preferences.internal.autoSave
import com.itsaky.androidide.preferences.internal.completionsMatchLower
import com.itsaky.androidide.preferences.internal.drawEmptyLineWs
import com.itsaky.androidide.preferences.internal.drawInnerWs
import com.itsaky.androidide.preferences.internal.drawLeadingWs
import com.itsaky.androidide.preferences.internal.drawLineBreak
import com.itsaky.androidide.preferences.internal.drawTrailingWs
import com.itsaky.androidide.preferences.internal.fontLigatures
import com.itsaky.androidide.preferences.internal.fontSize
import com.itsaky.androidide.preferences.internal.googleCodeStyle
import com.itsaky.androidide.preferences.internal.tabSize
import com.itsaky.androidide.preferences.internal.useIcu
import com.itsaky.androidide.preferences.internal.useMagnifier
import com.itsaky.androidide.preferences.internal.useSoftTab
import com.itsaky.androidide.preferences.internal.visiblePasswordFlag
import com.itsaky.androidide.preferences.internal.wordwrap
import kotlinx.parcelize.Parcelize

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
    addPreference(NonPrintablePaintingFlags())
    addPreference(FontLigatures())
    addPreference(UseSoftTab())
    addPreference(WordWrap())
    addPreference(UseMagnifier())
    addPreference(UseICU())
    addPreference(AutoSave())
    addPreference(VisibiblePasswordFlag())
    addPreference(CompletionsMatchLower())
  }
}

@Parcelize
private class JavaCodeConfigurations(
  override val key: String = "idepref_editor_java",
  override val title: Int = string.idepref_editor_category_java,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {
  init {
    addPreference(GoogleCodeStyle())
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
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = fontLigatures
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    fontLigatures = newValue as Boolean? ?: fontLigatures
    return true
  }
}

@Parcelize
private class UseSoftTab(
  override val key: String = USE_SOFT_TAB,
  override val title: Int = string.idepref_editor_useSoftTabs_title,
  override val summary: Int? = string.idepref_editor_useSoftTabs_summary,
  override val icon: Int? = drawable.ic_space,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = useSoftTab
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    useSoftTab = newValue as Boolean? ?: useSoftTab
    return true
  }
}

@Parcelize
private class TabSize(
  override val key: String = TAB_SIZE,
  override val title: Int = string.title_tab_size,
  override val summary: Int? = string.msg_tab_size,
  override val icon: Int? = drawable.ic_font_ligatures,
) : SingleChoicePreference() {

  override fun getChoices(): Array<String> {
    return arrayOf("2", "4", "6", "8")
  }

  override fun onItemSelected(position: Int, isSelected: Boolean) {
    var size = (position + 1) * 2
    if (size < 2 || size > 8) {
      size = 4
    }
    tabSize = size
  }

  override fun getSelectedItem(): Int {
    var current = tabSize / 2 - 1
    if (current < 0 || current >= getChoices().size) {
      current = 1
    }
    return current
  }
}

@Parcelize
private class NonPrintablePaintingFlags(
  override val key: String = PRINTABLE_CHARS,
  override val title: Int = string.idepref_editor_paintingflags_title,
  override val summary: Int? = string.idepref_editor_paintingflags_summary,
  override val icon: Int? = drawable.ic_drawing,
) : MultiChoicePreference() {

  override fun getChoices(): Array<String> {
    return arrayOf("Leading", "Trailing", "Inner", "Empty lines", "Line breaks")
  }

  override fun onItemSelected(position: Int, isSelected: Boolean) {
    when (position) {
      0 -> drawLeadingWs = isSelected
      1 -> drawTrailingWs = isSelected
      2 -> drawInnerWs = isSelected
      3 -> drawEmptyLineWs = isSelected
      4 -> drawLineBreak = isSelected
    }
  }

  override fun getCheckedItems(): BooleanArray {
    return booleanArrayOf(
      drawLeadingWs,
      drawTrailingWs,
      drawInnerWs,
      drawEmptyLineWs,
      drawLineBreak
    )
  }
}

@Parcelize
private class WordWrap(
  override val key: String = WORD_WRAP,
  override val title: Int = string.idepref_editor_word_wrap_title,
  override val summary: Int? = string.idepref_editor_word_wrap_summary,
  override val icon: Int? = drawable.ic_wrap_text,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = wordwrap
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    wordwrap = newValue as Boolean? ?: wordwrap
    return true
  }
}

@Parcelize
private class UseMagnifier(
  override val key: String = USE_MAGNIFER,
  override val title: Int = string.idepref_editor_use_magnifier_title,
  override val summary: Int? = string.idepref_editor_use_magnifier_summary,
  override val icon: Int? = drawable.ic_loupe,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = useMagnifier
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    useMagnifier = newValue as Boolean? ?: useMagnifier
    return true
  }
}

@Parcelize
private class AutoSave(
  override val key: String = AUTO_SAVE,
  override val title: Int = string.idepref_editor_autoSave_title,
  override val summary: Int? = string.idepref_editor_autoSave_summary,
  override val icon: Int? = drawable.ic_save,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = autoSave
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    autoSave = newValue as Boolean? ?: autoSave
    return true
  }
}

@Parcelize
private class CompletionsMatchLower(
  override val key: String = COMPLETIONS_MATCH_LOWER,
  override val title: Int = string.idepref_java_matchLower_title,
  override val summary: Int? = string.idepref_java_matchLower_summary,
  override val icon: Int? = drawable.ic_text_lower,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = completionsMatchLower
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    completionsMatchLower = newValue as Boolean? ?: completionsMatchLower
    return true
  }
}

@Parcelize
private class GoogleCodeStyle(
  override val key: String = GOOGLE_CODE_STYLE,
  override val title: Int = string.idepref_java_useGoogleStyle_title,
  override val summary: Int? = string.idepref_java_useGoogleStyle_summary,
  override val icon: Int? = drawable.ic_format_code,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = googleCodeStyle
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    googleCodeStyle = newValue as Boolean? ?: googleCodeStyle
    return true
  }
}

@Parcelize
private class VisibiblePasswordFlag(
  override val key: String = FLAG_PASSWORD,
  override val title: Int = string.idepref_visiblePassword_title,
  override val summary: Int? = string.idepref_editor_paintingflags_summary,
  override val icon: Int? = drawable.ic_password_input,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = visiblePasswordFlag
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    visiblePasswordFlag = newValue as Boolean? ?: visiblePasswordFlag
    return true
  }
}

@Parcelize
private class UseICU(
  override val key: String = USE_ICU,
  override val title: Int = string.idepref_useIcu_title,
  override val summary: Int? = string.idepref_useIcu_summary,
  override val icon: Int? = drawable.ic_expand_selection,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = useIcu
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    useIcu = newValue as Boolean? ?: useIcu
    return true
  }
}
