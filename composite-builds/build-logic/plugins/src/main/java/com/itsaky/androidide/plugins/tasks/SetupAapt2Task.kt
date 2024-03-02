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

import com.itsaky.androidide.build.config.FDroidConfig
import com.itsaky.androidide.plugins.util.DownloadUtils
import com.itsaky.androidide.plugins.util.ELFUtils
import com.itsaky.androidide.build.config.isFDroidBuild
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

/**
 * @author Akash Yadav
 */
abstract class SetupAapt2Task : DefaultTask() {

  /**
   * The output directory.
   */
  @get:OutputDirectory
  abstract val outputDirectory: DirectoryProperty

  companion object {

    private val AAPT2_CHECKSUMS = mapOf(
      "arm64-v8a" to "be2cea61814678f7a9e61bf818a6666e6097a7d67d6c19498a4d7aa690bc4151",
      "armeabi-v7a" to "ba3413c680933dffd3c3d35da8d450c474ff5ccab95c4b9db28841c53b7a3cdf",
      "x86_64" to "4861171c1efcffe41f4466937e6a392b243ffb014813b4e60f0b77bb46ab254d"
    )

    private const val DEFAULT_VERSION = "34.0.4"
    private const val AAPT2_DOWNLOAD_URL = "https://github.com/AndroidIDEOfficial/platform-tools/releases/download/v%1\$s/aapt2-%2\$s"
  }

  @TaskAction
  fun setupAapt2() {

    // When building for F-Droid, simply copy the aapt2 file
    if (project.isFDroidBuild) {
      val arch = FDroidConfig.fDroidBuildArch!!

      val file = outputDirectory.file("${arch}/libaapt2.so").get().asFile
      file.parentFile.deleteRecursively()
      file.parentFile.mkdirs()

      val aapt2File = requireNotNull(FDroidConfig.aapt2Files[arch]) {
        "F-Droid build is enabled but path to AAPT2 file for $arch is not set."
      }

      val aapt2 = File(aapt2File)

      require(aapt2.exists() && aapt2.isFile) {
        "F-Droid AAPT2 file does not exist or is not a file: $aapt2"
      }

      logger.info("Copying $aapt2 to $file")
      aapt2.copyTo(file, overwrite = true)
      assertAapt2Arch(file, ELFUtils.ElfAbi.forName(arch)!!)
      return
    }

    // When not building for F-Droid, download aapt2 files from GitHub
    AAPT2_CHECKSUMS.forEach { (arch, checksum) ->
      val file = outputDirectory.file("${arch}/libaapt2.so").get().asFile
      file.parentFile.deleteRecursively()
      file.parentFile.mkdirs()

      val remoteUrl = AAPT2_DOWNLOAD_URL.format(DEFAULT_VERSION, arch)
      DownloadUtils.doDownload(file, remoteUrl, checksum, logger)
      assertAapt2Arch(file, ELFUtils.ElfAbi.forName(arch)!!)
    }
  }

  private fun assertAapt2Arch(aapt2: File, elfAbi: ELFUtils.ElfAbi) {
    val fileAbi = ELFUtils.getElfAbi(aapt2)
    check(fileAbi == elfAbi) {
      "Mismatched ABI for aapt2 binary. Required $elfAbi but found $fileAbi"
    }
  }
}