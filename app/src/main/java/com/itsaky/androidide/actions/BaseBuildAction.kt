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

package com.itsaky.androidide.actions

import android.text.TextUtils
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.UNKNOWN
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError

/**
 * Marker class for actions that execute build related tasks.
 *
 * @author Akash Yadav
 */
abstract class BaseBuildAction : EditorActivityAction() {

  protected val log: ILogger = ILogger.newInstance(javaClass.simpleName)
  protected val buildService: BuildService?
    get() = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)

  override fun prepare(data: ActionData) {
    val context = data.getActivity()
    if (context == null) {
      visible = false
      return
    } else {
      visible = true
    }

    if (isBuildInProgress()) {
      enabled = false
      return
    } else {
      enabled = true
    }
  }

  fun shouldPrepare() = visible && enabled

  private fun isBuildInProgress(): Boolean {
    return buildService == null || buildService?.isBuildInProgress == true
  }

  @JvmOverloads
  protected fun execTasks(
    data: ActionData,
    resultHandler: (TaskExecutionResult?) -> Unit = {},
    vararg tasks: String,
  ) {

    val buildService = this.buildService ?: return
    if (!buildService.isToolingServerStarted()) {
      flashError(R.string.msg_tooling_server_unavailable)
      return
    }

    val activity =
      data.getActivity()
        ?: run {
          resultHandler(TaskExecutionResult(false, UNKNOWN))
          return
        }

    activity.saveAll()

    buildService.executeTasks(tasks = tasks).whenComplete { result, err ->
      if (result == null || err != null) {
        log.error("Tasks failed to execute", TextUtils.join(", ", tasks))
      }

      resultHandler(result)
    }
  }
}
