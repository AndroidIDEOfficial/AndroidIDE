package com.itsaky.androidide.flashbar.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.itsaky.androidide.flashbar.R
import com.itsaky.androidide.flashbar.view.ShadowView.ShadowType.BOTTOM
import com.itsaky.androidide.flashbar.view.ShadowView.ShadowType.TOP

internal class ShadowView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
  View(context, attrs, defStyleAttr) {

  internal fun applyShadow(type: ShadowType) {
    when (type) {
      TOP -> setShadow(R.drawable.shadow_top)
      BOTTOM -> setShadow(R.drawable.shadow_bottom)
    }
  }

  private fun setShadow(@DrawableRes id: Int) {
    val shadow = ContextCompat.getDrawable(context, id)
    this.background = shadow
  }

  enum class ShadowType {
    TOP,
    BOTTOM
  }
}
