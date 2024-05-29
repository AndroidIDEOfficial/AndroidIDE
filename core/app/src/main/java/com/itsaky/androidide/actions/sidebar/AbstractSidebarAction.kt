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

package com.itsaky.androidide.actions.sidebar

import android.graphics.drawable.Drawable
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.SidebarActionItem

/**
 * @author Akash Yadav
 */
abstract class AbstractSidebarAction : SidebarActionItem {

  // sidebar actions should always be executed on UI thread
  override var requiresUIThread = true
  override var visible = true
  override var enabled = true

  // should never change
  final override var location = ActionItem.Location.EDITOR_SIDEBAR

  // Subclasses should accept a Context in their constructor and initialize these values
  // when the object instance is initialized
  override var icon: Drawable? = null
  override var label: String = ""

  override suspend fun execAction(data: ActionData): Any {
    return false
  }
}