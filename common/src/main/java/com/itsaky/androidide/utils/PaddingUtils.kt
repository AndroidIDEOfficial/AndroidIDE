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

import android.view.View

enum class PaddingSide {
  LEFT,
  TOP,
  RIGHT,
  BOTTOM,
}

/**
 * Sets the specified padding on the specified side
 *
 * @return The initial padding value on the specified side
 * @author Smooth E
 */
fun View.setPadding(padding: Int, side: PaddingSide): Int {
  val initialPadding = when (side) {
    PaddingSide.LEFT -> paddingLeft
    PaddingSide.TOP -> paddingTop
    PaddingSide.RIGHT -> paddingRight
    PaddingSide.BOTTOM -> paddingBottom
  }

  setPadding(
    if (side == PaddingSide.LEFT) padding else paddingLeft,
    if (side == PaddingSide.TOP) padding else paddingTop,
    if (side == PaddingSide.RIGHT) padding else paddingLeft,
    if (side == PaddingSide.BOTTOM) padding else paddingBottom
  )

  return initialPadding
}

/**
 * Adds the specified amount of padding on the specified side
 *
 * @return The initial padding value on the specified side
 * @author Smooth E
 */
fun View.addPadding(padding: Int, side: PaddingSide): Int {
  val initialPadding = when (side) {
    PaddingSide.LEFT -> paddingLeft
    PaddingSide.TOP -> paddingTop
    PaddingSide.RIGHT -> paddingRight
    PaddingSide.BOTTOM -> paddingBottom
  }

  return setPadding(initialPadding + padding, side)
}
