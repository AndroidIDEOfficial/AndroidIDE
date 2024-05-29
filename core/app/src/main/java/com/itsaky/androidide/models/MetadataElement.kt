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

class MetadataElement {
  @SerializedName("type") var type: String? = null
  @SerializedName("filters") var filters: List<Any>? = null
  @SerializedName("attributes") var attributes: List<Any>? = null
  @SerializedName("versionCode") var versionCode = 0
  @SerializedName("versionName") var versionName: String? = null
  @SerializedName("outputFile") var outputFile: String? = null
  
  override fun toString(): String {
    return "Element(type=$type, filters=$filters, attributes=$attributes, versionCode=$versionCode, versionName=$versionName, outputFile=$outputFile)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is MetadataElement) return false

    if (type != other.type) return false
    if (filters != other.filters) return false
    if (attributes != other.attributes) return false
    if (versionCode != other.versionCode) return false
    if (versionName != other.versionName) return false
    if (outputFile != other.outputFile) return false

    return true
  }

  override fun hashCode(): Int {
    var result = type?.hashCode() ?: 0
    result = 31 * result + (filters?.hashCode() ?: 0)
    result = 31 * result + (attributes?.hashCode() ?: 0)
    result = 31 * result + versionCode
    result = 31 * result + (versionName?.hashCode() ?: 0)
    result = 31 * result + (outputFile?.hashCode() ?: 0)
    return result
  }
}