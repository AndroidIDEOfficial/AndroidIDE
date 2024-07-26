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

package com.itsaky.androidide.jna

import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.testing.common.LinuxOnlyTestRule
import com.itsaky.androidide.utils.FDUtils
import com.sun.jna.Native
import com.sun.jna.platform.linux.ErrNo
import com.sun.jna.platform.linux.Fcntl
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets

/**
 * @author Akash Yadav
 */
@RunWith(JUnit4::class)
class LibCTest {

  @JvmField
  @Rule
  val linuxOnlyTestRule = LinuxOnlyTestRule()

  private fun createFile(name: String) : Pair<File, RandomAccessFile> {
    val file = File("build/${name}.tmp")
    val raFile = RandomAccessFile(file, "rw")

    if (file.exists()) {
      file.delete()
    }

    file.createNewFile()

    return file to raFile
  }

  @Test
  fun `test fallocate for sparse files`() {
    val data = "random-access-file".toByteArray(StandardCharsets.UTF_8)
    val (file, raFile) = createFile("fallocate-normal")
    try {
      val len = 1024L * 1024L

      // set length to 1MB, effectively equals to a ftruncate call
      raFile.setLength(len)

      // seek to half of file length, and write some data
      raFile.seek(len / 2)
      raFile.write(data)

      // ensure that the fallocate call works
      val fileDescriptor = FDUtils.getFd(raFile.fd)
      val result = LibC.INSTANCE.fallocate(fileDescriptor,
        Falloc.FALLOC_FL_PUNCH_HOLE or Falloc.FALLOC_FL_KEEP_SIZE, len / 2,
        data.size.toLong())

      assertThat(result).isEqualTo(0)
    } finally {
      file.delete()
    }
  }

  @Test
  fun `test fallocate for bad fd`() {
    val (file, raFile) = createFile("fallocate-normal")
    try {
      raFile.setLength(1024)

      // get the file descriptor and then close the file
      // this should make the file descriptor invalid
      val fileDescriptor = FDUtils.getFd(raFile.fd)
      raFile.close()

      val result = LibC.INSTANCE.fallocate(fileDescriptor,
        Falloc.FALLOC_FL_PUNCH_HOLE or Falloc.FALLOC_FL_KEEP_SIZE, 0, 1024)

      assertThat(result).isEqualTo(-1)
      assertThat(Native.getLastError()).isEqualTo(ErrNo.EBADF)
    } finally {
      file.delete()
    }
  }
}