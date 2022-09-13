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

import com.itsaky.androidide.classfile.attributes.ElementValue.Type.ANNOTATION_INTERFACE
import com.itsaky.androidide.classfile.attributes.ElementValue.Type.ARRAY_TYPE
import com.itsaky.androidide.classfile.attributes.ElementValue.Type.CLASS
import com.itsaky.androidide.classfile.attributes.ElementValue.Type.ENUM_CLASS
import com.itsaky.androidide.classfile.attributes.ElementValue.Type.UNKNOWN
import java.io.DataInputStream

/**
 * The element value in an annotation.
 *
 * @author Akash Yadav
 */
class ElementValue {

  /** The element tag. */
  var type: Type = UNKNOWN
    internal set

  /**
   * The constant value if the [type] is one of the following :
   *
   * - [Type.BYTE]
   * - [Type.CHAR]
   * - [Type.DOUBLE]
   * - [Type.FLOAT]
   * - [Type.INT]
   * - [Type.LONG]
   * - [Type.SHORT]
   * - [Type.BOOLEAN]
   * - [Type.STRING]
   */
  var constValIndex: Int = -1
    internal set

  /** The element constant value if the [type] is [Type.ENUM_CLASS]. */
  var enumValue: EnumConstValue? = null
    internal set

  /** The class name index if the [type] is [Type.CLASS]. */
  var classNameIndex: Int = -1
    internal set

  /** The annotation constant value if the [type] is [Type.ANNOTATION_INTERFACE]. */
  var annotationValue: Annotation? = null
    internal set

  /** The array constant value if the [type] is [Type.ARRAY_TYPE]. */
  var arrayValue: Array<ElementValue>? = null
    internal set

  companion object {
    internal fun read(input: DataInputStream): ElementValue {
      val value = ElementValue()
      value.type = Type.forTag(input.readUnsignedByte())
      when (value.type) {
        ENUM_CLASS -> value.enumValue = EnumConstValue.read(input)
        CLASS -> value.classNameIndex = input.readUnsignedShort()
        ANNOTATION_INTERFACE -> value.annotationValue = Annotation.read(input)
        ARRAY_TYPE -> {
          val count = input.readUnsignedShort()
          val nullElem = ElementValue()
          value.arrayValue = Array(count) { nullElem }

          var index = 0
          while (index < count) {
            value.arrayValue!![index] = read(input)
            ++index
          }
        }
        else -> value.constValIndex = input.readUnsignedShort()
      }

      return value
    }
  }

  /**
   * The enum constant value.
   *
   * @property typeNameIndex The index of the entry in the constant pool which corresponds to the
   * name of the type of this constant.
   * @property constNameIndex The index of the entry in the constant pool which corresponds to the
   * name of this constant.
   */
  data class EnumConstValue(val typeNameIndex: Int, val constNameIndex: Int) {
    companion object {
      internal fun read(input: DataInputStream): EnumConstValue {
        return EnumConstValue(input.readUnsignedShort(), input.readUnsignedShort())
      }
    }
  }

  /** The type of [ElementValue]. */
  enum class Type(val tag: Int) {
    BYTE('B'.code),
    CHAR('C'.code),
    DOUBLE('D'.code),
    FLOAT('F'.code),
    INT('I'.code),
    LONG('J'.code),
    SHORT('S'.code),
    BOOLEAN('Z'.code),
    STRING('s'.code),
    ENUM_CLASS('e'.code),
    CLASS('c'.code),
    ANNOTATION_INTERFACE('@'.code),
    ARRAY_TYPE('['.code),

    /** **FOR INTERNAL USE ONLY** */
    UNKNOWN('-'.code);

    companion object {

      /** Get the element type for the given tag value. */
      internal fun forTag(tag: Int): Type {
        return values().firstOrNull { it.tag == tag }
          ?: throw IllegalArgumentException("Invalid tag for element value type tag '$tag'")
      }
    }
  }
}
