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

import android.widget.MultiAutoCompleteTextView
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.inflater.models.UiWidget

/**
 * View adapter for [MultiAutoCompleteTextView].
 *
 * @author Akash Yadav
 */
@com.itsaky.androidide.annotations.inflater.ViewAdapter(forView = MultiAutoCompleteTextView::class)
@IncludeInDesigner(group = WIDGETS)
open class MultiAutoCompleteTextViewAdapter<T : MultiAutoCompleteTextView> :
  AutoCompleteTextViewAdapter<T>() {

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(
        MultiAutoCompleteTextView::class.java,
        R.string.widget_multi_auto_complete_textview,
        R.drawable.ic_widget_multi_auto_complete_textview
      )
    )
  }
}
