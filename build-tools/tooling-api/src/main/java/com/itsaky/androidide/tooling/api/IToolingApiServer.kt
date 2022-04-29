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

package com.itsaky.androidide.tooling.api

import com.itsaky.androidide.tooling.api.messages.InitializeProjectMessage
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import java.util.concurrent.*
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment

/**
 * A tooling api server provides services related to the Gradle Tooling API.
 *
 * @author Akash Yadav
 */
@JsonSegment("server")
interface IToolingApiServer {

    /** Initialize the server with the project directory. */
    @JsonRequest("initializeProject")
    fun initialize(params: InitializeProjectMessage): CompletableFuture<InitializeResult>

    /** Is the server initialized? */
    @JsonRequest("isInitialized") fun isInitialized(): CompletableFuture<Boolean>

    /** Get the root project. */
    @JsonRequest("getRootProject") fun getRootProject(): CompletableFuture<IdeGradleProject>
}
