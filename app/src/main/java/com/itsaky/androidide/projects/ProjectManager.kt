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

import com.itsaky.androidide.app.StudioApp
import com.itsaky.androidide.services.BuildService
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeJavaModule
import com.itsaky.androidide.tooling.api.model.IdeModule
import com.itsaky.androidide.tooling.api.util.ProjectDataCollector
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.java.models.JavaServerConfiguration
import java.io.File
import java.nio.file.Path
import java.util.concurrent.*

/**
 * Manages projects in AndroidIDE.
 *
 * @author Akash Yadav
 */
@Suppress("MemberVisibilityCanBePrivate")
object ProjectManager {
    private val log = ILogger.newInstance(javaClass.simpleName)

    var rootProject: IProject? = null
        set(value) {
            field = if (value == null) null else CachingProject(value)

            // Cache the module data in advance for future use
            if (field != null) {
                field!!.listModules()
            }
        }
    var projectPath: String? = null

    fun isInitialized() = this.rootProject != null && this.rootProject!!.isProjectInitialized.get()

    fun checkInit(): Boolean {
        if (isInitialized()) {
            return true
        }

        log.warn("Project is not initialized yet!")
        return false
    }

    fun getProjectDir(): File? = if (!checkInit()) null else File(getProjectDirPath()!!)

    fun getProjectDirPath(): String? {
        return projectPath
    }

    fun generateSources(builder: BuildService?) {
        if (builder == null) {
            log.warn("Cannot generate sources. BuildService is null.")
            return
        }

        getApplicationModule().whenCompleteAsync { app, _ ->
            if (app == null) {
                log.warn(
                    "Cannot run resource and source generation task. No application module found.")
                return@whenCompleteAsync
            }

            val debug = app.simpleVariants.firstOrNull { it.name == "debug" }
            if (debug == null) {
                log.warn("No debug variant found in application project ${app.name}")
                return@whenCompleteAsync
            }

            val mainArtifact = app.simpleVariants.first { it.name == "debug" }.mainArtifact
            val genResourcesTask = mainArtifact.resGenTaskName
            val genSourcesTask = mainArtifact.sourceGenTaskName
            builder
                .executeProjectTasks(
                    app.projectPath,
                    genResourcesTask ?: "",
                    genSourcesTask,

                    // If view binding is enabled, generate the view binding classes too
                    if (app.viewBindingOptions != null && app.viewBindingOptions!!.isEnabled)
                        "dataBindingGenBaseClassesDebug"
                    else "")
                .whenComplete { result, taskErr ->
                    if (!result.isSuccessful || taskErr != null) {
                        log.warn(
                            "Execution for tasks '$genResourcesTask' and '$genSourcesTask' failed.",
                            taskErr ?: "")
                    } else {
                        notifyProjectUpdate()
                    }
                }
        }
    }

    fun getApplicationModule(): CompletableFuture<IdeAndroidModule?> {
        if (rootProject == null) {
            log.error(
                "No root project instance is set. Is the project initialization process finished?")
            return CompletableFuture.completedFuture(null)
        }

        return rootProject!!.findFirstAndroidAppModule().whenComplete { app, err ->
            if (app == null || err != null) {
                log.debug("An error occurred while fetching model for application module.")
                return@whenComplete
            }
        }
    }

    fun getApplicationResDirectories(): CompletableFuture<List<File>?> {
        return getApplicationModule().thenApplyAsync {
            if (it == null) null else collectResDirectories(it)
        }
    }

    fun notifyProjectUpdate() {
        log.debug("Updating class paths in language servers...")
        getApplicationModule().whenCompleteAsync { it, _ ->
            if (it == null) {
                log.debug("Cannot find application module. Unable to update class paths to LSP.")
                return@whenCompleteAsync
            }

            val server = StudioApp.getInstance().javaLanguageServer
            val classPaths = collectClassPaths(it)
            val sourceDirs = collectSourceDirs(it)
            val configuration = JavaServerConfiguration(classPaths, sourceDirs)
            server.configurationChanged(configuration)
        }
    }

    fun collectResDirectories(android: IdeAndroidModule) =
        ProjectDataCollector.collectResDirectories(this.rootProject!!, android)

    fun collectClassPaths(app: IdeAndroidModule) =
        ProjectDataCollector.collectClassPaths(this.rootProject!!, app)

    fun collectSourceDirs(app: IdeAndroidModule): Set<Path> =
        ProjectDataCollector.collectSourceDirs(this.rootProject!!, app)

    @Suppress("unused")
    fun collectProjectDependencies(project: IProject, app: IdeAndroidModule) =
        ProjectDataCollector.collectProjectDependencies(project, app)

    @Suppress("unused")
    fun collectSourceDirs(projects: List<IdeModule>) =
        ProjectDataCollector.collectSourceDirs(projects)

    @Suppress("unused")
    fun collectSources(java: IdeJavaModule) = ProjectDataCollector.collectSources(java)

    @Suppress("unused")
    fun collectSources(android: IdeAndroidModule) = ProjectDataCollector.collectSources(android)

    fun findModuleForFile(file: File): CompletableFuture<IdeGradleProject?> {
        if (!checkInit()) {
            return CompletableFuture.completedFuture(null)
        }

        val notAvailable = ":::::"
        return rootProject!!
            .listModules()
            .thenApplyAsync { modules ->
                val path = file.canonicalPath

                var longestPath = ""
                for (module in modules) {
                    val modulePath = module.projectDir.canonicalPath
                    if (path.startsWith(modulePath)) {
                        if (longestPath.length < modulePath.length) {
                            longestPath = modulePath
                        }
                    }
                }

                if (longestPath.isEmpty()) {
                    return@thenApplyAsync notAvailable
                }

                return@thenApplyAsync longestPath
            }
            .thenApplyAsync {
                if (it == notAvailable) {
                    return@thenApplyAsync null
                } else return@thenApplyAsync rootProject!!.findByPath(it).get()
            }
    }
}
