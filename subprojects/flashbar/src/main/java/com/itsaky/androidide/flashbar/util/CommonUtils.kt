package com.itsaky.androidide.flashbar.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.Surface.ROTATION_0
import android.view.Surface.ROTATION_180
import android.view.Surface.ROTATION_270
import android.view.Surface.ROTATION_90
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import com.itsaky.androidide.flashbar.util.NavigationBarPosition.BOTTOM
import com.itsaky.androidide.flashbar.util.NavigationBarPosition.LEFT
import com.itsaky.androidide.flashbar.util.NavigationBarPosition.RIGHT
import com.itsaky.androidide.flashbar.util.NavigationBarPosition.TOP
import kotlin.math.roundToInt

internal fun Activity.getStatusBarHeightInPx(): Int {
  val rectangle = Rect()

  window.decorView.getWindowVisibleDisplayFrame(rectangle)

  val statusBarHeight = rectangle.top
  val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
  return if (contentViewTop == 0) { // Actionbar is not present
    statusBarHeight
  } else {
    contentViewTop - statusBarHeight
  }
}

internal fun Activity.getNavigationBarPosition(): NavigationBarPosition {
  val display =
    if (Build.VERSION.SDK_INT >= 30) {
      display
    } else {
      @Suppress("DEPRECATION") windowManager.defaultDisplay
    }
  return when (display?.rotation) {
    ROTATION_0 -> BOTTOM
    ROTATION_90 -> RIGHT
    ROTATION_180 -> TOP
    ROTATION_270 -> LEFT
    else -> BOTTOM
  }
}

internal fun Activity.getNavigationBarSizeInPx(): Int {
  val realScreenSize = getRealScreenSize()
  val appUsableScreenSize = getAppUsableScreenSize()
  val navigationBarPosition = getNavigationBarPosition()

  return if (navigationBarPosition == LEFT || navigationBarPosition == RIGHT) {
    realScreenSize.x - appUsableScreenSize.x
  } else {
    realScreenSize.y - appUsableScreenSize.y
  }
}

internal fun Activity?.getRootView(): ViewGroup? {
  if (this == null || window == null) {
    return null
  }
  return window.decorView as ViewGroup
}

internal fun Context.convertDpToPx(dp: Int): Int {
  return (dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

internal fun Context.convertPxToDp(px: Int): Int {
  return (px / (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

private fun Activity.getRealScreenSize(): Point {
  val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val defaultDisplay =
    if (Build.VERSION.SDK_INT >= 30) {
      display
    } else {
      @Suppress("DEPRECATION") windowManager.defaultDisplay
    }

  val size = Point()
  defaultDisplay?.getRealSize(size)
  return size
}

private fun Activity.getAppUsableScreenSize(): Point {
  val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val defaultDisplay = windowManager.defaultDisplay
  val size = Point()
  defaultDisplay.getSize(size)
  return size
}

inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
  viewTreeObserver.addOnGlobalLayoutListener(
    object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        if (measuredWidth > 0 && measuredHeight > 0) {
          viewTreeObserver.removeOnGlobalLayoutListener(this)
          f()
        }
      }
    }
  )
}

internal enum class NavigationBarPosition {
  BOTTOM,
  RIGHT,
  LEFT,
  TOP
}
