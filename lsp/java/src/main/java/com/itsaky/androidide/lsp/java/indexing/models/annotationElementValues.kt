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
import io.realm.RealmDictionary
import io.realm.RealmList
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required

sealed interface IAnnotationElementValue : ISharedJavaIndexable {
  var kind: Byte

  companion object {
    const val KIND_UNKNOWN: Byte = -1
    const val KIND_BOOLEAN: Byte = 0
    const val KIND_BYTE: Byte = 1
    const val KIND_CHAR: Byte = 2
    const val KIND_DOUBLE: Byte = 3
    const val KIND_FLOAT: Byte = 4
    const val KIND_INT: Byte = 5
    const val KIND_LONG: Byte = 6
    const val KIND_SHORT: Byte = 7
    const val KIND_STRING: Byte = 8
    const val KIND_CLASS: Byte = 9
    const val KIND_ENUM: Byte = 10
    const val KIND_ANNOTATION: Byte = 11
    const val KIND_ARRAY: Byte = 12
  }
}


@RealmClass
open class PrimitiveAnnotationElementValue : IAnnotationElementValue {

  @PrimaryKey
  @Required
  @RealmField("id")
  override var id: Int? = null

  @RealmField("kind")
  override var kind: Byte = 0

  @RealmField("value")
  var value: JavaConstant? = null

  override fun computeId() {
    this.id = Objects.hashCode(this.kind, this.value)
  }

  companion object {
    @JvmStatic
    fun newInstance(kind: Byte, value: JavaConstant): PrimitiveAnnotationElementValue {
      return PrimitiveAnnotationElementValue().apply {
        this.kind = kind
        this.value = value
        this.computeId()
      }
    }
  }
}


@RealmClass
open class EnumAnnotationElementValue : IAnnotationElementValue {
  @PrimaryKey
  @Required
  @RealmField("id")
  override var id: Int? = null

  @RealmField("kind")
  override var kind: Byte = IAnnotationElementValue.KIND_ENUM

  @RealmField("name")
  var name: String? = null

  @RealmField("type")
  var type: JavaType? = null

  override fun computeId() {
    this.id = Objects.hashCode(this.kind, this.name, this.type)
  }

  companion object {
    @JvmStatic
    fun newInstance(name: String, type: JavaType): EnumAnnotationElementValue {
      return EnumAnnotationElementValue().apply {
        this.name = name
        this.type = type
        this.computeId()
      }
    }
  }
}


@RealmClass
open class ArrayAnnotationElementValue : IAnnotationElementValue {

  @PrimaryKey
  @Required
  @RealmField("id")
  override var id: Int? = null

  @RealmField("kind")
  override var kind: Byte = IAnnotationElementValue.KIND_ARRAY

  @RealmField("numValues")
  var numValues: Int = 0

  /**
   * The elements of the array element value. Always of type [IAnnotationElementValue].
   */
  @RealmField("values")
  var values: RealmList<RealmAny>? = null

  override fun computeId() {
    this.id = Objects.hashCode(this.kind, this.numValues, this.values)
  }

  companion object {
    @JvmStatic
    fun newInstance(values: RealmList<RealmAny>): ArrayAnnotationElementValue {
      return ArrayAnnotationElementValue().apply {
        this.numValues = values.size
        this.values = values
        this.computeId()
      }
    }
  }
}


@RealmClass
open class ClassAnnotationElementValue : IAnnotationElementValue {

  @PrimaryKey
  @Required
  @RealmField("id")
  override var id: Int? = null

  @RealmField("kind")
  override var kind: Byte = IAnnotationElementValue.KIND_CLASS

  @RealmField("type")
  var type: JavaType? = null

  override fun computeId() {
    this.id = Objects.hashCode(this.kind, this.type)
  }

  companion object {
    @JvmStatic
    fun newInstance(type: JavaType): ClassAnnotationElementValue {
      return ClassAnnotationElementValue().apply {
        this.type = type
        this.computeId()
      }
    }
  }
}


@RealmClass
open class AnnotationAnnotationElementValue : IAnnotationElementValue {

  @PrimaryKey
  @Required
  @RealmField("id")
  override var id: Int? = null

  @RealmField("kind")
  override var kind: Byte = IAnnotationElementValue.KIND_ANNOTATION

  @RealmField("type")
  var type: JavaType? = null

  /**
   * The named elements of the annotation element value. Always of type [IAnnotationElementValue].
   */
  @RealmField("values")
  var values: RealmDictionary<RealmAny>? = null

  override fun computeId() {
    // RealmDictionary seems to not override hashCode()
    this.id = Objects.hashCode(this.kind, this.type, this.values?.toMap()?.hashCode())
  }

  companion object {
    @JvmStatic
    fun newInstance(
      type: JavaType,
      values: RealmDictionary<RealmAny>
    ): AnnotationAnnotationElementValue {
      return AnnotationAnnotationElementValue().apply {
        this.type = type
        this.values = values
        this.computeId()
      }
    }
  }
}
