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

/**
 * Marker class for actions that execute build related tasks.
 *
 * @author Akash Yadav
 */
abstract class BaseBuildAction : EditorActivityAction() {

  protected val log: ILogger = ILogger.newInstance(javaClass.simpleName)
  protected val buildService: BuildService?
    get() = Lookup.DEFAULT.lookup(BuildService::class.java)

  override fun prepare(data: ActionData) {
    val context = getActivity(data)
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

  override fun postExec(data: ActionData, result: Any) {
    val context = getActivity(data) ?: return
    context.invalidateOptionsMenu()
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

    if (buildService == null) {
      return
    }

    val activity =
      getActivity(data)
        ?: run {
          resultHandler(TaskExecutionResult(false, UNKNOWN))
          return
        }

    activity.saveAll()
    activity.runOnUiThread {
      activity.appendBuildOut("Executing tasks: " + TextUtils.join(", ", tasks))
    }

    buildService!!.executeTasks(tasks = tasks).whenComplete { result, err ->
      if (result == null || err != null) {
        log.error("Tasks failed to execute", TextUtils.join(", ", tasks))
      }

      resultHandler(result)
    }
  }
}
