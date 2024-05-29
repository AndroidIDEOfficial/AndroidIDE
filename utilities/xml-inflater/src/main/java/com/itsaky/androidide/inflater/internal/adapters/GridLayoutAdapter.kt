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

import android.widget.GridLayout
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.LAYOUTS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string

/**
 * Attribute adapter for [GridLayout].
 *
 * @author Akash Yadav
 */
@ViewAdapter(GridLayout::class)
@IncludeInDesigner(group = LAYOUTS)
open class GridLayoutAdapter<T : GridLayout> : ViewGroupAdapter<T>() {

  override fun createAttrHandlers(
    create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit
  ) {
    super.createAttrHandlers(create)
    create("alignmentMode") { view.alignmentMode = parseAlignmentMode(value) }
    create("columnCount") {
      view.columnCount = parseInteger(value, Int.MIN_VALUE)
    }
    create("columnOrderPreserved") {
      view.isColumnOrderPreserved = parseBoolean(value)
    }
    create("orientation") { view.orientation = parseOrientation(value) }
    create("rowCount") { view.rowCount = parseInteger(value, Int.MIN_VALUE) }
    create("rowOrderPreserved") {
      view.isRowOrderPreserved = parseBoolean(value)
    }
    create("useDefaultMargins") { view.useDefaultMargins = parseBoolean(value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(UiWidget(GridLayout::class.java, string.widget_grid_layout,
      drawable.ic_widget_grid_layout))
  }

  override fun canAcceptChild(view: IViewGroup, child: IView?, name: String
  ): Boolean {
    (view.view as GridLayout).run {
      if (childCount >= rowCount * columnCount) {
        // the maximum number of child views has been reached
        // no more views can be added
        return false
      }
    }
    return super.canAcceptChild(view, child, name)
  }

  protected open fun parseOrientation(value: String): Int {
    return if ("vertical" == value) {
      GridLayout.VERTICAL
    } else GridLayout.HORIZONTAL
  }

  protected open fun parseAlignmentMode(value: String): Int {
    return if ("alignBounds" == value) {
      GridLayout.ALIGN_BOUNDS
    } else GridLayout.ALIGN_MARGINS
  }
}
