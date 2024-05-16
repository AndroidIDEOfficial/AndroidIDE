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
import com.itsaky.androidide.tooling.api.messages.GradleDistributionParams
import com.itsaky.androidide.tooling.api.messages.GradleDistributionType
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
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
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_DIRECTORY_INACCESSIBLE
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_NOT_DIRECTORY
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_NOT_FOUND
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_NOT_INITIALIZED
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNKNOWN
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_BUILD_ARGUMENT
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_CONFIGURATION
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNSUPPORTED_GRADLE_VERSION
import com.itsaky.androidide.tooling.api.models.ToolingServerMetadata
import com.itsaky.androidide.tooling.impl.internal.ProjectImpl
import com.itsaky.androidide.tooling.impl.sync.ModelBuilderException
import com.itsaky.androidide.tooling.impl.sync.RootModelBuilder
import com.itsaky.androidide.tooling.impl.sync.RootProjectModelBuilderParams
import com.itsaky.androidide.utils.StopWatch
import org.gradle.tooling.BuildCancelledException
import org.gradle.tooling.BuildException
import org.gradle.tooling.CancellationTokenSource
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.UnsupportedVersionException
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException
import org.gradle.tooling.exceptions.UnsupportedOperationConfigurationException
import org.gradle.tooling.internal.consumer.DefaultGradleConnector
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionException
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Implementation for the Gradle Tooling API server.
 *
 * @author Akash Yadav
 */
internal class ToolingApiServerImpl(private val project: ProjectImpl) :
  IToolingApiServer {

  private var client: IToolingApiClient? = null
  private var connector: GradleConnector? = null
  private var connection: ProjectConnection? = null
  private var lastInitParams: InitializeProjectParams? = null
  private var _buildCancellationToken: CancellationTokenSource? = null

  private val cancellationTokenAccessLock = ReentrantLock(/* fair = */ true)
  private var buildCancellationToken: CancellationTokenSource?
    get() = cancellationTokenAccessLock.withLock { _buildCancellationToken }
    set(value) = cancellationTokenAccessLock.withLock { _buildCancellationToken = value }

  /**
   * Whether the project has been initialized or not.
   */
  var isInitialized: Boolean = false
    private set

  /**
   * Whether a build or project synchronization is in progress.
   */
  private var isBuildInProgress: Boolean = false

  /**
   * Whether the server has a live connection to Gradle.
   */
  val isConnected: Boolean
    get() = connector != null || connection != null

  companion object {

    private val log = LoggerFactory.getLogger(ToolingApiServerImpl::class.java)

    /**
     * Time duration for which the the Tooling API server waits after calling
     * [DefaultGradleConnector.close] and before exiting the server's process.
     *
     * This delay should be long enough to let the tooling API stop the daemon but short enough so
     * that the server's process is not kept alive for longer duration.
     */
    const val DELAY_BEFORE_EXIT_MS = 1000L
  }

  override fun metadata(): CompletableFuture<ToolingServerMetadata> {
    return CompletableFuture.supplyAsync {
      ToolingServerMetadata(ProcessHandle.current().pid().toInt())
    }
  }

  override fun initialize(params: InitializeProjectParams): CompletableFuture<InitializeResult> {
    return runBuild {
      try {
        log.debug("Received project initialization request with params: {}", params)

        Main.checkGradleWrapper()

        if (buildCancellationToken != null) {
          cancelCurrentBuild().get()
        }

        val projectDirectory = File(params.directory)
        val failureReason = validateProjectDirectory(projectDirectory)

        if (failureReason != null) {
          log.error("Cannot initialize project: {}", failureReason)
          return@runBuild InitializeResult(false, failureReason)
        }

        val stopWatch = StopWatch("Connection to project")
        val isReinitializing = connector != null && connection != null && params == lastInitParams

        if (isReinitializing) {
          log.info("Project is being reinitialized")
          log.info("Reusing connector instance...")
        } else {
          // a new project is being initialized
          // or the project is being initialized with different parameters
          connector?.disconnect()

          connector = GradleConnector.newConnector().forProjectDirectory(projectDirectory)
          setupConnectorForGradleInstallation(this.connector!!, params.gradleDistribution)
          stopWatch.lap("Connector created")
        }

        lastInitParams = params

        val connector = checkNotNull(connector) {
          "Unable to create gradle connector for project directory: ${params.directory}"
        }

        notifyBeforeBuild(BuildInfo(emptyList()))

        if (isReinitializing) {
          log.info("Reusing project connection...")
        } else {
          connection = connector.connect()
        }

        val connection = checkNotNull(this.connection) {
          "Unable to create project connection for project directory: ${params.directory}"
        }

        stopWatch.lapFromLast("Project connection established")

        this.buildCancellationToken = GradleConnector.newCancellationTokenSource()

        val project = try {
          val modelBuilderParams = RootProjectModelBuilderParams(
            connection,
            this.buildCancellationToken!!.token()
          )
          val impl = RootModelBuilder(params).build(modelBuilderParams) as? ProjectImpl?
            ?: throw ModelBuilderException("Failed to build project model")
          impl
        } catch (err: Throwable) {
          throw err
        }

        stopWatch.lapFromLast("Project read successful")
        stopWatch.log()

        this.project.setFrom(project)
        this.isInitialized = true

        notifyBuildSuccess(emptyList())
        return@runBuild InitializeResult(true)
      } catch (err: Throwable) {
        log.error("Failed to initialize project", err)
        notifyBuildFailure(emptyList())
        return@runBuild InitializeResult(false, getTaskFailureType(err))
      }
    }
  }

  private fun validateProjectDirectory(
    projectDirectory: File
  ) = when {
    !projectDirectory.exists() -> PROJECT_NOT_FOUND
    !projectDirectory.isDirectory -> PROJECT_NOT_DIRECTORY
    !projectDirectory.canRead() -> PROJECT_DIRECTORY_INACCESSIBLE
    else -> null
  }

  override fun isServerInitialized(): CompletableFuture<Boolean> {
    return CompletableFuture.supplyAsync { isInitialized }
  }

  override fun getRootProject(): CompletableFuture<IProject> {
    return CompletableFuture.supplyAsync {
      assertProjectInitialized()
      return@supplyAsync this.project
    }
  }

  override fun executeTasks(message: TaskExecutionMessage): CompletableFuture<TaskExecutionResult> {
    return runBuild {
      if (!isServerInitialized().get()) {
        log.error("Cannot execute tasks: {}", PROJECT_NOT_INITIALIZED)
        return@runBuild TaskExecutionResult(false, PROJECT_NOT_INITIALIZED)
      }

      val lastInitParams = this.lastInitParams
      if (lastInitParams != null) {
        val projectDirectory = File(lastInitParams.directory)
        val failureReason = validateProjectDirectory(projectDirectory)
        if (failureReason != null) {
          log.error("Cannot execute tasks: {}", failureReason)
          return@runBuild TaskExecutionResult(isSuccessful = false, failureReason)
        }
      }

      log.debug("Received request to run tasks: {}", message)

      Main.checkGradleWrapper()

      val connection = checkNotNull(this.connection) {
        "ProjectConnection has not been initialized. Cannot execute tasks."
      }

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
        return@runBuild TaskExecutionResult.SUCCESS
      } catch (error: Throwable) {
        notifyBuildFailure(message.tasks)
        return@runBuild TaskExecutionResult(false, getTaskFailureType(error))
      }
    }
  }

  private fun setupConnectorForGradleInstallation(
    connector: GradleConnector,
    params: GradleDistributionParams
  ) {
    when (params.type) {
      GradleDistributionType.GRADLE_WRAPPER -> {
        log.info("Using Gradle wrapper for build...")
      }

      GradleDistributionType.GRADLE_INSTALLATION -> {
        val file = File(params.value)
        if (!file.exists() || !file.isDirectory) {
          log.error("Specified Gradle installation does not exist: {}", params)
          return
        }

        log.info("Using Gradle installation: {}", file.canonicalPath)
        connector.useInstallation(file)
      }

      GradleDistributionType.GRADLE_VERSION -> {
        log.info("Using Gradle version '{}'", params.value)
        connector.useGradleVersion(params.value)
      }
    }
  }

  private fun notifyBuildFailure(tasks: List<String>) {
    client?.onBuildFailed(BuildResult((tasks)))
  }

  private fun notifyBuildSuccess(tasks: List<String>) {
    client?.onBuildSuccessful(BuildResult(tasks))
  }

  private fun notifyBeforeBuild(buildInfo: BuildInfo) {
    client?.prepareBuild(buildInfo)
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
        this.buildCancellationToken = null
      } catch (e: Exception) {
        val failureReason = CANCELLATION_ERROR
        failureReason.message = "${failureReason.message}: ${e.message}"
        return@supplyAsync BuildCancellationRequestResult(false, failureReason)
      }

      return@supplyAsync BuildCancellationRequestResult(true, null)
    }
  }

  override fun shutdown(): CompletableFuture<Void> {
    return CompletableFuture.supplyAsync {
      log.info("Shutting down Tooling API Server...")

      connection?.close()
      connector?.disconnect()
      connection = null
      connector = null

      // Stop all daemons
      log.info("Stopping all Gradle Daemons...")
      DefaultGradleConnector.close()

      // update the initialization flag before cancelling future
      this.isInitialized = false

      // cancelling this future will finish the Tooling API server process
      // see com.itsaky.androidide.tooling.impl.Main.main(String[])
      Main.future?.cancel(true)

      this.client = null
      this.buildCancellationToken = null // connector.disconnect() cancels any running builds
      this.lastInitParams = null
      Main.future = null
      Main.client = null

      null
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

  private inline fun <T : Any?> supplyAsync(crossinline action: () -> T): CompletableFuture<T> =
    CompletableFuture.supplyAsync {
      action()
    }

  private inline fun <T : Any?> runBuild(crossinline action: () -> T): CompletableFuture<T> =
    supplyAsync {
      if (isBuildInProgress) {
        log.error("Cannot run build, build is already in prorgess!")
        throw IllegalStateException("Build is already in progress")
      }

      isBuildInProgress = true
      try {
        action()
      } finally {
        isBuildInProgress = false
      }
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
