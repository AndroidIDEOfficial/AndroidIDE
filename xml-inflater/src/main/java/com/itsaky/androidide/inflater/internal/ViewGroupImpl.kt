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
import com.android.aaptcompiler.XmlResource
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import java.io.File

open class ViewGroupImpl(file: LayoutFile, name: String, view: ViewGroup) :
  ViewImpl(file = file, name = name, view = view), IViewGroup {

  protected val children = mutableListOf<IView>()
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
  }

  override fun removeChild(view: IView) {
    this.children.remove(view)
    this.view.removeView(view.view)
  }

  override fun removeChild(index: Int): IView {
    val existing = this.children.removeAt(index)
    this.view.removeViewAt(index)
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
}
