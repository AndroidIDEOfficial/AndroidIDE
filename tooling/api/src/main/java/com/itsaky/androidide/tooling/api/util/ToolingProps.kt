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

package com.itsaky.androidide.tooling.api.util

import com.itsaky.androidide.utils.AndroidPluginVersion

/**
 * System properties for configuring the toolign API.
 *
 * @author Akash Yadav
 */
object ToolingProps {

  val TESTING_IS_TEST_ENV = propName("testing", "isTestEnv")
  val TESTING_LATEST_AGP_VERSION = propName("testing", "latestAgpVersion")

  val isTestEnv: Boolean
    get() = System.getProperty(TESTING_IS_TEST_ENV).toBoolean()

  val latestTestedAgpVersion: AndroidPluginVersion
    get() {
      if (!isTestEnv) {
        return AndroidPluginVersion.LATEST_TESTED
      }
      return System.getProperty(TESTING_LATEST_AGP_VERSION)?.let { AndroidPluginVersion.parse(it) }
        ?: AndroidPluginVersion.LATEST_TESTED
    }

  fun propName(cat: String, name: String) = "ide.tooling.$cat.$name"
}