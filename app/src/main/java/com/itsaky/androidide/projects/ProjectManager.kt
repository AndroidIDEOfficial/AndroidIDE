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
package com.itsaky.androidide.projects

import com.android.builder.model.v2.ide.LibraryType.ANDROID_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.JAVA_LIBRARY
import com.android.builder.model.v2.ide.LibraryType.PROJECT
import com.itsaky.androidide.app.StudioApp
import com.itsaky.androidide.services.BuildService
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeJavaModule
import com.itsaky.androidide.tooling.api.model.IdeModule
import com.itsaky.lsp.java.models.JavaServerConfiguration
import java.io.File
import java.nio.file.Path

/**
 * Manages projects in AndroidIDE.
 *
 * @author Akash Yadav
 */
object ProjectManager {
    private val log = com.itsaky.androidide.utils.ILogger.newInstance(javaClass.simpleName)
    var rootProject: IdeGradleProject? = null
    var projectPath: String? = null

    fun getProjectDir(): File? =
        if (getProjectDirPath() == null) null else File(getProjectDirPath()!!)

    fun getProjectDirPath(): String? =
        if (rootProject == null) projectPath else rootProject!!.projectDir!!.absolutePath

    fun generateSources(builder: BuildService?) {
        if (builder == null) {
            log.warn("Cannot generate sources. BuildService is null.")
            return
        }

        val app =
            getApplicationModule()
                ?: kotlin.run {
                    log.error("Cannot generate resources...")
                    return
                }

        val debug = app.variants.firstOrNull { it.name == "debug" }
        if (debug == null) {
            log.warn("No debug variant found in application project ${app.name}")
            return
        }

        val mainArtifact = app.variants.first { it.name == "debug" }.mainArtifact
        val genResourcesTask = mainArtifact.resGenTaskName
        val genSourcesTask = mainArtifact.sourceGenTaskName
        builder
            .executeProjectTasks(
                app.projectPath!!,
                genResourcesTask ?: "",
                genSourcesTask,

                // If view binding is enabled, generate the view binding classes too
                if (app.viewBindingOptions != null && app.viewBindingOptions!!.isEnabled)
                    "dataBindingGenBaseClassesDebug"
                else "")
            .whenComplete { result, err ->
                if (!result.isSuccessful || err != null) {
                    log.warn(
                        "Execution for tasks '$genResourcesTask' and '$genSourcesTask' failed.",
                        err ?: "")
                    return@whenComplete
                }

                notifyProjectUpdate()
            }
    }

    fun getApplicationModule(): IdeAndroidModule? {
        if (rootProject == null) {
            log.error(
                "No root project instance is set. Is the project initialization process finished?")
            return null
        }

        val app = rootProject!!.findFirstAndroidModule()
        if (app == null) {
            log.error("No application module found in root project.")
            return null
        }

        return app
    }

    fun getApplicationResDirectories(): List<File>? {
        return collectResDirectories(getApplicationModule() ?: return null)
    }

    fun collectResDirectories(android: IdeAndroidModule): List<File> {
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
            collectProjectDependencies(rootProject!!, android)
                .filterIsInstance(IdeAndroidModule::class.java)

        for (dependency in dependencies) {
            dirs.addAll(collectResDirectories(dependency))
        }

        return dirs
    }

    fun notifyProjectUpdate() {
        val server = StudioApp.getInstance().javaLanguageServer
        val sourceDirs = collectApplicationSourceDirs()
        val classPaths = collectApplicationClassPaths()
        val configuration = JavaServerConfiguration(classPaths, sourceDirs)
        server.configurationChanged(configuration)
    }

    private fun collectApplicationClassPaths(): Set<Path> {
        val app = getApplicationModule() ?: return emptySet()

        val libraries = app.variantDependencies["debug"]!!.libraries
        val paths = mutableSetOf<Path>()

        for (value in libraries.values) {
            if (value.type == ANDROID_LIBRARY) {
                paths.addAll(value.androidLibraryData!!.compileJarFiles.map { it.toPath() })
            } else if (value.type == JAVA_LIBRARY) {
                paths.add(value.artifact!!.toPath())
            } else {
                val project =
                    rootProject!!.findByPath(value.projectInfo!!.projectPath)!! as IdeModule
                paths.add(project.getGeneratedJar("debug").toPath())

                if (project is IdeAndroidModule && project.projectPath != app.projectPath) {
                    // R.jar and maybe other JARs
                    paths.addAll(
                        project.variants
                            .first { it.name == "debug" }
                            .mainArtifact.classesFolders
                            .filter { it.name.endsWith(".jar") }
                            .map { it.toPath() })
                }
            }
        }

        return paths
    }

    private fun collectApplicationSourceDirs(): Set<Path> {
        val app = getApplicationModule() ?: return emptySet()
        val sources = app.mainSourceSet ?: return emptySet()
        val javaDirs = sources.sourceProvider.javaDirectories
        val sourcePaths = javaDirs.toMutableSet()
        val projectSources = collectSourceDirs(collectProjectDependencies(rootProject!!, app))
        sourcePaths.addAll(projectSources)
        return sourcePaths.map { it.toPath() }.toSet()
    }

    fun collectProjectDependencies(
        project: IdeGradleProject,
        app: IdeAndroidModule
    ): List<IdeModule> {

        return app.variantDependencies["debug"]!!
            .libraries.values
            .filter { it.type == PROJECT }
            .mapNotNull { project.findByPath(it.projectInfo!!.projectPath) }
            .filterIsInstance(IdeModule::class.java)
    }

    fun collectSourceDirs(projects: List<IdeModule>): Set<File> {
        val sources = mutableSetOf<File>()
        for (project in projects) {
            if (project is IdeJavaModule) {
                sources.addAll(collectSources(project))
            } else if (project is IdeAndroidModule) {
                sources.addAll(collectSources(project))
            }
        }

        return sources
    }

    fun collectSourceDirs(vararg projects: IdeModule): List<File> {
        val sources = mutableListOf<File>()
        for (project in projects) {
            if (project is IdeJavaModule) {
                sources.addAll(collectSources(project))
            } else if (project is IdeAndroidModule) {
                sources.addAll(collectSources(project))
            }
        }

        return sources
    }

    fun collectSources(java: IdeJavaModule): List<File> {
        val sources = mutableListOf<File>()
        for (root in java.contentRoots) {
            sources.addAll(root.sourceDirectories.map { it.directory })
        }

        return sources
    }

    fun collectSources(android: IdeAndroidModule): List<File> {
        if (android.mainSourceSet == null) {
            return mutableListOf()
        }

        // src/main/java
        val sources = android.mainSourceSet!!.sourceProvider.javaDirectories.toMutableList()

        // src/main/kotlin
        sources.addAll(android.mainSourceSet!!.sourceProvider.kotlinDirectories)

        // build/generated/**
        // AIDL, ViewBinding, Renderscript, BuildConfig i.e every generated source sources
        sources.addAll(
            android.variants.first { it.name == "debug" }.mainArtifact.generatedSourceFolders)
        return sources
    }
}
