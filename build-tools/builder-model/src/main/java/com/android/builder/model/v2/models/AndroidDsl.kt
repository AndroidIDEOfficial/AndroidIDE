/*
 * Copyright (C) 2021 The Android Open Source Project
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
import com.android.builder.model.v2.dsl.BuildType
import com.android.builder.model.v2.dsl.DependenciesInfo
import com.android.builder.model.v2.dsl.ProductFlavor
import com.android.builder.model.v2.dsl.SigningConfig
import com.android.builder.model.v2.ide.AaptOptions
import com.android.builder.model.v2.ide.LintOptions
import com.android.builder.model.v2.ide.ProjectType

interface AndroidDsl: AndroidModel {

    /**
     * Returns the optional group-id of the artifact represented by this project.
     */
    val groupId: String?

    /**
     * The default config information
     */
    val defaultConfig: ProductFlavor

    /**
     * A list of all the [BuildType]
     */
    val buildTypes: List<BuildType>

    /**
     * The list of all the flavor dimensions, may be empty.
     */
    val flavorDimensions: Collection<String>

    /**
     * The list of all the [ProductFlavor] values, across all dimensions
     *
     * See [ProductFlavor.dimension] to know which dimension a flavor belongs to.
     */
    val productFlavors: List<ProductFlavor>

    /**
     * Returns the compilation target as a string. This is the full extended target hash string.
     * (see com.android.sdklib.IAndroidTarget#hashString())
     *
     * @return the target hash string
     */
    val compileTarget: String

    /**
     * Returns a list of [SigningConfig].
     */
    val signingConfigs: Collection<SigningConfig>

    /**
     * Returns the aapt options.
     */
    val aaptOptions: AaptOptions

    /**
     * Returns the lint options.
     */
    val lintOptions: LintOptions

    /**
     * The build tools version used by this module.
     */
    val buildToolsVersion: String

    /**
     * The options for Dependencies inclusion in APKs.
     *
     * Only non-null for [projectType] with values [ProjectType.APPLICATION]
     */
    val dependenciesInfo: DependenciesInfo?
}
