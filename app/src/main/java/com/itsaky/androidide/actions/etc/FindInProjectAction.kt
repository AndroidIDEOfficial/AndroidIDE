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
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.projects.ProjectManager

/** @author Akash Yadav */
class FindInProjectAction() : EditorActivityAction() {

  override var requiresUIThread: Boolean = true

  constructor(context: Context) : this() {
    label = context.getString(R.string.menu_find_project)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_search_project)
  }

  override val id: String = "editor_findInProject"

  override fun prepare(data: ActionData) {
    data.getActivity()
      ?: run {
        markInvisible()
        return
      }

    val project = ProjectManager.rootProject
    if (project == null || project.subProjects.isEmpty()) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  override fun execAction(data: ActionData): Boolean {
    val context = data.getActivity() ?: return false
    val dialog = context.findInProjectDialog

    return run {
      dialog.show()
      true
    }
  }
}
