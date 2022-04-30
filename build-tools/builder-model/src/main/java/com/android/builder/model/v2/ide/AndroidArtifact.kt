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
 * An Android Artifact.
 *
 * This is the entry point for the output of a [Variant]. This can be more than one
 * output in the case of multi-apk where more than one APKs are generated from the same set
 * of sources.
 */
interface AndroidArtifact : AbstractArtifact, AndroidModel {

    /**
     * The min SDK version of this artifact
     */
    val minSdkVersion: ApiVersion

    /**
     * The target SDK version override. If null, there is no override and the value may
     * be coming from the manifest(s) if present there.
     * If not null, this is the final resolved value.
     */
    val targetSdkVersionOverride: ApiVersion?

    /**
     * The max SDK version of this artifact, or null if not set
     */
    val maxSdkVersion: Int?

    /**
     * Returns whether the output file is signed. This can only be true for the main apk of an
     * application project.
     *
     * @return true if the app is signed.
     */
    val isSigned: Boolean

    /**
     * Returns the name of the [SigningConfig] used for the signing. If none are setup or if
     * this is not the main artifact of an application project, then this is null.
     *
     * @return the name of the setup signing config.
     */
    val signingConfigName: String?

    /**
     * Returns the name of the task used to generate the source code. The actual value might
     * depend on the build system front end.
     *
     * @return the name of the code generating task.
     */
    val sourceGenTaskName: String

    /**
     * The name of the task used to generate the resources. The actual value might
     * depend on the build system front end.
     *
     * Maybe null if the artifact does not support Android resources
     */
    val resGenTaskName: String?

    /**
     * Returns all the resource folders that are generated. This is typically the renderscript
     * output and the merged resources.
     *
     * @return a list of folder.
     */
    val generatedResourceFolders: Collection<File>

    /**
     * Returns the ABI filters associated with the artifact, or null if there are no filters.
     *
     * If the list contains values, then the artifact only contains these ABIs and excludes
     * others.
     */
    val abiFilters: Set<String>?

    /**
     * Returns the absolute path for the listing file that will get updated after each build. The
     * model file will contain deployment related information like applicationId, list of APKs.
     *
     * This is null for variants that do not generate APKs (libraries).
     *
     * @return the path to a json file.
     */
    val assembleTaskOutputListingFile: File?

    /**
     * The test info, if applicable, otherwise null
     */
    val testInfo: TestInfo?

    /**
     * The bundle info if applicable, otherwise null.
     *
     * This is only applicable to the main artifact of the APPLICATION modules. All other cases
     * this should be null.
     */
    val bundleInfo: BundleInfo?

    /**
     * Returns the code shrinker used by this artifact or null if no shrinker is used to build this
     * artifact.
     */
    val codeShrinker: CodeShrinker?
}
