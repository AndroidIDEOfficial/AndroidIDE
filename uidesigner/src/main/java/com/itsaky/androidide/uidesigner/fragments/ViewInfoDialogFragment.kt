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

package com.itsaky.androidide.uidesigner.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.uidesigner.adapters.ViewAttrListAdapter
import com.itsaky.androidide.uidesigner.databinding.LayoutViewInfoBinding

/**
 * A [BottomSheetDialogFragment] which shows information about a clicked view.
 *
 * @author Akash Yadav
 */
class ViewInfoDialogFragment : BottomSheetDialogFragment() {

  private var binding: LayoutViewInfoBinding? = null

  var view: IView? = null
    set(value) {
      field = value
      if (isShowing) {
        showViewInfo()
      }
    }

  val isShowing: Boolean
    get() = dialog?.isShowing ?: false
  
  companion object {
    const val TAG = "ide.uidesigner.viewinfo"
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    this.binding = LayoutViewInfoBinding.inflate(inflater, container, false)
    return this.binding!!.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    if (this.view != null) {
      showViewInfo()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    this.binding = null
  }

  private fun showViewInfo() {
    val binding = this.binding ?: return
    val view = this.view ?: return

    binding.viewName.text = view.simpleName
    binding.viewInfo.text = view.name
    binding.attrList.adapter = ViewAttrListAdapter(view.attributes)
    
    binding.btnDelete.setOnClickListener {
      view.removeFromParent()
      dismiss()
    }
  }
}
