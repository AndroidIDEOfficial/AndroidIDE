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

package com.itsaky.androidide.handlers

import com.itsaky.androidide.R
import com.itsaky.androidide.activities.editor.EditorHandlerActivity
import com.itsaky.androidide.preferences.internal.isFirstBuild
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.services.builder.GradleBuildService
import com.itsaky.androidide.tooling.api.messages.result.BuildInfo
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.tooling.events.configuration.ProjectConfigurationStartEvent
import com.itsaky.androidide.tooling.events.task.TaskStartEvent
import java.lang.ref.WeakReference

/**
 * Handles events received from [GradleBuildService] updates [EditorHandlerActivity].
 * @author Akash Yadav
 */
class EditorBuildEventListener : GradleBuildService.EventListener {

  private var activityReference: WeakReference<EditorHandlerActivity> = WeakReference(null)

  fun setActivity(activity: EditorHandlerActivity) {
    this.activityReference = WeakReference(activity)
  }

  override fun prepareBuild(buildInfo: BuildInfo) {
    val isFirstBuild = isFirstBuild
    activity()
      .setStatus(
        activity().getString(if (isFirstBuild) string.preparing_first else string.preparing)
      )

    if (isFirstBuild) {
      activity().showFirstBuildNotice()
    }

    activity().editorViewModel.isBuildInProgress = true
    activity().binding.bottomSheet.clearBuildOutput()

    if (buildInfo.tasks.isNotEmpty()) {
      activity().binding.bottomSheet.appendBuildOut(
        activity().getString(R.string.title_run_tasks) + " : " + buildInfo.tasks)
    }
  }

  override fun onBuildSuccessful(tasks: MutableList<String>) {
    analyzeCurrentFile()

    isFirstBuild = false
    activity().editorViewModel.isBuildInProgress = false
  }

  override fun onProgressEvent(event: ProgressEvent) {
    if (event is ProjectConfigurationStartEvent || event is TaskStartEvent) {
      activity().setStatus(event.descriptor.displayName)
    }
  }

  override fun onBuildFailed(tasks: MutableList<String>) {
    analyzeCurrentFile()

    isFirstBuild = false
    activity().editorViewModel.isBuildInProgress = false
  }

  override fun onOutput(line: String?) {
    line?.let { activity().appendBuildOutput(it) }
    // TODO This can be handled better when ProgressEvents are received from Tooling API server
    if (line!!.contains("BUILD SUCCESSFUL") || line.contains("BUILD FAILED")) {
      activity().setStatus(line)
    }
  }

  private fun analyzeCurrentFile() {
    val editorView = activity().getCurrentEditor()
    if (editorView != null) {
      val editor = editorView.editor
      editor?.analyze()
    }
  }

  fun activity(): EditorHandlerActivity {
    return activityReference.get()
      ?: throw IllegalStateException("Activity reference has been destroyed!")
  }
}
