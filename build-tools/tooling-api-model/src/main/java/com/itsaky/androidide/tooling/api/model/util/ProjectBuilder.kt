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
package com.itsaky.androidide.tooling.api.model.util

import com.android.builder.model.v2.ide.ProjectType
import com.android.builder.model.v2.ide.ProjectType.APPLICATION
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeGradleTask
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.tooling.api.model.internal.DefaultJavaCompileOptions
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant
import com.itsaky.androidide.tooling.api.model.internal.DefaultViewBindingOptions
import java.io.File

/**
 * Builds instances of [IdeGradleProject].
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
    var parent: IdeGradleProject? = null
    var subprojects: List<IdeGradleProject> = mutableListOf()
    var tasks: List<IdeGradleTask> = mutableListOf()
    var bootClasspath: Collection<File> = mutableListOf()
    var buildFolder: File = File("<no_path>")
    var buildTypeSourceSets: Collection<DefaultSourceSetContainer> = mutableListOf()
    var dynamicFeatures: Collection<String>? = mutableListOf()
    var flags: DefaultAndroidGradlePluginProjectFlags = DefaultAndroidGradlePluginProjectFlags()
    var javaCompileOptions: DefaultJavaCompileOptions = DefaultJavaCompileOptions()
    var lintRuleJars: List<File> = mutableListOf()
    var mainSourceSet: DefaultSourceSetContainer = DefaultSourceSetContainer()
    var productFlavorSourceSets: Collection<DefaultSourceSetContainer> = mutableListOf()
    var projectType: ProjectType = APPLICATION
    var resourcePrefix: String? = ""
    var variants: Collection<DefaultVariant> = mutableListOf()
    var viewBindingOptions: DefaultViewBindingOptions? = null

    fun buildGradleProject(): IdeGradleProject {
        return IdeGradleProject(
            name, description, path, projectDir, buildDir, buildScript, parent, subprojects, tasks)
    }

    fun buildAndroidModule(): IdeAndroidModule =
        IdeAndroidModule(
            name,
            description,
            projectDir,
            buildDir,
            buildScript,
            parent,
            subprojects,
            tasks,
            path,
            bootClasspath,
            buildFolder,
            buildTypeSourceSets,
            dynamicFeatures,
            flags,
            javaCompileOptions,
            lintRuleJars,
            mainSourceSet,
            productFlavorSourceSets,
            projectType,
            resourcePrefix,
            variants,
            viewBindingOptions)
}
