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

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.widget.ImageView.ScaleType
import android.widget.ImageView.ScaleType.FIT_CENTER
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import com.itsaky.androidide.flashbar.Flashbar
import com.itsaky.androidide.flashbar.Flashbar.Gravity.TOP
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.FlashType.ERROR
import com.itsaky.androidide.utils.FlashType.INFO
import com.itsaky.androidide.utils.FlashType.SUCCESS

const val LENGTH_SHORT = 2000L
const val LENGTH_LONG = 3500L

private val COLOR_SUCCESS = Color.parseColor("#4CAF50")
private val COLOR_ERROR = Color.parseColor("#f44336")
private const val COLOR_INFO = Color.DKGRAY

fun Activity.flashbarBuilder(duration: Long = LENGTH_SHORT): Flashbar.Builder {
  return Flashbar.Builder(this)
    .gravity(TOP)
    .duration(duration)
    .backgroundColor(resolveAttr(R.attr.colorPrimaryContainer))
    .messageColor(resolveAttr(R.attr.colorOnPrimaryContainer))
}

fun Activity.flashMessage(msg: String?, type: FlashType) {
  msg ?: return
  when (type) {
    ERROR -> flashError(msg)
    INFO -> flashInfo(msg)
    SUCCESS -> flashSuccess(msg)
  }
}

fun Activity.flashMessage(@StringRes msg: Int, type: FlashType) {
  when (type) {
    ERROR -> flashError(msg)
    INFO -> flashInfo(msg)
    SUCCESS -> flashSuccess(msg)
  }
}

fun Activity.flashSuccess(msg: String?) {
  msg ?: return
  flashbarBuilder().successIcon().message(msg).show()
}

fun Activity.flashError(msg: String?) {
  msg ?: return
  flashbarBuilder().errorIcon().message(msg).show()
}

fun Activity.flashInfo(msg: String?) {
  msg ?: return
  flashbarBuilder().infoIcon().message(msg).show()
}

fun Activity.flashSuccess(@StringRes msg: Int) {
  flashbarBuilder().successIcon().message(msg).show()
}

fun Activity.flashError(@StringRes msg: Int) {
  flashbarBuilder().errorIcon().message(msg).show()
}

fun Activity.flashInfo(@StringRes msg: Int) {
  flashbarBuilder().infoIcon().message(msg).show()
}

private fun Flashbar.Builder.successIcon(): Flashbar.Builder {
  return withIcon(R.drawable.ic_ok, colorFilter = COLOR_SUCCESS)
}

private fun Flashbar.Builder.errorIcon(): Flashbar.Builder {
  return withIcon(R.drawable.ic_error, colorFilter = COLOR_ERROR)
}

private fun Flashbar.Builder.infoIcon(): Flashbar.Builder {
  return withIcon(R.drawable.ic_info, colorFilter = COLOR_INFO)
}

private fun Flashbar.Builder.withIcon(
  @DrawableRes icon: Int,
  @FloatRange(from = 0.0, to = 1.0) scale: Float = 1.0f,
  @ColorInt colorFilter: Int = -1,
  colorFilterMode: PorterDuff.Mode = SRC_ATOP,
  scaleType: ScaleType = FIT_CENTER
): Flashbar.Builder {
  return showIcon(scale = scale, scaleType = scaleType).icon(icon).also {
    if (colorFilter != -1) {
      iconColorFilter(colorFilter, colorFilterMode)
    }
  }
}