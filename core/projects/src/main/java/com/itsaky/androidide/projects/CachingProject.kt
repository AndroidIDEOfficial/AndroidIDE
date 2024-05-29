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

import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY
import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IGradleProject
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.BasicProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.tooling.api.models.result.SelectProjectResult
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture

/**
 * A project which lazily caches some required properties of the given project.
 *
 * @author Akash Yadav
 */
@RestrictTo(LIBRARY)
open class CachingProject(val project: IProject) : IProject {

  private val projects = mutableListOf<BasicProjectMetadata>()
  private var syncIssues: DefaultProjectSyncIssues? = null

  companion object {

    private val log = LoggerFactory.getLogger(CachingProject::class.java)
  }

  override fun getProjects(): CompletableFuture<List<BasicProjectMetadata>> {
    return if (this.projects.isNotEmpty()) {
      log.info("Using cached project metadata...")
      CompletableFuture.completedFuture(this.projects)
    } else this.project.getProjects().whenComplete { projects, err ->
      if (err != null || projects == null) {
        log.debug("Unable to fetch project metadata from tooling server", err)
        return@whenComplete
      }

      if (projects.isEmpty()) {
        log.debug("Empty project metadata returned by tooling server. Ignoring...")
        return@whenComplete
      }

      this.projects.clear()
      this.projects.addAll(projects)
    }
  }

  override fun getProjectSyncIssues(): CompletableFuture<DefaultProjectSyncIssues> {
    this.syncIssues?.also {
      return CompletableFuture.completedFuture(it)
    }

    return this.project.getProjectSyncIssues().whenComplete { projectSyncIssues, err ->
      if (err != null || projectSyncIssues == null) {
        log.debug("Unable to fetch project sync issues from tooling server", err)
        return@whenComplete
      }

      if (projectSyncIssues.syncIssues.isEmpty()) {
        log.debug("No sync issues.")
        return@whenComplete
      }

      this.syncIssues = projectSyncIssues
    }
  }

  override fun selectProject(param: StringParameter): CompletableFuture<SelectProjectResult> {
    return project.selectProject(param)
  }

  override fun getType(): CompletableFuture<ProjectType> {
    return project.getType()
  }

  override fun asGradleProject(): IGradleProject {
    return project.asGradleProject()
  }

  override fun asAndroidProject(): IAndroidProject {
    return project.asAndroidProject()
  }

  override fun asJavaProject(): IJavaProject {
    return project.asJavaProject()
  }
}