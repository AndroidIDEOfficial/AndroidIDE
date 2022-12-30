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
 * Represents how an [IViewGroup] lays its children. This is used to determine the position of the
 * view that the user drags in the UI Designer's workspace.
 *
 * @author Akash Yadav
 */
enum class LayoutBehavior {

  /** The child views are laid out vertically. */
  VERTICAL,

  /** The child views are laid out horizontally. */
  HORIZONTAL,

  /**
   * The child views are placed on the top left corner of the view group. This is the default value.
   */
  TOP_LEFT
}
