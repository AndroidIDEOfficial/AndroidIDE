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

package com.itsaky.androidide.shell

import kotlinx.coroutines.Dispatchers
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * Process builder for executing commands.
 *
 * @author Akash Yadav
 */
interface IProcessBuilder {

  /**
   * The command to execute.
   */
  var command: List<String>

  /**
   * The environment variables.
   */
  var environment: Map<String, String>

  /**
   * The working directory for the command. If set to `null`, the
   * [Environment.HOME][com.itsaky.androidide.utils.Environment.HOME] directory will be used.
   *
   * Default value is `null`.
   */
  var workingDirectory: File?

  /**
   * Whether the error stream should be redirected to the output stream.
   */
  var redirectErrorStream: Boolean
}

internal interface IProcessBuilderInternal : IProcessBuilder {

  /**
   * Start the process asynchronously.
   *
   * @return The [Process] instance.
   */
  fun startAsync() : Process

  /**
   * Start the process and suspend until the process finishes.
   *
   * @return The exit code of the process.
   */
  suspend fun start(context: CoroutineContext = Dispatchers.IO): Int
}