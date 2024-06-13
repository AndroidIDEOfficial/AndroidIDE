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

package com.itsaky.androidide.db

import com.itsaky.androidide.utils.ServiceLoader
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * An [IRealmProvider] provides the instance of [Realm].
 *
 * @author Akash Yadav
 */
interface IRealmProvider {

  /**
   * Get the [Realm] instance.
   *
   * @param path The path of the database.
   */
  fun get(path: String): Realm {
    return get(path, null)
  }

  /**
   * Get the [Realm] instance.
   *
   * @param path The path of the database.
   * @param config An optional function to configure the [RealmConfiguration] builder.
   */
  fun get(path: String, config: (RealmConfiguration.Builder.() -> Unit)?): Realm

  companion object {

    private lateinit var sInstance: IRealmProvider

    /**
     * The separator used to separate segments in an index path.
     */
    const val PATH_SEPARATOR = '/'

    /**
     * Create the path for the given segments.
     *
     * @param first The first (root) path segment.
     * @param rest The rest of the path segments.
     * @return The created path string.
     */
    @JvmStatic
    fun createPath(first: String, vararg rest: String): String {

      val sb = StringBuilder()
      sb.append(PATH_SEPARATOR)
      sb.append(first)

      for (segment in rest) {
        sb.append(PATH_SEPARATOR)
        sb.append(segment)
      }

      return sb.toString()
    }

    /**
     * Get the [IRealmProvider] instance, finding the implementation if necessary.
     */
    @JvmStatic
    fun instance(): IRealmProvider {
      if (!Companion::sInstance.isInitialized) {
        val clazz = IRealmProvider::class.java
        sInstance = ServiceLoader.load(clazz, clazz.classLoader).findFirstOrThrow()
      }
      return sInstance
    }
  }
}