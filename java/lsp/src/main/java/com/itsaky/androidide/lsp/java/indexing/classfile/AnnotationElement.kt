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

package com.itsaky.androidide.lsp.java.indexing.classfile

import com.itsaky.androidide.lsp.java.indexing.IJavaSymbol
import io.realm.RealmAny
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * A method defined in an annotation type.
 *
 * @property defaultValue The default value of the annotation element.
 * @author Akash Yadav
 */
@RealmClass(embedded = true)
open class AnnotationElement : IJavaSymbol {

  @Index
  @Required
  @RealmField("name")
  var name: String? = null

  @RealmField("type")
  var type: JavaType? = null

  @RealmField("accessFlags")
  override var accessFlags: Int = 0

  @LinkingObjects("methods")
  val ofClass: RealmResults<JavaAnnotation>? = null

  /**
   * The default value of the annotation element. One of the following:
   * - [PrimitiveAnnotationElementValue]
   * - [EnumAnnotationElementValue]
   * - [ArrayAnnotationElementValue]
   * - [ClassAnnotationElementValue]
   * - [AnnotationAnnotationElementValue]
   */
  @RealmField("defaultValue")
  var defaultValue: RealmAny? = null

  /**
   * Returns the signature of the method.
   */
  fun signature(): String {
    val sb = StringBuilder()
    sb.append(name)
    sb.append("(")
    sb.append(")")
    sb.append(type?.internalForm())
    return sb.toString()
  }

  companion object {
    @JvmStatic
    fun newAnnotationElement(
      name: String,
      type: JavaType,
      accessFlags: Int,
      defaultValue: RealmAny? = null
    ): AnnotationElement {
      return AnnotationElement().apply {
        this.name = name
        this.type = type
        this.accessFlags = accessFlags
        this.defaultValue = defaultValue
      }
    }
  }
}