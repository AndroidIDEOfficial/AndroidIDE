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
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.R
import com.itsaky.androidide.databinding.LayoutBuildVariantItemBinding
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.models.BuildVariantInfo
import com.itsaky.androidide.tooling.api.models.BuildVariantInfo.Companion.withSelection
import com.itsaky.androidide.viewmodel.BuildVariantsViewModel
import java.util.Objects

/**
 * [RecyclerView] adapter for showing the list of Android modules and their selected build variant.
 *
 * @property items
 * @author Akash Yadav
 */
class BuildVariantsAdapter(
  private val viewModel: BuildVariantsViewModel,
  private var items: List<BuildVariantInfo>
) : RecyclerView.Adapter<BuildVariantsAdapter.ViewHolder>() {

  class ViewHolder(internal val binding: LayoutBuildVariantItemBinding) :
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = LayoutBuildVariantItemBinding.inflate(LayoutInflater.from(parent.context), parent,
      false)
    return ViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val binding = holder.binding
    val variantInfo = items[position]

    binding.moduleName.text = variantInfo.projectPath

    binding.variantName.apply {

      val viewModel = viewModel

      setAdapter(
        ArrayAdapter(binding.root.context, R.layout.support_simple_spinner_dropdown_item,
          variantInfo.buildVariants
        )
      )

      var listSelection = variantInfo.buildVariants.indexOf(variantInfo.selectedVariant)
      if (listSelection < 0 || listSelection >= variantInfo.buildVariants.size) {
        listSelection = 0
      }

      this.listSelection = listSelection
      setText(variantInfo.selectedVariant, false)

      addTextChangedListener { editable ->
        // update the changed build variants map
        viewModel.updatedBuildVariants = viewModel.updatedBuildVariants.also { variants ->

          // the newly selected build variant
          // if this is different that the variant that was used while initializing the project,
          // then the user is notified to re-sync the project
          // else the selection is cleared
          val newSelection = editable?.toString() ?: IAndroidProject.DEFAULT_VARIANT

          if (!Objects.equals(variantInfo.selectedVariant, newSelection)) {
            variants[variantInfo.projectPath] = variantInfo.withSelection(newSelection)
          } else {
            variants.remove(variantInfo.projectPath)
          }
        }
      }
    }
  }
}