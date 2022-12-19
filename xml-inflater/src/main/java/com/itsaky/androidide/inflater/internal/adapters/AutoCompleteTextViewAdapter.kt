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

import android.widget.AutoCompleteTextView
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.inflater.AttributeHandlerScope

/**
 * Attribute adapter for [AutoCompleteTextView].
 *
 * @author Akash Yadav
 */
@ViewAdapter(AutoCompleteTextView::class)
open class AutoCompleteTextViewAdapter<T : AutoCompleteTextView> : EditTextAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("completionHint") { view.completionHint = parseString(value) }
    create("completionThreshold") { view.threshold = parseInteger(value, 1) }
    create("dropDownAnchor") { view.dropDownAnchor = parseId(file.resName, value) }
    create("dropDownWidth") { view.dropDownWidth = parseDimension(context, value) }
    create("dropDownHeight") { view.dropDownHeight = parseDimension(context, value) }
  }
}
