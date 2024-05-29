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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.uidesigner.adapters.WidgetsCategoryAdapter.VH
import com.itsaky.androidide.uidesigner.databinding.LayoutUiWidgetsCategoryBinding
import com.itsaky.androidide.uidesigner.models.UiWidgetCategory
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/** @author Akash Yadav */
internal class WidgetsCategoryAdapter(
  categories: List<UiWidgetCategory>,
  private val viewModel: WorkspaceViewModel
) : RecyclerView.Adapter<VH>() {

  private val categories =
    categories.let {
      val mutable = it.toMutableList()
      mutable.removeIf { category -> category.widgets.isEmpty() }
      mutable
    }

  inner class VH(val binding: LayoutUiWidgetsCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun updateExpandedState(isExpanded: Boolean) {
      binding.widgets.visibility = if (isExpanded) View.VISIBLE else View.GONE
      binding.chevron.rotation = if (isExpanded) 90f else 0f
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(
      LayoutUiWidgetsCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun getItemCount(): Int {
    return categories.size
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    val binding = holder.binding
    val category = categories[position]
    binding.name.setText(category.label)

    binding.widgets.adapter = WidgetsItemAdapter(category.widgets, viewModel)

    holder.updateExpandedState(category.isExpanded)

    binding.root.setOnClickListener {
      category.isExpanded = !category.isExpanded
      holder.updateExpandedState(category.isExpanded)
    }
  }
}
