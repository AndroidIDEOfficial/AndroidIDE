/*
 * Copyright (C) 2013 The Android Open Source Project
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
package com.android.builder.model.v2.dsl

import com.android.builder.model.v2.AndroidModel
import java.io.File

/**
 * A Signing Configuration.
 *
 * This is an interface for the gradle tooling api, and should only be used from Android Studio.
 * It is not part of the DSL & API interfaces of the Android Gradle Plugin.
 *
 * @since 4.2
 */
interface SigningConfig: AndroidModel {
    /** Returns the name of the Signing config */
    val name: String

    /** The keystore file. */
    val storeFile: File?

    /** The keystore password. */
    val storePassword: String?

    /** The key alias name. */
    val keyAlias: String?

    /** The key password. */
    val keyPassword: String?

    /** Signing using JAR Signature Scheme (aka v1 scheme) is enabled. */
    val enableV1Signing: Boolean?

    /** Signing using APK Signature Scheme v2 (aka v2 scheme) is enabled. */
    val enableV2Signing: Boolean?

    /** Signing using JAR Signature Scheme v3 (aka v3 scheme) is enabled. */
    val enableV3Signing: Boolean?

    /** Signing using JAR Signature Scheme v4 (aka v4 scheme) is enabled. */
    val enableV4Signing: Boolean?

    /**
     * Whether the config is fully configured for signing.
     *
     * i.e. all the required information are present.
     */
    val isSigningReady: Boolean
}