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
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.itsaky.androidide.editor.ui.IDEEditor

/** @author Akash Yadav */
abstract class BaseEditorAction : EditorActionItem {

  override var label: String = ""
  override var visible: Boolean = true
  override var enabled: Boolean = true
  override var icon: Drawable? = null
  override var requiresUIThread: Boolean = true // all editor actions must be executed on UI thread
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS

  override fun prepare(data: ActionData) {
    super.prepare(data)
    getEditor(data)
      ?: kotlin.run {
        visible = false
        enabled = false
        return
      }

    visible = true
    enabled = true
  }

  fun getEditor(data: ActionData): IDEEditor? {
    return data.get(IDEEditor::class.java)
  }

  fun getContext(data: ActionData): Context? {
    val editor = getEditor(data) ?: return null
    return editor.context
  }

  fun tintDrawable(context: Context, drawable: Drawable): Drawable {
    drawable.setTint(
      ContextCompat.getColor(context, com.itsaky.androidide.resources.R.color.primaryIconColor)
    )
    return drawable
  }
}
