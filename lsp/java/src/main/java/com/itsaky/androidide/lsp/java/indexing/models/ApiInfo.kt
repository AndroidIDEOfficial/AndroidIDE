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
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * @property since The API in which the symbol was added.
 * @property deprecatedIn The API in which the symbol was deprecated.
 * @property removedIn The API in which the symbol was removed.
 * @author Akash Yadav
 */

@RealmClass
open class ApiInfo : ISharedJavaIndexable {

  @Required
  @PrimaryKey
  @RealmField("id")
  override var id: Int? = null

  @RealmField("since")
  var since: Int = 1

  @RealmField("deprecatedIn")
  var deprecatedIn: Int = 0

  @RealmField("removedIn")
  var removedIn: Int = 0

  override fun computeId() {
    this.id = Objects.hashCode(this.since, this.removedIn, this.deprecatedIn)
  }

  companion object {
    @JvmStatic
    fun newInstance(since: Int, deprecatedIn: Int, removedIn: Int): ApiInfo {
      return ApiInfo().apply {
        this.since = since
        this.deprecatedIn = deprecatedIn
        this.removedIn = removedIn
        this.computeId()
      }
    }
  }
}
