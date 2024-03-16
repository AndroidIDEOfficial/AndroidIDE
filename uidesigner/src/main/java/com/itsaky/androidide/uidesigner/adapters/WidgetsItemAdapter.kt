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

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.uidesigner.adapters.WidgetsItemAdapter.VH
import com.itsaky.androidide.uidesigner.databinding.LayoutUiWidgetsItemBinding
import com.itsaky.androidide.uidesigner.drag.WidgetDragShadowBuilder
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel

/** @author Akash Yadav */
internal class WidgetsItemAdapter(
  private val widgets: List<UiWidget>,
  private val viewModel : WorkspaceViewModel
) : RecyclerView.Adapter<VH>() {

  inner class VH(val binding: LayoutUiWidgetsItemBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(
      LayoutUiWidgetsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )
  }

  override fun getItemCount(): Int {
    return widgets.size
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    val binding = holder.binding
    val widget = widgets[position]

    binding.name.setText(widget.label)
    binding.icon.setImageResource(widget.icon)
    binding.root.setOnLongClickListener {
      val shadow = WidgetDragShadowBuilder(binding.icon)
      val dataItem = ClipData.Item(DesignerWorkspaceFragment.DRAGGING_WIDGET)
      val data =
        ClipData(
          DesignerWorkspaceFragment.DRAGGING_WIDGET,
          arrayOf(DesignerWorkspaceFragment.DRAGGING_WIDGET_MIME),
          dataItem
        )
      return@setOnLongClickListener binding.icon.startDragAndDrop(data, shadow, widget, 0).also {
        viewModel.drawerOpened = !it
      }
    }
  }
}
