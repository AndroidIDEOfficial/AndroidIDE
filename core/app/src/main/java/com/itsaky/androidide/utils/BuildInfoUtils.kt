/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.utils

import android.content.Context
import android.os.Build
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.itsaky.androidide.BuildConfig
import com.itsaky.androidide.app.IDEApplication
import com.itsaky.androidide.app.configuration.IDEBuildConfigProvider
import com.itsaky.androidide.buildinfo.BuildInfo
import com.termux.shared.android.PackageUtils
import com.termux.shared.termux.TermuxConstants
import com.termux.shared.termux.TermuxUtils

/**
 * @author Akash Yadav
 */
object BuildInfoUtils {

  private val BUILD_INFO_HEADER by lazy {
    val map = mapOf(
      "Version" to "v${BuildInfo.VERSION_NAME_SIMPLE} (${AppUtils.getAppVersionCode()})",
      "CI Build" to BuildInfo.CI_BUILD,
      "Branch" to BuildInfo.CI_GIT_BRANCH,
      "Commit" to BuildInfo.CI_GIT_COMMIT_HASH,
      "Variant" to "${IDEBuildConfigProvider.getInstance().cpuAbiName} (${BuildConfig.BUILD_TYPE})",
      "Build type" to getBuildType(),
      "F-Droid Build" to BuildInfo.FDROID_BUILD,
      "F-Droid Version" to BuildInfo.FDROID_BUILD_VERSION_NAME,
      "F-Droid Version code" to BuildInfo.FDROID_BUILD_VERSION_CODE,
      "SDK Version" to Build.VERSION.SDK_INT,
      "Supported ABIs" to "[${Build.SUPPORTED_ABIS.joinToString(separator = ", ")}]",
      "Manufacturer" to DeviceUtils.getManufacturer(),
      "Device" to DeviceUtils.getModel(),
    )
    map.entries.joinToString(separator = System.lineSeparator()) { "${it.key} : ${it.value}" }.trim()
  }

  @JvmStatic
  fun getBuildInfoHeader(): String {
    return BUILD_INFO_HEADER
  }

  private fun getBuildType() = getBuildType(IDEApplication.instance)

  fun getBuildType(context: Context) = if (isOfficialBuild(context)) "OFFICIAL" else "UNOFFICIAL"

  /**
   * Whether the AndroidIDE build is official or not. This checks the signature digest of the
   * APK against the signature digest of the official signing key.
   */
  @JvmStatic
  fun isOfficialBuild(context: Context): Boolean {
    val sha256DigestForPackage = PackageUtils.getSigningCertificateSHA256DigestForPackage(
      context,
      TermuxConstants.TERMUX_PACKAGE_NAME
    )

    val signer = TermuxUtils.getAPKRelease(sha256DigestForPackage)

    return TermuxConstants.APK_RELEASE_ANDROIDIDE == signer
        || TermuxConstants.APK_RELEASE_FDROID == signer
  }
}
