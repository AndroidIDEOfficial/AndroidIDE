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

@file:JvmName("InjectedProperties")
package com.android.builder.model

// ------------------
// PROPERTIES FOR SYNC INVOCATIONS
// ------------------

// Injectable properties to use with -P
// Sent by Studio 4.2+
const val PROPERTY_BUILD_MODEL_V2_ONLY = "android.injected.build.model.v2"

// Sent in when external native projects models requires a refresh.
const val PROPERTY_REFRESH_EXTERNAL_NATIVE_MODEL =
    "android.injected.refresh.external.native.model"

// ------------------
// PROPERTIES FOR BUILD INVOCATIONS FROM IDE
// ------------------

// Has the effect of telling the Gradle plugin to
//   1) Generate machine-readable errors
//   2) Generate build metadata JSON files
const val PROPERTY_INVOKED_FROM_IDE = "android.injected.invoked.from.ide"

// Sent by Studio 2.2+
// This property is sent when a run or debug is invoked.  APK built with this property should
// be marked with android:testOnly="true" in the AndroidManifest.xml such that it will be
// rejected by the Play store.
const val PROPERTY_TEST_ONLY = "android.injected.testOnly"

/** Absolute path to the Build Analyzer data file */
const val PROPERTY_ATTRIBUTION_FILE_LOCATION = "android.injected.attribution.file.location"

/** Absolute path to a file containing the result of the `CheckJetifier` task.  */
const val PROPERTY_CHECK_JETIFIER_RESULT_FILE = "android.injected.checkJetifier.resultFile"

/** Logging level of native build output. Possible values are "quiet" and "verbose".  */
const val PROPERTY_NATIVE_BUILD_OUTPUT_LEVEL = "android.native.buildOutput"

const val PROPERTY_BUILD_WITH_STABLE_IDS = "android.injected.enableStableIds"

// ------------------
// PROPERTIES FOR OPTIMIZED BUILDS
// ------------------
// Sent by Studio 1.5+
// The version api level of the target device.
const val PROPERTY_BUILD_API = "android.injected.build.api"

// The version codename of the target device. Null for released versions,
const val PROPERTY_BUILD_API_CODENAME = "android.injected.build.codename"
const val PROPERTY_BUILD_ABI = "android.injected.build.abi"
const val PROPERTY_BUILD_DENSITY = "android.injected.build.density"

// ------------------
// PROPERTIES FOR RELEASE BUILDS FROM IDE
// ------------------
const val PROPERTY_SIGNING_STORE_FILE = "android.injected.signing.store.file"
const val PROPERTY_SIGNING_STORE_PASSWORD =
    "android.injected.signing.store.password"
const val PROPERTY_SIGNING_KEY_ALIAS = "android.injected.signing.key.alias"
const val PROPERTY_SIGNING_KEY_PASSWORD = "android.injected.signing.key.password"
const val PROPERTY_SIGNING_STORE_TYPE = "android.injected.signing.store.type"
const val PROPERTY_SIGNING_V1_ENABLED = "android.injected.signing.v1-enabled"
const val PROPERTY_SIGNING_V2_ENABLED = "android.injected.signing.v2-enabled"
/**
 * Location for APKs. If defined as a relative path, then it is resolved against the
 * project's path.
 */
const val PROPERTY_APK_LOCATION = "android.injected.apk.location"

// ------------------
// PROPERTIES FOR DEPLOYMENTS FROM THE IDE
// ------------------
const val PROPERTY_DEPLOY_AS_INSTANT_APP = "android.injected.deploy.instant-app"
const val PROPERTY_APK_SELECT_CONFIG = "android.inject.apkselect.config"
const val PROPERTY_EXTRACT_INSTANT_APK = "android.inject.bundle.extractinstant"

/**
 * Comma separated list of on-demand dynamic modules or instant app modules names that are
 * selected by the user for installation on the device during deployment.
 */
const val PROPERTY_INJECTED_DYNAMIC_MODULES_LIST = "android.injected.modules.install.list"
