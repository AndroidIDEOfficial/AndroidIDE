package com.itsaky.androidide.flashbar.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.view.View
import android.view.animation.Interpolator
import androidx.annotation.InterpolatorRes

class FlashAnimIconBuilder(context: Context) : BaseFlashAnimBuilder(context) {

  private val DEFAULT_PULSE_START = 1.0f
  private val DEFAULT_PULSE_END = 0.6f

  private var pulse: Boolean = false
  private var pulseStart = 0f
  private var pulseEnd = 0f

  /** Specifies the duration (in millis) for the animation */
  override fun duration(millis: Long) = apply { super.duration(millis) }

  /** Specifies accelerate interpolator for the animation */
  override fun accelerate() = apply { super.accelerate() }

  /** Specifies decelerate interpolator for the animation */
  override fun decelerate() = apply { super.decelerate() }

  /** Specifies accelerate-decelerate interpolator for the animation */
  override fun accelerateDecelerate() = apply { super.accelerateDecelerate() }

  /** Specifies custom interpolator for the animation */
  override fun interpolator(interpolator: Interpolator) = apply { super.interpolator(interpolator) }

  /** Specifies custom interpolator resource for the animation */
  override fun interpolator(@InterpolatorRes id: Int) = apply { super.interpolator(id) }

  /** Specifies that the animation should have alpha effect */
  override fun alpha() = apply { super.alpha() }

  /** Specifies that the animation should have pulse effect */
  @JvmOverloads
  fun pulse(start: Float = DEFAULT_PULSE_START, end: Float = DEFAULT_PULSE_END) = apply {
    this.pulse = true
    this.pulseStart = start
    this.pulseEnd = end
  }

  override fun withView(view: View) = apply { super.withView(view) }

  internal fun build(): FlashAnim {
    requireNotNull(view, { "Target view can not be null" })

    val compositeAnim = AnimatorSet()
    val animators = linkedSetOf<Animator>()

    if (pulse) {
      val scaleAnim =
        ObjectAnimator.ofPropertyValuesHolder(
          view,
          PropertyValuesHolder.ofFloat("scaleX", pulseStart, pulseEnd),
          PropertyValuesHolder.ofFloat("scaleY", pulseStart, pulseEnd)
        )

      scaleAnim.repeatCount = INFINITE
      scaleAnim.repeatMode = REVERSE

      animators.add(scaleAnim)
    }

    if (alpha) {
      val alpha = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.6f)

      alpha.repeatCount = INFINITE
      alpha.repeatMode = REVERSE

      animators.add(alpha)
    }

    compositeAnim.playTogether(animators)

    compositeAnim.duration = duration
    compositeAnim.interpolator = interpolator

    return FlashAnim(compositeAnim)
  }
}
