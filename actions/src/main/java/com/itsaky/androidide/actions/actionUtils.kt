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

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.rosemoe.sora.widget.CodeEditor
import java.io.File
import java.nio.file.Path

fun ActionItem.requireContext(data: ActionData): Context {
  return data.get(Context::class.java)
    ?: throw IllegalArgumentException("No context instance provided")
}

fun ActionItem.requireFile(data: ActionData): File {
  return data.get(File::class.java) ?: throw IllegalArgumentException("No file instance provided")
}

fun ActionItem.requirePath(data: ActionData): Path {
  return requireFile(data).toPath()
}

fun ActionItem.requireEditor(data: ActionData): CodeEditor {
  return data.get(CodeEditor::class.java)
    ?: throw IllegalArgumentException("An editor instance is required but none was provided")
}

fun ActionItem.newDialogBuilder(data: ActionData): MaterialAlertDialogBuilder {
  val klass = Class.forName("com.itsaky.androidide.utils.DialogUtils")
  val method = klass.getDeclaredMethod("newMaterialDialogBuilder", Context::class.java)
  return method.invoke(null, data.get(Context::class.java)!!) as MaterialAlertDialogBuilder
}

/**
 * Checks if the given [ActionData] has instances of the given [types].
 *
 * @param data The data to check.
 * @param types The type of objects to look for.
 * @return `true` if the [data] has the given [types], `false` otherwise.
 */
fun ActionItem.hasRequiredData(data: ActionData, vararg types: Class<*>): Boolean {
  for (type in types) {
    data.get(type) ?: return false
  }

  return true
}

/** Marks this action item as invisible. */
fun ActionItem.markInvisible() {
  visible = false
  enabled = false
}
