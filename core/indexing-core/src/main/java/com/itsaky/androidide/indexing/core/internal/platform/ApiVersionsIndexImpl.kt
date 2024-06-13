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

package com.itsaky.androidide.indexing.core.internal.platform

import com.itsaky.androidide.db.IRealmProvider
import com.itsaky.androidide.indexing.AbstractDBIndex
import com.itsaky.androidide.indexing.core.platform.ClassOrMemberInfo
import com.itsaky.androidide.indexing.core.platform.IApiVersionsIndex
import com.itsaky.androidide.indexing.core.platform.IPlatformIndex
import io.realm.Realm

/**
 * Index to store [ClassOrMemberInfo].
 *
 * @author Akash Yadav
 */
internal class ApiVersionsIndexImpl private constructor(params: IApiVersionsIndex.Params?) :
  AbstractDBIndex<ClassOrMemberInfo, IApiVersionsIndex.Params>(params),
  IApiVersionsIndex {

  companion object {
    private const val NAME = "member-info"
    private val PATH = IRealmProvider.createPath(IPlatformIndex.PLATFORM_INDEX_BASE_PATH, NAME)

    @JvmStatic
    fun newInstance(params: IApiVersionsIndex.Params?) = ApiVersionsIndexImpl(params)
  }

  override val name: String = NAME
  override val path: String = PATH

  override val realm: Realm by lazy {
    val provider = IRealmProvider.instance()
    provider.get(path)
  }
}