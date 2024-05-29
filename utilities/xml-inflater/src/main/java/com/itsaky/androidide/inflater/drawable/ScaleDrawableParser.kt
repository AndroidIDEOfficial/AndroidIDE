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
package com.itsaky.androidide.inflater.drawable

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ScaleDrawable
import android.view.Gravity
import com.itsaky.androidide.inflater.InflateException
import org.xmlpull.v1.XmlPullParser

/**
 * Parser for parsing &lt;scale&gt; drawables.
 *
 * @author Akash Yadav
 */
open class ScaleDrawableParser protected constructor(parser: XmlPullParser?, minDepth: Int) :
  IDrawableParser(parser, minDepth) {
  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable {
    var index = attrIndex("drawable")
    if (index == -1) {
      throw InflateException("<scale> drawable must specify android:drawable attribute")
    }
    val v =
      value(index)
    val drawable =
      parseDrawable(context, v)
    var gravity = Gravity.LEFT
    index = attrIndex("scaleGravity")
    if (index != -1) {
      try {
        gravity = parseGravity(value(index))
      } catch (th: Throwable) {
        // ignored
      }
    }
    var scaleWidth = -1.0f
    var scaleHeight = -1.0f // DO_NOT_SCALE by default
    index = attrIndex("scaleWidth")
    if (index != -1) {
      try {
        scaleWidth = parseScale(value(index))
      } catch (th: Throwable) {
        // ignored
      }
    }
    index = attrIndex("scaleHeight")
    if (index != -1) {
      try {
        scaleHeight = parseScale(value(index))
      } catch (th: Throwable) {
        // ignored
      }
    }
    return ScaleDrawable(drawable, gravity, scaleWidth, scaleHeight)
  }

  private fun parseScale(value: String): Float {
    if (!value.endsWith("%")) {
      throw InflateException("Invalid scale value:$value")
    }
    val factor = value.substring(0, value.length - 1).toFloat() / 100
    if (factor < 0f || factor > 1f) {
      throw InflateException("Scale factor must be between 0% and 100%")
    }
    return factor
  }
}
