/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.builder.model

/**
 * a Product Flavor. This is only the configuration of the flavor.
 *
 * This is an interface for the gradle tooling api, and should only be used from Android Studio.
 * It is not part of the DSL & API interfaces of the Android Gradle Plugin.
 *
 * It does not include the sources or the dependencies. Those are available on the container
 * or in the artifact info.
 *
 * @see ProductFlavorContainer
 *
 * @see BaseArtifact.getDependencies
 */
interface ProductFlavor : BaseConfig, DimensionAware {
    /** The name of the flavor. */
    override fun getName(): String

    /**
     * The name of the product flavor. This is only the value set on this product flavor.
     * To get the final application id name, use [AndroidArtifact.getApplicationId].
     */
    val applicationId: String?

    /**
     * The version code associated with this flavor or null if none have been set.
     * This is only the value set on this product flavor, not necessarily the actual
     * version code used.
     */
    val versionCode: Int?

    /** The version name. This is only the value set on this product flavor.
     * To get the final value, use [Variant.getMergedFlavor] with
     * [.getVersionNameSuffix] and [BuildType.getVersionNameSuffix].
     */
    val versionName: String?

    /** The minSdkVersion, or null if not specified. This is only the value set on this product flavor. */
    val minSdkVersion: ApiVersion?

    /** The targetSdkVersion, or null if not specified. This is only the value set on this product flavor. */
    val targetSdkVersion: ApiVersion?

    /** The maxSdkVersion, or null if not specified. This is only the value set on this produce flavor. */
    val maxSdkVersion: Int?

    /**
     * The renderscript target api, or null if not specified. This is only the value set on this product flavor.
     * TODO: make final renderscript target api available through the model
     */
    val renderscriptTargetApi: Int?

    /**
     * Whether the renderscript code should be compiled in support mode to
     * make it compatible with older versions of Android.
     *
     * True if support mode is enabled, false if not, and null if not specified.
     */
    val renderscriptSupportModeEnabled: Boolean?

    /**
     * Whether the renderscript BLAS support lib should be used to
     * make it compatible with older versions of Android.
     *
     * True if BLAS support lib is enabled, false if not, and null if not specified.
     */
    val renderscriptSupportModeBlasEnabled: Boolean?

    /**
     * Whether the renderscript code should be compiled to generate C/C++ bindings.
     * True for C/C++ generation, false for Java, null if not specified.
     */
    val renderscriptNdkModeEnabled: Boolean?

    /**
     * The test application id. This is only the value set on this product flavor.
     * To get the final value, use [Variant.getExtraAndroidArtifacts] with
     * [AndroidProject.ARTIFACT_ANDROID_TEST] and then
     * [AndroidArtifact.getApplicationId]
     */
    val testApplicationId: String?

    /**
     * The test instrumentation runner. This is only the value set on this product flavor.
     * TODO: make test instrumentation runner available through the model.
     */
    val testInstrumentationRunner: String?

    /** The arguments for the test instrumentation runner.*/
    val testInstrumentationRunnerArguments: Map<String, String>

    /** The handlingProfile value. This is only the value set on this product flavor. */
    val testHandleProfiling: Boolean?

    /** The functionalTest value. This is only the value set on this product flavor. */
    val testFunctionalTest: Boolean?

    /**
     * The resource configuration for this variant.
     *
     * This is the list of -c parameters for aapt.
     */
    val resourceConfigurations: Collection<String>

    val vectorDrawables: VectorDrawablesOptions
    /**
     *  Whether to enable unbundling mode for embedded wear app.
     *
     * If true, this enables the app to transition from an embedded wear app to one
     * distributed by the play store directly.
     */
    val wearAppUnbundled: Boolean?
}
