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

import com.itsaky.androidide.lookup.Lookup

/**
 * API information about classes.
 *
 * @author Akash Yadav
 */
interface ApiVersions {

  companion object {
    @JvmStatic
    val COMPLETION_LOOKUP_KEY = Lookup.Key<ApiVersions>()
  }

  /**
   * Get the API version info about the class with the given name.
   *
   * @param name The fully qualified name of the class, in its internal form.
   */
  fun classInfo(name: String): ApiVersion?

  /**
   * Get the API version info about the member of the given class.
   *
   * @param className The fully qualified name of the class, in its internal form.
   * @param identifier The identifier of the member. This is the member name in case the member is a
   * field. For method, this is the signature of the method without the return type.
   */
  fun memberInfo(className: String, identifier: String): ApiVersion?
}
