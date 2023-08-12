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
import com.itsaky.androidide.preferences.internal.prefManager
import kotlinx.parcelize.Parcelize

internal const val KEY_DEVOPTS = "ide.prefs.developerOptions"
internal const val KEY_DEVOPTS_DEBUGGING = "${KEY_DEVOPTS}.debugging"
internal const val KEY_DEVOPTS_DEBUGGING_DUMPLOGS = "${KEY_DEVOPTS_DEBUGGING}.dumpLogs"
internal const val KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER = "${KEY_DEVOPTS_DEBUGGING}.enableLogsender"

@Parcelize
internal class DeveloperOptionsScreen(override val key: String = KEY_DEVOPTS,
  override val title: Int = R.string.title_developer_options,
  override val summary: Int? = R.string.idepref_devOptions_summary,
  override val children: List<IPreference> = mutableListOf()) : IPreferenceScreen() {

  init {
    addPreference(DebuggingPreferences())
  }
}

@Parcelize
internal class DebuggingPreferences(
  override val key: String = KEY_DEVOPTS_DEBUGGING,
  override val title: Int = R.string.idepref_group_debugging,
  override val children: List<IPreference> = mutableListOf()) : IPreferenceGroup() {

  init {
    addPreference(DumpLogsPreference())
    addPreference(EnableLogSenderPreference())
  }
}

internal var dumpLogs: Boolean
  get() = prefManager.getBoolean(KEY_DEVOPTS_DEBUGGING_DUMPLOGS, false)
  set(value) {
    prefManager.putBoolean(KEY_DEVOPTS_DEBUGGING_DUMPLOGS, value)
  }

@Parcelize
internal class DumpLogsPreference(
  override val key: String = KEY_DEVOPTS_DEBUGGING_DUMPLOGS,
  override val title: Int = R.string.idepref_devOptions_dumpLogs_title,
  override val summary: Int? = R.string.idepref_devOptions_dumpLogs_summary) :
  SwitchPreference(setValue = ::dumpLogs::set, getValue = ::dumpLogs::get)

internal var logsenderEnabled: Boolean
  get() = prefManager.getBoolean(KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER, true)
  set(value) {
    prefManager.putBoolean(KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER, value)
  }

@Parcelize
internal class EnableLogSenderPreference(
  override val key: String = KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER,
  override val title: Int = R.string.idepref_devOptions_enableLogsender_title,
  override val summary: Int? = R.string.idepref_devOptions_enableLogsender_summary) :
  SwitchPreference(setValue = ::logsenderEnabled::set, getValue = ::logsenderEnabled::get)