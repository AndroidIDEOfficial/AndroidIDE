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
import java.io.File

/**
 * A Simple model that contains a map of (build-name, build rootDir).
 *
 * This can be queried on any Android module (and returns the same value regardless of the module)
 */
interface BuildMap: AndroidModel {

    /**
     * Map of build-ID, for composite builds.
     *
     * The map contais (name to rootDir) where rootDir is what is typically
     * returned by [org.gradle.tooling.model.BuildIdentifier.BuildIdenfier.rootDir] while name is
     * what is returned by [org.gradle.api.artifacts.component.BuildIdentifier.getName]
     */
    val buildIdMap: Map<String, File>
}
