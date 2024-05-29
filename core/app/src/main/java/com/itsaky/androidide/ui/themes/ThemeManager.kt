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
import com.google.auto.service.AutoService
import com.itsaky.androidide.preferences.internal.GeneralPreferences
import com.itsaky.androidide.utils.isSystemInDarkMode

/**
 * Theme manager for AndroidIDE.
 *
 * @author Akash Yadav
 */
@Suppress("unused")
@AutoService(IThemeManager::class)
class ThemeManager : IThemeManager {

  /**
   * Apply the current theme to the given activity. Does nothing if theme is set to [Material You][IDETheme.MATERIAL_YOU].
   */
  override fun applyTheme(activity: Activity) {

    val theme = getCurrentTheme()
    if (theme == IDETheme.MATERIAL_YOU) {
      // No need to apply Material You theme
      return
    }

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
  override fun getCurrentTheme(): IDETheme {
    return GeneralPreferences.selectedTheme?.let { IDETheme.valueOf(it) } ?: IDETheme.DEFAULT
  }
}