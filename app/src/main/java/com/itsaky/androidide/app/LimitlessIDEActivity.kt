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

package com.itsaky.androidide.app

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.view.WindowCompat

/**
 * Same as IDEActivity but DecorFitsSystemWindows is set to false
 * Useful for creating immersive experiences
 *
 * @author Smooth E
 */
abstract class LimitlessIDEActivity : IDEActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    makeLimitless()
  }

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    makeLimitless()
  }

  private fun makeLimitless() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT

    // TODO: Implement correct behaviour with LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    //  in landscape mode
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    //   window.attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    // }
  }

}
