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

import com.itsaky.androidide.plugins.util.DownloadUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * @author Akash Yadav
 */
abstract class SetupAapt2Task : DefaultTask() {

  /**
   * The CPU architecture for which the `aapt2` should be downloaded.
   */
  @get:Input
  abstract val arch: Property<String>

  /**
   * The version of the file, basically the tag name of the release.
   */
  @get:Input
  abstract val version: Property<String>

  /**
   * The SHA-256 checksum of the file to download. This will be used to verify the file's
   * integrity.
   */
  @get:Input
  abstract val checksum: Property<String>

  /**
   * The path of the static `aapt2` file which should be used instead of downloading it from GitHub.
   */
  @get:Input
  abstract val staticAapt2: Property<String>

  /**
   * The output directory.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  init {
    run {
      version.convention(DEFAULT_VERSION)
      staticAapt2.convention("")
    }
  }

  companion object {

    private const val DEFAULT_VERSION = "34.0.4"
    private const val AAPT2_DOWNLOAD_URL = "https://github.com/AndroidIDEOfficial/platform-tools/releases/download/v%1\$s/aapt2-%2\$s"
  }

  @TaskAction
  fun setupAapt2() {
    val file = outputDirectory.file("data/${arch.get()}/aapt2").get().asFile
    file.parentFile.deleteRecursively()
    file.parentFile.mkdirs()

    if (staticAapt2.getOrElse("").isNotBlank()) {
      val aapt2 = File(staticAapt2.get())

      require(aapt2.exists() && aapt2.isFile) {
        "F-Droid AAPT2 file does not exist or is not a file: $aapt2"
      }

      aapt2.copyTo(file, overwrite = true)
      return
    }

    val arch = this.arch.get()
    val version = this.version.get()
    val expectedChecksum = this.checksum.get()

    val remoteUrl = AAPT2_DOWNLOAD_URL.format(version, arch)

    DownloadUtils.doDownload(file, remoteUrl, expectedChecksum, logger)
  }
}