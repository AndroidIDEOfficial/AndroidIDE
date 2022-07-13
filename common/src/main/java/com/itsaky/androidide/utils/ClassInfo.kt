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

package com.itsaky.androidide.utils

import androidx.core.text.isDigitsOnly

/**
 * Information about a class in a JAR file.
 *
 * @author Akash Yadav
 */
data class ClassInfo(val name: String) {

  val isTopLevel: Boolean = name.indexOf('$') == -1

  val simpleName: String =
    if (!isTopLevel) {
      name.substringAfterLast('$')
    } else if (name.indexOf('.') != -1) {
      name.substringAfterLast('.')
    } else {
      name
    }

  val packageName: String =
    if (name.contains('.')) {
      name.substringBeforeLast('.')
    } else {
      name
    }

  val isAnonymous: Boolean = simpleName.isDigitsOnly()
  val isLocal: Boolean = simpleName[0].isDigit() && simpleName.contains(Regex("[A-Za-z]"))
  val isInner: Boolean = !isTopLevel && !isLocal && !isAnonymous
}
