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
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.ImageView.ScaleType.CENTER
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.ImageView.ScaleType.FIT_CENTER
import android.widget.ImageView.ScaleType.FIT_END
import android.widget.ImageView.ScaleType.FIT_START
import android.widget.ImageView.ScaleType.FIT_XY
import android.widget.ImageView.ScaleType.MATRIX
import com.itsaky.androidide.annotations.inflater.AttributeAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.R
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adpater [ImageView]s.
 *
 * @author Akash Yadav
 */
@AttributeAdapter(ImageView::class)
open class ImageViewAttrAdapter : ViewAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<ImageView>(view, attribute) {
        _: LayoutFile,
        context: Context,
        _: LayoutParams,
        _: INamespace,
        name: String,
        value: String ->
      var applied = true

      when (name) {
        "adjustViewBounds" -> adjustViewBounds = parseBoolean(value)
        "baseline" -> baseline = parseDimension(context, value, 0)
        "baselineAlignBottom" -> baselineAlignBottom = parseBoolean(value)
        "cropToPadding" -> cropToPadding = parseBoolean(value)
        "maxHeight" -> maxHeight = parseDimension(context, value, 0)
        "maxWidth" -> maxWidth = parseDimension(context, value, 0)
        "scaleType" -> scaleType = parseScaleType(value)
        "src" -> setImageDrawable(parseDrawable(context, value))
        "tint" -> imageTintList = parseColorStateList(context, value)
        "tintMode" -> imageTintMode = parsePorterDuffMode(value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
  }

  override fun applyBasic(view: IView) {
    super.applyBasic(view)
    (view.view as ImageView).setImageResource(R.drawable.ic_android)
  }

  private fun parseScaleType(value: String): ScaleType {
    return when (value) {
      "center" -> CENTER
      "centerCrop" -> CENTER_CROP
      "centerInside" -> CENTER_INSIDE
      "fitEnd" -> FIT_END
      "fitStart" -> FIT_START
      "fitXY" -> FIT_XY
      "matrix" -> MATRIX
      "fitCenter" -> FIT_CENTER
      else -> FIT_CENTER
    }
  }
}
