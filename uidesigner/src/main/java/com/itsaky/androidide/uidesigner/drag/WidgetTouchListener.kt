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

package com.itsaky.androidide.uidesigner.drag

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.view.ViewCompat
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment

/**
 * Touch listener for inflated widgets.
 *
 * @author Akash Yadav
 */
internal class WidgetTouchListener(
  private val view: com.itsaky.androidide.inflater.IView,
  context: Context,
  private val onClick: (com.itsaky.androidide.inflater.IView) -> Boolean = { false }
) : OnTouchListener {

  private var touchedView: View? = null

  private val gestureDetector by lazy {
    GestureDetector(
      context,
      object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
          return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
          return onClick(this@WidgetTouchListener.view)
        }

        override fun onLongPress(e: MotionEvent) {
          touchedView
            ?: throw IllegalStateException("Received onLongPress event but 'touchedView' is null")
          val shadow = WidgetDragShadowBuilder(touchedView!!)
          val dataItem = ClipData.Item(DesignerWorkspaceFragment.DRAGGING_WIDGET)
          val data =
            ClipData(
              DesignerWorkspaceFragment.DRAGGING_WIDGET,
              arrayOf(DesignerWorkspaceFragment.DRAGGING_WIDGET_MIME),
              dataItem
            )
          ViewCompat.startDragAndDrop(touchedView!!, data, shadow, view, 0)
        }
      }
    )
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouch(v: View, event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      this.touchedView = v
      view.onHighlightStateUpdated(true)
    } else if (event.action == MotionEvent.ACTION_UP) {
      this.touchedView = null
      view.onHighlightStateUpdated(false)
    }
    return gestureDetector.onTouchEvent(event)
  }
}
