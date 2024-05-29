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
import com.itsaky.androidide.common.databinding.LayoutIconTitleDescriptionBinding
import com.itsaky.androidide.models.IconTitleDescriptionItem

/**
 * [RecyclerView.Adapter] for showing [IconTitleDescription] items.
 *
 * @author Akash Yadav
 */
abstract class IconTitleDescriptionAdapter() :
  RecyclerView.Adapter<IconTitleDescriptionAdapter.ViewHolder>() {

  class ViewHolder(val binding: LayoutIconTitleDescriptionBinding) :
    RecyclerView.ViewHolder(binding.root)

  abstract fun getItem(position: Int): IconTitleDescriptionItem

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LayoutIconTitleDescriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val binding = holder.binding
    val item = getItem(position)

    binding.apply {
      icon.setImageDrawable(item.icon)
      title.text = item.title
      description.text = item.description
    }
  }
}