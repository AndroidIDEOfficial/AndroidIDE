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

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.view.Menu
import android.view.MenuItem
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionMenu
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.R
import com.itsaky.androidide.actions.locations.CodeActionsMenu
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.resolveAttr
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

/**
 * Default implementation for the [ActionsRegistry]
 *
 * @author Akash Yadav
 */
class DefaultActionsRegistry : ActionsRegistry() {

  private val log = ILogger.newInstance("DefaultActionsRegistry")
  private val actions = ConcurrentHashMap<String, LinkedHashMap<String, ActionItem>>()
  private val listeners = mutableSetOf<ActionExecListener>()

  init {
    registerAction(CodeActionsMenu)
  }

  override fun getActions(location: ActionItem.Location): MutableMap<String, ActionItem> {
    if (actions[location.id] == null) {
      actions[location.id] = java.util.LinkedHashMap()
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
      return true
    }

    return false
  }

  override fun unregisterAction(id: String): Boolean {
    for (locations in this.actions.values) {
      val older = locations.remove(id)
      if (older != null) {
        return true
      }
    }

    return false
  }

  override fun findAction(location: ActionItem.Location, id: String): ActionItem? =
    getActions(location)[id]

  override fun clearActions(location: ActionItem.Location) = getActions(location).clear()

  override fun registerActionExecListener(listener: ActionExecListener) {
    listeners.add(listener)
  }

  override fun unregisterActionExecListener(listener: ActionExecListener) {
    listeners.remove(listener)
  }

  override fun fillMenu(data: ActionData, location: ActionItem.Location, menu: Menu) {
    val actions = getActions(location)

    for (action in actions.values) {
      action.prepare(data)

      if (!action.visible) {
        continue
      }
      addActionToMenu(menu, action, data)
    }
  }

  private fun addActionToMenu(menu: Menu, action: ActionItem, data: ActionData) {
    val context = data[Context::class.java]
    val item: MenuItem =
      if (action is ActionMenu) {
        val sub = menu.addSubMenu(action.label)

        var shouldBeEnabled = false
        for (subItem in action.children) {
          subItem.prepare(data)
          if (subItem.visible) {
            addActionToMenu(sub, subItem, data)
          }

          if (action.enabled && subItem.enabled && !shouldBeEnabled) {
            shouldBeEnabled = true
          }
        }

        action.enabled = shouldBeEnabled
        sub.item
      } else {
        menu.add(action.label)
      }

    item.isEnabled = action.enabled
    item.icon =
      action.icon?.apply {
        colorFilter = PorterDuffColorFilter(context!!.resolveAttr(R.attr.colorOnSurface), SRC_ATOP)
      }

    if (item.icon != null) {
      item.icon!!.alpha = if (action.enabled) 255 else 76
      item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
    } else {
      item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)
    }

    if (action.getShowAsActionFlags(data) != -1) {
      item.setShowAsAction(action.getShowAsActionFlags(data))
    }

    action.createActionView(data)?.let { item.actionView = it }

    if (action !is ActionMenu) {
      item.setOnMenuItemClickListener {
        if (action.requiresUIThread) {
          ThreadUtils.runOnUiThread {
            val result = action.execAction(data)
            action.postExec(data, result)
            notifyActionExec(action, result)
          }
        } else {
          execInBackground(action, data, it)
        }

        true
      }
    }
  }

  private fun execInBackground(action: ActionItem, data: ActionData, it: MenuItem) {
    val start = System.currentTimeMillis()
    CompletableFuture.supplyAsync { action.execAction(data) }
      .whenComplete { result, error ->
        if (result == null || (result is Boolean && !result) || error != null) {
          log.error(
            "An error occurred when performing action '${it.title}'. Action failed in ${System.currentTimeMillis() - start}ms",
            error
          )
        } else {
          log.info("Action '${it.title}' completed in ${System.currentTimeMillis() - start}ms")
        }

        ThreadUtils.runOnUiThread {
          action.postExec(data, result ?: false)
          notifyActionExec(action, result ?: false)
        }
      }
  }

  private fun notifyActionExec(action: ActionItem, result: Any) {
    for (listener in listeners) {
      listener.onExec(action, result)
    }
  }
}
