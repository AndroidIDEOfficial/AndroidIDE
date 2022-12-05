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

package com.itsaky.androidide.uidesigner.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IView.SingleAttributeChangeListener
import com.itsaky.androidide.uidesigner.adapters.ViewAttrListAdapter.VH
import com.itsaky.androidide.uidesigner.databinding.LayoutViewattrItemBinding
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/**
 * A [RecyclerView.Adapter] which shows the list of attributes of the selected view in the UI
 * designer.
 *
 * @author Akash Yadav
 */
class ViewAttrListAdapter(attributes: List<IAttribute>, private val viewModel: WorkspaceViewModel?, private val onClick: (IAttribute) -> Unit) :
  RecyclerView.Adapter<VH>() {
  
  private val attributes = attributes.sortedBy { it.name }
  
  class VH(val binding: LayoutViewattrItemBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(LayoutViewattrItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun getItemCount(): Int {
    return attributes.size
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: VH, position: Int) {
    val binding = holder.binding
    val attr = this.attributes[position]
    
    binding.attrName.text = "${attr.namespace.prefix}:${attr.name}"
    binding.attrValue.text = attr.value
    
    binding.root.setOnClickListener {
      onClick(attr)
      val viewModel = this.viewModel ?: return@setOnClickListener
      val attrUpdateListener = object : SingleAttributeChangeListener() {
        override fun onAttributeUpdated(view: IView, attribute: IAttribute, oldValue: String) {
          binding.attrValue.text = attribute.value
        }
      }
      val viewInfoScreenObserver = object : Observer<Int> {
        override fun onChanged(t: Int?) {
          if (t == WorkspaceViewModel.SCREEN_VIEW_INFO) {
            viewModel._viewInfoScreen.removeObserver(this)
          }
        }
      }
      viewModel._viewInfoScreen.observe(binding.root.context as LifecycleOwner, viewInfoScreenObserver)
      viewModel.view?.registerAttributeChangeListener(attrUpdateListener)
    }
  }
}
