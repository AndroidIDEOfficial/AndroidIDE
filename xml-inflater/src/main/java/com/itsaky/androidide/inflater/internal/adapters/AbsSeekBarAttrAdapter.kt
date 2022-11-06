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

import android.widget.AbsSeekBar
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IView

/**
 * Attribute adapter for [AbsSeekBar].
 *
 * @author Akash Yadav
 */
abstract class AbsSeekBarAttrAdapter : ProgressBarAttrAdapter() {

  override fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply<AbsSeekBar>(view, attribute) {
        _,
        context,
        _,
        _,
        name,
        value ->
      var applied = true

      when (name) {
        "thumbTint" -> thumbTintList = parseColorStateList(context, value)
        "thumbTintMode" -> thumbTintMode = parsePorterDuffMode(value)
        "tickMarkTint" -> tickMarkTintList = parseColorStateList(context, value)
        "tickMarkTintMode" -> tickMarkTintMode = parsePorterDuffMode(value)
        else -> applied = false
      }

      if (!applied) {
        applied = super.apply(view, attribute)
      }

      return@doApply applied
    }
  }
}
