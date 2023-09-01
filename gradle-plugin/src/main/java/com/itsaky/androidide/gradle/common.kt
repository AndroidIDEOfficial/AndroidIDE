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
import com.itsaky.androidide.gradle.AndroidIDEInitScriptPlugin.Companion.PROPERTY_IS_TEST_ENV
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * @author Akash Yadav
 */

const val APP_PLUGIN = "com.android.application"
const val LIBRARY_PLUGIN = "com.android.library"

internal val Project.isTestEnv: Boolean
  get() = hasProperty(PROPERTY_IS_TEST_ENV) && property(
    PROPERTY_IS_TEST_ENV).toString().toBoolean()

fun Project.ideDependency(artifact: String): Dependency {
  return dependencies.ideDependency(artifact, isTestEnv)
}

fun DependencyHandler.ideDependency(artifact: String, testEnv: Boolean): Dependency {
  val version = if (testEnv) {
    BuildInfo.VERSION_NAME_SIMPLE
  } else {
    BuildInfo.VERSION_NAME_DOWNLOAD
  }

  return create("${BuildInfo.MVN_GROUP_ID}:${artifact}:${version}")
}
