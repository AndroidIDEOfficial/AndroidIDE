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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentDialog
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.transition.TransitionManager.beginDelayedTransition
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.transition.MaterialSharedAxis
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoSheetBinding
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel.Companion.SCREEN_VALUE_EDITOR
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel.Companion.SCREEN_VIEW_INFO

/** @author Akash Yadav */
class ViewInfoSheet : BottomSheetDialogFragment() {
  private var binding: LayoutViewInfoSheetBinding? = null
  private val viewModel by viewModels<WorkspaceViewModel>(ownerProducer = { requireActivity() })

  val isShowing: Boolean
    get() = dialog?.isShowing ?: false

  private val backPressedCallback =
    object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        if (viewModel.viewInfoScreen == SCREEN_VALUE_EDITOR) {
          viewModel.viewInfoScreen = SCREEN_VIEW_INFO
          isEnabled = false
        }
      }
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    this.binding = LayoutViewInfoSheetBinding.inflate(inflater, container, false)
    return this.binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel._viewInfoScreen.observe(viewLifecycleOwner) {
      binding?.apply {
        val prev = flipper.displayedChild
        beginDelayedTransition(root, MaterialSharedAxis(MaterialSharedAxis.X, prev < it))
        flipper.displayedChild = it
      }
      this.backPressedCallback.isEnabled = it == SCREEN_VALUE_EDITOR
    }

    viewModel._view.observe(viewLifecycleOwner) { viewModel.viewInfoScreen = SCREEN_VIEW_INFO }

    (requireDialog() as ComponentDialog)
      .onBackPressedDispatcher
      .addCallback(viewLifecycleOwner, backPressedCallback)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }
}
