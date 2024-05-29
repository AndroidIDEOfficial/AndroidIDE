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

package com.itsaky.androidide.uidesigner.fragments

import com.itsaky.androidide.uidesigner.models.UiAttribute
import com.itsaky.androidide.uidesigner.undo.AttrAddedAction
import com.itsaky.androidide.uidesigner.undo.AttrRemovedAction
import com.itsaky.androidide.uidesigner.undo.AttrUpdatedAction

/**
 * Handles view attribute changes in [DesignerWorkspaceFragment].
 *
 * @author Akash Yadav
 */
internal class WorkspaceViewAttrHandler : com.itsaky.androidide.inflater.IView.AttributeChangeListener {

  private var fragment: DesignerWorkspaceFragment? = null

  internal fun init(fragment: DesignerWorkspaceFragment) {
    this.fragment = fragment
  }

  internal fun release() {
    this.fragment = null
  }

  override fun onAttributeAdded(view: com.itsaky.androidide.inflater.IView, attribute: com.itsaky.androidide.inflater.IAttribute) {
    val frag = this.fragment ?: return
    frag.undoManager.push(AttrAddedAction(view = view, attr = attribute as UiAttribute))
  }

  override fun onAttributeRemoved(view: com.itsaky.androidide.inflater.IView, attribute: com.itsaky.androidide.inflater.IAttribute) {
    val frag = this.fragment ?: return
    frag.undoManager.push(AttrRemovedAction(view = view, attr = attribute as UiAttribute))
  }

  override fun onAttributeUpdated(view: com.itsaky.androidide.inflater.IView, attribute: com.itsaky.androidide.inflater.IAttribute, oldValue: String) {
    val frag = this.fragment ?: return
    frag.undoManager.push(
      AttrUpdatedAction(
        view = view,
        attr = attribute as UiAttribute,
        oldValue = oldValue
      )
    )
  }
}
