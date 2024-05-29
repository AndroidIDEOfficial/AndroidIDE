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

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat.getDrawable
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.uidesigner.drawable.UiViewLayeredForeground
import com.itsaky.androidide.utils.resolveAttr

fun layeredForeground(context: Context, drawable: Drawable): Drawable {
  return UiViewLayeredForeground(context, drawable)
}

@JvmOverloads
fun bgDesignerView(
  context: Context,
  color: Int = context.resolveAttr(R.attr.colorOutline)
): Drawable? {
  return getDrawable(context, R.drawable.bg_designer_view)?.apply {
    colorFilter = PorterDuffColorFilter(color, SRC_ATOP)
  }
}
