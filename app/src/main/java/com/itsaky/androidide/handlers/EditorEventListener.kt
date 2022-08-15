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

import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.R.string
import com.itsaky.androidide.models.prefs.isFirstBuild
import com.itsaky.androidide.services.GradleBuildService
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.tooling.events.download.FileDownloadFinishEvent
import com.itsaky.androidide.tooling.events.task.TaskProgressEvent
import com.itsaky.androidide.utils.ILogger
import java.lang.ref.WeakReference

/**
 * Handles events received from [GradleBuildService] updates [EditorActivity].
 * @author Akash Yadav
 */
class EditorEventListener : GradleBuildService.EventListener {

  private var activityReference: WeakReference<EditorActivity?> = WeakReference(null)
  private val log = ILogger.newInstance(javaClass.simpleName)

  fun setActivity(activity: EditorActivity) {
    this.activityReference = WeakReference(activity)
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

    activity().viewModel.progressBarVisible.value = true
  }

  override fun onBuildSuccessful(tasks: MutableList<String>) {
    analyzeCurrentFile()
    appendOutputSeparator()

    isFirstBuild = false
    activity().viewModel.progressBarVisible.value = false
    activity().invalidateOptionsMenu()
  }

  override fun onProgressEvent(event: ProgressEvent) {
    if (event is FileDownloadFinishEvent) {
      activity().setStatus("")
    } else if (event is TaskProgressEvent) {
      activity().setStatus(activity().getString(string.msg_running_task, event.descriptor.taskPath))
    }
  }

  private fun appendOutputSeparator() {
    activity().appendBuildOut("\n\n")
  }

  override fun onBuildFailed(tasks: MutableList<String>) {
    analyzeCurrentFile()
    appendOutputSeparator()

    isFirstBuild = false
    activity().viewModel.progressBarVisible.value = false
    activity().invalidateOptionsMenu()
  }

  override fun onOutput(line: String?) {
    activity().appendBuildOut(line)

    // TODO This can be handled better when ProgressEvents are received from Tooling API server
    if (line!!.contains("BUILD SUCCESSFUL") || line.contains("BUILD FAILED")) {
      activity().setStatus(line)
    }
  }

  private fun analyzeCurrentFile() {
    val editorView = activity().currentEditor
    if (editorView != null) {
      val editor = editorView.editor
      editor?.analyze()
    }
  }

  fun activity(): EditorActivity {
    return activityReference.get()
      ?: throw IllegalStateException("Activity reference has been destroyed!")
  }
}
