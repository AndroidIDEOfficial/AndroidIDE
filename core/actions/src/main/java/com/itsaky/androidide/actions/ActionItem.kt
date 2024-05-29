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

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.view.Menu
import android.view.View
import androidx.annotation.CallSuper
import com.itsaky.androidide.utils.resolveAttr

/**
 * An action that can be registered using the [ActionsRegistry]
 * [com.itsaky.androidide.actions.ActionsRegistry]
 *
 * @author Akash Yadav
 */
interface ActionItem {

  /**
   * A unique ID for this action.
   */
  val id: String

  /**
   * The label for this action.
   */
  var label: String

  /**
   * Whether the action should be visible to the user or not.
   */
  var visible: Boolean

  /**
   * Whether the action should be enabled.
   */
  var enabled: Boolean

  /**
   * Icon for this action.
   */
  var icon: Drawable?

  /**
   * Whether the [execAction] method of this action must be executed on UI thread.
   */
  var requiresUIThread: Boolean

  /**
   * The location of this [ActionItem].
   */
  var location: Location

  /**
   * The order of this action item. This is used only at some locations and not everywhere.
   *
   * @see android.view.MenuItem.getOrder
   */
  val order: Int
    get() = Menu.NONE

  /**
   * The item ID that will be set to the menu item.
   */
  val itemId: Int
    get() = id.hashCode()

  /**
   * Prepare the action. Subclasses can modify the visual properties of this action here.
   *
   * @param data The data containing various information about the event.
   */
  @CallSuper
  fun prepare(data: ActionData) {
    visible = true
    enabled = true
  }

  /**
   * Execute the action. The action executed in a background thread by default.
   *
   * @param data The data containing various information about the event.
   * @return `true` if this action was executed successfully, `false` otherwise.
   */
  suspend fun execAction(data: ActionData): Any

  /**
   * Called just after the [execAction] method executes **successfully** (i.e. returns `true`).
   * Subclasses are free to do UI related work here as this method is called on UI thread.
   *
   * @param data The data containing various information about the event.
   */
  fun postExec(data: ActionData, result: Any) = Unit

  /**
   * Called when the action item is to be destroyed. Any resource references must be released if
   * held.
   */
  fun destroy() = Unit

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

  /**
   * Creates the color filter for this action's icon drawable.
   *
   * The default implementation returns a [PorterDuffColorFilter] instance with color [R.attr.colorOnSurface].
   */
  fun createColorFilter(data: ActionData): ColorFilter? {
    return data.getContext()?.let {
      PorterDuffColorFilter(it.resolveAttr(R.attr.colorOnSurface), PorterDuff.Mode.SRC_ATOP)
    }
  }

  /** Location where an action item will be shown. */
  enum class Location(val id: String) {

    /** Location marker for action items shown in editor activity's toolbar. */
    EDITOR_TOOLBAR("ide.editor.toolbar"),

    /**
     * Location marker for action items shown in editor activity's sidebar (navigation rail in the drawer).
     */
    EDITOR_SIDEBAR("ide.editor.sidebar"),

    /**
     * Location marker for action items shown in the default category of editor activity's sidebar (navigation rail in the drawer).
     */
    EDITOR_SIDEBAR_DEFAULT_ITEMS("ide.editor.sidebar.defaultItems"),

    /** Location marker for action items shown in editor's text action menu. */
    EDITOR_TEXT_ACTIONS("ide.editor.textActions"),

    /**
     * Location marker for action items shown in 'Code actions' submenu in editor's text action
     * menu.
     */
    EDITOR_CODE_ACTIONS("ide.editor.codeActions"),

    /** Location marker for action items shown when file tabs are reselected. */
    EDITOR_FILE_TABS("ide.editor.fileTabs"),

    /**
     * Location marker for action items that are shown when the files in the editor activity's file
     * tree are long clicked.
     */
    EDITOR_FILE_TREE("ide.editor.fileTree"),

    /** Location marker for action items shown in UI Designer activity's toolbar. */
    UI_DESIGNER_TOOLBAR("ide.uidesigner.toolbar");

    override fun toString(): String {
      return id
    }

    fun forId(id: String): Location {
      return entries.first { it.id == id }
    }
  }
}
