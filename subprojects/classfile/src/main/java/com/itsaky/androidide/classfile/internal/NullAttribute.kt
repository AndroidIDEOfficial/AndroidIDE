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

import com.itsaky.androidide.classfile.ClassFileVersion
import com.itsaky.androidide.classfile.IClassFile
import com.itsaky.androidide.classfile.attributes.IAttribute
import com.itsaky.androidide.classfile.attributes.IAttribute.Type
import java.io.DataInputStream

/**
 * **FOR INTERNAL USE ONLY**
 *
 * Represents a null attribute. Not present in a class file.
 *
 * @author Akash Yadav
 */
internal object NullAttribute : IAttribute {
  override val version: ClassFileVersion
    get() = throw UnsupportedOperationException()
  override val nameIndex: Int
    get() = throw UnsupportedOperationException()
  override val type: Type
    get() = throw UnsupportedOperationException()

  override fun read(attrLength: Int, file: IClassFile, input: DataInputStream) {
    throw UnsupportedOperationException()
  }
}
