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

/**
 * The dependencies information for a given artifact.
 *
 * It contains the compile graph, always, and optionally the runtime graph.
 *
 * Each graph is fairly lightweight, with each artifact node being mostly an address, children,
 * and modifiers that are specific to this particular usage of the artifact rather than
 * artifact properties.*
 *
 * @since 4.2
 */
interface ArtifactDependencies: AndroidModel {
    /**
     * The compile dependency graph.
     */
    val compileDependencies: List<GraphItem>

    /**
     * The runtime dependency graph.
     */
    val runtimeDependencies: List<GraphItem>

    val unresolvedDependencies: List<UnresolvedDependency>
}

interface UnresolvedDependency {
    val name: String
    val cause: String?
}
