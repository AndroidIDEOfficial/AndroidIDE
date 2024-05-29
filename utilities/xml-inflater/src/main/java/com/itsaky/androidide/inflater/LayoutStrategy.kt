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

package com.itsaky.androidide.inflater

import android.graphics.RectF

/**
 * Represents how an [IViewGroup] lays its children. This is used to determine the position of the
 * view that the user drags in the UI Designer's workspace.
 *
 * @author Akash Yadav
 */
interface LayoutStrategy {

  companion object {

    /** The child views are laid out vertically. */
    @JvmStatic
    val VERTICAL = VerticalLayoutStrategy()

    /** The child views are laid out horizontally. */
    @JvmStatic
    val HORIZONTAL = HorizontalLayoutStrategy()

    /**
     * The child views are placed on the top left corner of the view group. This is the default value.
     */
    @JvmStatic
    val TOP_LEFT = TopLeftLayoutStrategy()
  }

  /**
   * Compute the index of the child based on the given [x] and [y] coordinates
   * of the drag event.
   *
   * @param parent The [IViewGroup] in which the child will be added.
   *   This is guaranteed to have at least one child view.
   * @param x The X coordinate of the drag event.
   * @param y The Y coordinate of the drag event.
   * @return The index at which the new child must be added
   */
  fun computeChildIndex(parent: IViewGroup, x: Float, y: Float) : Int

  class TopLeftLayoutStrategy : LayoutStrategy {
    override fun computeChildIndex(parent: IViewGroup, x: Float, y: Float): Int {
      return parent.childCount
    }
  }

  class VerticalLayoutStrategy : LayoutStrategy {

    override fun computeChildIndex(parent: IViewGroup, x: Float, y: Float
    ): Int {
      return parent.run {
        get(0).apply {
          val rect = getViewRect()
          if (y < rect.top) {
            return@run 0
          }
        }

        get(childCount - 1).apply {
          val rect = getViewRect()
          if (y > rect.bottom) {
            return@run childCount
          }
        }

        val (child, index) = findNearestChild(x, y, true) ?: return@run childCount
        val rect = child.getViewRect()
        val mid = rect.top + (rect.height() / 2)
        val top = RectF(rect).apply { this.bottom = mid - 10 }
        val bottom = RectF(rect).apply { this.top = mid + 10 }

        if (y > top.top && y < top.bottom) {
          return@run index - 1
        }

        if (y > bottom.top && y < bottom.bottom) {
          return@run index + 1
        }

        return@run index
      }
    }
  }

  class HorizontalLayoutStrategy : LayoutStrategy {

    override fun computeChildIndex(parent: IViewGroup, x: Float, y: Float): Int {
      return parent.run {
        get(0).apply {
          val rect = getViewRect()
          if (x < rect.left) {
            return@run 0
          }
        }
        get(childCount - 1).apply {
          val rect = getViewRect()
          if (x > rect.right) {
            return@run childCount
          }
        }
        val (child, index) = findNearestChild(x, y, false) ?: return@run childCount
        val rect = child.getViewRect()
        val mid = rect.left + (rect.width() / 2)
        val left = RectF(rect).apply { this.right = mid - 10 }
        val right = RectF(rect).apply { this.left = mid + 10 }
        if (x > left.left && x < left.right) {
          return@run index - 1
        }
        if (x > right.left && x < right.right) {
          return@run index + 1
        }

        return@run index
      }
    }
  }
}
