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
import com.itsaky.androidide.preferences.internal.JavaPreferences
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import kotlinx.parcelize.Parcelize

@Parcelize
internal class JavaCodeConfigurations(
  override val key: String = "idepref_editor_java",
  override val title: Int = string.idepref_editor_category_java,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {

  init {
    addPreference(GoogleCodeStyle())
    addPreference(JavaDiagnosticsEnabled())
  }
}

/** @author Akash Yadav */
@Parcelize
private class GoogleCodeStyle(
  override val key: String = JavaPreferences.GOOGLE_CODE_STYLE,
  override val title: Int = string.idepref_java_useGoogleStyle_title,
  override val summary: Int? = string.idepref_java_useGoogleStyle_summary,
  override val icon: Int? = drawable.ic_format_code,
) : SwitchPreference(getValue = JavaPreferences::googleCodeStyle::get,
  setValue = JavaPreferences::googleCodeStyle::set)

@Parcelize
private class JavaDiagnosticsEnabled(
  override val key: String = JavaPreferences.JAVA_DIAGNOSTICS_ENABLED,
  override val title: Int = R.string.idepref_java_diagnosticEnabled_title,
  override val summary: Int? = R.string.idepref_java_diagnosticsEnabled_summary,
  override val icon: Int? = drawable.ic_compilation_error
) :
  SwitchPreference(
    getValue = JavaPreferences::isJavaDiagnosticsEnabled::get,
    setValue = JavaPreferences::isJavaDiagnosticsEnabled::set
  )
