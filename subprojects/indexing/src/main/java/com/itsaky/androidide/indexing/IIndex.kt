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

import com.itsaky.androidide.indexing.internal.DefaultIndex

/**
 * An index of symbols of type [IndexableT].
 *
 * @author Akash Yadav
 */
interface IIndex<IndexableT : IIndexable> {

  companion object {
    private const val DEF_IS_ASYNC = true

    /**
     * Create an index path.
     */
    fun createIndexPath(parentPath: String, name: String): String =
      "${parentPath}${IRealmProvider.PATH_SEPARATOR}${name}"
  }

  /**
   * The name of the index.
   */
  val name: String

  /**
   * The path of the index. This does not represent the actual file path on the disk but rather used
   * to identify the index.
   */
  val path: String

  /**
   * Index the given symbol.
   *
   * @param symbol The symbol to index.
   */
  fun index(symbol: IndexableT, async: Boolean = DEF_IS_ASYNC)

  /**
   * Index the given symbols.
   *
   * @param symbols The symbols to index.
   */
  fun indexAll(symbols: Collection<IndexableT>, async: Boolean = DEF_IS_ASYNC) = indexBatched(
    symbols,
    symbols.size
  )

  /**
   * Index the given symbols in batches.
   *
   * @param symbols The symbols to index.
   * @param batchSize The number of symbols to index in each batch.
   */
  fun indexBatched(
    symbols: Collection<IndexableT>,
    batchSize: Int = DefaultIndex.DEFAULT_PUT_BATCH_SIZE,
    async: Boolean = DEF_IS_ASYNC,
  )

  /**
   * Create a sub-index in this index.
   *
   * @param name The name of the sub-index.
   * @return The created sub-index.
   */
  fun <I : IIndexable> createSubIndex(
    name: String,
    config: IndexConfig? = null
  ): IIndex<I>

  /**
   * Delete the index.
   */
  fun delete()
}