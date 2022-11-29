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

package com.itsaky.androidide.inflater.internal

import android.graphics.RectF
import android.view.ViewGroup
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.IViewGroup.OnHierarchyChangeListener
import com.itsaky.androidide.inflater.IViewGroupAdapter
import com.itsaky.androidide.inflater.LayoutBehavior.HORIZONTAL
import com.itsaky.androidide.inflater.LayoutBehavior.TOP_LEFT
import com.itsaky.androidide.inflater.LayoutBehavior.VERTICAL

open class ViewGroupImpl(file: LayoutFile, name: String, view: ViewGroup) :
  ViewImpl(file = file, name = name, view = view), IViewGroup {

  protected val children = mutableListOf<IView>()
  protected val hierarchyChangeListeners = mutableListOf<OnHierarchyChangeListener>()
  override val childCount: Int
    get() = children.size

  // For easy access
  override val view: ViewGroup
    get() = super.view as ViewGroup

  override fun addChild(view: IView) {
    addChild(-1, view)
  }

  override fun addChild(index: Int, view: IView) {
    if (view.parent != null) {
      throw IllegalStateException("View already has a parent")
    }
    val idx = if (index < 0) childCount else index
    this.children.add(idx, view)
    this.view.addView(view.view, idx)
    view.parent = this
    notifyOnViewAdded(view)
  }

  override fun removeChild(view: IView) {
    this.view.removeView(view.view)
    this.children.remove(view)
    view.parent = null
    notifyOnViewRemoved(view)
  }

  override fun removeChild(index: Int) {
    removeChild(this.children[index])
  }

  override operator fun get(index: Int): IView {
    return this.children[index]
  }

  override operator fun set(index: Int, view: IView): IView {
    val existing = this.children[index]
    removeChild(existing)
    addChild(index, view)
    return existing
  }

  override fun printHierarchy(builder: StringBuilder, indent: Int) {
    super.printHierarchy(builder, indent)
    for (child in children) {
      (child as ViewImpl).printHierarchy(builder, indent + 1)
    }
  }

  override fun computeViewIndex(x: Float, y: Float): Int {
    if (childCount == 0) {
      return 0
    }
    val adapter =
      ViewAdapterIndex.getAdapter(name) as? IViewGroupAdapter
        ?: throw IllegalStateException("No view adapter for '$name'")
    return when (adapter.getLayoutBehavior(this)) {
      TOP_LEFT -> childCount
      VERTICAL -> computeViewIndexVertically(x, y)
      HORIZONTAL -> computeViewIndexHorizontally(x, y)
    }
  }

  private fun computeViewIndexHorizontally(x: Float, y: Float): Int {
    get(0).apply {
      val rect = getViewRect(this)
      if (x < rect.left) {
        return 0
      }
    }
    get(childCount - 1).apply {
      val rect = getViewRect(this)
      if (x > rect.right) {
        return childCount
      }
    }
    val (child, index) = findNearestChild(x, y, false) ?: return childCount
    val rect = getViewRect(child)
    val mid = rect.left + (rect.width() / 2)
    val left = RectF(rect).apply { this.right = mid - 10 }
    val right = RectF(rect).apply { this.left = mid + 10 }
    if (x > left.left && x < left.right) {
      return index - 1
    }
    if (x > right.left && x < right.right) {
      return index + 1
    }
    return index
  }

  private fun computeViewIndexVertically(x: Float, y: Float): Int {
    get(0).apply {
      val rect = getViewRect(this)
      if (y < rect.top) {
        return 0
      }
    }
    get(childCount - 1).apply {
      val rect = getViewRect(this)
      if (y > rect.bottom) {
        return childCount
      }
    }
    val (child, index) = findNearestChild(x, y, true) ?: return childCount
    val rect = getViewRect(child)
    val mid = rect.top + (rect.height() / 2)
    val top = RectF(rect).apply { this.bottom = mid - 10 }
    val bottom = RectF(rect).apply { this.top = mid + 10 }
    if (y > top.top && y < top.bottom) {
      return index - 1
    }
    if (y > bottom.top && y < bottom.bottom) {
      return index + 1
    }
    return index
  }

  private fun findNearestChild(x: Float, y: Float, vertical: Boolean = true): Pair<IView, Int>? {
    for (i in 0 until childCount) {
      val child = get(i) as ViewImpl
      if (!child.includeInIndexComputation) {
        continue
      }
      val rect = getViewRect(child)
      if (vertical && (y > rect.top && y < rect.bottom)) {
        return child to i
      }

      if (!vertical && (x > rect.left && x < rect.right)) {
        return child to i
      }
    }

    return null
  }

  override fun addOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.add(listener)
  }

  override fun removeOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.remove(listener)
  }

  protected open fun notifyOnViewAdded(child: IView) {
    this.hierarchyChangeListeners.forEach { it.onViewAdded(this, child) }
  }

  protected open fun notifyOnViewRemoved(child: IView) {
    this.hierarchyChangeListeners.forEach { it.onViewRemoved(this, child) }
  }

  protected open fun getViewRect(view: IView): RectF {
    val v = view.view
    val rect = RectF()
    rect.left = v.left.toFloat()
    rect.top = v.top.toFloat()
    rect.right = rect.left + v.width
    rect.bottom = rect.top + v.height
    return rect
  }

  private fun topHalf(src: RectF): RectF {
    val result = RectF(src)
    result.bottom -= result.height() / 2
    return src
  }

  private fun bottomHalf(src: RectF): RectF {
    val result = RectF(src)
    result.top += result.height() / 2
    return src
  }
}
