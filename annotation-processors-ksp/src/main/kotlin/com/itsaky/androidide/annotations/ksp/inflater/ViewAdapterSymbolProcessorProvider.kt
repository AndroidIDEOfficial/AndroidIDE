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

package com.itsaky.androidide.annotations.ksp.inflater

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * [SymbolProcessorProvider] implementation for
 * [ViewAdapter][com.itsaky.androidide.annotations.inflater.ViewAdapter] annotation.
 *
 * @author Akash Yadav
 */
@AutoService(SymbolProcessorProvider::class)
class ViewAdapterSymbolProcessorProvider : SymbolProcessorProvider {

  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return ViewAdapterSymbolProcessor(environment.codeGenerator, environment.logger)
  }
}
