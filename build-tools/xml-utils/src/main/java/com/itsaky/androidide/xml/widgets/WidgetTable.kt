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

package com.itsaky.androidide.xml.widgets

import com.itsaky.androidide.lookup.Lookup

/**
 * A widget table holds information about all widgets defined in the `widgets.txt`.
 *
 * @author Akash Yadav
 */
interface WidgetTable {

  companion object {
    @JvmStatic val COMPLETION_LOOKUP_KEY = Lookup.Key<WidgetTable>()
  }

  /**
   * Get the widget for the given fully qualified name. For example, `android.widget.TextView`.
   *
   * @return The widget or `null`.
   */
  fun getWidget(name: String): Widget?

  /**
   * Finds the first widget with the given simple name. This searches the whole table so [getWidget]
   * is preferable.
   */
  fun findWidgetWithSimpleName(name: String): Widget?

  /**
   * Get the set of all registered widgets.
   *
   * @return The set of widgets.
   */
  fun getAllWidgets(): Set<Widget>
}
