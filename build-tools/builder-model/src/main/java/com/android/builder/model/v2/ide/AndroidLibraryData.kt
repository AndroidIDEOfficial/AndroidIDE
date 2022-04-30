/*
 * Copyright (C) 2021 The Android Open Source Project
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

import java.io.File

/**
 * Data for Android external Libraries
 */
interface AndroidLibraryData {

    /**
     * The location of the manifest file.
     */
    val manifest: File

    /**
     * The list of jar files for compilation.
     */
    val compileJarFiles: List<File>

    /**
     * The list of jar files for runtime/packaging.
     */
    val runtimeJarFiles: List<File>

    /**
     * The android resource folder.
     *
     * The folder may not exist.
     */
    val resFolder: File

    /**
     * The namespaced resources static library (res.apk).
     */
    val resStaticLibrary: File

    /**
     * The assets folder.
     *
     * The folder may not exist.
     */
    val assetsFolder: File

    /**
     * The jni libraries folder.
     *
     * The folder may not exist.
     */
    val jniFolder: File

    /**
     * The AIDL import folder
     *
     * The folder may not exist.
     */
    val aidlFolder: File

    /**
     * The RenderScript import folder
     *
     * The folder may not exist.
     */
    val renderscriptFolder: File

    /**
     * The proguard file rule.
     *
     * The file may not exist.
     */
    val proguardRules: File

    /**
     * the zip file with external annotations
     *
     * The file may not exist.
     */
    val externalAnnotations: File

    /**
     * The file listing the public resources
     *
     * The file may not exist.
     */
    val publicResources: File

    /**
     * The symbol list file
     *
     * The file may not exist.
     */
    val symbolFile: File
}
