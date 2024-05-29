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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.R
import com.itsaky.androidide.fragments.sidebar.FileTreeFragment
import kotlin.reflect.KClass

/**
 * Sidebar action for showing file tree.
 *
 * @author Akash Yadav
 */
class FileTreeSidebarAction(context: Context, override val order: Int) : AbstractSidebarAction() {

  companion object {
    const val ID ="ide.editor.sidebar.fileTree"
  }

  override val id: String = ID
  override val fragmentClass: KClass<out Fragment> = FileTreeFragment::class

  init {
    label = context.getString(R.string.msg_file_tree)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_folder)
  }
}