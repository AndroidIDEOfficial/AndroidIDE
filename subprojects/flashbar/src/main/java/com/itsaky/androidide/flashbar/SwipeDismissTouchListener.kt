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

package com.itsaky.androidide.flashbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

internal class SwipeDismissTouchListener(
  private val view: View,
  private val callbacks: DismissCallbacks
) : View.OnTouchListener {

  private val slop: Int
  private val minFlingVelocity: Int
  private val animationTime: Long
  private var viewWidth = 1

  private var downX: Float = 0.toFloat()
  private var downY: Float = 0.toFloat()
  private var swiping: Boolean = false
  private var swipingSlop: Int = 0
  private var velocityTracker: VelocityTracker? = null
  private var translationX: Float = 0.toFloat()

  init {
    val vc = ViewConfiguration.get(view.context)
    slop = vc.scaledTouchSlop
    minFlingVelocity = vc.scaledMinimumFlingVelocity * 16
    animationTime =
      view.context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
  }

  override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
    motionEvent.offsetLocation(translationX, 0f)

    if (viewWidth < 2) {
      viewWidth = this.view.width
    }

    when (motionEvent.actionMasked) {
      MotionEvent.ACTION_DOWN -> {
        downX = motionEvent.rawX
        downY = motionEvent.rawY
        velocityTracker = VelocityTracker.obtain()
        velocityTracker!!.addMovement(motionEvent)
        return false
      }
      MotionEvent.ACTION_UP -> {
        if (velocityTracker != null) {
          val deltaX = motionEvent.rawX - downX
          velocityTracker!!.addMovement(motionEvent)
          velocityTracker!!.computeCurrentVelocity(1000)
          val velocityX = velocityTracker!!.xVelocity
          val absVelocityX = abs(velocityX)
          val absVelocityY = abs(velocityTracker!!.yVelocity)
          var dismiss = false
          var dismissRight = false
          if (abs(deltaX) > viewWidth / 2 && swiping) {
            dismiss = true
            dismissRight = deltaX > 0
          } else if (minFlingVelocity <= absVelocityX && absVelocityY < absVelocityX && swiping) {
            dismiss = velocityX < 0 == deltaX < 0
            dismissRight = velocityTracker!!.xVelocity > 0
          }
          if (dismiss) {
            this.view
              .animate()
              .translationX(if (dismissRight) viewWidth.toFloat() else -viewWidth.toFloat())
              .alpha(0f)
              .setDuration(animationTime)
              .setListener(
                object : AnimatorListenerAdapter() {
                  override fun onAnimationEnd(animation: Animator) {
                    performDismiss()
                  }
                }
              )
          } else if (swiping) {
            this.view
              .animate()
              .translationX(0f)
              .alpha(1f)
              .setDuration(animationTime)
              .setListener(null)
          }
          velocityTracker!!.recycle()
          velocityTracker = null
          translationX = 0f
          downX = 0f
          downY = 0f
          swiping = false
          callbacks.onSwipe(false)
        }
      }
      MotionEvent.ACTION_CANCEL -> {
        if (velocityTracker != null) {
          this.view
            .animate()
            .translationX(0f)
            .alpha(1f)
            .setDuration(animationTime)
            .setListener(null)
          velocityTracker!!.recycle()
          velocityTracker = null
          translationX = 0f
          downX = 0f
          downY = 0f
          swiping = false
          callbacks.onSwipe(false)
        }
      }
      MotionEvent.ACTION_MOVE -> {
        if (velocityTracker != null) {
          velocityTracker!!.addMovement(motionEvent)
          val deltaX = motionEvent.rawX - downX
          val deltaY = motionEvent.rawY - downY
          if (abs(deltaX) > slop && abs(deltaY) < abs(deltaX) / 2) {
            swiping = true
            callbacks.onSwipe(true)
            swipingSlop = if (deltaX > 0) slop else -slop
            this.view.parent.requestDisallowInterceptTouchEvent(true)

            val cancelEvent = MotionEvent.obtain(motionEvent)
            cancelEvent.action =
              MotionEvent.ACTION_CANCEL or
                (motionEvent.actionIndex shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
            this.view.onTouchEvent(cancelEvent)
            cancelEvent.recycle()
          }

          if (swiping) {
            translationX = deltaX
            this.view.translationX = deltaX - swipingSlop
            this.view.alpha = 0f.coerceAtLeast(1f.coerceAtMost(1f - 2f * abs(deltaX) / viewWidth))
            return true
          }
        }
      }
      else -> {
        view.performClick()
        return false
      }
    }
    return false
  }

  private fun performDismiss() {
    val lp = view.layoutParams
    val originalHeight = view.height

    val animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(animationTime)

    animator.addListener(
      object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          callbacks.onDismiss(view)
          view.alpha = 1f
          view.translationX = 0f
          lp.height = originalHeight
          view.layoutParams = lp
        }
      }
    )

    animator.addUpdateListener { valueAnimator ->
      lp.height = valueAnimator.animatedValue as Int
      view.layoutParams = lp
    }

    animator.start()
  }

  internal interface DismissCallbacks {

    fun onSwipe(isSwiping: Boolean)

    fun onDismiss(view: View)
  }
}
