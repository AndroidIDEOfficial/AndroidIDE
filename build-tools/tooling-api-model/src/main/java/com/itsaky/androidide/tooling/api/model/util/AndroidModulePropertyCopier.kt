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

import com.android.builder.model.v2.ModelSyncFile
import com.android.builder.model.v2.ide.AndroidArtifact
import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags
import com.android.builder.model.v2.ide.ApiVersion
import com.android.builder.model.v2.ide.ArtifactDependencies
import com.android.builder.model.v2.ide.BundleInfo
import com.android.builder.model.v2.ide.GraphItem
import com.android.builder.model.v2.ide.JavaArtifact
import com.android.builder.model.v2.ide.JavaCompileOptions
import com.android.builder.model.v2.ide.SourceProvider
import com.android.builder.model.v2.ide.SourceSetContainer
import com.android.builder.model.v2.ide.TestInfo
import com.android.builder.model.v2.ide.TestedTargetVariant
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.ide.ViewBindingOptions
import com.android.builder.model.v2.models.VariantDependencies
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidArtifact
import com.itsaky.androidide.tooling.api.model.internal.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.tooling.api.model.internal.DefaultApiVersion
import com.itsaky.androidide.tooling.api.model.internal.DefaultArtifactDependencies
import com.itsaky.androidide.tooling.api.model.internal.DefaultBundleInfo
import com.itsaky.androidide.tooling.api.model.internal.DefaultGraphItem
import com.itsaky.androidide.tooling.api.model.internal.DefaultJavaArtifact
import com.itsaky.androidide.tooling.api.model.internal.DefaultJavaCompileOptions
import com.itsaky.androidide.tooling.api.model.internal.DefaultModelSyncFile
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceProvider
import com.itsaky.androidide.tooling.api.model.internal.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.model.internal.DefaultTestInfo
import com.itsaky.androidide.tooling.api.model.internal.DefaultTestedTargetVariant
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariant
import com.itsaky.androidide.tooling.api.model.internal.DefaultVariantDependencies
import com.itsaky.androidide.tooling.api.model.internal.DefaultViewBindingOptions
import com.itsaky.androidide.utils.ILogger
import java.lang.reflect.Proxy

/**
 * As the data is sent over streams, and the instances of properties specified in [IdeAndroidModule]
 * are just proxy classes, we need to make copy of those properties so that they can ben serialized
 * by Gson.
 *
 * This class handles the work of making copy of those properties.
 *
 * @author Akash Yadav
 */
object AndroidModulePropertyCopier {

    private val log = ILogger.newInstance(javaClass.simpleName)

    fun copy(module: IdeAndroidModule): IdeAndroidModule {
        return IdeAndroidModule(
            module.name,
            module.description,
            module.projectDir,
            module.buildDir,
            module.buildScript,
            module.parent,
            module.subprojects,
            module.tasks,
            module.path,
            module.bootClasspath,
            module.buildFolder,
            copy(module.buildTypeSourceSets),
            module.dynamicFeatures,
            copy(module.flags),
            copy(module.javaCompileOptions),
            module.lintRuleJars,
            copy(module.mainSourceSet),
            copy(module.productFlavorSourceSets),
            module.projectType,
            module.resourcePrefix,
            copy(module.variants),
            copy(module.viewBindingOptions))
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
            buildType = variant.buildType
            desugaredMethods = variant.desugaredMethods
            displayName = variant.displayName
            isInstantAppCompatible = variant.isInstantAppCompatible
            mainArtifact = copy(variant.mainArtifact)!!
            name = variant.name
            productFlavors = variant.productFlavors
            testFixturesArtifact = copy(variant.testFixturesArtifact)
            testedTargetVariant = copy(variant.testedTargetVariant)
            unitTestArtifact = copy(variant.unitTestArtifact)
        }
    fun copy(artifact: JavaArtifact?): DefaultJavaArtifact? {
        if (artifact == null) {
            return null
        }

        return DefaultJavaArtifact().apply {
            assembleTaskName = artifact.assembleTaskName
            classesFolders = artifact.classesFolders
            compileTaskName = artifact.compileTaskName
            generatedSourceFolders = artifact.generatedSourceFolders
            ideSetupTaskNames = artifact.ideSetupTaskNames
            multiFlavorSourceProvider = copy(artifact.multiFlavorSourceProvider)
            variantSourceProvider = copy(artifact.variantSourceProvider)
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
            multiFlavorSourceProvider = copy(artifact.multiFlavorSourceProvider)
            variantSourceProvider = copy(artifact.variantSourceProvider)
        }
    }

    @JvmName("copyModelSyncFiles")
    fun copy(modelSyncFiles: Collection<ModelSyncFile>): Collection<DefaultModelSyncFile> {
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

    fun copy(flags: AndroidGradlePluginProjectFlags): DefaultAndroidGradlePluginProjectFlags {
        return DefaultAndroidGradlePluginProjectFlags().apply {
            booleanFlagMap = flags.booleanFlagMap
        }
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

    fun copy(variantDependencies: VariantDependencies): DefaultVariantDependencies {
        return DefaultVariantDependencies().apply {
            this.name = variantDependencies.name
            this.mainArtifact = copy(variantDependencies.mainArtifact)!!
            this.androidTestArtifact = copy(variantDependencies.androidTestArtifact)
            this.testFixturesArtifact = copy(variantDependencies.testFixturesArtifact)
            this.unitTestArtifact = copy(variantDependencies.unitTestArtifact)
        }
    }

    private fun copy(artifact: ArtifactDependencies?): DefaultArtifactDependencies? =
        DefaultArtifactDependencies().apply {
            if (artifact == null) {
                return null
            }

            this.compileDependencies = copy(artifact.compileDependencies)!!
            this.runtimeDependencies = copy(artifact.runtimeDependencies)
        }

    private fun copy(graphs: List<GraphItem>?): List<DefaultGraphItem>? {
        if (graphs == null) {
            return null
        }

        return graphs.map { copy(it) }
    }

    private fun copy(graph: GraphItem): DefaultGraphItem =
        DefaultGraphItem().apply {
            this.dependencies = copy(graph.dependencies)!!
            this.requestedCoordinates = graph.requestedCoordinates
        }
}
