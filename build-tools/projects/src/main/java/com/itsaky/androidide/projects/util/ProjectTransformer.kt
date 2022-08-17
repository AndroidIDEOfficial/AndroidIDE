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

package com.itsaky.androidide.projects.util

import com.android.builder.model.v2.ide.ProjectType.LIBRARY
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.JavaModule
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IProject.Type.Android
import com.itsaky.androidide.tooling.api.IProject.Type.Gradle
import com.itsaky.androidide.tooling.api.IProject.Type.Java
import com.itsaky.androidide.tooling.api.IProject.Type.Unknown
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData
import com.itsaky.androidide.utils.ILogger

/**
 * Transforms project models from tooling API to the projects API.
 *
 * @author Akash Yadav
 */
class ProjectTransformer {

  private val log = ILogger.newInstance(javaClass.simpleName)

  fun transform(project: IProject): Project? {
    try {
      val path = project.projectPath.get()
      val root =
        Project(
          name = project.name.get(),
          description = project.description.get() ?: "",
          path = path,
          projectDir = project.projectDir.get(),
          buildDir = project.buildDir.get(),
          buildScript = project.buildScript.get(),
          tasks = project.tasks.get() ?: listOf()
        )
      (root.subModules as MutableList).addAll(transform(project.listModules().get(), project))
      return root
    } catch (error: Throwable) {
      log.error("Unable to transform project", error)
      return null
    }
  }

  private fun transform(
    project: com.itsaky.androidide.tooling.api.model.AndroidModule
  ): AndroidModule {
    return AndroidModule(
      name = project.name,
      description = project.description ?: "",
      path = project.projectPath,
      projectDir = project.projectDir,
      buildDir = project.buildDir,
      buildScript = project.buildScript,
      tasks = project.tasks,
      packageName = project.packageName,
      resourcePrefix = project.resourcePrefix,
      namespace = project.namespace,
      androidTestNamespace = project.androidTestNamespace,
      testFixtureNamespace = project.testFixturesNamespace,
      projectType = project.projectType ?: LIBRARY,
      mainSourceSet = project.mainSourceSet,
      flags = project.flags,
      javaCompileOptions = project.javaCompileOptions,
      viewBindingOptions = project.viewBindingOptions ?: DefaultViewBindingOptions(),
      bootClassPaths = project.bootClassPaths,
      libraries = project.libraries,
      libraryMap = project.libraryMap,
      dynamicFeatures = project.dynamicFeatures,
      lintCheckJars = project.lintChecksJars,
      modelSyncFiles = project.modelSyncFiles,
      variants = project.simpleVariants
    )
  }

  private fun transform(project: com.itsaky.androidide.tooling.api.model.JavaModule): JavaModule {
    return JavaModule(
      name = project.name,
      description = project.description ?: "",
      path = project.projectPath,
      projectDir = project.projectDir,
      buildDir = project.buildDir,
      buildScript = project.buildScript,
      tasks = project.tasks,
      contentRoots = project.contentRoots,
      dependencies = project.javaDependencies,
      compilerSettings = project.compilerSettings
    )
  }

  private fun transform(modules: MutableList<SimpleModuleData>, root: IProject): List<Project> {
    return mutableListOf<Project>().apply {
      for (module in modules) {
        add(createProject(module, root))
      }
    }
  }

  private fun createProject(module: SimpleModuleData, root: IProject): Project {
    val project =
      root.findByPath(module.path).get()
        ?: throw java.lang.IllegalStateException("Invalid module data")
    val type = project.type.get() ?: throw java.lang.IllegalStateException("Invalid module data")

    return when (type) {
      Gradle,
      Unknown -> transform(project)!!
      Android -> transform(project as com.itsaky.androidide.tooling.api.model.AndroidModule)
      Java -> transform(project as com.itsaky.androidide.tooling.api.model.JavaModule)
    }
  }
}
