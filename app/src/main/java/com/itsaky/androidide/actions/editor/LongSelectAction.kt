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

package com.itsaky.androidide.actions.editor

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.EditorRelatedAction
import com.itsaky.androidide.actions.markInvisible

/**
 * An action to long select text in the editor.
 *
 * @author Akash Yadav
 */
class LongSelectAction(context: Context, override val order: Int) : EditorRelatedAction() {

  override val id: String = "ide.editor.code.text.longSelect"
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS

  init {
    label = context.getString(R.string.title_begin_long_select)
    icon = ContextCompat.getDrawable(context, R.drawable.editor_text_select_start)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    data.getEditor() ?: markInvisible()
  }

  override suspend fun execAction(data: ActionData): Any {
    return data.getEditor()?.beginLongSelect() ?: false
  }
}