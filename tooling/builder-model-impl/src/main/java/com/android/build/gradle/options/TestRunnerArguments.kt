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

package com.android.build.gradle.options

/**
 * A list of standard instrumentation test runner keys.
 *
 * Details: https://developer.android.com/studio/test/command-line#AMOptionsSyntax
 */
enum class TestRunnerArguments(
    private val key: String
) {
    @Suppress("unused")
    PACKAGE("package"),

    @Suppress("unused")
    CLASS("class"),

    @Suppress("unused")
    FUNC("func"),

    @Suppress("unused")
    SIZE("size"),

    @Suppress("unused")
    PERF("perf"),

    @Suppress("unused")
    DEBUG("debug"),

    @Suppress("unused")
    LOG("log"),

    @Suppress("unused")
    EMMA("emma"),

    @Suppress("unused")
    COVERAGE_FILE("coverageFile"),
    ;

    fun getFullKey() : String = "$TEST_RUNNER_ARGS_PREFIX$key"

    fun getShortKey() : String = key

    companion object {
        const val TEST_RUNNER_ARGS_PREFIX = "android.testInstrumentationRunnerArguments."
    }
}