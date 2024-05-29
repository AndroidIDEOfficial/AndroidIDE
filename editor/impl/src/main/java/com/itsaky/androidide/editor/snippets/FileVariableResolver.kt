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

package com.itsaky.androidide.editor.snippets

import com.itsaky.androidide.editor.ui.IDEEditor
import com.itsaky.androidide.projects.IProjectManager
import io.github.rosemoe.sora.widget.snippet.variable.FileBasedSnippetVariableResolver

/**
 * Resolver for resolving snippet variables related to the file opened in an editor.
 *
 * @author Akash Yadav
 */
class FileVariableResolver(editor: IDEEditor) : FileBasedSnippetVariableResolver(), AbstractSnippetVariableResolver {

  var editor: IDEEditor? = editor
    private set

  companion object {
    private const val TM_FILENAME = "TM_FILENAME"
    private const val TM_FILENAME_BASE = "TM_FILENAME_BASE"
    private const val TM_DIRECTORY = "TM_DIRECTORY"
    private const val TM_FILEPATH = "TM_FILEPATH"
    private const val RELATIVE_FILEPATH = "RELATIVE_FILEPATH"
  }

  override fun resolve(name: String): String {
    val file = editor?.file ?: return ""
    return when (name) {
      TM_FILENAME -> file.name
      TM_FILENAME_BASE -> file.nameWithoutExtension
      TM_DIRECTORY -> file.parentFile?.absolutePath ?: ""
      TM_FILEPATH -> file.absolutePath
      RELATIVE_FILEPATH -> file.relativeTo(IProjectManager.getInstance().projectDir).absolutePath
      else -> ""
    }
  }

  override fun getResolvableNames(): Array<String> {
    return arrayOf(TM_FILENAME, TM_FILENAME_BASE, TM_DIRECTORY, TM_FILEPATH, RELATIVE_FILEPATH)
  }

  override fun close() {
    editor = null
  }
}
