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
    removeInternal(view)
  }

  override fun removeChild(index: Int): IView {
    val existing = this.children.removeAt(index)
    removeInternal(existing, false)
    return existing
  }

  override operator fun get(index: Int): IView {
    return this.children[index]
  }

  override operator fun set(index: Int, view: IView): IView {
    val existing = removeChild(index)
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
    val count = childCount
    for (i in 0 until childCount) {
      val child = this[i]
      val rect = getViewRect(child)
      if (rect.contains(x, y)) {
        val top = topHalf(rect)
        val bottom = bottomHalf(rect)
        if (top.contains(x, y)) {
          return 0.coerceAtLeast(i - 1)
        } else if (bottom.contains(x, y)) {
          return count.coerceAtMost(i + 1)
        }
      }
    }
    // If we don't find a suitable index, return the last index
    return count
  }
  
  override fun addOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.add(listener)
  }
  
  override fun removeOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.remove(listener)
  }
  
  protected open fun notifyOnViewAdded(child: IView) {
    this.hierarchyChangeListeners.forEach {
      it.onViewAdded(this, child)
    }
  }
  
  protected open fun notifyOnViewRemoved(child: IView) {
    this.hierarchyChangeListeners.forEach {
      it.onViewRemoved(this, child)
    }
  }
  
  private fun removeInternal(view: IView, removeFromList: Boolean = true) {
    if (removeFromList) {
      this.children.remove(view)
    }
    this.view.removeView(view.view)
    view.parent = null
    notifyOnViewRemoved(view)
  }

  private fun getViewRect(view: IView): RectF {
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
