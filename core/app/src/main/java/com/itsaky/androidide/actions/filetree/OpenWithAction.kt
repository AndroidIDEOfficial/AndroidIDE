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

package com.itsaky.androidide.actions.filetree

import android.content.Context
import android.content.Intent
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.requireFile
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.IntentUtils

/**
 * File tree action to open files with external applications.
 *
 * @author Akash Yadav
 */
class OpenWithAction(context: Context, override val order: Int) :
  BaseFileTreeAction(
    context = context,
    labelRes = R.string.open_with,
    iconRes = R.drawable.ic_open_with
  ) {

  override val id: String = "ide.editor.fileTree.openWith"

  override suspend fun execAction(data: ActionData) {
    IntentUtils.startIntent(data.requireActivity(), data.requireFile(), "*/*", Intent.ACTION_VIEW)
  }
}
