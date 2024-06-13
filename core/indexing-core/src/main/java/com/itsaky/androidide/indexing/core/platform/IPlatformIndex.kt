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

package com.itsaky.androidide.indexing.core.platform

import com.itsaky.androidide.db.IRealmProvider
import com.itsaky.androidide.indexing.IIndex
import com.itsaky.androidide.indexing.IIndexParams

/**
 * Index for data available in the Android Platform SDK.
 *
 * @author Akash Yadav
 */
interface IPlatformIndex<T : IPlatformIndexable, C : IIndexParams> : IIndex<T, C> {

  companion object {

    /**
     * Base path for the platform index.
     */
    val PLATFORM_INDEX_BASE_PATH = IRealmProvider.createPath(IIndex.INDEX_BASE_PATH, "platform")
  }
}