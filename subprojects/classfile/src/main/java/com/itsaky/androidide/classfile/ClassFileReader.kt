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

package com.itsaky.androidide.classfile

import com.itsaky.androidide.classfile.constants.IConstant
import com.itsaky.androidide.classfile.constants.IConstant.Type.DOUBLE
import com.itsaky.androidide.classfile.constants.IConstant.Type.LONG
import com.itsaky.androidide.classfile.internal.ClassFile
import com.itsaky.androidide.classfile.internal.NullConstant
import com.itsaky.androidide.classfile.internal.NullField
import java.io.DataInputStream
import java.io.InputStream

/**
 * Reads class file from the given stream. This currently does not read the entire class file but
 * only the things we need (upto the `super_class` entry).
 *
 * @author Akash Yadav
 */
class ClassFileReader(private val stream: InputStream) {

  /**
   * Read the class file.
   *
   * @return The info about the class file.
   */
  fun read(): IClassFile {
    val input = DataInputStream(stream)
    val magic = input.readInt()
    if (magic != ClassFileConstants.MAGIC) {
      throw IllegalArgumentException(
        "Not a class file. Expecting magic value ${ClassFileConstants.MAGIC} but found $magic."
      )
    }

    val file = ClassFile()
    val minorVersion = input.readUnsignedShort()
    val majorVersion = input.readUnsignedShort()
    
    if (majorVersion >= 56 && minorVersion != 0 && minorVersion != 65535) {
      // When major version is >= 56, minor version must be 0 or 65535
      throw IllegalStateException("Invalid class file minor version: '${minorVersion}'")
    }
  
    file.version = ClassFileVersion.get(majorVersion, minorVersion)

    val poolCount = input.readUnsignedShort()
    file.constantPool = Array(poolCount) { NullConstant }

    var index = 1
    while (index < poolCount) {
      val constant = readPoolConstant(input)
      file.constantPool[index] = constant

      if (constant.type == LONG || constant.type == DOUBLE) {
        ++index
      }

      ++index
    }

    file.accessFlags = input.readUnsignedShort()
    file.thisClass = input.readUnsignedShort()
    file.superClass = input.readUnsignedShort()

    val interfaceCount = input.readUnsignedShort()
    file.interfaces = IntArray(interfaceCount)

    index = 0
    while (index < interfaceCount) {
      file.interfaces[index] = input.readUnsignedShort()
      ++index
    }

    val fieldCount = input.readUnsignedShort()
    file.fields = Array(fieldCount) { NullField }

    index = 0
    while (index < fieldCount) {
      file.fields[index] = IFieldInfo.read(input, file)
      ++index
    }

    return file
  }

  private fun readPoolConstant(input: DataInputStream): IConstant {
    val tag = input.readUnsignedByte()
    val type = IConstant.forTag(tag)
    val constant = IConstant.forType(type)
    constant.read(input)
    return constant
  }
}
