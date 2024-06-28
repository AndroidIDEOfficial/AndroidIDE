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

package com.itsaky.androidide.xml.internal.versions

import com.itsaky.androidide.xml.versions.ApiVersion
import com.itsaky.androidide.xml.versions.ApiVersions

/**
 * This implementation is not thread safe. Do not modify concurrently.
 *
 * @author Akash Yadav
 */
internal class DefaultApiVersions : ApiVersions {

  val classes = HashMap<String, Pair<ApiVersion?, HashMap<String, ApiVersion>>>()

  private fun String.flatten() = replace('.', '/')

  override fun classInfo(name: String): ApiVersion? {
    return classes[name.flatten()]?.first
  }

  override fun memberInfo(className: String, identifier: String): ApiVersion? {
    return classes[className.flatten()]?.second?.get(identifier)
  }

  fun containsClass(name: String): Boolean {
    return classes.containsKey(name.flatten())
  }

  fun containsClassMember(name: String, member: String): Boolean {
    return classes[name.flatten()]?.second?.containsKey(member) ?: false
  }

  fun putClass(name: String, version: ApiVersion) {
    computeClass(name, version)
  }

  fun putMember(className: String, identifier: String, version: ApiVersion) {
    computeClass(className).second[identifier] = version
  }

  private fun computeClass(
    name: String,
    version: ApiVersion? = null
  ): Pair<ApiVersion?, HashMap<String, ApiVersion>> {
    return classes.computeIfAbsent(name.flatten()) {
      version to hashMapOf()
    }
  }
}
