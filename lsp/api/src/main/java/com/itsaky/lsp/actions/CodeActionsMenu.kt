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

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
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

    private fun tryGetIcon(context: Context): Drawable? {
        return try {
            val klass = Class.forName("com.itsaky.androidide.R\$drawable")
            val field = klass.getDeclaredField("ic_code")
            ContextCompat.getDrawable(context, field.get(null) as Int)
        } catch (error: Throwable) {
            null
        }
    }

    override var requiresUIThread: Boolean = false
    override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS

    override fun prepare(data: ActionData) {
        if (icon == null) {
            icon = tryGetIcon(data[Context::class.java]!!)
        }
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
