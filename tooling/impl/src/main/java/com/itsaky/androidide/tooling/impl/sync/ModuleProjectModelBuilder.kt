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

import com.itsaky.androidide.tooling.api.IModuleProject
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams

/**
 * Builds models for module projects (either Android app/library or Java library projects).
 *
 * @author Akash Yadav
 */
class ModuleProjectModelBuilder(initializationParams: InitializeProjectParams) :
  AbstractModelBuilder<ModuleProjectModelBuilderParams, IModuleProject>(initializationParams) {

  override fun build(param: ModuleProjectModelBuilderParams): IModuleProject {
    val versions = getAndroidVersions(param.module, param.controller)
    return if (versions != null) {
      checkAgpVersion(versions, param.syncIssueReporter)
      AndroidProjectModelBuilder(initializationParams)
        .build(AndroidProjectModelBuilderParams(
          param.controller,
          param.module,
          versions,
          param.syncIssueReporter
        ))
    } else {
      JavaProjectModelBuilder(initializationParams).build(
        JavaProjectModelBuilderParams(param))
    }
  }
}