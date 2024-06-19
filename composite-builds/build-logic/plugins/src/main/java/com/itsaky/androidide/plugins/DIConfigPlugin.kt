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

import com.itsaky.androidide.plugins.util.isAndroidModule
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension

/**
 * Configures dependency injection in a module.
 *
 * @author Akash Yadav
 */
class DIConfigPlugin : Plugin<Project> {

  override fun apply(target: Project) = target.run {
    dependencies.add("implementation", "javax.inject:javax.inject:1")
    if (isAndroidModule) {
      val libs =
        rootProject.extensions.getByType(VersionCatalogsExtension::class.java).named("libs")
      val hiltAndroid = libs.findLibrary("hilt-android").get().get()
      dependencies.add("implementation", hiltAndroid)
    }
    Unit
  }
}