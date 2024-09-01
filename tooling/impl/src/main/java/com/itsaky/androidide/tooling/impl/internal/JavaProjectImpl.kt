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

import com.itsaky.androidide.builder.model.IJavaCompilerSettings
import com.itsaky.androidide.tooling.api.IJavaProject
import com.itsaky.androidide.tooling.api.models.GradleArtifact
import com.itsaky.androidide.tooling.api.models.JavaContentRoot
import com.itsaky.androidide.tooling.api.models.JavaModuleDependency
import com.itsaky.androidide.tooling.api.models.JavaModuleExternalDependency
import com.itsaky.androidide.tooling.api.models.JavaModuleProjectDependency
import com.itsaky.androidide.tooling.api.models.JavaProjectMetadata
import com.itsaky.androidide.tooling.api.models.JavaSourceDirectory
import com.itsaky.androidide.tooling.api.models.ProjectMetadata
import org.gradle.tooling.model.idea.IdeaModule
import org.gradle.tooling.model.idea.IdeaModuleDependency
import org.gradle.tooling.model.idea.IdeaSingleEntryLibraryDependency
import java.io.File
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * @author Akash Yadav
 */
internal class JavaProjectImpl(
  private val ideaModule: IdeaModule,
  private val compilerSettings: IJavaCompilerSettings,
  private var allModulePaths: Map<String, String> = emptyMap()
) : GradleProjectImpl(ideaModule.gradleProject),
  IJavaProject, Serializable {

  private val serialVersionUID = 1L

  override fun getContentRoots(): CompletableFuture<List<JavaContentRoot>> {
    return CompletableFuture.supplyAsync {
      val list = ArrayList<JavaContentRoot>()
      for (contentRoot in ideaModule.contentRoots) {
        val thisRoot = JavaContentRoot()
        for (sourceDir in contentRoot!!.sourceDirectories) {
          (thisRoot.sourceDirectories as MutableList).add(
            JavaSourceDirectory(sourceDir!!.directory, sourceDir.isGenerated))
        }
        for (testDir in contentRoot.testDirectories) {
          (thisRoot.testDirectories as MutableList).add(
            JavaSourceDirectory(testDir!!.directory, testDir.isGenerated))
        }
        list.add(thisRoot)
      }

      return@supplyAsync list
    }
  }

  override fun getDependencies(): CompletableFuture<List<JavaModuleDependency>> {
    return CompletableFuture.supplyAsync {
      val list = ArrayList<JavaModuleDependency>()
      for (dependency in ideaModule.dependencies) {
        // TODO There might be unresolved dependencies here. We need to handle them too.
        if (dependency is IdeaSingleEntryLibraryDependency) {
          val file = dependency.file
          val source = dependency.source
          val javadoc = dependency.javadoc
          val artifact = getGradleArtifact(dependency)
          list.add(
            JavaModuleExternalDependency(
              file,
              source,
              javadoc,
              artifact,
              dependency.getScope().scope,
              dependency.getExported()))
        } else if (dependency is IdeaModuleDependency) {
          val moduleName = dependency.targetModuleName
          list.add(
            JavaModuleProjectDependency(
              moduleName,
              allModulePaths[moduleName] ?: "",
              dependency.scope.scope,
              dependency.exported))
        }
      }

      return@supplyAsync list
    }
  }

  private fun getGradleArtifact(external: IdeaSingleEntryLibraryDependency): GradleArtifact? {
    val moduleVersion = external.gradleModuleVersion ?: return null
    return GradleArtifact(
      moduleVersion.group, moduleVersion.name, moduleVersion.version)
  }

  private fun getClassesJar(): File {
    return getClassesJar(getMetadata().get())
  }

  private fun getClassesJar(metadata: ProjectMetadata): File {
    var jar = File(metadata.buildDir, "libs/${metadata.name}.jar")
    if (jar.exists()) {
      return jar
    }

    jar =
      File(metadata.buildDir, "libs").listFiles()?.firstOrNull { metadata.name?.let(it.name::startsWith) ?: false }
        ?: File("module-jar-does-not-exist.jar")

    return jar
  }

  override fun getClasspaths(): CompletableFuture<List<File>> {
    return CompletableFuture.supplyAsync {
      getDependencies().get().mapNotNull { it.jarFile }.toMutableList().apply { add(getClassesJar()) }
    }
  }

  override fun getMetadata(): CompletableFuture<ProjectMetadata> {
    return CompletableFuture.supplyAsync {
      val base = super.getMetadata().get()

      // do not call getClassesJar() here
      // it'll try to fetch metadata which will in return call this method
      // this will result in an infinite loop
      return@supplyAsync JavaProjectMetadata(base, compilerSettings, getClassesJar(base))
    }
  }
}