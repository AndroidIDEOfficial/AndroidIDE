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

/**
 * Enum for supported CPU architectures.
 *
 * @author Akash Yadav
 */
enum class CpuArch(val abi: String) {

  /**
   * `arm64-v8a` (64-bit).
   */
  AARCH64("arm64-v8a"),

  /**
   * `armeabi-v7a` flavor (32-bit).
   */
  ARM("armeabi-v7a"),

  /**
   * `x86` (32-bit).
   */
  X86("x86"),

  /**
   * `x86_64` (64-bit).
   */
  X86_64("x86_64");

  companion object {

    /**
     * Get the [CpuArch] for the given ABI.
     */
    @JvmStatic
    fun forAbi(abi: String): CpuArch? {
      return entries.firstOrNull { it.abi == abi }
    }
  }
}