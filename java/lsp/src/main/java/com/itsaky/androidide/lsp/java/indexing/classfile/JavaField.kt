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
import com.itsaky.androidide.lsp.java.indexing.apiinfo.ApiInfo
import io.realm.RealmResults
import io.realm.annotations.Index
import io.realm.annotations.LinkingObjects
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField

/**
 * @author Akash Yadav
 */
@RealmClass(embedded = true)
open class JavaField : IJavaSymbol {

  @Index
  @RealmField("name")
  var name: String? = null

  @RealmField("type")
  var type: JavaType? = null

  @RealmField("accessFlags")
  override var accessFlags: Int = 0

  @RealmField("apiInfo")
  override var apiInfo: ApiInfo? = null

  @RealmField("constantValue")
  var constantValue: JavaConstant? = null

  @LinkingObjects("fields")
  val ofClass: RealmResults<JavaClass>? = null

  companion object {
    @JvmStatic
    fun newField(
      name: String,
      type: JavaType,
      accessFlags: Int,
      apiInfo: ApiInfo? = null,
      constantValue: JavaConstant? = null
    ): JavaField {
      return JavaField().apply {
        this.name = name
        this.type = type
        this.accessFlags = accessFlags
        this.apiInfo = apiInfo
        this.constantValue = constantValue
      }
    }
  }
}