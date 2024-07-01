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

package com.itsaky.androidide.projects.internal

import com.android.builder.model.v2.models.ProjectSyncIssues
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.itsaky.androidide.projects.GradleProject
import com.itsaky.androidide.projects.IWorkspace
import com.itsaky.androidide.projects.ModuleProject
import com.itsaky.androidide.projects.android.AndroidModule
import com.itsaky.androidide.tooling.api.models.BuildVariantInfo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Model for representing the whole project that is opened in the IDE (including the root project).
 *
 * @property rootProject The root Gradle project.
 * @property subProjects List of all project that are included the project.
 * @property projectSyncIssues The issues that occurred while syncing the project.
 * @author Akash Yadav
 */
internal class WorkspaceImpl(
  private val projectDir: File,
  private val rootProject: GradleProject,
  private val subProjects: List<GradleProject>,
  private val projectSyncIssues: ProjectSyncIssues
) : IWorkspace {

  private val variantSelections = mutableMapOf<String, BuildVariantInfo>()

  internal fun setVariantSelections(selections: Map<String, BuildVariantInfo>) {
    this.variantSelections.apply {
      clear()
      putAll(selections)
    }
  }

  override fun getProjectDir(): File {
    return this.projectDir
  }

  override fun getRootProject(): GradleProject {
    return this.rootProject
  }

  override fun getSubProjects(): List<GradleProject> {
    return ImmutableList.copyOf(this.subProjects)
  }

  override fun getProjectSyncIssues(): ProjectSyncIssues {
    return this.projectSyncIssues
  }

  override fun getAndroidVariantSelections(): Map<String, BuildVariantInfo> {
    return ImmutableMap.copyOf(this.variantSelections)
  }

  override fun findProject(path: String): GradleProject? {
    return this.subProjects.find { it.path == path }
  }

  override fun androidProjects(): Sequence<AndroidModule> {
    return subProjects.asSequence().filterIsInstance<AndroidModule>()
  }

  override fun findModuleForFile(file: Path, checkExistance: Boolean): ModuleProject? {
    return findModuleForFile(file.toFile(), checkExistance)
  }

  override fun findModuleForFile(file: File, checkExistance: Boolean): ModuleProject? {

    if (!file.exists() && checkExistance) {
      return null
    }

    val path = file.canonicalPath
    var longestPath = ""
    var moduleWithLongestPath: ModuleProject? = null

    for (module in subProjects) {
      if (module !is ModuleProject) {
        continue
      }

      val moduleDir = module.projectDir.canonicalPath
      if (path.startsWith(moduleDir) && longestPath.length < moduleDir.length) {
        longestPath = moduleDir
        moduleWithLongestPath = module
      }
    }

    if (longestPath.isEmpty() || moduleWithLongestPath == null) {
      return null
    }

    return moduleWithLongestPath
  }

  override fun containsSourceFile(file: Path): Boolean {
    if (!Files.exists(file)) {
      return false
    }

    for (module in subProjects) {
      if (module !is ModuleProject) {
        continue
      }

      val source = module.compileJavaSourceClasses.findSource(file)
      if (source != null) {
        return true
      }
    }

    return false
  }

  override fun isAndroidResource(file: File): Boolean {
    val module = findModuleForFile(file, true) ?: return false
    if (module is AndroidModule) {
      return module.getResourceDirectories().find { file.path.startsWith(it.path) } != null
    }
    return true
  }
}