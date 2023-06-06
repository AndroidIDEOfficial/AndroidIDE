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
import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
import androidx.core.view.WindowCompat
import androidx.core.view.doOnAttach

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
    installOnDecorViewAttachedListener()
  }

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    makeLimitless()
    installOnDecorViewAttachedListener()
  }

  private fun makeLimitless() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT

    // This removes a black strip on the side where the camera cutout is in landscape mode
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      window.attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
  }

  private fun installOnDecorViewAttachedListener() {
    window.decorView.doOnAttach { decorView -> updateInsets(decorView) }
  }

  private fun updateInsets(decorView: View)
  {
    val insets: Rect
    val rootWindowInsets = decorView.rootWindowInsets

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

      val receivedInsets: Insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val typeMask = WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout()
        rootWindowInsets.getInsetsIgnoringVisibility(typeMask)
      } else {
        decorView.rootWindowInsets.stableInsets
      }

      insets = Rect(
        receivedInsets.left,
        receivedInsets.top,
        receivedInsets.right,
        receivedInsets.bottom
      )
    } else {
      insets = Rect(
        rootWindowInsets.stableInsetLeft,
        rootWindowInsets.stableInsetTop,
        rootWindowInsets.stableInsetRight,
        rootWindowInsets.stableInsetBottom
      )
    }

    onInsetsUpdated(insets)
  }

  /** Called when insets are updated */
  open fun onInsetsUpdated(insets: Rect) { }

}
