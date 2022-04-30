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

/** Model for a test variant build output */
public interface TestVariantBuildOutput extends BaseBuildOutput {

    /** Enum for all possible test variant types that can be returned. */
    enum TestType {
        UNIT,
        ANDROID_TEST
    }

    String UNIT = TestType.UNIT.name();
    String ANDROID_TEST = TestType.ANDROID_TEST.name();

    /**
     * Returns the variant name of the tested variant.
     *
     * @return the tested variant name.
     */
    @NonNull
    String getTestedVariantName();

    /**
     * Returns the test variant type as a String
     *
     * @return one of {@link TestType} value as a {@link String}
     */
    @NonNull
    String getType();
}
