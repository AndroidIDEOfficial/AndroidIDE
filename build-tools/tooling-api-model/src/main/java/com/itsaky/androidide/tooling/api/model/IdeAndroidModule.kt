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
import com.itsaky.androidide.tooling.api.model.internal.DefaultModelSyncFile
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariantDependencies
import com.itsaky.androidide.tooling.api.model.internal.DefaultViewBindingOptions
import java.io.File

/**
 * Default implementation of [IdeAndroidModule].
 *
 * @author Akash Yadav
 */
class IdeAndroidModule(
    name: String?,
    path: String?,
    description: String?,
    projectDir: File?,
    buildDir: File?,
    buildScript: File?,
    parent: IdeGradleProject?,
    tasks: List<IdeGradleTask>,
    val projectType: ProjectType?,
    override var dynamicFeatures: Collection<String>?,
    override var flags: DefaultAndroidGradlePluginProjectFlags,
    override var javaCompileOptions: DefaultJavaCompileOptions,
    override var resourcePrefix: String?,
    override var variants: Collection<DefaultVariant>,
    override var viewBindingOptions: DefaultViewBindingOptions?,
    override val lintChecksJars: List<File>,
    override val modelSyncFiles: List<DefaultModelSyncFile>
) :
    IdeGradleProject(name, description, path, projectDir, buildDir, buildScript, parent, tasks),
    IdeModule,
    AndroidProject {

    var boothclasspaths: Collection<File> = emptyList()
    var mainSourceSet: DefaultSourceSetContainer? = null
    var variantDependencies: MutableMap<String, DefaultVariantDependencies> = mutableMapOf()

    fun copy(): IdeAndroidModule {
        return IdeAndroidModule(
            name,
            description,
            projectPath,
            projectDir,
            buildDir,
            buildScript,
            parent,
            tasks,
            projectType,
            dynamicFeatures,
            flags,
            javaCompileOptions,
            resourcePrefix,
            variants,
            viewBindingOptions,
            lintChecksJars,
            modelSyncFiles)
    }

    override fun getGeneratedJar(variant: String): File {
        return File(
            buildDir,
            "${AndroidProject.FD_INTERMEDIATES}/compile_library_classes_jar/$variant/classes.jar")
    }

    override fun toString(): String {
        return "IdeAndroidModule(dynamicFeatures=$dynamicFeatures, flags=$flags, javaCompileOptions=$javaCompileOptions, resourcePrefix=$resourcePrefix, variants=$variants, viewBindingOptions=$viewBindingOptions, lintChecksJars=$lintChecksJars, modelSyncFiles=$modelSyncFiles, variantDependencies=$variantDependencies, androidTestNamespace=$androidTestNamespace, namespace='$namespace', testFixturesNamespace=$testFixturesNamespace)"
    }

    // These properties are not supported on newer versions
    override val androidTestNamespace: String? = null
    override val namespace: String = ""
    override val testFixturesNamespace: String? = null
}
