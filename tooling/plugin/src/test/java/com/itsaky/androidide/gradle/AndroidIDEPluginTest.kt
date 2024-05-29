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
import com.itsaky.androidide.tooling.api.LogSenderConfig.PROPERTY_LOGSENDER_ENABLED
import org.junit.jupiter.api.Test

/**
 * @author Akash Yadav
 */
class AndroidIDEPluginTest {

  @Test
  fun `test logsender must be enabled by default`() {
    val result = buildProject()
    assertThat(result.output).doesNotContain("LogSender is disabled")
  }

  @Test
  fun `test logsender must be enabled if specified explicitly`() {
    val result = buildProject(configureArgs = {
      it.add("-P$PROPERTY_LOGSENDER_ENABLED=true")
    })
    assertThat(result.output).doesNotContain("LogSender is disabled")
  }

  @Test
  fun `test logsender must be disabled if specified explicitly`() {
    val result = buildProject(configureArgs = {
      it.add("-P$PROPERTY_LOGSENDER_ENABLED=false")
    })
    assertThat(result.output).contains("LogSender is disabled")
  }

  @Test
  fun `test logsender must be added as non-changing dependency`() {
    val result = buildProject(configureArgs = {
      it.add("--debug")
    })
    assertThat(result.output).contains("Marking logsender dependency as not-changing")
  }
}