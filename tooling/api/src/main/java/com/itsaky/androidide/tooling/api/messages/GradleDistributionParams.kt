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

package com.itsaky.androidide.tooling.api.messages

import java.io.Serializable

/**
 * Parameters for specifying the Gradle distribution which should be used during project initialization.
 *
 * @property type The type of Gradle distribution.
 * @property value The value for the distribution type.
 * @author Akash Yadav
 */
data class GradleDistributionParams(val type: GradleDistributionType, val value: String) :
  Serializable {

  companion object {

    /**
     * [GradleDistributionParams] with type [GRADLE_WRAPPER][GradleDistributionType.GRADLE_WRAPPER].
     */
    @JvmStatic
    val WRAPPER = GradleDistributionParams(GradleDistributionType.GRADLE_WRAPPER, "")

    /**
     * Creates [GradleDistributionParams] for the given [distribution path][path].
     */
    @JvmStatic
    fun forInstallationDir(path: String): GradleDistributionParams {
      return GradleDistributionParams(GradleDistributionType.GRADLE_INSTALLATION, path)
    }

    /**
     * Creates [GradleDistributionParams] for the given [Gradle version][version].
     */
    @JvmStatic
    fun forVersion(version: String): GradleDistributionParams {
      return GradleDistributionParams(GradleDistributionType.GRADLE_VERSION, version)
    }
  }
}

/**
 * Type of Gradle distributions for project initialization.
 */
enum class GradleDistributionType {

  /**
   * Initialize the project using the distribution specified in `gradle-wrapper.properties`.
   */
  GRADLE_WRAPPER,

  /**
   * Initialize the project using a specific Gradle version.
   */
  GRADLE_VERSION,

  /**
   * Initialize the project using a specific Gradle distribution path.
   */
  GRADLE_INSTALLATION
}