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
import org.gradle.tooling.model.idea.IdeaModule
import org.gradle.tooling.model.idea.IdeaProject

typealias AndroidProjectModelBuilderParams = Triple<BuildController, IdeaModule, Versions>

class ModuleProjectModelBuilderParams(val controller: BuildController, project: IdeaProject,
  module: IdeaModule, modulePaths: Map<String, String>) : JavaProjectModelBuilderParams(
  project, module, modulePaths)

open class JavaProjectModelBuilderParams(val project: IdeaProject, val module: IdeaModule,
  val modulePaths: Map<String, String>) {

  constructor(base: ModuleProjectModelBuilderParams) : this(base.project, base.module,
    base.modulePaths)
}