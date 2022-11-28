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

package com.itsaky.androidide.inflater.internal.adapters

import android.content.Context
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adapter for [FrameLayout].
 *
 * @author Akash Yadav
 */
@ViewAdapter(FrameLayout::class)
open class FrameLayoutAttrAdapter : ViewGroupAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<FrameLayout>(view, attribute) {
        _: LayoutFile,
        _: Context,
        _: LayoutParams,
        _: INamespace,
        name: String,
        value: String ->
      
      var applied = true
      when (name) {
        "foregroundGravity" -> foregroundGravity = parseGravity(value)
        "measureAllChildren" -> measureAllChildren = parseBoolean(value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
  }
}
