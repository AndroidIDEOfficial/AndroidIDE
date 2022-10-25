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

package com.itsaky.androidide.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.adapters.RunTasksCategoryAdapter.VH
import com.itsaky.androidide.databinding.LayoutRunTasksCategoryBinding
import com.itsaky.androidide.models.Checkable
import com.itsaky.androidide.models.RunTasksCategory
import com.itsaky.androidide.tooling.api.model.GradleTask

/** @author Akash Yadav */
class RunTasksCategoryAdapter(
  val items: List<RunTasksCategory>,
  private val onCheckChanged: (Checkable<GradleTask>) -> Unit = {}
) : RecyclerView.Adapter<VH>() {

  class VH(val binding: LayoutRunTasksCategoryBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(
      LayoutRunTasksCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun getItem(index: Int): RunTasksCategory {
    return items[index]
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    val binding = holder.binding
    val category = getItem(position)
    binding.header.setText(category.title)
    binding.tasks.adapter = RunTasksListAdapter(category.tasks, onCheckChanged)
  }
}
