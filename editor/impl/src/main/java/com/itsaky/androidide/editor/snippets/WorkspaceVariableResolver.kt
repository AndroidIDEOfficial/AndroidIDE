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

import com.itsaky.androidide.projects.IProjectManager
import io.github.rosemoe.sora.widget.snippet.variable.WorkspaceBasedSnippetVariableResolver

/**
 * Resolver for resolving snippet variables related to the opened workspace folder (project).
 *
 * @author Akash Yadav
 */
class WorkspaceVariableResolver :
  WorkspaceBasedSnippetVariableResolver(), AbstractSnippetVariableResolver {

  companion object {

    private const val WORKSPACE_NAME = "WORKSPACE_NAME"
    private const val WORKSPACE_FOLDER = "WORKSPACE_FOLDER"
  }

  override fun resolve(name: String): String {
    val directory = IProjectManager.getInstance().projectDir
    return when (name) {
      WORKSPACE_NAME -> directory.name
      WORKSPACE_FOLDER -> directory.absolutePath
      else -> ""
    }
  }

  override fun getResolvableNames(): Array<String> {
    return arrayOf(WORKSPACE_NAME, WORKSPACE_FOLDER)
  }
}
