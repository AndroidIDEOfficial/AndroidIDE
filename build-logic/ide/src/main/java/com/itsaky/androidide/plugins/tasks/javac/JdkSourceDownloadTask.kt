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

package com.itsaky.androidide.plugins.tasks.javac

import com.itsaky.androidide.plugins.util.DownloadUtils
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Task to download JDK source code.
 *
 * @author Akash Yadav
 */
abstract class JdkSourceDownloadTask : DefaultTask() {

  /**
   * The commit hash to download.
   */
  @get:Input
  abstract val commitHash: Property<String>

  /**
   * The SHA-256 hash of the source tarball.
   */
  @get:Input
  abstract val sourceSHA256: Property<String>

  /**
   * The directory to download the source tarbar to.
   */
  @get:OutputFile
  @get:Optional
  abstract val outputFile: RegularFileProperty

  init {
    run {
      commitHash.convention(DEFAULT_JDK_COMMIT_HASH)
      sourceSHA256.convention(DEFAULT_JDK_SOURCE_SHA)
      outputFile.convention(project.layout.buildDirectory.dir(name).get().file("${commitHash.get()}.tar.gz"))
    }
  }

  companion object {

    private const val DEFAULT_JDK_COMMIT_HASH = "3036d3921cae2900aab4610b1e22ee816ac64e2d"
    private const val DEFAULT_JDK_SOURCE_SHA = "d6d9aa6465becf1332bdca6d60cbf37f66ac0d2e7755c3197125d452f03ec657"
    private const val REPOSITORY_OWNER = "itsaky"
    private const val REPOSITORY_NAME = "openjdk-21-android"
    private const val JDK_DOWNLOAD_URL = "https://github.com/${REPOSITORY_OWNER}/${REPOSITORY_NAME}/archive/%s.tar.gz"
  }

  @TaskAction
  fun downloadSource() {
    val downloadUrl = JDK_DOWNLOAD_URL.format(commitHash.get())
    val outFile = outputFile.get().asFile

    DownloadUtils.doDownload(outFile, downloadUrl, sourceSHA256.get(), logger)

    if (!outFile.exists() || !outFile.isFile) {
      throw GradleException("$outFile does not exist or is not a file.")
    }
  }

  fun jdkSourceDirName() : String {
    return "${REPOSITORY_NAME}-${commitHash.get()}"
  }
}