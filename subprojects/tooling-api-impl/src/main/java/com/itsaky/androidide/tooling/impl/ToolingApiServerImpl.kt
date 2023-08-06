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

import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult
import com.itsaky.androidide.tooling.api.messages.result.BuildCancellationRequestResult.Reason.CANCELLATION_ERROR
import com.itsaky.androidide.tooling.api.messages.result.BuildInfo
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.BUILD_CANCELLED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.BUILD_FAILED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.CONNECTION_CLOSED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.CONNECTION_ERROR
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNKNOWN
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_BUILD_ARGUMENT
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_CONFIGURATION
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_GRADLE_VERSION
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.tooling.impl.internal.ProjectImpl
import com.itsaky.androidide.tooling.impl.sync.ModelBuilderException
import com.itsaky.androidide.tooling.impl.sync.RootModelBuilder
import com.itsaky.androidide.tooling.impl.util.StopWatch
import com.itsaky.androidide.utils.ILogger
import org.gradle.tooling.BuildCancelledException
import org.gradle.tooling.BuildException
import org.gradle.tooling.CancellationTokenSource
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.UnsupportedVersionException
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException
import org.gradle.tooling.exceptions.UnsupportedOperationConfigurationException
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException

/**
 * Implementation for the Gradle Tooling API server.
 *
 * @author Akash Yadav
 */
internal class ToolingApiServerImpl(private val project: ProjectImpl) :
  IToolingApiServer {

  private var initialized = false
  private var client: IToolingApiClient? = null
  private var connector: GradleConnector? = null
  private var buildCancellationToken: CancellationTokenSource? = null
  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun initialize(params: InitializeProjectMessage): CompletableFuture<InitializeResult> {
    return CompletableFuture.supplyAsync {
      try {
        if (initialized && connector != null) {
          connector?.disconnect()
          connector = null
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
          throw IllegalStateException(
            "Unable to create gradle connector for project directory: ${params.directory}")
        }

        notifyBeforeBuild(BuildInfo(emptyList()))

        val connection = this.connector!!.connect()
        stopWatch.lapFromLast("Project connection established")

        val project = try {
          val impl = RootModelBuilder.build(connection to params.androidVariant) as? ProjectImpl?
            ?: throw ModelBuilderException("Failed to build project model")
          impl
        } catch (err: Throwable) {
          throw err
        } finally {
          connection.close()
        }

        stopWatch.lapFromLast("Project read successful")
        stopWatch.log()

        this.project.rootProject = project.rootProject
        this.project.projects = project.projects
        initialized = true

        notifyBuildSuccess(emptyList())
        return@supplyAsync InitializeResult(true)
      } catch (err: Throwable) {
        log.error("Failed to initialize project", err)
        notifyBuildFailure(emptyList())
        return@supplyAsync InitializeResult(false)
      }
    }
  }

  override fun isServerInitialized(): CompletableFuture<Boolean> {
    return CompletableFuture.supplyAsync { initialized }
  }

  override fun getRootProject(): CompletableFuture<IProject> {
    return CompletableFuture.supplyAsync {
      assertProjectInitialized()
      return@supplyAsync this.project
    }
  }

  override fun executeTasks(message: TaskExecutionMessage): CompletableFuture<TaskExecutionResult> {
    return CompletableFuture.supplyAsync {
      assertProjectInitialized()

      log.debug("Received request to run tasks.", message)
      Main.checkGradleWrapper()

      var projectPath = message.projectPath
      if (projectPath == null) {
        projectPath = IProject.ROOT_PROJECT_PATH
      }

      val project = this.project.run {
        selectProject(StringParameter(projectPath)).get()
        asGradleProject()
      }

      this.connector!!.forProjectDirectory(project.getMetadata().get().projectDir)
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

      notifyBeforeBuild(BuildInfo(message.tasks))

      try {
        builder.run()
        this.buildCancellationToken = null
        notifyBuildSuccess(message.tasks)
        return@supplyAsync TaskExecutionResult(true, null)
      } catch (error: Throwable) {
        notifyBuildFailure(message.tasks)
        return@supplyAsync TaskExecutionResult(false, getTaskFailureType(error))
      }
    }
  }

  private fun setupConnectorForGradleInstallation(
    connector: GradleConnector,
    gradleDistribution: String?
  ) {
    if (gradleDistribution.isNullOrBlank()) {
      log.info("Using Gradle wrapper for build...")
      return
    }
    val file = File(gradleDistribution)
    if (!file.exists() || !file.isDirectory) {
      log.error("Specified Gradle installation does not exist:", gradleDistribution)
      return
    }

    log.info("Using Gradle installation:", file.canonicalPath)
    connector.useInstallation(file)
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

  private fun notifyBeforeBuild(buildInfo: BuildInfo) {
    if (client != null) {
      client!!.prepareBuild(buildInfo)
    }
  }

  override fun cancelCurrentBuild(): CompletableFuture<BuildCancellationRequestResult> {
    return CompletableFuture.supplyAsync {
      if (this.buildCancellationToken == null) {
        return@supplyAsync BuildCancellationRequestResult(
          false,
          BuildCancellationRequestResult.Reason.NO_RUNNING_BUILD
        )
      }

      try {
        this.buildCancellationToken!!.cancel()
      } catch (e: Exception) {
        val failureReason = CANCELLATION_ERROR
        failureReason.message = "${failureReason.message}: ${e.message}"
        return@supplyAsync BuildCancellationRequestResult(false, failureReason)
      }

      return@supplyAsync BuildCancellationRequestResult(true, null)
    }
  }

  override fun shutdown(): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
      connector?.disconnect()
      Main.future?.cancel(true)

      this.connector = null
      this.client = null
      this.buildCancellationToken = null
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
    if (!isServerInitialized().get()) {
      throw CompletionException(IllegalStateException("Project is not initialized!"))
    }
  }

  fun connect(client: IToolingApiClient) {
    this.client = client
  }
}
