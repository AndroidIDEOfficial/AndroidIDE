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

package com.itsaky.androidide.actions.editor.find

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData

/**
 * 'Go to next' action in editor's search action mode.
 * @author Akash Yadav
 */
class GoToNextAction() : SearchActionModeAction() {
    override val id: String = "editor.find.next"

    constructor(context: Context) : this() {
        label = context.getString(R.string.next)
        icon = ContextCompat.getDrawable(context, R.drawable.ic_search_next)
    }

    override fun execAction(data: ActionData): Any {
        val editor = getEditor(data)!!
        if (!editor.searcher.hasQuery()) {
            return false
        }
        
        return editor.searcher.gotoNext()
    }
}
