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

package com.itsaky.androidide.levelhash.util

import com.itsaky.androidide.jna.Falloc
import com.itsaky.androidide.jna.LibC
import com.itsaky.androidide.utils.FDUtils
import com.sun.jna.Native
import java.io.RandomAccessFile

/**
 * @author Akash Yadav
 */
internal object FileUtils {

  /**
   * Deallocate the given region of the file. Uses `fallocate(2)` with
   * [Falloc.FALLOC_FL_PUNCH_HOLE].
   */
  fun deallocate(file: RandomAccessFile, offset: Long, len: Long) {
    val fd = FDUtils.getFd(file.fd)
    val result = LibC.INSTANCE.fallocate(fd,
      Falloc.FALLOC_FL_PUNCH_HOLE or Falloc.FALLOC_FL_KEEP_SIZE, offset, len)
    if (result != 0) {
      throw RuntimeException("fallocate(2) failed: ${Native.getLastError()}")
    }
  }
}