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

package com.itsaky.androidide.uidesigner.models

import android.view.ViewGroup
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroupAdapter
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewGroupImpl
import com.itsaky.androidide.inflater.viewAdapter

/**
 * Extension of [IView] for the UI Designer.
 *
 * @author Akash Yadav
 */
internal open class UiViewGroup(file: LayoutFile, name: String, view: ViewGroup) :
  ViewGroupImpl(file, name, view), CommonUiView by CommonUiViewImpl() {

  /**
   * Finds the index of the view based on the touch location.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @return The index where the new child should be added. This index must be valid.
   */
  fun computeViewIndex(x: Float, y: Float): Int {
    if (childCount == 0) {
      return 0
    }
    val adapter = viewAdapter

    if (adapter !is IViewGroupAdapter) {
      return childCount
    }

    return adapter.computeChildIndex(this, x, y)
  }

  override fun findNearestChild(x: Float, y: Float, vertical: Boolean
  ): Pair<IView, Int>? {
    for (i in 0 until childCount) {
      val child = get(i)
      if (child is CommonUiView && !child.includeInIndexComputation) {
        continue
      }
      val rect = child.getViewRect()
      if (vertical && (y > rect.top && y < rect.bottom)) {
        return child to i
      }

      if (!vertical && (x > rect.left && x < rect.right)) {
        return child to i
      }
    }

    return null
  }
}
