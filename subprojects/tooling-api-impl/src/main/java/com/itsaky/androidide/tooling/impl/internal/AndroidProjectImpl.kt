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

import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.GraphItem
import com.android.builder.model.v2.ide.Library
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.models.AndroidProject
import com.android.builder.model.v2.models.BasicAndroidProject
import com.android.builder.model.v2.models.VariantDependencies
import com.android.builder.model.v2.models.Versions
import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.DefaultModelSyncFile
import com.itsaky.androidide.builder.model.DefaultSourceProvider
import com.itsaky.androidide.builder.model.DefaultSourceSetContainer
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.builder.model.UNKNOWN_PACKAGE
import com.itsaky.androidide.tooling.api.IAndroidProject
import com.itsaky.androidide.tooling.api.models.AndroidArtifactMetadata
import com.itsaky.androidide.tooling.api.models.AndroidProjectMetadata
import com.itsaky.androidide.tooling.api.models.AndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.BasicAndroidVariantMetadata
import com.itsaky.androidide.tooling.api.models.ProjectMetadata
import com.itsaky.androidide.tooling.api.models.params.StringParameter
import com.itsaky.androidide.tooling.api.util.AndroidModulePropertyCopier
import com.itsaky.androidide.tooling.api.util.AndroidModulePropertyCopier.copy
import com.itsaky.androidide.tooling.api.util.compareSemanticVersions
import com.itsaky.androidide.tooling.api.util.extractPackageName
import com.itsaky.androidide.utils.AndroidPluginVersion
import org.gradle.tooling.model.GradleProject
import java.io.File
import java.io.Serializable
import java.util.concurrent.CompletableFuture

/**
 * @author Akash Yadav
 */
internal class AndroidProjectImpl(
  gradleProject: GradleProject,
  private val configuredVariant: String,
  private val basicAndroidProject: BasicAndroidProject,
  private val androidProject: AndroidProject,
  private val variantDependencies: VariantDependencies,
  private val versions: Versions
) : GradleProjectImpl(gradleProject), IAndroidProject, Serializable {

  private val serialVersionUID = 1L

  private var shouldLookupPackage = true
  var packageName: String = UNKNOWN_PACKAGE
    get() {
      if (field == UNKNOWN_PACKAGE && shouldLookupPackage) {
        findPackageName()
      }

      return field
    }

  override fun getConfiguredVariant(): CompletableFuture<String> {
    return CompletableFuture.completedFuture(this.configuredVariant)
  }

  override fun getVariants(): CompletableFuture<List<BasicAndroidVariantMetadata>> {
    return CompletableFuture.supplyAsync {
      androidProject.variants.map {
        BasicAndroidVariantMetadata(it.name, it.mainArtifact.toMetadata(it.name))
      }
    }
  }

  private fun AndroidArtifact.toMetadata(name: String): AndroidArtifactMetadata {
    return AndroidArtifactMetadata(
      name = name,
      resGenTaskName = resGenTaskName,
      assembleTaskOutputListingFile = assembleTaskOutputListingFile,
      generatedResourceFolders = generatedResourceFolders,
      generatedSourceFolders = generatedSourceFolders, maxSdkVersion = maxSdkVersion,
      minSdkVersion = minSdkVersion.apiLevel, signingConfigName = signingConfigName,
      sourceGenTaskName = sourceGenTaskName, assembleTaskName = assembleTaskName,
      classJars = classesFolders.filter { it.name.endsWith(".jar") },
      compileTaskName = compileTaskName,
      targetSdkVersionOverride = targetSdkVersionOverride?.apiLevel ?: -1
    )
  }

  override fun getVariant(param: StringParameter): CompletableFuture<AndroidVariantMetadata?> {
    return CompletableFuture.supplyAsync {
      androidProject.variants.find { it.name == param.value }?.toMetadata()
    }
  }

  private fun Variant.toMetadata(): AndroidVariantMetadata {
    return AndroidVariantMetadata(
      name = name,
      mainArtifact = mainArtifact.toMetadata(name),
      otherArtifacts = mutableMapOf()
    )
  }

  override fun getBootClasspaths(): CompletableFuture<Collection<File>> {
    return CompletableFuture.supplyAsync {
      basicAndroidProject.bootClasspath
    }
  }

  override fun getLibraryMap(): CompletableFuture<Map<String, DefaultLibrary>> {
    return CompletableFuture.supplyAsync {
      val seen = HashMap<String, DefaultLibrary>()
      val compileDependencies = variantDependencies.mainArtifact.compileDependencies
      val libraries = variantDependencies.libraries
      for (dependency in compileDependencies) {
        seen[dependency.key] ?: fillLibrary(dependency, libraries, seen)
      }
      seen
    }
  }

  private fun fillLibrary(
    item: GraphItem,
    libraries: Map<String, Library>,
    seen: HashMap<String, DefaultLibrary>
  ): DefaultLibrary? {

    val lib = libraries[item.key] ?: return null
    val library = copy(lib)

    for (dependency in item.dependencies) {
      val dep = fillLibrary(dependency, libraries, seen)!!
      library.dependencies.add(dep.key)
    }

    seen[item.key] = library

    return library
  }

  override fun getMainSourceSet(): CompletableFuture<DefaultSourceSetContainer?> {
    return CompletableFuture.supplyAsync {
      basicAndroidProject.mainSourceSet?.let(AndroidModulePropertyCopier::copy)
    }
  }

  override fun getLintCheckJars(): CompletableFuture<List<File>> {
    return CompletableFuture.supplyAsync { androidProject.lintChecksJars }
  }

  override fun getModelSyncFiles(): CompletableFuture<List<DefaultModelSyncFile>> {
    return CompletableFuture.supplyAsync {

      // model sync files available only in v7.3.0 and later
      return@supplyAsync if (AndroidPluginVersion.parse(versions.agp) >= AndroidPluginVersion(7, 3, 0))
        copy(androidProject.modelSyncFiles)
      else emptyList()
    }
  }

  private fun getClassesJar(): File {
    // TODO(itsaky): this should handle product flavors as well
    return File(gradleProject.buildDirectory,
      "${IAndroidProject.FD_INTERMEDIATES}/compile_library_classes_jar/$configuredVariant/classes.jar")
  }

  override fun getClasspaths(): CompletableFuture<List<File>> {
    return CompletableFuture.supplyAsync {
      mutableListOf<File>().apply {
        add(getClassesJar())
        getVariant(StringParameter(configuredVariant)).get()?.mainArtifact?.classJars?.let(
          this::addAll)
      }
    }
  }

  override fun getMetadata(): CompletableFuture<ProjectMetadata> {
    return CompletableFuture.supplyAsync {
      val gradleMetadata = super.getMetadata().get()

      val viewBindingOptions = androidProject.viewBindingOptions?.let(
        AndroidModulePropertyCopier::copy)
        ?: DefaultViewBindingOptions()

      return@supplyAsync AndroidProjectMetadata(
        gradleMetadata,
        packageName,
        basicAndroidProject.projectType,
        copy(androidProject.flags),
        copy(androidProject.javaCompileOptions),
        viewBindingOptions,
        androidProject.resourcePrefix,
        androidProject.namespace,
        androidProject.androidTestNamespace,
        androidProject.testFixturesNamespace,
        getClassesJar()
      )
    }
  }

  private fun findPackageName() {

    val namespace = androidProject.namespace
    if (namespace.isNotBlank()) {
      this.packageName = namespace
      this.shouldLookupPackage = false
      return
    }

    val mainSourceSet = basicAndroidProject.mainSourceSet
    if (mainSourceSet == null) {
      shouldLookupPackage = false
      return
    }

    val manifestFile = mainSourceSet.sourceProvider.manifestFile
    if (manifestFile == DefaultSourceProvider.NoFile) {
      shouldLookupPackage = false
      return
    }

    this.packageName = extractPackageName(manifestFile) ?: UNKNOWN_PACKAGE
    this.shouldLookupPackage = false
  }
}