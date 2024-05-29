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

import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.tooling.api.models.result.SelectProjectResult
import org.eclipse.lsp4j.jsonrpc.services.JsonDelegate
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import java.util.concurrent.CompletableFuture

/**
 * Transmiting the whole project models over the communication streams results in a bad performance.
 * If the project is huge, the IDE may even run out of allocated memory.
 *
 * In the previous tooling API implementation, we used to fetch project models from Gradle, copy their
 * data to our own models, then send it to the client. This resulted in a bad performance and
 * increased memory usage. To overcome this, we keep a reference to the project models we get from
 * from the Android Gradle Plugin and compute only the requested information from those models.
 *
 * With LSP4J's JsonRpc implementation, it is not possible to create a delegate method in a
 * service which can accept parameters. What this means is that if a method is annotated with
 * [JsonDelegate][org.eclipse.lsp4j.jsonrpc.services.JsonDelegate], it cannot accept any parameters.
 *
 * As we cannot send the whole project model to the client for the reasons mentioned above, we use
 * another approach in this implementation. The client first selects the project it wants to fetch
 * information about. After this, when the delegate methods are called, it fetches the information
 * about the selected project.
 *
 * TODO(itsaky): Find a better approach to this issue.
 *
 * @author Akash Yadav
 */
interface IProjectQueries {

  /**
   * Select the project to work with. This overwrites the existing selection.
   *
   * @param param A [StringParameter] with the project's path as the value. If the value is an empty string, the root project will be selected.
   */
  @JsonRequest
  fun selectProject(param: StringParameter): CompletableFuture<SelectProjectResult>

  /**
   * Get the type of selected project.
   */
  @JsonRequest
  fun getType(): CompletableFuture<ProjectType>

  /**
   * Get the selected project as Gradle project.
   */
  @JsonDelegate
  fun asGradleProject(): IGradleProject

  /**
   * Get the selected project as Android project.
   */
  @JsonDelegate
  fun asAndroidProject(): IAndroidProject

  /**
   * Get the selected project as Java project.
   */
  @JsonDelegate
  fun asJavaProject(): IJavaProject
}