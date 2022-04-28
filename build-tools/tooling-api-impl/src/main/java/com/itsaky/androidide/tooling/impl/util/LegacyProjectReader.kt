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
package com.itsaky.androidide.tooling.impl.util

import com.android.builder.model.AndroidArtifact
import com.android.builder.model.AndroidLibrary
import com.android.builder.model.AndroidProject
import com.android.builder.model.Dependencies
import com.android.builder.model.JavaLibrary
import com.android.builder.model.ModelBuilderParameter
import com.android.builder.model.Variant
import com.itsaky.androidide.tooling.impl.progress.LoggingProgressListener
import java.io.File
import org.gradle.tooling.ConfigurableLauncher
import org.gradle.tooling.ProjectConnection

/** @author Akash Yadav */
object LegacyProjectReader {

    fun findDependencyJars(connection: ProjectConnection, variantName: String): Set<File> {
        val builder =
            connection.action { controller ->
                controller.findModel(Variant::class.java, ModelBuilderParameter::class.java) {
                    it.variantName = variantName
                    it.shouldBuildVariant = true
                    it.shouldGenerateSources = false
                }
            }
        builder.addProgressListener(LoggingProgressListener())
        addProperty(
            builder,
            AndroidProject.PROPERTY_BUILD_MODEL_ONLY_VERSIONED,
            AndroidProject.MODEL_LEVEL_3_VARIANT_OUTPUT_POST_BUILD)
        addProperty(builder, AndroidProject.PROPERTY_BUILD_MODEL_FEATURE_FULL_DEPENDENCIES, true)

        val variant = builder.run()
        val dep = mutableSetOf<File>()
        addJarsFromVariant(variant, dep)

        return dep
    }

    private fun addJarsFromVariant(variant: Variant, jars: MutableSet<File>) {
        val mainArtifact: AndroidArtifact = variant.mainArtifact
        addJarsFromDependencies(mainArtifact.dependencies, jars)
        for (artifact in variant.extraAndroidArtifacts) {
            addJarsFromDependencies(artifact.dependencies, jars)
        }
    }

    private fun addJarsFromDependencies(dependencies: Dependencies, jars: MutableSet<File>) {
        for (lib in dependencies.libraries) {
            addJarsFromAndroidLibrary(lib, jars)
        }
        for (javaLib in dependencies.javaLibraries) {
            addJarsFromJavaLibrary(javaLib, jars)
        }
    }

    private fun addJarsFromAndroidLibrary(lib: AndroidLibrary, jars: MutableSet<File>) {
        jars.add(lib.jarFile)
        for (javaLib in lib.javaDependencies) {
            addJarsFromJavaLibrary(javaLib, jars)
        }
        for (androidLib in lib.libraryDependencies) {
            addJarsFromAndroidLibrary(androidLib, jars)
        }
        for (jar in lib.localJars) {
            jars.add(jar)
        }
    }

    private fun addJarsFromJavaLibrary(lib: JavaLibrary, jars: MutableSet<File>) {
        jars.add(lib.jarFile)
        for (javaLib in lib.dependencies) {
            addJarsFromJavaLibrary(javaLib, jars)
        }
    }

    private fun addProperty(
        launcher: ConfigurableLauncher<*>,
        property: String,
        value: Any,
    ) {
        launcher.addArguments(String.format("-P%s=%s", property, value))
    }
}
