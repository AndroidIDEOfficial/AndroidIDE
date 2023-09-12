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

package com.itsaky.androidide.xml.versions

import com.itsaky.androidide.utils.ServiceLoader
import com.itsaky.androidide.xml.registry.XmlRegistry

/**
 * Registry that reads information about the API versions of classes, their fields and methods.
 *
 * The following information is stored :
 * - Since
 * - Removed
 * - Deprecated
 * @author Akash Yadav
 */
interface ApiVersionsRegistry : XmlRegistry<ApiVersions> {

  companion object {

    private var sInstance: ApiVersionsRegistry? = null

    /** Get the default instance of [ApiVersionsRegistry]. */
    @JvmStatic
    fun getInstance(): ApiVersionsRegistry {
      val klass = ApiVersionsRegistry::class.java
      return sInstance ?: ServiceLoader.load(klass, klass.classLoader).findFirstOrThrow()
        .also { sInstance = it }
    }
  }
}
