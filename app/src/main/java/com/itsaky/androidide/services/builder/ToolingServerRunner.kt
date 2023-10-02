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

package com.itsaky.androidide.services.builder

import com.itsaky.androidide.shell.executeProcessAsync
import com.itsaky.androidide.tasks.cancelIfActive
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.concurrent.CancellationException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Runner thread for the Tooling API.
 *
 * @author Akash Yadav
 */
internal class ToolingServerRunner(
  private var listener: OnServerStartListener?,
  private var observer: Observer?,
) {

  private var _isStarted = AtomicBoolean(false)

  var isStarted: Boolean
    get() = _isStarted.get()
    private set(value) {
      _isStarted.set(value)
    }

  private val runnerScope = CoroutineScope(Dispatchers.IO + CoroutineName("ToolingServerRunner"))

  companion object {

    private val log = ILogger.newInstance("ToolingServerRunner")
  }

  fun setListener(listener: OnServerStartListener?) {
    this.listener = listener
  }

  fun startAsync() = runnerScope.launch {
    try {
      log.info("Starting tooling API server...")
      val command = listOf(
        Environment.JAVA.absolutePath, // The 'java' binary executable
        // Allow reflective access to private members of classes in the following
        // packages:
        // - java.lang
        // - java.io
        // - java.util
        //
        // If any of the model classes in 'tooling-api-model' module send/receive
        // objects from the JDK, their package name must be declared here with
        // '--add-opens' to prevent InaccessibleObjectException.
        // For example, some of the model classes has members of type java.io.File.
        // When sending/receiving these type of objects using LSP4J, members of
        // these objects are reflectively accessed by Gson. If we do no specify
        // '--add-opens' for 'java.io' (for java.io.File) package, JVM will throw an
        // InaccessibleObjectException.
        "--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens",
        "java.base/java.util=ALL-UNNAMED", "--add-opens",
        "java.base/java.io=ALL-UNNAMED", // The JAR file to run
        "-jar", Environment.TOOLING_API_JAR.absolutePath
      )

      val process = executeProcessAsync {
        this.command = command

        // input and output is used for communication to the tooling server
        // error stream is used to read the server logs
        this.redirectErrorStream = false
        this.workingDirectory = null // HOME
        this.environment = Environment.getEnvironment()
      }

      val inputStream = process.inputStream
      val outputStream = process.outputStream
      val errorStream = process.errorStream

      val processJob = launch(Dispatchers.IO) {
        process.waitFor()
      }

      val launcher = ToolingApiLauncher.newClientLauncher(
        observer!!.getClient(),
        inputStream,
        outputStream
      )

      val future = launcher.startListening()
      observer?.onListenerStarted(
        server = launcher.remoteProxy as IToolingApiServer,
        projectProxy = launcher.remoteProxy as IProject,
        errorStream = errorStream
      )

      isStarted = true

      listener?.onServerStarted()

      // we don't need the listener anymore
      // also, this might be a reference to the activity
      // release to prevent memory leak
      listener = null

      // Wait(block) until the process terminates
      val serverJob = launch(Dispatchers.IO) {
        try {
          future.get()
        } catch (err: Throwable) {
          when (err) {
            is CancellationException,
            is InterruptedException,
            -> log.info("ToolingServerThread has been cancelled or interrupted.")

            else -> throw err
          }
        }
      }

      val waitJob = launch(Dispatchers.IO) {
        // wait for both, process and server job to complete
        serverJob.join()
        processJob.join()
      }

      waitJob.join()
    } catch (e: Throwable) {
      if (e !is CancellationException) {
        log.error("Unable to start tooling API server", e)
      }
    }
  }

  fun release() {
    this.listener = null
    this.observer = null
    this.runnerScope.cancelIfActive("Cancellation was requested")
  }

  interface Observer {

    fun onListenerStarted(
      server: IToolingApiServer,
      projectProxy: IProject,
      errorStream: InputStream,
    )

    fun onServerExited(exitCode: Int)

    fun getClient(): IToolingApiClient
  }

  /** Callback to listen for Tooling API server start event.  */
  fun interface OnServerStartListener {

    /** Called when the tooling API server has been successfully started.  */
    fun onServerStarted()
  }
}
