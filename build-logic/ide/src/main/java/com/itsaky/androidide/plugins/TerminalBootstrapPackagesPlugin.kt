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

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.itsaky.androidide.plugins.tasks.TerminalBootstrapDownloadTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Gradle plugin which downloads the bootstrap packages for the terminal.
 *
 * @author Akash Yadav
 */
class TerminalBootstrapPackagesPlugin : Plugin<Project> {

  companion object {

    /**
     * The bootstrap packages, mapped with the CPU ABI as the key and the ZIP file's sha256sum as the value.
     */
    private val BOOTSTRAP_PACKAGES = mapOf(
      "aarch64" to "68da03ed270d59cafcd37981b00583c713b42cb440adf03d1bf980f39a55181d",
      "arm" to "f3d9f2da7338bd00b02a8df192bdc22ad431a5eef413cecf4cd78d7a54ffffbf",
      "x86_64" to "6e4e50a206c3384c36f141b2496c1a7c69d30429e4e20268c51a84143530af67"
    )

    /**
     * The bootstrap packages version, basically the tag name of the GitHub release.
     */
    private const val BOOTSTRAP_PACKAGES_VERSION = "16.12.2023"
  }

  override fun apply(target: Project) {
    target.run {
      val downloadTask = tasks.register("downloadBootstrapPackages",
        TerminalBootstrapDownloadTask::class.java) {
        packages.set(BOOTSTRAP_PACKAGES)
        version.set(BOOTSTRAP_PACKAGES_VERSION)
      }

      val cleanTask = tasks.register("deleteBootstrapPackages", Delete::class.java)  {
        doLast {
          fileTree("src/main/cpp").apply {
            include("bootstrap-*.zip")
          }.forEach { file ->
            file.delete()
          }
        }
      }

      tasks.withType(JavaCompile::class.java).configureEach {
        dependsOn(downloadTask)
      }

      tasks.named("clean").configure {
        dependsOn(cleanTask)
      }
    }
  }
}