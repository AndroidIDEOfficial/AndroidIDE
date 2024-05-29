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

package com.itsaky.androidide.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

/**
 * A [DrawerLayout] that scales its content when navigation drawers are opened or closed.
 *
 * @author Akash Yadav
 */
class ContentTranslatingDrawerLayout : InterceptableDrawerLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr)

  /**
   * The ID of the child view which will be translated when the navigation views are expanded/collapsed.
   *
   * Set this value to `-1` to disable transition.
   */
  var childId: Int = -1

  /**
   * The [TranslationBehavior] for the start navigation view.
   */
  var translationBehaviorStart: TranslationBehavior = TranslationBehavior.DEFAULT

  /**
   * The [TranslationBehavior] for the end navigation view.
   */
  var translationBehaviorEnd: TranslationBehavior = TranslationBehavior.DEFAULT

  private val mListener =
    object : SimpleDrawerListener() {
      override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        if (childId == -1) {
          return
        }

        val gravity = (drawerView.layoutParams as LayoutParams).gravity
        val view = findViewById<View>(childId) ?: return
        val (direction, maxOffset) = if (gravity == GravityCompat.START) {
          1 to translationBehaviorStart.maxOffset
        } else {
          -1 to translationBehaviorEnd.maxOffset
        }

        val offset = (drawerView.width * slideOffset) * maxOffset
        view.translationX = direction * offset
      }
    }

  init {
    addDrawerListener(mListener)
  }

  /**
   * Translation behavior for content view of [ContentTranslatingDrawerLayout].
   */
  enum class TranslationBehavior(val maxOffset: Float) {

    /**
     * The default translation behavior. This makes the child view translate partially according to
     * the slide offset of the [NavigationView][com.google.android.material.navigation.NavigationView]
     */
    DEFAULT(0.2f),

    /**
     * Makes the child child view translate according to the slide offset of the
     * [NavigationView][com.google.android.material.navigation.NavigationView]. The translation offset
     * is always equal to the slide offset in this behavior.
     */
    FULL(0.95f);
  }
}
