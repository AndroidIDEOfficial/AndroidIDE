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
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.tooling.api.model.IdeProject
import com.itsaky.androidide.tooling.api.model.internal.DefaultIdeProject
import com.itsaky.androidide.tooling.impl.util.InitScriptHandler
import com.itsaky.androidide.tooling.impl.util.ProjectReader
import com.itsaky.androidide.tooling.impl.util.StopWatch
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.*
import org.eclipse.lsp4j.jsonrpc.CompletableFutures
import org.gradle.tooling.BuildAction
import org.gradle.tooling.ConfigurableLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ModelBuilder
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.GradleProject
import org.gradle.tooling.model.HierarchicalElement
import org.gradle.tooling.model.eclipse.EclipseProject
import org.gradle.tooling.model.idea.IdeaProject

/**
 * Implementation for the Gradle Tooling API server.
 *
 * @author Akash Yadav
 */
internal class ToolingApiServerImpl : IToolingApiServer {

    private var initialized = false
    private var client: IToolingApiClient? = null
    private var connector: GradleConnector? = null
    private var project: IdeProject? = null
    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun initialize(params: InitializeProjectParams): CompletableFuture<Void> {
        return CompletableFutures.computeAsync {
            val stopWatch = StopWatch("Connection to project")
            this.connector = GradleConnector.newConnector().forProjectDirectory(params.directory)
            stopWatch.lap("Connector created")

            if (this.connector == null) {
                throw CompletionException(
                    RuntimeException(
                        "Unable to create gradle connector for project directory: ${params.directory}"))
            }

            val connection = this.connector!!.connect()
            stopWatch.lapFromLast("Project connection established")
            
            this.project = ProjectReader.read(connection)
            stopWatch.lapFromLast("Project read ${if(this.project == null) "failed" else "successful"}")

            connection.close()
            stopWatch.log()

            initialized = true
            return@computeAsync null
        }
    }

    private fun ensureIsRoot(element: HierarchicalElement): HierarchicalElement {
        var el = element
        while (el.parent != null) {
            el = el.parent
        }

        return el
    }

    override fun isInitialized(): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { initialized }
    }

    override fun getRootProject(): CompletableFuture<IdeProject> {
        return CompletableFutures.computeAsync {
            assertProjectInitialized()

            return@computeAsync null
        }
    }

    private fun assertProjectInitialized() {
        if (!isInitialized().get()) {
            throw CompletionException(IllegalStateException("Project is not initialized!"))
        }
    }

    fun connect(client: IToolingApiClient) {
        this.client = client
    }

    private fun applyArguments(launcher: ConfigurableLauncher<*>) {
        launcher.withArguments("--init-script", InitScriptHandler.getInitScript().absolutePath)
    }
}
