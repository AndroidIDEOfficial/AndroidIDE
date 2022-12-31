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

package com.itsaky.androidide.uidesigner.models

import android.graphics.RectF
import android.view.ViewGroup
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroupAdapter
import com.itsaky.androidide.inflater.LayoutBehavior.HORIZONTAL
import com.itsaky.androidide.inflater.LayoutBehavior.TOP_LEFT
import com.itsaky.androidide.inflater.LayoutBehavior.VERTICAL
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewGroupImpl
import com.itsaky.androidide.inflater.viewAdapter

/**
 * Extension of [IView] for the UI Designer.
 *
 * @author Akash Yadav
 */
internal class UiViewGroup(file: LayoutFile, name: String, view: ViewGroup) :
  ViewGroupImpl(file, name, view), CommonUiView by CommonUiViewImpl() {

  /**
   * Finds the index of the view based on the touch location.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @return The index where the new child should be added. This index must be valid.
   */
  fun computeViewIndex(x: Float, y: Float): Int {
    if (childCount == 0) {
      return 0
    }
    val adapter = viewAdapter

    if (adapter !is IViewGroupAdapter) {
      return childCount
    }

    return when (adapter.getLayoutBehavior(this)) {
      TOP_LEFT -> childCount
      VERTICAL -> computeViewIndexVertically(x, y)
      HORIZONTAL -> computeViewIndexHorizontally(x, y)
    }
  }

  private fun computeViewIndexHorizontally(x: Float, y: Float): Int {
    get(0).apply {
      val rect = getViewRect(this)
      if (x < rect.left) {
        return 0
      }
    }
    get(childCount - 1).apply {
      val rect = getViewRect(this)
      if (x > rect.right) {
        return childCount
      }
    }
    val (child, index) = findNearestChild(x, y, false) ?: return childCount
    val rect = getViewRect(child)
    val mid = rect.left + (rect.width() / 2)
    val left = RectF(rect).apply { this.right = mid - 10 }
    val right = RectF(rect).apply { this.left = mid + 10 }
    if (x > left.left && x < left.right) {
      return index - 1
    }
    if (x > right.left && x < right.right) {
      return index + 1
    }
    return index
  }

  private fun computeViewIndexVertically(x: Float, y: Float): Int {
    get(0).apply {
      val rect = getViewRect(this)
      if (y < rect.top) {
        return 0
      }
    }
    get(childCount - 1).apply {
      val rect = getViewRect(this)
      if (y > rect.bottom) {
        return childCount
      }
    }
    val (child, index) = findNearestChild(x, y, true) ?: return childCount
    val rect = getViewRect(child)
    val mid = rect.top + (rect.height() / 2)
    val top = RectF(rect).apply { this.bottom = mid - 10 }
    val bottom = RectF(rect).apply { this.top = mid + 10 }
    if (y > top.top && y < top.bottom) {
      return index - 1
    }
    if (y > bottom.top && y < bottom.bottom) {
      return index + 1
    }
    return index
  }

  private fun findNearestChild(x: Float, y: Float, vertical: Boolean = true): Pair<IView, Int>? {
    for (i in 0 until childCount) {
      val child = get(i)
      if (child is CommonUiView && !child.includeInIndexComputation) {
        continue
      }
      val rect = getViewRect(child)
      if (vertical && (y > rect.top && y < rect.bottom)) {
        return child to i
      }

      if (!vertical && (x > rect.left && x < rect.right)) {
        return child to i
      }
    }

    return null
  }

  private fun getViewRect(view: IView): RectF {
    val v = view.view
    val rect = RectF()
    rect.left = v.left.toFloat()
    rect.top = v.top.toFloat()
    rect.right = rect.left + v.width
    rect.bottom = rect.top + v.height
    return rect
  }
}
