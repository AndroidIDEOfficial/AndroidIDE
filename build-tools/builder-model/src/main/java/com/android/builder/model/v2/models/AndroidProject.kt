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
interface AndroidProject: AndroidModel {

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

    /**
     * Returns the compile options for Java code.
     */
    val javaCompileOptions: JavaCompileOptions

    /**
     * Returns the resource prefix to use, if any. This is an optional prefix which can
     * be set and which is used by the defaults to automatically choose new resources
     * with a certain prefix, warn if resources are not using the given prefix, etc.
     * This helps work with resources in the app namespace where there could otherwise
     * be unintentional duplicated resource names between unrelated libraries.
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

    /** Returns the AGP flags for this project.  */
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
