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
import com.itsaky.androidide.builder.model.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.builder.model.DefaultJavaCompileOptions
import com.itsaky.androidide.builder.model.DefaultModelSyncFile
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.builder.model.IJavaCompilerSettings
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData
import com.itsaky.androidide.tooling.api.model.AndroidModule
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.JavaContentRoot
import com.itsaky.androidide.tooling.api.model.JavaModule
import com.itsaky.androidide.tooling.api.model.JavaModuleCompilerSettings
import com.itsaky.androidide.tooling.api.model.JavaModuleDependency
import java.io.File

/**
 * Builds instances of [IdeGradleProject].
 *
 * @author Akash Yadav
 */
class ProjectBuilder {
  var name: String = ""
  var description: String? = null
  var path: String = ":"
  var projectDir: File = File(".")
  var buildDir: File = File(".")
  var buildScript: File = File(".")
  var parent: IdeGradleProject? = null
  var modules: List<IdeGradleProject> = mutableListOf()
  var tasks: List<GradleTask> = mutableListOf()
  var dynamicFeatures: Collection<String>? = mutableListOf()
  var flags: DefaultAndroidGradlePluginProjectFlags =
    DefaultAndroidGradlePluginProjectFlags(emptyMap())

  // For Android modules only
  var javaCompileOptions: DefaultJavaCompileOptions = DefaultJavaCompileOptions()

  // For Java modules
  var javaCompilerSettings: IJavaCompilerSettings = JavaModuleCompilerSettings()

  var resourcePrefix: String? = ""
  var viewBindingOptions: DefaultViewBindingOptions? = null
  var modelSyncFiles: List<DefaultModelSyncFile> = emptyList()
  var lintChecksJars: List<File> = mutableListOf()
  var contentRoots: List<JavaContentRoot> = mutableListOf()
  var javaDependencies: MutableList<JavaModuleDependency> = mutableListOf()
  var projectType: ProjectType? = null
  var simpleVariants: MutableList<SimpleVariantData> = mutableListOf()

  fun buildGradleProject(): IdeGradleProject {
    return IdeGradleProject(
      name,
      description,
      path,
      projectDir,
      buildDir,
      buildScript,
      parent,
      tasks
    )
  }

  fun buildJavaModule(): JavaModule {
    return JavaModule(
      name,
      path,
      description,
      projectDir,
      buildDir,
      buildScript,
      parent,
      tasks,
      javaCompilerSettings,
      contentRoots,
      javaDependencies
    )
  }

  fun buildAndroidModule(): AndroidModule =
    AndroidModule(
      name = name,
      path = path,
      description = description,
      projectDir = projectDir,
      buildDir = buildDir,
      buildScript = buildScript,
      parent = parent,
      tasks = tasks,
      projectType = projectType,
      dynamicFeatures = dynamicFeatures,
      flags = flags,
      javaCompileOptions = javaCompileOptions,
      resourcePrefix = resourcePrefix,
      viewBindingOptions = viewBindingOptions,
      lintChecksJars = lintChecksJars,
      modelSyncFiles = modelSyncFiles,
      simpleVariants = simpleVariants
    )
}
