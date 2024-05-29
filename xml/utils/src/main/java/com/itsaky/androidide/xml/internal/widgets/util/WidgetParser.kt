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

package com.itsaky.androidide.xml.internal.widgets.util

import com.itsaky.androidide.xml.widgets.WidgetType

/**
 * Parses widget information from the string representation.
 *
 * @author Akash Yadav
 */
object WidgetParser {

  /**
   * Parses a [DefaultWidget] from the given string representation. Returns `null` if cannot parse.
   */
  fun parse(line: String): DefaultWidget? {
    val split = line.split(' ')
    if (split.size < 2) {
      return null
    }

    val type = WidgetType.Factory.forChar(split[0][0]) ?: return null
    val name = split[0].substring(1)
    val simpleName = name.substringAfterLast('.')
    val superclasses = split.subList(1, split.size)
    return DefaultWidget(simpleName, name, type, superclasses)
  }
}
