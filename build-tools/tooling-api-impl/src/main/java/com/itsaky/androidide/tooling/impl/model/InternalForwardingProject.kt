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

import com.itsaky.androidide.tooling.api.model.IProject
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeGradleTask
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.util.concurrent.*

/** @author Akash Yadav */
class InternalForwardingProject(var project: IProject?) : IProject {

    private val log = ILogger.newInstance(javaClass.simpleName)

    override fun getName(): CompletableFuture<String> {
        log.debug("getName", "this.project", this.project)
        return if (this.project != null) this.project!!.name else CompletableFuture.completedFuture("")
    }
    override fun getDescription(): CompletableFuture<String> =
        if (this.project != null) this.project!!.description
        else CompletableFuture.completedFuture("")
    override fun getProjectPath(): CompletableFuture<String> =
        if (this.project != null) this.project!!.projectPath
        else CompletableFuture.completedFuture("")
    override fun getProjectDir(): CompletableFuture<File> =
        if (this.project != null) this.project!!.projectDir
        else CompletableFuture.completedFuture(File("."))
    override fun getBuildDir(): CompletableFuture<File> =
        if (this.project != null) this.project!!.buildDir
        else CompletableFuture.completedFuture(File("."))
    override fun getBuildScript(): CompletableFuture<File> =
        if (this.project != null) this.project!!.buildScript
        else CompletableFuture.completedFuture(File("."))
    override fun getTasks(): CompletableFuture<MutableList<IdeGradleTask>> =
        if (this.project != null) this.project!!.tasks
        else CompletableFuture.completedFuture(mutableListOf())
    override fun getModules(): CompletableFuture<MutableList<IdeGradleProject>> =
        if (this.project != null) this.project!!.modules
        else CompletableFuture.completedFuture(mutableListOf())
    override fun findByPath(path: String): CompletableFuture<IdeGradleProject?> =
        if (this.project != null) this.project!!.findByPath(path)
        else CompletableFuture.completedFuture(null)
    override fun findAndroidModules(): CompletableFuture<MutableList<IdeAndroidModule>> =
        if (this.project != null) this.project!!.findAndroidModules()
        else CompletableFuture.completedFuture(mutableListOf())
    override fun findFirstAndroidModule(): CompletableFuture<IdeAndroidModule?> =
        if (this.project != null) this.project!!.findFirstAndroidModule()
        else CompletableFuture.completedFuture(null)
}
