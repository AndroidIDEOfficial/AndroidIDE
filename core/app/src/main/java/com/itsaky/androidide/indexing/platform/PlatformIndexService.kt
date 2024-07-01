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

package com.itsaky.androidide.indexing.platform

import com.google.auto.service.AutoService
import com.itsaky.androidide.indexing.IIndexService
import com.itsaky.androidide.projects.IWorkspace
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author Akash Yadav
 */
@AutoService(IIndexService::class)
internal class PlatformIndexService : IIndexService {

  companion object {
    private val log = LoggerFactory.getLogger(PlatformIndexService::class.java)
  }

  override val displayName: String
    get() = "Android Platform Indexing Service"

  override fun scanFiles(workspace: IWorkspace): Collection<File> {
    return mutableListOf<File>().apply {
      workspace.androidProjects().forEach { androidModule ->
        add(androidModule.getPlatformDir()?.also {
          log.debug("Adding {} to the list of indexable paths", it)
        } ?: return@forEach)
      }
    }
  }

  override suspend fun indexFiles(
    workspace: IWorkspace,
    files: Collection<File>
  ) {
  }
}