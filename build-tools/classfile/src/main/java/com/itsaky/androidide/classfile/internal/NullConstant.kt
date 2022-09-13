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

package com.itsaky.androidide.classfile.internal

import com.itsaky.androidide.classfile.constants.IConstant
import com.itsaky.androidide.classfile.constants.IConstant.Type
import com.itsaky.androidide.classfile.constants.IConstant.Type.UNKNOWN
import java.io.DataInputStream

/** @author Akash Yadav */
object NullConstant : IConstant {
  override val type: Type = UNKNOWN

  override fun read(input: DataInputStream) {
    throw UnsupportedOperationException()
  }
}
