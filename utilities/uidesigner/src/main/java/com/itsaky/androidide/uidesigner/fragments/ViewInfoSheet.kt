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

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentDialog
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoSheetBinding
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/** @author Akash Yadav */
class ViewInfoSheet : BottomSheetDialogFragment() {

  private var binding: LayoutViewInfoSheetBinding? = null
  private val viewModel by viewModels<WorkspaceViewModel>(ownerProducer = { requireActivity() })

  companion object {

    const val TAG = "ide.uidesigner.viewinfo"
  }

  private val viewInfoBackPressedCallback =
    object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        val containerView = this@ViewInfoSheet.binding?.navHost ?: return
        val navController = containerView.findNavController()
        navController.navigateUp()
      }
    }

  override fun onDismiss(dialog: DialogInterface) {
    viewModel.undoManager.enable()
    viewModel.notifyAttrUpdated()
    super.onDismiss(dialog)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return LayoutViewInfoSheetBinding.inflate(inflater, container, false)
      .also { this.binding = it }
      .root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    findNavControllerFromFragment().addOnDestinationChangedListener { _, destination, _ ->
      viewInfoBackPressedCallback.isEnabled = destination.id != R.id.viewInfoFragment

      // not disabling the undo manager when going into 'edit mode' will result in a lot
      // AttrUpdatedAction being pushed to UndoManager's undo stack.
      viewModel.undoManager.enabled = destination.id != R.id.attrValueEditorFragment

      if (viewModel.currentDestination == R.id.attrValueEditorFragment) {
        // if we are returning from value editor, then
        // 1 - if we were updating the value of an existing attribute, then the updated value must
        //     be updated in the view
        // 2 - if we were adding a new attribute, then that attribute should be added to the view.
        viewModel.notifyAttrUpdated()
        viewModel.selectedAttr = null
        viewModel.addAttrMode = false
      }

      viewModel.currentDestination = destination.id
    }

    (requireDialog() as ComponentDialog)
      .onBackPressedDispatcher
      .addCallback(viewLifecycleOwner, viewInfoBackPressedCallback)
  }

  private fun findNavControllerFromFragment(): NavController {
    val framgent = childFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
    return framgent.navController
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }
}
