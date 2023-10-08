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

import android.os.Build
import com.google.auto.service.AutoService
import com.itsaky.androidide.BuildConfig

/**
 * @author Akash Yadav
 */
@AutoService(IDEBuildConfigProvider::class)
@Suppress("UNUSED")
open class IDEBuildConfigProviderImpl : IDEBuildConfigProvider {

  override val buildFlavor: String
    get() = BuildConfig.FLAVOR

  override val supportedBuildFlavors: Array<String>
    get() {
      val flavors = mutableListOf<String>()
      if (supportedAbis.contains(BuildConfig.FLAVOR_ARM64_V8A)) {
        flavors.add(BuildConfig.FLAVOR_ARM64_V8A)
      }
      if (supportedAbis.contains(BuildConfig.FLAVOR_ARMEABI_V7A)) {
        flavors.add(BuildConfig.FLAVOR_ARMEABI_V7A)
      }

      return flavors.toTypedArray()
    }

  override val supportedAbis: Array<String>
    get() = Build.SUPPORTED_ABIS

  override fun isArm64v8aBuild(): Boolean {
    return buildFlavor == BuildConfig.FLAVOR_ARM64_V8A
  }

  override fun isArm64v8aDevice(): Boolean {
    return Build.SUPPORTED_64_BIT_ABIS.contains(BuildConfig.FLAVOR_ARM64_V8A)
  }

  override fun isArmeabiv7aDevice(): Boolean {
    return Build.SUPPORTED_32_BIT_ABIS.contains(BuildConfig.FLAVOR_ARMEABI_V7A)
  }

  override fun isArmeabiv7aBuild(): Boolean {
    return buildFlavor == BuildConfig.FLAVOR_ARMEABI_V7A
  }

  override fun supportsBuildFlavor(): Boolean {
    return supportedBuildFlavors.contains(buildFlavor)
  }
}