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

import com.itsaky.androidide.plugins.AbstractDownloadTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * @author Akash Yadav
 */
abstract class DownloadAapt2Task : AbstractDownloadTask() {

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
   * The output directory.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  init {
    version.convention(DEFAULT_VERSION)
  }

  companion object {
    private const val DEFAULT_VERSION = "34.0.4"
    private val AAPT2_DOWNLOAD_URL = "https://github.com/AndroidIDEOfficial/platform-tools/releases/download/v%1\$s/aapt2-%2\$s"
  }

  @TaskAction
  fun downloadAapt2() {
    val arch = this.arch.get()
    val version = this.version.get()
    val expectedChecksum = this.checksum.get()

    val file = outputDirectory.file("data/$arch/aapt2").get().asFile
    file.parentFile.mkdirs()

    val remoteUrl = AAPT2_DOWNLOAD_URL.format(version, arch)

    doDownload(file, remoteUrl, expectedChecksum)
  }
}