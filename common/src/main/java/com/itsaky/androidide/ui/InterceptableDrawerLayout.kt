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
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout

/**
 * A [DrawerLayout] which allows intercepting the touch events by its children.
 *
 * @author Akash Yadav
 */
open class InterceptableDrawerLayout : DrawerLayout {

  private val rect: Rect = Rect()

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
  ) : super(context, attrs, defStyleAttr)

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    val child = findScrollingChild(this, event.x, event.y)
    return if (child != null) {
      false
    } else super.onInterceptTouchEvent(event)
  }

  private fun findScrollingChild(parent: ViewGroup, x: Float, y: Float): View? {
    val n = parent.childCount
    if (parent === this && n <= 1) {
      return null
    }

    var start = 0
    if (parent === this) {
      start = 1
    }

    for (i in start until n) {
      val child = parent.getChildAt(i)
      if (child.visibility != View.VISIBLE) {
        continue
      }

      child.getHitRect(rect)
      if (!rect.contains(x.toInt(), y.toInt())) {
        continue
      }

      if (child.canScrollHorizontally(-1) // left
        || child.canScrollHorizontally(1) // right
        ) {
        return child
      }

      if (child !is ViewGroup) {
        continue
      }

      val v = findScrollingChild(child, x - rect.left, y - rect.top)
      if (v != null) {
        return v
      }
    }
    return null
  }
}
