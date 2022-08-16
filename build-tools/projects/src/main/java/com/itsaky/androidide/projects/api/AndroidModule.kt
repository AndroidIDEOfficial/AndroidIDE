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

package com.itsaky.androidide.projects.api

import com.android.builder.model.v2.ide.LibraryType.ANDROID_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.JAVA_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.PROJECT
import com.android.builder.model.v2.ide.ProjectType
import com.itsaky.androidide.builder.model.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.builder.model.DefaultJavaCompileOptions
import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.DefaultModelSyncFile
import com.itsaky.androidide.builder.model.DefaultSourceSetContainer
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.tooling.api.IProject.Type.Android
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData
import com.itsaky.androidide.tooling.api.model.AndroidModule.Companion.FD_INTERMEDIATES
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.utils.ILogger
import java.io.File

/**
 * A [Project] model implementation for Android modules which is exposed to other modules and
 * provides additional helper methods.
 *
 * @param name The display name of the project.
 * @param description The project description.
 * @param path The project path (same as Gradle project paths). For example, `:app`,
 * `:module:submodule`, etc. Root project is always represented by path `:`.
 * @param projectDir The project directory.
 * @param buildDir The build directory of the project.
 * @param buildScript The Gradle buildscript file of the project.
 * @param tasks The tasks of the project.
 * @param packageName The package name of this module extracted from `AndroidManifest.xml`.
 * @param resourcePrefix The resource prefix.
 * @param namespace The namespace of this project. As defined in the buildscript.
 * @param androidTestNamespace The androidTestNamespace of this project. As defined in the
 * buildscript.
 * @param testFixtureNamespace The testFixtureNamespace of this project. As defined in the
 * @param projectType The type of Android project. See [ProjectType].
 * @param mainSourceSet The main source of this module.
 * @param flags The Android Gradle Plugin flags. No-op currently.
 * @param javaCompileOptions The java compilation options as configured in buildscript.
 * @param viewBindingOptions The view binding options of this module.
 * @param bootClassPaths The boot class paths of the project. Usually contains the path to the
 * `android.jar` file.
 * @param libraries The list of libraries for the debug variant. Values must be values in the
 * [libraryMap].
 * @param libraryMap The map of libraries. Keys are the key field obtained from the GraphItem
 * instance in the Tooling API implementation.
 * @param dynamicFeatures The dynamic features.
 * @param lintCheckJars The lint check jar files.
 * @param modelSyncFiles The model sync files.
 * @author Akash Yadav
 */
open class AndroidModule( // Class must be open because BaseXMLTest mocks this...
  name: String,
  description: String,
  path: String,
  projectDir: File,
  buildDir: File,
  buildScript: File,
  tasks: List<GradleTask>,
  open val packageName: String, // Property must be open because BaseXMLTest mocks this...
  val resourcePrefix: String?,
  val namespace: String?,
  val androidTestNamespace: String?,
  val testFixtureNamespace: String?,
  val projectType: ProjectType,
  val mainSourceSet: DefaultSourceSetContainer?,
  val flags: DefaultAndroidGradlePluginProjectFlags,
  val javaCompileOptions: DefaultJavaCompileOptions,
  val viewBindingOptions: DefaultViewBindingOptions,
  val bootClassPaths: Collection<File>,
  val libraries: Set<String>,
  val libraryMap: Map<String, DefaultLibrary>,
  val dynamicFeatures: Collection<String>?,
  val lintCheckJars: List<File>,
  val modelSyncFiles: List<DefaultModelSyncFile>,
  val variants: List<SimpleVariantData> = listOf()
) :
  ModuleProject(
    name,
    description,
    path,
    projectDir,
    buildDir,
    buildScript,
    tasks,
    javaCompileOptions
  ),
  WithModuleData {

  private val log = ILogger.newInstance(javaClass.simpleName)
  override var moduleData: SimpleModuleData? = null

  init {
    type = Android
  }

  override fun getGeneratedJar(variant: String): File {
    return File(buildDir, "${FD_INTERMEDIATES}/compile_library_classes_jar/$variant/classes.jar")
  }

  override fun getClassPaths(): Set<File> {
    return getModuleClasspaths()
  }

  fun getVariant(name: String): SimpleVariantData? {
    return this.variants.firstOrNull { it.name == name }
  }

  fun getResourceDirectories(): Set<File> {
    if (mainSourceSet == null) {
      log.error("No main source set found in application module: $name")
      return emptySet()
    }

    val dirs = mutableSetOf<File>()
    if (mainSourceSet.sourceProvider.resDirectories != null) {
      dirs.addAll(mainSourceSet.sourceProvider.resDirectories!!)
    }

    val dependencies = getCompileModuleProjects().filterIsInstance(AndroidModule::class.java)

    for (dependency in dependencies) {
      dirs.addAll(dependency.getResourceDirectories())
    }

    return dirs
  }

  override fun getSourceDirectories(): Set<File> {
    if (mainSourceSet == null) {
      log.warn("No main source set is available for project $name. Cannot get source directories.")
      return mutableSetOf()
    }

    // src/main/java
    val sources = mainSourceSet.sourceProvider.javaDirectories.toMutableSet()

    // src/main/kotlin
    sources.addAll(mainSourceSet.sourceProvider.kotlinDirectories)

    // build/generated/**
    // AIDL, ViewBinding, Renderscript, BuildConfig i.e every generated source sources
    val debugVariant = getVariant("debug")
    if (debugVariant != null) {
      sources.addAll(debugVariant.mainArtifact.generatedSourceFolders)
    }
    return sources
  }

  override fun getCompileSourceDirectories(): Set<File> {
    val dirs = mutableSetOf<File>()
    dirs.addAll(getSourceDirectories())
    getCompileModuleProjects().forEach { dirs.addAll(it.getSourceDirectories()) }
    return dirs
  }

  override fun getModuleClasspaths(): Set<File> {
    return mutableSetOf<File>().apply {
      add(getGeneratedJar("debug"))
      addAll(getVariant("debug")?.mainArtifact?.classJars ?: emptyList())
    }
  }

  override fun getCompileClasspaths(): Set<File> {
    val root = ProjectManager.rootProject ?: return emptySet()
    val result = mutableSetOf<File>()
    result.addAll(getModuleClasspaths())

    collectLibraries(root, this.libraries, result)
    return result
  }

  private fun collectLibraries(root: Project, libraries: Set<String>, result: MutableSet<File>) {
    for (library in libraries) {
      val lib = this.libraryMap[library] ?: continue
      if (lib.type == PROJECT) {
        val module = root.findByPath(lib.projectInfo!!.projectPath) ?: continue
        if (module !is ModuleProject) {
          continue
        }

        result.addAll(module.getCompileClasspaths())
      } else if (lib.type == ANDROID_LIBRARY) {
        result.addAll(lib.androidLibraryData!!.compileJarFiles)
      } else if (lib.type == JAVA_LIBRARY) {
        result.add(lib.artifact!!)
      }

      collectLibraries(root, lib.dependencies, result)
    }
  }

  override fun getCompileModuleProjects(): List<ModuleProject> {
    val root = ProjectManager.rootProject ?: return emptyList()
    val result = mutableListOf<ModuleProject>()

    for (library in this.libraries) {
      val lib = this.libraryMap[library] ?: continue
      if (lib.type != PROJECT) {
        continue
      }

      val module = root.findByPath(lib.projectInfo!!.projectPath) ?: continue
      if (module !is ModuleProject) {
        continue
      }

      result.add(module)
      result.addAll(module.getCompileModuleProjects())
    }

    return result
  }
}
