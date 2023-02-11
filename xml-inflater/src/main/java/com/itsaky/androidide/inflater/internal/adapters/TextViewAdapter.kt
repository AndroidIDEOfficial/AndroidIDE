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

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.TextUtils
import android.text.util.Linkify
import android.util.TypedValue
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R
import java.util.regex.Pattern

/**
 * Attribute adapter for [TextView].
 *
 * @author Akash Yadav
 */
@com.itsaky.androidide.annotations.inflater.ViewAdapter(TextView::class)
@IncludeInDesigner(group = WIDGETS)
open class TextViewAdapter<T : TextView> : ViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("autoLink") { view.autoLinkMask = parseAutoLinkMask(value) }
    create("drawableLeft") {
      val drawables = view.compoundDrawables
      view.setCompoundDrawables(
        parseDrawable(context, value),
        drawables[1],
        drawables[2],
        drawables[3]
      )
    }
    create("drawableTop") {
      val drawables = view.compoundDrawables
      view.setCompoundDrawables(
        drawables[0],
        parseDrawable(context, value),
        drawables[2],
        drawables[3]
      )
    }
    create("drawableRight") {
      val drawables = view.compoundDrawables
      view.setCompoundDrawables(
        drawables[0],
        drawables[1],
        parseDrawable(context, value),
        drawables[3]
      )
    }
    create("drawableBottom") {
      val drawables = view.compoundDrawables
      view.setCompoundDrawables(
        drawables[0],
        drawables[1],
        drawables[2],
        parseDrawable(context, value)
      )
    }
    create("drawableStart") {
      val drawablesRelative = view.compoundDrawablesRelative
      view.setCompoundDrawables(
        parseDrawable(context, value),
        drawablesRelative[1],
        drawablesRelative[2],
        drawablesRelative[3]
      )
    }
    create("drawableEnd") {
      val drawablesRelative = view.compoundDrawablesRelative
      view.setCompoundDrawables(
        drawablesRelative[0],
        drawablesRelative[1],
        parseDrawable(context, value),
        drawablesRelative[3]
      )
    }
    create("drawablePadding") { view.compoundDrawablePadding = parseDimension(context, value, 0) }
    create("ellipsize") { view.ellipsize = parseEllipsize(value) }
    create("gravity") { view.gravity = parseGravity(value) }
    create("hint") { view.hint = parseString(value) }
    create("letterSpacing") { view.letterSpacing = parseFloat(value) }
    create("lineHeight") { view.setLines(parseInteger(value, Int.MAX_VALUE)) }
    create("linksClickable") { view.linksClickable = parseBoolean(value) }
    create("marqueeRepeatLimit") { view.marqueeRepeatLimit = parseInteger(value, Int.MAX_VALUE) }
    create("maxLines") { view.maxLines = parseInteger(value, Int.MAX_VALUE) }
    create("minLines") { view.minLines = parseInteger(value, 1) }
    create("singleLine") { view.isSingleLine = parseBoolean(value) }
    create("text") { view.text = parseString(value) }
    create("textAllCaps") { view.isAllCaps = parseBoolean(value) }
    create("textColor") { view.setTextColor(parseColor(context, value)) }
    create("textColorHint") { view.setHintTextColor(parseColor(context, value)) }
    create("textSize") {
      view.setTextSize(
        TypedValue.COMPLEX_UNIT_PX,
        parseDimensionF(context, value, SizeUtils.sp2px(14f).toFloat())
      )
    }
    create("textStyle") { view.setTypeface(view.typeface, parseTextStyle(value)) }
    create("typeface") { view.typeface = parseTypeface(value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(TextView::class.java, R.string.widget_textview, R.drawable.ic_widget_textview)
    )
  }

  @SuppressLint("SetTextI18n")
  override fun applyBasic(view: IView) {
    super.applyBasic(view)
    (view.view as? TextView)?.text = "AndroidIDE"
  }

  protected open fun parseTextStyle(value: String): Int {
    val splits: Array<String> =
      value.split(Pattern.quote("|").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var mask = 0
    for (split in splits) {
      mask = mask or textStyleFor(split)
    }
    return mask
  }

  protected open fun textStyleFor(split: String): Int {
    return when (split) {
      "bold" -> Typeface.BOLD
      "italic" -> Typeface.ITALIC
      "normal" -> Typeface.NORMAL
      else -> Typeface.NORMAL
    }
  }

  protected open fun parseTypeface(value: String): Typeface? {
    return when (value) {
      "sans" -> Typeface.SANS_SERIF
      "serif" -> Typeface.SERIF
      "monospace" -> Typeface.MONOSPACE
      "normal" -> Typeface.DEFAULT
      else -> Typeface.DEFAULT
    }
  }

  protected open fun parseEllipsize(value: String): TextUtils.TruncateAt? {
    return when (value) {
      "end" -> TextUtils.TruncateAt.END
      "start" -> TextUtils.TruncateAt.START
      "marquee" -> TextUtils.TruncateAt.MARQUEE
      "middle" -> TextUtils.TruncateAt.MIDDLE
      "none" -> null
      else -> null
    }
  }

  protected open fun parseAutoLinkMask(value: String): Int {
    val splits: Array<String> =
      value.split(Pattern.quote("|").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var mask = 0
    for (split in splits) {
      mask = mask or autoLinkMaskFor(split)
    }
    return mask
  }

  @Suppress("DEPRECATION")
  protected open fun autoLinkMaskFor(mask: String): Int {
    return when (mask) {
      "all" -> Linkify.ALL
      "web" -> Linkify.WEB_URLS
      "phone" -> Linkify.PHONE_NUMBERS
      "map" -> Linkify.MAP_ADDRESSES
      "email" -> Linkify.EMAIL_ADDRESSES
      "none" -> 0
      else -> 0
    }
  }
}
