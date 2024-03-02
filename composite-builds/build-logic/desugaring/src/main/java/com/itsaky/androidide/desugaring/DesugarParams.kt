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

import com.android.build.api.instrumentation.InstrumentationParameters
import com.itsaky.androidide.desugaring.dsl.ReplaceMethodInsn
import com.itsaky.androidide.desugaring.dsl.ReplaceMethodInsnKey
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input

/**
 * Parameters for [DesugarClassVisitorFactory].
 *
 * @author Akash Yadav
 */
interface DesugarParams : InstrumentationParameters {

  /**
   * Whether the desugaring is enabled.
   */
  @get:Input
  val enabled: Property<Boolean>

  /**
   * The replacement instructions.
   */
  @get:Input
  val replacements: MapProperty<ReplaceMethodInsnKey, ReplaceMethodInsn>

  @get:Input
  val includedPackages: SetProperty<String>

  companion object {

    /**
     * Sets [DesugarParams] properties from [DesugarExtension].
     */
    fun DesugarParams.setFrom(extension: DesugarExtension) {
      replacements.convention(emptyMap())
      includedPackages.convention(emptySet())

      enabled.set(extension.enabled)
      replacements.set(extension.replacements.instructions)
      includedPackages.set(extension.replacements.includePackages)
    }
  }
}