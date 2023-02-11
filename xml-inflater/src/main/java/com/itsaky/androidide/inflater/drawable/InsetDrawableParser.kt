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
import android.graphics.drawable.InsetDrawable
import com.itsaky.androidide.inflater.InflateException
import org.xmlpull.v1.XmlPullParser

/**
 * Parser for &lt;inset&gt; drawables.
 *
 * @author Akash Yadav
 */
open class InsetDrawableParser protected constructor(parser: XmlPullParser?, minDepth: Int) :
  IDrawableParser(parser, minDepth) {
  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable {
    var index = attrIndex("drawable")
    if (index == -1) {
      throw InflateException("No drawable specified for <inset> drawable")
    }
    val value = value(index)
    val drawable = parseDrawable(context, value)
    index = attrIndex("insetLeft")
    val left = if (index == -1) 0 else parseDimension(context, value(index))
    index = attrIndex("insetTop")
    val top = if (index == -1) 0 else parseDimension(context, value(index))
    index = attrIndex("insetRight")
    val right = if (index == -1) 0 else parseDimension(context, value(index))
    index = attrIndex("insetBottom")
    val bottom = if (index == -1) 0 else parseDimension(context, value(index))
    return InsetDrawable(drawable, left, top, right, bottom)
  }
}
