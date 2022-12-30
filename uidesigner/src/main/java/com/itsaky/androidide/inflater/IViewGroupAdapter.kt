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
   * Get the layout behavior for the given view group and the view. Implementations must take the
   * attributes of the view group into account.
   *
   * @param group The view group whose layout behavior should be returned.
   * @return The layout behavior.
   */
  fun getLayoutBehavior(group: IViewGroup): LayoutBehavior
  
  /**
   * Called by the UI Designer to check if the given view group (which this adpater handles) can
   * accept the child view with the given qualified name.
   */
  fun canAcceptChild(view: IViewGroup, name: String): Boolean {
    return true
  }
}
