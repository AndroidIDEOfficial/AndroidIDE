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

package com.itsaky.androidide.uidesigner.models

import android.view.ViewGroup
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.LayoutFile

/**
 * [UiViewGroup] implementation that is used for the root workspace view.
 *
 * @author Akash Yadav
 */
internal class RootWorkspaceView(
  file: LayoutFile,
  name: String,
  view: ViewGroup
) :
  UiViewGroup(file, name, view) {

  override fun canAcceptChild(name: String, child: IView?): Boolean {
    return childCount == 0 && super.canAcceptChild(name, child)
  }
}