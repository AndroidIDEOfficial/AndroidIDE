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

package com.itsaky.androidide.tooling.impl.internal

import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IGradleProject
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.IProject
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.BasicProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.tooling.api.models.result.SelectProjectResult
import com.itsaky.androidide.tooling.impl.internal.forwarding.ForwardingProject
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * @author Akash Yadav
 */
internal class ProjectImpl(
  var rootProject: IGradleProject? = null,
  var rootProjectPath: String? = null,
  var projects: List<IGradleProject> = emptyList(),
  var projectSyncIssues: DefaultProjectSyncIssues = DefaultProjectSyncIssues(emptyList())
) : IProject, Serializable {

  private val serialVersionUID = 1L

  @Transient
  private var _lock: Any? = null
  private val lock: Any
    get() = _lock ?: Any().also { _lock = it }

  @Transient
  private val selectedProject: ForwardingProject

  init {
    require((rootProject == null) == (rootProjectPath == null)) {
      "rootProject, rootProjectPath: both must be specified or null"
    }
    this.selectedProject = ForwardingProject()
  }

  fun setFrom(other: ProjectImpl) {
    this.rootProject = other.rootProject
    this.rootProjectPath = other.rootProjectPath
    this.projects = other.projects
    this.projectSyncIssues = other.projectSyncIssues
  }

  private fun getProject(path: String): IGradleProject? {
    return if (path.isBlank()) rootProject else projects.find {
      it.getMetadata().get().projectPath == path
    }
  }

  override fun getProjects(): CompletableFuture<List<BasicProjectMetadata>> {
    return CompletableFuture.supplyAsync {
      projects.map { it.getMetadata().get() }
    }
  }

  override fun getProjectSyncIssues(): CompletableFuture<DefaultProjectSyncIssues> {
    return CompletableFuture.completedFuture(
      this.projectSyncIssues ?: DefaultProjectSyncIssues(emptyList())
    )
  }

  override fun selectProject(param: StringParameter): CompletableFuture<SelectProjectResult> {
    return CompletableFuture.supplyAsync {
      synchronized(lock) {
        this.selectedProject.project = getProject(param.value)
        SelectProjectResult(this.selectedProject.project != null)
      }
    }
  }

  override fun getType(): CompletableFuture<ProjectType> {
    return CompletableFuture.supplyAsync {
      synchronized(lock) {
        return@supplyAsync when (this.selectedProject.project) {
          is IAndroidProject -> ProjectType.Android
          is IJavaProject -> ProjectType.Java
          is IGradleProject -> ProjectType.Gradle
          else -> ProjectType.Unknown
        }
      }
    }
  }

  override fun asGradleProject(): IGradleProject {
    return this.selectedProject
  }

  override fun asAndroidProject(): IAndroidProject {
    return this.selectedProject
  }

  override fun asJavaProject(): IJavaProject {
    return this.selectedProject
  }
}