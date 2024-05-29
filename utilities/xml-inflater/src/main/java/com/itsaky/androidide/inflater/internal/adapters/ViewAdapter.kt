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
import android.widget.RelativeLayout.ABOVE
import android.widget.RelativeLayout.ALIGN_BASELINE
import android.widget.RelativeLayout.ALIGN_BOTTOM
import android.widget.RelativeLayout.ALIGN_END
import android.widget.RelativeLayout.ALIGN_LEFT
import android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM
import android.widget.RelativeLayout.ALIGN_PARENT_END
import android.widget.RelativeLayout.ALIGN_PARENT_LEFT
import android.widget.RelativeLayout.ALIGN_PARENT_RIGHT
import android.widget.RelativeLayout.ALIGN_PARENT_START
import android.widget.RelativeLayout.ALIGN_PARENT_TOP
import android.widget.RelativeLayout.ALIGN_RIGHT
import android.widget.RelativeLayout.ALIGN_START
import android.widget.RelativeLayout.ALIGN_TOP
import android.widget.RelativeLayout.BELOW
import android.widget.RelativeLayout.CENTER_HORIZONTAL
import android.widget.RelativeLayout.CENTER_IN_PARENT
import android.widget.RelativeLayout.CENTER_VERTICAL
import android.widget.RelativeLayout.END_OF
import android.widget.RelativeLayout.LEFT_OF
import android.widget.RelativeLayout.RIGHT_OF
import android.widget.RelativeLayout.START_OF
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewAdapter
import com.itsaky.androidide.inflater.internal.IncludeView
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.inflater.utils.newAttribute
import com.itsaky.androidide.resources.R

/**
 * Attribute adapter for [View].
 *
 * @author Akash Yadav
 */
@ViewAdapter(forView = View::class)
@IncludeInDesigner(group = WIDGETS)
open class ViewAdapter<T : View> : IViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    create("alpha") { view.alpha = parseFloat(value, def = 1f) }
    create("background") { view.background = parseDrawable(context, value) }
    create("backgroundTint") { view.backgroundTintList = parseColorStateList(context, value) }
    create("backgroundTintMode") { view.backgroundTintMode = parsePorterDuffMode(value) }
    create("clipToOutline") { view.clipToOutline = parseBoolean(value) }
    create("contentDescription") { view.contentDescription = parseString(value) }
    create("contextClickable") { view.isContextClickable = parseBoolean(value) }
    create("defaultFocusHighlightEnabled") {
      view.defaultFocusHighlightEnabled = parseBoolean(value)
    }
    create("drawingCacheQuality") {
      @Suppress("DEPRECATION")
      view.drawingCacheQuality = parseDrawingCacheQuality(value)
    }
    create("duplicateParentState") { view.isDuplicateParentStateEnabled = parseBoolean(value) }
    create("elevation") { view.elevation = parseDimensionF(context, value) }
    create("fadeScrollbars") { view.isScrollbarFadingEnabled = parseBoolean(value) }
    create("fadingEdgeLength") { view.setFadingEdgeLength(parseDimension(context, value)) }
    create("filterTouchesWhenObscured") { view.filterTouchesWhenObscured = parseBoolean(value) }
    create("foreground") { view.foreground = parseDrawable(context, value) }
    create("foregroundGravity") { view.foregroundGravity = parseGravity(value) }
    create("foregroundTint") { view.foregroundTintList = parseColorStateList(context, value) }
    create("foregroundTintMode") { view.foregroundTintMode = parsePorterDuffMode(value) }
    create("id") { view.id = parseId(file.resName, value) }
    create("minHeight") { view.minimumWidth = parseDimension(context, value) }
    create("minWidth") { view.minimumHeight = parseDimension(context, value) }
    create("padding") {
      parseDimension(context, value).also {
        view.updatePadding(left = it, top = it, right = it, bottom = it)
      }
    }
    create("paddingLeft") { view.updatePadding(left = parseDimension(context, value)) }
    create("paddingTop") { view.updatePadding(top = parseDimension(context, value)) }
    create("paddingRight") { view.updatePadding(right = parseDimension(context, value)) }
    create("paddingBottom") { view.updatePadding(bottom = parseDimension(context, value)) }
    create("paddingStart") { view.updatePaddingRelative(start = parseDimension(context, value)) }
    create("paddingEnd") { view.updatePaddingRelative(end = parseDimension(context, value)) }
    create("rotation") { view.rotation = parseFloat(value) }
    create("rotationX") { view.rotationX = parseFloat(value) }
    create("rotationY") { view.rotationY = parseFloat(value) }
    create("scaleX") { view.scaleX = parseFloat(value) }
    create("scaleY") { view.scaleY = parseFloat(value) }
    create("scrollX") { view.scrollX = parseInteger(value) }
    create("scrollY") { view.scrollY = parseInteger(value) }
    create("textAlignment") { view.textAlignment = parseTextAlignment(value) }
    create("textDirection") { view.textDirection = parseTextDirection(value) }
    create("tooltipText") { view.tooltipText = parseString(value) }
    create("transformPivotX") { view.pivotX = parseFloat(value) }
    create("transformPivotY") { view.pivotY = parseFloat(value) }
    create("translationX") { view.translationX = parseFloat(value) }
    create("translationY") { view.translationY = parseFloat(value) }
    create("translationZ") { view.translationZ = parseFloat(value) }
    create("visibility") { view.visibility = parseVisibility(value) }
  }

  override fun AttributeHandlerScope<T>.applyLayoutParams(): Boolean {

    var applied = false

    if (layoutParams is LinearLayout.LayoutParams) {
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
      view.layoutParams = layoutParams
    }

    return applied
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(UiWidget(View::class.java, R.string.widget_view, R.drawable.ic_widget_view))
  }

  override fun applyBasic(view: IView) {
    if (view is IncludeView) {
      return
    }

    view.addAttribute(newAttribute(view = view, name = "layout_height", value = "wrap_content"))
    view.addAttribute(newAttribute(view = view, name = "layout_width", value = "wrap_content"))
  }

  override fun isRequiredAttribute(attribute: IAttribute): Boolean {
    if (attribute.namespace?.uri != INamespace.ANDROID.uri) {
      return false
    }

    return when (attribute.name) {
      "layout_width",
      "layout_height" -> true
      else -> false
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
