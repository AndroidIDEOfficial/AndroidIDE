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

package com.itsaky.androidide.plugins.util

import java.io.File
import java.io.InputStream

/**
 * Utility methods for working with ELF binaries.
 *
 * @author Akash Yadav
 * @see <a href="https://en.wikipedia.org/wiki/Executable_and_Linkable_Format">Executable and Linkable Format</a>
 */
object ELFUtils {

  /**
   * The magic number of an ELF binary.
   */
  const val ELF_MAGIC_NUMBER: Int = 0x7F454C46

  /**
   * 4-byte magic number of an ELF binary.
   */
  val ELF_MAGIC_NUMBER_ARR = byteArrayOf(0x7F, 0x45, 0x4C, 0x46)

  /**
   * `EI_OSABI` of an ELF binary. This enumeration only contains ABIs that are supported by AndroidIDE.
   *
   * @property abiName The name of CPU ABI.
   * @property abi The number representing the ABI (`e_machine`).
   */
  enum class ElfAbi(val abiName: String, val abi: Byte) {
    ARMV7("armeabi-v7a", 0x28),
    AARCH64("arm64-v8a", 0xB7.toByte()),
    X86_64("x86_64", 0x3E);

    companion object {
      fun forName(name: String) : ElfAbi? = ElfAbi.values().firstOrNull { it.abiName == name }
      fun forAbi(abi: Byte) : ElfAbi? = ElfAbi.values().firstOrNull { it.abi == abi }
    }
  }

  /**
   * Check whether the data in given input stream corresponds to an ELF binary.
   * This checks if the magic number (first 4 bytes) in the byte array is `0x7F454C46` i.e. `0x7F E L F`.
   *
   * @return `true` if the file is an ELF binary, `false` otherwise.
   */
  fun isElf(bytes: ByteArray) : Boolean {
    if (bytes.size < 4) {
      return false
    }

    for (i in 0..3) {
      if (bytes[i] != ELF_MAGIC_NUMBER_ARR[i]) {
        return false
      }
    }

    return true
  }

  /**
   * Check whether the data in given input stream corresponds to an ELF binary.
   * This basically checks if the magic number in the input stream is `0x7F454C46` i.e. `0x7F E L F`.
   *
   * @return `true` if the file is an ELF binary, `false` otherwise.
   */
  fun isElf(inputStream: InputStream) : Boolean {
    val bytes = ByteArray(4)
    val read = inputStream.read(bytes)
    if (read != 4) {
      return false
    }
    return isElf(inputStream.readNBytes(4))
  }

  /**
   * Check whether the given file is an ELF binary.
   *
   * @return `true` if the file is an ELF binary, `false` otherwise.
   */
  fun isElf(file: File): Boolean {
    return file.inputStream().use { isElf(it) }
  }

  /**
   * Get the [ElfAbi] for the given file.
   *
   * @return The [ElfAbi] for the file, or `null` if the file is not an ELF binary.
   */
  fun getElfAbi(file: File) : ElfAbi? {
    return file.inputStream().use { getElfAbi(it) }
  }

  /**
   * Get the [ElfAbi] from the given input stream.
   *
   * @return The [ElfAbi] for the input stream, or `null` if the input stream does not represent an ELF binary.
   */
  fun getElfAbi(inputStream: InputStream) : ElfAbi? {
    val header = ByteArray(20)
    if (inputStream.read(header) < 20) {
      // incomplete data
      return null
    }

    if (!isElf(header)) {
      // not an elf binary
      return null
    }
    
    return ElfAbi.forAbi(header[18])
  }
}