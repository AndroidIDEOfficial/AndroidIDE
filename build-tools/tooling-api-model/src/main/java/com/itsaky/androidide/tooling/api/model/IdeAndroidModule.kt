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
package com.itsaky.androidide.tooling.api.model

import com.android.builder.model.v2.ide.ProjectType
import com.android.builder.model.v2.models.AndroidProject
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.tooling.api.model.internal.DefaultJavaCompileOptions
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant
import com.itsaky.androidide.tooling.api.model.internal.DefaultViewBindingOptions
import java.io.File

/**
 * Default implementation of [IdeAndroidModule].
 *
 * @author Akash Yadav
 */
class IdeAndroidModule(
    name: String?,
    description: String?,
    projectDir: File?,
    buildDir: File?,
    buildScript: File?,
    parent: IdeGradleProject?,
    subprojects: List<IdeGradleProject>,
    tasks: List<IdeGradleTask>,
    override var path: String,
    override var bootClasspath: Collection<File>,
    override var buildFolder: File,
    override var buildTypeSourceSets: Collection<DefaultSourceSetContainer>,
    override var dynamicFeatures: Collection<String>?,
    override var flags: DefaultAndroidGradlePluginProjectFlags,
    override var javaCompileOptions: DefaultJavaCompileOptions,
    override var lintRuleJars: List<File>,
    override var mainSourceSet: DefaultSourceSetContainer,
    override var productFlavorSourceSets: Collection<DefaultSourceSetContainer>,
    override var projectType: ProjectType,
    override var resourcePrefix: String?,
    override var variants: Collection<DefaultVariant>,
    override var viewBindingOptions: DefaultViewBindingOptions?
) :
    IdeGradleProject(
        name, description, path, projectDir, buildDir, buildScript, parent, subprojects, tasks),
    AndroidProject {

    fun copy(): IdeAndroidModule {
        return IdeAndroidModule(
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

    override fun toString(): String {
        return "IdeAndroidModule(path='$path', bootClasspath=$bootClasspath, buildFolder=$buildFolder, buildTypeSourceSets=$buildTypeSourceSets, dynamicFeatures=$dynamicFeatures, flags=$flags, javaCompileOptions=$javaCompileOptions, lintRuleJars=$lintRuleJars, mainSourceSet=$mainSourceSet, productFlavorSourceSets=$productFlavorSourceSets, projectType=$projectType, resourcePrefix=$resourcePrefix, variants=$variants, viewBindingOptions=$viewBindingOptions, androidTestNamespace=$androidTestNamespace, buildName='$buildName', namespace='$namespace', testFixturesNamespace=$testFixturesNamespace)"
    }

    // These properties are not supported on newer versions
    override val androidTestNamespace: String? = null
    override val buildName: String = ""
    override val namespace: String = ""
    override val testFixturesNamespace: String? = ""
}
