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

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.BuildConfig
import org.junit.Test

/**
 * @author Akash Yadav
 */
class IDEBuildConfigProviderImplTest {

  @Test
  fun `test build config provider`() {
    object : IDEBuildConfigProviderImpl() {
      var flavor = fun () = BuildConfig.FLAVOR_ARM64_V8A
      var abis = fun () = arrayOf(BuildConfig.FLAVOR_ARM64_V8A, BuildConfig.FLAVOR_ARMEABI_V7A)

      override val buildFlavor: String
        get() = flavor()
      override val supportedAbis: Array<String>
        get() = abis()
    }.apply {

      // ======================= AARCH64 variant on an AARCH64 device ==================
      assertThat(buildFlavor).isEqualTo(BuildConfig.FLAVOR_ARM64_V8A)

      assertThat(supportedAbis.toList()).containsExactly(
        BuildConfig.FLAVOR_ARM64_V8A,
        BuildConfig.FLAVOR_ARMEABI_V7A
      )
      assertThat(supportedBuildFlavors.toList()).containsExactly(
        BuildConfig.FLAVOR_ARM64_V8A,
        BuildConfig.FLAVOR_ARMEABI_V7A
      )

      assertThat(isArm64v8aBuild()).isTrue()
      assertThat(isArmeabiv7aBuild()).isFalse()
      assertThat(supportsBuildFlavor()).isTrue()

      // ======================= ARM32 variant on an ARM32 device ==================
      flavor = fun () = BuildConfig.FLAVOR_ARMEABI_V7A
      abis = fun () = arrayOf(BuildConfig.FLAVOR_ARMEABI_V7A)

      assertThat(buildFlavor).isEqualTo(BuildConfig.FLAVOR_ARMEABI_V7A)

      assertThat(supportedAbis.toList()).containsExactly(
        BuildConfig.FLAVOR_ARMEABI_V7A
      )
      assertThat(supportedBuildFlavors.toList()).containsExactly(
        BuildConfig.FLAVOR_ARMEABI_V7A
      )

      assertThat(isArm64v8aBuild()).isFalse()
      assertThat(isArmeabiv7aBuild()).isTrue()
      assertThat(supportsBuildFlavor()).isTrue()

      // ======================= ARM32 variant on an AARCH64 device ==================
      flavor = fun () = BuildConfig.FLAVOR_ARMEABI_V7A
      abis = fun () = arrayOf(BuildConfig.FLAVOR_ARM64_V8A, BuildConfig.FLAVOR_ARMEABI_V7A)

      assertThat(buildFlavor).isEqualTo(BuildConfig.FLAVOR_ARMEABI_V7A)

      assertThat(supportedAbis.toList()).containsExactly(
        BuildConfig.FLAVOR_ARM64_V8A,
        BuildConfig.FLAVOR_ARMEABI_V7A
      )
      assertThat(supportedBuildFlavors.toList()).containsExactly(
        BuildConfig.FLAVOR_ARM64_V8A,
        BuildConfig.FLAVOR_ARMEABI_V7A
      )

      assertThat(isArm64v8aBuild()).isFalse()
      assertThat(isArmeabiv7aBuild()).isTrue()
      assertThat(supportsBuildFlavor()).isTrue()

      // ======================= AARCH64 variant on an ARM32 device ==================
      flavor = fun () = BuildConfig.FLAVOR_ARM64_V8A
      abis = fun () = arrayOf(BuildConfig.FLAVOR_ARMEABI_V7A)

      assertThat(buildFlavor).isEqualTo(BuildConfig.FLAVOR_ARM64_V8A)

      assertThat(supportedAbis.toList()).containsExactly(
        BuildConfig.FLAVOR_ARMEABI_V7A
      )
      assertThat(supportedBuildFlavors.toList()).containsExactly(
        BuildConfig.FLAVOR_ARMEABI_V7A
      )

      assertThat(isArm64v8aBuild()).isTrue()
      assertThat(isArmeabiv7aBuild()).isFalse()
      assertThat(supportsBuildFlavor()).isFalse()
    }
  }
}