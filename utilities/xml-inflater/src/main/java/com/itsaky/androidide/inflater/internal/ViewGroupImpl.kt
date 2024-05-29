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

import android.view.ViewGroup
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.IViewGroup.OnHierarchyChangeListener

open class ViewGroupImpl(file: LayoutFile, name: String, view: ViewGroup) :
  ViewImpl(file = file, name = name, view = view), IViewGroup {

  internal var childrenModifiable = true

  protected val children = mutableListOf<IView>()
  protected val hierarchyChangeListeners = mutableListOf<OnHierarchyChangeListener>()
  override val childCount: Int
    get() = children.size

  // For easy access
  override val view: ViewGroup
    get() = super.view as ViewGroup

  override fun canModifyChildViews(): Boolean {
    return this.childrenModifiable
  }

  override fun addChild(view: IView) {
    addChild(-1, view)
  }

  override fun addChild(index: Int, view: IView) {
    if (!canModifyChildViews()) {
      throw UnsupportedOperationException("'$name' does not support adding child views.")
    }

    if (view.parent != null) {
      throw IllegalStateException("View already has a parent")
    }
    notifyBeforeViewAdded(view, index)
    val idx = if (index < 0) childCount else index
    this.children.add(idx, view)
    this.view.addView(view.view, idx)
    view.parent = this
    notifyOnViewAdded(view, index)
  }

  override fun removeChild(view: IView) {
    if (!canModifyChildViews()) {
      throw UnsupportedOperationException("'$name' does not support removing child views.")
    }

    val index = indexOfChild(view)
    notifyBeforeViewRemoved(view, index)
    this.view.removeView(view.view)
    this.children.remove(view)
    view.parent = null
    notifyOnViewRemoved(view, index)
  }

  override fun removeChild(index: Int) {
    removeChild(this.children[index])
  }

  override operator fun get(index: Int): IView {
    return this.children[index]
  }

  override operator fun set(index: Int, view: IView): IView {
    if (!canModifyChildViews()) {
      throw UnsupportedOperationException("'$name' does not support updating child views.")
    }

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

  override fun addOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.add(listener)
  }

  override fun removeOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.remove(listener)
  }

  override fun iterator(): Iterator<IView> {
    return ViewIterator(immutable())
  }

  protected open fun notifyBeforeViewAdded(child: IView, index: Int) {
    this.hierarchyChangeListeners.forEach { it.beforeViewAdded(this, child, index) }
  }

  protected open fun notifyBeforeViewRemoved(child: IView, index: Int) {
    this.hierarchyChangeListeners.forEach { it.beforeViewRemoved(this, child, index) }
  }

  protected open fun notifyOnViewAdded(child: IView, index: Int) {
    this.hierarchyChangeListeners.forEach { it.onViewAdded(this, child, index) }
  }

  protected open fun notifyOnViewRemoved(child: IView, index: Int) {
    this.hierarchyChangeListeners.forEach { it.onViewRemoved(this, child, index) }
  }

  override fun immutable(): IViewGroup {
    return ImmutableViewGroupImpl(this)
  }

  private class ViewIterator(private val group: IViewGroup) : Iterator<IView> {

    private var index = 0

    override fun hasNext(): Boolean {
      return index < group.childCount
    }

    override fun next(): IView {
      if (index < 0 || index >= group.childCount) {
        throw NoSuchElementException()
      }

      return group[index].also { ++index }
    }
  }
}
