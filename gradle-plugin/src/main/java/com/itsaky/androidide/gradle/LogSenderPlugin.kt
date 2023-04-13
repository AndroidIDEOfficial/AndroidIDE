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

package com.itsaky.androidide.gradle

import com.itsaky.androidide.buildinfo.BuildInfo
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin to manage LogSender in Android applications.
 *
 * @author Akash Yadav
 */
class LogSenderPlugin : Plugin<Project> {

  companion object {
    private const val LOGSENDER_DEPENDENCY_ARTIFACT = "logsender"
    private const val LOGSENDER_DEPENDENCY =
      "${BuildInfo.MVN_GROUP_ID}:${LOGSENDER_DEPENDENCY_ARTIFACT}:${BuildInfo.VERSION_NAME_PUBLISHING}"
  }

  override fun apply(target: Project) {
    target.run {
      check(plugins.hasPlugin(APP_PLUGIN)) {
        "${javaClass.simpleName} can only be applied to Android application projects."
      }

      val extension = extensions.create("logsender", LogSenderPluginExtension::class.java)
      
      configurations
        .getByName("${extension.variant}RuntimeOnly")
        .dependencies
        .add(dependencies.create(LOGSENDER_DEPENDENCY))
    }
  }
}
