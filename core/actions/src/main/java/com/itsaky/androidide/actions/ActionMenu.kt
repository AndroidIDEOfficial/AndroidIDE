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
 * An action menu is an action which can contain child actions.
 * @author Akash Yadav
 */
interface ActionMenu : ActionItem {

  val children: MutableSet<ActionItem>

  fun addAction(action: ActionItem) = children.add(action)

  fun removeAction(action: ActionItem) = children.remove(action)

  /**
   * Find the action item with the given action ID.
   *
   * @return The action item or `null` if not found.
   */
  fun findAction(id: String): ActionItem? {
    return children.find { it.id == id }
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    visible = children.isNotEmpty() && isAtLeastOneChildVisible(data)
    enabled = visible
  }

  /** Action menus are not supposed to perform any action */
  override suspend fun execAction(data: ActionData): Boolean {
    return false
  }

  /**
   * Calls [ActionItem.prepare] on each child action and returns `true` if at least one of them
   * is [visible][ActionItem.visible].
   */
  fun isAtLeastOneChildVisible(data: ActionData) : Boolean {
    return children.firstOrNull { it.prepare(data); it.visible } != null
  }
}
