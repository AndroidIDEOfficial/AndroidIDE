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
import android.widget.CompoundButton
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adapter for [CompoundButton].
 *
 * @author Akash Yadav
 */
abstract class CompoundButtonAttrAdapter : ButtonAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<CompoundButton>(view, attribute) {
      _: LayoutFile,
      context: Context,
      _: LayoutParams,
      _: INamespace,
      name: String,
      value: String ->
      var handled = true

      when (name) {
        "buttonTint" -> buttonTintList = parseColorStateList(context, value)
        "button" -> buttonDrawable = parseDrawable(context, value)
        "buttonTintMode" -> buttonTintMode = parsePorterDuffMode(value)
        "checked" -> isChecked = parseBoolean(value = value, def = true)
        else -> handled = false
      }

      if (!handled) {
        handled = super.apply(view, attribute)
      }

      return@doApply handled
    }
  }

  override fun applyBasic(view: IView) {
    super.applyBasic(view)
    (view.view as CompoundButton).isChecked = true
  }
}
