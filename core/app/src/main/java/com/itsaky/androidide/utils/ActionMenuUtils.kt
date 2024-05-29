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

package com.itsaky.androidide.utils

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.core.widget.PopupMenuCompat.getDragToOpenListener
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.FillMenuParams

/**
 * Utility class to show a popup menu with [com.itsaky.androidide.actions.ActionsRegistry].
 *
 * @author Akash Yadav
 */
object ActionMenuUtils {

  @JvmStatic
  @JvmOverloads
  fun createMenu(
    context: Context,
    anchor: View,
    location: ActionItem.Location,
    dragToOpen: Boolean = false
  ): PopupMenu {
    return PopupMenu(context, anchor).apply {
      val data = ActionData()
      data.put(Context::class.java, context)
      ActionsRegistry.getInstance().fillMenu(FillMenuParams(data, location, menu))

      if (dragToOpen) {
        anchor.setOnTouchListener(getDragToOpenListener(this))
      }
    }
  }
}
