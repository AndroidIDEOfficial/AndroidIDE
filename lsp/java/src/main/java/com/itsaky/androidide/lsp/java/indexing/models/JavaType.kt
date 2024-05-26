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

package com.itsaky.androidide.lsp.java.indexing.models

import com.google.common.base.Objects
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * @author Akash Yadav
 */

@RealmClass
open class JavaType : ISharedJavaIndexable, ICloneable {

  @Required
  @PrimaryKey
  @RealmField("id")
  override var id: Int? = null

  @Index
  @Required
  @RealmField("name")
  var name: String? = null

  @RealmField("kind")
  var kind: Short = KIND_UNKNOWN

  @RealmField("arrayDims")
  var arrayDims: Int = 0

  /**
   * Returns the internal representation of the type.
   */
  fun internalForm(): String {
    val sb = StringBuilder()
    if (arrayDims > 0) {
      sb.append("[".repeat(arrayDims))
    }

    if (kind == KIND_REF) {
      sb.append("L")
    }

    sb.append(name)

    if (kind == KIND_REF) {
      sb.append(";")
    }

    return sb.toString()
  }

  /**
   * Returns `true` if the type represents a primitive type.
   */
  fun isPrimitive(): Boolean {
    return when (kind) {
      KIND_BOOLEAN,
      KIND_BYTE,
      KIND_CHAR,
      KIND_DOUBLE,
      KIND_FLOAT,
      KIND_INT,
      KIND_LONG,
      KIND_SHORT -> true

      else -> false
    }
  }

  /**
   * Returns `true` if the type represents a void type.
   */
  fun isVoid(): Boolean {
    return kind == KIND_VOID
  }

  /**
   * Returns `true` if the type represents the `java.lang.String` type.
   */
  fun isStringType(): Boolean {
    return name == STRING.name
  }

  /**
   * Returns `true` if the type represents the `java.lang.Object` type.
   */
  fun isObjectType(): Boolean {
    return name == OBJECT.name
  }

  /**
   * Returns `true` if the type represents the `java.lang.Class` type.
   */
  fun isClassType(): Boolean {
    return name == CLASS.name
  }

  override fun clone(): JavaType {
    return newInstance(name = name, kind = kind, arrayDims = arrayDims)
  }

  override fun computeId() {
    this.id = Objects.hashCode(this.kind, this.name, this.arrayDims)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is JavaType) return false

    if (name != other.name) return false
    if (kind != other.kind) return false
    if (arrayDims != other.arrayDims) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name?.hashCode() ?: 0
    result = 31 * result + kind
    result = 31 * result + arrayDims
    return result
  }

  override fun toString(): String {
    return "JavaType(id=$id, name=$name, kind=$kind, arrayDims=$arrayDims)"
  }
  
  companion object {
    const val KIND_UNKNOWN: Short = -1
    const val KIND_BOOLEAN: Short = 0
    const val KIND_BYTE: Short = 1
    const val KIND_CHAR: Short = 2
    const val KIND_DOUBLE: Short = 3
    const val KIND_FLOAT: Short = 4
    const val KIND_INT: Short = 5
    const val KIND_LONG: Short = 6
    const val KIND_SHORT: Short = 7
    const val KIND_VOID: Short = 8
    const val KIND_REF: Short = 9

    const val TYPE_BOOLEAN = 'Z'
    const val TYPE_BYTE = 'B'
    const val TYPE_CHAR = 'C'
    const val TYPE_DOUBLE = 'D'
    const val TYPE_FLOAT = 'F'
    const val TYPE_INT = 'I'
    const val TYPE_LONG = 'J'
    const val TYPE_SHORT = 'S'
    const val TYPE_VOID = 'V'

    @JvmStatic
    val BOOLEAN = newInstance(name = TYPE_BOOLEAN, kind = KIND_INT)

    @JvmStatic
    val BYTE = newInstance(name = TYPE_BYTE, kind = KIND_BYTE)

    @JvmStatic
    val CHAR = newInstance(name = TYPE_CHAR, kind = KIND_CHAR)

    @JvmStatic
    val DOUBLE = newInstance(name = TYPE_DOUBLE, kind = KIND_DOUBLE)

    @JvmStatic
    val FLOAT = newInstance(name = TYPE_FLOAT, kind = KIND_FLOAT)

    @JvmStatic
    val INT = newInstance(name = TYPE_INT, kind = KIND_INT)

    @JvmStatic
    val LONG = newInstance(name = TYPE_LONG, kind = KIND_LONG)

    @JvmStatic
    val SHORT = newInstance(name = TYPE_SHORT, kind = KIND_SHORT)

    @JvmStatic
    val VOID = newInstance(name = TYPE_VOID, kind = KIND_VOID)

    @JvmStatic
    val STRING = newInstance(name = "java/lang/String", kind = KIND_REF)

    @JvmStatic
    val OBJECT = newInstance(name = "java/lang/Object", kind = KIND_REF)

    @JvmStatic
    val CLASS = newInstance(name = "java/lang/Class", kind = KIND_REF)

    @JvmStatic
    fun kindForType(type: Char) = when (type) {
      TYPE_BOOLEAN -> KIND_BOOLEAN
      TYPE_BYTE -> KIND_BYTE
      TYPE_CHAR -> KIND_CHAR
      TYPE_DOUBLE -> KIND_DOUBLE
      TYPE_FLOAT -> KIND_FLOAT
      TYPE_INT -> KIND_INT
      TYPE_LONG -> KIND_LONG
      TYPE_SHORT -> KIND_SHORT
      TYPE_VOID -> KIND_VOID
      else -> KIND_UNKNOWN
    }

    @JvmStatic
    fun typeForKind(kind: Short) = when (kind) {
      KIND_BOOLEAN -> TYPE_BOOLEAN
      KIND_BYTE -> TYPE_BYTE
      KIND_CHAR -> TYPE_CHAR
      KIND_DOUBLE -> TYPE_DOUBLE
      KIND_FLOAT -> TYPE_FLOAT
      KIND_INT -> TYPE_INT
      KIND_LONG -> TYPE_LONG
      KIND_SHORT -> TYPE_SHORT
      KIND_VOID -> TYPE_VOID
      else -> TYPE_VOID
    }

    @JvmStatic
    fun primitiveFor(char: Char): JavaType? {
      return when (char) {
        TYPE_BOOLEAN -> BOOLEAN
        TYPE_BYTE -> BYTE
        TYPE_CHAR -> CHAR
        TYPE_DOUBLE -> DOUBLE
        TYPE_FLOAT -> FLOAT
        TYPE_INT -> INT
        TYPE_LONG -> LONG
        TYPE_SHORT -> SHORT
        TYPE_VOID -> VOID
        else -> null
      }
    }

    @JvmStatic
    fun newInstance(name: String?, kind: Short, arrayDims: Int = 0): JavaType {
      return JavaType().apply {
        this.name = name
        this.kind = kind
        this.arrayDims = arrayDims
        this.computeId()
      }
    }

    @JvmStatic
    fun newInstance(name: Char, kind: Short, arrayDims: Int = 0): JavaType {
      return newInstance(name.toString(), kind, arrayDims)
    }
  }
}