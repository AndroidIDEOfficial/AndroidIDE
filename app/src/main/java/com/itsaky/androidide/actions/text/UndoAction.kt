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

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorRelatedAction

/** @author Akash Yadav */
class UndoAction(context: Context) : EditorRelatedAction() {

  override val id: String = "editor_undo"

  init {
    label = context.getString(R.string.undo)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_undo)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible) {
      return
    }

    val editor = getEditor(data)!!
    enabled = editor.canUndo()
  }

  override fun execAction(data: ActionData): Any {
    val editor = getEditor(data)
    return if (editor != null) {
      editor.undo()
      getActivity(data)?.invalidateOptionsMenu()
      true
    } else {
      false
    }
  }
}
