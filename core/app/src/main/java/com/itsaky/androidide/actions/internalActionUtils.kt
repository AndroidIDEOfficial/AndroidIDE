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

import android.content.Context
import com.itsaky.androidide.R
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.android.AndroidModule
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError

/**
 * @see openApplicationModuleChooser
 */
inline fun openApplicationModuleChooser(data: ActionData,
  crossinline callback: (AndroidModule) -> Unit) =
  openApplicationModuleChooser(data.requireContext(), callback)

/**
 * Shows a dialog to let the user choose between Android application modules in case the project has
 * multiple subproject with `com.android.application` plugin. If the project contains only a single
 * application module, it is selected by default and the dialog is not shown to the user.
 *
 * @param
 */
inline fun openApplicationModuleChooser(context: Context,
  crossinline callback: (AndroidModule) -> Unit) {
  val applications = IProjectManager.getInstance()
    .getWorkspace()
    ?.androidProjects()
    ?.filter(AndroidModule::isApplication)
    ?.toList() ?: emptyList()

  if (applications.isEmpty()) {
    flashError(R.string.msg_launch_failure_no_app_module)
    ILogger.ROOT.error("Cannot run application. No application modules found in project.")
    return
  }

  if (applications.size == 1) {
    // Only one application module in available in the project.
    callback(applications.first())
    return
  }

  // there are multiple application modules in the project
  // ask the user to select the application module to build
  val builder = DialogUtils.newSingleChoiceDialog(
    context,
    context.getString(R.string.title_choose_application),
    applications.map { it.path }.toTypedArray(),
    0
  ) { selection ->
    val app = applications[selection]
    ILogger.ROOT.info("Selected application: '{}'", app.path)
    callback(app)
  }

  builder.show()
}