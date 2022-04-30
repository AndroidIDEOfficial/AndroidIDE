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
import com.android.annotations.Nullable;

/**
 * Represents the version of an Android Platform.
 *
 * A version is defined by an API level and an optional code name.
 *
 * Release versions of the Android platform are identified by their API level (integer),
 * (technically the code name for release version is "REL" but this class will return
 * <code>null</code> instead.)
 *
 * Preview versions of the platform are identified by a code name. Their API level
 * is usually set to the value of the previous platform.
 */
public interface ApiVersion {

    /**
     * Returns the api level as an integer.
     * <p>For target that are in preview mode, this can be superseded by
     * {@link #getCodename()}.
     *
     * @see #getCodename()
     */
    int getApiLevel();

    /**
     * Returns the version code name if applicable, null otherwise.
     * <p>If the codename is non null, then the API level should be ignored, and this should be
     * used as a unique identifier of the target instead.
     */
    @Nullable
    String getCodename();

    /**
     * Returns the API value as a string.
     *
     * If there's a codename, this returns it, otherwise this returns the string version
     * of the integer api level.
     * @return a String.
     */
    @NonNull
    String getApiString();
}
