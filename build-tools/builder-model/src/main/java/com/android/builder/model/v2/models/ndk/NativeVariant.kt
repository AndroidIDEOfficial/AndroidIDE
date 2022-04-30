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

package com.android.builder.model.v2.models.ndk

import com.android.builder.model.v2.AndroidModel

/**
 * Response returned by Gradle to Android Studio containing information about a variant in a native
 * module.
 */
interface NativeVariant: AndroidModel {
    /** Name of the variant. For example "debug", "release". */
    val name: String

    /** ABIs in this variant. */
    val abis: List<NativeAbi>
}