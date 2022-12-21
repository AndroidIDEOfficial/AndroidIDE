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

import com.itsaky.androidide.activities.editor.EditorHandlerActivity
import com.itsaky.androidide.preferences.internal.isFirstBuild
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.services.GradleBuildService
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
  
  fun releaes() {
  }

  override fun prepareBuild() {
    val isFirstBuild = isFirstBuild
    activity()
      .setStatus(
        activity().getString(if (isFirstBuild) string.preparing_first else string.preparing)
      )

    if (isFirstBuild) {
      activity().showFirstBuildNotice()
    }

    activity().viewModel.isBuildInProgress = true
  }

  override fun onBuildSuccessful(tasks: MutableList<String>) {
    analyzeCurrentFile()
    appendOutputSeparator()

    isFirstBuild = false
    activity().viewModel.isBuildInProgress = false
  }

  override fun onProgressEvent(event: ProgressEvent) {
    if (event is ProjectConfigurationStartEvent || event is TaskStartEvent) {
      activity().setStatus(event.descriptor.displayName)
    }
  }

  private fun appendOutputSeparator() {
    activity().appendBuildOutput("\n\n")
  }

  override fun onBuildFailed(tasks: MutableList<String>) {
    analyzeCurrentFile()
    appendOutputSeparator()

    isFirstBuild = false
    activity().viewModel.isBuildInProgress = false
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
