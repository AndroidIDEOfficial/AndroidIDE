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

package com.android.builder.model;

import com.android.annotations.NonNull;
import java.io.File;
import java.util.Collection;

/**
 * A set of dependencies for an {@link AndroidArtifact}.
 */
public interface Dependencies {

    /**
     * The list of Android library dependencies.
     *
     * <p>On versions &lt; 3.0, the list contains direct dependencies only, which themselves contain
     * their transitive dependencies. Starting with version 3.0, the list is flattened and contains
     * all the transitive dependencies.
     *
     * <p>This includes both modules and external dependencies. They can be differentiated with
     * {@link Library#getProject()}.
     *
     * @return the list of libraries.
     */
    @NonNull
    Collection<AndroidLibrary> getLibraries();

    /**
     * The list of Java library dependencies.
     *
     * <p>This is a flattened list containing all transitive external dependencies.
     *
     * @return the list of Java library dependencies.
     */
    @NonNull
    Collection<JavaLibrary> getJavaLibraries();

    /**
     * The list of project dependencies. This is only for non Android module dependencies (which
     * right now is Java-only modules).
     *
     * <p>IMPORTANT: This is not compatible with Composite Builds. This should not be used anymore
     * starting with version 3.1. This is now superseded by {@link #getJavaModules()}.
     *
     * @return the list of projects.
     * @see #getJavaLibraries()
     * @see #getJavaModules()
     */
    @NonNull
    @Deprecated
    Collection<String> getProjects();

    /** ' A Unique identifier for a project. */
    interface ProjectIdentifier {
        /** The build id. This is typically the root dir of the build */
        @NonNull
        String getBuildId();

        /** The project path. This is unique for a given build, but not across builds. */
        @NonNull
        String getProjectPath();
    }

    /** Returns the list of Java Modules. @Since 3.1 */
    @NonNull
    Collection<ProjectIdentifier> getJavaModules();

    /** Returns the list of runtime only dependency classes. @Since 3.5 */
    @NonNull
    Collection<File> getRuntimeOnlyClasses();
}
