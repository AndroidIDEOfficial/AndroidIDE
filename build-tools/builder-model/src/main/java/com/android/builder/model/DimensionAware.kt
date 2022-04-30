/*
 * Copyright (C) 2015 The Android Open Source Project
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
package com.android.builder.model

/**
 * Interface for objects has a dimension.
 *
 * This is an interface for the gradle tooling api, and should only be used from Android Studio.
 * It is not part of the DSL & API interfaces of the Android Gradle Plugin.
 */
interface DimensionAware {
    /**
     * Specifies the flavor dimension that this product flavor belongs to.
     *
     *
     * When configuring product flavors with Android plugin 3.0.0 and higher, you must specify at
     * least one flavor dimension, using the `flavorDimensions` property, and then assign each
     * flavor to a dimension.
     * Otherwise, you will get the following build error:
     *
     * ```
     * Error:All flavors must now belong to a named flavor dimension.
     * The flavor 'flavor_name' is not assigned to a flavor dimension.
     * ```
     *
     *
     * By default, when you specify only one dimension, all flavors you configure automatically
     * belong to that dimension. If you specify more than one dimension, you need to manually assign
     * each flavor to a dimension, as shown in the sample below:
     *
     * ```
     * android {
     *     ...
     *     // Specifies the flavor dimensions you want to use. The order in which you
     *     // list each dimension determines its priority, from highest to lowest,
     *     // when Gradle merges variant sources and configurations. You must assign
     *     // each product flavor you configure to one of the flavor dimensions.
     *     flavorDimensions 'api', 'version'
     *    
     *     productFlavors {
     *         demo {
     *             // Assigns this product flavor to the 'version' flavor dimension.
     *             dimension 'version'
     *             ...
     *         }
     *            
     *         full {
     *             dimension 'version'
     *             ...
     *         }
     *            
     *         minApi24 {
     *             // Assigns this flavor to the 'api' dimension.
     *             dimension 'api'
     *             minSdkVersion '24'
     *             versionNameSuffix "-minApi24"
     *             ...
     *         }
     *            
     *         minApi21 {
     *             dimension "api"
     *             minSdkVersion '21'
     *             versionNameSuffix "-minApi21"
     *             ...
     *         }
     *     }
     * }
     * ```
     *
     *
     * To learn more about configuring flavor dimensions, read
     * [Combine multiple flavors](https://developer.android.com/studio/build/build-variants.html#flavor-dimensions).
     */
    val dimension: String?
}
