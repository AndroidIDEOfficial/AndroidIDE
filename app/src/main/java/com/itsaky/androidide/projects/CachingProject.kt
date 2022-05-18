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

import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.messages.VariantDataRequest
import com.itsaky.androidide.tooling.api.messages.result.SimpleModuleData
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.IdeGradleProject
import com.itsaky.androidide.tooling.api.model.IdeGradleTask
import com.itsaky.androidide.utils.ILogger
import java.io.File
import java.util.concurrent.*

/**
 * A project which lazily caches some required properties of the given project.
 *
 * @author Akash Yadav
 */
class CachingProject(val project: IProject) : IProject {

    private val log = ILogger.newInstance(javaClass.simpleName)

    private val mName: String by lazy { this.project.name.get() }
    private val mDescription: String by lazy { this.project.description.get() }
    private val mProjectPath: String by lazy { this.project.projectPath.get() }
    private val mProjectDir: File by lazy { this.project.projectDir.get() }
    private val mBuildDir: File by lazy { this.project.buildDir.get() }
    private val mBuildScript: File by lazy { this.project.buildScript.get() }

    private var mFirstAppModule: IdeAndroidModule? = null
    private val mCachedVariants: MutableMap<VariantDataRequest, SimpleVariantData> = mutableMapOf()
    private val mModules: MutableList<SimpleModuleData> = mutableListOf()

    override fun isProjectInitialized(): CompletableFuture<Boolean> {
        return CompletableFuture.completedFuture(true)
    }

    override fun getName(): CompletableFuture<String> {
        return CompletableFuture.completedFuture(this.mName)
    }

    override fun getDescription(): CompletableFuture<String> {
        return CompletableFuture.completedFuture(mDescription)
    }

    override fun getProjectPath(): CompletableFuture<String> {
        return CompletableFuture.completedFuture(mProjectPath)
    }

    override fun getProjectDir(): CompletableFuture<File> {
        return CompletableFuture.completedFuture(mProjectDir)
    }

    override fun getBuildDir(): CompletableFuture<File> {
        return CompletableFuture.completedFuture(mBuildDir)
    }

    override fun getBuildScript(): CompletableFuture<File> {
        return CompletableFuture.completedFuture(mBuildScript)
    }

    override fun getTasks(): CompletableFuture<MutableList<IdeGradleTask>> {
        return this.project.tasks
    }

    override fun getModules(): CompletableFuture<MutableList<IdeGradleProject>> {
        return this.project.modules
    }

    override fun listModules(): CompletableFuture<MutableList<SimpleModuleData>> {
        return if (this.mModules.isNotEmpty()) {
            log.debug("Using cached module data...")
            CompletableFuture.completedFuture(mModules)
        } else {
            return this.project.listModules().whenComplete { modules, err ->
                if (err != null || modules == null) {
                    log.debug("Unable to fetch module data from tooling server", err)
                    return@whenComplete
                }

                if (modules.isEmpty()) {
                    log.debug("Empty module data returned by tooling server. Ignoring...")
                    return@whenComplete
                }

                mModules.clear()
                mModules.addAll(modules)
            }
        }
    }

    override fun getVariantData(request: VariantDataRequest): CompletableFuture<SimpleVariantData> {
        val cached = mCachedVariants[request]
        if (cached != null) {
            return CompletableFuture.completedFuture(cached)
        }

        return this.project.getVariantData(request).whenComplete { t, u ->
            if (t == null) {
                log.warn(
                    "Unable to get variant data for " +
                        "variant ${request.variantName} ('${request.projectPath}') from tooling server",
                    u)
                return@whenComplete
            }

            mCachedVariants[request] = t
        }
    }

    override fun findByPath(path: String): CompletableFuture<IdeGradleProject> {
        return this.project.findByPath(path)
    }

    override fun findAndroidModules(): CompletableFuture<MutableList<IdeAndroidModule>> {
        return this.project.findAndroidModules()
    }

    override fun findFirstAndroidModule(): CompletableFuture<IdeAndroidModule> {
        return this.project.findFirstAndroidModule()
    }

    override fun findFirstAndroidAppModule(): CompletableFuture<IdeAndroidModule> {
        if (mFirstAppModule != null) {
            return CompletableFuture.completedFuture(mFirstAppModule)
        }

        return this.project.findFirstAndroidAppModule().whenComplete { module, _ ->
            this.mFirstAppModule = module
        }
    }
}
