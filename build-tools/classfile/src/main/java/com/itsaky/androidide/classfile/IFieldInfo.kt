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

import com.itsaky.androidide.classfile.attributes.IAttribute
import com.itsaky.androidide.classfile.internal.ClassFile
import com.itsaky.androidide.classfile.internal.FieldInfo
import com.itsaky.androidide.classfile.internal.NullAttribute
import java.io.DataInputStream

/**
 * Information about a field.
 *
 * @author Akash Yadav
 */
interface IFieldInfo {

  /** Access flags for this field. */
  val accessFlags: Int

  /** The index in the constant pool which corresponds to the name of this field. */
  val nameIndex: Int

  /**
   * The index in the constant pool which corresponds to the field descriptor string for this field.
   */
  val descriptorIndex: Int

  /** The attributes of this field. */
  val attributes: Array<IAttribute>

  companion object {

    internal fun read(input: DataInputStream, file: ClassFile): IFieldInfo {
      val field = FieldInfo()
      field.accessFlags = input.readUnsignedShort()
      field.nameIndex = input.readUnsignedShort()
      field.descriptorIndex = input.readUnsignedShort()

      val attrCount = input.readUnsignedShort()
      field.attributes = Array(attrCount) { NullAttribute }

      var index = 0
      while (index < attrCount) {
        field.attributes[index] = IAttribute.read(input, file)
        ++index
      }
      return field
    }
  }
}
