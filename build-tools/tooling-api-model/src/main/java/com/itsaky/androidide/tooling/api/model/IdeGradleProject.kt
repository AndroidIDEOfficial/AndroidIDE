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

import com.android.builder.model.v2.ide.ProjectType.APPLICATION
import com.itsaky.androidide.tooling.api.IProject.Type
import com.itsaky.androidide.tooling.api.IProject.Type.Gradle
import com.itsaky.androidide.tooling.api.messages.VariantDataRequest
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData
import org.eclipse.lsp4j.jsonrpc.CompletableFutures
import java.io.File
import java.io.Serializable
import java.util.concurrent.*

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
  @JvmField val tasks: List<GradleTask>,
) : com.itsaky.androidide.tooling.api.IProject, Serializable {
  private val serialVersionUID = 1L

  private val gsonType: String = javaClass.name
  @JvmField val moduleProjects: MutableList<IdeGradleProject> = mutableListOf()

  override fun isProjectInitialized(): CompletableFuture<Boolean> {
    return CompletableFuture.completedFuture(true)
  }

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

  override fun getType(): CompletableFuture<Type> {
    return CompletableFuture.completedFuture(Gradle)
  }

  override fun getBuildDir(): CompletableFuture<File> {
    return CompletableFutures.computeAsync { this.buildDir }
  }

  override fun getBuildScript(): CompletableFuture<File> {
    return CompletableFutures.computeAsync { this.buildScript }
  }

  override fun getTasks(): CompletableFuture<MutableList<GradleTask>> {
    return CompletableFutures.computeAsync { this.tasks.toMutableList() }
  }

  override fun listModules(): CompletableFuture<MutableList<SimpleModuleData>> {
    return CompletableFutures.computeAsync {
      return@computeAsync this.moduleProjects
        .map {
          SimpleModuleData(
            name = it.name,
            path = it.projectPath,
            projectDir = it.projectDir,
            classPaths = if (it is ModuleProject) it.getClassPaths() else emptySet()
          )
        }
        .toMutableList()
    }
  }

  override fun getVariantData(request: VariantDataRequest): CompletableFuture<SimpleVariantData> {
    return findByPath(request.projectPath).thenApply { project ->
      if (project !is AndroidModule) {
        return@thenApply null
      }

      return@thenApply project.simpleVariants.firstOrNull { it.name == request.variantName }
    }
  }

  override fun findByPath(path: String): CompletableFuture<IdeGradleProject?> {
    return CompletableFutures.computeAsync {
      if (path == this.projectPath) {
        return@computeAsync this
      }

      return@computeAsync moduleProjects.firstOrNull { it.projectPath == path }
    }
  }

  override fun findAndroidModules(): CompletableFuture<List<AndroidModule>> {
    return CompletableFutures.computeAsync {
      moduleProjects.filterIsInstance(AndroidModule::class.java)
    }
  }

  override fun findFirstAndroidModule(): CompletableFuture<AndroidModule?> {
    return findAndroidModules().thenApply { it.firstOrNull() }
  }

  override fun findFirstAndroidAppModule(): CompletableFuture<AndroidModule> {
    if (this is AndroidModule) {
      return CompletableFuture.completedFuture(this)
    }

    return findAndroidModules().thenApply { modules ->
      val application =
        modules.stream().filter { it != null && it.projectType == APPLICATION }.findFirst()
      application.orElse(null)
    }
  }
}
