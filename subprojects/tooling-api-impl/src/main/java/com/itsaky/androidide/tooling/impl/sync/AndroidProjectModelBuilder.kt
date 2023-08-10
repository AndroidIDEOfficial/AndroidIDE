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

import com.android.builder.model.v2.models.AndroidProject
import com.android.builder.model.v2.models.BasicAndroidProject
import com.android.builder.model.v2.models.ModelBuilderParameter
import com.android.builder.model.v2.models.VariantDependencies
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.impl.internal.AndroidProjectImpl

/**
 * Builds model for Android application and library projects.
 *
 * @author Akash Yadav
 */
class AndroidProjectModelBuilder(androidVariant: String) :
  AbstractModelBuilder<BuildControllderAndIdeaModule, IAndroidProject>(androidVariant) {

  override fun build(param: BuildControllderAndIdeaModule): IAndroidProject {
    val (controller, module) = param
    
    val projectpath = module.gradleProject.path
    val basicModel = controller.getModelAndLog(module, BasicAndroidProject::class.java)
    val androidModel = controller.getModelAndLog(module, AndroidProject::class.java)

    val variantNames = basicModel.variants.map { it.name }
    log(
      "${variantNames.size} build variants found for project '$projectpath': $variantNames")

    val selectedVariant = androidVariant.ifBlank { variantNames.firstOrNull() }
    if (selectedVariant.isNullOrBlank()) {
      throw ModelBuilderException(
        "No variant found for project '$projectpath'. providedVariant=$androidVariant")
    }

    log("Selected build variant '$selectedVariant' for project '$projectpath'")

    val variantDependencies = controller.getModelAndLog(module, VariantDependencies::class.java,
      ModelBuilderParameter::class.java) {
      it.variantName = selectedVariant
      it.dontBuildRuntimeClasspath = false
    }

    return AndroidProjectImpl(
      module.gradleProject,
      selectedVariant,
      basicModel,
      androidModel,
      variantDependencies
    )
  }
}