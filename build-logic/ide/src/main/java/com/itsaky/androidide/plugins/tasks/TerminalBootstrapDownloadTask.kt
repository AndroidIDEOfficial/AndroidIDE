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

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.security.DigestInputStream
import java.security.MessageDigest

/**
 * @author Akash Yadav
 */
abstract class TerminalBootstrapDownloadTask : DefaultTask() {

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
      doDownload(arch, sha256, version)
    }
  }

  private fun doDownload(arch: String, expectedChecksum: String, version: String) {
    val digest = MessageDigest.getInstance("SHA-256")
    val localPath = "src/main/cpp/bootstrap-${arch}.zip"
    val file = project.file(localPath)

    if (file.exists()) {
      digest.update(file.readBytes())
      var checksum = BigInteger(1, digest.digest()).toString(16)
      while (checksum.length < 64) {
        checksum = "0$checksum"
      }

      if (checksum == expectedChecksum) {
        logger.info("$localPath is already downloaded and valid. Skipping download.")
        return
      } else {
        logger.info(
          "Deleting old bootstrap package with invalid checksum: $checksum expected: $expectedChecksum file: $localPath")
        file.delete()
      }
    }

    val remoteUrl = PACKAGES_DOWNLOAD_URL.format(version, arch)
    logger.info("Downloading ${remoteUrl}...")

    file.parentFile.mkdirs()

    val connection = URL(remoteUrl).openConnection() as HttpURLConnection
    connection.instanceFollowRedirects = true

    file.outputStream().use { out ->
      DigestInputStream(connection.inputStream, digest).transferTo(out.buffered())
    }
    connection.inputStream.buffered().transferTo(file.outputStream())

    var checksum = BigInteger(1, digest.digest()).toString(16)
    while (checksum.length < 64) {
      checksum = "0$checksum"
    }

    if (checksum != expectedChecksum) {
      file.delete()
      throw GradleException(
        "Wrong checksum for $remoteUrl: expected: $expectedChecksum, actual: $checksum")
    }
  }
}