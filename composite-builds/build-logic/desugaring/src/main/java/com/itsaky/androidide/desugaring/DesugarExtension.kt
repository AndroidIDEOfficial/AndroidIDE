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

package com.itsaky.androidide.desugaring

import com.itsaky.androidide.desugaring.dsl.DesugarReplacementsContainer
import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested

/**
 * Extension for configuring the [DesugarGradlePlugin] and the desugar process.
 *
 * @author Akash Yadav
 */
interface DesugarExtension {

  /**
   * Whether the desugaring is enabled. Defaults to `true`.
   */
  val enabled: Property<Boolean>

  /**
   * The method replacement instructions container.
   */
  @get:Nested
  val replacements: DesugarReplacementsContainer

  /**
   * Define the replacement methods for desugaring.
   */
  fun replacements(action: Action<DesugarReplacementsContainer>) {
    action.execute(replacements)
  }
}