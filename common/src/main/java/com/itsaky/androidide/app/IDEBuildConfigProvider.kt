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

package com.itsaky.androidide.app

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
  val buildFlavor: String

  /**
   * The build flavors which can be installed on the current device.
   */
  val supportedBuildFlavors: Array<String>

  /**
   * The supported CPU architectures of the device.
   */
  val supportedAbis: Array<String>

  /**
   * @return Whether this build is the 64-bit variant of the IDE.
   */
  fun isArm64v8aBuild(): Boolean

  /**
   * @return Whether the device supports `arm64-v8a` instructions.
   */
  fun isArm64v8aDevice() : Boolean

  /**
   * @return Whether this build is the 32-bit variant of the IDE.
   */
  fun isArmeabiv7aBuild(): Boolean

  /**
   * @return Whether the device supports `armeabi-v7a` instructions.
   */
  fun isArmeabiv7aDevice() : Boolean

  /**
   * @return Whether the IDE can be run on the device's CPU arch.
   */
  fun supportsBuildFlavor() : Boolean

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