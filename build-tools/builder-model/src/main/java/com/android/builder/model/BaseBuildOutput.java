/*
 * Copyright (C) 2017 The Android Open Source Project
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
import java.util.Collection;

/** Base model to represents a variant or a test variant build output. */
@Deprecated
public interface BaseBuildOutput {

    /**
     * Variant full name.
     *
     * @return a {@link String} representing this variant's name.
     */
    @NonNull
    String getName();

    /**
     * Returns the collection of build output for this Variant.
     *
     * @return a possibly empty {@link Collection} of {@link AndroidArtifactOutput} for each output
     *     file from this variant.
     */
    @NonNull
    Collection<OutputFile> getOutputs();
}
