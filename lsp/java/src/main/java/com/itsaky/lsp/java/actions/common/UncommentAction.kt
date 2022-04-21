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
package com.itsaky.lsp.java.actions.common

import com.itsaky.androidide.actions.ActionData
import com.itsaky.lsp.java.R
import com.itsaky.lsp.java.actions.BaseCodeAction

/** @author Akash Yadav */
class UncommentAction : BaseCodeAction() {
    override val id: String = "lsp_java_uncommentLine"
    override var label: String = ""

    override val titleTextRes: Int = R.string.action_uncomment_line

    override fun execAction(data: ActionData): Boolean {
        val editor = requireEditor(data)
        val text = editor.text
        val cursor = editor.cursor
        var line = cursor.leftLine

        text.beginBatchEdit()
        while (line >= cursor.leftLine && line <= cursor.rightLine) {
            val l = text.getLineString(line)
            if (l.trim { it <= ' ' }.startsWith("//")) {
                val i = l.indexOf("//")
                text.delete(line, i, line, i + 2)
            }
            line++
        }
        text.endBatchEdit()

        return true
    }
}
