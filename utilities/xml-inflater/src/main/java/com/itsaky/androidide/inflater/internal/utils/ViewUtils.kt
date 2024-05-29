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

package com.itsaky.androidide.inflater.internal.utils

import com.android.SdkConstants.ANDROID_PKG

// android.$it.$widget
val androidPackages = listOf("view", "widget", "gesture")

fun String.simpleName(): String {
  return if (contains('.')) {
    substringAfterLast(delimiter = '.')
  } else this
}

fun String.tagName(): String {
  val split = split('.')
  if (split.size == 3) {
    if (ANDROID_PKG == split[0] && androidPackages.find { it == split[1] } != null) {
      return split[2]
    }
  }
  return this
}
