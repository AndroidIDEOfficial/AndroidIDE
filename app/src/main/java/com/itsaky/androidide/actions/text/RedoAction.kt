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

package com.itsaky.androidide.actions.text

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorRelatedAction

/** @author Akash Yadav */
class RedoAction() : EditorRelatedAction() {

  constructor(context: Context) : this() {
    label = context.getString(R.string.redo)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_redo)
  }

  override val id: String = "editor_redo"

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible) {
      return
    }

    val editor = getEditor(data)!!
    enabled = editor.canRedo()
  }

  override fun execAction(data: ActionData): Boolean {
    val editor = getEditor(data)!!
    editor.redo()
    getActivity(data)?.invalidateOptionsMenu()
    return true
  }

  override fun getShowAsActionFlags(data: ActionData): Int {
    return if (KeyboardUtils.isSoftInputVisible(data.get(Context::class.java) as Activity)) {
      MenuItem.SHOW_AS_ACTION_IF_ROOM
    } else {
      MenuItem.SHOW_AS_ACTION_NEVER
    }
  }
}
