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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import com.itsaky.androidide.R
import com.itsaky.androidide.preferences.internal.GeneralPreferences
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.resources.localization.LocaleProvider
import com.itsaky.androidide.ui.themes.IDETheme
import com.itsaky.androidide.ui.themes.IThemeManager
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class GeneralPreferencesScreen(
  override val key: String = "idepref_general",
  override val title: Int = string.title_general,
  override val summary: Int? = string.idepref_general_summary,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceScreen() {

  init {
    addPreference(InterfaceConfig())
    addPreference(ProjectConfig())
    addPreference(TerminalConfig())
  }
}

@Parcelize
class InterfaceConfig(
  override val key: String = "idepref_general_interface",
  override val title: Int = string.title_interface,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {

  init {
    addPreference(UiMode())
    addPreference(ThemeSelector())
    addPreference(LocaleSelector())
  }
}

@Parcelize
class ProjectConfig(
  override val key: String = "idepref_general_project",
  override val title: Int = R.string.idepref_general_projectConfig,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {

  init {
    addPreference(OpenLastProject())
    addPreference(ConfirmProjectOpen())
  }
}

@Parcelize
class TerminalConfig(
  override val key: String = "idepref_general_terminal",
  override val title: Int = R.string.title_terminal,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {

  init {
    addPreference(UseSytemShell())
  }
}

@Parcelize
class UiMode(
  override val key: String = GeneralPreferences.UI_MODE,
  override val title: Int = R.string.idepref_general_uiMode,
  override val summary: Int? = R.string.idepref_general_uiMode_summary,
  override val icon: Int? = R.drawable.ic_ui_mode
) : SingleChoicePreference() {

  @IgnoredOnParcel
  override val dialogCancellable = true

  override fun getEntries(preference: Preference): Array<PreferenceChoices.Entry> {
    val context = preference.context
    val currentUiMode = GeneralPreferences.uiMode

    return Array(3) { index ->
      val (label, mode) = when (index) {
        0 -> context.getString(R.string.uiMode_light) to AppCompatDelegate.MODE_NIGHT_NO
        1 -> context.getString(R.string.uiMode_dark) to AppCompatDelegate.MODE_NIGHT_YES
        2 -> context.getString(R.string.uiMode_system) to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        else -> throw IllegalStateException("Invalid index")
      }

      PreferenceChoices.Entry(label, currentUiMode == mode, mode)
    }
  }

  override fun onChoiceConfirmed(
    preference: Preference,
    entry: PreferenceChoices.Entry?,
    position: Int
  ) {
    GeneralPreferences.uiMode = (entry?.data as? Int?) ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
  }
}

@Parcelize
class ThemeSelector(
  override val key: String = GeneralPreferences.SELECTED_THEME,
  override val title: Int = R.string.idepref_general_themeSelector_title,
  override val summary: Int? = R.string.idepref_general_themeSelector_summary,
  override val icon: Int? = R.drawable.ic_color_scheme
) : SingleChoicePreference() {

  @IgnoredOnParcel
  private val themes = IDETheme.entries

  override fun getEntries(preference: Preference): Array<PreferenceChoices.Entry> {
    val context = preference.context
    val currentTheme = IThemeManager.getInstance().getCurrentTheme()
    return Array(themes.size) { index ->
      val ideTheme = themes[index]
      PreferenceChoices.Entry(
        label = ContextCompat.getString(context, ideTheme.title),
        _isChecked = currentTheme.name == ideTheme.name,
        data = ideTheme
      )
    }
  }

  override fun onChoiceConfirmed(
    preference: Preference,
    entry: PreferenceChoices.Entry?,
    position: Int
  ) {
    GeneralPreferences.selectedTheme = (entry?.data as? IDETheme?)?.name ?: IDETheme.DEFAULT.name
  }
}

@Parcelize
class LocaleSelector(
  override val key: String = GeneralPreferences.SELECTED_LOCALE,
  override val title: Int = R.string.idepref_general_localeSelector_title,
  override val summary: Int? = R.string.idepref_general_localeSelector_summary,
  override val icon: Int? = R.drawable.ic_translate
) : SingleChoicePreference() {

  override fun getEntries(preference: Preference): Array<PreferenceChoices.Entry> {
    val context = preference.context
    val currentLocale = GeneralPreferences.selectedLocale
    val supportedLocales = LocaleProvider.SUPPORTED_LOCALES.keys.toList()
    return Array(supportedLocales.size + 1) { index ->
      if (index == 0) {
        PreferenceChoices.Entry(
          label = ContextCompat.getString(context, R.string.locale_system_default),
          _isChecked = GeneralPreferences.selectedLocale == null,
          data = 0
        )
      } else {
        val localeKey = supportedLocales[index - 1]
        val locale = LocaleProvider.getLocale(localeKey)!!
        PreferenceChoices.Entry(
          label = locale.getDisplayName(locale),
          _isChecked = currentLocale == localeKey,
          data = localeKey
        )
      }
    }
  }

  override fun onChoiceConfirmed(
    preference: Preference,
    entry: PreferenceChoices.Entry?,
    position: Int
  ) {
    GeneralPreferences.selectedLocale = entry?.data?.let { localeKey ->
      if (localeKey is Int) null else localeKey as String
    }
  }
}

@Parcelize
class OpenLastProject(
  override val key: String = GeneralPreferences.OPEN_PROJECTS,
  override val title: Int = string.title_open_projects,
  override val summary: Int? = string.msg_open_projects,
  override val icon: Int? = drawable.ic_open_project
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = GeneralPreferences.autoOpenProjects
    return pref
  }

  override fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    GeneralPreferences.autoOpenProjects = newValue as Boolean?
      ?: GeneralPreferences.autoOpenProjects
    return true
  }
}

@Parcelize
class ConfirmProjectOpen(
  override val key: String = GeneralPreferences.CONFIRM_PROJECT_OPEN,
  override val title: Int = string.title_confirm_project_open,
  override val summary: Int? = string.msg_confirm_project_open,
  override val icon: Int? = drawable.ic_open_project
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = GeneralPreferences.confirmProjectOpen
    return pref
  }

  override fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    GeneralPreferences.confirmProjectOpen = newValue as Boolean?
      ?: GeneralPreferences.confirmProjectOpen
    return true
  }
}

@Parcelize
class UseSytemShell(
  override val key: String = GeneralPreferences.TERMINAL_USE_SYSTEM_SHELL,
  override val title: Int = string.title_default_shell,
  override val summary: Int? = string.msg_default_shell,
  override val icon: Int? = drawable.ic_bash_commands
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = GeneralPreferences.useSystemShell
    return pref
  }

  override fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    GeneralPreferences.useSystemShell = newValue as Boolean? ?: GeneralPreferences.useSystemShell
    return true
  }
}
