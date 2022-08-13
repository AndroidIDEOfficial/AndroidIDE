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

package com.itsaky.androidide.models.prefs

const val IS_FIRST_PROJECT_BUILD = "project_isFirstBuild"
const val OPEN_PROJECTS = "idepref_general_autoOpenProjects"
const val CONFIRM_PROJECT_OPEN = "idepref_general_confirmProjectOpen"
const val TERMINAL_USE_SYSTEM_SHELL = "idepref_general_terminalShell"
const val LAST_OPENED_PROJECT = "ide_last_project"

const val NO_OPENED_PROJECT = "<NO_OPENED_PROJECT>"

var isFirstBuild: Boolean
  get() = prefManager.getBoolean(IS_FIRST_PROJECT_BUILD, true)
  set(value) {
    prefManager.putBoolean(IS_FIRST_PROJECT_BUILD, value)
  }

var autoOpenProjects: Boolean
  get() = prefManager.getBoolean(OPEN_PROJECTS, true)
  set(value) {
    prefManager.putBoolean(OPEN_PROJECTS, value)
  }

var confirmProjectOpen: Boolean
  get() = prefManager.getBoolean(CONFIRM_PROJECT_OPEN, false)
  set(value) {
    prefManager.putBoolean(CONFIRM_PROJECT_OPEN, value)
  }

var useSystemShell: Boolean
  get() = prefManager.getBoolean(TERMINAL_USE_SYSTEM_SHELL, false)
  set(value) {
    prefManager.putBoolean(TERMINAL_USE_SYSTEM_SHELL, value)
  }

var lastOpenedProject: String
  get() = prefManager.getString(LAST_OPENED_PROJECT, NO_OPENED_PROJECT)
  set(value) {
    prefManager.putString(LAST_OPENED_PROJECT, value)
  }
