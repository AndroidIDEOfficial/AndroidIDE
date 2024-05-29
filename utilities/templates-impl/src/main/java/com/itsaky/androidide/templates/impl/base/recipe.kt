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

package com.itsaky.androidide.templates.impl.base

import com.itsaky.androidide.templates.ModuleTemplateRecipeResult
import com.itsaky.androidide.templates.ProjectTemplateRecipeResult
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.TemplateRecipe
import com.itsaky.androidide.templates.TemplateRecipeResult
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.ExecutorDataTemplateBuilder
import com.itsaky.androidide.templates.base.ProjectTemplateBuilder

internal inline fun <R : TemplateRecipeResult> ExecutorDataTemplateBuilder<*, *>.createRecipe(
  crossinline action: RecipeExecutor.() -> R
): TemplateRecipe<R> {
  return TemplateRecipe {
    return@TemplateRecipe executor.run(action)
  }
}

internal inline fun AndroidModuleTemplateBuilder.createRecipe(
  crossinline action: RecipeExecutor.() -> Unit
): TemplateRecipe<ModuleTemplateRecipeResult> {
  return TemplateRecipe {
    executor.run(action)
    recipeResult()
  }
}

internal inline fun ProjectTemplateBuilder.createRecipe(
  crossinline action: RecipeExecutor.() -> Unit
): TemplateRecipe<ProjectTemplateRecipeResult> {
  return TemplateRecipe {
    executor.run(action)
    recipeResult()
  }
}