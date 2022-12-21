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

package com.itsaky.androidide.classfile.attributes

import java.io.DataInputStream

/**
 * Annotation model for (in)visible type annotations.
 *
 * @author Akash Yadav
 */
class TypeAnnotation {

  lateinit var targetInfo: TargetInfo
    internal set

  lateinit var typePath: TypePath
    internal set

  /**
   * Index of the entry in the constant pool which corresponds to the type of the the annotation.
   * The entry at this index is always an UTF-8 constant.
   */
  var typeIndex: Int = -1
    internal set

  /** The element value pairs. */
  var elementValues: Array<Pair<Int, ElementValue>> = emptyArray()
    internal set

  companion object {
    internal fun read(input: DataInputStream): TypeAnnotation {
      val annotation = TypeAnnotation()
      val targetType = input.readUnsignedByte()
      annotation.targetInfo = TargetInfo.forType(targetType).apply { read(input) }
      annotation.typePath = TypePath().apply { read(input) }
      annotation.typeIndex = input.readUnsignedShort()
      val valueCount = input.readUnsignedShort()
      annotation.elementValues =
        Array(valueCount) { input.readUnsignedShort() to ElementValue.read(input) }
      return annotation
    }
  }
}
