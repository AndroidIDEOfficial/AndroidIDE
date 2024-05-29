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

import com.android.build.gradle.BaseExtension
import com.itsaky.androidide.build.config.KEY_ALIAS
import com.itsaky.androidide.build.config.KEY_PASS
import com.itsaky.androidide.build.config.KEY_STORE_PASS
import com.itsaky.androidide.build.config.isFDroidBuild
import com.itsaky.androidide.build.config.signingKey
import com.itsaky.androidide.plugins.util.SigningKeyUtils.downloadSigningKey
import com.itsaky.androidide.plugins.util.SigningKeyUtils.getEnvOrProp
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Configures the signing keys to application modules.
 *
 * @author Akash Yadav
 */
class SigningConfigPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.run {

      if (isFDroidBuild) {
        logger.warn("!!! Do not apply ${javaClass.simpleName} when building or F-Droid.")
        return
      }

      downloadSigningKey()

      val signingKey = signingKey.get().asFile
      if (!signingKey.exists()) {
        logger.warn("Signing key not found. Debug signing will be used.")
        return
      }

      // Create and apply the signing config
      extensions.getByType(BaseExtension::class.java).let { extension ->
        // Keystore credentials
        val alias = getEnvOrProp(KEY_ALIAS)
        val storePass = getEnvOrProp(KEY_STORE_PASS)
        val keyPass = getEnvOrProp(KEY_PASS)

        if (alias != null && storePass != null && keyPass != null && signingKey.exists()) {
          val config = extension.signingConfigs.create("common") {
            storeFile = signingKey
            keyAlias = alias
            storePassword = storePass
            keyPassword = keyPass
          }

          extension.buildTypes.forEach { buildType ->
            buildType.signingConfig = config
          }
        } else {
          logger.warn(
            "Signing info not configured. keystoreFile=$signingKey[exists=${signingKey.exists()}]"
          )
          null
        }
      }
    }
  }
}