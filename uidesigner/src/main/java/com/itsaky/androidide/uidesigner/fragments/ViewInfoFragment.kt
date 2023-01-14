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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itsaky.androidide.inflater.internal.NamespaceImpl
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.utils.newAttribute
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.adapters.ViewAttrListAdapter
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoBinding
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoHeaderBinding
import com.itsaky.androidide.uidesigner.models.UiAttribute
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/**
 * A [BottomSheetDialogFragment] which shows information about a clicked view.
 *
 * @author Akash Yadav
 */
class ViewInfoFragment : Fragment() {

  private val viewModel by viewModels<WorkspaceViewModel>(ownerProducer = { requireActivity() })
  private var binding: LayoutViewInfoBinding? = null
    set(value) {
      if (value == null) {
        field = null
        header = null
        return
      }

      field = value
      header = LayoutViewInfoHeaderBinding.bind(value.root)
    }

  private var header: LayoutViewInfoHeaderBinding? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    this.binding = LayoutViewInfoBinding.inflate(inflater, container, false)
    return this.binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel._view.observe(viewLifecycleOwner) { showViewInfo() }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }

  private fun showViewInfo() {
    val binding = this.binding ?: return
    val header = this.header ?: return
    val view = this.viewModel.view as? ViewImpl ?: return

    header.name.text = view.simpleName
    header.desc.text = view.name
    binding.attrList.adapter =
      ViewAttrListAdapter(
        attributes = view.attributes,
        viewModel = viewModel,
        onDeleteAttr = {
          view.removeAttribute(it)
          true
        }
      ) {
        // Store a copy of the attribute so that we can check if the value of the attribute has
        // changed in WorkspaceViewModel.notifyAttrUpdated()
        viewModel.selectedAttr =
          newAttribute(
            view = view,
            namespace = it.namespace as NamespaceImpl?,
            name = it.name,
            value = it.value
          )
            as UiAttribute
        findNavController().navigate(R.id.attrValueEditorFragment)
      }

    binding.btnDelete.setOnClickListener {
      view.removeFromParent()
      (parentFragment?.parentFragment as? ViewInfoSheet)?.dismiss()
    }

    binding.btnAdd.setOnClickListener { findNavController().navigate(R.id.addAttrFragment) }
  }
}
