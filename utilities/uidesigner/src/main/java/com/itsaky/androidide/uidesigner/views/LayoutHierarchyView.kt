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

package com.itsaky.androidide.uidesigner.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils.TruncateAt.MIDDLE
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.core.view.updateMarginsRelative
import androidx.core.view.updatePaddingRelative
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.textview.MaterialTextView
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.uidesigner.R
import com.itsaky.androidide.utils.resolveAttr

/**
 * A view that shows component hierarchy in the UI Designer.
 *
 * @author Akash Yadav
 */
class LayoutHierarchyView
@JvmOverloads
constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

  private val textToIView = mutableMapOf<HierarchyText, IView>()
  private var onClick: ((IView) -> Unit)? = null

  private val clickListener = OnClickListener { view ->
    onClick?.let { click -> textToIView[view]?.let(click) }
  }

  private val paint = Paint()
  private val pointRadius = SizeUtils.dp2px(3f).toFloat()
  private val dp8 = SizeUtils.dp2px(8f)
  private val dp16 = dp8 * 2

  init {
    orientation = VERTICAL

    paint.color = context.resolveAttr(R.attr.colorOutline)
    paint.strokeWidth = SizeUtils.dp2px(1f).toFloat()
    paint.isAntiAlias = true
  }

  fun setupWithView(view: IView, onClick: ((IView) -> Unit)? = null) {
    removeAllViews()
    textToIView.clear()

    this.onClick = onClick
    addViews(view, 1)
  }

  private fun addViews(view: IView, depth: Int) {
    val text =
      HierarchyText(context, depth, dp16).apply {
        this.text = view.tag
        setOnClickListener(clickListener)
      }
    addView(text)

    if (view is com.itsaky.androidide.inflater.IViewGroup) {
      text.childCount = computeChildCount(view)
      view.forEach { addViews(it, depth + 1) }
    }

    textToIView[text] = view
  }

  private fun computeChildCount(view: com.itsaky.androidide.inflater.IViewGroup): Int {
    var count = view.childCount
    view.forEachIndexed { index, child ->
      if (index != view.childCount - 1 && child is com.itsaky.androidide.inflater.IViewGroup) {
        count += computeChildCount(child)
      }
    }
    return count
  }

  override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)

    if (childCount == 0) {
      return
    }

    for (i in 0 until childCount) {
      drawView(getChildAt(i) as HierarchyText, canvas)
    }
  }

  private fun drawView(view: HierarchyText, canvas: Canvas) {

    val left = view.left
    val top = view.top
    val h = view.height

    val mid = top + (h / 2)
    val pX = left.toFloat()
    val pY = mid.toFloat()

    if (view.hasChildren) {
      canvas.drawRect(
        left - pointRadius,
        mid - pointRadius,
        left + pointRadius,
        mid + pointRadius,
        paint
      )

      val endY = pY + (h * view.childCount)
      canvas.drawLine(pX, pY, pX, endY, paint)
    } else {
      canvas.drawCircle(pX, pY, pointRadius, paint)
    }
    if (view.depth > 1) {
      canvas.drawLine(pX - dp16, pY, pX, pY, paint)
    }
  }

  @SuppressLint("ViewConstructor")
  class HierarchyText(context: Context, var depth: Int, private val offset: Int) :
    MaterialTextView(context) {

    var childCount = 0
    val hasChildren: Boolean
      get() = childCount > 0

    init {
      maxLines = 1
      isClickable = true
      isFocusable = true
      ellipsize = MIDDLE
      gravity = Gravity.CENTER_VERTICAL
      layoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48f)).apply {
          updateMarginsRelative(start = depth * offset)
        }
      setBackgroundResource(R.drawable.bg_ripple)
      setTextAppearance(R.style.TextAppearance_Material3_BodyMedium)
      setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
      updatePaddingRelative(start = offset, end = offset)
    }
  }
}
