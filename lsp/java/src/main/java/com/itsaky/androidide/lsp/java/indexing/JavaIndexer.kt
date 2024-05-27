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

package com.itsaky.androidide.lsp.java.indexing

import com.itsaky.androidide.indexing.IIndex
import com.itsaky.androidide.indexing.IIndexer
import com.itsaky.androidide.indexing.IIndexer.Params
import com.itsaky.androidide.tasks.cancelIfActive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.File

/**
 * @author Akash Yadav
 */
class JavaIndexer(
  private val jarPaths: List<File>
) : IIndexer<IJavaIndexable, Unit> {

  companion object {
    const val INDEX_PATH = "/java"
  }

  private val coroutineScope = CoroutineScope(Dispatchers.IO)
  override val indexPath: String = INDEX_PATH

  override fun index(index: IIndex<IJavaIndexable>, params: Params): Collection<Deferred<Unit>> {
    return jarPaths.map { jar ->
      coroutineScope.async {
        val worker = JavaJarModelBuilder(jar)
//        worker.index(index)
      }
    }
  }

  override fun close() {
    coroutineScope.cancelIfActive("Cancellation requested")
  }
}