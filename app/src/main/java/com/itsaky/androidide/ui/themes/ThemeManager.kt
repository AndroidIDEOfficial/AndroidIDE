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

package com.itsaky.androidide.ui.themes

import android.app.Activity
import com.itsaky.androidide.managers.PreferenceManager
import com.itsaky.androidide.preferences.internal.enableMaterialYou
import com.itsaky.androidide.utils.isSystemInDarkMode

/**
 * Theme manager for AndroidIDE.
 *
 * @author Akash Yadav
 */
object ThemeManager {

  const val KEY_CURRENT_THEME = "ide.preferences.currentTheme"

  /**
   * Apply the current theme to the given activity. Does nothing if [Material You][enableMaterialYou] is enabled.
   */
  fun applyTheme(activity: Activity) {

    // Don't apply theme if Material You is enabled
    if (enableMaterialYou) {
      return
    }

    val theme = getCurrentTheme(activity)
    val style = if (activity.isSystemInDarkMode()) {
      theme.styleDark
    } else {
      theme.styleLight
    }

    activity.setTheme(style)
  }

  /**
   * Get the currently selected theme.
   */
  fun getCurrentTheme(activity: Activity): IDETheme {
    val preferences = PreferenceManager(activity, "ide.preferences.theme")

    return preferences.getString(KEY_CURRENT_THEME, null)?.let { IDETheme.valueOf(it) }
      ?: IDETheme.DEFAULT
  }
}