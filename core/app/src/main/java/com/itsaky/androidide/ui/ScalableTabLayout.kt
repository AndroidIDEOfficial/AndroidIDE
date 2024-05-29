package com.itsaky.androidide.ui

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout

// https://stackoverflow.com/a/65605542
class ScalableTabLayout : TabLayout {
  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr)

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val tabLayout = getChildAt(0) as ViewGroup
    val childCount = tabLayout.childCount
    if (childCount > 0) {
      val widthPixels = MeasureSpec.getSize(widthMeasureSpec)
      val tabMinWidth = widthPixels / childCount
      var remainderPixels = widthPixels % childCount
      for (i in 0 until childCount) {
        val it = tabLayout.getChildAt(i)
        if (remainderPixels > 0) {
          it.minimumWidth = tabMinWidth + 1
          remainderPixels--
        } else {
          it.minimumWidth = tabMinWidth
        }
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }
}
