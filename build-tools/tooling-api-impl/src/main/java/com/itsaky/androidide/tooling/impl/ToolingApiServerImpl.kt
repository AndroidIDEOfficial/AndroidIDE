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
import com.itsaky.androidide.tooling.api.messages.ProjectInitializedResponse
import com.itsaky.androidide.utils.ILogger
import org.eclipse.lsp4j.jsonrpc.CompletableFutures
import java.util.concurrent.*

/**
 * Implementation for the Gradle Tooling API server.
 *
 * @author Akash Yadav
 */
internal class ToolingApiServerImpl : IToolingApiServer {

    private var initialized = false
    private var client: IToolingApiClient? = null
    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun initialize(
        params: InitializeProjectParams
    ): CompletableFuture<ProjectInitializedResponse> {
        return CompletableFutures.computeAsync {
            initialized = true
            return@computeAsync ProjectInitializedResponse()
        }
    }

    override fun isInitialized(): CompletableFuture<Boolean> {
        return CompletableFuture.supplyAsync { initialized }
    }

    fun connect(client: IToolingApiClient) {
        this.client = client
    }
}
