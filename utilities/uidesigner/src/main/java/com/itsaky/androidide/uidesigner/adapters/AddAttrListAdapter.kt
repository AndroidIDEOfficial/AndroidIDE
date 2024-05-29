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
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.uidesigner.adapters.AddAttrListAdapter.VH
import com.itsaky.androidide.uidesigner.databinding.LayoutAddAttrItemBinding

/**
 * Adapter to show the list of attributes that can be added to the selected view in the UI designer.
 *
 * @author Akash Yadav
 */
class AddAttrListAdapter(
  private val attributes: List<com.itsaky.androidide.inflater.IAttribute>,
  private val onClick: (com.itsaky.androidide.inflater.IAttribute) -> Unit = {}
) : RecyclerView.Adapter<VH>() {

  class VH(val binding: LayoutAddAttrItemBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(LayoutAddAttrItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun getItemCount(): Int {
    return attributes.size
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: VH, position: Int) {
    val binding = holder.binding
    val attr = this.attributes[position]

    val ns = attr.namespace?.prefix?.let { "${it}:" } ?: ""
    binding.name.text = "${ns}${attr.name}"
    binding.root.setOnClickListener { onClick(attr) }
  }
}
