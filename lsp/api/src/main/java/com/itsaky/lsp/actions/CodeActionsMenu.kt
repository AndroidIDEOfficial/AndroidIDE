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

package com.itsaky.lsp.actions

import android.graphics.drawable.Drawable
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionMenu

/** @author Akash Yadav */
open class CodeActionsMenu : ActionMenu {

    companion object {
        const val ID = "editor_text_codeActions"
    }

    override val children: MutableSet<ActionItem> = mutableSetOf()
    override val id: String = ID

    override var label: String = "Code actions"
    override var visible = true
    override var enabled: Boolean = true
    override var icon: Drawable? = null
    override var requiresUIThread: Boolean = false
    override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS

    override fun prepare(data: ActionData) {
        visible = children.size > 0 && atLeastOneChildVisible(data)
        enabled = true
    }

    private fun atLeastOneChildVisible(data: ActionData): Boolean {
        for (child in children) {
            child.prepare(data)
            if (child.visible) {
                return true
            }
        }

        return false
    }
}
