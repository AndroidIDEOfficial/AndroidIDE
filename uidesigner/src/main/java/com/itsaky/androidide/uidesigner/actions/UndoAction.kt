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

package com.itsaky.androidide.uidesigner.actions

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.uidesigner.R

/**
 * Undo action for UI Designer.
 *
 * @author Akash Yadav
 */
class UndoAction(context: Context) : UiDesignerAction() {
  
  override val id: String = "ide.uidesigner.undo"
  
  init {
    label = context.getString(R.string.undo)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_undo)
  }
  
  override fun prepare(data: ActionData) {
    super.prepare(data)
    visible = true
    enabled = data.requireWorkspace().undoManager.canUndo()
  }
  
  override suspend fun execAction(data: ActionData): Any {
    data.requireWorkspace().undoManager.undo()
    data.requireActivity().invalidateOptionsMenu()
    return true
  }
}