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

package com.itsaky.androidide.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.itsaky.androidide.models.ProjectItem
import java.util.Locale
import kotlin.random.Random


fun extractAcronyms(name: String): String {
  val words = name.split(" ")

  if (words.size == 1) {
    return name.take(1).uppercase(Locale.ROOT)
  }

  return words.joinToString("") { it.take(1).uppercase(Locale.ROOT) }
}

fun getIcon(context: Context, name: String): Drawable {
  val acronym = extractAcronyms(name)

  val shape = ShapeAppearanceModel.builder()
  shape.setAllCorners(CornerFamily.ROUNDED, 30f)

  val width = 90
  val height = 90
  val iconDrawable = MaterialShapeDrawable(shape.build())
  iconDrawable.setBounds(0,0, height, width)
  iconDrawable.setTint(generateRandomColor())

  val textView = TextView(context)
  textView.text = acronym
  textView.setTextColor(Color.WHITE)
  textView.gravity = Gravity.CENTER

  val layers = arrayOf(iconDrawable, textViewToDrawable(textView))
  val layerDrawable = LayerDrawable(layers)

  return layerDrawable
}

private fun textViewToDrawable(textView: TextView): Drawable {
  textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
  val width = textView.measuredWidth
  val height = textView.measuredHeight
  val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  textView.layout(0, 0, width, height)
  textView.draw(canvas)

  return BitmapDrawable(textView.context.resources, bitmap)
}


/*
 fun ProjectItem.getIcon(context: Context): Drawable {

  val acronym = extractAcronyms()

  val textView = TextView(context)
  textView.text = acronym
  textView.setTextColor(Color.WHITE)
  textView.gravity = Gravity.CENTER
   val backgroundDrawable = ColorDrawable(Color.TRANSPARENT)
   textView.background = backgroundDrawable

  val outerRadius = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, 30f, context.resources.displayMetrics
  )
  val radii = floatArrayOf(
    outerRadius, outerRadius, outerRadius, outerRadius, outerRadius, outerRadius, outerRadius,
    outerRadius
  )
  val shapeDrawable = ShapeDrawable(RoundRectShape(radii, null, null))
  shapeDrawable.paint.color = generateRandomColor()
  shapeDrawable.intrinsicWidth = 100
  shapeDrawable.intrinsicHeight = 100

  val layers = arrayOf<Drawable>(shapeDrawable, textView.background)
  val layerDrawable = LayerDrawable(layers)
  layerDrawable.setLayerInset(1, 20, 20, 20, 20)

  return layerDrawable
}
*/

fun generateRandomColor(): Int {
  val random = Random.Default
  val minColorValue = 128

  val red = random.nextInt(minColorValue, 256)
  val green = random.nextInt(minColorValue, 256)
  val blue = random.nextInt(minColorValue, 256)

  val color = Color.rgb(red, green, blue)

  return color
}




