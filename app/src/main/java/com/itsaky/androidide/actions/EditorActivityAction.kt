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
import com.itsaky.androidide.activities.editor.EditorHandlerActivity
import com.itsaky.androidide.tasks.cancelIfActive
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus

/** @author Akash Yadav */
abstract class EditorActivityAction : ActionItem {

  override var enabled: Boolean = true
  override var visible: Boolean = true
  override var icon: Drawable? = null
  override var label: String = ""
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_TOOLBAR

  override var requiresUIThread: Boolean = false

  protected val actionScope = CoroutineScope(Dispatchers.Default) +
      CoroutineName("${javaClass.simpleName}Scope")

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (!data.hasRequiredData(Context::class.java)) {
      markInvisible()
    }
  }

  fun ActionData.getActivity(): EditorHandlerActivity? {
    return this[Context::class.java] as? EditorHandlerActivity
  }

  fun ActionData.requireActivity(): EditorHandlerActivity {
    return getActivity()!!
  }

  override fun destroy() {
    super.destroy()
    actionScope.cancelIfActive("Action is being destroyed")
  }
}
