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

import android.view.MenuItem

/**
 * Listener for listening to click events on [ActionItem].
 *
 * @author Akash Yadav
 */
fun interface OnActionClickListener {

  /**
   * Called when the given [action] is clicked.
   *
   * @param registry The [ActionsRegistry].
   * @param action The action that was clicked.
   * @param data The action data associated with the action.
   * @return Whether the click event was consumed.
   */
  fun onClick(registry: ActionsRegistry, action: ActionItem, item: MenuItem, data: ActionData): Boolean
}