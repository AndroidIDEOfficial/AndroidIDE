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

package com.itsaky.androidide.actions

/**
 * A [ActionItem] related to the code editor. Subclasses can choose whether to dismiss the actions
 * window when this action is performed.
 *
 * @author Akash Yadav
 */
interface EditorActionItem : ActionItem {

  /**
   * Whether the actions window should be dismissed or not once this action is performed.
   *
   * @return `true` if the actions window must be dismissed, `false` otherwise.
   */
  fun dismissOnAction(): Boolean = true
}
