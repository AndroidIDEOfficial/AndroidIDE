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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM
import android.widget.RelativeLayout.ALIGN_PARENT_TOP
import android.widget.TextView
import androidx.annotation.ColorInt
import com.itsaky.androidide.flashbar.Flashbar.Gravity
import com.itsaky.androidide.flashbar.Flashbar.Gravity.BOTTOM
import com.itsaky.androidide.flashbar.Flashbar.Gravity.TOP
import com.itsaky.androidide.flashbar.Flashbar.OnActionTapListener
import com.itsaky.androidide.flashbar.Flashbar.OnTapListener
import com.itsaky.androidide.flashbar.Flashbar.ProgressPosition
import com.itsaky.androidide.flashbar.Flashbar.ProgressPosition.LEFT
import com.itsaky.androidide.flashbar.Flashbar.ProgressPosition.RIGHT
import com.itsaky.androidide.flashbar.SwipeDismissTouchListener.DismissCallbacks
import com.itsaky.androidide.flashbar.anim.FlashAnimIconBuilder
import com.itsaky.androidide.flashbar.databinding.FlashBarViewBinding
import com.itsaky.androidide.flashbar.util.convertDpToPx
import com.itsaky.androidide.flashbar.util.getStatusBarHeightInPx
import com.itsaky.androidide.flashbar.view.FbButton
import com.itsaky.androidide.flashbar.view.FbProgress
import com.itsaky.androidide.flashbar.view.ShadowView

/**
 * The actual Flashbar withView representation that can consist of the title, message, button, icon,
 * etc. Its size is adaptive and depends solely on the amount of content present in it. It always
 * matches the width of the screen.
 *
 * It can either be present at the top or at the bottom of the screen. It will always consume touch
 * events and respond as necessary.
 */
internal class FlashbarView(context: Context) : LinearLayout(context) {

  private val compensationMarginTop =
    resources.getDimension(R.dimen.fb_top_compensation_margin).toInt()
  private val compensationMarginBottom =
    resources.getDimension(R.dimen.fb_bottom_compensation_margin).toInt()

  private lateinit var binding: FlashBarViewBinding
  private lateinit var parentFlashbarContainer: FlashbarContainerView
  private lateinit var gravity: Gravity

  private var isMarginCompensationApplied: Boolean = false

  private val fbContent: LinearLayout
    get() = this.binding.fbContent

  private val fbRoot: LinearLayout
    get() = this.binding.fbRoot

  private val fbIcon: ImageView
    get() = this.binding.fbIcon

  private val fbLeftProgress: FbProgress
    get() = this.binding.fbLeftProgress

  private val fbRightProgress: FbProgress
    get() = this.binding.fbRightProgress

  private val fbMessage: TextView
    get() = this.binding.fbMessage

  private val fbPrimaryAction: FbButton
    get() = this.binding.fbPrimaryAction

  private val fbPositiveAction: FbButton
    get() = this.binding.fbPositiveAction

  private val fbNegativeAction: FbButton
    get() = this.binding.fbNegativeAction

  private val fbSecondaryActionContainer: LinearLayout
    get() = this.binding.fbSecondaryActionContainer

  private val fbTitle: TextView
    get() = this.binding.fbTitle

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    if (!isMarginCompensationApplied) {
      isMarginCompensationApplied = true

      val params = layoutParams as MarginLayoutParams
      when (gravity) {
        TOP -> params.topMargin = -compensationMarginTop
        BOTTOM -> params.bottomMargin = -compensationMarginBottom
      }
      requestLayout()
    }
  }

  internal fun init(gravity: Gravity, castShadow: Boolean, shadowStrength: Int) {
    this.gravity = gravity
    this.orientation = VERTICAL

    // If the bar appears with the bottom, then the shadow needs to added to the top of it,
    // Thus, before the inflation of the bar
    if (castShadow && gravity == BOTTOM) {
      castShadow(ShadowView.ShadowType.TOP, shadowStrength)
    }

    this.binding = FlashBarViewBinding.inflate(LayoutInflater.from(context), this, true)

    // If the bar appears with the top, then the shadow needs to added to the bottom of it,
    // Thus, after the inflation of the bar
    if (castShadow && gravity == TOP) {
      castShadow(ShadowView.ShadowType.BOTTOM, shadowStrength)
    }
  }

  internal fun adjustWitPositionAndOrientation(activity: Activity, gravity: Gravity) {
    val flashbarViewLp = RelativeLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    val statusBarHeight = activity.getStatusBarHeightInPx()

    val flashbarViewContentLp = fbContent.layoutParams as LayoutParams

    when (gravity) {
      TOP -> {
        flashbarViewContentLp.topMargin = statusBarHeight.plus(compensationMarginTop / 2)
        flashbarViewLp.addRule(ALIGN_PARENT_TOP)
      }
      BOTTOM -> {
        flashbarViewContentLp.bottomMargin = compensationMarginBottom
        flashbarViewLp.addRule(ALIGN_PARENT_BOTTOM)
      }
    }
    fbContent.layoutParams = flashbarViewContentLp
    layoutParams = flashbarViewLp
  }

  internal fun addParent(flashbarContainerView: FlashbarContainerView) {
    this.parentFlashbarContainer = flashbarContainerView
  }

  internal fun setBarBackgroundDrawable(drawable: Drawable?) {
    if (drawable == null) return
    this.fbRoot.background = drawable
  }

  internal fun setBarBackgroundColor(@ColorInt color: Int?) {
    if (color == null) return
    this.fbRoot.setBackgroundColor(color)
  }

  internal fun setBarTapListener(listener: OnTapListener?) {
    if (listener == null) return

    this.fbRoot.setOnClickListener { listener.onTap(parentFlashbarContainer.parentFlashbar) }
  }

  internal fun setTitle(title: String?) {
    if (TextUtils.isEmpty(title)) return

    this.fbTitle.text = title
    this.fbTitle.visibility = VISIBLE
  }

  internal fun setTitleSpanned(title: Spanned?) {
    if (title == null) return

    this.fbTitle.text = title
    this.fbTitle.visibility = VISIBLE
  }

  internal fun setTitleTypeface(typeface: Typeface?) {
    if (typeface == null) return
    fbTitle.typeface = typeface
  }

  internal fun setTitleSizeInPx(size: Float?) {
    if (size == null) return
    fbTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  internal fun setTitleSizeInSp(size: Float?) {
    if (size == null) return
    fbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  internal fun setTitleColor(color: Int?) {
    if (color == null) return
    fbTitle.setTextColor(color)
  }

  internal fun setTitleAppearance(titleAppearance: Int?) {
    if (titleAppearance == null) return
    this.fbTitle.setTextAppearance(titleAppearance)
  }

  internal fun setMessage(message: String?) {
    if (TextUtils.isEmpty(message)) return

    this.fbMessage.text = message
    this.fbMessage.visibility = VISIBLE
  }

  internal fun setMessageSpanned(message: Spanned?) {
    if (message == null) return

    this.fbMessage.text = message
    this.fbMessage.visibility = VISIBLE
  }

  internal fun setMessageTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbMessage.typeface = typeface
  }

  internal fun setMessageSizeInPx(size: Float?) {
    if (size == null) return
    this.fbMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  internal fun setMessageSizeInSp(size: Float?) {
    if (size == null) return
    this.fbMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  internal fun setMessageColor(color: Int?) {
    if (color == null) return
    this.fbMessage.setTextColor(color)
  }

  internal fun setMessageAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbMessage.setTextAppearance(messageAppearance)
  }

  internal fun setPrimaryActionText(text: String?) {
    if (TextUtils.isEmpty(text)) return

    this.fbPrimaryAction.text = text
    this.fbPrimaryAction.visibility = VISIBLE
  }

  internal fun setPrimaryActionTextSpanned(text: Spanned?) {
    if (text == null) return

    this.fbPrimaryAction.text = text
    this.fbPrimaryAction.visibility = VISIBLE
  }

  internal fun setPrimaryActionTextTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbPrimaryAction.typeface = typeface
  }

  internal fun setPrimaryActionTextSizeInPx(size: Float?) {
    if (size == null) return
    this.fbPrimaryAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  internal fun setPrimaryActionTextSizeInSp(size: Float?) {
    if (size == null) return
    this.fbPrimaryAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  internal fun setPrimaryActionTextColor(color: Int?) {
    if (color == null) return
    this.fbPrimaryAction.setTextColor(color)
  }

  internal fun setPrimaryActionTextAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbPrimaryAction.setTextAppearance(messageAppearance)
  }

  internal fun setPrimaryActionTapListener(listener: OnActionTapListener?) {
    if (listener == null) return

    this.fbPrimaryAction.setOnClickListener {
      listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
    }
  }

  internal fun setPositiveActionText(text: String?) {
    if (TextUtils.isEmpty(text)) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbPositiveAction.text = text
    this.fbPositiveAction.visibility = VISIBLE
  }

  internal fun setPositiveActionTextSpanned(text: Spanned?) {
    if (text == null) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbPositiveAction.text = text
    this.fbPositiveAction.visibility = VISIBLE
  }

  internal fun setPositiveActionTextTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbPositiveAction.typeface = typeface
  }

  internal fun setPositiveActionTextSizeInPx(size: Float?) {
    if (size == null) return
    this.fbPositiveAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  internal fun setPositiveActionTextSizeInSp(size: Float?) {
    if (size == null) return
    this.fbPositiveAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  internal fun setPositiveActionTextColor(color: Int?) {
    if (color == null) return
    this.fbPositiveAction.setTextColor(color)
  }

  internal fun setPositiveActionTextAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbPositiveAction.setTextAppearance(messageAppearance)
  }

  internal fun setPositiveActionTapListener(listener: OnActionTapListener?) {
    if (listener == null) return

    this.fbPositiveAction.setOnClickListener {
      listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
    }
  }

  internal fun setNegativeActionText(text: String?) {
    if (TextUtils.isEmpty(text)) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbNegativeAction.text = text
    this.fbNegativeAction.visibility = VISIBLE
  }

  internal fun setNegativeActionTextSpanned(text: Spanned?) {
    if (text == null) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbNegativeAction.text = text
    this.fbNegativeAction.visibility = VISIBLE
  }

  internal fun setNegativeActionTextTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbNegativeAction.typeface = typeface
  }

  internal fun setNegativeActionTextSizeInPx(size: Float?) {
    if (size == null) return
    this.fbNegativeAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  internal fun setNegativeActionTextSizeInSp(size: Float?) {
    if (size == null) return
    this.fbNegativeAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  internal fun setNegativeActionTextColor(color: Int?) {
    if (color == null) return
    this.fbNegativeAction.setTextColor(color)
  }

  internal fun setNegativeActionTextAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbNegativeAction.setTextAppearance(messageAppearance)
  }

  internal fun setNegativeActionTapListener(listener: OnActionTapListener?) {
    if (listener == null) return

    this.fbNegativeAction.setOnClickListener {
      listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
    }
  }

  internal fun showIcon(showIcon: Boolean) {
    this.fbIcon.visibility = if (showIcon) VISIBLE else GONE
  }

  internal fun showIconScale(scale: Float, scaleType: ImageView.ScaleType?) {
    this.fbIcon.scaleX = scale
    this.fbIcon.scaleY = scale
    this.fbIcon.scaleType = scaleType
  }

  internal fun setIconDrawable(icon: Drawable?) {
    if (icon == null) return
    this.fbIcon.setImageDrawable(icon)
  }

  internal fun setIconBitmap(bitmap: Bitmap?) {
    if (bitmap == null) return
    this.fbIcon.setImageBitmap(bitmap)
  }

  internal fun setIconColorFilter(colorFilter: Int?, filterMode: PorterDuff.Mode?) {
    if (colorFilter == null) return
    if (filterMode == null) {
      this.fbIcon.setColorFilter(colorFilter)
    } else {
      this.fbIcon.setColorFilter(colorFilter, filterMode)
    }
  }

  internal fun startIconAnimation(animator: FlashAnimIconBuilder?) {
    animator?.withView(fbIcon)?.build()?.start()
  }

  internal fun stopIconAnimation() {
    fbIcon.clearAnimation()
  }

  @SuppressLint("ClickableViewAccessibility")
  internal fun enableSwipeToDismiss(enable: Boolean, callbacks: DismissCallbacks) {
    if (enable) {
      fbRoot.setOnTouchListener(SwipeDismissTouchListener(this, callbacks))
    }
  }

  internal fun setProgressPosition(position: ProgressPosition?) {
    if (position == null) return
    when (position) {
      LEFT -> {
        fbLeftProgress.visibility = VISIBLE
        fbRightProgress.visibility = GONE
      }
      RIGHT -> {
        fbLeftProgress.visibility = GONE
        fbRightProgress.visibility = VISIBLE
      }
    }
  }

  internal fun setProgressTint(progressTint: Int?, position: ProgressPosition?) {
    if (position == null || progressTint == null) return

    val progressBar =
      when (position) {
        LEFT -> fbLeftProgress
        RIGHT -> fbRightProgress
      }

    progressBar.setBarColor(progressTint)
  }

  private fun castShadow(shadowType: ShadowView.ShadowType, strength: Int) {
    val params = RelativeLayout.LayoutParams(MATCH_PARENT, context.convertDpToPx(strength))
    val shadow = ShadowView(context)
    shadow.applyShadow(shadowType)
    addView(shadow, params)
  }
}
