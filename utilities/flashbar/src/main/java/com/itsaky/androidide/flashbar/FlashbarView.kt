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
import android.content.res.ColorStateList
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
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
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
import com.itsaky.androidide.flashbar.util.getStatusBarHeightInPx

/**
 * The actual Flashbar withView representation that can consist of the title, message, button, icon,
 * etc. Its size is adaptive and depends solely on the amount of content present in it. It always
 * matches the width of the screen.
 *
 * It can either be present at the top or at the bottom of the screen. It will always consume touch
 * events and respond as necessary.
 */
class FlashbarView(context: Context) : LinearLayout(context) {

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

  private val fbLeftProgress: CircularProgressIndicator
    get() = this.binding.fbLeftProgress

  private val fbRightProgress: CircularProgressIndicator
    get() = this.binding.fbRightProgress

  private val fbMessage: TextView
    get() = this.binding.fbMessage

  private val fbPrimaryAction: MaterialButton
    get() = this.binding.fbPrimaryAction

  private val fbPositiveAction: MaterialButton
    get() = this.binding.fbPositiveAction

  private val fbNegativeAction: MaterialButton
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

  internal fun init(gravity: Gravity) {
    this.gravity = gravity
    this.orientation = VERTICAL
    this.binding = FlashBarViewBinding.inflate(LayoutInflater.from(context), this, true)

    this.binding.root.apply {
      // add margin to the card view so that the card elevation is visible
      val dp16 = context.resources.getDimensionPixelSize(R.dimen.fb_card_elevation)
      val (topIncr, bottomIncr) = when (gravity) {
        TOP -> 0 to dp16
        BOTTOM -> dp16 to 0
      }

      updateLayoutParams<MarginLayoutParams> {
        setMargins(
          /* left = */ marginLeft,
          /* top = */ marginTop + topIncr,
          /* right = */ marginRight,
          /* bottom = */ marginBottom + bottomIncr
        )
      }
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

  fun setBarBackgroundDrawable(drawable: Drawable?) {
    if (drawable == null) return
    this.fbRoot.background = drawable
  }

  fun setBarBackgroundColor(@ColorInt color: Int?) {
    if (color == null) return
    this.fbRoot.setBackgroundColor(color)
  }

  fun setBarTapListener(listener: OnTapListener?) {
    if (listener == null) return

    this.fbRoot.setOnClickListener { listener.onTap(parentFlashbarContainer.parentFlashbar) }
  }

  fun setTitle(title: String?) {
    if (TextUtils.isEmpty(title)) return

    this.fbTitle.text = title
    this.fbTitle.visibility = VISIBLE
  }

  fun setTitleSpanned(title: Spanned?) {
    if (title == null) return

    this.fbTitle.text = title
    this.fbTitle.visibility = VISIBLE
  }

  fun setTitleTypeface(typeface: Typeface?) {
    if (typeface == null) return
    fbTitle.typeface = typeface
  }

  fun setTitleSizeInPx(size: Float?) {
    if (size == null) return
    fbTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  fun setTitleSizeInSp(size: Float?) {
    if (size == null) return
    fbTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  fun setTitleColor(color: Int?) {
    if (color == null) return
    fbTitle.setTextColor(color)
  }

  fun setTitleAppearance(titleAppearance: Int?) {
    if (titleAppearance == null) return
    this.fbTitle.setTextAppearance(titleAppearance)
  }

  fun setMessage(message: String?) {
    if (TextUtils.isEmpty(message)) return

    this.fbMessage.text = message
    this.fbMessage.visibility = VISIBLE
  }

  fun setMessageSpanned(message: Spanned?) {
    if (message == null) return

    this.fbMessage.text = message
    this.fbMessage.visibility = VISIBLE
  }

  fun setMessageTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbMessage.typeface = typeface
  }

  fun setMessageSizeInPx(size: Float?) {
    if (size == null) return
    this.fbMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  fun setMessageSizeInSp(size: Float?) {
    if (size == null) return
    this.fbMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  fun setMessageColor(color: Int?) {
    if (color == null) return
    this.fbMessage.setTextColor(color)
  }

  fun setMessageAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbMessage.setTextAppearance(messageAppearance)
  }

  fun setPrimaryActionText(text: String?) {
    if (TextUtils.isEmpty(text)) return

    this.fbPrimaryAction.text = text
    this.fbPrimaryAction.visibility = VISIBLE
  }

  fun setPrimaryActionTextSpanned(text: Spanned?) {
    if (text == null) return

    this.fbPrimaryAction.text = text
    this.fbPrimaryAction.visibility = VISIBLE
  }

  fun setPrimaryActionTextTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbPrimaryAction.typeface = typeface
  }

  fun setPrimaryActionTextSizeInPx(size: Float?) {
    if (size == null) return
    this.fbPrimaryAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  fun setPrimaryActionTextSizeInSp(size: Float?) {
    if (size == null) return
    this.fbPrimaryAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  fun setPrimaryActionTextColor(color: Int?) {
    if (color == null) return
    this.fbPrimaryAction.setTextColor(color)
  }

  fun setPrimaryActionTextAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbPrimaryAction.setTextAppearance(messageAppearance)
  }

  fun setPrimaryActionTapListener(listener: OnActionTapListener?) {
    if (listener == null) return

    this.fbPrimaryAction.setOnClickListener {
      listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
    }
  }

  fun setPositiveActionText(text: String?) {
    if (TextUtils.isEmpty(text)) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbPositiveAction.text = text
    this.fbPositiveAction.visibility = VISIBLE
  }

  fun setPositiveActionTextSpanned(text: Spanned?) {
    if (text == null) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbPositiveAction.text = text
    this.fbPositiveAction.visibility = VISIBLE
  }

  fun setPositiveActionTextTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbPositiveAction.typeface = typeface
  }

  fun setPositiveActionTextSizeInPx(size: Float?) {
    if (size == null) return
    this.fbPositiveAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  fun setPositiveActionTextSizeInSp(size: Float?) {
    if (size == null) return
    this.fbPositiveAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  fun setPositiveActionTextColor(color: Int?) {
    if (color == null) return
    this.fbPositiveAction.setTextColor(color)
  }

  fun setPositiveActionTextAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbPositiveAction.setTextAppearance(messageAppearance)
  }

  fun setPositiveActionTapListener(listener: OnActionTapListener?) {
    if (listener == null) return

    this.fbPositiveAction.setOnClickListener {
      listener.onActionTapped(parentFlashbarContainer.parentFlashbar)
    }
  }

  fun setNegativeActionText(text: String?) {
    if (TextUtils.isEmpty(text)) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbNegativeAction.text = text
    this.fbNegativeAction.visibility = VISIBLE
  }

  fun setNegativeActionTextSpanned(text: Spanned?) {
    if (text == null) return

    this.fbSecondaryActionContainer.visibility = VISIBLE
    this.fbNegativeAction.text = text
    this.fbNegativeAction.visibility = VISIBLE
  }

  fun setNegativeActionTextTypeface(typeface: Typeface?) {
    if (typeface == null) return
    this.fbNegativeAction.typeface = typeface
  }

  fun setNegativeActionTextSizeInPx(size: Float?) {
    if (size == null) return
    this.fbNegativeAction.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
  }

  fun setNegativeActionTextSizeInSp(size: Float?) {
    if (size == null) return
    this.fbNegativeAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
  }

  fun setNegativeActionTextColor(color: Int?) {
    if (color == null) return
    this.fbNegativeAction.setTextColor(color)
  }

  fun setNegativeActionTextAppearance(messageAppearance: Int?) {
    if (messageAppearance == null) return
    this.fbNegativeAction.setTextAppearance(messageAppearance)
  }

  fun setNegativeActionTapListener(listener: OnActionTapListener?) {
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

  fun setIconDrawable(icon: Drawable?) {
    if (icon == null) return
    this.fbIcon.setImageDrawable(icon)
  }

  fun setIconBitmap(bitmap: Bitmap?) {
    if (bitmap == null) return
    this.fbIcon.setImageBitmap(bitmap)
  }

  fun setIconColorFilter(colorFilter: Int?, filterMode: PorterDuff.Mode?) {
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

  fun setProgressPosition(position: ProgressPosition?) {
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

  fun setProgressTint(progressTint: Int?, position: ProgressPosition?) {
    if (position == null || progressTint == null) return

    val progressBar =
      when (position) {
        LEFT -> fbLeftProgress
        RIGHT -> fbRightProgress
      }

    progressBar.progressTintList = ColorStateList.valueOf(progressTint)
  }
}
