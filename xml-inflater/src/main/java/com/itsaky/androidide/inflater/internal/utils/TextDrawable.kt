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

package com.itsaky.androidide.inflater.internal.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Paint.Align.CENTER
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.TypedValue

class TextDrawable(private val mText: CharSequence, displayMetrics: DisplayMetrics) : Drawable() {
  private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val mIntrinsicWidth: Int
  private val mIntrinsicHeight: Int
  
  init {
    mPaint.color = DEFAULT_COLOR
    mPaint.textAlign = CENTER
    val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE.toFloat(), displayMetrics)
    mPaint.textSize = textSize
    mIntrinsicWidth = (mPaint.measureText(mText, 0, mText.length) + .5).toInt()
    mIntrinsicHeight = mPaint.getFontMetricsInt(null)
  }
  
  override fun draw(canvas: Canvas) {
    val bounds = bounds
    canvas.drawText(mText, 0, mText.length, bounds.centerX().toFloat(), bounds.centerY().toFloat(), mPaint)
  }
  
  override fun setAlpha(alpha: Int) {
    mPaint.alpha = alpha
  }
  
  override fun setColorFilter(filter: ColorFilter?) {
    mPaint.colorFilter = filter
  }
  
  @Deprecated("Deprecated in Java")
  override fun getOpacity(): Int {
    return mPaint.alpha
  }
  
  override fun getIntrinsicWidth(): Int {
    return mIntrinsicWidth
  }
  
  override fun getIntrinsicHeight(): Int {
    return mIntrinsicHeight
  }
  
  companion object {
    private const val DEFAULT_COLOR = Color.WHITE
    private const val DEFAULT_TEXT_SIZE = 15
  }
}