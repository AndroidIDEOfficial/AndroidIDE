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

  override val flavorArch: CpuArch
    get() = CpuArch.forAbi(buildFlavor)!!

  override val deviceArch: CpuArch
    get() = CpuArch.forAbi(supportedAbis[0])!!

  override val supportedAbis: Array<String>
    get() = Build.SUPPORTED_ABIS

  override val supportedBuildFlavors: Array<String> by lazy {
    val flavors = mutableListOf<String>()
    if (supportedAbis.contains(BuildConfig.FLAVOR_ARM64_V8A)) {
      flavors.add(BuildConfig.FLAVOR_ARM64_V8A)
    }
    if (supportedAbis.contains(BuildConfig.FLAVOR_X86_64)) {
      flavors.add(BuildConfig.FLAVOR_X86_64)
    }
    if (supportedAbis.contains(BuildConfig.FLAVOR_ARMEABI_V7A)) {
      flavors.add(BuildConfig.FLAVOR_ARMEABI_V7A)
    }
    return@lazy flavors.toTypedArray()
  }

  companion object {
    private fun Array<String>?.checkIsPrimaryArch(arch: String): Boolean {
      return this?.isNotEmpty() == true && this[0] == arch
    }
  }
}