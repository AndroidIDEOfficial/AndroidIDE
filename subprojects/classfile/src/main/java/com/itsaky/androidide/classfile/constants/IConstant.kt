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

import com.itsaky.androidide.classfile.constants.IConstant.Type.CLASS
import com.itsaky.androidide.classfile.constants.IConstant.Type.DOUBLE
import com.itsaky.androidide.classfile.constants.IConstant.Type.DYNAMIC
import com.itsaky.androidide.classfile.constants.IConstant.Type.FIELD_REF
import com.itsaky.androidide.classfile.constants.IConstant.Type.FLOAT
import com.itsaky.androidide.classfile.constants.IConstant.Type.INTEGER
import com.itsaky.androidide.classfile.constants.IConstant.Type.INTERFACE_METHOD_REF
import com.itsaky.androidide.classfile.constants.IConstant.Type.INVOKE_DYNAMIC
import com.itsaky.androidide.classfile.constants.IConstant.Type.LONG
import com.itsaky.androidide.classfile.constants.IConstant.Type.METHOD_HANDLE
import com.itsaky.androidide.classfile.constants.IConstant.Type.METHOD_REF
import com.itsaky.androidide.classfile.constants.IConstant.Type.METHOD_TYPE
import com.itsaky.androidide.classfile.constants.IConstant.Type.MODULE
import com.itsaky.androidide.classfile.constants.IConstant.Type.NAME_AND_TYPE
import com.itsaky.androidide.classfile.constants.IConstant.Type.PACKAGE
import com.itsaky.androidide.classfile.constants.IConstant.Type.STRING
import com.itsaky.androidide.classfile.constants.IConstant.Type.UNKNOWN
import com.itsaky.androidide.classfile.constants.IConstant.Type.UTF8
import java.io.DataInputStream

/**
 * A constant in a constant pool from a class file.
 *
 * @property type The type of the constant.
 * @author Akash Yadav
 */
interface IConstant {

  val type: Type

  /** Reads the data from the given [DataInputStream]. */
  fun read(input: DataInputStream)

  /**
   * The type of the [IConstant].
   *
   * @author Akash Yadav
   */
  enum class Type(val tag: Int) {
    UTF8(1),
    INTEGER(3),
    FLOAT(4),
    LONG(5),
    DOUBLE(6),
    CLASS(7),
    STRING(8),
    FIELD_REF(9),
    METHOD_REF(10),
    INTERFACE_METHOD_REF(11),
    NAME_AND_TYPE(12),
    METHOD_HANDLE(15),
    METHOD_TYPE(16),
    DYNAMIC(17),
    INVOKE_DYNAMIC(18),
    MODULE(19),
    PACKAGE(20),

    /** FOR INTERNAL USE ONLY! */
    UNKNOWN(-1)
  }

  companion object {
    @JvmStatic
    fun forTag(tag: Int): Type {
      for (value in Type.values()) {
        if (value != UNKNOWN && value.tag == tag) {
          return value
        }
      }
      throw IllegalArgumentException("Unsupported or unknown constant pool tag: '$tag'")
    }

    @JvmStatic
    fun forType(type: Type): IConstant {
      return when (type) {
        UTF8 -> Utf8Constant()
        INTEGER -> IntegerConstant()
        FLOAT -> FloatConstant()
        LONG -> LongConstant()
        DOUBLE -> DoubleConstant()
        CLASS -> ClassConstant()
        STRING -> StringConstant()
        FIELD_REF,
        METHOD_REF,
        INTERFACE_METHOD_REF -> ReferenceConstant(type)
        NAME_AND_TYPE -> NameAndTypeConstant()
        METHOD_HANDLE -> MethodHandleConstant()
        METHOD_TYPE -> MethodTypeConstant()
        DYNAMIC,
        INVOKE_DYNAMIC -> DynamicConstant(type)
        MODULE -> ModuleConstant()
        PACKAGE -> PackageConstant()
        UNKNOWN -> throw IllegalArgumentException("$type is not allowed or not supported.")
      }
    }
  }
}
