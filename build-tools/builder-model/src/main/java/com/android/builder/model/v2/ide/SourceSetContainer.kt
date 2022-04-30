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

package com.android.builder.model.v2.ide

import com.android.builder.model.v2.AndroidModel

/**
 * A container of source sets for a given dimension value (ie a build type or a flavor)
 */
interface SourceSetContainer: AndroidModel {

    /**
     * The production source set
     */
    val sourceProvider: SourceProvider

    /**
     * The optional source set for the AndroidTest component
     */
    val androidTestSourceProvider: SourceProvider?

    /**
     * The optional source set for the UnitTest component
     */
    val unitTestSourceProvider: SourceProvider?

    /**
     * The optional source set for the TestFixtures component
     */
    val testFixturesSourceProvider: SourceProvider?
}
