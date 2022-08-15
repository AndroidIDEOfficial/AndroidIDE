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

package com.itsaky.androidide.projects.builder

import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import java.util.concurrent.CompletableFuture

/**
 * A build service provides API to initialize project, execute builds, query a build, cancel running
 * builds, etc.
 *
 * @author Akash Yadav
 */
interface BuildService {

  /** Whether a build is in progress or not. */
  val isBuildInProgress: Boolean

  /**
   * Initialize the project.
   *
   * @param rootDir The root directory of the project to initialize.
   * @return A [CompletableFuture] which returns an [InitializeResult] when the project
   * initialization process finishes.
   */
  fun initializeProject(rootDir: String): CompletableFuture<InitializeResult>

  /**
   * Execute the given tasks.
   *
   * @param tasks The tasks to execute. If the fully qualified path of the task is not specified,
   * then it will be executed in the root project directory.
   * @return A [CompletableFuture] which returns a list of [TaskExecutionResult]. The result
   * contains a list of tasks that were executed and the result of the whole execution.
   * @see BuildService.executeProjectTasks
   */
  fun executeTasks(vararg tasks: String): CompletableFuture<TaskExecutionResult>

  /**
   * Execute the given tasks of the given project.
   *
   * @param projectPath The path of the project. All the tasks will be executed in this project.
   * @param tasks The tasks to execute. These may or may not be fully qualified names of the
   * project. If the task name is not fully qualified, then it will be executed in the given project
   * path. For example, if the project path is ':app' and the task is 'assembleDebug', then,
   * ':app:assembleDebug' task will be executed.
   * @return A [CompletableFuture] which returns a list of [TaskExecutionResult]. The result
   * contains a list of tasks that were executed and the result of the whole execution.
   */
  fun executeProjectTasks(
    projectPath: String,
    vararg tasks: String
  ): CompletableFuture<TaskExecutionResult>

  /** Cancel any running build. */
  fun cancelCurrentBuild(): CompletableFuture<BuildCancellationRequestResult>
}
