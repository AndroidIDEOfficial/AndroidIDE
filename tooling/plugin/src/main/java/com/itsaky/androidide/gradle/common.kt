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
import com.itsaky.androidide.tooling.api.LogSenderConfig._PROPERTY_IS_TEST_ENV
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * @author Akash Yadav
 */

const val APP_PLUGIN = "com.android.application"
const val LIBRARY_PLUGIN = "com.android.library"

const val LIB_GROUP_LOGGING = "logging"
const val LIB_GROUP_TOOLING = "tooling"

internal val Project.isTestEnv: Boolean
  get() = hasProperty(_PROPERTY_IS_TEST_ENV) && property(
    _PROPERTY_IS_TEST_ENV).toString().toBoolean()

internal fun depVersion(testEnv: Boolean): String {
  return if (testEnv && !System.getenv("CI").toBoolean()) {
    BuildInfo.VERSION_NAME_SIMPLE
  } else {
    BuildInfo.VERSION_NAME_DOWNLOAD
  }
}

fun Project.ideDependency(group: String, artifact: String): Dependency {
  return dependencies.ideDependency(group, artifact, isTestEnv)
}

fun DependencyHandler.  ideDependency(group: String, artifact: String, testEnv: Boolean): Dependency {
  return create("${BuildInfo.MVN_GROUP_ID}.${group}:${artifact}:${depVersion(testEnv)}")
}
