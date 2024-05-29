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

package com.itsaky.androidide.db.internal

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * An entity to store information about all registered box stores.
 *
 * @author Akash Yadav
 */
open class DatabaseEntity(
  @Required
  @PrimaryKey
  @RealmField("path")
  var path: String = "",

  @Required
  @RealmField("name")
  var name: String = "",

  @Required
  @RealmField("directory")
  var directory: String = "",
) : RealmObject() {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DatabaseEntity) return false

    if (path != other.path) return false
    if (name != other.name) return false
    if (directory != other.directory) return false

    return true
  }

  override fun hashCode(): Int {
    var result = path.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + directory.hashCode()
    return result
  }

  override fun toString(): String {
    return "DatabaseEntity(path='$path', name='$name', directory='$directory')"
  }
}