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

import com.itsaky.androidide.templates.EMPTY_RECIPE
import com.itsaky.androidide.templates.RecipeExecutor
import com.itsaky.androidide.templates.Template
import com.itsaky.androidide.templates.TemplateBuilder
import com.itsaky.androidide.templates.TemplateData
import com.itsaky.androidide.templates.TemplateRecipe
import com.itsaky.androidide.templates.TemplateRecipeConfigurator
import com.itsaky.androidide.templates.TemplateRecipeFinalizer
import com.itsaky.androidide.templates.TemplateRecipeResult

sealed class PrePostRecipeTemplateBuilder<R : TemplateRecipeResult> : TemplateBuilder<R>() {

  internal var preRecipe: TemplateRecipeConfigurator = {}
  internal var postRecipe: TemplateRecipeFinalizer = {}

  override var recipe: TemplateRecipe<R>? = null
    get() = TemplateRecipe {
      preRecipe(it)
      val result = (field ?: EMPTY_RECIPE).execute(it)
      postRecipe(it)
      result
    }
}

/**
 * @property executor The [RecipeExecutor] instance.
 * @property data The project template data.
 */
sealed class ExecutorDataTemplateBuilder<R : TemplateRecipeResult, D : TemplateData> :
  PrePostRecipeTemplateBuilder<R>() {

  internal var _executor: RecipeExecutor? = null
  internal var _data: D? = null

  val executor: RecipeExecutor
    get() = checkNotNull(_executor)

  val data: D
    get() = checkNotNull(_data)
}