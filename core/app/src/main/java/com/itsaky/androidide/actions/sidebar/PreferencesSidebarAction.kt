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
import com.itsaky.androidide.activities.PreferencesActivity
import kotlin.reflect.KClass

/**
 * A sidebar action to navigate the user to IDE Preferences.
 *
 * @author Akash Yadav
 */
class PreferencesSidebarAction(context: Context, override val order: Int) : AbstractSidebarAction() {

  override val id: String = "ide.editor.sidebar.preferences"

  // TODO : Should we show the preferences in the sidebar itself?
  override val fragmentClass: KClass<out Fragment>? = null

  init {
    label = context.getString(R.string.ide_preferences)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_settings)
  }

  override suspend fun execAction(data: ActionData): Any {
    val context = data.requireContext()
    return context.startActivity(Intent(context, PreferencesActivity::class.java))
  }
}