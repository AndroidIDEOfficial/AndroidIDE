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
package com.itsaky.androidide.actions.internal

import android.view.Menu
import android.view.MenuItem
import com.google.auto.service.AutoService
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionMenu
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.FillMenuParams
import com.itsaky.androidide.actions.OnActionClickListener
import com.itsaky.androidide.actions.locations.CodeActionsMenu
import com.itsaky.androidide.utils.withStopWatch
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for the [ActionsRegistry]
 *
 * @author Akash Yadav
 */
@AutoService(ActionsRegistry::class)
class DefaultActionsRegistry : ActionsRegistry() {

  private val actions = ConcurrentHashMap<String, ConcurrentHashMap<String, ActionItem>>()
  private val listeners = HashSet<ActionExecListener>()

  private val actionsCoroutineScope = CoroutineScope(Dispatchers.Default) +
      CoroutineName("DefaultActionsRegistry")

  companion object {
    private val log = LoggerFactory.getLogger(DefaultActionsRegistry::class.java)
  }

  init {
    registerAction(CodeActionsMenu)
  }

  override fun getActions(location: ActionItem.Location): MutableMap<String, ActionItem> {
    if (actions[location.id] == null) {
      actions[location.id] = ConcurrentHashMap()
    }

    return actions[location.id]!!
  }

  override fun registerAction(action: ActionItem): Boolean {
    val actions = getActions(action.location)
    actions[action.id] = action
    return true
  }

  override fun unregisterAction(action: ActionItem): Boolean {
    val actions = getActions(action.location)
    val older = actions.remove(action.id)
    if (older != null) {
      older.destroy()
      return true
    }

    return false
  }

  override fun unregisterAction(id: String): Boolean {
    for (locations in this.actions.values) {
      val older = locations.remove(id)
      if (older != null) {
        older.destroy()
        return true
      }
    }

    return false
  }

  override fun findAction(location: ActionItem.Location, id: String): ActionItem? =
    getActions(location)[id]

  override fun findAction(location: ActionItem.Location, itemId: Int): ActionItem? {
    for (action in getActions(location)) {
      if (action.value.itemId == itemId) {
        return action.value
      }
    }

    return null
  }

  override fun clearActions(location: ActionItem.Location) {
    val actions = getActions(location)
    actions.forEach { it.value.destroy() }
    actions.clear()
  }

  override fun registerActionExecListener(listener: ActionExecListener) {
    listeners.add(listener)
  }

  override fun unregisterActionExecListener(listener: ActionExecListener) {
    listeners.remove(listener)
  }

  override fun fillMenu(params: FillMenuParams) {
    val (data, location, menu, onClickListener) = params
    val actions = getActions(location)

    for (action in actions.values) {
      action.prepare(data)

      if (!action.visible) {
        continue
      }
      addActionToMenu(menu, action, data, onClickListener)
    }
  }

  private fun addActionToMenu(
    menu: Menu,
    action: ActionItem,
    data: ActionData,
    onClickListener: OnActionClickListener
  ) {

    val item: MenuItem = if (action is ActionMenu) {
      val sub = menu.addSubMenu(Menu.NONE, action.itemId, action.order, action.label)

      var shouldBeEnabled = false
      for (subItem in action.children) {
        subItem.prepare(data)
        if (subItem.visible) {
          addActionToMenu(sub, subItem, data, onClickListener)
        }

        if (action.enabled && subItem.enabled && !shouldBeEnabled) {
          shouldBeEnabled = true
        }
      }

      action.enabled = shouldBeEnabled
      sub.item
    } else {
      menu.add(Menu.NONE, action.itemId, action.order, action.label)
    }

    item.isEnabled = action.enabled

    item.icon = action.icon?.apply {
      colorFilter = action.createColorFilter(data)
      alpha = if (action.enabled) 255 else 76
    }

    var showAsAction = action.getShowAsActionFlags(data)
    if (showAsAction == -1) {
      showAsAction = if (action.icon != null) {
        MenuItem.SHOW_AS_ACTION_IF_ROOM
      } else {
        MenuItem.SHOW_AS_ACTION_NEVER
      }
    }

    if (!action.enabled) {
      showAsAction = MenuItem.SHOW_AS_ACTION_NEVER
    }

    item.setShowAsAction(showAsAction)

    action.createActionView(data)?.let { item.actionView = it }

    if (action !is ActionMenu) {
      item.setOnMenuItemClickListener {
        onClickListener.onClick(this, action, it, data)
      }
    }
  }

  /** Executes the given action item with the given */
  fun executeAction(action: ActionItem, data: ActionData): Job {
    val onMainThread = action.requiresUIThread
    val context = if (onMainThread) Dispatchers.Main.immediate else Dispatchers.Default
    return actionsCoroutineScope.launch(context) {
      val result = withStopWatch("Action '${action.id}'") {
        action.execAction(data)
      }

      val post = fun() = run {
        action.postExec(data, result)
        notifyActionExec(action, result)
      }

      if (onMainThread) {
        post()
      } else {
        withContext(Dispatchers.Main.immediate) {
          post()
        }
      }
    }.also { job ->
      job.invokeOnCompletion { error ->
        if (error != null) {
          log.error("An error occurred when performing action '{}'", action.id, error)
        }
      }
    }
  }

  private fun notifyActionExec(action: ActionItem, result: Any) {
    for (listener in listeners) {
      listener.onExec(action, result)
    }
  }
}
