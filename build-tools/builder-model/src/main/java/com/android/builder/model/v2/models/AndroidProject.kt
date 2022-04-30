/*
 * Copyright (C) 2020 The Android Open Source Project
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
package com.android.builder.model.v2.models

import com.android.builder.model.v2.AndroidModel
import com.android.builder.model.v2.ModelSyncFile
import com.android.builder.model.v2.ide.AndroidGradlePluginProjectFlags
import com.android.builder.model.v2.ide.JavaCompileOptions
import com.android.builder.model.v2.ide.ProjectType
import com.android.builder.model.v2.ide.Variant
import com.android.builder.model.v2.ide.ViewBindingOptions
import java.io.File

/**
 * Extended information for Android modules.
 *
 * See [BasicAndroidProject]
 */
@Suppress("unused")
interface AndroidProject : AndroidModel {

    companion object {
        //  Injectable properties to use with -P
        // Sent by Studio 4.2+
        const val PROPERTY_BUILD_MODEL_ONLY = "android.injected.build.model.v2"

        // Sent by Studio 2.2+ and Android Support plugin running with IDEA from 4.1+
        // This property will enable compatibility checks between Android Support plugin and the
        // Android
        // Gradle plugin.
        // A use case for this property is that by restricting which versions are compatible
        // with the plugin, we could safely remove deprecated methods in the builder-model
        // interfaces.
        const val PROPERTY_ANDROID_SUPPORT_VERSION = "android.injected.studio.version"

        // Sent in when external native projects models requires a refresh.
        const val PROPERTY_REFRESH_EXTERNAL_NATIVE_MODEL =
            "android.injected.refresh.external.native.model"

        // Sent by Studio 2.2+
        // This property is sent when a run or debug is invoked.  APK built with this property
        // should
        // be marked with android:testOnly="true" in the AndroidManifest.xml such that it will be
        // rejected by the Play store.
        const val PROPERTY_TEST_ONLY = "android.injected.testOnly"

        // Sent by Studio 1.5+
        // The version api level of the target device.
        const val PROPERTY_BUILD_API = "android.injected.build.api"

        // The version codename of the target device. Null for released versions,
        const val PROPERTY_BUILD_API_CODENAME = "android.injected.build.codename"
        const val PROPERTY_BUILD_ABI = "android.injected.build.abi"
        const val PROPERTY_BUILD_DENSITY = "android.injected.build.density"

        // Has the effect of telling the Gradle plugin to
        //   1) Generate machine-readable errors
        //   2) Generate build metadata JSON files
        const val PROPERTY_INVOKED_FROM_IDE = "android.injected.invoked.from.ide"
        const val PROPERTY_SIGNING_STORE_FILE = "android.injected.signing.store.file"
        const val PROPERTY_SIGNING_STORE_PASSWORD = "android.injected.signing.store.password"
        const val PROPERTY_SIGNING_KEY_ALIAS = "android.injected.signing.key.alias"
        const val PROPERTY_SIGNING_KEY_PASSWORD = "android.injected.signing.key.password"
        const val PROPERTY_SIGNING_STORE_TYPE = "android.injected.signing.store.type"
        const val PROPERTY_SIGNING_V1_ENABLED = "android.injected.signing.v1-enabled"
        const val PROPERTY_SIGNING_V2_ENABLED = "android.injected.signing.v2-enabled"
        const val PROPERTY_DEPLOY_AS_INSTANT_APP = "android.injected.deploy.instant-app"
        const val PROPERTY_SIGNING_COLDSWAP_MODE = "android.injected.coldswap.mode"
        const val PROPERTY_APK_SELECT_CONFIG = "android.inject.apkselect.config"
        const val PROPERTY_EXTRACT_INSTANT_APK = "android.inject.bundle.extractinstant"

        /** Version code to be used in the built APK. */
        const val PROPERTY_VERSION_CODE = "android.injected.version.code"

        /** Version name to be used in the built APK. */
        const val PROPERTY_VERSION_NAME = "android.injected.version.name"

        /**
         * Location for APKs. If defined as a relative path, then it is resolved against the
         * project's path.
         */
        const val PROPERTY_APK_LOCATION = "android.injected.apk.location"

        /**
         * Location of the build attribution file produced by the gradle plugin to be deserialized
         * and used in the IDE build attribution.
         */
        const val PROPERTY_ATTRIBUTION_FILE_LOCATION = "android.injected.attribution.file.location"

        /**
         * Comma separated list of on-demand dynamic modules or instant app modules names that are
         * selected by the user for installation on the device during deployment.
         */
        const val PROPERTY_INJECTED_DYNAMIC_MODULES_LIST = "android.injected.modules.install.list"

        const val FD_INTERMEDIATES = "intermediates"
        const val FD_LOGS = "logs"
        const val FD_OUTPUTS = "outputs"
        const val FD_GENERATED = "generated"
    }

    /**
     * The namespace of the main artifact.
     *
     * This is here rather than on [com.android.builder.model.v2.ide.AndroidArtifact] because this
     * is common to all artifacts as it cannot be changed per variants.
     */
    val namespace: String

    /**
     * The namespace of the AndroidTest artifact.
     *
     * This is here rather than on [com.android.builder.model.v2.ide.AndroidArtifact] because this
     * is common to all artifacts as it cannot be changed per variants.
     *
     * If there are no AndroidTest components, this returns null.
     */
    val androidTestNamespace: String?

    /**
     * The namespace of the Test Fixtures artifact.
     *
     * This is here rather than on [com.android.builder.model.v2.ide.AndroidArtifact] because this
     * is common to all artifacts as it cannot be changed per variants.
     *
     * If there are no AndroidTest components, this returns null.
     */
    val testFixturesNamespace: String?

    /**
     * Returns a list of all the variants.
     *
     * This does not include test variant. Test variants are additional artifacts in their
     * respective variant info.
     *
     * @return a list of the variants.
     */
    val variants: Collection<Variant>

    /** Returns the compile options for Java code. */
    val javaCompileOptions: JavaCompileOptions

    /**
     * Returns the resource prefix to use, if any. This is an optional prefix which can be set and
     * which is used by the defaults to automatically choose new resources with a certain prefix,
     * warn if resources are not using the given prefix, etc. This helps work with resources in the
     * app namespace where there could otherwise be unintentional duplicated resource names between
     * unrelated libraries.
     *
     * @return the optional resource prefix, or null if not set
     */
    val resourcePrefix: String?

    /**
     * The list of dynamic features.
     *
     * The values are Gradle project paths.
     *
     * Only non-null for [projectType] with values [ProjectType.APPLICATION]
     */
    val dynamicFeatures: Collection<String>?

    /**
     * The options for view binding.
     *
     * Only non-null if view-binding is enabled
     */
    val viewBindingOptions: ViewBindingOptions?

    /** Returns the AGP flags for this project. */
    val flags: AndroidGradlePluginProjectFlags

    /**
     * The lint jars that this module uses to run extra lint checks on this project.
     *
     * This is the resolution of the `lintCheck` configuration.
     */
    val lintChecksJars: List<File>

    /**
     * Returns all the [ModelSyncFile] for this project.
     *
     * @return a list of [ModelSyncFile]
     * @since 7.3
     */
    val modelSyncFiles: Collection<ModelSyncFile>
}
