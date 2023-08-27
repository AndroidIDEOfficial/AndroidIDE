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

package com.itsaky.androidide.actions.sidebar

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireContext
import com.itsaky.androidide.activities.TerminalActivity
import com.itsaky.androidide.projects.IProjectManager
import java.util.Objects
import kotlin.reflect.KClass

/**
 * Sidebar action for opening the terminal.
 *
 * @author Akash Yadav
 */
class TerminalSidebarAction(context: Context, override val order: Int) : AbstractSidebarAction() {

  override val id: String = "ide.editor.sidebar.terminal"
  override val fragmentClass: KClass<out Fragment>? = null

  init {
    label = context.getString(R.string.title_terminal)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_terminal)
  }

  override fun execAction(data: ActionData): Any {
    val context = data.requireContext()
    val intent = Intent(context, TerminalActivity::class.java)
    intent.putExtra(
      TerminalActivity.KEY_WORKING_DIRECTORY,
      Objects.requireNonNull(IProjectManager.getInstance().projectDirPath)
    )
    context.startActivity(intent)
    return true
  }
}