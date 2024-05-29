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

package com.itsaky.androidide.plugins

import com.itsaky.androidide.build.config.BuildConfig
import com.itsaky.androidide.plugins.exts.GroupConfigPluginExtension.Companion.groupConfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author Akash Yadav
 */
class GroupConfigPlugin : Plugin<Project> {
  override fun apply(target: Project) = target.run {
    val extension = groupConfigExtension()
    extension.groupIdSuffix.convention("")

    afterEvaluate {
      val suffix = extension.groupIdSuffix.get().let {
        if (it.isNotEmpty()) ".${it}" else ""
      }

      project.group = "${BuildConfig.packageName}${suffix}"
      logger.info("Maven group ID set to : ${project.group}")
    }
  }
}