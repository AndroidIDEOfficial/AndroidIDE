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
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * @author Akash Yadav
 */

@RealmClass(embedded = true)
open class JavaMethod : IJavaSymbol {
  @Index
  @Required
  @RealmField("name")
  var name: String? = null

  @RealmField("paramTypes")
  var paramsTypes: RealmList<JavaType>? = null

  @RealmField("returnType")
  var returnType: JavaType? = null

  @RealmField("accessFlags")
  override var accessFlags: Int = 0

  @LinkingObjects("methods")
  val ofClass: RealmResults<JavaClass>? = null

  /**
   * Returns the signature of the method.
   */
  fun signature(): String {
    val sb = StringBuilder()
    sb.append(name)
    sb.append("(")
    paramsTypes?.forEach {
      sb.append(it.internalForm())
    }
    sb.append(")")
    sb.append(returnType?.internalForm())
    return sb.toString()
  }

  companion object {
    @JvmStatic
    fun newInstance(
      name: String,
      paramsTypes: RealmList<JavaType>,
      returnType: JavaType,
      accessFlags: Int,
    ): JavaMethod {
      return JavaMethod().apply {
        this.name = name
        this.paramsTypes = paramsTypes
        this.returnType = returnType
        this.accessFlags = accessFlags
      }
    }
  }
}