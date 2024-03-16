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
import android.os.Looper
import android.widget.ImageView.ScaleType
import android.widget.ImageView.ScaleType.FIT_CENTER
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.flashbar.Flashbar
import com.itsaky.androidide.flashbar.Flashbar.Gravity.TOP
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.FlashType.ERROR
import com.itsaky.androidide.utils.FlashType.INFO
import com.itsaky.androidide.utils.FlashType.SUCCESS

const val DURATION_SHORT = 2000L
const val DURATION_LONG = 3500L
const val DURATION_INDEFINITE = Flashbar.DURATION_INDEFINITE

val COLOR_SUCCESS = Color.parseColor("#4CAF50")
val COLOR_ERROR = Color.parseColor("#f44336")
const val COLOR_INFO = Color.DKGRAY

@JvmOverloads
fun Activity.flashbarBuilder(
  gravity: Flashbar.Gravity = TOP,
  duration: Long = DURATION_SHORT,
  backgroundColor: Int = resolveAttr(R.attr.colorPrimaryContainer),
  messageColor: Int = resolveAttr(R.attr.colorOnPrimaryContainer)
): Flashbar.Builder {
  return Flashbar.Builder(this)
    .gravity(gravity)
    .duration(duration)
    .backgroundColor(backgroundColor)
    .messageColor(messageColor)
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
  flashbarBuilder().successIcon().message(msg).showOnUiThread()
}

fun Activity.flashError(msg: String?) {
  msg ?: return
  flashbarBuilder().errorIcon().message(msg).showOnUiThread()
}

fun Activity.flashInfo(msg: String?) {
  msg ?: return
  flashbarBuilder().infoIcon().message(msg).showOnUiThread()
}

fun Activity.flashSuccess(@StringRes msg: Int) {
  flashbarBuilder().successIcon().message(msg).showOnUiThread()
}

fun Activity.flashError(@StringRes msg: Int) {
  flashbarBuilder().errorIcon().message(msg).showOnUiThread()
}

fun Activity.flashInfo(@StringRes msg: Int) {
  flashbarBuilder().infoIcon().message(msg).showOnUiThread()
}

@JvmOverloads
fun <R : Any?> Activity.flashProgress(
  configure: (Flashbar.Builder.() -> Unit)? = null,
  action: (Flashbar) -> R
) : R {
  val builder = flashbarBuilder(gravity = TOP, duration = DURATION_INDEFINITE)
    .showProgress(Flashbar.ProgressPosition.LEFT)

  configure?.invoke(builder)

  val flashbar = builder.build()
  flashbar.show()

  return action(flashbar)
}

fun Flashbar.Builder.showOnUiThread() {
  build().showOnUiThread()
}

fun Flashbar.showOnUiThread() {
  if (Looper.myLooper() == Looper.getMainLooper()) {
    show()
  } else {
    ThreadUtils.runOnUiThread { show() }
  }
}

fun Flashbar.Builder.successIcon(): Flashbar.Builder {
  return withIcon(R.drawable.ic_ok, colorFilter = COLOR_SUCCESS)
}

fun Flashbar.Builder.errorIcon(): Flashbar.Builder {
  return withIcon(R.drawable.ic_error, colorFilter = COLOR_ERROR)
}

fun Flashbar.Builder.infoIcon(): Flashbar.Builder {
  return withIcon(R.drawable.ic_info, colorFilter = COLOR_INFO)
}

fun Flashbar.Builder.withIcon(
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
