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
import com.itsaky.androidide.classfile.constants.IConstant.Type.UTF8
import java.io.DataInputStream

/**
 * A UTF-8 constant in the constant pool.
 *
 * @property bytes The bytes of this UTF-8 constant. This always has a length of
 * [Utf8Constant.length].
 * @author Akash Yadav
 */
class Utf8Constant : IConstant {
  override val type: Type = UTF8
  var bytes: ByteArray = ByteArray(0)
    private set

  /** Get the string represented by this constant. */
  fun content() = this.bytes.decodeToString()

  override fun read(input: DataInputStream) {
    val length = input.readUnsignedShort()
    this.bytes = ByteArray(length)
    input.readFully(bytes)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Utf8Constant) return false

    if (type != other.type) return false
    if (!bytes.contentEquals(other.bytes)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + bytes.contentHashCode()
    return result
  }

  override fun toString(): String {
    return "Utf8Constant(type=$type, bytes=${bytes.contentToString()}, bytesAsString=${content()})"
  }
}
