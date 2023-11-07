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

package com.itsaky.androidide.uidesigner.actions

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionItem.Location
import com.itsaky.androidide.actions.ActionItem.Location.UI_DESIGNER_TOOLBAR
import com.itsaky.androidide.actions.hasRequiredData
import com.itsaky.androidide.actions.markInvisible
import com.itsaky.androidide.uidesigner.UIDesignerActivity
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment

/**
 * Base class for actions that are shown in the UI Designer activity.
 *
 * @author Akash Yadav
 */
abstract class UiDesignerAction : ActionItem {

  override var requiresUIThread: Boolean = true
  override var location: Location = UI_DESIGNER_TOOLBAR
  override var label: String = ""
  override var icon: Drawable? = null
  override var enabled: Boolean = true
  override var visible: Boolean = true

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (!data.hasRequiredData(Context::class.java, Fragment::class.java)) {
      markInvisible()
      return
    }
  }

  fun ActionData.requireActivity(): UIDesignerActivity {
    return get(Context::class.java) as? UIDesignerActivity
      ?: throw IllegalArgumentException("UIDesignerActivity instance is required in ActionData")
  }

  fun ActionData.requireWorkspace(): DesignerWorkspaceFragment {
    return get(Fragment::class.java) as? DesignerWorkspaceFragment
      ?: throw IllegalArgumentException(
        "DesignerWorkspaceFragment instance is required in ActionData"
      )
  }
}
