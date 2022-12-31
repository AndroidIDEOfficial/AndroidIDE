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

import android.gesture.GestureOverlayView
import com.blankj.utilcode.util.ReflectUtils
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.AttributeHandlerScope

/**
 * Attribute adapter for [GestureOverlayView].
 *
 * @author Akash Yadav
 */
@ViewAdapter(GestureOverlayView::class)
open class GestureOverlayViewAdapter<T : GestureOverlayView> : FrameLayoutAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("eventsInterceptionEnabled") { view.isEventsInterceptionEnabled = parseBoolean(value) }
    create("fadeDuration") {
      ReflectUtils.reflect(view).field("mFadeDuration", parseLong(value, 150))
    }
    create("fadeEnabled") { view.isFadeEnabled = parseBoolean(value) }
    create("fadeOffset") { view.fadeOffset = parseLong(value, 420) }
    create("gestureColor") { view.gestureColor = parseColor(context, value) }
    create("gestureStrokeAngleThreshold") { view.gestureStrokeAngleThreshold = parseFloat(value) }
    create("gestureStrokeLengthThreshold") { view.gestureStrokeLengthThreshold = parseFloat(value) }
    create("gestureStrokeSquarenessThreshold") {
      view.gestureStrokeSquarenessTreshold = parseFloat(value)
    }
    create("gestureStrokeType") { view.gestureStrokeType = parseGestureStrokeType(value) }
    create("gestureStrokeWidth") { view.gestureStrokeWidth = parseFloat(value) }
    create("orientation") { view.orientation = parseOrientation(value) }
    create("uncertainGestureColor") { view.uncertainGestureColor = parseColor(context, value) }
  }

  protected open fun parseOrientation(value: String): Int {
    return if ("horizontal" == value) {
      GestureOverlayView.ORIENTATION_HORIZONTAL
    } else GestureOverlayView.ORIENTATION_VERTICAL
  }

  protected open fun parseGestureStrokeType(value: String): Int {
    return if ("multiple" == value) {
      GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE
    } else GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE
  }
}
