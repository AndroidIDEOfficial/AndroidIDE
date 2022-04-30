/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.builder.model.level2;

import com.android.annotations.NonNull;
import com.android.builder.model.AndroidProject;
import java.util.List;

/**
 * A set of dependency Graphs.
 *
 * It contains both the compile and the package graphs, through the latter could be empty in
 * non full sync.
 *
 * Each graph is fairly lightweight, with each artifact node being mostly an address, children,
 * and modifiers that are specific to this particular usage of the artifact rather than
 * artifact properties.*
 *
 * @see AndroidProject#PROPERTY_BUILD_MODEL_FEATURE_FULL_DEPENDENCIES
 */
public interface DependencyGraphs {

    /**
     * Returns the compile dependency graph.
     */
    @NonNull
    List<GraphItem> getCompileDependencies();

    /**
     * Returns the package dependency graph.
     *
     * Only valid in full dependency mode.
     */
    @NonNull
    List<GraphItem> getPackageDependencies();

    /**
     * Returns the list of provided libraries.
     *
     * The values in the list match the values returned by {@link GraphItem#getArtifactAddress()}
     * and {@link Library#getArtifactAddress()}.
     *
     * Only valid in full dependency mode.
     */
    @NonNull
    List<String> getProvidedLibraries();

    /**
     * Returns the list of skipped libraries.
     *
     * The values in the list match the values returned by {@link GraphItem#getArtifactAddress()}
     * and {@link Library#getArtifactAddress()}.
     *
     * Only valid in full dependency mode.
     */
    @NonNull
    List<String> getSkippedLibraries();

}
