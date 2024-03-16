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

package com.itsaky.androidide.actions.etc

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.EditorActivityAction
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.tasks.launchAsyncWithProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Action which reloads the editor color schemes.
 *
 * @author Akash Yadav
 */
class ReloadColorSchemesAction(context: Context, override val order: Int) : EditorActivityAction() {

  override val id: String = "ide.editor.colorScheme.reload"

  // Schemes are reloaded in a background thread
  // This property is set to true just to make sure that the ProgressDialog instance is created on
  // the UI thread
  override var requiresUIThread: Boolean = true

  init {
    label = context.getString(R.string.title_reload_color_schemes)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_reload)
  }

  override suspend fun execAction(data: ActionData): Boolean {
    val context = data.requireActivity()
    context.lifecycleScope.launchAsyncWithProgress(
      context = Dispatchers.Default,
      configureFlashbar = { builder, _ ->
        builder.message(R.string.please_wait)
      }) { flashbar, _ ->
      IDEColorSchemeProvider.reload()
      withContext(Dispatchers.Main) {
        flashbar.dismiss()
      }
    }
    return true
  }
}
