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

import java.io.Serializable

/** Parameter sent by Android Studio when querying Gradle. */
interface NativeModelBuilderParameter : Serializable {

  /**
   * Variants for which the build information should be generated. Unrecognized variants are
   * ignored.
   *
   * If null, it means the build information should be generated for all variants.
   *
   * Build information includes the following:
   *
   * - compile_commands.json
   * - build_file_index.txt
   * - symbol_folder_index.txt
   */
  var variantsToGenerateBuildInformation: List<String>?

  /**
   * ABIs for which the build information should be generated.
   *
   * If null, it means the build information should be generated for all ABIs. Unrecognized ABIs are
   * ignored.
   *
   * Build information includes the following:
   *
   * - compile_commands.json
   * - build_file_index.txt
   * - symbol_folder_index.txt
   */
  var abisToGenerateBuildInformation: List<String>?
}
