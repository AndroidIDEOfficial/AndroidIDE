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

import com.itsaky.androidide.tooling.api.models.GradleTask
import com.itsaky.androidide.tooling.api.models.ProjectMetadata
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment
import java.util.concurrent.CompletableFuture

/**
 * A model for representing a project which is not an Android or Java project.
 *
 * @author Akash Yadav
 */
@JsonSegment("gradle")
interface IGradleProject {

  /**
   * Get the metadata about the project. This includes basic information about the project.
   *
   * @see [ProjectMetadata].
   */
  @JsonRequest
  fun getMetadata(): CompletableFuture<ProjectMetadata>

  /**
   * Get this project's tasks.
   */
  @JsonRequest
  fun getTasks(): CompletableFuture<List<GradleTask>>
}