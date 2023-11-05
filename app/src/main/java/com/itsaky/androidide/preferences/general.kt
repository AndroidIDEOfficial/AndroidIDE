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
import androidx.preference.Preference
import com.itsaky.androidide.R
import com.itsaky.androidide.preferences.internal.CONFIRM_PROJECT_OPEN
import com.itsaky.androidide.preferences.internal.OPEN_PROJECTS
import com.itsaky.androidide.preferences.internal.SELECTED_LOCALE
import com.itsaky.androidide.preferences.internal.SELECTED_THEME
import com.itsaky.androidide.preferences.internal.TERMINAL_USE_SYSTEM_SHELL
import com.itsaky.androidide.preferences.internal.UI_MODE
import com.itsaky.androidide.preferences.internal.autoOpenProjects
import com.itsaky.androidide.preferences.internal.confirmProjectOpen
import com.itsaky.androidide.preferences.internal.selectedLocale
import com.itsaky.androidide.preferences.internal.selectedTheme
import com.itsaky.androidide.preferences.internal.uiMode
import com.itsaky.androidide.preferences.internal.useSystemShell
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.resources.localization.LocaleProvider
import com.itsaky.androidide.ui.themes.IDETheme
import com.itsaky.androidide.ui.themes.ThemeManager
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
class GeneralPreferences(
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
  override val key: String = UI_MODE,
  override val title: Int = R.string.idepref_general_uiMode,
  override val summary: Int? = R.string.idepref_general_uiMode_summary,
  override val icon: Int? = R.drawable.ic_ui_mode
) : SingleChoicePreference() {

  @IgnoredOnParcel
  override val dialogCancellable = true

  override fun getChoices(context: Context): Array<String> {
    return arrayOf(
      context.getString(R.string.uiMode_light),
      context.getString(R.string.uiMode_dark),
      context.getString(R.string.uiMode_system)
    )
  }

  override fun getInitiallySelectionItemPosition(context: Context): Int {
    return when (uiMode) {
      AppCompatDelegate.MODE_NIGHT_NO -> 0
      AppCompatDelegate.MODE_NIGHT_YES -> 1
      else -> 2
    }
  }

  override fun onChoiceConfirmed(position: Int) {
    uiMode = when (position) {
      0 -> AppCompatDelegate.MODE_NIGHT_NO
      1 -> AppCompatDelegate.MODE_NIGHT_YES
      else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
  }
}

@Parcelize
class ThemeSelector(
  override val key: String = SELECTED_THEME,
  override val title: Int = R.string.idepref_general_themeSelector_title,
  override val summary: Int? = R.string.idepref_general_themeSelector_summary,
  override val icon: Int? = R.drawable.ic_color_scheme
) : SingleChoicePreference() {

  @IgnoredOnParcel
  private val themes = IDETheme.values()

  override fun getChoices(context: Context): Array<String> {
    return themes.map { context.getString(it.title) }.toTypedArray()
  }

  override fun getInitiallySelectionItemPosition(context: Context): Int {
    return themes.indexOf(ThemeManager.getCurrentTheme())
  }

  override fun onChoiceConfirmed(position: Int) {
    selectedTheme = themes[position].name
  }
}

@Parcelize
class LocaleSelector(
  override val key: String = SELECTED_LOCALE,
  override val title: Int = R.string.idepref_general_localeSelector_title,
  override val summary: Int? = R.string.idepref_general_localeSelector_summary,
  override val icon: Int? = R.drawable.ic_translate
) : SingleChoicePreference() {
  
  @IgnoredOnParcel
  private val locales: Array<String>? = null

  override fun getChoices(context: Context): Array<String> {
    return locales ?: mutableListOf<String>().apply { 
      add(context.getString(R.string.locale_system_default))
      
      LocaleProvider.SUPPORTED_LOCALES.map {
        val locale = Locale.forLanguageTag(it)
        locale.getDisplayName(locale)
      }.also { addAll(it) }
    }.toTypedArray()
  }

  override fun getInitiallySelectionItemPosition(context: Context): Int {
    val applicationLocales = AppCompatDelegate.getApplicationLocales()
    val locale = if (applicationLocales.size() > 0) {
      applicationLocales.get(0)!!.language
    } else selectedLocale ?: return 0 // Use system default if not set
    return LocaleProvider.SUPPORTED_LOCALES.indexOf(locale) + 1
  }

  override fun onChoiceConfirmed(position: Int) {
    // Reset to null if 'System Default' is selected
    selectedLocale = if (position == 0) null else LocaleProvider.SUPPORTED_LOCALES[position - 1]
  }
}

@Parcelize
class OpenLastProject(
  override val key: String = OPEN_PROJECTS,
  override val title: Int = string.title_open_projects,
  override val summary: Int? = string.msg_open_projects,
  override val icon: Int? = drawable.ic_open_project
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = autoOpenProjects
    return pref
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    autoOpenProjects = newValue as Boolean? ?: autoOpenProjects
    return true
  }
}

@Parcelize
class ConfirmProjectOpen(
  override val key: String = CONFIRM_PROJECT_OPEN,
  override val title: Int = string.title_confirm_project_open,
  override val summary: Int? = string.msg_confirm_project_open,
  override val icon: Int? = drawable.ic_open_project
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = confirmProjectOpen
    return pref
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    confirmProjectOpen = newValue as Boolean? ?: confirmProjectOpen
    return true
  }
}

@Parcelize
class UseSytemShell(
  override val key: String = TERMINAL_USE_SYSTEM_SHELL,
  override val title: Int = string.title_default_shell,
  override val summary: Int? = string.msg_default_shell,
  override val icon: Int? = drawable.ic_bash_commands
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val pref = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    pref.isChecked = useSystemShell
    return pref
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    useSystemShell = newValue as Boolean? ?: useSystemShell
    return true
  }
}
