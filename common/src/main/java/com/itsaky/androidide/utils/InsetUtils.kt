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

import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat

/**
 * Acquires screen insets.
 *
 * @param view Any View that is currently attached to a Window.
 * @return [Insets] containing acquired insets.
 * @author Smooth E
 */
fun getSystemBarInsets(view: View): Insets {
  val rootWindowInsets = view.rootWindowInsets
  return getSystemBarInsets(WindowInsetsCompat.toWindowInsetsCompat(rootWindowInsets))
}

/**
 * Acquires system bar and display cutout insets.
 *
 * @param insets The window insets.
 * @return [Insets] containing acquired insets.
 */
fun getSystemBarInsets(insets: WindowInsetsCompat): Insets {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
    @Suppress("DEPRECATION")
    return insets.stableInsets
  }

  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
    @Suppress("DEPRECATION")
    return insets.stableInsets
  }

  val typeMask = WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout()

  // noinspection WrongConstant
  return insets.getInsetsIgnoringVisibility(typeMask)
}
