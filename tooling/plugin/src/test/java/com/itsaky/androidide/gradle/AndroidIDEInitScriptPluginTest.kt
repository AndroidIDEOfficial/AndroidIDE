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

package com.itsaky.androidide.gradle

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.buildinfo.BuildInfo
import org.gradle.testkit.runner.BuildResult
import org.junit.jupiter.api.Test

/**
 * @author Akash Yadav
 */
class AndroidIDEInitScriptPluginTest {

  @Test
  fun `test plugins are applied and log sender dependency is added properly`() {
    val result = buildProject()
    assertBasics(result)
  }

  @Test
  fun `test behavior on minimum supported version`() {
    val result = buildProject(agpVersion = BuildInfo.AGP_VERSION_MININUM, gradleVersion = "7.5.1")
    assertBasics(result)
  }

  @Test
  fun `test behavior with apply plugin syntax`() {
    val result = buildProject(
      agpVersion = BuildInfo.AGP_VERSION_MININUM,
      gradleVersion = "7.5.1",
      useApplyPluginGroovySyntax = true
    )
    assertBasics(result)
  }

  private fun assertBasics(result: BuildResult) {
    // These plugins must be applied to the
    for ((project, plugins) in mapOf(
      ":app" to arrayOf(AndroidIDEGradlePlugin::class, LogSenderPlugin::class))) {
      for (plugin in plugins) {
        assertThat(result.output).contains(
          "Applying ${plugin.simpleName} to project '${project}'"
        )
      }
    }

    // LogSender should be applied to these
    for ((project, variants) in mapOf(":app" to arrayOf("demoDebug", "fullDebug"))) {
      for (variant in variants) {
        assertThat(result.output).contains(
          "Adding LogSender dependency (version '${
            depVersion(true)
          }') to variant '${variant}' of project '${project}'"
        )
      }
    }

    // LogSender should not be applied to these
    for ((project, variants) in mapOf(":app" to arrayOf("demoRelease", "fullRelease"))) {
      for (variant in variants) {
        assertThat(result.output).doesNotContain(
          "Adding LogSender dependency to variant '${variant}' of project '${project}'"
        )
      }
    }
  }
}