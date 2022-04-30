/*
 * Copyright (C) 2015 The Android Open Source Project
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

/**
 * A native artifact.
 *
 * Represent an native library.
 */
public interface NativeArtifact {

    /**
     * Returns the name of the artifact.
     */
    @NonNull
    String getName();

    /**
     * Returns the toolchain used for compilation.
     */
    @NonNull
    String getToolChain();

    /**
     * Returns the group this artifact is associated with.
     */
    @NonNull
    String getGroupName();

    /**
     * Returns the name of the task used to generate the artifact output.
     */
    @NonNull
    String getAssembleTaskName();

    /**
     * Returns the source files.
     */
    @NonNull
    Collection<NativeFile> getSourceFiles();

    /**
     * Returns the folders container headers exported for the library.
     */
    @NonNull
    Collection<File> getExportedHeaders();

    /**
     * Returns the target ABI of the artifact.
     */
    @NonNull
    String getAbi();

    /**
     * Returns the name of the target that builds this artifact.
     */
    @NonNull
    String getTargetName();

    /** Returns the output file with debug symbols unstripped. */
    @Nullable
    File getOutputFile();

    /**
     * Returns the files that needs to be deployed with getOutputFile to be executed on device.
     */
    @NonNull
    Collection<File> getRuntimeFiles();
}
