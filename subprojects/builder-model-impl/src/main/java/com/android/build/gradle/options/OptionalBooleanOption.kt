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

import com.android.build.gradle.options.Version.VERSION_7_0
import com.android.build.gradle.options.Version.VERSION_8_0
import com.android.build.gradle.options.Version.VERSION_BEFORE_4_0
import com.android.builder.model.PROPERTY_SIGNING_V1_ENABLED
import com.android.builder.model.PROPERTY_SIGNING_V2_ENABLED
import com.android.builder.model.PROPERTY_TEST_ONLY

enum class OptionalBooleanOption(
    override val propertyName: String,
    val stage: Stage,
    val recommendedValue: Boolean? = null
) : Option<Boolean> {
    SIGNING_V1_ENABLED(PROPERTY_SIGNING_V1_ENABLED, ApiStage.Stable),
    SIGNING_V2_ENABLED(PROPERTY_SIGNING_V2_ENABLED, ApiStage.Stable),
    IDE_TEST_ONLY(PROPERTY_TEST_ONLY, ApiStage.Stable),

    /**
     * This project property is read by the firebase plugin, and has no direct impact on AGP behavior.
     *
     * It is included as an OptionalBooleanOption in order that its value, if set, is recorded in the AGP analytics.
     */
    FIREBASE_PERF_PLUGIN_ENABLE_FLAG("firebasePerformanceInstrumentationEnabled", ApiStage.Stable),

    // Flags for Android Test Retention
    ENABLE_TEST_FAILURE_RETENTION_COMPRESS_SNAPSHOT("android.experimental.testOptions.emulatorSnapshots.compressSnapshots", ApiStage.Experimental),

    ENABLE_API_MODELING_AND_GLOBAL_SYNTHETICS("android.enableApiModelingAndGlobalSynthetics", ApiStage.Experimental),

    /* ----------------
    * SOFTLY ENFORCED FEATURES
    */

    /* ----------------
     * ENFORCED FEATURES
     */
    @Suppress("unused")
    ENABLE_R8(
        "android.enableR8",
        FeatureStage.Enforced(VERSION_7_0, "Please remove it from `gradle.properties`.")
    ),

    @Suppress("unused")
    DISABLE_AUTOMATIC_COMPONENT_CREATION(
        "android.disableAutomaticComponentCreation",
        FeatureStage.Enforced(VERSION_8_0, "Please remove it from `gradle.properties`.")
    ),

    /* ----------------
     * REMOVED FEATURES
     */

    @Suppress("unused")
    SERIAL_AAPT2(
        "android.injected.aapt2.serial",
        FeatureStage.Removed(
            VERSION_BEFORE_4_0,
            "Invoking AAPT2 serially is no longer supported."
        )
    ),

    ;

    override val status = stage.status

    override fun parse(value: Any): Boolean {
        return parseBoolean(propertyName, value)
    }
}
