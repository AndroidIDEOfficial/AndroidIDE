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
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.preferences.internal.hideFileTreeButton

/** @author Akash Yadav */
class FileTreeAction(context: Context) : EditorActivityAction() {

  override val id: String = "editor_fileTree"

  init {
    label = context.getString(R.string.msg_file_tree)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_folder)
  }

  override fun prepare(data: ActionData) {
    visible = true
    enabled = true
  }

  override fun execAction(data: ActionData): Boolean {
    val context = data.getActivity() ?: return false

    context.binding.root.apply {
      if (!isDrawerOpen(GravityCompat.START)) {
        openDrawer(GravityCompat.START)
        return true
      }
    }

    return false
  }

  override fun getShowAsActionFlags(data: ActionData): Int {
    return if (hideFileTreeButton) {
      MenuItem.SHOW_AS_ACTION_NEVER
    } else {
      MenuItem.SHOW_AS_ACTION_IF_ROOM
    }
  }
}
