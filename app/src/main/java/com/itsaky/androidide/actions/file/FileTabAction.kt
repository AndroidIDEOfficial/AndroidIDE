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

import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem.Location
import com.itsaky.androidide.actions.ActionItem.Location.EDITOR_FILE_TABS
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.actions.markInvisible

/**
 * Action related to file tabs. Shown only when there is at least one file opened.
 *
 * @author Akash Yadav
 */
abstract class FileTabAction : EditorActivityAction() {

  override var location: Location = EDITOR_FILE_TABS
  override var requiresUIThread: Boolean = true

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible) {
      return
    }

    val activity =
      getActivity(data)
        ?: run {
          markInvisible()
          return
        }

    visible = activity.viewModel.openedFiles.isNotEmpty()
    enabled = visible
  }

  override fun execAction(data: ActionData): Any {
    val activity = getActivity(data) ?: return false
    return activity.doAction(data)
  }

  abstract fun EditorActivity.doAction(data: ActionData): Boolean
}
