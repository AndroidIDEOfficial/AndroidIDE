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

import android.view.DragEvent
import android.view.View
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.inflater.viewGroup
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment.Companion.DRAGGING_WIDGET_MIME
import com.itsaky.androidide.uidesigner.models.UiView
import com.itsaky.androidide.uidesigner.models.UiViewGroup
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

/**
 * Listens for drag events in the given view group.
 *
 * @author Akash Yadav
 */
internal class WidgetDragListener(
  val view: UiViewGroup,
  private val placeholder: IView,
  private val touchSlop: Int
) : View.OnDragListener {

  private var lastX = 0f
  private var lastY = 0f

  override fun onDrag(v: View, event: DragEvent): Boolean {
    when (event.action) {
      DragEvent.ACTION_DRAG_STARTED -> {

        val name =
          when (val data = event.localState) {
            is IView -> data.name
            is UiWidget -> data.name
            else -> throw IllegalArgumentException("A local state of UiWidget or IView is expected")
          }
 
        return event.clipDescription.hasMimeType(DRAGGING_WIDGET_MIME) &&
          event.localState != view &&
          view.canAcceptChild(name, event.localState as? IView?)
      }
      DragEvent.ACTION_DRAG_ENTERED,
      DragEvent.ACTION_DRAG_LOCATION -> {

        if (event.action == DragEvent.ACTION_DRAG_ENTERED) {
          view.onHighlightStateUpdated(true)
        }

        val distX = event.x - lastX
        val distY = event.y - lastY
        if (distX.absoluteValue < touchSlop && distY.absoluteValue < touchSlop) {
          return true
        }

        placeholder.removeFromParent()
        val state = event.localState
        if (state is UiView) {
          state.includeInIndexComputation = false
        }

        val index = view.computeViewIndex(event.x, event.y)
        view.addChild(index, placeholder)

        lastX = event.x
        lastY = event.y

        return true
      }
      DragEvent.ACTION_DRAG_EXITED -> {
        view.onHighlightStateUpdated(false)
        return true
      }
      DragEvent.ACTION_DROP -> {
        val child =
          when (val data = event.localState) {
            is IView -> {
              data.removeFromParent()
              data
            }
            is UiWidget -> data.createView(view.viewGroup.context, view.viewGroup, view.file)
            else -> throw IllegalArgumentException("A local state of UiWidget or IView is expected")
          }

        var index = view.indexOfChild(this.placeholder)
        this.placeholder.removeFromParent()
        index = min(max(0, index), view.childCount)

        this.view.addChild(index, child)
        this.view.onHighlightStateUpdated(false)

        if (child is UiView) {
          child.includeInIndexComputation = true
        }

        return true
      }
      DragEvent.ACTION_DRAG_ENDED -> {
        this.placeholder.removeFromParent()
        return true
      }
      else -> return false
    }
  }
}
