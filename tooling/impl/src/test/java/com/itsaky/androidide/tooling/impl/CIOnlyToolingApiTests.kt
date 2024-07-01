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

package com.itsaky.androidide.tooling.impl

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.builder.model.IDESyncIssue
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.testing.common.CIOnlyTestRule
import com.itsaky.androidide.testing.tooling.ToolingApiTestLauncher
import com.itsaky.androidide.tooling.api.util.ToolingProps
import com.itsaky.androidide.utils.AndroidPluginVersion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class CIOnlyToolingApiTests {

  @JvmField
  @Rule
  val ciOnlyTestRule = CIOnlyTestRule()

  /**
   * Tests the functionality of the tooling API implementation against multiple versions of the
   * Android Gradle   Plugin. This test runs only in the CI environment.
   */
  @Test
  fun `test CI-only simple multi module project initialization with multiple AGP versions`() {
    // Test the minimum supported and the latest AGP version
    val versions = listOf(
      // AGP to Gradle
      BuildInfo.AGP_VERSION_MININUM to "7.3.3",
      BuildInfo.AGP_VERSION_LATEST to BuildInfo.AGP_VERSION_GRADLE_LATEST
    )

    val client = ToolingApiTestLauncher.MultiVersionTestClient()
    for ((agpVersion, gradleVersion) in versions) {
      client.agpVersion = agpVersion
      client.gradleVersion = gradleVersion
      ToolingApiTestLauncher.launchServer(client = client) {
        assertThat(result?.isSuccessful).isTrue()

        performBasicProjectAssertions(project = project, server = server)
      }
    }
  }

  @Test
  fun `test CI-only latest tested AGP version warning`() {
    val agpVersion = AndroidPluginVersion.parse(BuildInfo.AGP_VERSION_LATEST)

    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      agpVersion = agpVersion.toStringSimple(),
      gradleVersion = BuildInfo.AGP_VERSION_GRADLE_LATEST
    )

    ToolingApiTestLauncher.launchServer(
      client = client,

      // Version of Android Gradle Plugin that the tooling API should recognize
      // as the latest one
      sysProps = mapOf(
        ToolingProps.TESTING_LATEST_AGP_VERSION
            to ToolingApiTestLauncher.MultiVersionTestClient.DEFAULT_AGP_VERSION
      )
    ) {
      assertThat(result?.isSuccessful).isTrue()

      val syncIssues = project.getProjectSyncIssues().get()
      assertThat(syncIssues).isNotNull()
      assertThat(syncIssues.syncIssues).isNotEmpty()
      assertThat(
        syncIssues.syncIssues.find { it.type == IDESyncIssue.TYPE_AGP_VERSION_TOO_NEW }).isNotNull()
    }
  }

  @Test
  fun `test CI-only minimum AGP version failure`() {
    val agpVersion = "7.1.0"
    val client = ToolingApiTestLauncher.MultiVersionTestClient(
      agpVersion = agpVersion,
      gradleVersion = "7.2",
      outputValidator = { line ->
        line.contains("Android Gradle Plugin version $agpVersion is not supported by AndroidIDE.")
      }
    )

    ToolingApiTestLauncher.launchServer(client = client) {
      assertThat(result?.isSuccessful).isFalse()
      assertThat(client.isOutputValid).isTrue()
    }
  }
}