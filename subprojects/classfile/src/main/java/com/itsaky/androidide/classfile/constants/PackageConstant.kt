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
import com.itsaky.androidide.classfile.constants.IConstant.Type.MODULE
import com.itsaky.androidide.classfile.constants.IConstant.Type.PACKAGE
import java.io.DataInputStream

/**
 * A package info constant in the constant pool of class file.
 *
 * @property nameIndex The index of the name constant the constant pool. The entry at this index is always of type [IConstant.Type.UTF8].
 * @author Akash Yadav
 */
class PackageConstant : IConstant {
  override val type: Type = PACKAGE
  var nameIndex: Int = -1
    private set
  
  override fun read(input: DataInputStream) {
    this.nameIndex = input.readUnsignedShort()
  }
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PackageConstant) return false
    
    if (type != other.type) return false
    if (nameIndex != other.nameIndex) return false
    
    return true
  }
  
  override fun hashCode(): Int {
    var result = type.hashCode()
    result = 31 * result + nameIndex
    return result
  }
  
  override fun toString(): String {
    return "PackageConstant(type=$type, nameIndex=$nameIndex)"
  }
}