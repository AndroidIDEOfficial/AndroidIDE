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

import com.itsaky.androidide.tooling.api.messages.FindProjectParams
import com.itsaky.androidide.tooling.api.model.IProject
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeGradleTask
import java.io.File
import java.util.concurrent.*

/** @author Akash Yadav */
class InternalForwardingProject(var project: IProject?) : IProject {
    override fun getName(): CompletableFuture<String> =
        this.project?.name ?: CompletableFuture.completedFuture("")
    override fun getDescription(): CompletableFuture<String> =
        this.project?.description ?: CompletableFuture.completedFuture("")
    override fun getProjectPath(): CompletableFuture<String> =
        this.project?.projectPath ?: CompletableFuture.completedFuture("")
    override fun getProjectDir(): CompletableFuture<File> =
        this.project?.projectDir ?: CompletableFuture.completedFuture(File("."))
    override fun getBuildDir(): CompletableFuture<File> =
        this.project?.buildDir ?: CompletableFuture.completedFuture(File("."))
    override fun getBuildScript(): CompletableFuture<File> =
        this.project?.buildScript ?: CompletableFuture.completedFuture(File("."))
    override fun getTasks(): CompletableFuture<MutableList<IdeGradleTask>> =
        this.project?.tasks ?: CompletableFuture.completedFuture(mutableListOf())
    override fun getModules(): CompletableFuture<MutableList<IdeGradleProject>> =
        this.project?.modules ?: CompletableFuture.completedFuture(mutableListOf())
    override fun findByPath(params: FindProjectParams): CompletableFuture<IdeGradleProject?> =
        this.project?.findByPath(params) ?: CompletableFuture.completedFuture(null)
    override fun findAndroidModules(): CompletableFuture<MutableList<IdeAndroidModule>> =
        this.project?.findAndroidModules() ?: CompletableFuture.completedFuture(mutableListOf())
    override fun findFirstAndroidModule(): CompletableFuture<IdeAndroidModule?> =
        this.project?.findFirstAndroidModule() ?: CompletableFuture.completedFuture(null)
}
