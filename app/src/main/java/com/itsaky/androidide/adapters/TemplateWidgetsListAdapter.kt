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
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.adapters.TemplateWidgetsListAdapter.WidgetViewHolder
import com.itsaky.androidide.databinding.LayoutTemplateWidgetlistItemBinding
import com.itsaky.androidide.templates.ITemplateWidgetViewProvider
import com.itsaky.androidide.templates.Widget

/**
 * A [RecyclerView.Adapter] that is used to show the widgets from templates.
 *
 * @author Akash Yadav
 */
class TemplateWidgetsListAdapter(private val widgets: List<Widget<*>>) :
  RecyclerView.Adapter<WidgetViewHolder>() {

  class WidgetViewHolder(
    internal val binding: LayoutTemplateWidgetlistItemBinding
  ) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
  ): WidgetViewHolder {
    return WidgetViewHolder(LayoutTemplateWidgetlistItemBinding.inflate(
      LayoutInflater.from(parent.context), parent, false))
  }

  override fun getItemCount(): Int {
    return widgets.size
  }

  override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
    holder.binding.apply {
      val viewProvider = ITemplateWidgetViewProvider.getInstance()
      val widget = widgets[position]
      val view = viewProvider.createView(root.context, widget)

      root.removeAllViews()
      root.addView(view,
        LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT))
    }
  }
}