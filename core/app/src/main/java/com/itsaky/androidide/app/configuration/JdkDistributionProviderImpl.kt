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

package com.itsaky.androidide.app.configuration

import com.google.auto.service.AutoService
import com.itsaky.androidide.models.JdkDistribution
import com.itsaky.androidide.preferences.internal.BuildPreferences
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.JdkUtils
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author Akash Yadav
 */
@AutoService(IJdkDistributionProvider::class)
class JdkDistributionProviderImpl : IJdkDistributionProvider {

  companion object {

    private val log = LoggerFactory.getLogger(JdkDistributionProviderImpl::class.java)
  }

  private var _installedDistributions: List<JdkDistribution>? = null

  override val installedDistributions: List<JdkDistribution>
    get() = _installedDistributions ?: emptyList()

  override fun loadDistributions() {
    _installedDistributions = doLoadDistributions()
  }

  private fun doLoadDistributions(): List<JdkDistribution> {
    return JdkUtils.findJavaInstallations().also { distributions ->

      // set the default value for the 'javaHome' preference
      if (BuildPreferences.javaHome.isBlank() && distributions.isNotEmpty()) {
        var defaultDist = distributions.find {
          it.javaVersion.startsWith(IJdkDistributionProvider.DEFAULT_JAVA_VERSION)
        }

        if (defaultDist == null) {
          // if JDK 17 is not installed, use the first available installation
          defaultDist = distributions[0]
        }

        BuildPreferences.javaHome = defaultDist.javaHome
      }

      val home = File(BuildPreferences.javaHome)
      val java = File(home, "bin/java")

      // the previously selected JDK distribution does not exist
      // check if we have other distributions installed
      if (!home.exists() || !java.exists() || !java.isFile) {
        if (distributions.isNotEmpty()) {
          log.warn(
            "Previously selected java.home does not exists! Falling back to ${distributions[0]}...")
          BuildPreferences.javaHome = distributions[0].javaHome
        }
      }

      if (!java.canExecute()) {
        java.setExecutable(true)
      }

      log.debug("Setting Environment.JAVA_HOME to {}", BuildPreferences.javaHome)

      Environment.JAVA_HOME = File(BuildPreferences.javaHome)
      Environment.JAVA = Environment.JAVA_HOME.resolve("bin/java")
    }
  }
}