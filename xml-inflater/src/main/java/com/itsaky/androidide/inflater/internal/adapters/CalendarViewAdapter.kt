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

import android.widget.CalendarView
import com.itsaky.androidide.annotations.inflater.ViewAdapter
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner
import com.itsaky.androidide.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string

/**
 * Attribute adapter for [CalendarView].
 *
 * @author Deep Kr. Ghosh
 */
@ViewAdapter(CalendarView::class)
@IncludeInDesigner(group = WIDGETS)
open class CalendarViewAdapter<T : CalendarView> : FrameLayoutAdapter<T>() {

  @Suppress("DEPRECATION")
  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    // TODO (theme): create("dateTextAppearance")
    create("firstDayOfWeek") { view.firstDayOfWeek = parseInteger(value) }
    create("focusedMonthDateColor") { view.focusedMonthDateColor = parseColor(context, value) }
    create("maxDate") { view.maxDate = parseDate(value) }
    create("minDate") { view.minDate = parseDate(value) }
    // TODO (theme): create("selectedDateVerticalBar")
    create("selectedWeekBackgroundColor") {
      view.selectedWeekBackgroundColor = parseColor(context, value)
    }
    create("showWeekNumber") { view.showWeekNumber = parseBoolean(value) }
    create("shownWeekCount") { view.shownWeekCount = parseInteger(value) }
    create("unfocusedMonthDateColor") { view.unfocusedMonthDateColor = parseColor(context, value) }
    // TODO (theme): create("weekDayTextAppearance")
    create("weekNumberColor") { view.weekNumberColor = parseColor(context, value) }
    create("weekSeparatorLineColor") { view.weekSeparatorLineColor = parseColor(context, value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(
        CalendarView::class.java,
        string.widget_calendar_view,
        drawable.ic_widget_calendar_view
      )
    )
  }
}
