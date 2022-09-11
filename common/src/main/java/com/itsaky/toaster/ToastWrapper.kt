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

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewAnimationUtils.createCircularReveal
import android.widget.LinearLayout
import com.google.android.material.animation.ArgbEvaluatorCompat
import kotlin.math.max

class ToastWrapper : LinearLayout {
  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, style: Int) : super(context, attrs, style)

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    startRevealAnimation()
    startIconColorChangeAnimation()
  }

  private fun startIconColorChangeAnimation() {
    val anim = ValueAnimator.ofObject(ArgbEvaluatorCompat(), Color.WHITE, toaster.iconColor)
    anim.duration = (Toaster.REVEAL_ANIM_DURATION + 300).toLong()
    anim.addUpdateListener { animator: ValueAnimator ->
      toaster.icon!!.setTint(animator.animatedValue as Int)
    }
    anim.start()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    setBackgroundColor(Color.TRANSPARENT)
  }

  private fun startRevealAnimation() {
    this.background = toaster.backgroundDrawable
    val x = left
    val y = (top + bottom) / 2
    val endRadius = max(height, width)
    createCircularReveal(this, x, y, 0f, endRadius.toFloat()).also {
      it.duration = Toaster.REVEAL_ANIM_DURATION.toLong()
      it.start()
    }
  }
}
