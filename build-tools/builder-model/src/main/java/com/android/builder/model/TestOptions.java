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

package com.android.builder.model;

/** Test options for running tests - e.g. instrumented or not. */
public interface TestOptions {
    enum Execution {
        /** On device orchestration is not used in this case. */
        HOST,
        /** On device orchestration is used. */
        ANDROID_TEST_ORCHESTRATOR,
        /** On device orchestration is used, with androidx class names. */
        ANDROIDX_TEST_ORCHESTRATOR,
    }

    boolean getAnimationsDisabled();

    Execution getExecution();
}
