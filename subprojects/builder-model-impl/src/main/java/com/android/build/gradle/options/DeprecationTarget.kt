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

package com.android.build.gradle.options
/** Enum for deprecated element removal target.  */
enum class DeprecationTarget constructor(

  /**
   * The target version when a deprecated element will be removed.
   *
   * Usage note: Do not use this field to construct a deprecation message, use
   * getDeprecationTargetMessage() instead to ensure consistent message format.
   */
  val removalTarget: Version,

  /**
   * Additional message to be shown below the pre-formatted error/warning message.
   *
   * Note that this additional message should be constructed such that it fits well in the
   * overall message:
   *
   *     "This feature will be removed in version X.Y of the Android Gradle plugin.\n
   *     $additionalMessage"
   *
   * For example, avoid writing additional messages that say "This feature is planned for
   * removal", as it will be duplicated.
   */
  private val additionalMessage: String? = null
) {
  VERSION_9_0(Version.VERSION_9_0),

  VERSION_8_3(Version.VERSION_8_3),

  VERSION_8_0(Version.VERSION_8_0),

  // Obsolete dx Dex Options
  DEX_OPTIONS(Version.VERSION_8_0, "Using it has no effect, and the Android" +
      "Gradle plugin optimizes dexing automatically."),

  // Deprecation of Task Access in the variant API
  TASK_ACCESS_VIA_VARIANT(Version.VERSION_9_0),

  USE_PROPERTIES(
    Version.VERSION_8_0,
    "Gradle Properties must be used to change Variant information."
  ),

  LINT_CHECK_ONLY(
    Version.VERSION_8_0,
    "`check` has been renamed to `checkOnly` to make it clear that it " +
        "will turn off all other checks than those explicitly listed. If that is " +
        "what you actually intended, use `checkOnly`; otherwise switch to `enable`."
  ),

  DEFAULT_PUBLISH_CONFIG(
    Version.VERSION_8_0,
    "The support for publishing artifacts with Maven Plugin is removed, " +
        "please migrate Maven Publish Plugin. See " +
        "https://developer.android.com/studio/build/maven-publish-plugin for more information."
  ),

  TRANSFORM_API(
    Version.VERSION_8_0,
    """
                The Transform API support has been removed in AGP 8.0.
            """.trimIndent()
  ),

  DENSITY_SPLIT_API(
    Version.VERSION_9_0,
    "Density-based apk split feature is deprecated and will be removed in AGP 9.0. " +
        "Use Android App Bundle (https://developer.android.com/guide/app-bundle) " +
        "to generate optimized APKs."
  ),

  BUILD_CONFIG_GLOBAL_PROPERTY(
    Version.VERSION_9_0,
    "You can resolve this warning in Android Studio via `Refactor` > `Migrate " +
        "BuildConfig to Gradle Build Files`"
  )
  ;

  fun getDeprecationTargetMessage(): String {
    return removalTarget.getDeprecationTargetMessage() +
        (additionalMessage?.let { "\n$it" } ?: "")
  }
}