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

import com.google.auto.service.AutoService
import com.itsaky.androidide.indexing.IIndex
import com.itsaky.androidide.indexing.IIndexFactory
import com.itsaky.androidide.indexing.core.platform.AbstractIndexFactory
import com.itsaky.androidide.indexing.core.platform.ClassOrMemberInfo
import com.itsaky.androidide.indexing.core.platform.IApiVersionsIndex.Params

/**
 * @author Akash Yadav
 */
@AutoService(IIndexFactory::class)
internal class ApiVersionsIndexFactory : AbstractIndexFactory<ClassOrMemberInfo, Params>() {

  override fun indexableType(): Class<ClassOrMemberInfo> {
    return ClassOrMemberInfo::class.java
  }

  override fun paramType(): Class<out Params> {
    return Params::class.java
  }

  override fun create(): IIndex<ClassOrMemberInfo, Params> {
    return ApiVersionsIndexImpl.newInstance(params)
  }
}