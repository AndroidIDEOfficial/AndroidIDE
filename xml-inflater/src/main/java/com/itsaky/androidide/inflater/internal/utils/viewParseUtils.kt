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

import android.graphics.PorterDuff
import android.view.View

@JvmOverloads
fun parseId(resName: String, value: String, def: Int = 0): Int {
  val name = parseResourceReference(value)?.third ?: return 0
  return IDTable.get(resName, name) ?: def
}

@Suppress("DEPRECATION")
fun parseDrawingCacheQuality(value: String): Int {
  return when (value) {
    "high" -> View.DRAWING_CACHE_QUALITY_HIGH
    "low" -> View.DRAWING_CACHE_QUALITY_LOW
    "auto" -> View.DRAWING_CACHE_QUALITY_AUTO
    else -> View.DRAWING_CACHE_QUALITY_AUTO
  }
}

fun parseVisibility(value: String): Int {
  return when (value) {
    "gone" -> View.GONE
    "invisible" -> View.INVISIBLE
    "visible" -> View.VISIBLE
    else -> View.VISIBLE
  }
}

fun parseTextAlignment(value: String): Int {
  return when (value) {
    "center" -> View.TEXT_ALIGNMENT_CENTER
    "gravity" -> View.TEXT_ALIGNMENT_GRAVITY
    "textEnd" -> View.TEXT_ALIGNMENT_TEXT_END
    "textStart" -> View.TEXT_ALIGNMENT_TEXT_START
    "viewEnd" -> View.TEXT_ALIGNMENT_VIEW_END
    "viewStart" -> View.TEXT_ALIGNMENT_VIEW_START
    "inherit" -> View.TEXT_ALIGNMENT_INHERIT
    else -> View.TEXT_ALIGNMENT_INHERIT
  }
}

fun parseTextDirection(value: String): Int {
  return when (value) {
    "anyRtl" -> View.TEXT_DIRECTION_ANY_RTL
    "firstStrong" -> View.TEXT_DIRECTION_FIRST_STRONG
    "firstStrongLtr" -> View.TEXT_DIRECTION_FIRST_STRONG_LTR
    "firstStrongRtl" -> View.TEXT_DIRECTION_FIRST_STRONG_RTL
    "locale" -> View.TEXT_DIRECTION_LOCALE
    "ltr" -> View.TEXT_DIRECTION_LTR
    "rtl" -> View.TEXT_DIRECTION_RTL
    "inherit" -> View.TEXT_DIRECTION_INHERIT
    else -> View.TEXT_DIRECTION_INHERIT
  }
}

fun parsePorterDuffMode(mode: String): PorterDuff.Mode {
  return when (mode) {
    "add" -> PorterDuff.Mode.ADD
    "multiply" -> PorterDuff.Mode.MULTIPLY
    "screen" -> PorterDuff.Mode.SCREEN
    "src_atop" -> PorterDuff.Mode.SRC_ATOP
    "src_in" -> PorterDuff.Mode.SRC_IN
    "src_over" -> PorterDuff.Mode.SRC_OVER
    else -> PorterDuff.Mode.SRC
  }
}
