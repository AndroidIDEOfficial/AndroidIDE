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

package com.itsaky.androidide.actions.etc

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionMenu
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.resources.R

/** @author Akash Yadav */
class FindActionMenu(context: Context, override val order: Int) : EditorActivityAction(),
  ActionMenu {

  override val children: MutableSet<ActionItem> = mutableSetOf()
  override val id: String = "ide.editor.find"

  init {
    label = context.getString(R.string.menu_find)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_search)

    addAction(FindInFileAction(context, 0))
    addAction(FindInProjectAction(context, 1))
  }

  override fun prepare(data: ActionData) {
    super<EditorActivityAction>.prepare(data)
    super<ActionMenu>.prepare(data)
  }
}
