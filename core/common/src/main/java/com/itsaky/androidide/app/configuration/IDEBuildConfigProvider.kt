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

package com.itsaky.androidide.app.configuration

import android.os.Build
import com.itsaky.androidide.utils.ServiceLoader

/**
 * [IDEBuildConfigProvider] provides information about the build variant of the IDE itself.
 *
 * @author Akash Yadav
 */
interface IDEBuildConfigProvider {

  /**
   * The current product flavor of the IDE.
   */
  val cpuAbiName: String

  /**
   * Get the [CpuArch] for the build flavor.
   */
  val cpuArch: CpuArch

  /**
   * Get the primary CPU architecture of the device.
   */
  val deviceArch: CpuArch

  /**
   * The supported CPU architectures of the device.
   */
  val supportedAbis: Array<String>

  /**
   * @return Whether this build is the `arm64-v8a` variant of the IDE.
   */
  fun isArm64v8aBuild(): Boolean = cpuArch == CpuArch.AARCH64

  /**
   * @return Whether the device supports `arm64-v8a` instructions.
   */
  fun isArm64v8aDevice(): Boolean = deviceArch == CpuArch.AARCH64

  /**
   * @return Whether this build is the 32-bit variant of the IDE.
   */
  fun isArmeabiv7aBuild(): Boolean = cpuArch == CpuArch.ARM

  /**
   * @return Whether the device supports `armeabi-v7a` instructions.
   */
  fun isArmeabiv7aDevice(): Boolean = deviceArch == CpuArch.ARM

  /**
   * @return Whether this build is the `x86_64` variant of the IDE.
   */
  fun isX86_64Build(): Boolean = cpuArch == CpuArch.X86_64

  /**
   * @return Whether the device supports `x86_64` instructions.
   */
  fun isX86_64Device(): Boolean = deviceArch == CpuArch.X86_64

  /**
   * @return Whether the IDE can be run on the device's CPU arch.
   */
  fun supportsCpuAbi(): Boolean = Build.SUPPORTED_ABIS.contains(cpuAbiName)

  companion object {

    private val _instance by lazy {
      ServiceLoader.load(IDEBuildConfigProvider::class.java).findFirstOrThrow()
    }

    /**
     * Get instance of the [IDEBuildConfigProvider] implementation.
     */
    @JvmStatic
    fun getInstance(): IDEBuildConfigProvider {
      return _instance
    }
  }
}