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

package com.itsaky.androidide

import org.gradle.api.JavaVersion

/**
 * Version constants for the IDE.
 *
 * @author Akash Yadav
 */
object Versions {

  /** The compile SDK version. */
  const val compileSdk = 32

  /** The build tools version. */
  const val buildTools = "31.0.0"

  /** The minimum SDK version. */
  const val minSdk = 26

  /** The target SDK version. */
  const val targetSdk = 28

  /** The version code. */
  const val versionCode = 212

  /**
   * The version name for the IDE. When in CI build, includes the [releaseVersionName], branch name
   * and the short commit hash.
   */
  val versionName by lazy {
    var version = releaseVersionName
    if (CI.isCiBuild) {
      // e.g. 2.1.2-beta_main-d5e9c9ea
      version += "_${CI.branchName}-${CI.commitHash}"
    }
    version
  }

  /** The source and target Java compatibility. */
  val javaVersion = JavaVersion.VERSION_11

  /** The release version name. */
  const val releaseVersionName = "2.1.2-beta"
}
