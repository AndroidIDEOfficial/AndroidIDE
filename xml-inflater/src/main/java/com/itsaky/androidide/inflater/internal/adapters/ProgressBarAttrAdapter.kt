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
import android.widget.ProgressBar
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * Attribute adapter for [ProgressBar].
 *
 * @author Akash Yadav
 */
@ViewAdapter(ProgressBar::class)
open class ProgressBarAttrAdapter : ViewAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<ProgressBar>(view, attribute) {
      _: LayoutFile,
      context: Context,
      _: LayoutParams,
      _: INamespace,
      name: String,
      value: String ->
      var applied = true

      when (name) {
        "indeterminate" -> isIndeterminate = parseBoolean(value)
        "indeterminateDrawable" -> indeterminateDrawable = parseDrawable(context, value)
        "indeterminateTint" -> indeterminateTintList = parseColorStateList(context, value)
        "indeterminateTintMode" -> indeterminateTintMode = parsePorterDuffMode(value)
        "max" -> max = parseInteger(value, 100)
        "maxHeight" ->
          if (isApi29()) {
            maxHeight = parseDimension(context, value, Int.MAX_VALUE)
          }
        "maxWidth" ->
          if (isApi29()) {
            maxWidth = parseDimension(context, value, Int.MAX_VALUE)
          }
        "min" -> min = parseInteger(value, 0)
        "minHeight" ->
          if (isApi29()) {
            minHeight = parseDimension(context, value, 0)
          }
        "minWidth" ->
          if (isApi29()) {
            minWidth = parseDimension(context, value, 0)
          }
        "progress" -> progress = parseInteger(value, 50)
        "progressBackgroundTint" -> progressBackgroundTintList = parseColorStateList(context, value)
        "progressBackgroundTintMode" -> progressBackgroundTintMode = parsePorterDuffMode(value)
        "progressDrawable" -> progressDrawable = parseDrawable(context, value)
        "progressTint" -> progressTintList = parseColorStateList(context, value)
        "progressTintMode" -> progressTintMode = parsePorterDuffMode(value)
        "secondaryProgress" -> secondaryProgress = parseInteger(value, 0)
        "secondaryProgressTint" -> secondaryProgressTintList = parseColorStateList(context, value)
        "secondaryProgressTintMode" -> secondaryProgressTintMode = parsePorterDuffMode(value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
  }
}
