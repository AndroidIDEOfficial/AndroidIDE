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
import com.itsaky.androidide.databinding.LayoutMainActionItemBinding
import com.itsaky.androidide.models.MainScreenAction

/**
 * Adapter for the actions available on the main screen.
 *
 * @author Akash Yadav
 */
class MainActionsListAdapter
@JvmOverloads
constructor(val actions: List<MainScreenAction> = emptyList()) :
  RecyclerView.Adapter<MainActionsListAdapter.VH>() {
  class VH(val binding: LayoutMainActionItemBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    VH(LayoutMainActionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  override fun getItemCount(): Int = actions.size

  fun getAction(index: Int) = actions[index]
  
  override fun onBindViewHolder(holder: VH, position: Int) {
    val action = getAction(index = position)
    val binding = holder.binding
    
    binding.root.apply {
      setText(action.text)
      setIconResource(action.icon)
      setOnClickListener {
        action.onClick?.invoke(action, it)
      }
      action.onLongClick?.let { onLongClick ->
        setOnLongClickListener {
          onLongClick(action, it)
        }
      }
    }
  }
}
