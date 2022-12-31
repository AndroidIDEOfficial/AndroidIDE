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
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Shader.TileMode
import android.graphics.Shader.TileMode.CLAMP
import android.graphics.Shader.TileMode.MIRROR
import android.graphics.Shader.TileMode.REPEAT
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.itsaky.androidide.inflater.InflateException
import org.jetbrains.annotations.Contract
import org.xmlpull.v1.XmlPullParser

/**
 * Parser for parsing &lt;bitmap&gt; drawables.
 *
 * @author Akash Yadav
 */
class BitmapDrawableParser protected constructor(parser: XmlPullParser, minDepth: Int) : IDrawableParser(parser, minDepth) {
  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable {
    var index = attrIndex("src")
    if (index == -1) {
      throw InflateException("Invalid <bitmap> drawable. No android:src specified!")
    }
    val `val` = value(index)
    val dr = parseDrawable(context, `val`)
    val bitmap = BitmapDrawable(context.resources, toBitmap(dr))
    index = attrIndex("antialias")
    if (index != -1) {
      bitmap.setAntiAlias(parseBoolean(value(index)))
    }
    index = attrIndex("dither")
    if (index != -1) {
      bitmap.setDither(parseBoolean(value(index)))
    }
    index = attrIndex("filter")
    if (index != -1) {
      bitmap.isFilterBitmap = parseBoolean(value(index))
    }
    index = attrIndex("gravity")
    if (index != -1) {
      bitmap.gravity = parseGravity(value(index))
    }
    index = attrIndex("mipMap")
    if (index != -1) {
      bitmap.setMipMap(parseBoolean(value(index)))
    }
    index = attrIndex("tileMode")
    if (index != -1) {
      val mode = parseTileMode(value(index))
      bitmap.setTileModeXY(mode, mode)
    }
    index = attrIndex("tileModeX")
    if (index != -1) {
      val mode = parseTileMode(value(index))
      bitmap.tileModeX = mode
    }
    index = attrIndex("tileModeY")
    if (index != -1) {
      val mode = parseTileMode(value(index))
      bitmap.tileModeY = mode
    }
    return bitmap
  }
  
  @Contract(pure = true)
  private fun parseTileMode(value: String): TileMode? {
    return when (value) {
      "clamp" -> CLAMP
      "mirror" -> MIRROR
      "repeat" -> REPEAT
      "disabled" -> null // null = disabled
      else -> null
    }
  }
  
  private fun toBitmap(dr: Drawable): Bitmap {
    if (dr is BitmapDrawable) {
      val bit = dr
      if (bit.bitmap != null) {
        return bit.bitmap
      }
    }
    val bit: Bitmap
    bit = if (dr.intrinsicWidth > 0 && dr.intrinsicHeight > 0) {
      Bitmap.createBitmap(
        dr.intrinsicWidth, dr.intrinsicHeight, ARGB_8888)
    } else {
      Bitmap.createBitmap(1, 1, ARGB_8888)
    }
    val canvas = Canvas(bit)
    dr.setBounds(0, 0, canvas.width, canvas.height)
    dr.draw(canvas)
    return bit
  }
}