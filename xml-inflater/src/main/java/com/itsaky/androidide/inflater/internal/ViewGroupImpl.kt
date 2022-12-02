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
    notifyBeforeViewAdded(view)
    val idx = if (index < 0) childCount else index
    this.children.add(idx, view)
    this.view.addView(view.view, idx)
    view.parent = this
    notifyOnViewAdded(view)
  }

  override fun removeChild(view: IView) {
    notifyBeforeViewRemoved(view)
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

  override fun addOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.add(listener)
  }

  override fun removeOnHierarchyChangeListener(listener: OnHierarchyChangeListener) {
    this.hierarchyChangeListeners.remove(listener)
  }

  protected open fun notifyBeforeViewAdded(child: IView) {
    this.hierarchyChangeListeners.forEach { it.beforeViewAdded(this, child) }
  }

  protected open fun notifyBeforeViewRemoved(child: IView) {
    this.hierarchyChangeListeners.forEach { it.beforeViewRemoved(this, child) }
  }

  protected open fun notifyOnViewAdded(child: IView) {
    this.hierarchyChangeListeners.forEach { it.onViewAdded(this, child) }
  }

  protected open fun notifyOnViewRemoved(child: IView) {
    this.hierarchyChangeListeners.forEach { it.onViewRemoved(this, child) }
  }
}
