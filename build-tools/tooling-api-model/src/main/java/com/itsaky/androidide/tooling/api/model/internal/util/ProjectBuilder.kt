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

package com.itsaky.androidide.tooling.api.model.internal.util

import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags
import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags.BooleanFlag
import com.android.builder.model.v2.ide.JavaCompileOptions
import com.android.builder.model.v2.ide.ProjectType
import com.android.builder.model.v2.ide.ProjectType.APPLICATION
import com.android.builder.model.v2.ide.SourceProvider
import com.android.builder.model.v2.ide.SourceSetContainer
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.ide.ViewBindingOptions
import com.itsaky.androidide.tooling.api.model.IAndroidModule
import com.itsaky.androidide.tooling.api.model.IGradleProject
import com.itsaky.androidide.tooling.api.model.IGradleTask
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidModule
import com.itsaky.androidide.tooling.api.model.internal.DefaultGradleProject
import java.io.File

/**
 * Builds instances of [IAndroidProject]
 *
 * @author Akash Yadav
 */
class ProjectBuilder {
    var name: String? = null
    var description: String? = null
    var path: String = ":"
    var projectDir: File? = null
    var buildDir: File? = null
    var buildScript: File? = null
    var parent: IGradleProject? = null
    var subprojects: List<IGradleProject> = mutableListOf()
    var tasks: List<IGradleTask> = mutableListOf()
    var androidTestNamespace: String = ""
    var bootClasspath: Collection<File> = mutableListOf()
    var buildFolder: File = File("<no_path>")
    var buildName: String = ""
    var buildTypeSourceSets: Collection<SourceSetContainer> = mutableListOf()
    var dynamicFeatures: Collection<String>? = mutableListOf()
    var flags: AndroidGradlePluginProjectFlags = NoOpAndroidGradlePluginProjectFlags()
    var javaCompileOptions: JavaCompileOptions = DefaultJavaCompileOptions()
    var lintRuleJars: List<File> = mutableListOf()
    var mainSourceSet: SourceSetContainer = DefaultSourceSetContainer()
    var namespace: String = ""
    var productFlavorSourceSets: Collection<SourceSetContainer> = mutableListOf()
    var projectType: ProjectType = APPLICATION
    var resourcePrefix: String = ""
    var testFixturesNamespace: String = ""
    var variants: Collection<Variant> = mutableListOf()
    var viewBindingOptions: ViewBindingOptions? = null

    fun buildGradleProject(): IGradleProject {
        return DefaultGradleProject(
            name, description, path, projectDir, buildDir, buildScript, parent, subprojects, tasks)
    }

    fun buildAndroidModule(): IAndroidModule =
        DefaultAndroidModule(
            name,
            description,
            projectDir,
            buildDir,
            buildScript,
            parent,
            subprojects,
            tasks,
            path,
            androidTestNamespace,
            bootClasspath,
            buildFolder,
            buildName,
            buildTypeSourceSets,
            dynamicFeatures,
            flags,
            javaCompileOptions,
            lintRuleJars,
            mainSourceSet,
            namespace,
            productFlavorSourceSets,
            projectType,
            resourcePrefix,
            testFixturesNamespace,
            variants,
            viewBindingOptions)

    class NoOpAndroidGradlePluginProjectFlags : AndroidGradlePluginProjectFlags {
        override val booleanFlagMap: Map<BooleanFlag, Boolean> = mutableMapOf()
    }

    class DefaultJavaCompileOptions : JavaCompileOptions {
        override val encoding: String = "UTF-8"
        override val isCoreLibraryDesugaringEnabled: Boolean = false
        override val sourceCompatibility: String = "11"
        override val targetCompatibility: String = "11"
    }

    class DefaultSourceSetContainer : SourceSetContainer {
        override val androidTestSourceProvider: SourceProvider? = null
        override val sourceProvider: SourceProvider = DefaultSourceProvider()
        override val testFixturesSourceProvider: SourceProvider? = null
        override val unitTestSourceProvider: SourceProvider? = null

        class DefaultSourceProvider : SourceProvider {
            override val aidlDirectories: Collection<File>? = null
            override val assetsDirectories: Collection<File>? = null
            override val javaDirectories = mutableListOf<File>()
            override val jniLibsDirectories: Collection<File> = mutableListOf()
            override val kotlinDirectories: Collection<File> = mutableListOf()
            override val manifestFile = File("<no_path>")
            override val mlModelsDirectories: Collection<File>? = null
            override val name: String = ""
            override val renderscriptDirectories: Collection<File>? = null
            override val resDirectories: Collection<File>? = null
            override val resourcesDirectories: Collection<File> = mutableListOf()
            override val shadersDirectories: Collection<File>? = null
        }
    }
}
