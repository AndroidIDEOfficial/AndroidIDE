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
import com.itsaky.androidide.utils.FileProvider
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

/**
 * @author Akash Yadav
 */
class AndroidIDEInitScriptPluginTest {

  @Test
  fun `test init script plugin is applied`() {
    val projectRoot = openProject()
    val initScript = FileProvider.testHomeDir().resolve(".androidide/init/androidide.init.gradle")

    val runner = GradleRunner.create()
      .withProjectDir(projectRoot.toFile())
      .withPluginClasspath()
      .withArguments(
        ":app:tasks", // run any task, as long as it applies the plugins
        "--init-script", initScript.pathString,
        "-Pandroidide.plugins.internal.testing.enableMavenLocal=true", // plugins should be published to maven local first
        "--stacktrace"
      )

    writeInitScript(initScript.toFile(), runner.pluginClasspath)

    val result = runner.build()

    assertThat(result.output).contains(
      "[AndroidIDE] Applying ${AndroidIDEInitScriptPlugin::class.java.simpleName}"
    )
  }
}