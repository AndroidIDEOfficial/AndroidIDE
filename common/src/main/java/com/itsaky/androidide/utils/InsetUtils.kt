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

import android.graphics.Insets
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.WindowInsets

/**
 * Acquires screen insets
 *
 * @param view Any View that is currently attached to a Window
 *
 * @return [Rect] containing acquired insets
 *
 * @author Smooth E
 */
fun getInsets(view: View): Rect {
  val insets: Rect
  val rootWindowInsets = view.rootWindowInsets

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

    val receivedInsets: Insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      val typeMask = WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout()
      rootWindowInsets.getInsetsIgnoringVisibility(typeMask)
    } else {
      view.rootWindowInsets.stableInsets
    }

    insets = Rect(
      receivedInsets.left,
      receivedInsets.top,
      receivedInsets.right,
      receivedInsets.bottom
    )
  } else {
    insets = Rect(
      rootWindowInsets.stableInsetLeft,
      rootWindowInsets.stableInsetTop,
      rootWindowInsets.stableInsetRight,
      rootWindowInsets.stableInsetBottom
    )
  }

  return insets
}
