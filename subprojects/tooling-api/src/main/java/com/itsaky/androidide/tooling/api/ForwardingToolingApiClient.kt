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

import com.itsaky.androidide.tooling.api.messages.LogMessageParams
import com.itsaky.androidide.tooling.api.messages.result.BuildInfo
import com.itsaky.androidide.tooling.api.messages.result.BuildResult
import com.itsaky.androidide.tooling.api.messages.result.GradleWrapperCheckResult
import com.itsaky.androidide.tooling.events.ProgressEvent
import java.util.concurrent.CompletableFuture

/**
 * A [IToolingApiClient] which forwards all of its calls to the given client.
 *
 * @author Akash Yadav
 */
class ForwardingToolingApiClient(var client: IToolingApiClient?) : IToolingApiClient {

  override fun logMessage(params: LogMessageParams) {
    client?.logMessage(params)
  }

  override fun logOutput(line: String) {
    client?.logOutput(line)
  }

  override fun prepareBuild(buildInfo: BuildInfo) {
    client?.prepareBuild(buildInfo)
  }

  override fun onBuildSuccessful(result: BuildResult) {
    client?.onBuildSuccessful(result)
  }

  override fun onBuildFailed(result: BuildResult) {
    client?.onBuildFailed(result)
  }

  override fun onProgressEvent(event: ProgressEvent) {
    client?.onProgressEvent(event)
  }

  override fun getBuildArguments(): CompletableFuture<List<String>> {
    return client?.getBuildArguments() ?: CompletableFuture.completedFuture(emptyList())
  }

  override fun checkGradleWrapperAvailability(): CompletableFuture<GradleWrapperCheckResult> {
    return client?.checkGradleWrapperAvailability()
      ?: CompletableFuture.completedFuture(GradleWrapperCheckResult(false))
  }
}
