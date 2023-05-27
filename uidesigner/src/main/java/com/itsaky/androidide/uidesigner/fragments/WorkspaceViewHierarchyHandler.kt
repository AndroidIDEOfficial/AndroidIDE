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

import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.viewGroup
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.models.PlaceholderView
import com.itsaky.androidide.uidesigner.undo.ViewAddedAction
import com.itsaky.androidide.uidesigner.undo.ViewMovedAction
import com.itsaky.androidide.uidesigner.undo.ViewRemovedAction
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/**
 * Handles hierarchy changes in [DesignerWorkspaceFragment].
 *
 * @author Akash Yadav
 */
internal class WorkspaceViewHierarchyHandler :
  IViewGroup.SingleOnHierarchyChangeListener() {

  private var fragment: DesignerWorkspaceFragment? = null

  internal fun init(fragment: DesignerWorkspaceFragment) {
    this.fragment = fragment
  }

  internal fun release() {
    this.fragment = null
  }

  private fun animateLayoutChange() {
    val frag = this.fragment ?: return
    TransitionManager.beginDelayedTransition(frag.workspaceView.view,
      ChangeBounds().setDuration(
        DesignerWorkspaceFragment.HIERARCHY_CHANGE_TRANSITION_DURATION))
  }

  private fun pushAction(view: com.itsaky.androidide.inflater.IView,
                         parent: IViewGroup, index: Int, added: Boolean
  ) {
    val frag = this.fragment ?: return
    if (view is PlaceholderView) {
      return
    }

    val lastAction = frag.undoManager.peekUndo()

    val action =
      if (added && lastAction is ViewRemovedAction && lastAction.child == view) {
        frag.undoManager.popUndo()
        ViewMovedAction(view, lastAction.parent, parent, lastAction.index,
          index)
      } else if (added) {
        ViewAddedAction(view, parent, index)
      } else {
        ViewRemovedAction(view, parent, index)
      }

    frag.undoManager.push(action)
    frag.updateHierarchy()
    frag.requireActivity().invalidateOptionsMenu()
  }

  override fun beforeViewAdded(group: IViewGroup,
                               view: com.itsaky.androidide.inflater.IView,
                               index: Int
  ) {
    animateLayoutChange()
  }

  override fun beforeViewRemoved(group: IViewGroup,
                                 view: com.itsaky.androidide.inflater.IView,
                                 index: Int
  ) {
    animateLayoutChange()
  }

  override fun onViewAdded(group: IViewGroup,
                           view: com.itsaky.androidide.inflater.IView,
                           index: Int
  ) {
    val frag = this.fragment ?: return
    if (!frag.isInflating && view !is PlaceholderView) {
      // when the inflation process is in progress, setupView method will be called
      // after OnInflateViewEvent
      frag.setupView(view)
    }

    if (frag.workspaceView.viewGroup.childCount > 0 && frag.viewModel.workspaceScreen == WorkspaceViewModel.SCREEN_ERROR) {
      frag.viewModel.workspaceScreen = WorkspaceViewModel.SCREEN_WORKSPACE
    }

    pushAction(view, group, index, true)
  }

  override fun onViewRemoved(group: IViewGroup,
                             view: com.itsaky.androidide.inflater.IView,
                             index: Int
  ) {
    val frag = this.fragment ?: return
    if (frag.workspaceView.viewGroup.childCount == 0) {
      frag.viewModel.errText = frag.getString(R.string.msg_empty_ui_layout)
    }

    pushAction(view, group, index, false)
  }
}
