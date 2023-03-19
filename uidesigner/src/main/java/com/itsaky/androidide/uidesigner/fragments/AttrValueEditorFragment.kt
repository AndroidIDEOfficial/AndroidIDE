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
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.uidesigner.databinding.LayoutAttrValueEditorBinding
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoHeaderBinding
import com.itsaky.androidide.uidesigner.utils.ValueCompletionProvider
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

  private var textWatcher: TextWatcher? = null

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
    showAttrInfo()
  }

  @SuppressLint("SetTextI18n")
  private fun showAttrInfo() {
    val binding = this.binding ?: return
    val header = this.header ?: return
    val view = this.viewModel.view ?: return
    val attr = this.viewModel.selectedAttr ?: return

    val ns = attr.namespace?.prefix?.let { "${it}:" } ?: ""
    header.name.text = "${ns}${attr.name}"
    header.desc.text = attr.namespace?.uri ?: ""
    binding.attrValue.let { textView ->
      textWatcher?.let { watcher -> textView.removeTextChangedListener(watcher) }

      this.textWatcher =
        ValueCompletionProvider(viewModel.file, viewModel.view!! as ViewImpl, attr) { result ->
          textView.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, result)
          )
          textView.dropDownVerticalOffset = -binding.root.height
          textView.showDropDown()

          val value = binding.attrValue.text.toString()
          attr.value = value
          try {
            view.applyAttribute(attr.copyAttr(value = value))
          } catch (e: Exception) {
            // When the user is editing the attribute value, it is always invalid and cannot be
            // resolved. Exceptions may be thrown while parsing the value which should be ignored
          }
        }

      textView.setText(attr.value)
      textView.addTextChangedListener(textWatcher)
      textView.dropDownAnchor = binding.root.id
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }
}
