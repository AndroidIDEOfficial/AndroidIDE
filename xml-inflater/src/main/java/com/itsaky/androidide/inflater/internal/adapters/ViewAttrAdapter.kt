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
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import com.android.SdkConstants
import com.itsaky.androidide.annotations.inflater.AttributeAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IAttributeAdapter
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.endParse
import com.itsaky.androidide.inflater.internal.utils.parseId
import com.itsaky.androidide.inflater.internal.utils.parseString
import com.itsaky.androidide.inflater.internal.utils.startParse
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.api.AndroidModule

/**
 * Attribute adapter for [View].
 *
 * @author Akash Yadav
 */
@AttributeAdapter(forView = View::class)
open class ViewAttrAdapter : IAttributeAdapter() {

  override fun apply(view: IView, attribute: IAttribute) {
    doApply<View>(view, attribute) { file, context, layoutParams, namespace, name, value ->
      var handled = true
      when (name) {
        "layout_height" -> layoutParams.height = parseDimension(context, value)
        "layout_width" -> layoutParams.width = parseDimension(context, value)
        "alpha" -> alpha = parseFloat(value)
        "background" -> background = parseDrawable(context, value)
        "backgroundTint" -> backgroundTintList = parseColorStateList(context, value)
        "backgroundTintMode" -> backgroundTintMode = parsePorterDuffMode(value)
        "clipToOutline" -> clipToOutline = parseBoolean(value)
        "contentDescription" -> contentDescription = parseString(value)
        "contextClickable" -> isContextClickable = parseBoolean(value)
        "defaultFocusHighlightEnabled" -> defaultFocusHighlightEnabled = parseBoolean(value)
        "drawingCacheQuality" -> drawingCacheQuality = parseDrawingCacheQuality(value)
        "duplicateParentState" -> isDuplicateParentStateEnabled = parseBoolean(value)
        "elevation" -> elevation = parseDimensionF(context, value)
        "fadeScrollbars" -> isScrollbarFadingEnabled = parseBoolean(value)
        "fadingEdgeLength" -> setFadingEdgeLength(parseDimension(context, value))
        "filterTouchesWhenObscured" -> filterTouchesWhenObscured = parseBoolean(value)
        "foreground" -> foreground = parseDrawable(context, value)
        "foregroundGravity" -> foregroundGravity = parseGravity(value)
        "foregroundTint" -> foregroundTintList = parseColorStateList(context, value)
        "foregroundTintMode" -> foregroundTintMode = parsePorterDuffMode(value)
        "id" -> parseId(file.resName, value)
        "minHeight" -> minimumWidth = parseDimension(context, value)
        "minWidth" -> minimumHeight = parseDimension(context, value)
        "padding" ->
          parseDimension(context, value).also {
            updatePadding(left = it, top = it, right = it, bottom = it)
          }
        "paddingLeft" -> updatePadding(left = parseDimension(context, value))
        "paddingTop" -> updatePadding(top = parseDimension(context, value))
        "paddingRight" -> updatePadding(right = parseDimension(context, value))
        "paddingBottom" -> updatePadding(bottom = parseDimension(context, value))
        "paddingStart" -> updatePaddingRelative(start = parseDimension(context, value))
        "paddingEnd" -> updatePaddingRelative(end = parseDimension(context, value))
        "rotation" -> rotation = parseFloat(value)
        "rotationX" -> rotationX = parseFloat(value)
        "rotationY" -> rotationY = parseFloat(value)
        "scaleX" -> scaleX = parseFloat(value)
        "scaleY" -> scaleY = parseFloat(value)
        "scrollX" -> scrollX = parseInteger(value)
        "scrollY" -> scrollY = parseInteger(value)
        "textAlignment" -> textAlignment = parseTextAlignment(value)
        "textDirection" -> textDirection = parseTextDirection(value)
        "tooltipText" -> tooltipText = parseString(value)
        "transformPivotX" -> pivotX = parseFloat(value)
        "transformPivotY" -> pivotY = parseFloat(value)
        "translationX" -> translationX = parseFloat(value)
        "translationY" -> translationY = parseFloat(value)
        "translationZ" -> translationZ = parseFloat(value)
        "visibility" -> visibility = parseVisibility(value)
        else -> handled = false
      }

      if (handled) {
        this.layoutParams = layoutParams
      }
    }
  }

  override fun applyBasic(view: IView) {
    TODO("Not yet implemented")
  }

  protected open fun canHandleNamespace(namespace: INamespace): Boolean {
    return this.canHandleNamespace(namespace.uri)
  }
  protected open fun canHandleNamespace(nsUri: String): Boolean {
    return SdkConstants.ANDROID_URI == nsUri
  }

  /**
   * Provides easy access to various properties related to the view and attribute when applying an
   * attributes.
   */
  protected open fun <T : View> doApply(
    view: IView,
    attribute: IAttribute,
    block:
      T.(
        file: LayoutFile,
        context: Context,
        layoutParams: ViewGroup.LayoutParams,
        namespace: INamespace,
        name: String,
        value: String
      ) -> Unit
  ) {
    if (!canHandleNamespace(attribute.namespace)) {
      return
    }
    (view.view as T).apply {
      val file = (view as ViewImpl).file
      val module =
        ProjectManager.findModuleForFile(file.file) as? AndroidModule
          ?: throw IllegalStateException("Cannot find module for file: $file")
      startParse(module)
      block(file, context, layoutParams, attribute.namespace, attribute.name, attribute.value)
      endParse()
    }
  }

  protected open fun parseDrawingCacheQuality(value: String): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseDrawingCacheQuality(value)
  }

  protected open fun parseVisibility(value: String): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseVisibility(value)
  }

  protected open fun parseTextAlignment(value: String): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseTextAlignment(value)
  }

  protected open fun parseTextDirection(value: String): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseTextDirection(value)
  }

  protected open fun parsePorterDuffMode(mode: String): PorterDuff.Mode {
    return com.itsaky.androidide.inflater.internal.utils.parsePorterDuffMode(mode)
  }
}
