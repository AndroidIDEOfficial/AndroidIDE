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
import android.widget.LinearLayout
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.LayoutBehavior
import com.itsaky.androidide.inflater.LayoutBehavior.HORIZONTAL
import com.itsaky.androidide.inflater.LayoutBehavior.VERTICAL
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adapter for [LinearLayout].
 *
 * @author Akash Yadav
 */
@ViewAdapter(LinearLayout::class)
open class LinearLayoutAttrAdapter : ViewGroupAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<LinearLayout>(view, attribute) {
      _: LayoutFile,
      _: Context,
      _: LayoutParams,
      _: INamespace,
      name: String,
      value: String ->
      var applied = true

      when (name) {
        "baselineAligned" -> isBaselineAligned = parseBoolean(value)
        "baselineAlignedChildIndex" -> baselineAlignedChildIndex = parseInteger(value, childCount)
        "gravity" -> gravity = parseGravity(value)
        "measureWithLargestChild" -> isMeasureWithLargestChildEnabled = parseBoolean(value)
        "orientation" -> orientation = parseOrientation(value)
        "weightSum" -> weightSum = parseFloat(value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
  }

  override fun getLayoutBehavior(group: IViewGroup): LayoutBehavior {
    val orientation = group.findAttribute(INamespace.ANDROID.uri, "orientation")
    return if (orientation?.value == "vertical") VERTICAL else HORIZONTAL
  }

  protected open fun parseOrientation(value: String): Int {
    return when (value) {
      "vertical" -> LinearLayout.VERTICAL
      "horizontal" -> LinearLayout.HORIZONTAL
      else -> LinearLayout.HORIZONTAL
    }
  }
}
