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

package com.itsaky.androidide.testing.tooling.models

import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IToolingApiServer
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import java.util.concurrent.CompletableFuture

/**
 * Scope for Tooling API tests. Provides access to the [IToolingApiServer], [IProject] and the [InitializeResult].
 *
 * @author Akash Yadav
 */
class ToolingApiTestScope(
  val server: IToolingApiServer,
  val project: IProject,
  val initializeResult: CompletableFuture<InitializeResult>
) {

  val result: InitializeResult?
    get() = initializeResult.get()
}