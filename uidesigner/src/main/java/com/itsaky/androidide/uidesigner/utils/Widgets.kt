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

package com.itsaky.androidide.uidesigner.utils

import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.LAYOUTS
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.IViewAdapterIndex
import com.itsaky.androidide.inflater.internal.utils.simpleName
import com.itsaky.androidide.uidesigner.R.string
import com.itsaky.androidide.uidesigner.models.UiWidgetCategory

internal object Widgets {

  @JvmField val CATEGORY_WIDGETS = UiWidgetCategory(string.ui_category_widgets)
  @JvmField val CATEGORY_LAYOUTS = UiWidgetCategory(string.ui_category_layouts)

  private val internalCategories = mutableListOf<UiWidgetCategory>()

  val categories: List<UiWidgetCategory>
    get() = this.internalCategories

  init {
    internalCategories.add(getLayouts())
    internalCategories.add(getWidgets())
  }

  private fun getWidgets(): UiWidgetCategory {
    return CATEGORY_WIDGETS.apply {
      if (widgets.isNotEmpty()) {
        return@apply
      }
      this.widgets =
        IViewAdapterIndex.instance.getWidgetProviders(WIDGETS)
          ?.flatMap { it.getUiWidgets() }
          ?.sortedBy { it.name.simpleName() } ?: emptyList()
    }
  }

  private fun getLayouts(): UiWidgetCategory {
    return CATEGORY_LAYOUTS.apply {
      if (widgets.isNotEmpty()) {
        return@apply
      }

      this.widgets =
        IViewAdapterIndex.instance.getWidgetProviders(LAYOUTS)
          ?.flatMap { it.getUiWidgets() }
          ?.sortedBy { it.name.simpleName() } ?: emptyList()
    }
  }
}
