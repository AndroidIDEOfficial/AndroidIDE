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

package com.android.build.gradle.options

import com.android.build.gradle.options.Version.VERSION_4_0
import com.android.build.gradle.options.Version.VERSION_4_1
import com.android.build.gradle.options.Version.VERSION_7_0
import com.android.build.gradle.options.Version.VERSION_BEFORE_4_0

/**
 * [Option] that has been replaced by something else (e.g., another option or the DSL).
 *
 * Note that replaced options are different from removed options. Removed options belong to the
 * [Option] subclass where they were first introduced, and should not be put in this class.
 */
enum class ReplacedOption(
    override val propertyName: String,
    private val replacedVersion: Version,
    private val additionalMessage: String
) : Option<String> {

    @Suppress("unused")
    INCREMENTAL_JAVA_COMPILE(
        "android.incrementalJavaCompile",
        VERSION_BEFORE_4_0,
        "The android.incrementalJavaCompile property has been replaced by a DSL property. "
                + "Please add the following to your build.gradle instead:\n"
                + "android {\n"
                + "  compileOptions.incremental = false\n"
                + "}"
    ),

    @Suppress("unused")
    THREAD_POOL_SIZE_OLD(
        "com.android.build.threadPoolSize",
        VERSION_BEFORE_4_0,
        "This property has been replaced by ${IntegerOption.THREAD_POOL_SIZE.propertyName}"
    ),

    @Suppress("unused")
    VERSION_CHECK_OVERRIDE_PROPERTY_OLD(
        "com.android.build.gradle.overrideVersionCheck",
        VERSION_BEFORE_4_0,
        "This property has been replaced by ${BooleanOption.VERSION_CHECK_OVERRIDE_PROPERTY.propertyName}"
    ),

    @Suppress("unused")
    OVERRIDE_PATH_CHECK_PROPERTY_OLD(
        "com.android.build.gradle.overridePathCheck",
        VERSION_BEFORE_4_0,
        "This property has been replaced by ${BooleanOption.OVERRIDE_PATH_CHECK_PROPERTY.propertyName}"
    ),

    @Suppress("unused")
    AAPT_NAMESPACING(
        "android.aaptNamespacing",
        VERSION_BEFORE_4_0,
        "This property has been replaced by android.aaptOptions.namespaced (DSL)"
    ),

    @Suppress("unused")
    EXCLUDE_R_AND_MANIFEST_DOT_JAVA_FROM_GENERATED_SOURCES(
        "android.excludeRAndManifestDotJavaFromGeneratedSources",
        VERSION_BEFORE_4_0,
        "Subsumed by ${BooleanOption.ENABLE_SEPARATE_R_CLASS_COMPILATION.propertyName}"
    ),

    // This option should belong to the LongOption class, but that class has been removed so we put
    // it here.
    @Suppress("unused")
    DEPRECATED_NDK_COMPILE_LEASE(
        "android.deprecatedNdkCompileLease",
        VERSION_BEFORE_4_0,
        "NdkCompile is no longer supported"
    ),

    @Suppress("unused")
    USE_NON_FINAL_RES_IDS_IN_TESTS(
        "android.androidTest.nonFinalResIds",
        VERSION_BEFORE_4_0,
        "This property has been replaced by ${BooleanOption.USE_NON_FINAL_RES_IDS.propertyName}"
    ),

    @Suppress("unused")
    ENABLE_INCREMENTAL_DESUGARING_V2(
        "android.enableIncrementalDesugaringV2",
        VERSION_4_0,
        "This property has been replaced by ${BooleanOption.ENABLE_INCREMENTAL_DEXING_TASK_V2.propertyName}"
    ),

    @Suppress("unused")
    ENABLE_INCREMENTAL_DEXING_V2(
        "android.enableIncrementalDexingV2",
        VERSION_4_1,
        "This property has been replaced by ${BooleanOption.ENABLE_INCREMENTAL_DEXING_TASK_V2.propertyName}"
    ),

    @Suppress("unused")
    NAMESPACED_R_CLASS(
        "android.namespacedRClass",
        VERSION_4_1,
        "This property has been replaced by ${BooleanOption.NON_TRANSITIVE_R_CLASS.propertyName}"
    ),

    @Suppress("unused")
    ENABLE_PREFAB(
        "android.enablePrefab",
        VERSION_4_1,
        "This property has been replaced by android.buildFeatures.prefab (DSL)"
    ),

    // The flag was renamed.
    @Suppress("unused")
    ENABLE_STABLE_IDS(
        "android.experimental.enableStableIds",
        VERSION_4_1,
        "This property has been replaced by ${BooleanOption.ENABLE_STABLE_IDS.propertyName}"
    ),

    @Suppress("unused")
    TEST_FAILURE_RETENTION_0(
        "android.experimental.testOptions.emulatorSnapshots",
        VERSION_7_0,
        "This property has been replaced by ${IntegerOption.TEST_FAILURE_RETENTION.propertyName}"
    ),

    @Suppress("unused")
    TEST_FAILURE_RETENTION_1(
        "android.testOptions.failureRetention",
        VERSION_7_0,
        "This property has been replaced by ${IntegerOption.TEST_FAILURE_RETENTION.propertyName}"
    ),

    @Suppress("unused")
    ENABLE_TEST_FAILURE_RETENTION_COMPRESS_SNAPSHOT(
        "android.testOptions.failureRetention.compressSnapshots",
        VERSION_7_0,
        "This property has been replaced by ${OptionalBooleanOption.ENABLE_TEST_FAILURE_RETENTION_COMPRESS_SNAPSHOT.propertyName}"
    ),

    // Jetifier: List of regular expressions for libraries that should not be jetified
    @Suppress("unused", "WrongTerminology")
    JETIFIER_BLACKLIST(
        "android.jetifier.blacklist",
        VERSION_7_0,
        "This property has been replaced by ${StringOption.JETIFIER_IGNORE_LIST.propertyName}"
    )

    ;

    override val status: Option.Status
        get() = Option.Status.Removed(replacedVersion, additionalMessage)

    override fun parse(value: Any): String {
        return value.toString()
    }
}
