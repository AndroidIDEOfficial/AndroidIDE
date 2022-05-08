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

package com.itsaky.lsp.java.actions

import android.content.Context
import android.graphics.drawable.Drawable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.lsp.java.FileStore
import com.itsaky.lsp.java.JavaLanguageServer
import io.github.rosemoe.sora.widget.CodeEditor
import java.io.File
import java.nio.file.Path

/**
 * Base class for java code actions
 * @author Akash Yadav
 */
abstract class BaseCodeAction : ActionItem {
    override var visible: Boolean = true
    override var enabled: Boolean = true
    override var icon: Drawable? = null
    override var requiresUIThread: Boolean = false
    override var location: ActionItem.Location = ActionItem.Location.EDITOR_CODE_ACTIONS

    protected abstract val titleTextRes: Int

    override fun prepare(data: ActionData) {

        if (!hasRequiredData(
            data, Context::class.java, JavaLanguageServer::class.java, File::class.java)) {
            markInvisible()
            return
        }

        if (titleTextRes != -1) {
            label = data[Context::class.java]!!.getString(titleTextRes)
        }

        val file = requireFile(data)
        visible = FileStore.isJavaFile(file.toPath())
        enabled = visible
    }

    fun requireFile(data: ActionData): File {
        return data.get(File::class.java)
            ?: throw IllegalArgumentException("No file instance provided")
    }

    fun requirePath(data: ActionData): Path {
        return requireFile(data).toPath()
    }

    fun requireEditor(data: ActionData): CodeEditor {
        return data.get(CodeEditor::class.java)
            ?: throw IllegalArgumentException(
                "An editor instance is required but none was provided")
    }

    fun newDialogBuilder(data: ActionData): MaterialAlertDialogBuilder {
        val klass = Class.forName("com.itsaky.androidide.utils.DialogUtils")
        val method = klass.getDeclaredMethod("newMaterialDialogBuilder", Context::class.java)
        return method.invoke(null, data.get(Context::class.java)!!) as MaterialAlertDialogBuilder
    }
}
