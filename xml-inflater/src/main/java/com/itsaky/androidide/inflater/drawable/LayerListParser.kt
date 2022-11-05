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
import android.graphics.drawable.LayerDrawable
import com.itsaky.androidide.inflater.internal.utils.TextDrawable
import org.jetbrains.annotations.Contract
import org.xmlpull.v1.XmlPullParser

/**
 * Parser for parsing &lt;layer-list&gt; drawables.
 *
 * @author Akash Yadav
 */
open class LayerListParser protected constructor(parser: XmlPullParser?, minDepth: Int) :
  IDrawableParser(parser, minDepth) {
  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable {
    val layer = LayerDrawable(arrayOfNulls(0))
    var index: Int
    var value: String?
    var event = parser!!.eventType
    var left = 0
    var top = 0
    var right = 0
    var bottom = 0
    var parseInner = true
    while (event != XmlPullParser.END_DOCUMENT && canParse()) {
      if (event == XmlPullParser.START_TAG) {
        val name = parser!!.name
        if ("item" == name) {
          index = attrIndex("drawable")
          var drawable: Drawable? = null
          if (index != -1) {
            value = value(index)
            drawable = parseDrawable(context, value)

            // if the android:drawable is specified, we do not parse the inner elements
            // of <item>
            parseInner = false
          }
          index = attrIndex("left")
          left = if (index == -1) 0 else parseDimension(context, value(index))
          index = attrIndex("top")
          top = if (index == -1) 0 else parseDimension(context, value(index))
          index = attrIndex("right")
          right = if (index == -1) 0 else parseDimension(context, value(index))
          index = attrIndex("bottom")
          bottom = if (index == -1) 0 else parseDimension(context, value(index))
          if (drawable == null) {
            parseInner = true
          } else {
            addToLayer(layer, drawable, left, top, right, bottom)
          }
          if (!parseInner) {
            skipToEndTag(parser!!.depth)
          }
        } else if (parser!!.depth > 2 && parseInner) {
          // depth 1 = <layer-list>
          // depth 2 = <item>
          // depth > 2 = child drawables inside <item> tag
          val parser = DrawableParserFactory.parserForTag(null, parser, name)
          if (parser != null) {
            parser.minDepth = this.parser!!.depth
            var d = parser.parse(context)
            if (d == null) {
              d = unsupported(context)
            }
            addToLayer(layer, d, left, top, right, bottom)
          } else {
            addToLayer(layer, unsupported(context), left, top, right, bottom)
          }
        }
      }
      event = parser!!.next()
    }
    return layer
  }

  private fun addToLayer(
    layer: LayerDrawable,
    drawable: Drawable,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
  ) {
    val index = layer.addLayer(drawable)
    layer.setLayerInset(index, left, top, right, bottom)
  }

  @Throws(Exception::class)
  private fun skipToEndTag(depth: Int) {
    var event = parser!!.eventType
    while (event != XmlPullParser.END_DOCUMENT) {
      if (event == XmlPullParser.END_TAG && depth == parser!!.depth) {
        break
      }
      event = parser!!.next()
    }
  }

  @Contract(" -> new")
  private fun unsupported(context: Context): Drawable {
    return TextDrawable("Unsupported drawable", context.resources.displayMetrics)
  }
}
