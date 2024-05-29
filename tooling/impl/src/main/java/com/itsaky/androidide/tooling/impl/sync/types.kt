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

package com.itsaky.androidide.tooling.impl.sync

import com.android.builder.model.v2.models.Versions
import org.gradle.tooling.BuildController
import org.gradle.tooling.CancellationToken
import org.gradle.tooling.ProjectConnection
import org.gradle.tooling.model.idea.IdeaModule
import org.gradle.tooling.model.idea.IdeaProject

/**
 * Parameters for the root project model builder.
 *
 * @property projectConnection The project connection
 * @property cancellationToken The cancellation token.
 */
data class RootProjectModelBuilderParams(
  val projectConnection: ProjectConnection,
  val cancellationToken: CancellationToken?
)

/**
 * Parameters for building model for an Android project.
 *
 * @property controller The build controller that will be used to fetch project models.
 * @property module The [IdeaModule] to fetch the models from.
 * @property versions The Android Gradle Plugin version information.
 * @property syncIssueReporter [ISyncIssueReporter] to report project synchronization issues.
 */
data class AndroidProjectModelBuilderParams(
  val controller: BuildController,
  val module: IdeaModule,
  val versions: Versions,
  val syncIssueReporter: ISyncIssueReporter
)

class ModuleProjectModelBuilderParams(
  val controller: BuildController,
  project: IdeaProject,
  module: IdeaModule,
  modulePaths: Map<String, String>,
  val syncIssueReporter: ISyncIssueReporter
) : JavaProjectModelBuilderParams(
  project, module, modulePaths)

open class JavaProjectModelBuilderParams(val project: IdeaProject, val module: IdeaModule,
  val modulePaths: Map<String, String>) {

  constructor(base: ModuleProjectModelBuilderParams) : this(base.project, base.module,
    base.modulePaths)
}