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

import java.io.File
import java.io.Serializable
import java.util.concurrent.*
import org.eclipse.lsp4j.jsonrpc.CompletableFutures

/**
 * A root Gradle project.
 * @author Akash Yadav
 */
open class IdeGradleProject(
    @JvmField val name: String,
    @JvmField val description: String?,
    @JvmField val projectPath: String,
    @JvmField val projectDir: File,
    @JvmField val buildDir: File,
    @JvmField val buildScript: File,
    @JvmField val parent: IdeGradleProject?,
    @JvmField val tasks: List<IdeGradleTask>,
) : IProject, Serializable {
    private val serialVersionUID = 1L

    private val gsonType: String = javaClass.name
    @JvmField val moduleProjects: MutableList<IdeGradleProject> = mutableListOf()

    override fun getName(): CompletableFuture<String> {
        return CompletableFutures.computeAsync { this.name }
    }

    override fun getDescription(): CompletableFuture<String> {
        return CompletableFutures.computeAsync { this.description }
    }

    override fun getProjectPath(): CompletableFuture<String> {
        return CompletableFutures.computeAsync { this.projectPath }
    }

    override fun getProjectDir(): CompletableFuture<File> {
        return CompletableFutures.computeAsync { this.projectDir }
    }

    override fun getBuildDir(): CompletableFuture<File> {
        return CompletableFutures.computeAsync { this.buildDir }
    }

    override fun getBuildScript(): CompletableFuture<File> {
        return CompletableFutures.computeAsync { this.buildScript }
    }

    override fun getTasks(): CompletableFuture<MutableList<IdeGradleTask>> {
        return CompletableFutures.computeAsync { this.tasks.toMutableList() }
    }

    override fun getModules(): CompletableFuture<MutableList<IdeGradleProject>> {
        return CompletableFutures.computeAsync { this.moduleProjects.toMutableList() }
    }

    override fun findByPath(path: String): CompletableFuture<IdeGradleProject?> {
        return CompletableFutures.computeAsync {
            if (path == this.projectPath) {
                return@computeAsync this
            }

            return@computeAsync moduleProjects.firstOrNull { it.projectPath == path }
        }
    }

    override fun findAndroidModules(): CompletableFuture<List<IdeAndroidModule>> {
        return CompletableFutures.computeAsync {
            moduleProjects.filterIsInstance(IdeAndroidModule::class.java)
        }
    }

    override fun findFirstAndroidModule(): CompletableFuture<IdeAndroidModule?> {
        return findAndroidModules().thenApply { it.firstOrNull() }
    }
}
