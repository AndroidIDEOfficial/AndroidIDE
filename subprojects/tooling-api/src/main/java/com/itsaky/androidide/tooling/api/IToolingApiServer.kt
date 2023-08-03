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

import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment
import java.util.concurrent.*

/**
 * A tooling api server provides services related to the Gradle Tooling API.
 *
 * @author Akash Yadav
 */
@JsonSegment("server")
interface IToolingApiServer {

  /** Initialize the server with the project directory. */
  @JsonRequest fun initialize(params: InitializeProjectMessage): CompletableFuture<InitializeResult>

  /** Is the server initialized? */
  @JsonRequest fun isServerInitialized(): CompletableFuture<Boolean>

  /** Get the root project. */
  @JsonRequest fun getRootProject(): CompletableFuture<IProject>

  /** Execute the tasks specified in the message. */
  @JsonRequest
  fun executeTasks(message: TaskExecutionMessage): CompletableFuture<TaskExecutionResult>

  /**
   * Cancel the current build.
   *
   * @return A [CompletableFuture] which completes when the current build cancellation process
   * finishes (either successfully or with an error).
   */
  @JsonRequest fun cancelCurrentBuild(): CompletableFuture<BuildCancellationRequestResult>

  /**
   * Shutdown the tooling API server. This will disconnect all the project connection instances.]
   *
   * @return A [CompletableFuture] which completes when the shutdown process is finished.
   */
  @JsonNotification fun shutdown(): CompletableFuture<Void>
}
