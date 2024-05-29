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
import com.itsaky.androidide.utils.DialogUtils
import io.github.rosemoe.sora.widget.CodeEditor
import java.io.File
import java.nio.file.Path

fun ActionData.getContext(): Context? {
  return get(Context::class.java)
}

fun ActionData.requireContext(): Context {
  return getContext() ?: throw IllegalArgumentException("No context instance provided")
}

fun ActionData.requireFile(): File {
  return get(File::class.java) ?: throw IllegalArgumentException("No file instance provided")
}

fun ActionData.requirePath(): Path {
  return requireFile().toPath()
}

fun ActionData.requireEditor(): CodeEditor {
  return get(CodeEditor::class.java)
    ?: throw IllegalArgumentException("An editor instance is required but none was provided")
}

fun newDialogBuilder(data: ActionData): MaterialAlertDialogBuilder {
  val context = data.get(Context::class.java)!!
  return DialogUtils.newMaterialDialogBuilder(context)
}

/**
 * Checks if the given [ActionData] has instances of the given [types].
 *
 * @param types The type of objects to look for.
 * @return `true` if this [ActionData] has the given [types], `false` otherwise.
 */
fun ActionData.hasRequiredData(vararg types: Class<*>): Boolean {
  for (type in types) {
    get(type) ?: return false
  }

  return true
}

/** Marks this action item as invisible. */
fun ActionItem.markInvisible() {
  visible = false
  enabled = false
}
