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

package com.itsaky.androidide.classfile.constants

import com.itsaky.androidide.classfile.constants.IConstant.Type
import com.itsaky.androidide.classfile.constants.IConstant.Type.DOUBLE
import java.io.DataInputStream

/**
 * A double constant in the constant pool of class file.
 *
 * @property value The value of this constant.
 * @author Akash Yadav
 */
class DoubleConstant : IConstant {

  override val type: Type = DOUBLE

  var value: Double = 0.0
    private set

  override fun read(input: DataInputStream) {
    this.value = input.readDouble()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DoubleConstant) return false

    if (type != other.type) return false
    if (value != other.value) return false

    return true
  }

  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + value.hashCode()
    return result
  }

  override fun toString(): String {
    return "DoubleConstant(type=$type, value=$value)"
  }
}
