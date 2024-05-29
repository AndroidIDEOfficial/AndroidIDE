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

package com.itsaky.androidide.tooling.api

import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import com.itsaky.androidide.tooling.api.models.BasicProjectMetadata
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment
import java.util.concurrent.CompletableFuture

/**
 * A service providing access to the whole Gradle project and its structure.
 *
 * This class
 *
 * @author Akash Yadav
 */
@JsonSegment("root")
interface IProject : IProjectQueries {

  /**
   * Get all the projects included in this root project.
   */
  @JsonRequest
  fun getProjects(): CompletableFuture<List<BasicProjectMetadata>>

  /**
   * Get the project sync issues.
   */
  @JsonRequest
  fun getProjectSyncIssues(): CompletableFuture<DefaultProjectSyncIssues>

  companion object {

    /**
     * Name that can be used for project whose [BasicProjectMetadata.name] property is null.
     */
    const val PROJECT_UNKNOWN = "Unknown"
  }
}