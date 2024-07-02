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

package com.itsaky.androidide.projects.android

import com.android.SdkConstants
import com.android.aaptcompiler.AaptResourceType
import com.android.builder.model.v2.ide.LibraryType.ANDROID_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.JAVA_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.PROJECT
import com.android.builder.model.v2.ide.ProjectType
import com.itsaky.androidide.builder.model.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.builder.model.DefaultJavaCompileOptions
import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.DefaultSourceSetContainer
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.builder.model.UNKNOWN_PACKAGE
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.projects.IWorkspace
import com.itsaky.androidide.projects.ModuleProject
import com.itsaky.androidide.tooling.api.ProjectType.Android
import com.itsaky.androidide.tooling.api.models.BasicAndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.GradleTask
import com.itsaky.androidide.tooling.api.util.findPackageName
import com.itsaky.androidide.utils.withStopWatch
import com.itsaky.androidide.xml.res.IResourceTable
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.versions.ApiVersions
import com.itsaky.androidide.xml.versions.ApiVersionsRegistry
import com.itsaky.androidide.xml.widgets.WidgetTable
import com.itsaky.androidide.xml.widgets.WidgetTableRegistry
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * A [GradleProject] model implementation for Android modules which is exposed to other modules and
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
 * @param resourcePrefix The resource prefix.
 * @param namespace The namespace of this project. As defined in the buildscript.
 * @param androidTestNamespace The androidTestNamespace of this project. As defined in the
 * buildscript.
 * @param testFixtureNamespace The testFixtureNamespace of this project. As defined in the
 * @param projectType The type of Android project. See [ProjectType].
 * @param mainSourceSet The main source of this module.
 * @param flags The Android Gradle Plugin flags. No-op currently.
 * @param compilerSettings The java compilation options as configured in buildscript.
 * @param viewBindingOptions The view binding options of this module.
 * @param bootClassPaths The boot class paths of the project. Usually contains the path to the
 * `android.jar` file.
 * @param libraries The list of libraries for the debug variant. Values must be values in the
 * [libraryMap].
 * @param libraryMap The map of libraries. Keys are the key field obtained from the GraphItem
 * instance in the Tooling API implementation.
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
  val resourcePrefix: String?,
  val namespace: String?,
  val androidTestNamespace: String?,
  val testFixtureNamespace: String?,
  val projectType: ProjectType,
  val mainSourceSet: DefaultSourceSetContainer?,
  val flags: DefaultAndroidGradlePluginProjectFlags,
  override val compilerSettings: DefaultJavaCompileOptions,
  val viewBindingOptions: DefaultViewBindingOptions,
  val bootClassPaths: Collection<File>,
  val libraries: Set<String>,
  val libraryMap: Map<String, DefaultLibrary>,
  val lintCheckJars: List<File>,
  val variants: List<BasicAndroidVariantMetadata> = listOf(),
  val configuredVariant: BasicAndroidVariantMetadata?,
  val classesJar: File?
) : ModuleProject(
  name, description, path, projectDir, buildDir, buildScript, tasks
) {

  /**
   * Whether this project is an Android library project.
   */
  val isLibrary: Boolean
    get() = this.projectType == ProjectType.LIBRARY

  /**
   * Whether this project is an Android application project.
   */
  val isApplication: Boolean
    get() = this.projectType == ProjectType.APPLICATION

  companion object {

    private val log = LoggerFactory.getLogger(AndroidModule::class.java)
  }

  init {
    type = Android
  }

  fun getGeneratedJar(): File {
    return classesJar ?: File("does-not-exist.jar")
  }

  override fun getClassPaths(): Set<File> {
    return getModuleClasspaths()
  }

  fun getVariant(name: String): BasicAndroidVariantMetadata? {
    return this.variants.firstOrNull { it.name == name }
  }

  fun getResourceDirectories(): Set<File> {
    if (mainSourceSet == null) {
      log.error("No main source set found in application module: {}", name)
      return emptySet()
    }

    val dirs = mutableSetOf<File>()
    if (mainSourceSet.sourceProvider.resDirectories != null) {
      dirs.addAll(mainSourceSet.sourceProvider.resDirectories!!)
    }

    val dependencies = getCompileModuleProjects().filterIsInstance<AndroidModule>()

    for (dependency in dependencies) {
      dirs.addAll(dependency.getResourceDirectories())
    }

    return dirs
  }

  override fun getSourceDirectories(): Set<File> {
    if (mainSourceSet == null) {
      log.warn(
        "No main source set is available for project {}. Cannot get source directories.", name
      )
      return mutableSetOf()
    }

    // src/main/java
    val sources = mainSourceSet.sourceProvider.javaDirectories.toMutableSet()

    // src/main/kotlin
    sources.addAll(mainSourceSet.sourceProvider.kotlinDirectories)

    // build/generated/**
    // AIDL, ViewHolder, Renderscript, BuildConfig i.e every generated source sources
    val selectedVariant = getSelectedVariant()
    if (selectedVariant != null) {
      sources.addAll(selectedVariant.mainArtifact.generatedSourceFolders)
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
      add(getGeneratedJar())
      addAll(getSelectedVariant()?.mainArtifact?.classJars ?: emptyList())
    }
  }

  override fun getCompileClasspaths(): Set<File> {
    val project = IProjectManager.getInstance().getWorkspace() ?: return emptySet()
    val result = mutableSetOf<File>()
    result.addAll(getModuleClasspaths())

    collectLibraries(project, this.libraries, result)
    return result
  }

  private fun collectLibraries(root: IWorkspace, libraries: Set<String>, result: MutableSet<File>) {
    for (library in libraries) {
      val lib = this.libraryMap[library] ?: continue
      if (lib.type == PROJECT) {
        val module = root.findProject(lib.projectInfo!!.projectPath) ?: continue
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
    val root = IProjectManager.getInstance().getWorkspace() ?: return emptyList()
    val result = mutableListOf<ModuleProject>()

    for (library in this.libraries) {
      val lib = this.libraryMap[library] ?: continue
      if (lib.type != PROJECT) {
        continue
      }

      val module = root.findProject(lib.projectInfo!!.projectPath) ?: continue
      if (module !is ModuleProject) {
        continue
      }

      result.add(module)
      result.addAll(module.getCompileModuleProjects())
    }

    return result
  }

  /**
   * Reads the resource files are creates the [com.android.aaptcompiler.ResourceTable] instances for
   * the corresponding resource directories.
   */
  suspend fun readResources() {
    // Read resources in parallel
    withStopWatch("Read resources for module : $path") {
      val resourceReaderScope = CoroutineScope(
        Dispatchers.IO + CoroutineName("ResourceReader($path)")
      )

      val resourceFlow = flow {
        emit(getFrameworkResourceTable())
        emit(getResourceTable())
        emit(getDependencyResourceTables())
        emit(getApiVersions())
        emit(getWidgetTable())
      }

      val jobs = resourceFlow.map { result ->
        resourceReaderScope.async {
          result
        }
      }

      jobs.toList().awaitAll()
    }
  }

  /**
   * Get the [ApiVersions] instance for this module.
   *
   * @return The [ApiVersions] for this module.
   */
  fun getApiVersions(): ApiVersions? {
    val platformDir = getPlatformDir()
    if (platformDir != null) {
      return ApiVersionsRegistry.getInstance().forPlatformDir(platformDir)
    }

    return null
  }

  /**
   * Get the [WidgetTable] instance for this module.
   *
   * @return The [WidgetTable] for this module.
   */
  fun getWidgetTable(): WidgetTable? {
    val platformDir = getPlatformDir()
    if (platformDir != null) {
      return WidgetTableRegistry.getInstance().forPlatformDir(platformDir)
    }

    return null
  }

  /** Get the resource table for this module i.e. without resource tables for dependent modules. */
  fun getResourceTable(): IResourceTable? {
    val namespace = this.namespace ?: return null

    val resDirs = mainSourceSet?.sourceProvider?.resDirectories ?: return null
    return ResourceTableRegistry.getInstance().forPackage(namespace, *resDirs.toTypedArray())
  }

  /** Updates the resource table for this module. */
  fun updateResourceTable() {
    if (this.namespace == null) {
      return
    }

    CompletableFuture.runAsync {
      val tableRegistry = ResourceTableRegistry.getInstance()
      val resDirs = mainSourceSet?.sourceProvider?.resDirectories ?: return@runAsync
      tableRegistry.removeTable(this.namespace)
      tableRegistry.forPackage(this.namespace, *resDirs.toTypedArray())
    }
  }

  /**
   * Get the [IResourceTable] instance for this module's compile SDK.
   *
   * @return The [ApiVersions] for this module.
   */
  fun getFrameworkResourceTable(): IResourceTable? {
    val platformDir = getPlatformDir()
    if (platformDir != null) {
      return ResourceTableRegistry.getInstance().forPlatformDir(platformDir)
    }

    return null
  }

  /**
   * Get the resource tables for this module as well as it's dependent modules.
   *
   * @return The set of resource tables. Empty when project is not initalized.
   */
  fun getSourceResourceTables(): Set<IResourceTable> {
    val set = mutableSetOf(getResourceTable() ?: return emptySet())
    getCompileModuleProjects().filterIsInstance<AndroidModule>().forEach {
      it.getResourceTable()?.also { table -> set.add(table) }
    }
    return set
  }

  /** Get the resource tables for external dependencies (not local module project dependencies). */
  fun getDependencyResourceTables(): Set<IResourceTable> {
    return mutableSetOf<IResourceTable>().also {
      var deps: Int
      it.addAll(libraryMap.values.filter { library ->
        library.type == ANDROID_LIBRARY && library.androidLibraryData!!.resFolder.exists() && library.findPackageName() != UNKNOWN_PACKAGE
      }.also { libs -> deps = libs.size }.mapNotNull { library ->
        ResourceTableRegistry.getInstance().let { registry ->
          registry.isLoggingEnabled = false
          registry.forPackage(
            library.packageName,
            library.androidLibraryData!!.resFolder,
          ).also {
            registry.isLoggingEnabled = true
          }
        }
      })

      log.info("Created {} resource tables for {} dependencies of module '{}'", it.size, deps, path)
    }
  }

  /**
   * Checks all the resource tables from this module and returns if any of the tables contain
   * resources for the the given package.
   *
   * @param pck The package to look for.
   */
  fun findResourceTableForPackage(
    pck: String,
    hasGroup: AaptResourceType? = null
  ): IResourceTable? {
    return findAllResourceTableForPackage(pck, hasGroup).let {
      if (it.isNotEmpty()) {
        return it.first()
      } else null
    }
  }

  /**
   * Checks all the resource tables from this module and returns if any of the tables contain
   * resources for the the given package.
   *
   * @param pck The package to look for.
   */
  fun findAllResourceTableForPackage(
    pck: String, hasGroup: AaptResourceType? = null
  ): List<IResourceTable> {
    if (pck == SdkConstants.ANDROID_PKG) {
      return getFrameworkResourceTable()?.let { listOf(it) } ?: emptyList()
    }

    val tables: List<IResourceTable> = mutableListOf<IResourceTable>().apply {
      getResourceTable()?.let { add(it) }
      addAll(getSourceResourceTables())
      addAll(getDependencyResourceTables())
    }

    val result = mutableListOf<IResourceTable>()
    for (table in tables) {
      val resPck = table.findPackage(pck) ?: continue
      if (hasGroup == null) {
        result.add(table)
        continue
      }
      if (resPck.findGroup(hasGroup) != null) {
        result.add(table)
        continue
      }
    }

    return emptyList()
  }

  /**
   * Returns all the resource tables associated with this module (including the framework resource
   * table).
   *
   * @return The associated resource tables.
   */
  fun getAllResourceTables(): Set<IResourceTable> {
    return mutableSetOf<IResourceTable>().apply {
      getResourceTable()?.let { add(it) }
      getFrameworkResourceTable()?.let { add(it) }
      addAll(getSourceResourceTables())
      addAll(getDependencyResourceTables())
    }
  }

  /** Get the resource table for the attrs_manifest.xml file. */
  fun getManifestAttrTable(): IResourceTable? {
    val platform = getPlatformDir() ?: return null
    return ResourceTableRegistry.getInstance().getManifestAttrTable(platform)
  }

  /** @see ResourceTableRegistry.getActivityActions */
  fun getActivityActions(): List<String> {
    return ResourceTableRegistry.getInstance()
      .getActivityActions(getPlatformDir() ?: return emptyList())
  }

  /** @see ResourceTableRegistry.getBroadcastActions */
  fun getBroadcastActions(): List<String> {
    return ResourceTableRegistry.getInstance()
      .getBroadcastActions(getPlatformDir() ?: return emptyList())
  }

  /** @see ResourceTableRegistry.getServiceActions */
  fun getServiceActions(): List<String> {
    return ResourceTableRegistry.getInstance()
      .getServiceActions(getPlatformDir() ?: return emptyList())
  }

  /** @see ResourceTableRegistry.getCategories */
  fun getCategories(): List<String> {
    return ResourceTableRegistry.getInstance().getCategories(getPlatformDir() ?: return emptyList())
  }

  /** @see ResourceTableRegistry.getFeatures */
  fun getFeatures(): List<String> {
    return ResourceTableRegistry.getInstance().getFeatures(getPlatformDir() ?: return emptyList())
  }

  /**
   * Returns the build variant that is selected by the user. This may return `null` in
   * some misconfiguration scenarios.
   */
  fun getSelectedVariant(): BasicAndroidVariantMetadata? {
    val projectManager = IProjectManager.getInstance()

    val info =
      (projectManager.getWorkspace()?.getAndroidVariantSelections() ?: emptyMap())[this.path]
    if (info == null) {
      log.error(
        "Failed to find selected build variant for module: '{}'", this.path
      )
      return null
    }

    val variant = this.getVariant(info.selectedVariant)
    if (variant == null) {
      log.error(
        "Build variant with name '{}' not found.", info.selectedVariant
      )
      return null
    }

    return variant
  }

  /**
   * Get the Android SDK platform directory for this Android module.
   *
   * @return The Android SDK platform directory for this Android module, or `null` if none is found.
   */
  fun getPlatformDir() = bootClassPaths.firstOrNull { it.name == "android.jar" }?.parentFile
}