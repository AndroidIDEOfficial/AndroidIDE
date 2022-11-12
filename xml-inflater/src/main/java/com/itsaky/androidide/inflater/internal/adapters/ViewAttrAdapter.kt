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
import android.os.Build
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.*
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import com.android.SdkConstants
import com.itsaky.androidide.annotations.inflater.AttributeAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IAttributeAdapter
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.endParse
import com.itsaky.androidide.inflater.internal.utils.parseId
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

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<View>(view, attribute) { file, context, layoutParams, _, name, value ->
      var applied = true
      @Suppress("DEPRECATION")
      when (name) {
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
        "id" -> id = parseId(file.resName, value)
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
        else -> applied = false
      }

      if (!applied && layoutParams is LinearLayout.LayoutParams) {
        applied = applyLinearLayoutParams(layoutParams, name, value)
      }

      if (!applied && layoutParams is RelativeLayout.LayoutParams) {
        applied = applyRelativeLayoutParams(layoutParams, file.resName, name, value)
      }

      if (!applied && layoutParams is FrameLayout.LayoutParams) {
        applied = applyFrameLayoutParams(layoutParams, name, value)
      }

      if (!applied && layoutParams is MarginLayoutParams) {
        applied = applyMarginParams(context, layoutParams, name, value)
      }

      if (!applied) {
        applied = applyLayoutParams(context, layoutParams, name, value)
      }

      if (applied) {
        this.layoutParams = layoutParams
      }

      return@doApply applied
    }
  }

  override fun applyBasic(view: IView) {
    view.apply {
      apply(this, AttributeImpl(name = "layout_height", value = "wrap_content"))
      apply(this, AttributeImpl(name = "layout_width", value = "wrap_content"))
    }
  }

  protected open fun applyLayoutParams(
    context: Context,
    params: LayoutParams,
    name: String,
    value: String,
  ): Boolean {
    var applied = true
    when (name) {
      "layout_height" -> params.height = parseDimension(context, value)
      "layout_width" -> params.width = parseDimension(context, value)
      else -> applied = false
    }
    return applied
  }

  protected open fun applyMarginParams(
    context: Context,
    params: MarginLayoutParams,
    name: String,
    value: String,
  ): Boolean {
    var handled = true
    when (name) {
      "layout_margin" -> {
        val margin = parseDimension(context, value, 0)
        params.setMargins(margin, margin, margin, margin)
      }
      "layout_marginLeft" -> params.leftMargin = parseDimension(context, value)
      "layout_marginTop" -> params.topMargin = parseDimension(context, value)
      "layout_marginRight" -> params.rightMargin = parseDimension(context, value)
      "layout_marginBottom" -> params.bottomMargin = parseDimension(context, value)
      "layout_marginStart" -> params.marginStart = parseDimension(context, value)
      "layout_marginEnd" -> params.marginEnd = parseDimension(context, value)
      else -> handled = false
    }
    return handled
  }

  protected open fun applyLinearLayoutParams(
    params: LinearLayout.LayoutParams,
    name: String,
    value: String,
  ): Boolean {
    var applied = true
    when (name) {
      "layout_gravity" -> params.gravity = parseGravity(value)
      "layout_weight" -> params.weight = parseFloat(value, 1f)
      else -> applied = false
    }
    return applied
  }

  protected open fun applyRelativeLayoutParams(
    params: RelativeLayout.LayoutParams,
    resName: String,
    name: String,
    value: String,
  ): Boolean {
    var handled = true
    when (name) {
      "layout_above" -> params.addRule(ABOVE, parseId(resName, value))
      "layout_alignBaseline" -> params.addRule(ALIGN_BASELINE, parseId(resName, value))
      "layout_alignBottom" -> params.addRule(ALIGN_BOTTOM, parseId(resName, value))
      "layout_alignEnd" -> params.addRule(ALIGN_END, parseId(resName, value))
      "layout_alignLeft" -> params.addRule(ALIGN_LEFT, parseId(resName, value))
      "layout_alignParentTop" -> setRuleIf(parseBoolean(value), ALIGN_PARENT_TOP, params)
      "layout_alignParentBottom" -> setRuleIf(parseBoolean(value), ALIGN_PARENT_BOTTOM, params)
      "layout_alignParentStart" -> setRuleIf(parseBoolean(value), ALIGN_PARENT_START, params)
      "layout_alignParentEnd" -> setRuleIf(parseBoolean(value), ALIGN_PARENT_END, params)
      "layout_alignParentLeft" -> setRuleIf(parseBoolean(value), ALIGN_PARENT_LEFT, params)
      "layout_alignParentRight" -> setRuleIf(parseBoolean(value), ALIGN_PARENT_RIGHT, params)
      "layout_alignRight" -> params.addRule(ALIGN_RIGHT, parseId(resName, value))
      "layout_alignStart" -> params.addRule(ALIGN_START, parseId(resName, value))
      "layout_alignTop" -> params.addRule(ALIGN_TOP, parseId(resName, value))
      "layout_alignWithParentIfMissing" -> params.alignWithParent = parseBoolean(value)
      "layout_below" -> params.addRule(BELOW, parseId(resName, value))
      "layout_centerHorizontal" -> setRuleIf(parseBoolean(value), CENTER_HORIZONTAL, params)
      "layout_centerInParent" -> setRuleIf(parseBoolean(value), CENTER_IN_PARENT, params)
      "layout_centerVertical" -> setRuleIf(parseBoolean(value), CENTER_VERTICAL, params)
      "layout_toEndOf" -> params.addRule(END_OF, parseId(resName, value))
      "layout_toStartOf" -> params.addRule(START_OF, parseId(resName, value))
      "layout_toLeftOf" -> params.addRule(LEFT_OF, parseId(resName, value))
      "layout_toRightOf" -> params.addRule(RIGHT_OF, parseId(resName, value))
      else -> handled = false
    }
    return handled
  }

  protected open fun applyFrameLayoutParams(
    params: FrameLayout.LayoutParams,
    name: String,
    value: String,
  ): Boolean {
    var applied = true
    if ("layout_gravity" == name) {
      params.gravity = parseGravity(value)
    } else {
      applied = false
    }
    return applied
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
        layoutParams: LayoutParams,
        namespace: INamespace,
        name: String,
        value: String,
      ) -> Boolean,
  ): Boolean {
    if (!canHandleNamespace(attribute.namespace)) {
      return false
    }
    @Suppress("UNCHECKED_CAST")
    return (view.view as T).let {
      val file = (view as ViewImpl).file
      val module =
        ProjectManager.findModuleForFile(file.file) as? AndroidModule
          ?: throw IllegalStateException("Cannot find module for file: $file")
      startParse(module)
      val applied =
        it.block(
          file,
          it.context,
          it.layoutParams,
          attribute.namespace,
          attribute.name,
          attribute.value
        )
      endParse()
      return@let applied
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
  
  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
  protected open fun isApi29(): Boolean {
    return Build.VERSION.SDK_INT >= 29
  }

  private fun setRuleIf(
    condition: Boolean,
    rule: Int,
    params: RelativeLayout.LayoutParams,
  ) {
    if (condition) {
      params.addRule(rule)
    } else {
      params.removeRule(rule)
    }
  }
}
