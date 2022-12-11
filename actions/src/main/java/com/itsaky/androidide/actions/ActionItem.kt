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

import android.graphics.drawable.Drawable
import android.view.View

/**
 * An action that can be registered using the [ActionsRegistry]
 * [com.itsaky.androidide.actions.ActionsRegistry]
 * @author Akash Yadav
 */
interface ActionItem {

  val id: String
  var label: String
  var visible: Boolean
  var enabled: Boolean
  var icon: Drawable?
  var requiresUIThread: Boolean
  var location: Location

  /**
   * Prepare the action. Subclasses can modify the visual properties of this action here.
   * @param data The data containing various information about the event.
   */
  fun prepare(data: ActionData)

  /**
   * Execute the action. The action executed in a background thread by default.
   *
   * @param data The data containing various information about the event.
   * @return `true` if this action was executed successfully, `false` otherwise.
   */
  fun execAction(data: ActionData): Any

  /**
   * Called just after the [execAction] method executes **successfully** (i.e. returns `true`).
   * Subclasses are free to do UI related work here as this method is called on UI thread.
   *
   * @param data The data containing various information about the event.
   */
  fun postExec(data: ActionData, result: Any) = Unit

  /**
   * Return the show as action flags for the menu item.
   *
   * @return The show as action flags.
   */
  fun getShowAsActionFlags(data: ActionData): Int = -1

  /**
   * Create custom action view for this action item.
   *
   * @return The custom action view or `null`.
   */
  fun createActionView(data: ActionData): View? = null

  /** Location where an action item will be shown. */
  enum class Location(val id: String) {

    /** Location marker for action items shown in editor activity's toolbar. */
    EDITOR_TOOLBAR("ide.editor.toolbar"),

    /** Location marker for action items shown in UI Designer activity's toolbar. */
    UI_DESIGNER_TOOLBAR("ide.uidesigner.toolbar"),

    /** Location marker for action items shown in editor's text action menu. */
    EDITOR_TEXT_ACTIONS("ide.editor.textActions"),

    /**
     * Location marker for action items shown in 'Code actions' submenu in editor's text action
     * menu.
     */
    EDITOR_CODE_ACTIONS("ide.editor.codeActions"),

    /** Location marker for action items shown when file tabs are reselected. */
    EDITOR_FILE_TABS("ide.editor.fileTabs");

    override fun toString(): String {
      return id
    }

    fun forId(id: String): Location {
      return values().first { it.id == id }
    }
  }
}
