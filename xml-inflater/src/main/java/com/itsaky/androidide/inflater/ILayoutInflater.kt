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

package com.itsaky.androidide.inflater

import android.view.ViewGroup
import com.itsaky.androidide.inflater.internal.LayoutInflaterImpl
import java.io.File

/**
 * An [ILayoutInflater] converts (inflates) XML layout to Android view which can be easily rendered.
 *
 * @author Akash Yadav
 */
abstract class ILayoutInflater {

  /** The listener which listens to layout inflation events. */
  abstract var inflationEventListener: IInflateEventsListener?

  /**
   * Inflate the given raw XML file.
   *
   * @param file The file to inflate.
   * @param parent The parent view.
   */
  abstract fun inflate(file: File, parent: ViewGroup): List<IView>
  
  companion object {
    @JvmStatic
    fun newInflater(): ILayoutInflater {
      return LayoutInflaterImpl()
    }
  }
}
