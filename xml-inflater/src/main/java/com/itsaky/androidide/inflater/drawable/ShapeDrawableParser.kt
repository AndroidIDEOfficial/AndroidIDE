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
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.GradientDrawable.Orientation.BL_TR
import android.graphics.drawable.GradientDrawable.Orientation.BOTTOM_TOP
import android.graphics.drawable.GradientDrawable.Orientation.BR_TL
import android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT
import android.graphics.drawable.GradientDrawable.Orientation.RIGHT_LEFT
import android.graphics.drawable.GradientDrawable.Orientation.TL_BR
import android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM
import android.graphics.drawable.GradientDrawable.Orientation.TR_BL
import org.jetbrains.annotations.Contract
import org.xmlpull.v1.XmlPullParser
import java.util.Arrays

/**
 * Parses a drawable whose root element is `<shape>`. If the parse was successful, returns a
 * [GradientDrawable], otherwise `null`.
 *
 * @author Akash Yadav
 */
class ShapeDrawableParser protected constructor(parser: XmlPullParser?, minDepth: Int) : IDrawableParser(parser, minDepth) {
  @Throws(Exception::class)
  public override fun parseDrawable(context: Context): Drawable {
    val drawable = GradientDrawable()
    var index = attrIndex("shape")
    drawable.shape = if (index == -1) GradientDrawable.RECTANGLE else parseShape(parser!!.getAttributeValue(index))
    if (isApi29) {
      index = attrIndex("innerRadius")
      drawable.innerRadius = if (index == -1) 0 else parseDimension(context, value(index))
      index = attrIndex("innerRadiusRatio")
      drawable.innerRadiusRatio = if (index == -1) 1f else value(index).toFloat()
      index = attrIndex("thickness")
      drawable.thickness = if (index == -1) 0 else parseDimension(context, value(index))
      index = attrIndex("thicknessRatio")
      drawable.thicknessRatio = if (index == -1) 1f else value(index).toFloat()
    }
    index = attrIndex("useLevel")
    drawable.useLevel = index != -1 && parseBoolean(value(index))
    var event = parser!!.eventType
    while (event != XmlPullParser.END_DOCUMENT && canParse()) {
      val name = parser.name
      if (event == XmlPullParser.START_TAG) {
        when (name) {
          "corners" -> parseCorners(context, drawable)
          "gradient" -> parseGradient(context, drawable)
          "padding" -> parsePadding(context, drawable)
          "size" -> parseSize(context, drawable)
          "solid" -> parseSolid(context, drawable)
          "stroke" -> parseStroke(context, drawable)
        }
      }
      event = parser.next()
    }
    return drawable
  }
  
  private fun parseStroke(context: Context, drawable: GradientDrawable) {
    var index = attrIndex("width")
    var strokeWidth = 0
    if (index != -1) {
      drawable.setStroke(parseDimension(context, value(index)).also { strokeWidth = it }, Color.TRANSPARENT)
    }
    index = attrIndex("color")
    var strokeColor = Color.TRANSPARENT
    if (index != -1) {
      drawable.setStroke(strokeWidth, parseColor(context, value(index)).also { strokeColor = it })
    }
    index = attrIndex("dashWidth")
    var dashWidth = 0
    if (index != -1) {
      drawable.setStroke(
        strokeWidth, strokeColor, parseDimension(context, value(index)).also { dashWidth = it }.toFloat(), 0f)
    }
    index = attrIndex("dashGap")
    if (index != -1) {
      drawable.setStroke(
        strokeWidth, strokeColor, dashWidth.toFloat(), parseDimension(context, value(index)).toFloat())
    }
  }
  
  private fun parseSolid(context: Context, drawable: GradientDrawable) {
    val index = attrIndex("color")
    if (index != -1) {
      drawable.setColor(parseColor(context, value(index)))
    }
  }
  
  private fun parseSize(context: Context, drawable: GradientDrawable) {
    var index = attrIndex("width")
    if (index != -1) {
      drawable.setSize(parseDimension(context, value(index)), drawable.intrinsicHeight)
    }
    index = attrIndex("height")
    if (index != -1) {
      drawable.setSize(drawable.intrinsicWidth, parseDimension(context, value(index)))
    }
  }
  
  private fun parsePadding(context: Context, drawable: GradientDrawable) {
    
    // Padding is available from API 29 only
    if (!isApi29) {
      return
    }
    val rect = Rect()
    if (!drawable.getPadding(rect)) {
      rect[0, 0, 0] = 0
    }
    var changed = false
    var index = attrIndex("left")
    if (index != -1) {
      rect.left = parseDimension(context, value(index))
      changed = true
    }
    index = attrIndex("top")
    if (index != -1) {
      rect.top = parseDimension(context, value(index))
      changed = true
    }
    index = attrIndex("right")
    if (index != -1) {
      rect.right = parseDimension(context, value(index))
      changed = true
    }
    index = attrIndex("bottom")
    if (index != -1) {
      rect.bottom = parseDimension(context, value(index))
      changed = true
    }
    if (changed) {
      drawable.setPadding(rect.left, rect.top, rect.right, rect.bottom)
    }
  }
  
  private fun parseGradient(context: Context, drawable: GradientDrawable) {
    var index = attrIndex("angle")
    drawable.orientation = if (index == -1) LEFT_RIGHT else parseGradientOrientation(value(index))
    index = attrIndex("centerX")
    var center = if (index == -1) drawable.gradientCenterX else value(index).toFloat()
    drawable.setGradientCenter(center, drawable.gradientCenterY)
    index = attrIndex("centerY")
    center = if (index == -1) drawable.gradientCenterX else value(index).toFloat()
    drawable.setGradientCenter(drawable.gradientCenterX, center)
    val colors = IntArray(3)
    var changed = false
    Arrays.fill(colors, -1)
    index = attrIndex("centerColor")
    if (index != -1) {
      colors[1] = parseColor(context, value(index))
      changed = true
    }
    index = attrIndex("endColor")
    if (index != -1) {
      colors[2] = parseColor(context, value(index))
      changed = true
    }
    index = attrIndex("gradientRadius")
    drawable.gradientRadius = if (index == -1) drawable.gradientRadius else value(index).toFloat()
    index = attrIndex("startColor")
    if (index != -1) {
      colors[0] = parseColor(context, value(index))
      changed = true
    }
    if (changed) {
      drawable.colors = colors
    }
    index = attrIndex("type")
    drawable.gradientType = if (index == -1) GradientDrawable.LINEAR_GRADIENT else parseGradientType(value(index))
    index = attrIndex("useLevel")
    drawable.useLevel = index != -1 && parseBoolean(value(index))
  }
  
  private fun parseGradientType(value: String): Int {
    return when (value) {
      "radial" -> GradientDrawable.RADIAL_GRADIENT
      "sweep" -> GradientDrawable.SWEEP_GRADIENT
      "linear" -> GradientDrawable.LINEAR_GRADIENT
      else -> GradientDrawable.LINEAR_GRADIENT
    }
  }
  
  private fun parseGradientOrientation(value: String): Orientation {
    val angle = value.toInt()
    
    // Angle must be between 0-315 and a multiple of 45
    // Angle moves in anti-clockwise direction
    if (angle == 45) {
      return BL_TR
    }
    if (angle == 90) {
      return BOTTOM_TOP
    }
    if (angle == 135) {
      return BR_TL
    }
    if (angle == 180) {
      return RIGHT_LEFT
    }
    if (angle == 225) {
      return TR_BL
    }
    if (angle == 270) {
      return TOP_BOTTOM
    }
    return if (angle == 315) {
      TL_BR
    } else LEFT_RIGHT
    
    // Angle 0 or any invalid valid should make the orientation left to right
  }
  
  private fun parseCorners(context: Context, drawable: GradientDrawable) {
    var index = attrIndex("radius")
    drawable.cornerRadius = if (index == -1) 0f else parseDimension(context, value(index)).toFloat()
    var changed = false
    val radii = FloatArray(8)
    Arrays.fill(radii, 0f)
    index = attrIndex("topLeftRadius")
    if (index != -1) {
      radii[1] = parseDimension(context, value(index)).toFloat()
      radii[0] = radii[1]
      changed = true
    }
    index = attrIndex("topRightRadius")
    if (index != -1) {
      radii[3] = parseDimension(context, value(index)).toFloat()
      radii[2] = radii[3]
      changed = true
    }
    index = attrIndex("bottomLeftRadius")
    if (index != -1) {
      radii[5] = parseDimension(context, value(index)).toFloat()
      radii[4] = radii[5]
      changed = true
    }
    index = attrIndex("bottomRightRadius")
    if (index != -1) {
      radii[7] = parseDimension(context, value(index)).toFloat()
      radii[6] = radii[7]
      changed = true
    }
    if (changed) {
      drawable.cornerRadii = radii
    }
  }
  
  @Contract(pure = true)
  private fun parseShape(value: String): Int {
    return when (value) {
      "oval" -> GradientDrawable.OVAL
      "line" -> GradientDrawable.LINE
      "ring" -> GradientDrawable.RING
      "rectangle" -> GradientDrawable.RECTANGLE
      else -> GradientDrawable.RECTANGLE
    }
  }
}