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

package com.itsaky.androidide.adapters.onboarding

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.databinding.LayoutOnboardingItemBinding
import com.itsaky.androidide.models.OnboardingItem

/**
 * Default implmentation of [RecyclerView.Adapter] for showing [OnboardingItem]s.
 *
 * @author Akash Yadav
 */
open class DefaultOnboardingItemAdapter<T : OnboardingItem>(
  protected val items: List<T>,
  protected val onItemClickListener: OnItemClickListener<T>? = null,
  protected val onItemLongClickListener: OnItemLongClickListener<T>? = null
) : RecyclerView.Adapter<DefaultOnboardingItemAdapter.ViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LayoutOnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    doBindViewHolder(holder, position, getItem(position), holder.binding)
  }

  protected open fun doBindViewHolder(
    holder: ViewHolder,
    position: Int,
    item: T,
    binding: LayoutOnboardingItemBinding
  ) {
    binding.content.title.text = item.title

    if (item.description.isNotBlank()) {
      binding.content.description.text = item.description
    } else {
      binding.content.description.visibility = View.INVISIBLE
    }

    if (item.icon != 0) {
      binding.content.icon.setImageResource(item.icon)
      if (item.iconTint != 0) {
        binding.content.icon.supportImageTintList = ColorStateList.valueOf(item.iconTint)
      }
    } else {
      binding.content.icon.visibility = View.INVISIBLE
    }

    binding.root.isClickable = item.isClickable
    binding.root.isFocusable = item.isClickable

    if (item.isClickable && onItemClickListener != null) {
      binding.root.setOnClickListener { onItemClickListener.onClick(item, position, binding) }
    }

    if (item.isLongClickable && onItemLongClickListener != null) {
      binding.root.setOnLongClickListener { onItemLongClickListener.onLongClick(item, position, binding) }
    }
  }

  override fun getItemCount(): Int {
    return items.size
  }

  fun getItem(index: Int): T {
    return items[index]
  }

  class ViewHolder(val binding: LayoutOnboardingItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  fun interface OnItemClickListener<T : OnboardingItem> {

    fun onClick(item: T, position: Int, binding: LayoutOnboardingItemBinding)
  }

  fun interface OnItemLongClickListener<T : OnboardingItem> {

    fun onLongClick(item: T, position: Int, binding: LayoutOnboardingItemBinding): Boolean
  }
}