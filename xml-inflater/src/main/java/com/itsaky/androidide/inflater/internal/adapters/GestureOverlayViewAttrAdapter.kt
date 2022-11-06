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
import android.gesture.GestureOverlayView
import android.view.ViewGroup.LayoutParams
import com.blankj.utilcode.util.ReflectUtils
import com.itsaky.androidide.annotations.inflater.AttributeAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adapter for [GestureOverlayView].
 *
 * @author Akash Yadav
 */
@AttributeAdapter(GestureOverlayView::class)
open class GestureOverlayViewAttrAdapter : FrameLayoutAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<GestureOverlayView>(view, attribute) {
      _: LayoutFile,
      context: Context,
      _: LayoutParams,
      _: INamespace,
      name: String,
      value: String ->
      var applied = true
      when (name) {
        "eventsInterceptionEnabled" -> isEventsInterceptionEnabled = parseBoolean(value)
        "fadeDuration" ->
          ReflectUtils.reflect(gesture).field("mFadeDuration", parseLong(value, 150))
        "fadeEnabled" -> isFadeEnabled = parseBoolean(value)
        "fadeOffset" -> fadeOffset = parseLong(value, 420)
        "gestureColor" -> gestureColor = parseColor(context, value)
        "gestureStrokeAngleThreshold" -> gestureStrokeAngleThreshold = parseFloat(value)
        "gestureStrokeLengthThreshold" -> gestureStrokeLengthThreshold = parseFloat(value)
        "gestureStrokeSquarenessThreshold" -> gestureStrokeSquarenessTreshold = parseFloat(value)
        "gestureStrokeType" -> gestureStrokeType = parseGestureStrokeType(value)
        "gestureStrokeWidth" -> gestureStrokeWidth = parseFloat(value)
        "orientation" -> orientation = parseOrientation(value)
        "uncertainGestureColor" -> uncertainGestureColor = parseColor(context, value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
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
