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
import com.android.builder.model.v2.ide.BundleInfo
import com.android.builder.model.v2.ide.JavaArtifact
import com.android.builder.model.v2.ide.JavaCompileOptions
import com.android.builder.model.v2.ide.SourceProvider
import com.android.builder.model.v2.ide.SourceSetContainer
import com.android.builder.model.v2.ide.TestInfo
import com.android.builder.model.v2.ide.TestedTargetVariant
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.ide.ViewBindingOptions
import com.itsaky.androidide.tooling.api.model.IdeAndroidModule
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultAndroidArtifact
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultAndroidGradlePluginProjectFlags
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultApiVersion
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultBundleInfo
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultJavaArtifact
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultJavaCompileOptions
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultModelSyncFile
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultSourceProvider
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultSourceSetContainer
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultTestInfo
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultTestedTargetVariant
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultVariant
import com.itsaky.androidide.tooling.api.model.android.internal.DefaultViewBindingOptions

/**
 * As the data is sent over streams, and the instances of properties specified in [IdeAndroidModule]
 * are just proxy classes, we need to make copy of those properties so that they can ben serialized
 * by Gson.
 *
 * This class handles the work of making copying of those properties.
 *
 * @author Akash Yadav
 */
object AndroidModulePropertyCopier {

    fun copy(module: IdeAndroidModule): IdeAndroidModule {
        module.buildTypeSourceSets = copy(module.buildTypeSourceSets)
        module.flags = copy(module.flags)
        module.javaCompileOptions = copy(module.javaCompileOptions)
        module.mainSourceSet = copy(module.mainSourceSet)
        module.productFlavorSourceSets = copy(module.productFlavorSourceSets)
        module.variants = copy(module.variants)
        module.viewBindingOptions = copy(module.viewBindingOptions)
        return module
    }

    private fun copy(viewBindingOptions: ViewBindingOptions?): ViewBindingOptions? {
        return when (viewBindingOptions) {
            null -> null
            else -> DefaultViewBindingOptions(viewBindingOptions.isEnabled)
        }
    }

    @JvmName("copyVariants")
    private fun copy(variants: Collection<Variant>): Collection<Variant> {
        val new = mutableListOf<Variant>()
        for (variant in variants) {
            new.add(copy(variant))
        }
        return new
    }

    private fun copy(variant: Variant): Variant =
        DefaultVariant(
            copy(variant.androidTestArtifact),
            variant.buildType,
            variant.desugaredMethods,
            variant.displayName,
            variant.isInstantAppCompatible,
            copy(variant.mainArtifact)!!,
            variant.name,
            variant.productFlavors,
            copy(variant.testFixturesArtifact),
            copy(variant.testedTargetVariant),
            copy(variant.unitTestArtifact))

    private fun copy(artifact: JavaArtifact?): JavaArtifact? {
        if (artifact == null) {
            return null
        }

        return DefaultJavaArtifact(
            artifact.assembleTaskName,
            artifact.classesFolders,
            artifact.compileTaskName,
            artifact.generatedSourceFolders,
            artifact.ideSetupTaskNames,
            copy(artifact.multiFlavorSourceProvider),
            copy(artifact.variantSourceProvider),
            artifact.mockablePlatformJar,
            artifact.runtimeResourceFolder)
    }

    private fun copy(variant: TestedTargetVariant?): TestedTargetVariant? {
        if (variant == null) {
            return null
        }

        return DefaultTestedTargetVariant(variant.targetProjectPath, variant.targetVariant)
    }

    private fun copy(artifact: AndroidArtifact?): AndroidArtifact? {
        if (artifact == null) {
            return null
        }

        return DefaultAndroidArtifact(
            artifact.abiFilters,
            artifact.assembleTaskOutputListingFile,
            copy(artifact.bundleInfo),
            artifact.codeShrinker,
            artifact.generatedResourceFolders,
            artifact.isSigned,
            artifact.maxSdkVersion,
            copy(artifact.minSdkVersion)!!,
            artifact.signingConfigName,
            artifact.sourceGenTaskName,
            copy(artifact.testInfo),
            artifact.assembleTaskName,
            artifact.classesFolders,
            artifact.compileTaskName,
            artifact.generatedSourceFolders,
            artifact.ideSetupTaskNames,
            copy(artifact.multiFlavorSourceProvider),
            copy(artifact.variantSourceProvider))
    }

    @JvmName("copyModelSyncFiles")
    private fun copy(modelSyncFiles: Collection<ModelSyncFile>): Collection<ModelSyncFile> {
        val new = mutableListOf<ModelSyncFile>()
        for (file in modelSyncFiles) {
            new.add(copy(file))
        }

        return new
    }

    private fun copy(file: ModelSyncFile): ModelSyncFile =
        DefaultModelSyncFile(file.modelSyncType, file.syncFile, file.taskName)

    private fun copy(info: TestInfo?): TestInfo? {
        if (info == null) {
            return null
        }

        return DefaultTestInfo(
            info.additionalRuntimeApks,
            info.animationsDisabled,
            info.execution,
            info.instrumentedTestTaskName)
    }

    private fun copy(version: ApiVersion?): ApiVersion? =
        if (version == null) null else DefaultApiVersion(version.apiLevel, version.codename)

    private fun copy(bundleInfo: BundleInfo?): BundleInfo? {
        if (bundleInfo == null) {
            return null
        }

        return DefaultBundleInfo(
            bundleInfo.apkFromBundleTaskName,
            bundleInfo.apkFromBundleTaskOutputListingFile,
            bundleInfo.bundleTaskName,
            bundleInfo.bundleTaskOutputListingFile)
    }

    private fun copy(javaCompileOptions: JavaCompileOptions): JavaCompileOptions {
        return DefaultJavaCompileOptions(
            javaCompileOptions.encoding,
            javaCompileOptions.isCoreLibraryDesugaringEnabled,
            javaCompileOptions.sourceCompatibility,
            javaCompileOptions.targetCompatibility)
    }

    private fun copy(flags: AndroidGradlePluginProjectFlags): AndroidGradlePluginProjectFlags {
        return DefaultAndroidGradlePluginProjectFlags(flags.booleanFlagMap)
    }

    private fun copy(containers: Collection<SourceSetContainer>): Collection<SourceSetContainer> {
        val new = mutableListOf<SourceSetContainer>()

        for (container in containers) {
            new.add(copy(container))
        }

        return new
    }

    private fun copy(container: SourceSetContainer): SourceSetContainer {
        return DefaultSourceSetContainer(
            copy(container.androidTestSourceProvider),
            copy(container.sourceProvider)!!,
            copy(container.testFixturesSourceProvider),
            copy(container.unitTestSourceProvider))
    }

    private fun copy(provider: SourceProvider?): SourceProvider? {
        if (provider == null) {
            return null
        }

        return DefaultSourceProvider(
            provider.aidlDirectories,
            provider.assetsDirectories,
            provider.javaDirectories,
            provider.jniLibsDirectories,
            provider.kotlinDirectories,
            provider.manifestFile,
            provider.mlModelsDirectories,
            provider.name,
            provider.renderscriptDirectories,
            provider.resDirectories,
            provider.resourcesDirectories,
            provider.shadersDirectories)
    }
}
