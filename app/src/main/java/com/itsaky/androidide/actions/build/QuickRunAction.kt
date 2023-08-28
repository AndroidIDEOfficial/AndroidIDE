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

package com.itsaky.androidide.actions.build

import android.content.Context
import androidx.core.content.ContextCompat
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.BaseBuildAction
import com.itsaky.androidide.actions.requireContext
import com.itsaky.androidide.models.ApkMetadata
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.api.models.BasicAndroidVariantMetadata
import com.itsaky.androidide.utils.ApkInstaller
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.InstallationResultHandler
import com.itsaky.androidide.utils.flashError
import java.io.File

/**
 * The 'Quick Run' action in the editor activity.
 *
 * @author Akash Yadav
 */
class QuickRunAction(context: Context) : BaseBuildAction() {

  init {
    label = context.getString(R.string.quick_run_debug)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_run_outline)
  }

  override val id: String = "ide.editor.build.quickRun"

  // Execute on UI thread as this action might try to show dialogs to the user
  override var requiresUIThread: Boolean = true

  override fun execAction(data: ActionData): Boolean {
    chooseApplication(data) { module ->
      val activity = data.requireActivity()
      val projectManager = IProjectManager.getInstance()

      val variant = module.getSelectedVariant() ?: run {
        activity.flashError(
          activity.getString(R.string.err_selected_variant_not_found))
        return@chooseApplication
      }

      val taskName = "${module.path}:${variant.mainArtifact.assembleTaskName}"
      log.info(
        "Running task '$taskName' to assemble variant '${variant.name}' of project '${module.path}'")

      execTasks(
        data,
        resultHandler = { result ->
          executeAsyncProvideError({ handleResult(data, result, module, variant) }) { _, error ->
            if (error != null) {
              log.error("Failed to process task execution result", error)
            }
          }
        },
        taskName
      )
    }
    return true
  }

  private fun handleResult(
    data: ActionData,
    result: TaskExecutionResult?,
    module: AndroidModule,
    variant: BasicAndroidVariantMetadata
  ) {
    if (result == null || !result.isSuccessful) {
      log.debug("Cannot install APK. Task execution result:", result)
      return
    }

    log.debug("Installing APK(s) for project: '${module.path}' variant: '${variant.name}'")

    val main = variant.mainArtifact
    val outputListingFile = main.assembleTaskOutputListingFile
    if (outputListingFile == null) {
      log.error("No output listing file provided with project model")
      return
    }

    log.verbose("Parsing metadata")
    val apkFile = ApkMetadata.findApkFile(outputListingFile)
    if (apkFile == null) {
      log.error("No apk file specified in output listing file:", outputListingFile)
      return
    }

    if (!apkFile.exists()) {
      log.error("APK file specified in output listing file does not exist!", apkFile)
      return
    }

    install(data, apkFile)
  }

  private fun chooseApplication(data: ActionData, callback: (AndroidModule) -> Unit) {
    val projectManager = IProjectManager.getInstance()
    val applications = projectManager.getAndroidAppModules()
    if (applications.isEmpty()) {
      log.error("Cannot run application. No application modules found in project.")
      return
    }

    if (applications.size == 1) {
      // Only one application module in available in the project.
      callback(applications.first())
      return
    }

    // there are multiple application modules in the project
    // ask the user to select the application module to build
    val context = data.requireContext()
    val builder = DialogUtils.newSingleChoiceDialog(
      context,
      context.getString(com.itsaky.androidide.R.string.title_choose_application),
      applications.map { it.path }.toTypedArray(),
      0) { selection ->
      val app = applications[selection]
      log.info("Selected application: '${app.path}'")
      callback(app)
    }

    builder.show()
  }

  private fun install(data: ActionData, apk: File) {
    val activity =
      data.getActivity()
        ?: run {
          log.error("Cannot install APK. Unable to get activity instance.")
          return
        }

    activity.runOnUiThread {
      log.debug("Installing APK:", apk)

      if (!apk.exists()) {
        log.error("APK file does not exist!")
        return@runOnUiThread
      }

      ApkInstaller.installApk(
        activity,
        InstallationResultHandler.createEditorActivitySender(activity),
        apk,
        activity.installationSessionCallback()
      )
    }
  }
}
