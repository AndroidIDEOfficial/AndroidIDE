/*
 * Copyright (C) 2012 The Android Open Source Project
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

/**
 * Java compile options.
 *
 * @since 4.2
 */
interface JavaCompileOptions: AndroidModel {
    /**
     * @return the java compiler encoding setting.
     */
    val encoding: String

    /**
     * @return the level of compliance Java source code has.
     */
    val sourceCompatibility: String

    /**
     * @return the Java version to be able to run classes on.
     */
    val targetCompatibility: String

    /** @return true if core library desugaring is enabled, false otherwise.
     */
    val isCoreLibraryDesugaringEnabled: Boolean
}