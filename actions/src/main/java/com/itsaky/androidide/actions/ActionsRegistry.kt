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

import android.view.Menu
import com.itsaky.androidide.actions.internal.DefaultActionsRegistry

/** @author Akash Yadav */
abstract class ActionsRegistry {

  companion object {

    private var instance: ActionsRegistry? = null

    @JvmStatic
    fun getInstance(): ActionsRegistry {
      if (instance == null) {
        instance = DefaultActionsRegistry()
      }

      return instance as ActionsRegistry
    }
  }

  /**
   * Register the given action.
   *
   * @param action The action to register. Must not be `null`.
   * @return `true` if the action was registered successfully. `false` otherwise.
   */
  abstract fun registerAction(action: ActionItem): Boolean

  /**
   * Unregister the given action.
   *
   * @param action The action to unregister. Must not be `null`.
   * @return `true` if the action was unregistered successfully. `false` otherwise.
   */
  abstract fun unregisterAction(action: ActionItem): Boolean

  /**
   * Unregister the action with the given ID.
   *
   * @param id The ID of action to unregister. Must not be `null`.
   * @return `true` if the action was unregistered successfully. `false` otherwise.
   */
  abstract fun unregisterAction(id: String): Boolean

  /**
   * Find the action with the given ID.
   *
   * @param id The ID of the action to find.
   * @return The found action or `null`.
   */
  abstract fun findAction(location: ActionItem.Location, id: String): ActionItem?

  /**
   * Fill the given menu with the registered actions.
   *
   * Subclasses must first call [ActionItem.prepare] to update the action.
   */
  abstract fun fillMenu(data: ActionData, location: ActionItem.Location, menu: Menu)

  /** Get all the registered actions at the given location. */
  abstract fun getActions(location: ActionItem.Location): Map<String, ActionItem>
  /** Clear all the registered actions. */
  abstract fun clearActions(location: ActionItem.Location)

  /**
   * Register a listener that will be called when any action is executed. The callbacks are called
   * irrespective of the action's execution result.
   *
   * @param listener The listener to register.
   */
  abstract fun registerActionExecListener(listener: ActionExecListener)

  /**
   * Unregister the given listener.
   *
   * @param listener The listener to unregister.
   */
  abstract fun unregisterActionExecListener(listener: ActionExecListener)

  /** Listener for listening to callbacks for action execution. */
  interface ActionExecListener {

    /**
     * The given action was executed.
     * @param action The action that was executed.
     * @param result The result of the action execution.
     */
    fun onExec(action: ActionItem, result: Any)
  }
}
