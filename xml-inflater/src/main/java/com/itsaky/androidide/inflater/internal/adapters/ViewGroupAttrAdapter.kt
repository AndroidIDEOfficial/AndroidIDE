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

import android.animation.LayoutTransition
import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Base attribute adapter for all [ViewGroup].
 *
 * @author Akash Yadav
 */
abstract class ViewGroupAttrAdapter : ViewAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<ViewGroup>(view, attribute) {
      _: LayoutFile,
      _: Context,
      _: LayoutParams,
      _: INamespace,
      name: String,
      value: String ->
      var applied = true
      when (name) {
        "animateLayoutChanges" -> layoutTransition = LayoutTransition()
        "clipChildren" -> clipChildren = parseBoolean(value)
        "clipToPadding" -> clipToPadding = parseBoolean(value)
        "descendantFocusability" -> descendantFocusability = parseDescendantsFocusability(value)
        "layoutMode" -> layoutMode = parseLayoutMode(value)
        else -> applied = false
      }
      
      if (!applied) {
        applied = super.apply(view, attribute)
      }
      
      return@doApply applied
    }
  }

  protected open fun parseLayoutMode(value: String): Int {
    return when (value) {
      "opticalBounds" -> ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS
      "clipBounds" -> ViewGroup.LAYOUT_MODE_CLIP_BOUNDS
      else -> ViewGroup.LAYOUT_MODE_CLIP_BOUNDS
    }
  }

  protected open fun parseDescendantsFocusability(value: String): Int {
    return when (value) {
      "beforeDescendants" -> ViewGroup.FOCUS_BEFORE_DESCENDANTS
      "blocksDescendants" -> ViewGroup.FOCUS_BLOCK_DESCENDANTS
      "afterDescendants" -> ViewGroup.FOCUS_AFTER_DESCENDANTS
      else -> ViewGroup.FOCUS_AFTER_DESCENDANTS
    }
  }
}
