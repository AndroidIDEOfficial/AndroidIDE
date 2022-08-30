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
import com.itsaky.androidide.classfile.IClassFile
import com.itsaky.androidide.classfile.constants.ClassConstant
import com.itsaky.androidide.classfile.constants.Utf8Constant

/**
 * Model class for holding information about class files.
 *
 * @author Akash Yadav
 */
internal class ClassFile : IClassFile {
  
  /** Major version of the class file. */
  override var majorVersion: Int = 0

  /** Minor version of the class file. */
  override var minorVersion: Int = 0

  /** The constant pool. */
  override var constantPool: Array<IConstant> = emptyArray()

  /** Access flags for the class. */
  override var accessFlags: Int = 0

  /**
   * The index in the constant pool which corresponds to the internal representation of the name of
   * this class.
   */
  override var thisClass: Int = -1

  /**
   * The index in the constant pool which corresponds to the internal representation of the name of
   * the super class of this class.
   */
  override var superClass: Int = -1
  
  override var interfaces: IntArray = IntArray(0)
  
  override fun getName(): String {
    val entry = constantPool[thisClass]
    if (entry !is ClassConstant) {
      throw IllegalStateException("Invalid constant at index: '$thisClass'")
    }
    
    val nameEntry = constantPool[entry.nameIndex]
    if (nameEntry !is Utf8Constant) {
      throw IllegalStateException("Invalid constant at index: '$thisClass'")
    }
    
    return nameEntry.bytes.decodeToString()
  }
  
  override fun getSuperClassName(): String {
    val entry = constantPool[superClass]
    if (entry !is ClassConstant) {
      throw IllegalStateException("Invalid constant at index: '$superClass'")
    }
  
    val nameEntry = constantPool[entry.nameIndex]
    if (nameEntry !is Utf8Constant) {
      throw IllegalStateException("Invalid constant at index: '$superClass'")
    }
  
    return nameEntry.bytes.decodeToString()
  }
}
