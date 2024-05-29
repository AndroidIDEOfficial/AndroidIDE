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
import com.itsaky.androidide.inflater.utils.newAttribute
import com.itsaky.androidide.inflater.viewAdapter
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.R.string
import com.itsaky.androidide.uidesigner.adapters.AddAttrListAdapter
import com.itsaky.androidide.uidesigner.databinding.LayoutAddAttrBinding
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoHeaderBinding
import com.itsaky.androidide.uidesigner.models.UiAttribute
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/**
 * A fragment that shows a list of attributes that can be added to the selected attribute in the UI
 * designer.
 *
 * @author Akash Yadav
 */
class AddAttrFragment : Fragment() {

  private val viewModel by viewModels<WorkspaceViewModel>(ownerProducer = { requireActivity() })
  private var header: LayoutViewInfoHeaderBinding? = null
  private var binding: LayoutAddAttrBinding? = null
    set(value) {
      if (value == null) {
        field = null
        header = null
        return
      }

      field = value
      header = LayoutViewInfoHeaderBinding.bind(value.root)
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return LayoutAddAttrBinding.inflate(inflater, container, false).also { this.binding = it }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    header?.apply {
      name.setText(string.msg_viewaction_add_attr)
      desc.setText(string.msg_select_attr)
    }

    showAttrs()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }

  private fun showAttrs() {
    val binding = this.binding ?: return
    val view = viewModel.view ?: return
    val adapter = view.viewAdapter ?: return
    val attributes =
      adapter.supportedAttributes.filterNot { view.hasAttribute(it.name, it.namespace?.uri) }
    binding.attrList.adapter =
      AddAttrListAdapter(attributes) {
        val attribute =
          newAttribute(view = viewModel.view, namespace = it.namespace, name = it.name, value = "")
        viewModel.selectedAttr = attribute as UiAttribute
        viewModel.addAttrMode = true
        findNavController().navigate(R.id.attrValueEditorFragment)
      }
  }
}
