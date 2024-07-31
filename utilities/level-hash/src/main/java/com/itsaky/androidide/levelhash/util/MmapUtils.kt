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

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.MappedByteBuffer

/**
 * Unmap the memory mapped buffer. This [KeymapSegment] becomes invalid after
 * this operation and must not be used further unless remapped.
 *
 * @return `true` if the unmapping request was successful, `false` otherwise.
 */
fun MappedByteBuffer.unmap(): Boolean {
  try {
    val cleanerF = javaClass.getDeclaredField("cleaner")
    cleanerF.isAccessible = true
    val cleaner = cleanerF.get(this)

    val cleanM = cleaner.javaClass.getDeclaredMethod("clean")
    cleanM.isAccessible = true

    cleanM.invoke(cleaner)
    return true
  } catch (err: Throwable) {
    LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)
      .error("Failed to unmap keymap segment")
    err.printStackTrace()
    return false
  }
}