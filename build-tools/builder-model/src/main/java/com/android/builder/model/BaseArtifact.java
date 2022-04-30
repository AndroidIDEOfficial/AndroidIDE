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
import com.android.annotations.Nullable;
import com.android.builder.model.level2.DependencyGraphs;
import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * The base information for all generated artifacts
 */
public interface BaseArtifact {

    /**
     * Name of the artifact. This should match {@link ArtifactMetaData#getName()}.
     */
    @NonNull
    String getName();

    /**
     * @return the name of the task used to compile the code.
     */
    @NonNull
    String getCompileTaskName();

    /**
     * Returns the name of the task used to generate the artifact output(s).
     *
     * @return the name of the task.
     */
    @NonNull
    String getAssembleTaskName();

    /**
     * Returns the absolute path for the listing file that will get updated after each build. The
     * model file will contain deployment related information like applicationId, list of APKs.
     *
     * @return the path to a json file.
     */
    @NonNull
    String getAssembleTaskOutputListingFile();

    /**
     * Returns the folder containing the class files. This is the output of the java compilation.
     *
     * @return a folder.
     */
    @NonNull
    File getClassesFolder();

    /**
     * Folders or jars containing additional classes (e.g., R.jar or those registered by third-party
     * plugins like Kotlin).
     */
    @NonNull
    Set<File> getAdditionalClassesFolders();

    /**
     * Returns the folder containing resource files that classes form this artifact expect to find
     * on the classpath.
     */
    @NonNull
    File getJavaResourcesFolder();

    /**
     * Return the level 0-1 type dependencies
     */
    @NonNull
    Dependencies getDependencies();

    /**
     * Return the level 0-1 type dependencies.
     * @deprecated use {@link #getDependencies()}
     */
    @Deprecated
    @NonNull
    Dependencies getCompileDependencies();

    /**
     * Returns the resolved dependencies for this artifact.
     *
     * This is a composite of all the
     * dependencies for that artifact: default config + build type + flavor(s).
     *
     * @return The dependencies.
     */
    @NonNull
    DependencyGraphs getDependencyGraphs();

    /**
     * A SourceProvider specific to the variant. This can be null if there is no flavors as
     * the "variant" is equal to the build type.
     *
     * @return the variant specific source provider
     */
    @Nullable
    SourceProvider getVariantSourceProvider();

    /**
     * A SourceProvider specific to the flavor combination.
     *
     * For instance if there are 2 dimensions, then this would be Flavor1Flavor2, and would be
     * common to all variant using these two flavors and any of the build type.
     *
     * This can be null if there is less than 2 flavors.
     *
     * @return the multi flavor specific source provider
     */
    @Nullable
    SourceProvider getMultiFlavorSourceProvider();

    /**
     * Returns names of tasks that need to be run when setting up the IDE project. After these
     * tasks have run, all the generated source files etc. that the IDE needs to know about should
     * be in place.
     */
    @NonNull
    Set<String> getIdeSetupTaskNames();

    /**
     * Returns all the source folders that are generated. This is typically folders for the R,
     * the aidl classes, and the renderscript classes.
     *
     * @return a list of folders.
     * @since 1.2
     */
    @NonNull
    Collection<File> getGeneratedSourceFolders();
}
