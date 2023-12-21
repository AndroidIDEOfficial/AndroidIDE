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

package com.itsaky.androidide.fragments.onboarding

import com.itsaky.androidide.app.configuration.CpuArch

private val ARM_ONLY = arrayOf(CpuArch.AARCH64, CpuArch.ARM)
private val ALL = arrayOf(CpuArch.AARCH64, CpuArch.ARM, CpuArch.X86_64)

/**
 * Android SDK versions.
 *
 * @author Akash Yadav
 */
enum class SdkVersion(val version: String, val supportedArchs: Array<CpuArch>) {

  SDK_33_0_1("33.0.1", ARM_ONLY),
  SDK_34_0_0("34.0.0", ARM_ONLY),
  SDK_34_0_1("34.0.1", ARM_ONLY),
  SDK_34_0_3("34.0.3", ARM_ONLY),
  SDK_34_0_4("34.0.4", ALL),
  ;

  val displayName = "SDK $version"

  companion object {

    @JvmStatic
    fun fromDisplayName(displayName: CharSequence) =
      entries.first { it.displayName.contentEquals(displayName) }

    @JvmStatic
    fun fromVersion(version: CharSequence) = entries.first { it.version.contentEquals(version) }
  }
}

/**
 * JDK versions.
 *
 * @author Akash Yadav
 */
enum class JdkVersion(val version: String) {

  JDK_17("17"),
  JDK_21("21"),
  ;

  val displayName = "JDK $version"

  companion object {

    @JvmStatic
    fun fromDisplayName(displayName: CharSequence) =
      entries.first { it.displayName.contentEquals(displayName) }

    @JvmStatic
    fun fromVersion(version: CharSequence) = entries.first { it.version.contentEquals(version) }
  }
}