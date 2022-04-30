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
import com.android.annotations.Nullable;
import java.util.List;

/**
 * A node in a dependency graph, representing a direct or transitive dependency.
 *
 * This does not directly contain artifact information, instead it focuses on the graph
 * information (transitive dependencies) as well as the usage of this particular dependency
 * in this node of the graph (ie what are its modifiers: what version was originally requested.)
 *
 * @since 2.3
 */
public interface GraphItem {

    /**
     * Returns the artifact address in a unique way.
     *
     * This is either a module path for sub-modules, or a maven coordinate for external
     * dependencies.
     *
     * The maven coordinates are in the format: groupId:artifactId:version[:classifier][@extension]
     *
     */
    @NonNull
    String getArtifactAddress();

    /**
     * Returns this library's Maven coordinates, as requested in the build file.
     */
    @Nullable
    String getRequestedCoordinates();

    /**
     * Return the direct dependency of this node.
     */
    @NonNull
    List<GraphItem> getDependencies();
}
