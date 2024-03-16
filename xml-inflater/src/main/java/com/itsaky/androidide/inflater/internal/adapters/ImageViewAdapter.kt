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
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string

/**
 * Attribute adpater [ImageView]s.
 *
 * @author Akash Yadav
 */
@com.itsaky.androidide.annotations.inflater.ViewAdapter(ImageView::class)
@IncludeInDesigner(group = WIDGETS)
open class ImageViewAdapter<T : ImageView> : ViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("adjustViewBounds") { view.adjustViewBounds = parseBoolean(value) }
    create("baseline") { view.baseline = parseDimension(context, value, 0) }
    create("baselineAlignBottom") { view.baselineAlignBottom = parseBoolean(value) }
    create("cropToPadding") { view.cropToPadding = parseBoolean(value) }
    create("maxHeight") { view.maxHeight = parseDimension(context, value, 0) }
    create("maxWidth") { view.maxWidth = parseDimension(context, value, 0) }
    create("scaleType") { view.scaleType = parseScaleType(value) }
    create("src") { view.setImageDrawable(parseDrawable(context, value)) }
    create("tint") { view.imageTintList = parseColorStateList(context, value) }
    create("tintMode") { view.imageTintMode = parsePorterDuffMode(value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(ImageView::class.java, string.widget_image_view, drawable.ic_widget_imageview)
    )
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
