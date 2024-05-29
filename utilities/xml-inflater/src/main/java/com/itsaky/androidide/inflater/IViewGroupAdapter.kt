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

/**
 * Handles logic for things related to [IViewGroup].
 *
 * @author Akash Yadav
 */
interface IViewGroupAdapter {

  /**
   * Get the layout strategy for the given view group and the view. Implementations must take the
   * attributes of the view group into account.
   *
   * @param group The view group whose layout strategy should be returned.
   * @return The layout strategy.
   */
  fun getLayoutStrategy(group: IViewGroup): LayoutStrategy

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
  fun computeChildIndex(parent: IViewGroup, x: Float, y: Float): Int {
    return getLayoutStrategy(parent).computeChildIndex(parent, x, y)
  }

  /**
   * Called by the UI Designer to check if the given view group (which this adpater handles) can
   * accept the child view with the given qualified name.
   *
   * @param view The parent in which the child will be added.
   * @param name The fully qualified name of the child view.
   */
  fun canAcceptChild(view: IViewGroup, name: String): Boolean {
    return canAcceptChild(view, null, name)
  }

  /**
   * Called by the UI Designer to check if the given view group (which this adpater handles) can
   * accept the child view with the given qualified name.
   *
   * @param view The parent in which the child will be added.
   * @param child The child view which will be added. May be `null`.
   * @param name The fully qualified name of the child view.
   */
  fun canAcceptChild(view: IViewGroup, child: IView?, name: String): Boolean {
    return true
  }
}
