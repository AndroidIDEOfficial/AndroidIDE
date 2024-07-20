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

/**
 * An index of symbols of type [IndexableT].
 *
 * @author Akash Yadav
 */
interface IIndex<IndexableT : IIndexable> {

  companion object {

    const val PATH_SEPARATOR = '/'

    /**
     * The default batch size for indexing.
     */
    const val DEFAULT_PUT_BATCH_SIZE = 100

    /**
     * Base path for indices.
     */
    val INDEX_BASE_PATH = createIndexPath("", "index")

    private const val DEF_IS_ASYNC = true

    /**
     * Create an index path.
     */
    fun createIndexPath(parentPath: String, name: String): String =
      "${parentPath}${PATH_SEPARATOR}${name}"
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
  fun index(symbol: IndexableT)

  /**
   * Index the given symbol asynchronously.
   *
   * @param symbol The symbol to index.
   */
  fun indexAsync(symbol: IndexableT)

  /**
   * Index the given symbols.
   *
   * @param symbols The symbols to index.
   */
  fun indexAll(symbols: Collection<IndexableT>)

  /**
   * Index the given symbols asynchronously.
   *
   * @param symbols The symbols to index.
   */
  fun indexAllAsync(symbols: Collection<IndexableT>)

  /**
   * Create a sub-index in this index. The default implementation will throw an [UnsupportedOperationException].
   *
   * @return The created sub-index.
   */
  fun <I : IIndexable> createSubIndex(
    params: IIndexParams? = null
  ): IIndex<I> {
    throw UnsupportedOperationException()
  }

  /**
   * Delete the index.
   */
  fun delete()
}