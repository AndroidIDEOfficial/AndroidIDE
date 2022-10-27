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
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop

/**
 * A helper method wrapping [ViewCompat.setOnApplyWindowInsetsListener], additionally providing the
 * initial padding and margins of the view.
 *
 * This method does not consume any window insets, allowing any and all children to receive the same
 * insets.
 *
 * This is a `set` listener, so only the last [windowInsetsListener] applied by
 * [doOnApplyWindowInsets] will be ran.
 */
fun View.doOnApplyWindowInsets(
  windowInsetsListener:
    (
      insetView: View,
      windowInsets: WindowInsetsCompat,
      initialPadding: Insets,
      initialMargins: Insets
    ) -> Unit
) {
  val initialPadding = Insets.of(paddingStart, paddingTop, paddingEnd, paddingBottom)
  val initialMargins = Insets.of(marginStart, marginTop, marginEnd, marginBottom)

  ViewCompat.setOnApplyWindowInsetsListener(this) { insetView, windowInsets ->
    windowInsets.also {
      windowInsetsListener(insetView, windowInsets, initialPadding, initialMargins)
    }
  }

  addOnAttachStateChangeListener(
    object : View.OnAttachStateChangeListener {
      override fun onViewAttachedToWindow(v: View) {
        v.requestApplyInsets()
      }

      override fun onViewDetachedFromWindow(v: View) = Unit
    }
  )

  // If the view is already attached, immediately request insets be applied.
  if (isAttachedToWindow) {
    requestApplyInsets()
  }
}
