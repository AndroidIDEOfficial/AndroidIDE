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

import android.widget.Spinner
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.AttributeHandlerScope

/**
 * Attribute adapter for [Spinner].
 *
 * @author Akash Yadav
 */
@ViewAdapter(Spinner::class)
open class SpinnerAdapter<T : Spinner> : AbsSpinnerAdapter<T>() {
  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("dropDownHorizontalOffset") {
      view.dropDownHorizontalOffset = parseDimension(context, value, 0)
    }
    create("dropDownVerticalOffset") {
      view.dropDownVerticalOffset = parseDimension(context, value, 0)
    }
    create("dropDownWidth") { view.dropDownWidth = parseDimension(context, value, 0) }
    create("gravity") { view.gravity = parseGravity(value) }
    create("popupBackground") { view.setPopupBackgroundDrawable(parseDrawable(context, value)) }
  }
}
