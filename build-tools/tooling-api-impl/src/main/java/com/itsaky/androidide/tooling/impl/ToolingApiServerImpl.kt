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

package com.itsaky.androidide.tooling.impl

import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult.Reason.CANCELLATION_ERROR
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.BUILD_CANCELLED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.BUILD_FAILED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.CONNECTION_CLOSED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.CONNECTION_ERROR
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_NOT_FOUND
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNKNOWN
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_BUILD_ARGUMENT
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_CONFIGURATION
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_GRADLE_VERSION
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.impl.model.InternalForwardingProject
import com.itsaky.androidide.tooling.impl.util.ProjectReader
import com.itsaky.androidide.tooling.impl.util.StopWatch
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.util.concurrent.*
import org.eclipse.lsp4j.jsonrpc.CompletableFutures
import org.gradle.tooling.BuildCancelledException
import org.gradle.tooling.BuildException
import org.gradle.tooling.CancellationTokenSource
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.UnsupportedVersionException
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException
import org.gradle.tooling.exceptions.UnsupportedOperationConfigurationException

/**
 * Implementation for the Gradle Tooling API server.
 *
 * @author Akash Yadav
 */
internal class ToolingApiServerImpl(private val forwardingProject: InternalForwardingProject) :
  IToolingApiServer {

  private var initialized = false
  private var client: IToolingApiClient? = null
  private var connector: GradleConnector? = null
  private var project: IdeGradleProject? = null
  private var buildCancellationToken: CancellationTokenSource? = null
  private val log = ILogger.newInstance(javaClass.simpleName)

  @Suppress("UnstableApiUsage")
  override fun initialize(params: InitializeProjectMessage): CompletableFuture<InitializeResult> {
    forwardingProject.projectPath = params.directory
    return CompletableFutures.computeAsync {
      try {
        if (initialized && connector != null) {
          connector?.disconnect()
          connector = null
          project = null
        }

        Main.checkGradleWrapper()

        if (buildCancellationToken != null) {
          cancelCurrentBuild().get()
        }

        log.debug("Got initialize request", params)
        val stopWatch = StopWatch("Connection to project")
        this.connector = GradleConnector.newConnector().forProjectDirectory(File(params.directory))
        setupConnectorForGradleInstallation(this.connector!!, params.gradleInstallation)
        stopWatch.lap("Connector created")

        if (this.connector == null) {
          throw CompletionException(
            RuntimeException(
              "Unable to create gradle connector for project directory: ${params.directory}"
            )
          )
        }

        notifyBeforeBuild()

        val connection = this.connector!!.connect()
        stopWatch.lapFromLast("Project connection established")

        val issues: MutableMap<String, DefaultProjectSyncIssues> = mutableMapOf()
        this.project = ProjectReader.read(connection, issues)
        stopWatch.lapFromLast("Project read ${if(this.project == null) "failed" else "successful"}")

        connection.close()
        stopWatch.log()

        this.forwardingProject.project = this.project

        initialized = true

        notifyBuildSuccess(emptyList())
        return@computeAsync InitializeResult(issues)
      } catch (err: Throwable) {
        log.error(err)
        notifyBuildFailure(emptyList())
      }

      return@computeAsync InitializeResult(emptyMap())
    }
  }

  override fun isServerInitialized(): CompletableFuture<Boolean> {
    return CompletableFuture.supplyAsync { initialized }
  }

  override fun getRootProject(): CompletableFuture<IdeGradleProject> {
    return CompletableFutures.computeAsync {
      assertProjectInitialized()
      return@computeAsync this.project
    }
  }

  override fun executeTasks(message: TaskExecutionMessage): CompletableFuture<TaskExecutionResult> {
    return CompletableFutures.computeAsync {
      assertProjectInitialized()

      log.debug("Received request to run tasks.", message)
      Main.checkGradleWrapper()

      var projectPath = message.projectPath
      if (projectPath == null) {
        projectPath = ":"
      }

      val project =
        this.project!!.findByPath(projectPath).get()
          ?: return@computeAsync TaskExecutionResult(false, PROJECT_NOT_FOUND)

      this.connector!!.forProjectDirectory(project.projectDir)
      setupConnectorForGradleInstallation(this.connector!!, message.gradleInstallation)

      val connection = this.connector!!.connect()
      val builder = connection.newBuild()

      // System.in and System.out are used for communication between this server and the
      // client.
      val out = LoggingOutputStream()
      builder.setStandardInput("NoOp".byteInputStream())
      builder.setStandardError(out)
      builder.setStandardOutput(out)
      builder.forTasks(*message.tasks.filter { it.isNotBlank() }.toTypedArray())
      Main.finalizeLauncher(builder)

      this.buildCancellationToken = GradleConnector.newCancellationTokenSource()
      builder.withCancellationToken(this.buildCancellationToken!!.token())

      notifyBeforeBuild()

      try {
        builder.run()
        this.buildCancellationToken = null
        notifyBuildSuccess(message.tasks)
        return@computeAsync TaskExecutionResult(true, null)
      } catch (error: Throwable) {
        notifyBuildFailure(message.tasks)
        return@computeAsync TaskExecutionResult(false, getTaskFailureType(error))
      }
    }
  }

  private fun setupConnectorForGradleInstallation(
    connector: GradleConnector,
    gradleDistribution: String?
  ) {
    if (gradleDistribution != null && gradleDistribution.isNotBlank()) {
      val file = File(gradleDistribution)
      if (file.exists() && file.isDirectory) {
        log.info("Using Gradle installation:", file.canonicalPath)
        connector.useInstallation(file)
      } else {
        log.error("Specified Gradle installation does not exist:", gradleDistribution)
      }
    } else {
      log.info("Using Gradle wrapper for build...")
    }
  }

  private fun notifyBuildFailure(tasks: List<String>) {
    if (client != null) {
      client!!.onBuildFailed(BuildResult((tasks)))
    }
  }

  private fun notifyBuildSuccess(tasks: List<String>) {
    if (client != null) {
      client!!.onBuildSuccessful(BuildResult(tasks))
    }
  }

  private fun notifyBeforeBuild() {
    if (client != null) {
      client!!.prepareBuild()
    }
  }

  override fun cancelCurrentBuild(): CompletableFuture<BuildCancellationRequestResult> {
    return CompletableFutures.computeAsync {
      if (this.buildCancellationToken == null) {
        return@computeAsync BuildCancellationRequestResult(
          false,
          BuildCancellationRequestResult.Reason.NO_RUNNING_BUILD
        )
      }

      try {
        this.buildCancellationToken!!.cancel()
      } catch (e: Exception) {
        val failureReason = CANCELLATION_ERROR
        failureReason.message = "${failureReason.message}: ${e.message}"
        return@computeAsync BuildCancellationRequestResult(false, failureReason)
      }

      return@computeAsync BuildCancellationRequestResult(true, null)
    }
  }

  @Suppress("UnstableApiUsage")
  override fun shutdown(): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
      connector?.disconnect()
      Main.future?.cancel(true)

      this.connector = null
      this.client = null
      this.project = null
      this.buildCancellationToken = null
      this.forwardingProject.project = null
      Main.future = null
      Main.client = null

      this.initialized = false
    }
  }

  private fun getTaskFailureType(error: Throwable): Failure =
    when (error) {
      is BuildException -> BUILD_FAILED
      is BuildCancelledException -> BUILD_CANCELLED
      is UnsupportedOperationConfigurationException -> UNSUPPORTED_CONFIGURATION
      is UnsupportedVersionException -> UNSUPPORTED_GRADLE_VERSION
      is UnsupportedBuildArgumentException -> UNSUPPORTED_BUILD_ARGUMENT
      is GradleConnectionException -> CONNECTION_ERROR
      is java.lang.IllegalStateException -> CONNECTION_CLOSED
      else -> UNKNOWN
    }

  private fun assertProjectInitialized() {
    if (!isServerInitialized().get() || project == null) {
      throw CompletionException(IllegalStateException("Project is not initialized!"))
    }
  }

  fun connect(client: IToolingApiClient) {
    this.client = client
  }
}
