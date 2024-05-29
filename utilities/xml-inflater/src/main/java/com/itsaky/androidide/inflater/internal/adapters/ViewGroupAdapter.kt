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
import android.view.ViewGroup
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.IViewGroupAdapter
import com.itsaky.androidide.inflater.LayoutStrategy
import com.itsaky.androidide.inflater.LayoutStrategy.Companion.TOP_LEFT

/**
 * Base attribute adapter for all [ViewGroup].
 *
 * @author Akash Yadav
 */
abstract class ViewGroupAdapter<T : ViewGroup> : ViewAdapter<T>(),
                                                 IViewGroupAdapter {

  override fun createAttrHandlers(
    create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit
  ) {
    super.createAttrHandlers(create)
    create("animateLayoutChanges") {
      view.layoutTransition = LayoutTransition()
    }
    create("clipChildren") { view.clipChildren = parseBoolean(value) }
    create("clipToPadding") { view.clipToPadding = parseBoolean(value) }
    create("descendantFocusability") {
      view.descendantFocusability = parseDescendantsFocusability(value)
    }
    create("layoutMode") { view.layoutMode = parseLayoutMode(value) }
  }

  override fun getLayoutStrategy(group: IViewGroup): LayoutStrategy {
    return TOP_LEFT
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
