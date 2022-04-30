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
import java.util.Collection;

/** Minimal model for a project output. */
@Deprecated
public interface ProjectBuildOutput {

    /**
     * Returns a collection of {@link VariantBuildOutput} identifying all the variants for this
     * project.
     *
     * @return a list of {@link VariantBuildOutput} per variant.
     */
    @NonNull
    Collection<VariantBuildOutput> getVariantsBuildOutput();
}
