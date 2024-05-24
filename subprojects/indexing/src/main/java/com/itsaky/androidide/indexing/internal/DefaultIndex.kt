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

package com.itsaky.androidide.indexing.internal

import com.itsaky.androidide.indexing.IIndex
import com.itsaky.androidide.indexing.IIndexable
import com.itsaky.androidide.indexing.IndexConfig
import com.itsaky.androidide.indexing.IndexProvider
import io.realm.Realm
import org.slf4j.LoggerFactory

/**
 * Default implementation of [IIndex].
 *
 * @author Akash Yadav
 */
class DefaultIndex<IndexableT : IIndexable> private constructor(
  override val name: String,
  override val path: String,
  private val realm: Realm,
) : IIndex<IndexableT> {

  companion object {
    private val log = LoggerFactory.getLogger(DefaultIndex::class.java)
    const val DEFAULT_PUT_BATCH_SIZE = 100

    /**
     * Creates a new [IIndex] instance.
     */
    fun <IndexableT : IIndexable> create(
      name: String,
      path: String,
      realm: Realm,
    ): IIndex<IndexableT> {
      return DefaultIndex(name, path, realm)
    }
  }

//  override fun query(condition: QueryCondition<IndexableT>): Query<IndexableT> {
//    return box.query(condition).build()
//  }

  override fun index(symbol: IndexableT, async: Boolean) {
    TODO("Not yet implemented")
  }

  override fun indexBatched(symbols: Collection<IndexableT>, batchSize: Int, async: Boolean) {
    TODO("Not yet implemented")
  }

  override fun <I : IIndexable> createSubIndex(
    name: String,
    config: IndexConfig?
  ): IIndex<I> {
    return IndexProvider.getIndexForPath(IIndex.createIndexPath(this.path, name), config)
  }

  override fun delete() {
    TODO("Not yet implemented")
  }
}