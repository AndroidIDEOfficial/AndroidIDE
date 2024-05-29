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

package com.itsaky.androidide.uidesigner.drag

import android.graphics.Point
import android.view.View

/**
 * Builds drag shadow for draggable widgets.
 *
 * @author Akash Yadav
 */
class WidgetDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
  companion object {
    const val TOUCH_Y_OFFSET = 0.3
  }

  override fun onProvideShadowMetrics(outShadowSize: Point, outShadowTouchPoint: Point) {
    if (view != null) {
      val width = view.width
      val height = view.height
      outShadowSize.set(width, height)
      outShadowTouchPoint.set(width / 2, height + (height * TOUCH_Y_OFFSET).toInt())
    } else {
      super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
    }
  }
}
