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

package com.itsaky.androidide.tooling.api.model.util

import com.android.builder.model.v2.CustomSourceDirectory
import com.android.builder.model.v2.ModelSyncFile
import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags
import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags.BooleanFlag
import com.android.builder.model.v2.ide.AndroidLibraryData
import com.android.builder.model.v2.ide.ApiVersion
import com.android.builder.model.v2.ide.ArtifactDependencies
import com.android.builder.model.v2.ide.BundleInfo
import com.android.builder.model.v2.ide.GraphItem
import com.android.builder.model.v2.ide.JavaArtifact
import com.android.builder.model.v2.ide.JavaCompileOptions
import com.android.builder.model.v2.ide.Library
import com.android.builder.model.v2.ide.LibraryInfo
import com.android.builder.model.v2.ide.ProjectInfo
import com.android.builder.model.v2.ide.SourceProvider
import com.android.builder.model.v2.ide.SourceSetContainer
import com.android.builder.model.v2.ide.SyncIssue
import com.android.builder.model.v2.ide.TestInfo
import com.android.builder.model.v2.ide.TestedTargetVariant
import com.android.builder.model.v2.ide.UnresolvedDependency
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.ide.ViewBindingOptions
import com.android.builder.model.v2.models.ProjectSyncIssues
import com.android.builder.model.v2.models.VariantDependencies
import com.itsaky.androidide.builder.model.DefaultAndroidArtifact
import com.itsaky.androidide.builder.model.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.builder.model.DefaultAndroidLibraryData
import com.itsaky.androidide.builder.model.DefaultApiVersion
import com.itsaky.androidide.builder.model.DefaultArtifactDependencies
import com.itsaky.androidide.builder.model.DefaultBundleInfo
import com.itsaky.androidide.builder.model.DefaultCustomSourceDirectory
import com.itsaky.androidide.builder.model.DefaultGraphItem
import com.itsaky.androidide.builder.model.DefaultJavaArtifact
import com.itsaky.androidide.builder.model.DefaultJavaCompileOptions
import com.itsaky.androidide.builder.model.DefaultLibrary
import com.itsaky.androidide.builder.model.DefaultLibraryInfo
import com.itsaky.androidide.builder.model.DefaultModelSyncFile
import com.itsaky.androidide.builder.model.DefaultProjectInfo
import com.itsaky.androidide.builder.model.DefaultProjectSyncIssues
import com.itsaky.androidide.builder.model.DefaultSourceProvider
import com.itsaky.androidide.builder.model.DefaultSourceSetContainer
import com.itsaky.androidide.builder.model.DefaultSyncIssue
import com.itsaky.androidide.builder.model.DefaultTestInfo
import com.itsaky.androidide.builder.model.DefaultTestedTargetVariant
import com.itsaky.androidide.builder.model.DefaultUnresolvedDependency
import com.itsaky.androidide.builder.model.DefaultVariant
import com.itsaky.androidide.builder.model.DefaultVariantDependencies
import com.itsaky.androidide.builder.model.DefaultViewBindingOptions
import com.itsaky.androidide.tooling.api.messages.result.SimpleArtifact
import com.itsaky.androidide.tooling.api.messages.result.SimpleVariantData
import com.itsaky.androidide.tooling.api.model.AndroidModule
import com.itsaky.androidide.utils.ILogger

/**
 * As the data is sent over streams, and the instances of properties specified in [AndroidModule]
 * are just proxy classes, we need to make copy of those properties so that they can ben serialized
 * by Gson.
 *
 * This class handles the work of making copy of those properties.
 *
 * @author Akash Yadav
 */
object AndroidModulePropertyCopier {

  private val log = ILogger.newInstance(javaClass.simpleName)

  fun copy(module: AndroidModule): AndroidModule {
    @Suppress("DEPRECATION")
    return AndroidModule(
      module.name,
      module.projectPath,
      module.description,
      module.projectDir,
      module.buildDir,
      module.buildScript,
      module.parent,
      module.tasks,
      module.projectType,
      module.dynamicFeatures,
      copy(module.flags),
      copy(module.javaCompileOptions),
      module.resourcePrefix,
      copy(module.variants),
      copy(module.viewBindingOptions),
      module.lintChecksJars,
      copy(module.modelSyncFiles)
    )
  }

  fun copy(viewBindingOptions: ViewBindingOptions?): DefaultViewBindingOptions? {
    return when (viewBindingOptions) {
      null -> null
      else -> DefaultViewBindingOptions().apply { isEnabled = viewBindingOptions.isEnabled }
    }
  }

  @JvmName("copyVariants")
  fun copy(variants: Collection<Variant>): Collection<DefaultVariant> {
    val new = mutableListOf<DefaultVariant>()
    for (variant in variants) {
      new.add(copy(variant))
    }
    return new
  }

  fun copy(variant: Variant): DefaultVariant =
    DefaultVariant().apply {
      androidTestArtifact = copy(variant.androidTestArtifact)
      displayName = variant.displayName
      isInstantAppCompatible = variant.isInstantAppCompatible
      mainArtifact = copy(variant.mainArtifact)!!
      name = variant.name
      testFixturesArtifact = copy(variant.testFixturesArtifact)
      testedTargetVariant = copy(variant.testedTargetVariant)
      unitTestArtifact = copy(variant.unitTestArtifact)
      desugaredMethods = emptyList()
    }
  fun copy(artifact: JavaArtifact?): DefaultJavaArtifact? {
    if (artifact == null) {
      return null
    }

    return DefaultJavaArtifact().apply {
      modelSyncFiles = emptyList()
      assembleTaskName = artifact.assembleTaskName
      classesFolders = artifact.classesFolders
      compileTaskName = artifact.compileTaskName
      generatedSourceFolders = artifact.generatedSourceFolders
      ideSetupTaskNames = artifact.ideSetupTaskNames
      mockablePlatformJar = artifact.mockablePlatformJar
      runtimeResourceFolder = artifact.runtimeResourceFolder
    }
  }

  fun copy(variant: TestedTargetVariant?): DefaultTestedTargetVariant? {
    if (variant == null) {
      return null
    }

    return DefaultTestedTargetVariant().apply {
      targetProjectPath = variant.targetProjectPath
      targetVariant = variant.targetVariant
    }
  }

  fun copy(artifact: AndroidArtifact?): DefaultAndroidArtifact? {
    if (artifact == null) {
      return null
    }

    return DefaultAndroidArtifact().apply {
      resGenTaskName = artifact.resGenTaskName
      abiFilters = artifact.abiFilters
      assembleTaskOutputListingFile = artifact.assembleTaskOutputListingFile
      bundleInfo = copy(artifact.bundleInfo)
      codeShrinker = artifact.codeShrinker
      generatedResourceFolders = artifact.generatedResourceFolders
      isSigned = artifact.isSigned
      maxSdkVersion = artifact.maxSdkVersion
      minSdkVersion = copy(artifact.minSdkVersion)!!
      signingConfigName = artifact.signingConfigName
      sourceGenTaskName = artifact.sourceGenTaskName
      testInfo = copy(artifact.testInfo)
      assembleTaskName = artifact.assembleTaskName
      classesFolders = artifact.classesFolders
      compileTaskName = artifact.compileTaskName
      generatedSourceFolders = artifact.generatedSourceFolders
      ideSetupTaskNames = artifact.ideSetupTaskNames
      targetSdkVersionOverride = copy(artifact.targetSdkVersionOverride)
      modelSyncFiles = copy(artifact.modelSyncFiles)
    }
  }

  @JvmName("copyModelSyncFiles")
  fun copy(modelSyncFiles: Collection<ModelSyncFile>): List<DefaultModelSyncFile> {
    val new = mutableListOf<DefaultModelSyncFile>()
    for (file in modelSyncFiles) {
      new.add(copy(file))
    }

    return new
  }

  fun copy(file: ModelSyncFile): DefaultModelSyncFile =
    DefaultModelSyncFile().apply {
      modelSyncType = file.modelSyncType
      syncFile = file.syncFile
      taskName = file.taskName
    }

  fun copy(info: TestInfo?): DefaultTestInfo? {
    if (info == null) {
      return null
    }

    return DefaultTestInfo().apply {
      additionalRuntimeApks = info.additionalRuntimeApks
      animationsDisabled = info.animationsDisabled
      execution = info.execution
      instrumentedTestTaskName = info.instrumentedTestTaskName
    }
  }

  fun copy(version: ApiVersion?): DefaultApiVersion? =
    if (version == null) null
    else
      DefaultApiVersion().apply {
        apiLevel = version.apiLevel
        codename = version.codename
      }

  fun copy(bundleInfo: BundleInfo?): DefaultBundleInfo? {
    if (bundleInfo == null) {
      return null
    }

    return DefaultBundleInfo().apply {
      apkFromBundleTaskName = bundleInfo.apkFromBundleTaskName
      apkFromBundleTaskOutputListingFile = bundleInfo.apkFromBundleTaskOutputListingFile
      bundleTaskName = bundleInfo.bundleTaskName
      bundleTaskOutputListingFile = bundleInfo.bundleTaskOutputListingFile
    }
  }

  fun copy(javaCompileOptions: JavaCompileOptions): DefaultJavaCompileOptions {
    return DefaultJavaCompileOptions().apply {
      encoding = javaCompileOptions.encoding
      isCoreLibraryDesugaringEnabled = javaCompileOptions.isCoreLibraryDesugaringEnabled
      sourceCompatibility = javaCompileOptions.sourceCompatibility
      targetCompatibility = javaCompileOptions.targetCompatibility
    }
  }

  fun copy(
    @Suppress("UNUSED_PARAMETER") flags: AndroidGradlePluginProjectFlags
  ): DefaultAndroidGradlePluginProjectFlags {
    val flagMap: MutableMap<BooleanFlag, Boolean?> = mutableMapOf()
    //        log.debug("Flags:", flags)
    //
    //        flagMap[APPLICATION_R_CLASS_CONSTANT_IDS] =
    //            flags.getFlagValue(APPLICATION_R_CLASS_CONSTANT_IDS.name)
    //        flagMap[JETPACK_COMPOSE] = flags.getFlagValue(JETPACK_COMPOSE.name)
    //        flagMap[ML_MODEL_BINDING] = flags.getFlagValue(ML_MODEL_BINDING.name)
    //        flagMap[TEST_R_CLASS_CONSTANT_IDS] =
    // flags.getFlagValue(TEST_R_CLASS_CONSTANT_IDS.name)
    //        flagMap[TRANSITIVE_R_CLASS] = flags.getFlagValue(TRANSITIVE_R_CLASS.name)
    //        flagMap[UNIFIED_TEST_PLATFORM] = flags.getFlagValue(UNIFIED_TEST_PLATFORM.name)
    //
    //        log.debug("Flag map:", flagMap)
    return DefaultAndroidGradlePluginProjectFlags(flagMap)
  }

  fun copy(containers: Collection<SourceSetContainer>): Collection<DefaultSourceSetContainer> {
    val new = mutableListOf<DefaultSourceSetContainer>()

    for (container in containers) {
      new.add(copy(container))
    }

    return new
  }

  fun copy(container: SourceSetContainer): DefaultSourceSetContainer {
    return DefaultSourceSetContainer().apply {
      androidTestSourceProvider = copy(container.androidTestSourceProvider)
      sourceProvider = copy(container.sourceProvider)!!
      testFixturesSourceProvider = copy(container.testFixturesSourceProvider)
      unitTestSourceProvider = copy(container.unitTestSourceProvider)
    }
  }

  fun copy(provider: SourceProvider?): DefaultSourceProvider? {
    if (provider == null) {
      return null
    }

    return DefaultSourceProvider().apply {
      aidlDirectories = provider.aidlDirectories
      assetsDirectories = provider.assetsDirectories
      customDirectories = copy(provider.customDirectories)
      javaDirectories = provider.javaDirectories
      jniLibsDirectories = provider.jniLibsDirectories
      kotlinDirectories = provider.kotlinDirectories
      manifestFile = provider.manifestFile
      mlModelsDirectories = provider.mlModelsDirectories
      name = provider.name
      renderscriptDirectories = provider.renderscriptDirectories
      resDirectories = provider.resDirectories
      resourcesDirectories = provider.resourcesDirectories
      shadersDirectories = provider.shadersDirectories
    }
  }

  @JvmName("copyCustomSourceDirectories")
  private fun copy(
    directories: Collection<CustomSourceDirectory>?
  ): Collection<DefaultCustomSourceDirectory>? {
    return directories?.map { copy(it) }
  }

  private fun copy(it: CustomSourceDirectory): DefaultCustomSourceDirectory {
    return DefaultCustomSourceDirectory(it.directory, it.sourceTypeName)
  }

  fun copy(variantDependencies: VariantDependencies): DefaultVariantDependencies {
    return DefaultVariantDependencies().apply {
      this.name = variantDependencies.name
      this.libraries = copy(variantDependencies.libraries)
      this.mainArtifact = copy(variantDependencies.mainArtifact)!!
      this.androidTestArtifact = copy(variantDependencies.androidTestArtifact)
      this.testFixturesArtifact = copy(variantDependencies.testFixturesArtifact)
      this.unitTestArtifact = copy(variantDependencies.unitTestArtifact)
    }
  }

  private fun copy(libraries: Map<String, Library>): Map<String, DefaultLibrary> {
    val new = mutableMapOf<String, DefaultLibrary>()
    for ((key, value) in libraries) {
      new[key] = copy(value)
    }

    return new
  }

  fun copy(value: Library): DefaultLibrary {
    return DefaultLibrary().apply {
      this.androidLibraryData = copy(value.androidLibraryData)
      this.artifact = value.artifact
      this.key = value.key
      this.libraryInfo = copy(value.libraryInfo)
      this.lintJar = value.lintJar
      this.projectInfo = copy(value.projectInfo)
      this.type = value.type
    }
  }

  private fun copy(info: ProjectInfo?): DefaultProjectInfo? {
    return if (info == null) null
    else
      DefaultProjectInfo(
        info.attributes,
        info.buildType,
        info.capabilities,
        info.isTestFixtures,
        info.productFlavors,
        info.buildId,
        info.projectPath
      )
  }

  private fun copy(info: LibraryInfo?): DefaultLibraryInfo? {
    return if (info == null) null
    else
      DefaultLibraryInfo(
        info.attributes,
        info.buildType,
        info.capabilities,
        info.isTestFixtures,
        info.productFlavors,
        info.group,
        info.name,
        info.version
      )
  }

  private fun copy(data: AndroidLibraryData?): DefaultAndroidLibraryData? {
    return if (data == null) null
    else
      DefaultAndroidLibraryData(
        data.aidlFolder,
        data.assetsFolder,
        data.compileJarFiles,
        data.externalAnnotations,
        data.jniFolder,
        data.manifest,
        data.proguardRules,
        data.publicResources,
        data.renderscriptFolder,
        data.resFolder,
        data.resStaticLibrary,
        data.runtimeJarFiles,
        data.symbolFile
      )
  }

  fun copy(artifact: ArtifactDependencies?): DefaultArtifactDependencies? =
    DefaultArtifactDependencies().apply {
      if (artifact == null) {
        return null
      }

      this.compileDependencies = copy(artifact.compileDependencies)
      this.runtimeDependencies = copy(artifact.runtimeDependencies)
      this.unresolvedDependencies = copy(artifact.unresolvedDependencies)
    }

  private fun copy(dependencies: List<UnresolvedDependency>): List<DefaultUnresolvedDependency> {
    return dependencies.map { copy(it) }
  }

  private fun copy(it: UnresolvedDependency): DefaultUnresolvedDependency {
    return DefaultUnresolvedDependency(it.cause, it.name)
  }

  @JvmName("copyGraphItems")
  fun copy(graphs: List<GraphItem>): List<DefaultGraphItem> {
    return graphs.map { copy(it) }
  }

  fun copy(graph: GraphItem): DefaultGraphItem =
    DefaultGraphItem().apply {
      this.key = graph.key
      this.dependencies = copy(graph.dependencies)
      this.requestedCoordinates = graph.requestedCoordinates
    }

  fun copy(issues: ProjectSyncIssues): DefaultProjectSyncIssues =
    DefaultProjectSyncIssues(copy(issues.syncIssues))

  @JvmName("copySyncIssue")
  fun copy(syncIssues: Collection<SyncIssue>): Collection<DefaultSyncIssue> {
    return syncIssues.map { copy(it) }
  }

  fun copy(issue: SyncIssue) =
    DefaultSyncIssue(issue.data, issue.message, issue.multiLineMessage, issue.severity, issue.type)

  fun asSimpleVariants(variants: MutableCollection<Variant>): MutableList<SimpleVariantData> {
    return mutableListOf<SimpleVariantData>().apply {
      for (variant in variants) {
        add(
          SimpleVariantData(
            variant.name,
            asSimpleArtifact("main", variant.mainArtifact),
            mutableMapOf()
          )
        )
      }
    }
  }

  @Suppress("SameParameterValue") // We might need details about other artifacts in future
  private fun asSimpleArtifact(name: String, artifact: AndroidArtifact): SimpleArtifact {
    return SimpleArtifact(
      name = name,
      assembleTaskName = artifact.assembleTaskName,
      assembleTaskOutputListingFile = artifact.assembleTaskOutputListingFile,
      classJars = artifact.classesFolders.filter { it.name.endsWith(".jar") },
      compileTaskName = artifact.compileTaskName,
      generatedResourceFolders = artifact.generatedResourceFolders,
      generatedSourceFolders = artifact.generatedSourceFolders,
      maxSdkVersion = artifact.maxSdkVersion,
      minSdkVersion = artifact.minSdkVersion.apiLevel,
      resGenTaskName = artifact.resGenTaskName,
      signingConfigName = artifact.signingConfigName,
      sourceGenTaskName = artifact.sourceGenTaskName,
      targetSdkVersionOverride = artifact.targetSdkVersionOverride?.apiLevel ?: -1
    )
  }
}
