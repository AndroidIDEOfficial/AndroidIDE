package com.android.aaptcompiler

object ToolFingerprint {
  const val TOOL_NAME = "Android Asset Packaging Tool (aapt)"

  // DO NOT UPDATE, this is more of a marketing version
  const val MAJOR_VERSION = 2

  /*
   * Represents the current version of Aapt2 that is being followed.
   *
   * Update minor version when features or flags are added to reflect the C++ value in
   * //frameworks/base/tools/aapt2/util/Util.cpp under the function GetToolFingerprint
   */
  const val MINOR_VERSION = 19

  // TODO(@daniellabar): Find the build id of aapt2 Kotlin
  val BUILD_NUMBER = "0"

  val FINGERPRINT = "$MAJOR_VERSION.$MINOR_VERSION-$BUILD_NUMBER"
}