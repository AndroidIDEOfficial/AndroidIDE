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

package com.itsaky.androidide.uidesigner.undo

import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.uidesigner.models.UiAttribute

/**
 * Represents the action when the user updates an attribute in an [IView].
 *
 * @author Akash Yadav
 */
internal class AttrUpdatedAction(view: com.itsaky.androidide.inflater.IView, attr: UiAttribute, private val oldValue: String) :
  AttrAction(view, attr) {

  override fun undo() {
    // NOTE : We need to provide the view instance so that the new attribute object created will be
    // an instance of UiAttribute
    view.updateAttribute(attr.copyAttr(view = view, value = oldValue))
  }

  override fun redo() {
    view.updateAttribute(attr)
  }
}
