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

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope.ALL
import com.android.build.api.variant.AndroidComponentsExtension
import com.itsaky.androidide.desugaring.DesugarParams.Companion.setFrom
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class DesugarGradlePlugin : Plugin<Project> {

  override fun apply(target: Project) {
    target.run {
      logger.trace("Creating desugaring extension")
      val extension = extensions.create<DesugarExtension>("desugaring")
      extension.enabled.convention(true)

      val androidComponents =
        extensions.findByType(AndroidComponentsExtension::class.java) ?: run {
          logger.warn("Could not find androidComponents extension")
          return
        }

      androidComponents.onVariants { variant ->
        logger.debug("Applying desugaring to ${variant.name}")

        variant.instrumentation.apply {
          transformClassesWith(
            DesugarClassVisitorFactory::class.java, ALL
          ) { params ->
            params.setFrom(extension)
          }

          setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
      }
    }
  }
}