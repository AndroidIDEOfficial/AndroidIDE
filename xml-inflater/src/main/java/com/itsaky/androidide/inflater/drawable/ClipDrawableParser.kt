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
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import org.xmlpull.v1.XmlPullParser

/**
 * Parser for parsing &lt;clip&gt; drawables;
 *
 * @author Akash Yadav
 */
open class ClipDrawableParser protected constructor(parser: XmlPullParser, minDepth: Int) :
  IDrawableParser(parser, minDepth) {
  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable? {
    var index = attrIndex("drawable")
    if (index == -1) {
      // TODO Parse inner drawables
      return ClipDrawable(null, Gravity.LEFT, ClipDrawable.HORIZONTAL)
    }
    val value = value(index)
    val drawable = parseDrawable(context, value)
    var orientation = ClipDrawable.HORIZONTAL
    index = attrIndex("clipOrientation")
    if (index != -1) {
      orientation = parseClipOrientation(value(index))
    }
    var gravity = Gravity.LEFT
    index = attrIndex("gravity")
    if (index != -1) {
      gravity = parseGravity(value(index))
    }
    return ClipDrawable(drawable, gravity, orientation)
  }

  protected open fun parseClipOrientation(value: String): Int {
    return when (value) {
      "vertical" -> ClipDrawable.VERTICAL
      "horizontal" -> ClipDrawable.HORIZONTAL
      else -> ClipDrawable.HORIZONTAL
    }
  }
}
