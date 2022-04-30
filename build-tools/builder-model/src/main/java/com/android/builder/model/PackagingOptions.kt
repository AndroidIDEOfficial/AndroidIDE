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
 * Options for APK packaging.
 */
interface PackagingOptions {
    /**
     * Glob patterns to exclude from packaging.
     *
     * @return a set of glob pattern that use forward slash as a separator
     */
    val excludes: Set<String>

    /**
     * Glob patterns to pick first.
     *
     * @return a set of glob pattern that use forward slash as a separator
     */
    val pickFirsts: Set<String>

    /**
     * Glob patterns to merge.
     *
     * @return a set of glob pattern that use forward slash as a separator
     */
    val merges: Set<String>

    /**
     * Glob patterns to exclude native libraries from being stripped.
     *
     * @return a set of glob pattern that use forward slash as a separator
     */
    val doNotStrip: Set<String>
}