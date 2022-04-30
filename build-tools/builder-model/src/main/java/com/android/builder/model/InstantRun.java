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
import java.io.File;

/**
 * Model for InstantRun related information.
 */
public interface InstantRun {

    /** Instant Run is supported. */
    int STATUS_SUPPORTED = 0;

    /** Instant Run is not supported for non-debug build variants. */
    int STATUS_NOT_SUPPORTED_FOR_NON_DEBUG_VARIANT = 1;

    /** Instant Run is not supported because the variant is used for testing. */
    int STATUS_NOT_SUPPORTED_VARIANT_USED_FOR_TESTING = 2;

    /** Instant Run is not supported when Jack is used. */
    int STATUS_NOT_SUPPORTED_FOR_JACK = 3;

    /** Instant Run currently does not support projects with external native build system. */
    int STATUS_NOT_SUPPORTED_FOR_EXTERNAL_NATIVE_BUILD = 4;

    /** Instant Run is currently disabled for the experimental plugin. */
    int STATUS_NOT_SUPPORTED_FOR_EXPERIMENTAL_PLUGIN = 5;

    /** Instant Run is currently disabled for multi-apk applications (http://b/77685496) */
    int STATUS_NOT_SUPPORTED_FOR_MULTI_APK = 6;

    /** Instant Run was removed */
    int STATUS_REMOVED = 7;

    /**
     * Returns the last incremental build information, including success or failure, verifier
     * reason for requesting a restart, etc...
     * @return a file location, possibly not existing.
     */
    @NonNull
    File getInfoFile();

    /**
     * Whether the owner artifact supports Instant Run. This may depend on the toolchain used.
     */
    boolean isSupportedByArtifact();

    /**
     * Returns a status code indicating whether Instant Run is supported and why.
     */
    int getSupportStatus();
}
