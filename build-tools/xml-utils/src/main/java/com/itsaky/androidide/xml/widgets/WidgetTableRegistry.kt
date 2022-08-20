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

import com.itsaky.androidide.xml.widgets.internal.DefaultWidgetTableRegistry
import java.io.File

/**
 * Information about widgets, layouts and layout params extracted from `widgets.txt` from the
 * Android SDK.
 *
 * @author Akash Yadav
 */
interface WidgetTableRegistry {

  companion object {

    /** Get the default instance of [WidgetTableRegistry]. */
    @JvmStatic fun getInstance(): WidgetTableRegistry = DefaultWidgetTableRegistry
  }

  /**
   * Get the widget info table for the given platform directory.
   *
   * @param platformDir The `platform-API` directory in the Android SDK.
   * @return The widget information table for the given platform or `null`.
   */
  fun forPlatform(platformDir: File): WidgetTable?

  /** Clear all cached widget info tables. */
  fun clear()
}
