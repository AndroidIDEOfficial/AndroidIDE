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

package com.itsaky.androidide.templates.base

import com.itsaky.androidide.templates.base.models.Dependency

/**
 * Configures the template to use AndroidX and Material Design Components dependencies.
 */
fun AndroidModuleTemplateBuilder.baseAndroidXDependencies() {
  addDependency(Dependency.AndroidX.AppCompat)
  addDependency(Dependency.AndroidX.ConstraintLayout)
  addDependency(Dependency.Google.Material)
}

fun AndroidModuleTemplateBuilder.composeDependencies() {
  addDependency(Dependency.AndroidX.Compose.Core_Ktx)
  addDependency(Dependency.AndroidX.Compose.LifeCycle_Runtime_Ktx)
  addDependency(Dependency.AndroidX.Compose.Activity)

  addDependency(dependency = Dependency.AndroidX.Compose.BOM, isPlatform = true)
  addDependency(Dependency.AndroidX.Compose.UI)
  addDependency(Dependency.AndroidX.Compose.UI_Graphics)
  addDependency(Dependency.AndroidX.Compose.UI_Tooling_Preview)
  addDependency(Dependency.AndroidX.Compose.Material3)
  addDependency(Dependency.AndroidX.Compose.UI_Tooling)
  addDependency(Dependency.AndroidX.Compose.UI_Test_Manifest)
}