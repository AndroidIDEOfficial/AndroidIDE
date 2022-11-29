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

import android.view.View
import com.itsaky.androidide.inflater.internal.LayoutFile
import java.io.File

/**
 * View which acts as a placeholder to show the position where the dragged view will be added.
 *
 * @author Akash Yadav
 */
internal class PlaceholderView(view: View) :
  UiView(LayoutFile(File(""), ""), View::class.qualifiedName!!, view) {
  override var includeInIndexComputation: Boolean = false
}
