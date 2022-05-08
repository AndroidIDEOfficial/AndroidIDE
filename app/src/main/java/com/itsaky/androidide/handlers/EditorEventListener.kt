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

import android.content.DialogInterface
import android.text.Layout.Alignment.ALIGN_CENTER
import android.text.Layout.Alignment.ALIGN_NORMAL
import android.text.SpannableStringBuilder
import android.text.style.AlignmentSpan
import android.view.View
import android.webkit.URLUtil
import androidx.core.view.GravityCompat
import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.R.string
import com.itsaky.androidide.managers.PreferenceManager
import com.itsaky.androidide.services.GradleBuildService
import com.itsaky.androidide.tooling.events.ProgressEvent
import com.itsaky.androidide.tooling.events.StatusEvent
import com.itsaky.androidide.tooling.events.download.FileDownloadFinishEvent
import com.itsaky.androidide.tooling.events.download.FileDownloadOperationDescriptor
import com.itsaky.androidide.tooling.events.task.TaskProgressEvent
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.StudioUtils
import java.io.File
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
        val isFirstBuild =
            activity()
                .app.prefManager.getBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, true)
        activity()
            .setStatus(
                activity()
                    .getString(if (isFirstBuild) string.preparing_first else string.preparing))

        if (isFirstBuild) {
            activity().showFirstBuildNotice()
        }

        activity().binding.buildProgressIndicator.visibility = View.VISIBLE
    }

    override fun onBuildSuccessful(tasks: MutableList<String>) {
        analyzeCurrentFile()
        appendOutputSeparator()

        activity().app.prefManager.putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false)
        activity().binding.buildProgressIndicator.visibility = View.GONE
    }

    override fun onProgressEvent(event: ProgressEvent) {
        if (event is FileDownloadFinishEvent) {
            activity().setStatus("")
        } else if (event is TaskProgressEvent) {
            activity()
                .setStatus(activity().getString(string.msg_running_task, event.descriptor.taskPath))
        } else if (event is StatusEvent && event.descriptor is FileDownloadOperationDescriptor) {
            val total = StudioUtils.bytesToSizeString(event.total)
            val progress = StudioUtils.bytesToSizeString(event.progress)
            val fileName =
                URLUtil.guessFileName(
                    (event.descriptor as FileDownloadOperationDescriptor).uri.path, null, null)

            val status = SpannableStringBuilder()
            status.append(
                "[$progress/$total]",
                AlignmentSpan.Standard(ALIGN_NORMAL),
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
            status.append(
                " Download $fileName",
                AlignmentSpan.Standard(ALIGN_CENTER),
                SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
            activity().setStatus(status, GravityCompat.START)
        }
    }

    private fun appendOutputSeparator() {
        activity().appendBuildOut("\n\n")
    }

    override fun onBuildFailed(tasks: MutableList<String>) {
        analyzeCurrentFile()
        appendOutputSeparator()

        activity().app.prefManager.putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false)
        activity().binding.buildProgressIndicator.visibility = View.GONE
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

    private fun installApks(apks: Set<File>?) {
        if (apks == null || apks.isEmpty()) {
            IDEHandler.LOG.error("Cannot install APKs: $apks")
            return
        }

        if (apks.size == 1) {
            activity().install(apks.iterator().next())
        } else {
            IDEHandler.LOG.info("Multiple APKs found. Let the user select...")
            val files: List<File> = ArrayList(apks)
            val builder = DialogUtils.newMaterialDialogBuilder(activity())
            builder.setTitle(activity().getString(string.title_install_apks))
            builder.setItems(getNames(files)) { d: DialogInterface, w: Int ->
                d.dismiss()
                activity().install(files[w])
            }
            builder.show()
        }
    }

    private fun getNames(apks: Collection<File>): Array<String?> {
        val names = arrayOfNulls<String>(apks.size)
        for ((i, apk) in apks.withIndex()) {
            names[i] = apk.name
        }

        return names
    }
}
