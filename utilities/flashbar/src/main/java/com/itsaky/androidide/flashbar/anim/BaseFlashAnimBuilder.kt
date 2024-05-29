package com.itsaky.androidide.flashbar.anim

import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.CallSuper
import androidx.annotation.InterpolatorRes
import com.itsaky.androidide.flashbar.R

abstract class BaseFlashAnimBuilder(private val context: Context) {

  private val DEFAULT_DURATION =
    context.resources.getInteger(R.integer.default_animation_duration).toLong()

  protected val DEFAULT_ALPHA_START = 0.2f
  protected val DEFAULT_ALPHA_END = 1.0f

  protected var view: View? = null
  protected var duration = DEFAULT_DURATION
  protected var interpolator: Interpolator? = null

  protected var alpha: Boolean = false

  @CallSuper
  open fun duration(millis: Long) = apply {
    require(duration >= 0) { "Duration must not be negative" }
    this.duration = millis
  }

  @CallSuper open fun accelerate() = apply { this.interpolator = AccelerateInterpolator() }

  @CallSuper open fun decelerate() = apply { this.interpolator = DecelerateInterpolator() }

  @CallSuper
  open fun accelerateDecelerate() = apply { this.interpolator = AccelerateDecelerateInterpolator() }

  @CallSuper
  open fun interpolator(interpolator: Interpolator) = apply { this.interpolator = interpolator }

  @CallSuper
  open fun interpolator(@InterpolatorRes id: Int) = apply {
    this.interpolator = AnimationUtils.loadInterpolator(context, id)
  }

  @CallSuper open fun alpha() = apply { this.alpha = true }

  @CallSuper internal open fun withView(view: View) = apply { this.view = view }
}
