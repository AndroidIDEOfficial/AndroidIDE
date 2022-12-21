/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.toaster

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.create
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.app.BaseApplication.getBaseInstance
import com.itsaky.androidide.resources.R.color
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.common.databinding.LayoutToastBinding
import com.itsaky.toaster.Toaster.Gravity.BOTTOM_LEFT
import com.itsaky.toaster.Toaster.Gravity.BOTTOM_RIGHT
import com.itsaky.toaster.Toaster.Gravity.TOP_LEFT
import com.itsaky.toaster.Toaster.Gravity.TOP_RIGHT
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.Toaster.Type.INFO
import com.itsaky.toaster.Toaster.Type.SUCCESS

class Toaster internal constructor(context: Context) {

  enum class Type {
    ERROR,
    SUCCESS,
    INFO
  }

  enum class Gravity {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT
  }

  companion object {
    val COLOR_SUCCESS = Color.parseColor("#4CAF50")
    val COLOR_ERROR = Color.parseColor("#F44336")
    const val REVEAL_ANIM_DURATION = 500
    const val SHORT = 2000
    const val LONG = 3500
  }

  internal val backgroundDrawable: Drawable = createBackgroundDrawable(context)
  private var binding: LayoutToastBinding? = null

  var gravity: Gravity? = null
    private set
  var type: Type = INFO
    private set
  var icon: AnimatedVectorDrawableCompat? = null
    private set
  var iconColor = Color.DKGRAY
    private set
  var duration = 0
    private set

  init {
    createView(context)
  }

  private fun createBackgroundDrawable(context: Context): Drawable {
    val drawable = GradientDrawable()
    drawable.shape = GradientDrawable.RECTANGLE
    drawable.setColor(ContextCompat.getColor(context, color.color_toast_background))
    drawable.cornerRadius = 25f
    return drawable
  }

  private fun createView(context: Context) {
    binding = LayoutToastBinding.inflate(LayoutInflater.from(context))
  }

  fun show() {
    ThreadUtils.runOnUiThread(this::showInternal)
  }

  fun setText(text: String?): Toaster {
    binding!!.toastText.text = text
    return this
  }

  fun setText(textResId: Int): Toaster {
    binding!!.toastText.setText(textResId)
    return this
  }

  fun setDuration(duration: Int): Toaster {
    this.duration = duration + REVEAL_ANIM_DURATION
    return this
  }

  fun setType(type: Type): Toaster {
    this.type = type
    return this
  }

  fun setGravity(gravity: Gravity?): Toaster {
    this.gravity = gravity
    return this
  }

  private fun showInternal() {
    val dp16 = SizeUtils.dp2px(16f)
    iconColor = getIconColor()
    icon = createToastIconAnimation(iconColor)
    val gravity = getGravity()
    val mToast = Toast.makeText(getBaseInstance(), binding!!.toastText.text, duration)
    mToast.setGravity(gravity, dp16, dp16)
    mToast.duration = duration

    @Suppress("DEPRECATION")
    mToast.view = binding!!.root

    mToast.show()
    binding!!.toastImage.setImageDrawable(icon)
    icon!!.start()
  }

  @JvmName("getIconColorInternal")
  private fun getIconColor(): Int {
    return when (type) {
      SUCCESS -> COLOR_SUCCESS
      ERROR -> COLOR_ERROR
      else -> Color.DKGRAY
    }
  }

  private fun getGravity(): Int {
    return when (gravity) {
      TOP_LEFT -> android.view.Gravity.TOP or android.view.Gravity.START
      TOP_RIGHT -> android.view.Gravity.TOP or android.view.Gravity.END
      BOTTOM_RIGHT -> android.view.Gravity.BOTTOM or android.view.Gravity.END
      BOTTOM_LEFT -> android.view.Gravity.BOTTOM or android.view.Gravity.START
      else -> android.view.Gravity.TOP or android.view.Gravity.END
    }
  }

  private fun createToastIconAnimation(tintColor: Int): AnimatedVectorDrawableCompat? {
    val imageDrawable =
      create(getBaseInstance(), getIconId())?.also {
        it.setTint(tintColor)
        it.setTintMode(SRC_ATOP)
      }
    binding!!.toastImage.setImageDrawable(imageDrawable)
    return imageDrawable
  }

  private fun getIconId(): Int {
    return if (type == SUCCESS) {
      drawable.ic_ok
    } else {
      drawable.ic_error
    }
  }
}
