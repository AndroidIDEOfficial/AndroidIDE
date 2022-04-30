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

/**
 * Basic information about dependency components
 */
interface ComponentInfo {

    /**
     * The build type attribute of this component.
     *
     * Null if the component does not have Android variants
     */
    val buildType: String?

    /**
     * The product flavor attributes of this component, keyed by flavor dimension name.
     *
     * May be empty if the component does not have Android product flavors.
     */
    val productFlavors: Map<String, String>

    /**
     * The list of attributes associated with the component.
     *
     * Build types and product flavor attributes are handled explicitly in [buildType] and
     * [productFlavors], so they are not included here
     */
    val attributes: Map<String, String>

    /**
     * The list of capabilities associated with the component
     */
    val capabilities: List<String>

    /**
     * Indicates whether this component (library or module) is a test fixtures component (i.e. has
     * a test fixtures capability).
     */
    val isTestFixtures: Boolean
}
