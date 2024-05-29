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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.itsaky.androidide.R
import com.itsaky.androidide.app.IDEApplication
import com.itsaky.androidide.contributors.Contributor
import com.itsaky.androidide.databinding.LayoutContributorsItemBinding

/**
 * @author Akash Yadav
 */
class ContributorsGridAdapter(
  contributors: List<Contributor>
) : RecyclerView.Adapter<ContributorsGridAdapter.ViewHolder>() {

  private val contributors = contributors.toMutableList()

  class ViewHolder(val binding: LayoutContributorsItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun getItemCount(): Int {
    return contributors.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LayoutContributorsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val binding = holder.binding
    val contributor = contributors[position]

    if (contributor is EmptyContributor) {
      binding.root.visibility = View.INVISIBLE
      return
    }

    Glide.with(binding.root)
      .load(contributor.avatarUrl)
      .placeholder(R.drawable.ic_account)
      .transition(DrawableTransitionOptions.withCrossFade(100))
      .into(binding.root)

    binding.root.setOnClickListener {
      IDEApplication.instance.openUrl(contributor.profileUrl)
    }
  }

  object EmptyContributor : Contributor {
    override val id: Int
      get() = 0
    override val username: String
      get() = ""
    override val avatarUrl: String
      get() = ""
    override val profileUrl: String
      get() = ""
  }

  internal fun fillDiff(extras: Int) {
    val count = itemCount
    for (i in 1..extras) {
      contributors.add(EmptyContributor)
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