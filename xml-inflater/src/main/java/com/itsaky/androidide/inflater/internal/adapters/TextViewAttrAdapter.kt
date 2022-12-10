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
import android.graphics.Typeface
import android.text.TextUtils
import android.text.util.Linkify
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.utils.newAttribute
import java.util.regex.Pattern

/**
 * Attribute adapter for [TextView].
 *
 * @author Akash Yadav
 */
@ViewAdapter(TextView::class)
open class TextViewAttrAdapter : ViewAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<TextView>(view, attribute) {
      _: LayoutFile,
      context: Context,
      _: LayoutParams,
      _: INamespace,
      name: String,
      value: String ->
      val drawables = compoundDrawables
      val drawablesRelative = compoundDrawablesRelative
      var applied = true
      when (name) {
        "autoLink" -> autoLinkMask = parseAutoLinkMask(value)
        "drawableLeft" ->
          setCompoundDrawables(
            parseDrawable(context, value),
            drawables[1],
            drawables[2],
            drawables[3]
          )
        "drawableTop" ->
          setCompoundDrawables(
            drawables[0],
            parseDrawable(context, value),
            drawables[2],
            drawables[3]
          )
        "drawableRight" ->
          setCompoundDrawables(
            drawables[0],
            drawables[1],
            parseDrawable(context, value),
            drawables[3]
          )
        "drawableBottom" ->
          setCompoundDrawables(
            drawables[0],
            drawables[1],
            drawables[2],
            parseDrawable(context, value)
          )
        "drawableStart" ->
          setCompoundDrawables(
            parseDrawable(context, value),
            drawablesRelative[1],
            drawablesRelative[2],
            drawablesRelative[3]
          )
        "drawableEnd" ->
          setCompoundDrawables(
            drawablesRelative[0],
            drawablesRelative[1],
            parseDrawable(context, value),
            drawablesRelative[3]
          )
        "drawablePadding" -> compoundDrawablePadding = parseDimension(context, value, 0)
        "ellipsize" -> ellipsize = parseEllipsize(value)
        "gravity" -> gravity = parseGravity(value)
        "hint" -> hint = parseString(value)
        "letterSpacing" -> letterSpacing = parseFloat(value)
        "lineHeight" -> setLines(parseInteger(value, Int.MAX_VALUE))
        "linksClickable" -> linksClickable = parseBoolean(value)
        "marqueeRepeatLimit" -> marqueeRepeatLimit = parseInteger(value, Int.MAX_VALUE)
        "maxLines" -> maxLines = parseInteger(value, Int.MAX_VALUE)
        "minLines" -> minLines = parseInteger(value, 1)
        "singleLine" -> isSingleLine = parseBoolean(value)
        "text" -> text = parseString(value)
        "textAllCaps" -> isAllCaps = parseBoolean(value)
        "textColor" -> setTextColor(parseColor(context, value))
        "textColorHint" -> setHintTextColor(parseColor(context, value))
        "textSize" ->
          setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            parseDimensionF(context, value, SizeUtils.sp2px(14f).toFloat())
          )
        "textStyle" -> setTypeface(typeface, parseTextStyle(value))
        "typeface" -> typeface = parseTypeface(value)
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
    view.addAttribute(newAttribute(view = view, name = "text", value = "AndroidIDE"))
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
