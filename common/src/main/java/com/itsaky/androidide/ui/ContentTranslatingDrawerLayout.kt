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
import com.itsaky.androidide.utils.ILogger

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

  private val log = ILogger.newInstance("ContentTranslatingDrawerLayout")

  companion object {
    const val MAX_SCALE = 0.2f
  }

  var childId: Int = -1
  private val mListener =
    object : SimpleDrawerListener() {
      override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        if (childId == -1) {
          return
        }
        val gravity = (drawerView.layoutParams as LayoutParams).gravity
        val view = findViewById<View>(childId) ?: return
        val offset = (drawerView.width * slideOffset) * MAX_SCALE
        view.translationX = (if (gravity == GravityCompat.START) 1 else -1) * offset
      }
    }

  init {
    addDrawerListener(mListener)
  }
}
