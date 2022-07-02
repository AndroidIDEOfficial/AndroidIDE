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

import com.android.builder.model.v2.ide.LibraryType.ANDROID_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.JAVA_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.PROJECT
import com.android.builder.model.v2.ide.LibraryType.RELOCATED
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.model.AndroidModule
import com.itsaky.androidide.tooling.api.model.JavaModule
import com.itsaky.androidide.tooling.api.model.ModuleProject
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.nio.file.Path

/**
 * Provides API to collect data from projects.
 *
 * @author Akash Yadav
 */
object ProjectDataCollector {
  private val log = ILogger.newInstance(javaClass.simpleName)

  fun collectResDirectories(rootProject: IProject, android: AndroidModule): List<File> {
    val main = android.mainSourceSet
    if (main == null) {
      log.error("No main source set found in application module: ${android.name}")
      return emptyList()
    }

    val dirs = mutableListOf<File>()
    if (main.sourceProvider.resDirectories != null) {
      dirs.addAll(main.sourceProvider.resDirectories!!)
    }

    val dependencies =
      collectProjectDependencies(rootProject, android).filterIsInstance(AndroidModule::class.java)

    for (dependency in dependencies) {
      dirs.addAll(collectResDirectories(rootProject, dependency))
    }

    return dirs
  }

  fun collectClassPaths(rootProject: IProject, app: AndroidModule): Set<Path> {

    val libraries = app.debugLibraries
    val paths = mutableSetOf<Path>()
    val modules = rootProject.listModules().get()
    for (value in libraries) {
      if (value.type == RELOCATED) {
        // this library does not have any artifacts
        continue
      }

      if (value.type == ANDROID_LIBRARY) {
        paths.addAll(value.androidLibraryData!!.compileJarFiles.map { it.toPath() })
      } else if (value.type == JAVA_LIBRARY) {
        paths.add(value.artifact!!.toPath())
      } else {
        val projectPath = value.projectInfo!!.projectPath
        val module = modules.firstOrNull { it.path == projectPath } ?: continue
        if (module.classPaths.isNotEmpty()) {
          paths.addAll(module.classPaths.map { it.toPath() })
        }
      }
    }

    return paths
  }

  fun collectSourceDirs(rootProject: IProject, app: AndroidModule): Set<Path> {
    val sourcePaths = mutableSetOf<File>()
    val projectSources = collectSourceDirs(collectProjectDependencies(rootProject, app))
    sourcePaths.addAll(projectSources)
    sourcePaths.addAll(collectSources(app))
    return sourcePaths.map { it.toPath() }.toSet()
  }

  fun collectProjectDependencies(project: IProject, app: AndroidModule): List<ModuleProject> {
    return app.debugLibraries
      .filter { it.type == PROJECT }
      .map { project.findByPath(it.projectInfo!!.projectPath) }
      .filterIsInstance(ModuleProject::class.java)
  }

  fun collectSourceDirs(projects: List<ModuleProject>): Set<File> {
    val sources = mutableSetOf<File>()
    for (project in projects) {
      if (project is JavaModule) {
        sources.addAll(collectSources(project))
      } else if (project is AndroidModule) {
        sources.addAll(collectSources(project))
      }
    }

    return sources
  }

  fun collectSourceDirs(vararg projects: ModuleProject): List<File> {
    val sources = mutableListOf<File>()
    for (project in projects) {
      if (project is JavaModule) {
        sources.addAll(collectSources(project))
      } else if (project is AndroidModule) {
        sources.addAll(collectSources(project))
      }
    }

    return sources
  }

  fun collectSources(java: JavaModule): List<File> {
    val sources = mutableListOf<File>()
    for (root in java.contentRoots) {
      sources.addAll(root.sourceDirectories.map { it.directory })
    }

    return sources
  }

  fun collectSources(android: AndroidModule): List<File> {
    if (android.mainSourceSet == null) {
      return mutableListOf()
    }

    // src/main/java
    val sources = android.mainSourceSet!!.sourceProvider.javaDirectories.toMutableList()

    // src/main/kotlin
    sources.addAll(android.mainSourceSet!!.sourceProvider.kotlinDirectories)

    // build/generated/**
    // AIDL, ViewBinding, Renderscript, BuildConfig i.e every generated source sources
    val debugVariant = android.simpleVariants.firstOrNull { it.name == "debug" }
    if (debugVariant != null) {
      sources.addAll(debugVariant.mainArtifact.generatedSourceFolders)
    }
    return sources
  }
}
