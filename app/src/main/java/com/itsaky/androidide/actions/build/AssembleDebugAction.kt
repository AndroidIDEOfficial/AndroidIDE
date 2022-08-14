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
import com.blankj.utilcode.util.IntentUtils
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.BaseBuildAction
import com.itsaky.androidide.models.ApkMetadata
import com.itsaky.androidide.projects.ProjectManager
import java.io.File

/** @author Akash Yadav */
open class AssembleDebugAction() : BaseBuildAction() {

  protected open var installApk: Boolean = false
  
  constructor(context: Context) : this() {
    label = context.getString(R.string.build_debug)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_run_debug)
  }

  override val id: String = "editor_assembleDebug"

  override fun execAction(data: ActionData): Boolean {
    execTasks(
      data,
      resultHandler = { result ->
        if (result == null || !result.isSuccessful) {
          log.debug("Cannot install APK. Task execution result:", result)
          return@execTasks
        }
        
        if (!installApk) {
          return@execTasks
        }

        log.debug("Installing APK(s) for variant: debug")
        // TODO Handle multiple application modules
        val projectManager = ProjectManager
        val app = projectManager.getApplicationModule()
        if (app == null) {
          log.warn("No application module found. Cannot install APKs")
          return@execTasks
        }

        val foundVariant = app.variants.stream().filter { it.name == "debug" }.findFirst()

        if (!foundVariant.isPresent) {
          log.error("No debug variant found in application module", app.path)
          return@execTasks
        }

        val (_, main) = foundVariant.get()
        val outputListingFile = main.assembleTaskOutputListingFile
        if (outputListingFile == null) {
          log.error("No output listing file provided with project model")
          return@execTasks
        }

        val apkFile = ApkMetadata.findApkFile(outputListingFile)
        if (apkFile == null) {
          log.error("No apk file specified in output listing file:", outputListingFile)
          return@execTasks
        }

        if (!apkFile.exists()) {
          log.error("APK file specified in output listing file does not exist!", apkFile)
          return@execTasks
        }

        install(data, apkFile)
      },
      "assembleDebug"
    )
    return true
  }

  private fun install(data: ActionData, apk: File) {
    val activity =
      getActivity(data)
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

      IntentUtils.getInstallAppIntent(apk).let { activity.startActivity(it) }
    }
  }
}
