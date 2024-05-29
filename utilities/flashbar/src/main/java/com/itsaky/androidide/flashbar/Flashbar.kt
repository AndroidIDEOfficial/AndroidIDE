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

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.ImageView.ScaleType
import android.widget.ImageView.ScaleType.CENTER_CROP
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.itsaky.androidide.flashbar.Flashbar.Gravity.BOTTOM
import com.itsaky.androidide.flashbar.Flashbar.Gravity.TOP
import com.itsaky.androidide.flashbar.Flashbar.ProgressPosition.LEFT
import com.itsaky.androidide.flashbar.Flashbar.ProgressPosition.RIGHT
import com.itsaky.androidide.flashbar.anim.FlashAnim
import com.itsaky.androidide.flashbar.anim.FlashAnimBarBuilder
import com.itsaky.androidide.flashbar.anim.FlashAnimIconBuilder

private const val DEFAULT_SHADOW_STRENGTH = 4
private const val DEFAUT_ICON_SCALE = 1.0f

@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
class Flashbar private constructor(private var builder: Builder) {

  private lateinit var flashbarContainerView: FlashbarContainerView

  lateinit var flashbarView: FlashbarView

  /** Shows a flashbar */
  fun show() {
    flashbarContainerView.show(builder.activity)
  }

  /** Dismisses a flashbar */
  fun dismiss() {
    flashbarContainerView.dismiss()
  }

  /**
   * Returns true/false depending on whether the flashbar is showing or not This represents the
   * partial appearance of the flashbar
   */
  fun isShowing() = flashbarContainerView.isBarShowing()

  /**
   * Returns true/false depending on whether the flashbar has been completely shown or not This
   * represents the complete appearance of the flashbar
   */
  fun isShown() = flashbarContainerView.isBarShown()

  private fun construct() {
    flashbarContainerView = FlashbarContainerView(builder.activity)
    flashbarContainerView.adjustOrientation(builder.activity)
    flashbarContainerView.addParent(this)
    flashbarContainerView.fitsSystemWindows = true

    flashbarView = FlashbarView(builder.activity)
    flashbarView.init(builder.gravity)
    flashbarView.adjustWitPositionAndOrientation(builder.activity, builder.gravity)
    flashbarView.addParent(flashbarContainerView)

    flashbarContainerView.attach(flashbarView)

    initializeContainerDecor()
    initializeBarDecor()

    flashbarContainerView.construct()
  }

  private fun initializeContainerDecor() {
    with(flashbarContainerView) {
      setDuration(builder.duration)
      setBarShowListener(builder.onBarShowListener)
      setBarDismissListener(builder.onBarDismissListener)
      setBarDismissOnTapOutside(builder.barDismissOnTapOutside)
      setOnTapOutsideListener(builder.onTapOutsideListener)
      setOverlay(builder.overlay)
      setOverlayColor(builder.overlayColor)
      setOverlayBlockable(builder.overlayBlockable)
      setVibrationTargets(builder.vibrationTargets)
      setIconAnim(builder.iconAnimBuilder)

      setEnterAnim(builder.enterAnimBuilder!!)
      setExitAnim(builder.exitAnimBuilder!!)
      enableSwipeToDismiss(builder.enableSwipeToDismiss)
    }
  }

  private fun initializeBarDecor() {
    with(flashbarView) {
      setBarBackgroundColor(builder.backgroundColor)
      setBarBackgroundDrawable(builder.backgroundDrawable)
      setBarTapListener(builder.onBarTapListener)

      setTitle(builder.title)
      setTitleSpanned(builder.titleSpanned)
      setTitleTypeface(builder.titleTypeface)
      setTitleSizeInPx(builder.titleSizeInPx)
      setTitleSizeInSp(builder.titleSizeInSp)
      setTitleColor(builder.titleColor)
      setTitleAppearance(builder.titleAppearance)

      setMessage(builder.message)
      setMessageSpanned(builder.messageSpanned)
      setMessageTypeface(builder.messageTypeface)
      setMessageSizeInPx(builder.messageSizeInPx)
      setMessageSizeInSp(builder.messageSizeInSp)
      setMessageColor(builder.messageColor)
      setMessageAppearance(builder.messageAppearance)

      setPrimaryActionText(builder.primaryActionText)
      setPrimaryActionTextSpanned(builder.primaryActionTextSpanned)
      setPrimaryActionTextTypeface(builder.primaryActionTextTypeface)
      setPrimaryActionTextSizeInPx(builder.primaryActionTextSizeInPx)
      setPrimaryActionTextSizeInSp(builder.primaryActionTextSizeInSp)
      setPrimaryActionTextColor(builder.primaryActionTextColor)
      setPrimaryActionTextAppearance(builder.primaryActionTextAppearance)
      setPrimaryActionTapListener(builder.onPrimaryActionTapListener)

      setPositiveActionText(builder.positiveActionText)
      setPositiveActionTextSpanned(builder.positiveActionTextSpanned)
      setPositiveActionTextTypeface(builder.positiveActionTextTypeface)
      setPositiveActionTextSizeInPx(builder.positiveActionTextSizeInPx)
      setPositiveActionTextSizeInSp(builder.positiveActionTextSizeInSp)
      setPositiveActionTextColor(builder.positiveActionTextColor)
      setPositiveActionTextAppearance(builder.positiveActionTextAppearance)
      setPositiveActionTapListener(builder.onPositiveActionTapListener)

      setNegativeActionText(builder.negativeActionText)
      setNegativeActionTextSpanned(builder.negativeActionTextSpanned)
      setNegativeActionTextTypeface(builder.negativeActionTextTypeface)
      setNegativeActionTextSizeInPx(builder.negativeActionTextSizeInPx)
      setNegativeActionTextSizeInSp(builder.negativeActionTextSizeInSp)
      setNegativeActionTextColor(builder.negativeActionTextColor)
      setNegativeActionTextAppearance(builder.negativeActionTextAppearance)
      setNegativeActionTapListener(builder.onNegativeActionTapListener)

      showIcon(builder.showIcon)
      showIconScale(builder.iconScale, builder.iconScaleType)
      setIconDrawable(builder.iconDrawable)
      setIconBitmap(builder.iconBitmap)
      setIconColorFilter(builder.iconColorFilter, builder.iconColorFilterMode)

      setProgressPosition(builder.progressPosition)
      setProgressTint(builder.progressTint, builder.progressPosition)
    }
  }

  class Builder(internal var activity: Activity) {
    internal var gravity: Gravity = BOTTOM
    internal var backgroundColor: Int? = null
    internal var backgroundDrawable: Drawable? = null
    internal var duration: Long = DURATION_INDEFINITE
    internal var onBarTapListener: OnTapListener? = null
    internal var onBarShowListener: OnBarShowListener? = null
    internal var onBarDismissListener: OnBarDismissListener? = null
    internal var barDismissOnTapOutside: Boolean = false
    internal var onTapOutsideListener: OnTapListener? = null
    internal var overlay: Boolean = false
    internal var overlayColor: Int = ContextCompat.getColor(activity, R.color.modal)
    internal var overlayBlockable: Boolean = false
    internal var shadowStrength: Int = DEFAULT_SHADOW_STRENGTH
    internal var enableSwipeToDismiss: Boolean = false
    internal var vibrationTargets: List<Vibration> = emptyList()

    internal var title: String? = null
    internal var titleSpanned: Spanned? = null
    internal var titleTypeface: Typeface? = null
    internal var titleSizeInPx: Float? = null
    internal var titleSizeInSp: Float? = null
    internal var titleColor: Int? = null
    internal var titleAppearance: Int? = null

    internal var message: String? = null
    internal var messageSpanned: Spanned? = null
    internal var messageTypeface: Typeface? = null
    internal var messageSizeInPx: Float? = null
    internal var messageSizeInSp: Float? = null
    internal var messageColor: Int? = null
    internal var messageAppearance: Int? = null

    internal var primaryActionText: String? = null
    internal var primaryActionTextSpanned: Spanned? = null
    internal var primaryActionTextTypeface: Typeface? = null
    internal var primaryActionTextSizeInPx: Float? = null
    internal var primaryActionTextSizeInSp: Float? = null
    internal var primaryActionTextColor: Int? = null
    internal var primaryActionTextAppearance: Int? = null
    internal var onPrimaryActionTapListener: OnActionTapListener? = null

    internal var positiveActionText: String? = null
    internal var positiveActionTextSpanned: Spanned? = null
    internal var positiveActionTextTypeface: Typeface? = null
    internal var positiveActionTextSizeInPx: Float? = null
    internal var positiveActionTextSizeInSp: Float? = null
    internal var positiveActionTextColor: Int? = null
    internal var positiveActionTextAppearance: Int? = null
    internal var onPositiveActionTapListener: OnActionTapListener? = null

    internal var negativeActionText: String? = null
    internal var negativeActionTextSpanned: Spanned? = null
    internal var negativeActionTextTypeface: Typeface? = null
    internal var negativeActionTextSizeInPx: Float? = null
    internal var negativeActionTextSizeInSp: Float? = null
    internal var negativeActionTextColor: Int? = null
    internal var negativeActionTextAppearance: Int? = null
    internal var onNegativeActionTapListener: OnActionTapListener? = null

    internal var showIcon: Boolean = false
    internal var iconScale: Float = DEFAUT_ICON_SCALE
    internal var iconScaleType: ScaleType = CENTER_CROP
    internal var iconDrawable: Drawable? = null
    internal var iconBitmap: Bitmap? = null
    internal var iconColorFilter: Int? = null
    internal var iconColorFilterMode: PorterDuff.Mode? = null
    internal var iconAnimBuilder: FlashAnimIconBuilder? = null

    internal var progressPosition: ProgressPosition? = null
    internal var progressTint: Int? = null

    internal var enterAnimBuilder: FlashAnimBarBuilder? = null
    internal var exitAnimBuilder: FlashAnimBarBuilder? = null

    /**
     * Specifies the gravity from where the flashbar will be shown (top/bottom) Default gravity is
     * TOP
     */
    fun gravity(gravity: Gravity) = apply { this.gravity = gravity }

    /** Specifies the background drawable of the flashbar */
    fun backgroundDrawable(drawable: Drawable) = apply { this.backgroundDrawable = drawable }

    /** Specifies the background drawable resource of the flashbar */
    fun backgroundDrawable(@DrawableRes drawableId: Int) = apply {
      this.backgroundDrawable = ContextCompat.getDrawable(activity, drawableId)
    }

    /** Specifies the background color of the flashbar */
    fun backgroundColor(@ColorInt color: Int) = apply { this.backgroundColor = color }

    /** Specifies the background color resource of the flashbar */
    fun backgroundColorRes(@ColorRes colorId: Int) = apply {
      this.backgroundColor = ContextCompat.getColor(activity, colorId)
    }

    /** Sets listener to receive tap events on the surface of the bar */
    fun listenBarTaps(listener: OnTapListener) = apply { this.onBarTapListener = listener }

    /**
     * Specifies the duration for which the flashbar will be visible By default, the duration is
     * infinite
     */
    fun duration(milliseconds: Long) = apply {
      require(milliseconds == DURATION_INDEFINITE || milliseconds > 0) { "Duration can only be $DURATION_INDEFINITE or > 0" }
      this.duration = milliseconds
    }

    /** Sets listener to receive bar showing/shown events */
    fun barShowListener(listener: OnBarShowListener) = apply { this.onBarShowListener = listener }

    /** Sets listener to receive bar dismissing/dismissed events */
    fun barDismissListener(listener: OnBarDismissListener) = apply {
      this.onBarDismissListener = listener
    }

    /** Sets listener to receive tap events outside flashbar surface */
    fun listenOutsideTaps(listener: OnTapListener) = apply { this.onTapOutsideListener = listener }

    /** Dismisses the bar on being tapped outside */
    fun dismissOnTapOutside() = apply { this.barDismissOnTapOutside = true }

    /** Shows the modal overlay */
    fun showOverlay() = apply { this.overlay = true }

    /** Specifies modal overlay color */
    fun overlayColor(@ColorInt color: Int) = apply { this.overlayColor = color }

    /**
     * Specifies modal overlay color resource Modal overlay is automatically shown if color is set
     */
    fun overlayColorRes(@ColorRes colorId: Int) = apply {
      this.overlayColor = ContextCompat.getColor(activity, colorId)
    }

    /** Specifies if modal overlay is blockable and should comsume touch events */
    fun overlayBlockable() = apply { this.overlayBlockable = true }

    /** Specifies the enter animation of the flashbar */
    fun enterAnimation(builder: FlashAnimBarBuilder) = apply { this.enterAnimBuilder = builder }

    /** Specifies the exit animation of the flashbar */
    fun exitAnimation(builder: FlashAnimBarBuilder) = apply { this.exitAnimBuilder = builder }

    /** Enables swipe-to-dismiss for the flashbar */
    fun enableSwipeToDismiss() = apply { this.enableSwipeToDismiss = true }

    /** Specifies whether the device should vibrate during flashbar enter/exit/both */
    fun vibrateOn(vararg vibrate: Vibration) = apply {
      require(vibrate.isNotEmpty()) { "Vibration targets can not be empty" }
      this.vibrationTargets = vibrate.toList()
    }

    /** Specifies the title string */
    fun title(title: String) = apply { this.title = title }

    /** Specifies the title string res */
    fun title(@StringRes titleId: Int) = apply { this.title = activity.getString(titleId) }

    /** Specifies the title span */
    fun title(title: Spanned) = apply { this.titleSpanned = title }

    /** Specifies the title typeface */
    fun titleTypeface(typeface: Typeface) = apply { this.titleTypeface = typeface }

    /** Specifies the title size (in pixels) */
    fun titleSizeInPx(size: Float) = apply { this.titleSizeInPx = size }

    /** Specifies the title size (in sp) */
    fun titleSizeInSp(size: Float) = apply { this.titleSizeInSp = size }

    /** Specifies the title color */
    fun titleColor(@ColorInt color: Int) = apply { this.titleColor = color }

    /** Specifies the title color resource */
    fun titleColorRes(@ColorRes colorId: Int) = apply {
      this.titleColor = ContextCompat.getColor(activity, colorId)
    }

    /** Specifies the title appearance */
    fun titleAppearance(@StyleRes appearance: Int) = apply { this.titleAppearance = appearance }

    /** Specifies the message string */
    fun message(message: String) = apply { this.message = message }

    /** Specifies the message string resource */
    fun message(@StringRes messageId: Int) = apply { this.message = activity.getString(messageId) }

    /** Specifies the message string span */
    fun message(message: Spanned) = apply { this.messageSpanned = message }

    /** Specifies the message typeface */
    fun messageTypeface(typeface: Typeface) = apply { this.messageTypeface = typeface }

    /** Specifies the message size (in pixels) */
    fun messageSizeInPx(size: Float) = apply { this.messageSizeInPx = size }

    /** Specifies the message size (in sp) */
    fun messageSizeInSp(size: Float) = apply { this.messageSizeInSp = size }

    /** Specifies the message color */
    fun messageColor(@ColorInt color: Int) = apply { this.messageColor = color }

    /** Specifies the message color resource */
    fun messageColorRes(@ColorRes colorId: Int) = apply {
      this.messageColor = ContextCompat.getColor(activity, colorId)
    }

    /** Specifies the message appearance */
    fun messageAppearance(@StyleRes appearance: Int) = apply { this.messageAppearance = appearance }

    /** Specifies the primary action text string */
    fun primaryActionText(text: String) = apply {
      require(progressPosition != RIGHT) { "Cannot show action button if right progress is set" }
        this.primaryActionText = text
    }

    /** Specifies the primary action text string resource */
    fun primaryActionText(@StringRes actionTextId: Int) = apply {
      primaryActionText(activity.getString(actionTextId))
    }

    /** Specifies the primary action text string span */
    fun primaryActionText(actionText: Spanned) = apply {
      this.primaryActionTextSpanned = actionText
    }

    /** Specifies the primary action text typeface */
    fun primaryActionTextTypeface(typeface: Typeface) = apply {
      this.primaryActionTextTypeface = typeface
    }

    /** Specifies the primary action text size (in pixels) */
    fun primaryActionTextSizeInPx(size: Float) = apply { this.primaryActionTextSizeInPx = size }

    /** Specifies the primary action text size (in sp) */
    fun primaryActionTextSizeInSp(size: Float) = apply { this.primaryActionTextSizeInSp = size }

    /** Specifies the primary action text color */
    fun primaryActionTextColor(@ColorInt color: Int) = apply { this.primaryActionTextColor = color }

    /** Specifies the primary action text color resource */
    fun primaryActionTextColorRes(@ColorRes colorId: Int) = apply {
      this.primaryActionTextColor = ContextCompat.getColor(activity, colorId)
    }

    /** Specifies the primary action text appearance */
    fun primaryActionTextAppearance(@StyleRes appearance: Int) = apply {
      this.primaryActionTextAppearance = appearance
    }

    /** Sets listener to receive tap events on the primary action */
    fun primaryActionTapListener(onActionTapListener: OnActionTapListener) = apply {
      this.onPrimaryActionTapListener = onActionTapListener
    }

    /** Specifies the positive action text string */
    fun positiveActionText(text: String) = apply { this.positiveActionText = text }

    /** Specifies the positive action text string resource */
    fun positiveActionText(@StringRes actionTextId: Int) = apply {
      positiveActionText(activity.getString(actionTextId))
    }

    /** Specifies the positive action text string span */
    fun positiveActionText(actionText: Spanned) = apply {
      this.positiveActionTextSpanned = actionText
    }

    /** Specifies the positive action text typeface */
    fun positiveActionTextTypeface(typeface: Typeface) = apply {
      this.positiveActionTextTypeface = typeface
    }

    /** Specifies the positive action text size (in pixels) */
    fun positiveActionTextSizeInPx(size: Float) = apply { this.positiveActionTextSizeInPx = size }

    /** Specifies the positive action text size (in sp) */
    fun positiveActionTextSizeInSp(size: Float) = apply { this.positiveActionTextSizeInSp = size }

    /** Specifies the positive action text color */
    fun positiveActionTextColor(@ColorInt color: Int) = apply {
      this.positiveActionTextColor = color
    }

    /** Specifies the positive action text color resource */
    fun positiveActionTextColorRes(@ColorRes colorId: Int) = apply {
      this.positiveActionTextColor = ContextCompat.getColor(activity, colorId)
    }

    /** Specifies the positive action text appearance */
    fun positiveActionTextAppearance(@StyleRes appearance: Int) = apply {
      this.positiveActionTextAppearance = appearance
    }

    /** Sets listener to receive tap events on the positive action */
    fun positiveActionTapListener(onActionTapListener: OnActionTapListener) = apply {
      this.onPositiveActionTapListener = onActionTapListener
    }

    /** Specifies the negative action text string */
    fun negativeActionText(text: String) = apply { this.negativeActionText = text }

    /** Specifies the negative action text string resource */
    fun negativeActionText(@StringRes actionTextId: Int) = apply {
      negativeActionText(activity.getString(actionTextId))
    }

    /** Specifies the negative action text string span */
    fun negativeActionText(actionText: Spanned) = apply {
      this.negativeActionTextSpanned = actionText
    }

    /** Specifies the negative action text typeface */
    fun negativeActionTextTypeface(typeface: Typeface) = apply {
      this.negativeActionTextTypeface = typeface
    }

    /** Specifies the negative action text size (in pixels) */
    fun negativeActionTextSizeInPx(size: Float) = apply { this.negativeActionTextSizeInPx = size }

    /** Specifies the negative action text size (in sp) */
    fun negativeActionTextSizeInSp(size: Float) = apply { this.negativeActionTextSizeInSp = size }

    /** Specifies the negative action text color */
    fun negativeActionTextColor(@ColorInt color: Int) = apply {
      this.negativeActionTextColor = color
    }

    /** Specifies the negative action text color resource */
    fun negativeActionTextColorRes(@ColorRes colorId: Int) = apply {
      this.negativeActionTextColor = ContextCompat.getColor(activity, colorId)
    }

    /** Specifies the negative action text appearance */
    fun negativeActionTextAppearance(@StyleRes appearance: Int) = apply {
      this.negativeActionTextAppearance = appearance
    }

    /** Sets listener to receive tap events on the negative action */
    fun negativeActionTapListener(onActionTapListener: OnActionTapListener) = apply {
      this.onNegativeActionTapListener = onActionTapListener
    }

    /** Specifies if the icon should be shown. Also configures its scale factor and scale type */
    @JvmOverloads
    fun showIcon(scale: Float = DEFAUT_ICON_SCALE, scaleType: ScaleType = CENTER_CROP) = apply {
      require(progressPosition != LEFT) { "Cannot show icon if left progress is set" }
        require(scale > 0) { "Icon scale cannot be negative or zero" }
    
        this.showIcon = true
      this.iconScale = scale
      this.iconScaleType = scaleType
    }

    /** Specifies the icon drawable */
    fun icon(icon: Drawable) = apply { this.iconDrawable = icon }

    /** Specifies the icon drawable resource */
    fun icon(@DrawableRes iconId: Int) = apply {
      this.iconDrawable = ContextCompat.getDrawable(activity, iconId)
    }

    /** Specifies the icon bitmap */
    fun icon(bitmap: Bitmap) = apply { this.iconBitmap = bitmap }

    /** Specifies the icon color filter and mode */
    @JvmOverloads
    fun iconColorFilter(@ColorInt color: Int, mode: PorterDuff.Mode? = null) = apply {
      this.iconColorFilter = color
      this.iconColorFilterMode = mode
    }

    /** Specifies the icon color filter resource and mode */
    @JvmOverloads
    fun iconColorFilterRes(@ColorRes colorId: Int, mode: PorterDuff.Mode? = null) = apply {
      this.iconColorFilter = ContextCompat.getColor(activity, colorId)
      this.iconColorFilterMode = mode
    }

    /** Specifies the icon builder */
    fun iconAnimation(builder: FlashAnimIconBuilder) = apply { this.iconAnimBuilder = builder }

    /** Specifies the gravity in which the indeterminate progress is shown (left/right) */
    fun showProgress(position: ProgressPosition) = apply {
      this.progressPosition = position

      if (progressPosition == LEFT && showIcon) {
        throw IllegalArgumentException("Cannot show progress at left if icon is already set")
      }

      if (progressPosition == RIGHT && primaryActionText != null) {
        throw IllegalArgumentException(
          "Cannot show progress at right if action button is already set"
        )
      }
    }

    /** Specifies the indeterminate progress tint */
    fun progressTint(@ColorInt color: Int) = apply { this.progressTint = color }

    /** Specifies the indeterminate progress tint resource */
    fun progressTintRes(@ColorRes colorId: Int) = apply {
      this.progressTint = ContextCompat.getColor(activity, colorId)
    }

    /** Builds a flashbar instance */
    fun build(): Flashbar {
      configureAnimation()
      val flashbar = Flashbar(this)
      flashbar.construct()
      return flashbar
    }

    /** Shows the flashbar */
    fun show() = build().show()

    private fun configureAnimation() {
      enterAnimBuilder =
        if (enterAnimBuilder == null) {
          when (gravity) {
            TOP -> FlashAnim.with(activity).animateBar().enter().fromTop()
            BOTTOM -> FlashAnim.with(activity).animateBar().enter().fromBottom()
          }
        } else {
          when (gravity) {
            TOP -> enterAnimBuilder!!.enter().fromTop()
            BOTTOM -> enterAnimBuilder!!.enter().fromBottom()
          }
        }

      exitAnimBuilder =
        if (exitAnimBuilder == null) {
          when (gravity) {
            TOP -> FlashAnim.with(activity).animateBar().exit().fromTop()
            BOTTOM -> FlashAnim.with(activity).animateBar().exit().fromBottom()
          }
        } else {
          when (gravity) {
            TOP -> exitAnimBuilder!!.exit().fromTop()
            BOTTOM -> exitAnimBuilder!!.exit().fromBottom()
          }
        }
    }
  }

  companion object {
    const val DURATION_SHORT = 1000L
    const val DURATION_LONG = 2500L
    const val DURATION_INDEFINITE = -1L
  }

  enum class Gravity {
    TOP,
    BOTTOM
  }

  enum class DismissEvent {
    TIMEOUT,
    MANUAL,
    TAP_OUTSIDE,
    SWIPE
  }

  enum class Vibration {
    SHOW,
    DISMISS
  }

  enum class ProgressPosition {
    LEFT,
    RIGHT
  }

  fun interface OnActionTapListener {
    fun onActionTapped(bar: Flashbar)
  }

  interface OnBarDismissListener {
    fun onDismissing(bar: Flashbar, isSwiped: Boolean)
    fun onDismissProgress(bar: Flashbar, progress: Float)
    fun onDismissed(bar: Flashbar, event: DismissEvent)
  }

  interface OnTapListener {
    fun onTap(flashbar: Flashbar)
  }

  interface OnBarShowListener {
    fun onShowing(bar: Flashbar)
    fun onShowProgress(bar: Flashbar, progress: Float)
    fun onShown(bar: Flashbar)
  }
}
