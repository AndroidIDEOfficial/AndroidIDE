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

package com.itsaky.androidide.plugins.util

import com.itsaky.androidide.build.config.BuildConfig
import com.android.build.api.variant.AndroidComponentsExtension
import java.io.File

/**
 * @author Akash Yadav
 */
object SdkUtils {

  fun AndroidComponentsExtension<*, *, *>.getAndroidJar(assertExists: Boolean = true): File {
    val sdkDirectory = sdkComponents.sdkDirectory
    val androidJar = File(sdkDirectory.get().asFile,
      "platforms/android-${BuildConfig.compileSdk}/android.jar")

    if (assertExists) {
      check(androidJar.exists() && androidJar.isFile) {
        "$androidJar does not exist or is not a file."
      }
    }

    return androidJar
  }
}