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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.itsaky.androidide.R
import com.itsaky.androidide.adapters.ProjectListAdapter.ViewHolder
import com.itsaky.androidide.databinding.LayoutProjectListItemBinding
import com.itsaky.androidide.models.ProjectInfoDetails

class ProjectListAdapter(
  projects: List<ProjectInfoDetails>,
  private val onClick: ((ProjectInfoDetails, ViewHolder) -> Unit)? = null
) : RecyclerView.Adapter<ViewHolder>() {

  private val projects = projects.toMutableList()

  class ViewHolder(internal val binding: LayoutProjectListItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutProjectListItemBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    ))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.binding.apply {
      val project = projects[position]
      val context = root.context

      //Choose another icon
      val icon =  AppCompatResources.getDrawable(context, R.drawable.ic_file_apk)
      val name = project.file.name
      val path = project.file.absolutePath

      Glide.with(context)
        .load(icon)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .override(50, 50)
        .placeholder(R.drawable.ic_file_apk)
        .into(projectIcon)

      projectName.text = name
      projectPath.text = path


      root.setOnClickListener {
        onClick?.invoke(project, holder)
      }

    }
  }

  override fun getItemCount(): Int {
    return projects.size
  }

}