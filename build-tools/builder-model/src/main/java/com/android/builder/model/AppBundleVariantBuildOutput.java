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

/** Model that represents the variant build output of a bundle file. */
public interface AppBundleVariantBuildOutput {

    /**
     * Variant full name.
     *
     * @return a {@link String} representing this variant's name.
     */
    @NonNull
    String getName();

    /**
     * Returns the bundle file output (.aab file).
     *
     * @return the output file for this variant.
     */
    @NonNull
    File getBundleFile();

    /**
     * Returns the location of the generated APK(s) from the bundle.
     *
     * <p>The location is always returned but it may not have been generated if the build request
     * only built the bundle.
     *
     * @return the folder containing the APKs.
     */
    @NonNull
    File getApkFolder();
}
