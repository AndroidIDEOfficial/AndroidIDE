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

package com.itsaky.androidide.uidesigner.utils

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.content.ContextCompat.getDrawable
import com.itsaky.androidide.uidesigner.R.drawable

/**
 * Sets a foreground to the inflated views when they are touched.
 *
 * @author Akash Yadav
 */
class InflatedViewTouchListener : OnTouchListener {
  private var fg: Drawable? = null
  private var touched: Drawable? = null

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouch(v: View, event: MotionEvent): Boolean {
    return when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        this.fg = v.foreground
        this.touched = this.touched ?: getDrawable(v.context, drawable.bg_designer_view)
        v.foreground = this.touched
        true
      }
      MotionEvent.ACTION_UP -> {
        v.foreground = this.fg
        false
      }
      else -> false
    }
  }
}
