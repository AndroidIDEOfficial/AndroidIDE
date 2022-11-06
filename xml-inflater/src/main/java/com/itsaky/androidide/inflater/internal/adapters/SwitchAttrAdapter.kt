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
import android.graphics.Typeface.DEFAULT
import android.view.ViewGroup.LayoutParams
import android.widget.Switch
import com.itsaky.androidide.annotations.inflater.AttributeAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adapter for [Switch].
 *
 * @author Akash Yadav
 */
@AttributeAdapter(Switch::class)
class SwitchAttrAdapter : CompoundButtonAttrAdapter() {
  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<Switch>(view, attribute) {
        _: LayoutFile,
        context: Context,
        _: LayoutParams,
        _: INamespace,
        name: String,
        value: String ->
      var handled = true
      when (name) {
        "showText" -> showText = parseBoolean(value)
        "splitTrack" -> splitTrack = parseBoolean(value)
        "switchMinWidth" -> switchMinWidth = parseDimension(context, value, 0)
        "switchPadding" -> switchPadding = parseDimension(context, value, 0)
        "textOff" -> textOff = parseString(value)
        "textOn" -> textOn = parseString(value)
        "textStyle" -> setSwitchTypeface(DEFAULT, parseTextStyle(value))
        "thumb" -> thumbDrawable = parseDrawable(context, value)
        "thumbTextPadding" -> thumbTextPadding = parseDimension(context, value, 0)
        "thumbTint" -> thumbTintList = parseColorStateList(context, value)
        "thumbTintMode" -> thumbTintMode = parsePorterDuffMode(value)
        "track" -> trackDrawable = parseDrawable(context, value)
        "trackTint" -> trackTintList = parseColorStateList(context, value)
        "trackTintMode" -> trackTintMode = parsePorterDuffMode(value)
        "typeface" -> setSwitchTypeface(parseTypeface(value))
        else -> handled = false
      }

      if (!handled) {
        handled = super.apply(view, attribute)
      }

      return@doApply handled
    }
  }
}
