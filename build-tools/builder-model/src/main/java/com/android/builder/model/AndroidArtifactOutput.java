/*
 * Copyright (C) 2014 The Android Open Source Project
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
import com.android.build.OutputFile;
import java.io.File;

/**
 * The Actual output for a {@link AndroidArtifact}, which can be one file at the minimum or several
 * APKs in case of pure splits configuration.
 */
public interface AndroidArtifactOutput extends OutputFile {

    /**
     * Returns the name of the task used to generate this artifact output.
     *
     * @return the name of the task.
     */
    @NonNull
    @Deprecated
    String getAssembleTaskName();

    /**
     * The generated manifest for this variant's artifact's output.
     */
    @NonNull
    File getGeneratedManifest();
}
