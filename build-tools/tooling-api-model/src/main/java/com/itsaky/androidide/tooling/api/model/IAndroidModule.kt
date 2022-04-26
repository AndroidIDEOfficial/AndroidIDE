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

import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags
import com.android.builder.model.v2.ide.JavaCompileOptions
import com.android.builder.model.v2.ide.ProjectType
import com.android.builder.model.v2.ide.SourceSetContainer
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.ide.ViewBindingOptions
import com.android.builder.model.v2.models.AndroidProject
import java.io.File

/**
 * Default implementation of [IAndroidModule].
 *
 * @author Akash Yadav
 */
class IAndroidModule(
    name: String?,
    description: String?,
    projectDir: File?,
    buildDir: File?,
    buildScript: File?,
    parent: IGradleProject?,
    subprojects: List<IGradleProject>,
    tasks: List<IGradleTask>,
    override var path: String,
    override var androidTestNamespace: String?,
    override var bootClasspath: Collection<File>,
    override var buildFolder: File,
    override var buildName: String,
    override var buildTypeSourceSets: Collection<SourceSetContainer>,
    override var dynamicFeatures: Collection<String>?,
    override var flags: AndroidGradlePluginProjectFlags,
    override var javaCompileOptions: JavaCompileOptions,
    override var lintRuleJars: List<File>,
    override var mainSourceSet: SourceSetContainer,
    override var namespace: String,
    override var productFlavorSourceSets: Collection<SourceSetContainer>,
    override var projectType: ProjectType,
    override var resourcePrefix: String?,
    override var testFixturesNamespace: String?,
    override var variants: Collection<Variant>,
    override var viewBindingOptions: ViewBindingOptions?
) :
    IGradleProject(
        name, description, path, projectDir, buildDir, buildScript, parent, subprojects, tasks),
    AndroidProject {
    
    fun copy(): IAndroidModule {
        return IAndroidModule(
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
    }
}
