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
object DevOpsPreferences {

  const val KEY_DEVOPTS = "ide.prefs.developerOptions"
  const val KEY_DEVOPTS_DEBUGGING = "${KEY_DEVOPTS}.debugging"
  const val KEY_DEVOPTS_DEBUGGING_DUMPLOGS = "${KEY_DEVOPTS_DEBUGGING}.dumpLogs"
  const val KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER = "${KEY_DEVOPTS_DEBUGGING}.enableLogsender"

  var dumpLogs: Boolean
    get() = prefManager.getBoolean(KEY_DEVOPTS_DEBUGGING_DUMPLOGS, false)
    set(value) {
      prefManager.putBoolean(KEY_DEVOPTS_DEBUGGING_DUMPLOGS, value)
    }

  var logsenderEnabled: Boolean
    get() = prefManager.getBoolean(KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER, true)
    set(value) {
      prefManager.putBoolean(KEY_DEVOPTS_DEBUGGING_ENABLE_LOGSENDER, value)
    }
}