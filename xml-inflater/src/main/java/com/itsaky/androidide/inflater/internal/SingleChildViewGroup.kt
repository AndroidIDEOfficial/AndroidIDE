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

/**
 * A view group which can have only one child view.
 *
 * @author Akash Yadav
 */
open class SingleChildViewGroup(file: LayoutFile, name: String, view: ViewGroup) :
  ViewGroupImpl(file, name, view) {

  override fun addChild(index: Int, view: IView) {
    if (childCount == 1) {
      throw IllegalStateException("There can be only one child")
    }
    super.addChild(index, view)
  }
}
