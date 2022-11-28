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
import android.view.ViewGroup
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.internal.ViewAdapterIndex.getAdapter
import com.itsaky.androidide.inflater.internal.LayoutFile
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.ViewFactory.generateLayoutParams
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment.Companion.DRAGGING_WIDGET_MIME
import com.itsaky.androidide.uidesigner.models.UiWidget
import java.io.File

/**
 * Listens for drag events in the given view group.
 *
 * @author Akash Yadav
 */
class WidgetDragListener(val view: IViewGroup, private val placeholderView: View) :
  View.OnDragListener {

  private val placeholder by lazy { ViewImpl(LayoutFile(File(""), ""), "", placeholderView) }

  override fun onDrag(v: View, event: DragEvent): Boolean {
    return when (event.action) {
      DragEvent.ACTION_DRAG_STARTED -> {
        event.clipDescription.hasMimeType(DRAGGING_WIDGET_MIME)
      }
      DragEvent.ACTION_DRAG_ENTERED,
      DragEvent.ACTION_DRAG_LOCATION -> {
        if (event.action == DragEvent.ACTION_DRAG_ENTERED) {
          view.onHighlightStateUpdated(true)
        }
        placeholder.removeFromParent()
        val index = view.computeViewIndex(event.x, event.y)
        view.addChild(index, placeholder)
        true
      }
      DragEvent.ACTION_DRAG_EXITED -> {
        view.onHighlightStateUpdated(false)
        true
      }
      DragEvent.ACTION_DROP -> {
        val child =
          when (val data = event.localState) {
            is IView -> {
              data.removeFromParent()
              data
            }
            is UiWidget ->
              data.createView(view.viewGroup.context, (view as ViewImpl).file).apply {
                this.view.layoutParams =
                  generateLayoutParams(this@WidgetDragListener.view.viewGroup)
                val adapter = getAdapter(this.name)
                adapter?.applyBasic(this)
              }
            else -> throw IllegalArgumentException("A local state of UiWidget or IView is expected")
          }

        val index = view.indexOfChild(this.placeholder)
        this.placeholder.removeFromParent()

        this.view.addChild(index, child)
        this.view.onHighlightStateUpdated(false)

        child.onHighlightStateUpdated(false)

        true
      }
      else -> false
    }
  }

  private val IViewGroup.viewGroup: ViewGroup
    get() = view as ViewGroup
}
