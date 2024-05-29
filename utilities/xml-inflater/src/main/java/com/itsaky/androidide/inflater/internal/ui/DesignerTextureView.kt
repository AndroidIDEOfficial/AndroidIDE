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

package com.itsaky.androidide.inflater.internal.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.TextureView
import androidx.annotation.StyleRes

/**
 * Custom [TextureView] that is used in the XML Layout Inflater. This implementation does not throw
 * an [UnsupportedOperationException] when [setForeground] or [setBackgroundDrawable] is called.
 *
 * @author Akash Yadav
 */
class DesignerTextureView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
  @StyleRes defStyleAttr: Int = 0, @StyleRes defStyleRes: Int = 0) :
  TextureView(context, attrs, defStyleAttr, defStyleRes) {

  override fun setForeground(foreground: Drawable?) {
    // ignore
  }

  @Deprecated("Deprecated in Java")
  override fun setBackgroundDrawable(background: Drawable?) {
    // ignore
  }
}