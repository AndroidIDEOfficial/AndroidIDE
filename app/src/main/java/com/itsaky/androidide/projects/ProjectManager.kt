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
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeJavaModule
import com.itsaky.androidide.tooling.api.model.IdeModule
import com.itsaky.lsp.java.JavaLanguageServer
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

    fun notifyProjectUpdate(server: JavaLanguageServer) {
        val sourceDirs = collectApplicationSourceDirs(rootProject!!)
        val classPaths = collectApplicationClassPaths(rootProject!!)
        val configuration = JavaServerConfiguration(classPaths, sourceDirs)
        server.configurationChanged(configuration)
    }

    private fun collectApplicationClassPaths(rootProject: IdeGradleProject): Set<Path> {
        val app = getApplicationModule(rootProject)
        val libraries = app.variantDependencies["debug"]!!.libraries
        val paths = mutableSetOf<Path>()

        for (value in libraries.values) {
            if (value.type == ANDROID_LIBRARY) {
                paths.addAll(value.androidLibraryData!!.compileJarFiles.map { it.toPath() })
            } else if (value.type == JAVA_LIBRARY) {
                paths.add(value.artifact!!.toPath())
            } else {
                val project = rootProject.findByPath(value.projectInfo!!.projectPath)!! as IdeModule
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

    private fun collectApplicationSourceDirs(rootProject: IdeGradleProject): Set<Path> {
        val app = getApplicationModule(rootProject)
        val sources = app.mainSourceSet ?: return emptySet()
        val javaDirs = sources.sourceProvider.javaDirectories
        val sourcePaths = javaDirs.toMutableSet()
        val projectSources = collectSourceDirs(collectProjectDependencies(rootProject, app))
        sourcePaths.addAll(projectSources)
        return sourcePaths.map { it.toPath() }.toSet()
    }

    private fun getApplicationModule(rootProject: IdeGradleProject): IdeAndroidModule {
        return rootProject.findFirstAndroidModule()
            ?: throw UnsupportedOperationException(
                "No application module found in project ${rootProject.name} (${rootProject.projectPath})")
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
