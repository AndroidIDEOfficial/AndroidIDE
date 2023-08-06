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

import com.itsaky.androidide.projects.api.AndroidModule
import com.itsaky.androidide.projects.api.JavaModule
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.AndroidProjectMetadata
import com.itsaky.androidide.tooling.api.models.BasicProjectMetadata
import com.itsaky.androidide.tooling.api.models.JavaProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.utils.ILogger
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Transforms project models from tooling API to the projects API.
 *
 * @author Akash Yadav
 */
class ProjectTransformer {

  private val log = ILogger.newInstance(javaClass.simpleName)

  fun transform(project: IProject): Project? {
    try {
      val allProjects = project.getProjects().get()
      val selectionResult = project.selectProject(StringParameter(IProject.ROOT_PROJECT_PATH)).get()
      check(selectionResult.isSuccessful) {
        "Cannot find root project with path '${IProject.ROOT_PROJECT_PATH}'"
      }

      val rootProject = when (project.getType().get()) {
        ProjectType.Gradle -> project.asGradleProject()
        ProjectType.Android -> project.asGradleProject()
        else -> throw IllegalStateException(
          "Root project must be either an Android project or a Gradle project")
      }

      val metadata = rootProject.getMetadata().get()
      return Project(
        name = metadata.name ?: IProject.PROJECT_UNKNOWN,
        description = metadata.description ?: "",
        path = metadata.projectPath,
        projectDir = metadata.projectDir,
        buildDir = metadata.buildDir,
        buildScript = metadata.buildScript,

        // As these lists will never change, we could make these thread-safe with
        // CopyOnWriteArrayList
        tasks = CopyOnWriteArrayList(rootProject.getTasks().get() ?: listOf()),
        subModules = CopyOnWriteArrayList(transform(allProjects, project))
      )
    } catch (error: Throwable) {
      log.error("Unable to transform project", error)
      return null
    }
  }

  private fun transform(
    project: IAndroidProject
  ): AndroidModule {
    val metadata = project.getMetadata().get() as AndroidProjectMetadata
    val libraryMap = project.getLibraryMap().get()
    return AndroidModule(
      name = metadata.name ?: IProject.PROJECT_UNKNOWN,
      description = metadata.description ?: "",
      path = metadata.projectPath,
      projectDir = metadata.projectDir,
      buildDir = metadata.buildDir,
      buildScript = metadata.buildScript,
      tasks = project.getTasks().get(),
      packageName = metadata.packageName,
      resourcePrefix = metadata.resourcePrefix,
      namespace = metadata.namespace,
      androidTestNamespace = metadata.androidTestNamespace,
      testFixtureNamespace = metadata.testFixtureNamespace,
      projectType = metadata.androidType,
      mainSourceSet = project.getMainSourceSet().get(),
      flags = metadata.flags,
      compilerSettings = metadata.javaCompileOptions,
      viewBindingOptions = metadata.viewBindingOptions,
      bootClassPaths = project.getBootClasspaths().get(),
      libraries = libraryMap.keys,
      libraryMap = libraryMap,
      lintCheckJars = project.getLintCheckJars().get(),
      modelSyncFiles = project.getModelSyncFiles().get(),
      variants = project.getVariants().get(),
      classesJar = metadata.classesJar
    )
  }

  private fun transform(project: IJavaProject): JavaModule {
    val metadata = project.getMetadata() as JavaProjectMetadata
    return JavaModule(
      name = metadata.name ?: IProject.PROJECT_UNKNOWN,
      description = metadata.description ?: "",
      path = metadata.projectPath,
      projectDir = metadata.projectDir,
      buildDir = metadata.buildDir,
      buildScript = metadata.buildScript,
      tasks = project.getTasks().get(),
      contentRoots = project.getContentRoots().get(),
      dependencies = project.getDependencies().get(),
      compilerSettings = metadata.compilerSettings,
      classesJar = metadata.classesJar
    )
  }

  private fun transform(modules: List<BasicProjectMetadata>, root: IProject): List<Project> {
    return mutableListOf<Project>().apply {
      for (module in modules) {
        add(createProject(module, root))
      }
    }
  }

  private fun createProject(moduleMetadata: BasicProjectMetadata, root: IProject): Project {
    val selectionResult = root.selectProject(StringParameter(moduleMetadata.projectPath)).get()
    check(selectionResult.isSuccessful) {
      "Selection failed for project '${moduleMetadata.projectPath}' but it is included in all projects."
    }

    val type = root.getType().get() ?: throw java.lang.IllegalStateException("Invalid module data")

    return when (type) {
      ProjectType.Gradle,
      ProjectType.Unknown -> transform(project)!!
      ProjectType.Android -> transform(project as IAndroidProject)
      ProjectType.Java -> transform(project as IJavaProject)
    }
  }
}
