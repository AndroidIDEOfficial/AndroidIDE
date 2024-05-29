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

package com.itsaky.androidide.tooling.api.messages.result

/**
 * Result for a task execution.
 *
 * @param isSuccessful The result of the task execution.
 * @param failure The type of failure. Non-null only if [isSuccessful] is `false`.
 *
 * @author Akash Yadav
 */
data class TaskExecutionResult(val isSuccessful: Boolean, val failure: Failure?) {

  companion object {

    /**
     * Result for a successful build.
     */
    @JvmStatic
    val SUCCESS = TaskExecutionResult(true, null)
  }

  enum class Failure {
    PROJECT_NOT_FOUND,
    PROJECT_NOT_INITIALIZED,
    PROJECT_NOT_DIRECTORY,
    PROJECT_DIRECTORY_INACCESSIBLE,
    UNKNOWN,
    UNSUPPORTED_GRADLE_VERSION,
    UNSUPPORTED_CONFIGURATION,
    UNSUPPORTED_BUILD_ARGUMENT,
    BUILD_FAILED,
    BUILD_CANCELLED,
    CONNECTION_ERROR,
    CONNECTION_CLOSED
  }
}
