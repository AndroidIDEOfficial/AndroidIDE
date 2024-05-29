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

package com.itsaky.androidide.tooling.impl.internal.forwarding

import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.IGradleProject
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.models.AndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.BasicAndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.GradleTask
import com.itsaky.androidide.tooling.api.models.JavaContentRoot
import com.itsaky.androidide.tooling.api.models.JavaModuleDependency
import com.itsaky.androidide.tooling.api.models.ProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * @author Akash Yadav
 */
@Suppress("NewApi")
internal class ForwardingProject(var project: IGradleProject? = null) : IGradleProject,
  IAndroidProject, IJavaProject {

  private val androidProject: IAndroidProject?
    get() = this.project as? IAndroidProject?

  private val javaProject: IJavaProject?
    get() = this.project as? IJavaProject?


  override fun getContentRoots(): CompletableFuture<List<JavaContentRoot>> {
    return this.javaProject?.getContentRoots() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getDependencies(): CompletableFuture<List<JavaModuleDependency>> {
    return this.javaProject?.getDependencies() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getConfiguredVariant(): CompletableFuture<String> {
    return this.androidProject?.getConfiguredVariant() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException()
    )
  }

  override fun getVariants(): CompletableFuture<List<BasicAndroidVariantMetadata>> {
    return this.androidProject?.getVariants() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getVariant(param: StringParameter): CompletableFuture<AndroidVariantMetadata?> {
    return this.androidProject?.getVariant(param) ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getBootClasspaths(): CompletableFuture<Collection<File>> {
    return this.androidProject?.getBootClasspaths() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getLibraryMap(): CompletableFuture<Map<String, DefaultLibrary>> {
    return this.androidProject?.getLibraryMap() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getMainSourceSet(): CompletableFuture<DefaultSourceSetContainer?> {
    return this.androidProject?.getMainSourceSet() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getLintCheckJars(): CompletableFuture<List<File>> {
    return this.androidProject?.getLintCheckJars() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getClasspaths(): CompletableFuture<List<File>> {
    return this.javaProject?.getClasspaths() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getMetadata(): CompletableFuture<ProjectMetadata> {
    return this.project?.getMetadata() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }

  override fun getTasks(): CompletableFuture<List<GradleTask>> {
    return this.project?.getTasks() ?: CompletableFuture.failedFuture(
      UnsupportedOperationException())
  }
}