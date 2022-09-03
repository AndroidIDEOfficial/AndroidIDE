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

import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_CONSTANT_VALUE
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_DEPRECATED
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_RUNTIME_INVISIBLE_ANNOTATIONS
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_RUNTIME_VISIBLE_ANNOTATIONS
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_SIGNATURE
import com.itsaky.androidide.classfile.ClassFileConstants.ATTR_SYNTHETIC
import com.itsaky.androidide.classfile.ClassFileVersion
import com.itsaky.androidide.classfile.IClassFile
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.CONSTANT_VALUE
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.DEPRECATED
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.RUNTIME_INVISIBLE_ANNOTATIONS
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.RUNTIME_VISIBLE_ANNOTATIONS
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.RUNTIME_VISIBLE_TYPE_ANNOTATIONS
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.SIGNATURE
import com.itsaky.androidide.classfile.attributes.IAttribute.Type.SYNTHETIC
import com.itsaky.androidide.classfile.constants.Utf8Constant
import com.itsaky.androidide.classfile.internal.ClassFile
import java.io.DataInputStream

/**
 * Base class for all attributes in a class, field or method.
 *
 * @author Akash Yadav
 */
interface IAttribute {

  /** The version of the class file in which this attribute appears. */
  val version: ClassFileVersion

  /** The index of the entry in the constant pool which represents the name of this attrbute. */
  val nameIndex: Int

  /** The type of attribute. */
  val type: Type

  /**
   * **FOR INTERNAL USE ONLY**
   *
   * Read further information from the given input.
   */
  fun read(attrLength: Int, file: IClassFile, input: DataInputStream)

  enum class Type(val attrName: String) {
    CONSTANT_VALUE(ATTR_CONSTANT_VALUE),
    SYNTHETIC(ATTR_SYNTHETIC),
    SIGNATURE(ATTR_SIGNATURE),
    DEPRECATED(ATTR_DEPRECATED),
    RUNTIME_VISIBLE_ANNOTATIONS(ATTR_RUNTIME_VISIBLE_ANNOTATIONS),
    RUNTIME_INVISIBLE_ANNOTATIONS(ATTR_RUNTIME_INVISIBLE_ANNOTATIONS),
    RUNTIME_VISIBLE_TYPE_ANNOTATIONS(ATTR_RUNTIME_VISIBLE_TYPE_ANNOTATIONS),
    RUNTIME_INVISIBLE_TYPE_ANNOTATIONS(ATTR_RUNTIME_INVISIBLE_TYPE_ANNOTATIONS);

    companion object {
      internal fun forName(name: String): Type {
        return values().firstOrNull { it.attrName == name }
          ?: throw IllegalArgumentException("Unknown attribute '$name'")
      }
    }
  }

  companion object {
    internal fun read(input: DataInputStream, file: ClassFile): IAttribute {
      val nameIndex = input.readUnsignedShort()
      val attrLength = input.readInt()
      val name = (file.constantPool[nameIndex] as Utf8Constant).content()
      val type = Type.forName(name)
      return createForType(type, nameIndex).also { it.read(attrLength, file, input) }
    }

    private fun createForType(type: Type, nameIndex: Int): IAttribute {
      return when (type) {
        CONSTANT_VALUE -> ConstantValueAttr(nameIndex)
        SYNTHETIC -> SyntheticAttr(nameIndex)
        SIGNATURE -> SignatureAttr(nameIndex)
        DEPRECATED -> DeprecatedAttr(nameIndex)
        RUNTIME_VISIBLE_ANNOTATIONS -> RuntimeVisibleAnnotationsAttr(nameIndex)
        RUNTIME_INVISIBLE_ANNOTATIONS -> RuntimeVisibleAnnotationsAttr(nameIndex)
        RUNTIME_VISIBLE_TYPE_ANNOTATIONS -> RuntimeVisibleTypeAnnotations(nameIndex)
        RUNTIME_INVISIBLE_TYPE_ANNOTATIONS -> RuntimeInvisibleTypeAnnotations(nameIndex)
        else -> throw IllegalArgumentException("Unknown attribute type $type")
      }
    }
  }
}
