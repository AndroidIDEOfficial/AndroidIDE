/*
 * Copyright (C) 2018 The Android Open Source Project
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
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Contains information about a specific variant + ABI for a native project. This structure is
 * effectively partial information of a NativeAndroidProject. This is the information that is slow
 * to compute, for example we need to call cmake.exe to generate a project.
 */
public interface NativeVariantAbi extends Serializable {
    /** Returns a collection of files that affect the build. */
    @NonNull
    Collection<File> getBuildFiles();

    /** Returns a collection of native artifacts. */
    @NonNull
    Collection<NativeArtifact> getArtifacts();

    /** Returns a collection of toolchains. */
    @NonNull
    Collection<NativeToolchain> getToolChains();

    /** Returns a collection of all compile settings. */
    @NonNull
    Collection<NativeSettings> getSettings();

    /**
     * Return a map of file extension to each file type.
     *
     * <p>The key is the file extension, the value is either "c" or "c++".
     */
    @NonNull
    Map<String, String> getFileExtensions();

    /** Returns the variant name. */
    @NonNull
    String getVariantName();

    /** Returns the abi. */
    @NonNull
    String getAbi();
}
