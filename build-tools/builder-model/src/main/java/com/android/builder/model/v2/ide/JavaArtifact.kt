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
 * The information for a generated Java artifact.
 *
 * This artifact is for Java components inside an Android project, which is only unit tests
 * for now.
 *
 * @since 4.2
 */
interface JavaArtifact : AbstractArtifact, AndroidModel {
    /** Path to the mockable platform jar generated for this [JavaArtifact], if present.  */
    val mockablePlatformJar: File?

    /**
     * Returns the folder containing resource files that classes from this artifact expect to find
     * on the classpath.
     *
     * This is used to run the unit tests
     */
    val runtimeResourceFolder: File?
}
