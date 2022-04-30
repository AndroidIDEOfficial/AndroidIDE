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
 * Information for test Artifacts.
 *
 * This includes both Android test components in app/lib modules, and the main component
 * in test modules.
 *
 * @since 4.2
 */
interface TestInfo: AndroidModel {

    enum class Execution {
        /** On device orchestration is not used in this case.  */
        HOST,

        /** On device orchestration is used.  */
        ANDROID_TEST_ORCHESTRATOR,

        /** On device orchestration is used, with androidx class names.  */
        ANDROIDX_TEST_ORCHESTRATOR
    }

    val animationsDisabled: Boolean
    val execution: Execution?

    /**
     * Returns a list of additional APKs that need to installed on the device for this artifact to
     * work correctly.
     *
     *
     * For test artifacts, these will be "buddy APKs" from the `androidTestUtil`
     * configuration.
     */
    val additionalRuntimeApks: Collection<File>

    /**
     * Returns the name of the task used to run instrumented tests or null if the variant is not a
     * test variant.
     *
     * @return name of the task used to run instrumented tests
     */
    val instrumentedTestTaskName: String
}