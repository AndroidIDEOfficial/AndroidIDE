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

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.inflater.IView.SingleAttributeChangeListener
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.adapters.ViewAttrListAdapter.VH
import com.itsaky.androidide.uidesigner.databinding.LayoutViewattrItemBinding
import com.itsaky.androidide.uidesigner.models.UiAttribute
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel
import com.itsaky.androidide.utils.DialogUtils

/**
 * A [RecyclerView.Adapter] which shows the list of attributes of the selected view in the UI
 * designer.
 *
 * @author Akash Yadav
 */
internal class ViewAttrListAdapter(
  attributes: List<com.itsaky.androidide.inflater.IAttribute>,
  private val viewModel: WorkspaceViewModel?,
  private val onDeleteAttr: (com.itsaky.androidide.inflater.IAttribute) -> Boolean,
  private val onClick: (com.itsaky.androidide.inflater.IAttribute) -> Unit
) : RecyclerView.Adapter<VH>() {

  private val attributes = attributes.sortedBy { it.name }.toMutableList()

  class VH(val binding: LayoutViewattrItemBinding) : RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(LayoutViewattrItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
  }

  override fun getItemCount(): Int {
    return attributes.size
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: VH, position: Int) {
    val binding = holder.binding
    val attr = this.attributes[position] as UiAttribute
  
    val ns = attr.namespace?.prefix?.let { "${it}:" } ?: ""
    binding.attrName.text = "${ns}${attr.name}"
    binding.attrValue.text = attr.value

    if (!attr.isRequired) {
      binding.deleteAttr.visibility = View.VISIBLE
      binding.deleteAttr.setOnClickListener {
        confirmDeleteAttr(binding.deleteAttr.context, attr, position)
      }
    } else binding.deleteAttr.visibility = View.INVISIBLE

    binding.root.setOnClickListener {
      onClick(attr)
      val viewModel = this.viewModel ?: return@setOnClickListener
      val attrUpdateListener =
        object : SingleAttributeChangeListener() {
          override fun onAttributeUpdated(view: com.itsaky.androidide.inflater.IView, attribute: com.itsaky.androidide.inflater.IAttribute, oldValue: String) {
            binding.attrValue.text = attribute.value
          }
        }
      viewModel.view?.registerAttributeChangeListener(attrUpdateListener)
    }
  }

  private fun confirmDeleteAttr(context: Context, attribute: UiAttribute, position: Int) {
    DialogUtils.newYesNoDialog(
        context = context,
        title = context.getString(R.string.title_confirm_delete),
        message = context.getString(R.string.msg_confirm_delete, attribute.qualifiedName),
        positiveClickListener = { dialog, _ ->
          dialog.dismiss()
          if (onDeleteAttr(attribute)) {
            this.attributes.removeAt(position)
            notifyItemRemoved(position)
          }
        }
      )
      .show()
  }
}
