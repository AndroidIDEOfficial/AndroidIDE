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

import com.itsaky.androidide.tooling.api.IGradleProject
import com.itsaky.androidide.tooling.api.ProjectType
import com.itsaky.androidide.tooling.api.models.GradleTask
import com.itsaky.androidide.tooling.api.models.ProjectMetadata
import org.gradle.tooling.model.GradleProject
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * @author Akash Yadav
 */
internal open class GradleProjectImpl (
  protected val gradleProject: GradleProject
) : IGradleProject, Serializable {

  private val serialVersionUID = 1L

  override fun getMetadata(): CompletableFuture<ProjectMetadata> {
    return CompletableFuture.supplyAsync {
      ProjectMetadata(
        gradleProject.name,
        gradleProject.path,
        gradleProject.projectDirectory,
        gradleProject.buildDirectory,
        gradleProject.description,
        gradleProject.buildScript.sourceFile,
        ProjectType.Gradle
      )
    }
  }

  override fun getTasks(): CompletableFuture<List<GradleTask>> {
    return CompletableFuture.supplyAsync {
      mutableListOf<GradleTask>().apply {
        for (task in gradleProject.tasks) {
          add(GradleTask(
            task.name,
            task.description,
            task.group,
            task.path,
            task.displayName,
            task.isPublic,
            task.project.path
          ))
        }
      }
    }
  }
}