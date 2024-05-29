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

package com.itsaky.androidide.actions.file

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.activities.editor.EditorHandlerActivity

/**
 * Closes all opened files.
 *
 * @author Akash Yadav
 */
class CloseAllFilesAction(context: Context, override val order: Int) : FileTabAction() {

  override val id: String = "ide.editor.fileTab.close.all"

  init {
    label = context.getString(R.string.action_closeAll)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_close_all)
  }

  override fun EditorHandlerActivity.doAction(data: ActionData): Boolean {
    closeAll {
      invalidateOptionsMenu()
    }
    return true
  }
}
