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

package com.itsaky.androidide.indexing.core.platform

import com.google.common.base.Objects
import com.itsaky.androidide.indexing.IIndexable
import com.itsaky.androidide.models.ICloneable
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
open class ApiVersion : IIndexable, ICloneable {

  @Required
  @PrimaryKey
  @RealmField("id")
  var id: Int? = null

  @RealmField("since")
  var since: Int = 1
    private set

  @RealmField("deprecatedIn")
  var deprecatedIn: Int = 0
    private set

  @RealmField("removedIn")
  var removedIn: Int = 0
    private set

  private fun computeId() {
    this.id = Objects.hashCode(this.since, this.removedIn, this.deprecatedIn)
  }

  /**
   * Update the API information from the given [ApiVersion].
   */
  fun update(apiVersion: ApiVersion) = apply {
    this.since = apiVersion.since
    this.deprecatedIn = apiVersion.deprecatedIn
    this.removedIn = apiVersion.removedIn
    this.computeId()
  }

  /**
   * Update the API information with the given parameters.
   */
  fun update(
    since: Int = this.since,
    deprecatedIn: Int = this.deprecatedIn,
    removedIn: Int = this.removedIn
  ) = apply {
    this.since = since
    this.deprecatedIn = deprecatedIn
    this.removedIn = removedIn
    this.computeId()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ApiVersion) return false

    if (id != other.id) return false
    if (since != other.since) return false
    if (deprecatedIn != other.deprecatedIn) return false
    if (removedIn != other.removedIn) return false

    return true
  }

  override fun hashCode(): Int {
    var result = id ?: 0
    result = 31 * result + since
    result = 31 * result + deprecatedIn
    result = 31 * result + removedIn
    return result
  }

  override fun toString(): String {
    return "ApiVersion(id=$id, since=$since, deprecatedIn=$deprecatedIn, removedIn=$removedIn)"
  }

  override fun clone(): ApiVersion {
    return newInstance(since, deprecatedIn, removedIn)
  }

  companion object {
    @JvmStatic
    fun newInstance(since: Int, deprecatedIn: Int, removedIn: Int): ApiVersion {
      return ApiVersion().apply {
        this.since = since
        this.deprecatedIn = deprecatedIn
        this.removedIn = removedIn
        this.computeId()
      }
    }
  }
}
