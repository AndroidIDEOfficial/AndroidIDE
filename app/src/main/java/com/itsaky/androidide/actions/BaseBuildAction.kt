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

package com.itsaky.androidide.actions

import com.itsaky.androidide.EditorActivity

/**
 * Marker class for actions that execute build related tasks.
 *
 * @author Akash Yadav
 */
abstract class BaseBuildAction : EditorActivityAction() {

    override fun prepare(data: ActionData) {
        val context = getActivity(data)
        if (context == null) {
            visible = false
            return
        } else {
            visible = true
        }

        if (isBuildInProgress(context)) {
            enabled = false
            return
        } else {
            enabled = true
        }
    }

    override fun postExec(data: ActionData, result: Any) {
        val context = getActivity(data) ?: return
        context.invalidateOptionsMenu()
    }

    fun shouldPrepare() = visible && enabled

    private fun isBuildInProgress(activity: EditorActivity): Boolean =
        activity.buildService == null || activity.buildService.isBuildInProgress
}
