/*
 * Copyright (C) 2020 The Android Open Source Project
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

package com.android.builder.model.v2.ide

import com.android.builder.model.v2.AndroidModel
import java.io.File

/**
 * Information for artifact that generates bundle
 *
 * @since 4.2
 */
interface BundleInfo: AndroidModel {

    /**
     * The name of the task used to generate the bundle file (.aab)
     */
    val bundleTaskName: String

    /**
     * The path to the listing file generated after each [bundleTaskName] task
     * execution. The listing file will contain a reference to the produced bundle file (.aab).
     */
    val bundleTaskOutputListingFile: File

    /**
     * The name of the task used to generate APKs via the bundle file (.aab)
     */
    val apkFromBundleTaskName: String

    /**
     * The path to the model file generated after each [apkFromBundleTaskName]
     * task execution. The model will contain a reference to the folder where APKs from bundle are
     * placed into.
     */
    val apkFromBundleTaskOutputListingFile: File
}