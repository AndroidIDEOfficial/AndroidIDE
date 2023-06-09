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

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
import androidx.core.view.WindowCompat
import androidx.core.view.doOnAttach
import com.itsaky.androidide.utils.NavigationBar
import com.itsaky.androidide.utils.getInsets
import com.itsaky.androidide.utils.resolveAttr

/**
 * Same as IDEActivity but DecorFitsSystemWindows is set to false
 * Useful for creating immersive edge-to-edge experiences
 *
 * @author Smooth E
 */
abstract class LimitlessIDEActivity(
  private val highlightNavigationBar: Boolean = false
) : IDEActivity() {

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

    paintNavigationBar()

    // This removes a black strip on the side where the camera cutout is in landscape mode
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      window.attributes.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
  }

  private fun installOnDecorViewAttachedListener() {
    window.decorView.doOnAttach { decorView -> onInsetsUpdated(getInsets(decorView)) }
  }

  /** Called whenever insets are updated */
  open fun onInsetsUpdated(insets: Rect) { }

  private fun paintNavigationBar() {
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT

    if (!highlightNavigationBar) {
      return
    }

    val refrainHighlighting =
      NavigationBar.getInteractionMode(this) == NavigationBar.MODE_GESTURES ||
      resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (refrainHighlighting) {
      return
    }

    val navigationBarColor = resolveAttr(com.google.android.material.R.attr.colorSurface)
    window.navigationBarColor = navigationBarColor

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      val dividerColor = resolveAttr(com.google.android.material.R.attr.colorOutlineVariant)
      window.navigationBarDividerColor = dividerColor
    }
  }

}
