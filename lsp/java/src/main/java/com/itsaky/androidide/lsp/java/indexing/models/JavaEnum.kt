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

import io.realm.RealmList
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * A Java enum type.
 *
 * @author Akash Yadav
 */
@RealmClass
open class JavaEnum : IJavaType<JavaField, JavaMethod> {

  @Index
  @PrimaryKey
  @Required
  @RealmField("fqn")
  override var fqn: String? = null

  @Required
  @RealmField("name")
  override var name: String? = null

  @Required
  @RealmField("pck")
  override var packageName: String? = null

  @RealmField("isInner")
  override var isInner: Boolean = false

  @RealmField("superClassFqn")
  override var superClassFqn: String? = null

  @RealmField("superInterfacesFqn")
  override var superInterfacesFqn: RealmList<String>? = null

  @RealmField("accessFlags")
  override var accessFlags: Int = 0

  @RealmField("apiInfo")
  override var apiInfo: ApiInfo? = null

  @RealmField("fields")
  override var fields: RealmList<JavaField>? = null

  @RealmField("methods")
  override var methods: RealmList<JavaMethod>? = null

  override val isEnum: Boolean
    get() = true

  companion object {
    @JvmStatic
    fun newInstance(
      fqn: String,
      name: String,
      packageName: String,
      accessFlags: Int,
      isInner: Boolean = false,
      superClassFqn: String? = null,
      superInterfacesFqn: RealmList<String>? = null,
      apiInfo: ApiInfo? = null,
      fields: RealmList<JavaField>? = null,
      methods: RealmList<JavaMethod>? = null
    ): JavaEnum {
      return JavaEnum().apply {
        this.fqn = fqn
        this.name = name
        this.packageName = packageName
        this.isInner = isInner
        this.superClassFqn = superClassFqn
        this.superInterfacesFqn = superInterfacesFqn
        this.accessFlags = accessFlags
        this.apiInfo = apiInfo
        this.fields = fields
        this.methods = methods
      }
    }
  }
}