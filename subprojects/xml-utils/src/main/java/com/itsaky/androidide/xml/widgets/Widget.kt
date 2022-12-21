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

/**
 * A widget defined in the `widgets.txt` file in the Android SDK.
 *
 * @author Akash Yadav
 */
interface Widget {
  /** The simple name of the widget. For example, `TextView`. */
  val simpleName: String

  /** The qualified name of the widget. For example, `android.widget.TextView`. */
  val qualifiedName: String

  /** The type of widget. */
  val type: WidgetType

  /** The fully qualified names of the superclasses of this widget. */
  val superclasses: List<String>
}
