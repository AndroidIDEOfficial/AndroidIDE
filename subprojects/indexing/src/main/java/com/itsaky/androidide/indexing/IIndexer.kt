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

import kotlinx.coroutines.Deferred

/**
 * An indexer is responsible for indexing symbols.
 *
 * @author Akash Yadav
 */
interface IIndexer<IndexableT : IIndexable, ReturnT> : AutoCloseable {

  /**
   * The path of the index. This does not represent the actual path in the file system, but rather the path
   * that will be used to uniquely identify the index.
   */
  val indexPath: String

  /**
   * Perform the indexing in the given index.
   *
   * @param index The index to perform the indexing in.
   * @param params The parameters for the indexing.
   * @return The collection of deferred results.
   */
  fun index(index: IIndex<IndexableT>, params: Params): Collection<Deferred<ReturnT>>

  /**
   * The parameters for the indexer.
   */
  interface Params
}