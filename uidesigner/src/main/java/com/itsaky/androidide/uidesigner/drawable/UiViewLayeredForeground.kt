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

package com.itsaky.androidide.uidesigner.drawable

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.uidesigner.utils.bgDesignerView

/**
 * Marker class to be able to differentiate between normal foregrounds and already layered
 * foregrounds.
 *
 * @author Akash Yadav
 */
class UiViewLayeredForeground(context: Context, val src: Drawable) : LayerDrawable(emptyArray())
{
  init {
    val dp1 = SizeUtils.dp2px(1f)
    val index = addLayer(src)
    setLayerInsetRelative(index, dp1, dp1, dp1, dp1)
    bgDesignerView(context)?.let { addLayer(it) }
  }
}
