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

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.doOnAttach
import androidx.core.view.setPadding
import com.itsaky.androidide.utils.EdgeToEdgeUtils
import com.itsaky.androidide.utils.getInsets
import org.slf4j.LoggerFactory

/**
 * Same as IDEActivity but DecorFitsSystemWindows is set to false
 * Useful for creating immersive edge-to-edge experiences.
 *
 * @author Smooth E
 * @author Akash Yadav
 */
abstract class EdgeToEdgeIDEActivity : IDEActivity() {

  /**
   * Whether edge-to-edge should be applied to the activity.
   */
  protected open var edgeToEdgeEnabled = true

  /**
   * Whether the decor view padding should be updated to match the system bars in landscape orientation.
   */
  protected open var eteUpdateDecorViewPaddingInLandscape = true

  /**
   * Style for the status bar.
   */
  protected open val statusBarStyle = EdgeToEdgeUtils.DEFAULT_STATUS_BAR_STYLE!!

  /**
   * Style for the navigation bar.
   */
  protected open val navigationBarStyle = EdgeToEdgeUtils.DEFAULT_NAVIGATION_BAR_STYLE!!

  /**
   * Original padding of the window's decor view.
   */
  protected open var decorViewPadding: Rect? = null

  override var enableSystemBarTheming: Boolean
    get() = false
    set(@Suppress("UNUSED_PARAMETER") value) {
      throw UnsupportedOperationException("Use edgeToEdgeEnabled and systemBarStyles instead")
    }

  private val log = LoggerFactory.getLogger(EdgeToEdgeIDEActivity::class.java)

  @SuppressLint("WrongConstant")
  private val onApplyWindowInsetsListener = OnApplyWindowInsetsListener { v, insets ->
    if (!eteUpdateDecorViewPaddingInLandscape ||
        v.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE
      ) {
      decorViewPadding?.also { p ->
        // when switching from landscape to portrait, restore the original padding
        v.setPadding(p.left, p.top, p.right, p.bottom)
      }
      return@OnApplyWindowInsetsListener insets
    }

    if (decorViewPadding == null) {
      Rect().apply {
        left = v.paddingLeft
        top = v.paddingTop
        right = v.paddingRight
        bottom = v.paddingBottom
      }.also { paddings ->
        decorViewPadding = paddings
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      v.setPadding(
        insets.getInsets(WindowInsets.Type.systemBars()).left,
        0,
        insets.getInsets(WindowInsets.Type.systemBars()).right,
        insets.getInsets(WindowInsets.Type.systemBars()).bottom)
    } else {
      @Suppress("DEPRECATION")
      v.setPadding(
        insets.stableInsetLeft,
        0,
        insets.stableInsetRight,
        insets.stableInsetBottom)
    }

    insets
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    applyEdgeToEdge()
    super.onCreate(savedInstanceState)
  }

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    applyEdgeToEdge()
    super.onCreate(savedInstanceState, persistentState)
  }

  @SuppressLint("RestrictedApi")
  private fun applyEdgeToEdge() {
    this.window!!.apply {
      addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
      @Suppress("DEPRECATION")
      clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }

    EdgeToEdgeUtils.applyEdgeToEdge(this, this.statusBarStyle, this.navigationBarStyle)

    ViewCompat.setOnApplyWindowInsetsListener(
      this.window.decorView,
      this.onApplyWindowInsetsListener
    )

    this.window.decorView.doOnAttach { decorView -> onInsetsUpdated(getInsets(decorView)) }
  }

  /** Called whenever insets are updated */
  protected open fun onInsetsUpdated(insets: Rect) {}
}
