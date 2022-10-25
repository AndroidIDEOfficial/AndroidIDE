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
import androidx.preference.Preference
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.preferences.internal.CONFIRM_PROJECT_OPEN
import com.itsaky.androidide.preferences.internal.OPEN_PROJECTS
import com.itsaky.androidide.preferences.internal.TERMINAL_USE_SYSTEM_SHELL
import com.itsaky.androidide.preferences.internal.autoOpenProjects
import com.itsaky.androidide.preferences.internal.confirmProjectOpen
import com.itsaky.androidide.preferences.internal.useSystemShell
import kotlinx.parcelize.Parcelize

@Parcelize
class GeneralPreferences(
  override val key: String = "idepref_general",
  override val title: Int = string.title_general,
  override val summary: Int? = string.idepref_general_summary,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceScreen() {
  init {
    addPreference(OpenLastProject())
    addPreference(ConfirmProjectOpen())
    addPreference(UseSytemShell())
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
