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
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string

/**
 * Attribute adapter for [AutoCompleteTextView].
 *
 * @author Akash Yadav
 */
@ViewAdapter(AutoCompleteTextView::class)
@IncludeInDesigner(group = WIDGETS)
open class AutoCompleteTextViewAdapter<T : AutoCompleteTextView> : EditTextAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("completionHint") { view.completionHint = parseString(value) }
    create("completionThreshold") { view.threshold = parseInteger(value, 1) }
    create("dropDownAnchor") { view.dropDownAnchor = parseId(file.resName, value) }
    create("dropDownWidth") { view.dropDownWidth = parseDimension(context, value) }
    create("dropDownHeight") { view.dropDownHeight = parseDimension(context, value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(
        AutoCompleteTextView::class.java,
        string.widget_auto_complete_textview,
        drawable.ic_widget_auto_complete_textview
      )
    )
  }
}
