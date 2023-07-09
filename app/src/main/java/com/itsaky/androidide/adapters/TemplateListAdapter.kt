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
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ConvertUtils
import com.google.android.material.shape.CornerFamily
import com.itsaky.androidide.adapters.TemplateListAdapter.ViewHolder
import com.itsaky.androidide.databinding.LayoutTemplateListItemBinding
import com.itsaky.androidide.templates.Template

/**
 * [RecyclerView.Adapter] for showing templates in a [RecyclerView].
 *
 * @author Akash Yadav
 */
class TemplateListAdapter(
  templates: List<Template<*>>,
  private val onClick: ((Template<*>, ViewHolder) -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {

  private val templates = templates.toMutableList()

  class ViewHolder(internal val binding: LayoutTemplateListItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutTemplateListItemBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    ))
  }

  override fun getItemCount(): Int {
    return templates.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.binding.apply {
      val template = templates[position]
      if (template == Template.EMPTY) {
        root.visibility = View.INVISIBLE
        return@apply
      }
      templateName.setText(template.templateName)
      templateIcon.setImageResource(template.thumb)
      templateIcon.shapeAppearanceModel =
        templateIcon.shapeAppearanceModel.toBuilder()
          .setAllCorners(CornerFamily.ROUNDED, ConvertUtils.dp2px(8f).toFloat())
          .build()

      root.setOnClickListener {
        onClick?.invoke(template, holder)
      }
    }
  }

  internal fun fillDiff(extras: Int) {
    val count = itemCount
    for (i in 1..extras) {
      templates.add(Template.EMPTY)
    }

    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
      override fun getOldListSize(): Int {
        return count
      }

      override fun getNewListSize(): Int {
        return count + extras
      }

      override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newItemPosition < count && oldItemPosition == newItemPosition
      }

      override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemPosition, newItemPosition)
      }
    })

    diff.dispatchUpdatesTo(this)
  }
}
