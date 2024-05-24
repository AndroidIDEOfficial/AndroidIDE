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
import io.realm.RealmAny
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField

/**
 * A constant value in Java.
 *
 * A unique ID is used as the primary key in order to ensure that a constant is not stored multiple
 * times in the database. Also, a string constant always has type [JavaType.KIND_REF]. A constant
 * is never of type [JavaType.KIND_VOID].
 *
 * @property id A unique ID which is generated for the constant.
 * @property kind The kind of contant. See [JavaType] for a list of kinds.
 * @property value The value of the constant. Cast this value based on the value of [kind].
 * @author Akash Yadav
 */
@RealmClass
open class JavaConstant : ISharedJavaIndexable {

  @PrimaryKey
  @RealmField("id")
  override var id: Int? = null

  @RealmField("kind")
  var kind: Short = JavaType.KIND_UNKNOWN

  @RealmField("value")
  var value: RealmAny? = null

  companion object {
    fun newInstance(kind: Short, value: RealmAny): JavaConstant {
      return JavaConstant().apply {
        this.kind = kind
        this.value = value
        this.computeId()
      }
    }
  }

  override fun computeId() {
    this.id = Objects.hashCode(this.kind, this.kind, this.value)
  }

  // boolean, byte, short, int are stored as int in the bytecode

  fun asBoolean(): Boolean? {
    return safeCast(JavaType.KIND_BOOLEAN) { asInteger() == 1 }
  }

  fun asByte(): Byte? {
    return safeCast(JavaType.KIND_BYTE) { asInteger()?.toByte() }
  }

  fun asShort(): Short? {
    return safeCast(JavaType.KIND_SHORT) { asInteger()?.toShort() }
  }

  fun asInt(): Int? {
    return safeCast(JavaType.KIND_INT) { asInteger() }
  }

  fun asLong(): Long? {
    return safeCast(JavaType.KIND_LONG) { asLong() }
  }

  fun asFloat(): Float? {
    return safeCast(JavaType.KIND_FLOAT) { asFloat() }
  }

  fun asDouble(): Double? {
    return safeCast(JavaType.KIND_DOUBLE) { asDouble() }
  }

  fun asString(): String? {
    return safeCast(JavaType.KIND_REF) { asString() }
  }

  fun asChar(): Char? {
    return safeCast(JavaType.KIND_CHAR) { asInteger()?.toChar() }
  }

  private inline fun <R> safeCast(kind: Short, action: RealmAny.() -> R): R? {
    return value?.let {
      if (this.kind == kind) {
        try {
          it.action()
        } catch (ex: ClassCastException) {
          throw IllegalStateException("Kind == $kind but safeCast failed", ex)
        }
      } else null
    }
  }
}