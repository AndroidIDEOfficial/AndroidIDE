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

package com.itsaky.androidide.actions.build

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.BaseBuildAction
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.utils.flashSuccess

/**
 * Triggers a project sync request.
 *
 * @author Akash Yadav
 */
class ProjectSyncAction(context: Context, override val order: Int) : BaseBuildAction() {

  override val id: String = "action_editor_syncProject"
  override var requiresUIThread = true

  init {
    label = context.getString(string.title_sync_project)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_sync)
  }

  override fun execAction(data: ActionData): Any {
    return data.getActivity()!!.apply {
      saveAllResult()
      flashSuccess(string.all_saved)
      
      initializeProject()
    }
  }
}
