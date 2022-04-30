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

/**
 * The type of Library dependency.
 */
enum class LibraryType {

    /**
     * The dependency is a sub-project of the build.
     */
    PROJECT,

    /**
     * The dependency is an external Android Library (AAR)
     */
    ANDROID_LIBRARY,

    /**
     * The dependency is an external Java Library (JAR)
     */
    JAVA_LIBRARY,

    /**
     * The dependency is an external dependency with no artifact, pointing to a different artifact
     * (via Gradle's available-at feature, and possibly via POM's relocation feature.)
     */
    RELOCATED;
}
