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
    private var name: String = ""
    private var description: String = ""
    private var path: String = ""
    private var projectDir: File = File("<no_path>")
    private var buildDir: File = File("<no_path>")
    private var buildScript: File = File("<no_path>")
    private var parent: IGradleProject? = null
    private var subprojects: List<IGradleProject> = mutableListOf()
    private var tasks: List<IGradleTask> = mutableListOf()
    private var androidTestNamespace: String = ""
    private var bootClasspath: Collection<File> = mutableListOf()
    private var buildFolder: File = File("<no_path>")
    private var buildName: String = ""
    private var buildTypeSourceSets: Collection<SourceSetContainer> = mutableListOf()
    private var dynamicFeatures: Collection<String>? = mutableListOf()
    private var flags: AndroidGradlePluginProjectFlags = NoOpAndroidGradlePluginProjectFlags()
    private var javaCompileOptions: JavaCompileOptions = DefaultJavaCompileOptions()
    private var lintRuleJars: List<File> = mutableListOf()
    private var mainSourceSet: SourceSetContainer = DefaultSourceSetContainer()
    private var namespace: String = ""
    private var productFlavorSourceSets: Collection<SourceSetContainer> = mutableListOf()
    private var projectType: ProjectType = APPLICATION
    private var resourcePrefix: String = ""
    private var testFixturesNamespace: String = ""
    private var variants: Collection<Variant> = mutableListOf()
    private var viewBindingOptions: ViewBindingOptions? = null

    fun buildGradleProject(): IGradleProject {
        return DefaultGradleProject(
            name, description, path, projectDir, buildDir, buildScript, parent, subprojects, tasks)
    }

    fun buildAndroidModule(): IAndroidModule =
        DefaultAndroidModule(
            name,
            description,
            path,
            projectDir,
            buildDir,
            buildScript,
            parent,
            subprojects,
            tasks,
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
