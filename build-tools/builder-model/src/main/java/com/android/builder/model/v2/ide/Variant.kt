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
package com.android.builder.model.v2.ide

import com.android.builder.model.v2.AndroidModel
import java.io.File

/**
 * A build Variant.
 */
interface Variant: AndroidModel {
    /**
     * The name of the variant.
     */
    val name: String

    /**
     * The display name for the variant.
     */
    val displayName: String

    /**
     * The main artifact for this variant.
     */
    val mainArtifact: AndroidArtifact

    /**
     * The AndroidTest artifact for this variant, if applicable.
     */
    val androidTestArtifact: AndroidArtifact?

    /**
     * The Unit Test artifact for this variant, if applicable.
     */
    val unitTestArtifact: JavaArtifact?

    /**
     * The TestFixtures artifact for this variant, if applicable.
     */
    val testFixturesArtifact: AndroidArtifact?

    /**
     * For standalone test plugins: information about the tested project.
     *
     * For other plugin types, this is null
     */
    val testedTargetVariant: TestedTargetVariant?

    /**
     * Whether the variant is instant app compatible.
     *
     * Only application modules and dynamic feature modules will set this property.
     */
    val isInstantAppCompatible: Boolean

    /**
     * Desugared methods supported by D8 and core library desugaring.
     */
    val desugaredMethods: List<File>
}
