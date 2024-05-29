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

package com.itsaky.androidide.indexing

import com.itsaky.androidide.db.IRealmProvider
import com.itsaky.androidide.db.IRealmProvider.Companion.PATH_SEPARATOR
import com.itsaky.androidide.indexing.internal.DefaultIndex
import io.realm.RealmConfiguration

/**
 * The [IndexProvider] provides APIs to index symbols in a database.
 *
 * @author itsaky
 */
object IndexProvider {

  const val INDEX_BASE_PATH = "${PATH_SEPARATOR}indexes"

  /**
   * Get the index for the given indexable type in the root path.
   *
   * @param T The indexable type.
   * @param config The index configuration.
   * @param name The internal path of the index.
   */
  fun <T : IIndexable> getRootIndex(
    name: String,
    config: IndexConfig? = null
  ): IIndex<T> {
    require(name.indexOf(PATH_SEPARATOR) == -1)
    return getIndexForPath("${PATH_SEPARATOR}${name}", config)
  }

  /**
   * Get the index for the given indexable type.
   *
   * @param path The internal path of the index.
   * @param config The index configuration.
   */
  fun <T : IIndexable> getIndexForPath(
    path: String,
    config: IndexConfig? = null
  ): IIndex<T> {
    val sepIdx = path.indexOfLast { it == PATH_SEPARATOR }
    val name = if (sepIdx > 0) path.substring(sepIdx + 1) else path
    val provider = IRealmProvider.instance()
    val realm = provider.get("${INDEX_BASE_PATH}${path}", config)
    val index = DefaultIndex.create<T>(name, path, realm)
    return index
  }
}

/**
 * The [IndexConfig] is used to configure the index.
 */
typealias IndexConfig = RealmConfiguration.Builder.() -> Unit
