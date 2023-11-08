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

import AUTH_PASS
import AUTH_USER
import KEY_BIN
import KEY_URL
import org.gradle.api.Project
import signingKey
import java.util.Base64

/**
 * Helper class for downloading and setting up the signing key.
 *
 * @author Akash Yadav
 */
object SigningKeyUtils {

  @JvmStatic
  fun Project.downloadSigningKey() {
    val signingKey = signingKey.get().asFile
    if (signingKey.exists()) {
      logger.info("Skipping download as ${signingKey.name} file already exists.")
      return
    }

    signingKey.parentFile.mkdirs()

    getEnvOrProp(key = KEY_BIN, warn = false)?.also { bin ->
      val contents = Base64.getDecoder().decode(bin)
      signingKey.writeBytes(contents)
      return
    }

    // URL to download the signing key
    val url = getEnvOrProp(KEY_URL) ?: return

    // Username and password required to download the keystore
    val user = getEnvOrProp(AUTH_USER) ?: return
    val pass = getEnvOrProp(AUTH_PASS) ?: return

    logger.info("Downloading signing key...")
    val result = exec {
      workingDir(rootProject.projectDir)
      commandLine("bash", "./.tools/download_key.sh", signingKey.absolutePath, url, user, pass)
    }

    result.assertNormalExitValue()
  }

  internal fun Project.getEnvOrProp(key: String, warn: Boolean = true): String? {
    var value: String? = System.getenv(key)
    if (value.isNullOrBlank()) {
      value = project.properties[key] as? String?
    }

    if (value.isNullOrBlank()) {
      if (warn) {
        logger.warn("$key is not set. Debug key will be used to sign the APK")
      }
      return null
    }
    return value
  }

}