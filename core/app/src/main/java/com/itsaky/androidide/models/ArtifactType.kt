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

package com.itsaky.androidide.models

import com.google.gson.annotations.SerializedName

class ArtifactType {
  @SerializedName("type") var type: String? = null
  @SerializedName("kind") var kind: String? = null

  companion object {
    var TYPE_APK = "APK"
  }

  override fun toString(): String {
    return "ArtifactType(type=$type, kind=$kind)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ArtifactType) return false

    if (type != other.type) return false
    if (kind != other.kind) return false

    return true
  }

  override fun hashCode(): Int {
    var result = type?.hashCode() ?: 0
    result = 31 * result + (kind?.hashCode() ?: 0)
    return result
  }
}