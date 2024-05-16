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

package com.itsaky.androidide.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.widget.RelativeLayout
import androidx.annotation.CallSuper
import androidx.annotation.FloatRange
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.customview.widget.ViewDragHelper
import com.itsaky.androidide.R
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * A layout which can be dragged vertically to reveal a hidden content.
 *
 * @author Akash Yadav
 */
open class SwipeRevealLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

  /**
   * Interface for listening to drag events.
   */
  interface OnDragListener {

    /**
     * Called when the drag state changes.
     */
    fun onDragStateChanged(swipeRevealLayout: SwipeRevealLayout, state: Int)

    /**
     * Called when the drag progress changes.
     */
    fun onDragProgress(swipeRevealLayout: SwipeRevealLayout, progress: Float)
  }

  private val onLayoutChangeListener = OnLayoutChangeListener { v, _, _, _, _, oldLeft, oldTop, oldRight, oldBottom ->
    if (isDragging) {
      v.left = oldLeft
      v.top = oldTop
      v.right = oldRight
      v.bottom = oldBottom
    }
  }

  private val dragHelperCallback = object : ViewDragHelper.Callback() {
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return true
    }

    override fun getOrderedChildIndex(index: Int): Int {
      val idx = dragHandleContainerView?.let(this@SwipeRevealLayout::indexOfChild) ?: index
      if (idx > index) {
        return idx
      }

      return index
    }

    override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
      draggingViewTop = top
      onDragProgress(min(0f, max(1f, top.toFloat() / dragRangeVertical.toFloat())))
    }

    override fun getViewVerticalDragRange(child: View): Int {
      return dragRangeVertical
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      return min(max(top, paddingTop), dragRangeVertical)
    }

    override fun onViewDragStateChanged(state: Int) {
      if (state == draggingState) {
        return
      }

      if (isDragging && state == ViewDragHelper.STATE_IDLE && draggingViewTop == dragRangeVertical) {
          isOpen = false
      }

      draggingState = state
      onDragStateChanged(draggingState)
    }

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      if (draggingViewTop == 0) {
        isOpen = false
        return
      }

      if (draggingViewTop == dragRangeVertical) {
        isOpen = true
        return
      }

      // whether the view should settle to open or close
      val settleDestY = if (yvel > AUTO_OPEN_VELOCITY_LIM || draggingViewTop > dragRangeVertical / 2) {
        dragRangeVertical
      } else {
        dragLowerLimit
      }

      if (dragHelper.settleCapturedViewAt(0, settleDestY)) {
        left = 0
        this@SwipeRevealLayout.postInvalidateOnAnimation()
      }
    }
  }

  private var dragHandleView: View? = null
    get() {
      if (field == null) {
        field = findViewById(dragHandleViewId)
      }
      return field
    }

  private var dragHandleContainerView: View? = null
    get() {
      if (field == null) {
        field = findViewById<View?>(dragHandleContainerViewId)?.also {
          it.removeOnLayoutChangeListener(onLayoutChangeListener)
          it.addOnLayoutChangeListener(onLayoutChangeListener)
        }
      }

      if (field == null) {
        field = dragHandleView
      }

      return field
    }

  private var dragHiddenContent: View? = null
    get() {
      if (field == null) {
        field = findViewById(dragHiddenContentId)
      }
      return field
    }

  private var isOpen = false

  private var draggingState = -1
  private var draggingViewTop = 0
  private var dragRangeVertical = 0
    get() = dragHiddenContent?.height ?: maxDragHeight.takeIf { it >= 0 } ?: field

  private lateinit var dragHelper: ViewDragHelper

  /**
   * Whether the view is currently in 'dragging' state.
   */
  val isDragging: Boolean
    get() = draggingState == ViewDragHelper.STATE_DRAGGING ||
        draggingState == ViewDragHelper.STATE_SETTLING

  /**
   * The ID of the view which will be dragged to reveal the content.
   */
  @IdRes
  var dragHandleViewId = 0

  /**
   * The ID of the outermost view containing [dragHandleViewId] which will be dragged.
   */
  @IdRes
  var dragHandleContainerViewId = 0

  /**
   * The ID of the view which is shown when the view is open.
   */
  @IdRes
  var dragHiddenContentId = 0

  /**
   * The % of this view's height at which the dragged view will be settled after open.
   */
  @FloatRange(from = 0.0, to = 1.0)
  var draggedViewSettleRatio = 0.0

  /**
   * The maximum height of the drag container view when it is open.
   */
  @IntRange(from = -1)
  var maxDragHeight = -1

  /**
   * The minimum height of the drag container view when it is closed.
   */
  @IntRange(from = 0)
  var dragLowerLimit = 0

  /**
   * Listener for drag events.
   */
  var dragListener: OnDragListener? = null

  companion object {
    @Suppress("UNUSED")
    const val STATE_IDLE = ViewDragHelper.STATE_IDLE
    @Suppress("UNUSED")
    const val STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING
    @Suppress("UNUSED")
    const val STATE_SETTLING = ViewDragHelper.STATE_SETTLING

    const val AUTO_OPEN_VELOCITY_LIM = 800.0

    @FloatRange(from = 0.0, to = 1.0)
    const val DEFAULT_DRAGGED_VIEW_SETTLE_RATIO = 0.5
  }

  init {
    if (attrs != null) {
      val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeRevealLayout,
        defStyleAttr, defStyleRes)
      dragHandleViewId = typedArray.getResourceId(R.styleable.SwipeRevealLayout_dragHandle,
        dragHandleViewId)
      dragHandleContainerViewId = typedArray.getResourceId(
        R.styleable.SwipeRevealLayout_dragHandleContainer, dragHandleContainerViewId)
      dragHiddenContentId = typedArray.getResourceId(
        R.styleable.SwipeRevealLayout_dragHiddenContentLayout, dragHiddenContentId)
      typedArray.recycle()
    }
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    this.dragHelper = ViewDragHelper.create(this, dragHelperCallback)
    this.isOpen = false

    // call the getter to initialize the view and set the listener
    dragHandleContainerView
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    dragRangeVertical = (h * draggedViewSettleRatio).roundToInt()
    if (dragRangeVertical <= 0) {
      dragRangeVertical = (h * DEFAULT_DRAGGED_VIEW_SETTLE_RATIO).roundToInt()
    }
    super.onSizeChanged(w, h, oldw, oldh)
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return isInTargetView(ev) && dragHelper.shouldInterceptTouchEvent(ev)
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (isInTargetView(event) || isDragging) {
      dragHelper.processTouchEvent(event)
      return true
    }

    return super.onTouchEvent(event)
  }

  override fun computeScroll() {
    if (dragHelper.continueSettling(true)) {
      postInvalidateOnAnimation()
    }
  }

  /**
   * Internal callback. Invoked when the drag state changes.
   */
  @CallSuper
  protected open fun onDragStateChanged(state: Int) {
    dragListener?.onDragStateChanged(this, state)
  }

  /**
   * Internal callback. Invoked when the drag progress changes.
   */
  @CallSuper
  protected open fun onDragProgress(progress: Float) {
    dragListener?.onDragProgress(this, progress)
  }

  /**
   * Whether the given motion event lies within the target view.
   */
  private fun isInTargetView(ev: MotionEvent): Boolean {
    val queenLocation = IntArray(2)
    dragHandleView!!.getLocationOnScreen(queenLocation)
    val upperLimit: Int = queenLocation[1] + dragHandleView!!.measuredHeight
    val lowerLimit = queenLocation[1]
    val y = ev.rawY.toInt()
    return y in (lowerLimit + 1)..<upperLimit
  }
}