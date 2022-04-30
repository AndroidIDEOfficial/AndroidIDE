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

import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.TaskExecutionMessage
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
import com.itsaky.androidide.tooling.api.model.internal.DefaultProjectSyncIssues
import com.itsaky.androidide.tooling.impl.progress.LoggingProgressListener
import com.itsaky.androidide.tooling.impl.util.ProjectReader
import com.itsaky.androidide.tooling.impl.util.StopWatch
import com.itsaky.androidide.utils.ILogger
import org.eclipse.lsp4j.jsonrpc.CompletableFutures
import org.gradle.tooling.BuildCancelledException
import org.gradle.tooling.BuildException
import org.gradle.tooling.ConfigurableLauncher
import org.gradle.tooling.GradleConnectionException
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.UnsupportedVersionException
import org.gradle.tooling.exceptions.UnsupportedBuildArgumentException
import org.gradle.tooling.exceptions.UnsupportedOperationConfigurationException
import java.io.File
import java.util.concurrent.*

/**
 * Implementation for the Gradle Tooling API server.
 *
 * @author Akash Yadav
 */
internal class ToolingApiServerImpl : IToolingApiServer {

    private var initialized = false
    private var client: IToolingApiClient? = null
    private var connector: GradleConnector? = null
    private var project: IdeGradleProject? = null
    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun initialize(params: InitializeProjectMessage): CompletableFuture<InitializeResult> {
        return CompletableFutures.computeAsync {
            try {
                log.debug("Got initialize request", params)
                val stopWatch = StopWatch("Connection to project")
                this.connector =
                    GradleConnector.newConnector().forProjectDirectory(File(params.directory))
                stopWatch.lap("Connector created")

                if (this.connector == null) {
                    throw CompletionException(
                        RuntimeException(
                            "Unable to create gradle connector for project directory: ${params.directory}"))
                }

                val connection = this.connector!!.connect()
                stopWatch.lapFromLast("Project connection established")

                val issues: MutableMap<String, DefaultProjectSyncIssues> = mutableMapOf()
                this.project = ProjectReader.read(connection, issues)
                stopWatch.lapFromLast(
                    "Project read ${if(this.project == null) "failed" else "successful"}")

                connection.close()
                stopWatch.log()

                initialized = true

                return@computeAsync InitializeResult(project, issues)
            } catch (err: Throwable) {
                log.error(err)
            }

            return@computeAsync InitializeResult(project, emptyMap())
        }
    }

    override fun isInitialized(): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { initialized }
    }

    override fun getRootProject(): CompletableFuture<IdeGradleProject> {
        return CompletableFutures.computeAsync {
            assertProjectInitialized()
            return@computeAsync this.project
        }
    }

    override fun executeTasks(
        message: TaskExecutionMessage
    ): CompletableFuture<TaskExecutionResult> {
        return CompletableFutures.computeAsync {
            assertProjectInitialized()

            log.debug("Received request to run tasks.", message)

            var projectPath = message.projectPath
            if (projectPath == null) {
                projectPath = ":"
            }

            val project =
                this.project!!.findByPath(projectPath)
                    ?: return@computeAsync TaskExecutionResult(false, PROJECT_NOT_FOUND)

            val connection = this.connector!!.forProjectDirectory(project.projectDir).connect()
            val builder = connection.newBuild()
            builder.addProgressListener(LoggingProgressListener())

            // System.in and System.out are used for communication between this server and the
            // client.
            val out = LoggingOutputStream()
            builder.setStandardInput("NoOp".byteInputStream())
            builder.setStandardError(out)
            builder.setStandardOutput(out)
            builder.forTasks(*message.tasks.toTypedArray())
            applyArguments(builder)

            try {
                builder.run()
                return@computeAsync TaskExecutionResult(true, null)
            } catch (error: Throwable) {
                return@computeAsync TaskExecutionResult(false, getTaskFailureType(error))
            }
        }
    }

    private fun getTaskFailureType(error: Throwable): Failure =
        when (error) {
            is BuildException -> BUILD_FAILED
            is BuildCancelledException -> BUILD_CANCELLED
            is GradleConnectionException -> CONNECTION_ERROR
            is java.lang.IllegalStateException -> CONNECTION_CLOSED
            is UnsupportedVersionException -> UNSUPPORTED_GRADLE_VERSION
            is UnsupportedBuildArgumentException -> UNSUPPORTED_BUILD_ARGUMENT
            is UnsupportedOperationConfigurationException -> UNSUPPORTED_CONFIGURATION
            else -> UNKNOWN
        }

    private fun assertProjectInitialized() {
        if (!isInitialized().get() || project == null) {
            throw CompletionException(IllegalStateException("Project is not initialized!"))
        }
    }

    fun connect(client: IToolingApiClient) {
        this.client = client
    }

    private fun applyArguments(launcher: ConfigurableLauncher<*>) {}
}
