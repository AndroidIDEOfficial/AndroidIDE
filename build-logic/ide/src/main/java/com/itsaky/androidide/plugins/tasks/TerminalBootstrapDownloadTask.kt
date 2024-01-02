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
import org.gradle.api.GradleException
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * @author Akash Yadav
 */
abstract class TerminalBootstrapDownloadTask : AbstractDownloadTask() {

  companion object {

    private const val PACKAGES_DOWNLOAD_URL = "https://github.com/AndroidIDEOfficial/terminal-packages/releases/download/bootstrap-%1\$s/bootstrap-%2\$s.zip"
  }

  /**
   * The bootstrap packages to download. The keys in this map are supported the CPU ABIs and
   * the values are the `sha256sum` of the bootstrap packages.
   */
  @get:Input
  abstract val packages: MapProperty<String, String>

  /**
   * Version of the bootstrap packages. This is basically the tag name of the GitHub release.
   */
  @get:Input
  abstract val version: Property<String>

  @TaskAction
  fun downloadBootstrap() {
    val packages = this.packages.get()
    val version = this.version.get()

    if (packages.isEmpty() || version.isNullOrBlank()) {
      throw GradleException("Packages map is empty!")
    }

    packages.forEach { (arch, sha256) ->
      val localPath = "src/main/cpp/bootstrap-${arch}.zip"
      val file = project.file(localPath)
      doDownload(file, PACKAGES_DOWNLOAD_URL.format(version, arch), sha256)
    }
  }
}