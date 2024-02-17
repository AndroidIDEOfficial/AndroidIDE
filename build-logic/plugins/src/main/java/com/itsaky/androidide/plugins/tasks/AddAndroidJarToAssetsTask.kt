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

package com.itsaky.androidide.plugins.tasks

import com.itsaky.androidide.build.config.BuildConfig
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * Adds the `android.jar` file from the Android SDK to assets.
 *
 * @author Akash Yadav
 */
abstract class AddAndroidJarToAssetsTask : DefaultTask() {

  /**
   * Path to the `android.jar` file.
   */
  @get:Internal
  internal var androidJar: File? = null

  /**
   * The output directory to copy the `android.jar` file to.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  @TaskAction
  fun addAndroidJarToAssets() {
    val outFile = outputDirectory.dir("data/common").get().asFile.also { dir -> dir.mkdirs() }
      .let { dir -> File(dir, "android.jar") }

    val androidJar = this.androidJar!!
    if (!androidJar.exists() || !androidJar.isFile) {
      throw GradleException("File $androidJar does not exist or is not a file.")
    }

    androidJar.copyTo(outFile, overwrite = true)
  }
}