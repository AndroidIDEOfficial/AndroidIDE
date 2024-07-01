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
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.resources.R

/** @author Akash Yadav */
class FindInProjectAction() : EditorActivityAction() {

  override var requiresUIThread: Boolean = true
  override var order: Int = 0

  constructor(context: Context, order: Int) : this() {
    this.label = context.getString(R.string.menu_find_project)
    this.icon = ContextCompat.getDrawable(context, R.drawable.ic_search_project)
    this.order = order
  }

  override val id: String = "ide.editor.find.inProject"

  override fun prepare(data: ActionData) {
    super.prepare(data)
    data.getActivity()
      ?: run {
        markInvisible()
        return
      }

    val project = IProjectManager.getInstance().getWorkspace()
    if (project == null || project.getSubProjects().isEmpty()) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  override suspend fun execAction(data: ActionData): Boolean {
    val context = data.getActivity() ?: return false
    val dialog = context.findInProjectDialog

    return run {
      dialog.show()
      true
    }
  }
}
