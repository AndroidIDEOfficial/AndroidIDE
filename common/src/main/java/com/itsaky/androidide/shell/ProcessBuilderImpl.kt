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

import com.itsaky.androidide.utils.Environment
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * Process builder for executing commands.
 *
 * @author Akash Yadav
 */
data class ProcessBuilderImpl(
  override var command: List<String> = emptyList(),
  override var environment: Map<String, String> = emptyMap(),
  override var workingDirectory: File? = null,
  override var redirectErrorStream: Boolean = false
) : IProcessBuilderInternal {

  override fun startAsync(): Process {
    require(command.isNotEmpty()) {
      "Cannot start process. No commands provided."
    }

    if (workingDirectory == null) {
      workingDirectory = Environment.HOME
    }

    require(workingDirectory?.isDirectory == true) {
      "Working directory does not exist."
    }

    return ProcessBuilder().run {
      command(command)
      directory(workingDirectory)
      redirectErrorStream(redirectErrorStream)
      environment().putAll(environment)
      start()
    }
  }

  override suspend fun start(context: CoroutineContext): Int {
    return withContext(context) {
      @Suppress("BlockingMethodInNonBlockingContext")
      startAsync().waitFor()
    }
  }
}