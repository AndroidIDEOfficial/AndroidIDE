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

import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.itsaky.androidide.resources.R

/**
 * Themes in AndroidIDE.
 *
 * @author Akash Yadav
 */
enum class IDETheme(
  @StyleRes val styleLight: Int,
  @StyleRes val styleDark: Int,
  @StringRes val title: Int
) {

  /**
   * Blue Wave theme.
   */
  BLUE_WAVE(R.style.Theme_AndroidIDE_BlueWave, R.style.Theme_AndroidIDE_BlueWave_Dark,
    R.string.theme_blue_wave),

  /**
   * Sunny Glow theme.
   */
  SUNNY_GLOW(R.style.Theme_AndroidIDE_SunnyGlow, R.style.Theme_AndroidIDE_SunnyGlow_Dark,
    R.string.theme_sunny_glow),

  /**
   * Material You theme.
   */
  MATERIAL_YOU(-1, -1, R.string.theme_material_you);

  companion object {

    /**
     * The default theme.
     */
    val DEFAULT = BLUE_WAVE
  }
}