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

package com.itsaky.androidide.testing.tooling.models

import com.itsaky.androidide.testing.tooling.ToolingApiTestLauncher
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.utils.FileProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.pathString

/**
 * Parameters for launching the tooling API server.
 *
 * @author Akash Yadav
 */
data class ToolingApiTestLauncherParams(
  val projectDir: Path = FileProvider.testProjectRoot(),
  val client: ToolingApiTestLauncher.MultiVersionTestClient = ToolingApiTestLauncher.MultiVersionTestClient(),
  val initParams: InitializeProjectParams = InitializeProjectParams(
    projectDir.pathString,
    client.gradleDistParams
  ),
  val log: Logger = LoggerFactory.getLogger("BuildOutputLogger"),
  val sysProps: Map<String, String> = emptyMap(),
  val sysEnvs: Map<String, String> = emptyMap()
)