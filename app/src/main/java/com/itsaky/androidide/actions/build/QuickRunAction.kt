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
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.BaseBuildAction

/**
 * The 'Quick Run' action in the editor activity.
 *
 * @author Akash Yadav
 */
class QuickRunAction() : BaseBuildAction() {

    constructor(context: Context) : this() {
        label = context.getString(R.string.quick_run_debug)
        icon = ContextCompat.getDrawable(context, R.drawable.ic_run_outline)
    }

    override val id: String = "editor_quick_run"

    override fun execAction(data: ActionData): Boolean {
        val context = getActivity(data) ?: return false
        context.assembleDebug(true)
        return true
    }
}
