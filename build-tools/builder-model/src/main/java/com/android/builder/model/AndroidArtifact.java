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
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An Android Artifact.
 *
 * <p>This is the entry point for the output of a {@link Variant}. This can be more than one output
 * in the case of multi-apk where more than one APKs are generated from the same set of sources.
 */
public interface AndroidArtifact extends BaseArtifact {

    /** @deprecated Use post-build model instead. See {@link VariantBuildInformation} */
    @Deprecated
    @NonNull
    Collection<AndroidArtifactOutput> getOutputs();

    /**
     * Returns whether the output file is signed. This can only be true for the main apk of an
     * application project.
     *
     * @return true if the app is signed.
     */
    boolean isSigned();

    /**
     * Returns the name of the {@link SigningConfig} used for the signing. If none are setup or if
     * this is not the main artifact of an application project, then this is null.
     *
     * @return the name of the setup signing config.
     */
    @Nullable
    String getSigningConfigName();

    /**
     * Returns the application id of this artifact.
     *
     * <p>Deprecated as of 4.2. Application ID is now only provided post-build.
     *
     * @return the application id.
     */
    @Deprecated
    @NonNull
    String getApplicationId();

    /**
     * Returns the name of the task used to generate the source code. The actual value might depend
     * on the build system front end.
     *
     * @return the name of the code generating task.
     */
    @NonNull
    String getSourceGenTaskName();

    /**
     * Returns all the source folders that are generated. This is typically folders for the R, the
     * aidl classes, and the renderscript classes.
     *
     * <p>Deprecated, as of 1.2, present in super interface.
     *
     * @return a list of folders.
     */
    @NonNull
    @Override
    Collection<File> getGeneratedSourceFolders();

    /**
     * Returns all the resource folders that are generated. This is typically the renderscript
     * output and the merged resources.
     *
     * @return a list of folder.
     */
    @NonNull
    Collection<File> getGeneratedResourceFolders();

    /**
     * Returns the ABI filters associated with the artifact, or null if there are no filters.
     *
     * <p>If the list contains values, then the artifact only contains these ABIs and excludes
     * others.
     */
    @Nullable
    Set<String> getAbiFilters();

    /**
     * Returns the native libraries associated with the artifact.
     *
     * <p>Deprecated since ndk-compile has been deprecated.
     */
    @Nullable
    @Deprecated
    Collection<NativeLibrary> getNativeLibraries();

    /**
     * Map of generated res values where the key is the res name. This method is deprecated and will
     * always return an empty map
     *
     * @return empty map
     */
    @NonNull
    @Deprecated
    Map<String, ClassField> getResValues();

    /**
     * Returns the InstantRun feature related model.
     *
     * @return the model for all InstantRun related information.
     */
    @NonNull
    InstantRun getInstantRun();

    /**
     * Returns a list of additional APKs that need to installed on the device for this artifact to
     * work correctly.
     *
     * <p>For test artifacts, these will be "buddy APKs" from the {@code androidTestUtil}
     * configuration.
     *
     * @since 3.0
     */
    @NonNull
    Collection<File> getAdditionalRuntimeApks();

    /**
     * Returns the test options only if the variant type is testing.
     *
     * @since 3.0
     */
    @Nullable
    TestOptions getTestOptions();

    /**
     * Returns the name of the task used to run instrumented tests or null if the variant is not a
     * test variant.
     *
     * @return name of the task used to run instrumented tests
     * @since 3.1
     */
    @Nullable
    String getInstrumentedTestTaskName();

    /**
     * Returns the name of the task used to generate the bundle file (.aab), or null if the task is
     * not supported.
     *
     * @return name of the task used to generate the bundle file (.aab)
     * @since 3.2
     */
    @Nullable
    String getBundleTaskName();

    /**
     * Returns the path to the listing file generated after each {@link #getBundleTaskName()} task
     * execution. The listing file will contain a reference to the produced bundle file (.aab).
     * Returns null when {@link #getBundleTaskName()} returns null.
     *
     * @return the file path for the bundle model file.
     * @since 4.0
     */
    @Nullable
    String getBundleTaskOutputListingFile();

    /**
     * Returns the name of the task used to generate APKs via the bundle file (.aab), or null if the
     * task is not supported.
     *
     * @return name of the task used to generate the APKs via the bundle
     * @since 3.2
     */
    @Nullable
    String getApkFromBundleTaskName();

    /**
     * Returns the path to the model file generated after each {@link #getApkFromBundleTaskName()}
     * task execution. The model will contain a reference to the folder where APKs from bundle are
     * placed into. Returns null when {@link #getApkFromBundleTaskName()} returns null.
     *
     * @return the file path for the {@link #getApkFromBundleTaskName()} output model.
     * @since 4.0
     */
    @Nullable
    String getApkFromBundleTaskOutputListingFile();

    /**
     * Returns the code shrinker used by this artifact or null if no shrinker is used to build this
     * artifact.
     */
    @Nullable
    CodeShrinker getCodeShrinker();
}
