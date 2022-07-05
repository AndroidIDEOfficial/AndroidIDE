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

package com.itsaky.androidide.tooling.impl.model

import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.IProject.Type
import com.itsaky.androidide.tooling.api.IProject.Type.Unknown
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.messages.VariantDataRequest
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData
import com.itsaky.androidide.tooling.api.model.AndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.GradleTask
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.util.concurrent.*

/**
 * An [IProject] implementation which forwards all calls to another [IProject].
 *
 * We provide [IProject] as a service to the [IToolingApiClient]. As there is no real project
 * instance available when the server starts, we use this implementation. When the project is
 * initialized, the Tooling API server updates the project instance here. Once the project instance
 * is available, the client can fetch real information about the project.
 *
 * @author Akash Yadav
 */
class InternalForwardingProject(
  var project: IProject?,
  var projectPath: String = IProject.FILE_PATH_NOT_AVAILABLE
) : IProject {

  private val log = ILogger.newInstance(javaClass.simpleName)

  override fun isProjectInitialized(): CompletableFuture<Boolean> {
    return CompletableFuture.completedFuture(this.project != null)
  }

  override fun getName(): CompletableFuture<String> =
    if (this.project != null) this.project!!.name else CompletableFuture.completedFuture("")

  override fun getDescription(): CompletableFuture<String> =
    if (this.project != null) this.project!!.description else CompletableFuture.completedFuture("")

  override fun getProjectPath(): CompletableFuture<String> =
    if (this.project != null) this.project!!.projectPath else CompletableFuture.completedFuture("")

  override fun getProjectDir(): CompletableFuture<File> =
    if (this.project != null) this.project!!.projectDir
    else CompletableFuture.completedFuture(File(projectPath))

  override fun getType(): CompletableFuture<Type> =
    if (this.project != null) this.project!!.type else CompletableFuture.completedFuture(Unknown)

  override fun getBuildDir(): CompletableFuture<File> =
    if (this.project != null) this.project!!.buildDir
    else CompletableFuture.completedFuture(File(projectPath))

  override fun getBuildScript(): CompletableFuture<File> =
    if (this.project != null) this.project!!.buildScript
    else CompletableFuture.completedFuture(File(projectPath))

  override fun getTasks(): CompletableFuture<MutableList<GradleTask>> =
    if (this.project != null) this.project!!.tasks
    else CompletableFuture.completedFuture(mutableListOf())

  override fun listModules(): CompletableFuture<MutableList<SimpleModuleData>> =
    if (this.project != null) project!!.listModules()
    else CompletableFuture.completedFuture(mutableListOf())

  override fun findByPath(path: String): CompletableFuture<IdeGradleProject?> =
    if (this.project != null) this.project!!.findByPath(path)
    else CompletableFuture.completedFuture(null)

  override fun findAndroidModules(): CompletableFuture<MutableList<AndroidModule>> =
    if (this.project != null) this.project!!.findAndroidModules()
    else CompletableFuture.completedFuture(mutableListOf())

  override fun findFirstAndroidModule(): CompletableFuture<AndroidModule?> =
    if (this.project != null) this.project!!.findFirstAndroidModule()
    else CompletableFuture.completedFuture(null)

  override fun findFirstAndroidAppModule(): CompletableFuture<AndroidModule> =
    if (this.project != null) this.project!!.findFirstAndroidAppModule()
    else CompletableFuture.completedFuture(null)

  override fun getVariantData(request: VariantDataRequest): CompletableFuture<SimpleVariantData> =
    if (this.project != null) this.project!!.getVariantData(request)
    else CompletableFuture.completedFuture(null)
}
