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
import com.itsaky.androidide.fragments.sidebar.HomeSidebarFragment
import kotlin.reflect.KClass

/**
 * Miscellaneous items shown in the sidebar.
 *
 * @author Akash Yadav
 */
class HomeSidebarAction(context: Context) : AbstractSidebarAction() {

  companion object {

    const val ID = "ide.editor.sidebar.home"
  }

  override val fragmentClass: KClass<out Fragment> = HomeSidebarFragment::class
  override val id: String = ID
  override val order: Int = 0

  init {
    label = context.getString(R.string.title_home)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_home)
  }
}