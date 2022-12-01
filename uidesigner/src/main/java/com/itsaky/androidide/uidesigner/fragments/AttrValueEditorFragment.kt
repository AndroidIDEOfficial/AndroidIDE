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

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import com.itsaky.androidide.uidesigner.databinding.LayoutAttrValueEditorBinding
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoHeaderBinding
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/**
 * Fragment to allow the user to edit the value of the selected fragment.
 *
 * @author Akash Yadav
 */
class AttrValueEditorFragment : Fragment() {

  private var header: LayoutViewInfoHeaderBinding? = null
  private val viewModel by viewModels<WorkspaceViewModel>(ownerProducer = { requireActivity() })
  private var binding: LayoutAttrValueEditorBinding? = null
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
    this.binding = LayoutAttrValueEditorBinding.inflate(inflater, container, false)
    return this.binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel._selectedAttr.observe(viewLifecycleOwner) { showAttrInfo() }
  }

  @SuppressLint("SetTextI18n")
  private fun showAttrInfo() {
    val binding = this.binding ?: return
    val header = this.header ?: return
    val attr = this.viewModel.selectedAttr ?: return

    header.name.text = "${attr.namespace.prefix}:${attr.name}"
    header.desc.text = attr.namespace.uri
    binding.attrValue.editText?.setText(attr.value)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }
}
