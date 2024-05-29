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

import com.itsaky.androidide.R
import com.itsaky.androidide.preferences.internal.DevOpsPreferences
import kotlinx.parcelize.Parcelize

@Parcelize
internal class DeveloperOptionsScreen(
  override val key: String = DevOpsPreferences.KEY_DEVOPTS,
  override val title: Int = R.string.title_developer_options,
  override val summary: Int? = R.string.idepref_devOptions_summary,
  override val children: List<IPreference> = mutableListOf()) : IPreferenceScreen() {

  init {
    addPreference(DebuggingPreferences())
  }
}

@Parcelize
internal class DebuggingPreferences(
  override val key: String = DevOpsPreferences.KEY_DEVOPTS_DEBUGGING,
  override val title: Int = R.string.idepref_group_debugging,
  override val children: List<IPreference> = mutableListOf()) : IPreferenceGroup() {

  init {
    addPreference(DumpLogsPreference())
    addPreference(EnableLogSenderPreference())
  }
}

@Parcelize
internal class DumpLogsPreference(
  override val key: String = DevOpsPreferences.KEY_DEVOPTS_DEBUGGING_DUMPLOGS,
  override val title: Int = R.string.idepref_devOptions_dumpLogs_title,
  override val summary: Int? = R.string.idepref_devOptions_dumpLogs_summary) :
  SwitchPreference(setValue = DevOpsPreferences::dumpLogs::set,
    getValue = DevOpsPreferences::dumpLogs::get)

@Parcelize
internal class EnableLogSenderPreference(
  override val key: String = DevOpsPreferences.KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER,
  override val title: Int = R.string.idepref_devOptions_enableLogsender_title,
  override val summary: Int? = R.string.idepref_devOptions_enableLogsender_summary) :
  SwitchPreference(setValue = DevOpsPreferences::logsenderEnabled::set,
    getValue = DevOpsPreferences::logsenderEnabled::get)