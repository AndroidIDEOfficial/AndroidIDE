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
import com.itsaky.androidide.classfile.constants.IConstant.Type.METHOD_HANDLE
import com.itsaky.androidide.classfile.constants.MethodHandleConstant.ReferenceKind.UNKNOWN
import java.io.DataInputStream

/**
 * A method handle constant in the constant pool of class file.
 *
 * @property referenceKind The kind of the reference.
 * @property referenceIndex The index of the reference constant in the constant pool. The type of
 * the entry in the constant pool at this index depends on [referenceKind].
 * @author Akash Yadav
 */
class MethodHandleConstant : IConstant {
  override val type: Type = METHOD_HANDLE
  
  var referenceKind: ReferenceKind = UNKNOWN
    private set
  var referenceIndex: Int = -1
    private set

  @Suppress("EnumEntryName")
  enum class ReferenceKind(val kind: Int) {
    REF_getField(1),
    REF_getStatic(2),
    REF_putField(3),
    REF_putStatic(4),
    REF_invokeVirtual(5),
    REF_invokeStatic(6),
    REF_invokeSpecial(7),
    REF_newInvokeSpecial(8),
    REF_invokeInterface(9),
  
    /**
     * FOR INTERNAL USE ONLY!
     */
    UNKNOWN(-1)
  }
  
  override fun read(input: DataInputStream) {
    this.referenceKind = kindFor(input.readUnsignedByte())
    this.referenceIndex = input.readUnsignedShort()
  }
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MethodHandleConstant) return false
    
    if (type != other.type) return false
    if (referenceKind != other.referenceKind) return false
    if (referenceIndex != other.referenceIndex) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + referenceKind.hashCode()
    result = 31 * result + referenceIndex
    return result
  }
  
  override fun toString(): String {
    return "MethodHandleConstant(type=$type, referenceKind=$referenceKind, referenceIndex=$referenceIndex)"
  }
  
  companion object {
    @JvmStatic
    fun kindFor(kind: Int): ReferenceKind {
      for (value in ReferenceKind.values()) {
        if (value != UNKNOWN && value.kind == kind) {
          return value
        }
      }
      throw IllegalArgumentException("Invalid reference kind: '$kind'")
    }
  }
}
