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
object JavaPreferences {

  const val GOOGLE_CODE_STYLE = "idepref_editor_java_googleCodeStyle"
  const val JAVA_DIAGNOSTICS_ENABLED = "idepref_editor_java_diagnosticsEnabled"

  var googleCodeStyle: Boolean
    get() = prefManager.getBoolean(GOOGLE_CODE_STYLE, false)
    set(value) {
      prefManager.putBoolean(GOOGLE_CODE_STYLE, value)
    }

  /** Whether diagnostics are enabled for the Java source files. */
  var isJavaDiagnosticsEnabled: Boolean
    get() = prefManager.getBoolean(JAVA_DIAGNOSTICS_ENABLED, true)
    set(value) {
      prefManager.putBoolean(JAVA_DIAGNOSTICS_ENABLED, value)
    }

}