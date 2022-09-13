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
import java.io.DataInputStream

/**
 * **NOT SUPPORTED**
 *
 * A constant of type [IConstant.Type.DYNAMIC] or [IConstant.Type.INVOKE_DYNAMIC] in the constant
 * pool of class file.
 *
 * @property bootstrapMethodAttrIndex The index into the bootstrap_methods array of the bootstrap
 * method table of the class file.
 * @property nameTypeIndex The index of the name and type constant in the constant pool. The entry
 * at this index is always of type [IConstant.Type.NAME_AND_TYPE].
 * @author Akash Yadav
 */
class DynamicConstant(override val type: Type) :
  IConstant {

  var bootstrapMethodAttrIndex: Int = -1
    private set
  var nameTypeIndex: Int = -1
    private set

  override fun read(input: DataInputStream) {
    this.bootstrapMethodAttrIndex = input.readUnsignedShort()
    this.nameTypeIndex = input.readUnsignedShort()
  }
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DynamicConstant) return false
    
    if (type != other.type) return false
    if (bootstrapMethodAttrIndex != other.bootstrapMethodAttrIndex) return false
    if (nameTypeIndex != other.nameTypeIndex) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + bootstrapMethodAttrIndex
    result = 31 * result + nameTypeIndex
    return result
  }
  
  override fun toString(): String {
    return "DynamicConstant(type=$type, bootstrapMethodAttrIndex=$bootstrapMethodAttrIndex, nameTypeIndex=$nameTypeIndex)"
  }
}
