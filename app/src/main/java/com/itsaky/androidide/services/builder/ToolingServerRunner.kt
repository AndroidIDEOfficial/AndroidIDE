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

import com.itsaky.androidide.shell.CommonProcessExecutor
import com.itsaky.androidide.shell.ProcessStreamsHolder
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.util.ToolingApiLauncher
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.CancellationException

/**
 * Runner thread for the Tooling API.
 *
 * @author Akash Yadav
 */
internal class ToolingServerRunner(
  private var listener: OnServerStartListener?,
  private var observer: Observer?,
) : Thread(ToolingServerRunner::class.java.simpleName) {

  var isStarted = false
    private set

  private val log = ILogger.newInstance("ToolingServerRunner")

  fun setListener(listener: OnServerStartListener?) {
    this.listener = listener
  }

  override fun run() {
    try {
      log.info("Starting tooling API server...")
      val serverStreams = ProcessStreamsHolder()
      val executor = CommonProcessExecutor()
      executor.execAsync(serverStreams, { observer?.onServerExited(it) }, false,
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
        "-jar", Environment.TOOLING_API_JAR.absolutePath)

      val launcher = ToolingApiLauncher.newClientLauncher(observer!!.getClient(),
        serverStreams.`in`, serverStreams.out)

      val future = launcher.startListening()
      observer?.onListenerStarted(
        server = launcher.remoteProxy as IToolingApiServer,
        projectProxy = launcher.remoteProxy as IProject,
        streams = serverStreams)

      isStarted = true

      if (listener != null) {
        listener!!.onServerStarted()

        // we don't need the listener anymore
        // also, this might be a reference to the activity
        // release to prevent memory leak
        listener = null
      }

      // Wait(block) until the process terminates
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
    } catch (e: Throwable) {
      log.error("Unable to start tooling API server", e)
    }
  }

  override fun interrupt() {
    release()
    super.interrupt()
  }

  fun release() {
    this.listener = null
    this.observer = null
  }

  interface Observer {

    fun onListenerStarted(
      server: IToolingApiServer,
      projectProxy: IProject,
      streams: ProcessStreamsHolder,
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
